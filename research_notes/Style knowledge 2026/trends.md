# Hanger trends knowledge base: evidence notes

Updated 29 October 2026. These notes support `trends.json` in this folder. Source IDs (t1, t2 and so on) match the `sources` array in the JSON. Trend IDs (t01, t02 and so on) match `trends`.

## What this covers

- **AW26 (live now):** Autumn/Winter 2026-27 shows from February to March 2026, the January 2026 men's shows, and street style. Trends run from 2026-09-01 to 2027-02-28. A trend backed by both seasons' shows runs to 2027-08-31.
- **SS27 (upcoming):** Spring/Summer 2027 shows. The men's shows were in June 2026; NYFW, LFW, MFW and PFW ran from 10 September to 6 October 2026. Trends run from 2027-03-01 to 2027-08-31.
- **Colour authorities:** Pantone's Colour of the Year 2026 and its NYFW AW26/27 and SS27 palettes; WGSN x Coloro's Colours of the Year for 2026, 2027 and 2028, and its SS27 key colours.
- **Retail and search data:** Lyst Index Q3 2026, published 28 October 2026; FashionUnited's denim search data, 13 October 2026; and the T-Fashion Runway Index share-of-look counts for FW26 and SS27.

## How much to trust it

1. **Runway counts and editors disagree on purple.** Editors and stylists call royal purple *the* colour of AW26: RTÉ stylists (t1), The Zoe Report (t14), Style Academy (t16), and Who What Wear, which is undated and cited via TLM Edit. Sarah Constantin's AI-assisted count of Vogue Runway images (t11) found purple dropped *out* of the FW26 top 30, with burgundy the most common non-neutral. The JSON keeps purple at "peak" because editors, stylists and retailers are pushing it to shoppers this autumn. Burgundy is "peak" on both counts.
2. **Brown is established, not new.** T-Fashion has chocolate as the #2 FW26 colour at 22.5% of looks (t2), up 4 points in New York (t5) but down 3.8 in Milan (t4). Constantin found brown slightly down on FW25. That is why chocolate is "peak" rather than "rising".
3. **Minimalism versus quiet luxury.** T-Fashion's SS27 "Minimalist" theme *rose* to 25.4%, +3.5 points (t6). What is fading is the beige-on-beige quiet-luxury *uniform*: FashionUnited (t31), the Galeries Lafayette buyer (t34) and Lyst (t27) all describe a turn to colour, pattern and ornament. Clean shapes are not fading. The fading entry is worded to match.
4. **Wide-leg trousers flip between seasons.** Wide-leg trousers rose for FW26 in Paris, 17.4% to 20.1% (t3), then fell for SS27 in Paris, 15.2% to 9.4% (t7). Loose and palazzo trousers collapsed for SS27 (t6, t8). t28 is "peak" and ends on 2027-02-28. Palazzo and very loose trousers are in `fading`.
5. **Some trends have empty tokens on purpose.** These are velvet, suede, leather, faux fur, slim trousers, chartreuse, linen tailoring, bubble volume, low waists, lace and hand-made knits. The app vocabulary has no token for these fabrics or fits. Chartreuse and lime are left empty because snapping them to GREEN or YELLOW would wrongly tag emerald or butter-yellow outfits.
6. **Tokens stand in for a few fits.** Sneakers (t33) and Boots (t31) cover broader shapes than the trend describes, which is slim retro trainers and knee-high boots. The summaries name the precise shape.

## Where research hit limits

- **Search budget:** The session's shared web-search budget, 200 calls, ran out partway through. All later evidence came from fetching known URLs directly.
- **Sites I could not read:**
  - Vogue and British Vogue block this crawler.
  - WWD redirects to a paywall, HTTP 402. I saw its Milan FW26 trends, Milan and Paris buyer surveys and Lyst Q3 coverage in search summaries only, so those pieces are not cited in the JSON.
  - Who What Wear and Marie Claire returned only a page title. Their content is known from search summaries and secondary recaps, with no reliable dates, so they appear only under "Undated corroboration" below.
  - Fashionista returned HTTP 403. The Forbes SS27 pieces (t43, t44) also returned 403. Their content comes from search-result summaries; the dates come from the article URLs.
- **Outlets not found:** No Business of Fashion trend piece and no Esquire piece turned up. Elle appeared only as a syndicated snippet. GQ is the Singapore edition (t26). Harper's Bazaar and InStyle are AOL syndications with full bylines and dates (t13, t12).
- **Pantone 2027:** Not yet announced. It is due in the first week of December 2026, per colorcombinations.org as of 16 October 2026. Add it when it lands.

---

## Colours of the Year

| Name | By | Year | Nearest token | Evidence |
|---|---|---|---|---|
| Cloud Dancer, PANTONE 11-4201 | Pantone | 2026 | WHITE | Announced 4 Dec 2025. Described as a "billowy, balanced white", and the first white Pantone has ever picked (TIME, t47). |
| Transformative Teal | WGSN x Coloro | 2026 | TEAL | Announced 3 Sep 2024. Described as a "fluid fusion between dependable dark blue and aquatic green" (WGSN, t46). |
| Luminous Blue, Coloro 125-28-38 | WGSN x Coloro | 2027 | BLUE | Announced 29 Apr 2025. A lapis and cobalt blue. The SS27 key colours that go with it are Energy Orange 018-57-34, Pop Pink 151-73-22, Meadowland Green 050-61-19 and Clay 014-60-13 (t45). |

Further context, not in the JSON:

- WGSN x Coloro named **Radiant Earth** (Coloro 017-42-31) as Colour of the Year **2028** on 9 April 2026. It is a warm earthy neutral, nearest RUST. Source: https://www.wgsn.com/en/blog/colour-year-2028-radiant-earth.
- Coloro's A/W 27/28 key colours are Luminous Blue, Russet, Peaceful Lilac, Maize and Deep Green (https://coloro.com/key-colors). Use these for the next update.
- Paint brands' 2027 picks lean towards green and earth tones, for example Benjamin Moore Webster Green on 14 Oct 2026 and Behr Grounded. These are interiors, not fashion (https://colorcombinations.org/trends/color-trends-2027/, as of 16 Oct 2026).

**Pantone's seasonal palettes:**

- **NYFW AW26/27** (12 Feb 2026, t20): Muted Clay, Neptune Green, Green Envy, Arabian Spice, Foxglove, Festival Fuchsia, Red Mahogany, Acacia, All Aboard and Burnt Olive. The core neutrals are Egret, Candied Ginger, Toffee, Underworld and Poseidon.
- **NYFW SS27** (21 Sep 2026, t36): Anise Flower (pale yellow), Princess Blue, Waterfall, Langoustino (coral), Chili Pepper, Rosy Future (light pink), Dragon Fire (orange), Daiquiri Green (lime), Hibiscus and Lavender Oil. The neutrals are Antique White, Tarragon, Arrowwood, Blue Indigo and Dolphin.

---

## Trends: AW26 (live now)

**t01 Chocolate brown and powder blue** (pairing, peak). RTÉ, 22 Oct 2026 (t1): stylists Clare Chambers and Deborah Sheridan-Taylor use sky blue as an accent with chocolate and caramel, and note that the London runway paired sky blue with chocolate brown. Style Academy, 1 Sep 2026 (t17), pairs a brown coat with a vivid blue knit. T-Fashion (t2) has chocolate as the #2 FW26 runway colour.

**t02 Chocolate brown as the new neutral** (colour, peak, runs to Aug 2027). T-Fashion FW26 (t2): chocolate is 22.5% of 13,370 looks, second only to black. In New York it rose from 19.2% to 23.2% (t5). Harper's Bazaar NYFW street style, 17 Feb 2026 (t13): chocolate and warm brown, especially in suede. Fenwick, 1 Sep 2026 (t18), lists chocolate and espresso as the core AW26 palette. In SS27 chocolate is still #3 at 5.9% (t6).

**t03 Royal purple** (colour, peak).

- RTÉ, 22 Oct 2026 (t1): a royal purple knit as the entry point.
- The Zoe Report PFW, 11 Mar 2026 (t14): royal purple at Chloé, Loewe, Mugler and Balenciaga.
- Style Academy, 12 Mar 2026 (t16): royal purple and jewel tones at Gucci, Ferragamo and Bottega Veneta.
- Undated corroboration: Who What Wear calls purple the "#1 color trend" of Fall 2026.
- Counterpoint: Constantin's count (t11).

**t04 Purple and chocolate** (pairing, rising). RTÉ, 22 Oct 2026 (t1): a purple knit with navy or chocolate wide-leg trousers. Undated corroboration: TLM Edit suggests a plum blouse with chocolate trousers. There is only one dated source, so the status is "rising".

**t05 Burgundy and oxblood** (colour, peak).

- Constantin, 14 Mar 2026 (t11): burgundy replaced red as the most common non-neutral for FW26. Oxblood, brick and wine entered the top 30.
- Harper's Bazaar, 17 Feb 2026 (t13): "deep burgundy, a defining shade of the season".
- Editor & Co, 27 Jul 2026 (t19): deep burgundy is the lead shade.
- Style Arcade men's FW26, 3 Feb 2026 (t24): spiced reds and bordeaux.

**t06 Cobalt and electric blue** (colour, rising, runs to Aug 2027). RTÉ (t1): Yves Klein blue at Stella McCartney, Givenchy and Bottega Veneta. Constantin (t11): cobalt entered the FW26 top 30. Style Arcade (t24): cobalt is a key menswear accent. WGSN (t45): Luminous Blue leads 2027. Pantone's SS27 palette includes Princess Blue (t36).

**t07 Cobalt and chocolate** (pairing, rising). RTÉ (t1): pair cobalt with chocolate brown, mustard or burgundy. Style Academy (t17): a brown coat with a vivid blue knit.

**t08 Poppy red** (colour, rising).

- RTÉ (t1): poppy red at Louis Vuitton, described as closer to orange than wine.
- Style Academy (t17): bright and tomato reds alongside Red Mahogany.
- Style Arcade (t24): hot reds in menswear.
- Search summaries also reported tomato red in Paris (Fashionista) and Paris buyers citing red, yellow and purple (WWD). I could not open either piece.

**t09 Red and purple** (pairing, rising). RTÉ (t1): "Purple and red are also a trending combination"; a poppy red jumper with purple. There is only one source, so the status is "rising".

**t10 Mustard yellow** (colour, rising). Constantin (t11): mustard "moved significantly up in rank" for FW26. Style Arcade (t24): mustard is a key accent. RTÉ (t1): mustard pairs with cobalt. A search summary also reported mustard at Boss, Prada and MM6 in Milan (Fashionista); I could not open it.

**t11 Burnt olive** (colour, peak). Pantone NYFW AW26/27, 12 Feb 2026 (t20): Burnt Olive, 18-0521 TCX, an "understated, layered green". T-Fashion (t2): Moss is in the FW26 top 10 at 3.1%. Constantin (t11): olive and sage entered the top 30. Fenwick (t18) lists olive in its core palette.

**t12 Fuchsia accents** (colour, rising, runs to Aug 2027). Pantone AW26/27 (t20): Festival Fuchsia, a "punchy fuchsia accent". Constantin (t11): fuchsia and magenta entered the FW26 top 30. WGSN SS27 (t45): Pop Pink.

**t13 Tonal grey** (styling, peak). Harper's Bazaar (t13): "build an all-grey outfit with slightly different shades and textures", for example faux fur over a charcoal roll-neck and pleated trousers. T-Fashion Paris (t3): charcoal rose from 6.4% to 9.4%, the biggest colour riser. InStyle, 19 Mar 2026 (t12), "Fashion Finance": roomy grey suits.

**t14 Relaxed grey suiting** (item, peak). InStyle (t12): relaxed, 1990s-inspired tailoring and roomy grey suits at Boss, Emporio Armani and Givenchy. #legend, 7 Oct 2026 (t22): Giorgio Armani's greige, charcoal and nuanced greys. T-Fashion (t3): tailored pants and charcoal both rising.

**t15 Leopard as a neutral** (pattern, peak, runs to Aug 2027). Harper's Bazaar (t13): "leopard and animal prints, treated as a neutral". InStyle (t12): snow leopard faux fur at Bottega Veneta, Carolina Herrera and Tom Ford. #legend (t22): Fendi animal print. FashionUnited SS27 report, 28 May 2026 (t35): tone-on-tone animal prints for spring.

**t16 Playful checks and plaid** (pattern, peak). InStyle (t12): looser, more voluminous plaids at Chloé, Acne Studios and Loewe. Style Arcade (t24), "Checking in": herringbone, Prince of Wales and tonal checks. Haute Garbage, 26 Feb 2026 (t21): mixed plaids at London. A search summary also has Who What Wear calling checks the season's leading and most wearable print; that piece is undated.

**t17 Dark florals** (pattern, rising). InStyle (t12), "Noir Florals": florals on black grounds in knits, fringe and pixelated prints at Ralph Lauren, Givenchy and Dries Van Noten. T-Fashion (t2): floral is the #1 FW26 pattern at 9.5%.

**t18 Polka dots** (pattern, rising, runs to Aug 2027). The Zoe Report street style, 3 Feb 2026 (t15): polka dots in a stylist's forecast. W Magazine, 2 Mar 2026 (t23): Marni's oversized dots. Harper's Bazaar (t13): polka-dot scarves. #legend (t22): Dior polka-dot pumps.

**t19 Velvet by day** (fabric, peak; no tokens). Style Academy (t16): velvet in Opulent Romance. Style Arcade (t24): velvet blazers, with velvet's average revenue up 244% over 12 months per Particl. The Zoe Report (t15): a flocked velvet skirt.

**t20 Suede in warm browns** (fabric, peak; no tokens). Fenwick (t18): suede jackets, bags, boots and skirts in tan, chestnut and chocolate. Harper's Bazaar (t13): chocolate suede, suede totes and Western suede boots.

**t21 Leather beyond the jacket** (fabric, peak; no tokens). Style Academy (t16), "Leather Everything": coats, trenches, trousers, pleated skirts and shirts at Balmain, McQueen and Burberry. T-Fashion (t2): leather is 10.1% of FW26 looks, the #2 fabric. Harper's Bazaar (t13) describes head-to-toe leather in street style.

**t22 Faux fur and shearling coats** (item, peak; no tokens). The Zoe Report (t15): Net-a-Porter's buyer predicts statement outerwear everywhere, plus shearling and faux fur in bright colours and animal prints. Style Academy (t16): faux fur and statement outerwear at Louis Vuitton, Isabel Marant and Stella McCartney. Harper's Bazaar (t13) and InStyle (t12): "Grandma (Faux) Furs". There is no fur or shearling token, and "Coat" alone would tag every coat, so tokens are left empty.

**t23 Bombers and flight jackets** (item, peak, runs to Aug 2027). The Zoe Report PFW (t14), "Top Gun": flight jackets with shearling at Balmain and Dior. Harper's Bazaar (t13): oversized leather bombers. GQ Singapore, 8 Sep 2026 (t26): Dolce & Gabbana leather bombers. FashionUnited green, 24 Oct 2026 (t31): bombers in SS27, including an olive suede bomber at Aje and a kelly-green striped bomber at Loewe.

**t24 Funnel necks and high collars** (silhouette, peak). Style Academy (t16): funnel necks, high collars and roll-necks at Joseph, Victoria Beckham, Burberry, Saint Laurent and Ferragamo. InStyle (t12), "Calling All Collars". The Zoe Report (t14): collars worn up "in line with the funnel-neck trend". Harper's Bazaar (t13): turtlenecks.

**t25 Cinched-waist tailoring** (silhouette, peak, runs to Aug 2027). The Zoe Report (t14): peplum as "a default jacket silhouette" at Dior, The Row and Givenchy; wrap blazers. Fenwick (t18): blazers with subtle waist definition. Style Arcade (t24): "nip-tuck tailoring". FashionUnited SS27 report (t35): the "return of the defined waist". Token note: "Blazer + Belt" is the everyday version.

**t26 The skirt suit** (item, rising, runs to Aug 2027). Style Academy (t16): "The Return of the Skirt Suit" at Gucci, Fendi and Jil Sander. Haute Garbage (t21): Tolu Coker's 1960s skirt suits. Florré, 10 Oct 2026 (t39), for SS27: knee skirts with jackets at Prada, Loewe, Chanel and Balenciaga.

**t27 Tailored trousers** (item, peak, runs to Aug 2027). T-Fashion (t2): tailored pants are the #1 FW26 key item, rising from 19.1% to 22.3%. Paris rose from 20.6% to 23.9% (t3). For SS27 they are still #2 at 12.4% (t6).

**t28 Wide-leg tailoring for winter** (silhouette, peak, ends Feb 2027). T-Fashion Paris FW26 (t3): wide-leg pants rose from 17.4% to 20.1%. In Milan they are 19.1%, the #2 key item (t4). Paris SS27 (t7): wide-leg fell from 15.2% to 9.4%.

**t29 Pea coats and sailor details** (item, rising). InStyle (t12), "Set Sail": sailor collars, toggles and pea coats at Etro, Ferragamo and Dries Van Noten. W Magazine (t23): Ferragamo's red leather suit with a sailor neckline. Token note: a navy coat is the closest everyday match.

**t30 Shirt and tie** (styling, rising, runs to Aug 2027). InStyle (t12), "Tie It Up", at Givenchy and Jean Paul Gaultier. #legend (t22): Dries Van Noten's shirts and ties. i-D, 23 Jun 2026 (t40): Ralph Lauren SS27 with a tie worn as a belt. FashionUnited SS27 report (t35): "softly tied ties".

**t31 Knee-high and Western boots** (item, peak). Harper's Bazaar (t13): Western-leaning suede boots and chunky, knee-high leather boots. Style Academy (t16): lace-up knee boots at Prada, Versace and McQueen. FashionUnited double denim (t30): SS27 double denim finished with cowboy boots.

**t32 Ballet flats** (item, peak, runs to Aug 2027). Lyst Q3 2026 (t27): Chanel ballet flats are #9 of the 10 hottest products. Harper's Bazaar (t13): ballet flats in street style.

**t33 Slim retro trainers** (item, peak, runs to Aug 2027). Lyst Q3 (t27, t28): the Celine/Reebok Freestyle Lo is #3 and the Vans Premium Authentic is #4. The Zoe Report street style (t15): a Shopbop director predicts "Vans, vintage wash denim".

**t34 Cardigan as a jacket** (styling, peak). Harper's Bazaar (t13): "cashmere cardigans worn as jackets" and "cardigan as outerwear". Fenwick (t18): chunky knit cardigans worn as jackets. Style Arcade (t24): plush cardigans.

**t35 Shirt under a sweater vest** (styling, rising). Style Arcade (t24): "shrunken vests" and patterned vests at Dries Van Noten. #legend (t22): Dries Van Noten's school-uniform tailoring with shirts and ties. Haute Garbage (t21): "Femme Varsity". A search summary also reported Who What Wear's preppy "school's out" direction; it is undated.

**t36 Mixed textures, one palette** (styling, peak). Harper's Bazaar (t13): "combine suede, leather, faux fur, knits, and calf hair within one palette". Fenwick (t18): brushed and bouclé finishes. Token note: TEXTURE pattern is the proxy.

**t37 Scarves everywhere** (styling, peak, runs to Aug 2027). Harper's Bazaar (t13): triangle and polka-dot scarves. Lyst Q3 (t27): the Dries Van Noten Long Wrap Scarf-Skirt is the #5 product, demand up 383%. i-D (t40): Prada SS27 silk scarves knotted at the waist. Galeries Lafayette buyer (t34): scarves and bandanas personalise SS27 looks. A search summary also has Fashionista reporting sweaters and scarves moving from shoulders to waists at NYFW SS27.

**t38 Statement gloves** (item, rising). InStyle (t12), "Cherry On Top Gloves": bright or patterned gloves. Harper's Bazaar (t13): leather gloves.

**t39 Newsboy caps and berets** (item, rising). The Zoe Report (t15), "Unexpected Headwear": silk kerchiefs, pillbox hats and newsboy caps. Style Arcade (t24): berets and pageboy caps.

**t40 Gold and silver metallics** (colour, rising, runs to Aug 2027). InStyle (t12): "Golden Age", gold lamé, and "OTT Opulence", oversized gold jewellery. FashionUnited, 10 Oct 2026 (t33): PFW SS27 gold and silver for day and night, styled with trenches, knits and tees. Forbes PFW, 8 Oct 2026 (t44): metallic gold and silver in Paris. Editor & Co (t19): metallic silver as AW26's modern accent.

**t41 Big statement jewellery** (styling, rising, runs to Aug 2027). The Zoe Report (t15): "oversized bracelets worn over outerwear". InStyle (t12): oversized gold jewellery at Saint Laurent, Celine and Chanel. Galeries Lafayette buyer (t34): costume jewellery, pearls and colourful necklaces for SS27.

**t42 Sleek rain slickers** (item, rising). InStyle (t12), "Sleek Slickers": high-tech rain slickers at Lacoste, Loewe and Tod's. Style Arcade (t24), "The weatherman": utility rain jackets and macs.

**t43 Slimmer trousers creep back** (silhouette, rising; no tokens).

- The Zoe Report (t14): stirrup trousers at Stella McCartney, Mugler and Balmain, "part of a longer-running move toward more fitted trousers".
- W Magazine (t23): Gucci's skintight slacks.
- Istituto Marangoni, 28 Jan 2026 (t25): Prada's fitted silhouettes.
- i-D (t40): Prada SS27 skinny five-pocket jeans.
- FashionUnited (t29): only one skinny jean in Paris, and a spring search spike in skinny jeans has faded. That is why the status is "rising" rather than "peak".

**t44 Brown jeans** (item, rising, runs to Aug 2027). FashionUnited, 13 Oct 2026 (t29): searches for brown jeans are up 110%, against light blue +80% and white +70%, all growing faster than black and blue, which it links to warmer runway washes. T-Fashion (t2): chocolate is the #2 colour.

**t45 Jeans with a blazer** (styling, peak, runs to Aug 2027). FashionUnited (t29): Paris SS27 favoured relaxed straight jeans "often under blazers", and wool suiting appeared in 23% of denim looks. i-D (t40): Prada SS27 "jeans, T-shirts and blazers".

**t46 Chartreuse and lime accents** (colour, rising, runs to Aug 2027; no tokens). RTÉ (t1): chartreuse at the Miu Miu front row, worn as a small accent with navy, cobalt, purple or khaki. TheDoubleF, 2 Jul 2026 (t41): Prada SS27 acid green accents. Pantone SS27 (t36): Daiquiri Green, a "sharp, citrus-toned lime". FashionUnited green (t31): chartreuse shorts at Christopher Esber and lime sandals at Aje.

## Trends: SS27 (upcoming, from 1 March 2027)

**t47 Orange and citrus** (colour). The Zoe Report PFW SS27, 7 Oct 2026 (t37), "Vitamin C": orange at Hermès, Chanel, Loewe and Mugler. WGSN (t45): Energy Orange. Pantone SS27 (t36): Dragon Fire and Langoustino. TheDoubleF (t41): Celine trousers in turquoise and coral.

**t48 Every shade of green** (colour). FashionUnited, 24 Oct 2026 (t31): green across all four capitals, "functioned almost as neutrals", including emerald at Prada and Alberta Ferretti, pistachio at Chanel, mint at Victoria Beckham and Tory Burch, and kelly green at Loewe. WGSN (t45): Meadowland Green. Pantone SS27 (t36): Waterfall, Daiquiri Green and Tarragon.

**t49 Mint and lavender** (pairing). FashionUnited, 29 Oct 2026 (t32): pastels combined in one look, including mint with lavender and chartreuse at Dries Van Noten. FashionUnited green (t31): a mint shirt with lavender accents at Victoria Beckham.

**t50 Soft pink and pistachio** (pairing). FashionUnited green (t31): seafoam and mint with rose pink at Carolina Herrera; a pistachio wrap skirt with a pearl-pink blouse at Chanel. WGSN (t45): Pop Pink. Pistachio maps to MINT.

**t51 Butter yellow and lilac** (pairing). FashionUnited pastels (t32): pale yellow with pastel purple at Blumarine, butter-yellow trousers at Bottega Veneta, and pastel yellow with sage at Dior. Pantone SS27 (t36): Anise Flower (pale yellow) and Lavender Oil.

**t52 Powder blue** (colour). T-Fashion NYFW SS27 (t9): powder blue is 4.4%, +2.7 points, in the top 10. In Milan it is 4.2%, also top 10 (t8). FashionUnited pastels (t32): powder blue.

**t53 Tonal whites** (styling). T-Fashion SS27 (t6): white is 28.4% of looks, +4.7 points, the biggest colour riser; ecru also rose 1.4 points. Forbes LFW, 24 Sep 2026 (t43): "white as a statement shade". T-Fashion London (t10): white rose from 25.7% to 30.2%. Pantone's 2026 colour is a white (t47).

**t54 White jeans** (item). FashionUnited (t29): searches for white jeans are up 70%. The Zoe Report (t37), "Party Pants": white denim at Stella McCartney.

**t55 Double denim** (styling). FashionUnited, 17 Oct 2026 (t30): matching denim sets defined SS27, with denim at about 7% of runway looks per T-Fashion, at Conner Ives, Zimmermann, Gabriela Hearst and Tibi. FashionUnited (t29): 75% of Paris denim pieces were trousers.

**t56 The skirt is back** (item). T-Fashion SS27 (t6): skirts rose from 19.3% to 27.9% of looks, with straight skirts up from 5.1% to 10.3%. In New York, midi skirts rose from 4.9% to 11.0% (t9). The Galeries Lafayette buyer (t34) calls the skirt "the key piece of the season". Florré (t39): knee skirts with jackets.

**t57 Trench as the outfit** (styling). Florré (t39), "Trenches as Dresses": Prada, Celine, Bottega Veneta, Jil Sander and Hermès. FashionUnited metallics (t33): metallic trench coats. Daily Front Row, 14 Oct 2026 (t38): oversized trenches.

**t58 Florals in full bloom** (pattern). T-Fashion SS27 (t6): floral is the #1 pattern at 14.2%, +2.8 points. Milan rose from 10.7% to 15.0% (t8). The Zoe Report (t37), "Garland Girls", at Balenciaga, Courrèges and Vivienne Westwood. Daily Front Row (t38): embroidered roses and 3D flowers. A search summary also has Fashionista reporting 3D floral appliqués in Paris.

**t59 Stripes on stripes** (pattern). T-Fashion SS27 (t6): striped is 8.9%, +1.5 points, the #2 pattern. New York rose 3.8 points (t9). W Magazine (t23): Marni's "stripes on stripes" set the tone in February.

**t60 Gingham and picnic checks** (pattern). T-Fashion SS27 (t6): checked is 5.3%, +1.5 points. FashionUnited SS27 report (t35): Vichy, handkerchief and tea-towel checks. The Zoe Report (t37): plaid separates at Miu Miu and Vivienne Westwood.

**t61 Polos and prep** (item). The Zoe Report (t37), "Prep School Redux": argyle knits at Acne Studios and polos at Dries Van Noten. i-D (t40): knit polos at Pitti Uomo and Ralph Lauren's "playful prep". Haute Garbage (t21): a rugby polo at London AW26.

**t62 Soft linen tailoring** (fabric; no tokens). i-D (t40): linen suits and pleated trousers at Pitti. Style Cartel, 9 Jul 2026 (t42): "Lighter Tailoring" in linen, light cotton and silk blends at Prada, Armani and Ralph Lauren. TheDoubleF (t41): Celine's soft gabardine and relaxed tailoring.

**t63 Graphic and slogan tees** (item). The Zoe Report (t37), "Graphic Content", at Dries Van Noten, Celine, Stella McCartney, Sacai and Valentino. Haute Garbage (t21): slogan and graphic tees at London AW26.

**t64 Sleeveless tops** (item). T-Fashion Milan SS27 (t8): sleeveless tops rose from 8.8% to 14.9%, the biggest riser. There is a single data source.

**t65 Bubble and balloon volume** (silhouette; no tokens). FashionUnited SS27 report (t35): balloon shapes and sculpted bubble skirts. Daily Front Row (t38): bubble-hem skirts. Florré (t39): fluid forms. A search summary also has Fashionista reporting bubble silhouettes at Acne, Chloé and Matières Fécales.

**t66 Lowered waists** (silhouette; no tokens). Florré (t39), "The Waist Drop": low-rise trousers and hip-slung skirts at Chanel and Julie Kegels. T-Fashion SS27 (t6): dropped-waist dresses rose from 0.6% to 4.9%.

**t67 Lace and lingerie details** (fabric; no tokens). Lyst Q3 (t27): shoppers favour silk and lace pieces, and Free People's lace-trimmed bloomer trousers are #7. Galeries Lafayette buyer (t34): light, sheer, lingerie-inspired blouses are key. Florré (t39): lace at Chloé, Zimmermann and Isabel Marant.

**t68 Hand-made knits** (fabric; no tokens). Florré (t39), "Stitches & Knits": visible stitching at Bottega Veneta, The Row and Victoria Beckham. Style Cartel (t42): craftsmanship and textured knitwear. Forbes LFW (t43): handmade detail and knitwear. FashionUnited SS27 report (t35), "the human mark".

**t69 Belts as the statement** (item). Galeries Lafayette buyer (t34): "belts and costume jewellery are central". FashionUnited SS27 report (t35): double belts. Harper's Bazaar (t13): mixed-metal belts in AW26 street style.

---

## Fading

| Name | Evidence |
|---|---|
| All-beige quiet luxury | FashionUnited green (t31): green marks "a move away from the beige-heavy 'quiet luxury' look". Galeries Lafayette buyer (t34): "after quiet luxury and minimalism, fashion is becoming more joyful". Constantin (t11): beige and light beige fell for FW26. Lyst Q3 (t27): shoppers move from pared-back labels to ornament and pattern. Clean minimal shapes are not fading (see note 3 above). |
| Palazzo and very loose trousers | T-Fashion SS27 (t6): loose pants fell from 11.5% to 3.0%. Milan palazzo pants fell from 8.6% to 3.4% (t8). Paris wide-leg fell from 15.2% to 9.4% (t7). No tokens, because Wide-leg trousers is still a live AW26 trend (t28). |
| Chinos as the default trouser | T-Fashion SS27 (t6): chinos fell from 6.7% to 1.5%. In Paris they fell from 7.8% to 1.3% (t7). |
| Mini dresses | T-Fashion SS27 (t6): mini dresses fell from 9.8% to 4.8%. In New York they fell from 10.9% to 3.9% (t9). |
| Barrel-leg jeans | FashionUnited (t29): barrel searches peaked in April 2026 and "settled below half that peak". Straight jeans are searched about three times as often. The article calls barrel "maturing rather than fading". This is the softest entry in the list. |
| Mom jeans and flares | FashionUnited (t29): searches for mom jeans fell 10% and for flared jeans 20%. Relaxed fits are 77% of Paris denim, led by straight and wide legs. |
| Athleisure as an outfit | FashionUnited SS27 report (t35): "Streetwear's dominance fades, and the waist and tailoring return". Brunette from Wall Street, 30 Apr 2025 (t48): athleisure is giving way to elevated casual wear. A search summary also has WWD's Paris FW26 buyer survey saying oversized and athletic looks "took a back seat"; I could not open it. |
| Cargo trousers | Brunette from Wall Street (t48): cargo pants are out through saturation. T-Fashion SS27 (t6): loose trouser shapes collapsed. The evidence is older and indirect, so this has low confidence. |
| Sheer-everything layering | T-Fashion Milan SS27 (t8): sheer fell from 9.8% to 6.3% "despite drawing press coverage". Lace and lingerie details carry on as t67. There is a single data source. |

## Classics

Classics are backed by evidence that they keep appearing across 2026's data and reporting, not by one season.

- **Trench coat:** Fenwick, longer trenches (t18). Florré, trench as dress (t39). FashionUnited, metallic trench (t33).
- **Navy blazer:** T-Fashion Paris SS27, blazers are 11.0% of looks and the #3 key item (t7). FashionUnited, jeans under blazers (t29). i-D, Prada blazers (t40).
- **Tailored trousers:** #1 key item for FW26 (t2) and #2 for SS27 (t6).
- **Straight mid-blue jeans:** FashionUnited, the straight leg leads and mid-wash is 55% of Paris denim colour (t29). Fenwick, "intelligent denim" in clean mid-blue (t18).
- **Crisp white shirt:** T-Fashion, poplin rose from 8.4% to 10.1% for FW26 (t2). i-D, Prada's reduced basics (t40).
- **Fine knit and roll-neck:** Fenwick crew-necks and ribbed turtlenecks (t18). Harper's Bazaar, turtlenecks (t13).
- **Camel coat:** Camel is in T-Fashion's SS27 top 10 at 4.0% (t6). W Magazine, MaxMara wool coats (t23). Fenwick, camel in core palette (t18).
- **White leather trainers:** Lyst Q3, two low classic trainers in the top 4 products (t27, t28).
- **Loafers:** GQ Singapore, Berluti Capri loafers and the season's "designed to endure" ethos (t26). TheDoubleF, monk-strap shoes at Prada (t41).
- **Breton stripe top:** Stripes are in T-Fashion's top 3 patterns for both FW26 (7.0%, t2) and SS27 (8.9%, t6).
- **Leather jacket:** Style Academy, "Leather Everything" (t16). Harper's Bazaar, leather bombers (t13).
- **Denim jacket:** Harper's Bazaar, trucker jackets (t13). FashionUnited, double denim (t30).
- **Black dress:** Black is the #1 runway colour in every season counted: Constantin (t11) and T-Fashion FW26 at 54.1% (t2).
- **Cashmere cardigan:** Harper's Bazaar (t13). Fenwick (t18).
- **Ankle and Chelsea boots:** Harper's Bazaar, ankle boots and the "simple uniform" of faux fur, denim and sleek ankle boots (t13).
- **White T-shirt:** i-D, Prada SS27 built on jeans, T-shirts and blazers (t40).
- **Ballet flats:** Lyst Q3, Chanel ballet flats #9 (t27). Harper's Bazaar (t13).
- **Simple watch:** Lyst Q3, the Cartier Tank is the first watch on the hottest-products list since 2021 (t27).

---

## Full dated source list

| ID | Date | Publisher | Title | URL |
|---|---|---|---|---|
| t1 | 2026-10-22 | RTÉ Lifestyle | 5 colour trends the fashion crowd are wearing this autumn/winter | https://www.rte.ie/lifestyle/fashion/2026/1022/1596414-5-colour-trends-the-fashion-crowd-are-wearing-this-autumn-winter/ |
| t2 | 2026-10-07 | T-Fashion | FW26 Womenswear Trends, all cities (13,370 looks) | https://tfashion.ai/runway/fw26 |
| t3 | 2026-10-07 | T-Fashion | Paris FW26 (5,607 looks) | https://tfashion.ai/runway/fw26/paris |
| t4 | 2026-10-07 | T-Fashion | Milan FW26 | https://tfashion.ai/runway/fw26/milan |
| t5 | 2026-10-07 | T-Fashion | New York FW26 (3,306 looks) | https://tfashion.ai/runway/fw26/new-york |
| t6 | 2026-10-18 | T-Fashion | SS27 Womenswear, all cities (12,320 looks) | https://tfashion.ai/runway/ss27 |
| t7 | 2026-10-18 | T-Fashion | Paris SS27 (3,751 looks) | https://tfashion.ai/runway/ss27/paris |
| t8 | 2026-10-18 | T-Fashion | Milan SS27 (2,070 looks) | https://tfashion.ai/runway/ss27/milan |
| t9 | 2026-10-18 | T-Fashion | New York SS27 (3,261 looks) | https://tfashion.ai/runway/ss27/new-york |
| t10 | 2026-10-18 | T-Fashion | London SS27 colours | https://tfashion.ai/runway/ss27/london/colors |
| t11 | 2026-03-14 | Sarah Constantin (Substack) | FW26 Color Stats | https://sarahconstantin.substack.com/p/fw26-color-stats |
| t12 | 2026-03-19 | InStyle (via AOL) | 20 Fashion Month Trends for Fall 2026 | https://www.aol.com/articles/20-fashion-month-trends-fall-130230680.html |
| t13 | 2026-02-17 | Harper's Bazaar (via AOL) | The Best New York Fashion Week Street-Style Looks Are a Master Class in Winter Dressing | https://www.aol.com/articles/best-york-fashion-week-street-195500446.html |
| t14 | 2026-03-11 | The Zoe Report | Paris Fashion Week's Boldest Trends For Fall/Winter 2026 | https://www.thezoereport.com/fashion/paris-fashion-week-fall/winter-2026-trends |
| t15 | 2026-02-03 | The Zoe Report | The 5 Trends That'll Rule The Streets This Fashion Month | https://www.thezoereport.com/fashion/fashion-month-fall-winter-2026-street-style-predictions |
| t16 | 2026-03-12 | Style Academy International | The Biggest Runway Trends from AW26 Fashion Month | https://www.styleacademyintl.com/post/the-biggest-runway-trends-from-autumn-winter-2026-fashion-month |
| t17 | 2026-09-01 | Style Academy International | Fall 2026 Color Trends | https://www.styleacademyintl.com/post/fall-2026-color-trends |
| t18 | 2026-09-01 | Fenwick | AW26 trends worth investing in | https://fenwick.co.uk/blogs/daily-muse/the-autumn-fashion-trends-worth-investing-in |
| t19 | 2026-07-27 | Editor & Co | AW26 Colour Trends | https://editorandco.com/article/autumn-winter-2026-colour-trends-the-shades-defining-the-season |
| t20 | 2026-02-12 | DesignTAXI | Pantone NYFW AW26/27 colour report | https://designtaxi.com/news/427452/PANTONE-Fall-Winter-Color-Trends-For-2026-2027-Paint-A-Bold-Balancing-Act/ |
| t21 | 2026-02-26 | Haute Garbage (Substack) | LFW Fall 2026: The Trends To Know | https://hautegarbage.substack.com/p/lfw-fall-2026-trends |
| t22 | 2026-10-07 | #legend | Womenswear AW26 runway report (part 2 of 4) | https://hashtaglegend.com/post/womenswear-autumn-winter-2026-runway-report-2 |
| t23 | 2026-03-02 | W Magazine | 10 Things We Saw and Loved at Milan Fashion Week Fall 2026 | https://www.wmagazine.com/fashion/milan-fashion-week-fall-2026-recap |
| t24 | 2026-02-03 | Style Arcade | Men's Fashion Week FW26-27 Trend Report | https://www.stylearcade.com/blog/mens-fashion-week-fall-winter-2026-27-trend-report |
| t25 | 2026-01-28 | Istituto Marangoni | Inside Milan Fashion Week Men's FW 2026-2027 | https://www.istitutomarangoni.com/en/maze35/industry/milan-fashion-week-menswear-fw-2026-27-trends |
| t26 | 2026-09-08 | GQ Singapore | Fall/Winter '26: Has Menswear Had Enough of Novelty? | https://www.gqsingapore.com/fall-winter-26-report-has-menswear-had-enough-of-novelty/ |
| t27 | 2026-10-28 | Lyst | The Lyst Index Q3-26 (page undated; date from same-day press coverage, t28) | https://www.lyst.com/the-lyst-index/Q3-26/ |
| t28 | 2026-10-28 | FashionUnited | Prada is back on top, dethroning Chanel in Lyst's Q3 2026 Index | https://fashionunited.in/news/fashion/prada-is-back-on-top-dethroning-chanel-in-lysts-q3-2026-index/2026102856741 |
| t29 | 2026-10-13 | FashionUnited | After the barrel jean, denim settles into the relaxed straight leg | https://fashionunited.com/news/fashion/after-the-barrel-jean-denim-settles-into-the-relaxed-straight-leg/2026101375097 |
| t30 | 2026-10-17 | FashionUnited | SS27 Double denim trend | https://fashionunited.com/news/fashion/ss27-double-denim-trend-at-conner-ives-zimmermann-and-more/2026101775133 |
| t31 | 2026-10-24 | FashionUnited | SS27 Color Trend: Many shades of green | https://fashionunited.com/news/fashion/ss27-color-trend-many-shades-of-green-at-chanel-prada-loewe/2026102475309 |
| t32 | 2026-10-29 | FashionUnited | SS27 Color Trend: Visual ASMR and harmonious pastels | https://fashionunited.com/news/fashion/ss27-color-trend-visual-asmr-and-harmonious-pastels-shown-at-bottega-veneta-dior-and-more/2026102975414 |
| t33 | 2026-10-10 | FashionUnited | PFW SS27 trend watch: Metallics shine | https://fashionunited.com/news/fashion/pfw-ss27-trend-watch-metallics-shine-at-saint-laurent-dior-and-balmain/2026101075076 |
| t34 | 2026-10-23 | FashionUnited | Galeries Lafayette's Victoria Dartigues on SS27 | https://fashionunited.com/news/fashion/galeries-lafayettes-victoria-dartigues-on-ss27-we-want-joyful-and-sparkling-fashion/2026102375268 |
| t35 | 2026-05-28 | FashionUnited | SS27 trend report: pleasure, protection and the quiet return of elegance | https://fashionunited.com/news/fashion/spring-summer-2027-trend-report-navigating-a-polycrisis-season-of-pleasure-protection-and-the-quiet-return-of-elegance/2026052872627 |
| t36 | 2026-09-21 | FashionUnited | Pantone NYFW SS27 colour trend report | https://fashionunited.com/news/fashion/pantone-nyfw-spring-summer-2027-color-trend-report-the-top-10-colors/2026092174743 |
| t37 | 2026-10-07 | The Zoe Report | Top Trends From Paris Fashion Week SS27 | https://www.thezoereport.com/fashion/paris-fashion-weeks-spring-summer-2027-trends |
| t38 | 2026-10-14 | Daily Front Row | 7 Trends That Took Over Paris Fashion Week! | https://fashionweekdaily.com/7-trends-that-took-over-paris-fashion-week/ |
| t39 | 2026-10-10 | Florré | The 6 Biggest Trends at the Spring 2027 Runway Shows | https://www.florre.co/master-blogs/the-spring-2027-trend-report-fashion |
| t40 | 2026-06-23 | i-D | Are Men Okay? At Spring 2027 Fashion Week, They're Great | https://i-d.co/article/pitti-uomo-milan-fashion-week-mens-spring-2027-recap/ |
| t41 | 2026-07-02 | TheDoubleF (The Rooster) | Milan & Paris men's Fashion Week SS27 | https://www.thedoublef.com/en-it/blogs/the-rooster/paris-milan-fashion-week-2027-spring-summer-menswear |
| t42 | 2026-07-09 | Style Cartel | The 5 Menswear Trends Defining SS27 | https://stylecartel.com/from-florence-to-paris-the-5-menswear-trends-defining-spring-summer-2027/ |
| t43 | 2026-09-24 | Forbes | London Fashion Week SS27 key trends (via search summary; page blocked) | https://www.forbes.com/sites/laiafarrangraves/2026/09/24/london-fashion-week-springsummer-2027-key-trends-to-know/ |
| t44 | 2026-10-08 | Forbes | Paris Fashion Week SS27 key trends (via search summary; page blocked) | https://www.forbes.com/sites/laiafarrangraves/2026/10/08/paris-fashion-week-springsummer-2027-key-trends-to-know/ |
| t45 | 2025-04-29 | WGSN | WGSN and Coloro reveal Colour of the Year 2027, Luminous Blue, and S/S 27 Key Colours | https://www.wgsn.com/en/wgsn/press/press-releases/wgsn-and-coloro-reveal-colour-year-2027-luminous-blue-and-s-s-27-key |
| t46 | 2024-09-03 | WGSN | Colour of the Year 2026: Transformative Teal | https://www.wgsn.com/en/blog/colour-year-2026-transformative-teal |
| t47 | 2025-12-04 | TIME | Pantone Chooses White as Color of the Year for the First Time | https://time.com/7338176/pantone-color-of-the-year-2026/ |
| t48 | 2025-04-30 | Brunette from Wall Street (Substack) | AW25-26: what is out of fashion | https://fashiontrendsandoutfits.substack.com/p/aw25-26-out-of-fashion |

### Context sources (not cited in the JSON)

| Date | Publisher | Title | URL |
|---|---|---|---|
| 2026-04-09 | WGSN | Colour of the Year 2028: Radiant Earth | https://www.wgsn.com/en/blog/colour-year-2028-radiant-earth |
| undated | Coloro | Key Colours (S/S 27 to A/W 28/29) | https://coloro.com/key-colors |
| 2026-10-16 (as of) | colorcombinations.org | Color of the Year 2027: all picks so far (Pantone not yet announced) | https://colorcombinations.org/trends/color-trends-2027/ |
| undated | Lyst | The Lyst Index Q2-26 (jelly shoes, thong heels, shield sunglasses: summer 2026, now over) | https://www.lyst.com/the-lyst-index/Q2-26/ |

### Undated corroboration (seen only via search summaries or secondary recaps)

These agree with the dated evidence above. They are left out of the JSON because I could not open them or confirm their dates.

- **Who What Wear:**
  - "The Biggest Color Trends of Fall 2026, Period": royal purple, Yves Klein blue, poppy red and buttercream, as recapped by StyleGuru in October 2026 (https://www.whowhatwear.com/fashion/fall-fashion-color-trends-2026).
  - "17 Key Trends to Know for Autumn/Winter 2026": checks as the leading print, plastic raincoats, preppy dressing.
  - "The 7 Cult Buys Setting the Trends for Autumn 2026": purple knits, flap bags, khaki jeans, medallion belts.
  - A fall-editor buys piece: croc-effect bags, satin ballet flats, suit skirts.
  - "Dated fall fashion trends 2026".
- **WWD:**
  - "Fall 2026 Milan Fashion Week Trends: Power Tailoring, Black and More".
  - "Milan Fashion Week Fall 2026: Retailers Praise Craftsmanship, Layering Trend". Buyers cited layering, strong outerwear, pony hair and shearling, and the return of the stiletto.
  - "Paris Fashion Week Fall 2026: Buyers Favor Coats, Corsets & Chanel Buzz".
  - "Paris Fashion Week: Skirts, Femininity & Fun Define Spring 2027 Trends".
- **Fashionista:**
  - Milan Fall 2026: mustard yellow and seafoam green.
  - Paris Fall 2026: tomato red.
  - NYFW Spring 2027: sweaters and scarves moving from shoulders to the waist.
  - Paris Spring 2027: 3D florals, bubble volume, crisp shirts and denim, citrus.
- **TLM Edit, "The Colours of Autumn 2026":** aubergine, Burnt Olive, chocolate as a neutral, navy, fuchsia accent (https://www.tlmedit.com/blogs/tlm-edit-style-memos/the-colours-of-autumn-2026-what-we-ll-all-be-wearing-this-season).
- **Heuritech, Men's FW26 Fashion Weeks Report (February 2026):** denim endurance, the return of pink, V-necks and moccasins; brown is the anchor colour (https://heuritech.com/market-reports/fw-26-men-fashion-weeks-report/).
