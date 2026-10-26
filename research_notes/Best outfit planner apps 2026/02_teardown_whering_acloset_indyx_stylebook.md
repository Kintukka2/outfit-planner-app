# Teardown of Whering, Acloset, Indyx and Stylebook: product, UX and mechanics (as of 25 October 2026)

Scope note. These notes cover product, UX and mechanics for four established digital-wardrobe apps, at a level of detail meant to let a product team rebuild them. Reviews, business-model economics and tech stacks belong to other researchers; pricing appears here only where it gates features. "US App Store" figures come from the iTunes Lookup API and apps.apple.com pages fetched on 25 Oct 2026. Raw downloads (HTML, JSON and App Store screenshots) are in `/tmp/claude-0/-home-user-outfit-planner-app/bd7b84c5-3686-5200-b171-3a2b128c387a/scratchpad/clusterA/`.

Shorthand used in citations:
- App Store US pages: Whering = https://apps.apple.com/us/app/id1519461680 · Acloset = https://apps.apple.com/us/app/id1542311809 · Indyx = https://apps.apple.com/us/app/id1599179405 · Stylebook = https://apps.apple.com/us/app/id335709058
- iTunes Lookup: https://itunes.apple.com/lookup?id=<id>&country=us (also `&country=gb` / `&country=kr`)

---

## 1. Identity: developer, HQ, founders, launch, platforms, positioning, target user

### Takeaway
These are four different kinds of company. **Whering** is a London, VC-backed social wardrobe app with no paywall and 10M+ claimed users. **Acloset** is a Seoul AI-first app with a freemium item cap and AI credits, claiming 8M+ users. **Indyx** is a San Francisco startup built on "human stylists, not robots", combining a free wardrobe, an Insider subscription and paid stylist and digitisation services. **Stylebook** is a 2009 husband-and-wife indie iOS app sold for a one-time $4.99, which has not been updated since June 2025.

### Cited Findings

**Whering**
- Seller: Whering Ltd. Category Lifestyle/Shopping. US App Store title "Whering: Your Digital Closet", first released 20 Aug 2020. Current version 3.4.147 (7 Oct 2026). Rating 4.67 from 11,996 US ratings, age rating 12+/13+, requires iOS 15.5 — [iTunes Lookup](https://itunes.apple.com/lookup?id=1519461680&country=us); [App Store](https://apps.apple.com/us/app/id1519461680)
- Platforms: iPhone and iPod touch. No native iPad build (no iPad screenshots, not "iosUniversal"). It runs on Apple-silicon Macs (macOS 15+) and Apple Vision as a compatible iPhone app — [App Store compatibility block](https://apps.apple.com/us/app/id1519461680). It is on Android (5M+ downloads, 4.4★, 27.9K reviews) — [Google Play](https://play.google.com/store/apps/details?id=com.whering.app). There is a web sign-in at app.whering.co.uk, which tells new users "Download the free app for the full Whering experience" — [Whering web login](https://app.whering.co.uk/login). A Chrome extension exists (v3.0.4, updated 7 Sep 2026, 10,000 users, 3.5★) — [Chrome Web Store](https://chromewebstore.google.com/detail/whering/kjdgcemdlnbdohbcmnamlnnfkjoijfol)
- UK storefront title: "Whering: Your Digital Wardrobe" (4.60★, 3,080 GB ratings) — [GB lookup](https://itunes.apple.com/lookup?id=1519461680&country=gb). Languages: EN, FR, DE, IT, PT, ES — [App Store](https://apps.apple.com/us/app/id1519461680)
- Company: Whering Ltd, England & Wales company no. 12660509, 396 St John Street, London EC1V 4NJ — [Whering Terms (updated 17 Sep 2026)](https://whering.co/terms-conditions). The team is "over 20 people across the globe, with a base in Farringdon" — [Whering About](https://whering.co/about)
- Founder: Bianca Rangecroft, described as "ex-banker". Whering says "Bianca founded Whering in 2020" and that it is "used by over 10 million people globally" — [Whering About](https://whering.co/about); [whering.co.uk About](https://whering.co.uk/about). Press puts the founding in 2021 and reports a $7M seed round (7 Jul 2026) led by eBay Ventures and Google AI Futures Fund — [TheIndustry.fashion](https://www.theindustry.fashion/whering-secures-7m-to-expand-ai-powered-wardrobe-app/); [Tech.eu](https://tech.eu/2026/07/07/whering-lands-7m-as-digital-wardrobe-platform-reaches-10m-users/). Founding year conflict: 2020 (company/app store) vs 2021 (press).
- Positioning: "The world's largest free social styling & closet app (9 M+ users)"; "Whering is your closet's search engine, stylist and shopping sense-check built into one" — [App Store description](https://apps.apple.com/us/app/id1519461680). The current website headline is "Love your wardrobe… Join over 10 million people getting dressed with Whering", and the brand is "Originally inspired by the iconic wardrobe in Clueless" — [whering.co](https://whering.co/); [About](https://whering.co/about). The user base is "mainly Gen Z" (company-reported) — [FashionUnited](https://fashionunited.ca/news/business/whering-secures-funding-from-ebay-ventures-and-google-ai-futures/2026070745812)
- User-count conflict: 9M+ (App Store description), 10M+ (website and July 2026 press), 4M (older Dragons' Den page), 3M users ("over 20 million items styled… 500 million new outfits", same older page) — [Dragons' Den page](https://whering.co.uk/dress-me-app-dragons-den)

**Acloset**
- Seller: Looko Inc. (룩코). US title "Acloset - AI Fashion Assistant", first released 12 Mar 2021, v6.41.2 (20 Oct 2026). 4.45★ from 5,464 US ratings, 4+, iOS 15.1+, 18 languages — [iTunes Lookup](https://itunes.apple.com/lookup?id=1542311809&country=us). Korean storefront title "에이클로젯 - AI 디지털 옷장" (4.28★, 677 KR ratings) — [KR lookup](https://itunes.apple.com/lookup?id=1542311809&country=kr)
- Platforms: universal iPhone + iPad app, also runs on Apple-silicon Macs (macOS 13+) and Vision — [App Store](https://apps.apple.com/us/app/id1542311809). Android: 5M+ downloads, 4.5★ (31.1K reviews), "Editors' Choice", and the listing flags "Contains ads" — [Google Play](https://play.google.com/store/apps/details?id=com.looko.acloset). A Chrome extension (v1.0.7, 16 Jun 2026, 4,000 users) gives a desktop closet and wishlist side panel — [Chrome Web Store](https://chromewebstore.google.com/detail/acloset/ojndhgpicjeilhoaolbookejcdgbnkml)
- HQ: 양재대로12길 56 AI캠퍼스 703호, Seocho-gu, Seoul (developer address on the Chrome listing) — [Chrome Web Store](https://chromewebstore.google.com/detail/acloset/ojndhgpicjeilhoaolbookejcdgbnkml). CEO 고해신 (Go Haesin). The service officially launched February 2021 and passed 1M users in 18 months. Funding: TIPS (Apr 2022) and a ₩3bn Series A from KT Investment, Laguna Investment and Springcamp (Jun 2022) — [Venture Square, Oct 2022](https://www.venturesquare.net/865179/). Origin story: "In 2020, two engineers… with backgrounds in AI and data" — [Acloset About](https://www.acloset.app/about/)
- Scale claims: "8M+ users worldwide, 110M+ clothes digitized, 150+ countries", Google Play Editor's Pick (US), 2023 Google Play Featured — [Acloset About](https://www.acloset.app/about/). The App Store description still says "Join our community of 4 million users" — [App Store](https://apps.apple.com/us/app/id1542311809). Conflict noted.
- Positioning: "your all-in-one digital wardrobe and personal stylist… discover your unique style by chatting with our AI" — [App Store](https://apps.apple.com/us/app/id1542311809). Website: "Your AI-Powered Smart Closet… Your AI-picked outfit, every day" — [acloset.app](https://www.acloset.app/)

**Indyx**
- Seller: Indyx Inc. US title "Indyx: Wardrobe & Outfit App", first released 20 Jan 2022, v9.8.5 (20 Oct 2026). 4.77★ from 1,635 US ratings, 4+, iOS 15.6+, English only. Universal iPhone/iPad, no Mac listing — [iTunes Lookup](https://itunes.apple.com/lookup?id=1599179405&country=us); [App Store](https://apps.apple.com/us/app/id1599179405). Android: 100K+ downloads, 4.5★ (1.31K) — [Google Play](https://play.google.com/store/apps/details?id=com.indyx.android). "Is there a desktop version of Indyx? Not yet!" — [Indyx Support FAQ](https://www.myindyx.com/contact-us)
- Founders: Yidi Campbell (Founder/CEO; ex-Gap Inc. and Athleta strategy/ops, ex-investment banking) and Devon Rule (Co-Founder/Growth); Therese Jacinto (Creative) — [Our Story](https://www.myindyx.com/our-story); [Meet our Founder (Jul 2022)](https://www.myindyx.com/blog/meet-our-founder-yidi-campbell). Based in San Francisco: Devon "leads Growth for the company from San Francisco" — [Are We Cannibalizing Ourselves? (May 2025)](https://www.myindyx.com/blog/are-we-cannibalizing-expert-styling). Terms are governed by Delaware law — [Indyx Terms](https://www.myindyx.com/terms-and-conditions). Round size and investors are not confirmed in primary sources (see Gaps).
- Positioning: "Indyx unlocks the potential of what you own… index your closet, plan outfits, track your cost-per-wear, share your closet with friends, and get styled by experts" — [App Store](https://apps.apple.com/us/app/id1599179405). "Always a trusted style advisor, never a robot… We believe that style is a human art that can never be replaced by an algorithm" — [Indyx home](https://www.myindyx.com/). Claims "tens of thousands of real digital closets and over 10 million digitized items" — [State of Our Wardrobes (Apr 2025)](https://www.myindyx.com/blog/the-state-of-our-wardrobes-is-concerning)

**Stylebook**
- Seller: left brain / right brain, LLC. First released 29 Oct 2009, current v10.1 released 15 Jun 2025 (no update since, as of 25 Oct 2026). Paid upfront $4.99, 4.68★ from 8,863 US ratings, iOS 17+, 21.4 MB, languages EN/FR/JA/PT/ZH/ES, Family Sharing enabled — [iTunes Lookup](https://itunes.apple.com/lookup?id=335709058&country=us); [App Store](https://apps.apple.com/us/app/id335709058). A separate "Stylebook Men" app is also $4.99 (v10.1, 13 Jun 2025) — [Stylebook Men lookup](https://itunes.apple.com/lookup?id=515126225&country=us)
- Platforms: iPhone and iPad only. "Stylebook is only available for iOS devices", "doesn't currently run on desktop or laptop computers", and Android is "Not at the moment. As of 2025, we are focusing on iOS" — [Stylebook FAQ](https://www.stylebookapp.com/faq.html)
- Founders: Jess Atkins (ex-intern in the Vogue and Modern Bride fashion closets, art assistant at Lucky) and Bill Atkins (ex-Wall Street/NYC startup programmer). "A small, family-owned and operated indie app", founded 2009 — [About](https://www.stylebookapp.com/about.html); [FAQ](https://www.stylebookapp.com/faq.html). Location is not stated beyond the NYC career history.
- Positioning: "the longest-running closet organization app… over 90 features"; "If you want to break out of the micro-trend cycle and gain true personal style - it's for you" — [App Store](https://apps.apple.com/us/app/id335709058)

### Inferences
- Target users differ. Whering skews young, social and UK/EU (Gen Z, Clueless nostalgia, community feed). Acloset targets a global, mobile-first, AI-curious mass market (18 languages, guest mode, ads). Indyx targets US style-conscious women willing to pay for human expertise (stylists, in-home Archivists, design-forward editorial branding). Stylebook targets organisers and planners who want a private, offline-capable, one-time-purchase tool.
- Stylebook's 16-month update gap is unusual for a category leader and is a risk signal for buyers.

### Gaps
- Indyx funding amount and investors: only secondary aggregator claims (e.g. "Pre-seed backed by Alante Capital") turned up, with no primary confirmation.
- Stylebook's company HQ is not stated on its site.
- Acloset's second co-founder is not named publicly in the sources found.

---

## 2. Onboarding: sign-up, quiz/style profile, permissions, first-run flow, paywall placement

### Takeaway
Only Indyx runs a real style-profile quiz (Style Quiz → style words/archetypes, used for stylist matching). Acloset is the only one that lets you use the core app as a guest and it shows a soft paywall after sign-up. Indyx gives every new user a 14-day Insider Preview. Whering requires an account and has no visible paywall. Stylebook has no account at all because the paywall is the $4.99 download.

### Cited Findings

**Whering**
- An account is required: "You must register for an Account to access and use the main features" — [Terms §3.1](https://whering.co/terms-conditions). Accounts are Private by default — [Terms §7.2](https://whering.co/terms-conditions)
- Sign-in methods: email, plus "Facebook, Google and Apple log-in" — [Whering Privacy Policy](https://whering.co/privacy-policy)
- Profile data collected at or after sign-up: gender preference ("Can I change my gender preferences after signing up?… Settings>Personal information… 'Style'"), country (which drives the weather unit and currency) and username — [Whering FAQ](https://whering.co.uk/faq). Settings let you specify "your country, sizes, and preferences" — [Style Within Grace review (Nov 2023)](https://stylewithingrace.com/whering-wardrobe-app-review/)
- Friend discovery: "sync your contacts from your phone or Facebook to add friends" — [Whering FAQ](https://whering.co.uk/faq). Google Play's data-safety section lists Contacts (optional) — [Play data safety](https://play.google.com/store/apps/datasafety?id=com.whering.app)
- Permissions described in the Terms: photo library ("we will request your explicit permission" before gallery scanning), location ("may request access to your device's location in order to provide weather-based outfit suggestions") and explicit consent for video try-on — [Terms §13](https://whering.co/terms-conditions)
- Paywall: one third-party capture of the flow shows "5 onboarding steps" and labels the paywall "No Paywall… no visible paywalls or subscription prompts" (capture undated) — [ScreensDesign](https://screensdesign.com/showcase/whering-your-digital-closet)

**Acloset**
- Guest mode: "Even before signing up, you can add clothes using photos, import items into your Closet from your purchase history or email receipts, and create wishlists. However, features such as Try-On, payments/subscriptions, certain Explore and Community features, and rewards require an account." When logging in, guest data can be transferred — [Acloset FAQ](https://www.acloset.app/support/)
- Login methods: Google, Apple, Facebook, KakaoTalk or Email — [5.32.0 notes (Mar 2025)](https://www.acloset.app/announcements/5-32-0-update-news-6d0c0a/). The Chrome extension accepts email, Google or Apple but cannot create accounts — [Chrome Web Store](https://chromewebstore.google.com/detail/acloset/ojndhgpicjeilhoaolbookejcdgbnkml)
- First-run aids: a "Photo Shooting Guide" (5.31.0), a mission guide with a mission gauge and "Beans" rewards (5.32.0 and 6.1.0), "Add basic items" (a curated list of essentials) after the first registration (6.1.0), a step-by-step import guide for first-time purchase-history import (6.39.0, Sep 2026), and a 3-Day OOTD Challenge with rewards (6.2.0) — [5.31.0](https://www.acloset.app/announcements/5-31-0-update-news-4d4b16/); [5.32.0](https://www.acloset.app/announcements/5-32-0-update-news-6d0c0a/); [6.1.0](https://www.acloset.app/announcements/6-1-0-update-news-696503/); [6.2.0](https://www.acloset.app/announcements/6-2-0-update-news-768aaf/); [6.39.0](https://www.acloset.app/announcements/6-39-0-1790831958703/)
- Third-party capture (undated): "14 onboarding steps", mandatory and lengthy sign-up, then "a welcome modal after sign-up leads to the subscription page", typed "No Free Trial - Soft Paywall". It shows Free (100 items), Basic and Expert tiers, an annual discount example "40% $28.49/yr" and a referral offer of free closet slots — [ScreensDesign](https://screensdesign.com/showcase/acloset-ai-fashion-assistant). This conflicts with the current FAQ's guest mode, so the capture probably predates it.
- Permissions: coarse location is linked to identity for App Functionality (weather) — [App Store privacy label](https://apps.apple.com/us/app/id1542311809). Gmail read-only OAuth is optional — [FAQ](https://www.acloset.app/support/). Push notifications are used for OOTD reminders and Gmail-import alerts — [5.26.0](https://www.acloset.app/announcements/5-26-0-update-news-0ed421/); [6.27.0](https://www.acloset.app/announcements/6-27-0-1782269205433/)

**Indyx**
- Sign-in: "Sign in with Google or Sign in with Apple… Sign in with Email". The email address cannot be changed later — [Indyx Support FAQ](https://www.myindyx.com/contact-us); [Terms](https://www.myindyx.com/terms-and-conditions). Registration collects email (required), name (required) and location (optional) — [Privacy Policy](https://www.myindyx.com/privacy-policy)
- Trial: "When you first download Indyx, you'll enjoy a free 14-day Insider Preview with full access to all premium features… When the preview ends, you can keep using Indyx for free". After that there is a 7-day free trial on subscribing — [Support FAQ](https://www.myindyx.com/contact-us). The Terms let Indyx delete trial-created content or assets after a trial — [Terms §4](https://www.myindyx.com/terms-and-conditions)
- Style Quiz: "Use the Style Quiz to help articulate your style words" — [App Store](https://apps.apple.com/us/app/id1599179405). The quiz gives "personalized style archetypes" and "uses full outfit photos" (third-party capture, undated) — [ScreensDesign](https://screensdesign.com/showcase/indyx-wardrobe-outfit-app). Quiz results drive "Best Match" stylist matching — [Styling FAQ blog (Aug 2024)](https://www.myindyx.com/blog/how-digital-styling-works-frequently-asked-questions). A free 8-week Style Workshop course is offered — [App Store](https://apps.apple.com/us/app/id1599179405)
- Third-party capture (undated, probably older): "8 onboarding steps"; paywall typed "Free Trial - Hard Paywall… Free 30-day trial"; upsell appears when tapping a premium feature; annual plan "Most Popular, 45% off" — [ScreensDesign](https://screensdesign.com/showcase/indyx-wardrobe-outfit-app). This conflicts with the current 14-day preview, which is the more authoritative source.

**Stylebook**
- No account and no sign-up. Data lives "on your device and in iCloud if enabled", and the developers say "We can't see or access your closet data" — [Stylebook FAQ](https://www.stylebookapp.com/faq.html). The paywall is the $4.99 upfront price, with no in-app purchases listed — [App Store](https://apps.apple.com/us/app/id335709058)
- First-run guidance: "Start by adding 10 - 20 of your favorite in-season clothes first like a mini capsule!" — [Stylebook import tips](https://www.stylebookapp.com/docs/import.html). New users on Stylebook 10 get sync automatically; existing users activate it from the Sync tile — [Stylebook Sync post (Mar 2025)](https://stylebookapptips.wordpress.com/2025/03/13/stylebook-sync/). The Home screen shows a sync banner (shown at most 3 times per 10.1) — [10.1 notes](https://apps.apple.com/us/app/id335709058)

### Inferences
- Onboarding philosophies split cleanly. Acloset monetises early (soft paywall after sign-up, item cap at 100). Indyx front-loads premium value with a 14-day preview, then falls back to a generous free tier. Whering keeps everything free and gates nothing visible. Stylebook gates at purchase.
- Only Indyx's quiz output has a downstream use (stylist matching and style words). Whering's "gender preference" and Acloset's "shopping preference/age group" mainly drive recommendations and shop suggestions.

### Gaps
- Step-by-step onboarding screens (exact questions and permission-prompt order) for Whering, Acloset and Indyx are not publicly documented. The only sources are undated third-party video captures.
- Whether Whering asks for location and notifications during onboarding or only in context could not be confirmed.

---

## 3. Item capture: every way to add clothes and how background removal works

### Takeaway
All four remove backgrounds automatically. The differences are in volume capture: Acloset (store order-history login import, Gmail OAuth, email forwarding, multi-item "Smart Detector", product-code lookup, Chrome extension, Clothes Library search) and Indyx (receipt forwarding/Gmail, URL paste and in-home human Archivists). Whering relies on a crowd-sourced item database, a Chrome/share-sheet extension, a planned gallery scanner and retailer-account import. Stylebook has the richest manual editing tools (eraser, reverse eraser, tap-to-erase, clean-up) plus Apple-Intelligence text-to-image.

### Cited Findings

**Whering**
- Add-item menu: "tap the wardrobe icon… Press the plus sign and tap 'Add items' to select from your camera roll/database/web" — [Whering FAQ](https://whering.co.uk/faq)
- Whering Database: "a database of clothing uploaded by other users… search the Whering database to see if it's already been uploaded". **Whering Basics** is "a curated selection of staples". The App Store claims "a database of over 100 million items" — [FAQ](https://whering.co.uk/faq); [App Store](https://apps.apple.com/us/app/id1519461680). You can also add items "directly from any other closet to yours" (friends' wardrobes) — [App Store](https://apps.apple.com/us/app/id1519461680). Users can hide their items from the database in Settings — [FAQ "Account Settings"](https://whering.co.uk/faq)
- Web/share import: hold an image on a retailer site → Share → Whering; or "use our in-app Google search to find your pieces online"; Wishlist also accepts pasted URLs — [FAQ](https://whering.co.uk/faq)
- Chrome extension (redesigned, v3.0.4): hover over a product image, a Whering button appears, then save to Wardrobe, Wishlist or Moodboard (with folders). Works on Pinterest too — [Chrome Web Store](https://chromewebstore.google.com/detail/whering/kjdgcemdlnbdohbcmnamlnnfkjoijfol); [FAQ](https://whering.co.uk/faq). The Terms describe "browser and mobile extensions" that collect image URL, domain, product name and price — [Terms §11](https://whering.co/terms-conditions)
- Batch limit: "You can select 10 photos to upload at a time" vs "You can select up to 15 items to upload at a time" (both in the same FAQ; conflict). No cap on total items — [FAQ](https://whering.co.uk/faq)
- Background removal is automatic ("we'll remove the background"). Users cannot edit it: "Right now it's not possible for users to edit background removal"; no rotation ("you can't rotate pictures you've uploaded"); one photo per item ("you can only add one photo per item right now"); the photo can be replaced via ⋮ → "replace item photo" — [App Store](https://apps.apple.com/us/app/id1519461680); [FAQ](https://whering.co.uk/faq). Listed processors include Kaleido AI GmbH (remove.bg) and Pixyle B.V. (pixyle.ai, fashion-tagging AI) — [Whering Privacy Policy](https://whering.co/privacy-policy)
- **Enhance** (shipped Apr 2026): "turn photos into product-style images… Fix bad lighting and creased fabric" — [App Store version history 3.2.101](https://apps.apple.com/us/app/id1519461680)
- Gallery scanning: "analyses images stored on your device to identify clothing items", keeps only the clothing images you choose, does no facial recognition — [Terms §13.1](https://whering.co/terms-conditions). Press describes it as funded/planned (Jul 2026) — [Tech.eu](https://tech.eu/2026/07/07/whering-lands-7m-as-digital-wardrobe-platform-reaches-10m-users/)
- Connected retailer accounts: "connect accounts you hold with supported third-party retailers in order to import your purchase history" — [Terms §15.2](https://whering.co/terms-conditions)
- Human digitisation: the FAQ says "we current do not offer a 'white glove' digitisation service" but takes a waitlist. A separate page says "We're quietly launching… a select, vetted group of premium closet organisers to offer a personalised, in-home wardrobe digitisation service… Join waitlist" — [FAQ](https://whering.co.uk/faq); [White Glove page](https://whering.co.uk/white_glove_service)

**Acloset**
- Camera and gallery: "Take a photo… our AI will automatically remove the background and fill in details like category and color… snap multiple photos in succession… or separate multiple pieces from a single photo" — [Acloset FAQ](https://www.acloset.app/support/). The camera has single vs continuous ("multi-shot") modes and remembers the last mode (6.36.0, Sep 2026) — [6.36.0](https://www.acloset.app/announcements/6-36-0-1789025324895/)
- **Smart Detector**: "Simply take a daily mirror selfie, and our AI will detect your outfit, identify each piece, and add them" (6.0.0, May 2025). Up to 4 items are auto-separated per photo from album, camera, clipboard or web capture (6.21.0, Apr 2026). An "Auto-split outfits" toggle was added in 6.25.0. **Smart Detector Pro** lets users add or edit the detected areas and restores clothes at higher quality, and the basic version became free (6.9.0) — [6.0.0](https://www.acloset.app/announcements/6-0-0-update-news-b11b81/); [6.21.0](https://www.acloset.app/announcements/6-21-0-1776924729783/); [6.25.0](https://www.acloset.app/announcements/6-25-0-1781577355481/); [6.9.0](https://www.acloset.app/announcements/6-9-0-update-news-bb3ff9/)
- **Beautify / Beautify Pro**: "transforms your item shots into clean, high-quality images just like the ones you'd find in an online store" (6.0.0). Beautify Pro switched to "Gemini 2.5 Flash Image aka nano banana" and "preserves fine fabric textures, seams… buttons and patterns" (Sep 2025). It extends to accessories and jewellery (6.32.0), runs from the upload preview thumbnail (6.22.0) and from imported item cards with cancel/revert (6.37.0) — [Beautify Pro](https://www.acloset.app/announcements/meet-the-new-beautify-pro-39a609/); [6.22.0](https://www.acloset.app/announcements/6-22-0-1777344868568/); [6.32.0](https://www.acloset.app/announcements/6-32-0-1789024972907/); [6.37.0](https://www.acloset.app/announcements/6-37-0-1789041364168/)
- **Online-store order-history import**: pick a store, log in inside the in-app browser, open the order history, tap "Import" and select items. Imported fields are "order number, order date, order status, product name, brand, price, quantity/options, product image, and product page link". Passwords are not stored and buyer name, address and payment are excluded — [FAQ](https://www.acloset.app/support/). The screenshot shows Amazon, SHEIN, ZARA and ASOS as "Recommended" — [App Store screenshot 3](https://apps.apple.com/us/app/id1542311809). The app auto-navigates to the order-history page and picks the regional store (6.29.0); "Find photo on product page" with Auto Scan or Capture (6.39.0) — [6.29.0](https://www.acloset.app/announcements/6-29-0-1784856595517/); [6.39.0](https://www.acloset.app/announcements/6-39-0-1790831958703/)
- **Email receipts**: Gmail OAuth ("read-only access… only scans shopping receipt emails… scan history is automatically deleted after 7 days"; "Import 6 more months" back to 5 years; auto-detects new receipts with push notifications), or forwarding to a unique `[ID]@acloset.app` address with auto-forward setup. Items are never auto-saved, and the user selects them — [FAQ](https://www.acloset.app/support/); [6.19.0](https://www.acloset.app/announcements/6-19-0-1774514316683/); [6.27.0](https://www.acloset.app/announcements/6-27-0-1782269205433/). The first Gmail scan was extended to 1 year and returns/refunds/marketing are filtered out (6.31.1) — [App Store version history](https://apps.apple.com/us/app/id1542311809)
- **Clothes Library**: "Search for your clothes among millions of items… by text or by image" (6.0.0) — [6.0.0](https://www.acloset.app/announcements/6-0-0-update-news-b11b81/). **Product code**: type a product code or photograph the tag (6.10.0, Oct 2025) — [6.10.0](https://www.acloset.app/announcements/6-10-0-update-news-7c2513/). **AI Scan** detects item images on shopping pages (5.32.0), and web images can be saved instantly (5.33.0) — [5.32.0](https://www.acloset.app/announcements/5-32-0-update-news-6d0c0a/); [5.33.0](https://www.acloset.app/announcements/5-33-0-update-news-674a9a/)
- **Chrome extension**: "Start selection" in the side panel, click a product image, and AI fills category, colour and brand (editable). Bulk registration from PC — [Chrome Web Store](https://chromewebstore.google.com/detail/acloset/ojndhgpicjeilhoaolbookejcdgbnkml); [6.25.0](https://www.acloset.app/announcements/6-25-0-1781577355481/)
- Image editing: "Edit Clothes Image… adjust the color and angle of the image in more detail" (iOS, Dec 2022) — [3.9.0](https://www.acloset.app/announcements/3-9-0-update-news-97a21c/)

**Indyx**
- Own photos: "messy hanger shots, rushed flatlays… we'll automatically remove the background, auto-tag with AI that works, and transform them into clean, editorial flatlays" — [App Store](https://apps.apple.com/us/app/id1599179405)
- **Enhance** (9.6.3, May 2026): "turns any photo… rushed, badly-lit, hanger and all — into something beautiful". **Refine** (9.8.3, Sep 2026): "write a quick prompt on any Enhanced image" — [App Store version history](https://apps.apple.com/us/app/id1599179405). "Unlimited enhanced flatlays" and "Super HD wardrobe images" are Insider-only — [Indyx pricing table](https://www.myindyx.com/)
- Receipts: forward order confirmations to `[yourusername]@scan.myindyx.com` — [Support FAQ](https://www.myindyx.com/contact-us). An optional connected email account reads only "Purchases"-category emails, and an "AI parsing service" extracts product name, price, brand and image URL — [Privacy Policy](https://www.myindyx.com/privacy-policy). Paste a product link for "URL upload" (9.3.0, Feb 2026) — [App Store 9.3.0](https://apps.apple.com/us/app/id1599179405)
- Retailer images: "grabbing the flat-lay photo from the brand or retailer… Save these images to your camera roll and upload" (PNG/JPG; screenshot as a workaround) — [Indyx home FAQ](https://www.myindyx.com/); [Support FAQ](https://www.myindyx.com/contact-us)
- **Human digitisation, "The Catalog" by Indyx Archivists**: booked in the Style tab, available in select US metros. "We come to you to catalog 100 items in five hours… $2 per item above the initial 100"; the client needs about 20 minutes. The Archivist captures a lay-flat photo plus category, subcategory, brand, size, colour, condition and material, uploading live — [Pro Cataloging blog (Jul 2022)](https://www.myindyx.com/blog/what-to-expect-during-a-pro-indyx-wardrobe-cataloging-session); [Support FAQ](https://www.myindyx.com/contact-us). Rescheduling within 24h costs $100 and late cancellations are forfeited — [Support FAQ](https://www.myindyx.com/contact-us). Time estimate for DIY: "about 2 minutes per item… 6-8 hours total" — [Indyx home FAQ](https://www.myindyx.com/)

**Stylebook**
- Five no-photo import routes: AI text-to-image generation, drag and drop, clipping from the web, multiple import from an album, and a "built-in clothing image catalog" (Wardrobe Basics) — [Stylebook FAQ](https://www.stylebookapp.com/faq.html). There is also a Google Lens visual-search guide (Jul 2025; the post is paywalled on the tips blog) — [Tips blog](https://stylebookapptips.wordpress.com/2025/07/28/how-to-add-photos-to-stylebook-with-google-lens/)
- Camera, photo library, copy and paste ("paste multiple images at once"), Continuous Import, and a clipper/built-in browser where you "type keywords to search the web" — [Features](https://www.stylebookapp.com/features.html); [Stylebook 10](https://www.stylebookapp.com/stylebook10.html)
- **AI image generation**: "Type a short simple description… like 'red sweater'… add 'flat lay'". Requires iOS 18.2+ and an Apple Intelligence device — [AI Image Generator post](https://stylebookapptips.wordpress.com/2025/03/13/al-image-generator/)
- **Multiple import**: toggle Multiple Import, choose from album, and backgrounds are removed simultaneously. Failed cut-outs show a red outline. Then "Categorize Items → Bulk Select → folder" — [Multiple import post](https://stylebookapptips.wordpress.com/2025/03/13/import-multiple-photos-from-an-album/)
- **Background removal**: automatic AI removal "virtually instantly" with no special background needed (since v10). Advanced Edit offers a paintbrush "reverse eraser" (restores pre-save only), Tap-to-Erase with a tolerance slider, an eraser with size slider, and "Keep Original Background & Save". "Clean Up" re-processes old images and normalises their crop — [AI Background Removal post](https://stylebookapptips.wordpress.com/2025/03/13/ai-background-removal/); [Clean Up post](https://stylebookapptips.wordpress.com/2025/03/13/how-to-clean-up-existing-images/); [Import tips](https://www.stylebookapp.com/docs/import.html)
- **Multiple images per item**: "X Extra Images", with "Make Primary Image" — [Extra images post](https://stylebookapptips.wordpress.com/2025/03/13/how-to-add-extra-images-to-closet-entries/)
- A power-user "virtual mannequin" technique (photograph yourself, then photograph clothes in the same pose) is documented in a 2021 post — [Virtual Mannequin (Apr 2021)](https://stylebookapptips.wordpress.com/2021/04/08/how-to-make-a-virtual-mannequin/)

### Inferences
- Acloset has the most complete "zero-photo" funnel (order history, Gmail, forwarding, product code, library, extension). It is clearly where its 2026 engineering went: most 6.19–6.41 releases touch import.
- Indyx is the only one of the four with a live, bookable human-digitisation service. Whering has a waitlist for one.
- Stylebook's manual mask tools are the most granular, but it is the only one without a generative "enhance to product shot" feature (Clean Up only re-cuts).

### Gaps
- Exact supported-store lists for Acloset imports and Whering retailer-account connections are not published.
- It is undocumented whether Whering's gallery scanning and retailer-account import are live for all users or only described in the Terms (the Terms use "may offer").
- Indyx Enhance's free-tier quota (versus "unlimited" for Insider) is not stated publicly.

---

## 4. Item data model: fields, AI auto-tagging vs manual, editing UX

### Takeaway
Acloset has the deepest AI-filled attribute schema (category, colour, pattern, material, neck style, length, fit, season, TPO, laundry), because its AI stylist consumes those attributes. Stylebook has the deepest manual schema (100+ colour shades, status workflow, size tracker), with no AI tagging. Whering and Indyx sit in between, with AI tagging of category, subcategory and colour plus user tags.

### Cited Findings

**Whering**
- Auto-tags are editable ("Edit automated tags by selecting an item… Press the pencil"). Custom private tags; brand (custom brands allowed); price (never visible to others); seasons; occasions; categories and subcategories; favourite ♡; visibility (eye icon hides an item from the public profile); archive; a "Status" tag for temporary states such as "in the wash" — [Whering FAQ](https://whering.co.uk/faq)
- Bulk edit: long-press → multi-select → More → Tags, Brands, Occasions, Categories, Seasons or visibility — [FAQ](https://whering.co.uk/faq)
- Item info can include purchase date, colours, composition materials, tags and price — [Hinchi review (May 2025)](https://hinchi2.wordpress.com/2025/05/02/app-review-whering-clothing-tracking/). Stats break down the share of new, preloved, rental and handmade items, implying an acquisition-source field — [Style Within Grace (Nov 2023)](https://stylewithingrace.com/whering-wardrobe-app-review/); App Store "Intake Tracking: Log new vs. pre-loved buys" — [App Store](https://apps.apple.com/us/app/id1519461680)
- Sizes are UK-only ("Why can I only see UK sizes?… working on adding different sizing options") — [FAQ](https://whering.co.uk/faq)
- Under the item image there is a "Styling tab" with outfit ideas for that item — [FAQ](https://whering.co.uk/faq)

**Acloset**
- AI analysis fills category and colour on upload and shows a progress bar. If analysis is slow, users can enter details manually (5.31.0) — [FAQ](https://www.acloset.app/support/); [5.31.0](https://www.acloset.app/announcements/5-31-0-update-news-4d4b16/). The extension's AI also fills "category, color, season, and pattern" — [acloset.app](https://www.acloset.app/)
- Attributes used by AI Styling: "category, color, pattern, material, neck style, length, fit, etc." — [5.24.0 (Oct 2024)](https://www.acloset.app/announcements/5-24-0-update-news-623ca1/). Planned/added: expanded categories, colours, patterns and materials; neck style, fit, length and laundry instructions — [Roadmap notice (Sep 2024)](https://www.acloset.app/announcements/notice-on-upcoming-updates-including-the-ai-styling-service-775c9c/); [5.26.0 "Expanded Closet Attributes"](https://www.acloset.app/announcements/5-26-0-update-news-0ed421/)
- Other fields: purchase date and cost — [App Store](https://apps.apple.com/us/app/id1542311809). Hijab and Traditional Clothing categories (5.31.0). "Hashtag" renamed "Label" (5.31.0). Manual wear count (5.26.0). A "Not set" filter for items with no value (6.38.0). Category search during registration (6.30.0) — [5.31.0](https://www.acloset.app/announcements/5-31-0-update-news-4d4b16/); [5.26.0](https://www.acloset.app/announcements/5-26-0-update-news-0ed421/); [6.38.0](https://www.acloset.app/announcements/6-38-0-1789625052486/); [6.30.0](https://www.acloset.app/announcements/6-30-0-1785377094985/). Item detail editing covers season, occasion, colour, material and pattern (undated capture) — [ScreensDesign](https://screensdesign.com/showcase/acloset-ai-fashion-assistant)
- **Quick Closet Review**: rapid per-attribute review and editing across many items (5.24.0) — [5.24.0](https://www.acloset.app/announcements/5-24-0-update-news-623ca1/). It appears in the Closet shortcut row as "Closet Review" — [App Store screenshot](https://apps.apple.com/us/app/id1542311809)

**Indyx**
- AI auto-tags "category, subcategory, and color for every item". Users add "brand, season, location, and more", and can filter and search by any of them — [App Store](https://apps.apple.com/us/app/id1599179405). Play listing: "Categorize and filter your items by brand, category, subcategory, color, season, and location" — [Google Play](https://play.google.com/store/apps/details?id=com.indyx.android)
- Archivist schema: Category (Top, Bottom, Outerwear, Dress, Handbag, Shoes, Swim, Jewelry, Accessory), Subcategory, Brand, Size, Color, Item Condition, Material — [Pro Cataloging blog (2022)](https://www.myindyx.com/blog/what-to-expect-during-a-pro-indyx-wardrobe-cataloging-session)
- A "do not style" tag hides items from stylists, and archived items are also hidden from them — [Styling FAQ blog (Aug 2024)](https://www.myindyx.com/blog/how-digital-styling-works-frequently-asked-questions). Material type is grouped into natural, semi-synthetic and synthetic for analytics (9.7.0) — [App Store](https://apps.apple.com/us/app/id1599179405). Price is implied by CPW tracking — [App Store](https://apps.apple.com/us/app/id1599179405)
- "Improved auto-tagging for item upload" (8.9.11, Nov 2025) — [App Store version history](https://apps.apple.com/us/app/id1599179405)

**Stylebook**
- Notes page fields: free-text keywords (searchable); colour (100+ shades, multiple per item, optional colour names for accessibility); brand and fabric (autocomplete from previous entries); size; season; status (clean, in the laundry, at the cleaners, lent out, wishlist item, and since v10 Archived); URL; price → automatic $/wear; worn-dates list ("Worn X Days") — [Features](https://www.stylebookapp.com/features.html); [Clothing notes post](https://stylebookapptips.wordpress.com/2016/04/12/how-to-save-clothing-notes/); [Multiple colours post](https://stylebookapptips.wordpress.com/2016/01/30/using-multiple-colors-in-stylebook/); [Archive post](https://stylebookapptips.wordpress.com/2025/03/13/stylebook-archive/)
- No AI auto-tagging is documented. "Date Purchased" was listed as planned, not shipped — [Stylebook 10](https://www.stylebookapp.com/stylebook10.html)
- Bulk edit: Select → notes icon → change colour, fabric, size, season, brand, status or URL for many items. Freeform notes cannot be bulk-edited — [Multi-select post](https://stylebookapptips.wordpress.com/2015/08/03/multi-select-and-bulk-actions/)

### Inferences
- A rebuild should treat attribute quality as an AI-input problem, as Acloset does: Quick Closet Review and the "Not set" filter exist because recommendation quality depends on clean attributes.
- Stylebook's status workflow (laundry, lent out) is a distinctive availability model the others lack. Whering uses a "Status" tag, but the FAQ doesn't spell out its values.

### Gaps
- Full field lists for Whering and Indyx (e.g. size, purchase date, care) are not published in a single help article. Only partial lists from FAQs, reviews and the 2022 Archivist schema exist.
- Whether Indyx "location" means storage location or home/city is undocumented.

---

## 5. Organisation: closets, categories, filters, sort, search, multiple closets, archive/sold states, wishlist

### Takeaway
Acloset supports multiple user-defined closets (with items in several closets at once) and Stylebook supports a two-level folder tree. Whering has one wardrobe with fixed top categories. Indyx uses Collections for capsules, wishlists and packing. All four have an archive for retired items, but only Acloset counts archived items against plan limits.

### Cited Findings

**Whering**
- One wardrobe only ("Currently, users can only have one wardrobe"). Top-level category chips (All, Tops, Bottoms, Outerwear, Shoes, Accessories). A sliders filter covers subcategory, colours, seasons, tags, and most/least worn, with Reset. Favourites ♡ filter. Search bar. No manual re-ordering ("Items are displayed in the order of upload") — [Whering FAQ](https://whering.co.uk/faq); [App Store screenshot 2](https://apps.apple.com/us/app/id1519461680)
- Archive: for items "you no longer own (sold, donated, rented or recycled)". Archived items are excluded from stats and search but keep their styling history. Outfits can also be archived — [FAQ](https://whering.co.uk/faq)
- Wishlist: heart-ribbon icon by the profile photo, folders, add by URL, image or marketplace heart, "View on Web" to buy, shareable, privacy per list or item. Sold-out items stay listed — [FAQ](https://whering.co.uk/faq). Moodboards: collage icon, camera-roll or extension adds, pin friends' outfits — [FAQ](https://whering.co.uk/faq). Lookbooks group outfits or items — [FAQ](https://whering.co.uk/faq)

**Acloset**
- Multiple closets: "Create your closet however you want… Add the same item to multiple closets… Deleting an item permanently deletes it from all closets", with outfit suggestions from a chosen closet (Sep 2022) — [Closets announcement](https://www.acloset.app/announcements/now-you-can-organize-your-clothes-by-closet-the-way-you-want-e17003/). Closet cover images and order, custom item order, and per-closet filters (5.26.0) — [5.26.0](https://www.acloset.app/announcements/5-26-0-update-news-0ed421/). Screenshot shows "All Clothes 50 items", "Winter items 28 items" and "Create closet" — [App Store screenshot 2](https://apps.apple.com/us/app/id1542311809)
- Sort and filter: sort clothes and outfits by rating, price, date or custom order; 3/4/5-column grids (6.35.0). Filters for wear count and "Added to closet" (6.30.0), plus "Not set" (6.38.0). Filters for colour, material and pattern in pickers (5.30.0) — [App Store version history](https://apps.apple.com/us/app/id1542311809); [5.30.0](https://www.acloset.app/announcements/5-30-0-update-news-8e05d0/)
- Archive counts toward the plan item limit, with bulk delete and bulk restore — [Subscription overview (Dec 2023)](https://www.acloset.app/announcements/subscription-plans-overview-eb9fc8/); [5.26.0](https://www.acloset.app/announcements/5-26-0-update-news-0ed421/)
- Wishlist (6.9.0, Oct 2025): wishlist items are "not included in app statistics, recommendations, or subscription limits", can be tried in the "Fitting room", and used in outfit suggestion — [6.9.0](https://www.acloset.app/announcements/6-9-0-update-news-bb3ff9/)

**Indyx**
- Profile shows tabs **Items / Outfits / Collections** with counts, a search bar and filter icons. Items show brand captions — [App Store screenshots 1, 3](https://apps.apple.com/us/app/id1599179405)
- Grid density "2 items across" to "7-items across". Custom order — [Google Play](https://play.google.com/store/apps/details?id=com.indyx.android). "Customize your wardrobe display" (drag-and-drop tiles, bird's-eye view) is Insider-only — [Pricing table](https://www.myindyx.com/); [Insider launch (Sep 2024)](https://www.myindyx.com/blog/why-we-are-launching-insider)
- Sort by number of wears and CPW (free). Most/Least Recently Worn sort added in 9.8.3 (Sep 2026) — [App Store](https://apps.apple.com/us/app/id1599179405)
- Collections: folders such as Packing, Capsules, Wishlist and Clearance, with collections like "Fiji / Melbourne 15 items", "Swap Meet", "Wishlist - Shoes". "Unlimited packing lists, wishlists, and capsules" on Free — [App Store screenshot 7](https://apps.apple.com/us/app/id1599179405); [Pricing table](https://www.myindyx.com/)

**Stylebook**
- Two-level closet: folders with subcategories (e.g. Tops → Long Sleeve Tops, Tank Tops, Sweaters, Short Sleeve Tops). "All Items in [folder]". Add, rename, delete and reorder categories. Custom category icons. Looks and Inspiration have single-level categories — [Customize categories post](https://stylebookapptips.wordpress.com/2015/03/24/how-to-customize-your-stylebook-categories/); [App Store screenshot 7](https://apps.apple.com/us/app/id335709058)
- Search by colour, season, fabric, brand, status, category name and keywords. Archived items can be excluded from search (10.1) — [Search post](https://stylebookapptips.wordpress.com/2015/07/06/how-to-search-your-closet-by-season-color-etc/); [10.1 notes](https://apps.apple.com/us/app/id335709058)
- Thumbnail size is selectable at 4 sizes universally — [Icon sizes post](https://stylebookapptips.wordpress.com/2025/03/13/how-to-customize-stylebook-icon-sizes/)
- Archive status keeps items visible but excludes them from Style Stats, and "protect[s] your calendar streak" — [Archive post](https://stylebookapptips.wordpress.com/2025/03/13/stylebook-archive/). Wishlist: clipped products and a text wishlist in Shop — [Features](https://www.stylebookapp.com/features.html)

### Inferences
- The multi-closet (tag-like, many-to-many) model in Acloset and the folder model in Stylebook support "capsule" workflows natively. Whering pushes capsules into Lookbooks and Indyx into Collections.

### Gaps
- Whering's and Indyx's full sort menus are not documented beyond the items above.

---

## 6. Outfit building: canvas, generators, AI suggestions, saving and tagging

### Takeaway
Every app has a free-form collage canvas. The generators range from random shuffle (Stylebook Outfit Shuffle™, Whering Dress Me) to LLM-style conversational AI (Acloset's AI stylists) to human stylists (Indyx's Feed, Lookbook and Call). Acloset is the only one where AI fills specific category slots on request. Indyx deliberately has no algorithmic outfit generator.

### Cited Findings

**Whering**
- **Canvas** (Styling tab): blank canvas where you add, drag and resize pieces. Tap an item to bring it forward or back, duplicate or remove — [Style Within Grace (Nov 2023)](https://stylewithingrace.com/whering-wardrobe-app-review/). The screenshot shows Styling → tabs "Dress me / Canvas / Moodboards", an undo button, "Save", a right-hand column of icon buttons (including a delete/bin icon) and an "Add" drawer — [App Store screenshot 1](https://apps.apple.com/us/app/id1519461680). Moodboard images can be pulled into outfits — [Style Within Grace](https://stylewithingrace.com/whering-wardrobe-app-review/)
- **Dress Me** ("Clueless-inspired outfit creator"): category rows (sliders) that shuffle. 2-, 3- or 4-piece layouts. Pin items to hold them. Filters cover season, colour, favourites, Lookbook, least-worn priority, and excluding activewear, swimwear or underwear. "Style in Dress Me" from an item card pre-pins it. Save → schedule, add to Lookbook or save. Layout toggle icons appear at the bottom in the screenshot — [FAQ](https://whering.co.uk/faq); [Style Within Grace](https://stylewithingrace.com/whering-wardrobe-app-review/); [App Store screenshot 4](https://apps.apple.com/us/app/id1519461680). Known limits per 2023 review: can pair two tops and doesn't include accessories — [Style Within Grace](https://stylewithingrace.com/whering-wardrobe-app-review/)
- **W Pick / "Discover new outfits"** (BETA): "daily outfit suggestions based on your previous styling choices"; "based on your personal style and the weather forecast"; "6 outfits per day", each ✗ shows another (up to 6); only owned items; save via the wardrobe icon, then edit like any outfit — [FAQ](https://whering.co.uk/faq). The screenshot shows swipe cards labelled "Dismiss" and "Schedule" with ✗, Save and "edit" buttons — [App Store screenshot 10](https://apps.apple.com/us/app/id1519461680)
- 2026 Planner AI: "Planner now suggests outfits based on your wardrobe and the weather… plan ahead for trips, weekends and events by changing your location" (3.3.35+ through 3.4.147) — [App Store version history](https://apps.apple.com/us/app/id1519461680)
- Outfit Maker / Style Pass: "Lacking inspiration? Get a Style Pass and try our Outfit Maker" — [App Store](https://apps.apple.com/us/app/id1519461680). An "Outfit Maker" IAP at $4.99 exists — [App Store IAP list](https://apps.apple.com/us/app/id1519461680)
- Outfit management: unlimited outfits; duplicate ("Create a copy" opens Canvas); Lookbooks; archive; bulk tag outfits; share collage (no save-to-photos) — [FAQ](https://whering.co.uk/faq)

**Acloset**
- **Create idea / outfit canvas**: freeform placement of items, text and stickers (undated capture). The screenshot toolbar shows layout, image, text (T), sticker and more, with item picker tabs All/Tops/Pants/Outerwears/Shoes/Bags and a closet selector "All Clothes" — [ScreensDesign](https://screensdesign.com/showcase/acloset-ai-fashion-assistant); [App Store screenshot 5](https://apps.apple.com/us/app/id1542311809). Pin items and shuffle the rest (5.32.0). Text input in Ideas, My Rate scores, multiple Idea Books with covers (5.26.0) — [5.32.0](https://www.acloset.app/announcements/5-32-0-update-news-6d0c0a/); [5.26.0](https://www.acloset.app/announcements/5-26-0-update-news-0ed421/)
- **AI outfit recommendation**: "analyzing the weather, occasion, and your personal wardrobe… actively utilizes the attribute information of each clothing item" (5.24.0). Choose by situation (Daily, School, Date, Wedding, Travel) or free-text requests such as "An outfit idea with a shirt" (6.8.0). Considers "location, current time, day of the week, and weather" (6.30.0). Any date in the next 7 days (6.34.0). Custom high/low temperature (6.38.0). Accessories as anchors (6.20.0) — [5.24.0](https://www.acloset.app/announcements/5-24-0-update-news-623ca1/); [6.8.0 via App Store/site](https://www.acloset.app/announcements/6-8-0-update-news-f649a5/); [6.30.0](https://www.acloset.app/announcements/6-30-0-1785377094985/); [6.34.0](https://www.acloset.app/announcements/6-34-0-1789025177704/); [6.38.0](https://www.acloset.app/announcements/6-38-0-1789625052486/); [6.20.0](https://www.acloset.app/announcements/6-20-0-1776058999305/)
- **Outfit completion ("Style with Items")**: keep chosen pieces and AI fills only the selected missing categories, or "Let AI choose". Never recommends incompatible pieces (e.g. pants + skirt) — [FAQ](https://www.acloset.app/support/); [6.30.0](https://www.acloset.app/announcements/6-30-0-1785377094985/)
- Controls: exclude mismatched combinations, recreate with the same conditions, "Today's Look" daily card (5.33.0). Like/dislike feedback ("Make it more casual") — [5.33.0](https://www.acloset.app/announcements/5-33-0-update-news-674a9a/); [Acloset Magazine (Feb 2026)](https://www.acloset.app/magazine/ai-styling-has-come-this-far-how-digital-closet-ai-changes/). AI-generated outfits auto-delete after 90 days, with a warning 3 days before (5.30.0) — [5.30.0](https://www.acloset.app/announcements/5-30-0-update-news-8e05d0/). The screenshot shows a recommendation card with thumbs-down, Edit and Save, plus chat refinement "Make it more formal?" — [App Store screenshot 4](https://apps.apple.com/us/app/id1542311809)
- "Top picks from my closet" for an event (e.g. "Autumn Warm Minimalist Museum Date"): per-category picks with a rationale ("Simple loafers or boots") — [App Store screenshot 7](https://apps.apple.com/us/app/id1542311809)

**Indyx**
- "Fully customizable drag-and-drop outfit boards"; outfit tags; duplicate an outfit to iterate — [App Store](https://apps.apple.com/us/app/id1599179405); [Outfit ideas blog (2023, updated 2026)](https://www.myindyx.com/blog/how-to-generate-outfit-ideas). Saving inspiration to outfit boards is Insider-only — [Pricing table](https://www.myindyx.com/)
- No algorithmic generator: "No random outfits styled by robots or complete chance" — [App Store](https://apps.apple.com/us/app/id1599179405)
- Human styling services (booked in the Style tab; each stylist sets prices):
  - **The Feed**: subscription; "professionally styled outfits every week"; request types are submitted and one is fulfilled weekly. Launched 2 May 2024 at "starts at $35 per month"; the Aug 2024 FAQ says "starts at just $25 a month" — [Launch post](https://www.myindyx.com/blog/launching-the-feed); [Styling FAQ](https://www.myindyx.com/blog/how-digital-styling-works-frequently-asked-questions)
  - **The Lookbook**: 10 outfits from your closet with up to 6 new shopping suggestions, "Starting at $150" — [Indyx home](https://www.myindyx.com/)
  - **The Lookbook Mini**: 3 outfits from your existing closet, "Starting at $60" (launched 9.0.0, Dec 2025) — [Indyx home](https://www.myindyx.com/); [App Store 9.0.0](https://apps.apple.com/us/app/id1599179405)
  - **The Call**: live session with "the reference of your digital closet" — [Google Play](https://play.google.com/store/apps/details?id=com.indyx.android)
  - Minimum 25 indexed items, 50 recommended. Stylists see the full wardrobe (minus archived or "do not style"), outfits, location, selfies, quiz results and style words — [Styling FAQ](https://www.myindyx.com/blog/how-digital-styling-works-frequently-asked-questions)
- Screenshot "THE FEED" with tabs Feed / Requests / History and a stylist note under the board — [App Store screenshot 4](https://apps.apple.com/us/app/id1599179405)

**Stylebook**
- Canvas: pinch to resize, drag to move, tap to bring to front, double-tap menu (Remove from Look, Clothing Details, Restore To Original Size, Manual Resize slider, Send To Back, Open URL). Simultaneous pinch and drag restored in 10.1 — [Create an outfit post](https://stylebookapptips.wordpress.com/2015/03/17/how-to-create-an-outfit/); [10.1 notes](https://apps.apple.com/us/app/id335709058)
- Add inspiration photos to outfits as mini mood boards, Clone Outfit, notes, tags and seasons per look, search clothing while building, reorder outfit categories and thumbnails — [Features](https://www.stylebookapp.com/features.html)
- **Outfit Shuffle™**: choose 3–8 categories, and the app "randomly generate[s] ten outfit ideas"; open one to edit and save; "Shuffle Again" (v10). Single-item shuffle locks one item plus 2–7 categories. Tip: pick subfolders (e.g. sweaters) for seasonal results — [Outfit Shuffle post](https://stylebookapptips.wordpress.com/2016/01/28/outfit-shuffle/); [Single-item shuffle](https://stylebookapptips.wordpress.com/2019/10/11/how-to-outfit-shuffle-a-single-item/); [Stylebook 10](https://www.stylebookapp.com/stylebook10.html)
- A "Rewear These!" looks folder with a shuffle icon appears in screenshots — [App Store screenshot 8](https://apps.apple.com/us/app/id335709058)

### Inferences
- Rebuild priority: a solid canvas (layers, resize, duplicate, text) is table stakes. The real differentiator is the generator: random shuffle (cheap and delightful), constrained AI completion (Acloset), or human service (Indyx).
- Whering's W Pick has been "BETA" since at least 2023, and the 2026 "Planner suggests outfits" release suggests it was re-platformed into the Planner.

### Gaps
- What Whering's "Outfit Maker"/"Style Pass" does differently from W Pick, and what credits buy, is not publicly documented.
- Acloset's underlying LLM for chat and styling is not disclosed. Only Beautify Pro's image model (Gemini 2.5 Flash Image) is.

---

## 7. Planning: calendar, wear logging, history, packing/trips, reminders, widgets

### Takeaway
All four log wears through a calendar, and each one's CPW comes from those logs. Packing is outfit-driven everywhere: add outfits and the item list auto-generates. Acloset is the only one with home-screen widgets (Oct 2026). Stylebook has calendar streaks and reminders. Whering and Indyx have selfie-based logging, and Indyx now has a Today tab.

### Cited Findings

**Whering**
- Planner tab: weather + date header, a week strip, a toggle (bell), suitcase and calendar icons, and an outfit-selfie card with item thumbnails — [App Store screenshot 3](https://apps.apple.com/us/app/id1519461680). Options: "Add from wardrobe, Create new outfit, Show me outfit ideas or Add outfit photo". Multiple outfits per day. Schedule from the wardrobe via long-press → More → Schedule. An outfit is "marked as worn once the day of your choice has passed" — [FAQ](https://whering.co.uk/faq). The weather unit follows the account country — [FAQ](https://whering.co.uk/faq)
- Events: dates, name, timings, location, category, mood and notes, plus a toggle "Generate packing list" — [FAQ](https://whering.co.uk/faq); [Style Within Grace](https://stylewithingrace.com/whering-wardrobe-app-review/)
- Packing lists: add outfits per trip day → itemised list. "We'll also suggest anything you may have forgotten". Add single items. Generate a list from a Lookbook. Lists are private and not shareable — [FAQ](https://whering.co.uk/faq). Screenshot "Amsterdam Trip… 1/10 items packed", tabs Unpacked/Packed, search and filter, "Have you thought about adding these?" — [App Store screenshot 8](https://apps.apple.com/us/app/id1519461680)
- Outfit selfies: a dedicated Outfit Selfies tab (3.2.77, Dec 2025). Assigning items to selfies is "essential to keep your My Stats accurate" — [App Store version history](https://apps.apple.com/us/app/id1519461680); [FAQ](https://whering.co.uk/faq)
- Challenges Hub: 60-day Styling Challenge (launched 29 Jan 2025), tracked via a flame icon on the community home, with weekly outfit recaps to share — [Challenges Hub post](https://whering.co.uk/thoughts/challenges-hub-launched)
- "Smart reminders" for packing — [App Store](https://apps.apple.com/us/app/id1519461680)

**Acloset**
- OOTD calendar: month view with outfit thumbnails and a footer of OOTD count, Most worn and Expenses — [App Store screenshot 6](https://apps.apple.com/us/app/id1542311809). Weekly calendar, start-day setting, OOTD notifications, copy OOTD (5.26.0). Monthly stats on the calendar (3.8.0). Multiple OOTDs per date (6.35.0). Year and weekday shown (6.38.0) — [5.26.0](https://www.acloset.app/announcements/5-26-0-update-news-0ed421/); [3.8.0](https://www.acloset.app/announcements/3-8-0-update-news-19331c/); [6.35.0](https://www.acloset.app/announcements/6-35-0-1788338023609/)
- Packing (5.27.0, Nov 2024; revamped 6.38.0, Sep 2026): register destination and dates; outfits per day showing date, place and weather; tabs Outfit / Item / Checklist; non-closet essentials checklist; recommendations "using only your packed clothes or by setting the weather for your destination". Also pitched as a capsule tool ("select 30 pieces") — [5.27.0](https://www.acloset.app/announcements/5-27-0-update-news-bc8a8b/); [6.38.0](https://www.acloset.app/announcements/6-38-0-1789625052486/); [App Store screenshot 8](https://apps.apple.com/us/app/id1542311809)
- **Widgets** (6.41.1, Oct 2026): "Acloset Outfit Suggestion" (weather-based AI picks, Next button, switches to tomorrow at 6 PM) and "Acloset OOTD" (today's planned outfit, start logging from the widget). Widget buttons are unsupported on iOS 15–16 — [6.41.1](https://www.acloset.app/announcements/6-41-1-1791974915033/); [FAQ](https://www.acloset.app/support/)
- Monthly Wardrobe Report card and quiz, sent monthly (5.30.0) — [5.30.0](https://www.acloset.app/announcements/5-30-0-update-news-8e05d0/)

**Indyx**
- **Today tab** (9.5.0/9.5.1, Mar 2026): "Your outfit calendar is now front and center… Add individual items, full outfit boards, or selfies. Every wear you track feeds your analytics and cost-per-wear". Week start Sunday or Monday. Weekly calendar view (9.8.3) — [App Store version history](https://apps.apple.com/us/app/id1599179405)
- Selfies: OOTD selfies on the calendar ("mirror selfies" are Insider). Add selfies to outfits without logging, auto-populated selfies when logging, full-screen gallery (9.6.6) — [App Store](https://apps.apple.com/us/app/id1599179405); [Pricing table](https://www.myindyx.com/)
- Packing: "Add outfits to a Collection to automatically generate an itemized list" — [App Store](https://apps.apple.com/us/app/id1599179405)
- Notification center (9.7.0): "new items ready to style, outfits styled just for you, the latest features" — [App Store](https://apps.apple.com/us/app/id1599179405)

**Stylebook**
- Calendar: add looks or single items, multiple per day, searchable notes, "combine" items into a new outfit without double-counting, jump to a date (date picker), move or copy entries, alerts/reminders ("the app will remind you if you set up alerts") — [Calendar post](https://stylebookapptips.wordpress.com/2019/02/21/how-to-use-the-stylebook-calendar/); [Date picker](https://stylebookapptips.wordpress.com/2025/03/19/calendar-date-picker/). The calendar footer shows "Most Worn This Month" and an "X Day Streak – Continuous calendar recording" — [App Store screenshot 3](https://apps.apple.com/us/app/id335709058). The week can start on Monday (8.3) — [App Store version history](https://apps.apple.com/us/app/id335709058)
- Packing lists: header image, title, notes and tags; "+ Look" adds outfits and auto-proposes their items with checkboxes; "+ Clothing"; Text Items; Packing Checklist; "Shareable Infographic" (pin, email, print); archive of past lists; grouping by category (10.1) — [Packing post](https://stylebookapptips.wordpress.com/2015/03/12/create-a-packing-list/); [Infographic post](https://stylebookapptips.wordpress.com/2017/12/19/how-to-create-and-share-a-packing-list-infographic/); [App Store screenshot 5](https://apps.apple.com/us/app/id335709058); [10.1 notes](https://apps.apple.com/us/app/id335709058)

### Inferences
- Acloset's widgets plus a "tomorrow after 6 PM" rollover is the most habit-forming planning mechanic shipped. Indyx's Today tab and Whering's weather-aware planner (2026) are converging on the same "what am I wearing today" home surface.

### Gaps
- No evidence of Apple Watch apps, Siri Shortcuts or App Intents for any of the four. No widgets were found for Whering, Indyx or Stylebook (absence of evidence, not confirmed absence).
- Whering's notification types and the bell toggle's function on the Planner are undocumented.

---

## 8. Analytics: CPW, wear counts, value, colour/category breakdowns, sustainability, nudges

### Takeaway
All four compute CPW as price ÷ logged wears. Stylebook has the most granular stat lists (Top 100s, date ranges, "Never Used in an Outfit", "Not Logged on Calendar", size tracker). Whering adds sustainability "intake" metrics and a seasonal "Unpacked" recap. Indyx paywalls its dashboard (Composition / Usage / Investment, Material Insights). Acloset added Style Stats in 2025 and a monthly shareable report.

### Cited Findings

**Whering**
- My Stats ("Think Spotify wrapped but for your wardrobe"): % of wardrobe worn, CPW per item, total wardrobe value, items per category, most-worn colours, most/least-worn items, % of created outfits worn. Private to the user. Archived items excluded — [FAQ](https://whering.co.uk/faq). The screenshot shows "Wardrobe value ±£6960", "Wardrobe usage 75%", "85% 17/20 outfits worn", New items, New outfits and "What's in my wardrobe?" — [App Store screenshot 5](https://apps.apple.com/us/app/id1519461680)
- App Store list: Cost-Per-Wear ("True ROI"), Wear Rate, Intake Tracking (new vs pre-loved), Colour Palette ("go-to hues and gaps"), Closet Longevity ("item lifespan, track net additions") — [App Store](https://apps.apple.com/us/app/id1519461680). The 2023 review adds category breakdown, share of new/preloved/rental/handmade, popular pairings and top brands — [Style Within Grace](https://stylewithingrace.com/whering-wardrobe-app-review/)
- **Unpacked** (3.2.101, Apr 2026): "See what you actually wore, the outfits you repeated, and how your wardrobe has changed this season… From cost per wear to most-worn pieces" — [App Store version history](https://apps.apple.com/us/app/id1519461680)

**Acloset**
- Style Stats (5.29.0, Feb 2025): Worn History, CPW, Closet Value, attribute statistics (category, season, material…) and outfit statistics (season, occasion, style, score, hashtags). Tapping a bar shows the total price for that category (5.32.0) — [5.29.0](https://www.acloset.app/announcements/5-29-0-update-news-03a878/); [5.32.0](https://www.acloset.app/announcements/5-32-0-update-news-6d0c0a/)
- Calendar footer: OOTD count, most worn, Expenses — [App Store screenshot 6](https://apps.apple.com/us/app/id1542311809). Monthly Wardrobe Report card: most-worn items, favourite brands, preferred colours, saveable and shareable, with a quiz — [5.30.0](https://www.acloset.app/announcements/5-30-0-update-news-8e05d0/)

**Indyx**
- Free: sort by wears and CPW — [Pricing table](https://www.myindyx.com/). Insider: "analytics dashboard" — [Pricing table](https://www.myindyx.com/). The dashboard covers "wardrobe composition, usage, and investment" — [Insider launch](https://www.myindyx.com/blog/why-we-are-launching-insider). The screenshot shows "INDYX Insider" with tabs COMPOSITION / USAGE / INVESTMENT, UTILIZATION bars for 1M/3M/6M/1Y (e.g. 43% / 66% / 71% / 71%), category chips and "MOST WORN ITEMS… 49 wears" — [App Store screenshot 6](https://apps.apple.com/us/app/id1599179405)
- Material Insights (9.7.0, Jul 2026): natural / semi-synthetic / synthetic breakdown "covering not just composition, but how you wear and invest in each category". Most/Least Recently Worn views added to the Dashboard (9.8.3) — [App Store version history](https://apps.apple.com/us/app/id1599179405)
- "Interactive charts that nudge you to wear your underused pieces" — [App Store](https://apps.apple.com/us/app/id1599179405)

**Stylebook**
- Style Stats: Size Tracker (body measurements, sizes by type, brand exceptions), Clothing Stats (Item Count, Total Closet Value, Color bar, 100 Most Recently Added, Never Used in an Outfit, Not Logged on Calendar, Worn History Top 100 most/least, Cost per Wear Top 100 best/worst, Purchase Price Top 100, 100 Most Packed), Looks Stats (outfit count, average items per look, worn history). Totals exclude archived items — [App Store screenshots 6, 2 & iPad](https://apps.apple.com/us/app/id335709058); [Features](https://www.stylebookapp.com/features.html). Date-range filter for Worn History, CPW and Not Logged (default All Time) — [Style Stats Range post](https://stylebookapptips.wordpress.com/2018/01/12/style-stats-range/). The iPad shows a "View Closet By…" pie chart — [iPad screenshots](https://apps.apple.com/us/app/id335709058)
- CPW: "price… divided by the number of times it was worn", counted from calendar appearances. Shown on each item's notes ("$/wear") — [CPW post](https://stylebookapptips.wordpress.com/2015/01/30/cost-per-wear/)

### Inferences
- "Unworn" nudges are mostly passive lists, Stylebook's "Not Logged on Calendar" and Indyx's least-recently-worn sort among them. None of the sources describe a proactive push notification for unworn items except Acloset's monthly report and Indyx's notification center.

### Gaps
- No public documentation of Whering's "Closet Longevity" formula or Indyx's "utilization" definition.

---

## 9. AI features: what each actually does and its limits

### Takeaway
Acloset is the most AI-heavy: four persona chat stylists, colour/fit/rate analyses, Beautify (Gemini 2.5 Flash Image), Smart Detector, avatar virtual try-on with Basic/Pro tiers, and weather/time-aware recommendations, paid via "Beans". Whering is catching up after a July 2026 raise: Enhance, weather planner AI, and the W-Chat assistant, gallery scan and virtual try-on described in its Sept 2026 Terms. Indyx uses AI for capture and visualisation only (auto-tag, Enhance/Refine, Virtual Selfies) and keeps styling human. Stylebook uses on-device Apple Intelligence for image generation and AI background removal only.

### Cited Findings

**Whering**
- Auto-tagging and background removal at upload (processors include Pixyle.ai and remove.bg) — [Privacy Policy](https://whering.co/privacy-policy); [FAQ](https://whering.co.uk/faq)
- W Pick (beta, daily, weather + style history) and the 2026 Planner suggestions (wardrobe + weather + chosen location) — [FAQ](https://whering.co.uk/faq); [App Store](https://apps.apple.com/us/app/id1519461680)
- Enhance (image clean-up to product style, Apr 2026) — [App Store](https://apps.apple.com/us/app/id1519461680)
- **W-Chat**: "an AI-powered chat assistant… powered by a third-party AI provider"; an in-app EU AI Act disclosure; styling-only scope; conversations may be retained — [Terms §12 (17 Sep 2026)](https://whering.co/terms-conditions). No independent confirmation of its launch was found (search returned nothing).
- Virtual try-on (image and video), gallery scanning, location-based suggestions — [Terms §13](https://whering.co/terms-conditions). Described as planned and funded in July 2026 press — [Tech.eu](https://tech.eu/2026/07/07/whering-lands-7m-as-digital-wardrobe-platform-reaches-10m-users/)
- Training: user content is used to "train… models… including for the purpose of identifying brands and clothing items within uploaded images", with an in-app opt-out — [Terms §10](https://whering.co/terms-conditions)

**Acloset**
- **AI Chat Stylist** (6.0.0, May 2025) with four personas (6.1.0): Leo "Cheerful and upbeat", Suzy "Kind and gentle", Jax "Chic and stylish", Clara "Smart and confident". It gives styling advice for newly registered clothes and takes outfit requests in chat (6.2.0) — [6.0.0](https://www.acloset.app/announcements/6-0-0-update-news-b11b81/); [6.1.0](https://www.acloset.app/announcements/6-1-0-update-news-696503/); [6.2.0](https://www.acloset.app/announcements/6-2-0-update-news-768aaf/)
- **Find My Color** (face photo → skin tone and contrast → palette) and **Find My Fit** (full-body photo → body shape → fits and silhouettes) (6.4.0, Aug 2025). **Rate Style**. All launchable inside Style Chat, with history for 5 AI features (6.26.0) — [6.4.0](https://www.acloset.app/announcements/6-4-0-update-news-7a3c50/); [6.26.0](https://www.acloset.app/announcements/6-26-0-1782206588618/). Home "AI Stylist" grid: Outfit suggestion, Style chat, Find my color, Find my fit, Rate style, Try On — [App Store screenshot 1](https://apps.apple.com/us/app/id1542311809)
- **Virtual Try-On**: first launched as "dress up your selected model" (6.1.0). Personal avatar "with a single full-body photo"; try on closet items, wishlist items and recommended outfits; save to outfits or daily looks; download and share (6.28.x, Jul 2026). Better face/body likeness (6.32.0). **Basic** tier "more affordable and includes free uses refreshed every day" vs **Pro**, with "Try a better version" to rerun in Pro (6.34.0) — [6.1.0](https://www.acloset.app/announcements/6-1-0-update-news-696503/); [App Store 6.28.2](https://apps.apple.com/us/app/id1542311809); [6.32.0](https://www.acloset.app/announcements/6-32-0-1789024972907/); [6.34.0](https://www.acloset.app/announcements/6-34-0-1789025177704/). Try-On requires an account; "the number of available uses may vary" — [FAQ](https://www.acloset.app/support/). Beans consumed by failed try-ons during 12–18 Nov 2025 were refunded — [Try-on refund notice](https://www.acloset.app/announcements/try-on-ai-feature-error-11-12-11-18-refund-completion-8a1994/)
- Beautify/Beautify Pro (Gemini 2.5 Flash Image) and Smart Detector/Pro (see §3) — [Beautify Pro](https://www.acloset.app/announcements/meet-the-new-beautify-pro-39a609/)
- Currency: "These new AI features can be used by spending 'Beans,' an in-app currency available for purchase". Beans are also earned via missions — [Terms update (May 2025)](https://www.acloset.app/announcements/updates-to-acloset-s-service-terms-effective-may-19-2025-5e79c6/); [6.1.0](https://www.acloset.app/announcements/6-1-0-update-news-696503/)
- Data: "AI never use personal digital wardrobe data for learning" (Jul 2024) — [Secondhand closure / AI Styling notice](https://www.acloset.app/announcements/notice-of-secondhand-market-service-discontinuation-and-upco-8c4e21/). Gmail data is not used to train generalised models — [Gmail privacy notice](https://www.acloset.app/announcements/notice-upcoming-privacy-policy-update-for-gmail-integration-1777475073146/). The marketing article frames its styling as LLM-era ("rise of Large Language Models… Acloset's AI styling… is a prime example") — [Acloset Magazine (Feb 2026)](https://www.acloset.app/magazine/ai-styling-has-come-this-far-how-digital-closet-ai-changes/)
- Limits: recommendations need "enough clothes registered… balanced selection of tops, bottoms, outerwear"; accuracy depends on correct attributes — [FAQ](https://www.acloset.app/support/)

**Indyx**
- AI auto-tagging (category, subcategory, colour), Enhance and Refine (prompt-based edits of the enhanced flatlay) — [App Store](https://apps.apple.com/us/app/id1599179405)
- **Virtual Selfies** (9.6.6, Jul 2026): "preview any look on a real photo of you". Insider-only — [App Store](https://apps.apple.com/us/app/id1599179405); [Pricing table](https://www.myindyx.com/)
- Receipt parsing via an "AI parsing service"; Google API data is not used to train generalised models — [Privacy Policy](https://www.myindyx.com/privacy-policy)
- Explicitly no AI stylist: "never a robot", with human Feed, Lookbook and Call — [Indyx home](https://www.myindyx.com/)

**Stylebook**
- AI background removal (instant, batch) and Apple Intelligence text-to-image (iOS 18.2+, iPhone 15 Pro/16 or A17 Pro/M1 iPad) — [AI Image Generator post](https://stylebookapptips.wordpress.com/2025/03/13/al-image-generator/); [App Store](https://apps.apple.com/us/app/id335709058)
- Outfit Shuffle is random ("like playing the fashion lottery"), not AI — [Outfit Shuffle post](https://stylebookapptips.wordpress.com/2016/01/28/outfit-shuffle/)
- The App Store developer tagline field reads "Color analysis, virtual closet" — [App Store page data](https://apps.apple.com/us/app/id335709058). No colour-analysis feature is documented in Stylebook's features page or manual (possible mismatch).

### Inferences
- Gap analysis and shopping suggestions (e.g. "what's missing") are human-delivered at Indyx (stylist shopping picks). Acloset delivers them via shop recommendations by channel and age group (6.29.0), and Whering via its Colour Palette "gaps" stat. None documents an AI wardrobe-gap engine.
- Acloset's two-tier try-on (cheap daily-free Basic, paid Pro rerun) is a reusable pattern for controlling generative-image costs.

### Gaps
- Whering W-Chat, virtual try-on and gallery-scan launch status (live vs planned) is unverified.
- Acloset's per-feature Bean prices (Beautify Pro, Try-On Pro, Smart Detector Pro) are not published.

---

## 10. Social and community

### Takeaway
Whering is the most social: a public feed, following, "style submissions" (styling another user from their wardrobe), challenges and contact sync. Acloset relaunched its Community in Oct 2026 with likes, comments, boards and "search with my items". Indyx has Open Closets and social styling, with "get styled by friends" Insider-gated. Stylebook has no community, only export and share.

### Cited Findings

**Whering**
- Community tab feed tabs: For you / Following / Outfit selfies / Style submissions. Cards "Styled by Beth for Ash", Follow, emoji reactions ("Tap to react"). Home quick actions: Upload, Create, Plan, Analyse, Read — [App Store screenshot 6](https://apps.apple.com/us/app/id1519461680)
- Style others: "Press the 'Style [username]' button"; you can only use their wardrobe; unlimited submissions; "Send to [username]"; submissions appear on both profiles — [FAQ](https://whering.co.uk/faq). Add items from friends' closets; save friends' wishlist items; pin outfits to Moodboards — [App Store](https://apps.apple.com/us/app/id1519461680); [FAQ](https://whering.co.uk/faq)
- Privacy model: public or private profile (private by default per Terms); per-item eye toggle; private Wishlists and Moodboards; tags and stats always private — [FAQ](https://whering.co.uk/faq); [Terms §7.2](https://whering.co/terms-conditions). A "Team Whering" public sandbox account exists — [FAQ](https://whering.co.uk/faq). Whering Social is free — [FAQ](https://whering.co.uk/faq)
- The Terms also describe user-to-user chat and "Brand Wardrobes" (labelled sponsored collections) — [Terms §§16–17](https://whering.co/terms-conditions)

**Acloset**
- Explore/Community: Feed renamed Explore (6.6.0) with item-based outfit search and Save → Boards. 6.40.0 (Oct 2026): filters in New/Following, likes, comments and bookmarks, "Search with my items", "Trending outfits" and "Trending users", search tabs Outfits/Users/Items, and public vs personal profile views — [6.6.0](https://www.acloset.app/announcements/6-6-0-update-news-c89e12/); [App Store 6.40.0](https://apps.apple.com/us/app/id1542311809)
- Visibility: only public outfits appear, "OOTDs are never shared to the Community", private account mode blocks posting, report and block. Browsing works without an account — [FAQ](https://www.acloset.app/support/)
- **Share Closet** (formerly "Ask Friend"): share items or an outfit via link or messenger, and friends' suggested outfits can be saved (6.4.0/6.9.0). Friend invitations grant 5 closet slots each, up to 5 friends — [6.4.0](https://www.acloset.app/announcements/6-4-0-update-news-7a3c50/); [6.9.0](https://www.acloset.app/announcements/6-9-0-update-news-bb3ff9/); [FAQ](https://www.acloset.app/support/)

**Indyx**
- Closets are private by default and can be opened to friends and other users. **Open Closets** has Featured and Pinned lists (users with location and three style words, e.g. "Sculptural · Ease · Modern") — [App Store](https://apps.apple.com/us/app/id1599179405); [App Store screenshot 8](https://apps.apple.com/us/app/id1599179405)
- Social styling (May 2025): "style your friends (and get them to style you!)", completing the "trifecta" of self, friends and expert styling — [Blog (May 2025)](https://www.myindyx.com/blog/are-we-cannibalizing-expert-styling). "Get styled by friends and other Indyx users" is Insider-only, while "Share your closet with friends" is free — [Pricing table](https://www.myindyx.com/)
- Insider community: a private Slack channel and events — [Insider launch](https://www.myindyx.com/blog/why-we-are-launching-insider)

**Stylebook**
- Share outfits, clothing and inspiration via email, text, Facebook, Twitter, Pinterest and Instagram (v10), "Save as Square Image", and Wifi Share to another device. No feed or following — [Features](https://www.stylebookapp.com/features.html); [Stylebook 10](https://www.stylebookapp.com/stylebook10.html)

### Inferences
- "Style a friend from their real closet" is the shared social primitive across Whering (free), Acloset (free) and Indyx (Insider-gated). It doubles as a top-of-funnel for paid styling at Indyx.

### Gaps
- Moderation tooling beyond report and block (Acloset) and the Terms (Whering) is not documented.

---

## 11. Commerce: shopping links, resale, sell flows, brand partnerships

### Takeaway
None of the four currently runs an in-app resale marketplace. Acloset closed its Preloved market in Aug 2024. Whering has partner services (repairs, laundry, Thrift+), a planned eBay listing integration after eBay's investment, and sponsored Brand Wardrobes. Indyx earns from stylist services and ran an NYC "Closet Sale" pop-up checkout. Stylebook has a shopping browser with bookmarks.

### Cited Findings

**Whering**
- Wishlist "View on Web" to retailers. Shop tab (cart icon in the tab bar). "Whering Marketplace" heart-to-wishlist — [FAQ](https://whering.co.uk/faq); [App Store screenshots](https://apps.apple.com/us/app/id1519461680)
- Circular services "accessible through the Whering app": Sojo and The Seam (alterations and repairs), Oxwash and Kair (laundry), Thrift+ (donate or sell via bag) — [Services page](https://whering.co.uk/services). Rental is UK-only ("Can I rent clothes… outside of the UK? Not right now") — [FAQ](https://whering.co.uk/faq)
- eBay resale: "may allow you to list wardrobe items for resale through eBay… Whering facilitates the creation of listings only" — [Terms §15.1](https://whering.co/terms-conditions). Press frames resale integrations as planned use of the July 2026 funds — [TheIndustry.fashion](https://www.theindustry.fashion/whering-secures-7m-to-expand-ai-powered-wardrobe-app/)
- Brand Wardrobes = sponsored commercial-partner collections, labelled per ASA/CAP — [Terms §16](https://whering.co/terms-conditions). A "for business" page exists — [whering.co.uk/for-business](https://whering.co.uk/for-business)

**Acloset**
- History: Shop tab with product recommendations (2022, only US, UK, FR, ES, DE, KR), discontinued in 3.10.0 (Dec 2022) — [3.7.0](https://www.acloset.app/announcements/3-7-0-update-news-68cfad/); [3.10.0 preview](https://www.acloset.app/announcements/3-10-0-update-preview-bb8852/). Preloved buy/sell market announced Jan 2023 and closed 26 Aug 2024 — [Preloved](https://www.acloset.app/announcements/introducing-preloved-feature-for-more-sustainable-fashion-6d006a/); [Closure notice](https://www.acloset.app/announcements/notice-of-secondhand-market-service-discontinuation-and-upco-8c4e21/)
- Current: in-app ads since Jan 2024 — [Freemium announcement](https://www.acloset.app/announcements/acloset-adopts-freemium-pricing-to-ensure-long-term-stabilit-848b71/); [App Store age-rating note "Contains Advertising"](https://apps.apple.com/us/app/id1542311809). Shop recommendations "based on your preferred shopping channels and age group" on home and import screens (6.29.0). The iPad home shows an AI Stylist "Shopping" tile — [App Store 6.29.0](https://apps.apple.com/us/app/id1542311809); [iPad screenshot 1](https://apps.apple.com/us/app/id1542311809). The Terms still contain seller and tax clauses from the marketplace era — [Acloset Terms](https://www.acloset.app/terms/)

**Indyx**
- Stylists may recommend up to 6 new items per Lookbook: "We don't currently make a dime off any items you buy, but your stylist might make their own commission" — [Styling FAQ (Aug 2024)](https://www.myindyx.com/blog/how-digital-styling-works-frequently-asked-questions)
- Resale: the 2022 Catalog post says users can "create a resale listing directly from your digital wardrobe" — [Pro Cataloging (2022)](https://www.myindyx.com/blog/what-to-expect-during-a-pro-indyx-wardrobe-cataloging-session). In 2024 Campbell said users will "soon be able to sell" — [Inside Retail (Apr 2024)](https://insideretail.com.au/sectors/meet-indyx-the-personal-style-app-that-is-solving-fashion-sustainability-issue-202404). 8.9.11 (Nov 2025) "optimized the check out experience for our very first Closet Sale in NYC" — [App Store](https://apps.apple.com/us/app/id1599179405). Gift cards: [gift.myindyx.com](https://gift.myindyx.com/) (linked from [home](https://www.myindyx.com/))

**Stylebook**
- Shop: bookmark stores, clipper to save product images into the closet or inspiration, product wishlist and text wishlist, "Shop from Outfits/Closet" back to the product URL — [Features](https://www.stylebookapp.com/features.html). An affiliate page exists on the site — [Stylebook sitemap](https://www.stylebookapp.com/sitemap.xml)

### Inferences
- The category has repeatedly tried and dropped commerce (Acloset's shop and market). Whering's investor mix (eBay) makes resale the likeliest next live commerce integration among the four.

### Gaps
- Whether Whering's eBay listing flow is live: the Terms describe it, press says "planned", and no in-app evidence was found.
- What Indyx's "Closet Sale" is in-app (marketplace vs event checkout) is undocumented.

---

## 12. Pricing and tier gating (only as it gates features)

### Takeaway
Gating is the clearest differentiator. **Stylebook**: $4.99 once, nothing gated. **Whering**: free and unlimited; a closed "Supporter Club" and credits/"Outfit Maker" IAPs exist, but what they gate is undocumented. **Indyx**: unlimited free items, outfits and calendar; Insider ($12.99/mo or $74.99/yr) gates analytics, Enhance volume, Super HD, display customisation, inspiration, selfies and social styling. **Acloset**: everything free up to 100 items (archive included), with Basic 300, Premium 1,000 and Expert unlimited; AI generative features cost "Beans".

### Cited Findings

**Whering**
- "Whering is completely free to use"; no item or outfit caps — [FAQ](https://whering.co.uk/faq). Supporter Club: "currently closed to new members… Historical members… early access to features, exclusive feedback forums, and a dedicated supporter badge" — [FAQ](https://whering.co.uk/faq)
- US IAP list: 10/50/100 Credits ($2.99/$7.99/$12.99); Supporter core $0.99, basic $9.99, extra $14.99, speed $29.99, gold standard $49.99; Outfit Maker $4.99 — [App Store](https://apps.apple.com/us/app/id1519461680). GB equivalents: 10 Credits £1.99, 50 £5.99, 100 £9.99, Outfit Maker £4.99 — [GB App Store](https://apps.apple.com/gb/app/id1519461680)

**Acloset**
- Free: "all features for free, for up to 100 items" — [App Store](https://apps.apple.com/us/app/id1542311809). Limits: Free 100, Basic 300, Premium 1,000 (each "+ event-added slots"), Expert unlimited, all including archived items. At the limit, adding items is blocked but Ideas and OOTDs continue. On lapse or downgrade over the limit, adding items, ideas and OOTDs is restricted, and view, edit and delete remain — [Acloset help (Notion)](https://marmalade-polonium-d4f.notion.site/What-happens-if-I-reach-my-item-limit-or-downgrade-my-plan-197c1f502e3e8063afb2d4693b1d8137); [Is Acloset free? (Notion)](https://marmalade-polonium-d4f.notion.site/Is-using-Acloset-free-ef4ca5c628ad485cb3fa32293fb98218)
- Paid plans add ad-free use and space — [Freemium announcement (Dec 2023)](https://www.acloset.app/announcements/acloset-adopts-freemium-pricing-to-ensure-long-term-stabilit-848b71/). A cheaper monthly plan with ads below Basic was added in 6.2.0 (Jun 2025) — [6.2.0](https://www.acloset.app/announcements/6-2-0-update-news-768aaf/). Regional pricing (e.g. Turkey and India "about half or less") and no lifetime option — [Freemium FAQ (Jan 2024)](https://www.acloset.app/announcements/freemium-pricing-faq-70296b/)
- US IAPs: Basic $3.99/mo or $27.99/yr; Premium $9.99/mo or $59.99/yr; Expert $24.99/mo or $147.99/yr; Beans 100 $1.99, 200 $3.99, 210 (bonus) $3.99, 1200 (bonus) $19.99 — [App Store](https://apps.apple.com/us/app/id1542311809)
- Referral: +5 closet slots per friend who signs up and adds items, up to 5 friends — [FAQ](https://www.acloset.app/support/). Beans are spent on generative features: the May 2025 terms name AI Chat, Beautify and Smart Detector, and Try-On consumes Beans (Nov 2025 refund notice). Since 6.9.0 the basic Smart Detector is free and "Pro" is the upgraded version; Try-On Basic includes daily free uses — [Terms update](https://www.acloset.app/announcements/updates-to-acloset-s-service-terms-effective-may-19-2025-5e79c6/); [Try-On refund notice (19 Nov 2025)](https://www.acloset.app/announcements/try-on-ai-feature-error-11-12-11-18-refund-completion-8a1994/); [6.9.0](https://www.acloset.app/announcements/6-9-0-update-news-bb3ff9/); [6.34.0](https://www.acloset.app/announcements/6-34-0-1789025177704/)

**Indyx**
- Free includes unlimited items with background removal, unlimited outfits, packing lists, wishlists and capsules, calendar, sort by wear and CPW, 1:1 styling access and closet sharing. **Insider only**: get styled by friends and other users, analytics dashboard, unlimited enhanced flatlays, Super HD images, custom wardrobe display, inspiration on outfit boards, virtual and mirror selfies, priority event access — [Pricing table on Indyx home](https://www.myindyx.com/)
- US IAPs: Monthly Membership $12.99, Annual Membership $74.99 — [App Store](https://apps.apple.com/us/app/id1599179405). The Fair Use Policy covers "unusually high storage, processing, or activity" — [Terms §7](https://www.myindyx.com/terms-and-conditions). Insider launched with 10% off Lookbooks — [Insider launch (Sep 2024)](https://www.myindyx.com/blog/why-we-are-launching-insider)
- Services are priced per stylist: Feed from $25–35/mo, Lookbook from $150, Lookbook Mini from $60, Call "low $100s". Catalog overage is $2 per item above 100 — [Indyx home](https://www.myindyx.com/); [Styling FAQ](https://www.myindyx.com/blog/how-digital-styling-works-frequently-asked-questions); [Catalog blog](https://www.myindyx.com/blog/what-to-expect-during-a-pro-indyx-wardrobe-cataloging-session)

**Stylebook**
- "a one-time charge ($4.99 in the US) - not a subscription"; "NO LIMITS: add unlimited clothing, accessories, & inspirations"; no IAPs; Family Sharing — [FAQ](https://www.stylebookapp.com/faq.html); [App Store](https://apps.apple.com/us/app/id335709058). AI image generation needs Apple Intelligence hardware (a hardware gate, not a paywall) — [AI Image Generator post](https://stylebookapptips.wordpress.com/2025/03/13/al-image-generator/)

### Inferences
- Acloset's cap counts archived items, which penalises long-time users who keep history. Indyx's founder publicly argued against this model ("the average wardrobe size is about 175 items… even if the paywall kicks in after your 100th item… why start?") — [Insider launch](https://www.myindyx.com/blog/why-we-are-launching-insider)

### Gaps
- Whering's "Style Pass" price and contents, and what "Credits" are spent on, are undocumented publicly.
- Acloset's ad-supported low-cost plan price and exact per-plan differences beyond item limits and ads are not published.

---

## 13. Privacy: App Store nutrition labels, data export, account deletion

### Takeaway
Stylebook is the most private: no account, data on device and in iCloud, and only "Data Not Linked to You: Product Interaction" on its label. Indyx declares no tracking but links contact info to identity. Whering and Acloset declare tracking (Whering: User ID; Acloset: email, device ID, product interaction). Only Indyx offers an export (CSV, by email, for a fee). Whering and Acloset say export isn't possible. All three account-based apps allow in-app deletion.

### Cited Findings

**Whering**
- Label: *Used to Track You* — Identifiers (User ID). *Linked to You* — Contact Info (Phone Number) for developer advertising; Usage Data (Product Interaction) for analytics, personalisation and app functionality; Contact Info (Name) for personalisation; Email for app functionality; Diagnostics (Crash Data). *Not Linked* — User ID and Other Usage Data — [App Store privacy](https://apps.apple.com/us/app/id1519461680)
- Play data safety: shares Photos and videos (app functionality); collects device IDs, app interactions, in-app messages, crash logs, contacts (optional), name/email/phone, precise location, photos/videos — [Play data safety](https://play.google.com/store/apps/datasafety?id=com.whering.app)
- Export: "Right now it's not possible to export the data from items you've uploaded" — [FAQ](https://whering.co.uk/faq). The policy nonetheless grants GDPR portability rights — [Privacy Policy](https://whering.co/privacy-policy). Deletion: in-app via Settings>Personal information>"Delete your account". Data is kept 3 months after deletion "with your consent" unless immediate deletion is requested — [FAQ](https://whering.co.uk/faq); [Privacy Policy](https://whering.co/privacy-policy). Hosting: Google Cloud in EU/UK regions — [Privacy Policy](https://whering.co/privacy-policy)

**Acloset**
- Label: *Used to Track You* — Contact Info (Email), Identifiers (Device ID), Usage Data (Product Interaction). *Linked* — Product Interaction (analytics), Coarse Location (app functionality). *Not Linked* — Device ID (third-party advertising, analytics, app functionality), crash and performance data, email, Photos or Videos, Customer Support — [App Store privacy](https://apps.apple.com/us/app/id1542311809)
- Play: shares location (approximate for functionality, precise for advertising/marketing); collects purchase history, photos and IDs — [Play data safety](https://play.google.com/store/apps/datasafety?id=com.looko.acloset)
- Export: "we are unable to accommodate this request at the moment" (Jan 2024) — [Subscription FAQ](https://www.acloset.app/announcements/faqs-regarding-subscriptions-8bcde6/). Deletion: Settings > Account > Delete Account, and subscriptions must be cancelled separately — [FAQ](https://www.acloset.app/support/). The privacy policy is effective 29 Apr 2026 and covers AI chat content and advertising partners — [Acloset Privacy](https://www.acloset.app/privacy/)

**Indyx**
- Label: no "Used to Track You" section. *Linked* — Contact Info (physical address, email, name, phone) for developer marketing, analytics, personalisation and functionality; Identifiers (User ID, Device ID); Usage Data; Financial Info (Payment Info). *Not Linked* — Other Diagnostic Data — [App Store privacy](https://apps.apple.com/us/app/id1599179405). Play: "No data shared with third parties" — [Play data safety](https://play.google.com/store/apps/datasafety?id=com.indyx.android)
- Export: "email us… we can prepare a secure export for you in CSV format… a small fee since this is a manual process" — [Support FAQ](https://www.myindyx.com/contact-us). Deletion: Profile → Account Settings → Permanently Delete Account, processed within 30 days — [Privacy Policy](https://www.myindyx.com/privacy-policy). "We don't sell it or share it with third parties" (anonymised aggregate insights possible, e.g. the 2025 wardrobe report) — [Support FAQ](https://www.myindyx.com/contact-us)

**Stylebook**
- Label: *Data Not Linked to You* — Usage Data (Product Interaction) for analytics only — [App Store privacy](https://apps.apple.com/us/app/id335709058)
- "Your data is stored on your device and in iCloud if enabled"; "We can't see or access your closet data, and we don't sell any information" — [FAQ](https://www.stylebookapp.com/faq.html). Backup and restore via iCloud Backup and Stylebook Sync (restore after app deletion) — [FAQ](https://www.stylebookapp.com/faq.html); [Stylebook 10](https://www.stylebookapp.com/stylebook10.html). Items can be saved to the camera roll in bulk, but there is no data (CSV) export — [Multi-select post](https://stylebookapptips.wordpress.com/2015/08/03/multi-select-and-bulk-actions/)

### Inferences
- Whering's Terms grant broad model-training rights over uploads, with an opt-out. Indyx and Acloset both explicitly exclude Gmail and Google data from model training. This difference may matter to privacy-sensitive users.

### Gaps
- Stylebook "account deletion" is not applicable (no account), and deleting the app plus iCloud data is the implicit path. This is not documented as a procedure.

---

## 14. Feature timeline, 2023 → Oct 2026

### Takeaway
2025–26 was an AI and import arms race. Acloset shipped near-weekly (AI stylist personas, Beautify/Gemini, try-on avatar, Gmail and store imports, packing, widgets). Indyx built Insider, social styling, the Today tab, Enhance and Virtual Selfies. Whering shipped selfies, Enhance, Unpacked and a weather planner, then raised $7M. Stylebook shipped one big release (v10, Mar 2025) and then a point release (Jun 2025).

### Cited Findings

**Whering** (source: [App Store version history](https://apps.apple.com/us/app/id1519461680) unless noted)
- 2023: W Pick described as beta — [Style Within Grace (Nov 2023)](https://stylewithingrace.com/whering-wardrobe-app-review/)
- 29 Jan 2025: Challenges Hub with the 60-day Styling Challenge — [Blog](https://whering.co.uk/thoughts/challenges-hub-launched)
- Sep–Nov 2025 (3.2.51–3.2.70): bug fixes and performance only
- 8 Dec 2025 (3.2.77): "A home for your outfit selfies"
- 8 Apr 2026 (3.2.101): **Unpacked** seasonal wardrobe recap + **Enhance** product-style photos
- 9 Jun → 7 Oct 2026 (3.3.35 → 3.4.147): Planner suggests outfits from wardrobe + weather, with location change for trips and events
- 7 Jul 2026: $7M seed (eBay Ventures, Google AI Futures Fund), funding image enhancement, gallery scanning, virtual try-on and resale — [Tech.eu](https://tech.eu/2026/07/07/whering-lands-7m-as-digital-wardrobe-platform-reaches-10m-users/)
- 7 Sep 2026: redesigned Chrome extension v3.0.4 — [Chrome Web Store](https://chromewebstore.google.com/detail/whering/kjdgcemdlnbdohbcmnamlnnfkjoijfol)
- 17 Sep 2026: Terms add W-Chat, gallery scanning, image/video try-on, eBay resale, retailer-account import, Brand Wardrobes, Stylist Chat — [Terms](https://whering.co/terms-conditions)

**Acloset** (announcement dates from [acloset.app/announcements](https://www.acloset.app/announcements/); App Store dates in brackets)
- Jan 2023: Preloved market announced — [link](https://www.acloset.app/announcements/introducing-preloved-feature-for-more-sustainable-fashion-6d006a/)
- Dec 2023–Feb 2024: freemium (100-item cap, plans, ads), then detail-page restrictions rolled back after backlash — [link](https://www.acloset.app/announcements/acloset-adopts-freemium-pricing-to-ensure-long-term-stabilit-848b71/); [link](https://www.acloset.app/announcements/important-update-removal-of-access-restrictions-to-detail-pa-f884fe/)
- 26 Aug 2024: Preloved market closed. Oct 2024 (5.24.0): AI Styling + Quick Closet Review — [link](https://www.acloset.app/announcements/notice-of-secondhand-market-service-discontinuation-and-upco-8c4e21/); [link](https://www.acloset.app/announcements/5-24-0-update-news-623ca1/)
- Nov 2024: 5.26.0 (attributes, idea books, weekly calendar, ratings), 5.27.0 (Packing) — [link](https://www.acloset.app/announcements/5-26-0-update-news-0ed421/); [link](https://www.acloset.app/announcements/5-27-0-update-news-bc8a8b/)
- Jan–Mar 2025: Monthly Wardrobe Report and referral slots (5.30.0), Style Stats (5.29.0), Today's Look and exclusions (5.33.0), Hijab/Traditional categories and Labels (5.31.0), AI Scan and pin-shuffle (5.32.0). Announcement dates are not in version order on the site — [5.29.0](https://www.acloset.app/announcements/5-29-0-update-news-03a878/); [5.30.0](https://www.acloset.app/announcements/5-30-0-update-news-8e05d0/)
- 26 May 2025 (6.0.0): Beautify, Smart Detector, AI Chat Stylist, Clothes Library, Classic vs New mode, Beans — [link](https://www.acloset.app/announcements/6-0-0-update-news-b11b81/)
- Jun–Oct 2025: 4 AI stylist personas + model try-on + missions (6.1.0); ad-supported cheap plan + 3-day challenge (6.2.0); colour and body-shape analysis + Ask Friend (6.4.0); Explore + Boards (6.6.0); situation/free-text recommendations (6.8.0); Beautify Pro on Gemini 2.5 Flash Image (Sep 2025); Wishlist, Smart Detector Pro, Share Closet (6.9.0); product-code import (6.10.0)
- Mar–Jun 2026: email-forward import (6.19.0); magazine and accessory-anchored recommendations (6.20.0); store order-history import + 4-item auto-split (6.21.0); Chrome extension bulk add (6.25.0); AI analyses inside Style Chat (6.26.0); Gmail integration (6.27.0)
- Jul–Oct 2026: avatar try-on [6.28.2, 22 Jul]; regional store navigation + shop recommendations (6.29.0); time/day-aware recommendations + completion helper (6.30.0); Beautify for jewellery + try-on likeness (6.32.0); 7-day forecast picking + Try-On Basic/Pro (6.34.0); sorting/columns + multiple OOTDs (6.35.0); new Packing + custom temperature (6.38.0); import guide (6.39.0); Community relaunch [6.40.0, 6 Oct]; Dark mode (6.41.0); widgets (6.41.1) — [App Store version history](https://apps.apple.com/us/app/id1542311809)

**Indyx** (App Store version history unless noted: [link](https://apps.apple.com/us/app/id1599179405))
- 2 May 2024: The Feed launched — [Blog](https://www.myindyx.com/blog/launching-the-feed). 4 Sep 2024: Indyx Insider launched — [Blog](https://www.myindyx.com/blog/why-we-are-launching-insider)
- 22 Apr 2025: State of Our Wardrobes report. ~11 May 2025: social styling (friends) — [Report](https://www.myindyx.com/wardrobes-report); [Blog](https://www.myindyx.com/blog/are-we-cannibalizing-expert-styling)
- 5 Nov 2025 (8.9.11): better auto-tagging and Closet Sale NYC checkout. 5 Dec 2025 (9.0.0): Lookbook Mini and refreshed Style tab. 28 Dec 2025 (9.1.0): "Your digital closet builds itself"
- 18 Feb 2026 (9.3.0): receipt forwarding + paste link. 17–30 Mar 2026 (9.5.x): Today tab and week-start setting. 22 May 2026 (9.6.3): Enhance
- 8 Jul 2026 (9.6.6): Virtual Selfies. 30 Jul 2026 (9.7.0): Material Insights + notification center. 26 Sep 2026 (9.8.3): Most/Least Recently Worn, weekly view, Refine. 20 Oct 2026 (9.8.5): memory and scroll performance for large closets

**Stylebook** ([App Store version history](https://apps.apple.com/us/app/id335709058))
- 23 Dec 2023 (8.3): Monday week start, performance; "Stylebook 9… coming soon" (it never shipped as 9)
- 10 Mar 2025 (10.0): 30+ features: iCloud sync, iPad landscape, Archive, Instagram share, AI background removal, Apple Intelligence image generation, Clean Up, multiple import, drag and drop, uniform cropping, keep-original-background, reverse eraser, web clipper browser, wardrobe-basics catalog, extra images, dark mode, Top 100 stats, jump-to-date, Shuffle Again, new packing UI
- 12 and 19 Mar 2025 (10.0.1/10.0.2): crash and CPW-screen fixes. 15 Jun 2025 (10.1): light/dark/system setting, exclude archived from colour stats and search, pinch+drag restored, packing lists grouped by category, Print restored
- No releases from 16 Jun 2025 to 25 Oct 2026

### Inferences
- Release cadence in 2026: Acloset weekly, Indyx about 2–3 per month, Whering about monthly with reused release notes, Stylebook none.

### Gaps
- Whering and Indyx release notes before Sep/Nov 2025 aren't in the App Store's 25-version window, and no third-party archive was consulted. The 2023–mid-2025 feature history for those two is partial.

---

## 15. UI: main screens, navigation, visual style (from App Store screenshots)

### Takeaway
Whering is loud and Gen-Z (acid lime, cyan, magenta, 3D chrome "W" props) with a 5-tab bar. Acloset is clean iOS-native white/grey with a centre "+" FAB. Indyx is editorial luxury (cream, forest green, serif wordmark, vertical script captions). Stylebook is a 2010s-iOS utility (lavender line icons, home grid of 9 tiles, a 5-tab bar, iPad sidebar).

### Cited Findings
(All observations below are from the US App Store screenshots downloaded via `screenshotUrls`/`ipadScreenshotUrls`.)

**Whering** ([App Store](https://apps.apple.com/us/app/id1519461680); 10 iPhone screenshots, no iPad. Dated content "13 Jun 2024" suggests mid-2024 captures)
- Tab bar (5): Community (globe), Shop (cart), Planner (calendar), Styling (sparkle), Wardrobe (wardrobe icon).
- Screens: Styling with segmented tabs Dress me / Canvas / Moodboards; Wardrobe profile (avatar, header image, counts "145 Items · 23 Outfits · 15 Lookbooks", category chips, "Archived (3)", search, ♡/eye/filter icons, blue "+" FAB); Planner (weather, week strip, selfie card); My Stats (gradient header "Your wardrobe stats at a glance"); Community home ("Hey, Beth!" with Upload/Create/Plan/Analyse/Read and feed tabs); Packing list; My Wishlist (folders + items with "Go to website"); daily outfit swipe cards (Dismiss/Schedule).
- Style: white UI cards on saturated marketing backgrounds (acid lime, cyan, lilac, magenta), 3D lettering props, sticker-like icons; lime "Save" buttons.

**Acloset** ([App Store](https://apps.apple.com/us/app/id1542311809); 9 iPhone + 8 iPad. Dates in screenshots are 2025)
- Tab bar: Home, Closet, centre black "+" button, Outfit, Explore.
- Home: "Hello, Olivia" with calendar/bell/avatar; "AI Stylist" tiles (Outfit suggestion, Style chat, Find my color, Find my fit, Rate style, Try On); "Casual Daily Look for Cold Winter" card with date, city and temperature; "Recently added items" carousel. The iPad home adds "Popular features" (Add items, Create outfit, Calendar, Beautify, Style stats) and a clipboard banner ("99 images saved / Go to clipboard").
- Closet: "Closet ▾" switcher, mission/reward banner ("Cool, don't forget your reward!"), shortcut row (Style stats, Wishlist, Beautify, Share Closet, Closet Review), closets as 2×2 thumbnail cards plus "Create closet".
- Other screens: store import list; AI stylist recommendation (stylist picker "Leo", weather, rationale text, chat input); canvas editor with floating toolbar; month calendar with OOTD / Most worn / Expenses; trip detail with a hero photo; Explore with NEW / Following and search or camera.
- Style: neutral white/light-grey, black primary buttons, SF-style typography, generous rounded cards. Dark mode since Oct 2026 — [6.41.0](https://www.acloset.app/announcements/6-41-0-1791464991179/)

**Indyx** ([App Store](https://apps.apple.com/us/app/id1599179405); 9 iPhone + 8 iPad. In-app dates are Feb 2024, so screens predate the 2026 Today tab)
- Tab bar in screenshots: Explore, Connect, centre "+" (dark green), Style (hanger), Profile.
- Profile header "INDYX" wordmark; segmented ITEMS (558) / OUTFITS (390) / COLLECTIONS (10); search; dense 3-column grid with brand captions (Tibi, Frankie Shop, The Row…); calendar month view filled with outfit boards; Insider analytics; Collections with coloured folder tiles; "THE FEED" with Feed/Requests/History; "OPEN CLOSETS" Featured/Pinned list.
- Style: cream background, dark-green accents, all-caps small labels, a serif wordmark, fashion-editorial photography and vertical script captions ("Digitize", "Save", "Style", "Track", "Analyze", "Organize", "Connect"). One iPad caption still promises "resell effortlessly".

**Stylebook** ([App Store](https://apps.apple.com/us/app/id335709058); 10 iPhone + 9 iPad. The calendar screenshot shows July 2026)
- Tab bar: Home, Closet, Looks, Inspiration, Calendar. The Home grid has 9 tiles: Closet, Looks, Inspiration, Calendar, Packing, Style Stats, Shop, Style Expert, Sync, with + (add), sync and Help in the nav bar.
- Look editor: white canvas with a bottom toolbar (edit, folder, hanger, add-to-calendar, delete, share). Closet: list of folders and subcategories with thumbnails. Style Stats: grouped iOS table cells. Packing List detail: hero photo, Clothing/Looks/Text Items, Notes and Tags.
- iPad: landscape split view with a persistent sidebar (Closet, Looks, Inspiration, Calendar, Packing, Style Stats, Shop, Style Expert, Sync, Help & Tutorials).
- Style: system iOS look with lavender/periwinkle line-art icons and plain white backgrounds. Dark mode (v10) and a light/dark/system toggle (10.1) — [App Store 10.1](https://apps.apple.com/us/app/id335709058)

### Inferences
- Navigation archetypes: Whering is social-first (feed as tab 1), Acloset is AI-home-first, Indyx is profile/closet-first with services in a Style tab, and Stylebook is hub-and-spoke (Home grid).

### Gaps
- Indyx's current (post-Mar 2026) tab bar with the Today tab is not shown in public screenshots.
- Whering's App Store screenshots are about 2024 and may not reflect the 2026 planner and selfie UI.

---

## 16. Strengths, weaknesses, distinctive mechanics, and who each is best for

### Takeaway
**Whering** is best for social, free and fun styling (Dress Me shuffle, styling friends, challenges), but its AI and data tooling has lagged. **Acloset** is best for the most automated capture and AI styling, at the cost of a 100-item cap, Beans and ads. **Indyx** is best for people who want a beautiful free closet plus paid human help (stylists, Archivists). **Stylebook** is best for private, one-time-purchase power organisers on iOS, but it is stagnating.

### Cited Findings
- **Whering strengths**: free with unlimited items and outfits; the Clueless-style Dress Me; style submissions; 100M-item database; Chrome extension; packing suggestions; intake and longevity sustainability stats — [App Store](https://apps.apple.com/us/app/id1519461680); [FAQ](https://whering.co.uk/faq). **Weaknesses (documented)**: no manual background editing, no rotation, one photo per item, one wardrobe, no reordering, no data export, UK-only sizes, W Pick "BETA" with "it will be working better again soon" — [FAQ](https://whering.co.uk/faq). A competitor describes its AI outfits as feeling "a bit random" (biased source) — [Indyx vs Whering](https://www.myindyx.com/versus/whering-vs-indyx)
- **Acloset strengths**: deepest import and AI stack, multiple closets, packing with destination weather, widgets, 18 languages, guest mode, Chrome extension — §§3, 7, 9 sources. **Weaknesses**: 100-item free cap including the archive; ads; Beans for generative features; no data export; past pricing backlash (detail-page restrictions reverted) — [Subscription overview](https://www.acloset.app/announcements/subscription-plans-overview-eb9fc8/); [Restrictions removed](https://www.acloset.app/announcements/important-update-removal-of-access-restrictions-to-detail-pa-f884fe/); [Subscription FAQ](https://www.acloset.app/announcements/faqs-regarding-subscriptions-8bcde6/). Recurring "intermittent AI item analysis delays" notices — [Announcements index](https://www.acloset.app/announcements/)
- **Indyx strengths**: unlimited free tier; human styling menu (Feed, Lookbook, Mini, Call); in-home Archivists; Open Closets; editorial UI; CSV export on request — [Indyx home](https://www.myindyx.com/); [Support FAQ](https://www.myindyx.com/contact-us). **Weaknesses**: analytics, selfies, social styling-by-others and Enhance volume are paywalled ($12.99/mo); English-only; no desktop; email address can't be changed; Archivists only in select US metros — [Pricing table](https://www.myindyx.com/); [Support FAQ](https://www.myindyx.com/contact-us)
- **Stylebook strengths**: one-time $4.99, no account, iCloud sync, 90+ features, the most granular stats (Top 100 lists, ranges, size tracker), item status workflow, packing infographics, best manual image tools — [Features](https://www.stylebookapp.com/features.html); [FAQ](https://www.stylebookapp.com/faq.html). **Weaknesses**: iOS-only (no Android, Mac or web), no social or AI styling, no update since Jun 2025, "Date Purchased" still planned — [FAQ](https://www.stylebookapp.com/faq.html); [Stylebook 10](https://www.stylebookapp.com/stylebook10.html); [App Store](https://apps.apple.com/us/app/id335709058)
- Distinctive mechanics worth copying:
  - Acloset: AI "outfit completion" with chosen slots; try-on Basic/Pro tiers with daily free uses; Gmail/receipt imports that never auto-save; widget rollover at 6 PM; archive-inclusive caps.
  - Whering: style submissions (style someone else from their closet); a W Pick daily cap of 6; Dress Me pinning; event → auto packing list → "Have you thought about adding these?"
  - Indyx: a "do not style" tag; a 14-day full-premium preview; style-quiz-to-stylist matching; human Archivist SLA (100 items in 5h); Material Insights.
  - Stylebook: Outfit Shuffle category-deck selection; item status (laundry, lent out); calendar streak protected by Archive; packing-list infographic; brand size exceptions.

### Inferences
- Best for whom:
  - Whering: young, social, UK/EU users who want free, playful styling and community.
  - Acloset: global users who want maximum automation and AI chat or try-on and accept a subscription.
  - Indyx: US users with large or curated wardrobes who will pay for expert human styling or digitisation.
  - Stylebook: iPhone/iPad users who want a private, ad-free, subscription-free organiser and value stats over AI.
- For a new entrant, the clearest white space is combining Acloset-grade automated capture with Stylebook-grade privacy and data ownership (export, local-first) and Whering-style social play, without an item cap.

### Gaps
- Review-sentiment validation of these strengths and weaknesses is out of scope here (covered by the reviews researcher).
