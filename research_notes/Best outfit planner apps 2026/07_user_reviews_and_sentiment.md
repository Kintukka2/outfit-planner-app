# User reviews and sentiment: outfit planner / digital wardrobe apps (as of 25 Oct 2026)

Scope: Whering, Acloset, Indyx, Stylebook, Cladwell, Smart Closet, Pureple, Combyne, Alta (Alta Daily), Google Doppl, Daydream, Style DNA, Gensmo. I added five apps with large review volume found in the same store searches: Fits, Dressly, Essembl, Closetly and GetWardrobe.

Short source labels used below:
- **"AS pull"**: my pull of the public App Store customer-reviews RSS feed (`https://itunes.apple.com/<cc>/rss/customerreviews/page=<1..10>/id=<id>/sortby=mostrecent/json`) for us, gb, ca and au, pulled 25 Oct 2026. Each bullet links the feed for the app it cites.
- **"GP pull"**: my pull of Google Play reviews using `google-play-scraper` 1.2.7 (lang=en, country=us, sort=newest up to 3,000 plus most-relevant up to 600, deduplicated), pulled 25 Oct 2026. Each bullet links the app's Play listing.
- **"own analysis"**: my regex codebook and pandas scripts over those pulls. Scripts are in `/tmp/claude-0/-home-user-outfit-planner-app/bd7b84c5-3686-5200-b171-3a2b128c387a/scratchpad/reviews/scripts/`. Raw data stays in `.../scratchpad/reviews/data/` and is not copied here. The quotes below are trimmed to a few words, with no usernames.

---

## Q1. What data was collected, what period does it cover, and how reliable is the coding?

### Takeaway
I collected **26,970 written reviews**: 12,344 from App Store RSS across US, GB, CA and AU, and 14,626 from Google Play US-English, covering 17 apps. Most of the volume falls between 2023 and Oct 2026. Recency bias and feed caps are significant, so treat shares as directional. I spot-read 7–10 matches for every theme regex. Most themes reach roughly 75–100% precision. Shopping-push and privacy are unreliable.

### Cited Findings
- **App IDs found via the iTunes Search API** (search date 25 Oct 2026; headline rating and count shown). Whering 1519461680 (4.67, 11,997 ratings). Acloset 1542311809 (4.45, 5,464). Indyx 1599179405 (4.77, 1,636). Stylebook 335709058 (4.68, 8,865). Cladwell 1140550878 (4.25, 1,036). Smart Closet 1198057728 (4.41, 4,345). Pureple 628106373 (3.94, 6,119). Combyne 989727742 (4.76, 87,744). Alta Daily 6481705400 (4.89, 17,389). Daydream 6692634357 (4.19, 79). Style DNA 1358319821 (4.29, 7,521). Gensmo 6636520663 (4.68, 1,760). Fits 6447482321 (4.59, 5,685). Dressly 6738174180 (4.40, 32,661). Essembl 6479642952 (4.54, 4,789). Closetly 6744034749 (4.57, 1,955). GetWardrobe 656212466 (4.30, 789). — [iTunes Search API](https://itunes.apple.com/search?term=outfit+planner&entity=software&country=us)
- **Google Doppl has no review data left in the stores.** An iTunes lookup of its former ID 6741596720 returns 0 results, and its RSS feed returns 0 entries ([iTunes lookup](https://itunes.apple.com/lookup?id=6741596720&country=us); [former listing](https://apps.apple.com/us/app/doppl-google/id6741596720)). Google Labs announced on X on 23 Mar 2026 that "the Doppl app will officially shut down". The service ended on 30 Apr 2026 as try-on moved into Google Search and Shopping. — [Jetstream (24 Mar 2026)](https://jetstream.blog/?p=213348)
- **Google Play listings** (installs; headline rating and count). Combyne `com.combyne.app` 10M+ (4.43; 231,961). Whering `com.whering.app` 5M+ (4.39; 27,917). Acloset `com.looko.acloset` 5M+ (4.46; 31,125). Smart Closet `com.rkk.closet` 1M+ (2.17; 6,188). Pureple `com.app.pureple` 1M+ (2.45; 4,004). Style DNA `style.dna.app` 1M+ (3.50; 17,852). Essembl `com.essembl.app` 1M+ (4.38; 12,435). GetWardrobe `com.ThreeBoots` 1M+ (4.03; 5,043). Alta `com.alta` 500K+ (no rating or reviews exposed). Fits `de.lhenne.fits` 500K+ (4.68; 15,106). Indyx `com.indyx.android` 100K+ (4.47; 1,308). Cladwell `com.cladwell` 100K+ (4.01; 518). Dressly `world.dressly.fashion` 100K+ (4.39; 12,437). — [Whering GP](https://play.google.com/store/apps/details?id=com.whering.app), [Smart Closet GP](https://play.google.com/store/apps/details?id=com.rkk.closet), [Pureple GP](https://play.google.com/store/apps/details?id=com.app.pureple), [Alta GP](https://play.google.com/store/apps/details?id=com.alta)
- **Store coverage gaps.** Stylebook and Daydream appear to be iOS-only, since no Play listing was found. Gensmo's reported Play package `one.srp.gensmo.app` returns NotFound for US, GB, IN, SG and AU, so it is likely delisted or region-locked. Alta's Play listing exposes no ratings or reviews to the scraper. — [Gensmo package ID per NowSecure/Grand Screen via search](https://grand-screen.com/apps/gensmo-your-fashion-ai-agent/); [Alta GP](https://play.google.com/store/apps/details?id=com.alta)
- **Rows pulled per app** (App Store / Google Play; date range of the rows):

| App | AS n (range) | GP n (range) |
|---|---|---|
| Combyne | 1,824 (2018-05 → 2026-10-24) | 3,385 (2018-09 → 2026-10-20) |
| Acloset | 749 (2021-06 → 2026-10-23) | 2,517 (2021-04 → 2026-10-24) |
| Whering | 1,449 (2021-02 → 2026-10-23) | 1,721 (2020-08 → 2026-10-23) |
| Style DNA | 885 (2021-03 → 2026-10-19) | 1,615 (2022-11 → 2026-10-24) |
| Dressly | 1,009 (2024-12 → 2026-10-24) | 1,299 (2025-03 → 2026-10-24) |
| Smart Closet | 552 (2017-02 → 2026-10-04) | 1,335 (2016-06 → 2026-09-13) |
| Pureple | 1,038 (2014-11 → 2026-05-27) | 352 (2020-04 → 2026-07-15) |
| Indyx | 826 (2023-01 → 2026-10-24) | 540 (2023-03 → 2026-10-20) |
| Essembl | 361 (2024-05 → 2026-10-24) | 917 (2024-05 → 2026-10-23) |
| Stylebook | 1,134 (2010-02 → 2026-10-18) | n/a (iOS only) |
| Alta | 1,001 (2025-03 → 2026-10-24) | 0 (not exposed) |
| Cladwell | 744 (2017-03 → 2026-10-24) | 140 (2021-03 → 2026-10-22) |
| GetWardrobe | 332 (2013-10 → 2026-09-16) | 492 (2019-02 → 2026-10-19) |
| Fits | 306 (2023-09 → 2026-10-23) | 313 (2025-04 → 2026-10-24) |
| Gensmo | 55 (2024-12 → 2026-07-30) | n/a |
| Closetly | 52 (2025-05 → 2026-04-09) | n/a |
| Daydream | 27 (2025-11 → 2026-10-09) | n/a |

  — AS pull ([e.g. Whering feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1519461680/sortby=mostrecent/json)); GP pull ([e.g. Combyne](https://play.google.com/store/apps/details?id=com.combyne.app)), own analysis
- **The RSS cap truncates recent history for busy apps.** The US feed's 500-review cap covers only 5 Aug–24 Oct 2026 for Alta, 14 Apr–24 Oct 2026 for Dressly, 15 Mar–23 Oct 2026 for Whering, 22 Sep 2025–23 Oct 2026 for Indyx and 15 Feb 2025–24 Oct 2026 for Combyne. Smaller apps reach back years. Country feeds below 500 are complete. Combyne Google Play hit the 3,000 "newest" cap, which goes back to 4 Jan 2023. Every other app's Play pull returned its full available written-review history. — AS pull ([Alta feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json)); GP pull, own analysis
- **Written reviews are far more negative than headline star ratings for subscription-first AI apps.** Written-review means vs. the App Store headline: Dressly 2.61 vs 4.40, Closetly 1.77 vs 4.57, Style DNA 2.04 vs 4.29, Essembl 2.27 vs 4.54. For catalog-first apps the two are close: Whering 4.63 vs 4.67, Indyx 4.74 vs 4.77, Alta 4.70 vs 4.89. — AS pull ([Dressly feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6738174180/sortby=mostrecent/json), [Closetly feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6744034749/sortby=mostrecent/json)); [iTunes Search API](https://itunes.apple.com/search?term=Style+DNA&entity=software&country=us)
- **Very short Google Play reviews suggest prompted ratings.** Median written-review length is 29 characters for Essembl and Style DNA and 26 for Fits, against 150 for Whering and 197 for Indyx. Style DNA's Play reviews jumped to 485 in Sep 2026 (median length 7 characters, e.g. "good", "nice app"), all on version 1.10.227. — GP pull ([Style DNA](https://play.google.com/store/apps/details?id=style.dna.app)), own analysis
- **Codebook.** I used 27 regex themes over title+body, plus a churn regex, a feature-request regex and a competitor-mention regex. I refined it after reading keyword-in-context samples. Fixes made: "recommend" alone ("I recommend this app") no longer counts as outfit suggestions. "model" alone ("subscription model") no longer counts as try-on. "cropped sweater" no longer counts as background removal. "important" no longer counts as import. "cataloging", which contains "login", no longer counts as login. "friends" in "recommend to friends" no longer counts as social. — own analysis
- **Spot-check precision** (matches judged on-theme out of 7–10 random matches each, after fixes). Billing/refund/cancel 10/10. Outfit suggestions 8/8. Calendar/wear-log 8/8. Packing 8/8. Customer support 8/8. UI/UX 8/8 (a broad theme). Decision-time 7/7. Crash/bugs about 9/10. Paywall/price 7/8. AI-stylist 7/8. Sync/data loss 7/8. Stats/CPW 7/8. Weather 7/8. Social 7/8. Background removal about 8/10. Try-on/avatar 6/8 (colour-analysis selfies and Combyne mannequins leak in). Adding-is-tedious 6/8. Ads 6/8 ("no ads!" praise also matches). Tagging/categorisation about 7/10, but only about 3/10 for auto-tagging accuracy as such. Privacy 5/8. Resale 5/7. Platform parity 5/7. Churn 6/7. Feature request 5/7. Import-from-web was weak (about 5/10 before fixes; Indyx sign-up "link" errors still leak in). Shopping-push was about 1–2/8 and is **unreliable**. — own analysis
- **Recall** was not measured. 21.6% of 1★ reviews and 56.1% of 5★ reviews match no theme; most of these are short ("love it", "trash"). — own analysis

### Inferences
- Theme shares are lower bounds on prevalence and are best read relatively, comparing apps or 1★ against 5★.
- Pooled figures are dominated by high-volume apps (Combyne, Acloset, Whering, Style DNA, Dressly). For that reason I report app-balanced means, the unweighted average of per-app shares, next to them.
- The gap between written reviews and headline stars for Dressly, Closetly, Style DNA and Essembl probably reflects two things. Star-only ratings are captured in-app at positive moments. Billing anger goes into written reviews. Their headline ratings therefore overstate satisfaction.

### Gaps
- App Store RSS does not include developer responses, and only the most recent 500 reviews per country are available, so long-run App Store history for big apps is missing.
- Google Play pulls are US-English only. Non-English markets (e.g. Korean users for Acloset, German users for Combyne and Fits) are not represented.
- Doppl's store reviews are gone; only press and aggregator data remain (see Q6).
- There is no human-coded gold set, so the precision figures come from small samples (±15–20 pp).

---

## Q2. How are the apps rated, and did specific releases or paywall changes cause drops?

### Takeaway
Ratings split into three groups.
- **Loved catalog-first apps:** Indyx, Alta, Whering, Stylebook and Combyne, with written means of 4.1–4.7 and ≤14% 1–2★.
- **Mixed:** Acloset, Fits and GetWardrobe.
- **Poorly reviewed:** Dressly, Style DNA, Essembl, Cladwell, Pureple, Smart Closet and Closetly, with 36–81% 1–2★.

The sharpest, clearly attributable drop in the data is Acloset's January 2024 paywall release. Others are Smart Closet's Oct 2025 login/backup failure, Cladwell's Oct 2026 AI-heavy redesign, and Combyne's slide into bugs through 2025–Oct 2026.

### Cited Findings
- **App-level written-review stats** (both stores pooled: n, mean, % 1–2★, % 4–5★).
  - Alta 1,001, 4.70, 5.1%, 92.5%.
  - Indyx 1,366, 4.56, 8.1%, 88.1%.
  - Whering 3,170, 4.26, 11.9%, 80.2%.
  - Combyne 5,209, 4.20, 13.4%, 78.3%.
  - Stylebook 1,134, 4.09, 13.8%, 76.0%.
  - GetWardrobe 824, 4.02, 20.5%, 74.0%.
  - Fits 619, 3.90, 20.5%, 71.7%.
  - Acloset 3,266, 3.51, 31.5%, 61.0%.
  - Smart Closet 1,887, 3.25, 36.0%, 51.6%.
  - Essembl 1,278, 3.05, 43.7%, 47.3%.
  - Gensmo 55, 2.93, 49.1%, 36.4%.
  - Style DNA 2,500, 2.86, 50.1%, 43.3%.
  - Pureple 1,390, 2.83, 44.8%, 39.5%.
  - Cladwell 884, 2.82, 45.0%, 36.7%.
  - Dressly 2,308, 2.54, 56.2%, 35.4%.
  - Closetly 52, 1.77, 80.8%, 17.3%.
  - Daydream 27, 4.41, 14.8%, 85.2%.

  — AS pull + GP pull ([Indyx feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1599179405/sortby=mostrecent/json), [Indyx GP](https://play.google.com/store/apps/details?id=com.indyx.android)), own analysis
- **Last 12 months** (25 Oct 2025–25 Oct 2026; n, mean, % 1–2★).
  - Indyx AS 641, 4.77, 4.1% / GP 280, 4.53, 7.5%.
  - Whering AS 886, 4.74, 2.7% / GP 401, 4.10, 13.2%.
  - Alta AS 934, 4.71, 5.0%.
  - GetWardrobe AS 106, 4.72 / GP 60, 3.65.
  - Stylebook AS 79, 4.48, 6.3%.
  - Acloset AS 94, 3.17, 36.2% / GP 511, 4.37, 10.2%.
  - Fits AS 166, 4.01 / GP 270, 3.85.
  - Combyne AS 291, 3.91, 22.0% / GP 223, 3.15, 39.0%.
  - Style DNA AS 143, 2.90, 53.1% / GP 680, 3.97, 18.7% (inflated by the short-review burst noted in Q1).
  - Essembl AS 292, 2.04, 70.5% / GP 633, 3.55, 32.5%.
  - Dressly AS 796, 2.68, 51.3% / GP 945, 2.35, 62.0%.
  - Smart Closet AS 27, 2.22, 59.3% / GP 43, 1.95, 74.4%.
  - Cladwell AS 22, 2.73 / GP 8, 2.88.
  - Pureple AS 11 / GP 10 (1.40).

  — AS pull + GP pull, own analysis
- **Review velocity, last 90 days** (written reviews per month, lower bound because of the RSS cap). Alta about 215 on the App Store. Style DNA about 204 on Play (short-review burst). Dressly about 97 AS + 58 GP. Acloset about 73 GP. Indyx about 59 AS + 18 GP. Essembl about 45 + 47. Whering about 43 + 36. Fits about 19 + 31. Combyne about 29 + 10. All others are under 10. — AS pull ([Alta feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json)) + GP pull, own analysis
- **Acloset paywall event (clearest causal drop).** Acloset announced on 18 Dec 2023 that free users would be limited to 100 items, citing more than 2 million users and AI/image-storage costs ([Acloset announcement](https://www.acloset.app/announcements/acloset-adopts-freemium-pricing-to-ensure-long-term-stabilit-848b71)). A 10 Jan 2024 follow-up said subscriptions were required above 100 items and "Ads will be introduced after January 22" ([Acloset announcement](https://www.acloset.app/announcements/acloset-s-transition-to-subscription-plans-starting-within-n-3271e0/)).
  - In the pull, reviews averaged 4.1–4.6 on versions 5.2–5.4 (Nov–mid Dec 2023). Version 5.5.1 (late Dec 2023, after the announcement) averaged 2.93 on Play, and 5.6.x averaged 3.4–3.9. Version 5.7.1 (26 Jan 2024) averaged 1.36 on the App Store (n=61) and 1.80 on Play (n=20). Version 5.7.2 averaged 1.59 on Play (n=78).
  - Quarterly mean fell from 3.86 (2023Q4) to 1.78 (2024Q1, n=601). 1–2★ share went from 23% to 79%, and paywall mentions from 14% to 66%.
  - Recovery took a year: 2025Q1 mean 4.21. A second dip came in 2025Q3 (2.59). By 2026Q3 the mean was 4.29, with paywall mentions at 6%.

  — AS pull ([Acloset feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1542311809/sortby=mostrecent/json)) + GP pull ([Acloset GP](https://play.google.com/store/apps/details?id=com.looko.acloset)), own analysis
- **Smart Closet collapse (Oct 2025).** October 2025 reviews averaged 1.21 on the App Store (n=14) and 1.38 on Play (n=26), with 50% of Play reviews mentioning bugs. Users reported Google login failures, backups that wiped closets, and no developer response. One review said "NO LONGER SUPPORTED". The Play headline rating is 2.17. The last App Store update was v3.8.3 on 10 Nov 2025. — AS pull ([Smart Closet feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1198057728/sortby=mostrecent/json)) + GP pull ([Smart Closet GP](https://play.google.com/store/apps/details?id=com.rkk.closet)); [iTunes lookup](https://itunes.apple.com/lookup?id=1198057728&country=us)
- **Combyne slow decline, then a crash wave.** The quarterly mean was 4.1–4.3 through 2025Q1. It then fell to 3.91 (2025Q2), 3.66 (2025Q4), 3.53 (2026Q1) and 2.71 (2026Q4 to date, n=49), when 51% of reviews mentioned bugs. Oct 2026 App Store reviews averaged 2.79 (n=38), on iOS versions 8.10.3–8.10.4. Complaints include the app not opening, feeds that won't load, and a paid-AI background-removal push. — AS pull ([Combyne feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=989727742/sortby=mostrecent/json)) + GP pull, own analysis
- **Cladwell AI redesign backlash (Oct 2026).** Cladwell shipped v6.x (iOS v6.4 dated 24 Oct 2026). In October 2026, 8 App Store reviews averaged 2.38, with 75% 1–2★ and 75% mentioning price. Review titles include "Too AI Focused". Complaints: previous outfits are not visible, planning is limited to about a week ahead, and the "latest AI update has made it unusable". Volume is small (n=8–9). — AS pull ([Cladwell feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1140550878/sortby=mostrecent/json)); [iTunes lookup](https://itunes.apple.com/lookup?id=1140550878&country=us)
- **Whering.**
  - 2024Q1 dip: mean 3.24, with 41% of reviews mentioning bugs, after the version 2.0.x rebrand. Android users complained of lag and crashes. One called it an "aggressively whimsical y2k rebrand".
  - 2026Q1: a large positive volume spike. The App Store received 432 reviews in Mar 2026 (mean 4.89) and 211 in Apr 2026 (mean 4.88), consistent with an in-app review prompt or campaign. The cause is unverified.
  - 2026Q3: paywall mentions rose to 12% (from 3% in 2026Q1–Q2), and the App Store had 25% in Jul 2026.

  — AS pull ([Whering feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1519461680/sortby=mostrecent/json)) + GP pull ([Whering GP](https://play.google.com/store/apps/details?id=com.whering.app)), own analysis
- **Indyx step-up from Aug 2025.** The mean was 3.46–3.78 in 2025Q1–Q2 and 4.84 in 2025Q3, then stayed at 4.60–4.85 through 2026. The rise coincides with versions 8.6.0–8.9.x (from 30 Jul 2025). Review volume rose and texts stayed long (median 180–257 characters), so the rise looks organic or prompt-driven, not spam-like. — AS pull ([Indyx feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1599179405/sortby=mostrecent/json)) + GP pull, own analysis
- **Chronic paywall-driven negativity.**
  - Style DNA: quarterly 1–2★ share was 56–80% in every quarter from 2023Q1 to 2026Q2 except 2023Q4 (31%), with paywall mentions at 32–64% in those quarters.
  - Dressly: 84–89% 1–2★ in 2025Q1–Q2, then 38–65% from 2025Q3 to 2026Q4 to date.
  - Essembl: paywall mentions rose from 2–4% (2024 to 2025Q1) to 29–49% (2026Q1–Q3).

  — AS pull + GP pull ([Style DNA GP](https://play.google.com/store/apps/details?id=style.dna.app), [Dressly GP](https://play.google.com/store/apps/details?id=world.dressly.fashion), [Essembl GP](https://play.google.com/store/apps/details?id=com.essembl.app)), own analysis
- **Stylebook is stable to improving:** 3.70 (2023), 4.10 (2024), 4.03 (2025), 4.58 (2026 YTD). Version 10.1 (Jun 2025) averaged 4.37 (n=115). — AS pull ([Stylebook feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=335709058/sortby=mostrecent/json))

### Inferences
- **Paywalls placed after users have invested effort are the most damaging monetisation pattern in the category.** Acloset's 100-item cap hit exactly the most engaged users. Whering, Indyx and Stylebook either keep the catalog free or charge once up front, and they avoided comparable drops.
- **Abandonment is fatal.** Smart Closet shows that if the backend breaks after users have spent hours digitising, data loss turns loyal users into 1★ reviewers.
- **A forced AI-first redesign carries risk.** Cladwell 6.x and Stylebook's 2025 auto-crop both triggered "AI 👎" reviews from long-time users.

### Gaps
- Exact Indyx and Whering in-app review-prompt timing and the content of Indyx's 8.6 release are not visible in reviews.
- Cladwell's Oct 2026 sample is tiny.
- Smart Closet's "sunset" claim comes from users. I found no official statement.

---

## Q3. What drives 1-star reviews and churn across the category?

### Takeaway
Monetisation dominates 1★ reviews: paywall, price, billing and ads appear in **53% of all 1★ reviews (38% app-balanced)**. Reliability follows (crash, data loss, login: 23% / 24%), then AI/styling quality (13% / 18%), then capture quality (9% / 11%).

Billing complaints are the most toxic theme. **90% of reviews mentioning refund, charge or cancel issues are 1★.** Explicit churn statements co-occur with paywall complaints 59% of the time.

### Cited Findings
- **1★ driver table** (pooled, n=5,797 1★ out of 26,970 reviews; baseline P(1★)=21.5%). Each row: share of 1★ reviews mentioning the theme (app-balanced), then P(1★ | theme).
  - Paywall/price 47.9% (34.5%), P=60.6%.
  - Billing/refund/cancel 18.9% (9.5%), P=**90.0%**.
  - Crash/bugs 18.0% (19.0%), P=34.6%.
  - Customer support 6.8% (4.3%), P=59.0%.
  - AI-stylist mentions 6.5% (10.7%), P=25.3%.
  - Ads 6.4% (4.5%), P=42.5%.
  - Outfit suggestions 5.6% (5.3%), P=17.5%.
  - Sync/data loss 3.4% (3.6%), P=37.1%.
  - Login/account 3.3% (4.1%), P=53.2%.
  - Import from web 2.4% (3.2%), P=27.7%.
  - Adding is tedious 2.0% (2.5%), P=27.3%.
  - Background removal 1.9% (3.6%), P=10.6%.
  - Try-on/avatar 1.8% (4.2%), P=20.4%.
  - Privacy 1.0% (3.5%), P=64.4%.

  — AS pull + GP pull ([e.g. Dressly feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6738174180/sortby=mostrecent/json)), own analysis
- **1★ composition by app** (% of the app's 1★ reviews: monetisation / reliability / AI-styling quality / capture quality).
  - Dressly 79/7/15/6 (n=1,124).
  - Closetly 77/5/23/10.
  - Cladwell 71/32/16/8.
  - Style DNA 68/10/14/3 (n=1,049).
  - Acloset 67/23/14/7 (n=813).
  - Essembl 58/15/20/4.
  - Stylebook 40/27/8/**47**.
  - GetWardrobe 38/23/5/5.
  - Fits 37/24/18/8.
  - Pureple 37/37/11/16.
  - Indyx 26/**39**/12/32 (capture inflated by sign-up "link" errors).
  - Smart Closet 14/**44**/3/15.
  - Combyne 10/**47**/3/8.
  - Whering **2**/35/4/10.
  - Alta 6/26/**35**/6 (n=31).
  - Gensmo 19/19/**44**/6 (n=16).

  — AS pull + GP pull, own analysis
- **Churn statements** ("deleted the app", "uninstall", "switching to", "cancel my subscription", "waste of money", etc.): 1,178 reviews, 4.4% of all, mean rating 1.59. Highest rates: Dressly 10.2%, Cladwell 9.8%, Style DNA 8.6%, Gensmo 7.3%, Acloset 6.1%, Pureple 5.5%. Lowest: Fits 0.8%, Alta 1.1%, Whering 1.5%, Indyx 1.7%, Combyne 1.7%. Themes co-occurring with churn: paywall 59.4%, billing 24.7%, crash 21.4%, outfit suggestions 10.4%, AI 8.9%, support 7.5%, ads 7.5%. — AS pull + GP pull, own analysis
- **Billing dark patterns are concentrated in AI-subscription apps.** Billing/refund/cancel mentions as a share of all reviews: Dressly 27.2%, Closetly 17.3%, Style DNA 15.6%, Cladwell 7.6%, Essembl 4.1%. Whering, Indyx, Combyne and Alta are at 0–0.1%. Typical complaints:
  - Dressly: "you sign up for what you think is one charge it is actually 3 different charges"; refunds "requiring you to complete a minimum usage of the app".
  - Style DNA: "after paying for the subscription… pay again to unlock another feature".
  - Essembl: "Makes you submit a selfie before you learn that the app costs money".

  — AS pull + GP pull ([Dressly GP](https://play.google.com/store/apps/details?id=world.dressly.fashion), [Style DNA feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1358319821/sortby=mostrecent/json), [Essembl feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6479642952/sortby=mostrecent/json))
- **Sunk-effort resentment** is a recurring 1★ narrative:
  - Acloset: "this app really took my data hostage" (Jan 2024).
  - Acloset: the heaviest users are "slapped with ridiculous fees once you have invested a lot of time".
  - Essembl: "After you… spend time putting all your data in it just makes u pay".
  - Smart Closet: "all the hard work and hours… digitizing my closet is now meaningless".

  — AS pull + GP pull ([Acloset GP](https://play.google.com/store/apps/details?id=com.looko.acloset), [Smart Closet feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1198057728/sortby=mostrecent/json))
- **Ads as the primary pain.** Pureple: ads appear in 31% of its 1–2★ reviews, e.g. "you have to watch 20 ads for it to shuffle your wardrobe". Acloset: ads appear in 18% of its 1–2★ reviews (after Jan 2024), e.g. "after every single action… you" get an ad. — GP pull ([Pureple GP](https://play.google.com/store/apps/details?id=com.app.pureple)) + AS pull ([Acloset feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1542311809/sortby=mostrecent/json))
- **Data loss and sync drive 1★ for older, local-storage apps.** Sync/data-loss share of 1–2★ reviews: Smart Closet 18.8%, Stylebook 14.7% (e.g. "all items are stored on my device"; iCloud sync crashing on a second device), Pureple 5.0%. — AS pull ([Stylebook feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=335709058/sortby=mostrecent/json)) + GP pull
- **Year-over-year mix shift (pooled; partly a composition effect from new AI-subscription apps launching in 2024–25).**
  - Paywall mentions: 8.9% of reviews in 2023, 24.0% in 2024, 21.5% in 2025, 19.4% in 2026.
  - Billing mentions: 1.3%, 4.4%, 9.3%, 6.5%.
  - Crash/bug mentions fell: 14.1%, 12.9%, 10.3%, 5.6%.
  - AI-stylist mentions rose: 3.8%, 6.2%, 7.8%, 7.9%.
  - Try-on/avatar mentions rose: 0.5%, 1.3%, 2.6%, 3.7%.

  — AS pull + GP pull, own analysis

### Inferences
- **The biggest 1★ risk is a hard paywall plus a hard-to-cancel subscription,** especially one shown after onboarding effort (selfie, quiz, item upload). A trustworthy billing UX (clear price, one-tap cancel, no stacked upsells) is a differentiator in itself.
- **Catalog-first freemium apps lose stars mainly to reliability**, i.e. slowness, crashes and failed uploads. Whering's 1★ reviews are only 2% monetisation and 35% reliability.
- **Losing user-built data after hours of cataloguing produces the angriest reviews.** Cloud backup and export are trust features, not extras.

### Gaps
- Star-only ratings without text, which are most store ratings, cannot be attributed to themes.
- I could not verify whether the billing practices users describe breach store policy. These are user claims.

---

## Q4. Which mechanics delight users?

### Takeaway
The best predictors of a 5★ review are planning and real-life wins, not AI:
- decision fatigue relieved and time saved in the morning
- packing for trips
- "shop my closet" and buying less
- the calendar/wear log
- cost-per-wear stats
- community styling

These themes carry mean ratings of 4.0–4.6 and P(5★) of 54–79%. AI and try-on mentions are polarised and average about 3.3–3.6.

### Cited Findings
- **Mean rating when mentioned and P(5★ | theme)** (pooled; baseline P(5★)=49.7%).
  - Decision fatigue / time saved: 4.58, P=**79.1%**.
  - Packing/travel: 4.50, 72.4%.
  - Resale/donate: 4.39, 68.3% (n=120).
  - Mindful/capsule ("shop my closet", buy less): 4.25, 69.2%.
  - Calendar/wear log: 4.20, 62.5%.
  - Stats/cost-per-wear: 4.17, 56.5%.
  - Social/community: 4.02, 53.6%.
  - UI/UX: 3.86, 53.0%.
  - Weather: 3.77, 44.1%.
  - Background removal: 3.71, 37.2%.
  - Tagging/categorisation: 3.63, 34.3%.
  - Try-on/avatar: 3.63, 48.3%.
  - Outfit suggestions: 3.54, 41.0%.
  - AI-stylist: 3.26, 36.1%.

  — AS pull + GP pull, own analysis
- **What 5★ reviews mention most, app-balanced:** UI/UX 10.1%, decision-time 7.1%, calendar/wear-log 7.0%, mindful/capsule 6.6%, outfit suggestions 6.2%, packing 5.3%, AI 5.1%, social 4.9%. — own analysis
- **Per-app delight signatures** (top themes in 4–5★ reviews):
  - Stylebook: calendar/wear-log 24%, packing 23%, stats/CPW 15%, decision-time 14%.
  - Indyx: mindful 20%, calendar 16%, UI 16%, packing 14%.
  - Whering: calendar 14%, UI 14%, outfit suggestions 10%, decision-time 8%, mindful 8%.
  - Alta: outfit suggestions 16%, decision-time 14%, UI 13%, AI 12%, try-on/avatar 12%.
  - Cladwell: mindful/capsule 23%, decision-time 21%, outfit suggestions 20%.
  - Combyne: social/community 12%.
  - GetWardrobe: UI 13%, calendar 12%.
  - Acloset: UI 11%, AI 9%, outfit suggestions 9%, auto-tagging and background removal 6% each.

  — AS pull + GP pull ([Stylebook feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=335709058/sortby=mostrecent/json), [Alta feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json)), own analysis
- **Representative 5★ phrases:**
  - Whering: "literally the clueless outfit generator!"; "stops me buying something similar".
  - Indyx: "I worried that the effort of uploading… would be a barrier, but it was honestly fine"; "I love the cost per wear tracker"; "Great help for packing. I finally stopped overpacking!"
  - Stylebook: "It helps me outfit plan, pack, focus my color palette, and know my cost-per-wear"; it "stopped me from buying new clothes, saving me far more money than the one off cost".
  - Alta: "try on outfits without having to get undressed".
  - Acloset: "I don't have to worry about labeling every item at all".

  — AS pull ([Whering feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1519461680/sortby=mostrecent/json), [Indyx feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1599179405/sortby=mostrecent/json)) + GP pull ([Acloset GP](https://play.google.com/store/apps/details?id=com.looko.acloset))
- **Pop-culture anchor.** "Clueless" is cited repeatedly as the aspiration, Cher's computerised closet, in Whering, Indyx, Alta and Cladwell reviews. — AS pull + GP pull, own analysis
- **"Free" is valued.** "Free" is mentioned in 11.9% of Indyx and 5.4% of Whering reviews, mostly approvingly (e.g. "free with NO ADS!"). In paywalled apps the same word appears in complaints ("Not actually free"). — AS pull + GP pull, own analysis

### Inferences
- **The core delight loop is utilitarian:** see everything I own → plan or pack quickly → wear more of it and buy less → see proof in stats. Generative styling is a second-order delight. It helps when it works and hurts when it doesn't (Q6).
- **Community styling is a retention hook.** Combyne's challenges and followers, and Indyx and Whering community styling, earn mentions mostly in positive reviews.

### Gaps
- Delight is measured by mention. Silent satisfaction is not captured, and 56% of 5★ reviews match no theme.

---

## Q5. Which features do users explicitly request?

### Takeaway
9.4% of reviews (2,536) contain an explicit request ("I wish", "please add", "would love", "would be great if"…). The most-requested improvements are:
- better and more varied outfit suggestions that use the whole closet
- bug fixes
- more and custom item categories, especially accessories and jewellery
- a free or cheaper or one-time price
- better search, filters and sorting

These are followed by richer item fields, better photo and background editing, social features, edit/duplicate controls, calendar improvements and layering control on the outfit canvas.

### Cited Findings
- **Ranked request categories** (unique reviews with a request sentence in the category; number of apps; mean rating). Category precision is about 60–75% because requests were classified by keyword.

| # | Requested feature | Reviews | Apps | Mean ★ |
|---|---|---|---|---|
| 1 | Better / more varied outfit suggestions (use more of my closet, occasions, style match) | 384 | 14 | 3.84 |
| 2 | Bug fixes / stability ("please fix") | 330 | 14 | 2.90 |
| 3 | More/custom categories & item types (accessories, jewellery, shoes, bags, custom tags/fields) | 311 | 14 | 3.95 |
| 4 | Free tier / lower price / one-time or lifetime purchase | 193 | 13 | 2.90 |
| 5 | Better search / filters / sorting | 192 | 14 | 3.94 |
| 6 | Item details (size, brand, price, purchase date, notes, fabric) | 127 | 15 | 3.80 |
| 7 | Better photo editing / background removal (manual fix, rotate, resize, backgrounds) | 114 | 11 | 3.85 |
| 8 | Social / sharing / community features | 100 | 13 | 4.09 |
| 9 | Undo / edit / delete / duplicate controls | 96 | 12 | 3.84 |
| 10 | Calendar / planner / outfit-log improvements | 82 | 11 | 4.19 |
| 11 | Layering / outfit canvas control | 80 | 13 | 3.78 |
| 12 | Wear tracking / stats / cost-per-wear (incl. sort by wears, archive excluded from stats) | 73 | 10 | 4.17 |
| 13 | Cloud sync / backup / multi-device | 60 | 8 | 3.85 |
| 14 | Packing lists / trips (looks inside packing) | 57 | 10 | 4.19 |
| 15 | Import from web links / screenshots | 54 | 13 | 3.31 |
| 16 | Weather / location-aware planning | 52 | 12 | 3.96 |
| 17 | Web / desktop / iPad version | 51 | 9 | 3.96 |
| 18 | Menswear / gender / body-type inclusivity | 49 | 14 | 3.57 |
| 19 | Dark mode / UI customisation | 45 | 11 | 3.96 |
| 20 | Laundry / item status (dirty, lent, stored, archived) | 42 | 10 | 4.07 |
| 21 | Android / platform parity, widgets | 33 | 9 | 3.70 |
| 22 | Wishlist / shopping list / gap analysis | 31 | 12 | 4.23 |
| 23 | Multiple closets / profiles (family, second home) | 26 | 10 | 4.04 |
| 24 | Try-on / avatar | 24 | 10 | 4.00 |
| 25 | Fewer / no ads | 24 | 8 | 2.96 |
| 26 | Export / data portability (CSV, spreadsheet) | 16 | 8 | 3.62 |
| 27 | Colour analysis / palette | 14 | 9 | 3.21 |
| 28 | Bulk / batch upload & edit | 13 | 5 | 4.54 |
| 29 | Resale / donate integration | 10 | 5 | 4.40 |

  — AS pull + GP pull ([e.g. Whering feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1519461680/sortby=mostrecent/json), [Stylebook feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=335709058/sortby=mostrecent/json)), own analysis
- **Example request sentences (trimmed):**
  - "I wish there was a way to search instead of just filter."
  - "I would REALLY like an option to sort by number of wears and by cost per wear!"
  - "PLEASE add dark mode."
  - "I wish they would bring back the zoomed out calendar view."
  - "I'd like the option to select multiple photos… instead of… one at a time."
  - "I wish… an archive function so that… item would still appear on my calendar… but would not be counted in my style stats."
  - "I think we should have an option to organize what's clean and what's dirty!!"
  - "wish there was landscape option for iPad."
  - "I only wish there was a way to export items… into a spreadsheet."
  - "If you are going to offer a non-binary option, you should allow for both men's and women's sizes."

  — AS pull + GP pull, own analysis
- **Request mix by app** (counts, notable concentrations).
  - Whering: outfit-suggestion requests 64, categories/fields 64, search/filter 36, background/photo editing 34, edit/duplicate controls 23, layering/canvas 22.
  - Acloset: outfit suggestions 75, categories 42, price 35, search 35.
  - Stylebook: dominates sync/multi-device requests (44 of 60), web/iPad requests (23 of 51), stats requests (21 of 73) and packing requests (22 of 57).

  — own analysis
- **Bulk-add requests (13) and complaints that adding is tedious (1.5% of reviews) are rarer than expected,** given that setup takes a long time. In 1–2★ reviews, tedium rises to 4.7% at Smart Closet and 15.4% at Stylebook (where manual background erasing is common). — AS pull + GP pull, own analysis

### Inferences
- **Users mostly ask for better versions of core loops,** i.e. smarter suggestions, richer taxonomy and fields, better search and editing, not new paradigms.
- **Accessories and jewellery being missing from generated outfits recurs across apps** (Acloset, Cladwell, Whering). It is a concrete gap.
- **Asking for price changes rather than features correlates with low ratings** (mean 2.90). These users are near churn.

### Gaps
- Request extraction misses implicit requests ("it doesn't let me X"). Category assignment is keyword-based, and some sentences get multiple labels.

---

## Q6. How do users react to AI stylists, avatars and virtual try-on, and to AI-first apps?

### Takeaway
AI is polarising. It delights when it removes labour (auto-tagging, background removal) or produces wearable outfits from the user's own closet. It produces 1★ reviews when it:
- suggests mismatched outfits
- inserts items the user doesn't own, to sell them
- renders avatars that "look nothing like me"
- is forced on long-time users

Alta is the success case: 92.5% of its reviews are 4–5★, and try-on mentions average 4.58. Gensmo, Style DNA and Dressly show the failure modes.

### Cited Findings
- **Try-on/avatar mentions by app** (n, mean, % 1–2★). Alta 123, 4.58, 6.5%. Dressly 103, 3.09, 40.8%. Style DNA 84, 2.50, 59.5% (largely selfie colour analysis). Indyx 49, 4.31. Whering 32, 4.56. Gensmo 16, 2.62, 56.3%. Fits 16, 3.19. — AS pull + GP pull ([Alta feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json), [Gensmo feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6636520663/sortby=mostrecent/json)), own analysis
- **AI-stylist mentions by app** (n, mean, % 1–2★). Acloset 325, 3.31, 33%. Essembl 185, 2.42, 56%. Dressly 163, 1.94, 74%. Style DNA 149, 2.46, 60%. Alta 130, 4.49, 9%. Whering 112, 4.21, 6%. Indyx 78, 4.19. Fits 75, 3.63. Combyne 73, 4.37. Cladwell 42, 3.00, 38%. — AS pull + GP pull, own analysis
- **Alta.**
  - Praise: an avatar "of me" for trying on outfits, plus occasion and weather suggestions.
  - Complaints, a small share of 1–2★ (n=51): AI 24%, crash 18%, try-on 16%, suggestions 12%. Examples: "Keeps recommending clothes I don't own"; "how horrible the generated avatars look"; "Gives ugly fits"; and a chat that "defaulted to asking me to type again".

  — AS pull ([Alta feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json))
- **Gensmo** (n=55; mean 2.93). Try-on/avatar is the top theme in both 1–2★ (33%) and 4–5★ (20%) reviews. Complaints: "Looks nothing like me. Needs more body shape options and less air brushing"; avatar distorts the face; "no longer making you style something from out your closet"; only two genders. — AS pull ([Gensmo feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6636520663/sortby=mostrecent/json))
- **Forced AI on existing users.**
  - Stylebook (Apr 2025): "disappointed to see the addition of generative AI… can't even opt out of the AI auto cropping".
  - Cladwell (Oct 2026): "Too AI Focused".
  - Combyne (Aug 2026): "Stop pushing your stupid paid AI".
  - Indyx (1★): objection to AI on ethical and privacy grounds.

  — AS pull ([Stylebook feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=335709058/sortby=mostrecent/json), [Cladwell feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1140550878/sortby=mostrecent/json), [Combyne feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=989727742/sortby=mostrecent/json))
- **Generic or ugly suggestions.**
  - Acloset: "matched 2 graphic tees in one outfit!"
  - Cladwell: "creates ridiculous outfit suggestions, and the algorithm refuses to learn".
  - Essembl: "spent ages 'training' the ai and it made no difference".
  - Cladwell (GP): "a dress, uggs and a kaway… Accessories are not in the outfits".

  — AS pull + GP pull ([Cladwell GP](https://play.google.com/store/apps/details?id=com.cladwell), [Essembl GP](https://play.google.com/store/apps/details?id=com.essembl.app))
- **Labour-removing AI is praised.** Acloset: "the AI will crop the background"; "It's the best ai styling app… I don't have to worry about labeling every item". Whering: "automatically scrubs the background". — AS pull + GP pull ([Acloset GP](https://play.google.com/store/apps/details?id=com.looko.acloset))
- **Google Doppl (shut down 30 Apr 2026).**
  - Google itself warned that "fit, appearance and clothing details may not always be accurate".
  - The Verge's hands-on reported pants rendered onto photos of shorts and AI-generated feet replacing socks, as summarised by secondary sources.
  - Reported limits: no footwear, swimwear, lingerie or accessories; US only.
  - AlternativeTo lists an average user rating of 3.41, with the number of ratings not shown.

  — [Perfect Corp/YouCam summary](https://www.perfectcorp.com/consumer/blog/ai-clothes/google-launches-doppl-for-virtual-try-on); [Techloy](https://www.techloy.com/google-unveils-doppl-a-new-virtual-outfit-try-on-app/); [AlternativeTo](https://alternativeto.net/software/doppl/about); [Jetstream](https://jetstream.blog/?p=213348)
- **Daydream** (AI shopping chat; n=27, mean 4.41). Praise is for search ("replaced google shopping for me"; "Great fashion understanding"). There is too little data for weaknesses. The company claims more than 1.5M shoppers have used it; this is press-sourced. — AS pull ([Daydream feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6692634357/sortby=mostrecent/json)); [Apparel Resources](https://apparelresources.com/technology-news/retail-tech/daydream-debuts-ai-powered-fashion-shopping-agent/)

### Inferences
- **Winning AI pattern:** AI that does the labour (cutout, tagging) and styles from the user's own closet, with an avatar that resembles the user, inside an app that is free or cheap to try.
- **Losing pattern:** AI used as a paywall teaser or upsell funnel, suggestions padded with shoppable items, idealised avatars, or AI forced on long-time users with no opt-out.
- **Doppl's shutdown suggests standalone try-on was not a durable product,** at least for Google. Try-on works better as a feature inside a closet app. This is an inference from the shutdown rationale.

### Gaps
- Alta's Play reviews are not exposed, so Alta's evidence is App Store only.
- Doppl's own store reviews are no longer retrievable.
- Daydream and Gensmo samples are tiny (27 and 55).

---

## Q7. Per-app voice-of-customer profiles

### Takeaway
Each app's sentiment closely tracks its business model and reliability. The catalog-first apps that are free or cheap (Whering, Indyx, Stylebook) are loved for planning, packing and stats. Their 1★ reviews are about bugs, uploads and editing. Subscription-first AI apps are reviewed mainly on billing.

### Cited Findings
Unless otherwise linked, every figure below comes from AS pull + GP pull and own analysis.
- **Whering** (n=3,170; 4.26; 1–2★ 11.9%; [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1519461680/sortby=mostrecent/json), [GP](https://play.google.com/store/apps/details?id=com.whering.app)).
  - Loved: calendar/wear-log (14% of 4–5★), easy upload and auto background removal, the Clueless-style randomiser, buying less.
  - 1–2★: crash/slow/server errors 36%, UI 9%, background removal 7%. Examples: "fun but soooo slow"; "poor internet connection no matter what"; "uploading is sooooo bad". Its Play reviews (3.94) run well below its App Store reviews (4.63), pointing to Android performance.
  - Requests: suggestions, categories and fields, search and sort, dark mode, web adding, desktop editing.
- **Indyx** (n=1,366; 4.56; 8.1%; [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1599179405/sortby=mostrecent/json), [GP](https://play.google.com/store/apps/details?id=com.indyx.android)).
  - Loved: mindful, shop-your-closet use (20% of 4–5★), cost-per-wear, calendar, packing, community styling.
  - 1–2★: crash 32%, paywall 30% (premium limits such as "maxed out my enhanced edits" despite membership), and sign-up email-link failures on Android in 2024–25.
- **Stylebook** (iOS only, one-time purchase; n=1,134; 4.09; [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=335709058/sortby=mostrecent/json)).
  - Loved: calendar 24%, packing 23%, stats/CPW 15%, value for a one-off price.
  - 1–2★: paywall 33% (mostly "paid, then…"), background removal 26% ("Tedious… separate the background"), adding is tedious 15%, sync/data loss 15% (local storage, iCloud sync), and forced AI auto-crop in 2025.
  - It leads platform-parity mentions (13.1% of reviews: iPad, web, multi-device).
- **Acloset** (n=3,266; 3.51; 31.5%; [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1542311809/sortby=mostrecent/json), [GP](https://play.google.com/store/apps/details?id=com.looko.acloset)).
  - Loved: AI auto-tagging and background removal, the canvas outfit tool, weather-aware daily suggestions.
  - 1–2★: paywall 55%, crash 19%, ads 18%. The Jan 2024 100-item cap is the defining event (Q2). It has recovered to 4.37 on Play over the last 12 months.
- **Alta Daily** (n=1,001 App Store; 4.70; 5.1%; [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json)).
  - Loved: a personalised avatar for try-on, suggestions, relief from decision fatigue, "no ads".
  - 1–2★ (n=51): ugly or mismatched outfits, suggesting items the user doesn't own, avatar quality, login.
  - It has the highest App Store review velocity in the set (US feed capped within about 80 days).
- **Combyne** (n=5,209; 4.20; 13.4%; [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=989727742/sortby=mostrecent/json), [GP](https://play.google.com/store/apps/details?id=com.combyne.app)).
  - Positioning: a social outfit-collage community that skews young; users often call it a "game".
  - Loved: challenges, followers, creativity.
  - 1–2★: crash/bugs 41%, lost accounts and followers, ads, paid AI background removal. It has deteriorated since 2025Q2 (Q2).
- **Cladwell** (n=884; 2.82; 45.0%; [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1140550878/sortby=mostrecent/json)).
  - Loved: capsule/mindful philosophy, daily outfits with weather, less decision fatigue.
  - 1–2★: paywall 64%, crash 31%, suggestion quality 17%, billing 14%, support 11%.
  - A Trustpilot reviewer (1 review total, Jun 2025) reported a long-paid subscription stopped "without notice" ([Trustpilot](https://www.trustpilot.com/review/cladwell.com)).
- **Smart Closet** (n=1,887; 3.25; 36.0%; [GP](https://play.google.com/store/apps/details?id=com.rkk.closet)).
  - Historically loved: calendar, categories, background removal, batch upload.
  - 1–2★: crash 26%, sync/data loss 19%, login 9%. Collapsed in Oct 2025 (Q2). Play headline rating 2.17.
- **Pureple** (n=1,390; 2.83; 44.8%; [GP](https://play.google.com/store/apps/details?id=com.app.pureple)).
  - 1–2★: crash 36%, ads 31%, paywall 16%.
  - Loved: community styling ("other people can make you outfits"), the randomiser.
  - The Play headline is 2.45, and its written reviews in the last 12 months are almost absent (21).
- **Style DNA** (n=2,500; 2.86; 50.1%; [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=1358319821/sortby=mostrecent/json)).
  - 1–2★: paywall 62%, billing 31% ("can not unsubscribe"; charged after deleting account; stacked upsells for the colour season), and suggestions showing $500–600 items.
  - Positive: colour analysis and the AI stylist Q&A. An independent blogger called it "addictive" ([Kat Sturges](http://www.kathrynsturges.com/home/2025/4/8/comparison-between-wardrobe-apps)).
- **Dressly** (n=2,308; 2.54; 56.2%; [GP](https://play.google.com/store/apps/details?id=world.dressly.fashion)).
  - 1–2★: paywall 70%, billing 47%, support 14%. Examples: "Price jumps to $60/mo!!"; charged multiple times; partial refunds.
  - Positive minority: AI try-on ("so helpful to 'try on' outfits!").
- **Essembl** (n=1,278; 3.05; 43.7%; [GP](https://play.google.com/store/apps/details?id=com.essembl.app)).
  - 1–2★: paywall 57%, AI 18%, crash 13%. A hard paywall comes after the selfie and email. Users report weather and location errors.
- **Fits** (n=619; 3.90; 20.5%; [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6447482321/sortby=mostrecent/json)).
  - Loved: ease of use, collages and templates, AI enhancement.
  - 1–2★: paywall 33% (AI features paid; the developer replies that "you can dismiss the paywall"), crash 22%.
- **GetWardrobe** (n=824; 4.02; 20.5%).
  - Loved: calendar, stats ("70% of my wardrobe was unworn impulse buys"), replace-piece-across-outfits.
  - Caveat: several recent 5★ App Store reviews read like marketing copy (e.g. a review titled "Upgrade choice" that lists plan features), so treat recent praise cautiously.
  - [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=656212466/sortby=mostrecent/json)
- **Closetly** (n=52; 1.77; 80.8%). Paywall 67% and billing 21% of 1–2★. Headline App Store rating is 4.57 (1,955), a gap of 2.8 stars from its written reviews. [feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6744034749/sortby=mostrecent/json)
- **Competitor mentions in reviews** (excluding self-mentions). Acloset reviews mention Whering 20× and Stylebook 13×. Whering reviews mention Acloset 14×. Cladwell reviews mention Stylebook 15×. Smart Closet reviews mention Stylebook 9×. Pinterest is mentioned across apps (Whering 26, Dressly 19). Spreadsheets/Notion/Excel are mentioned as the incumbent alternative in Stylebook (41), Acloset (32), Whering (31) and Indyx (22) reviews (this pattern also matches "notion" as a word, so the count is approximate).

### Inferences
- **Stylebook is the reference for depth** (stats, packing). **Whering is the reference for free and easy.** Both are used as benchmarks in competitors' reviews.
- **Spreadsheets and Pinterest are the real substitutes users compare against.**

### Gaps
- Per-app samples for Daydream, Gensmo and Closetly are too small for theme percentages to be stable.

---

## Q8. Do developers reply to reviews, how fast, and how?

### Takeaway
Reply behaviour varies widely.
- Near-universal replies: Acloset, Dressly and GetWardrobe answer about 97–100% of Play reviews, mostly with templates. Dressly's median is under 8 hours.
- Selective replies: Whering and Indyx reply to most negative reviews, with specific, helpful answers.
- Effectively no replies: Combyne, Fits, Smart Closet and Pureple.

High reply rates do not rescue ratings when the underlying issue is billing.

### Cited Findings
- **Google Play reply rates.** Columns: all reviews pulled (reply rate, reply rate on 1–2★, median latency), then the last 12 months.
  - Acloset 98.3% (98.7%, 31 h) / 99.0%, median 16 h.
  - Dressly 97.2% (97.1%, 7.7 h) / 99.3%, 7.1 h.
  - GetWardrobe 99.8% (99.1%, 44 h) / 100%, 46 h.
  - Style DNA 61.3% (85.4%, 25 h) / 9.9% (36.2% of 1–2★).
  - Whering 57.4% (95.1%, 47 h) / 24.2% (86.8% of 1–2★), 47 h.
  - Indyx 43.5% (54.9%, 103 h) / 38.9% (85.7% of 1–2★), 290 h.
  - Essembl 39.5% (43.0%, 62 h) / 53.2%.
  - Cladwell 33.6% (17.8%, 138 h).
  - Pureple 13.9% (374 h) / 0%.
  - Combyne 5.4% / 1.3%.
  - Smart Closet 2.4% / 0%.
  - Fits 0.6%.

  — GP pull ([Acloset GP](https://play.google.com/store/apps/details?id=com.looko.acloset), [Dressly GP](https://play.google.com/store/apps/details?id=world.dressly.fashion), [Combyne GP](https://play.google.com/store/apps/details?id=com.combyne.app)), own analysis
- **Reply style on Play** (most common reply openings).
  - Acloset: generic thanks ("Thanks so much for choosing Acloset…", 304×) and a paywall apology ("We understand your frustration with the new charges…", 157×).
  - Style DNA: playful templates ("Oopsie! That's definitely not the vibe we're going for…", 73× across two variants; "Hey there, fashionista!💕…", 30×).
  - Dressly: formal templates ("Dear Customer, Thank you for sharing your feedback…").
  - Indyx: warm one-liners ("You just earned a gold star in our hearts").
  - Whering: issue-specific fixes ("We've fixed these bugs! If you update…") and a recurring menswear note ("working on making Whering more gender inclusive").
  - Pureple: "We have just fixed the problem!"
  - Fits (rare): explains that the paywall can be dismissed.

  — GP pull, own analysis
- **App Store** (10 "featured" reviews shown on each app's US web review page, 25 Oct 2026). Developer responses / median days to respond:
  - Acloset 10/10, 3.1 d. Dressly 10/10, 1.8 d. Style DNA 10/10, 5.4 d. GetWardrobe 10/10, 4.9 d.
  - Essembl 9/10, 2.6 d. Gensmo 9/10, 5.4 d.
  - Indyx 6/10, 2.1 d. Whering 5/10, 1–3 d. Fits 4/10, 6.7 d.
  - Stylebook 1/10. Cladwell 1/10.
  - Pureple 10/10, but responses were posted a median of about 1,832 days after the review, with the latest in Jan 2022.
  - No responses: Smart Closet, Combyne, Alta, Daydream, Closetly.
  - Whering's responses give how-to fixes, e.g. how to duplicate outfits, or to "use the archive tool" to hide items from suggestions.

  — [Whering App Store page](https://apps.apple.com/us/app/whering-your-digital-closet/id1519461680?see-all=reviews), [Acloset](https://apps.apple.com/us/app/id1542311809?see-all=reviews), [Alta](https://apps.apple.com/us/app/id6481705400?see-all=reviews), [Pureple](https://apps.apple.com/us/app/id628106373?see-all=reviews)
- **Unanswered support drives anger.** "No reply" and "emailed… no response" appear in 13.9% of Dressly, 11.3% of Cladwell and 7.9% of Smart Closet 1–2★ reviews. — AS pull + GP pull, own analysis

### Inferences
- **Specific, solution-giving replies** (Whering, Indyx) correlate with loved apps. Fast templated replies (Dressly, Style DNA) do not offset billing grievances.
- **Silence during an outage** (Smart Closet 2025, Combyne 2026) compounds 1★ waves.

### Gaps
- The App Store figures come from only 10 featured reviews per app, a biased sample. RSS has no response field.
- Play reply latency is measured from the review's last-edit timestamp, so it is slightly underestimated for edited reviews.

---

## Q9. What does community discussion add (Reddit, Substack/blogs, TikTok/YouTube, Trustpilot)?

### Takeaway
Direct Reddit access was blocked from this environment, and Trustpilot holds almost nothing for these apps, so community evidence is thin. Independent user blogs and Substack threads match the store data:
- cataloguing is a big upfront investment (about two days) that users say pays off by revealing what they already own
- Indyx and Stylebook are favoured by serious trackers
- paid human or AI styling is judged "not actually wearable" by some users
- app migration (e.g. leaving Stylebook) is a real switching barrier

### Cited Findings
- **Reddit could not be accessed.** reddit.com and old.reddit.com return HTTP 403 to curl. WebFetch is refused for old.reddit.com. WebSearch rejects `reddit.com` as an allowed domain. Unfiltered searches for r/capsulewardrobe and r/femalefashionadvice threads returned no Reddit URLs. A third-party Reddit archive API (pullpush) returned 429 and explicitly forbids agent scraping, so I did not use it. — own access attempts (25 Oct 2026)
- **Trustpilot is sparse.** Whering's page shows 0 reviews (unclaimed) ([Trustpilot – Whering](https://www.trustpilot.com/review/whering.co.uk)). Cladwell has 1 review (3★, Jun 2025, complaining a paid subscription was stopped without notice) ([Trustpilot – Cladwell](https://www.trustpilot.com/review/cladwell.com)).
- **Upload effort and payoff (Whering user, 14 Jan 2024).** "It probably took me two days of solidly doing this to get everything… uploaded". Background removal worked even on dim photos and retailer screenshots. Packing and outfit planning were the highlights. Verdict: "Spoiler alert, I'm enjoying it." — [Does This Look Weird? (Substack)](https://doesthislookweird.substack.com/p/digitising-my-wardrobe-using-whering)
- **Indyx vs Stylebook thread (Sep 2024).** About 4 commenters explicitly favoured Indyx and 1 stayed with Stylebook because they "can't migrate". Cataloguing "opened my eyes" to what they owned. One cancelled Indyx's paid stylist service because "many were not actually wearable". Indyx's co-founder replied in-thread that an outfit generator was not planned and that the company relied on human stylists, which predates later changes. — [Rabbit Fur Coat (Substack) comments](https://rabbitfurcoat.substack.com/p/indyx-vs-stylebook/comments)
- **Independent comparison (8 Apr 2025).**
  - Whering: "easy to add pieces… fun", but outfit matching was "so so" and she found no recommendations.
  - Indyx: straightforward. Human stylists are expensive and not size-diverse, and the Poshmark sync didn't work for her.
  - Style DNA: the favourite. Free AI Q&A and colour analysis, though it "pushes shopping".

  — [Kat Sturges](http://www.kathrynsturges.com/home/2025/4/8/comparison-between-wardrobe-apps)
- **TikTok and creators.** A blogger reports discovering Alta through a TikTok creator and compares it to the Clueless closet ([The Handbook](https://www.thehandbook.com/ai-stylist/)). Alta's own App Store reviews also cite TikTok as the discovery path ("I found Alta thanks to TikTok") (AS pull, [Alta feed](https://itunes.apple.com/us/rss/customerreviews/page=1/id=6481705400/sortby=mostrecent/json)). Alta's self-published "user reviews" post claims requests for faster avatar generation, more categories (jewellery, activewear) and regional pricing; this is vendor-sourced ([Alta blog](https://www.altadaily.com/blog/user-reviews)).
- **Doppl in the press.** Reviewers found it easy and fun but unreliable on fit and proportions. Google labelled it an experiment ([Techloy](https://www.techloy.com/google-unveils-doppl-a-new-virtual-outfit-try-on-app/); [Perfect Corp](https://www.perfectcorp.com/consumer/blog/ai-clothes/google-launches-doppl-for-virtual-try-on)).
- **Most "best wardrobe app 2026" comparison pages are written by competitors** (Indyx's "versus" page, Alta, Vesta, Wardrowbe, Beauty AI), so I did not use them as evidence of user sentiment. — [Indyx versus page](https://www.myindyx.com/versus/whering-vs-indyx); [Beauty AI](https://beautyai.app/blog/acloset-alternatives)

### Inferences
- **Community sources agree with the store data:** the payoff is awareness and buying less, and the cost is setup time.
- **Inability to migrate is a moat** for incumbents such as Stylebook. Import and export would lower switching costs in both directions.

### Gaps
- Reddit, TikTok and YouTube comment themes could not be quantified from this sandbox because access was blocked and no comment APIs were available. A follow-up with an approved Reddit or YouTube data source is needed.
- Trustpilot volume is too low to analyse.
