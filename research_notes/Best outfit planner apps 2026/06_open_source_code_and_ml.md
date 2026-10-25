# Open-source code and ML behind outfit-planner features: an engineering code review (as of 25 Oct 2026)

Method: Whering, Acloset, Indyx, Stylebook, Cladwell, Alta and Doppl are closed-source, so this review covers (a) six open-source wardrobe apps and (b) the reference implementations of the ML techniques behind each feature. I shallow-cloned 11 repositories and read the code paths cited below. I did not run their code. Each repo is pinned to the commit I read: wardrowbe `0bbca3a` (24 Oct 2026), Libre-Closet `08f66dd` (11 Oct 2026), ai-closet `bff94bb` (9 Nov 2025), vestiq `0d61926` (26 Jul 2026), Cloa `a934168` (23 Aug 2026), shimmer-strip `2cab8da` (5 Jul 2026), rembg `c5b48b0` (21 Oct 2026), fashion-clip `0b4b4f9` (30 Jan 2025), fashion-compatibility `10a1717` (29 Dec 2025), outfit-transformer `eef4be5` (27 Dec 2025), CatVTON `7818397` (16 Dec 2025). Star counts come from the GitHub "wardrobe" topic page as fetched on 25 Oct 2026 ([GitHub topic: wardrobe](https://github.com/topics/wardrobe)) unless another source is cited.

## Q1. Background removal / clothing cut-out: SOTA, OSS implementations, licences, latency and cost

### Takeaway
The commercial-grade options today are on-device OS segmenters (Apple Vision foreground-instance masks on iOS, ML Kit Subject Segmentation on Android) for instant, private cut-outs, plus a server model from the BiRefNet family (MIT) for higher quality. The main licence traps are BRIA RMBG-2.0 (CC BY-NC; it is now rembg's *default* model) and the in-browser `@imgly/background-removal` (AGPL-3.0). Paid APIs cost roughly $0.02/image (Photoroom) to about $0.2/image (remove.bg, per third-party estimates).

### Cited Findings
**Reference library: rembg (MIT code, weights under their own licences)**
- rembg wraps about 20 ONNX "sessions": u2net, u2netp, u2net_human_seg, u2net_cloth_seg, silueta, isnet-general-use, isnet-anime, sam, birefnet-general/-lite/-portrait/-dis/-hrsod/-cod/-massive/-fp16, bria-rmbg, ben_custom, and a withoutbg cloud backend. — [rembg@c5b48b0 rembg/sessions/](https://github.com/danielgatis/rembg/tree/c5b48b0417a0254df7d461efa14573597f369a83/rembg/sessions)
- The README now says **bria-rmbg (RMBG-2.0) is "The default"**: about 1.02 GB, runs at 1024x1024, and is "released under a BRIA license that requires a paid agreement for commercial use … Model weights carry their own licenses, independent of rembg's MIT license." — [rembg README L533](https://github.com/danielgatis/rembg/blob/c5b48b0417a0254df7d461efa14573597f369a83/README.md#L533)
- `birefnet-general-fp16` is "Roughly 30% faster on CUDA with near-identical masks; it does not reduce VRAM, and on CPU it is usually slower than birefnet-general." — [rembg README L526](https://github.com/danielgatis/rembg/blob/c5b48b0417a0254df7d461efa14573597f369a83/README.md#L526)
- `remove()` post-processing options include classic alpha matting (foreground/background thresholds 240/10, erode 10), `post_process_mask`, `decontaminate` (removes background colour fringing on soft edges), and `vitmatte` refinement (default checkpoint `small-distinctions-646`, about 110 MB). — [rembg bg.py L260-L361](https://github.com/danielgatis/rembg/blob/c5b48b0417a0254df7d461efa14573597f369a83/rembg/bg.py#L260-L361), [README L410-L412](https://github.com/danielgatis/rembg/blob/c5b48b0417a0254df7d461efa14573597f369a83/README.md#L410-L412)
- Preprocessing is shared across sessions: LANCZOS resize to the model's input size, then ImageNet mean/std normalisation. — [rembg sessions/base.py L81-L98](https://github.com/danielgatis/rembg/blob/c5b48b0417a0254df7d461efa14573597f369a83/rembg/sessions/base.py#L81-L98)

**BiRefNet / RMBG-2.0**
- BiRefNet is MIT. Reported runtimes at 1024x1024: A100 86.8 ms FP32 / 69.4 ms FP16, RTX 4090 95.8 / 57.7 ms. A Jan 2025 note gives 17 FPS with 3.45 GB VRAM on a 4090 in FP16. ONNX is slower (SwinL about 165 ms on A100, about 90% slower than PyTorch). 2025 releases: BiRefNet_HR (2048x2048), HR-matting, and _dynamic (256 to 2304 px). — [ZhengPeng7/BiRefNet](https://github.com/ZhengPeng7/BiRefNet)
- RMBG-2.0 is "developed on the BiRefNet architecture" and trained on more than 15,000 manually labelled, licensed images. Licence is CC BY-NC 4.0; commercial use needs a BRIA agreement. — [briaai/RMBG-2.0](https://huggingface.co/briaai/RMBG-2.0)

**How the open-source apps do it (code read)**
- **wardrowbe (server, Python):** a `BackgroundRemovalProvider` ABC with two implementations. `RembgProvider` lazy-imports rembg and caches the session. `HttpProvider` POSTs a PNG to `{url}/api/remove-background` with an optional Bearer key and a 120 s timeout. The default model is pinned to `u2net`. rembg is an optional extra ("adds ~500MB for model weights"). — [wardrowbe services/background_removal.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/background_removal.py), [config.py L169-L170](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/config.py#L169-L170), [requirements-extras.txt](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/requirements-extras.txt)
- **Libre-Closet (in the browser):** runs `@imgly/background-removal` 1.7.0 in a Web Worker (`proxyToWorker: true`) with `device: 'gpu'` (WebGPU, degrading to WASM) and model `isnet_quint8`. Model files are self-hosted under `/bg-removal-models/` ("no runtime CDN required"). The photo is square-padded on an OffscreenCanvas first. The result is injected into a hidden `nobgPhoto` form field before submit. — [Libre-Closet public/js/background-removal.js](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/public/js/background-removal.js)
- Libre-Closet patches the library to disable WebGPU on iOS and force `numThreads = 1` there. — [patches/@imgly+background-removal+1.7.0.patch](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/patches/@imgly+background-removal+1.7.0.patch)
- Libre-Closet ships a manual **mask editor** with erase and restore brushes; restore samples the original pixels from an OffscreenCanvas. — [public/js/mask-editor.js](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/public/js/mask-editor.js)
- On the server, the cut-out is stored as `<name>-nobg` via sharp: WebP quality 100, fit inside 1080x1080. If no nobg file exists, `GET /file/nobg/:fileName` **302-redirects to the original image**. The JS comment says the server "handles generation lazily", but at this commit the controller does not generate anything. — [src/file/file-service.abstract.ts](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/src/file/file-service.abstract.ts), [src/file/controller/file.controller.ts L75-L93](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/src/file/controller/file.controller.ts#L75-L93)
- `@imgly/background-removal-js` is AGPL-3.0 ("free for use under the AGPL License"; contact IMG.LY for other licensing). — [imgly/background-removal-js](https://github.com/imgly/background-removal-js). Libre-Closet itself is AGPL-3.0. — [Libre-Closet LICENSE](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/LICENSE)
- **ai-closet (React Native → paid API):** calls fal.ai `fal-ai/birefnet/v2` with `model: "General Use (Light)"`, `operating_resolution: "1024x1024"`, `refine_foreground: true`, PNG output, and polls the queue status URL every 200 ms. — [ai-closet src/services/BackgroundRemoval.ts](https://github.com/zebangeth/ai-closet/blob/bff94bb050add8d579dbe76bd0259b8fb50b23ca/src/services/BackgroundRemoval.ts)
- **vestiq (Android, on-device):** ML Kit Subject Segmentation (`play-services-mlkit-subject-segmentation` 16.0.0-beta1) with `SubjectSegmenterOptions.Builder().enableForegroundBitmap()`. It waits on `segmenter.initTask` (model download), then `process(InputImage.fromBitmap(...))`, and keeps PNG when the result has transparency. — [vestiq PhotoCropActivity.kt L449-L470](https://github.com/kamil-kucharski/vestiq/blob/0d61926a773e037c994e8e293d3cf6f38e333f5f/app/src/main/java/com/example/szafa/ui/PhotoCropActivity.kt#L449-L470), [gradle/libs.versions.toml](https://github.com/kamil-kucharski/vestiq/blob/0d61926a773e037c994e8e293d3cf6f38e333f5f/gradle/libs.versions.toml). The README notes the component "may need an internet connection on first download". — [vestiq README](https://github.com/kamil-kucharski/vestiq)
- **Cloa (iOS, SwiftUI):** a grep of all Swift sources finds no `import Vision` or VisionKit usage. Item intake uses a camera plus crop tool, and duplicates are caught with a 64-bit dHash (9x8 greyscale, Hamming distance ≤ 10 = duplicate). — [Cloa Core/Services/PerceptualHashService.swift](https://github.com/Aerdax/Cloa/blob/a934168b79ec7d1fe846775f2d8282e603ae7521/Cloa/Core/Services/PerceptualHashService.swift)

**On-device OS APIs**
- Android ML Kit Subject Segmentation is in **beta** (no SLA, may break compatibility) and documented for Android only. It runs fully on-device, averages **~200 ms on a Pixel 7 Pro**, and outputs a 0.0–1.0 confidence mask, a foreground bitmap, and optional per-subject masks. Static images only; touching subjects merge into one. — [ML Kit Subject Segmentation](https://developers.google.com/ml-kit/vision/subject-segmentation)
- Apple (WWDC23, "Lift subjects from images in your app"):
  - VisionKit `ImageAnalysisInteraction` gives system-style subject lifting. It runs "out-of-process … the image size is limited".
  - The Vision foreground-instance-mask request runs in-process and "isn't as limited in image resolution". It returns a soft mask at input resolution and an instance mask where "the zero index is reserved for the background".
  - It is class-agnostic and outputs SDR. A man and his dog together can become "one combined subject".
  - Apple recommends `CIBlendWithMask` for compositing.
  - [WWDC23 session 10176](https://developer.apple.com/videos/play/wwdc2023/10176/)
- System subject lifting requires an A12 Bionic or later device (per iMore, cited via search). Third-party apps such as Liftframe advertise building on iOS's built-in subject lift and require iOS 17+. — [iMore](https://imore.com/google-pixel-may-have-magic-eraser-ios-16-can-turn-everything-into-stickers), [Liftframe App Store](https://apps.apple.com/cd/app/liftframe/id6761348026)

**Paid APIs**
- Photoroom Remove Background API (Basic): **$0.02/image** ($20 covers 1,000), up to 6,000 px. The Image Editing API (Plus) is $0.10/image and includes ghost mannequin and virtual try-on. The sandbox gives 1,000 watermarked calls/month. — [Photoroom API pricing](https://www.photoroom.com/api/pricing)
- remove.bg: the official pricing page did not expose API rates when fetched. Third-party trackers disagree: about $0.18–$0.23/image on subscription tiers, and $0.20–$0.40 pay-as-you-go with previews at 0.25 credits. — [remove.bg pricing](https://www.remove.bg/pricing); [Tekpon](https://tekpon.com/software/removebg/pricing/); [Tooliverse](https://tooliverse.ai/tools/remove-bg); conflicting figures at [CostBench](https://costbench.com/software/ai-media-apis/remove-bg-api/) and [Toolradar](https://toolradar.com/tools/remove-bg/pricing)

**Commercial apps**
- Acloset (Looko Inc., Seoul) automatically removes backgrounds and tags category, colour and material. A 2022 press piece says its AI was trained on a dataset built from users' clothing uploads. No model details are public. — [screensdesign Acloset profile](https://screensdesign.com/showcase/acloset-ai-fashion-assistant); [2022 press release](https://business.minstercommunitypost.com/minstercommunitypost/article/getnews-2022-11-24-acloset-is-taking-hyper-personalization-to-the-fashion-industry-through-the-ai-powered-digital-closet-platform)

### Inferences
- **Production pattern:**
  1. Run the OS segmenter on-device for an instant preview (Apple Vision instance mask on iOS 17+, ML Kit on Android) and let the user pick an instance.
  2. Asynchronously refine on the server with BiRefNet-general or BiRefNet-HR (MIT) at 1024–2048 px, plus decontamination or matting for edges such as knitwear fuzz and lace.
  3. Always keep the original, as wardrowbe (`original_image_path`) and Libre-Closet (`-nobg` sibling file) both do.
  4. Offer an erase/restore brush like Libre-Closet's for the 5–10% of failures.
- If you `pip install rembg` and use the default session in a commercial product, you now get a CC BY-NC model. Pin `u2net`, `isnet-general-use` or `birefnet-*` explicitly, as wardrowbe does. U²-Net's and IS-Net's own licences were not verified here (see Gaps).
- Libre-Closet's in-browser approach costs nothing per image but inherits AGPL, which is unsuitable for a closed-source commercial client unless you buy a licence from IMG.LY.
- Class-agnostic segmenters (Apple, ML Kit, BiRefNet) will happily keep the hanger, the bed, or the person's arm when the garment is worn or held. That is why garment-specific parsing (Q2) matters for photos that are not flat-lays.
- For whole-pipeline unit economics: on-device costs $0. Self-hosted BiRefNet is about 58–96 ms of GPU time per image at 1024 px on A100/4090-class cards (FP16–FP32, from the BiRefNet table above), so GPU cost is negligible next to paid APIs at $0.02–$0.2 per image.

### Gaps
- I could not fetch the body of Apple's `VNGenerateForegroundInstanceMaskRequest` reference page, so exact OS minimums and on-device latency numbers are unverified. The WWDC23 transcript does not give them.
- No first-party remove.bg API price list was retrievable. Third-party figures conflict.
- U²-Net (xuebinqin/U-2-Net) and IS-Net/DIS licences were not checked. ML Kit Subject Segmentation model size and delivery mechanism (bundled vs Play services) are not stated on the docs page.
- No public evidence found on which background-removal stack Whering, Stylebook, Indyx or Cladwell use.

## Q2. Garment detection and segmentation from full-body photos and the camera roll

### Takeaway
Pulling individual garments out of an outfit photo is done with **human parsing** (SCHP on LIP/ATR, or SegFormer fine-tuned on ATR) or a dedicated **3-class cloth segmenter** (U²-Net cloth-seg). Diffusion try-on stacks pair these with DensePose/OpenPose. As of Nov 2025, SAM 3's text "concept prompts" (e.g. "denim jacket") are a strong open-vocabulary alternative under Meta's custom SAM licence. None of the six reviewed wardrobe apps does camera-roll auto-extraction; all assume one garment per photo.

### Cited Findings
- **u2net_cloth_seg (via rembg):** input resized to 768x768 with ImageNet normalisation, `argmax` over 4 output channels, and separate masks for upper, lower and full-body clothing (`cloth_category` = upper/lower/full, else all three). — [rembg sessions/u2net_cloth_seg.py L93-L143](https://github.com/danielgatis/rembg/blob/c5b48b0417a0254df7d461efa14573597f369a83/rembg/sessions/u2net_cloth_seg.py#L93-L143)
  - Source repo (levindabhi/cloth-segmentation): U²-Net with 4 output channels, trained on iMaterialist (Fashion) 2019 (~45k images, 42 labels grouped into 3), MIT, 768 px, 165 MB checkpoint, no accuracy metrics. **Archived 18 May 2026.** — [levindabhi/cloth-segmentation](https://github.com/levindabhi/cloth-segmentation)
- **SegFormer-B2 clothes:**
  - Fine-tuned on ATR with 18 classes: Background, Hat, Hair, Sunglasses, Upper-clothes, Skirt, Pants, Dress, Belt, Left/Right-shoe, Face, Left/Right-leg, Left/Right-arm, Bag, Scarf.
  - Mean IoU 0.69, mean accuracy 0.80.
  - Licence "other" (NVIDIA SegFormer licence).
  - 450,561 downloads last month.
  - [mattmdjaga/segformer_b2_clothes](https://huggingface.co/mattmdjaga/segformer_b2_clothes)
- **SCHP (Self-Correction Human Parsing):** MIT. LIP (20 labels) 59.36% mIoU; ATR (18 labels, "focuses on fashion") 82.29% mIoU; Pascal-Person-Part (7 labels) 71.46% mIoU. TPAMI 2020. — [GoGoDuck912/Self-Correction-Human-Parsing](https://github.com/GoGoDuck912/Self-Correction-Human-Parsing)
- **Parsing + pose combined in practice:** CatVTON's `AutoMasker` maps DensePose body parts plus SCHP ATR and LIP labels to build masks per garment type. For example, `upper` masks Upper-clothes/Coat/Dress/Jumpsuits plus torso/arms and protects legs/skirt/pants. Variants exist for `lower`, `overall`, `inner` and `outer`. — [CatVTON model/cloth_masker.py](https://github.com/Zheng-Chong/CatVTON/blob/7818397f25613beedb3d861a34769f607cfcf3b1/model/cloth_masker.py)
  - IDM-VTON requires `parsing_atr.onnx`, `parsing_lip.onnx`, DensePose and OpenPose checkpoints. — [yisol/IDM-VTON](https://github.com/yisol/IDM-VTON)
  - FASHN VTON v1.5 ships DWPose ONNX and downloads a ~244 MB human parser on first use. — [fashn-AI/fashn-vton-1.5](https://github.com/fashn-AI/fashn-vton-1.5)
- **DeepFashion2 (detection, instance masks, landmarks):** 491,895 images, ~801K items, 13 categories (short/long-sleeve top, short/long-sleeve outwear, vest, sling, shorts, trousers, skirt, short/long-sleeve dress, vest dress, sling dress), boxes, polygon masks, 294 landmarks. Download needs a form-issued password. The README states no licence. — [switchablenorms/DeepFashion2](https://github.com/switchablenorms/DeepFashion2)
- **SAM 3 (Meta, 19 Nov 2025):** given a noun phrase or image exemplar, returns masks and IDs for every matching instance. Released under the custom "SAM License"; SAM 3.1 has since been listed. — [Roboflow: What is SAM 3](https://blog.roboflow.com/what-is-sam3/), [ComfyUI wiki SAM 3.1](https://comfyui-wiki.com/en/models/sam/sam3.1)
- rembg also exposes `sam` (ViT-B encoder/decoder, quantised ONNX). — [rembg README L524](https://github.com/danielgatis/rembg/blob/c5b48b0417a0254df7d461efa14573597f369a83/README.md#L524)
- In the GitHub wardrobe topic, `longliuyu2022/AI-wardrobe` (4 stars) describes splitting "one full-body photo into separate garments". I did not review it. — [GitHub topic: wardrobe](https://github.com/topics/wardrobe)
- Alta ingests closet items "from your photo library, email receipts, camera capture". — [Menlo Ventures on Alta](https://menlovc.com/perspective/agentic-styling-and-shopping-why-were-backing-alta/)

### Inferences
- **Production "outfit photo → items" pipeline:**
  1. Person detection, then human parsing (SegFormer/SCHP at ATR granularity) or SAM 3 prompted per category.
  2. Per-garment mask → tight crop → edge refinement (BiRefNet/matting).
  3. Feed each crop to tagging (Q3).
  4. Dedupe against the existing closet with embeddings (FashionSigLIP) rather than pHash alone. Worn garments look different from flat-lays, so pHash and dHash dedupe as used by wardrowbe and Cloa will miss them.
- Camera-roll mining (as in Alta's "photo library") needs the same pipeline plus a person and outfit-of-the-day classifier and heavy dedupe. On iOS the cheap first pass would be Vision person or instance masks on-device, sending only candidate crops to a server.
- ATR/LIP-trained parsers are the de facto standard, but the training datasets' commercial terms are unclear (see Gaps). Teams shipping commercially typically retrain on licensed or own-labelled data, which is what BRIA did for background removal.

### Gaps
- Licence terms for the LIP, ATR and iMaterialist 2019 datasets, and whether models trained on them can be used commercially, were not verified.
- No primary source checked for YOLO-based fashion detectors (e.g. DeepFashion2-trained YOLO weights) or their licences (Ultralytics models are typically AGPL, but this was not verified here).
- SAM 3 latency and the exact SAM License commercial terms were not retrieved from Meta's repository.

## Q3. Attribute auto-tagging (category, colour, pattern, material, season, style)

### Takeaway
Open-source apps today tag almost entirely with a **VLM prompted against a closed vocabulary and forced into JSON**: wardrowbe uses any OpenAI-compatible model (Ollama gemma3 or GPT-4o), and ai-closet uses GPT-4o with a strict JSON schema. The stronger engineering pattern is a hybrid:
- a fashion embedding model (Marqo-FashionSigLIP, Apache-2.0, or FashionCLIP 2.0, MIT) for zero-shot category and pattern plus search and dedupe embeddings;
- deterministic dominant-colour extraction on the masked pixels;
- a VLM only for long-tail attributes, with confidence scores and user confirmation.

### Cited Findings
**wardrowbe (most complete VLM tagger reviewed)**
- **Prompt.** A system prompt starting "OUTPUT ONLY JSON…" injects closed vocabularies: TYPE (required), SUBTYPE (with examples per type), PRIMARY_COLOR, PATTERN (10 values), MATERIAL, FORMALITY, STYLE (1–2 of 12), SEASON, FIT. It also has explicit disambiguation rules, e.g. tights vs leggings vs socks by where the legwear ends. — [wardrowbe prompts/clothing_analysis.txt](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/prompts/clothing_analysis.txt)
- **Vocabulary.** It lives in one JSON shared by backend and frontend: 32 types, 20 colours, 15 materials, 6 formality levels (very-casual → very-formal). — [backend/app/data/garment_vocabulary.json](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/data/garment_vocabulary.json). Frontend parity tests exist (`garment-vocabulary-parity.test.ts`, `subtype-prompt-parity.test.ts`). — [frontend/tests/](https://github.com/Anyesh/wardrowbe/tree/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/frontend/tests)
- **Pydantic schema.** `ClothingTags` keeps `unrecognized_type` so "the model answered something we don't support" is distinguishable from "unknown". — [services/ai_service.py L42-L76](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/ai_service.py#L42-L76)
- **Image preprocessing.** EXIF transpose, RGB, thumbnail to max 512x512 (LANCZOS), JPEG q85, base64. — [ai_service.py L335-L357](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/ai_service.py#L335-L357)
- **Confidence.**
  - `compute_confidence_from_logprobs` walks the token logprobs of the JSON output, takes the minimum token probability per field, and weights fields: type 0.30, primary_color 0.25, pattern/material/formality 0.15 each.
  - A separate `compute_tag_completeness` scores which fields were filled.
  - The code detects providers that reject `logprobs` or `reasoning_effort` and retries without them.
  - [ai_service.py L129-L254](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/ai_service.py#L129-L254)
- **Second call.** A second VLM call produces a one-sentence localised description, in 8 languages. — [ai_service.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/ai_service.py)
- **Data model and jobs.**
  - Tags are written both to a `tags` JSONB and to mirrored columns (`TAG_COLUMNS`).
  - `tagging_status` is pending/tagged and `tagged_by` is auto/manual. `ai_confidence` and `ai_raw_response` are stored.
  - Tagging runs as an arq background job, with a stale-item sweep every 15 min and a retry cooldown via `ai_failed_at`.
  - [models/item.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/models/item.py), [workers/worker.py L131-L136](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/workers/worker.py#L131-L136)
- **Models and hardware.** The README recommends Ollama `gemma3:latest` (3.4 GB) for both vision and text, or `gpt-4o`. It needs "about 4GB of RAM (with local Ollama models)". — [wardrowbe README](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/README.md)
- **Colour correction UI.** The user taps a pixel (canvas `getImageData`), and the RGB value is snapped to the nearest of 20 palette colours. The distance is a custom mix of RGB Euclidean distance, a saturation penalty, and hue difference weighted by `min(saturation)*2`. — [frontend/components/color-eyedropper.tsx L52-L85](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/frontend/components/color-eyedropper.tsx#L52-L85)

**Other apps**
- **ai-closet:**
  - GPT-4o chat completion with `response_format: { type: "json_schema", strict: true }` returning category, subcategory, colour[], season[], occasion[].
  - Image sent with `detail: "low"`.
  - The system prompt lists the allowed category→subcategory pairs and asks for "the top 5 colors in the order of decreasing prominence".
  - [ai-closet src/services/ClothingCategorization.ts](https://github.com/zebangeth/ai-closet/blob/bff94bb050add8d579dbe76bd0259b8fb50b23ca/src/services/ClothingCategorization.ts)
- **Cloa:** no auto-tagging. Category defaults to `.unknown` and "the user must choose manually"; primary and secondary colour are palette strings. — [Cloa Core/Models/ClothingItem.swift](https://github.com/Aerdax/Cloa/blob/a934168b79ec7d1fe846775f2d8282e603ae7521/Cloa/Core/Models/ClothingItem.swift)
- **vestiq:** manual typed tags (`Tag(name, type)`, unique on name+type) via cross-ref tables. — [vestiq data/Tag.kt](https://github.com/kamil-kucharski/vestiq/blob/0d61926a773e037c994e8e293d3cf6f38e333f5f/app/src/main/java/com/example/szafa/data/Tag.kt)

**Fashion embedding models**
- **FashionCLIP:** `zero_shot_classification` encodes the label texts and images, computes cosine similarity, and takes the argmax. — [fashion-clip fashion_clip/fashion_clip.py L247-L261](https://github.com/patrickjohncyh/fashion-clip/blob/0b4b4f9c7e0e6adfe3f2e75809f3f1e84f5ba76a/fashion_clip/fashion_clip.py#L247-L261)

  | Model | FMNIST | KAGL | DEEP |
  |---|---|---|---|
  | OpenAI CLIP | 0.66 | 0.63 | 0.45 |
  | FashionCLIP | 0.74 | 0.67 | 0.48 |
  | LAION CLIP | 0.78 | 0.71 | 0.58 |
  | FashionCLIP 2.0 | 0.83 | 0.73 | 0.62 |

  Weighted macro F1. FashionCLIP 2.0 is fine-tuned from `laion/CLIP-ViT-B-32-laion2B-s34B-b79K`. MIT. — [fashion-clip README](https://github.com/patrickjohncyh/fashion-clip/blob/0b4b4f9c7e0e6adfe3f2e75809f3f1e84f5ba76a/README.md)
- **Marqo-FashionSigLIP:** ViT-B-16-SigLIP fine-tuned with Generalised Contrastive Learning on descriptions, categories, styles, colours and materials. About 0.2B params, **Apache-2.0**. The card mentions a successor, "Marqo Fashion SigLIP 2". — [Marqo/marqo-fashionSigLIP](https://huggingface.co/Marqo/marqo-fashionSigLIP)

  | Metric | FashionSigLIP | FashionCLIP 2.0 |
  |---|---|---|
  | Category-to-product P@1 | 0.758 | 0.681 |
  | Sub-category P@1 | 0.767 | 0.676 |
  | Text-to-image Recall@10 | 0.340 | 0.249 |

**Dominant colour**
- Color Thief (MIT) quantises in RGB or OKLCH (default OKLCH) and runs in the browser and Node (via sharp). Functions: `getColor`, `getPalette`, `getSwatches`, plus a progressive mode. — [lokesh/color-thief](https://github.com/lokesh/color-thief)

**Commercial**
- Acloset's AI classifies "category, color, materials, season, and pattern" (2022). — [2022 press release](https://business.minstercommunitypost.com/minstercommunitypost/article/getnews-2022-11-24-acloset-is-taking-hyper-personalization-to-the-fashion-industry-through-the-ai-powered-digital-closet-platform)

### Inferences
- **VLM-only tagging, as in wardrowbe and ai-closet:**
  - Strength: it adapts to the vocabulary instantly.
  - Weaknesses: it costs per call, is non-deterministic, and is weak at precise colour.
  - Best practices worth copying from wardrowbe: closed vocab injected from a single source of truth; JSON output validated against enums; logprob-based field confidence; `tagged_by` auto/manual; async job queue with retry.
- **Better production design:**
  - Compute the dominant colour(s) only over alpha>0 pixels of the cut-out (k-means or median-cut in OKLCH/CIELAB), then name them via nearest palette entry in a perceptual space (ΔE), instead of trusting the VLM's colour word.
  - Use FashionSigLIP zero-shot (or a small linear probe on its embeddings) for category, pattern and season. Its embeddings can be cached once per item and reused for similarity search, duplicate detection, "complete the look" retrieval and as input to the compatibility model (Q4; owj0421's OutfitTransformer-CLIP already consumes FashionCLIP embeddings).
  - Call a VLM only for material, formality, style and fit, and only for low-confidence cases.
- The ViT-B models (~0.2B params) are small enough for on-device Core ML / TFLite conversion, which enables offline tagging. This is an engineering inference; no on-device latency numbers were found.

### Gaps
- No primary-source per-image VLM tagging cost was gathered for GPT-4o low-detail images or Gemini Flash. Gemini image *input* is billed per token ([Gemini pricing](https://ai.google.dev/gemini-api/docs/pricing)), but per-image token counts were not verified.
- No published accuracy comparison of VLM tagging vs FashionSigLIP on material, formality or season was found.
- The Marqo FashionSigLIP 2 model card was not fetched.

## Q4. Outfit compatibility and recommendation

### Takeaway
The research lineage runs Bi-LSTM (2017) → type-aware embeddings (ECCV 2018, Polyvore Outfits) → graph networks (NGNN 2019) → set transformers (OutfitTransformer). The best open implementation, owj0421/outfit-transformer (MIT), reports **0.95 AUC / 69.24% FITB** on Polyvore with a FashionCLIP encoder.

Every shipping open-source wardrobe app instead uses **rule-based scoring plus feedback heuristics**, and wardrowbe adds an LLM as the final picker. A production system should combine:
- hard rules (slots, weather, formality);
- a learned compatibility scorer;
- per-user re-ranking from feedback.

### Cited Findings
**Research and reference code**
- **Han et al. 2017 (ACM MM):** an outfit is treated as a sequence (top → bottom → accessories). A Bi-LSTM predicts the next item, and a visual-semantic embedding acts as a regulariser. — [arXiv 1707.05691](https://arxiv.org/abs/1707.05691)
- **Vasileva et al., ECCV 2018 (Type-aware embeddings):** introduced Polyvore Outfits, crawled from 68,306 outfits. — [arXiv 1803.09196](https://arxiv.org/abs/1803.09196)
- **Type-aware code (BSD-3):**
  - `TypeSpecificNet` projects a general image embedding into per-type-pair subspaces via masks: fixed disjoint, learned (`nn.Embedding`), or per-condition `nn.Linear`. — [fashion-compatibility type_specific_network.py L28-L133](https://github.com/mvasil/fashion-compatibility/blob/10a1717b7c6c9082b5d26ab8e94829cf9eabb20e/type_specific_network.py#L28-L133)
  - `Tripletnet` sums a type-specific triplet loss, a general-embedding similarity loss, a text-similarity loss, and a visual-semantic (VSE) loss with a selective margin for items lacking text. — [tripletnet.py](https://github.com/mvasil/fashion-compatibility/blob/10a1717b7c6c9082b5d26ab8e94829cf9eabb20e/tripletnet.py)
  - The pre-trained model gives compatibility **AUC 0.88, FITB 57.6** (paper: 0.86/55.3). — [README L31](https://github.com/mvasil/fashion-compatibility/blob/10a1717b7c6c9082b5d26ab8e94829cf9eabb20e/README.md#L31)
- **NGNN (WWW 2019):** categories are nodes in a "Fashion Graph" and an outfit is a subgraph. A node-wise GNN with attention outputs compatibility. — [arXiv 1902.08009](https://arxiv.org/abs/1902.08009)
- **OutfitTransformer (Sarkar, Bodla, Vasileva et al.):**
  - An outfit token trained with a classification loss handles compatibility.
  - A target-item token (category or text) with a set-wise ranking loss handles complementary item retrieval (CIR).
  - Uses pre-training plus curriculum learning.
  - [arXiv 2204.04812](https://arxiv.org/abs/2204.04812)
- **owj0421/outfit-transformer (MIT) implementation:**
  - A pre-norm `nn.TransformerEncoder` (6 layers, 16 heads, FFN 2024, dropout 0.3, Mish) over item embeddings, with learnable task tokens. `predict_score` = sigmoid(FFN(hidden[token 0])) and `embed_query` produces the CIR embedding. Padding uses a learnable `pad_emb`, and the image and text halves are L2-normalised separately. — [src/models/outfit_transformer.py](https://github.com/owj0421/outfit-transformer/blob/eef4be55dbdc2f86f47e3a5d9d4b85c2f17de08b/src/models/outfit_transformer.py)
  - The CLIP variant swaps in `patrickjohncyh/fashion-clip` as the item encoder and precomputes embeddings. — [outfit_clip_transformer.py](https://github.com/owj0421/outfit-transformer/blob/eef4be55dbdc2f86f47e3a5d9d4b85c2f17de08b/src/models/outfit_clip_transformer.py)
  - The demo builds a FAISS `IndexFlatL2` for retrieval. — [src/demo/vectorstore.py](https://github.com/owj0421/outfit-transformer/blob/eef4be55dbdc2f86f47e3a5d9d4b85c2f17de08b/src/demo/vectorstore.py)
  - The authors removed item descriptions and categories from CIR to avoid data leakage. — [readme.md](https://github.com/owj0421/outfit-transformer/blob/eef4be55dbdc2f86f47e3a5d9d4b85c2f17de08b/readme.md)
  - Reported Polyvore results ([readme.md L25-L32](https://github.com/owj0421/outfit-transformer/blob/eef4be55dbdc2f86f47e3a5d9d4b85c2f17de08b/readme.md#L25-L32)):

    | Model | CP AUC | FITB % |
    |---|---|---|
    | Type-Aware | 0.86 | 57.83 |
    | SCE-Net | 0.91 | 59.07 |
    | CSA-Net | 0.91 | 63.73 |
    | OutfitTransformer (paper) | 0.93 | 67.10 |
    | Their implementation | 0.93 | 67.02 |
    | Their implementation + CLIP | 0.95 | 69.24 |

**wardrowbe: two-stage "rules then LLM" recommender**
- **Stage 1, deterministic per-item score** = weather × formality × season × recency × preference × usage, plus a pair bonus. — [services/item_scorer.py L206-L528](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/item_scorer.py#L206-L528)
  - Formality: 1.0 if within the occasion's allowed set, 0.5 one step away, 0.15 otherwise.
  - Season: 1.0 in-season or all-season, 0.6 adjacent season, 0.2 otherwise.
  - Recency: 0.1 if worn today, ramping linearly to 1.0 over `avoid_repeat_days`.
  - Preference: clamped 0.3–1.2 from favourite/avoid colours and learned styles.
  - Usage: 1.15 for items worn ≤0.3× the median.
  - Pair bonus: ≤0.15 for the top-20 items whose learned good partners are also in the top 20.
  - Keeps `TOP_N = 70` and guarantees ≥4 candidates each for footwear and bottoms. Skips scoring entirely under 50 items (`MIN_ITEMS_FOR_SCORING`).
- **Stage 2, the LLM prompt** ([prompts/recommendation.txt](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/prompts/recommendation.txt)):
  - Encodes explicit colour harmony (monochrome / neutral base + one accent / analogous; "Limit to 3 distinct colors maximum. Neutrals do not count").
  - Encodes texture contrast, proportion ("Do not go oversized on both top and bottom"), and layering (base/mid/outer; "should work when the outer layer is removed").
  - Allows three silhouettes: separates, one-piece, or suit.
  - Requires 3 distinct outfits as JSON with item numbers, headline, highlights and styling tip.
- **Learning from feedback** ([services/learning_service.py L420-L672](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/learning_service.py#L420-L672)):
  - Outfit signal: accepted +0.3, rejected −0.5, rating ×0.4, worn +0.3, worn with modifications −0.1, "actually_worn = False" −0.4, plus "wore_instead_items" −0.2, clamped to [−1, 1].
  - Item-pair compatibility = (0.6·acceptance_rate + 0.4·rating_score)·2 − 1, only after a minimum number of pairings.
  - Profiles are recomputed hourly by arq cron. — [workers/worker.py L131-L136](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/workers/worker.py#L131-L136)
  - Persisted in `UserLearningProfile`, `ItemPairScore` (times paired/accepted/rejected, occasion and weather performance JSONB), `OutfitPerformance` and `StyleInsight`. — [models/learning.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/models/learning.py)

**vestiq: fully on-device combinatorial stylist (Kotlin)** — [ui/studio/SmartStylist.kt](https://github.com/kamil-kucharski/vestiq/blob/0d61926a773e037c994e8e293d3cf6f38e333f5f/app/src/main/java/com/example/szafa/ui/studio/SmartStylist.kt)
- **Candidates.** Items are bucketed by kind (Top, Bottom, Dress, Outerwear, Shoes, Accessory). The top-N per kind by `scoreItem` are combined into Separates or Dress templates. Locked items are honoured, and combinations on a "DoNotCombine" blocklist are filtered out.
- **`scoreItem`.** Favourite +2.5; has photo +0.35; formal/athletic subtype match +2.0; exact season +4 (all-season +2, inferred +2.5); style match +4 (mismatch −1.5); occasion-style match +2.75.
- **`colorCompatibility`.** Each colour maps to Neutral/Warm/Cool/Unknown, and the scoring depends on mood (Safe, Colorful, Bold). For example, in Safe mood a neutral scores +2.0, a colour next to an existing neutral +1.6, otherwise −0.4. In Bold mood, warm–cool contrast scores +2.6.
- **Feedback weights.** Liked item 0.7, liked combination 3.5, disliked item −0.6, disliked combination −6.0, preferred item 1.8. Diversity weight 5.0 across the 3 variants (BestMatch / Alternative / BoldChoice). Reasons (Occasion, Season, Style, ColorHarmony, Favorites, WardrobeDiscovery) are emitted for explanation.

**shimmer-strip: simplest form**
- Fixed structure rules (jumpsuit; dress + optional layer; top + bottom), each optionally with jacket, shoes and accessories.
- Per-weather `layerChance`: Hot 0.1, Warm 0.3, Mild 0.6, Cold 0.95, Rainy 0.9.
- Random picks weighted by colour match. `outfitColourScore` is the mean pairwise colour-match score over a 20-colour palette tagged neutral/warm/cool.
- [src/outfitEngine.js](https://github.com/TracySteel/shimmer-strip/blob/2cab8da1d9c1b3347b5a6fef9798ed3f8016975b/src/outfitEngine.js), [src/colours.js L114-L125](https://github.com/TracySteel/shimmer-strip/blob/2cab8da1d9c1b3347b5a6fef9798ed3f8016975b/src/colours.js#L114-L125), [src/mcp.js L22-L41](https://github.com/TracySteel/shimmer-strip/blob/2cab8da1d9c1b3347b5a6fef9798ed3f8016975b/src/mcp.js#L22-L41)

**Commercial**
- Alta uses "over a dozen specialized models" trained with "reinforcement learning" with stylists in an ongoing tuning loop. User directives like "Never give me denim on denim" become part of the profile. It reports 270,000+ outfits/day and 4,000+ brand partners. — [Menlo Ventures](https://menlovc.com/perspective/agentic-styling-and-shopping-why-were-backing-alta/)

### Inferences
- Polyvore-trained models learn *editorial* compatibility from user-made sets of product images. They are a good prior but do not capture a specific user's closet, body or weather.
- **Recommended stack:**
  1. **Constraint layer:** slot grammar (separates / one-piece / suit, layering) as in wardrowbe's prompt and vestiq's templates; weather; formality-by-occasion; laundry/wash state; recency.
  2. **Candidate generation:** enumerate or beam over top-k per slot, as vestiq does.
  3. **Learned compatibility:** OutfitTransformer-CLIP or FashionSigLIP features, scored in milliseconds on precomputed embeddings.
  4. **Personal re-ranking:** a contextual bandit or pairwise model on accept/reject/wear signals, using signal design like wardrowbe's (implicit "wore something else instead" is a strong negative).
  5. **Diversity:** maximal-marginal-relevance across returned looks.
  6. **Optional LLM:** for explanation and natural-language constraints.
- wardrowbe's design is pragmatic. The LLM only sees 70 pre-filtered items, which bounds tokens and cost, but its quality depends heavily on model size; their code adds post-hoc fixes "because prompt wording alone does not keep small models from adding tights on a hot day".
- Rule-based capsule engines like Cladwell's are plausibly close to vestiq/wardrowbe-style weighted rules. This is unverified; Cladwell is closed-source.

### Gaps
- The Polyvore Outfits licence and commercial-use terms were not verified. Polyvore.com no longer exists, so the dataset provenance is scraped content.
- Published OutfitTransformer metrics in the arXiv abstract were not shown on the abstract page. The numbers above come from the reimplementation's readme.
- No public technical detail on Cladwell, Whering, Indyx or Stylebook recommendation logic.
- No open-source wardrobe app reviewed uses a learned compatibility model in production.

## Q5. Weather-, calendar- and occasion-aware suggestion logic

### Takeaway
Weather awareness in open-source apps works the same way everywhere: Open-Meteo (free, keyless) plus Nominatim geocoding, mapped to temperature bands, a rain flag, and layer rules. wardrowbe is the most careful: it looks at the min/max over the remaining "wearing window" of the day, uses hemisphere-aware seasons, and lets the user tune thresholds. **No open-source app reviewed integrates a real calendar (ICS/Google/EventKit).** Occasion is a user-selected enum mapped to allowed formality levels. Calendar-driven styling exists only in closed apps (Alta) or via an agent's connectors (the fashion-skill Claude skill).

### Cited Findings
**wardrowbe**
- **Weather source.** Open-Meteo forecast plus Nominatim geocoding, with Redis-cached weather and geocodes. — [services/weather_service.py L74, L147](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/weather_service.py)
- **Wearing window.** `_wearing_window` takes hourly temperatures from now until `WEARING_DAY_END_HOUR = 21` local (needs ≥2 hours) and returns (min, max). The scorer uses that range rather than the current temperature. — [weather_service.py L14-L15, L362-L389](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/weather_service.py#L362-L389)
- **Defaults** ([data/scales.json](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/data/scales.json)):
  - Cold threshold 10 °C (user range −20..30).
  - Hot threshold 25 °C (user range 10..45).
  - Avoid-repeat 7 days (0..30). The variety level scales this ×1.5 (high) or ×0.5 (low).
- **Temperature and rain rules** ([item_scorer.py L50-L246](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/item_scorer.py#L50-L246)):
  - Cold band: wool/fleece/knit or warm layers score 1.0; shorts, tank-tops and sandals 0.05.
  - Hot band: cotton/linen/silk or summer items score 1.0; warm layers and boots 0.05.
  - Heavy layers are detected by type `coat`, materials wool/fleece/down/shearling, or subtypes puffer/parka/peacoat/overcoat.
  - Rain is "likely" at ≥50% precipitation chance and favours jacket/coat/hoodie.
  - `get_season(month, latitude)` flips the season map for latitude < 0.
- **Occasion → allowed formality.** 22 occasions, e.g. interview → business-casual/formal/very-formal; gym, running and hiking → very-casual/casual; date, dinner and party → smart-casual/business-casual/formal. — [data/garment_vocabulary.json](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/data/garment_vocabulary.json)
- **Time of day.** The prompt gives guidance per slot: morning/daytime, afternoon, evening, night out, full day. — [prompts/recommendation.txt](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/prompts/recommendation.txt)
- **Proactive delivery.** Scheduled daily suggestions go out via ntfy, Mattermost or email. arq cron checks schedules every minute, sends wash reminders 4×/day, and updates learning profiles hourly. — [README](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/README.md), [workers/worker.py L131-L136](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/workers/worker.py#L131-L136)
- **Planning data.** `Outfit.scheduled_for` (date), `weather_data` JSONB snapshot and `occasion`. — [models/outfit.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/models/outfit.py)

**Other apps**
- **Libre-Closet v0.6.0 (5 Oct 2026)** added forecast previews on calendar day cards while scheduling outfits. It uses Open-Meteo plus Nominatim reverse geocoding, with `WEATHER_CACHE_TTL_MS` defaulting to 1,800,000. Its `OutfitCalendar` entity has date, outfit, owner, `wornAt` (null until marked worn; "may differ from `date` if rescheduled") and notes. — [Libre-Closet README](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/README.md), [src/weather/weather.service.ts L19-L20](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/src/weather/weather.service.ts#L19-L20), [src/dal/entity/outfit-calendar.entity.ts](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/src/dal/entity/outfit-calendar.entity.ts)
- **shimmer-strip** uses Open-Meteo for a fixed latitude/longitude, with today or tomorrow ("for evening outfit planning"). Bands: cold <12 °C, mild 12–18, warm 18–25. — [src/weather-server.js](https://github.com/TracySteel/shimmer-strip/blob/2cab8da1d9c1b3347b5a6fef9798ed3f8016975b/src/weather-server.js)
- **vestiq** has no weather integration. Season, occasion, style and mood are chosen manually in `StylistPreferences`. — [SmartStylist.kt](https://github.com/kamil-kucharski/vestiq/blob/0d61926a773e037c994e8e293d3cf6f38e333f5f/app/src/main/java/com/example/szafa/ui/studio/SmartStylist.kt)
- **No calendar integration anywhere.** A grep for `icalendar|.ics|google calendar|caldav|EventKit|CalendarContract` across the wardrowbe, Libre-Closet, vestiq, Cloa, ai-closet and shimmer-strip sources returned no matches (my grep at the commits above).
- **Closed and agent-based calendar use:**
  - Alta's founder describes generating daily outfits from "calendar, the weather, and their style", plus packing lists. — via search summary of [TechCrunch](https://techcrunch.com/?p=3092988) / [WWD](https://wwd.com/fashion-news/fashion-features/cfda-partnership-alta-personal-styling-app-ai-1237089028)
  - The fashion-skill (Claude Code skill, MIT) does "week-ahead planning using calendar and weather" by relying on the agent's tools. — [googlarz/fashion-skill](https://github.com/googlarz/fashion-skill)

### Inferences
- **Production weather model:**
  - Use apparent temperature over the wearing window (as wardrowbe does), precipitation probability and wind, and the user's personal thresholds (people run hot or cold), learned from "wore something else" feedback.
  - Snapshot the forecast onto planned outfits (wardrowbe `weather_data`) so you can re-plan when the forecast changes.
- **Calendar → occasion** is the main open gap:
  - Read events via EventKit (iOS) or CalendarContract / Google Calendar API.
  - Classify title and location into the occasion enum with a small LLM.
  - Map that to the formality ranges already in wardrowbe's `OCCASION_FORMALITY`.
  - Schedule the push for the evening before.
- Trip and packing lists can reuse the same machinery over a multi-day forecast at the destination. None of the reviewed apps implements a Trip/PackingList entity (grep found only "round trip" comments).

### Gaps
- No open-source reference implementation of calendar-event → occasion classification was found.
- I did not review the repos that advertise packing lists (`stephthegeek/closetlint`, `SarveshP16/Wardro-app`, both 0 stars per the topic page).

## Q6. Virtual try-on: models, Google's work, licences and compute cost

### Takeaway
Most academic try-on models are **non-commercial**: CatVTON, IDM-VTON, OOTDiffusion, StableVITON and FitDiT are CC BY-NC-SA 4.0, and they are trained on VITON-HD (CC BY-NC 4.0, "research purposes only") or DressCode. As of Oct 2026, the commercially usable routes are:
- **FASHN VTON v1.5** (Apache-2.0, 972M params, mask-free, Jan 2026), self-hosted;
- paid APIs: Google Vertex `virtual-try-on-001` (GA Jan 2026, about $0.06/image per trackers), Gemini image-edit models ($0.034–$0.134/image), Photoroom ($0.10), Kling Kolors try-on;
- general image-edit models driven by careful prompts, which is what the open-source apps actually ship.

### Cited Findings
**CatVTON (code read)**
- Built on SD-1.5 inpainting. The whole UNet's cross-attention is replaced by `SkipAttnProcessor` (no text encoder), and only self-attention modules are trainable.
- The masked person latent and the garment latent are **concatenated spatially** (`concat_dim = -2`, i.e. stacked on the height axis), with a zero mask for the garment half.
- Classifier-free guidance is implemented by zeroing the garment latent.
- Defaults: 50 steps, guidance 2.5. The output is split back to the person half.
- [CatVTON model/pipeline.py L44-L197](https://github.com/Zheng-Chong/CatVTON/blob/7818397f25613beedb3d861a34769f607cfcf3b1/model/pipeline.py#L44-L197)
- README figures: "899.06M parameters totally", "49.57M parameters trainable", "< 8G VRAM for 1024X768". A CatVTON-FLUX LoRA (37.4M) for FLUX.1-Fill-dev also exists. "All the materials, including code, checkpoints, and demo" are **CC BY-NC-SA 4.0**. — [CatVTON README](https://github.com/Zheng-Chong/CatVTON/blob/7818397f25613beedb3d861a34769f607cfcf3b1/README.md)

**Other open models**
- **IDM-VTON:** CC BY-NC-SA 4.0 for code and checkpoints; SDXL IP-Adapter based; needs DensePose, OpenPose and ATR/LIP parsing; example inference at 768x1024, 30 steps, guidance 2.0. — [yisol/IDM-VTON](https://github.com/yisol/IDM-VTON)
- **Leffa:** MIT code; weights licence not stated; unified try-on and pose transfer; "generating an image in 6 seconds (on A100)" in fp16; trained on VITON-HD, DeepFashion and DressCode. — [franciszzj/Leffa](https://github.com/franciszzj/Leffa)
- **FASHN VTON v1.5:** Apache-2.0. Generates "directly in pixel space without requiring segmentation masks". Inputs are a person image, a garment image and a category (tops/bottoms/one-pieces). bf16 on Ampere+. Bundles DWPose and a human parser. — [fashn-AI/fashn-vton-1.5](https://github.com/fashn-AI/fashn-vton-1.5)
- **Vendor comparison (FASHN blog, updated Oct 2026; FASHN sells a competing try-on API, so treat as interested-party).** Per-model timings and VRAM:

  | Model | Time / VRAM |
  |---|---|
  | CatVTON | ~11 s on A100 |
  | IDM-VTON | ~17 s on A100 |
  | OOTDiffusion | ~46 s on L40S |
  | OmniTry (FLUX.1-Fill-dev LoRA) | ≥28 GB VRAM |
  | Qwen-Image-Edit-2509 + try-on LoRA | Apache-2.0, 20B params, "far more GPU memory" |

  Other points from the same post:
  - StableVITON and FitDiT are CC BY-NC-SA; Voost is non-commercial with weights not downloadable.
  - FASHN VTON v1.5: 972M params, about 2 GB of weights, retrainable for roughly $5–10k of compute.
  - June 2025 tests: CatVTON best on garment shape and structure; IDM-VTON best on texture and colour.
  - Sept 2026: Qwen-Image 2.1 matched a commercial API on 6 of 8 pairs, under a research-only licence.
  - [FASHN: comparing open-source VTON models](https://fashn.ai/blog/comparing-the-top-4-open-source-virtual-try-on-viton-models)
- **VITON-HD dataset:** 11,647 train / 2,032 test pairs at 1024×768, "Creative Commons BY-NC 4.0", "for research purposes only". — [shadow2496/VITON-HD](https://github.com/shadow2496/VITON-HD)
- **Newer research:** DiT-VTON (WACV 2026, diffusion transformer, multi-category) — [CVF open access](https://openaccess.thecvf.com/content/WACV2026/html/Li_DiT-VTON_Diffusion_Transformer_Framework_for_Unified_Multi-Category_Virtual_Try-On_and_WACV_2026_paper.html). OpenVTON-Bench (Jan 2026) is a high-res benchmark — [arXiv 2601.22725](https://arxiv.org/pdf/2601.22725). Tstars-Tryon 1.0 — [arXiv 2604.19748](https://arxiv.org/pdf/2604.19748). Licences unverified.

**Google**
- **TryOnDiffusion** (Zhu et al., CVPR 2023): Parallel-UNet that warps the garment implicitly via cross-attention and combines warping and blending in one pass. — [arXiv 2306.08276](https://arxiv.org/abs/2306.08276)
- **Shopping try-on** uses a "custom image generation model" that accounts for folds, stretching and drape across body types. — secondary coverage, e.g. [Inquirer](https://technology.inquirer.net/147179/you-can-now-try-on-clothes-and-accessories-virtually-with-google/amp), [Apparel Resources](https://in.apparelresources.com/technology-news/retail-tech/googles-virtual-try-tool-clothing-launches-india)
- **Doppl:** Google Labs app, iOS and Android, US-only at launch on 26 Jun 2025. Tries on outfits from screenshots and generates "AI videos" of the user. Builds on the Shopping try-on announced in May 2025. Google warns fit and details "may not be accurate". — [Google blog: Doppl](https://blog.google/technology/google-labs/doppl/). Early reviewers reported trouble with trousers. — [iamdave.ai summary](https://www.iamdave.ai/?p=993259) (secondary, low reliability)
- **Vertex AI `virtual-try-on-001`:** GA, release 20 Jan 2026, discontinuation 20 Jan 2027. Person image plus product images; output matches input aspect ratio; ≤4 outputs; 10 MB max; 50 requests/min regional quota. Pricing is listed under Imagen. — [Vertex AI docs](https://docs.cloud.google.com/vertex-ai/generative-ai/docs/models/imagen/virtual-try-on-001). Third-party trackers put it at **~$0.06/image**. — [cloudprice.net](https://cloudprice.net/models/google-virtual-try-on-1), [Pixazo ($0.063 resale)](https://www.pixazo.ai/blog/introducing-google-virtual-try-on-api-on-pixazo-api)
- **Gemini image models** (usable for prompt-driven try-on), output per 1K image: `gemini-3.1-flash-image` (Nano Banana 2) $0.067; `gemini-3.1-flash-lite-image` $0.0336; `gemini-3-pro-image` $0.134 (1K/2K); `gemini-nano-banana-2.1` $0.0336. Batch roughly halves these. `gemini-2.5-flash-image` ($0.039) "will be shut down on October 2, 2026". — [Gemini API pricing](https://ai.google.dev/gemini-api/docs/pricing)

**What the open-source apps actually ship**
- **ai-closet:**
  - Calls Kling's `/v1/images/kolors-virtual-try-on` API with a person photo and an outfit image, polling every 1 s for up to 60 tries.
  - Signs the Kling JWT **on the device** with `EXPO_PUBLIC_KWAI_SECRET_KEY` (via `expo-jwt`), which ships the secret in the app bundle.
  - [src/services/VirtualTryOn.ts](https://github.com/zebangeth/ai-closet/blob/bff94bb050add8d579dbe76bd0259b8fb50b23ca/src/services/VirtualTryOn.ts)
- **Cloa:**
  - Uses a general image-generation/edit model rather than a VTON model: Doubao-Seedream 5.0 Pro (`doubao-seedream-5-0-pro-260628`, fallback 4.5) via Volcano Ark `POST /api/v3/images/generations`.
  - Sends the avatar as image 1 and garments as images 2..n.
  - A long prompt casts the model as a "precise local outfit-swap engine". It freezes face, skin tone, body proportions, lighting and composition pixel-for-pixel, ignores any person in the garment photos, and only swaps garments on body parts visible in image 1.
  - [Core/Services/VolcArkImageService.swift L21-L71, L541-L571](https://github.com/Aerdax/Cloa/blob/a934168b79ec7d1fe846775f2d8282e603ae7521/Cloa/Core/Services/VolcArkImageService.swift#L541-L571)
- **Photoroom** includes virtual try-on in its $0.10/image Plus API. — [Photoroom API pricing](https://www.photoroom.com/api/pricing)
- **Alta's avatar** can layer at least eight items in seconds (per a report comparing it with Zara's tool). — via search summary of [TechCrunch](https://techcrunch.com/?p=3017882) (secondary)

### Inferences
- **For a commercial app in late 2026:**
  - Start with a paid API: Vertex `virtual-try-on-001` at ~$0.06, or a Gemini Flash image model at $0.034–0.067.
  - Use FASHN VTON v1.5 (Apache-2.0) if you need self-hosting.
  - Avoid CatVTON, IDM-VTON, OOTDiffusion, StableVITON and FitDiT in production regardless of code licence. Even MIT-coded Leffa was trained on BY-NC datasets (VITON-HD), a legal grey zone.
- **Self-hosted GPU cost** at the quoted 6–17 s/image on A100 is a few A100-seconds per render. Multiply by your GPU $/hour (no source gathered for GPU rental prices).
- **Wardrobe try-on is harder than retail try-on.** User garment photos are cut-outs of worn or folded clothes rather than studio flat-lays, and multi-item layering (top + outer + bottom + shoes) is needed. Single-garment VTON models need sequential passes. General editing models (Seedream, Gemini, Qwen-Image-Edit) accept multiple reference images in one call, which explains why Cloa and Doppl-like experiences lean on them. Identity drift is the cost, hence Cloa's prompt obsession with freezing face, skin and lighting.

### Gaps
- No primary-source Vertex AI try-on price (only trackers). No published latency for Vertex try-on, Doppl, or Kling Kolors try-on.
- The OOTDiffusion, FitDiT, HR-VITON, DCI-VTON and StableVITON licences were taken from the FASHN comparison, not verified on each repo. HR-VITON and DCI-VTON licences were not checked at all.
- No source confirms which try-on model powers Doppl or Alta.

## Q7. LLM stylist chat: grounding an LLM in the user's wardrobe

### Takeaway
None of the reviewed open-source apps has a production chat stylist with RAG over item embeddings. Grounding is done in three ways:
1. **Pre-ranked context stuffing with short item IDs and strict server-side validation** (wardrowbe; the most robust pattern seen).
2. **An MCP tool server** that exposes filtered wardrobe queries, weather and save-outfit actions to an external agent (shimmer-strip; wardrowbe's "external agent" mode).
3. **Flat JSON files read by an agent skill** (fashion-skill).

Libre-Closet's `/chat` is a stub.

### Cited Findings
**wardrowbe: context stuffing with validation**
- **Input format.** Scored items (≤70) are rendered as compact lines (`subtype (type)`, colours, non-solid pattern, size, etc.) keyed by **integer item numbers**, and the server keeps `number_map: dict[int, UUID]`. — [services/recommendation_service.py L345-L422](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/recommendation_service.py#L345-L422)
- **Output validation** ([recommendation_service.py L138-L198](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/recommendation_service.py#L138-L198)):
  - Non-numeric or unknown numbers are dropped with a warning (`picked_item_ids`).
  - Missing mandatory items are re-inserted, duplicates in the same body slot are removed, and legwear is dropped on hot days (`resolve_outfit_items`).
  - `fill_best_partial` deterministically fills missing body regions from the ranked list and records `_filled_item_ids` "because the headline and styling tip were written for the AI's picks alone".
- **Prompt context.** The prompt carries weather (temperature, feels-like, condition, rain %), occasion, time of day, learned preferences and good pairs, and asks for 3 outfits per call. Spare outfits are cached in Redis (`CACHE_TTL = 3600`) to serve "another suggestion" without a new LLM call. — [prompts/recommendation.txt](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/prompts/recommendation.txt), [services/suggestion_cache.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/suggestion_cache.py)
- **Pairings.** "Complete the look" for one item uses a separate prompt that forces the source item into every outfit and spells out slot exclusivity. — [prompts/item_pairing.txt](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/prompts/item_pairing.txt)
- **External agent mode.**
  - Works with any OpenAI-compatible endpoint (OpenAI, Ollama, LocalAI).
  - `AI_INTERNAL_ENABLED=false` "defers tagging/suggestions/pairings to an external agent", with the effective state at `GET /api/v1/capabilities`.
  - Externally authored outfits are ordinary rows with `source='external'`.
  - [README](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/README.md), [services/external_outfit_service.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/external_outfit_service.py)

**shimmer-strip: MCP tool server**
- Exposes an MCP server over Streamable HTTP at `/mcp` with 8 tools: `get_wardrobe_summary`, `get_wardrobe`, `get_weather`, `suggest_outfit`, `save_outfit`, `get_outfits`, `get_currently_wearing`, `get_weekly_picks`. — [README](https://github.com/TracySteel/shimmer-strip), [server.js L6](https://github.com/TracySteel/shimmer-strip/blob/2cab8da1d9c1b3347b5a6fef9798ed3f8016975b/server.js#L6)
- Tool descriptions steer token use, e.g. `get_wardrobe`: "ALWAYS use filters — never call without at least one filter. For outfit picking, call once per category … Use get_wardrobe_summary first"; `get_outfits` "Returns SUMMARIES by default (lightweight, token-efficient)". — [src/mcp.js L327-L370](https://github.com/TracySteel/shimmer-strip/blob/2cab8da1d9c1b3347b5a6fef9798ed3f8016975b/src/mcp.js#L327-L370)
- **Security:** the cookie auth uses a hard-coded token constant, and the comment says "MCP at /mcp is unaffected", i.e. the MCP endpoint is not behind that auth. — [server.js L98-L108](https://github.com/TracySteel/shimmer-strip/blob/2cab8da1d9c1b3347b5a6fef9798ed3f8016975b/server.js#L98-L108)

**Other approaches**
- **fashion-skill (Claude Code skill, MIT, 19 stars, v1.4.0 25 Apr 2026)** grounds the model with `profile.json`, `inventory.json`, `feedback.json` and `recommendations-history.json` in `FASHION_DATA_DIR`, plus instructions in SKILL.md. There are no scripts. — [googlarz/fashion-skill](https://github.com/googlarz/fashion-skill)
- **Libre-Closet** `/chat` posts the user message to an RxJS subject that echoes it back into HTML **unescaped** and replies with a hard-coded "1", i.e. a placeholder with an XSS-shaped bug. — [src/app.controller.ts L64-L100](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/src/app.controller.ts#L64-L100)
- **Alta:** user statements like "Never give me denim on denim" become profile constraints. — [Menlo Ventures](https://menlovc.com/perspective/agentic-styling-and-shopping-why-were-backing-alta/)

### Inferences
- **Production pattern:**
  - Run a tool-calling agent with typed tools: `search_items` (structured filters plus semantic query over FashionSigLIP text→image embeddings), `get_item`, `get_weather(date, place)`, `get_calendar(range)`, `propose_outfit(item_ids)` (server validates slots, ownership, laundry), `save_outfit`, `log_wear`, `add_constraint`.
  - Return short IDs plus thumbnails to the client, and send item images to the model only when a visual judgement is needed.
  - Persist user directives ("no denim on denim") as hard filters applied before the LLM sees candidates.
- wardrowbe's integer-ID-plus-validation design is directly reusable and avoids UUID hallucination. Its weakness is that it is single-shot generation, not conversational.
- MCP servers make the wardrobe usable from Claude or ChatGPT clients cheaply, but shimmer-strip shows the risk: an unauthenticated MCP endpoint exposes the whole wardrobe. Production needs OAuth per user.

### Gaps
- No open-source wardrobe repo implementing embedding-based RAG for chat was found. The topic page lists repos advertising Claude-based stylists (`SarveshP16/Wardro-app`, `andresmuttioblu/ownfit`, 0 stars) that were not reviewed.
- No quantitative evaluation of LLM stylists (acceptance rates, hallucination rates) was found.

## Q8. App architecture and data models of open-source wardrobe apps

### Takeaway
There are two architectural camps:
- **Self-hosted server apps:** wardrowbe (Next.js + FastAPI + Postgres + Redis/arq workers) and Libre-Closet (NestJS + HTMX + SQLite/Postgres + Workbox PWA).
- **Local-first mobile apps with no sync:** vestiq (Kotlin/Compose/Room, manual `.vestiq` export), Cloa (SwiftUI/SwiftData, CloudKit "pending"), ai-closet (Expo + AsyncStorage).

Converging data model: Item (images and variants, type/subtype, colours, attribute tags, AI provenance, wear/wash counters) ↔ Outfit (ordered items with slot/role, occasion, scheduled date, weather snapshot) ↔ WearLog/Feedback. Trip/PackingList is absent from all six. No Flutter app of substance was found.

### Cited Findings
**wardrowbe** ([README](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/README.md), [k8s/](https://github.com/Anyesh/wardrowbe/tree/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/k8s))
- **Stack:** Next.js + React Query frontend; FastAPI + async SQLAlchemy + Alembic; PostgreSQL; Redis job queue; arq background worker.
- **Deployment:** multi-arch images (amd64/arm64, "runs on a Raspberry Pi 5"); Docker Compose and K8s manifests including a separate image-worker.
- **`ClothingItem`** ([models/item.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/models/item.py)):
  - Images: `image_path`/`thumbnail_path`/`medium_path`/`original_image_path`, `image_hash` (pHash), and `upload_key` (client idempotency key from the frontend IndexedDB bulk-upload queue, unique per user).
  - Attributes: `type`/`subtype`, `tags` JSONB mirrored to colour/pattern/material/style/season/formality columns.
  - AI lifecycle: status, `ai_*` timestamps, confidence, raw response, `tagging_status`, `tagged_by`.
  - Usage: `wear_count`, `last_worn_at`, `suggestion_count`, `acceptance_count`.
  - Washing: `wears_since_wash`, `wash_interval`, `needs_wash`.
  - Purchase: price and date, favourite, archive with reason.
  - Children: `ItemHistory` (wear log with outfit_id and occasion), `WashHistory`, `ItemImage`.
- **`Outfit`** ([models/outfit.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/models/outfit.py)):
  - Context and content: `weather_data` JSONB, `occasion`, `scheduled_for`, reasoning, style notes, season, formality, palette.
  - Lifecycle and lineage: status, source, `replaces_outfit_id`, `cloned_from_outfit_id`, sent/viewed/responded timestamps.
  - `OutfitItem(position, layer_type)`.
  - `UserFeedback(accepted, rating, comfort_rating, style_rating, worn_at, worn_with_modifications, actually_worn, wore_instead_items)`.
  - `FamilyOutfitRating`.
- **Images:** 800 px medium (q90) and 400 px thumbnail (q88) variants; pHash via `imagehash`; HEIC via `pillow-heif`. — [services/image_service.py](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/app/services/image_service.py), [requirements.txt](https://github.com/Anyesh/wardrowbe/blob/0bbca3a4d92b9d3cec64a5a09ffdacce41f04d04/backend/requirements.txt)
- **Other features:** 8-language i18n, family accounts, OIDC/forward-auth. The repo holds 71 frontend test files and 68 backend test entries (directory counts at the pinned commit).

**Libre-Closet** ([src/dal/entity/](https://github.com/Lazztech/Libre-Closet/tree/08f66dd9fe071e3b62e529d0ca4050169d31532d/src/dal/entity), [README](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/README.md))
- **Stack:** NestJS on Fastify, server-rendered Handlebars + HTMX, MikroORM with SQLite (default) or Postgres (separate migration sets), local or S3 file storage, web push, optional JWT auth and registration lock.
- **Entities:** `Garment` (name, category, colour enum, brand, size, dateAquired, notes, archived, washingDetails, photo→File, owner, M2M outfits), `Outfit`, `OutfitCalendar`, `WardrobeShare`, `UserDevice`.
- **Offline:** Workbox service worker with `precacheAndRoute`, `NetworkFirst`, and an offline.html fallback. — [views/assets/src-sw.ts](https://github.com/Lazztech/Libre-Closet/blob/08f66dd9fe071e3b62e529d0ca4050169d31532d/views/assets/src-sw.ts)
- **Quality:** Playwright and Lighthouse CI configs present; version 0.6.0.

**ai-closet** ([package.json](https://github.com/zebangeth/ai-closet/blob/bff94bb050add8d579dbe76bd0259b8fb50b23ca/package.json), [src/types/](https://github.com/zebangeth/ai-closet/tree/bff94bb050add8d579dbe76bd0259b8fb50b23ca/src/types))
- **Stack:** Expo SDK 54, React Native 0.81, React Context providers, `@react-native-async-storage/async-storage` persistence, local file URIs, `react-native-view-shot` to rasterise outfit canvases. No backend.
- **`ClothingItem`:** `imageUri` + `backgroundRemovedImageUri`, category/subcategory, tags, colour[], season[], occasion[], brand, purchaseDate, price, and per-step `processingStatus` (backgroundRemoval, categorization: pending/processing/completed/error).
- **`Outfit.clothingItems[]`** holds `{id, transform:{x,y,scale,rotation}, zIndex}`. This is the collage-canvas model that Stylebook-style apps need.

**vestiq** ([data/](https://github.com/kamil-kucharski/vestiq/tree/0d61926a773e037c994e8e293d3cf6f38e333f5f/app/src/main/java/com/example/szafa/data), [README](https://github.com/kamil-kucharski/vestiq))
- **Stack:** Kotlin, Jetpack Compose, Room, coroutines, Android Image Cropper, ML Kit; minSdk 26.
- **Entities:** `ClothingItem`, `Outfit`, `OutfitItemCrossRef(outfit_id, clothing_item_id, garment_role, position)`, `Tag(name, type)`, item/outfit tag cross-refs, `StylistFeedback(type ∈ {Like, Dislike, DoNotCombine, PreferItem}, item_ids, occasion, season, style, mood, count)`.
- **Data handling:** Room migration tests exist. Backup is only via a versioned `.vestiq` archive containing photos and data (`data/transfer/BackupArchiveCodec.kt`). No account or cloud.

**Cloa** ([Cloa/Core/Models/](https://github.com/Aerdax/Cloa/tree/a934168b79ec7d1fe846775f2d8282e603ae7521/Cloa/Core/Models), [CloaApp.swift L30-L60](https://github.com/Aerdax/Cloa/blob/a934168b79ec7d1fe846775f2d8282e603ae7521/Cloa/CloaApp.swift#L30-L60), [README](https://github.com/Aerdax/Cloa))
- **Stack:** Swift 6, SwiftUI, SwiftData, `@Observable` MVVM, iOS 26.2, no third-party dependencies.
- **Models:** `@Model ClothingItem` stores *relative* paths under Documents/ (original plus 256×256 thumbnail). `Outfit.items` uses delete rule `.nullify`. `TryOnRecord` cascades from both items and outfits.
- **Sync:** the `ModelContainer` is local-only; a comment says CloudKit sync waits for container setup.

**shimmer-strip** — Express with JSON-file storage (`data/wardrobe.json`, `data/photos/`), React frontend, MCP endpoint. — [README](https://github.com/TracySteel/shimmer-strip)

**Flutter**
- Searches for "OpenWardrobe" (Flutter + Supabase) found no repository. Flutter wardrobe apps surfaced only as Google Gemini competition entries (Fashine, What-robe, OOTD) and `StarlitDreams/StyleSync`, none reviewed. — [ai.google.dev Fashine](https://ai.google.dev/competition/projects/fashine), [StyleSync](https://github.com/StarlitDreams/StyleSync)
- Google publishes a codelab for a Flutter + ADK Go + Gemini "virtual fitting room and AI stylist". — [Google codelab](https://codelabs.developers.google.com/codelabs/fitting-room/instructions)

### Inferences
**Reference data model for a production app**, union of the best ideas above:
- **`Item`:** id, owner, images {original, cutout (alpha), medium, thumb}, pHash plus embedding vector, type/subtype/role, colours (with hex and named), pattern, material, formality, seasons, styles, fit, brand, size, price, purchase date, provenance per attribute (auto/manual + confidence), status, wear count, last worn, wash state, archived reason.
- **`Outfit`:** id, items[{item_id, role/slot, layer, canvas transform, z}], occasion, source (user/ai/external), lineage, palette, notes.
- **`PlannedOutfit` / `CalendarEntry`:** date, outfit, event ref, weather snapshot, wornAt.
- **`WearLog`:** date, outfit or items, occasion, photo.
- **`Feedback`:** accept/reject/rating/actually-worn/wore-instead.
- **`PairScore`** and **`UserProfile`** (learned preferences and explicit constraints).
- **`Trip` / `PackingList`:** destination, dates, forecast, item checklist.
- **`Tag`** (user-defined).

**Sync and offline:**
- Local DB (SwiftData/Room/SQLite) as the source of truth on device, with a server for ML, backup and multi-device sync.
- Use client-generated UUIDs and idempotency keys (as wardrowbe's `upload_key`) for an offline upload queue.
- Store relative paths (as Cloa does) so app containers can move.
- CloudKit is the cheapest sync on iOS-only. A cross-platform app needs its own sync (e.g. Postgres plus change feeds).

**Framework choice:** native gives first-class access to Apple Vision and ML Kit segmentation and to Core ML / TFLite for embeddings. React Native/Expo apps (ai-closet) push all ML to paid APIs, and in ai-closet's case leak keys in the bundle.

### Gaps
- No substantial open-source Flutter or React Native wardrobe app with server sync was found to review.
- Commercial apps' sync architectures are not public.

## Q9. Repo-by-repo code review: quality, licence, activity, reusability

### Takeaway
**wardrowbe** is by far the most production-like open-source wardrobe codebase (MIT, 803 stars, commits days ago, extensive tests, careful recommender and AI plumbing) and the best source of reusable patterns. **Libre-Closet** (AGPL, 376 stars) is the best reference for in-browser background removal and PWA offline. vestiq and Cloa are small but clean native references. ai-closet is a demo with security anti-patterns. On the ML side, the reusable assets are rembg's model zoo (MIT, but check per-model licences), FashionCLIP/FashionSigLIP (MIT/Apache), the outfit-transformer reimplementation (MIT) and type-aware code (BSD-3). CatVTON is study-only (BY-NC-SA).

### Cited Findings
**Wardrobe apps**

| Repo | Licence | Activity | Stack | Strengths | Issues | Reuse |
|---|---|---|---|---|---|---|
| [Anyesh/wardrowbe](https://github.com/Anyesh/wardrowbe) | MIT ("Copyright (c) 2026 Anish S.") | 803 stars, last commit 24 Oct 2026; has App Store / Google Play listings and a separate Cloud edition | Next.js / FastAPI / Postgres / Redis / arq | Closed-vocab VLM tagging with logprob confidence; multiplicative rule scorer; LLM with ID validation; feedback learning; wash tracking; scheduled notifications; i18n | Recommender quality is bounded by the configured LLM; no embeddings; no calendar | Scorer, prompt templates, validation layer, data model |
| [Lazztech/Libre-Closet](https://github.com/Lazztech/Libre-Closet) | AGPL-3.0 | 376 stars, last commit 11 Oct 2026, v0.6.0 | NestJS / HTMX / MikroORM / Workbox | Client-side ISNet bg removal (WebGPU/WASM) with iOS fallback patch; mask editor; S3/local storage; calendar plus weather | Stale "server fallback" comment (no server generation); `/chat` stub echoes unescaped HTML; no AI tagging or recommendations | Patterns only for closed-source products, due to AGPL and AGPL dependency |
| [zebangeth/ai-closet](https://github.com/zebangeth/ai-closet) | MIT | 214 stars per [ecosyste.ms](https://awesome.ecosyste.ms/projects/github.com%2Fzebangeth%2Fai-closet); last commit 9 Nov 2025 | Expo 54 / RN 0.81 / AsyncStorage | Canvas outfit model; clean API-wrapper services | Ships OpenAI, fal and Kling keys as `EXPO_PUBLIC_*` client env vars and signs Kling JWTs on device; no backend or sync | Canvas model and RN UI only |
| [kamil-kucharski/vestiq](https://github.com/kamil-kucharski/vestiq) | MIT (2026) | 8 stars, last commit 26 Jul 2026 | Kotlin / Compose / Room | ML Kit cut-out; fully on-device explainable stylist with feedback types; Room migration tests; versioned backup | Manual tagging; no weather | `SmartStylist.kt` as a reference rule engine |
| [Aerdax/Cloa](https://github.com/Aerdax/Cloa) | MIT | 5 stars, last commit 23 Aug 2026 | Swift 6 / SwiftUI / SwiftData (iOS 26.2) | Clean SwiftData model; dHash dedupe; multi-image try-on prompt template | No Vision subject lifting; no auto-tagging; CloudKit not yet enabled; Chinese-language comments | SwiftData schema and try-on prompt approach |
| [TracySteel/shimmer-strip](https://github.com/TracySteel/shimmer-strip) | MIT | 9 stars, last commit 5 Jul 2026 | Express / JSON files / React | MCP tool design | Hard-coded auth token; unauthenticated `/mcp`; personal-use hobby app | MCP tool schema ideas |

**ML repos**

| Repo | Licence | Last commit | Notes | Reuse |
|---|---|---|---|---|
| [danielgatis/rembg](https://github.com/danielgatis/rembg) | MIT; model weights separately licensed | 21 Oct 2026 | Session abstraction over ~20 ONNX models; CLI / HTTP server / library | Production-usable with pinned permissive models |
| [patrickjohncyh/fashion-clip](https://github.com/patrickjohncyh/fashion-clip) | MIT | 30 Jan 2025 | Thin API over HF `patrickjohncyh/fashion-clip` (zero-shot, retrieval, attention maps); README says FashionCLIP 2.0 fine-tuned from LAION ViT-B/32 | Model weights via HF |
| [mvasil/fashion-compatibility](https://github.com/mvasil/fashion-compatibility) | BSD-3 | 29 Dec 2025 | Old PyTorch idioms (`Variable`); 2018 research code | Teaching reference |
| [owj0421/outfit-transformer](https://github.com/owj0421/outfit-transformer) | MIT (2024) | 27 Dec 2025 | Clean modern PyTorch: dataclass configs, precomputed CLIP embeddings, FAISS demo, DDP utils | Best starting point for a learned compatibility scorer |
| [Zheng-Chong/CatVTON](https://github.com/Zheng-Chong/CatVTON) | CC BY-NC-SA 4.0 | 16 Dec 2025 | Vendors DensePose / detectron2 / SCHP; Gradio, FLUX and P2P apps | Not usable commercially |

### Inferences
- **Quick-win reuse:**
  - From wardrowbe: the `item_scorer.py` scoring factors and constants, the prompt templates (tagging, recommendation, pairing), the `resolve_outfit_items` / `fill_best_partial` validation, and the feedback signal design.
  - From vestiq: the mood-based colour-group harmony and the explainability enums.
  - From Libre-Closet: the mask-editor UX.
  - From owj0421: the compatibility model trained on Polyvore, subject to data-licence review.
- Security lessons visible in this code: never ship third-party API secrets in mobile bundles (ai-closet); always authenticate MCP endpoints (shimmer-strip); escape user content in server-rendered chat (Libre-Closet stub).

### Gaps
- Star counts for the ML repos were not collected. ai-closet's star count is from a third-party index.
- The Libre-Closet mobile wrappers and the closed wardrowbe Cloud edition were not reviewed. wardrowbe's mobile apps exist in the stores, but their source is not in this repository.

## Q10. Licence constraints and unit-cost cheat-sheet across the stack

### Takeaway
A commercially clean 2026 stack:
- **Cut-out:** OS segmenters (Apple, Google) plus BiRefNet (MIT) or U²-Net via rembg with pinned models.
- **Tagging:** FashionSigLIP (Apache-2.0) or FashionCLIP (MIT) plus a paid VLM API.
- **Recommendation:** own rules plus OutfitTransformer code (MIT), with dataset licence review.
- **Try-on:** FASHN VTON v1.5 (Apache-2.0) or paid APIs.

Avoid RMBG-2.0 (BY-NC), the IMG.LY JS library (AGPL) and all CC BY-NC-SA try-on models unless you license them.

### Cited Findings

| Area | Component | Licence | Unit cost / performance | Source |
|---|---|---|---|---|
| Cut-out | rembg code | MIT; README warns weights carry their own licences | — | [rembg README](https://github.com/danielgatis/rembg/blob/c5b48b0417a0254df7d461efa14573597f369a83/README.md#L533) |
| Cut-out | BiRefNet | MIT | ~58–69 ms FP16 at 1024² on 4090/A100 | [BiRefNet](https://github.com/ZhengPeng7/BiRefNet) |
| Cut-out | RMBG-2.0 | CC BY-NC 4.0 (commercial needs BRIA agreement) | — | [HF card](https://huggingface.co/briaai/RMBG-2.0) |
| Cut-out | @imgly/background-removal | AGPL-3.0 | — | [imgly](https://github.com/imgly/background-removal-js) |
| Cut-out | ML Kit Subject Segmentation | Beta, on-device | ~200 ms on Pixel 7 Pro | [ML Kit](https://developers.google.com/ml-kit/vision/subject-segmentation) |
| Cut-out | Photoroom API | Paid | $0.02/image | [Photoroom](https://www.photoroom.com/api/pricing) |
| Parsing | SCHP | MIT | — | [SCHP](https://github.com/GoGoDuck912/Self-Correction-Human-Parsing) |
| Parsing | segformer_b2_clothes | NVIDIA SegFormer licence ("other") | — | [HF card](https://huggingface.co/mattmdjaga/segformer_b2_clothes) |
| Parsing | cloth-segmentation | MIT, archived | — | [repo](https://github.com/levindabhi/cloth-segmentation) |
| Parsing | SAM 3 | SAM License (custom) | — | [Roboflow](https://blog.roboflow.com/what-is-sam3/) |
| Tagging | FashionCLIP | MIT | — | [repo](https://github.com/patrickjohncyh/fashion-clip) |
| Tagging | Marqo-FashionSigLIP | Apache-2.0 | — | [HF card](https://huggingface.co/Marqo/marqo-fashionSigLIP) |
| Compatibility | type-aware code | BSD-3 | — | [repo](https://github.com/mvasil/fashion-compatibility) |
| Compatibility | outfit-transformer | MIT | — | [repo](https://github.com/owj0421/outfit-transformer) |
| Try-on | CatVTON, IDM-VTON, OOTDiffusion, StableVITON, FitDiT | CC BY-NC-SA 4.0 | — | [CatVTON](https://github.com/Zheng-Chong/CatVTON), [IDM-VTON](https://github.com/yisol/IDM-VTON), [FASHN blog](https://fashn.ai/blog/comparing-the-top-4-open-source-virtual-try-on-viton-models) |
| Try-on | VITON-HD data | CC BY-NC 4.0 | — | [VITON-HD](https://github.com/shadow2496/VITON-HD) |
| Try-on | Leffa | MIT code, weights unstated | ~6 s/image on A100 | [Leffa](https://github.com/franciszzj/Leffa) |
| Try-on | FASHN VTON v1.5 | Apache-2.0 | — | [repo](https://github.com/fashn-AI/fashn-vton-1.5) |
| Try-on | Vertex `virtual-try-on-001` | Paid | ~$0.06/image (third-party trackers) | [docs](https://docs.cloud.google.com/vertex-ai/generative-ai/docs/models/imagen/virtual-try-on-001), [cloudprice](https://cloudprice.net/models/google-virtual-try-on-1) |
| Try-on | Gemini image models | Paid | $0.0336–$0.134 per 1K image; batch about half | [Gemini pricing](https://ai.google.dev/gemini-api/docs/pricing) |
| Try-on | Photoroom Plus | Paid | $0.10/image | [Photoroom](https://www.photoroom.com/api/pricing) |

### Inferences
- **Illustrative per-user cost** for a 150-item closet onboarding: 150 cut-outs on-device ($0), 150 VLM tagging calls (cost depends on model, not sourced here), and embeddings computed once. Try-on is the only feature with material recurring cost, at roughly $0.03–$0.13 per render via Google APIs. This argues for gating try-on behind a subscription and caching renders.
- Because most try-on research is trained on BY-NC datasets, "MIT code" does not imply "commercially safe model". Licence diligence must cover code, weights *and* training data.

### Gaps
- Licence terms for the U²-Net/IS-Net weights, the ATR/LIP/iMaterialist datasets, the Polyvore data, the DeepFashion2 form agreement, and SAM 3's exact commercial clauses were not verified.
- Measured on-device latency for Apple's foreground-instance mask and for FashionSigLIP on phones was not found.
