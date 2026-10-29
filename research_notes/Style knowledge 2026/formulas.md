# Outfit formulas: research notes

Companion notes for `formulas.json` (Hanger on-device style knowledge base). Researched 29 October 2026.

## What is in the file

- **125 formulas** and **53 sources**. Every formula cites at least one source, and every source is used.
- Occasion tags (a formula can carry several): CASUAL 72, SMART 45, WORK 32, DATE 35, FORMAL 11, ACTIVE 8, LOUNGE 6, TRAVEL 11.
- Weather: 26 tagged SUMMER; 46 cold-weather formulas (AUTUMN/WINTER with a required OUTERWEAR slot or a warm LAYER: knit, cardigan, fleece, gilet, zip hoodie); 37 have an empty season list (all year).
- Rain-ready formulas (Raincoat or Trench coat plus Rain boots or Boots): f076, f086, f120, f121, f122. Related wet-weather formulas use other shoes: f106 (raincoat + hiking boots), f124 (trench + flats) and f125 (dress + rain boots, no coat).
- Formality spans 1 (active kit, lounge) to 5 (black tie, evening gown).
- Validation: every token is checked against the shared vocabulary. Each formula has a core (TOP+BOTTOM or ONE_PIECE) plus SHOES, at most 6 slots, no repeated category within a formula, and no two formulas share an identical slot set. Every `why` is 110 characters or fewer. `python3 -m json.tool` passes.

## How the formulas were built

- I read each source article itself (not just search snippets), except where noted below, and took formulas that editors or stylists present as repeatable outfit formulas or uniforms.
- Wearer language in sources (for example "men's", "women", "fashion girl") was dropped. Formulas describe pieces only. Several source titles contain gendered words and are quoted verbatim in `sources[].title` for accurate citation; if the app surfaces citations, showing the publisher alone avoids that.
- Colour is constrained only where it is the point (white tee, navy blazer, camel coat, black-tie colours, the black-and-pale contrasts). Otherwise `colors` is empty.
- `types` are always constrained for SHOES, because an empty list would let running shoes or rain boots satisfy a tailoring formula. TOP is left open (`types: []`) when a knit LAYER is the visible piece, because the vocabulary puts sweaters in LAYER and the core still needs a TOP.
- Nice-to-have pieces (belt, scarf, bag, an over-layer that only some sources mention) are marked `optional: true`.
- Brand names in source formulas (Birkenstocks, Converse, branded knit sets) were turned into generic types.

## Things the slot model cannot express (handled approximately)

- **Pattern.** Slots have no pattern field. The Breton stripe (f044, f031), plaid (f040), checked shirts (f061) and colourful prints (f098) are approximated by colour or left to the note. If slots ever gain a `patterns` field, f044 should take STRIPE and f040 CHECK.
- **Fabric.** Linen, leather, satin, suede, tweed, cashmere and Shetland are not encodable. Leather trousers/skirts -> Trousers/Skirt; satin slip skirt -> Skirt/Midi skirt; suede jacket -> Leather jacket/Jacket (f064); tweed jacket -> Blazer (f066, f068).
- **Same colour across slots.** Suits (f070, f077, f092, f094, f097, f100), co-ords (f017), matching sweatsuits (f104) and tonal looks (f045, f123) want the jacket and trousers (or top and bottom) in one colour. The JSON can only list allowed colours per slot. A matcher could add a bonus when the LAYER/TOP and BOTTOM colours match in those formulas.
- **Hosiery.** Tights matter in f031, f066, f081 and f088 but there is no type for them; the `why` text mentions them where relevant.
- **Black-tie details.** Cummerbund or waistcoat, studs, cufflinks and pocket square do not fit in 6 slots; bow tie -> Tie (BLACK).
- **Draped layers.** A knit tied over the shoulders (f015, f050) is an optional LAYER or omitted.
- **Brief deviation.** The brief's sample used GREY/CHARCOAL trousers under a navy blazer. Put This On explicitly says charcoal lacks contrast against dark jackets (f38, f39), so f052 uses GREY/LIGHT_GREY. Tops include Polo because f38 allows long-sleeved polos with a navy sport coat.

### Type mappings used (source wording -> vocabulary type)

| Source wording | Mapped to |
|---|---|
| sport coat, odd jacket, tweed jacket, dinner jacket, suit jacket | LAYER Blazer |
| suit trousers, slacks, flannels, worsteds, leather or silk trousers, capris | BOTTOM Trousers (silky/wide styles -> Wide-leg trousers) |
| khakis | BOTTOM Chinos (KHAKI/BEIGE) |
| sweatpants, track pants | BOTTOM Joggers |
| fatigues | BOTTOM Cargo trousers |
| Bermuda shorts | BOTTOM Tailored shorts |
| slip skirt, miniskirt, pencil skirt, maxi skirt, plaid skirt | BOTTOM Skirt / Midi skirt |
| camisole, shell, corset top, lace or silk tank | TOP Tank top (or Blouse) |
| OCBD / oxford button-down, button-down | TOP Shirt |
| knit polo, long-sleeved polo | TOP Polo |
| sports bra, activewear set top | TOP Sports top |
| base layer | TOP Long-sleeve top |
| jumper, Shetland, merino, cable knit, Aran | LAYER Knit sweater |
| chore coat, shacket | LAYER Overshirt |
| puffer vest | LAYER Gilet |
| moto/biker jacket, leather trench, suede jacket | OUTERWEAR Leather jacket |
| field jacket, waxed jacket | OUTERWEAR Jacket |
| trucker jacket | OUTERWEAR Denim jacket |
| topcoat, overcoat, Paletot, car coat, scarf coat, faux-fur coat | OUTERWEAR Coat |
| quilted coat | OUTERWEAR Puffer jacket (or Coat) |
| Mackintosh | OUTERWEAR Trench coat |
| mountain parka | OUTERWEAR Parka |
| pumps, stilettos, kitten heels, heeled mules, heel sandals | SHOES Heels |
| ballet/ballerina flats, slingback flats, block-heel flats | SHOES Flats |
| sliders/slides, fisherman sandals, huaraches, clogs, jelly sandals | SHOES Sandals |
| boat shoes, penny loafers | SHOES Loafers |
| derbies, oxfords, monk straps | SHOES Dress shoes |
| chukkas, lug-sole/biker/knee-high/Western boots | SHOES Boots (Chelsea boots where sleeker) |
| booties, heeled booties | SHOES Ankle boots |
| trainers, high-tops, dad sneakers | SHOES Sneakers |
| trail sneakers | SHOES Running shoes / Hiking boots |
| evening bag, mini bag | BAG Clutch (or Handbag) |
| shoulder bag, hobo bag, top-handle bag | BAG Handbag / Crossbody |
| basket bag, net bag, woven tote | BAG Tote |
| fanny pack | BAG Belt bag |
| bow tie | ACCESSORY Tie |
| pearls, pendant | ACCESSORY Necklace |
| dark-wash denim | NAVY |
| cream/ecru denim | CREAM |
| midnight blue | NAVY |
| oxford grey | CHARCOAL |
| off-white | CREAM |

## Source access and limits

- **Blocked to this research tool:** vogue.com, gq.com, esquire.com, harpersbazaar.com, elle.com, thecut.com, instyle.com, glamourmagazine.co.uk, cntraveler.com, travelandleisure.com, brides.com, self.com and runnersworld.com refuse this crawler, so none of them is cited even where they are the classic authority.
- **Unreachable:** mrporter.com, net-a-porter.com, rei.com and theknot.com returned 403. Debrett's dress-code pages are paywalled or returned 503, so Debrett's is reached through Wikipedia's Black tie article (f33). The Stylist office-formula piece is paywalled. Permanent Style served a bot challenge, so f42 rests on a search-engine summary of the article. It is the only source not read in full, and it only corroborates f052.
- **Search budget:** the shared web-search budget ran out partway through. Later sourcing used only article URLs already found or linked from fetched pages.
- **Source balance:** Who What Wear (US and UK) supplies 32 of the 53 sources, because it was the most reliably readable editor outlet. Menswear-associated tailoring and knitwear rest on Put This On (Derek Guy, Jesse Thorn) and Gentleman's Gazette. Heat dressing adds InsideHook and HELLO!; everyday formulas add The Gloss and Eleven Loves.
- **Cross-checking:** most formulas cite two or more sources. Single-source formulas are mostly specific editor formulas (for example f013, f043, f049) or narrow dress-code rules from one specialist guide (f097, f098). They are as sound as their one source; treat them as lower-confidence if ranking matters.
- **Dates:** several Who What Wear pieces are years old but updated. The date shown is the published date, or "updated" where only the update date was reliable.

## Not covered

- White tie (no tailcoat type), ski and snow sports, swimwear and beach cover-ups, and cultural or religious dress codes.
- Overalls and playsuits: no repeatable formula in the sources I could read.
- Retailer style guides (Uniqlo, COS, Mr Porter): Mr Porter returned 403 and the search budget ran out before others could be found.

## Sources

| id | Title | Publisher | Date | Used by |
|---|---|---|---|---|
| f1 | [9 Basic Outfits Our Editors Wear Over and Over Again](https://www.whowhatwear.com/easy-basic-outfits) | Who What Wear (Nicole Akhtarzad Eshaghpour) | 2020-02-06 | 7: f001, f002, f003, f005, f006, f047, f082 |
| f2 | [Outfit Repeat: The 8 Simple Looks We Wear More Than Any Others](https://www.whowhatwear.com/easy-winter-outfit-ideas) | Who What Wear (Bobby Schuessler) | 2020-10-29 | 8: f003, f007, f008, f033, f045, f048, f071, f110 |
| f3 | [The 3-Piece Outfit Formula Our Editors Always Wear to the Office](https://www.whowhatwear.com/editor-office-outfits) | Who What Wear (Kristen Nichols) | 2019-04-07 | 1: f071 |
| f4 | [The Easy Outfit Every Single Fashion Girl Wears](https://www.whowhatwear.com/most-versatile-outfit-formula) | Who What Wear (Allyson Payer) | 2017-02-16 | 2: f002, f067 |
| f5 | [I'm a Minimalist Fashion Editor: 6 Classic Outfit Formulas I Come Back to Time and Time Again](https://www.whowhatwear.com/fashion/outfit-ideas/classic-outfit-formulas) | Who What Wear (Chloe Gallacher) | 2025-12-21 | 8: f002, f003, f010, f022, f023, f024, f026, f073 |
| f6 | [I'm Convinced These 7 Outfit Combos Are the Secret to Looking Put-Together With Very Little Effort](https://www.whowhatwear.com/fashion/outfit-ideas/put-together-fall-outfit-formulas-2026) | Who What Wear (Michelle Scanga) | 2026-10-10 | 5: f007, f040, f050, f051, f072 |
| f7 | [5 Outfits With Tops, Jeans and Shoes Fashion People Will Be Rotating This Season](https://www.whowhatwear.com/fashion/outfit-ideas/denim-outfit-formulas-2026) | Who What Wear (Katie Sims) | 2026-02-19 | 4: f013, f014, f015, f016 |
| f8 | [From Paris to London: The Fall Outfit Formulas European Fashion People Are Wearing Right Now](https://www.whowhatwear.com/fashion/shopping/european-outfit-formulas-fall-2026) | Who What Wear (Audry Hiaoui) | 2026-10-22 | 2: f043, f049 |
| f9 | [The Chicest Women in Paris Always Reach For These Outfit Formulas When Fall Arrives](https://www.whowhatwear.com/fashion/outfit-ideas/paris-fall-transition-outfit-formulas) | Who What Wear (Sierra Mayhew) | 2026-09-05 | 5: f012, f041, f042, f044, f064 |
| f10 | [I'm a Fashion Editor, and I Swear By This 3-Piece Outfit Formula](https://www.whowhatwear.com/adult-outfit-formula) | Who What Wear (Chinazor 'Chichi' Offor) | 2022-11-29 | 1: f009 |
| f11 | [The Easy Two-Part Outfit Every Editor on Our Team Swears By (sweater and slip skirt)](https://www.whowhatwear.com/jumper-and-slip-skirt-outfits) | Who What Wear UK (Maxine Eggenberger) | 2022-08-05 | 2: f027, f028 |
| f12 | [My Friends and I All Live in Chunky Sweaters: 5 Outfits We Want to Try Now](https://www.whowhatwear.com/chunky-sweater) | Who What Wear (Bobby Schuessler) | 2018-12-12 | 3: f027, f028, f029 |
| f13 | [11 Easy Ways to Make Your Favourite Dress-and-Boots Outfits Feel Fresh](https://www.whowhatwear.co.uk/dresses-and-boots-winter-outfits) | Who What Wear UK (Jess Kelham-Hohler) | 2023-11-21 | 1: f030 |
| f14 | [I'm a Fashion Editor: 6 Easy Yet Impressive Outfits I'll Rely On This Winter](https://whowhatwear.com/what-to-wear-10-degree-weather) | Who What Wear UK (Harriet Davey) | 2022-10-19 | 5: f030, f031, f032, f033, f035 |
| f15 | [I Live in NYC, so I Know How to Dress for the Rain: 6 Outfit Formulas I Recommend](https://www.whowhatwear.com/rainy-day-outfits) | Who What Wear (Sierra Mayhew) | 2022-10-26 | 4: f076, f086, f120, f123 |
| f16 | [The Cutest Outfits to Wear on a Rainy Fall Day](https://www.whowhatwear.com/fall-rain-boots-outfits) | Who What Wear (Eva Thomas) | 2018-09-26 | 3: f120, f121, f125 |
| f17 | [Here Are 15 Outfit Ideas For When The Forecast Is Nothing But Rain](https://www.whowhatwear.com/winter-rainy-day-outfits) | Who What Wear (Yusra Siddiqui) | 2018-11-05 | 3: f034, f120, f122 |
| f18 | [12 Ways Women in Paris, London, and NYC Dress for Rainy Summer Days](https://www.whowhatwear.com/fashion/outfit-ideas/summer-rainy-day-outfits) | Who What Wear (Jasmine Fox-Suliaman) | 2024-07-14 | 2: f124, f125 |
| f19 | [Q and Answer: How Should I Dress in the Rain?](https://putthison.com/q-and-answer-how-should-i-dress-in-the-rain/) | Put This On (Jesse Thorn) | 2010-10-11 | 3: f076, f106, f122 |
| f20 | [5 Easy Summer Outfits to See You Through the Hot Weather This Week](https://www.whowhatwear.co.uk/what-to-wear-in-a-heatwave) | Who What Wear UK (Remy Farrell) | 2020-06-22 (updated 2025-08-11) | 6: f017, f018, f019, f020, f021, f074 |
| f21 | [What to Wear When It's Way Too Hot Outside, According to a Style Editor](https://www.insidehook.com/style-guides/wear-hot-outside-according-style-editor) | InsideHook (Paolo Sandoval) | 2026-07-13 | 1: f018 |
| f22 | [I'm a 49-year-old fashion editor - this is what I wear to the office in a heatwave](https://www.hellomagazine.com/fashion/913046/heatwave-office-outfits/) | HELLO! (Becky Donaldson, Head of Fashion) | 2026-07-17 | 4: f019, f062, f063, f075 |
| f23 | [It's Cuffing Season! 9 Date-Night Outfit Ideas That Will Come in Handy](https://www.whowhatwear.com/date-night-outfits) | Who What Wear (Jasmine Fox-Suliaman) | updated 2024-12-23 | 9: f066, f078, f079, f080, f081, f084, f085, f086, f099 |
| f24 | [I Think These Are the Chicest Outfits to Wear With Jeans for Date Night](https://www.whowhatwear.com/date-night-outfits-with-jeans) | Who What Wear (Bobby Schuessler) | 2022-03-23 | 6: f001, f067, f080, f087, f089, f090 |
| f25 | [Baby, It's Cold Outside! 24 Date-Night Outfits You Can Actually Wear in Winter](https://www.whowhatwear.com/winter-date-night-outfits) | Who What Wear (Jasmine Fox-Suliaman) | 2017-11-19 | 4: f028, f081, f088, f091 |
| f26 | [Take It From a Woman: Date-Night Outfits That Actually Hit](https://www.insidehook.com/take-it-from-a-woman/good-date-night-outfits) | InsideHook (Dani Brand, with stylist Sreya Halder) | 2026-10-14 | 3: f006, f082, f083 |
| f27 | [The Dos and Don'ts of Cocktail Attire, According to Fashion Editors](https://www.whowhatwear.com/cocktail-attire-for-women) | Who What Wear (Lauren Eggertsen) | updated 2026-04-15 | 4: f091, f095, f096, f098 |
| f28 | [Cocktail Attire For Men - Dress Code Guide For Weddings, Parties & Events](https://www.gentlemansgazette.com/cocktail-attire-for-men/) | Gentleman's Gazette (Sven Raphael Schneider) | 2024-04-17 | 2: f070, f097 |
| f29 | [Black Tie Dress Code Explained](https://www.gentlemansgazette.com/tuxedo-black-tie-guide/etiquette/black-tie-dress-code/) | Gentleman's Gazette (Peter Marshall) | 2018-11-21 | 3: f092, f094, f100 |
| f30 | [Black Tie Optional Dress Code Explained](https://www.gentlemansgazette.com/tuxedo-black-tie-guide/etiquette/black-tie-optional-dress-code/) | Gentleman's Gazette (Sven Raphael Schneider) | 2022-07-20 | 1: f094 |
| f31 | [Black Tie Dress Code for Women - Ladies' Evening Wear](https://www.gentlemansgazette.com/tuxedo-black-tie-guide/advanced/black-tie-white-women/) | Gentleman's Gazette | 2018-12-05 | 2: f093, f099 |
| f32 | [Tuxedo Overcoats, Hats, Scarves & Black Tie Outerwear](https://www.gentlemansgazette.com/tuxedo-black-tie-guide/classic/tuxedo-overcoats-hats-gloves/) | Gentleman's Gazette (Sven Raphael Schneider) | 2018-11-27 | 1: f100 |
| f33 | [Black tie](https://en.wikipedia.org/wiki/Black_tie) | Wikipedia (summarises Debrett's guidance) | accessed 2026-10-29 | 2: f092, f093 |
| f34 | [Lounge suit](https://en.wikipedia.org/wiki/Lounge_suit) | Wikipedia | accessed 2026-10-29 | 2: f070, f072 |
| f35 | [Smart Casual Dress Code For Men Explained](https://www.gentlemansgazette.com/smart-casual-dress-code/) | Gentleman's Gazette (Sven Raphael Schneider) | 2018-04-16 | 3: f061, f062, f063 |
| f36 | [Business Casual Attire for Men](https://www.gentlemansgazette.com/business-casual-men/) | Gentleman's Gazette (Sven Raphael Schneider) | 2024-01-03 | 6: f055, f056, f057, f058, f061, f073 |
| f37 | [Navy Serge Blazer Paired With Grey Worsted Trousers](https://www.gentlemansgazette.com/navy-blazer-grey-flannel-trousers/) | Gentleman's Gazette (Sven Raphael Schneider) | 2012-01-07 | 2: f052, f060 |
| f38 | [The Springboard Wardrobe](https://putthison.com/the-springboard-wardrobe/) | Put This On (Derek Guy) | 2021-07-21 | 12: f004, f005, f016, f037, f038, f052, f053, f054, f055, f059, f060, f077 |
| f39 | [How To Do Business Casual Without Looking Like A Schmuck](https://putthison.com/how-to-do-business-casual-without-looking-like-a/) | Put This On (Derek Guy) | 2017-01-04 | 7: f023, f052, f053, f054, f057, f070, f077 |
| f40 | [A Basic Guide To Useful Knitwear](https://putthison.com/a-basic-guide-to-useful-knitwear/) | Put This On (Derek Guy) | 2020-10-29 | 5: f004, f036, f058, f059, f065 |
| f41 | [The Most Versatile Sweater](https://putthison.com/the-most-versatile-sweater/) | Put This On (Derek Guy) | 2018-10-15 | 2: f036, f059 |
| f42 | [Adding interest to a navy blazer and grey trousers](https://www.permanentstyle.com/2021/08/adding-interest-to-a-navy-blazer-and-grey-trousers.html) | Permanent Style | 2021-08 | 1: f052 |
| f43 | [Every Perfect Airport Outfit Has These 4 Components](https://www.whowhatwear.com/airport-outfits) | Who What Wear (Nicole Akhtarzad Eshaghpour) | 2017-06-13 | 2: f113, f115 |
| f44 | [6 Editor-Approved Airport Outfits That'll Double Your Chances of an Upgrade](https://www.whowhatwear.com/airport-outfit-ideas) | Who What Wear UK (Maxine Eggenberger) | 2022-08-21 | 4: f111, f113, f114, f115 |
| f45 | [I Travel 5 Times a Year: These Easy Outfits Have Never Once Failed Me](https://www.whowhatwear.com/travel-outfits) | Who What Wear (Lauren Adhav) | updated 2024-12-24 | 7: f103, f104, f112, f116, f117, f118, f119 |
| f46 | [I Go to the Gym Every Morning: These Athleisure Outfits Get Me There in Style](https://www.whowhatwear.com/athleisure-outfits) | Who What Wear (Sierra Mayhew) | 2022-01-03 | 7: f034, f101, f102, f103, f107, f108, f109 |
| f47 | [These Hiking Outfits Are Peak Off-Duty Style](https://www.whowhatwear.com/hiking-outfits) | Who What Wear (Sierra Mayhew) | 2021-08-12 | 4: f103, f105, f106, f107 |
| f48 | [Since Moving to L.A. I've Been Hiking Nonstop: See the Cute Pieces I've Collected](https://www.whowhatwear.com/what-to-wear-hiking) | Who What Wear (Indya Brown) | 2018-05-15 | 1: f105 |
| f49 | [I'm 59 Years Old, and I Consistently Wear These 5 Outfits With Sweatshirts](https://www.whowhatwear.com/loungewear-outfits) | Who What Wear (Bobby Schuessler, with Janet Gunn) | 2020-12-02 | 3: f045, f046, f104 |
| f50 | [I'm an A-List Celeb Stylist: Here's How to Style a Puffer Jacket Right](https://www.whowhatwear.com/puffer-jacket-outfits) | Who What Wear (Ana Escalante, with stylist Karla Welch) | 2018-11-16 (updated 2023) | 1: f035 |
| f51 | [7 Simple Outfit Formulas That Always Look Ultra Elegant](https://thegloss.ie/7-simple-spring-outfit-formulas-that-always-look-elegant/) | The Gloss (Sarah Macken) | 2026-06-02 | 7: f002, f010, f011, f012, f025, f068, f069 |
| f52 | [5 go-to outfit formulas for effortless style](https://elevenloves.co.uk/blogs/news/5-go-to-outfit-formulas-for-effortless-style) | Eleven Loves (Hayley Thomas) | 2026-03-14 | 3: f001, f009, f039 |
| f53 | [7 Celebrity Outfit Formulas That Will Make You Look Like You Have a Personal Stylist](https://www.whowhatwear.com/fashion/outfit-ideas/celebrity-outfits-formulas-summer-2026) | Who What Wear (Audry Hiaoui) | 2026-08-14 | 2: f078, f095 |

## Formula by formula: sources and mapping notes

| id | Formula | Occasions | Seasons | Formality | Sources | Notes |
|---|---|---|---|---|---|---|
| f001 | White tee, straight jeans, white sneakers | CASUAL | all year | 2 | f1, f24, f52 | Source wording: "jeans and a white tee" (f1), "a white-tee-and-jeans ensemble is no-fail" (f24), jeans and a quality T-shirt (f52). "Straight jeans" -> Jeans. |
| f002 | Blazer, white tee, jeans, loafers | CASUAL, SMART | all year | 2-3 | f4, f1, f51, f5 | "A blazer, tee or blouse, jeans, and polished shoes", e.g. cropped flares and loafers (f4); "straight-leg jeans, a white T-shirt, and a blazer, with a silk scarf" (f51); blazer + jeans (f5). |
| f003 | Blazer, simple top, jeans, ankle boots | CASUAL, SMART, WORK | AUTUMN, WINTER | 2-3 | f2, f1, f5 | "Jeans, ankle boots, a basic top, and a great blazer" (f2); "turtleneck and jeans worn with a blazer and ankle boots" (f1); blazer + jeans with a heeled ankle boot (f5). |
| f004 | Grey sweatshirt, jeans or chinos, sneakers | CASUAL | SPRING, AUTUMN, WINTER | 2 | f38, f40 | Put This On calls the grey cotton sweatshirt the "workhorse knit", worn with jeans, chinos or fatigues and under field, leather or chore jackets (f38, f40). Fatigues -> Cargo trousers. |
| f005 | Sweatshirt, leather jacket, black jeans, boots | CASUAL | SPRING, AUTUMN | 2 | f1, f38 | "A sweatshirt, moto jacket, black jeans, and boots" (f1). Moto jacket -> Leather jacket. |
| f006 | Leather jacket, white tee, jeans, ankle boots | CASUAL, DATE | SPRING, AUTUMN | 2-3 | f1, f26 | "Rigid, high-rise jeans... a crisp white tee, basic ankle boots, and a leather jacket" (f1); leather jacket over an office jacket for dates (f26). |
| f007 | Cardigan, white tee, jeans | CASUAL | SPRING, AUTUMN | 2 | f2, f6 | "A cardigan with a white tee and jeans" plus a gold necklace (f2); cardigan + jeans + block-heel ballet flats + oversize bag (f6). Block-heel ballet flats -> Flats. |
| f008 | Camel coat, knit, jeans, loafers or boots | CASUAL, SMART | AUTUMN, WINTER | 2-3 | f2 | "Camel coat... jeans, a sweater or cute top, and loafers or boots" (f2). Sweaters are LAYER in the vocabulary, so the knit is optional and the TOP is left open. |
| f009 | Button-down shirt, jeans, loafers | CASUAL, SMART | all year | 2-3 | f10, f52 | "A button-down shirt, jeans, and loafers" (f10); jeans and a nice top with a belt (f52). |
| f010 | V-neck jumper over a tee, jeans | CASUAL | SPRING, AUTUMN, WINTER | 2 | f5, f51 | "V-neck jumper + jeans", fine-gauge in spring, chunky in winter, straight or barrel-leg jeans (f5); navy V-neck knit (f51). Barrel-leg -> Jeans. |
| f011 | White jeans, white tee, navy knit | CASUAL, SMART | SPRING, SUMMER | 2-3 | f51 | "White jeans, a white T-shirt, and a navy V-neck knit" (f51). Shoes not named in the source; loafers, flats or trainers added. |
| f012 | Trench coat, jeans, ballet flats or loafers | CASUAL, SMART | SPRING, AUTUMN | 2-3 | f51, f9 | "A trench coat, jeans, and elevated ballerina flats" (f51); "short trench + straight-leg jeans + loafers" (f9). Ballerina flats -> Flats. |
| f013 | Pale jeans, black tee, black sandals | CASUAL | SUMMER | 2 | f7 | "Cream denim... a simple black tee, and the black flip flops tie everything together" (f7). Cream/ecru denim -> CREAM. |
| f014 | White shirt, straight jeans, sandals | CASUAL | SPRING, SUMMER | 2 | f7 | "Straight-leg jeans + white shirt + sliders" (f7). Sliders/slides -> Sandals. |
| f015 | White tank, dark jeans, knit over the shoulders | CASUAL | SPRING, SUMMER | 2 | f7 | "Dark denim + white tank + layered knit" draped over the shoulders (f7). Dark-wash denim -> NAVY. A knit worn over the shoulders cannot be expressed, so it is an optional LAYER. |
| f016 | Double denim: denim jacket and jeans | CASUAL | SPRING, AUTUMN | 2 | f7, f38 | "Double-denim co-ord": wide-leg jeans + oversized denim jacket (f7); trucker jackets with raw denim (f38). Trucker -> Denim jacket. The tee is my addition as the base top. |
| f017 | Linen shirt and shorts, sandals | CASUAL | SUMMER | 2 | f20 | "Linen co-ord + Birkenstocks": a light shirt and matching shorts (f20). Matching fabric/colour and linen cannot be encoded; branded sandals -> Sandals. |
| f018 | Linen tee, loose shorts, sandals, sunglasses | CASUAL, LOUNGE | SUMMER | 1-2 | f21, f20 | "Loose linen tees", voluminous lounge shorts, "grown-up flip-flops" or fisherman sandals/huaraches, statement sunglasses (f21). Fisherman sandal/huarache -> Sandals. |
| f019 | Tank top, white midi skirt, sandals | CASUAL, SMART | SUMMER | 2-3 | f20, f22 | "White skirt + tank top" (white midi skirt) (f20); an ankle-length white linen skirt with a matching tank for smarter days (f22). |
| f020 | Simple black dress, minimal sandals | CASUAL, DATE | SUMMER | 2-3 | f20 | "Simple black dress + minimal sandals", a day-to-night option (f20). |
| f021 | Mini dress, flats, sunglasses | CASUAL | SPRING, SUMMER | 2 | f20 | "A-line mini + sunglasses": a mini shift dress worn with ballet flats, sandals, trainers or flip-flops (f20). Shift dress -> Dress. |
| f022 | Midi dress, loafers, trench | CASUAL, WORK, SMART | SPRING, AUTUMN | 2-3 | f5 | "Midi dress + loafers" with a sleek trench (f5). |
| f023 | Tailored trousers, tee, trainers | CASUAL, SMART | all year | 2-3 | f5, f39 | "Tailored trousers + trainers" with a tucked-in tee or cosy knit (f5); plain sneakers with soft, casual tailoring (f39). |
| f024 | White tee, black tailored trousers, pointed shoes | SMART, WORK, DATE | all year | 3 | f5 | "White T-shirt + tailored trousers": a boxy white tee, tapered black trousers and a pointed heel (f5). Pointed heel -> Heels; Flats added as the pointed-flat alternative. |
| f025 | Cream blazer, white tee, tailored trousers, tote | WORK, SMART | SPRING, SUMMER | 3 | f51 | "Tailored trousers, a cream blazer, and a white T-shirt, with an oversized leather tote" (f51). Shoes not named in the source. |
| f026 | Cardigan, leather skirt, knee-high boots | CASUAL, WORK, DATE | AUTUMN, WINTER | 2-3 | f5 | "Cardigan + leather skirt" with knee-high boots, office to drinks (f5). Leather cannot be encoded; knee-high boots -> Boots. |
| f027 | Chunky knit, slip skirt, boots | CASUAL, DATE | AUTUMN, WINTER | 2-3 | f11, f12 | Sweater + slip skirt, the two-part editor formula, dressed up with knee-high boots or down with chunky boots (f11); chunky sweater + slip skirt (f12). Jumper -> Knit sweater (LAYER), so TOP is open; slip skirt -> Skirt or Midi skirt. |
| f028 | Slip dress, chunky knit, ankle boots | CASUAL, DATE | AUTUMN, WINTER | 2-3 | f11, f25, f12 | Formula from the brief. Supported by the slip's satin + chunky knit + boots logic (f11, f12) and winterising slip pieces with knit layers (f25). |
| f029 | Chunky knit, wide-leg trousers, boots | CASUAL, SMART | AUTUMN, WINTER | 2-3 | f12 | "Chunky cable-knit sweater half-tucked into wide-leg trousers with simple boots" (f12). |
| f030 | Knit dress, chunky boots | CASUAL, WORK | AUTUMN, WINTER | 2-3 | f14, f13 | "Knit dress + thick tread boots" (f14); knitted dress + biker boots, maxi jumper dress + chunky sole boots (f13). Thick-tread/biker/chunky-sole boots -> Boots, Chelsea boots or Ankle boots. |
| f031 | Jumper, wool coat, knee-high boots, short hem | CASUAL | AUTUMN, WINTER | 2 | f14 | "Striped jumper + wool coat + knee-high boots", worn with shorts or a black miniskirt and tights (f14). Stripe and tights cannot be encoded. |
| f032 | Long coat, knit, tailored trousers | WORK, SMART | AUTUMN, WINTER | 3-4 | f14 | "Longline coat + knit + tailored trousers" (f14). |
| f033 | Camel coat, black turtleneck, black trousers | WORK, SMART, DATE | AUTUMN, WINTER | 3-4 | f2, f14 | Formula from the brief. Supported by the camel-coat formula (f2) and statement coat + knit + tailored trousers (f14). A search summary of a Net-a-Porter Porter "Fashion Memo" piece suggested a black turtleneck, neutral tailored trousers and flat loafers under a camel coat, but the page returned 403, so I did not cite it. |
| f034 | Puffer over a sweatsuit, trainers | LOUNGE, CASUAL | AUTUMN, WINTER | 1-2 | f17, f46 | "Lounge around in a sweatsuit at home and throw a short puffer on to run any errands" (f17); puffers piled over activewear (f46). |
| f035 | Puffer jacket, knit, jeans, boots | CASUAL | WINTER | 2 | f50, f14 | Celebrity stylist Karla Welch: a sleek base layer under a fuller, warmer puffer (f50); quilted coat in cold weather (f14). |
| f036 | Parka, textured knit, cords, boots | CASUAL | WINTER | 2 | f40, f41 | Shetlands "with five-pocket cords and mountain parkas", layered over a long-sleeved (oxford) shirt (f40, f41). Mountain parka -> Parka. |
| f037 | Olive field jacket, grey sweatshirt, jeans | CASUAL | SPRING, AUTUMN | 2 | f38 | "An olive field jacket can be layered over your gray cotton sweatshirt" (f38). Field jacket -> Jacket. |
| f038 | Overshirt, tee, jeans, sneakers | CASUAL | SPRING, AUTUMN | 2 | f38 | Chore coat or trucker as the third casual jacket (f38). Chore coat -> Overshirt (LAYER), per the vocabulary rule for chore jackets. |
| f039 | Denim jacket over a dress, trainers | CASUAL | SPRING, AUTUMN | 2 | f52 | Ribbed dress + denim jacket for casual, with white trainers or ankle boots (f52). Ribbed dress -> Dress or Knit dress. |
| f040 | Shirt, midi skirt, flats | CASUAL, WORK | SPRING, AUTUMN | 2-3 | f6 | "Plaid button-down shirt + midi skirt + flats + leather bag" (f6). Plaid cannot be encoded. |
| f041 | Leather jacket, midi skirt, boots | CASUAL, DATE | SPRING, AUTUMN | 2-3 | f9 | "Leather jacket + midi skirt" (f9). Shoes not named in the source. |
| f042 | Long wool coat, loose jeans, heels | CASUAL, DATE, SMART | AUTUMN, WINTER | 2-3 | f9 | "Long wool coat + loose jeans + pumps" (f9). Pumps -> Heels. |
| f043 | Beige trench, black dress, pointed heels | WORK, DATE, SMART | SPRING, AUTUMN | 3-4 | f8 | "Beige trench coat + black dress + pointed-toe pumps" (Camille Charriere in f8). |
| f044 | Striped tee, long skirt, ballet flats | CASUAL | SPRING, SUMMER | 2 | f9 | "Striped tee + maxi skirt + ballet flats" (f9). Slots have no pattern field, so the Breton stripe is approximated by WHITE/NAVY; maxi skirt -> Skirt or Midi skirt. |
| f045 | Hoodie, oversized blazer, joggers, trainers | CASUAL | all year | 2 | f2, f49 | "Upsized sweatpants paired with an oversize blazer plus a pair of sneakers and a polished bag" (f2); hooded sweatshirt + oversize blazer in a monochrome palette (f49). Sweatpants -> Joggers. The tonal rule cannot be encoded. |
| f046 | Sweatshirt, skirt, heeled ankle boots | CASUAL | AUTUMN, WINTER | 2 | f49 | "Graphic hoodie... with a faux-leather skirt and a heeled bootie" (f49). Heeled bootie -> Ankle boots. |
| f047 | Black trousers, white top, black boots | CASUAL, SMART | SPRING, AUTUMN, WINTER | 2-3 | f1 | "Black pants (faux leather), black boots, and a white top. Throw on a simple sweater or jacket as needed" (f1). |
| f048 | Turtleneck, sleek black trousers, ankle boots | DATE, SMART, CASUAL | AUTUMN, WINTER | 2-3 | f2 | "Leather pants, a turtleneck, and ankle boots", dressed up with a heeled boot or down with a combat boot (f2). Leather pants -> Trousers (BLACK/BROWN). |
| f049 | Wool coat, big scarf, jeans, boots | CASUAL | AUTUMN, WINTER | 2 | f8 | "Scarf coat + jeans + boots" (Stephanie Broek in f8). Scarf coat -> Coat + Scarf. |
| f050 | Oversized blazer, tank, black trousers, flats | SMART, WORK | SPRING, AUTUMN | 3 | f6 | "Oversize blazer + fitted tank + relaxed black trousers + sweater + flats" (f6). The sweater over the shoulders is omitted. |
| f051 | Long trench, shirt, jeans, pointed heels | SMART, DATE | SPRING, AUTUMN | 3 | f6 | "Lightweight long trench + button-down shirt + jeans + pointed-toe heels" (f6). |
| f052 | Navy blazer, grey trousers, loafers | WORK, SMART | all year | 3-4 | f38, f39, f42, f37 | Brief example, refined from the sources. Put This On: a navy sport coat with grey trousers, "mid- to light-gray works best, as charcoal doesn't have enough contrast against dark-colored coats" (f38), and navy sport coats as workhorses with light-to-mid grey trousers (f39). So I used GREY/LIGHT_GREY, not the brief's CHARCOAL. Polo allowed per f38 ("oxford button-downs or long-sleeved polos"); brown penny loafers (f38). Permanent Style (f42; search summary only, the site served a bot challenge): grey flannels give contrast to any dark jacket, and a navy jacket needs blazer details, not an orphaned suit jacket. Navy serge blazer + grey worsted trousers + white shirt (f37). Sport coat -> Blazer. |
| f053 | Navy blazer, blue oxford shirt, dark jeans | SMART, DATE, WORK | all year | 3 | f39, f38 | A casual sport coat with "a dressier pair of jeans... dark denim", usually without a tie (f39); light-blue OCBD (f38). Suede chukkas -> Boots. |
| f054 | Blazer, chinos, white sneakers | SMART, CASUAL | all year | 2-3 | f39, f38 | Swap traditional shoes for plain white sneakers when the sport coat is soft and the trousers casual (f39); "plain white sneakers" with a navy sport coat (f38). |
| f055 | Oxford shirt, chinos, loafers | WORK, SMART, CASUAL | all year | 3 | f36, f38 | The OCBD is "a business casual success, every time", khakis are the most popular trousers, and loafers dress it down (f36); light-blue OCBD as a springboard piece (f38). |
| f056 | Navy blazer, oxford shirt, khakis, brown loafers | WORK, SMART | all year | 3 | f36 | "The undisputed king of business casual jackets, the navy blazer" with khakis (f36). Khakis -> Chinos (KHAKI/BEIGE). |
| f057 | Knit polo, tailored trousers, loafers | SMART, WORK, DATE | SPRING, SUMMER | 3 | f36, f39 | Polo shirts for the casual end of business casual (f36); long-sleeved polos instead of shirts under sport coats (f39). Knit polo -> Polo. |
| f058 | Merino knit over a shirt, dark jeans | SMART, WORK | SPRING, AUTUMN, WINTER | 3 | f40, f36 | "The dress shirt with a pair of dark jeans and a smooth merino knit", called the persisting uniform (f40); merino V-neck (f36). |
| f059 | Shetland knit, oxford shirt, grey trousers, suede boots | SMART, WORK | AUTUMN, WINTER | 3 | f38, f40, f41 | Shetland sweater, light-blue OCBD, grey wool trousers and brown suede chukkas are all Springboard Wardrobe items (f38); Shetlands (f40, f41). Chukka -> Boots or Chelsea boots. |
| f060 | Wool topcoat over tailoring | WORK, SMART | AUTUMN, WINTER | 3-4 | f38, f37 | A heavy topcoat gives "some of the advantages of tailored clothing without the formality" (f38); a charcoal topcoat over a navy blazer, grey trousers and a white shirt, with a scarf (f37). Topcoat/overcoat -> Coat. |
| f061 | Shawl cardigan, checked shirt, cords, chukkas | SMART, CASUAL | AUTUMN, WINTER | 2-3 | f35, f36 | Smart-casual outfit rundown: an off-white heavy knit cardigan, a green checked shirt, navy corduroys and chukka boots (f35); shawl-collar cardigan (f36). Chukka -> Boots. |
| f062 | Linen shirt, pale trousers, loafers or espadrilles | SMART, CASUAL | SUMMER | 2-3 | f22, f35 | Lighter colours (white, cream, stone, pale blue) reflect sunlight, and linen is a summer staple (f22); linen or seersucker trousers in summer with boat shoes (f35). Boat shoes -> Loafers. |
| f063 | Polo or shirt, tailored shorts, loafers | SMART, CASUAL, WORK | SUMMER | 2-3 | f22, f35 | Tailored shorts with an oversized striped shirt for hot office days (f22); polos, Bermuda shorts and boat shoes in smart-casual settings (f35, including reader comments). Bermuda shorts -> Tailored shorts. |
| f064 | Suede jacket, layered knit, jeans | CASUAL, SMART | AUTUMN | 2-3 | f9 | "Suede or leather jacket + layered sweater" (f9). Suede jacket -> Leather jacket or Jacket in brown/camel/beige. Jeans and shoes are my additions. |
| f065 | Sport coat, turtleneck, trousers | SMART, DATE, WORK | AUTUMN, WINTER | 3 | f40 | "Turtlenecks can be a great way to dress down a sport coat, as the raised neckline helps make up for the lack of a necktie" (f40). Trousers and shoes are my additions. |
| f066 | Tweed jacket, turtleneck, short skirt, flats | DATE, SMART | AUTUMN, WINTER | 3 | f23 | Gallery date: "turtleneck + tweed jacket + miniskirt + tights + slingback flats + handbag" (f23). Tweed jacket -> Blazer; miniskirt -> Skirt; slingback flats -> Flats. |
| f067 | Blazer, white blouse, cropped jeans, ankle boots | DATE, SMART | SPRING, AUTUMN | 3 | f24, f4 | "A blazer teamed with a white blouse is quite elegant with cropped jeans and booties" (f24); the blazer + blouse + jeans formula (f4). Booties -> Ankle boots. |
| f068 | Tweed blazer, pale shirt, black jeans, black loafers | SMART, WORK | SPRING, AUTUMN | 3 | f51 | "Black jeans, black loafers, and an off-white or light yellow shirt, with a tweed blazer and corsage" (f51). Off-white -> CREAM/WHITE; the corsage is omitted. |
| f069 | Blazer, slip skirt, kitten heels | SMART, DATE, WORK | SPRING, AUTUMN | 3 | f51 | "A belted blazer, a silk slip skirt, and kitten heels, with a long amulet-style necklace" (f51). Kitten heels -> Heels. The source names no top; tank, tee or blouse added. |
| f070 | Suit, shirt and tie, dress shoes | WORK, FORMAL | all year | 4 | f34, f28, f39 | Lounge suit: jacket and trousers in matching cloth, collared dress shirt, necktie and dress shoes, used as business wear and for weddings (f34). Cocktail attire is "typified by a dark suit, muted shirts, sedate accessories, and dark shoes" (f28). The old office uniform (f39). Suit jacket -> Blazer, suit trousers -> Trousers. Matching cloth cannot be encoded; the app could prefer blazer colour = trouser colour. |
| f071 | Blazer, blouse, tailored trousers, heels | WORK | all year | 3-4 | f3, f2 | Editors' office trio "a blazer, pants, and heels" (f3); a suit as high impact for low effort (f2). Bodysuit/shell -> Tank top or Blouse. |
| f072 | Blouse, skirt, heels, optional blazer | WORK, SMART | all year | 3-4 | f34, f6 | Skirt suit with a blouse as the suit's counterpart (f34); shirt + midi skirt (f6). Pencil skirt -> Skirt or Midi skirt. |
| f073 | Fine knit, tailored trousers, loafers | WORK, SMART | SPRING, AUTUMN, WINTER | 3 | f36, f5 | A fine merino or cashmere sweater worn without a jacket, in navy, grey, dark green or brown (f36); knit with tailored trousers (f5). |
| f074 | Waistcoat, linen trousers, sandals | WORK, SMART, CASUAL | SPRING, SUMMER | 2-3 | f20 | "Knitted waistcoat + linen trousers", or a waistcoat "with shorts or wide-leg trousers" (f20). Linen cannot be encoded. |
| f075 | Pale linen trousers, tank, flat sandals | WORK, CASUAL | SUMMER | 2-3 | f22 | Linen drawstring trousers, light colours, a tank for smarter days (f22). |
| f076 | Trench, shirt, trousers, rubber-soled boots | WORK | SPRING, AUTUMN | 3-4 | f19, f15 | A classic trench or Mackintosh goes with more formal clothes; avoid leather soles in the wet, choose rubber or Dainite (f19); trench + boots in the rain (f15). Rubber-soled boots -> Chelsea boots or Boots. |
| f077 | Sport coat, shirt and tie, grey trousers, brown derbies | WORK | all year | 4 | f39, f38 | Traditional coat-and-tie: navy sport coats, light-to-mid grey trousers, white and light-blue shirts, dark ties, dark brown derbies (f39); f38 agrees on grey trousers. Derby -> Dress shoes. |
| f078 | Slip dress, oversized blazer, strappy sandals | DATE | all year | 3 | f23, f53 | Fancy dinner date: "oversize gray blazer + gray slip dress + red strappy sandals + red handbag" (f23); slip dress + thong heel sandals + clutch (f53). |
| f079 | Lace camisole, wide-leg trousers, pumps | DATE, SMART | all year | 3 | f23 | After-work drinks: "lace camisole top + earrings + wide-leg trousers + pumps + shoulder bag + chunky cuff bracelets" (f23). Camisole -> Tank top; pumps -> Heels. |
| f080 | Statement top, jeans, pointed heels | DATE | all year | 2-3 | f23, f24 | Casual dinner: "corset top + baggy jeans + pointed heels + mini bag" (f23); "a printed top, jeans, and stilettos" (f24). Corset top -> Blouse, Crop top or Tank top. |
| f081 | Little black dress, tights, heels, clutch | DATE, SMART | AUTUMN, WINTER | 3-4 | f25, f23 | "Layering tights underneath" a little black dress, with pointed pumps and a clutch (f25); a black cocktail dress with tights and a clutch for the theatre (f23). Tights cannot be encoded. |
| f082 | Black tee, leather jacket, dark jeans | DATE, CASUAL | SPRING, AUTUMN | 2-3 | f26, f1 | Stylist Sreya Halder: "A black T-shirt will always rule", and a leather jacket beats an office jacket (f26); leather jacket formulas (f1). |
| f083 | Loose shirt, dark jeans, clean trainers | DATE, CASUAL | all year | 2-3 | f26 | Bar date: "a nice button-down with a loose boyfriend fit... tailored pants, like khakis, slacks or a dark-wash denim... a classy sneaker" (f26). |
| f084 | Red dress, heels, clutch | DATE | all year | 3 | f23 | Dancing: "red minidress" + comfortable heels or strappy sandals + mini bag, with a long leather trench for cold weather (f23). Leather trench -> Leather jacket or Trench coat (optional). |
| f085 | Sleek maxi dress, statement jewellery, heels | DATE | all year | 3-4 | f23 | Live music: "oversize earrings + bodycon maxi dress + chunky cuff bracelet + heels + clutch bag" (f23). |
| f086 | Trench, tee, jeans, ankle boots | DATE, CASUAL | SPRING, AUTUMN | 2-3 | f23, f15 | Movie night: "trench coat + T-shirt + belt + jeans + ankle boots + hobo bag" (f23); the trench + boots rain logic (f15). Hobo bag omitted. |
| f087 | White tee, belted jeans, knee-high boots | DATE, CASUAL | AUTUMN | 2-3 | f24 | "Tucking in the T-shirt with a belt and adding knee-high boots" (f24). Knee-high -> Boots. |
| f088 | Silk camisole, leather skirt, heels | DATE | all year | 3 | f25 | "A low-cut silk tank top with a leather skirt, sheer tights" for warmer climates (f25). Silk/leather cannot be encoded. |
| f089 | Bright blazer, slouchy jeans, heeled sandals | DATE, CASUAL | SPRING, SUMMER | 2-3 | f24 | "A colorful blazer is chic and eye-catching with slouchy jeans and heeled sandals" (f24). "Colourful" is encoded as a list of non-neutral colours. |
| f090 | Knit tank, white jeans, trainers | CASUAL, DATE | SUMMER | 2 | f24 | "A beautiful knit tank and white jeans for spring and summer. Sneakers complete the vibe" (f24). |
| f091 | Jumpsuit, blazer, heels | DATE, FORMAL | all year | 3-4 | f27, f25 | A jumpsuit is a cocktail-attire option (f27); a lace jumpsuit with a leather blazer (f25). |
| f092 | Black tie: dinner jacket, white shirt, bow tie | FORMAL | all year | 5 | f29, f33 | Black tie: a black or midnight-blue dinner jacket with matching braided trousers, a white dress shirt, a black bow tie, and black patent or polished plain oxfords (f29, f33 summarising Debrett's). Dinner jacket -> Blazer; midnight blue -> NAVY; bow tie -> Tie; oxfords -> Dress shoes. Cummerbund/waistcoat, studs and cufflinks do not fit the slot model. |
| f093 | Evening gown, heels, clutch | FORMAL | all year | 5 | f31, f33 | "A long dress or skirt remains the safest option", evening sandals or pumps, an evening bag and sparkling jewellery (f31); dinner- or tea-length gowns (f33). Evening bag -> Clutch. |
| f094 | Black tie optional: dark suit, white shirt, dark tie | FORMAL | all year | 4-5 | f30, f29 | Black-tie optional: a black, charcoal or deep navy suit, never paired with a black bow tie (f30); "a dark suit, white dress shirt, conservative tie... and well-shined calfskin dress shoes" (f29). |
| f095 | Cocktail dress, heels, clutch | FORMAL, DATE | all year | 4 | f27, f53 | "Do: Wear a Mini or Midi Dress", "Do: Wear Heels", "Do: Use a Clutch", "Choose One Standout Accessory" (f27); slip dress + heel sandals + clutch (f53). |
| f096 | Cocktail trouser suit, silk top, heels | FORMAL, DATE | all year | 4 | f27 | "Do: Wear Sleek Trousers", "power pantsuit", clutch and heels (f27). |
| f097 | Daytime wedding suit in a lighter colour | FORMAL, SMART | SPRING, SUMMER | 4 | f28 | Daytime cocktail events (before 5pm) allow lighter suits in medium blues, dark or medium greens and most browns (f28). Brown dress shoes for a lighter daytime suit are my inference; f28 says dark shoes for evening. |
| f098 | Colourful midi dress, heels, small bag | FORMAL, SMART | SPRING, SUMMER | 4 | f27 | Cocktail-attire wedding: "the best place to start is with a midi dress... Play around with different colors and prints" (f27). Prints cannot be encoded, so non-neutral colours stand in. |
| f099 | Evening dress, dark coat, heels, clutch | FORMAL, DATE | AUTUMN, WINTER | 4-5 | f31, f23 | Black tie: "a fur, if you own one" as the wrap (f31); theatre date: faux-fur coat + pearls + black dress + leather gloves + clutch (f23). Faux fur -> Coat; pearls -> Necklace. |
| f100 | Black tie in winter: dinner suit and dark overcoat | FORMAL | AUTUMN, WINTER | 5 | f32, f29 | The double-breasted Paletot overcoat in black, dark blue or oxford grey, knee length or longer; "raincoats (a.k.a. trench coats) are not appropriate outerwear for a tuxedo"; the white evening scarf (f32). Oxford grey -> CHARCOAL. The scarf is left out to stay within 6 slots and is mentioned in the note. |
| f101 | Leggings, sports top, running shoes, windbreaker | ACTIVE | all year | 1 | f46 | Windbreakers as "the perfect lightweight finishing touch... when rushing to a workout class or on a run" (f46). Sports bra -> Sports top. |
| f102 | Activewear set under a trench | ACTIVE, CASUAL | SPRING, AUTUMN | 1-2 | f46 | "A classic trench coat will always prepare your activewear set for your next appointment" (f46). Activewear set -> Sports top + Leggings. |
| f103 | Oversized sweatshirt, leggings, trainers | LOUNGE, ACTIVE, TRAVEL | all year | 1-2 | f47, f45, f46 | Hiking: "shacket + oversize sweatshirt + white leggings" (f47); travel: "leggings + oversize sweater" (f45); shacket over a matching set (f46). Shacket -> Overshirt. |
| f104 | Matching sweatsuit, trainers | LOUNGE, TRAVEL, CASUAL | all year | 1-2 | f45, f49 | "Matching sweatsuit + sneakers" on and off the plane (f45); an all-black sweatsuit with a matching boot and bag (f49). Sweatpants -> Joggers. |
| f105 | Hiking layers: sports top, gilet or fleece, hiking boots | ACTIVE | SPRING, SUMMER, AUTUMN | 1 | f47, f48 | "Sports bra + matching shorts + vest", "onesie + vest + baseball hat", fanny pack (f47); activewear sets, trail sneakers, a shacket (f48). Puffer vest -> Gilet; fanny pack -> Belt bag; trail sneakers -> Running shoes or Hiking boots. |
| f106 | Rainy trail: raincoat, fleece, hiking boots | ACTIVE | SPRING, AUTUMN | 1 | f19, f47 | Packable rain coats for climates where rain and cold don't go together (f19); vests and layers for changing weather on a hike (f47). The base/mid/shell logic is standard outdoor advice; REI's guides returned 403, so I did not cite them. |
| f107 | Running shorts, tank, cap | ACTIVE | SUMMER | 1 | f47, f46 | Sports bra + matching shorts (f47); activewear sets (f46). Sports bra -> Sports top. |
| f108 | Winter run: base layer, fleece, leggings | ACTIVE | AUTUMN, WINTER | 1 | f46 | "Pile on the layers" over activewear in cold months (f46). Base layer -> Long-sleeve top. Generic layering advice; REI unreachable. |
| f109 | Zip hoodie, joggers, trainers, gym bag | ACTIVE | all year | 1 | f46 | Hoodies layered over activewear sets (f46). |
| f110 | Joggers, oversized shirt, trainers | LOUNGE, CASUAL | all year | 1-2 | f2 | "Comfy sweatpants, an oversize button-down, and sneakers... an oversize sweater vest" (Judith Jones in f2). |
| f111 | Knit cardigan set, tee, trainers | LOUNGE, TRAVEL | all year | 1-2 | f44 | A knit "cardigan-and-trouser combination, a much more stylish alternative to the regular tracksuit" (Remi Afolabi in f44). Brand removed. |
| f112 | Wide trousers, white shirt, trainers | TRAVEL, CASUAL | all year | 2 | f45 | "Breezy pants + white button-down" that take you "off the plane and straight to lunch" (f45). |
| f113 | Jeans, tee, blazer, flats, big scarf | TRAVEL | all year | 2-3 | f44, f43 | "Jeans, a T-shirt, and a smarter blazer, and I'll bring a big scarf for the plane... any shoe other than flats is out of the question" (Emma Spedding in f44); statement outerwear, tote and comfortable shoes (f43). |
| f114 | Trench, hoodie, wide trousers, trainers | TRAVEL, CASUAL | SPRING, AUTUMN | 2 | f44 | "Satin wide-leg trousers... a hoodie... a chic trench layered on top" with clog-style Birkenstocks (Emily Dawes in f44). Clogs -> Sandals (approximate). |
| f115 | Long cardigan, tank, jeans, tote | TRAVEL | all year | 2 | f44, f43 | A long cardigan or shawl to wrap up in plus a big basket bag (Hannah Almassi in f44); a tank under a jacket, an oversized tote and white high-tops (Remy Farrell in f44); "stylish tote" (f43). Basket bag -> Tote. |
| f116 | Quilted coat, comfy bottoms, chunky trainers, shoulder bag | TRAVEL | AUTUMN, WINTER | 2 | f45 | "Quilted coat + shoulder bag + chunky sneakers" for chilly destinations (f45). Quilted coat -> Puffer jacket or Coat. |
| f117 | Bomber jacket, jeans, shoulder bag | TRAVEL, CASUAL | SPRING, AUTUMN | 2 | f45 | "Bomber jacket + jeans + shoulder bag" for a day in a new city (f45). Shoulder bag -> Crossbody or Handbag. |
| f118 | Sundress, sandals, big tote | TRAVEL, CASUAL | SUMMER | 1-2 | f45 | "Cutout dress + sandals + net bag" for a beach trip (f45). Net bag -> Tote. |
| f119 | Knit polo, long skirt, crossbody | TRAVEL, CASUAL | SPRING, SUMMER | 2 | f45 | "Maxi skirt + knit polo" with a crossbody or top-handle bag; a crossbody keeps belongings secure (f45). Knit polo -> Polo. |
| f120 | Trench, dress, knee-high boots | CASUAL, WORK | SPRING, AUTUMN | 2-3 | f15, f16, f17 | "A very good trench coat and knee-high boots" (f15); a printed dress with rain boots (f16); midi dresses under a warm coat in the rain (f17). |
| f121 | Trench, dark jeans, tall rain boots | CASUAL | SPRING, AUTUMN | 2 | f16 | "Rocking a trench coat and tall rain boots"; "black rain boots... paired with dark-wash jeans"; a wide-brim hat (f16). Dark wash -> NAVY. |
| f122 | Raincoat, sweatshirt, jeans, rain boots | CASUAL | SPRING, AUTUMN, WINTER | 1-2 | f17, f19 | A sweatshirt and comfortable shoes for a rainy commute (f17); lightweight packable raincoats, plus rubber soles or rain-specific boots (f19). |
| f123 | Puffer gilet, jeans, lug-sole boots | CASUAL | AUTUMN, WINTER | 2 | f15 | "It's time for your puffer vest to shine... pairing it with similar colors", with lug-sole boots (f15). Puffer vest -> Gilet; lug-sole boots -> Boots or Chelsea boots. |
| f124 | Trench, white mini dress, ballet flats | CASUAL | SPRING, SUMMER | 2 | f18 | "Sunglasses + trench coat + white minidress + black ballet flats + shoulder bag": swap sandals for a closed toe in summer rain (f18). |
| f125 | Breezy dress, rain boots | CASUAL | SPRING, SUMMER | 2 | f18, f16 | "White baby-doll minidress + rain boots + umbrella" for warm, wet days (f18); a dress with rain boots (f16). |
