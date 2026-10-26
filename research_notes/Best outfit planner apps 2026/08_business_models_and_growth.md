# Business models, monetisation, pricing, funding, traction and growth of outfit-planner / digital-wardrobe / AI-stylist apps (status at 25 Oct 2026)

Scope: Whering, Acloset, Indyx, Stylebook, Cladwell, Smart Closet, Pureple, Combyne, Save Your Wardrobe (SYW), Alta, Google Doppl, Daydream, Style DNA, Gensmo. Added because they matter commercially: Fits, Dressly, Essembl, OpenWardrobe and Doji (try-on), plus two historical cases, The Yes and Finery.

Method notes:
- App Store pages were fetched with `curl -L` on 25 Oct 2026 from the US and GB storefronts (`apps.apple.com/{us|gb}/app/id<ID>`). Apple's page shows only the **top ~10 in-app purchase products**, so long-tail SKUs may be missing. Rating counts come from the iTunes Lookup API on the same day.
- Google Play pages were fetched on 25 Oct 2026 (US, en).
- Raw files are in `/tmp/claude-0/-home-user-outfit-planner-app/bd7b84c5-3686-5200-b171-3a2b128c387a/scratchpad/business/` (subfolders `raw/appstore`, `raw/play`, `raw/ch`, `raw/sec`, `raw/web`, `raw/adapty`, `raw/appgoblin`, `raw/social`). The parsed summaries are `iap_summary.txt`, `lookup_summary.txt`, `ch_summary.txt`, `ixbrl_wh.txt` and `ixbrl_syw.txt`.
- "AppGoblin" figures are free modelled estimates (installs, MAU, coarse revenue buckets) from appgoblin.info. "Adapty" figures are modelled estimates from Adapty's paywall library. Both are **low-confidence third-party estimates**, not company data. I found no public Sensor Tower, Appfigures or AppMagic posts on any of these apps.

---

## 1. Pricing: in-app purchases, tiers, free-tier limits, trials, paywalls, regional and web pricing

### Takeaway
The category has split into four pricing archetypes:
1. **Free core plus pay-per-use AI credits.** Whering, Gensmo, and Fits as a hybrid.
2. **Free core plus a premium membership that never caps items.** Indyx, Fits and Cladwell.
3. **Item-capped freemium with tiered subscriptions plus virtual currency.** Acloset (100 free items) and the older apps.
4. **Hard or near-hard paywall with weekly plans.** Pureple, Dressly, Essembl and Style DNA.

The VC-backed AI newcomers (Alta, Daydream) charge consumers nothing and sell no in-app purchases. Typical prices cluster at **$7.99–$14.99 a month, $30–$90 a year, and $6.99–$11.99 a week**. AI credits retail at roughly **$0.07–$0.50 per generation**.

### Cited Findings

#### Whering (Whering Ltd, UK)
- **US in-app purchases (25 Oct 2026):** 10 Credits $2.99, 50 Credits $7.99, 100 Credits $12.99. "Outfit Maker" $4.99. Supporter core $0.99, Supporter basic $9.99, supporter extra $14.99, supporter speed $29.99, supporter gold standard $49.99. — [App Store US](https://apps.apple.com/us/app/id1519461680)
- **GB in-app purchases:** 10 Credits £1.99, 50 Credits £5.99, 100 Credits £9.99. Outfit Maker £4.99. Supporter £0.99, £9.99, £14.99, £29.99 and £49.99. — [App Store GB](https://apps.apple.com/gb/app/id1519461680)
- **The help centre contradicts the store.** It lists credit top-ups of "25 credits for £1.99, 50 credits for £3.99, or 100 credits for £6.99", shared across Virtual Try On and Enhance (updated 24 Jun 2026). — [Whering help: VTO top-up plans](https://whering.zendesk.com/hc/en-gb/articles/5800092986143-What-are-the-top-up-plans-for-Virtual-Try-On-credits); [Enhance top-ups](https://whering.zendesk.com/hc/en-gb/articles/5695048920607-What-are-the-top-up-plans-for-Enhance-credits)
- **No subscription.** "Whering doesn't have a subscription, so there's nothing to cancel. Access runs on a credit system" (updated 25 Jun 2026). — [Whering help](https://whering.zendesk.com/hc/en-gb/articles/5928286841375-How-do-I-cancel-my-subscription)
- **What is free and what is paid.** All standard features are free. The paid features are Enhance, Virtual Try On and Gallery Scanning. — [Whering help: Is Whering free?](https://whering.zendesk.com/hc/en-gb/articles/5473162627231)
- **Credit consumption:** each try-on **and each regeneration** costs one credit. — [Whering help](https://whering.zendesk.com/hc/en-gb/articles/5800046039327-How-much-does-each-Virtual-Try-On-cost)
- Gallery scanning costs one credit per item digitised. — [Whering help](https://whering.zendesk.com/hc/en-gb/articles/5921842464799-Does-gallery-scanning-use-credits)
- **Supporter Club is closed.** "Currently closed to new members". Historical supporters keep early access and a badge. — [Whering help](https://whering.zendesk.com/hc/en-gb/articles/5446628933663-What-are-the-benefits-of-joining-the-supporter-club)
- **No ads.** "Whering doesn't show adverts. No banners, no sponsored clothes" (updated 10 Aug 2026). — [Whering help](https://whering.zendesk.com/hc/en-gb/articles/6126968328095)
- **Style Pass.** The store description says "Lacking inspiration? Get a Style Pass and try our Outfit Maker", which matches the $4.99 "Outfit Maker" product. — [App Store US description via iTunes lookup](https://itunes.apple.com/lookup?id=1519461680&country=us)
- **Google Play** lists in-app purchases at $1.49–$64.99 per item, with no "Contains ads" label. — [Google Play](https://play.google.com/store/apps/details?id=com.whering.app&hl=en&gl=US)
- **Pricing history.**
  - Feb 2021: the "'Whering Essentials' digital wardrobe is free and will always be free". A paid "Whering VIP" membership was added, and a "White Glove Service (one off cost)" was planned. — [TheIndustry.fashion, 11 Feb 2021](https://www.theindustry.fashion/the-interview-bianca-rangecroft-founder-of-whering/)
  - The founder described the model as "a B2C freemium model with a subscription service for premium features" plus "a commission on each sale of sustainable product", with "the long-term monetization strategy... a data play". — [Goldman Sachs alumni Q&A](https://www.goldmansachs.com/alumni/spotlight/2024/q-a-with-bianca-rangecroft-founder-and-ceo-of-whering)

#### Acloset (Looko Inc., South Korea)
- **US in-app purchases:**
  - Subscriptions: Basic Monthly $3.99, Basic Yearly $27.99, Premium Monthly $9.99, Premium Yearly $59.99, Expert Monthly $24.99, Expert Yearly $147.99.
  - Beans: 100 for $1.99, 200 for $3.99, 210 (with bonus) for $3.99, 1,200 (with bonus) for $19.99.
  - — [App Store US](https://apps.apple.com/us/app/id1542311809)
- **GB in-app purchases:** Basic £3.99 a month or £29.99 a year. Premium £9.99 a month or £59.99 a year. Expert £24.99 a month. Beans: 100 for £1.99, 500 for £9.99, 550 (bonus) for £9.99. — [App Store GB](https://apps.apple.com/gb/app/id1542311809)
- **Free tier:** "Enjoy all Acloset features for free, for up to 100 items… Need more space? Upgrade to one of our subscription plans". — [App Store description](https://itunes.apple.com/lookup?id=1542311809&country=us)
- **Cap enforcement (Dec 2023 policy).** Archived items count toward the cap. Over the limit, "access to each individual clothing item, idea, and OOTD page will become limited" and creating outfits or calendar entries is "temporarily paused". Existing users were offered 40%, 50% and 60% off yearly Basic, Premium and Expert respectively. — [Acloset announcement, 18 Dec 2023](https://www.acloset.app/announcements/subscription-plans-overview-eb9fc8/)
- **Google Play:** "Contains ads · In-app purchases", Editors' Choice, in-app purchases $1.99–$149.99 per item. — [Google Play](https://play.google.com/store/apps/details?id=com.looko.acloset&hl=en&gl=US)

#### Indyx (Indyx Inc., US)
- **In-app purchases:** Monthly Membership $12.99 and Annual Membership $74.99 in the US. In the GB store, £12.99 and £69.99. — [App Store US](https://apps.apple.com/us/app/id1599179405); [App Store GB](https://apps.apple.com/gb/app/id1599179405)
- **Free tier has no caps.** "You can digitize unlimited items, save unlimited outfits, track what you wear… Our paid membership called Indyx Insider takes your free digital wardrobe to the next level". — [myindyx.com FAQ](https://www.myindyx.com)
- **Insider launched 4 Sep 2024.** Features: an analytics dashboard, custom wardrobe display, unlimited outfit selfies, a private community (Slack and events), and 10% off Lookbooks. — [Indyx blog, "Why We're Launching Indyx Insider", 4 Sep 2024](https://www.myindyx.com/blog/why-we-are-launching-insider)
- The current homepage also lists for members: "Unlimited enhanced flatlays", "Super HD wardrobe images" and "virtual selfies", all "subject to Indyx Terms of Service". — [myindyx.com](https://www.myindyx.com)
- **Paid human services:** see section 2.

#### Stylebook (left brain / right brain, LLC, US)
- **Paid upfront:** $4.99 in the US and £4.99 in GB. The store shows **no in-app purchase section**. — [App Store US](https://apps.apple.com/us/app/id335709058); [iTunes lookup GB](https://itunes.apple.com/lookup?id=335709058&country=gb)
- **No item limits:** "NO LIMITS: add unlimited clothing, accessories, & inspirations". — [App Store description](https://itunes.apple.com/lookup?id=335709058&country=us)
- **Stale build:** the latest version is 10.1, dated 15 Jun 2025. — [iTunes lookup](https://itunes.apple.com/lookup?id=335709058&country=us)

#### Cladwell (Cladwell, Inc, Cincinnati)
- **US in-app purchases (10 shown):** Weekly Membership $7.99, Monthly Subscription $7.99, Cladwell Monthly $9.99, Cladwell Monthly $2.99, Cladwell Subscription $9.99 and $59.99, Quarterly $21.99, Annual $59.99, Cladwell Annual $79.99. — [App Store US](https://apps.apple.com/us/app/id1140550878)
- **GB in-app purchases:** Weekly £7.99, Monthly £7.99, £9.99, £2.99, Quarterly £19.99, Annual £59.99, £54.99, £58.99, £39.99 and £19.99. — [App Store GB](https://apps.apple.com/gb/app/id1140550878)
- **Free and paid features.** "Building and organizing your closet is free. Outfits, See it on me, your season capsule, the shopping list, color analysis and the stylist need a Cladwell subscription. A free trial is available for eligible new users. AI features include a monthly allowance." — [App Store description](https://itunes.apple.com/lookup?id=1140550878&country=us)
- **Web pricing:** "Web subscriptions to Your Stylist cost $9.99 a month or $79.99 a year (USD). Web purchases do not include a free trial. In-app prices and trial offers may differ by platform, region and eligibility… Existing subscribers keep their current terms." Sign-up starts in the app. — [cladwell.com/pricing](https://cladwell.com/pricing)
- **Historical price:** in 2017 the service cost "a few dollars per month". — [TechCrunch, 2 Oct 2017](https://techcrunch.com/2017/10/02/cladwell-helps-you-pick-out-your-clothes)
- **Paywall snapshot (Adapty, captured 3 Jul 2024):** weekly $7.99, monthly $9.99, annual $59.99, quarterly $21.99. — [Adapty paywall library: Cladwell](https://adapty.io/paywall-library/dwell/)

#### Smart Closet (Rabbit Tech Inc)
- **Paid upfront:** $2.99 in the US and £2.99 in GB. — [iTunes lookup US](https://itunes.apple.com/lookup?id=1198057728&country=us); [GB](https://itunes.apple.com/lookup?id=1198057728&country=gb)
- **Backup subscription:** "Auto Backup & Sync" costs $0.99 a month or $3.99 in the in-app purchase list (GB: £0.99 and £3.99). — [App Store US](https://apps.apple.com/us/app/id1198057728)
- The description instead states "$0.99 per month / $9.99 per year". That annual price is inconsistent with the IAP list. — [App Store description](https://itunes.apple.com/lookup?id=1198057728&country=us)
- Last version 3.8.3, dated 10 Nov 2025. — [iTunes lookup](https://itunes.apple.com/lookup?id=1198057728&country=us)

#### Pureple (ICECLIP LLC)
- **Advertised Premium prices:** "Weekly: $6.99/week; Monthly: $14.99/month; Yearly: $89.99/year (7-day free trial, then $89.99/year)… Pricing may vary by region". — [App Store description](https://itunes.apple.com/lookup?id=628106373&country=us)
- **US in-app purchases:** Pro Version $14.99 (legacy one-time), Weekly $6.99, Yearly $89.99, AI Style Advisor+ $39.99, Annual Storage Subscription $9.99, and five "Pureple Subscription" SKUs at $34.99, $41.99, $48.99, $55.99 and $62.99. — [App Store US](https://apps.apple.com/us/app/id628106373)
- **GB:** Pro £14.99, Weekly £6.99, Yearly £89.99, AI Style Advisor+ £39.99, storage £7.99, plus SKUs at £35.99–£63.99. — [App Store GB](https://apps.apple.com/gb/app/id628106373)
- **Feature gates:** AI Style Me "requires Pureple Pro". Whole Closet Scan is Pro. Premium covers unlimited AI suggestions, background erase, ad-free use and cloud sync. — [pureple.com](https://www.pureple.com); [App Store description](https://itunes.apple.com/lookup?id=628106373&country=us)
- **Google Play:** "Contains ads · In-app purchases", rated 2.5 stars from 4K reviews. — [Google Play](https://play.google.com/store/apps/details?id=com.app.pureple&hl=en&gl=US)

#### Combyne (combyne GmbH, Germany)
- **US in-app purchases:** Combyne Premium $2.99 and $19.99. Superlikes: 10 for $0.99, 30 for $1.99, 50 for $2.99, 100 for $4.99. GB prices mirror these at £2.99 / £19.99 and £0.99–£4.99. — [App Store US](https://apps.apple.com/us/app/id989727742); [GB](https://apps.apple.com/gb/app/id989727742)
- **Premium plan:** gives "full access to all features", including creating challenges and Combyne wallpapers, plus a PREMIUM badge. Free users get one challenge a week and five challenge submissions a week. — [combyne.com/plans (via search result)](https://combyne.com/plans)
- **Google Play:** "Contains ads · In-app purchases", $0.99–$33.99. — [Google Play](https://play.google.com/store/apps/details?id=com.combyne.app&hl=en&gl=US)

#### Save Your Wardrobe (Save your wardrobe Ltd., UK)
- **No in-app purchase section** on the US or GB App Store. — [App Store US](https://apps.apple.com/us/app/id1485757044)
- **No in-app purchase label on Google Play.** — [Google Play](https://play.google.com/store/apps/details?id=com.saveyourwardrobe.syw&hl=en&gl=US)
- Consumer monetisation runs through bookable aftercare services (see section 2).

#### Alta (Flagship AI, Inc., NYC)
- **No in-app purchase section** on the US or GB App Store. — [App Store US](https://apps.apple.com/us/app/id6481705400)
- Google Play shows no "In-app purchases" label. — [Google Play](https://play.google.com/store/apps/details?id=com.alta&hl=en&gl=US)
- The website says "Alta is free to use." — [altadaily.com](https://www.altadaily.com)
- In April 2025 the founder said "The app is free to use. If someone buys something, Alta gets a cut." — [WWD, 16 Apr 2025](https://wwd.com/fashion-news/fashion-features/cfda-partnership-alta-personal-styling-app-ai-1237089028/)

#### Google Doppl
- **Free experimental app, now withdrawn.** Doppl was a free Google Labs app, US-only and limited to users aged 18 and over, launched June 2025. — [TechCrunch, 26 Jun 2025](https://techcrunch.com/2025/06/26/google-launches-doppl-a-new-app-that-lets-you-visualize-how-an-outfit-might-look-on-you)
- Shut down on 30 Apr 2026. See section 8.

#### Daydream (Dahlia Labs, Inc.)
- **No in-app purchases** on the US or GB App Store. — [App Store US](https://apps.apple.com/us/app/id6692634357)
- The listing claims "SHOP 10,000+ TRUSTED BRANDS AND 2 MILLION ITEMS". — [App Store description](https://itunes.apple.com/lookup?id=6692634357&country=us)

#### Style DNA (AI Style by DNA S.L., Spain)
- **US in-app purchases:**
  - Monthly at $7.99, $9.99 and $19.99.
  - Three-month at $14.99 and $19.99.
  - Six-month at $29.99.
  - Annual at $29.99 and $39.99.
  - One-offs: "Palette and Body type guide" $12.99 and "Advanced Color Palette" $9.99.
  - — [App Store US](https://apps.apple.com/us/app/id1358319821)
- **GB:** the same structure, e.g. monthly £7.99 and £19.99, annual £29.99 and £39.99, guide £12.99. — [App Store GB](https://apps.apple.com/gb/app/id1358319821)
- **Adapty paywall captures (4 Sep 2024 and 4 Jun 2025):** describe subscriptions "from a monthly plan at $11.49 to annual plans between $19.99 and $39.99". — [Adapty: Style DNA](https://adapty.io/paywall-library/style-dna/)

#### Gensmo (Serendipity One Inc.)
- **Credits only, at $0.50 each in both stores:** 10 Credits $5.00, 20 $10.00, 30 $15.00, 40 $20.00, 60 $30.00, 100 $50.00 (GB £5–£50). — [App Store US](https://apps.apple.com/us/app/id6636520663); [GB](https://apps.apple.com/gb/app/id6636520663)
- Last update 14 Jan 2026 (v3.10.3). — [iTunes lookup](https://itunes.apple.com/lookup?id=6636520663&country=us)

#### Added apps
- **Fits** (L. & J. Henne UG, Germany).
  - US in-app purchases: Fits Pro $9.99, $29.99 and $59.99. Fits Pro + Credits $9.99 and $29.99. AI credits: 5 for $1.99, 20 for $4.99, 100 for $19.99, 500 for $79.99. GB Pro: £7.99, £24.99 and £39.99. — [App Store US](https://apps.apple.com/us/app/id6447482321); [GB](https://apps.apple.com/gb/app/id6447482321)
  - Free tier: "no limits to how many clothing or outfit items"; unlimited background removal. Pro: unlimited AI styling chat, 30 AI credits a month for image enhancement and virtual try-on, and advanced stats. — [Fits help: Free vs paid](https://www.fits-app.com/knowledge/free-vs-paid-features)
- **Dressly** (TIKVEX LIMITED). US weekly plans at $7.99 and $11.99, including a "1 week no trial subscription" at $11.99. Monthly at $24.99 and $34.99. Annual at $55.99 and $79.99. "Farb-Scanner" $9.99. "Unlock all features by subscribing to the premium subscription". — [App Store US](https://apps.apple.com/us/app/id6738174180); [description](https://itunes.apple.com/lookup?id=6738174180&country=us)
- **Essembl** (Essembl SA). Premium Yearly $29.99 and $39.99. "Premium AI (Weekly)" $9.99. "Premium AI Full Access" at $9.99, $19.99 and $59.99. "MAX Access" $19.99 and $39.99. — [App Store US](https://apps.apple.com/us/app/id6479642952)
- **OpenWardrobe** (OpenWardrobe, Inc.). Circle Monthly $12.99, Yearly $79.00, **Lifetime $199.00**. GB has nominal parity at £12.99, £79 and £199. — [App Store US](https://apps.apple.com/us/app/id1549029468); [GB](https://apps.apple.com/gb/app/id1549029468)

#### Regional pricing, US vs GB (nominal)
- **Whering:** 100 credits cost $12.99 vs £9.99. — [US](https://apps.apple.com/us/app/id1519461680); [GB](https://apps.apple.com/gb/app/id1519461680)
- **Indyx:** annual $74.99 vs £69.99. — [US](https://apps.apple.com/us/app/id1599179405); [GB](https://apps.apple.com/gb/app/id1599179405)
- **Acloset:** Basic yearly $27.99 vs £29.99. — [US](https://apps.apple.com/us/app/id1542311809); [GB](https://apps.apple.com/gb/app/id1542311809)
- **Fits:** top Pro tier $59.99 vs £39.99. — [US](https://apps.apple.com/us/app/id6447482321); [GB](https://apps.apple.com/gb/app/id6447482321)
- **Cladwell:** Cladwell Annual $79.99 vs top GB annual £59.99. — [US](https://apps.apple.com/us/app/id1140550878); [GB](https://apps.apple.com/gb/app/id1140550878)
- **Gensmo and OpenWardrobe** keep 1:1 nominal parity between $ and £. — [Gensmo GB](https://apps.apple.com/gb/app/id6636520663); [OpenWardrobe GB](https://apps.apple.com/gb/app/id1549029468)

#### Paywall placement: evidence and opinion
- **Argument against item-cap paywalls (Indyx CEO, Sep 2024).** "Many digital wardrobes either ask everyone to pay for the app upfront, or have a paywall that kicks in after a certain number of items or outfits. This makes it such that the 'free' plan only realistically serves as a trial… the average wardrobe size is about 175 items. So, even if the paywall kicks in after your 100th item… why start?" — [Indyx blog, 4 Sep 2024](https://www.myindyx.com/blog/why-we-are-launching-insider)
- **User reaction to Acloset's 100-item cap** (Google Play review, 10 Aug 2026): "Lets you add and customize 100 items before telling you that it wants some outrageous amount to continue." — [Google Play: Acloset](https://play.google.com/store/apps/details?id=com.looko.acloset&hl=en&gl=US)
- **User reaction to Whering's free model** (Google Play review, 19 Aug 2026): "AI is not thrown in your face and there is no paywall." — [Google Play: Whering](https://play.google.com/store/apps/details?id=com.whering.app&hl=en&gl=US)
- **User reaction to Pureple** (Google Play review, 17 Feb 2026): "I had this two years ago… all features were free. I got it again and there are no ads, but the paid version locks the AI outfits and adding photos straight from the web". — [Google Play: Pureple](https://play.google.com/store/apps/details?id=com.app.pureple&hl=en&gl=US)

### Inferences
- **Apple listings show A/B tests and grandfathered tiers.** Several apps list many SKUs for the same tier: Cladwell has four or more annual prices, Pureple five "Pureple Subscription" SKUs, Style DNA three monthly prices, Essembl many "Full Access" SKUs. This is consistent with paywall price testing and legacy plans; Cladwell explicitly says "Existing subscribers keep their current terms".
- **Weekly plans signal the AI-app-factory playbook.** Weekly subscriptions ($6.99–$11.99 a week at Pureple, Dressly, Essembl and Cladwell), plus "no trial" weekly SKUs, are the high-ARPU, hard-paywall tactic common to that playbook. The VC-backed brand apps (Whering, Alta, Indyx) avoid weekly plans entirely.
- **Item caps get the most complaints.** Item caps such as Acloset's 100 are the most user-hostile gate, because cataloguing is sunk effort. The newer entrants (Fits, Indyx, Whering, Alta, Cladwell) all advertise unlimited items and gate AI, analytics or styling instead.
- **Whering's store and help-centre credit prices disagree.** The store shows 10 credits for £1.99; the help centre shows 25 for £1.99. Either a price test, a recent repricing, or a stale help article. A founder should not treat either figure as settled.

### Gaps
- I could not see in-app paywall screens: no accounts were created, so onboarding hard versus soft paywall placement, trial lengths and default plan selection are not observed. The only evidence is the descriptions, Adapty's paywall captures (Cladwell, Style DNA) and user reviews.
- Apple shows only the top ~10 in-app products, so full SKU lists (e.g. Acloset's GB yearly Expert price, Pureple's monthly SKU) may be missing.
- Web pricing was found only for Cladwell. I found no public web checkout for Whering, Acloset, Indyx or Pureple.
- I found no public dated price history (e.g. the date Acloset added Expert, or when Whering moved from VIP and Supporter to credits) beyond the cited 2021, Dec 2023, Sep 2024 and 2026 snapshots.

---

## 2. Other revenue streams: affiliate, resale, brand partnerships, B2B / white-label / data, human services, ads

### Takeaway
Pure consumer subscriptions are the minority strategy among the funded players.
- **Alta** is building an affiliate business and, increasingly, a **B2B AI provider** business (Rent the Runway, Public School, Gap).
- **Daydream** is commission-only, with no ads.
- **SYW** has pivoted to a **B2B aftersales SaaS platform**.
- **Whering** is positioning around data and resale, with eBay as an investor.
- **Indyx** monetises **human services**: in-home digitisation at about $295 and stylist Lookbooks from $60.
- **Combyne, Acloset and Pureple** rely partly on **ads**.

### Cited Findings

#### Affiliate and commerce
- **Alta.**
  - Earns "from the consumer app and affiliate partnerships… If someone buys something, Alta gets a cut". Affiliate partners include Saks Fifth Avenue, Bloomingdale's and Nordstrom, plus networks Rakuten and Sovrn. The catalogue is "roughly 4,000 brands". — [WWD, 16 Apr 2025](https://wwd.com/fashion-news/fashion-features/cfda-partnership-alta-personal-styling-app-ai-1237089028/)
  - Menlo framed the target as "the $20B affiliate marketing industry and $160B U.S. online apparel market". — [Menlo Ventures](https://menlovc.com/perspective/agentic-styling-and-shopping-why-were-backing-alta/)
- **Daydream.**
  - "Commission-based revenue at the moment". It is "not looking to generate any ad-based revenue". It acts as a discovery layer and does not fulfil orders. White-labelling is a possible future option. — [TechCrunch, 20 Jun 2024](https://techcrunch.com/2024/06/20/former-stitch-fix-coo-julie-bornstein-secures-50m-to-build-a-new-age-e-commerce-search-engine)
  - Secondary summaries cite a 20% commission per sale, but I found no primary confirmation. — [search summary, unverified]
- **Whering.**
  - In 2023 its "marketplace operates an affiliate model, offering pre-loved, rental and new but more sustainable pieces" from partners such as Vestiaire Collective, Wolf & Badger, Lampoo and Beyond Retro, with "more than 150 brand partners". — [Drapers 30 Under 30, 2023](https://www.drapersonline.com/people/30under30-2023/drapers-30-under-30-2023-bianca-rangecroft-founder-and-ceo-whering)
  - The company now says it shows "no sponsored clothes". — [Whering help](https://whering.zendesk.com/hc/en-gb/articles/6126968328095)
- **Combyne:** "You can buy every item or outfit directly via the app at over 50 partner shops and brands. With one click, we will forward you". — [App Store description](https://itunes.apple.com/lookup?id=989727742&country=us)
- **Style DNA:** "Personal shopping — explore curated picks from 26,000 brands across 231 retailers (Amazon, H&M, Nordstrom, Old Navy, Urban Outfitters…)". — [App Store description](https://itunes.apple.com/lookup?id=1358319821&country=us)
- **Cladwell:** "Each missing capsule piece becomes a shopping list item with 3 to 5 real products from the brands you choose, within your budget. Tap View to see a product at the store." — [App Store description](https://itunes.apple.com/lookup?id=1140550878&country=us)
- **Gensmo:** "Snap to Shop… Shop by Brands… shop full outfits with one tap". — [App Store description](https://itunes.apple.com/lookup?id=6636520663&country=us)
- **Indyx rejects affiliate as a primary model.** "The affiliate commission model always felt disingenuous to me as a user and ineffective as an operator… putting shopping front and center in a digital wardrobe is distracting at best but also downright hypocritical at worst." — [Indyx blog, 4 Sep 2024](https://www.myindyx.com/blog/why-we-are-launching-insider)

#### B2B, white-label and data
- **Alta (B2B).**
  - Now "powers all AI within Rent the Runway's app, which saw a 35% jump in engagement". Gap is pursuing an integration. Zimmermann used Alta at Paris Fashion Week to gauge demand for units. Elf Beauty has trialled it. — [Fortune, 22 Oct 2026](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/)
  - Founder's positioning: "Our goal is to be the best possible AI provider for this space." — [Fortune, 22 Oct 2026](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/)
  - First brand integration: Public School New York added a "Style with Alta" button on product pages. Earlier partners: Poshmark and the CFDA. — [TechCrunch, 14 Feb 2026](https://techcrunch.com/2026/02/14/clueless-inspired-app-alta-partners-with-brand-public-school-to-start-integrating-styling-tools-into-websites/)
- **Save Your Wardrobe (B2B pivot).**
  - Its homepage now sells "The Aftersales Platform": "repairs, care, alterations, warranties, logistics, exchanges, compliance… and data reporting", an "AI Damage Detection Tool", and ERP and loyalty integrations. — [saveyourwardrobe.com](https://www.saveyourwardrobe.com)
  - In Sep 2025 it acquired the French B2B repair platform Les Raccommodeurs, which brought IKKS and "dozens of French boutiques". — [FashionUnited, Sep 2025](https://fashionunited.uk/news/business/save-your-wardrobe-acquires-french-aftersales-platform-les-raccommodeurs/2025093084172)
  - Its earlier B2B deal was with Zalando: "connects Zalando's customers with a network of aftercare specialists across Berlin and provides the retailer with data and insights". — [Just Style, 29 Jul 2022](https://www.just-style.com/news/save-your-wardrobe-secures-3m-to-drive-eu-expansion)
- **Whering (data and resale).**
  - Plans include "resale integrations" with eBay Ventures as lead investor. — [TheIndustry.fashion, 7 Jul 2026](https://www.theindustry.fashion/whering-secures-7m-to-expand-ai-powered-wardrobe-app/)
  - Aims to be "a definitive data source" for circular retail. — [FashionUnited, Jul 2026](https://fashionunited.com/news/business/whering-secures-funding-from-ebay-ventures-and-google-ai-futures/2026070773352)
  - The founder: "We have access to such a vast amount of data that hasn't existed before". — [Just Style, Jul 2026](https://www.just-style.com/news/whering-seed-funding-ai/)
  - Partnership enquiries go to partnerships@whering.co.uk. No public B2B product. — [whering.co](https://www.whering.co)
- **Acloset (agent platform):** "evolving Acloset from an app-centric service into an MCP-based next-generation digital closet platform that various AI agents can directly use". — [Wanted job post (Korean), undated, closed](https://www.wanted.co.kr/wd/391802)
- **Style DNA (retail data):** retail-consultancy experts say its data "is also used to refine assortment, pricing and profitability recommendations". Named retail partners: H&M, Zara, Old Navy. — [Mercado&Consumo, 22 May 2026](https://mercadoeconsumo.com.br/22/05/2026/inovacao/style-dna-um-personal-stylist-com-ia-que-cria-um-perfil-de-estilo-a-partir-de-uma-selfie/)

#### Human services
- **Indyx stylists:** "The Lookbook Starting at $150" (10 outfits, up to 6 new shopping suggestions); "The Lookbook Mini Starting at $60" (3 outfits). "Choose your stylist and your price". — [myindyx.com](https://www.myindyx.com)
- **Indyx Archivists ("The Catalog").** A "professional Indyx Archivist [visits] your home and digitize[s] your wardrobe… only available in select U.S. metro areas". — [myindyx.com FAQ](https://www.myindyx.com)
  - Price per the indexed official listing: $295 for up to 100 items within 5 hours, plus $2 per additional item. — [Indyx Catalog page (indexed; returned 404 on 25 Oct 2026)](https://myindyx.com/personal-stylists/pigmami/p/the-catalog); [how-it-works (indexed; 404 on 25 Oct 2026)](https://myindyx.com/how-it-works)
  - Bundles such as "The Catalog + The Lookbook" are offered per stylist. — [Indyx bundle page (indexed)](https://myindyx.com/personal-stylists/9to5chic/p/bundle)
- **Alta x TaskRabbit (Nov 2025):** users can book a Tasker to photograph their closet for upload. No fee was published. — [Alta blog](https://www.altadaily.com/blog/taskrabbit)
- **Save Your Wardrobe (consumer):** the app offers "on-demand local aftercare services, including alterations, repairs, eco-cleaning, customisation and upcycling". — [App Store description](https://itunes.apple.com/lookup?id=1485757044&country=us)
- **Whering (2021):** planned a "White Glove Service (one off cost)". — [TheIndustry.fashion, 2021](https://www.theindustry.fashion/the-interview-bianca-rangecroft-founder-of-whering/)

#### Ads
- **Ad labels on Google Play:** Acloset, Combyne and Pureple carry "Contains ads"; Whering, Indyx, Cladwell, Style DNA, Fits and Alta do not. — Play pages for [Acloset](https://play.google.com/store/apps/details?id=com.looko.acloset&hl=en&gl=US), [Combyne](https://play.google.com/store/apps/details?id=com.combyne.app&hl=en&gl=US), [Pureple](https://play.google.com/store/apps/details?id=com.app.pureple&hl=en&gl=US), [Whering](https://play.google.com/store/apps/details?id=com.whering.app&hl=en&gl=US)
- **AppGoblin's modelled Android revenue mix (low confidence):**
  - Combyne: 36% in-app purchases / 64% ads.
  - Pureple: 37% / 63%.
  - Acloset: 73% / 27%.
  - Whering: 100% in-app purchases / 0% ads.
  - — [AppGoblin Combyne](https://appgoblin.info/apps/com.combyne.app); [Pureple](https://appgoblin.info/apps/com.app.pureple); [Acloset](https://appgoblin.info/apps/com.looko.acloset); [Whering](https://appgoblin.info/apps/com.whering.app)
- **Daydream: no ads** (see above, TechCrunch 2024).

### Inferences
- **B2B is where VC-scale revenue is being pursued.** Alta's Series A narrative centres on Rent the Runway, Gap and Zimmermann, not consumer ARPU. SYW's filings and homepage show a full B2B pivot. The consumer wardrobe app increasingly acts as a data and avatar asset that feeds B2B or affiliate revenue.
- **Human services carry high ticket prices but don't scale.** Indyx's $295 Catalog and $60–$150 Lookbooks solve the cataloguing cold-start problem but are geography-limited ("select U.S. metro areas"). Alta's TaskRabbit tie-up is an attempt to outsource the same service.
- **Ad-supported wardrobe apps are the older, mass-market, lower-rated ones.** Combyne and Pureple show this pattern; Pureple's Play rating is 2.5.

### Gaps
- None of the companies disclose affiliate GMV, take rates, B2B contract values or service revenue.
- Daydream's commission rate (said to be 20%) has no primary source.
- SYW's per-brand SaaS pricing and its consumer service commission are not public.
- Whering's current affiliate status is unclear. The 2023 affiliate marketplace is not visible in the 2026 help centre, and "no sponsored clothes" suggests it was scaled back.

---

## 3. Company facts: entity, HQ, founding, founders, team, funding, accelerators, grants, M&A

### Takeaway
Capital is concentrating in the AI-native shopping and stylist players. In disclosed equity funding:
- **Gensmo** has announced **$60M+**.
- **Daydream** has about **$50M** (confirmed by SEC Form D: $49.66M sold).
- **Alta** has **$31M** ($11M seed plus $20M Series A in Oct 2026).

The "classic" wardrobe apps have raised little. **Whering** has about $14M claimed in total, including the $7M July 2026 seed. **Save Your Wardrobe** raised a $3M seed plus about £5.6M of convertible notes. **Acloset** raised a KRW 3B Series A. **Indyx** has only an undisclosed pre-seed, and **Cladwell** raised about $3.5–3.8M before a 2019 sale.

### Cited Findings

#### Whering
- **Entity and founder.** WHERING LTD, company no. 12660509, incorporated 10 Jun 2020. Registered office 396 St John Street, London EC1V 4NJ. SIC 62090. The sole PSC is Bianca Isabella Rangecroft. — [Companies House](https://find-and-update.company-information.service.gov.uk/company/12660509)
- **Launch and positioning:** launched 2021, Clueless-inspired. — [FashionUnited, Jul 2026](https://fashionunited.com/news/business/whering-secures-funding-from-ebay-ventures-and-google-ai-futures/2026070773352)
- **Seed round, 7 Jul 2026:** $7M (£5.2M) led by eBay Ventures and the Google AI Futures Fund. — [TheIndustry.fashion](https://www.theindustry.fashion/whering-secures-7m-to-expand-ai-powered-wardrobe-app/)
- **Total funding:** "nearly $14 million" including the new round. — [The Impression](https://theimpression.com/whering-raises-7-million-as-ebay-and-google-back-wardrobe-app/)
- **Team:** "Headquartered in London, with a global team of 19". — [Just Style, Jul 2026](https://www.just-style.com/news/whering-seed-funding-ai/)
- **Earlier funding:** "raised £2m, including a grant from national innovation agency Innovate UK". In 2023 the company had 20 full-time employees. — [Drapers 2023](https://www.drapersonline.com/people/30under30-2023/drapers-30-under-30-2023-bianca-rangecroft-founder-and-ceo-whering)
- **Dragons' Den:** the founder pitched on BBC1 in March 2022. — [Drapers 2023](https://www.drapersonline.com/people/30under30-2023/drapers-30-under-30-2023-bianca-rangecroft-founder-and-ceo-whering)
- **Accelerators:** "We've been through some amazing accelerators" (not named). — [TheIndustry.fashion 2021](https://www.theindustry.fashion/the-interview-bianca-rangecroft-founder-of-whering/)

#### Acloset
- **Entity and founding.** 룩코 (Looko Inc.) was founded in June 2020 and launched Acloset in February 2021. — [Wanted job post](https://www.wanted.co.kr/wd/343246)
- **Founders:** CEO 고해신 (Heasin Ko). — [VentureSquare, 4 May 2022](https://www.venturesquare.net/854274/)
- **Series A (May 2022):** KRW 3.0B (₩30억) led by KT Investment, with Laguna Investment and others co-investing. — [VentureSquare, 4 May 2022](https://www.venturesquare.net/854274/)
- **Programmes:** selected in April 2022 for TIPS (government-matched, up to ₩7억) and the fourth cohort of Google for Startups Campus Korea. — [VentureSquare, 4 May 2022](https://www.venturesquare.net/854274/)
- **Aggregator figures:** Caplight lists total funding raised of $2.81M, with an Oct 2022 grant as the last round. — [Caplight (via search)](https://www.caplight.com/company/acloset)

#### Indyx
- **Founders:** Yidi Campbell (Founder and CEO) and Devon Rule (Co-Founder, Growth). — [myindyx.com/our-story](https://www.myindyx.com/our-story)
- **Funding (aggregators only):** a pre-seed from Alante Capital in July 2022 (undisclosed amount). Another database lists a seed round dated 15 Jun 2022. Team about 16. — [bouncewatch](https://bouncewatch.com/company/indyx); [CB Insights](https://www.cbinsights.com/company/indyx)

#### Alta
- **Entity:** the App Store seller is Flagship AI, Inc. — [iTunes lookup](https://itunes.apple.com/lookup?id=6481705400&country=us)
- **HQ:** "Made with love in NYC". — [altadaily.com](https://www.altadaily.com)
- **Seed round, June 2025:** $11M led by Menlo Ventures, with Aglaé Ventures, Anthology Fund, Jasmine Tookes, Karlie Kloss and Jenny Fleiss among others. — [TechCrunch, 16 Jun 2025](https://techcrunch.com/2025/06/16/alta-raises-11m-to-bring-clueless-fashion-tech-to-life-with-all-star-investors/); [TechCrunch, 14 Feb 2026](https://techcrunch.com/2026/02/14/clueless-inspired-app-alta-partners-with-brand-public-school-to-start-integrating-styling-tools-into-websites/)
- **Series A, Oct 2026:** $20M led by Forerunner, which pre-empted the round. Menlo and Aglaé returned. Angels include Meredith Koop and Karlie Kloss. The team is **10 people**. Valuation was not disclosed. — [Fortune, 22 Oct 2026](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/)

#### Daydream
- **Entity:** the App Store seller is DAHLIA LABS, INC. — [iTunes lookup](https://itunes.apple.com/lookup?id=6692634357&country=us)
- **Founder background:** Julie Bornstein, ex-Stitch Fix COO and co-founder of The Yes. — [TechCrunch, 20 Jun 2024](https://techcrunch.com/2024/06/20/former-stitch-fix-coo-julie-bornstein-secures-50m-to-build-a-new-age-e-commerce-search-engine)
- **Seed round, June 2024:** $50M co-led by Forerunner and Index, with GV and True Ventures. 23 employees, planning for 35 by end-2024. — [TechCrunch, 20 Jun 2024](https://techcrunch.com/2024/06/20/former-stitch-fix-coo-julie-bornstein-secures-50m-to-build-a-new-age-e-commerce-search-engine)
- **SEC confirmation:** see section 4.

#### Gensmo
- **Entity:** the App Store seller is Serendipity One Inc. — [iTunes lookup](https://itunes.apple.com/lookup?id=6636520663&country=us)
- **Funding and founder:** "over 60 million dollars" in seed funding, announced June 2025, roughly six months after the December 2024 launch. Founder and CEO is Ning Hu (ex-Google, ex-Alibaba VP). — [FashionUnited, 27 Jun 2025](https://fashionunited.com/news/business/ai-powered-fashion-agent-gensmo-secures-60-million-dollars-in-funding/2025062766848)
- Some coverage calls the round "angel" funding. — [Pulse 2.0](https://pulse2.com/gensmo-over-60-million-angel-funding-raised-for-transforming-fashion-e-commerce)

#### Style DNA
- **Entity and HQ:** AI Style by DNA S.L. (Spain), with headquarters in Barcelona and London, founded 2019. — [Mercado&Consumo, May 2026](https://mercadoeconsumo.com.br/22/05/2026/inovacao/style-dna-um-personal-stylist-com-ia-que-cria-um-perfil-de-estilo-a-partir-de-uma-selfie/)
- **Seed round:** €1.3M in August 2023 from Black River Ventures and the Cyprus Business Angels Network. — [Startup Hub Catalonia](https://startupshub.catalonia.com/startup/barcelona/style-dna/4776)
- **Conflicting totals:** other aggregators list $1.4M, $3.2M or $4.11M. — [VCBacked](https://www.vcbacked.co/company/style-dna); [Welcome to the Jungle](https://app.welcometothejungle.com/companies/Style-DNA); [CB Insights](https://www.cbinsights.com/company/style-dna/financials)

#### Cladwell
- **Founding and early funding:** Cincinnati. The 2017 raise was $2.8M from Midwest angels and 500 Startups. — [TechCrunch, 2017](https://techcrunch.com/2017/10/02/cladwell-helps-you-pick-out-your-clothes)
- **Sale:** sold in August 2019 to Erin Flynn and Colin Flynn after raising $3.5–3.8M in total. The founders "hit a growth ceiling". — [TheyGotAcquired (updated 5 Apr 2022)](https://theygotacquired.com/saas/cladwell-acquired/)
- **Current site:** "Since 2016 · Cincinnati, Ohio". — [cladwell.com](https://cladwell.com)

#### Save Your Wardrobe
- **Entity:** SAVE YOUR WARDROBE LTD., company no. 10831653, incorporated 22 Jun 2017. Office at 86–90 Paul Street, London EC2A 4NE. Directors and PSCs are Hasna Kourdaa and Mehdi Doghri. — [Companies House](https://find-and-update.company-information.service.gov.uk/company/10831653)
- **Seed round:** $3M in July 2022. — [Just Style](https://www.just-style.com/news/save-your-wardrobe-secures-3m-to-drive-eu-expansion)
- **Programmes and awards:** LVMH Innovation Award 2023. — [FashionUnited 2025](https://fashionunited.uk/news/business/save-your-wardrobe-acquires-french-aftersales-platform-les-raccommodeurs/2025093084172)
- Earlier: Centre for Fashion Enterprise, Fashion for Good Plug and Play, and Farfetch Dream Assembly. — [UAL / LCF (via search)](https://www.arts.ac.uk/colleges/london-college-of-fashion/stories/meet-the-fash-tech-stars-of-the-future-save-your-wardrobe)
- **Acquisition:** acquired Les Raccommodeurs (France) in September 2025. — [FashionUnited](https://fashionunited.uk/news/business/save-your-wardrobe-acquires-french-aftersales-platform-les-raccommodeurs/2025093084172)

#### Combyne
- **Location:** combyne GmbH, Germany (aggregators say Munich). — [iTunes lookup](https://itunes.apple.com/lookup?id=989727742&country=us); [nordic9](https://nordic9.com/companies/combyne/)
- **Funding:** €1.5M in July 2018. Nordic9 lists $6.8M total investment. — [nordic9](https://nordic9.com/news/combyne-raised-15m/)
- CB Insights lists a $2.2M seed in March 2020, with Plug and Play among the investors. — [CB Insights (via search)](https://www.cbinsights.com/company/combyne/financials)

#### Others
- **Stylebook:** left brain / right brain, LLC, "Founded 2009". — [stylebookapp.com](https://www.stylebookapp.com)
- **Smart Closet:** Rabbit Tech Inc. — [smartcloset.me](https://smartcloset.me)
- **Pureple:** ICECLIP LLC ("Iceclip LLC" on Play). — [Google Play](https://play.google.com/store/apps/details?id=com.app.pureple&hl=en&gl=US)
- **Fits:** L. & J. Henne UG (Germany). — [iTunes lookup](https://itunes.apple.com/lookup?id=6447482321&country=us)
- **Dressly:** TIKVEX LIMITED (AppGoblin flags it as Cyprus). — [AppGoblin](https://appgoblin.info/apps/6738174180)
- **Essembl:** Essembl SA (AppGoblin flags it as Luxembourg). — [AppGoblin](https://appgoblin.info/apps/6479642952)
- **Doji (AI avatar try-on):** $14M seed in May 2025 led by Thrive Capital. — [TechCrunch, 15 May 2025](https://techcrunch.com/2025/05/15/doji-raises-14m-to-make-virtual-try-ons-fun-through-ai-avatars)

### Inferences
- **Sub-scale outcomes.** The two companies with the longest public operating history and the cleanest consumer-subscription model (Cladwell, Stylebook) never raised meaningful venture money. Cladwell's 2019 sale at about $330K ARR is the clearest data point on where pure consumer wardrobe subscriptions plateau without a second engine.
- **Big rounds go to shopping companies.** The largest rounds (Gensmo $60M+, Daydream about $50M) went to AI shopping agents with commerce monetisation, not to closet-cataloguing apps.

### Gaps
- No valuations are disclosed for any company.
- Indyx's and Alta's legal funding history is not in SEC filings (see section 4).
- Gensmo's investors are not named.
- LinkedIn headcounts were not collected (login required).

---

## 4. Public filings: UK Companies House, SEC Form D, Korean DART

### Takeaway
The filings show the economics plainly.
- **Whering** ran on thin cash. It held **£11,007 at 30 Jun 2025**, had net liabilities of £347K, owed **£236K in taxes and social security** and £203K in director loans, and lost about £0.97–1.14M a year. This was before the July 2026 $7M round. On the share register at 14 Oct 2026 the founder held about **74.5%** of ordinary shares, and no eBay or Google entity appeared.
- **Save Your Wardrobe** loses about **£1.75M a year**, has £5.57M of convertible notes awaiting a Series A, and held £1.14M cash at 30 Jun 2025.
- **Daydream's** $50M seed is confirmed by two SEC Form Ds totalling **$49.66M**.
- No Form D was found for Indyx, Alta (Flagship AI), Gensmo (Serendipity One), Cladwell or OpenWardrobe, and no DART filings for Looko.

### Cited Findings

#### Whering Ltd (12660509). Accounts are filleted, small-company "total exemption full" accounts with no profit and loss account published.
- **Balance sheet, 30 Jun 2025** (comparatives 30 Jun 2024). — [FY2025 accounts (iXBRL)](https://find-and-update.company-information.service.gov.uk/company/12660509/filing-history/MzUxMzY4MTU1MmFkaXF6a2N4/document?format=xhtml&download=1)
  - Cash £11,007 (£14,989); debtors £123,156 (£71,869).
  - Creditors due within one year £483,072 (£318,717), of which:
    - taxation and social security £236,466 (£128,000);
    - loans from directors £203,073 (£145,554);
    - trade creditors £13,287.
  - Net liabilities £(347,324) (£(227,448)).
  - Share premium £3,534,957 (£2,682,416); profit and loss reserve £(3,893,025) (£(2,920,608)).
  - Average employees 8 (10).
- **Earlier years.**
  - 30 Jun 2023: cash £188,651; 30 Jun 2022: £793,015. Net assets £153,121 in 2023.
  - Profit and loss reserve: £(1,903,034) in 2023 and £(764,276) in 2022.
  - Share premium: £2,046,514 in 2023 and £1,519,653 in 2022.
  - Average employees: 6 in FY2023 and 5 in FY2022.
  - — [FY2023 accounts](https://find-and-update.company-information.service.gov.uk/company/12660509/filing-history/MzQwOTA0OTAzNmFkaXF6a2N4/document?format=xhtml&download=1); [FY2024 accounts](https://find-and-update.company-information.service.gov.uk/company/12660509/filing-history/MzQ0NTYzNTI3MmFkaXF6a2N4/document?format=xhtml&download=1)
- **Implied annual losses** (movement in the profit and loss reserve; my arithmetic): FY2023 about £1.14M, FY2024 about £1.02M, FY2025 about £0.97M. — derived from the accounts above
- **Confirmation statement, 14 Oct 2026** (filed 15 Oct 2026). — [CS01 PDF](https://find-and-update.company-information.service.gov.uk/company/12660509/filing-history/MzU1MTIxNjQ0M2FkaXF6a2N4/document?format=pdf&download=0)
  - Share capital: 10,744,000 ordinary and 1,229,000 deferred shares.
  - Main holders: Bianca Rangecroft 8,000,000 ordinary; SBC Commerce 1719 B.V. 870,000; Jacobo Koenig 771,000 ordinary plus all 1,229,000 deferred; Tredoux Capital 235,000; Thomas Vles 156,000; Jeremy Coller 117,000; Hamble Angel Programme III LLC 58,000; Ascanio Piersanti Fendi 59,000; plus smaller angels.
  - No eBay or Google entity is listed.
- **14 Oct 2026 filings:** a share subdivision (SH02, effective 2 Dec 2025); resolutions authorising allotment and removing pre-emption rights; variation of share rights (SH10); and a share-class name change (SH08). — [Filing history](https://find-and-update.company-information.service.gov.uk/company/12660509/filing-history)
- **Governance signals.**
  - A First Gazette notice for compulsory strike-off (31 Aug 2021) was discontinued on 22 Sep 2021.
  - Statements of capital for allotments made in Nov 2023–May 2024 were filed only on 12 Feb 2026.
  - Tracy Guarino was appointed director on 31 Jan 2024, but the appointment was filed in Aug 2025.
  - — [Filing history](https://find-and-update.company-information.service.gov.uk/company/12660509/filing-history)

#### Save Your Wardrobe Ltd (10831653)
- **FY2025 (year to 30 Jun 2025; comparatives FY2024)**, from filleted small-company accounts with the full balance sheet and notes. — [FY2025 accounts (iXBRL)](https://find-and-update.company-information.service.gov.uk/company/10831653/filing-history/MzUyMzcxMzgwMGFkaXF6a2N4/document?format=xhtml&download=1); [FY2024 accounts](https://find-and-update.company-information.service.gov.uk/company/10831653/filing-history/MzQ3MTY2NjA3MmFkaXF6a2N4/document?format=xhtml&download=1)
  - Loss for the year £1,746,199 (FY2024 £1,777,226; FY2023 £1,450,360).
  - Cash £1,141,469 (£6,943); stocks £208,020 (£134,848); corporation tax recoverable £185,580 (£117,710).
  - Creditors within one year £3,599,634 (£2,491,146); creditors after one year £2,050,292 (nil).
  - Net liabilities £(3,772,449) (£(2,027,757)); share premium £2,679,514 (unchanged); accumulated losses £(6,489,265).
  - Average employees 9 (10).
- **Going-concern note:** "£5,568,293 of convertible loan notes outstanding as at 30 June 2025… signed conversion agreements are in place with all noteholders… will convert into equity upon the close of the company's Series A funding round". Directors forecast sufficient cash for at least 18 months "without reliance on Series A fundraising proceeds". — [FY2025 accounts](https://find-and-update.company-information.service.gov.uk/company/10831653/filing-history/MzUyMzcxMzgwMGFkaXF6a2N4/document?format=xhtml&download=1)
- **Confirmation statement, 21 Jun 2026.** — [CS01 PDF](https://find-and-update.company-information.service.gov.uk/company/10831653/filing-history/MzUyOTQxODMxOWFkaXF6a2N4/document?format=pdf&download=0)
  - 2,356,031 ordinary shares.
  - Holders include Hasna Kourdaa 500,000; Mehdi Doghri 500,000; Ismael Sassi 348,976; Banque Heritage SA 109,375; Farah Mami 104,375; Beechrock Holdings 79,369; **Farfetch UK Limited 45,345**.

#### SEC EDGAR
- **Dahlia Labs, Inc. (Daydream), CIK 0002008800**, Delaware, incorporated 2023, Hillsborough CA. Two Form D filings dated 24 Jun 2024, both equity under Rule 506(b) with revenue range "Decline to Disclose".
  - (a) First sale 15 Dec 2023. Offering $18,000,000; **sold $17,994,964**; 30 investors.
  - (b) First sale 4 Jun 2024. Offering $32,000,000; **sold $31,668,301**; 7 investors.
  - Related persons: Julie Bornstein (executive officer and director), Kirsten Green, Jon Callaghan and Vlad Loktev (directors).
  - — [Form D 0002008800-24-000002](https://www.sec.gov/Archives/edgar/data/2008800/000200880024000002/0002008800-24-000002-index.htm); [Form D 0002008800-24-000003](https://www.sec.gov/Archives/edgar/data/2008800/000200880024000003/0002008800-24-000003-index.htm)
- **No Form D found** in EDGAR company search or full-text search (forms=D) for "Indyx", "Flagship AI" (only the unrelated Whale Rock Flagship (AI) Fund), "Serendipity One", "Cladwell" or "OpenWardrobe" (25 Oct 2026). — [EDGAR full-text search](https://efts.sec.gov/LATEST/search-index?q=%22Indyx%22&forms=D)

#### Korea (DART)
- A DART disclosure search for "룩코" (Looko), covering 1 Jan 2020 to 25 Oct 2026, returned "조회 결과가 없습니다" (no results). — [DART](https://dart.fss.or.kr/dsab007/main.do)

### Inferences
- **Whering ran on near-zero cash before the 2026 round.** Its rising tax and social-security creditor and director loans are typical of a startup bridging on deferred PAYE. "Nearly $14M total" is hard to reconcile with about £3.55M of share capital plus premium on the balance sheet at June 2025 plus the $7M round. The gap could be grants, unconverted instruments or rounding in press figures.
- **The eBay and Google money had not been issued as shares by 14 Oct 2026.** Their absence from the register, together with the 14 Oct 2026 rights-variation and new-class resolutions, suggests either an advance subscription or SAFE-style instrument or a pending allotment.
- **SYW has burned about £5M over three years** on a sub-10-person team. The R&D tax credit (corporation tax recoverable) and the convertible notes are its financing. The B2B pivot is the bet that unlocks the Series A.
- **Daydream's Form D** shows a small investor count (7) in the main $31.7M close, consistent with an institutionally led round (Forerunner, Index, GV, True).

### Gaps
- UK small-company accounts omit turnover, so Whering's and SYW's revenue cannot be determined from filings.
- I did not read the scanned PDF pages of Whering's SH01s or resolutions; the share prices per allotment are therefore not captured.
- US companies raising under Rule 506 are not obliged to file a Form D promptly, and some never file. The absence of Form Ds for Alta, Indyx and Gensmo is not evidence that no funding took place.

---

## 5. Traction: users, MAU, items, downloads, revenue estimates, ratings

### Takeaway
User counts are self-reported and inflated by cumulative sign-ups. The headline claims are:
- Whering: **10M+ users**.
- Acloset: **8–9M cumulative users** and **110M items**, plus a claim of break-even and revenue growing 70–120% year on year.
- Style DNA: **3.2M downloads, 300K actives and 70K paying subscribers** (mid-2024).
- Alta: **700M outfits and 20M avatars**.
- Daydream: **1.5M shoppers**.
- Pureple: **3–4M users**.

App Store rating counts (US) as a proxy: Combyne 87.7K, Dressly 32.7K, Alta 17.4K, Whering 12.0K, Stylebook 8.9K, Style DNA 7.5K, Pureple 6.1K, Fits 5.7K, Acloset 5.5K, Smart Closet 4.3K, Indyx 1.6K, Gensmo 1.8K, Cladwell 1.0K, SYW 281, Daydream 79.

### Cited Findings

#### Company-reported traction
- **Whering.**
  - "Join over 10 million people". — [whering.co](https://www.whering.co)
  - Survey results: 84% of users say they wear their clothes more often, nearly 70% have reduced fast-fashion purchases, and over a third save £100–£300 a year. — [FashionUnited, Jul 2026](https://fashionunited.com/news/business/whering-secures-funding-from-ebay-ventures-and-google-ai-futures/2026070773352)
  - The App Store description claims "9 M+ users". — [iTunes lookup](https://itunes.apple.com/lookup?id=1519461680&country=us)
  - In 2023 the company had 1M+ users. — [Drapers](https://www.drapersonline.com/people/30under30-2023/drapers-30-under-30-2023-bianca-rangecroft-founder-and-ceo-whering)
- **Acloset.**
  - "누적 사용자 900만 명, 등록된 의류 1.1억 개… 매주 100만 개 이상의 새로운 의류… 지난 9월 손익분기점(BEP)도 넘어섰고, 매출은 전년 동월 대비 70~120% 가량… 성장". In English: 9M cumulative users and 110M registered garments, with more than 1M new garments a week; the company passed break-even "last September" and revenue is growing about 70–120% year on year. — [Wanted job post 391802 (undated, closed)](https://www.wanted.co.kr/wd/391802)
  - An earlier post: 4.5M members and 80M garments. — [Wanted 343246](https://www.wanted.co.kr/wd/343246)
  - Website (Oct 2026): "8M+ Users Worldwide · 110M+ Clothes Digitized · 150+ Countries". — [acloset.app](https://www.acloset.app)
  - May 2022: 700K users, 14M+ items, 60K uploads a day. — [VentureSquare](https://www.venturesquare.net/854274/)
  - The App Store description still says "4 million users" (stale). — [iTunes lookup](https://itunes.apple.com/lookup?id=1542311809&country=us)
- **Alta.**
  - 700M outfits generated, 20M avatars, 12 in-house models. — [Fortune, 22 Oct 2026](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/)
  - More than 100M outfits by Feb 2026. — [TechCrunch](https://techcrunch.com/2026/02/14/clueless-inspired-app-alta-partners-with-brand-public-school-to-start-integrating-styling-tools-into-websites/)
  - "Over 270,000 outfits a day" two months after launch. — [Menlo](https://menlovc.com/perspective/agentic-styling-and-shopping-why-were-backing-alta/)
- **Daydream.** A Sep 2026 press release claims more than 1.5M shoppers and 3M products. — [PR Newswire via Morningstar, 30 Sep 2026 (summary only; full text returned 403)](https://www.morningstar.com/news/pr-newswire/20260930ny59906/daydream-app-brings-conversational-support-and-visual-shopping-experiences-to-iphone-users-with-new-ios-27-capabilities)
- **Style DNA.**
  - "Trusted by over 3 million users". — [iTunes lookup](https://itunes.apple.com/lookup?id=1358319821&country=us)
  - Mid-2024: 3.2M downloads, 300K active users and **70K paying subscribers**. — [Mercado&Consumo, May 2026](https://mercadoeconsumo.com.br/22/05/2026/inovacao/style-dna-um-personal-stylist-com-ia-que-cria-um-perfil-de-estilo-a-partir-de-uma-selfie/)
- **Indyx:** "tens of thousands of real digital closets and over 10 million… digitized items". — [myindyx.com](https://www.myindyx.com)
- **Pureple:** "Trusted by 3M+ people" (App Store) vs "Join 4M+ users" (website). — [iTunes lookup](https://itunes.apple.com/lookup?id=628106373&country=us); [pureple.com](https://www.pureple.com)
- **Fits:** "Loved by over 2 million users". — [fits-app.com](https://www.fits-app.com/fits-vs-acloset)
- **Gensmo:** in beta, it generated 10M style matches and indexed 100M products. — [FashionUnited](https://fashionunited.com/news/business/ai-powered-fashion-agent-gensmo-secures-60-million-dollars-in-funding/2025062766848)
- **Cladwell (historic).** Peak subscription revenue "nearly $1M". At the 2019 sale: about $330K ARR, nearly 10,000 paid subscribers and a 100K+ email list. — [TheyGotAcquired](https://theygotacquired.com/saas/cladwell-acquired/)
- **Combyne:** an aggregator estimates 2024 revenue at about $612K (2023: about $484K), with a team of 5. **Low reliability.** — [Latka (via search)](https://getlatka.com/companies/combyne)

#### Store proxies (25 Oct 2026)

| App | US ratings (avg) | GB ratings | Google Play installs / reviews / stars |
|---|---|---|---|
| Combyne | 87,744 (4.76) | 7,949 | 10M+ / 232K / 4.4 |
| Dressly | 32,661 (4.40) | 1,201 | 100K+ / 12.4K / 4.4 |
| Alta | 17,389 (4.89) | 1,505 | 500K+ / – / – |
| Whering | 11,997 (4.67) | 3,080 | 5M+ / 27.9K / 4.4 |
| Stylebook | 8,865 (4.68) | 1,447 | n/a (iOS only) |
| Style DNA | 7,521 (4.29) | 1,112 | 1M+ / 17.9K / 3.5 |
| Pureple | 6,119 (3.94) | 649 | 1M+ / 4K / 2.5 |
| Fits | 5,685 (4.59) | 923 | 500K+ / 15.1K / 4.7 |
| Acloset | 5,464 (4.45) | 662 | 5M+ / 31.1K / 4.5 |
| Essembl | 4,789 (4.54) | 437 | 1M+ / 12.4K / 4.4 |
| Smart Closet | 4,345 (4.41) | 745 | – |
| Gensmo | 1,760 (4.68) | 9 | – |
| Indyx | 1,636 (4.77) | 246 | 100K+ / 1.31K / 4.5 |
| Cladwell | 1,036 (4.25) | 55 | 100K+ / 518 / 4.0 |
| Save Your Wardrobe | 281 (4.51) | 758 | 100K+ / 441 / 3.8 |
| OpenWardrobe | 171 (4.02) | 31 | 100K+ / 667 / – |
| Daydream | 79 (4.19) | 0 | – |

Sources: iTunes Lookup ([US](https://itunes.apple.com/lookup?id=1519461680,1542311809,1599179405,335709058,1140550878,1198057728,628106373,989727742,1485757044,6481705400,6692634357,1358319821,6636520663,6447482321,6738174180,6479642952,1549029468&country=us), [GB](https://itunes.apple.com/lookup?id=1519461680,1542311809,1599179405,335709058,1140550878,1198057728,628106373,989727742,1485757044,6481705400,6692634357,1358319821,6636520663,6447482321,6738174180,6479642952,1549029468&country=gb)); Google Play listings ([Whering](https://play.google.com/store/apps/details?id=com.whering.app), [Acloset](https://play.google.com/store/apps/details?id=com.looko.acloset), [Combyne](https://play.google.com/store/apps/details?id=com.combyne.app), [Alta](https://play.google.com/store/apps/details?id=com.alta), [Style DNA](https://play.google.com/store/apps/details?id=style.dna.app), [Fits](https://play.google.com/store/apps/details?id=de.lhenne.fits), [Pureple](https://play.google.com/store/apps/details?id=com.app.pureple), [Indyx](https://play.google.com/store/apps/details?id=com.indyx.android), [Cladwell](https://play.google.com/store/apps/details?id=com.cladwell), [SYW](https://play.google.com/store/apps/details?id=com.saveyourwardrobe.syw), [Dressly](https://play.google.com/store/apps/details?id=world.dressly.fashion), [Essembl](https://play.google.com/store/apps/details?id=com.essembl.app), [OpenWardrobe](https://play.google.com/store/apps/details?id=com.open_wardrobe_mobile)).

#### Third-party modelled estimates (LOW confidence; AppGoblin, retrieved 25 Oct 2026)

| App | Installs | MAU | Revenue bucket | New installs, last 4 weeks |
|---|---|---|---|---|
| Whering, Android | 6.1M | 232K | "$10K+ monthly" | +139K |
| Whering, iOS | 1.9M | 86K | "<$10K" | — |
| Acloset, Android | 5.6M | 233K | "$10K+" | +193K |
| Acloset, iOS | 944K | 54K | — | — |
| Alta, iOS | 1.7M | 157K | n/a | +190K |
| Alta, Android | 753K | 88K | — | — |
| Dressly, iOS | 2.2M | 157K | "$10K+" | — |
| Combyne, Android | 19.9M | 120K | — | — |
| Combyne, iOS | 6.6M | 262K | — | — |
| Style DNA, Android | 1.7M | 79K | — | +155K |
| Fits, Android | 933K | 81K | — | — |
| Indyx, iOS | 149K | 8.1K | — | — |
| Indyx, Android | 314K | 17K | — | — |
| Cladwell, iOS | 86K | 3.5K | — | — |
| Gensmo, iOS | 95K | 6.1K | — | — |
| Daydream, iOS (listing since Nov 2025) | 4K | 440 | — | — |
| SYW, iOS | 78K | 4.2K | — | — |

Sources: [Whering Android](https://appgoblin.info/apps/com.whering.app), [Whering iOS](https://appgoblin.info/apps/1519461680), [Acloset Android](https://appgoblin.info/apps/com.looko.acloset), [Acloset iOS](https://appgoblin.info/apps/1542311809), [Alta iOS](https://appgoblin.info/apps/6481705400), [Alta Android](https://appgoblin.info/apps/com.alta), [Dressly](https://appgoblin.info/apps/6738174180), [Combyne Android](https://appgoblin.info/apps/com.combyne.app), [Combyne iOS](https://appgoblin.info/apps/989727742), [Style DNA](https://appgoblin.info/apps/style.dna.app), [Fits](https://appgoblin.info/apps/de.lhenne.fits), [Indyx iOS](https://appgoblin.info/apps/1599179405), [Indyx Android](https://appgoblin.info/apps/com.indyx.android), [Cladwell](https://appgoblin.info/apps/1140550878), [Gensmo](https://appgoblin.info/apps/6636520663), [Daydream](https://appgoblin.info/apps/6692634357), [SYW](https://appgoblin.info/apps/1485757044)

- **Adapty estimates (LOW confidence; internally inconsistent).**
  - Cladwell: "<$5K" estimated monthly revenue in the header vs "$6K a month" in the prose. — [Adapty](https://adapty.io/paywall-library/dwell/)
  - Style DNA: "$20K" monthly revenue and 20,000+ monthly downloads in the header vs "about $200K in monthly revenue and 80K monthly downloads" in the prose. — [Adapty](https://adapty.io/paywall-library/style-dna/)

### Inferences
- **"Users" usually means cumulative registrations.** Whering's 10M claim against AppGoblin's roughly 0.3M modelled MAU, and Acloset's 9M against about 0.29M modelled MAU, imply a small monthly-active share. AppGoblin's MAU models are unvalidated, though.
- **Style DNA is the only app with a disclosed paying-subscriber count.** 70K payers on 3.2M downloads is about **2.2% download-to-paid**, and 70K of 300K actives is 23% of actives (my arithmetic). At its $29.99–$39.99 annual SKUs, 70K payers would imply roughly $2–3M gross annual bookings. That figure is an inference, not reported.
- **Ratings and installs favour different apps.** Dressly's 32.7K US ratings on a weekly-subscription app are disproportionate to its 100K+ Android installs. That points to heavy iOS paid acquisition and rating prompts, typical of app-factory publishers. Combyne's large but declining base reflects a 2016-era social and gamified model.
- **Gensmo and Daydream have weak app traction relative to capital raised.** Daydream's claimed users are likely web-first.

### Gaps
- None of the companies disclose MAU, DAU, retention or paying-subscriber counts, except Style DNA (mid-2024) and Cladwell (2019).
- No revenue figures exist for Whering, Alta, Daydream, Gensmo, Indyx or SYW.
- I found no free Sensor Tower, Appfigures or AppMagic numbers.
- The date of Acloset's break-even month ("last September") is not specified in the job post.

---

## 6. Growth and marketing: channels, ASO, PR, partnerships, retention levers

### Takeaway
Growth in this category is overwhelmingly **organic and press-led**:
- the *Clueless* closet narrative;
- Vogue and TIME mentions;
- celebrity and stylist angels (Alta);
- TV (Whering on Dragons' Den);
- creator content: Pureple via YouTuber Safiya Nygaard, Whering's 1,361 TikToks.

Supporting levers are browser extensions, sharing and social closets, data reports (Indyx's wardrobe report) and **SEO competitor pages** (Fits). Store names converge on the keyword "AI Stylist".

### Cited Findings

#### TikTok official accounts (followers, likes, videos, read from TikTok page JSON on 25 Oct 2026)

| Account | Followers | Likes | Videos |
|---|---|---|---|
| Whering, @whering | 42,400 | 2.0M | 1,361 |
| Alta, @altadaily | 22,600 | 669.4K | 139 |
| Style DNA, @styledna.app | 18,000 | 931.8K | 698 |
| Indyx, @myindyx | 5,184 | 22.6K | 312 |
| Stylebook, @stylebookapp | 3,669 | 38.1K | 200 |
| Acloset, @acloset_app | 2,211 | 9.35K | 51 |
| Gensmo, @gensmo | 1,279 | 627 | 54 |
| Cladwell, @cladwellapp | 1,202 | 16.5K | 50 |
| Fits, @fits.app | 983 | 21.6K | 82 |
| Daydream, @daydreamshopping | 282 | 298 | 8 |

Profile URLs: [whering](https://www.tiktok.com/@whering), [altadaily](https://www.tiktok.com/@altadaily), [styledna.app](https://www.tiktok.com/@styledna.app), [myindyx](https://www.tiktok.com/@myindyx), [stylebookapp](https://www.tiktok.com/@stylebookapp), [acloset_app](https://www.tiktok.com/@acloset_app), [gensmo](https://www.tiktok.com/@gensmo), [cladwellapp](https://www.tiktok.com/@cladwellapp), [fits.app](https://www.tiktok.com/@fits.app), [daydreamshopping](https://www.tiktok.com/@daydreamshopping). The handles for Whering, Alta, Acloset, Daydream, Style DNA and Gensmo are linked from the companies' own websites.

#### PR and press
- **Whering:** "'Revolutionized the way I dress and shop' – Vogue. Also loved by The New York Times, BBC, The Guardian, The Drew Barrymore Show". — [App Store](https://itunes.apple.com/lookup?id=1519461680&country=us)
- **Whering (2021):** "we started with a small group of incredibly engaged users and have focused on organic word of mouth… rolling out influencer marketing and PR". — [Goldman Sachs Q&A](https://www.goldmansachs.com/alumni/spotlight/2024/q-a-with-bianca-rangecroft-founder-and-ceo-of-whering)
- **Whering events:** pop-ups and a month-long Soho residency. — [Drapers](https://www.drapersonline.com/people/30under30-2023/drapers-30-under-30-2023-bianca-rangecroft-founder-and-ceo-whering)
- **Alta:** "Named a TIME Best Invention of 2025", "FEATURED IN VOGUE". — [altadaily.com](https://www.altadaily.com)
- **Alta celebrity backing:** angels include Karlie Kloss, Jasmine Tookes, and Michelle Obama's stylist Meredith Koop. — [Fortune, 2026](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/)
- **Alta organic sharing:** "Users have shared their Alta-generated outfits on social media for occasions ranging from 'Costco shopping run' to 'I'm getting fired tomorrow'". — [Menlo](https://menlovc.com/perspective/agentic-styling-and-shopping-why-were-backing-alta/)
- **Pureple:** "Featured on Good Morning America… TechCrunch… E! Online… Featured by Safiya Nygaard · 11M+ views". — [pureple.com](https://www.pureple.com)
- **Stylebook:** "FEATURED BY: Vogue, WSJ, Harper's Bazaar UK, New York Magazine". — [App Store](https://itunes.apple.com/lookup?id=335709058&country=us)
- **Daydream (platform partnership):** shipped iOS 27 Apple Intelligence features (shop from your camera roll, Siri search) on iOS 27 launch day. — [TechCrunch, 14 Sep 2026](https://techcrunch.com/2026/09/14/fashion-discovery-app-daydream-uses-apple-intelligence-to-help-you-shop-the-outfits-saved-in-your-camera-roll/)
- **Acloset:** Google Play Editors' Choice. — [Google Play](https://play.google.com/store/apps/details?id=com.looko.acloset&hl=en&gl=US)
- **Industry partnerships as PR:** Alta with the CFDA. — [WWD](https://wwd.com/fashion-news/fashion-features/cfda-partnership-alta-personal-styling-app-ai-1237089028/)

#### Product-led acquisition and retention levers
- **Browser extensions.** Whering has a Chrome extension "digitise as you go". — [whering.co](https://www.whering.co)
- Acloset has a Chrome extension to "Shop with your closet open". — [acloset.app](https://www.acloset.app)
- **Social and shared closets.** Whering offers style submissions, friends' wardrobes and "Unpacked", "a seasonal look at what you wore", a Spotify Wrapped-style recap. — [Whering help: Unpacked](https://whering.zendesk.com/hc/en-gb/articles/5695361150111)
- Indyx offers Open Closets and closet sharing. — [App Store](https://itunes.apple.com/lookup?id=1599179405&country=us)
- Combyne runs daily outfit challenges with Superlikes. — [App Store](https://itunes.apple.com/lookup?id=989727742&country=us)
- **Content and education.** Indyx runs a free 8-week Style Workshop. — [App Store](https://itunes.apple.com/lookup?id=1599179405&country=us)
- Indyx's data report "based on real, aggregated closet data". — [myindyx.com](https://www.myindyx.com)
- **SEO competitor pages.** Fits publishes "Fits vs Alta / Acloset / Whering / Indyx / Stylebook / SimpleCloset" pages with feature and price tables. — [fits-app.com/fits-vs-acloset](https://www.fits-app.com/fits-vs-acloset)
- **Sustainability narrative.** Whering cites survey results: 84% wear clothes more and about 70% buy less fast fashion. — [FashionUnited](https://fashionunited.com/news/business/whering-secures-funding-from-ebay-ventures-and-google-ai-futures/2026070773352)
- SYW cites Good On You ratings. — [App Store](https://itunes.apple.com/lookup?id=1485757044&country=us)
- **ASO naming.**
  - The localised names swap "Closet" in the US for "Wardrobe" in GB: Whering is "Your Digital Closet" (US) and "Your Digital Wardrobe" (GB); Style DNA is "AI Stylist & Closet" (US) and "AI Stylist Wardrobe" (GB).
  - Titles lead with "AI Stylist": Cladwell "AI Stylist & Outfits", Style DNA, Gensmo "AI Stylist & Try-On", Pureple "AI Outfit Planner", Acloset "AI Fashion Assistant", Alta "Digital AI Closet".
  - — [iTunes lookup US](https://itunes.apple.com/lookup?id=1519461680,1358319821,1140550878,6636520663,628106373,1542311809,6481705400&country=us); [GB](https://itunes.apple.com/lookup?id=1519461680,1358319821&country=gb)
- **Referral programmes:** Whering's help centre returns no results for "refer". — [Whering help search](https://whering.zendesk.com/hc/en-gb)

### Inferences
- **Whering's TikTok volume tracks its user base.** It is the only scaled consumer brand with a high-volume TikTok cadence (1,361 posts), consistent with its Gen Z skew.
- **Alta's growth is press and celebrity-led, with a viral product loop.** It generates many likes per video (about 4.8K per video vs Whering's about 1.5K).
- **AI shopping agents have weak owned social.** Daydream and Gensmo have negligible TikTok audiences despite $50–60M raised. They grow through press, platform partnerships (Apple) and web search, not creator social.
- **Fits shows a cheap indie playbook.** SEO comparison pages and an MCP integration on its help site are low-cost acquisition levers available to a new entrant.

### Gaps
- Paid acquisition spend, CAC, influencer contract terms and referral mechanics are not public for any app.
- Instagram follower counts were not collected (login walls).
- Keyword-ranking data is not covered here; another researcher is collecting store metadata.

---

## 7. Unit economics signals: AI generation cost vs price, margins, price changes and user reaction

### Takeaway
At public API prices, an AI try-on costs about **$0.04–$0.075 per image**: Gemini 2.5 Flash Image at about $0.039, and FASHN try-on at $0.075 on demand or $0.049–$0.068 on commitment tiers. Retail credit prices range from **about $0.07 (Whering help-centre GB pack) to $0.50 (Gensmo)**. That leaves positive but sometimes thin gross margin after Apple's 15–30% cut, especially because **regenerations are charged** (Whering).

The apps control AI costs in three ways: monthly allowances (Cladwell; Fits at 30 credits a month), fair-use clauses (Indyx), and per-use credits (Whering, Gensmo).

### Cited Findings

#### Generation cost anchors
- **FASHN API:**
  - "On-Demand $0.075 per credit"; "Virtual Try-On (v1.6): 1" credit per output; "Failed predictions do not consume credits".
  - Tiers: $19 a month with 282 credits and top-ups at $0.0675; $249 a month with 4,150 credits and $0.06; $1,249 a month with 25,594 credits and $0.0488.
  - — [FASHN API pricing](https://help.fashn.ai/plans-and-pricing/api-pricing)
- **Gemini 2.5 Flash Image:** $0.039 per output image, per third-party trackers (Future AGI says "last verified Jun 2, 2026"). Resellers range from $0.020 on fal.ai to $0.040 on Replicate. — [Future AGI](https://futureagi.com/llm-cost-calculator/google/gemini-2-5-flash-image); [Price Per Token](https://pricepertoken.com/image/model/google-gemini-2-5-flash-image)
- **Apple's commission:** 15% for developers earning up to $1M a year under the Small Business Program, 30% otherwise. — [Apple Small Business Program](https://developer.apple.com/app-store/small-business-program/)

#### Consumer price per AI unit (store prices, 25 Oct 2026)

| App / pack | Price per credit |
|---|---|
| Whering US, 10 for $2.99 | $0.30 |
| Whering US, 100 for $12.99 | $0.13 |
| Whering GB store, 10 for £1.99 | £0.20 |
| Whering GB store, 100 for £9.99 | £0.10 |
| Whering help centre, 100 for £6.99 | **£0.07** |
| Gensmo, flat | **$0.50** |
| Fits, 5 for $1.99 | $0.40 |
| Fits, 20 for $4.99 | $0.25 |
| Fits, 100 for $19.99 | $0.20 |
| Fits, 500 for $79.99 | $0.16 |
| Acloset beans, 100 for $1.99 | $0.02 per bean (bean cost per feature not public) |
| Acloset beans, 1,200 for $19.99 | $0.017 per bean |

Sources: [Whering US](https://apps.apple.com/us/app/id1519461680); [Whering GB](https://apps.apple.com/gb/app/id1519461680); [Whering help](https://whering.zendesk.com/hc/en-gb/articles/5800092986143-What-are-the-top-up-plans-for-Virtual-Try-On-credits); [Gensmo](https://apps.apple.com/us/app/id6636520663); [Fits](https://apps.apple.com/us/app/id6447482321); [Acloset](https://apps.apple.com/us/app/id1542311809)

#### Cost-control mechanics
- **Whering** charges a credit for each regeneration as well as each try-on. — [Whering help](https://whering.zendesk.com/hc/en-gb/articles/5800046039327-How-much-does-each-Virtual-Try-On-cost)
- **Cladwell:** "AI features include a monthly allowance". On the web FAQ, AI pauses "until usage becomes available again. You can still view your saved closet and outfits." — [App Store](https://itunes.apple.com/lookup?id=1140550878&country=us); [cladwell.com/pricing](https://cladwell.com/pricing)
- **Fits Pro** includes "30 AI credits per month to be used for image enhancements and virtual try-on". — [Fits help](https://www.fits-app.com/knowledge/free-vs-paid-features)
- **Indyx:** "Unlimited enhanced flatlays… *subject to Indyx Terms of Service, which help us support all users fairly and reliably". — [myindyx.com](https://www.myindyx.com)
- **Acloset (storage costs):** archived items "require storage and management within our system, contributing to the operational costs". — [Acloset announcement, Dec 2023](https://www.acloset.app/announcements/subscription-plans-overview-eb9fc8/)
- **Alta** trained "12 in-house AI models". Funds go to "AI infrastructure". — [Fortune, 2026](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/)

#### Margins
- **Acloset** reports reaching break-even ("손익분기점(BEP)") in an unspecified September, with revenue up 70–120% year on year. — [Wanted 391802](https://www.wanted.co.kr/wd/391802)
- **Whering** made implied annual losses of about £1M (FY2023–FY2025). — [Companies House accounts](https://find-and-update.company-information.service.gov.uk/company/12660509/filing-history)
- **SYW** lost £1.45M, £1.78M and £1.75M in FY2023, FY2024 and FY2025. — [Companies House accounts](https://find-and-update.company-information.service.gov.uk/company/10831653/filing-history)

#### Price changes and user reaction
- **Acloset (Dec 2023):** moved to Basic, Premium and Expert caps, with 40–60% loyalty discounts. — [Acloset](https://www.acloset.app/announcements/subscription-plans-overview-eb9fc8/)
  - Review, Aug 2026: "100 items before telling you that it wants some outrageous amount". — [Google Play](https://play.google.com/store/apps/details?id=com.looko.acloset&hl=en&gl=US)
  - Review, Apr 2026: "the high price of the app… This app was way better before they added so much AI ('stylist')". — [Google Play](https://play.google.com/store/apps/details?id=com.looko.acloset&hl=en&gl=US)
- **Pureple:** moved from ads plus a $2.99 one-time Pro to subscriptions that gate AI. Play rating is now 2.5 stars. Review: "the paid version locks the AI outfits". — [Google Play](https://play.google.com/store/apps/details?id=com.app.pureple&hl=en&gl=US)
- **Style DNA:** review, Sep 2024: "I paid for a subscription but was hit immediately with hidden fees… still CHARGED for a quarterly renewal". Play rating 3.5. — [Google Play](https://play.google.com/store/apps/details?id=style.dna.app&hl=en&gl=US)
- **Whering:** moved from VIP membership (2021) to Supporter tiers to credits only (2026). Review, Aug 2026: "no paywall". — [TheIndustry 2021](https://www.theindustry.fashion/the-interview-bianca-rangecroft-founder-of-whering/); [Whering help](https://whering.zendesk.com/hc/en-gb/articles/5928286841375-How-do-I-cancel-my-subscription); [Google Play](https://play.google.com/store/apps/details?id=com.whering.app&hl=en&gl=US)
- **Cladwell:** "a few dollars per month" in 2017 became $9.99 a month or $79.99 a year on the web by 2026. — [TechCrunch 2017](https://techcrunch.com/2017/10/02/cladwell-helps-you-pick-out-your-clothes); [cladwell.com/pricing](https://cladwell.com/pricing)

### Inferences
- **Whering's cheapest pack is near break-even at list API prices.** At £0.07 per credit (about $0.09) minus Apple's 15%, Whering nets about $0.077 per try-on. That roughly equals FASHN's $0.075 on-demand price, and regenerations raise usage per saved look. A self-hosted or Gemini-class model at about $0.04 or less is needed for healthy margin. Gensmo's $0.50 per credit has wide margin but low adoption.
- **The pricing unit is moving from storage to AI usage.** Item caps (Acloset) generate backlash. AI allowances (Fits, Cladwell) and credits (Whering) align price with marginal cost and are better tolerated.
- **Free unlimited AI is a funded strategy, not a business model.** Alta's free, unlimited avatar try-ons are sustainable only with VC money and B2B or affiliate monetisation.

### Gaps
- No company discloses its per-generation inference cost, gross margin, LTV, CAC or churn.
- The models each app actually uses (in-house, Gemini, FASHN or others) are not public. The cost anchors above are market prices, not their costs.
- Acloset's bean-to-feature price list is not public; the Notion help pages failed to render.

---

## 8. Cross-app synthesis: comparison, what works, what failed, most viable model for 2026

### Takeaway
No pure digital-wardrobe subscription has been shown to reach venture scale. Cladwell peaked near $1M a year, and Style DNA's 70K payers are the best disclosed result. The models attracting capital in 2026 are:
1. **AI stylist and avatar platforms that sell to brands and retailers (B2B)**, plus affiliate (Alta).
2. **AI shopping agents earning commission** (Daydream, Gensmo).
3. **Data and resale-oriented wardrobes** backed by strategic investors (Whering with eBay and Google).

Shutdowns and pivots (Doppl, The Yes, Cladwell's sale, SYW's B2B pivot, Whering dropping subscriptions) show that **consumer-only monetisation of a closet is hard**: the activation hurdle is high and usage is low-frequency.

### Cited Findings

#### Comparison table (Oct 2026)

| App | Consumer pricing model | Key price points (US) | Free tier | Other revenue | Funding (disclosed) | Status |
|---|---|---|---|---|---|---|
| Whering | Credits (no subscription); legacy Supporter tiers | 10/100 credits $2.99/$12.99; Outfit Maker $4.99 | All standard features; unlimited outfits | Data/resale plans (eBay); affiliate (2023); no ads | $7M seed Jul 2026; "nearly $14M" total | Active, raising |
| Acloset | Tiered subscription + beans + ads (Android) | $3.99 / $9.99 / $24.99 a month; $27.99 / $59.99 / $147.99 a year | 100 items | Ads; MCP/agent platform | KRW 3.0B Series A 2022 | Active; claims break-even |
| Indyx | Membership (Insider) + human services | $12.99 a month / $74.99 a year; Lookbook $60–$150; Catalog about $295 | Unlimited items and outfits | Stylist marketplace; Archivists | Undisclosed pre-seed (2022) | Active, small |
| Stylebook | Paid upfront | $4.99 | n/a | none | none found | Maintenance (last build Jun 2025) |
| Cladwell | Subscription (weekly to annual) | Web $9.99 a month / $79.99 a year; IAP weekly $7.99 | Closet only | Shopping list | $3.5–3.8M; sold 2019 | Active, small |
| Smart Closet | Paid upfront + backup subscription | $2.99; $0.99 a month | n/a | shopping links | none found | Maintenance |
| Pureple | Weekly/monthly/yearly + ads | $6.99 a week / $14.99 a month / $89.99 a year | Limited; ads | Ads | none found | Active; 2.5 stars on Play |
| Combyne | Premium + Superlikes + ads | $2.99 / $19.99; Superlikes $0.99–$4.99 | Most features | Affiliate (50+ shops); ads | €1.5M (2018) and others | Active; iOS installs declining (AppGoblin) |
| Save Your Wardrobe | Free app | — | Free | **B2B aftersales SaaS**; service bookings | $3M seed (2022) + £5.57M convertible notes | Pivoted to B2B |
| Alta | Free | — | Unlimited | **Affiliate + B2B AI** (Rent the Runway, Gap) | $11M seed + $20M Series A | Scaling |
| Google Doppl | Free | — | — | Google Shopping | n/a | **Shut down 30 Apr 2026** |
| Daydream | Free | — | — | **Commission**; no ads | $49.66M (Form D) | Active |
| Style DNA | Subscription + one-offs | $7.99–$19.99 a month; $29.99–$39.99 a year; guides $9.99–$12.99 | Free analysis | Retail partners | €1.3M (2023) | Active |
| Gensmo | Credits | $0.50 per credit | Free browsing | Shopping | $60M+ (2025) | App last updated Jan 2026 |
| Fits | Pro subscription + credits | Pro $9.99 / $29.99 / $59.99; credits $0.16–$0.40 | Unlimited items | — | none found | Active, indie |
| Dressly | Hard paywall, weekly | $7.99–$11.99 a week; $55.99–$79.99 a year | none (implied) | — | none found | Active |

Sources: rows draw on the citations in sections 1–5, including [App Store US pages](https://apps.apple.com/us/app/id1519461680), [Companies House](https://find-and-update.company-information.service.gov.uk/company/12660509), [SEC Form D](https://www.sec.gov/Archives/edgar/data/2008800/000200880024000003/0002008800-24-000003-index.htm) and [Fortune](https://fortune.com/2026/10/22/alta-ai-fashion-app-series-a-20-million-forerunner-rent-the-runway/).

#### Failures and pivots
- **Google Doppl.** On 23 Mar 2026 Google announced it would end Doppl on 30 Apr 2026: "Thanks for exploring virtual try-on with our experimental app Doppl. Your feedback has helped us refine the technology". Virtual try-on lives on in Google product listings and image search. — [jetstream.blog, 24 Mar 2026 (quoting Google Labs on X)](https://jetstream.blog/2026/03/24/google-doppl-shut-down-end-of-april-2026/)
  - On 25 Oct 2026 the iTunes Lookup API returned 0 results for Doppl's id 6741596720, and its App Store URL returned 404. — [iTunes lookup](https://itunes.apple.com/lookup?id=6741596720&country=us)
- **The Yes.** Bornstein's previous AI fashion-shopping app was acquired by Pinterest in June 2022, and Pinterest planned "to shutter The Yes app and website". — [TechCrunch, 2 Jun 2022](https://techcrunch.com/2022/06/02/pinterest-acquires-a-i-powered-shopping-startup-the-yes-co-founded-by-former-stitch-fix-exec); [FashionUnited](https://fashionunited.uk/news/business/pinterest-acquires-shopping-platform-the-yes/2022060463442)
- **Cladwell.** Sold in 2019 at about $330K ARR. The founders "hit a growth ceiling" and were unsure how to expand beyond subscriptions "in a way that was compelling to current or new investors". — [TheyGotAcquired](https://theygotacquired.com/saas/cladwell-acquired/)
- **Finery.** A 2017 email-receipt wardrobe app (Brooklyn Decker, Whitney Casey). Dealroom lists its status as "Acquisition" with no details. — [Dealroom](https://app.dealroom.co/companies/finery); [BoF](https://www.businessoffashion.com/articles/technology/finery-aims-to-succeed-where-other-virtual-closets-have-not)
- **Save Your Wardrobe.** Pivoted from consumer to "The Aftersales Platform" for brands, after losses of about £5M in FY2023–FY2025. — [saveyourwardrobe.com](https://www.saveyourwardrobe.com); [Companies House](https://find-and-update.company-information.service.gov.uk/company/10831653/filing-history)
- **Whering.** Its VIP membership (2021) and Supporter Club (closed) gave way to free plus credits. — [TheIndustry 2021](https://www.theindustry.fashion/the-interview-bianca-rangecroft-founder-of-whering/); [Whering help](https://whering.zendesk.com/hc/en-gb/articles/5446628933663-What-are-the-benefits-of-joining-the-supporter-club)
- **The founder's diagnosis (Indyx, 2024).** "Turning a digital wardrobe into a profitable business has already eluded many spirited entrepreneurs. Digital wardrobes of the past have generally tried to make money in one of two ways [pay-to-use, affiliate]… neither has yet proven successful." — [Indyx blog](https://www.myindyx.com/blog/why-we-are-launching-insider)

### Inferences

**What works, on the evidence:**
- **A free, uncapped closet drives scale.** Whering and Indyx both chose uncapped free tiers. Whering reached the largest claimed user base and the best review sentiment on monetisation.
- **AI features are the natural paid unit,** either as credits or as an allowance inside a subscription. They carry real marginal cost and users accept paying for them.
- **B2B is where outcomes become venture-scale.** Alta's Rent the Runway deal (+35% engagement) and SYW's aftersales platform turn consumer avatar, closet and wear data into products brands pay for.
- **Hard paywalls with weekly plans produce revenue but damage ratings and trust.** Dressly, Pureple and Style DNA show this pattern. They fit an app-factory model better than a brand-building one.

**What failed or plateaued:**
- Paid-upfront apps (Stylebook, Smart Closet) are in maintenance mode.
- Pure consumer subscription capped out (Cladwell at about $330K–$1M a year).
- Item-capped freemium draws complaints (Acloset), although Acloset claims break-even, so it can work at scale with ads.
- Big-tech experiments were folded into core shopping (Doppl).
- Social or gamified closets with ads (Combyne) still have large install bases but declining momentum.

**The most viable model for a 2026 entrant (synthesis):**
1. A free, uncapped digital closet with near-zero-effort ingestion: receipts, gallery scan and browser extension, so the activation hurdle falls.
2. A paid AI layer, either an annual subscription of about $40–$80 with a monthly AI allowance, or credit packs priced at least 2–3 times the generation cost and charging for regenerations.
3. Commerce as a secondary engine: affiliate links on gap-filling and resale integrations (an eBay-style or Vinted-style listing from the closet).
4. A B2B path from day one: an embeddable try-on and styling widget, or anonymised wardrobe and wear-data insights, licensed to brands and rental or resale platforms.

Growth should lean on organic short-form video, press-friendly narratives (*Clueless*, sustainability, "wear what you own") and SEO comparison pages, and avoid weekly-plan hard paywalls that erode ratings.

### Gaps
- There is no public revenue for any current leader except the claimed break-even at Acloset. "What works" therefore rests on funding, traction proxies, filings and founder statements, not audited P&L.
- The value of B2B contracts (Alta, SYW) is undisclosed. Whether B2B revenue exceeds consumer revenue anywhere is unknown.
- I found no public data on how the shopping-agent models (Daydream, Gensmo) convert users to purchases.
