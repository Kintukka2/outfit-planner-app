# Pairings and styling rules: research notes

Companion to `pairings.json` in this folder. It feeds Hanger's on-device style knowledge base: the outfit scorer and its explanations (for example "Loafers sharpen chinos" or "Running shoes undercut a blazer") and the in-app style guide.

Researched 29 October 2026. All `a`/`b` values in the JSON are exact type names or pattern tokens from the shared vocabulary. The build script checked token spelling, that each pair spans two categories, that no unordered pair repeats, the score values, the length limits (`why` 110 characters or fewer, rule `text` 200 or fewer), the minimum counts, and that every `src` id exists and every source is cited.

## What's in the file

| Section | Count | Notes |
|---|---|---|
| `sources` | 76 | p1 to p76; full list at the end of this file |
| `pairings` | 236 | 87 at +2, 118 at +1, 19 at -1, 12 at -2 (31 negatives) |
| shoes with a bottom or one-piece | 93 | spans all 14 shoe types except Rain boots |
| top with bottom | 45 | |
| layer with top or bottom | 33 | |
| outerwear with anything | 35 | |
| tie, belt, scarf, hat (incl. beanie and cap) or bag | 24 | 8 tie, 7 belt, 3 scarf, 2 headwear, 4 bag |
| `patterns` | 45 | all unordered pairs of the 9 patterns, doubles included; SOLID is 2 with everything |
| `rules` | 72 | shoes 12, proportion 9, accessories 9, layering 8, pattern 7, tucking 6, formality 6, fit 5, texture 5, denim 5 |

Some pairings also count as one-piece layering: Slip dress with T-shirt, Knit sweater, Blazer, Cardigan and Leather jacket; Jumpsuit with Blazer, Shirt, Belt and Handbag; Maxi dress with Blazer; Dress with Leather jacket and Belt.

## How to read the scores

- **2 (classic):** the textbook pairing most sources name first, such as Loafers with Chinos, Blazer with Jeans or Hoodie under Puffer jacket.
- **1 (good):** works and is widely recommended, but it is not the default. Many high-low pairings sit here.
- **-1 (risky, needs care):** works only under a condition, and the `why` names it. Examples: Tie with Knit sweater (only a V-neck), Loafers with Cargo trousers (only slim, smooth cargos), Backpack with Blazer (fine with jeans, not with a suit).
- **-2 (avoid):** the sources agree it clashes, usually because of a formality gap (Running shoes with Trousers, Dress shoes with Shorts) or a broken dress code (anything sporty with Evening dress).

A score describes the pairing in its usual context. The app's formality and occasion logic should still carry the weight. For example, Sneakers with Trousers scores +1, and the `why` already limits it to clean, low-profile pairs and smart-casual settings. The formality rule "Formal codes stay formal" covers black tie and business formal.

## Where current practice has replaced old rules

Each of these is scored for how people dress now, and the `why` says so:

1. **Sneakers with suits (Sneakers with Trousers or Blazer, +1).** Mr Porter's Journal now calls trainers with a suit "A-OK" if they are clean, low-profile and classic, and still warns off high-tops and performance styles. Bloomberg (2024) reports Wall Street professionals wearing sneakers with suits as banks loosen their dress codes. Ape to Gentleman (2026) sets the limits: white or cream works with almost any suit, black sneakers with a dark suit look "schoolboy", and formal or business dress codes still mean leather shoes. **Running shoes** stay at -2 with tailoring. Every source separates minimal leather sneakers from runners and chunky soles.
2. **Hoodie under a blazer or overcoat (+1).** FashionBeans (2025) calls a casual blazer over a hoodie "a high-low balance" and says an overcoat "instantly elevates" a hoodie. Net-a-Porter frames it as a take on power dressing, and Who What Wear and The Modest Man both show it. The conditions are a plain, trim hoodie (roomy but not sloppy), a relaxed blazer, and jeans, chinos or wool trousers rather than joggers. Blazer with Joggers stays at -1.
3. **Running-style sneakers with dresses (Running shoes with Slip dress, +1).** Refinery29 (2025) pairs retro runners and athletic sneakers with slip and white cotton dresses. This is a recent shift, and running shoes with tailoring is still rated as avoid.
4. **Mixed metals in jewellery.** Rapaport (2024) reports gold-and-silver mixing as an established trend. Belt buckle and watch hardware still follow the "match your metals" rule (Modest Man, Hartt), so the two ideas are separate rules.
5. **Double denim** is treated as normal, not a fashion risk (Denim jacket with Jeans, +1). The care point is wash and fit, not whether to wear it at all.
6. **Leggings outside the gym** are scored on how they are styled: oversized blazers, long knits, flats or loafers (+1). With a sports top they are active-only.

## Disagreements between sources

- **Double denim washes.** La Redoute (2025) says to keep washes similar for cohesion and warns that mismatched washes look unintentional. OPUMO (2026) and The Quality Edit (2025) prefer washes "a couple of shades apart", darker on the bottom. *Resolution:* two denim rules, "Contrast your washes" and "Or commit to one wash". The common ground is that a near-miss is the only wrong answer.
- **Cargo trousers with loafers or a blazer.** The Modest Man (2026) lists loafers, a sport coat and an unlined blazer as fine with cargos. Real Men Real Style (2026) says to avoid most loafers and tailoring, and allows suede loafers only with slim, smooth cargos. *Resolution:* both pairings score -1 with that condition.
- **Puffer over tailoring.** FashionBeans (2024) allows a slim, structured or blazer-style puffer, or a gilet under the jacket. Real Men Real Style treats parkas and other casual coats as unsuited to suits, and Die, Workwear! and RMRS point to overcoats, topcoats and trenches. *Resolution:* Puffer jacket with Blazer and Parka with Blazer both score -1, and a formality rule says how to make it work.
- **Half tuck or French tuck.** MasterClass (Tan France's front tuck) recommends it for loose tees and knits with mid to high-rise bottoms. RMRS's polo guide recommends a half-tuck with jeans. FashionBeans editors (2024) advise against it as sloppy. *Resolution:* the rule limits it to loose tees and knits with mid to high rises and does not apply it to shirts with tails.
- **Shirt under a jumper.** RMRS's tucking guide says to always tuck under a sweater. RMRS's sweater-and-shirt guide allows about an inch of tail with jeans. *Resolution:* the tucking rule says to tuck for smart looks and allows an inch with jeans.
- **Polos.** RMRS's tucking guide lists polos as worn out. RMRS's polo guide says to tuck with chinos or a suit and half-tuck with jeans. *Resolution:* the "Polos by bottom" rule follows the more specific polo guide.
- **Flannel shirts.** RMRS says to tuck flannel and work shirts. FashionBeans says never to tuck flannel except for DIY. No pairing depends on this, so it isn't encoded.
- **How strictly to match belt and shoes.** Gentleman's Gazette treats shoes, belt and watch strap sharing a colour family as traditional. Put This On (2010) says they should "roughly match" and that a perfect match tries too hard. The Modest Man calls the rules strict and partly outdated. *Resolution:* the rules match leather by colour family and finish rather than exact shade, and relax them for casual outfits.
- **Black shoes with jeans.** Permanent Style (2024) says black works but is less versatile than brown suede. FashionBeans (2026) says black derbies clash with mid-blue but suit stonewashed denim, and that sleek black Chelsea boots belong with black or grey jeans. RMRS (blazer with jeans) says to avoid black shoes in most cases. *Resolution:* the rule "Brown suede for denim" favours brown suede and keeps sleek black shoes for black or grey denim.
- **Stripes with checks.** Inside Out Style (2021) and Gentleman's Gazette say to vary the scale and avoid matching sizes. One menswear piece in the same search results said the opposite for *different* pattern types, that their scales should be similar. I couldn't open that page to confirm who said it, so it isn't encoded. *Resolution:* STRIPE with CHECK scores +1 with "vary the scale".
- **Animal print.** Inside Out Style treats leopard as a neutral that mixes with stripes and checks. The "one bold pattern" rule (Gentleman's Gazette's busy/quiet principle) argues for caution with other bold prints. *Resolution:* ANIMAL scores +1 with STRIPE and CHECK, and -1 with FLORAL, DOT, OTHER and another ANIMAL.
- **Backpack with a suit.** Bespoke Post says to avoid it with a full suit but finds a blazer with jeans fine. Advice columns in the same results (Ask a Manager, Attire Club) were more permissive in business-casual offices. *Resolution:* Backpack with Blazer scores -1, and the `why` says when it works.
- **Boots with shorts.** RMRS rules out boots with shorts entirely. Some editorial coverage pairs boots with denim shorts as a trend look, but I didn't find a source I could cite. *Resolution:* Boots, Chelsea boots and Tailored shorts pairings score -1, not -2.
- **Running shoes.** Mr Porter and Ape to Gentleman reject performance or chunky styles with suits. Refinery29 (2025) endorses retro runners with dresses. These don't really conflict, because the outfits differ. The app should keep Running shoes -2 with Trousers and Blazer and +1 with Slip dress.
- **No-show socks.** RMRS calls them the right choice with loafers, sneakers and shorts. Gentleman's Gazette rules them out for formal wear. Both appear in one rule, "When to go no-show".

## Mapping notes (precise wording to app type)

- Derby, oxford, brogue and monk strap become **Dress shoes**. Chukka, desert, work, combat, military and riding boots become **Boots**. Kitten heels, slingbacks, pumps and court shoes become **Heels**. Ballet flats and Mary Janes become **Flats**. Mules and slides become **Sandals**. Boat shoes and driving moccasins become **Loafers**. Retro runners and dad trainers become **Sneakers**, but performance running shoes are **Running shoes**.
- Suit jacket, sport coat and unstructured jacket become **Blazer**, and suit trousers become **Trousers**. Topcoat, overcoat, polo coat, peacoat and car coat become **Coat**. Field, chore, wax and quilted jackets become **Jacket**. Trucker becomes **Denim jacket**. Flight jacket and blouson become **Bomber jacket**. Quilted vest and padded gilet become **Gilet**, while a knitted vest is a **Sweater vest**. A henley is a **Long-sleeve top**, a knit polo is a **Polo**, and an oxford or chambray button-down is a **Shirt**.
- Chino or Bermuda shorts become **Tailored shorts** when pressed and knee-length, otherwise **Shorts**. Gym shorts become **Running shorts**. A jumper dress is a **Knit dress**.
- Socks and tights are app types in the `OTHER` category, which isn't in the research vocabulary. Sock guidance therefore lives in rules (topic `shoes`), not pairings.

## Coverage gaps and caveats

- **Not paired:** Rain boots, Overalls, Playsuit, Crossbody, Belt bag, Gym bag, Windbreaker, Hat (the brimmed type), Gloves, Sunglasses, Watch, Necklace, Earrings, Bracelet, Ring and Blouse (except with Scarf). Corduroys have only three pairings, and Skirt, Sundress and Shirt dress have one or two each. I found no citable guidance for these, or none I could read in full, and I didn't add unsourced pairings. Watch, metal and jewellery guidance is in the accessories rules.
- **Blocked sources:** Vogue, GQ, Esquire, Elle, Harper's Bazaar, The Cut, InStyle and the Guardian refuse this research tool. Mr Porter, Permanent Style, Net-a-Porter and MasterClass pages returned 403 errors when opened, and Who What Wear pages returned only their titles. For those, and for Bloomberg (p3, paywalled), claims come from the article excerpts that web search returned: p1, p3, p5, p8 to p11, p27, p34, p35, p40, p41, p43, p44, p46, p48, p51, p58, p60, p62 to p66 and p70 to p76.
- **Approximate attribution:** where one search returned several sibling Who What Wear articles, the citation points to the article in that set that best matches the claim. Attribution between those sibling articles may be imprecise. p70, p71 and p72 support their pairing by headline alone (ballet flats with wide-leg trousers, jumper with midi skirt, T-shirt with skirt).
- **Pages read in full:** FashionBeans, Real Men Real Style, The Modest Man, Gentleman's Gazette, Put This On, Die, Workwear!, Inside Out Style, Refinery29, Stylist, PureWow, La Redoute, OPUMO, The Quality Edit, Rapaport, Hartt, Bespoke Post, IMAGE, Harry Rosen, StudioSuits, Wikipedia and Ape to Gentleman.
- **Source mix:** the menswear sources (RMRS, The Modest Man, FashionBeans, Gentleman's Gazette) are written for a male readership, and Who What Wear and Refinery29 largely for women. Every `why` and rule describes the garment, never the wearer, so the guidance applies to anyone.
- **Weak extrapolations:** Flip-flops with Trousers (-2), Hiking boots with Evening dress (-2) and Dress shirt with Joggers (-2) extend a cited principle (formality clash or evening dress code) to the closest pair. The source didn't name these exact combinations.
- **Dates:** "n.d." means the page gave no date I could read. Real Men Real Style and The Modest Man show "last updated" dates, which are what the `date` field records.
- **Reproducing the file:** the build-and-validate script is in the session scratchpad, not the repo. Re-run `python3 -m json.tool pairings.json` after any hand edits.

## Full source list

| id | Publisher | Title | Date | Cited | URL |
|---|---|---|---|---|---|
| p1 | MR PORTER (The Journal) | You Asked: Can I Wear Sneakers With A Suit (And How)? | n.d. | 8 | https://www.mrporter.com/en-au/journal/fashion/you-asked-can-i-wear-sneakers-with-a-suit-and-how-902286 |
| p2 | Ape to Gentleman | How to Wear a Suit with Sneakers in 2026: A Visual Guide | 2026 (updated) | 13 | https://www.apetogentleman.com/suit-with-sneakers/ |
| p3 | Bloomberg | Sneaker Cleaning Services Replace Shoe Shines on Wall Street | 2024-06-24 | 2 | https://www.bloomberg.com/news/features/2024-06-24/sneaker-cleaning-services-replace-shoe-shines-on-wall-street |
| p4 | FashionBeans | What Shoes to Wear With Jeans (Jamie Millar) | 2026-10-27 | 10 | https://www.fashionbeans.com/article/what-shoes-to-wear-with-jeans/ |
| p5 | Permanent Style | Wearing black shoes with jeans | 2024-04 | 1 | https://www.permanentstyle.com/2024/04/how-to-wear-black-shoes-with-jeans.html |
| p6 | Real Men Real Style | What Shoes To Wear With Shorts? (2026 Summer Style Guide) (Antonio Centeno) | 2026-05-14 | 21 | https://www.realmenrealstyle.com/shoes-with-shorts/ |
| p7 | StudioSuits | Finding the Best Shoes to Wear with Chinos: From Dress Shoes to Sneakers | n.d. (c. 2024) | 8 | https://www.studiosuits.com/blogs/articles/best-shoes-for-chinos |
| p8 | Who What Wear | The 7 Best Shoe Styles to Wear With Wide-leg Trousers in 2026 | 2026 | 9 | https://www.whowhatwear.com/fashion/shoes/shoe-trends-to-wear-with-wide-leg-trousers-2026 |
| p9 | Who What Wear | The 15 Best Shoe Styles to Wear With Wide-Leg Pants | n.d. | 5 | https://www.whowhatwear.com/shoes-to-wear-with-wide-leg-pants |
| p10 | Who What Wear | 11 Tops That Go With Wide-Leg Trousers Best, Hands Down | n.d. | 4 | https://www.whowhatwear.com/fashion/outfit-ideas/tops-that-go-with-wide-leg-trousers |
| p11 | Who What Wear | Fashion People Everywhere Wear This Jacket Style to Transition From Winter to Spring | n.d. | 7 | https://www.whowhatwear.com/fashion/outfit-ideas/cropped-jacket-outfits |
| p12 | Harry Rosen | Ask Harry: How long should my pants be? | n.d. | 3 | https://www.harryrosen.com/en/editorial/ask-harry-how-long-should-my-pants-be |
| p13 | Stylist | How to style blazers with skirts: 4 easy outfit ideas (Soraya Gaied Chortane) | 2025-04-09 | 5 | https://www.stylist.co.uk/fashion/how-to-style-blazers-and-skirts/976694 |
| p14 | PureWow | The third-piece fashion trick (Sarah Stiefvater) | 2024-05-17 | 5 | https://www.purewow.com/fashion/third-piece-fashion-trick |
| p15 | Gentleman's Gazette | Color & Pattern Matching For Classic Spring & Summer Outfits (E.J. Daniels) | n.d. | 24 | https://www.gentlemansgazette.com/how-match-colors-patterns/ |
| p16 | Put This On | Textbook pattern mixing (Jesse Thorn) | 2010-03-22 | 2 | https://putthison.com/patternmixing/ |
| p17 | Inside Out Style | Do you mix patterns? Does everything have to match? (Imogen Lamport) | 2021-04 | 46 | https://insideoutstyleblog.com/2021/04/which-patterns-easily-mix-with-anything.html |
| p18 | Inside Out Style | How to Wear a Cropped Top when You Don't have a Defined Waist (Imogen Lamport) | 2016-02 | 2 | https://insideoutstyleblog.com/2016/02/wear-cropped-top-dont-defined-waist.html |
| p19 | La Redoute | How to wear double denim | 2025-07-31 | 6 | https://www.laredoute.co.uk/content/how-to-wear-double-denim/ |
| p20 | OPUMO Magazine | Double denim: How to pull it off in 2026 (Aobh O'Brien-Moody) | 2026 | 6 | https://www.opumo.com/magazine/double-denim-how-to-wear/ |
| p21 | The Quality Edit | The Denim-on-Denim Look That's So Back: How to Style a Canadian Tuxedo (Mia McManus) | 2025-05-28 | 4 | https://www.thequalityedit.com/articles/styling-the-canadian-tuxedo |
| p22 | Put This On | Q and Answer: Belts (Jesse Thorn) | 2010-04-22 | 7 | https://www.putthison.com/belts-2/ |
| p23 | The Modest Man | Matching your belt and shoes (Brock McGoff) | 2017-01-23 | 5 | https://www.themodestman.com/match-belt-and-shoes/ |
| p24 | Gentleman's Gazette | Combining Shoes, Socks, And Pants: A Beginner's Guide (E.J. Daniels) | 2024-08-02 | 10 | https://www.gentlemansgazette.com/beginners-combining-shoes-socks-pants/ |
| p25 | The Hartt Shoe Company | How to: Match Your Metal & Leather Accessories (Adriana Spragg) | 2020-03-27 | 2 | https://hartt.ca/blogs/style-guide/how-to-match-your-metal-leather-accessories |
| p26 | Rapaport Magazine | The Edgy Allure of Mixed Metals (Beth Bernstein) | 2024-05-22 | 1 | https://rapaport.com/magazine-article/the-edgy-allure-of-mixed-metals/ |
| p27 | Permanent Style | The rules and how to break them: Your socks should match your trousers | 2026-02 | 1 | https://www.permanentstyle.com/2026/02/the-rules-and-how-to-break-them-your-socks-should-match-your-trousers.html |
| p28 | Real Men Real Style | How To Layer With Style: 10 Rules (Antonio Centeno) | 2025-07-25 | 8 | https://www.realmenrealstyle.com/style-layer-rules/ |
| p29 | The Modest Man | How to Wear Layers During Fall and Winter + 16 Outfit Ideas (Brock McGoff) | 2026-07-14 | 18 | https://www.themodestman.com/how-to-wear-layers/ |
| p30 | The Modest Man | How to Layer Without Looking Bulky (Ian Chandler) | 2026-07-08 | 4 | https://www.themodestman.com/how-to-layer-without-looking-bulky/ |
| p31 | FashionBeans | How To Layer Every Jacket In Your Wardrobe (Ian Taylor) | 2024-07-11 | 6 | https://www.fashionbeans.com/article/how-to-layer-jackets/ |
| p32 | FashionBeans | Fashion Basics Part 79: Layering & Fabric Basics (Matt Allinson) | 2012-07-09 | 2 | https://www.fashionbeans.com/2012/texture-layering-basics/ |
| p33 | FashionBeans | The Easiest Way to Upgrade a Hoodie Without Ditching Comfort | 2025-07-08 | 9 | https://www.fashionbeans.com/article/easiest-way-to-upgrade-a-hoodie/ |
| p34 | NET-A-PORTER (PORTER) | The Fashion Memo: How to wear a hoodie now | n.d. | 2 | https://www.net-a-porter.com/en-ie/porter/article-6935923742753829/fashion/fashion-memo/how-to-wear-a-hoodie-now |
| p35 | Who What Wear | 13 Hoodie Outfits That Are Low-Key Elegant | n.d. | 4 | https://www.whowhatwear.com/hoodie-outfits |
| p36 | Real Men Real Style | 20 Types of Jackets: A Visual Guide (With Chart) (Antonio Centeno) | 2026-10-26 | 18 | https://www.realmenrealstyle.com/mens-coats-visual-guide/ |
| p37 | Die, Workwear! | The Ideal Coat Wardrobe | 2016-11-28 | 6 | https://dieworkwear.com/2016/11/28/the-ideal-coat-wardrobe/ |
| p38 | FashionBeans | How To Wear a Puffer Jacket To Stay Stylish in The Cold | 2024-12-18 | 7 | https://www.fashionbeans.com/article/how-to-wear-puffer-jacket/ |
| p39 | Real Men Real Style | Overcoat vs. Peacoat vs. Parka: Which One Fits Your Lifestyle? (Antonio Centeno) | 2026-10-26 | 7 | https://www.realmenrealstyle.com/overcoat-vs-peacoat-vs-parka/ |
| p40 | Who What Wear | 9 Best Boot Styles to Wear With Mini, Midi and Maxi Dresses | n.d. | 12 | https://www.whowhatwear.com/boots-to-wear-with-mini-midi-and-maxi-dresses |
| p41 | Who What Wear | 11 Shoes to Wear With Midi Dresses That Work for Summer | n.d. | 8 | https://www.whowhatwear.com/shoes-to-wear-with-midi-dresses |
| p42 | Refinery29 | 6 Dress & Sneaker Combos To Try This Summer (Laura Lajiness Kaupke) | 2025-05-15 | 5 | https://www.refinery29.com/en-us/summer-dress-sneakers |
| p43 | Who What Wear | How to Wear Dresses and Trainers: 7 Looks to Love | n.d. | 4 | https://www.whowhatwear.com/how-to-wear-dresses-and-trainers |
| p44 | Editorialist | Best Sneakers to Wear With Dresses: How to Wear Sneakers & Dresses | n.d. | 6 | https://editorialist.com/fashion/best-sneakers-to-wear-with-dresses/ |
| p45 | Refinery29 | How To Take The Terror Out Of A Slip Dress (Connie Wang) | 2016-11-20 | 3 | https://www.refinery29.com/en-us/2016/11/130173/how-to-wear-slip-dresses |
| p46 | Who What Wear | 10 Ways to Style A Slip Dress for Fall | n.d. | 7 | https://www.whowhatwear.com/how-to-wear-a-slip-dress-in-the-fall |
| p47 | Refinery29 | The Secret To Not Looking Frumpy In A Jumpsuit (Connie Wang) | 2015-08-16 | 5 | https://www.refinery29.com/en-us/jumpsuit-fashion |
| p48 | Who What Wear | How to Wear a Jumpsuit: 10 Chic Jumpsuit Outfits | n.d. | 6 | https://www.whowhatwear.com/jumpsuit-outfits |
| p49 | Real Men Real Style | Tucked In Vs Untucked Shirts: The Rules (Antonio Centeno) | 2026-09-08 | 11 | https://www.realmenrealstyle.com/rules-tucking-shirt/ |
| p50 | FashionBeans | How To Tuck In A Shirt Properly (And Keep It There) | 2024-10-01 | 2 | https://www.fashionbeans.com/article/how-to-tuck-in-shirt/ |
| p51 | MasterClass | How to Pull Off the Perfect French Tuck | n.d. | 1 | https://www.masterclass.com/articles/how-to-pull-off-the-perfect-french-tuck |
| p52 | Real Men Real Style | How To Wear A Sport Coat With Jeans (7 Tips + Color Chart) (Antonio Centeno) | 2026-10-13 | 16 | https://www.realmenrealstyle.com/sports-jacket-with-jeans/ |
| p53 | Real Men Real Style | Sweater With Collared Shirt: 5 Rules + 3 Outfits (Antonio Centeno) | 2026-10-15 | 9 | https://www.realmenrealstyle.com/mens-sweater-and-shirt/ |
| p54 | Gentleman's Gazette | 5 Ways To Wear A Knit Tie (Sven Raphael Schneider) | 2018-01-22 | 12 | https://www.gentlemansgazette.com/ways-to-wear-knit-ties/ |
| p55 | Real Men Real Style | How to Style a Knit Tie (Antonio Centeno) | 2026-05-26 | 4 | https://www.realmenrealstyle.com/knit-tie-guide/ |
| p56 | The Modest Man | What To Wear With Cargo Pants: More Versatile Than You Think (Karlton Miko Tyack) | 2026-06-22 | 11 | https://www.themodestman.com/what-to-wear-with-cargo-pants/ |
| p57 | Real Men Real Style | How To Wear Cargo Pants: Fit, Cuts & 4 Outfits (2026) (Antonio Centeno) | 2026-09-16 | 22 | https://www.realmenrealstyle.com/how-wear-cargo-pants/ |
| p58 | Who What Wear | The 3 Best Shoes to Wear With Cargo Pants, Hands Down | n.d. | 2 | https://www.whowhatwear.com/shoes-to-wear-with-cargo-trousers |
| p59 | Bespoke Post | The Tricks to Wearing a Backpack Like an Adult (Macaela MacKenzie) | n.d. | 2 | https://www.bespokepost.com/field-guide/the-tricks-to-wearing-a-backpack-like-an-adult |
| p60 | MasterClass | Tan France's Tips for Styling Different Fabric Textures | n.d. | 5 | https://www.masterclass.com/articles/tan-frances-tips-for-styling-different-fabric-textures |
| p61 | IMAGE | How To Mix And Match Texture (Marie Kelly) | 2016-09-30 | 1 | https://www.image.ie/fashion/mix-match-texture-68605 |
| p62 | Who What Wear | What Not to Wear With Leggings in 2023, According to an Editor | 2023 | 5 | https://www.whowhatwear.com/what-not-to-wear-with-leggings |
| p63 | Who What Wear | 5 Ways to Style Leggings Outfits in 2026 | 2026 | 6 | https://www.whowhatwear.com/fashion/outfit-ideas/leggings-styling-hacks-2026 |
| p64 | Who What Wear | 3 Tops to Wear With Midi Skirts | n.d. | 5 | https://www.whowhatwear.com/tops-to-wear-with-midi-skirts |
| p65 | Who What Wear | How to Wear a Crop Top and Not Show People Your Belly Button | n.d. | 3 | https://www.whowhatwear.com/how-to-wear-a-crop-top-covered-up |
| p66 | Who What Wear | Cool Girls Are Wearing Long-Sleeve T-Shirts With Midi-Skirts Right Now | 2026 | 1 | https://www.whowhatwear.com/fashion/celebrity-style/long-sleeve-t-shirt-midi-skirt-outfit-trend-elsa-hosk-2026 |
| p67 | Real Men Real Style | Ralph Lauren Polo Shirts: Fits, Fabrics & Worth It? (Antonio Centeno) | 2026-09-08 | 10 | https://www.realmenrealstyle.com/ralph-lauren-polo-styles/ |
| p68 | Wikipedia | Black tie | accessed 2026-10-29 | 8 | https://en.wikipedia.org/wiki/Black_tie |
| p69 | Wikipedia | Clutch (handbag) | accessed 2026-10-29 | 2 | https://en.wikipedia.org/wiki/Clutch_(handbag) |
| p70 | Who What Wear | Wide Leg Trousers and Ballet Flats Outfits 2026 | 2026 | 1 | https://www.whowhatwear.com/fashion/outfit-ideas/wide-leg-trousers-ballet-flats-outfits-2026 |
| p71 | Who What Wear | How to Do the Jumper-and-Midi-Skirt Trend | n.d. | 1 | https://www.whowhatwear.com/jumper-midi-skirt-trend |
| p72 | Who What Wear | 7 T-Shirt-and-Skirt Outfits That Signal You're a Fashion Person | n.d. | 2 | https://www.whowhatwear.com/fashion/outfit-ideas/t-shirt-over-skirt-outfit |
| p73 | Who What Wear | 8 Ways to Style Leggings With the Most Elevated Basics | n.d. | 2 | https://www.whowhatwear.com/leggings-with-basics-outfits |
| p74 | Who What Wear | The 8 Coolest Under-$100 Tops to Wear With Your Favorite Leggings | n.d. | 3 | https://www.whowhatwear.com/tops-to-wear-with-leggings |
| p75 | Who What Wear | Wearing One of These 5 Outfits Will Make Leggings Look Cool, Not Dated, in 2026 | 2026 | 2 | https://www.whowhatwear.com/fashion/outfit-ideas/fall-2026-leggings-outfits |
| p76 | Who What Wear | And Now, 14 Legging Outfits for Every Day of the Year | n.d. | 3 | https://www.whowhatwear.com/best-legging-outfits |
