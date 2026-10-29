package com.kintukka.hanger.engine

/**
 * A small, fixed style guide for engine tests, so assertions don't depend on the researched wording.
 * Install with [use] and the shipped guide comes back afterwards.
 */
object TestGuide {
    val knowledge: StyleKnowledge = StyleKnowledge.parse(
        """
        {
          "version": 1,
          "updated": "2026-10-29",
          "sources": [{"id": "s1", "title": "Test source", "publisher": "Tests", "url": "https://example.com", "date": "2026-10"}],
          "colors": {
            "pairs": [
              {"a": "NAVY", "b": "CAMEL", "score": 2, "why": "Navy and camel are a classic pairing", "src": ["s1"]},
              {"a": "NAVY", "b": "WHITE", "score": 2, "why": "Navy and white never miss"},
              {"a": "NAVY", "b": "BLACK", "score": -1, "why": "Navy against black can read as a mistake"},
              {"a": "RED", "b": "HOT_PINK", "score": -2, "why": "Red and hot pink fight for attention"},
              {"a": "PINK", "b": "RED", "score": 1, "why": "Pink and red make a playful tonal pair"}
            ],
            "palettes": [{"name": "Coastal", "colors": ["NAVY", "WHITE", "DENIM"], "why": "Crisp and easy"}],
            "profiles": {"NAVY": {"role": "neutral", "summary": "The softer black", "bestWith": ["CAMEL", "WHITE"], "avoidWith": ["BLACK"]}},
            "rules": [{"id": "c1", "title": "Anchor with a neutral", "text": "One neutral calms bright colours."}]
          },
          "formulas": [
            {"id": "f1", "name": "White tee, jeans, white sneakers",
             "slots": [{"category": "TOP", "types": ["T-shirt"], "colors": ["WHITE"]}, {"category": "BOTTOM", "types": ["Jeans"]},
                       {"category": "SHOES", "types": ["Sneakers"], "colors": ["WHITE"]}],
             "occasions": ["CASUAL"], "why": "The definitive easy uniform"},
            {"id": "f2", "name": "Blazer, trousers, loafers",
             "slots": [{"category": "LAYER", "types": ["Blazer"]}, {"category": "TOP", "types": ["Shirt"]},
                       {"category": "BOTTOM", "types": ["Trousers"]}, {"category": "SHOES", "types": ["Loafers"]},
                       {"category": "ACCESSORY", "types": ["Belt"], "optional": true}],
             "occasions": ["WORK", "SMART"], "why": "Sharp without trying"}
          ],
          "pairings": [
            {"a": "Loafers", "b": "Chinos", "score": 2, "why": "Loafers sharpen chinos"},
            {"a": "Running shoes", "b": "Trousers", "score": -2, "why": "Running shoes undercut tailored trousers"},
            {"a": "Hoodie", "b": "Blazer", "score": -1, "why": "A hoodie under a blazer is a risky high-low"}
          ],
          "patterns": [
            {"a": "STRIPE", "b": "FLORAL", "score": 1, "why": "Stripes steady a floral"},
            {"a": "FLORAL", "b": "ANIMAL", "score": -2, "why": "Floral and animal print shout over each other"}
          ],
          "styleRules": [{"id": "r1", "topic": "proportion", "title": "Balance volume", "text": "Pair a wide leg with a fitted top."}],
          "occasions": {
            "WORK": {"summary": "Polished and practical", "formality": [3, 4], "good": ["Blazer"], "avoid": ["Flip-flops", "Running shorts"], "tips": []}
          },
          "dressCodes": [{"name": "Business casual", "occasion": "WORK", "summary": "Tailoring without the tie", "examples": ["Blazer"], "avoid": ["Hoodie"]}],
          "weather": [
            {"id": "w1", "when": {"maxFeelsC": 0}, "title": "Freezing", "advice": "Wrap up: coat, hat and gloves.", "types": ["Coat"], "avoidTypes": ["Espadrilles"]},
            {"id": "w2", "when": {"rain": true}, "title": "Rain", "advice": "Waterproof layer and boots.", "types": ["Raincoat"], "avoidTypes": ["Loafers"]},
            {"id": "w3", "when": {"minFeelsC": 0, "maxFeelsC": 12}, "title": "Cold", "advice": "A warm layer and a coat.", "types": ["Coat"]}
          ],
          "fabrics": [
            {"material": "Wool", "match": ["wool"], "seasons": ["WINTER"], "warmth": 4, "tip": "Warm and breathable", "care": "Air between wears"},
            {"material": "Merino", "match": ["merino"], "seasons": [], "warmth": 3, "tip": "Fine, soft and odour-resistant"}
          ],
          "trends": {
            "updated": "2026-10-29",
            "trends": [
              {"id": "t1", "name": "Burgundy everything", "kind": "colour", "season": "AW26", "from": "2026-09-01", "to": "2027-02-28",
               "summary": "Deep red is the colour of the season", "colors": ["BURGUNDY"], "match": "any"},
              {"id": "t2", "name": "Brown and blue", "kind": "pairing", "season": "AW26", "from": "2026-09-01", "to": "2027-02-28",
               "summary": "Chocolate with powder blue", "colors": ["BROWN", "LIGHT_BLUE"], "match": "all"},
              {"id": "t4", "name": "Dark florals", "kind": "pattern", "season": "AW26", "from": "2026-09-01", "to": "2027-02-28",
               "summary": "Moody florals on black", "colors": ["BLACK"], "patterns": ["FLORAL"], "match": "all"},
              {"id": "t3", "name": "Butter yellow", "kind": "colour", "season": "SS27", "from": "2027-03-01", "to": "2027-08-31",
               "summary": "Soft yellow returns", "colors": ["YELLOW"], "match": "any"}
            ],
            "fading": [{"name": "Skinny everything", "summary": "Straight and wide legs took over"}],
            "classics": [{"name": "White shirt", "summary": "Works with everything", "types": ["Shirt"], "colors": ["WHITE"]}],
            "colourOfTheYear": [{"name": "Cloud Dancer", "by": "Pantone", "year": 2026, "nearest": "WHITE", "summary": "A soft white"}]
          },
          "wardrobe": {
            "tips": [{"id": "a", "topic": "capsule", "text": "Tip A"}, {"id": "b", "topic": "care", "text": "Tip B"}],
            "checks": [
              {"id": "k1", "title": "All neutrals", "metric": "neutralShare", "op": ">=", "value": 0.8, "minItems": 4,
               "message": "{value} of your closet is neutral", "action": "Add one accent colour"},
              {"id": "k2", "title": "No rain coat", "metric": "waterproofOuterwear", "op": "==", "value": 0, "minItems": 4,
               "message": "Nothing waterproof to wear"},
              {"id": "k3", "title": "Big closet", "metric": "items", "op": ">", "value": 500, "message": "Huge"}
            ],
            "ratios": {"summary": "About three tops per bottom", "perBottom": {"TOP": 3}},
            "essentials": [{"name": "Rain-ready coat", "category": "OUTERWEAR", "types": ["Raincoat", "Trench coat"], "why": "Rain happens"}]
          }
        }
        """.trimIndent(),
    )

    /** A guide with no content, for "without the guide" baselines. */
    val empty = StyleKnowledge(version = 1, updated = "")

    /** Run [block] with the test guide installed. */
    fun <T> use(block: () -> T): T = with(knowledge, block)

    fun <T> with(guide: StyleKnowledge, block: () -> T): T {
        val previous = StyleKnowledge.override
        StyleKnowledge.override = guide
        try {
            return block()
        } finally {
            StyleKnowledge.override = previous
        }
    }
}
