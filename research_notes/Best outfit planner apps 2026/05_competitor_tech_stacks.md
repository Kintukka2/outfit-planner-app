# Competitor tech stacks: what public evidence shows about how leading outfit-planner / digital-wardrobe apps are built (as of 25 Oct 2026)

Scope: Whering, Acloset, Indyx, Stylebook, Cladwell, Smart Closet, Pureple, Combyne (covered in depth), then Alta Daily, Google Doppl, Daydream, Style DNA and Gensmo (covered as far as the evidence goes). No binaries were downloaded or decompiled. No accounts were created and no forms were submitted. Evidence comes from: app-store metadata (iTunes Search API, App Store pages including privacy labels, Google Play listings and Data safety pages); first-party privacy policies, terms, sub-processor lists and careers pages; job boards; Google Patents; vendor and research pages; and my own Playwright inspection of public marketing sites, public web-app shells, JS bundles, response headers and `/.well-known/` files (run 25 Oct 2026).

Grading used throughout:
- **Confirmed**: the company says so in a first-party document (privacy policy, sub-processor list, job post, store listing), or I saw it directly in production code, headers or files the company serves.
- **Strongly indicated**: two or more independent indirect signals agree.
- **Weakly indicated**: one indirect signal, or a signal that has other plausible explanations.

Caveat that applies to everything below: what I saw in web bundles describes each company's *web* properties. Carrying that over to the native mobile app is an inference and is graded as one.

---

## 1. SDK fingerprints (Exodus Privacy, AppBrain, 42matters, Apptopia/AppFigures, tracker lists)

### Takeaway
I could not get per-app Android SDK lists with versions. Exodus Privacy, AppBrain, the Wayback Machine and APK mirrors were all unreachable from this sandbox on 25 Oct 2026, and 42matters, Apptopia and AppFigures put SDK data behind a login. I rebuilt SDK inventories from other sources instead: vendor lists in privacy policies, App Store privacy labels, Play Data safety sections, one NowSecure risk page (Gensmo), and SDKs loaded on the companies' websites. Treat the inventories below as "named vendors". They are not exhaustive decompiled SDK lists.

### Cited Findings
**Access attempts (method record)**
- Exodus Privacy report and search pages (`https://reports.exodus-privacy.eu.org/en/reports/com.whering.app/latest/`, `/en/reports/search/whering/`) failed on 25 Oct 2026 with TLS connection resets via curl, HTTP 503 via WebFetch and HTTP 502 via headless Chromium. Retried twice. — [Exodus report URL](https://reports.exodus-privacy.eu.org/en/reports/com.whering.app/latest/)
- AppBrain app pages returned a Cloudflare "Just a moment..." challenge (HTTP 403) to curl, Playwright and WebFetch. — [AppBrain Whering](https://www.appbrain.com/app/com.whering.app)
- The Wayback Machine availability API returned HTTP 429 for every package. APKCombo was blocked at the proxy (ERR_TUNNEL_CONNECTION_FAILED). — [Wayback API](https://archive.org/wayback/available?url=reports.exodus-privacy.eu.org/en/reports/com.whering.app/latest/)

**Package and bundle identifiers (needed for any future SDK lookup)**
- Whering: Android `com.whering.app`, iOS `com.whering.app` (App Store ID 1519461680, seller Whering Ltd). The universal-links file also lists dev flavors `com.whering.app.dev` for both OSes. — [iTunes Search API](https://itunes.apple.com/search?term=whering&entity=software&country=us); [Whering AASA](https://app.whering.co.uk/.well-known/apple-app-site-association); [Whering assetlinks](https://app.whering.co.uk/.well-known/assetlinks.json)
- Acloset: Android and iOS `com.looko.acloset` (iOS ID 1542311809, Looko Inc.). — [Play](https://play.google.com/store/apps/details?id=com.looko.acloset); [iTunes Search API](https://itunes.apple.com/search?term=acloset&entity=software&country=us)
- Indyx: Android `com.indyx.android`, iOS `com.indyx.ios` (ID 1599179405). The AASA file also lists a second iOS app ID, `com.indyx.laurieharden`. — [Indyx AASA](https://app.myindyx.com/.well-known/apple-app-site-association)
- Stylebook: iOS only, `com.leftbrainrightbrain.stylebook` (ID 335709058, seller "left brain / right brain, LLC"). A Play search found no Android app. — [iTunes Search API](https://itunes.apple.com/search?term=stylebook&entity=software&country=us)
- Cladwell: Android `com.cladwell`, iOS `com.cladwell.cladwell` (ID 1140550878). — [Cladwell AASA](https://app.cladwell.com/.well-known/apple-app-site-association)
- Smart Closet (Rabbit Tech Inc): Android `com.rkk.closet`, iOS **`com.rkk.SmartClosetRN`** (ID 1198057728). The AASA also lists `com.rkk.SmartClosetRN.localdebug`. — [iTunes Search API](https://itunes.apple.com/search?term=smart+closet&entity=software&country=us); [smartcloset.me AASA](https://smartcloset.me/.well-known/apple-app-site-association)
- Pureple (Iceclip LLC): Android `com.app.pureple`, iOS `com.pureple.wardrobe` (ID 628106373). — [pureple.com AASA](https://pureple.com/.well-known/apple-app-site-association)
- Combyne: Android `com.combyne.app`, iOS `com.pungme.combyne` (ID 989727742). — [iTunes Search API](https://itunes.apple.com/search?term=combyne&entity=software&country=us)
- Alta Daily (Flagship AI, Inc.): Android `com.alta`, iOS `com.altadaily.main` (ID 6481705400). — [Alta AASA](https://www.altadaily.com/.well-known/apple-app-site-association)
- Daydream (Dahlia Labs, Inc.): iOS `ing.daydream.daydream` (ID 6692634357). No Android listing turned up in US Play searches. — [Daydream AASA](https://daydream.ing/.well-known/apple-app-site-association)
- Style DNA (AI Style by DNA S.L.): Android `style.dna.app`, iOS `tech.escape-team.f2c` (ID 1358319821). — [iTunes Search API](https://itunes.apple.com/search?term=style+dna&entity=software&country=us)
- Gensmo (Serendipity One Inc.): Android `one.srp.gensmo.app`, iOS **`one.srp.favie`** (ID 6636520663). On 25 Oct 2026 the US Play details page for `one.srp.gensmo.app` returned "Not Found", but gensmo.com still declares the package in assetlinks. — [Gensmo assetlinks](https://gensmo.com/.well-known/assetlinks.json); [Play URL](https://play.google.com/store/apps/details?id=one.srp.gensmo.app)

**The only third-party binary analysis found: NowSecure (Gensmo Android)**
- NowSecure's public risk page for "Gensmo – Your Fashion AI Agent" (`one.srp.gensmo.app`, Android, version 121; page published 2025-09-29, modified 2025-10-15) lists these "AI Observations": **ML Kit** and **LiteRT model**. Network hosts observed: amazon.com, cloudflare.com, facebook.com, google.com, plus 2 more not shown. Permissions shown include camera, POST_NOTIFICATIONS, GET_ACCOUNTS, biometrics (USE_BIOMETRIC / USE_FINGERPRINT), RECEIVE_BOOT_COMPLETED, FOREGROUND_SERVICE, WAKE_LOCK, read/write external storage and READ_MEDIA_IMAGES/visual-user-selected, plus 13 more not shown. — [NowSecure Gensmo](https://www.nowsecure.com/?p=30563)

**Named-vendor SDK and service inventories, by app (first-party documents unless stated)**
- **Whering**, privacy-policy vendor list: "Google LLC (Google Cloud Platform, Firebase); Mongo DB Inc. (Mongo DB Cloud Services); Nevercode Ltd. (Codemagic); Pixyle B.V. (pixyle.ai); Kaleido AI GmbH. (remove.bg)" under Infrastructure. Also Vercel (web hosting), Firebase (monitoring, registration and authentication), Adjust, Google Analytics and Mixpanel (tracking), Braze and Instabug (customer support), Builder.io (website CMS). Login options: Facebook, Google and Apple. — [Whering privacy policy](https://www.whering.co.uk/privacy)
- **Cladwell**, privacy policy (last updated 5 Aug 2026): "Hosting, storage, delivery, and search: Amazon Web Services, Vercel, Cloudinary for legacy images, and Typesense"; "AI and photo analysis: OpenAI; OpenRouter and approved routed providers, including Microsoft Azure and Google Cloud Vertex AI"; "Payments and subscriptions: Apple App Store, Google Play, RevenueCat, Stripe, and legacy Squarespace Commerce"; "Communications and support: OneSignal, Postmark, Cladwell's self-hosted Listmonk service, Zendesk, and Google Workspace"; "Analytics and reliability: Google Analytics, Firebase Analytics, Firebase Crashlytics, Segment, and PostHog where enabled"; "Location and weather: Google Maps, Apple WeatherKit, and Open-Meteo"; legacy Typeform, Zapier, Google Sheets, Mailchimp; sign-in with Apple, Google, Facebook, Pinterest. — [Cladwell privacy policy](https://cladwell.com/privacy-policy)
- **Pureple**, privacy policy (last updated 21 Feb 2026): OpenAI ("the image is sent to OpenAI's Vision API to automatically identify attributes such as category, color, pattern, material, brand, season, and occasion"; AI Styling chat sends up to 20 recent messages plus a wardrobe summary, city/country and app version; outfit recommendations send wardrobe descriptions, location and weather). Google Gemini ("Try on Model" sends outfit image and preferred gender; "See on Yourself" sends the body photo plus outfit image, premium only, 18+ with explicit consent). Firebase "for authentication, cloud database (Firestore), file storage, push notifications, analytics, and crash reporting". — [Pureple privacy policy](http://www.pureple.com/Privacy.aspx)
- **Combyne**, privacy policy: "Our app is hosted by Amazon AWS"; Google Firebase / Google Cloud Platform "to, among other things, find errors in the app"; "Mongo DB Atlas cloud database"; Intercom chat; Mailchimp/Mandrill newsletters; Google Analytics; Facebook registration. — [Combyne privacy policy](https://www.combyne.com/privacy-policy-en)
- **Smart Closet**, privacy policy: "Your account information and content are stored in Google Cloud, Amazon AWS and Mongo DB"; Google Analytics; sign-in with Google, Facebook and Apple. — [Smart Closet privacy policy](https://smartcloset.me/privacy-policy)
- **Style DNA**, privacy policy (effective 1 Jun 2026): AWS and Google Cloud ("uploading encrypted images to our cloud provider"), **Smartlook** (session analytics; "will never record your photographs"), **Amplitude**, **AppsFlyer**, **Firebase**, "ChatGPT/Open AI" and "Google/Gemini" for chat features; Facebook/Instagram login. — [Style DNA privacy policy](https://styledna.ai/privacy-policy/)
- **Alta**, privacy policy (effective 1 Jan 2026): Google Calendar events are processed by "TypeSafe, which classifies events and does not retain the data, and Google Cloud Vertex AI, which generates outfit recommendations"; Meta Pixel. The terms say "we may share your User Content with third-party services, including but not limited to OpenAI". — [Alta privacy policy](https://www.altadaily.com/privacy); [Alta terms](https://www.altadaily.com/terms-conditions)
- **Gensmo**, sub-processor list (updated 5 Feb 2026): Cloudflare (CDN), "Atlas Mongo" (database storage, US), Google Cloud Platform (cloud infrastructure and computing, US), GitHub (code repository), Google Workspace. — [Gensmo sub-processors](https://gensmo.com/about/legal/sub-processors)
- **Daydream**, privacy policy (effective 24 Sep 2026) names service-provider *categories* only (hosting, cloud, AI and ML, analytics and so on). It discloses hashed emails and phone numbers to "advertising partners such as Meta and Google" for conversion measurement, and says "Powered by Daydream" interactions train its search and recommendation models. — [Daydream privacy policy](https://daydream.ing/legal/privacy-policy)
- **Acloset**, privacy policy (effective 29 Apr 2026) names no processors. It mentions Apple/Google IAP for "Beans" and subscriptions, and Gmail access via Google OAuth to import purchase receipts under the Google API Limited Use policy. — [Acloset privacy policy (Notion)](https://marmalade-polonium-d4f.notion.site/Privacy-Policy-320d318ed93147f3884a6b87565cfc34)
- **Indyx** and **Stylebook** privacy policies name no SDK vendors. — [Indyx privacy](https://www.myindyx.com/privacy-policy); [Stylebook privacy](https://www.stylebookapp.com/privacy.html)

**Web SDKs I saw loaded (25 Oct 2026; version shown where the asset exposed one)**
- Daydream (daydream.ing): Firebase Auth (`identitytoolkit`/`securetoken.googleapis.com`; project `daydream-auth-prod`, authDomain `login.daydream.ing`); Amplitude analytics, Session Replay (`background-capture-1.0.3`) and Experiment flags (proxied via `/experiment/sdk/v2/vardata`); Datadog browser RUM (`dd-evp-origin-version=7.7.0`, proxied via `/api/monitoring-dd`); Customer.io CDP plus Gist in-app messaging (`*.gist.build`); Contentful CMS (`cdn.contentful.com`, space `81lx55co6tj0`); Impact affiliate tracking (`utt.impactcdn.com`, `daydream.pxf.io`); SaaSquatch referrals; Google Tag Manager/GA4/Google Ads; Meta Pixel 2.9.427 plus Meta Conversions API Gateway; Cloudinary references. — [daydream.ing](https://daydream.ing/)
- Alta (altadaily.com): Statsig (`statsigapi.net`, plus its ad-block-resistant domains `featureassets.org` and `prodregistryv2.org`), Grafana Faro RUM (`faro-collector-prod-us-east-2.grafana.net`, app name "Alta", version = `VERCEL_DEPLOYMENT_ID`), TikTok Pixel, Meta Pixel plus Conversions API Gateway, Crisp chat, Cloudinary (`res.cloudinary.com`), Cloudflare Images (`imagedelivery.net`), CloudFront video, `browser-image-compression@2.0.2`. — [altadaily.com/login](https://www.altadaily.com/login)
- Gensmo (gensmo.com): Firebase (project `srpproduct-dc37e`, authDomain `auth.gensmo.com`, GA measurement ID), Sentry (`ingest.us.sentry.io`), Cloudflare Web Analytics, and optional `eruda` mobile debug console when a flag is set. — [gensmo.com](https://gensmo.com/)
- Whering web app (app.whering.co.uk): Firebase Auth (project `whering`), Redux Toolkit, axios. Marketing site: Builder.io, YouTube embeds, Google Ads. — [app.whering.co.uk](https://app.whering.co.uk/login); [whering.co.uk](https://whering.co.uk/)
- Cladwell web app (app.cladwell.com): Stripe.js v3 (web checkout, including Link and Klarna payment-method code), GA4, Google Maps JS. Marketing site: Microsoft Clarity. — [app.cladwell.com](https://app.cladwell.com/); [cladwell.com](https://cladwell.com/)
- Smart Closet web (smartcloset.me): Firebase (project `smart-closet-c490e`), Apollo GraphQL client (`graph.smartcloset.me/graphql`), Google Identity Services sign-in, GA4, Cloudflare. — [smartcloset.me](https://smartcloset.me/)
- Pureple web (pureple.com/login): Firebase (`pureple-prod.firebaseio.com`), Ant Design, GA4. — [pureple.com/login](https://pureple.com/login)
- Style DNA site: WordPress 6.4.13, Elementor 3.19.4 / Elementor Pro 3.19.2, AIOSEO 4.7.0, jQuery 3.7.1, Meta Pixel, GA4. — [styledna.ai](https://styledna.ai/)
- Combyne site: Framer (generator "Framer 121674c"). Acloset site: GA4 only. Indyx site: Squarespace plus GA4. Stylebook site: GA4 plus Pinterest widget. — [combyne.com](https://www.combyne.com/); [acloset.app](https://www.acloset.app/); [myindyx.com](https://www.myindyx.com/); [stylebookapp.com](https://www.stylebookapp.com/)

### Inferences
- Tracking SDKs, from privacy labels plus vendor lists: Whering (Adjust + Mixpanel + GA, "Identifiers used to track"), Combyne (usage and advertising data used to track; ads in app), Acloset (contact info, identifiers and usage data used to track; "Contains ads" on Play), Style DNA (AppsFlyer + Amplitude; usage data used to track), Cladwell (purchases and identifiers used to track) and Daydream (identifiers and usage data used to track; Meta/Google hashed matching) almost certainly ship an attribution or ad SDK. Strongly indicated for each.
- Pureple's iOS label says "Data Not Collected", but its own privacy policy lists Firebase Analytics and Crashlytics and its Play Data safety lists device IDs, photos, personal info and crash logs. The iOS label is very likely understated (strongly indicated; see §2).
- Gensmo's Android app runs on-device ML through Google ML Kit and a LiteRT (TensorFlow Lite) model. Likely uses are face/pose or segmentation for try-on photo prep or image labelling. That ML runs on device is confirmed by NowSecure; what it is used for is weakly indicated.

### Gaps
- No version-level Android SDK lists for any app: Exodus, AppBrain, APK mirrors and Wayback were unreachable, and paid SDK databases need a login. A follow-up from a different network should pull Exodus reports for the 12 package names above.
- No iOS framework-level SDK lists exist publicly for any of these apps.
- I could not confirm which paywall/subscription SDK (RevenueCat, Adapty, Superwall, Qonversion) any app uses, except Cladwell (RevenueCat named in its privacy policy). No RevenueCat, Adapty or Superwall customer story for these apps turned up.

---

## 2. App Store / Google Play metadata clues

### Takeaway
Store metadata is the hardest evidence available for client tech. Smart Closet's iOS bundle ID literally ends in "RN" (React Native). Daydream requires iOS 26 and advertises Siri and Apple Intelligence integrations, which points to native Swift. Stylebook is a 21 MB iPhone/iPad-only app whose AI image generation "requires Apple Intelligence", which points to native code using Apple frameworks. Privacy labels and Data safety sections line up with the named vendors in §1.

### Cited Findings
**iOS size, minimum OS, devices (iTunes Search API / App Store, 25 Oct 2026)**
| App | iOS version (date) | Size | Min iOS | Devices | Languages |
|---|---|---|---|---|---|
| Whering | 3.4.147 (7 Oct 2026) | 193.7 MB | 15.5 | iPhone only (runs on Apple-silicon Mac, Vision) | EN + 5 |
| Acloset | 6.41.2 (20 Oct 2026) | 127.6 MB | 15.1 | iPhone + iPad | EN + 17 |
| Indyx | 9.8.5 (20 Oct 2026) | 110.6 MB | 15.6 | iPhone + iPad; "Supports Wallet" | EN |
| Stylebook | 10.1 (15 Jun 2025) | 21.4 MB | 17.0 | iPhone + iPad (no Mac/Vision listing); Family Sharing | EN + 5 |
| Cladwell | 6.4 (24 Oct 2026) | 166 MB | 15.0 | iPhone + iPad | EN + 12 |
| Smart Closet | 3.8.3 (10 Nov 2025) | 50.5 MB | 15.6 | iPhone + iPad | EN |
| Pureple | 6.0.29 (23 Oct 2026) | 68.9 MB | 15.0 | iPhone + iPad | EN + 25 |
| Combyne | 8.10.4 (2 Oct 2026) | 98.2 MB | 17.6 | iPhone only | EN, DE, ES |
| Alta Daily | 2.2.8 (21 Oct 2026) | 133.1 MB | 16.4 | iPhone + iPad | EN |
| Daydream | 1.30.1 (22 Oct 2026) | 83.3 MB | **26.0** | iPhone + iPad | EN |
| Style DNA | 1.10.228 (5 Oct 2026) | 134.5 MB | 16.6 | iPhone only | EN, DE, IT |
| Gensmo | 3.10.3 (14 Jan 2026) | 242.5 MB | 16.4 | iPhone only | EN |
Sources: [iTunes Search API results](https://itunes.apple.com/search?term=whering&entity=software&country=us) (one query per app) and App Store pages, e.g. [Whering](https://apps.apple.com/us/app/whering-your-digital-closet/id1519461680), [Acloset](https://apps.apple.com/us/app/acloset-ai-fashion-assistant/id1542311809), [Indyx](https://apps.apple.com/us/app/indyx-wardrobe-outfit-app/id1599179405), [Stylebook](https://apps.apple.com/us/app/stylebook/id335709058), [Cladwell](https://apps.apple.com/us/app/cladwell-ai-stylist-outfits/id1140550878), [Smart Closet](https://apps.apple.com/us/app/smart-closet-your-stylist/id1198057728), [Pureple](https://apps.apple.com/us/app/pureple-ai-outfit-planner/id628106373), [Combyne](https://apps.apple.com/us/app/combyne-your-perfect-outfit/id989727742), [Alta](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400), [Daydream](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357), [Style DNA](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821), [Gensmo](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663)

**Google Play (US, 25 Oct 2026)**
- Whering 5M+ downloads, 4.4★ (27.9K reviews), updated 5 Oct 2026. Acloset 5M+, 4.5★ (31.1K), "Contains ads", Editors' Choice, updated 19 Oct 2026. Indyx 100K+, 4.5★ (1.31K). Cladwell 100K+, 4.0★ (518), updated 25 Oct 2026. Smart Closet 1M+, 2.2★ (6.19K), last updated 1 Dec 2025, category Shopping. Pureple 1M+, 2.5★ (4K), "Contains ads", category Beauty. Combyne 10M+, 4.4★ (232K), "Contains ads", rated Teen. Alta 500K+. Style DNA 1M+, 3.5★ (17.9K). — [Whering](https://play.google.com/store/apps/details?id=com.whering.app), [Acloset](https://play.google.com/store/apps/details?id=com.looko.acloset), [Indyx](https://play.google.com/store/apps/details?id=com.indyx.android), [Cladwell](https://play.google.com/store/apps/details?id=com.cladwell), [Smart Closet](https://play.google.com/store/apps/details?id=com.rkk.closet), [Pureple](https://play.google.com/store/apps/details?id=com.app.pureple), [Combyne](https://play.google.com/store/apps/details?id=com.combyne.app), [Alta](https://play.google.com/store/apps/details?id=com.alta), [Style DNA](https://play.google.com/store/apps/details?id=style.dna.app)
- The Play web listing no longer shows a permissions list, so I could not read permissions for any app from Play (NowSecure covered Gensmo only; see §1).

**App Store privacy labels (25 Oct 2026)**
- Whering: tracking = Identifiers. Linked = phone number (marketing), product interaction (analytics), name, email, crash data. Not linked = user ID, other usage data. — [Whering App Store](https://apps.apple.com/us/app/whering-your-digital-closet/id1519461680)
- Acloset: tracking = contact info, identifiers, usage data. Not linked = device ID for **third-party advertising** and analytics, crash data, performance data, photos (app functionality, customer support), coarse location (linked). — [Acloset App Store](https://apps.apple.com/us/app/acloset-ai-fashion-assistant/id1542311809)
- Indyx: no tracking. Linked = physical address, email, name, phone, user ID, product interaction, **payment info** (app functionality), device ID. Not linked = other diagnostic data. — [Indyx App Store](https://apps.apple.com/us/app/indyx-wardrobe-outfit-app/id1599179405)
- Stylebook: **only** "Data Not Linked to You: Analytics – Product Interaction". — [Stylebook App Store](https://apps.apple.com/us/app/stylebook/id335709058)
- Cladwell: tracking = purchases, identifiers. Linked = email, name, purchase history, coarse and precise location, user ID, device ID, photos, crash data. Not linked = purchase history and device ID for **third-party advertising**. — [Cladwell App Store](https://apps.apple.com/us/app/cladwell-ai-stylist-outfits/id1140550878)
- Smart Closet: no tracking. Linked = photos, other user content, user ID. Not linked = product interaction (analytics), crash data. — [Smart Closet App Store](https://apps.apple.com/us/app/smart-closet-your-stylist/id1198057728)
- Pureple: "Data Not Collected – The developer does not collect any data from this app." — [Pureple App Store](https://apps.apple.com/us/app/pureple-ai-outfit-planner/id628106373); contrast with its [privacy policy](http://www.pureple.com/Privacy.aspx) (Firebase analytics and crash reporting) and [Play Data safety](https://play.google.com/store/apps/datasafety?id=com.app.pureple) (collects device IDs, photos, name/email/user IDs, crash logs and diagnostics)
- Combyne: tracking = usage data. Linked = usage and **advertising data** for third-party advertising, email, name, photos, other user content, user ID. Not linked = coarse location, device ID, contacts, crash/performance/diagnostics. — [Combyne App Store](https://apps.apple.com/us/app/combyne-your-perfect-outfit/id989727742)
- Alta: no tracking. Linked = product interaction, precise location, email, name, phone, photos, search history, user ID, device ID. Not linked = crash data, customer support. — [Alta App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400)
- Daydream: tracking = identifiers, usage data. Linked = device ID, product interaction and **advertising data** for third-party advertising, email, name, phone, search history, user ID, crash and performance data. — [Daydream App Store](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357)
- Style DNA: tracking = usage data. Linked = user ID, email. Not linked = usage data for third-party advertising, crash and performance data, photos. — [Style DNA App Store](https://apps.apple.com/us/app/style-dna-ai-stylist-closet/id1358319821)
- Gensmo: all "Not Linked": analytics usage, crash and performance data, email, name, photos, device ID. — [Gensmo App Store](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663)

**Play Data safety (25 Oct 2026)**
- Whering *shares* photos and videos (app functionality). It collects in-app messages, app interactions (analytics, advertising/marketing), name/email/phone, **precise location**, device IDs, **contacts (optional)**, crash logs and diagnostics. — [Whering Data safety](https://play.google.com/store/apps/datasafety?id=com.whering.app)
- Acloset *shares* approximate and precise location (precise for "Advertising or marketing"). It collects device IDs (developer communications, advertising, fraud prevention), purchase history, photos and crash logs. — [Acloset Data safety](https://play.google.com/store/apps/datasafety?id=com.looko.acloset)
- Cladwell *shares* device IDs and purchase history for advertising or marketing. — [Cladwell Data safety](https://play.google.com/store/apps/datasafety?id=com.cladwell)
- Combyne *shares* photos (fraud prevention), approximate location (advertising), app interactions (analytics, personalization), device IDs (advertising), crash logs and email. — [Combyne Data safety](https://play.google.com/store/apps/datasafety?id=com.combyne.app)
- Alta *shares* device IDs, crash logs, app interactions and search history (analytics), email (developer communications), phone (account management) and photos (app functionality). — [Alta Data safety](https://play.google.com/store/apps/datasafety?id=com.alta)
- Style DNA *shares* crash logs (analytics). — [Style DNA Data safety](https://play.google.com/store/apps/datasafety?id=style.dna.app)
- Indyx, Smart Closet and Pureple declare "No data shared with third parties". — [Indyx](https://play.google.com/store/apps/datasafety?id=com.indyx.android); [Smart Closet](https://play.google.com/store/apps/datasafety?id=com.rkk.closet); [Pureple](https://play.google.com/store/apps/datasafety?id=com.app.pureple)

**Extensions, widgets, OS integrations and in-app purchases (App Store text)**
- Acloset: "New home screen widgets: see today's outfit suggestion and your OOTD" (v6.41.x, Oct 2026); avatar try-on (v6.28.2, Jul 2025); "Beautify" image cleanup extended to accessories (v6.33.0, Aug 2025); Gmail/purchase-history import. IAPs: Basic/Premium/Expert subscriptions and "Beans" virtual currency. — [Acloset App Store](https://apps.apple.com/us/app/acloset-ai-fashion-assistant/id1542311809)
- Alta: "Home Screen widget, event and trip planning … more realistic avatars" (v2.2.8). — [Alta App Store](https://apps.apple.com/us/app/alta-daily-digital-ai-closet/id6481705400)
- Daydream: v1.29.0 (Sep 2026) "added support for iOS 27 … On iOS 27 devices with Apple Intelligence, we've enhanced image search … we'll identify and label the products in it" and "If you've enabled Siri AI on iOS 27, you can search Daydream with Siri". — [Daydream App Store](https://apps.apple.com/us/app/daydream-ai-for-fashion/id6692634357)
- Stylebook: "AI background removal", "Generate clothing images with AI* … *AI image generation requires Apple Intelligence", "Fix old images with 'Clean Up'", iCloud backup, automatic iPhone/iPad sync, Wi-Fi sharing between devices, web clipper with built-in browser. Paid upfront ($4.99). — [Stylebook App Store](https://apps.apple.com/us/app/stylebook/id335709058)
- Cladwell: "Connect Cladwell to ChatGPT from Settings", "Made for iPad and Split View", "New closet items show up straight away while we improve the photo, and a slow upload is never lost", saved try-ons on outfit cards. — [Cladwell App Store](https://apps.apple.com/us/app/cladwell-ai-stylist-outfits/id1140550878)
- Combyne: v8.7.1 (Jun 2025) "Free AI Background Removal … automatic AI cut-out tool", "Ad Item Import"; later "Fire wallet" virtual currency, sponsored content and "Multiple AI try-on … bug fixes". IAPs: Premium and Superlikes. The copyright line reads "© 2025 SIA URSTYLE". — [Combyne App Store](https://apps.apple.com/us/app/combyne-your-perfect-outfit/id989727742)
- Whering: "Add your own photos – we'll remove the background", "Download our Chrome Extension and digitize as you go", catalog "of over 100 million items". IAPs include AI "Credits" (10/50/100) and "Supporter" tiers. — [Whering App Store](https://apps.apple.com/us/app/whering-your-digital-closet/id1519461680)
- Indyx: "automatically remove the background, auto-tag with AI … transform them into clean, editorial flatlays. You can also forward shopping receipts or paste in product links". — [Indyx App Store](https://apps.apple.com/us/app/indyx-wardrobe-outfit-app/id1599179405)
- Smart Closet: "Auto-remove background of your clothes by one-click"; IAP "Auto Backup & Sync" $0.99/mo or $3.99. — [Smart Closet App Store](https://apps.apple.com/us/app/smart-closet-your-stylist/id1198057728)
- Pureple: "virtual try-on" (v6.0.7, Sep 2025); "Backup via iCloud/iTunes; Premium adds cloud sync". A reviewer reports having to "watch an ad to remove the background". — [Pureple App Store](https://apps.apple.com/us/app/pureple-ai-outfit-planner/id628106373)
- Gensmo: credit packs ($5–$50); the app is flagged as containing advertising. — [Gensmo App Store](https://apps.apple.com/us/app/gensmo-ai-stylist-try-on/id6636520663)
- None of the AASA files I fetched declares App Clips. No app advertises an Apple Watch app (a Smart Closet reviewer asks for one). — [Smart Closet App Store](https://apps.apple.com/us/app/smart-closet-your-stylist/id1198057728)

### Inferences
- **Smart Closet uses React Native** (strongly indicated): the bundle ID `com.rkk.SmartClosetRN` plus a `.localdebug` variant is a naming fingerprint, and the company's web stack is JS/GraphQL (§3).
- **Daydream iOS is native Swift/SwiftUI** (strongly indicated): minimum iOS 26.0, iPhone+iPad, and day-one adoption of iOS 27 Apple Intelligence and Siri (App Intents) features. Cross-platform frameworks rarely force iOS 26 as the floor.
- **Stylebook is a native iOS app (Swift/Obj-C)** (strongly indicated): 21 MB, 17-year-old iOS-only codebase, Apple-Intelligence-gated image generation (most likely Apple's Image Playground API), and iCloud sync. Its background removal is probably on-device Apple Vision (iOS 17+ subject lifting): weakly indicated by the iOS 17 floor and the near-empty privacy label.
- Whering's 193.7 MB, Gensmo's 242.5 MB and Cladwell's 166 MB iOS binaries are large for utility apps. That fits bundled engines and ML assets (Flutter engine for Whering, confirmed separately in §4), but size alone is a weak signal.
- Virtual currencies or credit packs (Whering Credits, Acloset Beans, Gensmo Credits, Combyne Fire) show AI features are metered per generation. That implies server-side generative inference with a per-call cost. Strongly indicated.

### Gaps
- Android minimum SDK, ABI splits and permissions were not readable from Play web pages, and APK metadata sites were blocked.
- Google Doppl's store listings have been removed (the app shut down on 30 Apr 2026), so its size, minimum OS and privacy label could not be retrieved.

---

## 3. Web presence: frameworks, hosting, API hosts, CDNs and analytics (Playwright inspection, 25 Oct 2026)

### Takeaway
The web layer is mostly Next.js on Vercel or Cloudflare (Whering, Alta, Cladwell web app, Smart Closet, Gensmo, Daydream). Marketing sites use Astro, Framer, Squarespace, WordPress or static HTML. Several public bundles expose real architecture. Alta's client bundle carries server environment-variable checks naming Supabase, Upstash/Vercel KV Redis and **Temporal**, plus an LLM model-tier enum including `fast_claude`, `reasoning_claude`, `fast_open_source` and per-category fine-tunes (`ft_womens_dresses` and so on). Daydream's site talks to a Connect-RPC BFF behind Envoy on Google Cloud. Whering's web app calls a NestJS/Express REST API on Google Cloud plus two internal services named after *Clueless* characters (`cher`, `dionne`).

### Cited Findings
**Whering**
- whering.co.uk is Next.js (pages router) on **Vercel** (`server: Vercel`, `x-powered-by: Next.js`) with **Builder.io** CMS (`cdn.builder.io`). — [whering.co.uk](https://whering.co.uk/)
- The web app **app.whering.co.uk** is Next.js App Router (RSC `?_rsc=` requests) on Vercel. It uses Firebase Auth (project `whering`, `whering.firebaseapp.com`) and Redux Toolkit thunks calling `https://api.whering.co.uk/app/v1/...` (endpoints include `auth/generate-auth-code`, `lookbooks`, `outfit`, `piece`, `thoughts`, `users`, `users/suggested_users`, `wishlists/items`, `wishlists/folders`). A second axios client with `baseURL: "https://dionne.whering.co.uk"` attaches the Firebase user token and serves `/api/v1/marketplace/products` and `/api/v1/marketplace/search/similar`. — [app.whering.co.uk/login](https://app.whering.co.uk/login)
- `api.whering.co.uk` answers with `x-powered-by: Express`, `server: Google Frontend`, `via: 1.1 google` and `x-cloud-trace-context` (Google Cloud serverless/LB fingerprint). `cher.whering.co.uk` (newsletter endpoint `/api/v1/admin/newsletter`) answers `Apache/2.4.41 (Ubuntu)`. — [api.whering.co.uk](https://api.whering.co.uk/); [cher.whering.co.uk](https://cher.whering.co.uk/)

**Acloset**
- acloset.app is an **Astro** static site served from **Amazon S3 via CloudFront** (`server: AmazonS3`, `x-cache: Hit from cloudfront`). Images come from `media.acloset.app` (S3 + CloudFront). GA4 only. — [acloset.app](https://www.acloset.app/)

**Indyx**
- myindyx.com is **Squarespace** (Cloudflare in front) and loads helper scripts from a Google Cloud Storage bucket, `storage.googleapis.com/squarestylist/`. — [myindyx.com](https://www.myindyx.com/)
- `app.myindyx.com` is not a web app. It is a universal-link fallback page ("We weren't able to redirect you automatically … OPEN INDYX") with an `indyx://app.myindyx.com/` custom scheme. Its AASA lists `9XHLP25YU4.com.indyx.ios` **and `9XHLP25YU4.com.indyx.laurieharden`**. — [app.myindyx.com](https://app.myindyx.com/); [Indyx AASA](https://app.myindyx.com/.well-known/apple-app-site-association)

**Stylebook**
- stylebookapp.com is a hand-written static HTML site on Apache, with GA4 and a Pinterest widget. — [stylebookapp.com](https://www.stylebookapp.com/)

**Cladwell**
- cladwell.com is **Astro** on **Vercel**, with Microsoft Clarity. — [cladwell.com](https://cladwell.com/)
- **app.cladwell.com** is a Next.js (pages router) web app with Stripe.js v3 checkout and an auth/account API client at `https://account.cladwell.com`. AASA covers `com.cladwell.cladwell`, and assetlinks covers `com.cladwell`. — [app.cladwell.com](https://app.cladwell.com/)
- cladwell.com/chatgpt: "Cladwell connects your closet to ChatGPT … ChatGPT prepares and shows a clean clothing picture, then saves it with the required details directly to the right capsule". "See it on me … does not use Cladwell's image-generation service". — [Cladwell in ChatGPT](https://cladwell.com/chatgpt)

**Smart Closet**
- smartcloset.me is Next.js (pages router, `next-head-count`) behind **Cloudflare**. It has a logged-in web closet (`/closet`, `/wishlist`, `/shop/...`, `/signin`). The Apollo GraphQL client points at `https://graph.smartcloset.me/graphql`, which answers `x-powered-by: Express` behind Cloudflare. The Firebase config uses project `smart-closet-c490e`, plus Google Identity Services. — [smartcloset.me](https://smartcloset.me/); [graph.smartcloset.me](https://graph.smartcloset.me/)

**Pureple**
- pureple.com is a client-rendered React app (Ant Design) with Firebase (`pureple-prod.firebaseio.com`) and a `/login` web app. Responses carry `x-served-by: cache-iad-…` / `x-cache` headers of the kind Firebase Hosting's Fastly edge emits. — [pureple.com/login](https://pureple.com/login)

**Combyne**
- combyne.com is **Framer** (`server: Framer/24ab00f`, generator "Framer 121674c"). — [combyne.com](https://www.combyne.com/)

**Alta**
- altadaily.com is Next.js on **Vercel**, with a web sign-in at `/login`. `api.altadaily.com` also answers `server: Vercel`. — [altadaily.com](https://www.altadaily.com/); [api.altadaily.com](https://api.altadaily.com/)
- The client bundle includes server-config validation text: "SUPABASE_URL, SUPABASE_US_EAST_2_REPLICA_URL and SUPABASE_INTERNAL_REPLICA_URL must all point at the local stack, or none of them". Further env names: `REDIS_URL_V3`, `KV_REST_API_URL`, `TEMPORAL_GRPC_ENDPOINT`, `NOTIFICATIONS_TEMPORAL_GRPC_ENDPOINT`. It also bundles the Upstash Redis client and an OpenTelemetry tracer ("kv-tracer"). — [altadaily.com/login](https://www.altadaily.com/login)
- The same bundle has a zod enum of model tiers: `fast`, `fast_fallback`, `fast_exp`, `fast_claude`, `fastest`, `fastest_claude`, `fastest_mini`, `reasoning`, `reasoning_fallback`, `reasoning_claude`, `document_extraction`, `fast_open_source`, `dress_codes`, `ft_womens_dresses`, `ft_womens_shoes`, `ft_womens_skirts`, `ft_womens_tops`, `ft_womens_bottoms`, `ft_womens_outerwear`, `ft_womens_bags`. Next to it are clothing-category, occasion, weather-feel (°F thresholds such as ≥86 "scalding", ≥76 "hot") and precipitation enums, and Cloudinary asset names for shoe "VTON" placeholders. — [altadaily.com/login](https://www.altadaily.com/login)
- Media and CDN: Cloudflare Images (`imagedelivery.net`), Cloudinary, two CloudFront distributions for landing videos and fonts. — [altadaily.com](https://www.altadaily.com/)

**Daydream**
- daydream.ing is Next.js (Turbopack chunks, RSC) behind a **Google Cloud load balancer** (`via: 1.1 google`). Firebase Auth uses a custom auth domain (`login.daydream.ing`, project `daydream-auth-prod`). Loading the public homepage automatically created an anonymous "provisional user" (`accounts:signUp`, `/api/provisional-user`). That is site behavior; I submitted nothing. — [daydream.ing](https://daydream.ing/)
- API: `POST https://bff.shopper.api-web.dahlialabs.dev/home.v1.HomeService/GetModuleList` (Connect-RPC style path; the bundle references connectrpc.com). That host answers `server: envoy`, `x-envoy-upstream-service-time`, `via: 1.1 google`. — [bff.shopper.api-web.dahlialabs.dev](https://bff.shopper.api-web.dahlialabs.dev/)
- Images: `https://cdn.dahlialabs.dev/fotomancer/_/raw:1/plain/...`, an URL grammar that matches the open-source **imgproxy** format. Contentful (`images.ctfassets.net`) serves editorial images. — [daydream.ing](https://daydream.ing/)
- Footer links to `jobs.ashbyhq.com/daydream-ai` (0 open positions on 25 Oct 2026) and `partners.daydream.ing`. — [Daydream Ashby](https://jobs.ashbyhq.com/daydream-ai)

**Gensmo**
- gensmo.com is Next.js App Router on **Cloudflare Workers** (`server: cloudflare`, `server-timing: cfWorker;dur=…`, `x-powered-by: Next.js`). Its runtime config: `apiHost: {api: "/api", account: "https://account.gsmo.ai", recommend: "https://gateway.gsmo.ai", workflow: "https://gem-workflow.gsmo.ai"}` plus Firebase auth on `auth.gensmo.com`. The `*.gsmo.ai` hosts answer `server: cloudflare` + `via: 1.1 google` (Cloudflare in front of Google Cloud). Images are on `gimg.iminsp.com` (Cloudflare). Public share routes include `/share/try-on/:id` and `/share/moodboard/:id`. "Gensmo studio" (`studio.gensmo.com`, Next.js on Cloudflare) is linked as "New". — [gensmo.com](https://gensmo.com/); [studio.gensmo.com](https://studio.gensmo.com/)

**Style DNA**
- styledna.ai runs WordPress/Elementor on `nginx/1.22.1` + `PHP/8.2.35`. Some assets load from `dev-frontend.dnato.style` and a raw AWS EC2 IP (18.222.77.212, over plain http). `api.styledna.ai` answers `nginx/1.24.0 (Ubuntu)` behind `via: 1.1 google`. `app.styledna.ai` (Cloudflare) redirected my headless browser to an anti-crawler tarpit page, so I collected nothing from it. — [styledna.ai](https://styledna.ai/); [api.styledna.ai](https://api.styledna.ai/)

### Inferences
- **Alta backend = TypeScript on Vercel (Next.js API routes / api.altadaily.com) + Supabase Postgres (with a us-east-2 read replica) + Upstash/Vercel KV Redis + Temporal workflows (including a separate notifications Temporal cluster) + OpenTelemetry/Grafana Faro** (strongly indicated; the env checks leaked into the client bundle). The tier enum points to an LLM router that falls back across providers (OpenAI default per the ToS; Claude variants; an open-source model; Vertex AI for calendar outfits per the privacy policy) and to **fine-tuned per-category women's-wear models** (`ft_womens_*`), probably fine-tuned classifiers/taggers or generators for item attributes (weakly indicated as to their exact purpose).
- **Whering backend = NestJS on Google Cloud (Cloud Run behind Google Front End) + Firestore/Firebase Auth + MongoDB Atlas**. The `dionne` service handles marketplace product search including "similar" (visual or embedding similarity) search. Confirmed by job posts and the privacy policy (§4), plus headers.
- **Daydream backend = gRPC/Connect services behind an Envoy-based gateway on Google Cloud** (GKE or Cloud Run with Envoy), a web BFF, and an imgproxy-based image service ("fotomancer"). Strongly indicated. The language is not shown; Connect-RPC is most common with Go/TypeScript (weakly indicated).
- **Gensmo = Cloudflare edge (Workers for web) in front of GCP compute with MongoDB Atlas.** Separate account, recommendation-gateway and "gem-workflow" generation services. Strongly indicated (sub-processor list + headers + config).
- **Smart Closet = React Native app + Node/Express GraphQL (Apollo) API + MongoDB, with Firebase for auth or sync**, fronted by Cloudflare. Strongly indicated.
- **Pureple = Firebase-centric serverless app** (Auth, Firestore, Storage, FCM, Hosting) with direct OpenAI and Gemini calls, most likely through Cloud Functions (weakly indicated as to Functions; Firebase itself is confirmed).
- **Indyx white-labels its iOS app for stylists**: the second app ID in its AASA is named after a stylist. That points to a multi-target/flavor build of one codebase (weakly indicated; I could not find that app in the US App Store).

### Gaps
- Indyx, Acloset, Combyne and Style DNA expose no public web app bundle, so I could not see their API hostnames from the web.
- Alta's `app.altadaily.com` and several `api.*` subdomains for Indyx, Acloset, Combyne and Pureple did not resolve or returned proxy 502 errors.

---

## 4. Job postings, founder interviews, engineering talks and GitHub

### Takeaway
Job posts settle the client framework for Whering (**Flutter** + Patrol tests, with native Swift/Kotlin bridges) and the backends of Whering (**TypeScript/NestJS on GCP: Cloud Run, Cloud Functions, Firestore, MongoDB, BullMQ/Redis, BigQuery/Dataflow/Pub/Sub, Vertex AI**) and Acloset (**Go/Echo/Gorm + Python on AWS ECS/EC2/Batch/Lambda + on-prem Kubernetes; Postgres, Redis, OpenSearch, ClickHouse, Qdrant, DynamoDB**). Acloset's mobile app is **React Native**: stated in a 2020 Looko post and still a skill tag on Looko's latest posting. Alta hires for React/Next.js/TypeScript/Python/Node and **SwiftUI**. Daydream's CEO describes a query-understanding layer that routes to many "mini models" built and benchmarked across OpenAI and Gemini.

### Cited Findings
**Whering** (careers page JSON lists active and inactive posts with dates)
- "Flutter Developer" (created 26 Sep 2025, inactive): "Proven, expert-level experience building, deploying, and maintaining commercial Flutter applications"; "Deep expertise with the Firebase suite (Firestore, Cloud Functions, Auth) and experience integrating with REST APIs." — [Whering careers](https://whering.co.uk/careers); also listed on [Welcome to the Jungle](https://app.welcometothejungle.com/jobs/9RVsBZ0P/company)
- "Senior Mobile Engineer" (15 Jan 2026): "Deep expertise in building, shipping, and maintaining polished mobile applications using Flutter"; "Strong background in native development (Swift or Kotlin) with the ability to bridge custom widgets and integrate low-level OS features." — [Whering careers](https://whering.co.uk/careers); [WTTJ](https://app.welcometothejungle.com/jobs/46mDfp32/company)
- "Mobile QA Engineer" (15 Jan 2026): "automated test strategies for Flutter (using Patrol) and Web (using Playwright)." — [Whering careers](https://whering.co.uk/careers)
- "Head of Engineering" (3 Nov 2025): "TypeScript and Nest.js … GCP (Cloud Run, Cloud Functions, Firestore), and iOS and Android apps (Flutter)." — [Whering careers](https://whering.co.uk/careers); [WTTJ](https://app.welcometothejungle.com/jobs/S9ovVMFB/company)
- "Backend Engineer" (16 Sep 2025): "TypeScript and Nest.js … GCP … hands-on experience in both Firestore and MongoDB … React/Next.js." — [Whering careers](https://whering.co.uk/careers)
- "Senior Product Engineer (Backend-Leaning Fullstack)" (active, posted 30 Apr 2026, £70–80k): "TypeScript/NestJS services powering our mobile app"; "GCP (Cloud Run, Cloud Functions, Firestore), CI/CD pipelines, and event-driven patterns (BullMQ/Redis or similar)"; "building our new B2B offering from the ground up." — [Whering careers](https://whering.co.uk/careers)
- "Data Engineer" (18 Sep 2025): "Python and SQL … GCP, including … BigQuery, Dataflow, and Pub/Sub." "Middle/Senior ML Engineer" (15 Jul 2024): "computer vision and NLP models … several deployed computer vision models and the development of a recommendation engine … BigQuery, Vertex AI, and Cloud Storage … PyTorch/TensorFlow." "Machine Learning Engineer" (7 Mar 2023): "Computer Vision … visual search, object detection, automated tagging … Dataflow/Apache Beam." — [Whering careers](https://whering.co.uk/careers)
- "Product Owner" (19 Mar 2026) cites "nine million downloads". A CRM Specialist post (Dec 2025) asks for "Braze". — [Whering careers](https://whering.co.uk/careers)

**Acloset (Looko Inc., 룩코)**
- Wanted posting "[에이클로젯] Software Engineer (Problem Solver)" (closed, undated; mentions break-even "last September"). Company: 9M cumulative users, 110M registered garments, 1M+ new garments per week. Acloset is evolving into "MCP 기반 차세대 디지털 옷장 플랫폼" (an MCP-based next-generation digital-closet platform usable by AI agents). Stack: "주요 백엔드 스택: Go, Echo, Gorm, Python / 클라우드: AWS / 데이터베이스: Postgresql, Redis, Opensearch, Clickhouse, Qdrant, DynamoDB / 인프라: AWS ECS, EC2, Batch, Lambda, Kubernetes(On Premise), Github Action / 모니터링: Elastic APM, Kibana, Cloudwatch". Company skill tags on the post: Git, GitHub, JIRA, Slack, React, TypeScript, AWS, **React Native**, Figma. Benefits include Claude, GPT and Gemini tools for staff. — [Wanted job 391802](https://www.wanted.co.kr/wd/391802) (read via its public JSON at [wanted.co.kr/api/v4/jobs/391802](https://www.wanted.co.kr/api/v4/jobs/391802))
- Seoul National University CSE notice by "스타트업 룩코" (30 Jul 2020): "프런트: React-Native / 서버: Java Spring / AI-API: python Django". — [SNU CSE notice](https://cse.snu.ac.kr/community/notice/18309)

**Alta (Flagship AI)**
- Paraform listing "Full Stack Engineer" (SoHo NYC, $175K–$250K): tech stack "React, Next.js, TypeScript, Python, Node.js, Swift UI"; "Contributing to ranking algorithms and recommendation systems for Alta's new social feed"; "Alta is a top 100 app on the App Store in over 13 countries." — [Paraform Alta](https://www.paraform.com/share/alta/cmqzfcpgm000a0cldqx9mic9z)
- Menlo Ventures job board "Software Engineer" (posted 13 Jul 2026, closed): "Experience with React, TypeScript, Python. Have worked with NodeJS and ideally NextJS." — [Menlo jobs](https://jobs.menlovc.com/companies/alta-2-74f7de65-629e-4098-8a5a-fe5d04313e81/jobs/86174248-software-engineer)

**Daydream (Dahlia Labs)**
- CEO Julie Bornstein on The Kara Goldin Show (Ep. 779; undated, likely Dec 2025): "we have a query understanding layer, then we go out to different models"; "we have a series of models that are each focused on different elements of a fashion query"; "we actually build those models directly with llms, but we test each mini model with different llms"; "We actually work with a number of models, not just open AI"; "if we run Mini models at the same time, it's much faster"; "we also take the catalogs of all of the brands and retailers"; "our engineering team is about 25 people". — [Kara Goldin podcast 779](https://karagoldin.com/podcast/779)
- A secondary summary of the same interview says some mini models go to OpenAI and some to Gemini, chosen on "a combination of accuracy and speed". — [Kara Goldin podcast 779](https://karagoldin.com/podcast/779)

**Combyne**
- An expired iOS developer posting (listing expired 3 Jul 2020) asked for Swift and Objective-C and said the team was moving from Objective-C to Swift (per search-indexed text; the page now 404s). — [Hitmarker listing](https://hitmarker.net/jobs/combyne-ios-developer-remote-senior-junior-491738)
- StackShare's Combyne profile (undated, "updated 1 year ago" per search snippet) lists Amazon EC2/S3, Firebase, Docker, BigQuery, Python, Java, Swift, Android SDK, Node.js, PostgreSQL, MongoDB, Kotlin, TypeScript, Kubernetes, GraphQL, GitHub and Bitrise. I could not load the page directly (HTTP 429), so this is unverified. — [StackShare Combyne](https://stackshare.io/combyne/combyne)

**Indyx / Stylebook / Cladwell / Gensmo / Style DNA / Pureple / Smart Closet**
- Indyx: 2021 and 2024 profiles list Hilary Parker as CTO and say the team includes an "ex Stitchfix data scientist". No engineering job post or stack statement turned up. The Indyx careers page lists no openings. — [Daily Drop-out profile](https://dailydropout.substack.com/p/-indyx-supercharge-your-wardrobe); [Indyx careers](https://www.myindyx.com/careers)
- Stylebook: run by founders Bill and Jessica Atkins; "Bill handles the coding and technical work". — [Princeton Info](https://princetoninfo.com/business/an-app-for-wardrobe-management/)
- Gensmo: CEO Ning Hu was "tech lead for Mobile Search at Google" and later a VP at Alibaba, with a CMU AI PhD. The June 2025 press release claims a "proprietary try-on model, trained on hundreds of thousands of looks" and "deep learning, computer vision, and style preference algorithms". — [Gensmo about](https://gensmo.com/about); [PR via AI Journal, 27 Jun 2025](https://aijourn.com/?p=355323)
- Cladwell was acquired in Aug 2019 (about $330K ARR at sale per its founder). — [theygotacquired](https://theygotacquired.com/saas/cladwell-acquired/)

### Inferences
- **Whering client: Flutter**. Confirmed by four separate job posts, with `.dev` flavors visible in the AASA/assetlinks files and Codemagic (Nevercode) named as a processor, which is the CI/CD service that grew out of the Flutter ecosystem.
- **Acloset client: React Native** (strongly indicated: the 2020 Looko post says so directly, and the 2025–26 post still tags React Native). Backend moved from Java Spring + Django (2020) to Go + Python on AWS (confirmed for the current backend).
- **Alta iOS: native SwiftUI** (strongly indicated: SwiftUI is the only mobile technology in its stack list). Android framework unknown (likely Kotlin, weakly indicated by elimination).
- **Daydream search: LLM-based query decomposition plus parallel specialist "mini models"** over an ingested multi-retailer catalog (confirmed in the CEO's own words). §5 adds the embedding and vector-search details.
- **Combyne iOS: native Swift (legacy Obj-C)**, Android native Kotlin (strongly indicated for iOS as of 2020; weakly indicated for today).

### Gaps
- No public GitHub organisation, engineering blog or conference talk turned up via web search (25 Oct 2026) for any of the 13 companies. No Stack Overflow or Dev.to posts by identifiable company engineers turned up either. Searches for Indyx, Smart Closet, Gensmo and Style DNA engineering stacks returned only unrelated results.
- No public job post or engineer profile states the mobile framework for Indyx, Cladwell, Pureple, Style DNA or Gensmo.
- Daydream's Ashby board was empty on 25 Oct 2026, so I found no Daydream language or infra statement.
- Combyne's current ownership (the copyright line now reads "SIA URSTYLE") and current stack are unverified.

---

## 5. Patents and patent applications

### Takeaway
Looko (Acloset) has the deepest patent portfolio in the set: seven-plus KR/US/WO filings covering virtual-closet construction from photos, outfit-combination generation, clothing-image correction, garment measurement from images and size recommendation. Dahlia Labs (Daydream) has one pending US application on "pivot" search: LLM decomposition, an embedding model predicting a target-product embedding, and vector search with structured filters. Serendipity One (Gensmo) holds a US patent on LLM intent recognition followed by recall and generative-model presentation, which mentions virtual try-on. No patents were found for Whering, Cladwell, Indyx, Combyne, Pureple, Style DNA or Rabbit Tech.

### Cited Findings
- **Looko US20230140225A1** (priority 30 Jul 2020): "a user terminal detecting an image including a user's face, extracting a region corresponding to at least one fashion item … restoring the fashion item … collecting a fashion image matched to the restored fashion item … the server analyzing a wearing date and wearing frequency". — [Google Patents US20230140225A1](https://patents.google.com/patent/US20230140225A1/en)
- **Looko KR102471490B1** (priority 20 Jul 2021) "Digital closet building system": links social-media or identifier accounts to receive "core information related to clothes purchased online or offline" from purchase databases to build the closet. — [Google Patents KR102471490B1](https://patents.google.com/patent/KR102471490B1/en)
- **Looko KR102824768B1** (priority 7 Dec 2023) "Method for correcting clothing image": classify the garment type, extract preliminary feature points, choose fixed and target points, then correct the garment shape. — [Google Patents KR102824768B1](https://patents.google.com/patent/KR102824768B1/en)
- **Looko KR20250087080A** (priority 7 Dec 2023) "Method for extracting clothes size in clothing image": per-type keypoints, compute the "folding amount" per measurement area, correct the measured dimensions. — [Google Patents KR20250087080A](https://patents.google.com/patent/KR20250087080A/en)
- **Looko KR102484545B1** (priority 23 Nov 2021) "Similarity tendency based user recommendation": item feature vectors combined into user feature vectors, then distance-based grouping. — [Google Patents KR102484545B1](https://patents.google.com/patent/KR102484545B1/en)
- Further Looko filings (titles only): KR102344818B1 "Virtual closet building system" (2020), KR102695425B1/KR20230042243A "Coordination combination generation system" (2020), KR20240086245A "Method for extracting features of object in image" (2022), WO2024122724A1 "Size recommendation system based on fashion logging data of user" (2022). — [Google Patents assignee search "룩코"](https://patents.google.com/?assignee=%EB%A3%A9%EC%BD%94)
- **Dahlia Labs US20260099870A1** "Product Recommendation Systems and Methods" (priority 3 Oct 2024; filed 1 Oct 2025; pending; inventors Daniel Joseph Arcand Cary, Brad William Rathke): "An LLM, set of heuristics, or other machine learning system can be used to decompose the user's raw input … and route it to the correct subsystem"; "inputting the at least one modification to an embedding model … an ML model may predict a region of latent search space"; "A vector search can be used to retrieve products … similar to this predicted target product embedding … Structured data and attributes data can be fed into that search system as constraints and filters"; training uses synthetic "pivot text" generated between product pairs. — [Google Patents US20260099870A1](https://patents.google.com/patent/US20260099870A1/en)
- **Serendipity One US12625903B1** (priority 3 Dec 2024; inventors Ning Hu, Zhenyu Lu): identify query intent "through intent recognition models based on Transformer architecture or large language models", do a "scene breakdown", recall a target object plus associated objects, combine them, and generate "multiple multimedia contents … using a pre-trained generative model". The description covers "request a virtual try on … A virtual mannequin … which can be a 3D model or a 2D image". — [Google Patents US12625903B1](https://patents.google.com/patent/US12625903B1/en)
- Assignee searches returned 0 relevant results for Indyx, Iceclip, "AI Style by DNA" and Cladwell (only unrelated inventors named Cladwell). Whering, Flagship AI and combyne searches hit Google's rate-limit page (HTTP 503). — [Google Patents](https://patents.google.com/)

### Inferences
- Acloset's "Beautify" feature (garment image clean-up, Aug 2025) maps closely onto KR102824768B1's keypoint-based garment shape correction (weakly indicated). Its Gmail/purchase-history import maps onto KR102471490B1 (strongly indicated as the same idea).
- Daydream's "edit this product" chat refinements ("like this but in green / longer") are almost certainly the patented pivot-embedding + vector search pipeline (strongly indicated by the patent plus the CEO's description).

### Gaps
- Whering, Flagship AI (Alta) and Combyne assignee searches were rate-limited. I found no patents but cannot rule them out.
- Google's own virtual try-on patents (as opposed to papers) were not searched.

---

## 6. Vendor case studies and third-party confirmations

### Takeaway
Few vendor case studies exist for these apps. The clearest third-party confirmation is that Pixyle (fashion auto-tagging) lists Whering as a customer, which matches Whering's privacy policy. Otherwise the vendor evidence comes from the apps' own vendor lists in §1.

### Cited Findings
- TechCrunch lists "Whering, a wardrobe cataloging app" among Pixyle AI's customers, and CB Insights lists a 2023 Pixyle–Whering partnership. — [TechCrunch on Pixyle](https://techcrunch.com/?p=2454838); [CB Insights Pixyle customers](https://www.cbinsights.com/company/pixyle/customers)
- Whering's privacy policy names "Kaleido AI GmbH. (remove.bg)" and "Pixyle B.V. (pixyle.ai)" as infrastructure processors. — [Whering privacy policy](https://www.whering.co.uk/privacy)
- Cladwell names RevenueCat, the only confirmed subscription-infrastructure vendor in the set. — [Cladwell privacy policy](https://cladwell.com/privacy-policy)

### Inferences
- Background removal: **remove.bg API (Whering) is confirmed**. Acloset, Indyx, Smart Closet, Combyne, Alta and Pureple say they remove backgrounds automatically but name no vendor. Pureple's "watch an ad to remove the background" suggests a metered third-party call (weakly indicated). Stylebook's is probably Apple on-device Vision (weakly indicated). Cladwell's ChatGPT flow produces "clothing-only picture on white or transparent", so in that path ChatGPT's own image tools do the work (confirmed for the ChatGPT surface only).

### Gaps
- Searches (25 Oct 2026) for RevenueCat, Superwall or Adapty case studies naming any of these apps returned none. Neither did searches for Photoroom, Clipdrop or Google Cloud / Firebase case studies.
- No public evidence names Photoroom, Clipdrop or similar for any of these apps.
- No model-provider customer pages (OpenAI, Google, Anthropic) name these apps.

---

## 7. Google Doppl: the research behind its try-on and how the app likely used it

### Takeaway
Doppl (Google Labs) launched 26 Jun 2025 on iOS and Android in the US and shut down on 30 Apr 2026, when Google folded try-on into Search, Shopping and Images. It ran Google Shopping's virtual try-on: a "custom image generation model for fashion" announced at I/O in May 2025. That model descends from Google Research's TryOnDiffusion (CVPR 2023), and TryOnDiffusion's senior author co-wrote the Doppl launch post. Doppl added AI-generated video of the try-on. Google has not named the video model, so Veo is a guess. In Dec 2025 Google's try-on began using Nano Banana (Gemini 2.5 Flash Image) to build a full-body avatar from a selfie. Developers can call the same capability as Vertex AI `virtual-try-on-001`.

### Cited Findings
- Launch post (26 Jun 2025, by Sakshi Rambhia, PM Google Labs, and **Ira Kemelmacher-Shlizerman, Principal Scientist**): "Doppl's virtual try-on makes it fun and easy to see any outfit on a digital, animated version of yourself"; "Doppl also brings your looks to life with AI-generated videos — converting static images into dynamic visuals"; it builds on Shopping's May launch letting users "virtually try billions of clothing items on yourself, just by uploading a photo"; "available on iOS and Android starting today in the U.S." The post names no model. — [Google blog: Doppl](https://blog.google/technology/google-labs/doppl/)
- Google I/O shopping post (20 May 2025): try-on is "powered by a new custom image generation model for fashion, which understands the human body and nuances of clothing"; AI Mode shopping "brings together Gemini capabilities with our Shopping Graph" (50B+ products, refreshed hourly). — [Google blog: AI Mode shopping / try-on](https://blog.google/products/shopping/google-shopping-ai-mode-virtual-try-on-update/)
- TryOnDiffusion: A Tale of Two UNets (Google Research + University of Washington, CVPR 2023) introduces Parallel-UNet, a diffusion architecture with implicit warping via cross-attention, at 1024×1024. Kemelmacher-Shlizerman is a co-author. — [Google Research publication](https://research.google/pubs/tryondiffusion-a-tale-of-two-u-nets/); [arXiv 2306.08276](https://arxiv.org/abs/2306.08276)
- Shutdown: Google Labs posted on X on 23 Mar 2026 ("Thanks for exploring virtual try-on with our experimental app Doppl") that the app would shut down on 30 Apr 2026, with try-on continuing in Google Search/Shopping ("Try it virtually") and image results. labs.google/doppl now returns 404. — [jetstream.blog report](https://jetstream.blog/2026/03/24/google-doppl-shut-down-end-of-april-2026/); [labs.google/doppl](https://labs.google/doppl)
- Dec 2025 update: users can "use a selfie and Nano Banana, Google's Gemini 2.5 Flash Image model … to generate a full-body digital version of themselves for virtual try-ons", choosing their usual size and picking from several generated images. — [TechCrunch, 11 Dec 2025](https://techcrunch.com/2025/12/11/googles-ai-try-on-feature-for-clothes-now-works-with-just-a-selfie/)
- Vertex AI exposes `virtual-try-on-001` (person image + product images in; up to 4 output images; 10 MB max; English prompts; 50 RPM default quota) and the earlier `virtual-try-on-preview-08-04`. — [Vertex AI Virtual Try-On 001](https://docs.cloud.google.com/vertex-ai/generative-ai/docs/models/imagen/virtual-try-on-001); [preview 08-04](https://cloud.google.com/vertex-ai/generative-ai/docs/models/imagen/virtual-try-on-preview-08-04)
- Coverage of the app flow: upload a full-body photo, add outfit images or screenshots from anywhere, get a still try-on and then animated clips. Reviewers noted problems with trousers. — [etcentric](https://www.etcentric.org/google-doppl-lets-you-try-on-outfits-using-generative-video/); [Android Authority](https://www.androidauthority.com/google-doppl-3571667/)

### Inferences
- Doppl architecture: a thin native client (Google Labs app; framework unknown) that uploads the user photo and an outfit image (possibly first extracted by Gemini-based garment detection) to Google's server-side fashion try-on diffusion model, the Shopping try-on model in the TryOnDiffusion lineage, then optionally to a Google video-generation model for animation. Strongly indicated for the try-on model, since the same team and blog lineage tie them together. Weakly indicated that Veo was the video model, because Google never named it.
- For a third-party builder, the practical takeaways are that Google productised this as Vertex AI `virtual-try-on-001`, and that Gemini 2.5 Flash Image is already used for try-on by competitors (Pureple uses Gemini for "Try on Model" and "See on Yourself"; Alta's privacy policy names Vertex AI). Confirmed for those vendor relationships; which exact model endpoint each app calls is not public.

### Gaps
- No official statement of Doppl's client framework, video model or serving stack. Store metadata disappeared with the shutdown.

---

## 8. Synthesis: per-app tech profiles and the typical stack in this category

### Takeaway
The successful apps in this category share a pattern. They use a cross-platform client (Flutter or React Native) unless the company is iOS-first (Stylebook, Daydream and likely Alta use native Swift). Firebase handles auth, analytics and crash reporting nearly everywhere. The backend is TypeScript (NestJS/Express/Next.js) or Go, with MongoDB Atlas or Postgres, on GCP or AWS. The web front end is Next.js on Vercel or Cloudflare. AI is bought, not built: remove.bg-style background removal, OpenAI plus Gemini (and Claude via routers) for tagging, chat and outfit generation, and Gemini or Vertex for try-on. The firms with the most proprietary ML (Acloset, Daydream, Gensmo, Whering) add vector search and fine-tuned or specialist models. Monetization relies on store IAP (RevenueCat where named), metered AI credits, affiliate links and, for Acloset, Pureple and Combyne, ads. The 2025–26 frontier is agentic distribution: Cladwell's ChatGPT app, Acloset's MCP platform, Daydream's Siri/App Intents and "Powered by Daydream" B2B, and Whering's B2B offering.

### Cited Findings
- All facts in the table below are drawn from §§1–7. Key primary sources: [Whering privacy](https://www.whering.co.uk/privacy), [Whering careers](https://whering.co.uk/careers), [Looko Wanted job](https://www.wanted.co.kr/wd/391802), [SNU Looko notice](https://cse.snu.ac.kr/community/notice/18309), [Cladwell privacy](https://cladwell.com/privacy-policy), [Pureple privacy](http://www.pureple.com/Privacy.aspx), [Combyne privacy](https://www.combyne.com/privacy-policy-en), [Smart Closet privacy](https://smartcloset.me/privacy-policy), [Style DNA privacy](https://styledna.ai/privacy-policy/), [Alta privacy](https://www.altadaily.com/privacy), [Gensmo sub-processors](https://gensmo.com/about/legal/sub-processors), [NowSecure Gensmo](https://www.nowsecure.com/?p=30563), [Dahlia Labs patent](https://patents.google.com/patent/US20260099870A1/en), [Doppl blog](https://blog.google/technology/google-labs/doppl/), plus my 25 Oct 2026 inspection of [altadaily.com](https://www.altadaily.com/login), [daydream.ing](https://daydream.ing/), [gensmo.com](https://gensmo.com/), [app.whering.co.uk](https://app.whering.co.uk/login), [app.cladwell.com](https://app.cladwell.com/), [smartcloset.me](https://smartcloset.me/) and [pureple.com](https://pureple.com/login).

### Inferences

**Per-app tech profile table** (C = confirmed, S = strongly indicated, W = weakly indicated, ? = unknown)

| App | Mobile client | Backend / cloud | Data stores | Auth | Analytics / attribution / CRM / crash | Monetization infra | AI / ML vendors and models | Images / CDN | Web surface / public API |
|---|---|---|---|---|---|---|---|---|---|
| **Whering** | Flutter, with native Swift/Kotlin bridges; Patrol tests; Codemagic CI (C) | TypeScript/NestJS (Express) on GCP Cloud Run + Cloud Functions (C); BullMQ/Redis (C, per job post) | Firestore + MongoDB Atlas (C); BigQuery/Dataflow/Pub/Sub data platform (C) | Firebase Auth; Apple/Google/Facebook login (C) | Adjust, Mixpanel, GA, Firebase monitoring/Crashlytics, Braze, Instabug (C) | Store IAP: subscriptions/"Supporter" + AI Credits (C); paywall SDK ? | remove.bg (C); Pixyle auto-tagging (C); in-house CV + recommendation models on Vertex AI / PyTorch/TF (C, per ML job posts); LLM vendor ? | Firebase/GCS (S) | Next.js on Vercel (site + app.whering.co.uk) (C); REST `api.whering.co.uk/app/v1/*` (C); `dionne` marketplace/similar-search service (C exists; S purpose); Chrome extension (C) |
| **Acloset** | React Native (S) | Go (Echo/Gorm) + Python on AWS ECS/EC2/Batch/Lambda + on-prem Kubernetes; GitHub Actions (C) | PostgreSQL, Redis, OpenSearch, ClickHouse, **Qdrant** (vector), DynamoDB (C) | Gmail OAuth import (C); account auth ? | Elastic APM/Kibana/CloudWatch (C); ad SDK present (S: "Contains ads", tracking labels) | Subscriptions (Basic/Premium/Expert) + "Beans" currency + ads (C) | In-house CV patents: garment extraction, image correction, size measurement, recommendation vectors (C as patents; S as shipped); AI try-on/avatar (C feature, vendor ?); MCP agent platform (C, in progress) | S3 + CloudFront `media.acloset.app` (C) | Astro site on S3/CloudFront (C); no public API |
| **Indyx** | ? (white-label second iOS target suggests one multi-target codebase, W) | GCP touch-points (GCS bucket for the site) (W) | ? | ? | Privacy label shows no tracking (C) | Subscriptions + paid stylist services + resale; Apple Wallet passes (C) | AI bg removal, auto-tagging, "editorial flatlay" generation, receipt-email and link ingestion (C features; vendors ?) | ? | Squarespace marketing site; `app.myindyx.com` universal-link host only (C) |
| **Stylebook** | Native iOS Swift/Obj-C (S); iPhone+iPad only | None beyond iCloud (S: iCloud backup/sync, paid-upfront app) | iCloud/CloudKit (S) | none (S) | Minimal analytics only (C label) | Paid upfront $4.99 (C) | Apple Intelligence image generation (C "requires Apple Intelligence"); on-device bg removal via Vision (W) | n/a | Static Apache site (C) |
| **Cladwell** | ? (iPad Split View support; 166 MB) | AWS + Vercel (C); Next.js web app (C); Typesense search (C); `account.cladwell.com` auth API (C) | ? (Typesense index C) | Apple/Google/Facebook/Pinterest sign-in (C) | GA, Firebase Analytics, Crashlytics, Segment, PostHog, OneSignal, Postmark, self-hosted Listmonk, Zendesk (C) | **RevenueCat** + Stripe web checkout + store IAP (C) | OpenAI; OpenRouter routing to Azure and Vertex AI (C); own image-generation service for try-on (C); **ChatGPT app** (C), which implies MCP/Apps SDK (S) | Cloudinary (legacy) (C) | Astro site + Next.js web app on Vercel (C) |
| **Smart Closet** | **React Native** (S: bundle ID `…SmartClosetRN`) | Node/Express GraphQL (Apollo) behind Cloudflare (C web); Google Cloud + AWS (C) | MongoDB (C) | Firebase + Google/Apple/Facebook sign-in (C) | GA (C) | Paid app + backup/sync subscription (C) | One-click bg removal (C feature; vendor ?) | ? | Next.js web closet on Cloudflare (C); GraphQL `graph.smartcloset.me/graphql` (C) |
| **Pureple** | ? | Firebase serverless (C); Cloud Functions (W) | Firestore + Firebase Storage (C) | Firebase Auth (C) | Firebase Analytics + Crashlytics + FCM (C); ads SDK (S) | Subscriptions (weekly/monthly/yearly) + ads (C) | **OpenAI Vision API** item tagging; OpenAI chat + recommendations with weather; **Gemini** try-on (C) | Firebase Storage (C) | React + Ant Design web app on Firebase (C/S) |
| **Combyne** | Native iOS Swift (legacy Obj-C) (S, 2020); Android Kotlin (W) | AWS hosting (C); Docker/Kubernetes/Node/Python/Java (W, StackShare) | MongoDB Atlas (C); Postgres/BigQuery (W) | Facebook login (C) | Firebase/GA (C); Intercom, Mailchimp/Mandrill (C); ad tracking (C label) | Premium IAP, Superlikes, "Fire" currency, sponsored/ad marketplace (C) | AI background removal (C feature, Jun 2025); AI try-on (C feature); vendors ? | ? | Framer site (C) |
| **Alta Daily** | iOS SwiftUI (S); Android ? | TypeScript/Next.js on Vercel (`api.altadaily.com`) (C/S); **Temporal** workflows (S); OpenTelemetry + Grafana Faro (C web) | **Supabase Postgres** (us-east-2 replica) + Upstash/Vercel KV Redis (S) | Google/Apple sign-in (C) | Statsig, TikTok, Meta Pixel + CAPI Gateway, Crisp (C web) | Free app, no IAP listed on iOS (C) | OpenAI (C, ToS); Vertex AI for calendar outfits (C); "TypeSafe" event classifier (C); LLM tier router with Claude and open-source fallbacks + per-category fine-tunes `ft_womens_*` (S); VTON avatars (C feature) | Cloudinary, Cloudflare Images, CloudFront (C) | Next.js web app with login (C); Google Calendar read-only integration (C) |
| **Google Doppl** (closed 30 Apr 2026) | ? (Google Labs app, iOS+Android) | Google infrastructure (C) | — | Google account (C, per coverage) | — | Free (C) | Google Shopping fashion try-on model (TryOnDiffusion lineage) (S); AI video generation, model unnamed (C feature; Veo = W) | Google | Folded into Search/Shopping "Try it virtually" (C) |
| **Daydream** | Native Swift (S: iOS 26 floor, Siri/App Intents, Apple Intelligence image identification); iOS only (W) | Connect-RPC services behind Envoy on Google Cloud (S); Next.js web (C) | Vector search over an embedded multi-retailer catalog (S, patent) | Firebase Auth with custom domain (C) | Amplitude (analytics, replay, experiments), Datadog RUM, Customer.io + Gist, Meta/Google hashed matching (C) | Affiliate commerce (Impact) (C); SaaSquatch referrals (C); "Powered by Daydream" B2B (C) | OpenAI + Gemini "mini models" with a query-understanding router (C, CEO); LLM decomposition + pivot-embedding model + vector search (C, patent) | imgproxy-style "fotomancer" on `cdn.dahlialabs.dev` (S); Contentful (C) | Next.js shopper web app (C); BFF `bff.shopper.api-web.dahlialabs.dev` (C) |
| **Style DNA** | ? | AWS + Google Cloud (C); nginx API behind Google LB (C) | ? | Facebook/Instagram login (C) | Amplitude, AppsFlyer, Smartlook, Firebase (C) | Subscriptions + one-off palette/body-type guides (C) | OpenAI/ChatGPT + Gemini for chat (C); selfie colour analysis model (C feature; vendor ?) | AWS/GCS (C) | WordPress/Elementor site (C) |
| **Gensmo** | ? Android uses ML Kit + LiteRT on device (C) | GCP compute behind Cloudflare (C); Next.js on Cloudflare Workers (C); account/recommend/workflow microservices (C) | MongoDB Atlas (C) | Firebase Auth `auth.gensmo.com` (C) | Sentry, Firebase/GA (C) | Credit packs (C) | "Proprietary try-on model" (C claim, unverified); LLM/Transformer intent recognition + generative presentation (C, patent) | `gimg.iminsp.com` via Cloudflare (C) | Next.js web + share pages `/share/try-on/:id` (C); Gensmo Studio (C) |

**Feature-level architecture: most likely pattern, with evidence**
- *Wardrobe digitisation (photo → clean cut-out → tags).* Client uploads a photo, a server calls a background-removal API (remove.bg at Whering, C) or on-device Vision (Stylebook, W), then a tagging step: a fashion tagging API (Pixyle at Whering, C), a vision LLM (OpenAI Vision at Pureple, C; OpenAI or Vertex via OpenRouter at Cladwell, C), or in-house CV (Acloset patents, Whering ML posts, C). The result is stored in object storage behind a CDN (S3/CloudFront at Acloset, C; Cloudinary or Cloudflare Images at Alta and Cladwell, C).
- *Import from purchases/links.* Gmail OAuth receipt parsing (Acloset, C), email forwarding plus link scraping (Indyx, C), a Chrome extension (Whering, C), a web clipper (Stylebook, C), or ChatGPT reading receipts or links (Cladwell, C).
- *Outfit generation and daily suggestions.* An LLM over a structured wardrobe summary plus weather and calendar context (Pureple and Alta confirmed; Cladwell uses WeatherKit/Open-Meteo, C), increasingly behind a multi-provider router with fallbacks (Alta enum, S; Cladwell's OpenRouter, C; Daydream mini models, C). Durable workflow engines handle long or async generations (Alta Temporal, S; Gensmo "gem-workflow", S; Whering BullMQ, C).
- *Recommendations and shopping.* Embeddings plus a vector DB (Acloset Qdrant, C; Daydream patent, C; Whering "similar" search, S), affiliate catalogs (Daydream Impact, C), marketplace/resale (Whering, Indyx, Acloset, C).
- *Virtual try-on.* Bought from Google (Pureple uses Gemini, C; Doppl/Google Shopping use Google's fashion model, C; Vertex VTO API available, C) or claimed proprietary (Gensmo, C claim). Metered with credits (Whering, Acloset, Gensmo, C).
- *Agentic surfaces.* ChatGPT app over MCP/Apps SDK (Cladwell, C app; S protocol), an MCP closet platform (Acloset, C plan), Siri/App Intents (Daydream, C), B2B "Powered by" (Daydream, C), B2B offering (Whering, C).

**Cross-app pattern: the typical stack of a successful app in this category (2025–26)**
1. Client: Flutter (Whering) or React Native (Acloset, Smart Closet) for cross-platform teams. Native SwiftUI for iOS-first, design-led or Apple-Intelligence-heavy apps (Daydream, Stylebook, likely Alta).
2. Identity and telemetry: Firebase Auth + Crashlytics/Analytics (Whering, Pureple, Smart Closet, Gensmo, Daydream web, Cladwell, Combyne, Style DNA), plus one product-analytics tool (Mixpanel, Amplitude, Segment/PostHog or Statsig) and one attribution SDK (Adjust or AppsFlyer).
3. Backend: TypeScript (NestJS, Express or Next.js API routes) is most common. Go appears at the largest-scale player (Acloset). Python handles ML/data.
4. Data: MongoDB Atlas (Whering, Combyne, Smart Closet, Gensmo) or Postgres (Acloset, Alta via Supabase), plus Redis, plus a search or vector layer (Typesense at Cladwell, OpenSearch + Qdrant at Acloset, vector search at Daydream).
5. Cloud: GCP (Whering, Gensmo, Daydream) and AWS (Acloset, Combyne, Cladwell) are split roughly evenly. Web runs on Vercel (Whering, Alta, Cladwell) or Cloudflare (Gensmo, Smart Closet).
6. AI: multi-vendor LLMs (OpenAI everywhere it is named; Gemini at Pureple, Style DNA and Daydream; Claude in Alta's router; Azure/Vertex via OpenRouter at Cladwell), a third-party background-removal API, and Google models for try-on. Proprietary ML is concentrated in tagging, recommendation and search rather than in generation.
7. CRM: Braze (Whering), Customer.io (Daydream), OneSignal + Postmark (Cladwell), Mailchimp (Combyne), Intercom/Zendesk/Crisp for support.
8. Monetization: store subscriptions (RevenueCat is the only paywall vendor named), metered AI credits or virtual currencies, affiliate commerce, and ads for the mass-market or free-tier apps (Acloset, Pureple, Combyne).

### Gaps
- Mobile client framework is still unknown for Indyx, Cladwell, Pureple, Style DNA, Gensmo, Doppl and Alta Android. Resolving it needs Exodus/AppBrain library lists (e.g. presence of `io.flutter`, `com.facebook.react`, `expo`) from a network that can reach them.
- No paywall SDK confirmed beyond Cladwell's RevenueCat. Store-level evidence cannot tell RevenueCat, Adapty, Superwall and in-house apart.
- The vendor behind try-on is unknown for Acloset, Combyne, Alta and Whering (Whering sells credits, but its generation vendor is unnamed).
- Raw evidence (store HTML, Data safety pages, JS bundles, privacy texts, patent pages, AASA files) is saved under `/tmp/claude-0/-home-user-outfit-planner-app/bd7b84c5-3686-5200-b171-3a2b128c387a/scratchpad/techstack/raw/` (scripts in `.../techstack/scripts/`) for re-checking.
