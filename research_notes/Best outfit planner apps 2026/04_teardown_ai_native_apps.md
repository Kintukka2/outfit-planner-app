# Teardown: the AI-native wave of outfit / styling apps (2024 – Oct 2026)

Scope: Alta, Google Doppl (+ Google's in-Search try-on that replaced it), Daydream, Style DNA, Gensmo, Doji, Pronti AI, plus other AI stylist / try-on entrants with traction or press by 25 Oct 2026 (ChatGPT try-on, Glance, Phia, Pinterest/Amazon/Zalando moves, Dressly, Essembl, shoppin, Aiuta, Atricent, Slidez). Whering, Acloset, Indyx, Stylebook, Cladwell, Smart Closet, Pureple, Combyne, Fits, OpenWardrobe, Save Your Wardrobe and GetWardrobe are covered elsewhere and appear here only where an AI-native app copies or competes with them.

Method and dating: App Store metadata (version history, IAP list, privacy "nutrition" labels, ratings) was pulled on 2026-10-25 from the iTunes Lookup API and the apps.apple.com product pages (US storefront). Screenshots were downloaded from `screenshotUrls` into the scratchpad (`.../scratchpad/clusterC/shots/`) and viewed directly. The 50–100 most recent US App Store reviews per app came from the public iTunes customer-review RSS feed. Google Play download bands came from play.google.com detail pages. Everything else is cited inline. Undated statements describe the state on 2026-10-25.

---

## 1. Landscape: which AI-native styling apps matter in Oct 2026, and where do they stand?

### Takeaway
Seven of the eight named apps are live. Doppl is the exception: Google shut it down on 30 Apr 2026 and moved its try-on into Search and Shopping. The category has split into two camps. **Closet-first stylists** (Alta, Pronti, Style DNA) generate outfits from clothes the user already owns. **Shopping-first agents** (Daydream, Gensmo, Doji, Glance, Phia) chat, try on, and earn commission. Alta is the clear traction leader in the first camp: 17.4k US ratings at 4.89, 700M outfits and 20M avatars generated, and a $20M Series A on 22 Oct 2026. The biggest platform event of 2026 was ChatGPT adding global virtual try-on on 1 Oct 2026.

### Cited Findings
**Snapshot table (US App Store unless noted, pulled 2026-10-25)**

| App | Company / HQ | First iOS release | Current version (date) | US ratings | Monetisation on iOS | Status |
|---|---|---|---|---|---|---|
| Alta Daily | Flagship AI, Inc. (NYC) | 2025-03-13 | 2.2.8 (2026-10-21) | 17,369 @ 4.89 | Free, no IAP listed | Active, weekly releases — [App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400), [Lookup](https://itunes.apple.com/lookup?id=6481705400&country=us) |
| Daydream | Dahlia Labs, Inc. (NYC) | 2025-11-17 (web beta 2025-06-25) | 1.30.1 (2026-10-22) | 79 @ 4.19 | Free, no IAP; affiliate commission | Active — [App Store](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357) |
| Style DNA | AI Style by DNA S.L. (Barcelona/London) | listing dates 2018-08-05 | 1.10.228 (2026-10-05) | 7,521 @ 4.29 | Subscriptions $7.99–$39.99 + one-off guides | Active — [App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821) |
| Gensmo | Serendipity One Inc. | 2024-09-10 | 3.10.3 (2026-01-14) | 1,760 @ 4.68 | Credits ($0.50/credit) | **No iOS update for ~9 months** — [App Store](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663) |
| Doji | Doji Labs, Inc (NYC) | 2025-05-06 | 1.1.5 (2026-10-23) | 169 @ 4.13 | Free, no IAP listed | Active — [App Store](https://apps.apple.com/us/app/doji-try-on-designer-fashion/id6737292650) |
| Pronti | Pronti AI Inc. (Canada) | 2021-03-30 | 2.50.29 (2026-09-09) | 242 @ 4.22 | Premium $6.99/mo, $74.99/yr, $124.99 lifetime + chat credits | Active — [App Store](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583) |
| Doppl | Google Labs | 2025-06-26 | — | — | Free | **Shut down 30 Apr 2026**; iTunes lookup for id 6741596720 now returns no result — [Google Labs on X](https://x.com/GoogleLabs/status/2036188820128071786), [Help](https://support.google.com/labs/answer/16537062) |

- Google Play download bands: Alta 500K+ (updated 7 Oct 2026) — [Play](https://play.google.com/store/apps/details?id=com.alta); Style DNA 1M+ — [Play](https://play.google.com/store/apps/details?id=style.dna.app); Pronti 500K+ — [Play](https://play.google.com/store/apps/details?id=com.prod.pronti); Glance 100K+ — [Play](https://play.google.com/store/apps/details?id=com.glance.ai). Gensmo's website claims "1M+ Downloads" — [gensmo.com](https://gensmo.com).
- Other AI-native apps with notable US rating counts (2026-10-25): Dressly (TIKVEX LIMITED) 32,648 @ 4.40 — [App Store](https://apps.apple.com/us/app/dressly-ai-outfit-maker/id6738174180); Phia 22,793 @ 4.77 — [App Store](https://apps.apple.com/us/app/phia-fashion-deals-rewards/id6739351340); shoppin' 13,645 @ 4.74 — [App Store](https://apps.apple.com/us/app/shoppin-fashion-made-for-you/id6738202299); Essembl 4,789 @ 4.54 — [Lookup](https://itunes.apple.com/lookup?id=6479642952&country=us); Glance 3,351 @ 4.82 — [App Store](https://apps.apple.com/us/app/glance-my-personal-shopper/id6742974181); Polly.com 3,477 @ 4.84 — [Lookup](https://itunes.apple.com/lookup?id=6755986009&country=us).
- Funding 2025–26 (consumer AI styling/try-on): Doji $14M seed (May 2025, Thrive), Alta $11M seed (Jun 2025, Menlo), Gensmo $60M seed (Jun 2025, investors not named), Phia $8M seed (Sep 2025) and $35M Series A (Jan 2026), Vêtir $5.5M Series A (May 2026), Whering $7M seed (Jul 2026, eBay Ventures + Google AI Futures Fund; Whering is covered by another researcher) — [Fashion Tech Funding Tracker](https://ftw.pi.tv/fashion-tech-funding/). Alta then raised a $20M Series A (Oct 2026) — [Fortune](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/). Daydream raised a $50M seed in June 2024 — [BoF](https://www.businessoffashion.com/articles/technology/julie-bornstein-new-startup-raises-50-million/).
- Casualties and pivots: Doppl closed (above). Aiuta's consumer "AI Stylist" app was replaced in Aug 2026 by "AIUTA Editorial", a B2B editorial-image generator ("Say goodbye to AI Stylist, and hello to editorial content creation"; IAP packs $28.99–$128.99, Studio Pro $138.99/mo) — [App Store](https://apps.apple.com/us/app/id6447555778). A Medium post reports that Alle, an AI fashion stylist app, burned $3M of seed money and announced a shutdown in 2026 (snippet only; not fetched) — [Medium](https://medium.com/illumination/alle-apps-3m-ai-fashion-stylist-dream-died-in-six-pivots-97a79c85a359).

### Inferences
- Rating volume is a rough traction proxy. On that basis Alta is the only closet-first AI-native app at scale, and Dressly and Phia are the scale players on the shopping and "analysis" side. Daydream and Doji have heavy press coverage but small iOS rating counts.
- Gensmo raised a very large seed, then shipped no iOS update after Jan 2026 while its website promotes "Gensmo Studio" for brands. It may have de-prioritised the consumer app. This is not confirmed.

### Gaps
- No public MAU/DAU for any app except company-reported totals (Alta's outfits and avatars, Daydream's "1.5M shoppers", Phia's "1.5M shoppers"). Sensor Tower-type download estimates were not available.
- Doppl's final App Store rating count could not be retrieved, because the listing is removed and Wayback returned HTTP 429.

---

## 2. Alta (Alta Daily: Digital AI Closet) — feature-level teardown

### Takeaway
Alta is the most complete AI-native "Clueless closet". It combines closet digitisation (photo + "Prettify", outfit-selfie decomposition, email receipts, catalogue lookup, and as of 25 Oct 2026 live-video "Closet Scan"), daily weather- and calendar-aware outfit generation, a prompt box ("ask Alta anything"), photoreal avatar try-on of up to 8 layered items in about 20s, a calendar log, trip packing, a social feed with "try this look on my avatar", and B2B try-on for brands. It is free to consumers. Its weak spots are avatar fidelity (regressions reported in Oct 2026) and AI hallucination during capture (items invented or recoloured).

### Cited Findings
**Identity, funding, status**
- Founder/CEO Jenny Wang, 28 at seed time. She is a Harvard-trained engineer, former investor, and was a DoorDash intern; she moved from SF to NYC to build Alta and "still actively coding every day" — [TechCrunch, 16 Jun 2025](https://techcrunch.com/?p=3017882).
- $11M seed led by Menlo Ventures (Amy Wu joined the board), with Benchstrength, Aglaé Ventures (Arnault family), Phenomenal Ventures and Anthology (Anthropic's fund). Angels include Tony Xu, Karlie Kloss, Jasmine Tookes, Jenny Fleiss and Manish Chandra. Meredith Koop (Michelle Obama's stylist) "helped train Alta's AI" — [TechCrunch](https://techcrunch.com/?p=3017882).
- $20M Series A pre-empted by Forerunner (Eurie Kim joins the board), reported 22 Oct 2026; Menlo and Aglaé returned. Users have generated **700M outfits and 20M avatars**. The team is 10 people across consumer and enterprise. Funds go to AI infrastructure and localisation — [Fortune](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/).
- Earlier milestones: "more than 100 million outfits generated since its 2023 launch" (Feb 2026) — [TechCrunch, 14 Feb 2026](https://techcrunch.com/?p=3092988); "over 10,000 avatars every single day" (Aug 2025) — [Alta blog](https://www.altadaily.com/blog/virtual-dressing-room); TIME Best Invention 2025 — [altadaily.com](https://www.altadaily.com); Cannes Lions Creative Data shortlist (Jul 2026) — [Alta blog](https://www.altadaily.com/blog/cannes).
- Platforms: iPhone, iPad, Apple-silicon Mac and Vision Pro (iOS 16.4+), Android (500K+ downloads), and a web app at altadaily.com. English only. Age rating 13+ ("User-Generated Content, Social Media") — [App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400), [Play](https://play.google.com/store/apps/details?id=com.alta), [Lookup](https://itunes.apple.com/lookup?id=6481705400&country=us).
- Positioning: "Your personal AI stylist that truly gets you … built around the clothes you already own … Alta is free to use." Onboarding style-goal chips on the website: "Plan my outfits better / Look professional at work / Expand my wardrobe / Evolve my style / Wear my clothes more" — [altadaily.com](https://www.altadaily.com).

**Wardrobe capture (most methods of any app in this set)**
- Channels: (1) forward email receipts, (2) add from the Alta product database, (3) take a photo and Alta finds the original product photo "if possible". AI removes backgrounds and auto-adds fabric and fit details — [App Store description](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).
- **Prettify**: removes the background and smooths wrinkles and shadows to produce an e-commerce-style image. "Upload a selfie of your outfit and our AI stylist will isolate each individual item … then Prettify each piece"; bulk Prettify is available during upload — [Alta blog, 28 May 2026](https://www.altadaily.com/blog/prettify). The App Store screenshot "Upload to your closet with a fit pic" shows a mirror selfie decomposed into catalogue-style items (Bottega Veneta sunglasses, Totême coat, Hermès bag) — [App Store screenshots](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).
- **Closet Scan™** (launched 25 Oct 2026): prop the phone up with the front camera, hold items up to it, and live video scans, Prettifies and loads them in real time. Alta claims about 100 items in 5 min and 300 in 15 min. Items are auto-tagged by dress code and season, and users are told to review them afterwards — [Alta blog](https://www.altadaily.com/blog/closet-scanner).
- TaskRabbit partnership (4 Nov 2025): users can book a Tasker to photograph their closet. Alta's AI converts the photos into "either the original product image, or e-commerce quality flatlays" — [Alta blog](https://www.altadaily.com/blog/taskrabbit).
- Founder statement on the tech: "Alta invented a proprietary computer language based on fashion taxonomy, as well as a generative multimodal model composed of 12+ submodels, all trained on in-house research and offline data." Wardrobe digitisation uses "computer vision and image segmentation" — [Alta/Cannes press release, Jul 2026](https://www.altadaily.com/blog/cannes). Fortune: "12 in-house AI models that work at both the individual item and complete outfit level" — [Fortune](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/).

**Core AI mechanics**
- Daily outfits on Home, based on closet, weather and schedule. Event generation ("date night in Paris", "work conference in Vegas"). Anchor-item generation ("Ask Alta to generate outfits with [that jacket]") — [App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).
- Search, "aka ask Alta anything": free-text briefs such as "festival outfit with converse" or "make my ex jealous outfit" return outfit suggestions, with a toggle into shopping mode — [Alta blog, Aug 2025](https://www.altadaily.com/blog/alta-main-features). Screenshot prompt pill: "Style me for a party this weekend!" returns named outfit cards ("Velvet Glamour", "Sequin Glamour", "Polished Office Look") — [App Store screenshots](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).
- Calendar context: a Google Calendar connection is read-only and reads chosen calendars' event title, time, location and type to "decide which events need an outfit, suggest a dress code, and create outfit recommendations". Event classification is done by service provider "TypeSafe" and outfit generation for events by **Google Cloud Vertex AI**. Neither may train on the data (policy effective 1 Jan 2026) — [Alta privacy policy](https://www.altadaily.com/privacy).
- Learning loop: ratings, feedback, saves and dismissals "train and refine your personal AI model … specific to your Account". Alta staff may manually review feedback and annotate data, and aggregated or de-identified data improves the service generally — [Alta privacy policy §2A](https://www.altadaily.com/privacy). Screenshot action row under each outfit: bookmark, edit, thumbs-down, share, plus a "Create Avatar" CTA — [App Store screenshots](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).
- "Closet mode" restricts recommendations to owned items. A 3-month daily user called suggestions "occasionally brilliant, sometimes atrocious", said they needed active training, and noted the app did not push shopping — [Technocouture Substack, 1 Apr 2026](https://technocouture.substack.com/p/reflections-from-my-clueless-closet).

**Avatar / virtual try-on**
- Inputs: a clear profile photo or headshot; an "About You" form (height, measurements, skin tone, hair colour "and more"); an optional full-body selfie. Tap "Avatar" next to any outfit to render it — [Alta blog, 20 Aug 2025](https://www.altadaily.com/blog/virtual-dressing-room). The privacy policy lists collected "age, height, and body shape" — [Alta privacy](https://www.altadaily.com/privacy).
- Capability claims from Alta's own comparison with Zara: about 20s render vs Zara's "over 2 minutes"; up to 8 items including layering vs Zara's 4; supports tops, bottoms, outerwear, dresses, bags, shoes and accessories (hats, sunglasses, scarves); also poses, props, "runway-to-avatar" try-ons, menswear and womenswear — [Alta blog, 18 Feb 2026 (vendor comparison)](https://www.altadaily.com/blog/alta-vs-zara). Wang repeated the 8-items-in-seconds vs Zara 4-items/2-minutes claim to TechCrunch — [TechCrunch, Feb 2026](https://techcrunch.com/?p=3092988).
- Quality, independent: a reviewer who tried the avatar several times rarely used it because "items would change in appearance on the avatar, or the fit simply wasn't matching how it fits in real life". They asked for a feedback step before regeneration — [Technocouture](https://technocouture.substack.com/p/reflections-from-my-clueless-closet).
- Quality, recent App Store reviews (Oct 2026): "The avatars and the way the cloths are being generated has dropped so much in quality … the avatars are so far from what they're trying to portray". Also: "it just made up half my wardrobe turned my skirt into a shirt changed things color … Even when I turned the 'prettify' option it still did it" — [US review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json). Overall the 100 most recent reviews are 87× 5★, 7× 4★, 3× 3★, 1× 2★, 2× 1★ (same feed).

**Planning, analytics, social, commerce**
- Calendar log: multiple looks per day and a month grid of avatar thumbnails. The screenshot shows a streak flame counter "312". Most- and least-worn items per month — [Alta blog](https://www.altadaily.com/blog/alta-main-features), [App Store screenshots](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).
- Trip planning: multiple cities and climates, activities and luggage size produce a packing list plus a travel lookbook, shareable by link. The screenshot shows "Paris, London – 6 days" with per-day activity columns and a "Packing list 41 items" broken down by category — [App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400), [Alta blog, Aug 2026](https://www.altadaily.com/blog/trip-planning).
- Analytics: most- and least-worn items and cost-per-wear — [App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).
- Social (2026): a Community feed of avatar outfits where every look can be tried on with your own avatar; tap any piece to see similar shoppable items; a weekly "Top 100" ranked by views, likes and try-ons; Followers whose logged outfits appear above your own daily suggestions; friends can view your closet live, comment, and restyle your outfit; profiles can be public, friends-only or private — [Alta blog, 12 Jun 2026](https://www.altadaily.com/blog/community). Release notes mention "Best Dressed" (Feb–Apr 2026), then "Explore the community, Alta friends" (May–Jun 2026), then "explore page" (Jul 2026 onward) — [App Store version history](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).
- Commerce: closet-gap shopping recommendations, a wishlist with sale notifications, and "Alta will show you how to wear a new piece by generating outfits with it" — [App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400). Poshmark integration (Oct 2025) surfaces secondhand designer bags in recommendations and on-avatar try-on — [Alta blog](https://www.altadaily.com/blog/poshmark).
- B2B: a "Style with Alta" button on Public School NYC product pages lets shoppers style the collection on their own Alta avatar. Partners also include Poshmark and the CFDA — [TechCrunch, Feb 2026](https://techcrunch.com/?p=3092988). Alta "now powers all AI within the Rent the Runway app", which saw a 35% engagement lift. Gap encourages try-on and sharing in-app, Zimmermann used Alta try-on data to gauge demand for production quantities, and e.l.f. Beauty has integrated — [Fortune](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/). The Zimmermann SS27 runway collection was live in Alta's Discovery tab "within minutes" of the 5 Oct 2026 Paris show, months before retail; users tap "Style on Avatar" to mix runway pieces with their own closet — [Alta blog, 25 Oct 2026](https://www.altadaily.com/blog/zimmermann).

**Pricing**
- No in-app purchases appear on the US App Store page, and the website says "Alta is free to use" — [App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400), [altadaily.com](https://www.altadaily.com). A reviewer: "it's totally free?? I'm shocked" (Oct 2026) — [review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json).

**Privacy**
- App Store label: Data Linked to You covers Location, Contact Info, User Content, Search History, Identifiers and Usage Data. Not Linked covers User Content and Diagnostics. No "Data Used to Track You" section — [App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).
- Uploaded photographs and body measurements are processed by AI Functions. Data is shared with "AI and machine learning service providers". Third-party advertising partners may set tracking technologies on the Services (interest-based ads). Account deletion is in Profile Settings — [Alta privacy](https://www.altadaily.com/privacy).

**Timeline (from App Store version history and press)**
- 2023 founded; 2025-03-13 iOS release; 2025-06-16 $11M seed; 2025-07 v1.2.0; 2025-08 features post (Daily outfits, Calendar Log, Trip Planning, Inspo Feed, Search, Closet, Wishlist, Lookbook); 2025-10-09 v1.3.0 "Event and trip planning, outfit inspo"; Oct 2025 Home Screen widget and Poshmark; Nov 2025 "Alta friends" and TaskRabbit; 2026-02-18 v2.1.10 "Best Dressed" and Zara comparison; Public School integration; Jun 2026 community/Top 100; Jul 2026 "explore page" and Cannes shortlist; 25 Oct 2026 Closet Scan and Zimmermann — [App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400), [Alta blog index](https://www.altadaily.com/blog), [TechCrunch](https://techcrunch.com/?p=3017882).

**UI (from the 9 US screenshots)**
- White, minimal editorial look: black "ALTA" wordmark, black pill CTAs, grotesk sans-serif, catalogue-style cut-outs on light grey tiles. Home: greeting ("Good afternoon, Jane"), weather (71°, H/L), a horizontal week date strip, occasion-titled outfit cards ("Chic Brunch", "Dimes Square Date") each showing a full-body avatar or flat-lay, and icons for calendar, help, notifications and search in the header. Closet: "Newest · 250 items", category chips (Tops, Dresses, Bottoms, Outerwear, Shoes), a 3-column grid with brand and product name, and Closet/Wishlist tabs. "Customize your looks": a dark-mode slot-machine editor with a horizontal carousel per category (Outerwear, Tops, Bottoms, Shoes) and an eye toggle to hide a layer. Calendar: a month grid of avatar thumbnails. Trip: day columns plus a packing list — [App Store screenshots](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400).

### Inferences
- Alta's moat is its capture pipeline (Prettify, outfit-selfie segmentation, Closet Scan, TaskRabbit) plus a fast multi-item layered avatar renderer. Both reuse as B2B products (Rent the Runway, Public School, Zimmermann). That suggests the consumer app is partly a data and distribution engine for an enterprise "personal identity layer", which is Wang's own phrase in [TechCrunch](https://techcrunch.com/?p=3092988).
- Alta directly copies or out-features classic closet apps: cost-per-wear and most-worn analytics, calendar logging and packing lists (Stylebook/Indyx/Whering territory). It adds generative capture and avatars, which those apps lack. The Substack reviewer explicitly switched from Whering to Alta because of Prettify — [Technocouture](https://technocouture.substack.com/p/reflections-from-my-clueless-closet).
- Because Prettify is generative, it can invent detail that isn't in the photo. The skirt-into-shirt complaint is a systemic risk of generative "cleanup" versus plain segmentation, and a product team should keep the original photo plus a "no-AI" toggle that really bypasses generation.

### Gaps
- Which foundation models sit behind the "12+ submodels" is not public. The only named provider is Google Vertex AI, for calendar-event outfits.
- Avatar inputs beyond profile photo, measurements and optional full-body selfie, how many generations a user gets, and whether avatars are re-rendered per outfit or cached are not documented.
- Revenue model: no consumer IAP is listed. Affiliate share, brand-deal pricing and B2B pricing are not disclosed.
- Size of the Alta product database and how the "find the original photo" retrieval works are not public.

---

## 3. Google Doppl (Google Labs) and Google's in-Search virtual try-on

### Takeaway
Doppl was Google Labs' experimental standalone try-on app (US-only, 18+, iOS and Android). It launched 26 Jun 2025: upload one full-body photo, try on any outfit image, and animate the result into a short video. In Dec 2025 it added an AI-generated shoppable video feed. Google shut it down on 30 Apr 2026 and folded the tech into try-on on product listings and image results across Google. Selfie-only try-on there is powered by Nano Banana (Gemini 2.5 Flash Image). Doppl is the clearest evidence that big platforms see try-on as a feature, not a standalone app.

### Cited Findings
**Identity and lifecycle**
- Launched 26 Jun 2025 by Google Labs as "an experimental app", on iOS and Android, US only. Built on Google Shopping's try-on (announced at I/O, May 2025) but rendered on "a virtual version of you" instead of diverse stock models — [TechCrunch, 26 Jun 2025](https://techcrunch.com/2025/06/26/google-launches-doppl-a-new-app-that-lets-you-visualize-how-an-outfit-might-look-on-you).
- 8 Dec 2025: a shoppable discovery feed launched with "AI-generated videos of real products", outfit recommendations from "a personalized style profile" (built from stated preferences plus items you interact with), and "nearly everything you see is shoppable, with direct links to merchants". US, 18+ — [Google blog](https://blog.google/technology/google-labs/discover-new-outfits-try-them-on-and-shop-from-doppls-new-discovery-feed/), [TechCrunch, 8 Dec 2025](https://techcrunch.com/2025/12/08/googles-ai-try-on-app-doppl-adds-a-shoppable-discovery-feed).
- Shutdown: Google Labs said the Doppl app "will officially shut down" and that users can "use our virtual try-on tech on product listings and image results across Google" — [Google Labs on X](https://x.com/GoogleLabs/status/2036188820128071786). The help page sets the date as 30 Apr 2026 and advises downloading or screenshotting saved items — [Google Labs Help](https://support.google.com/labs/answer/16537062) (via search summary). AppBrain reports App Store removal on 19 Apr 2026 — [AppBrain](https://www.appbrain.com/appstore/doppl-|-google/ios-6741596720). On 2026-10-25 the iTunes lookup for id 6741596720 returns zero results, and labs.google/doppl returns 404 — [Lookup](https://itunes.apple.com/lookup?id=6741596720&country=us), [labs.google/doppl](https://labs.google/doppl).
- The last Android build seen on APKMirror is dated 2026-01-07, with build string "labs-glam" — [APKMirror](https://www.apkmirror.com/apk/google-labs/doppl/).

**Mechanics**
- Onboarding input: one full-body photo. Outfit sources: photos or screenshots from anywhere (thrift store, a friend, social media, shopping sites). Output: a still image of you wearing it. Saved looks are browsable and shareable — [TechCrunch](https://techcrunch.com/2025/06/26/google-launches-doppl-a-new-app-that-lets-you-visualize-how-an-outfit-might-look-on-you).
- Video: converts the static try-on into an AI-generated motion clip — [TechCrunch](https://techcrunch.com/2025/06/26/google-launches-doppl-a-new-app-that-lets-you-visualize-how-an-outfit-might-look-on-you).
- Unsupported at launch: footwear, swimwear, lingerie and accessories — [AlternativeTo](https://alternativeto.net/news/2025/6/google-doppl-app-lets-users-try-on-outfits-with-ai-powered-image-and-video-previews/).
- Google's own disclaimer: "Fit, appearance and clothing details may not always be accurate" — [TechCrunch](https://techcrunch.com/2025/06/26/google-launches-doppl-a-new-app-that-lets-you-visualize-how-an-outfit-might-look-on-you).
- Hands-on reviews (launch period): The Verge said it "mostly works" but struggled to put pants on photos of the reviewer in shorts; another tester saw jeans rendered like "leg warmers" and AI-generated replacement feet — [hyper.ai summary](https://hyper.ai/en/stories/ab9bf7f7b0a8333d3790c3529cf74db7). Another review reported body distortion and weak layered looks, with output "overly influenced by what they were already wearing" — [Perfect Corp blog](https://www.perfectcorp.com/consumer/blog/ai-clothes/google-launches-doppl-for-virtual-try-on). Tom's Guide was positive about the animated previews — [Tom's Guide](https://www.tomsguide.com/uk/ai/i-just-tested-googles-doppl-app-that-lets-you-try-on-clothes-with-ai-and-it-blew-me-away).
- Pricing: free, no subscription — [hyper.ai summary](https://hyper.ai/en/stories/ab9bf7f7b0a8333d3790c3529cf74db7).

**Successor: try-on inside Google Search, Shopping and Images**
- May 2025 (Search Labs): try on clothes from product listings on a full-length photo of yourself using "a new custom image generation model" that "understands the human body and nuances of clothing — like how different materials fold, stretch and drape on different bodies" — [PCWorld](https://www.pcworld.com/article/2787760/googles-using-ai-to-allow-you-to-try-clothes-on-before-you-buy.html), [TechCrunch, 20 May 2025](https://techcrunch.com/2025/05/20/google-adds-ai-powered-shopping-features-for-discovery-and-easy-check-out).
- July 2025: rolled out broadly in the US across Search, Images and Shopping — [Digital Commerce 360](https://www.digitalcommerce360.com/2025/07/24/google-brings-ai-updates-to-its-virtual-try-on-tool/). Oct 2025: shoes added, with expansion planned to Australia, Japan and Canada — [Yahoo Tech](https://tech.yahoo.com/ai/gemini/articles/google-expands-virtual-try-shoes-105100275.html).
- 11 Dec 2025: selfie-only try-on (US). Nano Banana (Gemini 2.5 Flash Image) generates a full-body you from a headshot; the user picks their usual clothing size, chooses one of several generated images, and that becomes the default try-on photo. A full-body photo or diverse models remain as options — [TechCrunch, 11 Dec 2025](https://techcrunch.com/2025/12/11/googles-ai-try-on-feature-for-clothes-now-works-with-just-a-selfie/).

**UI**
- No screenshots could be retrieved because the App Store listing has been removed.

### Inferences
- Doppl ran about 10 months. Google tested an avatar-from-one-photo flow, video try-on and an AI-only shoppable feed, then kept only the parts that sit on its Shopping Graph. For independent apps this cuts both ways: generic "try on any screenshot" is now free inside Google and (since Oct 2026) ChatGPT, so standalone try-on has to win on closet integration, styling and social.
- Google's selfie → full-body synthesis, with a user-chosen size, is a cheap onboarding pattern that independents (Glance, Gensmo, Alta) have converged on.

### Gaps
- No Doppl usage numbers were ever published, and Google gave no stated reason for the shutdown beyond "transition".
- Whether Google's 2026 try-on reached non-US markets beyond the Oct 2025 announcement is unconfirmed.
- How Doppl stored body photos, and whether they were used for training, could not be verified from a primary source after the shutdown.

---

## 4. Daydream (Dahlia Labs) — chat-first AI fashion shopping agent

### Takeaway
Daydream is not a closet app. It is a commission-funded conversational fashion search engine (about 2–3M products, 10k+ brands) built on an "ensemble" of frontier models (OpenAI, Gemini) plus proprietary fashion components. Personalisation runs through a "Style Passport". In 2026 it added iOS 27 hooks (Siri search, on-device photo product labelling), Collections, "Suggest Less" negative feedback, quizzes, Browse mode, and a B2B "Powered by Daydream" on-site search. It has no try-on and no wardrobe.

### Cited Findings
**Identity**
- CEO and founder Julie Bornstein (ex-COO Stitch Fix; founded THE YES, sold to Pinterest in 2022). Co-founders Lisa Yamner Green (Chief Brands Officer), Dan Cary (CPO) and Richard Kim (CSO); CTO Maria Belousova. $50M seed (announced 2024) from Forerunner, Index, True Ventures and GV, plus Karlie Kloss — [PR Newswire, 25 Jun 2025](https://www.prnewswire.com/news-releases/daydream-launches-first-chat-based-shopping-agent-built-exclusively-for-fashion-302490581.html), [BoF](https://www.businessoffashion.com/articles/technology/julie-bornstein-new-startup-raises-50-million/).
- Public beta launched 25 Jun 2025 with about 2M products from 8,000+ brands and 200+ retail partners (Net-A-Porter, Mytheresa, Nike, Uniqlo, Khaite, Alo, Mejuri …), covering women's and men's fashion — [PR Newswire](https://www.prnewswire.com/news-releases/daydream-launches-first-chat-based-shopping-agent-built-exclusively-for-fashion-302490581.html). iOS app first released 17 Nov 2025 — [Lookup](https://itunes.apple.com/lookup?id=6692634357&country=us).
- Sep 2026: "more than 1.5 million shoppers, more than 325 retailers, and more than 10,000 brands", catalogue of "roughly 3 million products" — [TechCrunch, 14 Sep 2026](https://techcrunch.com/2026/09/14/fashion-discovery-app-daydream-uses-apple-intelligence-to-help-you-shop-the-outfits-saved-in-your-camera-roll/). The App Store description still says "10,000+ trusted brands and 2 million items" and cites TIME Best Inventions 2025 — [App Store](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357).
- iOS requires iOS 26.0+. Age 4+. English only — [Lookup](https://itunes.apple.com/lookup?id=6692634357&country=us).

**AI approach**
- An "ensemble" model that "integrates frontier models, including OpenAI and Google Gemini, plus select open-source and proprietary models". "Specialized components act as domain experts" in silhouette, fit, fabric and shopper behaviour. Results are ranked by relevance "rather than advertiser placement" — [PR Newswire](https://www.prnewswire.com/news-releases/daydream-launches-first-chat-based-shopping-agent-built-exclusively-for-fashion-302490581.html).
- Style Passport: "a dynamic profile that captures fit preferences, style signals, brand affinities, and price sensitivity", updated in real time from searches, clicks and saves — [PR Newswire](https://www.prnewswire.com/news-releases/daydream-launches-first-chat-based-shopping-agent-built-exclusively-for-fashion-302490581.html). Later framed as optional sizes, brands, style and budget — [TechCrunch, Sep 2026](https://techcrunch.com/2026/09/14/fashion-discovery-app-daydream-uses-apple-intelligence-to-help-you-shop-the-outfits-saved-in-your-camera-roll/).
- iOS 27 (Sep 2026): on devices with Apple Intelligence, choose a photo from the library and Daydream "identif[ies] and label[s] the products in it; tap one to search". Siri search ("Siri, search Daydream for white sneakers") — [App Store v1.28.0 notes](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357). It decomposes outfits (sweater, pants, shoes, accessories), links exact items when they're still sold or else similar ones, and handles variation requests ("I like this sweater but want it in red"). TechCrunch notes there is no continuous camera-roll scanning; only user-chosen images are analysed — [TechCrunch](https://techcrunch.com/2026/09/14/fashion-discovery-app-daydream-uses-apple-intelligence-to-help-you-shop-the-outfits-saved-in-your-camera-roll/).

**Feature timeline 2026 (App Store version history)** — [App Store](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357)
- 1.10 (16 Mar): more personal feed plus alerts. 1.11 (26 Mar): brand pages and favourite brands. 1.12 (20 Apr): redesign, "Chat now lives in one seamless scroll", new tab bar, video. 1.16 (21 May): promos while browsing, faster image search. 1.17 (28 May): taste set-up via favourite brands, sizes, price ranges and inspiration. 1.21 (24 Jun): "sparkle" button for similar items. 1.22 (1 Jul): favourite specific styles and colourways. 1.24 (10 Aug): Collections (shareable). 1.25 (12 Aug): long-press "Suggest Less", with an "Avoids" section in Style Passport to undo. 1.26 (15 Aug): iPad and landscape. 1.27 (27 Aug): style quiz in feed and a materials section. 1.28 (12 Sep): iOS 27, Siri, on-device image labelling, guest mode (favourites, collections and Style Passport without an account), brand/size/price/style quizzes. 1.30 (19–22 Oct): "iPhone Duo" multi-screen support, multi-window, Browse Mode (Shop tab by category and brand, hand-off to Chat), camera or Files photo search.
- 29 Jul 2026: "Powered by Daydream" puts the natural-language search on brands' own sites (pilot: STAUD, Alice + Olivia, Couper, Cult Mia, Hampden; 25+ more signed, including Anine Bing, Mansur Gavriel, Sandro, Maje) — [PR via Webull](https://www.webull.com/news/15310957041558528).

**Commerce, pricing, privacy**
- Free to merchants to onboard. Revenue is commission on completed sales, and purchase happens on the retailer's site — [PR Newswire](https://www.prnewswire.com/news-releases/daydream-launches-first-chat-based-shopping-agent-built-exclusively-for-fashion-302490581.html). "Try Daydream for free. Zero ads." No IAP — [App Store](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357).
- Privacy label: Data Used to Track You covers Identifiers and Usage Data; Linked covers Contact Info, Search History, Identifiers, Usage Data and Diagnostics — [App Store](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357). The policy allows hashed emails and phone numbers plus conversion data to be shared with ad partners "such as Meta and Google", and use of interactions to "improve our products, algorithms, and machine-learning models" — [Daydream privacy policy](https://daydream.ing/legal/privacy-policy).

**Quality and complaints**
- Recent reviews (26): 21× 5★ and 3× 1★. Complaints include "A poor substitute for Google … told … options were scarce, when I found literally dozens of options from a simple google search"; can't send a photo and a prompt at the same time (Oct 2026); "gives me the most random pieces … doesn't understand my style … throws the most expensive brands in my face" (Jun 2026); "30 days to delete account is absurd" — [review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6692634357/sortby=mostrecent/json).

**UI (8 screenshots)**
- Lavender-to-white gradients, a large serif display face (editorial "Vogue" feel), rounded white cards. Home: "Good evening, Kara" with an "Ask me anything" field, then "Your favorites" and "Your latest search" carousels. Profile: an archetype label ("FEMININE VERSATILITY"), a written style summary, and "DAYDREAM ANALYSIS – Your Style DNA" sliders (Boldness: Understated↔Statement-making; Maturity). Image search: a photo with hotspot labels (Oval Sunglasses, Crewneck Sweater, Striped Oxford Shirt, Wide Leg Pants, Leopard Ballet Flats), suggestion chips ("Style for Work", "Preppy Style Inspiration") and a "Chat to refine" bar. Chat: "Maxi dress for a Nantucket wedding" returns "dresses … that match your style passport" with refinement chips (Navy, Powder Blue, A-Line, Column). Share-sheet extension inside Instagram with tabs Daydream / ebay / LTK. Product page with conversational refinement chips ("More Like This", "Longer Handles", "Darker Colors"). Push notifications "Price Drop Alert" and "Shop The Sale" — [App Store screenshots](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357).

### Inferences
- Daydream shows the "AI stylist" without a closet: personalisation comes entirely from behaviour plus explicit Style Passport data. Its 2026 releases (quizzes, Suggest Less/Avoids, guest mode) are retrofitting classic recommender feedback controls because the pure-chat UX under-collected preference signal. Low-rating reviews about random or expensive picks are consistent with this.
- The B2B pivot ("Powered by Daydream") mirrors Alta's and Gensmo's enterprise moves. Consumer apps here look like showcases for licensable fashion AI.
- Naming collision: Daydream labels its profile analysis "Your Style DNA", the same name as the Style DNA app.

### Gaps
- Commission rate: one search summary claimed 20%, but I could not verify the source, so I treat it as unconfirmed.
- No Android app was found. Whether Daydream will add try-on or a wardrobe is not public.
- Which model handles which task in the ensemble is not public.

---

## 5. Style DNA — selfie-based colour/body/style analysis plus AI closet stylist

### Takeaway
Style DNA is a pre-generative-AI "image consultant" app (Barcelona, launched about 2022) that rebuilt itself around GenAI. A selfie gives a 12-season colour type, a body type and a Kibbe-style type in about 35 seconds. A GPT-4-based RAG chatbot answers styling questions. It generates 5 daily outfits from the closet, offers an in-store "item match check", and shops 26k brands filtered by profile. Its privacy policy names Google Gemini as its virtual try-on and text provider. Independent colour-accuracy testing is poor, and subscription and billing complaints dominate recent reviews.

### Cited Findings
**Identity**
- Launched 2022. Co-founder Elena Volkova. €3.4M pre-seed from Black River Ventures plus a crowdfunding round. 3.2M downloads, 300k+ active users and 70k paying subscribers (as of Jun 2024) — [TechCrunch, 21 Jun 2024](https://techcrunch.com/2024/06/21/style-dna-generative-ai-fashion-stylist-app). Welcome to the Jungle lists $1.3M (Mar 2021) and $1.6M (Aug 2023) seed rounds — [WTTJ](https://app.welcometothejungle.com/companies/Style-DNA). Based in Barcelona and London — [Mercado&Consumo, May 2026](https://mercadoeconsumo.com.br/22/05/2026/inovacao/style-dna-um-personal-stylist-com-ia-que-cria-um-perfil-de-estilo-a-partir-de-uma-selfie/).
- The App Store now claims "Trusted by over 3 million users" and is "built for women and men". Android has 1M+ downloads. The iOS listing's first release date is 2018-08-05, which conflicts with TechCrunch's 2022 launch date (possibly a reused older listing) — [App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821), [Play](https://play.google.com/store/apps/details?id=style.dna.app), [Lookup](https://itunes.apple.com/lookup?id=1358319821&country=us).
- Languages: German, Italian, French and Spanish were added (release notes from Apr 2025 onward); the lookup lists EN, DE, IT — [App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821).

**AI mechanics**
- A selfie produces a "full style profile … in 35 seconds": colour analysis (one of 12 seasonal types, e.g. Bright Spring, True Summer, Soft Autumn, Deep Winter), body type (hourglass, triangle, inverted triangle, rectangle, pear, apple, oval, plus-size), and style type (Romantic, Natural, Classic, Dramatic, Gamine; "Kibbe-inspired") — [App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821).
- The chatbot uses "ChatGPT 4.0" in a RAG-like design over trends, image-consultant expertise and "a dataset of millions of images". Recommendations are "based on an individual's appearance" rather than purchase history — [TechCrunch](https://techcrunch.com/2024/06/21/style-dna-generative-ai-fashion-stylist-app). Example prompts: "What goes with green cargo pants?", "Help me build a capsule wardrobe" — [App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821).
- Privacy policy (current) lists "ChatGPT / Open AI functions to enable chat functions" and "Google/Gemini – Our text communication and virtual try on provider" (linking the Gemini image-generation docs). It says "We may also create your personal avatar [up]on your request and virtually try on outfits you choose" — [Style DNA privacy](https://styledna.ai/privacy).
- Daily outfits: "5 ready-to-wear outfit ideas every day, styled by AI from clothes you already own". Item match check: photograph any item in a store to see whether it suits your colour and figure — [App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821).
- Shopping: "26,000 brands across 231 retailers (Amazon, H&M, Nordstrom, Old Navy, Urban Outfitters …)" filtered to colour, body and style type — [App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821).

**Accuracy**
- An independent studio-lit test by Kettlewell (a colour-analysis vendor, so commercially interested) found Style DNA assigned "True Summer" to a clearly warm-toned subject, calling it "completely incorrect". Its broader finding was that AI tools got the broad season right only about 50% of the time — [Kettlewell Colours](https://us.kettlewellcolours.com/blogs/the-color-blog/does-ai-color-analysis-work).

**Pricing and reviews**
- US IAPs: Monthly $7.99 / $9.99 / $19.99; 3 months $14.99 / $19.99; 6 months $29.99; Annual $29.99 / $39.99; one-off "Palette and Body type guide" $12.99; "Advanced Color Palette" $9.99 — [App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821).
- Of the 100 most recent reviews, 48 are 5★ and 33 are 1★. Recurring complaints: being charged after cancelling or charged more than expected ("charging me almost $30/month for a $5 monthly subscription"); paywall before seeing any value ("demand money before you can see if their ai process works"); repetitive "frumpy" looks; broken shop search — [review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1358319821/sortby=mostrecent/json).

**Privacy**
- Label: Data Used to Track You covers Usage Data; Linked covers Contact Info and Identifiers; Not Linked covers User Content, Usage Data and Diagnostics. Age 16+ with "Unrestricted Web Access, Advertising" — [App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821).
- "We do not store selfies that you upload, we store only the results based on such images analysis." Clothing photos are kept for about 24 months after last activity. Photos are encrypted to AWS or Google Cloud. The app accesses only user-selected images, not whole albums — [Style DNA privacy](https://styledna.ai/privacy).

**UI (6 screenshots)**
- Loud colour-blocked marketing panels (lavender, pink, blush) with heavy uppercase sans headlines. Face-landmark dots overlaid on a selfie, with three cards: "Your Style type Romantic-Natural", "Your Body type Triangle", "Your Color type Light Summer" plus swatches. Add-item sheet with Text Search / Photo library / Camera tabs. Outfits grouped by occasion ("Work 22 outfits", "Workout 14 outfits"). Chat flow "Check if it matches me" returns "100%, it's a Match!" with a 4-bar breakdown (Color / Shape / Print / Fabric) and "Matches items from My Wardrobe". A "How to style" item page with guidelines. "My shop" with store logos (Macy's, Mejuri, ASOS, JCPenney) and category list — [App Store screenshots](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821).

### Inferences
- Style DNA's distinctive mechanic is a **suitability score** (colour/shape/print/fabric match for a given item against the user's profile), not generation. It competes with standalone colour-analysis apps and with Dressly, which has the same selfie → colour type → outfit score loop and 4× the US ratings.
- The heavy pre-value paywall plus aggressive multi-tier pricing look like a monetisation-first funnel, and they generate the most negative review volume of any app studied here.

### Gaps
- Whether the Gemini avatar try-on is live for all users, and how many generations are allowed, is not documented.
- Current user and subscriber numbers beyond the "3 million users" marketing claim are not available.

---

## 6. Gensmo (Serendipity One Inc.) — "Super Shopping Agent" with avatar try-on, video and couple try-on

### Takeaway
Gensmo is a generative-first shopping toolbox. It offers Snap-to-Shop, avatar try-on from one photo, Style-an-Item, find dupes, walk/turn videos, couple try-on, a gift-list planner and a remixable social feed, all priced in credits at $0.50 each. Founder Ning Hu is ex-Google Mobile Search and ex-Alibaba VP. It raised $60M+ in June 2025. Its iOS app has not been updated since 14 Jan 2026, and recent reviews report regressions (face distortion, pose edits that don't work, generic outfits).

### Cited Findings
**Identity**
- Founder and CEO Ning Hu: former Google Mobile Search tech lead and Alibaba VP, CMU AI PhD. Other co-founders are unnamed ex-Google/Alibaba and fashion-industry people. Launched Dec 2024. Offices in NY, LA, the Bay Area and Seattle. Raised "over $60 million" (called "angel" in the headline and "seed" in the body; no investors named) on 27 Jun 2025 — [PR Newswire](https://www.prnewswire.com/news-releases/gensmo-secures-60m-in-angel-funding-to-reinvent-fashion-e-commerce-with-ai-302493059.html).
- Claimed beta stats: 10M+ style matches, 100M fashion products indexed — [PR Newswire](https://www.prnewswire.com/news-releases/gensmo-secures-60m-in-angel-funding-to-reinvent-fashion-e-commerce-with-ai-302493059.html). Website: "1M+ Downloads", plus "Gensmo Studio" ("Build studio-quality visuals from any product") and an Enterprise section — [gensmo.com](https://gensmo.com).
- iOS: released 2024-09-10, v3.10.3 on 2026-01-14 (no later release as of 2026-10-25), 242.5 MB, Shopping category, 13+ ("Advertising, Messaging and Chat, User-Generated Content") — [App Store](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663), [Lookup](https://itunes.apple.com/lookup?id=6636520663&country=us).

**AI mechanics**
- Virtual try-on: "Upload a photo to instantly generate a realistic AI avatar". A proprietary try-on model "trained on hundreds of thousands of looks". Personal mood boards use "deep learning, computer vision, and style-preference algorithms". "Vibe Imagine" generates scenes for a mood or trip — [App Store](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663), [PR Newswire](https://www.prnewswire.com/news-releases/gensmo-secures-60m-in-angel-funding-to-reinvent-fashion-e-commerce-with-ai-302493059.html). The website quotes press saying Gensmo "layers best-in-class AI models with proprietary technology" — [gensmo.com](https://gensmo.com).
- Feature list: Snap to Shop (photo → shoppable outfits matching vibe and budget); Style an Item (complete outfit around one piece); Animate Your Looks (walk or full-turn video); Couple Try-On (two photos); Xmas Gift List Planner (per-person AI gift picks with budget tracking); Discover & Share feed (remix looks, try on instantly); Shop by Brands; "Find Dupes" — [App Store](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663).
- Release history: 3.3–3.4 (Nov 2025) "Gensmo 3.0 … Super Shopping Agent … Virtual Try On, Style an Item, Find Dupes"; 3.4–3.5 (Nov 2025) Couple Try-On, Gift List, Outfit of the Day, Image Search; 3.6–3.10 (Dec 2025–Jan 2026) Snap to Shop, Animate Your Looks, personalised Home, and new onboarding "to show your style with personalized pictures" — [App Store version history](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663).
- Infrastructure sub-processors: Cloudflare, MongoDB Atlas, Google Cloud Platform, GitHub, Google Workspace (updated 5 Feb 2026). No AI model vendor is listed — [Gensmo sub-processors](https://gensmo.com/about/legal/sub-processors).

**Pricing**
- Credits: 10 = $5, 20 = $10, 30 = $15, 40 = $20, 60 = $30, 100 = $50 (flat $0.50/credit) — [App Store](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663). A reviewer complains about spending credits on pose edits that return the same pose, and about the move away from a "pro button" — [review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6636520663/sortby=mostrecent/json).

**Privacy**
- Label: everything collected is "Data Not Linked to You" (Contact Info, User Content, Identifiers, Usage Data, Diagnostics). No tracking — [App Store](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663).

**Quality**
- Recent reviews (52): 19× 5★, 15× 1★, 10× 2★, 8× 3★. Failure modes reported: "now EVERY time I use the generator it distorts the face of my avatar"; outfits only work "when every single piece is plain" (Jul 2026); "doesn't capture my face or body type accurately"; can't represent weight or height; the feed is "all women"; generic or repeated styles after updates; ignoring negative constraints ("the items I specifically told it not to") — [review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6636520663/sortby=mostrecent/json).

**UI (8 screenshots)**
- Off-white background, large serif marketing headlines, tool-grid home. Home header "Emily's OOTD" with location weather (San Jose 63.1°F/42.3°F), a prompt field ("black fitted bodysuit"), and tiles: Daily Try-On (HOT), Style an item, Couple Try-On, Find Similar, Animate your looks, Snap to Shop, Xmas Gift List Planner, Join IG Style Group. "Today's Try-On" theme card ("wedding dresses"). Bottom tabs: Discover / Style / Shop. Try-on result: the user's selfie inset beside a generated full scene (party) with the item strip and prices, plus "Shop" and "Post" buttons. Style-an-item: a chat bubble with "Agent mode details" and "Shopping list" links, an outfit collage and a "Try on" chip. Dupes: price-band chips. Outfit rationale text with follow-up suggestion chips ("Try a midi skirt instead of mini?", "Explore velvet tops under $200?"). Discover: "Editor's daily pick" with view counts. Brands grid with "NYFW SS26" filter. Product page with "Complete the look" and a "Buy Now" one-tap checkout (the screenshot shows prices in RM, Malaysian ringgit) — [App Store screenshots](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663).

### Inferences
- Gensmo is the most "feature-maximalist" generative app here: couple try-on, video and gift planning. Reviews suggest feature sprawl degraded reliability ("Glitchy with way too many features"). Together with the 9-month iOS update gap and the website's emphasis on Gensmo Studio and Enterprise, this points to a shift toward B2B content generation. This is an inference, not confirmed.
- Per-credit pricing at $0.50 makes compute cost visible to the user. It contrasts with Alta, Doji and Glance, which are free and unlimited (Glance claims on-device generation).

### Gaps
- Credit cost per action (try-on vs video vs edit) is not documented publicly.
- Investors behind the $60M round, current usage, and any 2026 company news: none found.
- Google Play listing and download band could not be located.

---

## 7. Doji (Doji Labs) — photoreal "AI likeness" try-on plus social closet for designer fashion

### Takeaway
Doji uses its own diffusion models to train a personal photoreal avatar from about 6 selfies and 2 full-body photos (about 30 minutes at launch). Users try on curated designer items or any product link, including via a browser extension, build a closet and wishlist, and post "looks" to an Instagram-like social feed with try-on counts. It is free with external checkout. The core complaint is that avatars are thinner, taller or unrecognisable, which defeats the point of fit assessment. Doji itself says fit simulation is not yet supported.

### Cited Findings
**Identity**
- Co-founders Dorian Dargan (CEO; ex-Apple visionOS, ex-Meta Oculus Quest) and Jim Winkens (CTO; ex-DeepMind researcher, Google GenAI consumer product). Founded 2024 in NYC. The name combines DOrian + JIm. $14M seed led by Thrive Capital with Seven Seven Six (May 2025). Inspired by Lensa: "what if we could do this for fashion, but in a photo-realistic way?" — [TechCrunch, 15 May 2025](https://techcrunch.com/2025/05/15/doji-raises-14m-to-make-virtual-try-ons-fun-through-ai-avatars).
- At launch the app was invite-only, available in 80+ countries — [TechCrunch](https://techcrunch.com/2025/05/15/doji-raises-14m-to-make-virtual-try-ons-fun-through-ai-avatars). The current App Store description does not mention invites — [App Store](https://apps.apple.com/us/app/doji-try-on-designer-fashion/id6737292650).
- iOS: released 2025-05-06, v1.1.5 (2026-10-23). Very frequent releases (about 25 builds Jul–Oct 2026, all "bug fixes"). 462.9 MB (largest app in this set). Requires iOS 18+. 13+ with "Infrequent Mature or Suggestive Themes / Sexual Content or Nudity" — [App Store](https://apps.apple.com/us/app/doji-try-on-designer-fashion/id6737292650), [Lookup](https://itunes.apple.com/lookup?id=6737292650&country=us).

**Onboarding and avatar**
- Six selfies plus two full-body images, optionally favourite brands. The avatar takes about 30 minutes, with a notification when ready. Users can retrain with a new image set — [TechCrunch](https://techcrunch.com/2025/05/15/doji-raises-14m-to-make-virtual-try-ons-fun-through-ai-avatars).
- "Proprietary diffusion models" generate the avatar and make try-ons realistic. Fit is not modelled: "It can't yet show how an item will fit" (on the roadmap). The TechCrunch author found some outputs made him look thinner or taller — [TechCrunch](https://techcrunch.com/2025/05/15/doji-raises-14m-to-make-virtual-try-ons-fun-through-ai-avatars).

**Mechanics**
- Try personalised items, or add products "from anywhere on the internet". Remix others' looks or style from scratch. The closet holds owned clothes, styled together with wishlist items, plus item recommendations. Shop directly. Follow friends, tastemakers and brands. A profile with saved looks. A "Doji Extension" offers try-on while browsing any fashion website, wishlist saving, and similar items "across thousands of brands" — [App Store](https://apps.apple.com/us/app/doji-try-on-designer-fashion/id6737292650).

**Privacy and pricing**
- Label: Linked covers Contact Info and User Content; Not Linked covers Contact Info and Identifiers. No tracking. No IAP listed — [App Store](https://apps.apple.com/us/app/doji-try-on-designer-fashion/id6737292650). doji.com/privacy returned "This content isn't available" on 2026-10-25 — [doji.com/privacy](https://doji.com/privacy).

**Quality**
- Recent reviews (39): 19× 5★, 7× 1★, 5× 2★, 6× 3★. Complaints: "makes everyone skinnier"; "a 6'5" shredded model. I am 5'10" and fat"; "in each try on the person is different. Sometimes it puts me as a man sometimes a woman"; garment pasted on with the "tag in the back in the front"; an Aug 2026 update made the avatar "more saturated … aggressive face … skinnier and more tanned"; link parsing of online items fails ("Until link parsing … works, it's kind of useless"); requests for measurements, height and weight inputs and for accessories (socks, necklaces, rings) — [review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6737292650/sortby=mostrecent/json).

**UI (6 screenshots)**
- Gallery-white, fashion-editorial minimalism with a thin serif. Full-bleed cut-out avatar on white with caption "jlom wearing Our Legacy, Isabel Marant, Viktor & Rolf". Closet/Wishlist tabs with "Style" and "Select" buttons and a 3-column grid of luxury items (Maison Margiela, Rick Owens, Saint Laurent). Home "Doji" feed with an Instagram-style stories row and avatar looks captioned "ninar in Balenciaga, Saint Laurent and more · 1h". Create Look: a 2-column grid with per-item lock icons and "Style Me" / "Try On" buttons (lock items, let AI fill the rest). Profile "half_remembered – 102 Looks, 32.2k Try-Ons" with tabs Looks / Closet / Wishlist. Product page "Maison Margiela Cognac Frye Leather Boots – 24 Try Ons · In 4 Closets", a "Try On" CTA, retailer row (1stDibs $730), size picker (38 IT) and "Buy Now ↗". Floating bottom bar: Home, Search, +, Hanger (closet), Avatar — [App Store screenshots](https://apps.apple.com/us/app/doji-try-on-designer-fashion/id6737292650).

### Inferences
- Doji's "Lensa for fashion" approach (fine-tuning on the user's photos) maximises face likeness and aesthetic appeal, but it inherits the base model's idealised body priors. That explains the systematic "skinnier and taller" complaints. A rebuild should add body-measurement conditioning or a body-shape-preserving pipeline.
- The social mechanics ("Try Ons" counters, "In N Closets", stories) make try-on a content format more than a fit tool. This aligns with Alta's Top 100 and Gensmo's remix feed.

### Gaps
- Current avatar generation time, monetisation (affiliate rates or planned in-app checkout) and invite status in 2026 are not public.
- Doji's photo retention and training policy could not be read, because the privacy page was unavailable.

---

## 8. Pronti AI — closet-grounded AI stylist chat ("PAI") on a classic wardrobe app

### Takeaway
Pronti is a 2021-era closet and outfit-generator app (Canada, founder Mila Banerjee) that relaunched around "PAI", a closet-aware chat stylist (GA Feb 2026). It added OOTD, trip packing, week planning, weather and mood/occasion filters in 2026. It has a freemium model: Premium plus metered PAI chat credits. Capture covers camera, gallery, web images, full-outfit selfie and forwarded receipts (including Gmail). The weak point is AI tagging accuracy and nonsense outfit combinations reported by users.

### Cited Findings
**Identity**
- Founder and CEO Mila Banerjee, who started it after struggling to pick outfits for meetings — [Accelerator Centre](https://acceleratorcentre.com/resources/success-stories/founder-spotlight-mila-banerjee-ceo-of-pronti-ai). Founded 2021; CB Insights lists Smyrna, Delaware, while other profiles say Canadian-developed — [CB Insights](https://www.cbinsights.com/company/pronti). The privacy policy says "Pronti is based in Canada" — [Pronti privacy](https://www.pronti.com/legal/privacy/).
- iOS first release 2021-03-30, v2.50.29 (2026-09-09), 242 US ratings @ 4.22, 16+. Android "Pronti – Outfit Maker" 500K+. Screenshot claims "2M+ downloads". Release notes (May 2026) list Danish, Finnish, German, English, Spanish, French, Italian, Dutch, Polish, Turkish and Portuguese — [App Store](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583), [Play](https://play.google.com/store/apps/details?id=com.prod.pronti).

**Capture**
- Take a photo, use gallery, add from store images or Google, take a selfie, or forward purchase emails — [App Store](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583). The screenshot add-sheet lists Take a Picture / Use your Gallery / Find an online image / Head to Toe Selfie / Forward a Purchase / Try the Sample closet. An optional Gmail/Google connection locates receipt emails and pulls item images — [Pronti privacy §8](https://www.pronti.com/legal/privacy/). 2026 updates added fine-tune cropping (Jan), bulk colour correction (Mar), colour/pattern closet filter (Apr), a revamped colour system acknowledging AI colour errors (Jun) and a tag-correction icon (Jul) — [App Store version history](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583).

**AI mechanics**
- PAI "isn't a generic fashion chatbot. It understands your closet items, saved outfits, and style history". Example asks: "What can I wear today with my black blazer?", "Help me pack outfits for a weekend trip" — [App Store](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583).
- PAI timeline: early release and chat history as Premium (14 Feb 2026); "PAI is officially here!" with chat history, "quick context buttons" and +20 credits per referral (16 Feb); OOTD and chat about OOTD (6 Mar); trip packing (30 Mar); week planning (7 Apr); OOTD reminders and season/temperature filter (27 Apr); weather on the Find Outfits banner (21 May) — [App Store version history](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583).
- Feeds: "Community" (others' outfits), "Just Mine" (generated only from your closet) and "Make Your Own"; an "All Closet Mashup" — [App Store screenshots/notes](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583). Learning signals: saving, planning or wearing outfits and the thumbs-down; the outfit catalogue draws on "1000+ brands" — [pronti.com](https://www.pronti.com).
- Privacy policy: PAI processes messages, attached images, closet items, diary records and outfit history. Automated and manual review of AI I/O. De-identified data is used to improve AI. Images are used to "identify clothing items, colours, patterns, categories". "AI model or API providers" are service providers, but none is named. Also covers a sister app, "Selfie Outfit Diary / Diary Selfie" (revised 20 May 2026) — [Pronti privacy](https://www.pronti.com/legal/privacy/).

**Pricing**
- Premium Monthly $6.99, Yearly $74.99, Lifetime $124.99; PAI chat credits 25 = $1.99, 50 = $2.99, 150 = $6.99. "Free to use and supported by shoppable links, ads, and optional Premium features" — [App Store](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583). The screenshot shows a "57 Credits" counter in the PAI header; Premium gives "unlimited closet space" — [App Store screenshots](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583).

**Privacy label**
- Data Used to Track You covers Identifiers and Usage Data. Linked covers Purchases, Location, Contact Info, User Content, Identifiers and Usage Data — [App Store](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583).

**Quality**
- Of the 79 most recent reviews, 29 are 1★ and 13 are 5★. Users report AI mis-tagging ("tagged jackets as accessories, dresses as tops, pants as jackets"), nonsense combinations ("2 jackets and no pants, a formal dress and slippers, a summer dress with a winter beanie"), a closet count wrong (141 vs about 36), freezes, and slow support. Most of these predate the Feb 2026 PAI launch — [review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1560004583/sortby=mostrecent/json).

**UI (10 screenshots)**
- Teal and peach gradient marketing with a rounded geometric sans. Home "Let's find you an outfit, Kiera" with Community / Just Mine / Make Your Own tabs. Flat-lay outfit card with thumbs-down, chat, bookmark and a floating PAI star button. PAI chat with a credit counter, history and filter icons, and suggested prompts ("I always wear black – how do I change that up?"). Closet with tabs (All Closet / Unorganized / Casual / Office), "56 items – 100% of closet" and collapsible categories. Filter screen: Explore vs Just Mine, weather card (25°/18° Sunny), the day's schedule (6:30 Yoga Zoom, 11:30 Coffee meeting, 7:00 Dinner w/ client), mood picker (Happy / Sassy / Calm), "What's the occasion?" and "See Outfits With My Clothes". Gender slider (Female–Neutral–Male). Share outfit to Community or other apps. Bottom bar: search, calendar, +, bookmark, closet — [App Store screenshots](https://apps.apple.com/us/app/outfit-maker-pronti-ai/id1560004583).

### Inferences
- Pronti shows the "retrofit" path: a classic closet app that bolts on an LLM chat layer grounded in the user's closet, monetised with metered credits. The pattern competes directly with Cladwell/Whering/Acloset-style AI stylists. Its mood + schedule + weather filter screen is an unusually explicit context UI.
- Recognition and tagging quality, not the chat, remains the bottleneck for closet-first AI. Users who correct dozens of tags quit.

### Gaps
- PAI's underlying LLM provider is not named anywhere public.
- How many free PAI messages a user gets before credits are needed is not documented.

---

## 9. Other AI stylist / try-on entrants and platform moves (by Oct 2026)

### Takeaway
The most consequential new entrant is a platform: **ChatGPT added global virtual try-on on 1 Oct 2026** (Images 2.5, selfie or full-body photo, try-on button in shopping results). Glance (InMobi) offers free on-device try-on via Apple's Core AI plus Siri. Phia (deal-finding, $185M valuation) added a camera-roll-to-closet feature and try-on in 2026. Google kept try-on inside Search. Pinterest and Zalando are moving toward AI styling without a confirmed consumer clothing try-on. Dressly is a high-volume selfie/colour/outfit-score subscription app with heavy billing complaints.

### Cited Findings
**ChatGPT (OpenAI)**
- 1 Oct 2026 "global launch" of virtual try-on and Favorites. A "Try On" button sits on clothing and accessory results. Inputs are a selfie or full-body photo, and users can upload an item screenshot and ask ChatGPT to try it on. Saved items go to the Library alongside try-on images. Powered by "ChatGPT Images 2.5" (more natural lighting and textures, lower latency). It follows OpenAI's retreat from Instant Checkout — [TechCrunch, 1 Oct 2026](https://techcrunch.com/2026/10/01/chatgpt-can-now-virtually-try-on-clothes-for-you/).
- Reported privacy terms: the reference photo is retained for reuse (Settings → Personalization → Reference photos), and personal-account images can be used for training unless the user opts out. OpenAI's disclaimer says try-ons "do not guarantee fit or size" — [Technology.org](https://www.technology.org/2026/10/02/chatgpt-virtual-try-on-clothes-images-2-5/), [TechAIWire](https://techaiwire.com/articles/chatgpt-virtual-try-on-images-2-5-favorites/) (secondary; OpenAI help page not fetched).

**Glance – My Personal Shopper (InMobi)**
- A personal-shopper feed across fashion, beauty, home, hair and eyewear. "Generates styled try-ons entirely on your device — as fast as 30 seconds, unlimited, and free". You can add a photo "to see yourself in it". The feed is contextual (weather, trends, occasions, location). "Hey Siri, Let's Glance" works through Siri AI (iOS 27). Multi-item outfit try-on, widget and iMessage — [App Store](https://apps.apple.com/us/app/glance-my-personal-shopper/id6742974181).
- On-device try-on uses Apple's Core AI framework and works offline, rolling out to about 60M eligible devices mainly in the US — [Webull/PR](https://www.webull.com/news/15667120985031680), [LetsDataScience](https://letsdatascience.com/news/glance-integrates-ai-shopping-experience-with-siri-5f5006b0). Feb 2025 partnership to use Gemini and Imagen via Vertex AI — [Google Cloud Press](https://www.googlecloudpresscorner.com/2025-02-26-Glance-and-Google-Cloud-Join-Forces-to-Build-Consumer-Generative-AI-Experiences-for-Both-Smartphone-Lock-Screens-and-Ambient-TV-Screens). PayPal native checkout (Oct 2026) — [Benzinga](https://www.benzinga.com/markets/tech/26/10/62417892/paypal-teams-up-with-glance-to-turn-ai-shopping-discovery-into-instant-purchases). Android TV try-on in the US, India and Japan (Oct 2026) — [Chain Store Age](https://chainstoreage.com/news-briefs/2026-10-22?article=google-integrates-ai-commerce-android-tvs).
- Privacy label includes "Sensitive Info" linked to you, plus tracking via Identifiers. Rated 18+ — [App Store](https://apps.apple.com/us/app/glance-my-personal-shopper/id6742974181). One review warns its terms allow selling data "including your pictures" to partners (unverified user claim) — [review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6742974181/sortby=mostrecent/json).
- Screenshots: purple gradient; "Understands Me and My World" orbit diagram (Trends, Location, Weather, Occasion, Style Preferences, "Face analyzed"); Siri result card showing Myntra products priced in ₹; a shoppable feed starring the user's generated likeness with product callouts; tiles "Find the best sunglasses for my face", Hairstyles, "Combine Products"; "Shops From 40 Million+ Products"; "Try It On Me" grid — [App Store screenshots](https://apps.apple.com/us/app/glance-my-personal-shopper/id6742974181).

**Phia (Phoebe Gates & Sophia Kianni)**
- $35M Series A led by Notable Capital (Jan 2026) at about $185M valuation, after an $8M seed led by Kleiner Perkins (Sep 2025) — [TechCrunch, 27 Jan 2026](https://techcrunch.com/2026/01/27/phoebe-gates-and-sophia-kiannis-phia-raises-35m-to-make-shopping-fun-again/), [Fortune](https://fortune.com/article/phoebe-gates-startup-phia-succeed-without-parents-help-bill-gates-melinda-french-gates/).
- Core is a Safari/Chrome price-comparison agent across 220k+ sites and 350M items, with coupons, rewards and price drops. 2026 adds a closet feature: Closet opened to all (Aug 2026); Closet tab and multi-select outfit builder (Sep 2026); "Find outfits in your camera roll and bring the pieces into your digital closet" and pinch-to-zoom "virtual try-on images" (22 Oct 2026) — [App Store](https://apps.apple.com/us/app/phia-fashion-deals-rewards/id6739351340).
- Unverified allegation: a ColdFusion-sourced report claims revenue fell after Phia disabled "cookie-stuffing" features — [BigGo Finance](https://finance.biggo.com/news/512bdfc4a8d48d6a).

**Google, Pinterest, Amazon, Zalando**
- Google: see §3 (Search/Shopping/Images try-on, selfie via Nano Banana).
- Pinterest: no consumer clothing try-on was found for 2026. AI moves were "Styled for you" outfit collages from saved fashion Pins (US/Canada test) — [Dig.watch](https://dig.watch/updates/ai-brings-a-personal-stylist-to-pinterest-users); Restyle for rooms (Sep 2026) — [TechCrunch](https://techcrunch.com/2026/09/17/pinterest-teases-a-new-restyle-feature-that-lets-you-redesign-your-room-with-ai/); Beauty Guides (Oct 2026) — [TechCrunch](https://techcrunch.com/2026/10/06/pinterests-ai-now-turns-beauty-pins-into-action-plans/).
- Amazon: a virtual try-on hub covers clothing, shoes and jewelry — [Amazon](https://www.amazon.com/b?ie=UTF8&node=23595320011). AWS sells virtual try-on to brands via Nova Canvas — [AWS blog](https://aws.amazon.com/blogs/aws/amazon-nova-canvas-update-virtual-try-on-and-style-options-now-available/). No dedicated 2026 Amazon consumer styling app was found.
- Zalando: a Mar 2026 corporate AI deck lists a "conversational shopping assistant beta launch" and "virtual try-on" as milestones, without dates — [Zalando deck](https://corporate.zalando.com/sites/default/files/media-download/zalando-se_shaping-the-future_ai-and-zalando.pdf).

**Indie / smaller AI-native apps**
- Dressly (TIKVEX): a selfie gives colour analysis, body shape and an "Outfit Scoring" (style, fit, colour harmony) plus lookbooks. Subscriptions are $7.99–$11.99/week, $24.99–$34.99/month and $55.99–$79.99/year, plus a $9.99 "Farb-Scanner" add-on. The privacy label tracks Location — [App Store](https://apps.apple.com/us/app/dressly-ai-outfit-maker/id6738174180). Recent reviews are dominated by billing and cancellation complaints ("charged after canceling", "two charges of $49.99") — [review RSS](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6738174180/sortby=mostrecent/json).
- Essembl (seller: Essembl SA): camera-roll clothing detection ("Essembl can now find your clothes in your photos", Sep 2026). Redesigned 18–20 Oct 2026 into Today / Studio ("Rate me" score) / Rate my look (selfie tips and add-to-wardrobe) / Shop (wardrobe gaps styled with own clothes). Premium AI up to $59.99 and "MAX Access" tiers — [App Store](https://apps.apple.com/us/app/id6479642952).
- shoppin' (India): vibe search, universal search from pins, screenshots and links, virtual try-on, then made-to-measure garments tailored in India, with Apple Pay express checkout — [App Store](https://apps.apple.com/us/app/shoppin-fashion-made-for-you/id6738202299).
- Atricent: AI stylist, closet, try-on and community; "Wardrobe Video Scan" added 19 Oct 2026; Atricent+ $7.99/mo or $69.99/yr — [App Store](https://apps.apple.com/us/app/id6448105847).
- Closetly (AnswersAI): subscription-gated closet, avatar with waist-line setting; Monthly $7.99, Yearly $59.99; last update Jan 2026 — [App Store](https://apps.apple.com/us/app/id6744034749).
- Slidez (Z0 Inc, launched Aug 2026): occasion-driven AI stylist with try-on, small traction (6 ratings) — [App Store](https://apps.apple.com/us/app/id6777857331).

### Inferences
- Live-video or camera-roll closet ingestion became table stakes in Sep–Oct 2026 (Alta Closet Scan, Atricent Video Scan, Essembl and Phia camera-roll detection, Daydream on-device labelling). Any new outfit-planner app should plan for bulk, low-effort capture.
- ChatGPT and Google now give away try-on-from-a-screenshot. Standalone try-on apps (Doji, Gensmo, Doppl-style) need closet context, social and brand-exclusive content (e.g. Alta's runway drops) to justify themselves.

### Gaps
- No primary OpenAI help-page text was fetched for ChatGPT's photo retention and training opt-out.
- Snap and Amazon specific 2026 consumer styling launches: none found.
- Glance's model stack for on-device try-on (beyond "Core AI") is not public.

---

## 10. Cross-cutting: AI approaches, output quality, failure modes, privacy and pricing patterns

### Takeaway
Three technical stacks recur. (1) **LLM stylist over a structured closet** (Pronti PAI, Style DNA on GPT-4 RAG, Alta via Vertex AI for events). (2) **Diffusion/image-model try-on on a personal avatar**, either fine-tuned per user (Doji), synthesised from one photo/selfie (Gensmo, Google Nano Banana, Glance on-device, ChatGPT Images 2.5), or a proprietary multi-submodel stack (Alta). (3) **Vision decomposition** for capture and shopping (Prettify, outfit-selfie segmentation, screenshot-to-products). The dominant failure modes are identity and body-shape drift (thinner, taller, saturated faces), hallucinated garments during "cleanup", poor layering, wrong colour or category tags, and recommendations that ignore negative constraints. Privacy disclosures range from "selfies not stored" (Style DNA) to "photos used to train unless opted out" (ChatGPT, reported).

### Cited Findings
- Avatar inputs compared: Doji uses 6 selfies + 2 full-body photos and trains about 30 min — [TechCrunch](https://techcrunch.com/2025/05/15/doji-raises-14m-to-make-virtual-try-ons-fun-through-ai-avatars); Alta uses headshot + measurements + optional full-body selfie — [Alta blog](https://www.altadaily.com/blog/virtual-dressing-room); Doppl used one full-body photo — [TechCrunch](https://techcrunch.com/2025/06/26/google-launches-doppl-a-new-app-that-lets-you-visualize-how-an-outfit-might-look-on-you); Google Search uses a selfie + clothing size, picking one of several generated bodies — [TechCrunch](https://techcrunch.com/2025/12/11/googles-ai-try-on-feature-for-clothes-now-works-with-just-a-selfie/); Gensmo uses one photo — [App Store](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663); ChatGPT uses a selfie or full-body photo — [TechCrunch](https://techcrunch.com/2026/10/01/chatgpt-can-now-virtually-try-on-clothes-for-you/).
- Speed claims: Alta about 20s (vendor) vs Zara over 2 min — [Alta blog](https://www.altadaily.com/blog/alta-vs-zara); Glance "as fast as 30 seconds" on-device — [App Store](https://apps.apple.com/us/app/glance-my-personal-shopper/id6742974181); Google try-on about 30s per a CNET test (as summarised) — [PCWorld](https://www.pcworld.com/article/2787760/googles-using-ai-to-allow-you-to-try-clothes-on-before-you-buy.html).
- Body-shape drift across apps: Doji ("makes everyone skinnier", "taller", gender flips) — [Doji reviews](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6737292650/sortby=mostrecent/json); Gensmo (face distortion, can't do weight or height) — [Gensmo reviews](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6636520663/sortby=mostrecent/json); Alta (quality regression, fit mismatch) — [Alta reviews](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json), [Technocouture](https://technocouture.substack.com/p/reflections-from-my-clueless-closet); Doppl (body distortion, shorts/pants problems) — [Perfect Corp](https://www.perfectcorp.com/consumer/blog/ai-clothes/google-launches-doppl-for-virtual-try-on), [hyper.ai](https://hyper.ai/en/stories/ab9bf7f7b0a8333d3790c3529cf74db7); Glance ("heads cut off, change the face") — [Glance reviews](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6742974181/sortby=mostrecent/json).
- Every vendor disclaims fit: Google ("Fit, appearance and clothing details may not always be accurate") — [TechCrunch](https://techcrunch.com/2025/06/26/google-launches-doppl-a-new-app-that-lets-you-visualize-how-an-outfit-might-look-on-you); Doji ("can't yet show how an item will fit") — [TechCrunch](https://techcrunch.com/2025/05/15/doji-raises-14m-to-make-virtual-try-ons-fun-through-ai-avatars); OpenAI ("do not guarantee fit or size", reported) — [Technology.org](https://www.technology.org/2026/10/02/chatgpt-virtual-try-on-clothes-images-2-5/).
- Colour analysis is unreliable from selfies: about 50% broad-season accuracy in one test, and Style DNA got temperature wrong — [Kettlewell](https://us.kettlewellcolours.com/blogs/the-color-blog/does-ai-color-analysis-work).
- Pricing patterns: free with commerce or B2B (Alta, Daydream, Doji, Glance, Phia); per-generation credits (Gensmo $0.50/credit; Pronti chat credits $1.99–$6.99); subscription-gated analysis (Style DNA $7.99–$39.99; Dressly up to $34.99/mo or $11.99/wk) — App Store pages cited in §§2–9.
- Privacy-label patterns: Gensmo marks everything "Not Linked"; Alta links User Content and Location but declares no tracking; Daydream, Pronti, Style DNA, Dressly and Glance declare "Data Used to Track You" — App Store pages cited in §§2–9.

### Inferences
- To be rebuildable, a modern AI outfit app needs at least four model components: (a) garment segmentation and catalogue-style regeneration, ideally with an option to keep the raw photo; (b) an attribute tagger (category, colour, pattern, season, dress code) with an easy correction UI; (c) a constrained outfit generator (LLM or ranking model over closet items plus weather, calendar and occasion), with negative-feedback memory ("Suggest Less", thumbs-down); (d) an optional try-on renderer conditioned on identity *and* body measurements. Most failures cluster in (a) and (d).
- Body-measurement conditioning is the clearest unmet need. Users repeatedly ask for height and weight inputs, and only Alta and Google's size picker gesture at it.

### Gaps
- No public benchmark compares try-on fidelity across these apps. Every speed or quality comparison found is vendor-authored (Alta vs Zara).

---

## 11. What is genuinely new vs. classic digital-wardrobe apps, and where is the category heading?

### Takeaway
New in this wave: (1) generative capture (Prettify, outfit-selfie and live-video scanning, camera-roll detection) that collapses the closet-entry chore; (2) natural-language briefs and stylist chat grounded in the user's closet, calendar and weather; (3) photoreal personal-avatar try-on, including runway pieces before release; (4) social "try someone's look on me" loops. The direction of travel by Oct 2026 is agentic commerce and B2B licensing. Startups license their avatar and styling stack to brands (Alta → Rent the Runway, Public School, Zimmermann; Daydream → brand sites; Gensmo Studio). Platforms (Google, OpenAI, Apple Siri/Core AI) absorb generic try-on and search. Independents survive on closet context and community.

### Cited Findings
- Alta's stated vision is to become the "personal identity layer for the future of consumer AI and shopping" — [TechCrunch, Feb 2026](https://techcrunch.com/?p=3092988). Wang envisions agents that read calendars, preferences and travel plans and shop on users' behalf — [Fortune](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/). "All of it lays the groundwork for agentic commerce" — [Alta/Cannes PR](https://www.altadaily.com/blog/cannes).
- Daydream's long-term goal is a shopping agent "across the user's devices and daily activities", expanding on-device intelligence — [TechCrunch, Sep 2026](https://techcrunch.com/2026/09/14/fashion-discovery-app-daydream-uses-apple-intelligence-to-help-you-shop-the-outfits-saved-in-your-camera-roll/).
- OS-level hooks: Daydream and Glance both shipped Siri AI (iOS 27) entry points in Sep–Oct 2026, and Glance runs try-on on-device via Core AI — [Daydream App Store](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357), [Glance App Store](https://apps.apple.com/us/app/glance-my-personal-shopper/id6742974181).
- Runway-to-avatar pre-orders and demand sensing: Zimmermann SS27 was in Alta within minutes of the show, months before stores — [Alta blog](https://www.altadaily.com/blog/zimmermann), and it was used to inform production quantities — [Fortune](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/).
- Platform absorption: Doppl closed and folded into Google surfaces — [Google Labs on X](https://x.com/GoogleLabs/status/2036188820128071786); ChatGPT try-on is global — [TechCrunch](https://techcrunch.com/2026/10/01/chatgpt-can-now-virtually-try-on-clothes-for-you/).
- Classic closet apps are adding the same AI features. Whering (out of scope) raised $7M in Jul 2026 to add virtual try-on and resale — [Fashion Tech Funding Tracker](https://ftw.pi.tv/fashion-tech-funding/). Shopping-first Phia added closet and camera-roll import — [Phia App Store](https://apps.apple.com/us/app/phia-fashion-deals-rewards/id6739351340).

### Inferences
- Where AI-native apps directly copy classic closet apps: calendar logging and cost-per-wear (Alta vs Stylebook/Indyx), packing lists (Alta and Pronti vs Stylebook/Whering), and daily weather outfits (Alta, Pronti, Style DNA, Essembl vs Cladwell/Acloset). The differentiator is generation quality and capture speed, not the feature list.
- Likely next 12 months: (a) consolidation and B2B pivots among credit-burning try-on apps (Gensmo's quiet consumer app and Aiuta's pivot are early signals); (b) measurement- and size-aware try-on; (c) calendar-driven proactive styling ("agentic") and OS assistant integration; (d) closet data as a resale and commerce asset (Poshmark × Alta, Whering resale).

### Gaps
- No retention or cohort data from any app was found to test whether avatar try-on drives retention versus novelty use.
- Business-model sustainability (inference cost per avatar render vs affiliate revenue) is undisclosed for every app.

---

### Source-quality notes for the report writer
- App Store metadata (versions, dates, IAPs, privacy labels, ratings) is primary and was retrieved on 2026-10-25. Review quotes are a sample of the most recent US reviews and are not representative of all users.
- Company blogs (Alta) and press releases (Daydream, Gensmo, Glance) are vendor claims, especially the speed and quality comparisons.
- Doppl shutdown details come from Google's own X post and help page (the help page was read via search summary because a direct fetch returned only navigation chrome).
- ChatGPT privacy and training details come from secondary outlets and are marked as such.
- Raw downloads are in `/tmp/claude-0/-home-user-outfit-planner-app/bd7b84c5-3686-5200-b171-3a2b128c387a/scratchpad/clusterC/raw/` (lookup JSON, App Store HTML, extracted version histories `ext_*.txt`, reviews `reviews/all.txt`, Alta blog HTML) and screenshots in `.../clusterC/shots/` (per-app contact sheets `sheet_*.jpg`).
