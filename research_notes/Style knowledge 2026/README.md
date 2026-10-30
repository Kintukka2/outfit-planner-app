# Hanger style knowledge base (October 2026)

Hanger's style advice is offline. It comes from this research, which is bundled into the app as
`engine/src/main/resources/hanger/style-knowledge.json`. That file replaces the optional AI stylist the app used to have.
Six research tracks ran in parallel on 29 October 2026. Each produced:
- a JSON file mapped to the app's own vocabulary (30 colours, 8 categories, 93 garment types, 9 patterns,
  8 occasions);
- a notes file that explains its reasoning, where sources disagree, and the full source list.

| Track | Data file | Notes | What's in it |
|---|---|---|---|
| Colour | `colours.json` | `colours.md` | 203 rated colour pairs, 48 palettes, a profile for each of the 30 colours, 20 colour rules (44 sources) |
| Outfit formulas | `formulas.json` | `formulas.md` | 125 formulas with slots by category, type and colour, plus their occasions, seasons and formality (53 sources) |
| Garment pairings | `pairings.json` | `pairings.md` | 236 type-to-type pairings, all 45 pattern pairs, 72 styling rules across 10 topics (76 sources) |
| Occasions, weather, fabrics | `occasions.json` | `occasions.md` | 8 occasion guides, 22 dress codes, 19 weather rules covering −10 °C to 30 °C and above plus rain, snow, wind and UV, 32 fabrics (50 sources) |
| Trends | `trends.json` | `trends.md` | 69 dated trends (46 live in AW26, 23 for SS27), 9 fading, 18 classics, 3 colours of the year (48 sources) |
| Wardrobe building | `wardrobe.json` | `wardrobe.md` | 85 tips, 42 closet checks with tone and priority, closet ratios, 19 essentials (31 sources) |

After de-duplicating shared URLs there are 301 sources. Every entry the app shows can be traced to its source ids,
and the Style guide screen lists the sources.

## How the app uses it

- **Scoring and notes:**
  - Each colour pair, garment pair and pattern pair in an outfit nudges its score and can supply the note under a
    suggestion.
  - A soft cap near the top keeps great outfits distinct instead of all scoring 100%.
  - The dress-code "avoid" lists and the day's weather rules penalise pieces and say why ("Heavy rain: swap the suede
    loafers").
- **Suggestions:** formulas that fit the occasion and season are filled from the closet and added to the candidates,
  and outfits that follow a formula rank slightly higher.
- **Trends:**
  - Trends are live only between their `from` and `to` dates, so they retire on their own.
  - A piece counts towards a trend only if it fits every dimension the trend names; dark florals means a floral piece
    in black.
- **Closet checks:**
  - These run on metrics computed on the phone.
  - Wear-based checks stay silent until wears are logged and the closet is a month old.
  - The screen shows the most useful fixes plus at most one positive.

## Updating it

1. Edit the track's JSON in this folder.
2. Run `python3 tools/build_knowledge.py --check` and fix anything it reports. The script drops any colour, type,
   category, pattern, occasion or metric the app doesn't know, and lists what it dropped.
3. Run `python3 tools/build_knowledge.py` to rebuild the bundled file.
4. Run `./gradlew :engine:test`. `BundledGuideTest` checks:
   - every reference resolves;
   - every formula is wearable;
   - every temperature has a weather rule;
   - every check uses a metric the engine computes.

The trends need a refresh each season; the current set runs to the end of August 2027. Pantone announces its 2027
colour in early December 2026.

## Known limits

- **Search budget:** all six tracks shared one web-search budget, and it ran out near the end. Later work used pages
  the researchers had already found.
- **Unreadable sites:**
  - Several major outlets (Vogue, GQ, Esquire, Elle, Harper's Bazaar, The Cut) refuse automated readers, so they're
    cited rarely or only through search summaries.
  - Each notes file lists the sources it saw only as summaries.
- **What the vocabulary can't express:**
  - Fabrics and finishes in formulas and trends (suede, velvet, leather, lace).
  - Fits (slim, oversized).
  - Matching colours across pieces (suits, co-ords).
  - Those formulas and trends are approximated or kept as guide text only; `formulas.md` and `trends.md` list each
    case.
- **Missing pairs mean "no data":** unlisted colour or garment pairs fall back to the engine's colour-wheel rules.
- **Judgement calls:** some closet-check thresholds are the researchers' own judgement, not sourced figures (marked
  in `wardrobe.md`), and are worth tuning with use.
