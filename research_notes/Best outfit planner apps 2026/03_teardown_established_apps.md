# Teardown of the established second-tier outfit planner / digital wardrobe apps (Cladwell, Smart Closet, Pureple, Combyne, Fits, OpenWardrobe, Save Your Wardrobe, GetWardrobe + ShopLook and others), as of 25 Oct 2026

Method and conventions (applies to all sections):
- All App Store data comes from the US storefront, pulled on 25 Oct 2026 through the iTunes Lookup API (`https://itunes.apple.com/lookup?id=<id>&country=us`) and from the apps.apple.com product pages. Those pages embed the version history, the In-App Purchase list and the privacy "nutrition" label. I downloaded every iPhone screenshot in `screenshotUrls` to the scratchpad and looked at each one. The UI descriptions below come from those images, and the listing URL is cited as their source.
- Google Play install bands come from the play.google.com detail pages, fetched on 25 Oct 2026.
- "Ratings" means US App Store rating count and average unless stated otherwise.
- The App Store's In-App Purchase list shows only the top ~10 SKUs, so some SKUs may be missing. Legacy and regional SKUs also appear in that list, so the list alone is not a reliable guide to current pricing.
- I created no accounts and logged into nothing. Onboarding and paywall details therefore come from release notes, help centres and screenshots, not from installing the apps. Where nothing was documented, I say so.

---

## 1. Cladwell: what it is, how it works, every mechanic (including the capsule / daily-outfit engine)

### Takeaway
Cladwell is the original "capsule wardrobe" outfit app (Cincinnati, since 2016). After about two years of bug-fix-only releases, it relaunched between 3 and 24 Oct 2026 (v5.13 → v6.4) as an LLM-driven "AI stylist". The free tier is now only a closet catalog plus wear tracking. Everything that generates anything sits behind "Your Stylist" ($9.99/mo or $79.99/yr on the web) with a monthly AI allowance: daily outfits, a seasonal ~33-piece capsule, color analysis, a gap-filling shopping list, try-on and a chat stylist with memory. Its distinctive mechanic is still capsule-centric: outfits are generated within a selected capsule, and the planner counts "outfits unlocked" per missing piece.

### Cited Findings

**Identity and status**
- The developer/seller is Cladwell, Inc. It was first released on the App Store on 15 Mar 2017. The current version is 6.4 (24 Oct 2026). It has 1,036 US ratings (avg 4.25), is rated 4+, supports 13 languages and requires iOS 15. — [iTunes Lookup](https://itunes.apple.com/lookup?id=1140550878&country=us)
- The company is "The Cladwell Group, Inc. · Cincinnati, Ohio · since 2016". It claims 1M+ downloads since 2016, 13M items cataloged and 100M outfits suggested, and says "the average user wears 65% of their closet". — [cladwell.com/about](https://cladwell.com/about)
- Erin Flynn is presented as "the founder of Cladwell". She also wrote *The Road Less Worn* and co-owns East Main Cabinets & Design. — [cladwell.com/from-the-founder](https://cladwell.com/from-the-founder)
- One secondary source tells a different story. It lists the founders as Blake Allsmith, Erin Flynn, Tim Brunk, Chris Merchich and Greg Brunk, and says Erin Flynn and her husband Colin Flynn bought the company in Aug 2019 for an undisclosed price. — [theygotacquired.com](https://theygotacquired.com/saas/cladwell-acquired/) (not confirmed by a primary source)
- Platforms are iPhone, iPad, Android (100K+ installs) and a web subscription. A ChatGPT connector also exists. New accounts must sign up in the mobile apps. — [cladwell.com/faq](https://cladwell.com/faq); [Google Play](https://play.google.com/store/apps/details?id=com.cladwell)
- Status: very active. Six App Store releases shipped between 3 and 24 Oct 2026: 5.13.0, 5.14.0, 5.14.1, 5.15.0, 6.0, 6.1, 6.2, 6.3 and 6.4. Version 6.5 was in TestFlight on 25 Oct 2026. — [App Store version history](https://apps.apple.com/us/app/id1140550878); [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps)

**Classic mechanics (2016–2023, for context on the "capsule engine")**
- In 2017 TechCrunch described the core idea as first shrinking your wardrobe, then using every piece "like a bartender with a full bar". It said the app weighed weather, season and current trends when recommending pieces. — [TechCrunch, 2 Oct 2017](https://techcrunch.com/2017/10/02/cladwell-helps-you-pick-out-your-clothes)
- Classic onboarding (per DesignRush): the user picks items "similar enough" to their own from a catalog of generic illustrated items. Recommendations start once at least 10 items are selected. The app asks for location so it can account for the weather and prompts the user to log what they wear each day. Users can also create capsule collections such as work and travel. — [DesignRush](https://www.designrush.com/best-designs/apps/cladwell)
- Cladwell's own writing sets out its heuristics. An item should make 3 or more outfits with what you own, otherwise "it's a special-occasion purchase". Check color before fit. Price items per wear. "Cladwell counts pairs, not pieces… tells you which categories are short and how many outfits each addition unlocks." — [cladwell.com/faq/should-i-buy-this](https://cladwell.com/faq/should-i-buy-this)
- Its cleanout rule keeps an item if it was worn in the last year, fits your current life, and "pairs with three other things you own". Two "no" answers mean the item goes; undecided items go in a dated "maybe box". — [cladwell.com/faq/closet-cleanout-quiz](https://cladwell.com/faq/closet-cleanout-quiz)
- A free six-step "capsule wardrobe programme" covers prep, cleanout, the freedom of a capsule, identifying gaps ("measured by the outfits each addition would unlock"), a season-long "capsule test drive", and finalising. — [cladwell.com/capsule-wardrobe-program](https://cladwell.com/capsule-wardrobe-program)
- On 1 Jun 2023 (v5.6.0) Cladwell launched "Ask Cladwell, powered by ChatGPT". The same release notes claim 1M+ downloads, 13M items, 100M outfits suggested and users "styled 3.5 million times". — [App Store version history](https://apps.apple.com/us/app/id1140550878)

**Onboarding (2026)**
- v5.13.0 (3 Oct 2026, "A new Cladwell") added a style quiz that produces a "Style Map", a color season from "three quick questions" plus a palette, "See it on me", a dedicated "Ask Cladwell" tab, a "My Style" hub, menswear imagery, and an annual plan with a 7-day free trial for eligible new subscribers. — [App Store version history](https://apps.apple.com/us/app/id1140550878)
- Photo color analysis reads undertone, contrast, depth and clarity from one daylight photo and "shows you what it's seeing". It is available on iPhone/iPad only, while the Style Quiz is on all platforms. — [cladwell.com/faq](https://cladwell.com/faq); [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps)
- The privacy policy says an unclaimed onboarding photo is kept for up to 7 days "so you can resume". This implies photo analysis can run before account creation. Users can "skip photo analysis and still use a reduced onboarding path". — [cladwell.com/privacy-policy](https://cladwell.com/privacy-policy)
- v5.14.0 added a wardrobe preference (menswear/womenswear) in Settings. "Color quiz images and Style Quiz retakes follow your choice." — [App Store version history](https://apps.apple.com/us/app/id1140550878)

**Item capture**
- There are three routes: choose from about 15,000 cataloged items ("most people never take a single photo"), paste a product link, or take a photo. — [cladwell.com/faq](https://cladwell.com/faq)
- From 5.11.0 (15 Apr 2025): "Snap some photos… select them in the app, and let our AI do the rest", meaning bulk add with AI auto-categorisation. — [App Store version history](https://apps.apple.com/us/app/id1140550878)
- "Add a piece and Cladwell recognizes and describes it. You can edit every detail." "Improve photo" shows a before/after slider and lets you keep the original. Several photos can be improved in a queue. Background removal and photo replacement are separate options. — [App Store listing](https://apps.apple.com/us/app/id1140550878); [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps)
- Via ChatGPT, users can add clothes from a photo, link, receipt or description. ChatGPT prepares a clean clothing image and saves it to the named capsule without a further review step. If no photo exists, it "can create a representative flat lay". — [cladwell.com/chatgpt](https://cladwell.com/chatgpt)

**Item data model**
- The item page in the screenshots shows: name; category plus capsule ("Layers · in Fall 26"); wear count ("4× worn"); last worn date; cost per wear ("$18 per wear"); user tags ("Work"); and an editable "What your stylist sees" block (color = Blue, texture = Knit, pattern = Solid, type = Normal Sweater, fit = Loose). It ends with "Outfits with this item". — [App Store screenshots](https://apps.apple.com/us/app/id1140550878)
- The stylist asks "quick questions about a piece so outfits fit how you wear it". — [App Store listing](https://apps.apple.com/us/app/id1140550878)
- A 6.5 TestFlight item card adds price, purchase month, wears, cost per wear and brand. — [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps)

**Organisation**
- The closet is organised into capsules: multiple named, focused subsets of the closet such as "Fall 26", with a capsule switcher in the header. There are also "Storage" (items excluded from active capsules), tags, search and filters. Lists can be found across capsules, while Favorites follow the selected capsule. — [App Store screenshots and v6.1/6.4 notes](https://apps.apple.com/us/app/id1140550878); [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps)
- Cladwell's 2026 report, based on 6,726 recent capsules, found a median capsule of 30 clothing-and-shoe pieces (quartiles 19/30/62). 97% of multi-capsule owners share at least one item across capsules. — [cladwell.com/capsule-wardrobe-report](https://cladwell.com/capsule-wardrobe-report)

**Outfit building and the recommendation engine**
- "Open the Outfits tab and new looks are waiting, built from your own pieces." Users can swipe for more, rate a look, ask for a restyle, swap any piece, or add looks inspired by a photo. Each outfit includes "a short stylist's note on why it works". — [App Store listing](https://apps.apple.com/us/app/id1140550878)
- v6.3: "Stylist recommended outfits now lead each day, with a ribbon that shows why." "View more outfits" gives extras. Ratings on Home "teach your stylist". The manual outfit builder takes up to 12 pieces with closet search and filters. Morning outfits can be toggled in Settings. — [App Store version history](https://apps.apple.com/us/app/id1140550878)
- v6.2: "a new day shows starter outfits until you ask for more", and requested outfits "appear as they're styled". This implies a cheap pre-computed set first, then on-demand LLM generation. — [App Store version history](https://apps.apple.com/us/app/id1140550878)
- The documented engine inputs are:
  - the selected capsule's items;
  - weather and location, sent rounded to model providers;
  - the Style Map;
  - the color palette;
  - logged wear history;
  - outfit ratings;
  - stylist "memory" (Settings › Your style preferences, which holds rules and remembered notes and can be switched off).
  — [cladwell.com/privacy-policy](https://cladwell.com/privacy-policy); [v6.1 notes](https://apps.apple.com/us/app/id1140550878); [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps)
- The season capsule (Style tab) contains "about 33 pieces, mostly ones you already own". It comes with "outfit formulas, which colors play which role, and the few pieces worth finding", and the user can tap "Make this my capsule". — [App Store listing](https://apps.apple.com/us/app/id1140550878)
- The capsule screenshot reads "22 of these 33 pieces are already in your closet. The rest are ideas, not a shopping list". It shows counters (22 In your closet / 11 Suggested / Fall). Pieces can be filtered as Tops·4 / Bottoms / Layers / Shoes, and suggested pieces appear as line-art illustrations (e.g. "Knit in a main color"). The screen ends with an "Outfit formulas" section. — [App Store screenshots](https://apps.apple.com/us/app/id1140550878)
- Gap logic on the website gives an example: one missing item (a dark-brown low heel) "would add nine outfits". — [cladwell.com](https://cladwell.com)
- The Home screenshot shows: capsule selector "Fall 26"; "Today" with weather 63°/51°; a single outfit card (bookmark, edit, chat icons, plus a try-on icon); a "Wear today" button; page dots for alternative outfits; and "Create a look". — [App Store screenshots](https://apps.apple.com/us/app/id1140550878)
- Morning outfit delivery appears in two conflicting states. Cladwell's guide says "Morning outfit delivery is not yet available". The Android listing says "Switch on morning outfits and a few looks are ready when you wake up". — [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps); [Google Play](https://play.google.com/store/apps/details?id=com.cladwell)

**Planning and logging**
- Users can plan a future day, save outfits to Lists (long-press the bookmark), and "Log on another date…" for past days. An outfits-worn count and a streak ("days in a row") are tracked. — [App Store version history 6.2–6.4](https://apps.apple.com/us/app/id1140550878); [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps)
- A packing question can be asked through the chat ("what should I pack?"). There is no dedicated packing-list feature in the current documentation. — [App Store listing](https://apps.apple.com/us/app/id1140550878)

**Analytics**
- "Your closet at a glance" shows item count, % of closet worn in 90 days (83% in the screenshot), average cost per wear ($24), "1 item not worn in 90 days — Review it", most-worn items with counts, and a color bar comparing what you own with what you wear. — [App Store screenshots](https://apps.apple.com/us/app/id1140550878)
- In the 2026 report, about 42% of capsules logged on 10+ dates had the same saved outfit logged on more than one date. — [cladwell.com/capsule-wardrobe-report](https://cladwell.com/capsule-wardrobe-report)

**AI features**
- The stylist is available on every screen and is context-aware: it "already has the context: today's outfit, your shopping list, the look you're viewing". It can take actions (add to shopping list, save or wear a look, see it on you; "Nothing changes until you tap"). It accepts photos ("does this go with my green skirt?") and shows real products at your brands and budget. — [v6.1 notes](https://apps.apple.com/us/app/id1140550878)
- "See it on me" puts an outfit from your closet onto a photo of you. — [App Store listing](https://apps.apple.com/us/app/id1140550878)
- AI usage is metered. "A monthly AI usage screen shows how much of your allowance you've used." Images cost more than chat, the allowance resets on the billing date, and one-time extra-AI packs are in testing. — [App Store listing](https://apps.apple.com/us/app/id1140550878); [cladwell.com/pricing](https://cladwell.com/pricing); [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps)
- Model providers are OpenAI, plus OpenRouter-routed providers including Azure and Google Vertex AI. — [cladwell.com/privacy-policy](https://cladwell.com/privacy-policy)
- In ChatGPT, Cladwell lets users add clothes on free or paid accounts, while subscribers can expose their closet, capsules, Style Map, colors and wear history to ChatGPT. Try-on there uses ChatGPT's own image tools. — [cladwell.com/chatgpt](https://cladwell.com/chatgpt)

**Social**
- No social feed or community is documented. — [cladwell.com/outfit-apps](https://cladwell.com/outfit-apps) (Cladwell itself points users who want social styling to Whering)

**Commerce**
- Each missing capsule piece becomes a shopping-list item with "3 to 5 real products from the brands you choose, within your budget". The list shows a "Links found <date>" stamp and a Refresh option. Users can tap "View" to go to the store, mark "I bought it", or say "I have something like this". — [App Store listing and screenshots](https://apps.apple.com/us/app/id1140550878)
- The privacy label lists "Data Used to Track You: Purchases, Identifiers" and third-party advertising use of purchases and identifiers. This suggests attribution and affiliate tracking, but no commission model is documented. — [App Store privacy label](https://apps.apple.com/us/app/id1140550878)

**Pricing and gating**
- Free: build the closet, organise everything, track what you wear. Paid "Your Stylist": discover your style, find your colors, daily outfits, the stylist chat, planned purchases, and see outfits on yourself. Web pricing is $9.99/mo or $79.99/yr with no trial on the web. — [cladwell.com/pricing](https://cladwell.com/pricing)
- The US IAP list includes many legacy SKUs: $9.99, $7.99 monthly, $7.99 weekly, $21.99 quarterly, $59.99 annual, $79.99 annual, and a $2.99 monthly. — [App Store listing](https://apps.apple.com/us/app/id1140550878)
- "Your closet, capsules, and history are always yours" whether or not you subscribe. — [cladwell.com/pricing](https://cladwell.com/pricing)

**Privacy**
- The App Store label is unusually broad. Data used to track: Purchases, Identifiers. Data linked to the user: Purchases, Location, Contact Info, User Content, Identifiers, Usage Data, Diagnostics. — [App Store privacy label](https://apps.apple.com/us/app/id1140550878)
- Cladwell does not use photos or content for training. Users can request export or deletion via hello@cladwell.com, and accounts can be deleted in the app. Color-analysis face geometry is kept until the photo or account is deleted, under a published retention schedule. — [cladwell.com/privacy-policy](https://cladwell.com/privacy-policy)

**Timeline (2023 → Oct 2026)**
- 5.6.0 (Jun 2023): Ask Cladwell (ChatGPT).
- 5.7.0 (Aug 2023): save outfits and item lists from chats.
- 5.8.0 (Aug 2023): 13 languages.
- 5.9–5.10 (2024): bug fixes; pinch-zoom and auto-save (Dec 2024).
- 5.11.0 (Apr 2025): AI bulk photo add.
- 5.12.0 (May 2025): change category/type of an item.
- 5.12.3–5.12.4 (Feb–Mar 2026): iOS 26 fixes.
- 5.13.0 (3 Oct 2026): relaunch (quiz, color season, See it on me, Ask tab, menswear, annual plan with trial).
- 5.14 (6 Oct 2026): try-on in the outfit editor.
- 6.0 (20 Oct 2026): Outfits tab, season capsule, shopping list, closet care, AI usage screen.
- 6.1: stylist memory, actions and photos.
- 6.2: Lists back.
- 6.3: recommended outfits lead each day; 12-piece builder; ChatGPT connect; iPad.
- 6.4 (24 Oct 2026): "Log on another date…".
- Source for all entries: [App Store version history](https://apps.apple.com/us/app/id1140550878)

**UI**
- The visual style is a warm off-white/cream background, serif display headlines ("Fresh outfits from your closet"), terracotta eyebrow labels, black pill CTAs ("Wear today", "I bought it") and flat product imagery. — [App Store screenshots](https://apps.apple.com/us/app/id1140550878)
- The website names five tabs: Outfits, Closet, Style, Chat, Colors. — [cladwell.com/app](https://cladwell.com/app)
- The bottom bar in the screenshots shows five icons: home, hanger, closet rack, shopping bag, and a pie chart. — [App Store screenshots](https://apps.apple.com/us/app/id1140550878)

### Inferences
- Cladwell's moat is the capsule abstraction combined with "outfits unlocked per purchase" gap math. The 2026 rebuild layers LLM generation over that structure rather than replacing it. Its seasonal ~33-piece capsule is the closest thing in the category to a published, opinionated recommendation framework.
- The free tier no longer generates outfits, so free users get a catalog app. Weekly and quarterly SKUs suggest aggressive paywall testing.
- Shipping six releases in three weeks right before the research date suggests a re-platforming push. Stability and retention of the new AI features are unproven.
- Best for people who want to be told what to wear from what they own, especially capsule and minimalist users, and men (menswear support). It is weak for people who want creative collage, social, or deep manual organisation.

### Gaps
- The ranking and scoring logic for outfits (how weather, palette and history are weighted) is not published.
- The exact paywall placement in the new onboarding (before or after quiz results) is not documented.
- The AI allowance size per plan is not public.
- Founder history is inconsistent: the primary site names only Erin Flynn, and a secondary site lists five co-founders and a 2019 buyout.

---

## 2. Smart Closet (Rabbit Tech Inc): what it is, how it works, every mechanic

### Takeaway
Smart Closet is a 2017-era, manual-first closet and lookbook app. It has a ShopStyle-powered product catalog, a web "clipper" browser, a free-form look canvas, a rule-based random-look generator, a calendar, packing lists and a deep statistics list. It is in maintenance mode: a 35-month gap with no updates (Dec 2022 → Nov 2025), then sign-in fixes only. In the US it is now a paid-upfront $2.99 app, with an optional backup/sync subscription.

### Cited Findings

**Identity and status**
- The seller is Rabbit Tech Inc (website smartcloset.me). It was released 26 Jan 2017. The current version is 3.8.3 (10 Nov 2025). It has 4,345 US ratings (avg 4.41), costs $2.99 upfront, is English only and is rated 17+. — [iTunes Lookup](https://itunes.apple.com/lookup?id=1198057728&country=us)
- On Android it has 1M+ installs, was last updated 1 Dec 2025, and is free there. — [Google Play](https://play.google.com/store/apps/details?id=com.rkk.closet)
- Release cadence: 3.7.1 (11 Dec 2022) was followed by 3.8.1 (7 Nov 2025). The 2025 releases only fix Apple, Google and Facebook sign-in, improve backup and download options, and add "multiple closets with different accounts on the same device — available for every user". — [App Store version history](https://apps.apple.com/us/app/id1198057728)
- A competitor's review says "it looks like the app developers don't actively maintain the app" and describes it as a one-time fee with no free plan, with cloud backup extra. — [fits-app.com review](https://fits-app.com/posts/top-8-closet-outfit-planning-apps-reviewed) (competitor-authored; undated)

**Item capture**
- Users can "Select clothing from thousands of brands/retailers" (the Android listing says "powered by ShopStyle"), add from photos or the camera, and "Auto-remove background… by one-click". — [App Store](https://apps.apple.com/us/app/id1198057728); [Google Play](https://play.google.com/store/apps/details?id=com.rkk.closet)
- "Clip From Everywhere": an in-app browser (a Zara page in the screenshot) with a "Clip Images" button. v3.7 (Dec 2022) added "search & import clothing images from any websites". — [App Store screenshots and history](https://apps.apple.com/us/app/id1198057728)
- The manual crop editor has Rotate, Zoom, Crop, Erase, Fill and Auto tools, plus a "Transparent Background Color" slider. — [App Store screenshots](https://apps.apple.com/us/app/id1198057728)

**Data model and organisation**
- Item fields: category, color, brand, price, season "and more". Users can customise their own categories and occasions. — [App Store](https://apps.apple.com/us/app/id1198057728); [Google Play](https://play.google.com/store/apps/details?id=com.rkk.closet)
- The Closet screen has category bubbles (Add Item, All, Bags, Shoes, Dresses…), search and a 3-column grid. Re-ordering items and looks was added in v3.3.2 (Jan 2021). — [App Store screenshots and history](https://apps.apple.com/us/app/id1198057728)
- Multiple closets on one device are supported (Nov 2025). — [App Store version history](https://apps.apple.com/us/app/id1198057728)

**Outfit building**
- "Create your looks by combining your clothes freely." The canvas toolbar has Remove, Flip and Clone, and a closet search grid sits below the canvas. — [App Store](https://apps.apple.com/us/app/id1198057728)
- "Get lots of random looks by your custom rules." This is a rule-based randomiser, not AI. — [App Store](https://apps.apple.com/us/app/id1198057728)
- Looks are organised in a Lookbook, and the stats list includes "Looks by Collection". — [App Store screenshots](https://apps.apple.com/us/app/id1198057728)

**Planning**
- "Plan what to wear in your calendar. Get daily notification if need." Closet items and looks can be filtered inside the Calendar (v3.7.1). — [App Store](https://apps.apple.com/us/app/id1198057728)
- "Pack your clothing & looks for travel." — [App Store](https://apps.apple.com/us/app/id1198057728)

**Analytics**
- The Statistics screen lists: Item Count, Total Value, Items by Category, Favorite Colors, Favorite Brands, Items by Season, Look Count, Looks by Collection, Looks by Season, Look Value, Most Used Item, Item Unused, Calendar Records and Most Worn Look. The listing also promises "find the best price/wear clothes". — [App Store screenshots](https://apps.apple.com/us/app/id1198057728); [Google Play](https://play.google.com/store/apps/details?id=com.rkk.closet)

**Commerce and social**
- A "Shop Thousands of Brands" tab ("Search & Shop Anywhere") shows sale prices and a Favorite tab. The website shows Women/Men/Designers/Trending shop sections plus Closet and WishList. — [App Store screenshots](https://apps.apple.com/us/app/id1198057728); [smartcloset.me](https://smartcloset.me/)
- Social is limited to sharing a look to Facebook, Instagram and similar. — [Google Play](https://play.google.com/store/apps/details?id=com.rkk.closet)

**Pricing and gating**
- $2.99 upfront on the US App Store. "SmartCloset Pro" ($0.99/mo, or $9.99/yr per the description) unlocks only "auto backup & sync on multiple devices". The IAP list shows "Auto Backup & Sync" at $3.99 as well as $0.99 monthly. — [App Store](https://apps.apple.com/us/app/id1198057728)

**Privacy**
- Label: Data Linked = User Content, Identifiers; Not Linked = Usage Data, Diagnostics; no tracking. — [App Store privacy label](https://apps.apple.com/us/app/id1198057728)
- Data is stored on Google Cloud, AWS and MongoDB, and there is an account-deletion page. — [smartcloset.me privacy policy](https://smartcloset.me/about/privacy-policy)

**UI**
- White background, black uppercase headers ("CLOSET"), product-photo grid, and a 5-tab bar: Home, Closet, Lookbook, Favorite, More. — [App Store screenshots](https://apps.apple.com/us/app/id1198057728)

### Inferences
- The 17+ age rating most likely comes from the unrestricted in-app web browser used for clipping.
- Moving from free to paid upfront on iOS while doing almost no development is a "harvest" strategy. The app is a reference for a solid non-AI manual feature set (rules-based randomiser, clipper, rich stats list), not a moving competitor.
- Best for users who want a cheap, offline-ish, no-subscription manual organiser. Weak on AI, social and modern UI.

### Gaps
- The date Smart Closet switched to paid-upfront in the US was not found (third-party aggregators show $2.99 in 2026).
- The exact rule options of the random-look generator are undocumented.
- Whether ShopStyle affiliate links still function is not verifiable without installing.

---

## 3. Pureple (Iceclip LLC): what it is, how it works, every mechanic

### Takeaway
Pureple is a 2013-vintage manual outfit maker. In 2025–26 it was rebuilt around AI: "Style Me" outfit suggestions, AI outfit feedback, virtual try-on on you or a model, OpenAI-vision auto-tagging, and "Whole Closet Scan" multi-item detection (Pro, US rollout Oct 2026). It also has a light community: shared closets that friends can style, and direct messages. Items and manual outfits are free and unlimited. AI, calendar, packing lists and statistics require a costly Pro plan ($6.99/wk, $14.99/mo, $89.99/yr). Its App Store privacy label ("Data Not Collected") contradicts its own privacy policy.

### Cited Findings

**Identity and status**
- The seller is ICECLIP LLC (Rancho Santa Margarita, California). It was released 5 Apr 2013. The current version is 6.0.29 (23 Oct 2026). It has 6,119 US ratings (avg 3.94), 26 language codes and is rated 4+. — [iTunes Lookup](https://itunes.apple.com/lookup?id=628106373&country=us); [pureple.com](https://pureple.com/)
- Android: 1M+ installs. — [Google Play](https://play.google.com/store/apps/details?id=com.app.pureple)
- User claims conflict: "Trusted by 3M+ people across 100+ countries" on the App Store versus "4M+ downloads… rather than a count of unique or monthly active users" on the website. — [App Store](https://apps.apple.com/us/app/id628106373); [pureple.com/about](https://pureple.com/about)
- Press history: Good Morning America 2019; E! Online 2017; Safiya Nygaard's 2017 "I Let An App Pick My Outfits For A Week" video (11M+ views); listed as an existing competitor in TechCrunch's 29 Apr 2026 Google Photos closet story. — [pureple.com/about](https://pureple.com/about); [TechCrunch 2026](https://techcrunch.com/2026/04/29/google-photos-uses-ai-to-make-the-iconic-closet-from-clueless-a-reality/)
- Support runs through a Discord community and email. — [pureple.com/support](https://pureple.com/support)
- Status: active, with roughly monthly bug-fix builds through 2025–26 and a large UI refresh on 21–23 Oct 2026. — [App Store version history](https://apps.apple.com/us/app/id628106373)

**Item capture**
- The Add clothes sheet offers Photo Library, Camera, "Add from web" ("Save clothes from a website", with an updated web-import guide) and "Whole Closet Scan" ("Add several items from a closet photo"). — [App Store screenshots](https://apps.apple.com/us/app/id628106373); [v6.0.25 notes](https://apps.apple.com/us/app/id628106373)
- Whole Closet Scan (Pro, iOS) lets the user choose a closet photo, review the detected pieces, correct names, categories and colors, and save. "Scans start with descriptive thumbnails; you can add individual clothing photos later." It is a US rollout, and scan history and photo replacement are included. — [pureple.com/faq](https://pureple.com/faq); [v6.0.25 notes](https://apps.apple.com/us/app/id628106373)
- Photo analysis (v6.0.26): the user reviews identified categories and colors "including any warnings" before saving. A fine category (T-shirt) can be added alongside a broad one (Tops). The update improved handling of unclear or no-clothing photos. — [App Store version history](https://apps.apple.com/us/app/id628106373)
- Uploaded photos are sent to the OpenAI Vision API to identify "category, color, pattern, material, brand, season, and occasion". — [Pureple privacy policy](https://pureple.com/Privacy.aspx)
- Background erase is unlimited for Premium; the free limit is not stated. — [App Store](https://apps.apple.com/us/app/id628106373)

**Data model and organisation**
- Users can add clothes, shoes, accessories, beauty items, hairstyles and wishlists. Filters cover season, occasion, color, brand, size, price "and more". Custom categories (Oct 2026), custom season tags, lookbooks and batch-edit are also available. — [App Store](https://apps.apple.com/us/app/id628106373); [v6.0.25/6.0.29 notes](https://apps.apple.com/us/app/id628106373)
- The Closet screen is a white, photo-focused list grouped by category with counts (e.g. Bags 2, Dresses 2, Jackets 1), with collapsible sections, "+ Add" and a filter button. — [App Store screenshots](https://apps.apple.com/us/app/id628106373)

**Outfit building**
- The outfit editor is a free canvas (rotate, remove; "3 pieces · tap to edit") with a near-full-screen picker that scrolls vertically and has category chips (All, Bags, Dresses, Jackets…). Selections are check-marked and saved with "Save outfit". — [App Store screenshots](https://apps.apple.com/us/app/id628106373); [v6.0.27 notes](https://apps.apple.com/us/app/id628106373)
- The Outfits tab has a "My outfits / Instant Outfit" toggle (Instant Outfit is a generator). Occasion filter chips (All 6, Night 1, Weekend 5), a sort ("Oldest") and a primary "Style Me" button complete the screen. — [App Store screenshots](https://apps.apple.com/us/app/id628106373)
- "Style Me" (Pro) suggests outfits from the closet. On Android, "Style this item" builds a look around a chosen piece. — [pureple.com/faq](https://pureple.com/faq); [Google Play](https://play.google.com/store/apps/details?id=com.app.pureple)
- Recommendation inputs sent to OpenAI: "wardrobe item descriptions, location, weather data, and language preference". The chat gets up to the 20 most recent messages plus a wardrobe summary and city/country. — [Pureple privacy policy](https://pureple.com/Privacy.aspx)
- Outfits can be filtered by weather, season and custom season tags (restored in 6.0.29). Outfit images export on a clean white background. — [App Store version history](https://apps.apple.com/us/app/id628106373)

**Planning and analytics (Pro)**
- Calendar and packing lists: "Pureple Pro on iOS includes packing lists… plus an outfit calendar". On Android the calendar also requires Pro. — [pureple.com/faq](https://pureple.com/faq); [Google Play](https://play.google.com/store/apps/details?id=com.app.pureple)
- Stats (Pro, iOS) cover most and least worn items, cost per wear, closet value and value by category. They are computed from calendar entries and entered prices (e.g. a "$42 piece worn 12 times" gives $3.50). — [pureple.com/faq](https://pureple.com/faq); [pureple.com](https://pureple.com/)

**AI features**
- Virtual try-on lets users see an outfit set or collage "on you" or "on a virtual model". The result can be saved to a Lookbook, the Calendar or a Packing List ("not a sizing tool"). It first appeared in v6.0.6 (3 Sep 2025) and uses Google Gemini for image generation. — [App Store](https://apps.apple.com/us/app/id628106373); [Pureple privacy policy](https://pureple.com/Privacy.aspx)
- "Instant Outfit Feedback by AI": snap or build a look and get tips, e.g. "Try a cropped jacket to balance the wide-leg shape". — [App Store](https://apps.apple.com/us/app/id628106373); [pureple.com](https://pureple.com/)
- Explicit consent is requested before any data is sent to an AI provider. — [Pureple privacy policy](https://pureple.com/Privacy.aspx)

**Social**
- "Community styling" lets friends and other Pureple members create outfits from your shared wardrobe. The feature is opt-in. — [pureple.com/faq](https://pureple.com/faq)
- Direct messages, notifications, unread indicators, and follow and block actions on profiles were added or overhauled in 6.0.25. — [App Store version history](https://apps.apple.com/us/app/id628106373)

**Pricing and gating**
- Free: unlimited items and manual outfits, with ads. Pro: Weekly $6.99, Monthly $14.99, Yearly $89.99 (7-day trial). Pro covers Whole Closet Scan, AI suggestions, try-on, feedback, calendar and packing, unlimited background erase, no ads, and multi-device sync with cloud backup. Free users back up via iCloud/iTunes. — [App Store](https://apps.apple.com/us/app/id628106373)
- Legacy IAPs still listed: "Pro Version" $14.99 (one-time), "AI Style Advisor+" $39.99, "Annual Storage Subscription" $9.99, and several Pureple Subscription price points ($34.99–$62.99). — [App Store IAP list](https://apps.apple.com/us/app/id628106373)

**Privacy**
- The App Store label says "Data Not Collected". — [App Store privacy label](https://apps.apple.com/us/app/id628106373)
- The privacy policy says Pureple collects email, display name, credentials and AI chat messages; sends images, wardrobe data and location to OpenAI and images to Google Gemini; and stores account, wardrobe and images in Firebase (Firestore, Storage) with Firebase analytics and crash reporting. — [Pureple privacy policy](https://pureple.com/Privacy.aspx)

**Timeline**
- 6.0.4–6.0.5 (May–Jun 2025): fixes.
- 6.0.6 (3 Sep 2025): virtual try-on.
- Oct 2025 – Jul 2026: roughly monthly fixes.
- 6.0.25 (21 Oct 2026): redesign, messages, Whole Closet Scan (US Pro), custom categories.
- 6.0.26–6.0.29 (22–23 Oct 2026): photo-analysis review, outfit gallery and filters.
- Pre-May-2025 history is not visible in the store's version list.
- Source: [App Store version history](https://apps.apple.com/us/app/id628106373)

**UI**
- White, photo-forward layouts; violet (#7B61FF-like) primary buttons and selection states; rounded chips.
- Tab bar: Wardrobe, Outfits, AI (sparkle icon), Community, Profile.
- Source: [App Store screenshots](https://apps.apple.com/us/app/id628106373)

### Inferences
- The "Data Not Collected" label next to a policy describing Firebase analytics and OpenAI transfers is a material compliance risk and a credibility gap.
- The 3.94 average (lowest in this set) alongside a weekly $6.99 plan suggests aggressive monetisation of a large legacy install base.
- Best for long-time Pureple users and people who want unlimited free manual collage plus optional AI. Weak value for people who need the calendar or stats, since both are paid.

### Gaps
- Founders and team are undocumented. I found no named founder for Iceclip LLC.
- Free-tier limits on background erase and AI trial counts are not stated.
- Onboarding and paywall placement are undocumented.
- Pre-2025 feature history (e.g. when "Instant Outfit" launched) was not found.

---

## 4. Combyne: social-first mechanics in full

### Takeaway
Combyne is the largest app in this set: 10M+ Android installs, 87.7K US iOS ratings at 4.76, and Gen-Z skewed. It is a Polyvore-style collage game and social network, not a closet utility. Users drag items from a giant shared library of user-uploaded and brand items onto a canvas (the "Combyner") with wallpapers and hairstyles. They post to a feed (For You / Following / Recent), compete in user-created challenges, follow, comment, chat and join groups. Engagement currencies (Superlikes, and from 2026 "Fire") monetise visibility.

After a ~20-month update gap (Sep 2023 → May 2025), the app was revived. Its operator and data controller is now SIA URSTYLE (Riga, Latvia), although the App Store seller is still "combyne GmbH". The 2025–26 additions are AI try-on, AI background removal, a Fire wallet with daily check-ins, sponsored or "ad items" importable into collages, and age verification.

### Cited Findings

**Identity, ownership and status**
- The App Store seller is combyne GmbH. It was released 4 Feb 2016. The current version is 8.10.4 (2 Oct 2026). It has 87,738 US ratings (avg 4.76), supports EN/DE/ES, needs iOS 17.6+, is rated 4+, and has no iPad screenshots. — [iTunes Lookup](https://itunes.apple.com/lookup?id=989727742&country=us)
- Android: 10M+ installs (~19.9M on the Play counter), last updated 6 Oct 2026. — [Google Play](https://play.google.com/store/apps/details?id=com.combyne.app)
- Origins: Munich. Founded by Philipp Seybold. A 2016 interview names the founders Philipp (strategy, product, marketing), Christian (operations, finance) and Pung (tech lead and UX). The 2016 business model was merchants paying a subscription for an in-app "mobile shop", "not sales or click commissions". — [FashionUnited 2016](https://fashionunited.de/nachrichten/mode/fashiontech-berlin-x-rpten-die-zukunft-der-mode/2016050620107); [Munich Startup, 24 Mar 2016](https://www.munich-startup.de/news/wir-revolutionieren-die-statische-produktdarstellung-7-fragen-an-combyne)
- Current operator: the imprint, the withdrawal address in the terms, and the GDPR controller are all "SIA URSTYLE… registered… in Latvia… 40203629523, Elizabetes iela 63–2, Rīga", "legally represented by Mr Aleksey Nikulin". The terms are dated "Status August 2025". — [combyne.com/imprint](https://www.combyne.com/imprint); [combyne.com/privacy-policy-en](https://www.combyne.com/privacy-policy-en); [combyne.com/terms-of-usage](https://www.combyne.com/terms-of-usage)
- Release gap: 8.5.8 (14 Sep 2023) was followed by 8.5.9 (21 May 2025), about 20 months with no updates. Development has been active since then (8.6.x → 8.10.4). — [App Store version history](https://apps.apple.com/us/app/id989727742)
- A 2026 competitor review says Combyne "seems to not be that actively maintained… the developers behind it have moved on to another project". — [fits-app.com](https://www.fits-app.com/posts/top-10-polyvore-alternatives-the-ultimate-review) (competitor-authored, possibly written during the gap)
- Funding figures from aggregators conflict ($1.1M, $6.7M, $9.1M), so none is reliable. — [vcbacked.co](https://www.vcbacked.co/company/combyne); [craft.co](https://craft.co/combyne); [seedtable](https://seedtable.com/startups/combyne-XKKZNWW)

**Onboarding and account**
- Registration is mandatory, by email or Apple, Facebook, Google or Twitter. The service is for users aged 16+, and under-18s need parental consent. A web app gives limited access. — [combyne.com/terms-of-usage](https://www.combyne.com/terms-of-usage)
- Date-of-birth verification at signup and a backend age-validation flow were added in v8.6.14 and 8.6.16 (Jun 2026). — [App Store version history](https://apps.apple.com/us/app/id989727742)

**The Combyner (outfit and collage canvas)**
- The toolbar reads "Canvas Combyner" with buttons: My Outfits, Save, Wallpapers, Items, Next (orange). The canvas shows free-placed cut-out items. — [App Store screenshots](https://apps.apple.com/us/app/id989727742)
- The item browser has tabs Categories / Favorites / Collections / Uploaded, a "Browse Items" search, chips (Popular, Upper, Bottoms…) and a row of color swatches. Each item tile credits an "Item owner" (the uploading user). Hairstyles are items in the library. — [App Store screenshots](https://apps.apple.com/us/app/id989727742)
- Listing: "over 35 categories"; items from "over 800 brands and online shops" (stale marketing copy). — [App Store](https://apps.apple.com/us/app/id989727742)
- The web outfit page lists "Items in this creation", each with an "Item Creator" credit. This shows a shared, user-generated item library reused across creators. — [app.combyne.com example](https://app.combyne.com/outfits/B7OVhMbf4BrA)
- A 2020 user review described the "Create" tab as swapping each piece by scrolling a wardrobe, plus the ability to "browse the community's wardrobe" and upload your own photos. — [Daily Campus, Apr 2020](https://dailycampus.com/2020/04/08/2020-04-08-create-outfits-like-cher-from-clueless-with-this-app/)
- A competitor describes two creation modes: swiping clothes, or arranging on a collage. — [fits-app.com](https://www.fits-app.com/posts/top-10-polyvore-alternatives-the-ultimate-review)
- 2026: free automatic AI background removal (8.7.0). Earlier, v8.6.9 had "Premium pricing support for AI cutouts". — [App Store version history](https://apps.apple.com/us/app/id989727742)

**Wardrobe and collections**
- "Save your favourite items and outfits, name them by event or style." Saved lists are called "Collections" in the terms, e.g. an "Everyday" collection of outfit collages. — [App Store](https://apps.apple.com/us/app/id989727742); [terms](https://www.combyne.com/terms-of-usage); [screenshots](https://apps.apple.com/us/app/id989727742)

**Feed, discovery and social graph**
- The home feed has For You / Following / Recent tabs, with a follow-suggestions icon, search and notifications. — [App Store screenshots](https://apps.apple.com/us/app/id989727742)
- Users follow friends, bloggers and style icons, and can comment, like and repost. Direct messages exist ("chat feature"). — [App Store](https://apps.apple.com/us/app/id989727742); [Daily Campus](https://dailycampus.com/2020/04/08/2020-04-08-create-outfits-like-cher-from-clueless-with-this-app/)
- "Get matched and find your fashion soulmate, who thinks and dresses just like you" (style-similarity matching). — [App Store](https://apps.apple.com/us/app/id989727742)
- Groups and chats are free features. — [combyne.com/subscription](https://www.combyne.com/subscription)
- The web app navigation shows Outfits, Shop (BETA) and Spotlight. — [app.combyne.com](https://app.combyne.com/outfits/B7OVhMbf4BrA)

**Challenges**
- "Our users create Outfit Challenge every day. Create your look following the rules of the challenge, rate the others' looks, get the most likes." The Android listing says users "Create your own challenge, set the rules and the topic". — [App Store](https://apps.apple.com/us/app/id989727742); [Google Play](https://play.google.com/store/apps/details?id=com.combyne.app)
- The Challenges screen has search, Popular / Recent tabs, refresh and "+" (create). Cards show the challenge title, host and rule text (e.g. "Pink and White by Marie Svensson — Be creative my friends"). — [App Store screenshots](https://apps.apple.com/us/app/id989727742)
- Free users can join challenges with "weekly limits". Premium gives "Unlimited challenge creation". — [combyne.com/subscription](https://www.combyne.com/subscription)

**Engagement currencies**
- Superlikes (Aug 2023): multiple Superlikes per post, a "Superlikes Leaderboard" showing who gave them, and IAP packs to "boost your own or others' outfits for increased visibility". Packs are 10/$0.99, 30/$1.99, 50/$2.99 and 100/$4.99. A Superlikes counter in the feed header followed in Jun 2026. — [App Store version history and IAP](https://apps.apple.com/us/app/id989727742)
- Fire (2026): "Show some fire — Reward your favorite creators… unlock exclusive experiences". "Earn free Fire with daily check-ins and partner visits" (8.8.1, Jul 2026). A "Fire wallet" with paid packages appeared in 8.10.4 (Oct 2026). Premium includes "30 Fire every month". — [combyne.com](https://www.combyne.com/); [App Store version history](https://apps.apple.com/us/app/id989727742); [combyne.com/subscription](https://www.combyne.com/subscription)

**AI features**
- An AI try-on exists. It appears only as "Multiple AI try-on and signup bug fixes" in v8.6.14 (Jun 2026). Premium gives "Discounts on AI features", which implies AI is paid per use. — [App Store version history](https://apps.apple.com/us/app/id989727742); [combyne.com/subscription](https://www.combyne.com/subscription)

**Commerce and ads**
- Items link to partner shops ("With one click, we will forward you to your favorite item in the online shop"). The partner list in the listing (About You, Farfetch, Mango, Topshop…) is stale. — [App Store](https://apps.apple.com/us/app/id989727742)
- The free tier shows ads (banners, full-screen and sponsor logos), and ad blockers are prohibited. — [combyne.com/terms-of-usage](https://www.combyne.com/terms-of-usage)
- 2026 ad marketplace: "ad marketplace flow" and "sponsored/ad item links" (8.6.15); "Ad Item Import… import your favorite Ad items directly into your collages" (8.7.0); "impression tracking for sponsored content" (8.10.4). — [App Store version history](https://apps.apple.com/us/app/id989727742)

**Pricing and gating**
- Free: create outfits and collages, build a wardrobe, share, groups and chats, challenges (weekly limits).
- Premium: enhanced feed visibility, 30 Fire per month, exclusive groups and profile perks, unlimited challenge creation, ad-free, all premium filters, AI discounts.
- Premium IAP: $2.99 (monthly) and $19.99 (annual) in the US.
- Sources: [combyne.com/subscription](https://www.combyne.com/subscription); [App Store IAP](https://apps.apple.com/us/app/id989727742)

**Privacy**
- Label: Data used to track = Usage Data. Data linked = Contact Info, User Content, Identifiers, Usage Data. Not linked = Location, Contacts, Identifiers, Usage Data, Diagnostics. — [App Store privacy label](https://apps.apple.com/us/app/id989727742)
- Hosting is AWS (US and Ireland), MongoDB Atlas, Rubylight Analytics (Cyprus), Productboard, Intercom and Mailchimp. Data is deleted when the account is deleted. — [combyne.com/privacy-policy-en](https://www.combyne.com/privacy-policy-en)
- The content licence is worldwide and sub-licensable for promoting Combyne, and is "unlimited in time" for collages. — [terms](https://www.combyne.com/terms-of-usage)

**Timeline**
- Aug 2023: Superlikes (multiple per post, leaderboard, IAP).
- Sep 2023 – May 2025: no releases.
- May–Jun 2025: iOS 18 support.
- Sep 2025 – Mar 2026: UI fixes.
- May 2026: AI background removal (Premium-priced).
- Jun 2026: DOB verification, ad marketplace, free AI cut-outs, ad-item import.
- Jul 2026: Fire rewards and daily check-ins.
- Oct 2026: Fire wallet.
- Source: [App Store version history](https://apps.apple.com/us/app/id989727742)

**UI**
- White feed with editorial collage posts; orange accent (Next button, selected chips, Categories icon); black canvas header. Screenshots are dated (pre-notch iPhones in some panes) and are framed as "Discover / Dress / Personalize / Plan / Compete". — [App Store screenshots](https://apps.apple.com/us/app/id989727742)

### Inferences
- Combyne's mechanics map onto a creator economy: a shared UGC item library with attribution, challenges as prompts, likes/Superlikes/Fire as status and visibility, and premium sold as reach ("enhanced feed visibility").
- The 2025–26 roadmap is monetisation-heavy (currencies, ad items, sponsored impressions) rather than closet utility.
- The SIA URSTYLE controller strongly suggests an acquisition or transfer around 2025 that ended the update gap. I found no announcement, so this is an inference.
- Best for teens and Gen Z who treat outfit-making as creative play and social status. Weak as a personal-closet utility: no calendar, wear tracking or stats are documented.

### Gaps
- No announcement or date of the URSTYLE takeover was found.
- Fire pack prices, Fire spending options and what "exclusive experiences" are remain undocumented.
- The AI try-on pricing and model are undocumented.
- The current "fashion soulmate" matching logic is undocumented.
- The help centre (help.combyne.com) now redirects to the terms page, so no feature documentation is available.

---

## 5. Fits (L. & J. Henne UG): what it is, how it works, every mechanic

### Takeaway
Fits launched in May 2023. A small German team ships weekly, and it has become the most feature-dense app in this set:
- generous free tier (unlimited items, outfits and background removal);
- AI auto-tagging (category, color, season, visible brand logo);
- four outfit modes: collage canvas, swipe "Dressing Room" on a mannequin, AI try-on, and AI stylist chat;
- planner calendar with selfie logging;
- collections (wishlist, packing, custom);
- email-receipt and photo-library bulk import;
- an Instagram-like community with OOTD stories, polls and "style your friend";
- an official MCP server and ChatGPT/Claude connectors.

Pro ($9.99/mo; $59.99/yr SKU) unlocks unlimited AI chat, 30 AI credits/month and advanced stats. Credit packs are sold separately.

### Cited Findings

**Identity and status**
- The seller is L. & J. Henne UG (haftungsbeschränkt), Neuffenweg 5, 73269 Hochdorf, Germany. — [fits-app.com/imprint](https://www.fits-app.com/imprint)
- The jobs page says "Hey, I'm Ludwig. I built Fits" and lists a Berlin on-site role, citing "2M+ downloads". — [fits-app.com/jobs](https://www.fits-app.com/jobs)
- iOS: released 15 May 2023; current version 2.90.0 (6 Oct 2026); 5,683 US ratings (avg 4.59); rated 12+; 24 languages; iPad supported. — [iTunes Lookup](https://itunes.apple.com/lookup?id=6447482321&country=us)
- Android: 500K+ installs (~957K). — [Google Play](https://play.google.com/store/apps/details?id=de.lhenne.fits)
- There is no web app, though the iPad app runs on Mac. — [fits-app.com/knowledge/where-can-i-use-fits](https://www.fits-app.com/knowledge/where-can-i-use-fits)
- Self-reported scale conflicts:
  - "Trusted by 2M users" (App Store screenshot);
  - "over 1 million users" (community page);
  - "3m+ people… more than 50m clothes… more than 20k App Store reviews" (blog);
  - the blog also cites a Sensor Tower estimate of about 300K downloads/month in Sep 2026.
  — [App Store screenshots](https://apps.apple.com/us/app/id6447482321); [fits-app.com/community](https://www.fits-app.com/community); [fits-app.com blog](https://www.fits-app.com/posts/most-downloaded-closet-apps-in-2026)
- Status: very active, with 25 releases between 11 May and 6 Oct 2026. — [App Store version history](https://apps.apple.com/us/app/id6447482321)

**Onboarding**
- During onboarding, users can add common basic pieces with one tap ("More basic pieces in the onboarding", v2.70). A "getting started checklist for new users" was added in v2.82 (Aug 2026). — [fits-app.com/knowledge/add-your-first-clothing-item](https://www.fits-app.com/knowledge/add-your-first-clothing-item); [App Store version history](https://apps.apple.com/us/app/id6447482321)
- The profile asks for gender (not public; "influences… the AI stylist's decisions") plus Instagram and TikTok handles, and has a private-account toggle. — [fits-app.com/knowledge/set-up-your-profile](https://www.fits-app.com/knowledge/set-up-your-profile)
- Paywall: an "Upgrade button at the top of the home screen" and the settings screen lead to a checkout offering annual or monthly plans, with "a 7-days free trial (one per user)". — [fits-app.com/knowledge/how-can-i-upgrade-to-fits-pro](https://www.fits-app.com/knowledge/how-can-i-upgrade-to-fits-pro)

**Item capture**
- Routes:
  - search the web and the Fits community ("Levis jeans", "Adidas Samba");
  - photo library;
  - camera;
  - a built-in shop browser that detects product images;
  - email-receipt forwarding to a personal Fits address, which detects items, URL, price and purchase date (deselect returned items, then Import);
  - bulk "extract clothes from your outfit photos" (v2.69, May 2026);
  - "scan your photo library for outfit photos to extract clothes" (v2.71, Jun 2026).
  — [fits-app.com/knowledge/add-clothing-items](https://www.fits-app.com/knowledge/add-clothing-items); [import-clothes-from-email-receipts](https://www.fits-app.com/knowledge/import-clothes-from-email-receipts); [App Store version history](https://apps.apple.com/us/app/id6447482321)
- Background removal is automatic and free with no limit. A cutout editor adjusts the cutout, brightness, saturation and crop. AI image enhancement turns photos into "product pack shots", and receipt imports are upscaled for free. — [fits-app.com/knowledge/free-vs-paid-features](https://www.fits-app.com/knowledge/free-vs-paid-features); [v2.69 notes](https://apps.apple.com/us/app/id6447482321)
- The help centre recommends adding at most about 20 items at a time. — [add-clothing-items](https://www.fits-app.com/knowledge/add-clothing-items)

**Item data model**
- AI auto-detects category, color, season and brand (when a logo is visible). Users can edit name, category, brand (10,000+ predefined brands plus custom ones), color, season, purchase date, price and a private note. Pro adds extra images (e.g. the backside) and links ("Add links" in the screenshot). Custom tags have a color and emoji. — [add-clothing-items](https://www.fits-app.com/knowledge/add-clothing-items); [add-custom-brands-and-tags](https://www.fits-app.com/knowledge/add-custom-brands-and-tags); [App Store screenshots](https://apps.apple.com/us/app/id6447482321)
- Currency is configurable and drives cost per wear and wardrobe value. — [change-the-currency](https://www.fits-app.com/knowledge/change-the-currency-in-your-wardrobe)

**Organisation**
- Filters: tags, colors, categories, seasons, brands, name, and usage (used vs not used in outfits). Group-by: none, tags, categories, season or colors. Sort: custom drag-and-drop, date added, categories, colors, most/least worn, highest/lowest price, and highest/lowest cost per wear. — [filter-and-group](https://www.fits-app.com/knowledge/filter-and-group-items-in-your-wardrobe); [reorder-and-sort](https://www.fits-app.com/knowledge/reorder-and-sort-items)
- Collections come in three types: Wishlists (not in the main wardrobe; "mark as purchased" moves an item in), Packing lists (check off as packed) and Custom (e.g. a second home or kids' clothes). Collection-only items can be hidden from the main wardrobe (v2.86). — [organize-items-in-collections](https://www.fits-app.com/knowledge/organize-items-in-collections); [App Store version history](https://apps.apple.com/us/app/id6447482321)
- Archive is free, and multi-select archiving was added in v2.87. — [free-vs-paid](https://www.fits-app.com/knowledge/free-vs-paid-features); [v2.87](https://apps.apple.com/us/app/id6447482321)
- Tag limits conflict: the help centre says free users get 5 custom tags, but v2.77 (Jul 2026) says "Free users now get unlimited custom tags, all colors and hair styles". — [add-custom-brands-and-tags](https://www.fits-app.com/knowledge/add-custom-brands-and-tags); [App Store version history](https://apps.apple.com/us/app/id6447482321)

**Outfit building (four modes)**
- Collage: pick clothes, tap Next, then reposition, rotate and scale. Backgrounds can be colors, gradients, or custom background images (v2.89). Stickers include hairstyles, graphics, symbols and arrows, and an item's extra images can be placed on the canvas. — [create-outfit-collages](https://www.fits-app.com/knowledge/create-outfit-collages); [v2.89](https://apps.apple.com/us/app/id6447482321)
- Dressing Room: a mannequin with 3 default layers (tops, bottoms, footwear). Tap a layer, swipe sideways to cycle items, use a resize handle to fit the body, then save. The avatar's hairstyle, hair color and body type (female/male) can be customised or hidden. — [how-to-make-outfits](https://www.fits-app.com/knowledge/how-to-make-outfits-from-your-clothes); [personalize-your-dressing-room](https://www.fits-app.com/knowledge/personalize-your-dressing-room)
- Users can switch between collage, dressing room and AI try-on for the same outfit (v2.80). — [App Store version history](https://apps.apple.com/us/app/id6447482321)
- AI outfit suggestions need at least 5 items across categories. The user taps "Make an outfit" and describes the occasion. Outfits arrive in the chat, open in the editor, and follow-ups such as "replace the shoes" work. — [get-outfit-suggestions-from-ai](https://www.fits-app.com/knowledge/get-outfit-suggestions-from-ai)
- Selectable stylist personas (e.g. "Lizzy") have taste attributes such as minimal or edgy that change suggestions and ratings. — [pick-a-styling-persona](https://www.fits-app.com/knowledge/pick-a-styling-persona); [App Store screenshots](https://apps.apple.com/us/app/id6447482321)
- v2.77 (Jul 2026) added a home screen with "outfit ideas every day" and a "find your outfit" feature. — [App Store version history](https://apps.apple.com/us/app/id6447482321)

**Planning**
- The planner has daily and monthly views, supports multiple outfits per day, and lets users schedule from the outfit save screen. Users can log outfit selfies (with optional background removal and canvas editing), which appear on today's planner. — [plan-outfits-ahead](https://www.fits-app.com/knowledge/plan-outfits-ahead); [track-outfit-selfies](https://www.fits-app.com/knowledge/track-outfit-selfies)
- A daily reminder at a user-chosen time prompts users to pick or track an outfit (v2.81). — [App Store version history](https://apps.apple.com/us/app/id6447482321)
- Packing lists can be manual or made by AI: the chat asks for dates, location and activities and saves the list to Collections. — [make-a-packing-list-with-ai](https://www.fits-app.com/knowledge/make-a-packing-list-with-ai)

**Analytics**
- Stats cover:
  - pie-chart breakdowns;
  - purchases by year;
  - total value;
  - average price;
  - % of wardrobe used;
  - most and least worn;
  - highest and lowest cost per wear;
  - recently worn.
  Basic stats are free; advanced stats are Pro. — [get-wardrobe-insights](https://www.fits-app.com/knowledge/get-wardrobe-insights); [free-vs-paid](https://www.fits-app.com/knowledge/free-vs-paid-features)

**AI features**
- The AI stylist chat can:
  - suggest outfits;
  - rate an outfit from a selfie or a Fits outfit, with tips;
  - make packing lists;
  - find similar owned items from a photo;
  - identify wardrobe gaps ("Find wardrobe gaps" shortcut);
  - run color analysis from a selfie plus body type, with season and temperature;
  - suggest products (v2.88).
  Chat history arrived in v2.76. Free chat has a daily message limit; Pro is unlimited. — [chat-with-ai-stylist](https://www.fits-app.com/knowledge/chat-with-ai-stylist); [let-ai-rate-your-outfit](https://www.fits-app.com/knowledge/let-ai-rate-your-outfit); [find-gaps](https://www.fits-app.com/knowledge/find-gaps-in-your-wardrobe); [find-your-personal-color-palette](https://www.fits-app.com/knowledge/find-your-personal-color-palette); [App Store version history](https://apps.apple.com/us/app/id6447482321)
- AI try-on: selfie plus body type, then pick clothes from the wardrobe or a wishlist. Each generation costs 1 credit and takes up to about 40 seconds. Try-on backgrounds were added in v2.67, and the screenshot shows "44 AI credits", Regenerate and Canvas. — [how-to-try-on-clothes-virtually-with-ai](https://www.fits-app.com/knowledge/how-to-try-on-clothes-virtually-with-ai); [App Store screenshots](https://apps.apple.com/us/app/id6447482321)
- The color-analysis landing page claims an AI "trained by hundreds of thousands of color matches" that can generate a try-on in the palette colors. — [fits-app.com/fashion-color-analysis](https://www.fits-app.com/fashion-color-analysis)
- MCP and connectors: the MCP server at `https://mcp.fits-app.com/mcp` uses Streamable HTTP with OAuth and QR-code confirmation in the app.
  - Read tools: get_profile, list_pieces, find_pieces, get_piece, show_pieces, list_outfits, get_outfit, get_calendar, list_lists, show_outfit.
  - Write tools: save_outfit, create_list, add_to_list, remember.
  - There is an official ChatGPT plugin and a Claude connector.
  — [fits-app.com/mcp-documentation](https://www.fits-app.com/mcp-documentation); [connect-fits-to-chatgpt-or-claude-mcp](https://www.fits-app.com/knowledge/connect-fits-to-chatgpt-or-claude-mcp)
- Providers include the OpenAI API, Google Cloud/Firebase and Algolia. Try-on photos are stored privately and sent to AI providers only on use, are "not used to train their models", and are removed on account deletion. — [fits-app.com/privacy-policy-app](https://www.fits-app.com/privacy-policy-app)

**Social**
- Profiles are public by default, with an Instagram-style private mode. Users can post OOTD stories, browse "over 1 million wardrobes", and view popular items and outfits. — [private-wardrobe](https://www.fits-app.com/knowledge/private-wardrobe); [fits-app.com/community](https://www.fits-app.com/community)
- "Style @username": build an outfit from a friend's wardrobe, and the friend gets notified. Private profiles require following first. — [make-outfits-for-friends](https://www.fits-app.com/knowledge/make-outfits-for-friends)
- 2026 additions:
  - like comments and @mention users (2.80);
  - "Wardrobe Add Counts", which show how many people added your piece, with profile attribution (2.81);
  - a Following tab (2.83);
  - "This or That" polls in OOTD stories (2.86);
  - a profile bio (2.87);
  - a wardrobe discovery tab (2.88).
  — [App Store version history](https://apps.apple.com/us/app/id6447482321)

**Pricing and gating**
- Free: unlimited clothing and outfits, unlimited background removal, AI detection, basic stats, collections, OOTD, planner, selfies, archive.
- Pro: unlimited AI chat and suggestions, 30 AI credits/month, unlimited tags, extra item images, advanced stats, all stickers, hairstyles, colors and patterns.
- Sources: [free-vs-paid](https://www.fits-app.com/knowledge/free-vs-paid-features); [what-is-included-in-fits-pro](https://www.fits-app.com/knowledge/what-is-included-in-fits-pro)
- US IAPs: Fits Pro $9.99 / $29.99 / $59.99; credits 5/$1.99, 20/$4.99, 100/$19.99, 500/$79.99; "Fits Pro + Credits" $9.99 and $29.99. — [App Store IAP](https://apps.apple.com/us/app/id6447482321)

**Privacy**
- Label: tracking = Usage Data. Linked = Purchases, Contact Info, User Content, Identifiers, Usage Data. — [App Store privacy label](https://apps.apple.com/us/app/id6447482321)

**Timeline**
- Visible history (May–Oct 2026):
  - 2.66: Liquid Glass design.
  - 2.67: try-on backgrounds.
  - 2.69: outfit-photo extraction and receipt upscaling.
  - 2.71: photo-library scan.
  - 2.75: planner UI.
  - 2.76: chat history.
  - 2.77: daily home ideas.
  - 2.80: comments and mentions.
  - 2.81: add-counts and reminders.
  - 2.82: checklist.
  - 2.83: following tab.
  - 2.86: polls and collection visibility.
  - 2.87: bio and archive multi-select.
  - 2.88: discovery and product suggestions.
  - 2.89: custom backgrounds.
  - 2.90: fixes.
  — [App Store version history](https://apps.apple.com/us/app/id6447482321)
- 2023 – Apr 2026 history is not visible in the store.

**UI**
- Soft blush/peach gradient marketing frames with a serif display face. The app UI is white, with black pill buttons and a central black "+" FAB.
- Top segmented tabs: Clothes / Outfits / Collections. Bottom nav variants: Home / + / Profile, and Wardrobe / + / Calendar.
- Canvas bottom bar: Add clothes / Stickers / Background.
- Source: [App Store screenshots](https://apps.apple.com/us/app/id6447482321)

### Inferences
- Fits has the strongest "utility + social" blend in the set. The UGC item library with add-counts and attribution (the Polyvore DNA) feeds both catalog-based item capture and the community.
- Its MCP and agent tooling is the most developed of any app here.
- Weekly cadence and a free tier with no item caps make it a fast follower that is likely to keep compressing differentiation from Whering and Acloset.
- Best for Gen-Z and millennial users who want a pretty, free, fast closet with creative and social modes. Weaker for power analytics (stats are limited per competitor reviews) and for web or desktop use.

### Gaps
- Founder's full name is unconfirmed (first name "Ludwig" only; the company name suggests the Henne family).
- Team size, funding and pre-2026 release history were not found.
- Exact current Pro prices per term (which SKU is monthly vs annual) are not stated outside the IAP list.

---

## 6. OpenWardrobe: what it is, how it works, every mechanic

### Takeaway
OpenWardrobe (Redwood City, CA, founded 2021) is a sustainability-framed wardrobe OS. Its distinctive mechanics are:
- multiple "Wardrobes" (season, trip, home, family) with add-vs-move semantics;
- four object types (Items, Outfits, Looks = selfies or inspiration, plus Wardrobes);
- the deepest value analytics in the set (Investment, Utilization, Wears; resale value and value retention; "True Cost per Wear");
- granular sharing roles ("Style Buddy" read-only, "Style Guru" read-write);
- the LolaAI stylist plus a Chrome "shopping copilot" extension covering 700+ stores;
- after-life services: US alterations and repairs with free shipping, and one-click Poshmark listing.

It monetises through the "Circle" membership ($12.99/mo, $79/yr, $199 lifetime) and a $49 one-time "Style Blueprint". Its scale is small: 171 US ratings and 100K+ Android installs.

### Cited Findings

**Identity and status**
- Seller: OpenWardrobe, Inc. Released 18 Aug 2021. Current version 5.2.0 (17 Sep 2026). 171 US ratings (avg 4.02), English only. — [iTunes Lookup](https://itunes.apple.com/lookup?id=1549029468&country=us)
- Android: 100K+ installs. — [Google Play](https://play.google.com/store/apps/details?id=com.open_wardrobe_mobile)
- Team: Julia K. Dietmar (co-founder and CEO), Julius Dietmar (co-founder and CTO). Address: 570 El Camino Real, Redwood City, CA. — [openwardrobe.co/our-story](https://www.openwardrobe.co/our-story)
- Julia Dietmar previously led products at Yahoo, worked on e-commerce at Walmart and was CPO at Vue.ai. She says 93% of uploads are users' own photos, and that OpenWardrobe has no brand or retail relationships yet (Oct 2025). — [Ambitious Altruist podcast, 15 Oct 2025](https://ambitiousaltruist.buzzsprout.com/2098451/episodes/18018853-16-julia-dietmar)
- Platforms: iOS, Android and web. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq)
- Status: active. v5.0.0 (Mar 2026) was a platform rebuild ("faster loading, smoother navigation"), and v5.2.0 (Sep 2026) enabled location for weather. — [App Store version history](https://apps.apple.com/us/app/id1549029468)

**Item capture and data model**
- Items can come from uploaded photos, a "common items catalog", or imported online purchases via a Chrome extension ("import orders"; Gap and Zara importers were fixed in 4.4.1). The FAQ suggests starting with your "10 most worn items". — [openwardrobe.co/faq](https://www.openwardrobe.co/faq); [App Store version history](https://apps.apple.com/us/app/id1549029468); [Chrome Web Store](https://chromewebstore.google.com/detail/openwardrobe/jeibidfaelmiiioohfplflcpnelgipei)
- AI auto-detects "category, colors, and any other visible attributes". Users enter price, purchase date and condition to unlock cost per wear, resale value and utilization. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq)
- Item detail (screenshot): a photo carousel; a "Times worn" −/+ stepper; Last worn; Cost per wear; Resale value; and tabs Details / Outfits / Wardrobes. — [App Store screenshots](https://apps.apple.com/us/app/id1549029468)
- The app tracks the share of second-hand items in the wardrobe, per a user testimonial on the site. — [openwardrobe.co](https://www.openwardrobe.co/)

**Organisation**
- Top tabs: Wardrobes / Items / Outfits / Looks. Wardrobe cards have cover photos and item counts; examples in the screenshots are trips ("Lisbon/Vienna/Budapest – December'25", "Norway", "Argentina") and "Main Wardrobe 258 items". Toolbar: search, stats, settings, Reorder, +. — [App Store screenshots](https://apps.apple.com/us/app/id1549029468)
- "Move to wardrobe" removes an item from its original wardrobe. "Add to wardrobe" keeps it in both without duplicating or counting it twice, which suits packing lists and travel capsules. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq)
- Items screen: a category icon row (All, Activewear, Bags, Belts, Blazers, Cardigans…), Filters, Multi-select, a shuffle (randomiser) button and a grid-density toggle. Bulk edit and shuffle arrived in 4.6.6. — [App Store screenshots](https://apps.apple.com/us/app/id1549029468); [version history](https://apps.apple.com/us/app/id1549029468)
- Free plan: up to 500 items. Circle: unlimited. Outfits and looks are unlimited for everyone. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq)

**Outfits, looks and planning**
- An outfit is a combination of items. A look is "a styled visual idea that is either a selfie or an inspiration". Outfits can be shown in grids or collages. Bulk outfit editing can add, remove or replace an item across selected outfits and edit title, seasons and occasions. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq); [App Store](https://apps.apple.com/us/app/id1549029468)
- The calendar takes outfits or individual items. Looks are auto-placed by photo timestamp. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq)
- Outfit detail: Times worn, Planned, Last worn, and calendar, favourite and share icons. — [App Store screenshots](https://apps.apple.com/us/app/id1549029468)

**Analytics**
- "Wardrobe Statistics" is per wardrobe, with tabs Composition / Investment / Utilization / Wears. The Investment tab in the screenshot shows total purchase value $26,106 (258 items), average item value $204, total resale value $16,415.5, value retention 62.9%, and a spend-by-category donut (18 categories). — [App Store screenshots](https://apps.apple.com/us/app/id1549029468)
- Two cost-per-wear measures: "Worn Cost per Wear" (worn items only) and "True Cost per Wear", which "includes unworn items". — [openwardrobe.co/faq](https://www.openwardrobe.co/faq)
- Per-item and wardrobe resale value arrived in v4.4.0 (Mar 2025). — [App Store version history](https://apps.apple.com/us/app/id1549029468)

**AI features**
- LolaAI suggests outfits from wardrobe items, "suggestions that you previously saved or discarded, and your LolaAI settings". It helps think through purchases. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq)
- "Ask LolaAI Before You Buy": in the app and the Chrome extension, it shows how a candidate item styles with your wardrobe and "works with over 700 stores". — [openwardrobe.co](https://www.openwardrobe.co/)
- Style Blueprint is a 5–10 minute quiz plus photos. The result arrives in about 24 hours from "AI + stylist review" and includes:
  - a 30-colour palette (a tonal system with 6 dominant types, giving 30 of 42 colours from photos);
  - makeup colours;
  - a silhouette map;
  - a style-personality profile with 6–8 tiered brands;
  - workshops and masterclasses;
  - unlimited recalibration.
  Price: $49 one-time, which includes $20 toward alterations and $20 toward makeup. The page claims "Trusted by 150,000+ users". — [openwardrobe.app/style-blueprint/women](https://www.openwardrobe.app/style-blueprint/women); [openwardrobe.co/faq](https://www.openwardrobe.co/faq)
- "The Style Lab" (Dec 2024, premium) offers trend and color reports and workshops. — [App Store version history](https://apps.apple.com/us/app/id1549029468)

**Social and sharing**
- Wardrobes are private by default. Per friend and per wardrobe, the owner can choose what is visible (items, outfits, looks, statistics, calendars) and assign "Style Buddy" (read-only) or "Style Guru" (can create wardrobes, add items, suggest outfits). Making a wardrobe "Open to All" lists it in the community's "Open Wardrobes" section. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq)

**Commerce and services**
- Alterations and repairs (US): choose a service, add instructions, ship with free two-way shipping, and a vetted partner does the work. Launched in v4.3.0 (Feb 2025). — [openwardrobe.co/faq](https://www.openwardrobe.co/faq); [App Store version history](https://apps.apple.com/us/app/id1549029468)
- Poshmark resale (US and Canada, v4.2.0, Jan 2025): OpenWardrobe "pre-creat[es] your listing", and the user reviews it and lists it to their own Poshmark account in one click. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq); [App Store version history](https://apps.apple.com/us/app/id1549029468)

**Pricing and gating**
- Circle IAPs: Monthly $12.99, Yearly $79.00, Lifetime $199.00. — [App Store IAP](https://apps.apple.com/us/app/id1549029468)
- The Circle benefit list is described only vaguely ("advanced tools, deeper insights, AI-powered support"). — [openwardrobe.co/faq](https://www.openwardrobe.co/faq)

**Privacy**
- Label: Linked = Contact Info, User Content, Identifiers. Not linked = Location, Usage, Diagnostics. No tracking. — [App Store privacy label](https://apps.apple.com/us/app/id1549029468)
- Account deletion is in-app ("Delete Account" in Profile). Data review and deletion requests go to support@myopenwardrobe.com. — [openwardrobe.co/faq](https://www.openwardrobe.co/faq); [privacy policy](https://www.openwardrobe.co/company/privacy-policy)

**Timeline**
- 3.5.4 (Jul 2024): enhanced calendar.
- 4.0.2 (Oct 2024): redesign, search, sort, more attributes.
- 4.0.4: cost-per-wear fixes.
- 4.1.0 (Dec 2024): Style Lab, colors and patterns in stats.
- 4.2.0 (Jan 2025): Poshmark.
- 4.3.0 (Feb 2025): Alterations.
- 4.4.0 (Mar 2025): resale value.
- 4.6.0 (Sep 2025): Style Blueprint, Circle, new-experience beta.
- 4.6.6 (Nov 2025): shuffle, bulk edit.
- 5.0.0 (Mar 2026): platform rebuild.
- 5.2.0 (Sep 2026): weather.
- Source: [App Store version history](https://apps.apple.com/us/app/id1549029468)

**UI**
- Cream background, sage-green accents and buttons, a classic serif-free UI and photo-card wardrobes. Several screenshots show "TestFlight" in the status bar. — [App Store screenshots](https://apps.apple.com/us/app/id1549029468)

### Inferences
- OpenWardrobe has the most complete "lifecycle" model in the set: acquire (browser copilot), use (wear, CPW), value (resale and retention), maintain (alterations), exit (Poshmark).
- The multi-wardrobe plus add-vs-move semantics is a clean solution to packing and capsule modelling that others approximate with tags or collections.
- Low ratings volume and English-only support limit reach.
- Best for analytically minded, sustainability-motivated adults, often older users per testimonials, and for people who share closets with a stylist.

### Gaps
- The full Circle feature list and the free limit on LolaAI usage are not documented.
- The partner identity for alterations is not named.
- Whether Poshmark integration uses an API or a deep link is undocumented.
- Onboarding and paywall placement are undocumented.

---

## 7. Save Your Wardrobe (SYW): consumer app, B2B model, and the 2026 SYW-AI spin-out

### Takeaway
Save Your Wardrobe (London; founded 2017 by Hasna Kourda, CEO, and Mehdi Doghri, CTO) began as a free consumer "digital wardrobe + aftercare marketplace" app. It has pivoted to being primarily a B2B "aftersales platform". Brands and retailers (LVMH, H&M France, Bicester Village, Kiabi, Klarna) use its booking engine, atelier management, AI damage detection, no-code process builder and Digital Product Passport (DPP) integration to offer repair, care and alteration services.

The consumer app (no IAPs) keeps a free wardrobe/outfit/calendar core, adds booking of local repair, alteration, cleaning and upcycling services, and since mid-2026 creates Digital Service Passports stored in a Digital Wallet. In Sep 2026 SYW launched a separate consumer app, "SYW-AI". It is an AI agent that turns a photo and receipt of a faulty purchase into a claim, drafts and manages the email exchange with the brand, and tracks resolution.

### Cited Findings

**Identity and status**
- Seller: Save your wardrobe Ltd. Released 3 Mar 2020. Current version 3.0.7 (5 Oct 2026). 281 US ratings (avg 4.51). 11 languages. Price free with no IAPs listed. — [iTunes Lookup](https://itunes.apple.com/lookup?id=1485757044&country=us); [App Store](https://apps.apple.com/us/app/id1485757044)
- Android: 100K+ installs. — [Google Play](https://play.google.com/store/apps/details?id=com.saveyourwardrobe.syw)
- Co-founded in 2017 by Hasna Kourda (CEO) and Mehdi Doghri (CTO). Offices in the UK, France and Tunisia. — [saveyourwardrobe.com/about](https://www.saveyourwardrobe.com/about)
- Registered in England and Wales, company no. 10831652. — [Klarna press release](https://www.saveyourwardrobe.com/en-gb/blog/save-your-wardrobe-powers-klarnas-care-and-repair-feature)
- Apple editorial features listed in the app description include App of the Day, "Sustainability apps we love" and "Hottest apps in the UK". — [App Store](https://apps.apple.com/us/app/id1485757044)

**B2B model**
- Positioning: "the global standard for end-to-end aftersales solutions… repairs, care, alterations, warranties, logistics, exchanges, compliance, customer experience, and data reporting". — [saveyourwardrobe.com](https://www.saveyourwardrobe.com/)
- Components:
  - a dynamic booking engine (web, app, in-store);
  - an "Atelier Management Solution";
  - an admin dashboard with live data;
  - an AI Damage Detection Tool ("upload a photo, instantly verify product defects, validate warranty claims");
  - a no-code drag-and-drop business process designer;
  - DPP partnerships ("One Click Repairs" with pre-filled data);
  - ERP, warranty and loyalty integrations via API.
  — [saveyourwardrobe.com](https://www.saveyourwardrobe.com/); [about](https://www.saveyourwardrobe.com/about)
- It is industry-agnostic (fashion, electronics, jewellery, furniture) and "Regulation Ready" (France's Repairability Index, the EU DPP). — [saveyourwardrobe.com/products](https://www.saveyourwardrobe.com/products)
- Named clients and quotes: Bernard Arnault (LVMH), Thomas Estrany (H&M France), Bicester Village. Logos include Hugo Boss, Loro Piana, Maje, SMCP, Zalando, Sandro and Farfetch (inferred from logo files). Implementation can take as little as 3 weeks; one rollout covered 80 stores in France. — [saveyourwardrobe.com](https://www.saveyourwardrobe.com/); [saveyourwardrobe.com/faq](https://www.saveyourwardrobe.com/faq); [products (WebFetch)](https://www.saveyourwardrobe.com/products)
- Klarna (29 Sep 2026): SYW powers Klarna's "Care & Repair" in the UK and France. Eligible purchases in the Klarna app link to repair, alteration and cleaning, then hand off to SYW to book and pay. SYW reports a 248% increase in repair bookings from 2024 to 2025, and customers spending an average of 63% less than the item's estimated retail value. — [SYW press release](https://www.saveyourwardrobe.com/en-gb/blog/save-your-wardrobe-powers-klarnas-care-and-repair-feature)
- Kiabi (19 Oct 2026): a guided repair solution on Kiabi.com and in the Kiabi app, covering 200+ brands and integrating France's "Bonus Réparation". — [SYW news](https://www.saveyourwardrobe.com/en-gb/blog/save-your-wardrobe-powers-kiabis-repair-and-care-service)

**Consumer app: capture, data and organisation**
- Photograph garments, remove the background, and pick from a set of background colours. AI scans and identifies garments. Items can be labelled and put in folders. — [App Store](https://apps.apple.com/us/app/id1485757044)
- A 2021 review says the app tags "brand, category and colour". — [SANVT, Aug 2021](https://sanvt.com/blogs/journal/best-sustainable-fashion-apps)
- Organisation features, by release:
  - filters by folders, patterns and condition, plus multi-item add/edit (2.4.0, Jan 2025);
  - drag-and-drop custom sort for wardrobe and wishlist (2.3.0);
  - "Smart Folder filters" (mentioned in 3.0.2);
  - Archive to hide items or outfits without deleting (2.6.0, Nov 2025);
  - custom "moods" (2.5.2).
  — [App Store version history](https://apps.apple.com/us/app/id1485757044)
- Wardrobe tab: Wardrobe / Folders tabs, search and an "Add New Item" button. — [App Store screenshots](https://apps.apple.com/us/app/id1485757044)

**Outfits, planning and analytics**
- An outfit collage feature lets users schedule outfits in a calendar. Bulk actions create outfit lists (2.2.0). Items, outfits and looks can be viewed full screen. — [App Store](https://apps.apple.com/us/app/id1485757044); [version history](https://apps.apple.com/us/app/id1485757044)
- Home: "Your Wardrobe, Decoded" cards (Wardrobe Brands, e.g. "Ganni 5% of your items" with a brand rating; Wardrobe Colours, "Most common colour family: Black") and "Outfit For Today" with weather. — [App Store screenshots](https://apps.apple.com/us/app/id1485757044)
- Insights cover most-worn brands, colours and items, and impact "via their Good on You rating". — [App Store](https://apps.apple.com/us/app/id1485757044)
- Siri, Spotlight and Widgets were added in 2.0 (Sep 2024). — [App Store version history](https://apps.apple.com/us/app/id1485757044)

**Consumer app: services and passports**
- Booking flow: "Book Your Services Here — select a city and enter a postal code", then "What type of service do you need?" (e.g. Alteration: tailoring, re-sizing and adjustments). Categories are cleaning, repairs, alterations, upcycling and customisation. — [App Store screenshots and description](https://apps.apple.com/us/app/id1485757044)
- In 2021 the services also included donation, rental and responsible shopping. — [SANVT](https://sanvt.com/blogs/journal/best-sustainable-fashion-apps)
- Digital Service/Product Passports (3.00–3.0.4, Jun–Jul 2026): when booking a service, accepting a passport links it to the item and stores it in the Digital Wallet. The passport holds the full service history and a "scannable proof of ownership" that can be checked from another phone. — [App Store version history](https://apps.apple.com/us/app/id1485757044)

**Onboarding**
- Guest mode lets users explore without an account (2.5.0, Mar 2025). Users can start with limited access before verifying email (2.7.0, Feb 2026). — [App Store version history](https://apps.apple.com/us/app/id1485757044)

**Privacy**
- Label: Linked = Identifiers, Usage Data, Diagnostics. No tracking. — [App Store privacy label](https://apps.apple.com/us/app/id1485757044)
- The policy names AWS, Google Cloud Storage and mLab. — [SYW privacy policy](https://www.saveyourwardrobe.com/legal/privacy-policy)

**SYW-AI (new app)**
- Released 8 Sep 2026. Current version 1.3.0 (22 Oct 2026). Requires iOS 26. Shopping genre. — [iTunes Lookup](https://itunes.apple.com/lookup?id=6785557121&country=us)
- Flow:
  1. "Start with a photo, add your receipt and answer a few quick questions."
  2. The app identifies the item (industry, product type, colour) and the damage ("DAMAGED" tag).
  3. Receipt OCR follows.
  4. The user chooses an outcome: Refund, Repair, Replacement, Store credit or Other.
  5. The AI drafts a complaint email with tone controls (Balanced / Firmer / Shorter).
  6. Users can track the claim and file a new one. The app later analyses the brand's response ("highlights the key details, recommended actions and next steps") and follows up.
  The claims dashboard shows Active and Resolved claims with statuses ("Claim unfinished", "Waiting for reply") and notes such as "We found the brand's complaints address". — [App Store listing and screenshots](https://apps.apple.com/us/app/id6785557121)
- "Your photos are analysed on your device and only shared when you choose to submit your claim." It integrates with Siri and Visual Intelligence (1.2). — [App Store screenshots and history](https://apps.apple.com/us/app/id6785557121)

**UI**
- The consumer app uses a purple accent, serif "SAVE YOUR WARDROBE" wordmark, and tabs Home / Wardrobe / Services / Outfits. SYW-AI uses a cyan-gradient, "Liquid Glass" style with an indigo CTA. — [App Store screenshots](https://apps.apple.com/us/app/id1485757044); [SYW-AI screenshots](https://apps.apple.com/us/app/id6785557121)

### Inferences
- The consumer wardrobe app now works mainly as a demand and showcase channel for the B2B aftersales network. Most consumer releases since mid-2026 are bug fixes, apart from passports, which mirror the B2B DPP strategy.
- SYW-AI repackages the B2B "AI damage detection + claim workflow" for consumers, from the opposite side of the table. This is a novel adjacent mechanic, care and claims, that no other wardrobe app has.
- Best for UK/France users who want repair and care services and sustainability scoring. Weak on outfit intelligence: no AI outfit generation is documented.

### Gaps
- Consumer-side service pricing, take rate and which cities are covered are undocumented in public pages.
- Whether Good On You ratings are still shown in 2026 is not verified.
- The SYW-AI business model (free, commission, or brand-paid) is undocumented.
- How many brands accept SYW-AI claims is unknown.

---

## 8. GetWardrobe (Outfit Makers LLC): what it is, how it works, every mechanic

### Takeaway
GetWardrobe is a long-lived, utilitarian, privacy-positioned wardrobe system. It is the only app here with full iOS + Android + macOS + web apps and real-time sync. Distinctive mechanics:
- three generation modes: "AI Outfit Generator" batches of 10/20/30 by model tier, "AI Studio" one-at-a-time with style, mood, occasion, weather and color controls, and "Explore Combinations" pairing swipes;
- a weather-aware planner with clothing essentials (e.g. SPF/UV);
- family wardrobes (per-member profiles);
- trip packing lists with destination forecast and bag weight;
- a Sizes Notebook;
- AND/OR advanced search;
- a paid "Stylist Mode" B2B tier ($690/yr) that gives up to 10 clients Premium.

Free is capped at 100 items. AI credits are pay-as-you-go.

### Cited Findings

**Identity and status**
- Seller: Outfit Makers LLC. Released 10 Aug 2013. Current version 2026.09.1 (12 Oct 2026). 789 US ratings (avg 4.30). 41 language codes. — [iTunes Lookup](https://itunes.apple.com/lookup?id=656212466&country=us)
- Android package com.ThreeBoots: 1M+ installs (~2.47M). — [Google Play](https://play.google.com/store/apps/details?id=com.ThreeBoots)
- A separate macOS app exists, and the web app is at app.getwardrobe.com. — [getwardrobe.com](https://getwardrobe.com/)
- Founding claims conflict: "trusted by 3 million users since 2013" (App Store), "Trusted since 2008", "Founded in 2008" (website). The website footer reads "© 2008–2026 Outfit Maker LLC". — [App Store](https://apps.apple.com/us/app/id656212466); [getwardrobe.com/about](https://getwardrobe.com/about/)
- "An independent, privately owned American company. No investors". Scale claims: 3M+ wardrobes, 39+ countries, 17M+ items cataloged. — [getwardrobe.com/about](https://getwardrobe.com/about/)
- Status: active, with monthly versioned releases (YYYY.MM.n). — [App Store version history](https://apps.apple.com/us/app/id656212466)

**Item capture**
- Photo or gallery upload, with AI that "removes the background, detects the category and dominant colors, and suggests a name". "Premium AI background removal" is available. — [getwardrobe.com](https://getwardrobe.com/); [pricing](https://getwardrobe.com/pricing/)
- Paste a shopping link (brand, price, link and image auto-detected), or add from a screenshot of an online item (2025.04.1). "Store import" saves brand, price and link. — [App Store version history](https://apps.apple.com/us/app/id656212466); [App Store](https://apps.apple.com/us/app/id656212466)
- AI image enhancer for "catalog-ready photos" with auto-crop (2025.10.1). — [App Store version history](https://apps.apple.com/us/app/id656212466)

**Data model and organisation**
- Sort and filter by category, brand, color, season, weather, tags and more. AND/OR advanced filters. Statuses shown on the photo (e.g. "Reserved"/"Packed" overlays in the packing screenshot). A Sizes Notebook records "how each brand fits you". Capsules. Archive (Premium). Bulk edit (Premium). Universal offline search across items, outfits and packing lists (2026.01.1). — [App Store](https://apps.apple.com/us/app/id656212466); [version history](https://apps.apple.com/us/app/id656212466); [App Store screenshots](https://apps.apple.com/us/app/id656212466)
- Clothes screen: category tabs (All, Tops, Layers, Bottoms, Dresses), tag chips with counts (Dress 6, Sunglasses 5, Top 4, Evening 2) and a dense grid. — [App Store screenshots](https://apps.apple.com/us/app/id656212466)

**Outfit building**
- Outfit editor: drag, layer and resize on a canvas for "magazine-style collages". Premium adds backgrounds, photos and text overlays. 2026.01.1 brought a redesigned editor with "virtual avatars". — [App Store](https://apps.apple.com/us/app/id656212466); [pricing](https://getwardrobe.com/pricing/); [version history](https://apps.apple.com/us/app/id656212466)
- AI Outfit Generator:
  - Starter model: 10 outfits per run, free and unlimited, filtered by weather, occasion and mood.
  - Standard (20) and Pro (30) cost credits.
  - Run history is kept to "revisit and refine past results" (Feb 2026).
  - Small wardrobes are handled by "compos[ing] what it can and suggest[ing] categories to add" (Aug 2026).
  - Outfits can be rated thumbs up or down (Oct 2026).
  — [App Store](https://apps.apple.com/us/app/id656212466); [App Store version history](https://apps.apple.com/us/app/id656212466)
- AI Studio (Oct 2026): set "the style, mood or occasion… take the weather into account, and adjust your color preferences". Free and unlimited. — [App Store version history](https://apps.apple.com/us/app/id656212466)
- Explore Combinations: "Swipe through item pairings and save the ones you love". — [App Store](https://apps.apple.com/us/app/id656212466)
- The suggested-outfit screen has a "Filters applied" pill, history and layers icons, and regenerate / Save / bookmark controls. — [App Store screenshots](https://apps.apple.com/us/app/id656212466)
- GetWardrobe claims an analysis of 17M+ items in which "73% of items never make it into an outfit" and tops outnumber bottoms 2 to 1. — [getwardrobe.com](https://getwardrobe.com/)

**Planning**
- Outfit calendar with local weather. Plan tab: family-member avatar switcher; a Wardrobe / Plan / Shopping segment; a day strip; a weather card (city, temperature, Now / Morning / Day / Evening); and suggested essentials (sunscreen when UV is high). Individual items can be scheduled, not just outfits (2026.03.1). Long-press the date to jump to the calendar (2026.05.1). — [App Store screenshots](https://apps.apple.com/us/app/id656212466); [version history](https://apps.apple.com/us/app/id656212466)
- Packing lists (Premium): set trip location and dates, pull the forecast for those days, sort by trip purpose, destination weather and weight, view a category summary, and check off items ("Packed 5/30"). — [getwardrobe.com](https://getwardrobe.com/); [App Store](https://apps.apple.com/us/app/id656212466); [App Store screenshots](https://apps.apple.com/us/app/id656212466)
- Family wardrobes (Premium): "Add profiles for your kids, partner, or anyone you dress… Closet, outfit suggestions, and calendar are filtered per member." — [getwardrobe.com](https://getwardrobe.com/)

**Analytics**
- Style statistics (free) cover most worn, unused items and estimated value. Advanced (Premium) adds cost per wear, most-used combinations, wardrobe value and utilization. A sortable per-item table with custom dates, sort by CPW or %-worn, and breakdowns by colour, category, season or tag came in 2025.07.1. CPW per item arrived in 2026.06.3. — [App Store](https://apps.apple.com/us/app/id656212466); [version history](https://apps.apple.com/us/app/id656212466)

**AI features**
- Virtual try-on (Nov 2025) with try-on history (Feb 2026); unwanted try-ons can be hidden (Jul 2026). — [App Store version history](https://apps.apple.com/us/app/id656212466)
- AI content may be processed by "third-party AI services". Processed content is "typically deleted within 30 days unless saved". There is "Anonymized AI Training Data" use with an opt-out request. — [getwardrobe.com/privacy-policy](https://getwardrobe.com/privacy-policy)

**Social and B2B**
- Community feed and outfit sharing ("Publish your OOTD lookbook to social media or the GetWardrobe community"). — [App Store](https://apps.apple.com/us/app/id656212466)
- Stylist access (Premium): give a stylist or assistant read or edit access, revocable. — [getwardrobe.com](https://getwardrobe.com/)
- Stylist Mode (Pro):
  - a branded client dashboard for up to 10 clients ("talk to us" for more);
  - branded invitations, or creating accounts on clients' behalf;
  - switching into a client's account to add items, build outfits, capsules and packing lists;
  - running the AI generator on a client's wardrobe;
  - scheduling with weather and wear notes;
  - posting "Styling Advice" that appears on the client's main screen.
  Every linked client gets Premium. — [getwardrobe.com/stylists](https://getwardrobe.com/stylists/); [pricing](https://getwardrobe.com/pricing/)

**Pricing and gating**
- Free: up to 100 items and outfits; all platforms and sync; Starter generator; AI Studio; calendar and weather; background removal; community.
- Premium: $9.99/mo or $49.99/yr (US), with a trial on annual.
- AI credits: 20/$4.99, 60/$9.99, 150/$19.99, 500/$59.99, 1000/$89.99.
- Stylist Pro: $690/yr ($57.50/mo equivalent).
- Sources: [getwardrobe.com/pricing](https://getwardrobe.com/pricing/)
- The IAP list also shows legacy Premium prices ($4.99/mo, $34.99/yr) and "Stylist – 1 Month" $69.99. — [App Store IAP](https://apps.apple.com/us/app/id656212466)
- Downgrade behaviour: "If you go back over the 100-item free cap, items remain stored but you'll need to re-upgrade to add new ones." — [pricing](https://getwardrobe.com/pricing/)

**Privacy**
- 2026.05.1 release note: "Nothing in it tracks you for ads or hands data to advertisers. Crash and usage diagnostics are optional, off by default". — [App Store version history](https://apps.apple.com/us/app/id656212466)
- Label: no tracking. Linked = Purchases, Location, Contact Info, User Content, Identifiers, Usage Data, Diagnostics. — [App Store privacy label](https://apps.apple.com/us/app/id656212466)
- "Full data export" is advertised, along with download or delete anytime from Settings and a data-portability right. — [getwardrobe.com](https://getwardrobe.com/); [privacy policy](https://getwardrobe.com/privacy-policy)

**Timeline**
- 2025.04: link and screenshot import.
- 2025.07: stats table.
- 2025.10: AI photo enhancer.
- 2025.11: try-on.
- 2026.01: AI Outfit Generator, editor redesign, universal search.
- 2026.03: weather planning and item scheduling.
- 2026.05: privacy rework.
- 2026.06: CPW per item, currencies.
- 2026.08: small-wardrobe generation.
- 2026.09: AI Studio and outfit rating.
- Source: [App Store version history](https://apps.apple.com/us/app/id656212466)

**UI**
- Clean white and blue (#0A84FF-like) accents, a green circular "+" FAB, and tabs Home / Clothes / Add / Outfits / Community. Marketing headlines are bold black with blue highlights. — [App Store screenshots](https://apps.apple.com/us/app/id656212466)

### Inferences
- GetWardrobe is the "power-user / family / pro stylist" option. Cross-platform web and Mac support, family profiles, Stylist Mode and data export are rare in the category.
- Splitting AI into free low-tier batches and paid credits for bigger or better models is a transparent cost-pass-through design.
- The 100-item free cap pushes serious users to Premium early.
- Best for families, planners, stylists and desktop users. Weak on social and visual polish compared with Fits and Whering.

### Gaps
- Founders and company history behind "Outfit Makers LLC" and the com.ThreeBoots package name are not documented.
- The "since 2008" vs App Store 2013 discrepancy is unexplained.
- The model providers for AI features are not named.

---

## 9. ShopLook (Shoplook Inc.): an additional non-AI-native app with meaningful traction

### Takeaway
ShopLook (San Francisco, founder Laya Adib, a former Google Shopping employee) is a Polyvore successor. It has 7,190 US ratings at 4.75 and 100K+ Android installs. It is a free-form creative canvas (outfits, moodboards, vision boards, memes, home decor) built on a 70M-image community library, with background removal, visual effects (outline, shadow, glow), text, challenges, collections and "remix". Any item can be made shoppable. It is not a personal-closet planner, though it supports "outfit ideas using clothes from your closet".

### Cited Findings
- Seller: Shoplook Inc. Released 2 Aug 2018. Current version 2.7.1 (24 Jun 2026). 7,190 US ratings (avg 4.75). Rated 12+. English. — [iTunes Lookup](https://itunes.apple.com/lookup?id=1408832096&country=us)
- Android: 100K+. — [Google Play](https://play.google.com/store/apps/details?id=io.shoplook)
- Claims: "community library of over 70M images", "community of over 1M creators", "the largest community of Polyvore users". Features: remix designs, challenges (join or create), "Make any item in your design instantly shoppable". — [App Store](https://apps.apple.com/us/app/id1408832096)
- Built In (Nov 2022): founder and CEO Laya Adib got the idea while working on Google's shopping platform. At that time the app had about 1.2M users, around 20 min/day average use, 12M+ designs and a mostly Gen-Z audience. It positions itself as an outlet after Polyvore's 2018 shutdown. — [Built In SF](https://www.builtinsf.com/articles/san-francisco-future-5-startup-shoplook-q4-2022)
- CB Insights: founded 2017, San Francisco, about $150K raised (Yellow, 500 Global, Endure Capital). — [CB Insights](https://www.cbinsights.com/company/shoplook)
- Mechanics from screenshots:
  - canvas with download, save and next;
  - effects panel (None / Outline / Shadow / Glow accent);
  - background-removal showcase;
  - Challenges (Featured / Following / Recent; "Official" badges; participant counts and timers);
  - home feed (Featured / Following / Fashion);
  - search across Shop / Design / Images / Users / Collections;
  - collections (e.g. "Summer Fun" by shoplook_editorial, with prices on items);
  - bottom nav: Home, Search, +, Trophy (challenges), Profile.
  — [App Store screenshots](https://apps.apple.com/us/app/id1408832096)
- Pricing: the description says premium (e.g. premium texts) costs $3.99/mo. The IAP list shows Premium $6.99, "Level-up your creativity" $4.99, Annual $39.99 and Annual Offer $34.99. — [App Store](https://apps.apple.com/us/app/id1408832096)
- Privacy label: "Data Not Collected", despite accounts and a social feed. — [App Store privacy label](https://apps.apple.com/us/app/id1408832096)
- 2025–26 releases: image quality and cropping; canvas background images and colors (Jul 2025); dark mode (Aug 2025); canvas size customisation (Sep 2025); text tool (Nov 2025); profile and design-view improvements (2026). — [App Store version history](https://apps.apple.com/us/app/id1408832096)
- The UI uses saturated violet/blue gradients and a playful, sticker-heavy style. — [App Store screenshots](https://apps.apple.com/us/app/id1408832096)

### Inferences
- ShopLook competes with Combyne for the collage-as-hobby audience, not with closet utilities. Its "shoppable item" linking is its commerce lever.
- A "Data Not Collected" label on a social app with accounts is likely inaccurate, the same pattern as Pureple.

### Gaps
- shoplook.io is behind Cloudflare, so the affiliate mechanics, current user counts and web canvas could not be verified.

---

## 10. Other apps found with traction, and dead or stagnant apps (scope check)

### Takeaway
Beyond the named apps, the only clearly non-AI-native wardrobe or outfit app meeting the traction bar was ShopLook (covered above). Codibook (Korea, 1M+ Android) has drifted into a Korean fashion shopping mall with a residual outfit-coordination feature. Several high-traction apps that surfaced are AI-native 2022–2024 entrants that this section's coordinator did not list: Dressly, Essembl and Lumi. They may need assignment. Lookscope is effectively abandoned.

### Cited Findings
- Codibook (Ydea, Seoul):
  - Released 13 Nov 2015. Current version 2.2.0 (8 Sep 2026). 1,808 US ratings (4.75) and 11,240 Korean ratings (4.62). Android 1M+.
  - It is now positioned as "Shop trending Korean fashion" with curated malls, shipping to 50+ countries and a first-order discount. "나만의 스타일을 직접 코디" (make outfits from items) remains a minor feature.
  — [iTunes Lookup US](https://itunes.apple.com/lookup?id=1052178370&country=us); [iTunes Lookup KR](https://itunes.apple.com/lookup?id=1052178370&country=kr); [Google Play](https://play.google.com/store/apps/details?id=com.ydea.codibook)
- Dressly: AI Outfit Maker (TIKVEX LIMITED). Released 21 Nov 2024. 32,661 US ratings. A selfie-based color analysis, outfit scoring and lookbook app. AI-native, so it is not torn down here. — [iTunes Lookup](https://itunes.apple.com/lookup?id=6738174180&country=us)
- Essembl (Essembl SA). Released 24 May 2024. 4,789 US ratings; Android 1M+. Its Oct 2026 redesign has four tabs: Today (daily outfits for weather and plans), Studio (canvas plus "Rate me"), Rate my look (selfie), and Shop. — [iTunes Lookup](https://itunes.apple.com/lookup?id=6479642952&country=us); [Google Play](https://play.google.com/store/apps/details?id=com.essembl.app)
- Lumi: styling assistant (Bramol Limited). Released Sep 2022. 16,483 US ratings. A personalised "Style Plan" rather than a closet. — [iTunes Lookup](https://itunes.apple.com/lookup?id=1637080045&country=us)
- Outfit Formulas (GYPO LLC). 1,194 US ratings; Android 10K+. Below the threshold, but mechanically relevant. It is a subscription capsule program:
  - a monthly shopping list of staples plus trends, with substitution notes and a retailer directory (petite, tall and plus links);
  - a daily "Outfit Formula" calendar;
  - a "Wore It Today" streak.
  — [iTunes Lookup](https://itunes.apple.com/lookup?id=6447664832&country=us)
- Lookscope (Lookscope, Inc.): last update 2 Apr 2024 (more than 18 months ago), 484 ratings. Its swipe-based outfit exploration including wishlist items was "Featured by Apple… December, 2023". Status: abandoned. — [iTunes Lookup](https://itunes.apple.com/lookup?id=1214741326&country=us)
- Abandonment and stagnation summary for the named apps:
  - Combyne went 20 months without updates (Sep 2023 – May 2025) and was then revived under SIA URSTYLE.
  - Smart Closet went 35 months without updates (Dec 2022 – Nov 2025) and is now maintenance-only.
  - None of the eight named apps is currently dead.
  — [Combyne history](https://apps.apple.com/us/app/id989727742); [Smart Closet history](https://apps.apple.com/us/app/id1198057728)
- Platform threat: Google Photos announced (29 Apr 2026) an AI feature that builds a wardrobe automatically from a user's photo library, with filtering, outfit mixing, moodboards and try-on. It is due on Android in summer 2026 and later on iOS under "Collections". TechCrunch named Acloset, Combyne, Pureple, Whering and Alta as the apps it will face. — [TechCrunch](https://techcrunch.com/2026/04/29/google-photos-uses-ai-to-make-the-iconic-closet-from-clueless-a-reality/)

### Inferences
- Fits' "scan your photo library for outfit photos" (Jun 2026) and Pureple's Whole Closet Scan (Oct 2026) look like direct responses to the Google Photos zero-effort-capture threat.

### Gaps
- I did not systematically check Android-only wardrobe apps outside the search results. "My Wardrobe – All your clothes" (BF Apps 4 You) has 100K+ installs, below the threshold.
- Rollout status of the Google Photos closet feature as of Oct 2026 was not verified.

---

## 11. Cross-app synthesis: mechanics map, gating patterns and who each app is best for

### Takeaway
The second tier has split into four archetypes:
1. Opinionated AI stylists built on a capsule method: Cladwell.
2. Feature-dense freemium closets with creative and social modes: Fits, Pureple, GetWardrobe.
3. Collage-as-social-game networks: Combyne, ShopLook.
4. Lifecycle and sustainability platforms with services: OpenWardrobe, Save Your Wardrobe.

In 2025–26 every active app converged on the same AI stack: auto-tagging, an LLM chat stylist, generated outfits, try-on, image cleanup and, increasingly, MCP/ChatGPT connectors. Differentiation is therefore shifting to:
- capture effort: catalogs, receipts, photo-library and closet scans;
- the planning model: capsules, multi-wardrobes, family profiles;
- post-purchase services: repair, resale, claims;
- metering design: allowances vs credits vs tiers.

### Cited Findings
- AI metering approaches differ by app:
  - Cladwell: a monthly AI allowance, with images costing more. — [cladwell.com/pricing](https://cladwell.com/pricing)
  - Fits: 30 credits/month in Pro plus credit packs, with try-on costing 1 credit. — [fits-app.com/knowledge/what-is-included-in-fits-pro](https://www.fits-app.com/knowledge/what-is-included-in-fits-pro)
  - GetWardrobe: a free Starter model plus credit-priced Standard/Pro models. — [getwardrobe.com/pricing](https://getwardrobe.com/pricing/)
  - Combyne: Premium "Discounts on AI features". — [combyne.com/subscription](https://www.combyne.com/subscription)
  - Pureple: Pro-only AI. — [App Store](https://apps.apple.com/us/app/id628106373)
- Item caps on free tiers:
  - GetWardrobe: 100 items. — [pricing](https://getwardrobe.com/pricing/)
  - OpenWardrobe: 500 items. — [faq](https://www.openwardrobe.co/faq)
  - Fits and Pureple: unlimited. — [fits free-vs-paid](https://www.fits-app.com/knowledge/free-vs-paid-features); [pureple.com/faq](https://pureple.com/faq)
  - Cladwell: unlimited closet, but no outfits on free. — [cladwell.com/pricing](https://cladwell.com/pricing)
- Agent and LLM connectors:
  - Cladwell has a ChatGPT connector. — [cladwell.com/chatgpt](https://cladwell.com/chatgpt)
  - Fits has an MCP server plus ChatGPT and Claude connectors. — [fits-app.com/mcp-documentation](https://www.fits-app.com/mcp-documentation)
- Capture innovations:
  - catalogs (Cladwell 15K items; OpenWardrobe "common items"; Fits/Combyne community libraries);
  - receipt forwarding (Fits);
  - Chrome order import (OpenWardrobe);
  - closet-photo multi-detect (Pureple);
  - photo-library outfit extraction (Fits);
  - screenshot import (GetWardrobe).
  — sources as cited in sections 1, 3, 5, 6 and 8.
- Planning models:
  - capsules (Cladwell);
  - multi-wardrobes with add-vs-move (OpenWardrobe);
  - collections typed wish/packing/custom (Fits);
  - family profiles (GetWardrobe);
  - folders and Smart Folders (SYW).
  — sources in sections 1, 5, 6, 7 and 8.
- Privacy labels vs policies: Pureple and ShopLook declare "Data Not Collected" while running accounts and community. Pureple's own policy documents Firebase analytics and OpenAI and Gemini transfers. — [Pureple App Store](https://apps.apple.com/us/app/id628106373); [Pureple privacy](https://pureple.com/Privacy.aspx); [ShopLook App Store](https://apps.apple.com/us/app/id1408832096)

### Inferences
- Who each app is best for:
  - Cladwell: decision-fatigued adults (including men) who want daily answers within a capsule.
  - Fits: style-curious Gen-Z and millennials who want free, beautiful, social and creative tools.
  - Pureple: legacy users and people who want unlimited free manual collage with optional paid AI.
  - GetWardrobe: families, planners, professional stylists and desktop/web users.
  - OpenWardrobe: value-tracking, sustainability-minded users and closet-sharing with stylists.
  - Save Your Wardrobe: UK/EU users who want repair and care services, plus brands needing aftersales.
  - Combyne and ShopLook: teens and creators who treat outfit-making as play and social currency.
  - Smart Closet: users who want a cheap one-time-purchase manual organiser.
- White space visible across the set:
  - No app combines Cladwell's "outfits unlocked per purchase" math with real wear data and resale value as OpenWardrobe does.
  - No closet app integrates claims and repair (SYW-AI) with the item record. SYW's passports are the closest.
  - Family and stylist multi-tenancy exists only in GetWardrobe.

### Gaps
- No app publishes its outfit-ranking algorithm.
- Without installing, onboarding screen sequences and paywall timing could not be verified for any app. The documentation covers only Cladwell's 2026 quiz-first onboarding, Fits' basic-pieces onboarding and checklist, and SYW's guest mode.
