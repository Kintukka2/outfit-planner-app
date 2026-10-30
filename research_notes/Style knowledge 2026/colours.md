# Colour pairing: research notes

Research for the colour part of Hanger's on-device style knowledge base. The data lives in `colours.json` next to this file. These notes explain where it came from, how the scores were set, and where the sources disagree.

Researched 29 October 2026. Every pair, palette and rule in the JSON cites at least one source id (`c1` to `c44`, listed at the end).

## Summary of the JSON

| Section | Count | Notes |
|---|---|---|
| sources | 44 | 35 read in full, 2 read in part (c26 headline behind a paywall, c36 name and code only), 7 seen only through search summaries (marked below) |
| pairs | 203 | 21 score 2, 167 score 1, 14 score -1, 1 score -2. All 30 colours appear. |
| palettes | 48 | 16 neutral-only, 32 with accents; every season covered; 4 have four colours |
| profiles | 30 | 13 neutral, 15 accent, 2 metallic |
| rules | 20 | general guidance, each 200 characters or fewer |

The minimum number of positive partners per colour is 6 (TEAL, MINT, MUSTARD, SILVER). The most connected colours are NAVY and BROWN, with 26 positive partners each.

The build script checks that:
- every `bestWith` entry is a positive pair and every `avoidWith` entry is a negative pair;
- no palette contains a negative pair;
- no pair appears twice;
- every source is cited at least once.

## How the scores were set

| Score | Meaning | Test used |
|---|---|---|
| 2 | Classic or iconic | Two or more independent sources call it a classic, timeless or "always works", or it is a long-standing tailoring staple (navy and grey, navy and white, navy and light blue). |
| 1 | Reliable, good | At least one named stylist, editor or retailer guide recommends it without caveats. |
| -1 | Risky, works only with care | Stylists call it a clash or warn about it, but they also say how to make it work: mute one colour, let one lead, or keep the rest neutral. Most negatives sit here because almost every "never" in recent sources comes with a "unless". |
| -2 | Clash, avoid | Only pure red with pure green. Several sources reject it outright ("only Santa Claus can pull off red and green", c7). Sterling Style Academy (c39) and Gentleman's Gazette (c9) give the same fix: deepen it to burgundy and olive. That fix is in the data as BURGUNDY+OLIVE = 1. |

Pairs not listed should be treated as neutral (0 or unknown), not as bad. I only listed pairs I could source. Some likely good pairs are therefore missing, such as CHARCOAL+CAMEL, GREY+CAMEL and CREAM+DENIM. They are absent because I found no direct citation for them, not because they clash.

I did not invent any clash. Every negative pair has a source that names it, or names the principle behind it (two near-identical colours, as in c29).

## Findings by topic

### Classic and reliable pairings
These come up again and again across Who What Wear (c1, c13, c14, c16), Woman & Home (c2), PureWow (c3), FashionBeans (c7, c8), ShortList (c29) and the VOU (c28):
- **Navy** with white, camel, grey or charcoal, brown, beige, red and light blue. ShortList's Selfridges buyer: "Navy and charcoal or navy and grey always go... you can go anywhere now or in 15 years."
- **Black** with white, camel and red. Jenna Riddle in c7 suggests black with camel at roughly 70:30: "an all-black look... a camel overcoat as a finishing touch".
- **Brown** with blue in all its forms. FashionBeans: "the perfect partner to this classic neutral will always be blue. The pairing gives blue depth, while the brown appears richer."
- **Brown** also goes with cream, mustard and burgundy. Brown and burgundy in particular is framed as "the new black and beige" (c21).
- **Pink with grey** comes from three independent sources (c2, c3, c7), so it scores 2.
- Woman & Home's "red, white, navy and blue" (Kelsey Noelle Norris: "pretty much anyone can pull off black, white, navy, and true red") supports NAVY+RED = 2.

### Complementary, analogous and tonal dressing
- **Definitions** (c9, c38, c31):
  - Complementary colours sit opposite each other on the colour wheel and give the most contrast.
  - Analogous colours sit next to each other and look calm and related.
  - Monochromatic or tonal dressing uses tints and shades of a single hue.
- **Complementary pairs** need control. Gentleman's Gazette: "don't go for the true hues of all of these colours... or you will fall into that trap of looking like a clown"; tint or shade them instead. Westwood Hart (c31) gives a reliable formula: "two complementary colours anchored by a neutral", for example blue jeans, an orange sweater and a beige coat.
- **Analogous pairs** are easy. Highbrow (c30): "the closer the colours are on the colour wheel, the easier it is to have them coordinated". FashionBeans (c8) lists, for each difficult colour, which colours are "similar (easiest)", "contrasting (harder)" and "complementary (hardest)". Those lists were used directly. For example:
  - purple is hardest with yellows and harder with reds and greens;
  - orange is hardest with blue-violets;
  - green is harder with reds and violets.
- **Tonal dressing** (c4, c17, c12, c43):
  - vary the shade ("toffee, camel, cream and ecru");
  - vary the texture ("a silk skirt with faux-fur boots", "leather with mohair");
  - optionally break it up with a contrasting bag or shoe, or with metallics.
  - Turnbull & Asser's Michael Searle (c12) suggests a light-to-dark progression: pastel shirt, mid-tone knit, darker outer layer.
- **Near-misses** are the trap inside tonal dressing. ShortList (c29): "never pick colours that are too similar. If you're going to wear two greens, make sure there's a strong contrast." This is the basis for OLIVE+GREEN = -1 and YELLOW+MUSTARD = -1.

### Neutrals as anchors
- Every general guide recommends this:
  - Gok Wan (c2): "Start with a neutral base, then layer in one bold colour."
  - Highbrow (c30): "one item of colour and the rest of your outfit neutral".
  - Aza Fashions (c43): "Statement colours work best when the rest of the outfit steps back."
  - FashionBeans (c8) gives neutral anchors for each difficult colour:
    - yellow: white, pale grey, charcoal and navy;
    - orange: grey, navy and black;
    - purple: navy, grey, white and beige.
- **Which neutrals suit pastels.** Stylist (c25) says pastel and light trend colours work better on light or warm neutrals (white, beige, cream, even charcoal). Black and navy "tend to absorb rather than highlight" them. That one source is the basis for BLACK+MINT and BLACK+LAVENDER = -1. NAVY+LAVENDER stays positive because Aza (c43) names "lilac with navy or charcoal" as a polished pairing.

### The 60-30-10 idea
- The proportion comes from interior design: about 60% main colour, 30% secondary and 10% accent.
- Imogen Lamport (Inside Out Style, c27), a colour consultant, gives the fullest version:
  - the 10% is usually in accessories;
  - colours that read alike count as one;
  - an 80/20 or 80/10/10 split also works;
  - "what you generally want to avoid is even proportions of colours".
- Stylist Kelsey Noelle Norris (c2) uses "60% one colour, 30% a contrast, and 10% a pop".
- A Stylist feature from 22 October 2026 (c26) shows the idea is current. Its body is paywalled, so only the headline was read.
- The VOU (c28) notes that the 60-30-10 split and the three-colour rule are separate ideas, and that the proportions are a prompt, not a measurement.

### How many colours an outfit carries
- About three is the consensus:
  - Westwood Hart (c31): "three colours in a single outfit is a reliable upper limit... typically two neutrals and one non-neutral".
  - Stitch Fix (c5): "stick to three colors maximum".
  - Best Life quotes Sally Samuels (c32) on keeping to about three.
- Guides differ on whether neutrals count:
  - the VOU (c28) counts black, white, grey and navy, including shoes;
  - some menswear guides do not count neutrals.
- My recommendation for the app: count distinct colour families, merging tonal shades. Do not count GOLD or SILVER, and do not penalise a fourth colour when it is a neutral.

### Contrast
- Gentleman's Gazette (c9) and Inside Out Style (c27) both tie outfit contrast to personal colouring:
  - people with high contrast between hair and skin carry bold or complementary pairings well;
  - people with low contrast look better in muted, close-toned outfits.
- This is optional in the brief, so it appears as a single rule (c-rule-14) and does not affect pair scores.
- Within an outfit, two dark neutrals need texture or a lighter third colour so they do not blur together. This is a recurring theme in both debates below.

### The navy and black debate
- **For.** Modern sources treat the old rule as dead:
  - Who What Wear (c18): "a style myth... easily disproved";
  - Who What Wear (c13): "Forget what you've heard";
  - Who What Wear (c19): "as long as you match the right tones, these two are... the perfect pairing";
  - PureWow (c3): pairing them "dispels the old rule";
  - Mr Porter (c35, via search summary) says the French and Japanese have long worn them together.
- **Caveats.**
  - Stitch Fix (c5): avoid it "when the navy garment is so dark it's almost black", which "veers into ninja territory".
  - c19 suggests lighter navy with glossy black, with white socks as a break.
  - Corporette and M.M.LaFleur (search summaries only, not cited) suggest a third colour or a "bridge" piece.
- **Against.** OkDiario (c40, summary only) says the pair "can look dull and muddy".
- **Decision:** BLACK+NAVY = 1, with the caveat in the "why" and in c-rule-07.

### The black and brown debate
- **For.**
  - The Zoe Report (c6): "once considered a sartorial taboo — emphasis on once". Its tips:
    - mix textures (glossy black leather with soft brown cashmere);
    - let one colour be the focus;
    - try brown pattern with crisp black.
  - Who What Wear (c15, c17) calls it "one of the most sophisticated pairings".
  - c20 says to "find matching tones": warmer browns with solid black, softer browns with grey-toned black.
- **Hesitant.**
  - Stitch Fix (c5, 2018): only "if there is a significant contrast in the shades and you're going for a more casual look".
  - A stylist in c6: "you need a refined sense of style... the combination is often quite daring".
- **Against.** A Flathead Beacon columnist (c42, summary only): "competing neutrals and way too harsh next to each other".
- **Decision:** BLACK+BROWN = 1, because the weight of recent sources (2024 to 2026) is positive. The "why" and c-rule-08 carry the conditions: one colour leads, the textures differ, and the two are not the same depth.

### Mixing metals
- The Zoe Report (c10), quoting five jewellery designers:
  - mixing is now "highly encouraged";
  - let your favourite metal dominate rather than splitting 50/50;
  - start with a "bridge piece that has both silver and gold";
  - mix finishes (shiny gold with matte silver).
- Vitaly (c41, a jewellery retailer) suggests a 60/40 split and a consistent style.
- Metallics also work as neutrals for breaking up tonal outfits (c4). Gold appears with warm and earthy colours (c2: tomato red with gold accessories; earthy greens and browns with gold). Silver appears with cool, matte navy and grey (c13) and dark denim (c23).
- **Decision:** GOLD+SILVER = 1.
- No source said silver clashes with warm colours or gold clashes with cool ones, so there are no metallic negatives.

### Denim as a neutral
- Sources:
  - Who What Wear (c14): "Blue denim can act as a neutral".
  - c16: jeans are "practically a blank canvas"; brown, camel, white, navy, red and butter yellow lift them most.
  - c22 and c23: dark indigo works with navy, white and neutrals, and double denim works "when you match up your tones".
  - FashionBeans (c8) and Westwood Hart (c31) add yellow and orange.
- **Decision:** DENIM pairs positively with 17 colours and has no negatives. In c16, black with jeans is "a sound option" that can look "a little too stark against some washes", so BLACK+DENIM is 1, not 2.

### Seasonal palettes
- Westwood Hart (c31): "pastels suit spring and summer, while deeper tones such as burgundy and forest green suit autumn and winter". Highbrow (c30) gives per-season suggestions.
- **Autumn earth tones:**
  - rust, olive, camel, brown, terracotta and khaki (c43, c12);
  - ochre with dark brown or forest green, and plum with brown (c2);
  - brown with maroon (c21).
- **Summer pastels and brights:**
  - pastel pink with mint (c2);
  - butter yellow with sky blue, chocolate with baby blue, and bubblegum pink with merlot, all from Spring 2026 runways (c24);
  - pistachio with blush, and cherry red with lavender (c25, from the SS25 catwalks).
- **Winter:**
  - jewel tones: teal with burgundy, violet with teal, fuchsia with emerald (c1, c2);
  - rich browns and greys (c14);
  - winter whites.
- **White in winter.** The "no white after Labor Day" rule is a late-19th-century class marker (c11, c37). Who What Wear (c18) lists it among the dead rules. Farmers' Almanac (c37) keeps one useful remnant: fabric weight sets the season (linen reads summer, wool reads winter). Pantone's 2026 Colour of the Year is a white, Cloud Dancer (c36). This is the basis for c-rule-13 and for WHITE and CREAM having empty `seasons` lists.

### Colours that clash or need care
These are the 15 negative pairs and what each rests on:

| Pair | Score | Basis |
|---|---|---|
| GREEN + RED | -2 | c7 ("only Santa Claus"); c39 (pure red with pure green is "overly aggressive", use burgundy and olive); c9 (true-hue complements look clownish). Woman & Home's "red with forest green" uses a deep green, which the GREEN token is not. |
| BLUE + PURPLE | -1 | c7: "nobody wants to look like a fresh bruise"; swap to navy and burgundy. c2 and c31 like soft or muted versions, so not -2. |
| RED + PURPLE | -1 | c32 (stylist Tracy Cheng: they clash and overwhelm); c8 (harder pair). PureWow (c3) likes cherry red with violet, so not -2. |
| ORANGE + PURPLE | -1 | c1 ("shouldn't work" but can); c8 (hardest pair for orange). |
| YELLOW + PURPLE | -1 | c8 (hardest pair for both); c9 (complements); c29 (gives careful placement advice). MUSTARD + PURPLE stays +1 (ochre with violet, c2). |
| GREEN + YELLOW | -1 | c1: vivid green and yellow "teeter on the side of garish"; use muted versions. MINT + YELLOW is +1. |
| GREEN + ORANGE | -1 | c40 (bright green with bright orange feels loud); c30 (avoid power clashing). c2 likes muted versions (Sandy Lancaster: pumpkin or saffron with light sage or lime). |
| GREEN + PURPLE | -1 | c8 (harder pair from both sides). |
| ORANGE + RED | -1 | c40 (can blur); c30 (power clash). c1 shows it working with neutral accessories, so it is -1, not -2. |
| RED + HOT_PINK | -1 | c2, Susie Hasler: "Yes, they clash - that clash is what makes them work... but the colours must be vibrant". A deliberate clash. Soft PINK + RED is +1 (c3). |
| ORANGE + HOT_PINK | -1 | Deliberate clash (c30). PureWow (c3) praises orange with soft pink, so ORANGE + PINK is +1. |
| BLACK + MINT | -1 | c25: black absorbs light, fresh colours. |
| BLACK + LAVENDER | -1 | c25; c8 lists beige, grey and blues as lavender's partners. |
| OLIVE + GREEN | -1 | c29: two greens need strong contrast. |
| YELLOW + MUSTARD | -1 | c29: two near-matching yellows. |

Old "rules" I deliberately did **not** encode as negatives, because current sources reject them:
- navy with black;
- black with brown;
- pink with green ("pink and green should never be seen... disregard it", c1);
- pink with red (c2, c3);
- white after summer;
- mixing gold and silver;
- blue with green (FashionBeans says green "always looks best paired with blue"; Woman & Home calls it "cool, fresh, and striking").

## Where sources disagree

| Question | Position A | Position B | Used in data |
|---|---|---|---|
| Navy + black | Fine, even chic (c3, c13, c18, c19, c35) | Muddy, or only if navy is clearly blue (c40, c5) | 1, with the caveat in the why |
| Black + brown | Sophisticated (c6, c15, c17, c20) | Only casual or with strong contrast (c5); never (c42) | 1, with conditions |
| Charcoal + brown | "Match made in heaven" (c14, knitwear) | Charcoal is "the only colour that should not be worn with brown shoes" (c34, formal tailoring) | 1. The why says to choose black shoes with formal suits. |
| Blue + purple | Fresh bruise, avoid (c7) | Bright blues and purples pair well (c2); muted purple with blue (c31) | -1 |
| Red + purple | Clash (c32), harder (c8) | Cherry red and violet "works every time" (c3) | -1 |
| Red + orange | Can blur (c40) | "Will always serve you well" with neutral accessories (c1) | -1 (works with care) |
| Black or navy with pastels | Darker neutrals dull pastels (c25) | Lilac with navy or charcoal is polished (c43) | BLACK + MINT and BLACK + LAVENDER = -1; navy and charcoal pastel pairs positive |
| Pink + teal | Complementary, hardest to pair (c8) | Dusky pink with petrol blue is chic (c2) | 1 |
| Do neutrals count toward three colours? | Yes, including shoes (c28) | No, neutrals are foundations (some menswear guides) | Rule says shades count once; app logic should not count metallics |
| Burgundy + grey | Less good than burgundy with navy (c33/c34, summary only, unclear) | Burgundy with soft grey "feels rich" (c43) | LIGHT_GREY + BURGUNDY = 1 (c43), CHARCOAL + BURGUNDY = 1 (c28); GREY + BURGUNDY left unlisted |

## Mapping real-world colour names to the 30 tokens

Sources use many shade names. This is how I mapped them, so other researchers and the app's colour-snapping can stay consistent:

| Token | Shade names mapped to it |
|---|---|
| BLACK | jet, noir, inky black |
| CHARCOAL | charcoal grey, dark grey |
| GREY | mid grey, marl grey, heather grey |
| LIGHT_GREY | pale grey, soft grey, light grey |
| WHITE | optic white, crisp white, Cloud Dancer |
| CREAM | ivory, ecru, off-white, oat, winter white |
| BEIGE | stone, taupe, biscuit, nude, sand |
| CAMEL | tan, caramel, toffee, cognac |
| KHAKI | the sand or stone chino colour (US sense). *Ambiguous:* British "khaki" can mean green. I used KHAKI for the sandy chino colour and OLIVE for military green. |
| BROWN | chocolate, espresso, cocoa, mocha, chestnut, mole |
| NAVY | midnight, inky blue |
| DENIM | mid-wash blue jeans. Very dark indigo sits between DENIM and NAVY; c22 treats indigo and navy as tonal partners. |
| OLIVE | military green, moss, khaki green |
| BLUE | cobalt, royal, electric, azure, sapphire, Yves Klein |
| LIGHT_BLUE | sky, powder, baby, cerulean, duck egg |
| TEAL | turquoise, petrol, peacock, lagoon, aqua |
| GREEN | emerald, forest, bottle, jade, kelly |
| MINT | pistachio, matcha, pale green. *Sage* is ambiguous (between MINT and OLIVE), so no sage pairing was scored. |
| YELLOW | butter, lemon, canary |
| MUSTARD | ochre, saffron, honey, golden yellow |
| ORANGE | tangerine, pumpkin, bright orange, coral (borderline) |
| RUST | terracotta, burnt orange, sienna, brick (orange-leaning) |
| RED | tomato, cherry, poppy, geranium, true red, brick red (red-leaning) |
| BURGUNDY | maroon, merlot, wine, claret, oxblood |
| PINK | blush, rose, dusty rose, baby pink, bubblegum, pastel pink |
| HOT_PINK | fuchsia, magenta, neon pink |
| PURPLE | violet, plum, royal purple, ultra violet |
| LAVENDER | lilac, mauve, soft purple |

**Peach** (c1's rust and peach) was mapped to PINK for RUST + PINK. Woman & Home's soft pink with terracotta (c2) supports the same pair.

## Limitations and gaps

- **Blocked sites.**
  - The search tool would not crawl GQ, Vogue, Esquire, Harper's Bazaar, Elle, The Cut, InStyle, Glamour or The Guardian, so none of them are cited.
  - Mr Porter, Permanent Style, Best Life and Net-a-Porter blocked direct fetching (HTTP 403).
  - For Mr Porter (c35), Permanent Style (c33, c34) and Best Life (c32), I used only what search summaries reported.
  - Sterling Style Academy (c39), OkDiario (c40) and Flathead Beacon (c42) were also summary-only (JavaScript-rendered, removed, or not fetched).
  - These seven sources are never the only support for a positive pair, and are used mainly to record disagreement. GREEN + ORANGE leans on OkDiario plus Highbrow's general "avoid power clashing" advice, so it is the weakest negative.
- **Search budget.** The shared web-search budget for this session ran out partway through. Later work relied on fetching URLs already found. I could not run more searches for:
  - an authoritative print colour-theory textbook (Wikipedia, c38, stands in for definitions);
  - WGSN or Coloro forecasts;
  - Best Life's full list of 10 clashes, of which only red with purple was confirmed.
- **Gendered sources.** Several sources are written for one audience (menswear guides, women's magazines). Their colour advice was taken as garment-neutral, and nothing in the JSON is gendered. Some source titles contain "Men's" because they are quoted exactly.
- **Skin tone and personal colouring** (seasonal colour analysis) are only touched on in c-rule-14. It deserves its own module if the app ever collects user colouring.
- **Patterns.** c-rule-20 notes that pattern colours count toward the outfit's colour total. Pattern mixing is otherwise out of scope here.

## Suggestions for the app

- **Outfit score:** average the pair scores over every pair of distinct colours in the outfit. Ignore GOLD and SILVER in pairs with other colours unless a pair is listed. Treat unlisted pairs as 0.
- **Penalties and bonuses:**
  - more than three non-neutral colour families: -1, using c-rule-02;
  - two accents, both bright, from the clash list: use the -1 or -2 score;
  - every piece within one colour family (tonal): small bonus, using c-rule-04.
- **"What goes with this colour" explorer:** show `profiles[X].bestWith` first, then the other positive pairs sorted by score. Show `avoidWith` with the "why", and offer the softer alternative named in it (for example, red and green leads to burgundy and olive).
- **Seasonal suggestions:** palettes carry the season in their name or "why" text, not in a separate field, because the schema had none. If the app needs to filter palettes by season, the `profiles[*].seasons` of their colours give a reasonable proxy.

## Sources

"Read" means the article text was fetched and read. "Summary" means only search-engine summaries were available.

| id | Publisher | Title | Date | Access | Used for |
|---|---|---|---|---|---|
| c1 | Who What Wear UK (Maxine Eggenberger) | [These 16 Classic Colour Combinations Will Look Good Forever](https://www.whowhatwear.com/complementary-colours-outfits) | 2019-02-21 | Read | 16 named pairings; clash caveats (garish green and yellow, orange and purple) |
| c2 | Woman & Home (Anna Paul; stylists Gok Wan, Sandy Lancaster, Bella Hignett, Susie Hasler, Kelsey Noelle Norris, Shauna Colaci) | [Colour combinations to make your wardrobe pop, whatever the season](https://www.womanandhome.com/fashion/amazing-colour-combinations-loved-by-stylists/) | 2025-09-20 | Read | Many pairings, 60-30-10, neutral base, seasonal palettes |
| c3 | PureWow (Abby Hepworth) | [8 Color Combinations That Will Never Go Out of Style](https://www.purewow.com/fashion/timeless-color-combinations) | 2018-11-28 | Read | Navy + yellow, navy + beige, pink + red, black + navy, and others |
| c4 | Who What Wear (Judith Jones) | [How to Master Tonal Dressing Like You Know What You're Doing](https://www.whowhatwear.com/how-to-wear-tonal-colors) | 2019-03-01 | Read | Tonal dressing technique, metallics |
| c5 | Stitch Fix (Ask a Stylist) | [Can I wear navy with black?](https://www.stitchfix.com/men/blog/ask-a-stylist/wear-navy-and-black-together/) | 2018-09-24 | Read | Navy and black; black and brown caveat; three-colour maximum |
| c6 | The Zoe Report (Kelsey Stewart) | [How To Wear Black & Brown Together Like A Pro](https://thezoereport.com/fashion/how-to-wear-black-brown-together) | 2024-09-19 | Read | Black and brown tips |
| c7 | FashionBeans | [5 Colour Combinations That Always Work](https://www.fashionbeans.com/2017/5-colour-combinations-that-always-work/) | 2017-02-23 | Read | Black + camel, navy + burgundy, grey + pink, red/green and blue/purple warnings |
| c8 | FashionBeans | [How To Wear The Most Difficult Menswear Colours](https://www.fashionbeans.com/2017/how-to-wear-tricky-menswear-colours/) | 2021-09-07 | Read | Easiest, harder and hardest partners for pink, green, brown, orange, purple, yellow and lavender |
| c9 | Gentleman's Gazette (Preston Schlueter) | [How To Use The Color Wheel To Assemble Superior Outfits](https://www.gentlemansgazette.com/how-to-use-the-color-wheel-to-assemble-superior-outfits/) | 2019-01-18 | Read | Colour wheel, complements, contrast and colouring |
| c10 | The Zoe Report (Kelsey Stewart) | [How To Mix Gold & Silver Jewelry, According To The Pros](https://www.thezoereport.com/fashion/how-to-mix-gold-and-silver-jewelry) | 2024-08-20 | Read | Mixing metals |
| c11 | Mental Floss (Kathy Benjamin) | [Why Can't You Wear White After Labor Day?](https://www.mentalfloss.com/culture/fashion-beauty/why-cant-you-wear-white-after-labor-day) | 2019-08-29 | Read | Origin of the white rule |
| c12 | Country & Town House (Charlie Colville; Michael Searle, Turnbull & Asser) | [The Men's Guide: How To Wear & Style Colour](https://www.countryandtownhouse.com/style/fashion/how-to-wear-colour-men/) | 2024-10-18 | Read | Start small, tonal progression, natural pairings |
| c13 | Who What Wear UK (Bébhinn Campbell) | [Navy Always Looks Expensive: 8 Graceful Colours to Wear it With](https://www.whowhatwear.com/colours-that-go-with-navy) | 2023-10-05 | Read | Navy partners |
| c14 | Who What Wear UK (Avalon Afriyie) | [These Six Colours Pair Beautifully With Brown](https://www.whowhatwear.com/colours-that-go-with-brown) | 2023-11-01 | Read | Brown partners; denim as a neutral |
| c15 | Who What Wear UK (Chloe Gallacher) | [5 Ways to Wear Black and Brown Together](https://www.whowhatwear.com/fashion/outfit-ideas/black-and-brown-outfit-ideas) | 2025-12-12 | Read | Black and brown |
| c16 | Who What Wear UK (Maxine Eggenberger) | [6 Elegant Colours That Will Elevate Basic Blue Jeans](https://www.whowhatwear.com/fashion/denim/colour-trends-to-wear-with-blue-jeans) | 2025-02-05 | Read | Denim partners |
| c17 | Who What Wear UK (Natalie Munro) | [The Chicest Shades to Pair With Chocolate Brown](https://www.whowhatwear.com/fashion/celebrity-style/chocolate-brown-colour-combination-trends-over-50s-julianne-moore-emma-thompson-jennifer-lopez) | 2025-10-27 | Read | Black + brown, tonal browns |
| c18 | Who What Wear (Anna LaPlaca) | [11 Black-and-Navy Outfits You Need to Try](https://www.whowhatwear.com/black-and-navy-outfits) | 2017-11-17 | Read | Navy and black; dead rules (white, brown and black) |
| c19 | Who What Wear UK (Jess Kelham-Hohler) | [Navy Always Looks Expensive: 5 Shoe Colours](https://www.whowhatwear.com/fashion/shoes/what-colour-shoes-to-wear-with-navy) | 2024-08-09 | Read | Navy with black, white, gold, brown, red and khaki |
| c20 | Who What Wear UK (Jess Kelham-Hohler) | [6 Chic Colours to Wear With Black Jeans](https://www.whowhatwear.com/fashion/denim/colour-trends-to-wear-with-black-jeans) | 2025-10-07 | Read | Black partners |
| c21 | Who What Wear (Michelle Scanga) | [Brown and Maroon Are the New Black and Beige](https://www.whowhatwear.com/fashion/outfit-ideas/brown-maroon-color-trend-fall-2025) | 2025-09-24 | Read | Brown + burgundy, denim + burgundy |
| c22 | Who What Wear UK (Felicia Pennant) | [Dark Jeans Look Best With This Non-Black Neutral](https://www.whowhatwear.com/fashion/shopping/french-girl-dark-denim-navy-combo) | 2026-01-07 | Read | Indigo + navy |
| c23 | Who What Wear UK (Melissa Epifano) | [Elite Dressers Are Wearing Dark-Wash Denim](https://www.whowhatwear.com/fashion/outfit-ideas/dark-wash-denim-outfits) | 2026-02-24 | Read | Dark denim, double denim, silver |
| c24 | Marie Claire (Lauren Tappan) | [3 Viral Color Combinations I'm Holding On To](https://www.marieclaire.com/fashion/put-together-summer-2026-color-combinations/) | 2026-08-14 | Read | Spring/summer 2026 runway pairings |
| c25 | Stylist | [Summer colour combos](https://www.stylist.co.uk/fashion/summer-fashion-colour-combinations/986287) | 2025-05-19 | Read | SS25 pairings; avoid black and navy with pastels |
| c26 | Stylist (Soraya Gaied Chortane) | [This simple 60/30/10 formula made my outfits look instantly better](https://www.stylist.co.uk/fashion/60-30-10-outfit-formula/1120653) | 2026-10-22 | Headline only (paywall) | Shows 60-30-10 is current |
| c27 | Inside Out Style (Imogen Lamport) | [Using the 60/30/10 Rule to Create Fabulous Colour Combinations](https://insideoutstyleblog.com/?p=27881) | 2019-03-19 | Read | 60-30-10 in detail, value contrast |
| c28 | The VOU | [The Three-Colour Rule, How to Count Colours and When to Bend the Rule](https://thevou.com/blog/three-colour-rule-style-guide-men/) | 2025-01-05 (updated 2026-10-11) | Read | Colour counting, worked outfits |
| c29 | ShortList (Mithun Ramanandi, Selfridges) | [How not to clash colours](https://shortlist.com/news/how-not-to-clash-colours) | 2011-01-27 | Read | Near-miss colours, navy + grey/charcoal |
| c30 | Highbrow (Eddy Larkson) | [Color Mixing, Matching, and Power Clashing](https://gohighbrow.com/color-mixing-matching-and-power-clashing) | n/a | Read | One colour plus neutrals, power clashing, seasons |
| c31 | Westwood Hart | [Men's fashion color combinations: a practical guide](https://westwoodhart.com/blogs/westwood-hart/mens-fashion-color-combinations-guide-pairing-colours) | 2026-04-08 | Read | Schemes, three-colour limit, seasons |
| c32 | Best Life (Tracy Cheng, Sally Samuels) | [Stylists Say You Should Never Wear These 10 Colors Together](https://bestlifeonline.com/colors-you-should-never-wear-together/) | n/a | Summary | Red + purple, about three colours |
| c33 | Permanent Style (Simon Crompton) | [Green is the third colour](https://www.permanentstyle.com/2013/11/green-is-the-third-colour.html) | 2013-11 | Summary | Navy and grey core; green, purple and burgundy with navy |
| c34 | Permanent Style (Simon Crompton) | [How to wear colour](https://www.permanentstyle.com/2013/12/how-to-wear-colour.html) | 2013-12 | Summary | Blue + brown tone contrast; charcoal and brown shoes |
| c35 | Mr Porter, The Journal | [You Asked: What's The Rule On Navy And Black?](https://www.mrporter.com/en-gb/journal/fashion/you-asked-whats-the-rule-on-navy-and-black-539166) | n/a | Summary | Navy and black is fine |
| c36 | Pantone | [Cloud Dancer, Color of the Year 2026](https://www.pantone.com/color-of-the-year/2026) | 2025-12 | Read (name and code only) | White as the year's colour |
| c37 | Farmers' Almanac (Amber Kanuckel) | [Why You Cannot Wear White After Labor Day](https://www.farmersalmanac.com/wear-white-after-labor-day-35950) | 2025-08-29 | Read | Fabric weight sets the season |
| c38 | Wikipedia | [Color scheme](https://en.wikipedia.org/wiki/Color_scheme) | living page | Read | Definitions: monochromatic, complementary, analogous |
| c39 | Sterling Style Academy | [What Colors Should I Avoid Pairing Together?](https://online.sterlingstyleacademy.com/blog/what-colors-should-i-avoid-pairing-together) | 2025-11-18 | Summary | Pure red + pure green; burgundy + olive fix |
| c40 | OkDiario | [Never wear these 2 colors together](https://okdiario.com/metabolic/en/psychology/miscellany-and-curiosities/never-wear-these-2-colors-together-they-completely-clash-and-could-ruin-your-outfit-23810/) | n/a | Summary (page now 404) | Navy + black muddy, bright green + orange, red + orange (low authority) |
| c41 | Vitaly | [How To Mix Silver and Gold Jewelry Pieces](https://www.vitalydesign.com/blogs/editorial/how-to-mix-silver-and-gold-jewelry) | 2025-07-25 | Read | 60/40 metal split, consistent style |
| c42 | Flathead Beacon | [Black and Brown Throwdown](https://flatheadbeacon.com/?p=25134) | n/a | Summary | Dissent on black + brown |
| c43 | Aza Fashions (Ishika Sen) | [Consider This Your Menswear Colour Cheat Sheet](https://www.azafashions.com/blog/menswear-colour-cheat-sheet/) | 2026-08-13 | Read | Earth, deep, pastel and statement pairings; tonal grey |
| c44 | Who What Wear (Allyson Payer) | [What to Wear With Black to Look More Stylish](https://www.whowhatwear.com/what-to-wear-with-black) | 2019-01-25 | Read | Jewellery and neutral layers with black |

Low-quality, machine-rewritten pages that turned up in search were seen but not cited.
