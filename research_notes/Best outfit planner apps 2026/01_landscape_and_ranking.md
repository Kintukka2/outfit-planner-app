# Outfit planner / digital wardrobe / AI stylist apps on the App Store: landscape, numbers and ranking (October 2026)

Unless stated otherwise, every figure below was retrieved by me on 2026-10-25 (UTC). App Store metadata comes from the iTunes Lookup/Search API and from apps.apple.com product pages. Google Play data comes from google-play-scraper against play.google.com. Charts come from Apple's legacy RSS feeds and from the chart badges on product pages. Raw downloads and scripts are in `/tmp/claude-0/-home-user-outfit-planner-app/bd7b84c5-3686-5200-b171-3a2b128c387a/scratchpad/landscape/` (`data/` and `scripts/`). Figures more than 12 months old are labelled with their date.

## Which apps are the serious contenders today, and what does their App Store metadata say?

### Takeaway
I swept 60 App Store search terms, which returned 1,164 unique apps, and looked up 65 candidate IDs through the API. 61 of them are in the table: Google Doppl and Portae returned no US or GB listing, and two non-fashion chart hits are left out. On the US store only one dedicated closet/outfit-planning app passes 15,000 ratings: Alta (17,389, 4.89★, launched March 2025). It is followed by Whering (11,997), Stylebook (8,865, paid, last updated June 2025), Style DNA (7,521), Pureple (6,119), Fits (5,685), Acloset (5,464) and Essembl (4,789). The two biggest apps in the wider "AI outfit" search space do something else. Combyne (87,744) is a social collage maker built on brand catalogues. Dressly (32,661) is a selfie-based style coach with no closet of your own clothes. Google's Doppl was shut down on 2026-04-30 and has no App Store listing in any of the four markets I checked.

### Cited Findings
- **Method.** I ran the iTunes Search API (`entity=software&country=us&limit=50`) for 60 terms. These included "digital closet", "outfit planner", "wardrobe", "AI stylist", "closet organizer", "outfit maker", "virtual try on", "capsule wardrobe", "what to wear", "OOTD", "closet", every candidate name, and further names found along the way. The sweep returned 1,164 unique apps. I filtered them to wardrobe-relevant apps and looked up 56 + 9 IDs in US/GB/AU/CA ([iTunes Search API, e.g. "digital closet"](https://itunes.apple.com/search?term=digital%20closet&entity=software&country=us&limit=50); [iTunes Lookup batch, US](https://itunes.apple.com/lookup?id=1519461680,1542311809,1599179405,335709058,515126225,1140550878,1198057728,628106373,989727742,6447482321,1549029468,1485757044,656212466,6481705400,6741596720,6692634357,1358319821,6636520663,6737292650,1560004583,6738174180,1408832096,1637080045,6479642952,6447664832,6744034749,1194408342,6480053850,1463372370,1344153452,1214741326,6596771952,6753887920,6737596512,6744065736,6447555778,6448105847,6752877629,6755449231,6763523399,6742974181,6738202299,1022579925,6458191158,6471355187,6479453910,1530204829,6753664894,6745224732,6744468259,6739701827,1590427450,6742329531,6739629908,1536745174,6468986813&country=us); same URL with `country=gb|au|ca`; [extra chart-found IDs](https://itunes.apple.com/lookup?id=6742427939,6791276020,6502464645,6744337310,6777255343,883938724,6446857329,6761113926,6812807572&country=us)).
- **Search reach.** These apps came back on the most of the 60 search terms: Acloset (41 terms), Fits (35), Whering (33), Indyx (33), GetWardrobe (32), Combyne (31), Pureple (31), Dressly (28), My Wardrobe/INFY (28), Smart Closet (26), SimpleCloset (26), Cladwell (24), Alta (22) and Stylebook (22). Eight of the 60 terms were names of small vendor apps, which slightly inflates this count for the apps those searches returned. This measures keyword/ASO coverage, not popularity ([iTunes Search API sweep](https://itunes.apple.com/search?term=wardrobe&entity=software&country=us&limit=50)).
- **Google Doppl is gone.** Google Labs said on 2026-03-23 that the Doppl app would shut down on 2026-04-30 and be removed from the App Store and Google Play. Its try-on technology now lives in Google product listings and image results ([Google Labs on X](https://x.com/GoogleLabs/status/2036188820128071786); [Google Labs Help "Download items in Doppl before app shuts down"](https://support.google.com/labs/answer/16537062); [jetstream.blog, 2026-03-24](https://jetstream.blog/2026/03/24/google-doppl-shut-down-end-of-april-2026/)). I confirmed that the App Store ID 6741596720 returns no Lookup result in US, GB, AU or CA, and that https://apps.apple.com/us/app/id6741596720 returns HTTP 404 ([iTunes Lookup](https://itunes.apple.com/lookup?id=6741596720&country=us)). I found no Play listing under `com.google.android.apps.doppl` either.
- **Save Your Wardrobe has pivoted.** The consumer iOS app is still live (v3.0.7, updated 2026-10-05). The company website, however, now sells "End-to-End Aftersales Solutions" (repairs, warranty, AI damage detection) to brands ([saveyourwardrobe.com](https://www.saveyourwardrobe.com/)). Two 2026 roundups say they removed it from consumer rankings for this reason ([Modebase, 2026](https://www.modebase.co/journal/best-digital-wardrobe-apps-2026); [Wardrowbe, 2026](https://wardrowbe.com/blog/best-wardrobe-apps-2026/)).
- **Stylebook is the paid incumbent and has stalled.** It costs $4.99 upfront (Stylebook Men also $4.99) and was first released 2009-10-29. Its current version 10.1 dates from 2025-06-15, which is 497 days before retrieval. It is iPhone/iPad only, with no Android counterpart found ([iTunes Lookup 335709058](https://itunes.apple.com/lookup?id=335709058&country=us); [App Store page](https://apps.apple.com/us/app/id335709058)).
- **Other stale listings.** Lookscope was last updated 2024-04-02, which is 936 days ago ([Lookup 1214741326](https://itunes.apple.com/lookup?id=1214741326&country=us)). Smart Closet's last update was 2025-11-10 ([Lookup 1198057728](https://itunes.apple.com/lookup?id=1198057728&country=us)). Closetly's was 2026-01-30 and Gensmo's 2026-01-14 ([Lookup](https://itunes.apple.com/lookup?id=6744034749,6636520663&country=us)).
- **Recently launched entrants.** Alta first appeared on the App Store 2025-03-13, Daydream 2025-11-17, Doji 2025-05-06, Gensmo 2024-09-10 and Dressly 2024-11-21. In 2026 came Cloey (2026-03-13), BetterMe Style (2026-05-28, from the BetterMe fitness publisher), Lekondo (2026-01-09) and Modesona (2026-05-15). Setly arrived 2025-10-03 ([iTunes Lookup batch above](https://itunes.apple.com/lookup?id=6481705400,6692634357,6737292650,6636520663,6738174180,6753887920,6763523399,6755449231,6761113926,6752877629&country=us)).
- **Self-reported user counts** (marketing copy, unaudited). Whering says "9 M+ users", Acloset "4 million users", Pureple "3M+ people", GetWardrobe "3 million users since 2013", Style DNA "over 3 million users" and Inspoy "500,000+" ([App Store descriptions via iTunes Lookup](https://itunes.apple.com/lookup?id=1519461680,1542311809,628106373,656212466,1358319821,6480053850&country=us)). Alta's founder told Vogue Business in September 2026 that its user base is "in the millions" ([Vogue Business, 2026-09-08](https://www.vogue.com/article/why-elf-beauty-is-building-avatars-with-ai-platform-alta)). She told TechCrunch that more than 100 million outfits had been generated on Alta ([TechCrunch, 2026-02-14](https://techcrunch.com/2026/02/14/clueless-inspired-app-alta-partners-with-brand-public-school-to-start-integrating-styling-tools-into-websites/)).
- **Lifetime US ratings per day** (US ratings ÷ days since first release; my calculation from Lookup data). Dressly 46.5/day, Alta 29.4/day and Combyne 22.4/day lead. Essembl manages 5.4/day, Whering 5.3, Fits 4.5, Acloset 2.7, Style DNA 2.5 and Indyx 0.9. By this measure Alta is the fastest-accumulating closet app by a wide margin ([iTunes Lookup batch above](https://itunes.apple.com/lookup?id=6738174180,6481705400,989727742,6479642952,1519461680,6447482321,1542311809,1358319821,1599179405&country=us)).
- **Ratings by market.** The ratio of GB, AU and CA ratings to US ratings shows where each app is strong:
  - Save Your Wardrobe has 2.7× more GB ratings than US (758 vs 281), so it is a UK-first app.
  - Whering's GB count is 26% of its US count, the highest share among the large apps (UK-founded).
  - Essembl's CA count is 28% of US and Pronti's 43%, so both lean towards Canada.
  - Daydream (GB 0, AU 0) and Gensmo (GB 9) are effectively US-only.
  - Alta (GB 9%) and Combyne (AU 2%) skew to the US.

  Sources: [Lookup US](https://itunes.apple.com/lookup?id=1485757044,1519461680,6479642952,1560004583,6692634357,6636520663,6481705400,989727742&country=us), [GB](https://itunes.apple.com/lookup?id=1485757044,1519461680,6479642952,1560004583,6692634357,6636520663,6481705400,989727742&country=gb), [AU](https://itunes.apple.com/lookup?id=1485757044,1519461680,6479642952,1560004583,6692634357,6636520663,6481705400,989727742&country=au), [CA](https://itunes.apple.com/lookup?id=1485757044,1519461680,6479642952,1560004583,6692634357,6636520663,6481705400,989727742&country=ca).
- **Language coverage.** GetWardrobe ships 41 App Store languages, Pureple 26, Fits 24, Acloset 18 and Cladwell 13. Whering has 6 and Stylebook 6. Alta, Indyx, Essembl, Smart Closet, Daydream, Doji and Gensmo are English-only ([iTunes Lookup `languageCodesISO2A`](https://itunes.apple.com/lookup?id=656212466,628106373,6447482321,1542311809,1140550878,1519461680,335709058,6481705400,1599179405&country=us)).
- **Device support and size.** The product pages list Mac (Apple-silicon) and Apple Vision compatibility for most apps. Exceptions: Indyx (iPhone/iPad/iPod only), Stylebook (iPhone/iPad only), Doji (iPhone only), Cloey (iPhone only) and Style DNA (iPhone + Vision). No app in the set lists a native Apple Watch app except Weather Fit ([apps.apple.com product pages, e.g. Alta](https://apps.apple.com/us/app/id6481705400); [Indyx](https://apps.apple.com/us/app/id1599179405); [Doji](https://apps.apple.com/us/app/id6737292650)). Among the leading apps, install sizes run from Stylebook's 21.4 MB to Doji's 462.9 MB. Daydream needs iOS 26.0 or later, and Glance needs iOS 18.4 and describes itself as "Built for iOS 27" ([iTunes Lookup](https://itunes.apple.com/lookup?id=335709058,6737292650,6692634357,6742974181&country=us)).
- **Apple's "similar apps" shelf.** The "You Might Also Like" shelf on the product pages points to the same cluster. Across the 61 US product pages I fetched, the most frequently recommended bundles were GetWardrobe (19 pages), Pureple (19), Fits (18), Lookscope (14), Indyx (13), Acloset (13), Cladwell (11) and Whering (10) ([App Store product pages, e.g. Alta](https://apps.apple.com/us/app/id6481705400)). Apple also surfaced Shuffles by Pinterest (43,672 ratings), a collage/moodboard app, as an adjacent outfit-collage tool ([iTunes Lookup bundleId com.pinterest.shufflesapp](https://itunes.apple.com/lookup?bundleId=com.pinterest.shufflesapp&country=us)).
- **Self-promotional apps.** Many apps that publish "best wardrobe apps 2026" blog posts have almost no App Store presence. On 2026-10-25 they had: Clueless – Outfit Planner 4 ratings, Beauty AI 17, Stylin 8, COSH! 1 and Vêtir 35 US ratings ([iTunes Search "Clueless closet"](https://itunes.apple.com/search?term=Clueless%20closet&entity=software&country=us&limit=5); [iTunes Search "Vetir closet"](https://itunes.apple.com/search?term=Vetir%20closet&entity=software&country=us&limit=5)).

### Full candidate metadata table (App Store US primary; GB/AU/CA rating counts; Google Play counterpart)
Sources: the iTunes Lookup URLs above (US/GB/AU/CA), App Store product pages `https://apps.apple.com/us/app/id<trackId>` (devices, chart badge), Google Play `https://play.google.com/store/apps/details?id=<package>` via google-play-scraper. All retrieved 2026-10-25.

Notes on the columns:
- "US rating" is `averageUserRating`. Apple's API now returns the same value and count for `averageUserRatingForCurrentVersion` / `userRatingCountForCurrentVersion` as for the all-time fields on every app checked, so the current-version figures add no information and are omitted.
- "Devices" is the compatibility list on the US product page. The Lookup API's `supportedDevices` lists iPad models for every app, so it cannot tell iPad-native apps from iPhone apps running on iPad.
- Google Play rating counts are global. The scraper returned identical counts for `country=us` and `country=gb`.
- "GP rating hidden" means Play shows no rating for the listing. That is the case for Alta despite 500,000+ installs.

| App (key) | trackId | bundleId | Seller | Price | Genre | Age | US rating | US ratings | GB | AU | CA | Version | Current version date | First release | Size MB | Min iOS | Langs | Devices (US product page) | US chart badge 2026-10-25 | Google Play package | GP installs | GP rating | GP ratings | GP updated | Segment |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| Stitch Fix - Personal Styling (StitchFix) | 1022579925 | com.stitchfix.StitchFix-Appstore | Stitch Fix, Inc. | Free | Shopping | 12+ | 4.7 | 141635 | n/a | n/a | n/a | 11.27.0 | 2026-10-19 | 2015-08-26 | 272.9 | 18.0 | 1 | iPhone/Vision | #142 Shopping | com.stitchfix.stitchfix | 1,000,000+ | 4.78 | 4167 | 2026-10-11 | Styling service |
| Combyne - your perfect Outfit (Combyne) | 989727742 | com.pungme.combyne | combyne GmbH | Free | Lifestyle | 4+ | 4.76 | 87744 | 7949 | 1586 | 3326 | 8.10.4 | 2026-10-02 | 2016-02-04 | 98.2 | 17.6 | 3 | iPhone/Mac/Vision | - | com.combyne.app | 10,000,000+ | 4.43 | 231961 | 2026-10-06 | Social outfit creation |
| Dressly: AI Outfit Maker (Dressly) | 6738174180 | world.dressly.fashion | TIKVEX LIMITED | Free | Lifestyle | 4+ | 4.4 | 32661 | 1201 | 1828 | 4070 | 2.9.13 | 2026-10-23 | 2024-11-21 | 194.2 | 16.4 | 1 | iPhone/iPad/Mac/Vision | - | world.dressly.fashion | 100,000+ | 4.39 | 12436 | 2026-10-21 | Style coach (no own-closet) |
| Alta Daily: Digital AI Closet (Alta) | 6481705400 | com.altadaily.main | Flagship AI, Inc. | Free | Lifestyle | 12+ | 4.89 | 17389 | 1505 | 986 | 2006 | 2.2.8 | 2026-10-21 | 2025-03-13 | 133.1 | 16.4 | 1 | iPhone/iPad/Mac/Vision | #53 Lifestyle | com.alta | 500,000+ | hidden |  |  | AI recommender+avatar try-on |
| Lumi: styling assistant (Lumi) | 1637080045 | com.growth.accelerator.org.FashionApp | BRAMOL LIMITED | Free | Shopping | 17+ | 4.04 | 16483 | 1517 | 2020 | 1920 | 2.20.19 | 2026-08-10 | 2022-09-01 | 321.8 | 17.0 | 1 | iPhone/Vision | - | none found |  |  |  |  | Style coach (no own-closet) |
| shoppin: Fashion, Made For You (Shoppin) | 6738202299 | app.shoppin.ios | USAR COMMERCE TECHNOLOGIES PRI | Free | Shopping | 4+ | 4.74 | 13645 | 510 | 364 | 802 | 7.3.7 | 2026-10-21 | 2025-05-13 | 111.8 | 16.4 | 1 | iPhone/iPad/Mac | - | none found |  |  |  |  | Shopping-led (custom clothing) |
| Whering: Your Digital Closet (Whering) | 1519461680 | com.whering.app | Whering Ltd | Free | Lifestyle | 12+ | 4.67 | 11997 | 3080 | 947 | 1530 | 3.4.147 | 2026-10-07 | 2020-08-20 | 193.7 | 15.5 | 6 | iPhone/iPod/Mac/Vision | - | com.whering.app | 5,000,000+ | 4.39 | 27917 | 2026-10-05 | Cataloguer+social |
| Stylebook (Stylebook) | 335709058 | com.leftbrainrightbrain.stylebook | left brain / right brain, LLC | $4.99 | Lifestyle | 9+ | 4.68 | 8865 | 1447 | 1097 | 1035 | 10.1 | 2025-06-15 | 2009-10-29 | 21.4 | 17.0 | 6 | iPhone/iPad | #2 Lifestyle | none found |  |  |  |  | Cataloguer (manual, paid) |
| Style DNA: AI Stylist & Closet (StyleDNA) | 1358319821 | tech.escape-team.f2c | AI Style by DNA S.L. | Free | Lifestyle | 17+ | 4.29 | 7521 | 1112 | 738 | 1074 | 1.10.228 | 2026-10-05 | 2018-08-05 | 134.5 | 16.6 | 3 | iPhone/Vision | - | style.dna.app | 1,000,000+ | 3.5 | 17852 | 2026-10-02 | AI recommender+style analysis |
| ShopLook - Outfit Maker (ShopLook) | 1408832096 | com.shoplook.ShopLook | Shoplook Inc. | Free | Lifestyle | 12+ | 4.75 | 7190 | 520 | 122 | 303 | 2.7.1 | 2026-06-24 | 2018-08-02 | 99.0 | 15.6 | 1 | iPhone/iPad/iPod/Mac/Vision | - | io.shoplook | 100,000+ | 4.6 | 2480 | 2026-08-19 | Social outfit creation |
| Pureple: AI Outfit Planner (Pureple) | 628106373 | com.pureple.wardrobe | ICECLIP LLC | Free | Lifestyle | 4+ | 3.94 | 6119 | 649 | 451 | 1147 | 6.0.29 | 2026-10-23 | 2013-04-05 | 68.9 | 15.0 | 26 | iPhone/iPad/iPod/Mac/Vision | - | com.app.pureple | 1,000,000+ | 2.45 | 4004 | 2026-10-23 | Cataloguer+AI |
| Fits – Outfit Planner & Closet (Fits) | 6447482321 | de.lhenne.fitcheck | L. & J. Henne UG | Free | Lifestyle | 12+ | 4.59 | 5685 | 923 | 439 | 796 | 2.90.0 | 2026-10-06 | 2023-05-15 | 141.4 | 16.4 | 24 | iPhone/iPad/Mac/Vision | - | de.lhenne.fits | 500,000+ | 4.68 | 15106 | 2026-10-11 | Cataloguer+AI+social |
| Color Analysis - Dressika (Dressika) | 1536745174 | com.standysoftware.colorstyle | STANDY SOFT SL | Free | Lifestyle | 4+ | 4.34 | 5668 | 522 | 388 | 684 | 2.5.2 | 2026-10-15 | 2020-11-09 | 188.1 | 15.5 | 12 | iPhone/iPad/iPod/Mac | - | com.standysoftware.colorstyle | 5,000,000+ | 4.08 | 45231 | 2026-10-14 | Color analysis |
| Acloset - AI Fashion Assistant (Acloset) | 1542311809 | com.looko.acloset | Looko Inc. | Free | Lifestyle | 4+ | 4.45 | 5464 | 662 | 424 | 1002 | 6.41.2 | 2026-10-20 | 2021-03-12 | 127.6 | 15.1 | 18 | iPhone/iPad/iPod/Mac/Vision | - | com.looko.acloset | 5,000,000+ | 4.46 | 31125 | 2026-10-19 | Cataloguer+AI recommender |
| Essembl: Outfit Planner (Essembl) | 6479642952 | com.vdweb.fitgen | Essembl SA | Free | Lifestyle | 4+ | 4.54 | 4789 | 437 | 602 | 1342 | 1.0.1 | 2026-10-20 | 2024-05-24 | 137.6 | 15.0 | 1 | iPhone/iPad/iPod/Mac/Vision | - | com.essembl.app | 1,000,000+ | 4.38 | 12435 | 2026-10-18 | AI recommender |
| Smart Closet - Your Stylist (SmartCloset) | 1198057728 | com.rkk.SmartClosetRN | Rabbit Tech Inc | $2.99 | Shopping | 17+ | 4.41 | 4345 | 745 | 345 | 484 | 3.8.3 | 2025-11-10 | 2017-01-26 | 50.5 | 15.6 | 1 | iPhone/iPad/iPod/Mac/Vision | #18 Shopping | com.rkk.closet | 1,000,000+ | 2.17 | 6188 | 2025-12-01 | Cataloguer (paid) |
| Weather Fit: Outfit Planner (WeatherFit) | 1194408342 | com.antonchuiko.wthrd | Anton Chuiko | Free | Weather | 4+ | 4.63 | 3802 | 190 | 370 | 385 | 8.2.1 | 2026-10-23 | 2017-01-18 | 138.0 | 17.0 | 15 | iPhone/iPad/Mac/Vision/Apple Watch | - | none found |  |  |  |  | Weather-to-outfit utility |
| Glance - My Personal Shopper (Glance) | 6742974181 | com.glance.ai.ios | GLANCE INMOBI PTE LTD | Free | Shopping | 17+ | 4.82 | 3351 | 26 | 23 | 27 | 4.0.5 | 2026-10-22 | 2025-05-20 | 254.7 | 18.4 | 2 | iPhone/iPad | - | com.glance.ai | 100,000+ | 4.23 | 5060 | 2026-09-12 | Shopping-led AI |
| Inspoy: Outfit Maker, Planner (Inspoy) | 6480053850 | app.stylix | Applabel LTD | Free | Lifestyle | 4+ | 4.17 | 2381 | 261 | 257 | 286 | 3.27.0 | 2026-10-16 | 2024-07-11 | 226.9 | 15.5 | 8 | iPhone/iPod/Mac/Vision | - | app.stylix | 100,000+ | 3.65 | 3405 | 2026-10-15 | AI recommender+style analysis |
| Closetly - AI Wardrobe Stylist (Closetly) | 6744034749 | answersai.closetly-app | AnswersAI, Corp | Free | Lifestyle | 4+ | 4.57 | 1955 | 65 | 104 | 125 | 1.3.4 | 2026-01-30 | 2025-04-24 | 47.9 | 17 | 1 | iPhone/Mac/Vision | - | none found |  |  |  |  | AI recommender |
| GRWM - AI Stylist (GRWM_Karatkou) | 6737596512 | com.GRWM.GRWM | Aliaksei Karatkou | Free | Lifestyle | 4+ | 4.57 | 1868 | 308 | 147 | 234 | 1.1.7 | 2026-08-16 | 2024-11-04 | 51.1 | 15.0 | 6 | iPhone/iPod/Mac/Vision | - | none found |  |  |  |  | Style coach |
| Stylio - Personal Stylist (Stylio) | 6744065736 | stylio.app | nove8 LTD | Free | Lifestyle | 4+ | 3.7 | 1806 | 4 | 64 | 77 | 1.16 | 2026-10-08 | 2025-09-17 | 290.5 | 17.0 | 1 | iPhone/Mac/Vision | - | stylio.app | 10,000+ | 3.77 | 1118 | 2026-10-07 | Style coach+try-on |
| Gensmo: AI Stylist & Try-On (Gensmo) | 6636520663 | one.srp.favie | Serendipity One Inc. | Free | Shopping | 12+ | 4.68 | 1760 | 9 | 1 | 13 | 3.10.3 | 2026-01-14 | 2024-09-10 | 242.5 | 16.4 | 1 | iPhone/Mac/Vision | - | none found |  |  |  |  | Shopping-led AI + try-on |
| My Color Analysis・AI StyleLab (StyleLab) | 6468986813 | com.utilities.vstyle | IRONTECH LIMITED | Free | Lifestyle | 4+ | 4.48 | 1649 | 319 | 235 | 240 | 1.19 | 2025-08-08 | 2023-11-20 | 207.4 | 17.0 | 6 | iPhone | - | none found |  |  |  |  | Color analysis |
| Indyx: Wardrobe & Outfit App (Indyx) | 1599179405 | com.indyx.ios | Indyx Inc. | Free | Lifestyle | 4+ | 4.77 | 1636 | 246 | 165 | 238 | 9.8.5 | 2026-10-20 | 2022-01-20 | 110.6 | 15.6 | 1 | iPhone/iPad/iPod | - | com.indyx.android | 100,000+ | 4.47 | 1308 | 2026-10-19 | Cataloguer+analytics+human stylists |
| AIUTA Editorial (AiutaEditorial) | 6447555778 | com.aiuta.fashion | Aiuta USA, Inc. | Free | Lifestyle | 17+ | 4.58 | 1477 | 163 | 30 | 95 | 4.1.1 | 2026-10-01 | 2023-07-21 | 107.7 | 16.6 | 1 | iPhone/iPad/Mac/Vision | - | none found |  |  |  |  | B2B content (not consumer closet) |
| Cloey: Style Companion (Cloey) | 6753887920 | com.kevin.cloey | Kevin Tung | Free | Lifestyle | 12+ | 4.55 | 1237 | 142 | 124 | 306 | 1.1.0 | 2026-10-23 | 2026-03-13 | 140.7 | 16.4 | 1 | iPhone | - | com.kevin.cloey | 10,000+ | 4.38 | 6476 | 2026-10-12 | AI recommender |
| Outfit Formulas (OutfitFormulas) | 6447664832 | com.outfit.mobile.app | GYPO LLC. | Free | Lifestyle | 4+ | 4.4 | 1194 | 15 | 8 | 44 | 3.6.1 | 2026-10-24 | 2023-12-27 | 94.0 | 16.0 | 1 | iPhone/iPad | - | com.outfit.mobile.app | 10,000+ | 4.19 | 171 | 2026-10-23 | Capsule content subscription |
| Fitted (Fitted) | 6596771952 | com.fittedCloset | Chute Laundry Inc | Free | Lifestyle | 4+ | 4.47 | 1161 | 206 | 78 | 176 | 12.1 | 2026-10-22 | 2025-02-07 | 279.7 | 16.0 | 1 | iPhone/Mac/Vision | - | com.fittedcloset.app | 50,000+ | 4.4 | 574 |  | AI recommender+digital twin |
| Cladwell: AI Stylist & Outfits (Cladwell) | 1140550878 | com.cladwell.cladwell | Cladwell, Inc | Free | Lifestyle | 4+ | 4.25 | 1036 | 55 | 82 | 142 | 6.4 | 2026-10-24 | 2017-03-15 | 166.0 | 15.0 | 13 | iPhone/iPad/iPod/Mac/Vision | - | com.cladwell | 100,000+ | 4.01 | 518 | 2026-10-25 | AI recommender (capsule) |
| StyleScanner: Find Clothes (StyleScanner) | 6742427939 | SCGDev.ThirftAI | AntiSocial Apps LLC | Free | Shopping | 4+ | 4.53 | 802 | 169 | 83 | 80 | 1.35 | 2026-10-11 | 2025-02-25 | 48.2 | 16.0 | 1 | iPhone/iPad/Mac/Vision | - | none found |  |  |  |  | Visual search shopping |
| GetWardrobe Outfit Planner (GetWardrobe) | 656212466 | com.3botinka.ios | Outfit Makers LLC | Free | Lifestyle | 4+ | 4.3 | 789 | 107 | 71 | 83 | 2026.09.1 | 2026-10-12 | 2013-08-10 | 114.8 | 15.0 | 41 | iPhone/iPad/iPod/Mac/Vision | - | com.ThreeBoots | 1,000,000+ | 4.03 | 5043 | 2026-10-09 | Cataloguer+AI |
| SimpleCloset: Digital Wardrobe (SimpleCloset) | 1344153452 | studio.2players.Wardrobe | WU BINGRU | Free | Lifestyle | 4+ | 4.7 | 762 | 60 | 82 | 97 | 2.22.4 | 2026-05-15 | 2018-02-14 | 63.4 | 12.0 | 3 | iPhone/iPad/iPod/Mac/Vision | - | none found |  |  |  |  | Cataloguer |
| My Wardrobe - Outfit Planner (MyWardrobeInfy) | 1463372370 | life.mywardrobe.MyWardrobe | INFY LAB BILISIM VE YAZILIM TI | Free | Utilities | 4+ | 4.12 | 647 | 54 | 18 | 29 | 3.3.10 | 2026-10-21 | 2019-06-11 | 71.3 | 16.0 | 11 | iPhone/iPad/Mac/Vision | - | none found |  |  |  |  | Cataloguer |
| Outfit Planner Lookscope (Lookscope) | 1214741326 | ca.lookscope.lookscope2 | Lookscope, Inc. | Free | Lifestyle | 4+ | 4.34 | 484 | 106 | 63 | 199 | 1.18.19 | 2024-04-02 | 2018-03-17 | 78.4 | 16.0 | 9 | iPhone/Mac/Vision | - | none found |  |  |  |  | Cataloguer (stale) |
| dreamfits: try on any clothes (dreamfits) | 6744468259 | metaverse-limited.dress-me | Neer Sharma | Free | Entertainment | 4+ | 4.77 | 440 | 45 | 1 | 10 | 1.10.32 | 2026-01-23 | 2025-05-12 | 127.6 | 17.6 | 1 | iPhone/iPad/Mac/Vision | - | none found |  |  |  |  | Try-on |
| Setly - Outfit Maker & Closet (Setly) | 6752877629 | setlyapp | Setly Labs LLC | Free | Lifestyle | 12+ | 4.7 | 389 | 69 | 26 | 67 | 2.4.8 | 2026-10-21 | 2025-10-03 | 60.7 | 15.1 | 1 | iPhone/iPod | - | com.setly.setly | 1,000+ | hidden |  | 2026-10-20 | Social outfit creation |
| Lucky Sweater - Swap Closets (LuckySweater) | 1590427450 | com.isthmustrade.ios | Isthmus App, Inc. | Free | Lifestyle | 4+ | 4.9 | 362 | 4 | 0 | 71 | 58.0.0 | 2026-07-16 | 2021-10-14 | 115.8 | 16.4 | 1 | iPhone/iPad/Mac/Vision | - | none found |  |  |  |  | Closet swapping |
| Lekondo (Lekondo) | 6755449231 | com.lekondo.rayon.ios | Lekondo Inc | Free | Lifestyle | 4+ | 4.89 | 334 | 46 | 17 | 45 | 1.78.0 | 2026-10-24 | 2026-01-09 | 108.2 | 16.4 | 20 | iPhone/iPad/Mac/Vision | - | none found |  |  |  |  | Outfit journal/social |
| Atricent: Your AI Stylist (Atricent) | 6448105847 | com.Atricent | Atricent, Inc. | Free | Lifestyle | 4+ | 4.42 | 289 | 17 | 6 | 81 | 2.9.8 | 2026-10-24 | 2023-09-07 | 121.0 | 17.0 | 16 | iPhone/iPad/Mac | - | com.atricent | 10,000+ | 4 | 242 | 2026-05-25 | AI recommender |
| Save Your Wardrobe: Organiser (SaveYourWardrobe) | 1485757044 | com.saveyourwardrobe.SaveYourWardrobe | Save your wardrobe Ltd. | Free | Lifestyle | 4+ | 4.51 | 281 | 758 | 44 | 45 | 3.0.7 | 2026-10-05 | 2020-03-03 | 171.3 | 15.0 | 11 | iPhone/iPod/Mac/Vision | - | com.saveyourwardrobe.syw | 100,000+ | 3.84 | 441 | 2026-08-03 | Cataloguer (company pivoted to B2B aftersales) |
| GRWM AI - Personal Stylist (GRWM_AI_Hangzhou) | 6742329531 | com.aiany.grwm | Hangzhou Top Infinity Network  | Free | Lifestyle | 4+ | 4.19 | 263 | 30 | 12 | 16 | 2.0.0 | 2025-08-30 | 2025-02-24 | 80.1 | 15.5 | 10 | iPhone/iPod/Mac/Vision | - | none found |  |  |  |  | Style coach |
| FitRoom: Virtual Tryon Clothes (FitRoom) | 6739701827 | com.silverai.tryon | SilverAI Joint Stock Company | Free | Photo & Video | 4+ | 4.59 | 257 | 43 | 70 | 25 | 1.9.5 | 2026-09-25 | 2025-01-12 | 94.1 | 15.0 | 18 | iPhone/iPod | - | none found |  |  |  |  | Try-on |
| Stylebook Men (StylebookMen) | 515126225 | com.leftbrainrightbrain.stylebookmen | left brain / right brain, LLC | $4.99 | Lifestyle | 9+ | 4.46 | 254 | 39 | 19 | 43 | 10.1 | 2025-06-13 | 2012-04-06 | 19.9 | 17.0 | 6 | iPhone/iPad | #17 Lifestyle | none found |  |  |  |  | Cataloguer (manual, paid) |
| Outfit Maker - Pronti AI (Pronti) | 1560004583 | com.pronti-app.ProntiApp | Pronti AI Inc. | Free | Lifestyle | 17+ | 4.22 | 242 | 76 | 29 | 105 | 2.50.29 | 2026-09-09 | 2021-03-30 | 89.5 | 15.6 | 1 | iPhone/iPad/iPod/Mac/Vision | - | com.prod.pronti | 500,000+ | 3.19 | 1808 | 2026-09-09 | AI recommender (chat) |
| Stylegenai - Outfit Planner (Stylegenai) | 6745224732 | com.stylegenai.mobileapp | halil ibrahim demir | Free | Lifestyle | 17+ | 4.76 | 233 | 34 | 11 | 18 | 5.0.2 | 2026-10-02 | 2025-10-16 | 111.7 | 15.1 | 7 | iPhone/iPad/iPod/Mac/Vision | - | com.stylegenai.mobileapp | 10,000+ | 4.55 | 1353 |  | AI recommender+try-on |
| AI Outfit Maker - OnStyle (OnStyle) | 6739629908 | com.aiby.Style | AIBY Incorporated | Free | Lifestyle | 12+ | 3.11 | 187 | 18 | 17 | 34 | 1.25 | 2026-09-26 | 2025-08-04 | 146.1 | 18.0 | 3 | iPhone | - | outfit.maker.planner.color.analysis.closet.wardrobe | 1,000+ | hidden |  |  | AI recommender |
| Vesta Wardrobe: Outfit Planner (Vesta) | 6753664894 | com.megtech.Vesta | Meg Tech Inc. | Free | Lifestyle | 4+ | 4.64 | 185 | 17 | 8 | 21 | 1.4.16 | 2026-10-02 | 2025-11-06 | 81.8 | 17.0 | 1 | iPhone/iPad/Mac/Vision | - | none found |  |  |  |  | Cataloguer+AI |
| OpenWardrobe (OpenWardrobe) | 1549029468 | org.open-wardrobe | OpenWardrobe, Inc. | Free | Lifestyle | 4+ | 4.02 | 171 | 31 | 10 | 16 | 5.2.0 | 2026-09-17 | 2021-08-18 | 94.2 | 15.1 | 1 | iPhone/iPad/iPod/Mac/Vision | - | com.open_wardrobe_mobile | 100,000+ | 3.58 | 667 | 2026-09-16 | Cataloguer |
| Doji: Try On Designer Fashion (Doji) | 6737292650 | doji.labs | Doji Labs, Inc | Free | Shopping | 12+ | 4.13 | 169 | 31 | 5 | 18 | 1.1.5 | 2026-10-23 | 2025-05-06 | 462.9 | 18.0 | 1 | iPhone | - | none found |  |  |  |  | AI avatar try-on (+closet) |
| Clozzie - Your Digital Closet (Clozzie) | 6471355187 | com.pixelbrewstudio.clozzie | Pixelbrew Studio LLC | Free | Lifestyle | 4+ | 4.62 | 160 | 13 | 10 | 14 | 4.9.1 | 2026-10-06 | 2023-11-09 | 79.6 | 15.1 | 1 | iPhone/iPad/iPod | - | com.pixelbrewstudio.clozzie | 50,000+ | 4.02 | 986 | 2026-10-05 | Cataloguer |
| Garde-Robe: Closet & Wishlist (GardeRobe) | 6458191158 | com.garderobe.garderobe | Garde-Robe Inc. | Free | Shopping | 4+ | 4.73 | 105 | 2 | 2 | 7 | 4.2.2 | 2026-09-25 | 2023-09-22 | 99.4 | 15.1 | 1 | iPhone/iPad/iPod/Mac/Vision | - | none found |  |  |  |  | Wishlist/closet |
| Digital Closet Organizer－Shkaf (Shkaf) | 1530204829 | com.shkaf.app | haip IT FZ LLE | Free | Lifestyle | 4+ | 4.31 | 100 | 10 | 22 | 12 | 4.0.2 | 2026-10-15 | 2021-05-11 | 83.6 | 17.0 | 8 | iPhone/Mac/Vision | - | none found |  |  |  |  | Cataloguer |
| BetterMe Style (BetterMeStyle) | 6763523399 | world.betterme.style | BetterMe Limited | Free | Lifestyle | 17+ | 4.53 | 94 | 11 | 10 | 17 | 1.25.0 | 2026-10-23 | 2026-05-28 | 84.1 | 15.1 | 1 | iPhone/iPod | - | none found |  |  |  |  | Capsule content subscription |
| looklike: swipe fashion on you (looklike) | 6744337310 | com.egustav2027.lookapp | LookAI AB | Free | Shopping | 4+ | 4.48 | 87 | 8 | 5 | 4 | 2.142.4 | 2026-06-22 | 2025-06-16 | 104.1 | 16.4 | 1 | iPhone/Mac | - | none found |  |  |  |  | Try-on shopping |
| Daydream: AI for Fashion (Daydream) | 6692634357 | ing.daydream.daydream | DAHLIA LABS, INC. | Free | Shopping | 4+ | 4.19 | 79 | 0 | 0 | 5 | 1.30.1 | 2026-10-22 | 2025-11-17 | 83.3 | 26.0 | 1 | iPhone/iPad/Mac/Vision | - | none found |  |  |  |  | Shopping-led AI stylist |
| Dressed (Dressed) | 883938724 | com.kabukivision.Dressed | Kabuki Vision, LLC | $4.99 | Lifestyle | 4+ | 4.35 | 31 | 7 | 7 | 4 | 1.7.1 | 2026-09-10 | 2014-08-21 | 47.1 | 15.6 | 1 | iPhone/iPod/Vision | #83 Lifestyle | none found |  |  |  |  | Cataloguer (paid) |
| MUSH:Wardrobe & Outfit Planner (MUSH) | 6479453910 | style.mush.mush | Mush Style Incorporated | Free | Lifestyle | 17+ | 4.72 | 29 | 5 | 1 | 5 | 1.8.7 | 2026-10-15 | 2024-08-24 | 136.1 | 15.1 | 1 | iPhone/iPad/iPod/Mac/Vision | - | none found |  |  |  |  | Cataloguer |
| Modesona - Closet & Outfits (Modesona) | 6761113926 | com.appgeneration.aicloset | Appgeneration, Software Techno | Free | Lifestyle | 17+ | 4.65 | 23 | 6 | 4 | 1 | 4.7.0 | 2026-10-22 | 2026-05-15 | 177.8 | 18.0 | 1 | iPhone/iPad/Mac/Vision | - | ai.closet.organizer.clothes.outfit.stylist | 100,000+ | 4.12 | 545 | 2026-10-21 | AI recommender |
| AI Clothes Try-On: DressTry (DressTry) | 6502464645 | com.dresstry.app | Hazem Mostafa | Free | Shopping | 4+ | 4.27 | 15 | 4 | 2 | 1 | 1.8.0 | 2026-08-20 | 2024-05-16 | 282.4 | 15.1 | 3 | iPhone/iPod/Mac/Vision | - | none found |  |  |  |  | Try-on |
| Wearcount: Outfit Planner (Wearcount) | 6777255343 | com.realbobcorbin.Wearcount | Robert Corbin | $1.99 | Lifestyle | 4+ | 0 | 0 | 0 | 0 | 0 | 3.0.3 | 2026-10-06 | 2026-06-11 | 3.2 | 17.0 | 1 | iPhone/iPad/Mac/Vision | - | none found |  |  |  |  | Cataloguer (paid, new) |

### Inferences
- The category has a long tail of weekly-shipping indie apps with fewer than 500 ratings. The established set with real installed bases is small: Whering, Acloset, Stylebook, Pureple, Fits, Combyne, Style DNA, Essembl, Smart Closet, GetWardrobe, Indyx and Cladwell. Three 2023–2025 launches have reached that tier: Fits (2023), Essembl (2024) and Alta (2025). Alta got there fastest by a wide margin.
- "AI stylist" store traffic is split. The largest new apps by ratings velocity (Dressly, Lumi, shoppin) are selfie-based style-coach or shopping apps, not closet planners. Rankings that mix the two will overstate how well closet apps are doing.
- Stylebook's high rating count and #2 paid-Lifestyle rank sit alongside 16 months without an update. It is a cash-generating legacy product, not an innovator.

### Gaps
- Apple does not publish downloads, DAU or revenue. I had no access to paid estimators (Sensor Tower, Appfigures, data.ai), so the table has no download or revenue figures.
- I could not get historical rating counts to measure 12-month growth. The Wayback Machine returned HTTP 429 or reset the connection, so the momentum proxy is lifetime ratings per day.
- The Lookup API does not return in-app purchase price lists, and the product pages I parsed did not expose them reliably. Pricing is left to the business-model researcher.

## What do the Google Play counterparts show (installs, rating, recency)?

### Takeaway
Android confirms the iOS hierarchy and adds scale. Combyne has 10M+ installs. Whering, Acloset and Dressika have 5M+ each. Pureple, Smart Closet, GetWardrobe, Style DNA and Essembl have 1M+. Alta has 500K+ but shows no public rating. Android ratings are much lower than iOS ratings for several legacy apps (Smart Closet 2.17, Pureple 2.45, Pronti 3.19), which is a quality warning. Stylebook, Doji, Daydream, Gensmo, Lumi and Closetly have no Play app that I could find.

### Cited Findings
- Installs band / score / ratings count / last update, from [google-play-scraper](https://play.google.com/store/apps/details?id=com.whering.app) for each package, retrieved 2026-10-25:
  - **Combyne** (`com.combyne.app`): 10,000,000+ / 4.43 / 231,961 / 2026-10-06 ([Play](https://play.google.com/store/apps/details?id=com.combyne.app))
  - **Whering** (`com.whering.app`): 5,000,000+ / 4.39 / 27,917 / 2026-10-05 ([Play](https://play.google.com/store/apps/details?id=com.whering.app))
  - **Acloset** (`com.looko.acloset`): 5,000,000+ / 4.46 / 31,125 / 2026-10-19 ([Play](https://play.google.com/store/apps/details?id=com.looko.acloset))
  - **Dressika** (`com.standysoftware.colorstyle`): 5,000,000+ / 4.08 / 45,231 ([Play](https://play.google.com/store/apps/details?id=com.standysoftware.colorstyle))
  - **GetWardrobe** (`com.ThreeBoots`): 1,000,000+ / 4.03 / 5,043 / 2026-10-09 ([Play](https://play.google.com/store/apps/details?id=com.ThreeBoots))
  - **Style DNA** (`style.dna.app`): 1,000,000+ / 3.50 / 17,852 / 2026-10-02 ([Play](https://play.google.com/store/apps/details?id=style.dna.app))
  - **Smart Closet** (`com.rkk.closet`): 1,000,000+ / 2.17 / 6,188 / 2025-12-01 ([Play](https://play.google.com/store/apps/details?id=com.rkk.closet))
  - **Pureple** (`com.app.pureple`): 1,000,000+ / 2.45 / 4,004 / 2026-10-23 ([Play](https://play.google.com/store/apps/details?id=com.app.pureple))
  - **Essembl** (`com.essembl.app`): 1,000,000+ / 4.38 / 12,435 / 2026-10-18 ([Play](https://play.google.com/store/apps/details?id=com.essembl.app))
  - **Stitch Fix** (`com.stitchfix.stitchfix`): 1,000,000+ / 4.78 / 4,167 ([Play](https://play.google.com/store/apps/details?id=com.stitchfix.stitchfix))
  - **Fits** (`de.lhenne.fits`): 500,000+ / 4.68 / 15,106 / 2026-10-11; Android release 2025-03-15 ([Play](https://play.google.com/store/apps/details?id=de.lhenne.fits))
  - **Alta** (`com.alta`): 500,000+ / no public rating shown ([Play](https://play.google.com/store/apps/details?id=com.alta))
  - **Pronti** (`com.prod.pronti`): 500,000+ / 3.19 / 1,808 ([Play](https://play.google.com/store/apps/details?id=com.prod.pronti))
  - **Indyx** (`com.indyx.android`): 100,000+ / 4.47 / 1,308 / 2026-10-19 ([Play](https://play.google.com/store/apps/details?id=com.indyx.android))
  - **Cladwell** (`com.cladwell`): 100,000+ / 4.01 / 518 / 2026-10-25 ([Play](https://play.google.com/store/apps/details?id=com.cladwell))
  - **OpenWardrobe** (`com.open_wardrobe_mobile`): 100,000+ / 3.58 / 667 ([Play](https://play.google.com/store/apps/details?id=com.open_wardrobe_mobile))
  - **Save Your Wardrobe** (`com.saveyourwardrobe.syw`): 100,000+ / 3.84 / 441 / 2026-08-03 ([Play](https://play.google.com/store/apps/details?id=com.saveyourwardrobe.syw))
  - **Dressly** (`world.dressly.fashion`): 100,000+ / 4.39 / 12,436 ([Play](https://play.google.com/store/apps/details?id=world.dressly.fashion))
  - **Inspoy** (`app.stylix`): 100,000+ / 3.65 / 3,405 ([Play](https://play.google.com/store/apps/details?id=app.stylix))
  - **ShopLook** (`io.shoplook`): 100,000+ / 4.6 / 2,480 ([Play](https://play.google.com/store/apps/details?id=io.shoplook))
  - **Glance** (`com.glance.ai`): 100,000+ / 4.23 / 5,060 ([Play](https://play.google.com/store/apps/details?id=com.glance.ai))
  - **Modesona**: 100,000+ / 4.12 / 545; released on Android 2026-05-08 ([Play](https://play.google.com/store/apps/details?id=ai.closet.organizer.clothes.outfit.stylist))
  - **Cloey** (`com.kevin.cloey`): 10,000+ / 4.38 / 6,476 ([Play](https://play.google.com/store/apps/details?id=com.kevin.cloey))
  - **Fitted** (`com.fittedcloset.app`): 50,000+ / 4.4 / 574 ([Play](https://play.google.com/store/apps/details?id=com.fittedcloset.app))
  - **Clozzie**: 50,000+ / 4.02 / 986 ([Play](https://play.google.com/store/apps/details?id=com.pixelbrewstudio.clozzie))
- The scraper also returns Play's unrounded install field (`realInstalls`). Google does not display it, so treat it as indicative only. It gives Combyne 19,854,988, Dressika 6,742,601, Whering 6,125,926, Acloset 5,691,704, GetWardrobe 2,471,941, Style DNA 1,736,754, Smart Closet 1,681,593, Pureple 1,344,665, Essembl 1,214,405, Fits 956,689, Alta 772,673, Pronti 671,242 and Indyx 316,740 ([google-play-scraper on play.google.com](https://play.google.com/store/apps/details?id=com.alta)).
- I found no Play listing for Stylebook, Doji, Daydream, Gensmo, Lumi, Closetly or Google Doppl. I checked with Play search pages and the scraper; the guessed packages `ai.doji.app`, `com.gensmo.app`, `com.daydream.app` and `com.google.android.apps.doppl` all returned 404 ([Play search "Doji try on designer"](https://play.google.com/store/search?q=Doji%20try%20on%20designer&c=apps&hl=en&gl=us)).

### Inferences
- On Android, Whering and Acloset are the scale leaders among closet apps, each with about 5.7–6.1M installs by Play's raw counter. Alta reached roughly 0.77M within about 19 months.
- Pureple and Smart Closet are rated about 4 on iOS but below 2.5 on Play. Their Android experiences, or their monetisation there, are probably much weaker.

### Gaps
- Google Play does not publish install-band history. When each app crossed its current band is not public.

## Where do these apps rank in Apple's charts, and has Apple featured them editorially?

### Takeaway
On 2026-10-25 Alta was the only dedicated wardrobe app in the US Lifestyle top-free chart (#53). It also charted at GB #76, AU #48 and CA #47. Whering charted only outside the US (AU #87, CA #118, GB #141), and Dressly was at AU #59, CA #51 and GB #154. In paid charts Stylebook was #2 in US Lifestyle and Smart Closet #18 in US Shopping. No wardrobe app was in the US Lifestyle top-grossing top 100. I found no Apple editorial feature from 2025–2026 for any leading closet app; the only "featured by Apple" claims are self-reported and older.

### Cited Findings
- Apple legacy RSS feeds return at most 100 entries even when `limit=200` is requested. Feeds were updated 2026-10-25T16:13–16:14 PDT and I fetched Lifestyle (genre 6012) and Shopping (6024) top free, paid and grossing for US/GB/AU/CA ([US Lifestyle top free](https://itunes.apple.com/us/rss/topfreeapplications/limit=200/genre=6012/json); [US Lifestyle top paid](https://itunes.apple.com/us/rss/toppaidapplications/limit=200/genre=6012/json); [US Shopping top grossing](https://itunes.apple.com/us/rss/topgrossingapplications/limit=200/genre=6024/json); same pattern for gb/au/ca). Results:
  - **Lifestyle top free:** US: Alta #53 (Lemon8 #40 is the only other style-adjacent app). GB: Alta #76. AU: Alta #48, Dressly #59, Whering #87. CA: Alta #47, Dressly #51.
  - **Lifestyle top paid:** Stylebook was #2 in US, AU and CA and #6 in GB. Stylebook Men was US #17, AU #50, GB #74 and CA #81. Wearcount was CA #29.
  - **Shopping top paid:** Smart Closet ($2.99) was US #18, GB #2, AU #1 and CA #3.
  - **Top grossing:** No closet app was in the US Lifestyle top-grossing top 100. Lumi was #93 in US Shopping grossing. Smart Closet was AU #49, GB #79 and CA #100 in Shopping grossing. Atricent was CA Lifestyle grossing #81. The visual-search app StyleScanner was US Shopping grossing #58 (GB #47, AU #25, CA #22).
  - The US overall top-free 100 from the marketing-tools feed contained no candidate ([rss.applemarketingtools.com US top free](https://rss.applemarketingtools.com/api/v2/us/apps/top-free/100/apps.json)).
- Product-page chart badges go below the top 100 (fetched 2026-10-25 around 23:40 UTC):
  - US: Alta "#53 Lifestyle", Stylebook "#2 Lifestyle" (paid), Smart Closet "#18 Shopping" (paid), Stitch Fix "#142 Shopping" ([Alta](https://apps.apple.com/us/app/id6481705400); [Stylebook](https://apps.apple.com/us/app/id335709058); [Smart Closet](https://apps.apple.com/us/app/id1198057728)).
  - GB: Whering #141, Dressly #154, Inspoy #182 ([Whering GB](https://apps.apple.com/gb/app/id1519461680); [Dressly GB](https://apps.apple.com/gb/app/id6738174180)).
  - AU: Fits #194, Inspoy #165 ([Fits AU](https://apps.apple.com/au/app/id6447482321)).
  - CA: Whering #118 ([Whering CA](https://apps.apple.com/ca/app/id1519461680)).
  - No badge (not charting) on US pages for Whering, Acloset, Indyx, Fits, Pureple, Combyne, Essembl, Style DNA, Cladwell, GetWardrobe and Dressly ([e.g. Acloset](https://apps.apple.com/us/app/id1542311809)).
- Apple editorial is mostly found in apps' own copy:
  - Save Your Wardrobe lists "Featured by Apple as App of The Day", "4 New Apps We Love", "Sustainability apps we love" and "This Week's Favourites", all undated ([Lookup 1485757044](https://itunes.apple.com/lookup?id=1485757044&country=us)).
  - Lookscope claims "Editor's Recommendation of the Week (December, 2023)", which is older than 12 months ([Lookup 1214741326](https://itunes.apple.com/lookup?id=1214741326&country=us)).
  - Dressed ($4.99, 2014) says "FEATURED BY APPLE", undated ([Lookup 883938724](https://itunes.apple.com/lookup?id=883938724&country=us)).
  - No candidate's US product page carried an Editors' Choice flag. The page JSON field `isEditorsChoice` was false for every app in the "similar apps" shelves I parsed ([App Store page Alta](https://apps.apple.com/us/app/id6481705400)).

### Inferences
- Chart presence confirms Alta as the category's growth leader in late 2026, and Whering as the international (non-US) free leader.
- The paid-chart positions are high because few paid apps exist in these categories. Stylebook and Smart Closet ranking there reflects the small size of the paid Lifestyle and Shopping charts, not mass volume.

### Gaps
- This is a single-day snapshot. Apple publishes no historical chart archive, and I had no access to an aggregator's history. Chart volatility for apps ranked 40–200 is high.
- I could not find any Apple Today-tab stories or collections for 2025–2026 that include these apps. WebSearch returned none, and Apple story pages are not indexed in a searchable way.

## What is the editorial consensus in 2025–2026 "best wardrobe/outfit app" coverage?

### Takeaway
Mainstream independent "best of" lists from 2025–2026 are scarce. Most ranking content is written by competing apps for SEO. In independent coverage Alta gets the most attention (TechCrunch twice, ELLE, Vogue Business, a TIME special mention). Whering is named most often as an incumbent. In the 18 vendor-authored 2025–2026 roundups, Acloset (17), Whering (15), Stylebook (15), Indyx (13) and Cladwell (12) are near-universal, followed by Alta and Pureple (9 each).

### Cited Findings
- **Independent items dated 2025–2026 that name candidates:**
  - Hello! "Best fashion, shopping and style apps to download in 2025" lists Stylebook and Whering ([Hello!](https://www.hellomagazine.com/hfm/20221014154231/fashion-apps-to-download-right-now/)). The page's dateModified is 2024-08-20 despite the 2025 title.
  - A Considered Life, "The Best Wardrobe Tracking Apps 2025" (Jan 2025), covers Stylebook, Acloset, Whering and Indyx ([A Considered Life](https://www.aconsideredlife.co.uk/2025/01/best-wardrobe-apps-2025.html)).
  - WearView, "8 best AI fashion stylist apps in 2026" (2026-09-02), ranks Alta first, then Whering, Style DNA, Indyx, Acloset, Pureple, Combyne and Stitch Fix. WearView sells AI fashion photography, not a wardrobe app ([WearView](https://www.wearview.co/blog/best-ai-fashion-stylist-apps)).
  - TechCrunch on Google Photos' closet (2026-04-29) says it "will compete with existing apps like Acloset, Combyne, Pureple, Whering, Alta" ([TechCrunch](https://techcrunch.com/2026/04/29/google-photos-uses-ai-to-make-the-iconic-closet-from-clueless-a-reality/)).
  - TechCrunch on Alta's $11M seed (2025-06-16) names Whering and Cladwell as others in the space ([TechCrunch](https://techcrunch.com/2025/06/16/alta-raises-11m-to-bring-clueless-fashion-tech-to-life-with-all-star-investors/)).
  - Also: TechCrunch on Alta × Public School (2026-02-14) ([TechCrunch](https://techcrunch.com/2026/02/14/clueless-inspired-app-alta-partners-with-brand-public-school-to-start-integrating-styling-tools-into-websites/)); ELLE, "Alta Is the Smart Closet We Were Promised in 'Clueless'" (2025-08-01) ([ELLE](https://www.elle.com/fashion/personal-style/a65551355/alta-ai-closet-styling-app-announcement/)); Vogue Business on e.l.f. × Alta (2026-09-08) ([Vogue Business](https://www.vogue.com/article/why-elf-beauty-is-building-avatars-with-ai-platform-alta)); TechCrunch on Doji's $14M seed (2025-05-15, via search result) ([TechCrunch](https://techcrunch.com/2025/05/15/doji-raises-14m-to-make-virtual-try-ons-fun-through-ai-avatars)); TechCrunch on Daydream's chat-shopping launch (2025-06-25) ([TechCrunch](https://techcrunch.com/2025/06/25/julie-bornsteins-daydream-is-releasing-an-ai-powered-chatbot-for-fashion-related-shopping/)).
- **Older independent lists** (labelled, not counted in the 2025–26 tally):
  - MakeUseOf (modified 2023-09-18): GetWardrobe, OpenWardrobe, Acloset, XZ(Closet), Whering, Smart Closet, Save Your Wardrobe, Pureple ([MakeUseOf](https://www.makeuseof.com/best-apps-organize-closet-plan-outfits/)).
  - Good On You (2024-06-25): Whering, Indyx, Acloset ([Good On You](https://goodonyou.eco/wardrobe-organising-apps/)).
  - Harper's Bazaar UK Whering profile (2021-07-13) ([Bazaar UK](https://www.harpersbazaar.com/uk/fashion/shopping/a36228188/whering-app/)).
  - Refinery29 closet-organisation apps (2017) ([Refinery29](https://www.refinery29.com/en-us/closet-organization-apps)).
- **Awards.** A press release says TIME named Alta to "Best Inventions of 2025: Special Mentions", and Fast Company to its "Next Big Things in Tech" ([ABNewswire via FinancialContent, 2025-10-22](https://markets.financialcontent.com/pennwell.elp/article/abnewswire-2025-10-22-time-magazine-and-fast-company-have-selected-ai-styling-app-alta-for-their-lists-of-best-inventions-and-next-big-things-in-tech-of-2025)). Daydream's App Store copy claims "Special mention in Time Magazine as one of The Best Inventions of 2025" ([Lookup 6692634357](https://itunes.apple.com/lookup?id=6692634357&country=us)). Phia's copy says "Named a TIME Best Invention" ([Lookup bundleId com.phia.app](https://itunes.apple.com/lookup?bundleId=com.phia.app&country=us)). I could not open TIME's own list page to verify these.
- **Vendor-authored 2025–2026 roundups.** I counted which apps each lists as an entry, using the article headings and excluding the author's own app:
  - [Indyx 2026](https://myindyx.com/blog/the-best-wardrobe-apps); [Whering 2025](https://whering.co.uk/best-wardrobe-apps-2025); [Alta 2026](https://www.altadaily.com/blog/closet-apps-2026); [Alta 2025](https://altadaily.com/blog/best-closet-apps); [GetWardrobe 2026](https://getwardrobe.com/compare/); [COSH! 2026](https://cosh.eco/en/articles/best-digital-wardrobe-app)
  - [Modebase 2026](https://www.modebase.co/journal/best-digital-wardrobe-apps-2026); [Nouva 2026](https://www.nouva.app/blog/best-wardrobe-apps-2026-comparison); [Stylin 2026](https://stylinai.com/compare/best-virtual-wardrobe-app.html); [Wardrowbe 2026](https://wardrowbe.com/blog/best-wardrobe-apps-2026/); [Aura 2026](https://www.auraapp.fr/en/blog/meilleures-applications-garde-robe-2026); [StylePal 2026](https://www.stylepal.app/news/best-digital-wardrobe-2026)
  - [Clueless 2026](https://clueless.clothing/blog/free-clothing-inventory-app-guide/); [Beauty AI 2026](https://beautyai.app/blog/virtual-closet-apps-2026); [Future Reference 2026](https://www.futurereference.xyz/blogs/choosing-a-wardrobe-app-in-2024); [TryDrobe 2026](https://trydrobe.com/best-clothing-apps); [PutTogether 2026](https://www.puttogether.world/guides/digital-closet/closet-app-with-real-stylist-inside-2026); [Wove/Obsidian Ridge Labs 2026](https://obsidianridgelabs.com/journal/best-digital-wardrobe-apps/)
  - Fits' own "Top 8" is undated, so I excluded it from the count. It lists Fits, Acloset, SimpleCloset, Whering, Indyx, Smart Closet, GetWardrobe and Combyne ([Fits](https://fits-app.com/posts/top-8-closet-outfit-planning-apps-reviewed)).
- **Recurring verdicts in these roundups** (opinions, mostly from competitors): Whering is "best free"; Indyx is best for analytics and human stylists; Stylebook is the manual power tool with a one-time price and no AI; Acloset is best for automatic AI cataloguing but caps free use at 100 items; Cladwell is for capsule wardrobes; Alta is the free AI stylist with avatars ([Modebase](https://www.modebase.co/journal/best-digital-wardrobe-apps-2026); [Wardrowbe](https://wardrowbe.com/blog/best-wardrobe-apps-2026/); [Stylin](https://stylinai.com/compare/best-virtual-wardrobe-app.html)). Acloset's own listing confirms "free, for up to 100 items" ([Lookup 1542311809](https://itunes.apple.com/lookup?id=1542311809&country=us)).
- **Publishers I could not search.** WebSearch would not search vogue.com, harpersbazaar.com, glamour.com, cosmopolitan.com, elle.com, nytimes.com, theguardian.com, theverge.com, wired.com or cnet.com directly (domain filter refused). Publisher on-site search returned no 2025–2026 "best wardrobe apps" list from Vogue, Harper's Bazaar (US), Glamour (US/UK), Cosmopolitan, Marie Claire, Who What Wear, Refinery29 or Tom's Guide ([Vogue search](https://www.vogue.com/search?q=wardrobe%20app); [Glamour UK search](https://www.glamourmagazine.co.uk/search?q=wardrobe+apps); [Who What Wear search](https://www.whowhatwear.com/search?q=closet+app)).

### Editorial mention-count table (2025–2026 only)
"Independent" is the 10 non-competitor items listed above (the 9 used in scoring plus TechCrunch on Daydream, which is outside the ranked set) (roundups plus single-app features). "Vendor" is the 18 competitor-authored roundups, with self-mentions excluded. The weighted column counts independent items double.

| App | Independent 2025-26 items (of 10) | Vendor-authored 2025-26 roundups listing it (of 18, self excluded) | Weighted (2x indep + vendor) |
|---|---|---|---|
| Whering | 5 | 15 | 25 |
| Acloset | 3 | 17 | 23 |
| Alta | 6 | 9 | 21 |
| Stylebook | 2 | 15 | 19 |
| Indyx | 2 | 13 | 17 |
| Cladwell | 1 | 12 | 14 |
| Pureple | 2 | 9 | 13 |
| Combyne | 2 | 2 | 6 |
| Save Your Wardrobe | 0 | 4 | 4 |
| Fits | 0 | 3 | 3 |
| GetWardrobe | 0 | 3 | 3 |
| OpenWardrobe | 0 | 3 | 3 |
| Style DNA | 1 | 1 | 3 |
| Doji | 1 | 0 | 2 |
| Pronti | 0 | 2 | 2 |
| Smart Closet | 0 | 2 | 2 |
| Daydream (not in the ranked core set) | 1 | 0 | 2 |
| Essembl, Dressly, Gensmo, Inspoy, Fitted, Cloey and all other candidates | 0 | 0 | 0 |

### Inferences
- The editorial "canon" of Whering, Acloset, Indyx, Stylebook and Cladwell, plus Alta since 2025, is stable across almost every list. It is also self-reinforcing, because most lists are SEO content from small rivals copying one another's shortlists.
- Alta's independent press is unusually strong for an 18-month-old app: seed round, TIME/Fast Company lists, ELLE, Vogue Business and brand partnerships.

### Gaps
- I could not verify whether Wirecutter, The Verge, The Cut, Business Insider, Who What Wear, Glamour or Cosmopolitan published 2025–2026 roundups. Their search pages are JavaScript-rendered or blocked, and WebSearch refused those domains. This is a real coverage gap, not evidence that no such lists exist.
- TIME's own list page was not retrievable, so TIME recognition is reported via a press release and apps' own copy.

## What does community consensus (Reddit, YouTube/TikTok) recommend, and why?

### Takeaway
Across 29 Reddit recommendation threads (June 2024 to August 2026, about 828 posts and comments), the apps people actually name are Whering (20 threads), Indyx (17), Acloset (16), Alta (14), Stylebook (13) and OpenWardrobe (11). Cladwell, Smart Closet, GetWardrobe, Combyne and Pureple trail far behind. Stylebook's loyalists praise its one-time price, stats and longevity. Whering is praised as the best free option but called buggy. Indyx is praised for its calendar and analytics. Alta is praised for avatars and photo "prettify". Users explicitly allege Alta astroturfing, and the timing data is consistent with that allegation.

### Cited Findings
- **Method.** reddit.com JSON and old.reddit.com return 403 from this sandbox, but `www.reddit.com/.../search.rss` and per-thread `.rss` worked. I searched r/capsulewardrobe, r/femalefashionadvice, r/malefashionadvice, r/minimalism, r/OUTFITS, r/frugalfemalefashion and r/declutter for "wardrobe app", "closet app", "digital closet" and "outfit app". That returned 931 unique posts. I then pulled comments for 29 recommendation threads from 2024-06 onward ([example search feed](https://www.reddit.com/r/capsulewardrobe/search.rss?q=app&restrict_sr=1)).
- **Thread counts** (threads mentioning / total mentions in posts and comments), from the threads below: Whering 20/90, Indyx 17/65, Acloset 16/51, Alta 14/107, Stylebook 13/67, OpenWardrobe 11/33, Cladwell 5/6, Smart Closet 4/6, GetWardrobe 3/5, Combyne 2/2, Style DNA 2/2, Pureple 1/2. Spreadsheets/Excel appeared in 6 threads, Pinterest in 6, ChatGPT in 3 and Notion in 3. Fits, Essembl, Dressly and Daydream were essentially absent.
- **Thread URLs:**
  - 2025: [r/femalefashionadvice "which wardrobe app in 2025?" (2025-03-25, 104 entries)](https://www.reddit.com/r/femalefashionadvice/comments/1jjtgyt/the_perennial_question_which_wardrobe_app_in_2025/); [r/capsulewardrobe "A review of Alta" (2025-05-26, 164 entries)](https://www.reddit.com/r/capsulewardrobe/comments/1kvr7ia/a_review_of_alta_wardrobe_app/); [r/capsulewardrobe "So, what wardrobe indexing apps are people using?" (2025-05-01, 78)](https://www.reddit.com/r/capsulewardrobe/comments/1kc64q4/so_what_wardrobe_indexing_apps_are_people_using/); [r/capsulewardrobe "Do y'all use digital closet apps?" (2025-11-03, 99)](https://www.reddit.com/r/capsulewardrobe/comments/1onp0dk/do_yall_use_digital_closet_apps/); [r/capsulewardrobe "Suggestions for a closet/wardrobe tracking app?" (2025-03-17, 46)](https://www.reddit.com/r/capsulewardrobe/comments/1jd19pv/suggestions_for_a_closetwardrobe_tracking_app/); [r/capsulewardrobe "Recommendations for digital closet app?" (2025-06-20)](https://www.reddit.com/r/capsulewardrobe/comments/1lfvsre/recommendations_for_digital_closet_app_can_i_back/)
  - 2026: [r/femalefashionadvice "Digital wardrobe apps" (2026-03-24, 77)](https://www.reddit.com/r/femalefashionadvice/comments/1s242ng/digital_wardrobe_apps/); [r/malefashionadvice "On Wardrobe Apps" (2026-08-06)](https://www.reddit.com/r/malefashionadvice/comments/1vheqa3/on_wardrobe_apps/)
- **Year-by-year.** Mentions in search-result posts dated 2025 or later: Whering 18, Indyx 15, Acloset 12, Stylebook 8, Alta 2, OpenWardrobe 4. Across all years: Stylebook 54, Whering 38, Acloset 29, Indyx 21, Cladwell 17. So Stylebook and Cladwell dominated older discussions and Whering, Indyx and Acloset dominate 2025–26 ([search feeds as above](https://www.reddit.com/r/femalefashionadvice/search.rss?q=wardrobe%20app&restrict_sr=1&sort=relevance&t=all)). The retrieved set also has more wardrobe-app posts in recent years: 51 (2022), 108 (2023), 106 (2024), 145 (2025) and 188 (2026 to date). Half the queries were sorted by newest, though, so these counts lean towards recent posts and are not a reliable measure of growth.
- **What users say:**
  - Stylebook: "single cost to download and doesn't use a subscription model"; "used it for over a decade… stats… cost per wear" ([r/femalefashionadvice 2026-03-24](https://www.reddit.com/r/femalefashionadvice/comments/1s242ng/digital_wardrobe_apps/)).
  - Acloset: a complaint about "in-app credits that limit the amount of items" and "AI fluff" ([same thread](https://www.reddit.com/r/femalefashionadvice/comments/1s242ng/digital_wardrobe_apps/)).
  - Whering and Indyx: "whering was too buggy for me"; Indyx chosen with a paid subscription; "Alta used too much AI, Whering has bugs… their free tier has the most benefits" ([r/capsulewardrobe 2025-11-03](https://www.reddit.com/r/capsulewardrobe/comments/1onp0dk/do_yall_use_digital_closet_apps/)).
- **Alta astroturfing allegation.** A 2026 commenter wrote that "Every single reddit thread… is cluttered with heaps of bot comments praising Alta, usually… posted way later than the original thread" ([r/femalefashionadvice 2026-03-24](https://www.reddit.com/r/femalefashionadvice/comments/1s242ng/digital_wardrobe_apps/)). I measured comment timing in the 29 threads. 71% of Alta-mentioning comments (75 of 105) were posted more than 7 days after the thread opened. The same share was 15% for Indyx, 29% for Whering, 32% for Stylebook, 41% for Acloset and 27% for OpenWardrobe. This fits the allegation but does not prove it.
- **YouTube.** Search results surfaced these 2025–2026 comparison and review videos: "Best Wardrobe App 2025 – Indyx vs Whering vs Stylebook" (5,020 views, ~8 months old) ([YouTube](https://youtu.be/bY5U8KuqwiM)); "Alta Review and Styling Challenge" by Style Within Grace (7,900 views, ~7 months) ([YouTube](https://youtu.be/j6WH9pS7veo)); "Is Alta the Best AI Styling App?" (8,800 views, ~1 year) ([YouTube](https://youtu.be/BIyMEmQM3yM)); "INDYX App Review" (7,588 views, ~1 year) ([YouTube](https://youtu.be/j16ArWs27Kc)); ABC News "AI closet app promises to style your wardrobe in seconds" (2,951 views, ~5 months) ([YouTube](https://youtu.be/1rNwRwT6590)). The most-viewed category videos are generic "I digitally cataloged my closet" pieces from about 2 years ago, before October 2025: 465,709 views ([beepworld](https://youtu.be/yrAx960Ku2s)) and 176,955 views ([Christina Mychas](https://youtu.be/fj1Bp-3xaPY)). Whering's own app tour has 55,113 views (~2 years) ([YouTube](https://youtu.be/9xibJ0wWN3U)). View counts are as displayed on 2026-10-25 ([YouTube search "best wardrobe app"](https://www.youtube.com/results?search_query=best+wardrobe+app)).
- **TikTok.** One Reddit user says they found Alta on TikTok ([r/capsulewardrobe 2025-11-03](https://www.reddit.com/r/capsulewardrobe/comments/1onp0dk/do_yall_use_digital_closet_apps/)).

### Inferences
- The power users in r/capsulewardrobe and r/femalefashionadvice value data ownership, one-time pricing, stats and cost-per-wear over AI generation. That favours Stylebook, Indyx and Whering. Alta's appeal is to a broader, TikTok-driven audience.
- Spreadsheets, Notion and Pinterest are real competitors for the most engaged users.

### Gaps
- TikTok search and view data could not be retrieved from the sandbox, so TikTok is covered only anecdotally.
- Reddit RSS returns at most about 100 entries per thread, and some 2026 threads returned only the opening post. Comment counts are therefore lower bounds.
- I had no API access to Reddit vote scores, so mentions are unweighted.

## What is the market context: size, growth, key trends, entrants and exits (2023 to 2026)?

### Takeaway
Published market sizes for this niche come only from low-transparency syndicated report vendors and disagree by an order of magnitude (about $0.25B to $1.2B, CAGRs of 9–23%). They should be treated as unreliable. The verifiable story for 2023–2026 has four strands:
- AI auto-tagging and background removal became standard.
- Generative try-on moved from startups (Doji, Alta avatars) to platforms: Google Doppl launched and shut down, Google Photos Wardrobe launched, and ChatGPT added try-on.
- LLM "stylist chat" and shopping agents arrived (Daydream, Gensmo, Phia, Glance).
- Incumbents split between B2C and B2B: Save Your Wardrobe moved to aftersales, Alta added brand integrations.

### Cited Findings
- **Market-size estimates** (vendor reports; methodologies not disclosed in the free pages):
  - Intel Market Research: wardrobe app market $250M in 2025, $273M in 2026, $557M by 2034, CAGR 9.3% ([Intel Market Research](https://www.intelmarketresearch.com/wardrobe-app-2025-2032-667-1709)).
  - Research Intelo: AI Fashion Stylist App market $1.2B in 2024, reaching $7.8B by 2033, CAGR 23.1%, with Asia Pacific the fastest-growing region at 27.5% ([Research Intelo](https://researchintelo.com/report/ai-fashion-stylist-app-market)).
  - Market Intelo: "digital wardrobe platform" $1.2B in 2025, reaching $4.7B by 2034, CAGR 14.8% ([Market Intelo](https://marketintelo.com/report/digital-wardrobe-platform-market)).
  - Dataintelo, Wiseguy and Zion are cited in search snippets at 15.8%, 12.6% and 32.8% CAGR respectively. Dataintelo returned 403 to my fetch, so those figures are unverified ([Dataintelo](https://dataintelo.com/report/wardrobe-management-app-market); [Zion](https://www.zionmarketresearch.com/report/capsule-wardrobe-app-market)).
- **Funding:**
  - Alta raised an $11M seed led by Menlo Ventures (2025-06-16), with investors including Algaé Ventures (Arnault family), Anthropic's Anthology Fund, Karlie Kloss and Poshmark's CEO ([TechCrunch](https://techcrunch.com/2025/06/16/alta-raises-11m-to-bring-clueless-fashion-tech-to-life-with-all-star-investors/)).
  - Daydream raised a $50M seed and launched a chat-shopping beta with 8,000+ brands on 2025-06-25. It takes a cut of each sale ([TechCrunch](https://techcrunch.com/2025/06/25/julie-bornsteins-daydream-is-releasing-an-ai-powered-chatbot-for-fashion-related-shopping/)). Its iOS app followed on 2025-11-17 and has 79 US ratings ([Lookup 6692634357](https://itunes.apple.com/lookup?id=6692634357&country=us)).
  - Doji raised a $14M seed led by Thrive Capital in May 2025 (via search result) ([TechCrunch](https://techcrunch.com/2025/05/15/doji-raises-14m-to-make-virtual-try-ons-fun-through-ai-avatars)).
  - Gensmo reported $60M+ in seed/angel funding in mid-2025 (via search result) ([FashionUnited](https://fashionunited.uk/news/business/ai-powered-fashion-agent-gensmo-secures-60-million-dollars-in-funding/2025062782491)).
  - Vêtir raised a $5.5M Series A at a $150M valuation, per ELLE, October 2026 ([ELLE, 2026-10-21](https://www.elle.com/fashion/personal-style/a73663742/vetir-ai-closet-app-test-drive-2026/)).
- **Platform entrants:**
  - Google Photos announced an AI "Clueless"-style virtual closet on 2026-04-29. It builds the wardrobe from clothing in your photo library and adds outfit boards and virtual try-on ([TechCrunch](https://techcrunch.com/2026/04/29/google-photos-uses-ai-to-make-the-iconic-closet-from-clueless-a-reality/)). It reached all Android and iOS users in the US, Brazil and India on 2026-09-24, with Google saying wardrobe data is not shared with retailers ([TechCrunch](https://techcrunch.com/2026/09/24/google-photos-clueless-inspired-virtual-closet-is-now-available-on-android-and-ios/)). Tom's Guide says it uses Nano Banana image generation to make catalogue-style cut-outs and debuted on the Motorola Razr 2026 family ([Tom's Guide, 2026-04-29](https://www.tomsguide.com/phones/motorola-phones/i-tried-google-photos-wardrobe-on-the-motorola-razr-ultra-2026-and-its-the-ai-clueless-closet-ive-always-wanted)).
  - OpenAI rolled out virtual try-on in ChatGPT globally, using a user's reference photo and "ChatGPT Images 2.5" ([Tom's Guide, 2026-10-02](https://www.tomsguide.com/ai/chatgpt/you-can-now-virtually-try-on-clothes-with-chatgpt-before-you-buy-them-heres-how-it-works)).
  - Google Doppl launched June 2025, US only, for users 18+ ([Tom's Guide, 2025-06-27](https://www.tomsguide.com/ai/i-just-tested-googles-doppl-app-that-lets-you-try-on-clothes-with-ai-and-it-blew-me-away)). It was shut down 2026-04-30 and its tech folded into Search ([Google Labs on X](https://x.com/GoogleLabs/status/2036188820128071786)).
  - Retailers are building their own AI stylists. Vivrelle, Revolve and FWRD launched "Ella" in September 2025 ([TechCrunch](https://techcrunch.com/2025/09/04/fashion-retailers-partner-to-offer-personalized-ai-styling-tool-ella/)).
- **B2B and commerce pivots:**
  - Alta partnered with Public School New York in February 2026 on "Style with Alta" avatar try-on on the brand's website, and has partnerships with Poshmark and the CFDA ([TechCrunch](https://techcrunch.com/2026/02/14/clueless-inspired-app-alta-partners-with-brand-public-school-to-start-integrating-styling-tools-into-websites/)).
  - Alta signed a year-long exclusive beauty integration with e.l.f. Beauty in September 2026. Wang said she has "no near-term plans" for in-app checkout ([Vogue Business](https://www.vogue.com/article/why-elf-beauty-is-building-avatars-with-ai-platform-alta)).
  - Save Your Wardrobe now positions itself as a B2B aftersales platform ([saveyourwardrobe.com](https://www.saveyourwardrobe.com/)).
- **Exits before the window** (for context). Finery shut down in 2019 and Stitch Fix acquired its IP (via search result; older than 12 months) ([Retail Dive](https://www.retaildive.com/news/stitch-fix-acquires-ip-of-wardrobe-tech-company-finery/562127/)).
- **Feature trends visible in current App Store copy:**
  - AI background removal and auto-tagging: Indyx, Fits, Acloset, Cladwell, GetWardrobe.
  - Email/receipt import: Indyx, Fitted's Gmail import, Alta.
  - Virtual try-on or digital twin: Alta, Fitted, Pureple ("on a model"), Fits (credits), Doji, Gensmo.
  - LLM stylist chat: Pronti's "PAI", Cladwell's "Ask Cladwell anything", Acloset, Style DNA.
  - Weather and calendar-aware daily outfits: Whering's planner, Alta, Acloset, Essembl.
  - Cost-per-wear: Indyx, Acloset, Stylebook.
  - AI usage metering and credits: Cladwell's "monthly AI usage screen", GetWardrobe's "AI credits", Acloset item caps.

  Sources: [iTunes Lookup descriptions](https://itunes.apple.com/lookup?id=1599179405,6447482321,1542311809,1140550878,656212466,6596771952,6481705400,628106373,1560004583,1519461680&country=us).
- **Market framing.** One 2026 roundup describes the category as splitting into "apps that organise and apps that decide", with AI as "the dividing line", and data location (self-hosting) as a second split. This is a vendor opinion ([Wardrowbe](https://wardrowbe.com/blog/best-wardrobe-apps-2026/)).

### Inferences
- The biggest structural threat to standalone closet apps in 2026 is distribution, not features. Google Photos Wardrobe sits on billions of devices and fills the closet from existing photos, removing the main friction of photographing every item. ChatGPT try-on removes the need for a separate try-on app. Doppl's quick shutdown shows that Google prefers to fold these features into its existing surfaces.
- Monetisation is drifting from subscriptions to brand and affiliate revenue (Alta–e.l.f., Daydream's sale cut, Gensmo and Phia shopping agents). Analytics-first apps (Indyx, Stylebook) keep the subscription or one-time model.

### Gaps
- No credible, methodology-transparent market size exists for "digital wardrobe apps". Sensor Tower and Appfigures category data were not accessible, and I found no public Business of Fashion or Sensor Tower post sizing this niche.
- I could not verify independently the download or revenue impact of Google Photos Wardrobe on incumbents. It is too recent and no data is public.

## How does the market segment, and where does each app sit?

### Takeaway
Six consumer segments plus platform features. Most leaders now straddle two or more segments. The key boundary for an "outfit planner" ranking is whether the app works from the user's own catalogued clothes.

### Cited Findings
Segment placement is based on each app's own App Store description ([iTunes Lookup descriptions](https://itunes.apple.com/lookup?id=1519461680,1542311809,1599179405,335709058,1140550878,1198057728,628106373,989727742,6447482321,656212466,6481705400,1358319821,1560004583,6479642952,6738174180,1637080045,6692634357,6636520663,6737292650&country=us)) and the TechCrunch, Vogue Business and ELLE coverage cited above.
1. **Closet cataloguers / organisers.** Inventory, outfits, calendar, stats; you do the styling. Members: Stylebook ($4.99, manual), Whering (free, social), Indyx (analytics, human stylists, resale), Smart Closet ($2.99), GetWardrobe, Pureple, OpenWardrobe, SimpleCloset, My Wardrobe (INFY), Clozzie, Save Your Wardrobe.
2. **AI daily-outfit recommenders / stylists from your own closet.** Members: Alta, Acloset, Cladwell (capsule), Style DNA (colour/body analysis), Pronti (chat), Essembl, Fits (AI stylist chat in Pro), Closetly, Cloey, Inspoy, Fitted, Stylegenai, Atricent, OnStyle, Modesona.
3. **AI try-on / avatar / digital twin.** Members: Doji, Alta avatars, Fitted's "digital twin", Gensmo, FitRoom, dreamfits, DressTry, looklike. Platforms: Google Photos Wardrobe and ChatGPT try-on. Google Doppl is closed.
4. **Social outfit creation from brand catalogues.** Members: Combyne (catalogue of 800+ brands, challenges, feed), ShopLook, Setly, Lekondo. Adjacent: Shuffles by Pinterest.
5. **Shopping-led AI stylists / agents.** Members: Daydream, Gensmo, Glance, shoppin, Phia, StyleScanner, Vêtir (luxury), Stitch Fix (human and algorithmic service).
6. **Style-coach / self-analysis without own-closet planning.** Members: Dressly, Lumi, Dressika, My Color Analysis (StyleLab), BetterMe Style, Outfit Formulas, Stylio, GRWM.

### Inferences
- The volume leaders in segments 4–6 (Combyne, Dressly, Lumi, shoppin) are not outfit planners for your own wardrobe. The ranked leaderboard below therefore applies a core gate: the app must let users catalogue their own garments and build outfits from them. Combyne passes narrowly via its virtual wardrobe and is flagged.
- Segments 2 and 3 are merging (Alta, Fitted, Pureple and Fits all add try-on). Segment 1 is shrinking to analytics and privacy-focused users.

### Gaps
- Feature-level verification (for example, whether Combyne users can upload their own garments today) belongs to the per-app teardown phase. I relied on store copy.

## Final ranked leaderboard: method, results and raw table

### Takeaway
Under a reproducible composite score the top tier is Whering (87.9), Alta (78.9), Acloset (73.4) and Indyx (67.8). Then come Stylebook (57.4) and Combyne (56.9). A near-tie group follows: Cladwell (47.8), Pureple (47.7), Fits (47.3), Style DNA (44.7), GetWardrobe (42.9) and Essembl (41.9). Whering ranks 1–3 under every alternative weighting I tested. Alta ranks 1–5 and leads on store and press signals, but scores lower on community signals. Stylebook swings most (4–17) because it has no recent updates and no Android app.

### Cited Findings
- **Inputs** (all retrieved 2026-10-25): iTunes Lookup US/GB/AU/CA ([US batch](https://itunes.apple.com/lookup?id=1519461680,6481705400,1542311809,1599179405,335709058,989727742,1140550878,628106373,6447482321,1358319821,656212466,6479642952&country=us)); Google Play via google-play-scraper ([e.g. Whering](https://play.google.com/store/apps/details?id=com.whering.app)); Apple RSS and product-page chart badges ([US Lifestyle top free](https://itunes.apple.com/us/rss/topfreeapplications/limit=200/genre=6012/json)); the editorial and Reddit counts above ([Reddit example](https://www.reddit.com/r/capsulewardrobe/comments/1kc64q4/so_what_wardrobe_indexing_apps_are_people_using/); [Modebase example](https://www.modebase.co/journal/best-digital-wardrobe-apps-2026)).
- **Scoring method** (script `scripts/score.py`). Each signal is normalised 0–1 against the maximum in the 31-app core set.
  - **Store quality × volume (25%):** (US avg rating / 5) × log10(1 + US rating count).
  - **Reach (15%):** 0.5 × norm(log10(1 + GB + AU + CA iOS ratings)) + 0.5 × norm((Play rating, or iOS rating if hidden) / 5 × log10(1 + Play min installs)). Apps with no Android version get 0 on the Play half.
  - **Chart (10%):** best of (201 − position)/200 across Lifestyle/Shopping charts on 2026-10-25, with paid charts × 0.5 and non-US markets × 0.75.
  - **Update recency (10%):** 1.0 if updated within 30 days, 0.75 within 90, 0.5 within 180, 0.25 within 365, else 0.
  - **Editorial (15%):** 2 × independent 2025–26 items + vendor 2025–26 roundups (self excluded).
  - **Community (20%):** Reddit threads mentioning (of 29) + Reddit search posts mentioning since 2025-01-01.
  - **Feature breadth (5%):** count of 11 regex-detected features in description and release notes: own-closet, planning/calendar, AI, try-on, wear analytics, social, shopping, resale, weather, packing, chat.
- **Core gate** (manual, from store copy): the app must let users catalogue their own garments and assemble outfits from them. Excluded and listed as adjacent: Dressly, Lumi, Daydream, Gensmo, Glance, shoppin, Stitch Fix, Dressika, StyleLab, BetterMe Style, Outfit Formulas, Stylio, GRWM apps, the try-on-only apps, Weather Fit, and Google Doppl (dead).
- **Sensitivity.** Ranks were recomputed under four alternative weightings:
  - equal weights (1/7 each);
  - store-only (store 45 / reach 25 / chart 15 / recency 15);
  - buzz-only (editorial 50 / community 50);
  - no-Reddit (store 30 / reach 20 / chart 10 / recency 10 / editorial 25 / features 5).

  The last column of the table shows the min–max rank range.

### Leaderboard (base weights) with raw inputs
Columns: score 0–100. Store = rating × log10(US count). Reach = GB+AU+CA iOS ratings / Play min installs. Chart = chart score. Recency = recency score (days since last update). Editorial = independent/vendor. Reddit = threads/posts since 2025. Features = feature count. Rank range = min–max rank under the alternative weightings.

| Rank | App | Score | Store (rating x log n_US) | Reach (GB+AU+CA ratings / GP installs) | Chart | Recency (days) | Editorial (indep/vendor) | Reddit (threads/posts25) | Features | Rank range (alt weights) |
|---|---|---|---|---|---|---|---|---|---|---|
| 1 | Whering | 87.9 | 4.67 x 11,997 = 3.81 | 5,557 / 5,000,000+ | 0.43 | 1 (18) | 5/15 | 20/18 | 8 | 1-3 (eq 1, store 3, buzz 1, noRed 2) |
| 2 | Alta | 78.9 | 4.89 x 17,389 = 4.15 | 4,497 / 500,000+ | 0.74 | 1 (4) | 6/9 | 14/2 | 9 | 1-5 (eq 2, store 1, buzz 5, noRed 1) |
| 3 | Acloset | 73.4 | 4.45 x 5,464 = 3.33 | 2,088 / 5,000,000+ | 0.00 | 1 (5) | 3/17 | 16/12 | 7 | 2-5 (eq 3, store 5, buzz 2, noRed 3) |
| 4 | Indyx | 67.8 | 4.77 x 1,636 = 3.07 | 649 / 100,000+ | 0.00 | 1 (5) | 2/13 | 17/15 | 7 | 3-8 (eq 4, store 8, buzz 3, noRed 5) |
| 5 | Stylebook | 57.4 | 4.68 x 8,865 = 3.70 | 3,579 / 0+ | 0.50 | 0 (497) | 2/15 | 13/8 | 7 | 4-17 (eq 5, store 17, buzz 4, noRed 6) |
| 6 | Combyne | 56.9 | 4.76 x 87,744 = 4.71 | 12,861 / 10,000,000+ | 0.00 | 1 (23) | 2/2 | 2/0 | 4 | 2-9 (eq 6, store 2, buzz 9, noRed 4) |
| 7 | Cladwell | 47.8 | 4.25 x 1,036 = 2.56 | 279 / 100,000+ | 0.00 | 1 (1) | 1/12 | 5/1 | 6 | 6-16 (eq 8, store 16, buzz 6, noRed 8) |
| 8 | Pureple | 47.7 | 3.94 x 6,119 = 2.98 | 2,247 / 1,000,000+ | 0.00 | 1 (2) | 2/9 | 1/0 | 7 | 7-11 (eq 7, store 11, buzz 7, noRed 7) |
| 9 | Fits | 47.3 | 4.59 x 5,685 = 3.45 | 2,158 / 500,000+ | 0.03 | 1 (19) | 0/3 | 0/1 | 7 | 4-14 (eq 9, store 4, buzz 14, noRed 9) |
| 10 | StyleDNA | 44.7 | 4.29 x 7,521 = 3.33 | 2,924 / 1,000,000+ | 0.00 | 1 (20) | 1/1 | 2/0 | 5 | 7-12 (eq 11, store 7, buzz 12, noRed 10) |
| 11 | GetWardrobe | 42.9 | 4.3 x 789 = 2.49 | 261 / 1,000,000+ | 0.00 | 1 (13) | 0/3 | 3/2 | 9 | 10-15 (eq 10, store 15, buzz 10, noRed 12) |
| 12 | Essembl | 41.9 | 4.54 x 4,789 = 3.34 | 2,381 / 1,000,000+ | 0.00 | 1 (5) | 0/0 | 0/0 | 3 | 6-17 (eq 16, store 6, buzz 17, noRed 11) |
| 13 | Inspoy | 39.4 | 4.17 x 2,381 = 2.82 | 804 / 100,000+ | 0.14 | 1 (9) | 0/0 | 0/0 | 6 | 10-21 (eq 15, store 10, buzz 21, noRed 13) |
| 14 | OpenWardrobe | 39.3 | 4.02 x 171 = 1.80 | 57 / 100,000+ | 0.00 | 0.75 (38) | 0/3 | 11/4 | 9 | 8-25 (eq 12, store 25, buzz 8, noRed 23) |
| 15 | SmartCloset | 39.2 | 4.41 x 4,345 = 3.21 | 1,574 / 1,000,000+ | 0.46 | 0.25 (349) | 0/2 | 4/1 | 4 | 11-20 (eq 20, store 14, buzz 11, noRed 17) |
| 16 | Fitted | 38.9 | 4.47 x 1,161 = 2.74 | 460 / 50,000+ | 0.00 | 1 (3) | 0/0 | 0/0 | 8 | 12-19 (eq 13, store 12, buzz 19, noRed 14) |
| 17 | Cloey | 38.7 | 4.55 x 1,237 = 2.81 | 572 / 10,000+ | 0.00 | 1 (2) | 0/0 | 0/0 | 8 | 13-20 (eq 14, store 13, buzz 20, noRed 15) |
| 18 | ShopLook | 37.1 | 4.75 x 7,190 = 3.66 | 945 / 100,000+ | 0.00 | 0.5 (123) | 0/0 | 0/0 | 3 | 9-25 (eq 24, store 9, buzz 25, noRed 16) |
| 19 | SaveYourWardrobe | 36.4 | 4.51 x 281 = 2.21 | 847 / 100,000+ | 0.00 | 1 (20) | 0/4 | 0/0 | 4 | 13-19 (eq 18, store 18, buzz 13, noRed 18) |
| 20 | Stylegenai | 33.6 | 4.76 x 233 = 2.26 | 63 / 10,000+ | 0.00 | 1 (23) | 0/0 | 0/0 | 7 | 17-28 (eq 17, store 20, buzz 28, noRed 19) |
| 21 | Pronti | 32.5 | 4.22 x 242 = 2.01 | 210 / 500,000+ | 0.00 | 0.75 (46) | 0/2 | 0/0 | 8 | 15-23 (eq 19, store 23, buzz 15, noRed 20) |
| 22 | Atricent | 32.5 | 4.42 x 289 = 2.18 | 104 / 10,000+ | 0.00 | 1 (1) | 0/0 | 0/0 | 6 | 21-29 (eq 21, store 21, buzz 29, noRed 21) |
| 23 | Setly | 32.1 | 4.7 x 389 = 2.44 | 162 / 1,000+ | 0.00 | 1 (4) | 0/0 | 0/0 | 3 | 19-30 (eq 23, store 19, buzz 30, noRed 22) |
| 24 | Clozzie | 30.0 | 4.62 x 160 = 2.04 | 37 / 50,000+ | 0.00 | 1 (19) | 0/0 | 0/0 | 3 | 22-26 (eq 25, store 22, buzz 26, noRed 24) |
| 25 | MyWardrobeInfy | 28.2 | 4.12 x 647 = 2.32 | 101 / 0+ | 0.00 | 1 (4) | 0/0 | 0/0 | 4 | 23-27 (eq 27, store 24, buzz 23, noRed 25) |
| 26 | OnStyle | 27.0 | 3.11 x 187 = 1.41 | 69 / 1,000+ | 0.00 | 1 (29) | 0/0 | 0/0 | 7 | 22-31 (eq 22, store 30, buzz 31, noRed 28) |
| 27 | Doji | 26.9 | 4.13 x 169 = 1.84 | 54 / 0+ | 0.00 | 1 (2) | 1/0 | 0/0 | 5 | 16-29 (eq 26, store 29, buzz 16, noRed 26) |
| 28 | Vesta | 26.5 | 4.64 x 185 = 2.11 | 46 / 0+ | 0.00 | 1 (23) | 0/0 | 0/0 | 4 | 27-30 (eq 28, store 27, buzz 27, noRed 30) |
| 29 | SimpleCloset | 26.0 | 4.7 x 762 = 2.71 | 239 / 0+ | 0.00 | 0.5 (163) | 0/0 | 0/0 | 4 | 22-29 (eq 29, store 26, buzz 22, noRed 29) |
| 30 | Closetly | 25.8 | 4.57 x 1,955 = 3.01 | 294 / 0+ | 0.00 | 0.25 (268) | 0/0 | 0/0 | 5 | 18-30 (eq 30, store 28, buzz 18, noRed 27) |
| 31 | Lookscope | 20.4 | 4.34 x 484 = 2.33 | 368 / 0+ | 0.00 | 0 (936) | 0/0 | 0/0 | 6 | 24-31 (eq 31, store 31, buzz 24, noRed 31) |

### Top 12 at a glance (base ranking)
1. **Whering:** free cataloguer with a social layer. US 4.67★ / 11,997; Play 5M+. Charts in GB, AU and CA. Most-mentioned on Reddit and in independent and vendor lists. Weaknesses: bug complaints, no US chart presence.
2. **Alta:** AI stylist with avatar try-on. US 4.89★ / 17,389, the fastest-growing closet app. Only wardrobe app in the US Lifestyle top-free chart (#53). Strongest press. Play 500K+ with no public rating; suspected Reddit astroturfing; English only.
3. **Acloset:** AI cataloguing plus daily outfits. US 4.45★ / 5,464; Play 5M+ (4.46★ / 31K); 18 languages. Free tier capped at 100 items. Near-universal in roundups.
4. **Indyx:** analytics, cost-per-wear and human stylists. US 4.77★ / 1,636 but heavy Reddit advocacy (17 threads). Small Android base (100K+); English only.
5. **Stylebook:** paid ($4.99) manual power tool. US 4.68★ / 8,865; #2 paid Lifestyle in US, AU and CA. No update since 2025-06-15 and no Android.
6. **Combyne:** social outfit creation, with by far the largest volume (US 87,744; Play 10M+). Rarely recommended as a closet planner (2 Reddit threads); it does a different job.
7. **Cladwell:** capsule-focused AI stylist. Small store footprint (US 1,036; GB rating 3.91) but strong editorial canon presence.
8. **Pureple:** veteran planner, now with AI and try-on. US 3.94★ and Play 2.45★ are quality red flags; 26 languages.
9. **Fits:** fast-rising German planner (US 5,685; Play 500K+ at 4.68★; 24 languages). Little editorial or Reddit presence yet.
10. **Style DNA:** AI stylist with colour and body analysis. US 4.29★ / 7,521; Play 1M+ at 3.5★.
11. **GetWardrobe:** veteran cross-platform planner (Play 1M+; 41 languages; US only 789 ratings).
12. **Essembl:** AI daily planner. US 4.54★ / 4,789; Play 1M+ at 4.38★; strong in Canada. No editorial or Reddit presence.

### Inferences
- The ranking is robust at the top. Whering, Alta, Acloset and Indyx occupy the top four under the base and equal weights, and Whering, Alta and Acloset stay in the top five under every scheme.
- Positions 7–12 are statistically indistinguishable (within about 6 points) and should be presented as a tier, not a strict order.
- If the report emphasises "best for a new user in October 2026" and discounts the Reddit astroturfing concern, Alta has a credible claim to #1. It wins the store-only and no-Reddit variants. If it emphasises trusted community recommendation, Whering, Indyx and Stylebook rise.

### Gaps
- Feature breadth is regex-detected from store copy (5% weight) and is noisy. For example, Stylebook triggers the "AI" flag because it mentions AI image generation for imports. Per-app teardowns should replace it.
- No download, retention or revenue data went into the score because none is public. A Sensor Tower or Appfigures pull would materially improve the store components.
- The single-day chart snapshot and 12-month-old-plus Reddit threads (from 2024-06) are included. Signals dated before 2025-10-25 are older than 12 months: the 2024 Reddit threads, Good On You 2024 and MakeUseOf 2023. Only the threads feed the score; the editorial score counts 2025–26 items only.
