# Wardrobe building and closet balance: research notes

Companion to `wardrobe.json`, which feeds Hanger's on-device "closet insights" and "tip of the day".
Research date: 29 October 2026. Source ids (w1–w31) match the `sources` array in the JSON. The full list is at the end.

## What's in the JSON

| Section | Count | Notes |
|---|---|---|
| `sources` | 31 | Each one is cited by at least one tip, check, ratio or essential |
| `tips` | 85 | capsule 10, colour 10, versatility 10, care 15, shopping 8, fit 6, budget 8, sustainability 12, packing 6 |
| `checks` | 42 | All 27 metrics covered; 14 positive and 28 corrective |
| `ratios.perBottom` | 7 categories | TOP 2.5, LAYER 0.8, SHOES 0.8, OUTERWEAR 0.4, ONE_PIECE 0.4, BAG 0.4, ACCESSORY 0.8 |
| `essentials` | 19 | Every type token is checked against the vocabulary |

A validation script confirmed that the JSON parses, that every topic, metric, op, category and type token is in the allowed set, and that the length limits hold:
- tips: 156 characters at most (limit 180);
- check titles: 29 at most (limit 40);
- check messages: 118 at most (limit 140);
- check actions: 75 at most (limit 90);
- essential "why" text: 105 at most (limit 120).

The validator also flags US spellings and gendered words.

---

## 1. The headline numbers: how much of a closet people actually wear

| Figure | Source | Notes |
|---|---|---|
| About **30% of clothing in the average UK wardrobe has not been worn for at least a year**. The most common reason is that it no longer fits. | WRAP 2012 (w1); repeated in Love Your Clothes (w3) | Survey of 7,950 UK adults. The average household owns about **£4,000** of clothes. Unworn clothing across the UK is worth about £30bn. |
| **4 in 5 people** own clothes they haven't worn because they no longer fit or need altering. | WRAP (w3) | Supports the fit tips and the "Unworn pieces" check. |
| **40% of clothes are rarely or never worn**: 2.08bn of 5.2bn items. Adults owned on average 118 items (women) and 73 (men), underwear excluded. | Greenpeace survey, Germany, Sept 2015, n≈1,011 (w8) | Cross-checks the WRAP figure from another country. The gendered averages are kept here for context only and appear nowhere in the app text. |
| **"We wear 20% of our clothes 80% of the time."** | Joshua Becker, Becoming Minimalist (w18) | **This is a rule of thumb, not a measured statistic.** I found no primary survey behind the exact 80/20 split. The app text calls it a "rule of thumb", and the measured anchors are WRAP's 30% and Greenpeace's 40%. |
| New purchases are worn **about 7 times** before they are seen as "old" (6 in Glasgow). | Barnardo's survey, UK, 2015 (w9, w31) | The sample was women aged 16 and over, and this was a campaign survey. It is treated as indicative. The app text says "one UK survey". |
| Clothing utilisation (times worn before disposal) **fell 36% in 15 years** while production **doubled**. In China it fell 70%. | Ellen MacArthur Foundation, *A New Textiles Economy*, Nov 2017 (w4; exact quotes via w5) | Less than **1%** of the material used to make clothing is recycled into new clothing. About **USD 500bn** of value is lost each year. One garbage truck of textiles is landfilled or burned **every second** (w6). |
| Consumers buy **60% more garments a year** than in 2000 and **keep them about half as long**. | McKinsey, Oct 2016 (w7) | McKinsey's page returned 503 when fetched. The figures were confirmed from the report PDF's text in search results. |
| UK clothing lasts **3.3 years** on average. Coats last longest at **4.58 years**, followed by dresses 3.62, jumpers 3.46, jeans 3.40, shirts 3.03, trousers 2.81, T-shirts 2.78 and tops 2.74. | WRAP 2017 (w2), Table 1 | This is the basis for the warm-coat essential and the tip to "spend more on coats, knits and jeans". WRAP names jeans, jumpers and coats as priority products for durability. |
| Keeping **50% of UK clothing in use 9 months longer** would save **8% carbon, 10% water and 4% waste** per tonne of clothing. | WRAP 2017 (w2), Table 10 | The 2012 report's older claim of "20–30% each" used a different baseline, so it is not used here. Moving 5–10% of sales to hire and repair stretches active life from 3.3 to 4.5 years in WRAP's scenario. |
| The EU generated about **16 kg of textile waste per person** in 2022. Only 4.6 kg of it (about 29%) was collected separately. | European Environment Agency, Mar 2025 (w24) | |

## 2. Capsule wardrobes

- **Origins.** The term was used in the US from the 1940s. London boutique owner Susie Faux revived it in the 1970s for a core of timeless pieces (skirts, trousers, coats) topped up each season. Donna Karan popularised it in 1985 with "Seven Easy Pieces", a set of interchangeable workwear. Faux's suggested core: 2 trousers, a dress or skirt, a jacket, a coat, a knit, 2 pairs of shoes and 2 bags (w14). Wikipedia flags that last list as possibly unreliable, so it is used only as a cross-check.
- **Project 333** (Courtney Carver, since 2010). It allows **33 items for 3 months**, counting clothing, accessories, jewellery, outerwear and shoes. Excluded are underwear, sleepwear, loungewear, workout kit and one sentimental piece of jewellery. Everything else is boxed up or given away (w11). App text describes it as "one popular challenge" and avoids the name, following the no-brand-names rule.
- **Un-Fancy 37-piece capsule** (Caroline Rector, via Anuschka Rees, w12). It is rebuilt 4 times a year with a "3 of each" rule:
  - **9 shoes**: 3 flats, 3 heels, 3 boots;
  - **9 bottoms**: 3 jeans, 3 shorts, 3 skirts;
  - **15 tops**, including 3 sweaters and 3 vests;
  - **4 jackets or dresses**, split by season (winter 4 jackets and 0 dresses; summer 1 jacket and 3 dresses).

  Caroline plans with a wardrobe planner listing daily activities and events. She shops only four times a year and saves ahead. Note: another blog quotes 7 bottoms and 4 coats, but I used the breakdown on Rees's own page.
- **4x4 wardrobe** (The Vivienne Files, w13). It has 16 pieces in four groups of four: a "Core of Four" in neutral 1, a Core of Four in neutral 2, an "Expansion Four" that mixes the two neutrals, and a "Mileage Four" of accent pieces. That makes **12 of 16 (75%) neutral-based**. It is designed to fit in a carry-on.
- **10x10 challenge** (Lee Vosburgh, Style Bee). Wear **10 pieces for 10 days**; shoes and jackets count, and accessories such as scarves and jewellery can change daily (w29). Fashion Magazine's interview with Vosburgh was blocked (403). Her advice to "start with what you own" is known only from a search snippet, so the related tip cites w12 and w29 instead.
- A capsule guide (w26) puts typical capsules at **30–40 pieces**: 8–10 tops, 3–5 knits, 5–6 bottoms, 2–3 dresses, 2–3 outerwear (blazer, trench or wool coat, casual jacket), 3–5 shoes and 3–5 accessories.

## 3. Ratios: tops to bottoms, layers and shoes

| Source | Tops per bottom | Layers per bottom | Shoes per bottom | Other |
|---|---|---|---|---|
| L'Agence capsule guide (w15) | **3** ("one bottom per three tops") | 1 (one knit per 3 tops) | "thirds logic" | 1 dress per 4 tops; outerwear max 2 (weather + occasion) |
| Anne McAlpin, packing expert, via Islands (w16) | **3** | – | – | "two to three tops per pair of pants" in travel guides |
| Un-Fancy 37 (w12) | 1.0 tops + 0.67 knits/vests (1.67 counted together) | 0.67 | 1.0 | jackets/dresses 0.44 |
| Capsule guide midpoint (w26) | 1.6 | 0.73 | 0.73 | outerwear 0.45, dresses 0.45, accessories 0.73 |
| Faux core (w14) | – | 0.67 | 0.67 | coat 0.33, bags 0.67 |
| 5-4-3-2-1 packing (w17) | 1.25 | – | 0.75 | |

**Chosen `perBottom`:**
- TOP 2.5: the stylist rule of 3, pulled slightly towards published capsules, which come out at 1.6–1.7 once layers are counted separately;
- LAYER 0.8;
- SHOES 0.8;
- OUTERWEAR 0.4;
- ONE_PIECE 0.4 (optional; some closets have none);
- BAG 0.4;
- ACCESSORY 0.8.

The "tops per bottom" checks fire below **1.5** ("add tops") and above **5** ("bottoms will wear out first"), so anything between 1.5 and 5 counts as healthy. The usual reason given for more tops than bottoms is that bottoms can be re-worn between washes and tops can't (w16).

*Implementation note:* `perBottom` includes four keys beyond the schema example (OUTERWEAR, ONE_PIECE, BAG, ACCESSORY). They all use the category vocabulary. If the parser expects only TOP, LAYER and SHOES, it should ignore unknown keys.

## 4. Neutral vs accent share

- Recommended split (w25): **60–70% neutrals, 20–30% secondary colours, 0–10% bold accent**, built on **2–3 neutrals**.
- The 4x4 structure works out to 75% neutral (w13).
- A capsule guide (w26) suggests 2–3 core neutrals and **1–2 accent colours, each repeated in at least two pieces** so it can anchor an outfit. It also suggests keeping shoes, bags and belts in one neutral family.
- Geneva Vanderzeil (w28) describes two ways to wear neutrals. Tonal: shades next to each other. Contrasting: opposite on the colour wheel. Neutral days and colour days can alternate.
- L'Agence (w15) splits tops roughly into thirds (neutral, print, bold colour) and notes that **solid tops are the easiest to pair**. This is the basis for the bold-pattern thresholds: more than 35% bold patterns overall is corrective, and 20% or less is positive.
- The thresholds below follow from this:

  | Rule | Result |
  |---|---|
  | `neutralShare` under 0.4 | corrective |
  | `neutralShare` over 0.85 | corrective ("add an accent") |
  | `accentColours` == 2 or == 3 | positive |
  | `accentColours` over 5 | corrective |

  A closet can be 90% neutral and still have 2 accents, in which case k02 and k04 both fire. Their wording is compatible: "a few more pieces in one accent" alongside "focused palette".

## 5. Versatility and mix-and-match maths

- Outfits = tops × bottoms × (layer options + 1, for wearing no layer). For example, 5 × 3 × (2 + 1) = 45. One-pieces add (one-pieces × (layers + 1)). The tip uses the 5-tops, 3-bottoms, 2-layers example.
- Ways to multiply outfits:
  - Layers: one per three tops (w15).
  - Accessories: scarves and jewellery vary the 10x10 outfits (w29).
  - Outfit formulas: from *The Curated Closet* (w30).
  - Shoes that set formality: from editors' staples lists (w27) and smart vs casual shoe guidance (w22).

## 6. The 30-wears test, cost per wear and the 80/20 rule

- **#30wears.** Livia Firth (Eco-Age): "We created the #30Wears campaign so someone can ask, 'Will I wear it a minimum of 30 times?' If the answer is no, don't buy it." (Stylist, Feb 2019, w10). Some secondary sources credit Lucy Siegle with first proposing "30 wears". The app attributes no one and uses it as "the 30-wears test".
- **Cost per wear** = price ÷ wears, real or expected (w23). Encircled suggests setting separate cost-per-wear goals for everyday and occasion pieces, and says durability lowers cost per wear. Some calculators also add care costs and subtract resale value; that is not used here, because the app only knows price and wears.
- `avgCostPerWear` thresholds: over **10** is corrective and **2 or less** is positive, in the user's currency. With a 30-wear target, a 60-unit piece reaches 2 per wear, and anything over 10 per wear means pieces are worn about 6 times or fewer (close to the Barnardo's 7). **The app should consider scaling these by currency.** They are calibrated to GBP, EUR and USD and would be wrong for JPY or INR.
- **80/20.** Becker's line (w18) is used in tips and in k31/k32 as a rule of thumb. The measurable version is `wornShare90`: under 50% is corrective, 80% or more is positive.

## 7. Seasonal coverage and what most closets lack

- Seasonal rotation is built into Project 333 (3 months) and Un-Fancy (4 capsules a year, with a winter/summer swap of jackets and dresses) (w11, w12).
- `summerPieces` and `winterPieces` under 5 are corrective, with minItems 20. *Suggestion:* the app could raise the priority of these checks in the four to six weeks before each season.
- **Rain-ready outerwear.** L'Agence caps outerwear at two, "one for weather and one for special occasions", and lists "a water-ready boot" among the shoes (w15). Vogue Singapore calls the trench "the outerwear choice you can't get wrong" (w27). This became the "Rain-ready coat" essential and check k23.
- **Smart shoes.** Editors' staples include loafers and dress shoes (w27). A tailoring retailer recommends owning several smart pairs to rotate (w22). This became essential "Smart shoes" and checks k25/k26.
- **Formality gap.** No source gives a share, so k27 (`formalPieces` == 0) and k28 (`casualShare` over 0.85) are judgement calls based on the editors' blazer, tailored trouser and shirt staples (w27, w15).

## 8. Garment care that extends life

| Advice | Figure | Source |
|---|---|---|
| Washing and care share of clothing's carbon footprint | **about one third**; changed habits since 2012 saved 700,000 t CO2e; tumble drying fell from 32% to 26% of washes | WRAP 2017 (w2) |
| Wash at 30°C instead of 40°C | **38% less energy**; 20°C saves 62%; about £24 a year at four washes a week | Which?, Jun 2026 (w21) |
| Full loads | Fewer big loads beat daily small ones | w21 |
| Dryer filter | Blocked filters can use **50% more energy** on the last load | w21 |
| Wash jeans every 10 wears | Water and climate impact **down 77% (US) and 75% (UK/France)** | Levi Strauss LCA, Mar 2015 (w19) |
| Wash every 3rd wear instead of every 2nd | Climate and water impact down up to 20% | w20 |
| Cold wash | Climate impact down 24% and non-renewable energy down 21% (US) | w20 |
| Line-dry instead of tumble drying | Climate impact down 67% and energy down 64% (US) | w20 |
| Repair confidence (UK) | Sew a button **73%**, remove a stain 60%, hem 38%, darn or patch 35%, zip 17%, pocket 14%, take in or let out 12%, none of these 16% | WRAP 2017 (w2), Fig. 13 |
| Shoes | Rest **24 hours** between wears; insert cedar shoe trees **within an hour**; rotate several pairs | Tom James (w22) |

Not sourced, so left out:
- moth prevention;
- wool-specific care (airing knits, folding instead of hanging, drying flat).

The Woolmark care pages returned 403 and the Natural History Museum moth page returned 404, so I could not confirm their wording. Both are worth adding later.

## 9. Mindful shopping

- Use the 30-wears test (w10).
- Shop from a list and a wardrobe planner, a few times a year, and save ahead (w12).
- Check durability and judge sales on cost per wear (w23).
- Look second-hand first, and hire or repair for one-off occasions. WRAP's scenarios show both extend active life (w2).
- `wishlist` over 15 is corrective: run each item through the 30-wears test.

## 10. Packing

- 3 tops per bottom; start with 2–3 neutral bottoms and 4–6 light tops (Anne McAlpin via w16).
- **5-4-3-2-1** (Geneva Vanderzeil, 2015): 5 tops, 4 bottoms, 3 dresses and 3 shoes, 2 swimwear and 2 bags, then 1 hat, 1 watch and 1 pair of sunglasses (Stylist version, w17). The tip simplifies it to 5 tops, 4 bottoms, 3 pairs of shoes, 2 swimsuits and 1 hat. Other versions differ.
- A 4x4 capsule of 16 pieces fits a carry-on (w13).

---

## Checks: design notes

**Positive checks (14):**

| Check | Metric | Rule |
|---|---|---|
| k04 | accentColours | == 2 |
| k05 | accentColours | == 3 |
| k07 | boldPatternShare | <= 0.2 |
| k09 | items | <= 40 |
| k12 | onePieces | >= 2 |
| k14 | layers | >= 4 |
| k20 | accessories | >= 5 |
| k26 | smartShoes | >= 2 |
| k29 | casualShare | <= 0.6 |
| k30 | wornShare30 | >= 0.5 |
| k32 | wornShare90 | >= 0.8 |
| k34 | neverWornShare | == 0 |
| k36 | avgCostPerWear | <= 2 |
| k40 | duplicates | <= 2 |

All the other 28 checks are corrective. Wherever a metric has both a positive and a corrective check, the two thresholds do not overlap.

**Assumptions the app needs to meet:**
1. **`{value}` formatting.** Messages assume shares (`neutralShare`, `boldPatternShare`, `casualShare`, `wornShare30/90`, `neverWornShare`, `laundryShare`) render as percentages ("85%"), as in the brief's example. Counts render as integers, `topsPerBottom` with one decimal (e.g. "1.2"), and `avgCostPerWear` with a currency symbol. Count messages use "Your tops count is {value}" style wording, so a value of 1 still reads correctly.
2. **Wear-based checks.** These are k30–k36, on `wornShare30`, `wornShare90`, `neverWornShare` and `avgCostPerWear`. They are only meaningful if the user logs wears. I recommend gating them in code, for example only after the first logged wear and once the closet has been in the app for at least 30 days. Otherwise k31 ("much of the closet is resting") and k33 ("unworn pieces") will nag users who simply don't log wears. In particular, k34 (`neverWornShare` == 0) must not fire when no item is older than 60 days. If the denominator is empty, treat the metric as missing, not as 0.
3. **Missing metrics.** `avgCostPerWear` can be undefined when no item has both a price and wears. Skip the check rather than evaluating it as 0.
4. **Overlap.** Several checks can fire together on sparse closets. For example, the no-raincoat, no-warm-coat and no-smart-shoes checks all fire on a casual starter closet. In simulation, a 25-piece student closet fired 13 checks and a 200-piece closet fired 13. **Show at most 3–5 at a time.** Suggested priority:
   1. weather and occasion gaps (k23, k24, k25, k27);
   2. category gaps (k10, k11, k13, k16, k21, k22);
   3. colour and pattern (k01–k06);
   4. wear and value (k31, k33, k35);
   5. housekeeping (k39, k41, k42);
   6. positives, at most one per screen.
5. Simulated sample closets fire:
   - **typical 80-piece closet:** 6 checks (1 corrective, 5 positive);
   - **neutral 35-piece capsule:** 10 checks (1 corrective, 9 positive);
   - **casual 25-piece starter closet:** 13 checks (12 corrective, 1 positive);
   - **200-piece closet:** 13 checks (8 corrective, 5 positive).

**Thresholds that are judgement calls rather than sourced figures:**
- `items` over 150;
- `outerwear` over 8;
- `shoes` over 20;
- `duplicates` of 5 or more;
- `laundryShare` over 0.4;
- `wishlist` over 15;
- `summerPieces` and `winterPieces` under 5.

Each one is anchored as far as possible: 33 items in Project 333, 9 shoes in Un-Fancy, outerwear capped at 2 per L'Agence (with a 4-season allowance added). They should be tuned on real user data.

## Essentials: notes and vocabulary mapping

There are 19 essentials. Mappings from source wording to Hanger tokens:
- "trench" → Trench coat;
- "hooded rain jacket" → Raincoat or Parka;
- "water-ready boot" → Boots, Chelsea boots, Ankle boots or Rain boots;
- "button-down", "excellent shirting" or "white shirt" → Shirt, Dress shirt or Blouse;
- "cashmere sweater" or "knit" → Knit sweater or Cardigan;
- "tailored jacket" or "sport coat" → Blazer;
- "casual layer (denim or leather jacket)" → Denim jacket, Leather jacket, Jacket or Bomber jacket;
- "wrap skirt" → Skirt or Midi skirt;
- "pump" → Heels (not used as an essential);
- "vests" in the Un-Fancy capsule → Sweater vest or Tank top;
- "lace-up dress shoes" → Dress shoes;
- "leather lace-up sneakers" → Sneakers.

The essentials avoid gendered framing. "One-piece outfit" covers dresses and jumpsuits alike, and "Evening bag" covers Handbag and Clutch.

## Gaps and caveats

- **The web-search budget ran out partway through.** After that I could only fetch URLs I already had, so these areas are thinner than intended:
  - recent (2023–2026) wardrobe-utilisation surveys;
  - Met Office rain-day figures to back the rain-coat claim with data;
  - Hubbub's surveys;
  - GQ, Esquire, Mr Porter and Permanent Style essentials lists.
- **Blocked pages:**
  - Vogue Singapore returned 403. Its content (white tee as "MVP", shirting, trousers, blazer, loafers, trench) comes from the search-result snippet.
  - McKinsey returned 503. Figures come from the PDF text in search results.
  - Woolmark returned 403 and NHM returned 404, so there are no moth or wool-care tips.
  - Fashion Magazine and The Every Girl returned 403.
  - The EMF report PDF could not be downloaded. The utilisation and recycling quotes come via w5.
- **Dated data:** WRAP 2012 and 2017, Greenpeace 2015, Barnardo's 2015 and McKinsey 2016 are old but are still the most-cited primary figures. Barnardo's surveyed women only, and Greenpeace's survey was German.
- **Less authoritative colour and capsule sources:** some of the colour-proportion and capsule-count guidance comes from smaller style blogs (w25 The Capsule Report, w26 Powers of 10). They are cross-checked against the Vivienne Files structure (w13), L'Agence (w15) and Un-Fancy (w12). Treat the exact percentages as guidance, not research.

---

## Full source list

| id | Title | Publisher | Date | URL |
|---|---|---|---|---|
| w1 | Valuing Our Clothes: the true cost of how we design, use and dispose of clothing in the UK | WRAP | 2012-07-11 | https://wrap.ngo/resources/report/valuing-our-clothes-true-cost-how-we-design-use-and-dispose-clothing-uk-2012 |
| w2 | Valuing Our Clothes: the cost of UK fashion (text read from a mirrored copy of the PDF) | WRAP | 2017-07-17 | https://www.wrap.ngo/resources/report/valuing-our-clothes-cost-uk-fashion |
| w3 | Previous campaigns: Love Your Clothes | WRAP | n.d. (accessed 2026-10-29) | https://www.wrap.ngo/taking-action/citizen-behaviour-change/previous-campaigns |
| w4 | A New Textiles Economy: Redesigning fashion's future | Ellen MacArthur Foundation | 2017-11-28 | https://www.ellenmacarthurfoundation.org/a-new-textiles-economy |
| w5 | Textiles: Your Clothes are Pollutants (quotes from the EMF report) | Conversable Economist (Timothy Taylor) | 2018-01-22 | https://conversableeconomist.com/2018/01/22/textiles-your-clothes-are-pollutants/ |
| w6 | Truck of textiles landfilled or burned every second, says EMF report | Circular Online | 2017-11-30 | https://www.circularonline.co.uk/news/one-truck-textiles-landfilled-burned-every-second-says-new-macarthur-report/ |
| w7 | Style that's sustainable: A new fast-fashion formula (Remy, Speelman, Swartz) | McKinsey & Company | 2016-10 | https://www.mckinsey.com/capabilities/sustainability/our-insights/style-thats-sustainable-a-new-fast-fashion-formula |
| w8 | Germans throw away their clothes: Greenpeace | The Local Germany (dpa) | 2015-11-23 | https://www.thelocal.de/20151123/germans-throw-away-their-clothes-greenpeace |
| w9 | Charity exposes Armagh women's wardrobe habits in clothing donation appeal (Barnardo's survey) | Armagh I | 2015-07-15 | https://armaghi.com/news/charity-exposes-armagh-womens-wardrobe-habits-in-clothing-donation-appeal/28297 |
| w10 | Livia Firth wants to be remembered for being unapologetically outspoken (Colin Crummy) | Stylist | 2019-02-08 | https://stylist.co.uk/people/livia-firth-interview-environment-fashion-brandmark-eco-age/250276 |
| w11 | What is Project 333? (David Friedlander) | LifeEdited | 2013-08-23 | https://lifeedited.com/what-is-project-333/ |
| w12 | Caroline's 37-Piece Capsule Wardrobe + Creative Process | Anuschka Rees | 2014-07-14 | https://anuschkarees.com/blog/2014/07/14/carolines-37-piece-capsule-wardrobe-creative-process |
| w13 | Four by Four Wardrobes (Janice Riggs) | The Vivienne Files | n.d. (accessed 2026-10-29) | https://www.theviviennefiles.com/four-by-four-wardrobes/ |
| w14 | Capsule wardrobe | Wikipedia | accessed 2026-10-29 | https://en.wikipedia.org/wiki/Capsule_wardrobe |
| w15 | How to Build a Capsule Wardrobe | L'AGENCE | n.d. (accessed 2026-10-29) | https://lagence.com/blogs/how-to-style/build-a-capsule-wardrobe |
| w16 | Should You Pack More Tops Or Bottoms? (Christine Dulion, quoting Anne McAlpin) | Islands | 2025-10-18 | https://www.islands.com/1995162/should-pack-more-tops-bottoms-luggage/ |
| w17 | How to use the 5-4-3-2-1 method to nail holiday packing every time (Amy Beecham) | Stylist | 2025-01-30 | https://stylist.co.uk/travel/54321-packing-method/889794 |
| w18 | 20 Places to Apply the Pareto Principle to Help Minimize (Joshua Becker) | Becoming Minimalist | n.d. (c. 2025) | https://www.becomingminimalist.com/?p=32092 |
| w19 | How We Saved One Billion Liters of Water (501 jeans lifecycle assessment) | Levi Strauss & Co. | 2015-03-17 | https://www.levistrauss.com/2015/03/17/lca-findings/ |
| w20 | 3 surprising insights about those 501 jeans | Levi Strauss & Co. | 2015-03-26 | https://www.levistrauss.com/2015/03/26/3-surprising-insights-about-those-501-jeans/ |
| w21 | Cut your laundry costs this year (Rebecca Jakeman) | Which? | 2026-06-09 | https://www.which.co.uk/news/article/cut-your-laundry-costs-by-162-this-year-an6GD2M0Hgnn |
| w22 | The Ultimate Answer: How Many Shoes Should I Own? | Tom James | 2023-03-22 | https://www.tomjames.com/blog/how-many-shoes-do-you-really-need |
| w23 | What Is Your Cost Per Wear? Calculate Yours in 1 Easy Step! (Courtney Noble) | Encircled | 2020-05-14 | https://encircled.co/blogs/live/cost-per-wear |
| w24 | Textile waste generation per person in the EU per year | European Environment Agency | 2025-03-19 | https://www.eea.europa.eu/en/circularity/sectoral-modules/textiles/textile-waste-generation-per-person-in-the-eu-per-year |
| w25 | How to choose a colour palette (Claire Hartwell) | The Capsule Report | 2026-07-07 | https://thecapsulereport.com/blog/how-to-choose-colour-palette |
| w26 | Capsule Wardrobe Essentials (Bernice Blake) | Powers of 10 | 2026-09-24 | https://powersof10.com/?p=43053 |
| w27 | Capsule wardrobe: the essentials | Vogue Singapore | n.d. (accessed 2026-10-29; 403 on fetch, read via search snippet) | https://vogue.sg/capsule-wardrobe/ |
| w28 | Neutral wardrobe colours (Geneva Vanderzeil) | Geneva Vanderzeil | 2017-04-20 | https://genevavanderzeil.com/2017/04/neutral-wardrobe-colours/ |
| w29 | 10x10 Capsule Wardrobe (after Lee Vosburgh's 10x10 challenge) | Helen's Closet | 2017-11-14 | https://helensclosetpatterns.com/blogs/helens-closet/10x10-capsule-wardrobe |
| w30 | The Curated Closet (Anuschka Rees) | Ten Speed Press / Penguin Random House | 2016 | https://penguinrandomhouse.com/books/532611/the-curated-closet-by-anuschka-rees |
| w31 | Once worn, thrice shy: women's wardrobe habits exposed (Barnardo's Scotland survey) | Third Force News | 2015-06-11 | https://tfn.scot/news/once-worn-thrice-shy-womens-wardrobe-habits-exposed |

Other sources consulted but not cited in the JSON:
- Wikipedia/OED on early "capsule" usage;
- WRAP Clothing Knowledge Hub summary of the Levi's lifecycle assessment (weekly to monthly jean washing: −48% carbon, −35% water);
- European Parliament textiles infographic (page did not render);
- Greenpeace 2022 follow-up survey (no wardrobe-wear figures found).
