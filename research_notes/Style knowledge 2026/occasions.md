# Occasions, dress codes, weather and fabrics: research notes

Companion to `occasions.json`, the occasion/weather/fabric part of Hanger's on-device style knowledge base.
Researched October 2026. Written for `tools/build_knowledge.py`, which loads the file into
`StyleKnowledge.occasions`, `dressCodes`, `weather` and `fabrics` (see `engine/.../Knowledge.kt`).

## What's in the file

| Section | Count | Notes |
|---|---|---|
| `occasions` | 8 (all of LOUNGE, ACTIVE, CASUAL, WORK, SMART, DATE, FORMAL, TRAVEL) | 16-20 `good` types, 4-12 `avoid` types, 4-5 tips each |
| `dressCodes` | 22 | Every code in the brief, plus come-as-you-are, dressy casual, semi-formal, festive, lounge suits, morning dress, creative black tie and dress-to-impress |
| `weather` | 19 rules | 9 contiguous feels-like bands (below -10 °C to 30 °C and above) plus 10 condition rules |
| `fabrics` | 32 materials | All 28 required, plus synthetic insulation, tropical wool, faux leather and waxed cotton |
| `sources` | 50 | Full list at the end |

Validation: `python3 -m json.tool` passes. A checker script confirmed every type token against the vocabulary, the
length limits (weather advice ≤160, fabric tip ≤140, care ≤100, tips ≤112), no type in both `good` and `avoid`,
no gaps between the temperature bands, no duplicate fabric keywords and that every source is cited. `python3 tools/build_knowledge.py --check`
reports no problems for this file. Its only warnings are about other tracks' files that haven't been written yet.

## How the guidance reflects 2025-26 (not 1990s rules)

- **The office default is business casual, and smart casual is spreading.** Hybrid work has blurred the codes.
  Harry Rosen (Mar 2025) found dark jeans, knitted polos, unstructured jackets and separates worn from a suit
  across consulting, design and tech, with ties kept for "boardroom presentations or deal closings" [o1].
  The Philadelphia Inquirer (Jun 2025) quotes Robert Half and law-firm partners: jeans and dressy sneakers are common,
  ties appear mainly in court or with high-profile clients, and heels are rare [o2]. The Globe and Mail (updated May 2026)
  quotes Randstad: "Jeans are now accepted and very common, even for people with leadership positions" [o3].
  SEEK (Sep 2026) accepts "sleek, minimal sneakers" in many workplaces but still rules out hoodies, athletic wear,
  leggings and singlets [o4]. MR PORTER (Sep 2025) notes the return-to-office mandates and recommends a soft
  tailored jacket over the plain shirt-and-trousers default [o8, search summary only].
  → WORK `good` includes Jeans, Sneakers, Polo and Knit dress. `avoid` keeps only clear misses (Hoodie, Leggings, Sports top,
  Running shoes/shorts, Shorts, Crop top, Flip-flops, Evening dress). Tank top is deliberately **not** avoided: a camisole
  under a blazer or cardigan is normal office wear, and the engine penalises items one at a time.
- **Interviews: one notch above the company's everyday dress** [o7, o3]. Coursera's 2026 guide lists hoodies,
  sweatshirts, leggings, ripped jeans, athletic wear, shorts, flip-flops and casual sandals as things to avoid.
- **Restaurants have brought back dress codes.** Upscale venues refuse athletic wear, beachwear, flip-flops and hats
  (Washingtonian on Shōtō and others; Chicago steakhouses in 2024 coverage) [o12]. This trend started in 2022-24 and
  I could not find a 2025-only roundup. → SMART `avoid` includes Cap, Joggers, Leggings, Sports top, Flip-flops, Backpack and Gym bag.
- **Invitation codes still follow Debrett's and Emily Post,** with modern readings: cocktail can mean
  "smart lounge suits, velvet jackets", black tie accepts knee-length cocktail dresses as well as gowns, and
  "over-dressing is preferable to under-dressing" [o6, o5].
- **Travel is about comfort and layers:** stretchy soft fabrics, a cotton tee with a knit or cardigan, a cashmere wrap that doubles
  as a blanket, slip-on shoes, compression socks and minimal jewellery and belts. Avoid heels, knee-high boots and strappy sandals
  (stylist Oriona Robb, Sep 2026) [o10]. Wear your bulkiest shoes and coat to save luggage space [o11], and skip lint-prone fleece
  and corduroy on planes [o50].
- **Dates:** match the venue, wear something already tried and tested, and choose shoes you can walk in (Glamour, Mar 2025) [o9].

## Formality windows

The `formality` values mirror the engine's `Occasion` windows in `Model.kt` with one exception. **SMART is [3, 4]**,
while the engine allows 3..5, because level 5 (black tie and above) belongs to FORMAL. Cocktail and semi-formal are
level 4, which is SMART's `ideal` in the engine, so I mapped both to SMART. Lounge suits, morning dress and all the
black-tie and white-tie codes map to FORMAL.

## Mapping notes (real-world wording → app type)

- Dinner jacket, tuxedo jacket, suit jacket, sport coat, morning coat, velvet smoking jacket, linen blazer → **Blazer**
- Suit trousers, striped morning trousers, evening trousers → **Trousers** (palazzo/evening wide legs → **Wide-leg trousers**)
- Bow tie → **Tie**. Morning-dress waistcoat and black-tie vest → **Waistcoat**
- Ball gown, floor-length gown → **Evening dress**. Cocktail dress, LBD, day dress, tea-length dress → **Dress** (or **Midi skirt** separates)
- Oxfords, derbies, patent evening shoes → **Dress shoes**. Velvet slippers, boat shoes → **Loafers**. Ballet flats → **Flats**
- Wellies / wellington boots → **Rain boots**. Trail shoes → **Running shoes** or **Hiking boots**. Minimal leather trainers → **Sneakers**
- Slides and pool sliders → **Flip-flops**. Camisole, singlet → **Tank top**. Thermal base layer → **Long-sleeve top**
- Packable shell, cagoule → **Raincoat** (waterproof) or **Windbreaker** (wind and showers only). Insulated hooded coat → **Parka**
- Bucket hat, sun hat, fedora, top hat, wedding hat → **Hat**. Fascinator → **Hat** (closest type)

The app's `Catalog.kt` also defines OTHER types (**Swimwear, Socks, Tights, Sleepwear, Loungewear**) that are not in the shared
vocabulary, so I didn't use them. If the vocabulary is widened, good additions would be **Loungewear** and
**Sleepwear** in LOUNGE `good`, **Swimwear** in WORK/SMART/FORMAL `avoid` and in the beach-wedding `avoid` list (the
couples' codes on The Knot ban bathing suits [o15]), and **Tights** for cold-band skirts.

## Weather rules: how they work with the engine

The engine (`WeatherWhen.matches`) treats `minFeelsC` as **inclusive** and `maxFeelsC` as **exclusive**, ignores null
bounds, and lets every matching rule apply at once. The bands are therefore contiguous: <-10, -10..0, 0..5, 5..10,
10..15, 15..20, 20..25, 25..30 and ≥30 °C feels-like. The band edges follow Nike's Celsius layering ladder (-12/-4, -3/4,
4/13, 13/21, 21/29, 29+) and Brooks' running bands (<5, 5-15, 16+), rounded to 5 °C steps [o18, o19]. The Met Office explains
why feels-like is used: wind chill at low temperatures, heat index at high ones, and a blend in between [o20, o21].

Condition rules and the thresholds behind them:

| Rule | `when` | Basis |
|---|---|---|
| Rain | rain | NWS: outer garments "tightly woven, water repellent, and hooded" [o24]. Suede is damaged by water and salt [o49] |
| Cold rain | rain + feels <8 | NWS: wet clothing loses insulation, so layer and keep dry [o24] |
| Heavy, stormy rain | rain + wind ≥30 km/h | Beaufort force 5 (29-38 km/h). By force 6 (39-49 km/h) "umbrellas used with difficulty" [o22, o23] |
| Warm showers | rain + feels ≥18 | Light shells over breathable bases in mild weather [o18] |
| Snow | snow | Grip, insulation, covered head and hands [o24, o18] |
| Strong wind | wind ≥35 km/h | Top of force 5, close to force 6 [o22, o23]. Wind strips the warm boundary layer [o20, o21] |
| High UV | UV ≥6 | ECCC "High (6-7)": tightly woven clothing that covers skin, a hat over face and ears, shade near midday [o25] |
| Very high UV | UV ≥8 | ECCC "Very high/Extreme": stay in shade, limit sun 11am-3pm [o25]. SCF: brims ≥3 in, loose, tightly woven fabric [o26] |
| Hot and humid | feels ≥30 + day's high ≥28 | Humidity slows sweat evaporation [o20]. Loose, lightweight, light-coloured clothing [o28, o27] |
| Cool start, warm afternoon | feels <14 + day's high ≥20 | Removable layers [o18, o20] |

**Limitations to fix in the engine if wanted:**
- **Heavy rain can't be detected directly.** `WeatherContext` has no precipitation amount, and the `rain` flag is set for
  drizzle, rain, thunderstorms or ≥50% chance. So "heavy rain" is approximated as rain plus wind ≥30 km/h, the
  wind-driven case where umbrellas fail and only a hooded waterproof works. If the forecast API exposes mm/h,
  a `minRainMm` condition (heavy rain is commonly defined as ≥4 mm/h, a figure I didn't re-check this session)
  would let this rule match true downpours.
- **Humidity isn't in `WeatherContext`.** "Hot and humid" uses feels-like ≥30 and day high ≥28 as a proxy. It also fires in dry heat,
  where the advice (loose, light, breathable, no heavy denim) still holds.
- **Rules stack.** Two overlaps are intentional. On a hot day with UV ≥6, Tank top and Crop top are penalised
  (cover-up advice) while T-shirts and loose shirts are favoured. On the colour question, the Skin Cancer Foundation says darker,
  denser fabric blocks more UV, while heat guidance favours light colours. The advice text therefore stresses *tightly woven and loose*
  rather than colour.
- Leggings are not in any heat `avoidTypes`, so ACTIVE outfits (running tights) are not penalised in summer.

## Fabric guide: matcher notes

`fabricFor()` picks the **longest keyword contained in the material text**, and on a tie the earlier fabric in the
list wins. I chose keywords to resolve common collisions correctly (all checked with a simulated matcher):

- "merino wool" → Merino, "wool flannel" → Flannel, "cable knit wool" → Knit, "wool knit" → Wool (tie, Wool listed first)
- "cotton jersey" → Jersey and "cotton velvet" → Velvet (explicit keywords break the 6-letter tie with "cotton")
- "down alternative" / "synthetic down" → Synthetic insulation, not Down; "button-down cotton" → Cotton
- "recycled polyester fleece" → Fleece (via "polyester fleece"), "polyester satin" / "silk satin" → Satin
- "vegan leather" / "pu leather" → Faux leather; "shell cordovan" → Leather; "cordura" → Nylon (so neither hits "cord")
- "tropical wool" / "fresco" → Tropical wool, not winter Wool; "waxed cotton" → Waxed cotton, not Cotton

**Known limitation:** for blends the longest keyword wins whatever the proportions, so "cotton/polyester" shows the
polyester tip and "linen cotton" the cotton tip. For that reason I added **no "elastane/spandex" entry**: it would take over
every stretch-cotton item. If blends matter, the matcher could use the first-mentioned or highest-percentage fibre.

`warmth` uses the app's 1-5 scale (1 = very light, 5 = very warm). An empty `seasons` list means all year, which I used
for cotton, merino, mohair (summer suiting and winter knits), denim, silk, satin, jersey, polyester, nylon, the
membrane and modal/lyocell.

Fabric facts used (main points):
- Linen: cool to the touch, absorbs moisture and dries faster than cotton, gains strength when wet, creases easily, softens with washing, isn't attacked by moths [o30]. ECCC cites linen as a covering fabric for sun [o25]
- Wool and merino: thermoregulating, absorbs moisture vapour, locks away odour until washed [o29, search summary]. NWS: avoid cotton when active in cold because it holds moisture [o24]
- Cotton: a plain white cotton tee is only about UPF 5 [o26]
- Cashmere: hand-wash or 30 °C delicate, don't wring or tumble, fold rather than hang, cedar against moths, clean before storing [o36]
- Alpaca: warmer than sheep's wool and not prickly, almost no lanolin, insulates even when wet [o37]
- Mohair: lustrous, resilient, crease-resistant [o35, search summary]
- Denim and chambray: denim is a twill, chambray a lighter plain weave used for shirts [o32, search summary]. Dense denim, wool and corduroy block UV well [o26]
- Corduroy: wale count is ridges per inch. Low counts are chunky; needlecord (14+) is fine [o33, search summary]
- Flannel: brushed (napped) wool or cotton. "Flannel shirt" refers to the fabric, not a check pattern [o38]
- Tweed: "warm in winter and cool in summer… resists water, wear and tear" (the maker's own claim) [o34]
- Seersucker: the puckers lift fabric off the skin and create air pockets; it resists wrinkles [o31]. Gentleman's Gazette also notes open-weave fresco regulates heat well, which is the basis for the Tropical wool entry
- Silk: absorbent, cool to the touch, insulating, loses up to 20% of its strength when wet, yellowed by sweat, weakened by sunlight [o40]
- Satin: a weave, not a fibre. Glossy, drapes well, snags [o41]. Velvet: a cut pile, hard to clean [o42]. Jersey: a weft knit, stretchy [o43]
- Fleece: polyester, light and warm, quick-drying, **not windproof**, can pill, sheds microfibres [o44]
- Down: best warmth for its weight. Fill power 500-650 suits most conditions, 800-900 the lightest and coldest gear. Wet down loses almost all its insulation [o39]
- Waterproof membranes: wash with a technical detergent, tumble warm about 20 minutes to revive the DWR finish, reapply when water stops beading. Arc'teryx says PFAS-free finishes (2025) need more frequent care [o48]
- Viscose/rayon: cool, absorbent, drapes well, weak and prone to shrinking when wet, creases easily [o46]. Modal is stronger when wet and pills less [o46]. Lyocell and modal manage moisture well [o47]
- Acrylic: wool-like and cheap, pills, less warm than wool when wet, sheds the most microfibres of the fibres compared [o45]

**Entries from general textile knowledge, with no web source this session:** Leather, Faux leather and Waxed cotton (`src: []`).
These are well-established care basics, but a style editor should check them.

## Research caveats

- The shared **web-search budget ran out** during the session, so some planned lookups (gym/activewear guidance,
  a current CDC heat page, polyester and nylon comfort sources) weren't done. Several sources are cited from
  **search-result summaries** because the page blocked direct fetching (403/503) or wasn't fetched: o8 MR PORTER, o11 Broadsheet,
  o12 Washingtonian, o14 Greater London Lieutenancy, o15 The Knot (partly read), o16 Wikipedia Morning dress, o19 Brooks,
  o21 Met Office wind chill, o29 Woolmark, o32 Permanent Style, o33 Wikipedia Corduroy, o35 Wikipedia Mohair,
  o47 Lenzing, o50 Contiki. The other sources were fetched and read directly.
- o2's title is descriptive: the Inquirer headline wasn't captured, but URL, author and date are exact.
- o28 is a CDC tip sheet reprinted by the Kentucky High School Athletic Association. The current cdc.gov heat pages
  I fetched didn't repeat the clothing line ("wear loose, lightweight, light-colored clothing"), but NHS (2026) gives the
  same advice in short form: "a hat and light clothes" [o27].
- o5 (Emily Post) and several Wikipedia pages are undated. The dates shown elsewhere are publication or last-updated dates as stated on the page.
- The NWS brochure [o24] repeats the "40% of body heat is lost through the head" claim. I didn't use it, because it's a known overstatement.
- No brand names appear in user-facing text. Brand names appear only in `match` keywords (e.g. "gore-tex", "tencel",
  "primaloft", "cordura") so that users' free-text materials are recognised.

## Full source list

| id | Title | Publisher | Date | URL |
|---|---|---|---|---|
| o1 | Deciphering Dress Codes: Defining "Business Casual" in 2025 | Harry Rosen (Logan Ross) | 2025-03-31 | https://harryrosen.com/en/editorial/deciphering-dress-codes-defining-business-casual-in-2025 |
| o2 | Workplace dress codes go casual in Philadelphia offices (descriptive title) | The Philadelphia Inquirer (Erica Lamberg) | 2025-06-18 | https://inquirer.com/business/workplace-casual-dress-code-philadelphia-20250618.html |
| o3 | What to wear to the office now | The Globe and Mail (Truc Nguyen) | 2024-03-14, updated 2026-05-25 | https://theglobeandmail.com/life/style/article-what-to-wear-to-the-office-now |
| o4 | Your complete guide to smart casual work attire | SEEK | 2026-09-07 | https://au.seek.com/career-advice/article/your-complete-guide-to-smart-casual-work-attire |
| o5 | Attire Guide: Dress Codes from Casual to White Tie | The Emily Post Institute | n.d. | https://emilypost.com/advice/attire-guide-dress-codes-from-casual-to-white-tie |
| o6 | Deconstructing Dress Codes | Debrett's | © 2025 | https://debretts.com/deconstructing-dress-codes/ |
| o7 | What to Wear to an Interview (And What Not to Wear) | Coursera | 2026-07-06 | https://www.coursera.org/articles/what-to-wear-to-an-interview |
| o8 | How To Return To The Office In Style | MR PORTER (Derek Guy) | 2025-09 | https://www.mrporter.com/en-us/journal/fashion/stylish-workwear-mens-25325350 |
| o9 | A step-by-step guide on what to wear on a first date | Glamour | 2025-03-18 | https://www.glamour.co.za/fashion/a-step-by-step-guide-on-what-to-wear-on-a-first-date-cb0d12c9-f414-4ad0-b0db-d3290c42606b |
| o10 | What to wear on a long-haul flight, according to a stylist | Luxury London (Cordelia Aspinall; stylist Oriona Robb) | 2026-09-30 | https://luxurylondon.co.uk/style/hers/what-to-wear-on-a-long-haul-flight-according-to-a-stylist/ |
| o11 | What Stylists Pack: Eight Fail-Safe Airport Outfits To Travel In | Broadsheet | n.d. | https://www.broadsheet.com.au/national/fashion/article/stylist-airport-outfits |
| o12 | D.C. restaurants where dress codes are back | Washingtonian | 2022-12-01 | https://washingtonian.com/2022/12/01/dc-restaurants-where-dress-codes-are-back/ |
| o13 | Think before you pack for this year's Festival | Glastonbury Festival | 2023-06-04 | https://www.glastonburyfestivals.co.uk/think-before-you-pack-for-this-years-festival/ |
| o14 | Royal Garden Parties | Greater London Lieutenancy | n.d. | https://greaterlondonlieutenancy.com/helping-you/hmqs-garden-parties/ |
| o15 | What to Wear to a Beach Formal Wedding | The Knot | n.d. | https://www.theknot.com/content/what-to-wear-beach-formal |
| o16 | Morning dress | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Morning_dress |
| o17 | Get running with Couch to 5K | NHS | n.d. | https://www.nhs.uk/live-well/exercise/running-and-aerobic-exercises/get-running-with-couch-to-5k/ |
| o18 | How to Layer Clothes for Every Season | Nike (Hannah Singleton) | 2024-10-14 | https://www.nike.com/au/a/how-to-layer-clothes |
| o19 | Running clothes for different temperatures | Brooks Running | n.d. | https://www.brooksrunning.com/en_ie/blog/gear-stories/running-clothes-for-different-temperatures.html |
| o20 | Temperature versus 'feels like': why they are not always the same | Met Office | 2026-05-12 | https://www.metoffice.gov.uk/blog/2026/temperature-versus-feels-like-why-they-are-not-always-the-same |
| o21 | Wind chill factor | Met Office | n.d. | https://weather.metoffice.gov.uk/learn-about/weather/types-of-weather/wind/wind-chill-factor |
| o22 | Beaufort wind force scale | Met Office | n.d. | https://weather.metoffice.gov.uk/guides/coast-and-sea/beaufort-scale |
| o23 | Beaufort scale | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Beaufort_scale |
| o24 | Wind Chill brochure (Alaska Region) | US National Weather Service | 2005-09-15 | https://www.weather.gov/media/arh/brochures/WindChillTempIndexBro.pdf |
| o25 | How to use the UV index | Environment and Climate Change Canada | 2026-04-20 | https://www.canada.ca/en/environment-climate-change/services/weather-health/uv-index-sun-safety/how-to-use.html |
| o26 | Dress to Protect: 5 Things That Affect How Well Your Clothes Block UV Rays | The Skin Cancer Foundation (Ali Venosa) | 2023-08-09 | https://www.skincancer.org/blog/dress-to-protect-5-things-that-affect-how-well-your-clothes-block-uv-rays/ |
| o27 | Heatwave: how to cope in hot weather | NHS | reviewed 2026-06-12 | https://www.nhs.uk/live-well/seasonal-health/heatwave-how-to-cope-in-hot-weather/ |
| o28 | Tips for preventing heat-related illness (CDC tip sheet, reprinted) | CDC via KHSAA | n.d. | https://khsaa.org/sportsmedicine/heat/cdctipsforpreventingandmanagingheat.pdf |
| o29 | Wool performance: breathable and thermoregulating | The Woolmark Company | n.d. | https://www.woolmark.com/es-mx/performance/ |
| o30 | Linen | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Linen |
| o31 | Seersucker Guide: The Fabric, The Suits & Its Origins | Gentleman's Gazette (E.J. Daniels) | 2023-04-19 | https://www.gentlemansgazette.com/seersucker-fabric-suits-origins/ |
| o32 | The guide to denim and chambray shirtings | Permanent Style | 2018-04 | https://www.permanentstyle.com/2018/04/the-guide-to-denim-and-chambray-shirtings |
| o33 | Corduroy | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Corduroy |
| o34 | Word of the Week: Practicality | Harris Tweed Authority | n.d. | https://www.harristweed.org/journal/word-of-the-week-practicality-deisealachd/ |
| o35 | Mohair | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Mohair |
| o36 | Cashmere care guide | THE OUTNET | 2026-07-13 | https://www.theoutnet.com/blogs/news/cashmere-care-guide |
| o37 | Alpaca fiber | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Alpaca_fiber |
| o38 | Flannel | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Flannel |
| o39 | Down feather | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Down_feather |
| o40 | Silk | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Silk |
| o41 | Satin | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Satin |
| o42 | Velvet | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Velvet |
| o43 | Jersey (fabric) | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Jersey_(fabric) |
| o44 | Polar fleece | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Polar_fleece |
| o45 | Acrylic fiber | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Acrylic_fiber |
| o46 | Rayon | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Rayon |
| o47 | TENCEL Intimate leaflet (lyocell and modal moisture management) | Lenzing | 2022-07-14 | https://b2b.tencel.com/images/assets/downloads/TENCEL_Intimate_Leaflet_EN_V1.6_Secured_20220714.pdf |
| o48 | Protect your GORE-TEX: DWR care | Arc'teryx | n.d. | https://arcteryx.com/us/en/help/product-care/protect-gore-tex |
| o49 | Suede | Wikipedia | n.d. | https://en.wikipedia.org/wiki/Suede |
| o50 | How to find an outfit for a long-haul flight | Contiki Six Two | n.d. | https://www.contiki.com/six-two/article/how-to-find-outfit-for-a-long-haul-flight/ |
