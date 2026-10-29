#!/usr/bin/env python3
"""Build the app's bundled style guide from the research data.

Reads the six research files in `research_notes/Style knowledge 2026/`, checks every colour, type, category,
pattern, occasion and season against the app's vocabulary, normalises and de-duplicates entries, makes source ids
unique, and writes `engine/src/main/resources/hanger/style-knowledge.json`.

    python3 tools/build_knowledge.py            # build
    python3 tools/build_knowledge.py --check    # report problems without writing

Anything that doesn't fit the vocabulary is dropped and reported, so the app never sees a token it can't handle.
"""
import json
import re
import sys
from datetime import date
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
RESEARCH = ROOT / "research_notes" / "Style knowledge 2026"
OUT = ROOT / "engine" / "src" / "main" / "resources" / "hanger" / "style-knowledge.json"
ENGINE = ROOT / "engine" / "src" / "main" / "kotlin" / "com" / "kintukka" / "hanger" / "engine"

problems: list[str] = []


def warn(msg: str) -> None:
    problems.append(msg)


def enum_names(file: str, enum: str) -> list[str]:
    text = (ENGINE / file).read_text()
    body = re.search(r"enum class " + enum + r"\b[^{]*\{(.*?)\n\}", text, re.S).group(1)
    body = body.split(";")[0]
    return re.findall(r"^\s*([A-Z_]+)\s*[\(,]", body, re.M)


COLORS = enum_names("Model.kt", "FashionColor")
CATEGORIES = enum_names("Model.kt", "Category")
PATTERNS = enum_names("Model.kt", "Pattern")
OCCASIONS = enum_names("Model.kt", "Occasion")
SEASONS = ["SPRING", "SUMMER", "AUTUMN", "WINTER"]
TYPES: dict[str, tuple[str, str]] = {}  # lowercase -> (canonical name, category)
for name, cat in re.findall(r'p\("([^"]+)",\s*Category\.([A-Z_]+)', (ENGINE / "Catalog.kt").read_text()):
    TYPES[name.lower()] = (name, cat)
assert len(COLORS) == 30 and len(TYPES) > 90, (len(COLORS), len(TYPES))

METRICS = {
    "neutralShare", "accentColours", "boldPatternShare", "items", "tops", "bottoms", "onePieces", "layers",
    "outerwear", "shoes", "bags", "accessories", "topsPerBottom", "waterproofOuterwear", "warmOuterwear",
    "smartShoes", "formalPieces", "casualShare", "wornShare30", "wornShare90", "neverWornShare", "avgCostPerWear",
    "summerPieces", "winterPieces", "duplicates", "laundryShare", "wishlist",
}
OPS = {"<", "<=", ">", ">=", "=="}


def load(name: str) -> dict:
    path = RESEARCH / f"{name}.json"
    if not path.exists():
        warn(f"{name}.json is missing")
        return {}
    return json.loads(path.read_text())


# --- Sources: one global list with unique ids, shared when two tracks cite the same URL -------------------------

sources: list[dict] = []
by_url: dict[str, str] = {}


def import_sources(track: str, raw: list[dict]) -> dict[str, str]:
    mapping: dict[str, str] = {}
    for s in raw:
        old = str(s.get("id", "")).strip()
        url = (s.get("url") or "").strip()
        if url and url in by_url:
            mapping[old] = by_url[url]
            continue
        new = f"{track}-{old}"
        entry = {
            "id": new,
            "title": (s.get("title") or "").strip(),
            "publisher": (s.get("publisher") or "").strip(),
            "url": url,
            "date": str(s.get("date") or "").strip(),
        }
        if not entry["title"]:
            warn(f"{track}: source {old} has no title")
            continue
        sources.append(entry)
        mapping[old] = new
        if url:
            by_url[url] = new
    return mapping


def remap(src, mapping: dict[str, str], where: str) -> list[str]:
    out = []
    for s in src or []:
        if str(s) in mapping:
            if mapping[str(s)] not in out:
                out.append(mapping[str(s)])
        else:
            warn(f"{where}: unknown source id {s}")
    return out


# --- Token helpers ----------------------------------------------------------------------------------------------

def color(c, where):
    c = str(c).strip().upper().replace(" ", "_").replace("-", "_")
    if c in COLORS:
        return c
    warn(f"{where}: unknown colour {c}")
    return None


def colors(lst, where):
    return [c for c in (color(x, where) for x in (lst or [])) if c]


def type_name(t, where, category=None):
    hit = TYPES.get(str(t).strip().lower())
    if not hit:
        warn(f"{where}: unknown type {t!r}")
        return None
    if category and hit[1] != category:
        warn(f"{where}: type {t!r} is {hit[1]}, not {category}")
        return None
    return hit[0]


def type_names(lst, where, category=None):
    out = []
    for t in lst or []:
        n = type_name(t, where, category)
        if n and n not in out:
            out.append(n)
    return out


def enum(v, allowed, where, kind):
    v = str(v).strip().upper()
    if v in allowed:
        return v
    warn(f"{where}: unknown {kind} {v}")
    return None


def enums(lst, allowed, where, kind):
    return [x for x in (enum(v, allowed, where, kind) for v in (lst or [])) if x]


def text(v, where, limit=240):
    v = re.sub(r"\s+", " ", str(v or "")).strip()
    if not v:
        warn(f"{where}: empty text")
    elif len(v) > limit:
        warn(f"{where}: text longer than {limit} chars")
    return v


def score(v, where):
    try:
        s = int(v)
    except (TypeError, ValueError):
        warn(f"{where}: bad score {v}")
        return None
    if s not in (-2, -1, 1, 2):
        warn(f"{where}: score {s} out of range")
        return None
    return s


# --- Tracks -----------------------------------------------------------------------------------------------------

def build_colours(raw):
    m = import_sources("col", raw.get("sources", []))
    pairs, seen = [], {}
    for i, p in enumerate(raw.get("pairs", [])):
        where = f"colours.pairs[{i}]"
        a, b, s = color(p.get("a"), where), color(p.get("b"), where), score(p.get("score"), where)
        if not a or not b or s is None or a == b:
            continue
        a, b = sorted((a, b), key=COLORS.index)
        if (a, b) in seen:
            warn(f"{where}: duplicate pair {a}/{b}")
            continue
        seen[(a, b)] = True
        pairs.append({"a": a, "b": b, "score": s, "why": text(p.get("why"), where, 140), "src": remap(p.get("src"), m, where)})
    palettes = []
    for i, p in enumerate(raw.get("palettes", [])):
        where = f"colours.palettes[{i}]"
        cs = colors(p.get("colors"), where)
        if len(set(cs)) < 2:
            continue
        palettes.append({"name": text(p.get("name"), where, 60), "colors": cs, "why": text(p.get("why"), where, 200),
                         "src": remap(p.get("src"), m, where)})
    profiles = {}
    for c, p in (raw.get("profiles") or {}).items():
        where = f"colours.profiles.{c}"
        key = color(c, where)
        if not key:
            continue
        profiles[key] = {
            "role": p.get("role", "accent"),
            "summary": text(p.get("summary"), where, 220),
            "bestWith": [x for x in colors(p.get("bestWith"), where) if x != key],
            "avoidWith": [x for x in colors(p.get("avoidWith"), where) if x != key],
            "seasons": enums(p.get("seasons"), SEASONS, where, "season"),
        }
    missing = [c for c in COLORS if c not in profiles]
    if missing:
        warn(f"colours.profiles missing {missing}")
    rules = [{"id": f"col-{r.get('id', i)}", "topic": "colour", "title": text(r.get("title"), f"colours.rules[{i}]", 60),
              "text": text(r.get("text"), f"colours.rules[{i}]", 260), "src": remap(r.get("src"), m, f"colours.rules[{i}]")}
             for i, r in enumerate(raw.get("rules", []))]
    return {"pairs": pairs, "palettes": palettes, "profiles": profiles, "rules": rules}


def build_formulas(raw):
    m = import_sources("for", raw.get("sources", []))
    out, signatures = [], set()
    for i, f in enumerate(raw.get("formulas", [])):
        where = f"formulas[{i}] {f.get('id')}"
        slots, ok = [], True
        for j, s in enumerate(f.get("slots", [])):
            cat = enum(s.get("category"), CATEGORIES, f"{where}.slots[{j}]", "category")
            if not cat:
                ok = False
                break
            types = type_names(s.get("types"), f"{where}.slots[{j}]", cat)
            if s.get("types") and not types:
                ok = False
                break
            slot = {"category": cat, "types": types, "colors": colors(s.get("colors"), f"{where}.slots[{j}]")}
            if s.get("optional"):
                slot["optional"] = True
            slots.append(slot)
        if not ok:
            warn(f"{where}: dropped (bad slot)")
            continue
        req = [s["category"] for s in slots if not s.get("optional")]
        core = ("TOP" in req and "BOTTOM" in req) or "ONE_PIECE" in req
        if not core or "SHOES" not in req:
            warn(f"{where}: dropped (needs a core and shoes)")
            continue
        sig = json.dumps(sorted(json.dumps(s, sort_keys=True) for s in slots))
        if sig in signatures:
            warn(f"{where}: duplicate formula dropped")
            continue
        signatures.add(sig)
        formality = [int(x) for x in (f.get("formality") or []) if str(x).isdigit()]
        out.append({
            "id": str(f.get("id") or f"f{i:03}"),
            "name": text(f.get("name"), where, 70),
            "slots": slots,
            "occasions": enums(f.get("occasions"), OCCASIONS, where, "occasion"),
            "seasons": enums(f.get("seasons"), SEASONS, where, "season"),
            "formality": formality,
            "why": text(f.get("why"), where, 200),
            "src": remap(f.get("src"), m, where),
        })
    return out


def build_pairings(raw):
    m = import_sources("pai", raw.get("sources", []))
    pairs, seen = [], set()
    for i, p in enumerate(raw.get("pairings", [])):
        where = f"pairings[{i}]"
        a, b, s = type_name(p.get("a"), where), type_name(p.get("b"), where), score(p.get("score"), where)
        if not a or not b or s is None or a == b:
            continue
        key = tuple(sorted((a.lower(), b.lower())))
        if key in seen:
            warn(f"{where}: duplicate pairing {a}/{b}")
            continue
        seen.add(key)
        pairs.append({"a": a, "b": b, "score": s, "why": text(p.get("why"), where, 140), "src": remap(p.get("src"), m, where)})
    patterns, seen = [], set()
    for i, p in enumerate(raw.get("patterns", [])):
        where = f"patterns[{i}]"
        a, b = enum(p.get("a"), PATTERNS, where, "pattern"), enum(p.get("b"), PATTERNS, where, "pattern")
        s = score(p.get("score"), where)
        if not a or not b or s is None:
            continue
        key = tuple(sorted((a, b), key=PATTERNS.index))
        if key in seen:
            continue
        seen.add(key)
        patterns.append({"a": key[0], "b": key[1], "score": s, "why": text(p.get("why"), where, 140),
                         "src": remap(p.get("src"), m, where)})
    rules = []
    for i, r in enumerate(raw.get("rules", [])):
        where = f"pairings.rules[{i}]"
        rules.append({"id": f"pai-{r.get('id', i)}", "topic": str(r.get("topic") or "style"),
                      "title": text(r.get("title"), where, 60), "text": text(r.get("text"), where, 260),
                      "src": remap(r.get("src"), m, where)})
    return pairs, patterns, rules


def build_occasions(raw):
    m = import_sources("occ", raw.get("sources", []))
    occasions = {}
    for k, o in (raw.get("occasions") or {}).items():
        where = f"occasions.{k}"
        key = enum(k, OCCASIONS, where, "occasion")
        if not key:
            continue
        occasions[key] = {
            "summary": text(o.get("summary"), where, 260),
            "formality": [int(x) for x in o.get("formality", [])],
            "good": type_names(o.get("good"), where + ".good"),
            "avoid": type_names(o.get("avoid"), where + ".avoid"),
            "tips": [text(t, where + ".tips", 200) for t in o.get("tips", [])],
            "src": remap(o.get("src"), m, where),
        }
    missing = [o for o in OCCASIONS if o not in occasions]
    if missing:
        warn(f"occasions missing {missing}")
    codes = []
    for i, d in enumerate(raw.get("dressCodes", [])):
        where = f"dressCodes[{i}]"
        occ = enum(d.get("occasion"), OCCASIONS, where, "occasion")
        if not occ:
            continue
        codes.append({"name": text(d.get("name"), where, 50), "occasion": occ, "summary": text(d.get("summary"), where, 260),
                      "examples": type_names(d.get("examples"), where + ".examples"),
                      "avoid": type_names(d.get("avoid"), where + ".avoid"), "src": remap(d.get("src"), m, where)})
    weather = []
    for i, w in enumerate(raw.get("weather", [])):
        where = f"weather[{i}]"
        cond = {k: v for k, v in (w.get("when") or {}).items() if v not in (None, False)}
        allowed = {"minFeelsC", "maxFeelsC", "rain", "snow", "windKphMin", "uvMin", "minHighC"}
        for k in list(cond):
            if k not in allowed:
                warn(f"{where}: unsupported condition {k}")
                cond.pop(k)
        weather.append({"id": str(w.get("id") or f"w{i:02}"), "when": cond, "title": text(w.get("title"), where, 40),
                        "advice": text(w.get("advice"), where, 200), "types": type_names(w.get("types"), where + ".types"),
                        "avoidTypes": type_names(w.get("avoidTypes"), where + ".avoidTypes"),
                        "src": remap(w.get("src"), m, where)})
    fabrics = []
    for i, f in enumerate(raw.get("fabrics", [])):
        where = f"fabrics[{i}]"
        match = [str(x).strip().lower() for x in f.get("match", []) if str(x).strip()]
        if not match:
            warn(f"{where}: no match keywords")
            continue
        fabrics.append({"material": text(f.get("material"), where, 40), "match": match,
                        "seasons": enums(f.get("seasons"), SEASONS, where, "season"),
                        "warmth": int(f.get("warmth") or 3), "tip": text(f.get("tip"), where, 180),
                        "care": text(f.get("care"), where, 140) if f.get("care") else "",
                        "src": remap(f.get("src"), m, where)})
    return occasions, codes, weather, fabrics


def build_trends(raw):
    m = import_sources("tre", raw.get("sources", []))
    trends = []
    for i, t in enumerate(raw.get("trends", [])):
        where = f"trends[{i}] {t.get('id')}"
        try:
            start, end = date.fromisoformat(t["from"]), date.fromisoformat(t["to"])
        except (KeyError, ValueError):
            warn(f"{where}: dropped (bad dates)")
            continue
        if end < start:
            warn(f"{where}: dropped (ends before it starts)")
            continue
        trends.append({
            "id": str(t.get("id") or f"t{i:02}"), "name": text(t.get("name"), where, 60),
            "kind": str(t.get("kind") or "styling"), "season": str(t.get("season") or ""),
            "from": start.isoformat(), "to": end.isoformat(), "status": str(t.get("status") or "peak"),
            "summary": text(t.get("summary"), where, 220), "howToWear": text(t.get("howToWear"), where, 220),
            "colors": colors(t.get("colors"), where), "types": type_names(t.get("types"), where),
            "patterns": enums(t.get("patterns"), PATTERNS, where, "pattern"),
            "match": "any" if t.get("match") == "any" else "all", "src": remap(t.get("src"), m, where),
        })

    def notes(key):
        return [{"name": text(n.get("name"), f"{key}[{i}]", 60), "summary": text(n.get("summary"), f"{key}[{i}]", 220),
                 "colors": colors(n.get("colors"), f"{key}[{i}]"), "types": type_names(n.get("types"), f"{key}[{i}]"),
                 "src": remap(n.get("src"), m, f"{key}[{i}]")} for i, n in enumerate(raw.get(key, []))]

    coty = []
    for i, c in enumerate(raw.get("colourOfTheYear", [])):
        where = f"colourOfTheYear[{i}]"
        coty.append({"name": text(c.get("name"), where, 60), "by": str(c.get("by") or ""), "year": int(c.get("year") or 0),
                     "nearest": color(c["nearest"], where) if c.get("nearest") else None,
                     "summary": text(c.get("summary"), where, 220), "src": remap(c.get("src"), m, where)})
    return {"updated": str(raw.get("updated") or ""), "trends": trends, "fading": notes("fading"),
            "classics": notes("classics"), "colourOfTheYear": coty}


def build_wardrobe(raw):
    m = import_sources("war", raw.get("sources", []))
    tips = [{"id": str(t.get("id") or f"tip{i:02}"), "topic": str(t.get("topic") or "capsule"),
             "text": text(t.get("text"), f"tips[{i}]", 220), "src": remap(t.get("src"), m, f"tips[{i}]")}
            for i, t in enumerate(raw.get("tips", []))]
    checks = []
    for i, c in enumerate(raw.get("checks", [])):
        where = f"checks[{i}] {c.get('id')}"
        if c.get("metric") not in METRICS or c.get("op") not in OPS:
            warn(f"{where}: dropped (metric {c.get('metric')} / op {c.get('op')})")
            continue
        checks.append({"id": str(c.get("id")), "title": text(c.get("title"), where, 50), "metric": c["metric"], "op": c["op"],
                       "value": float(c.get("value")), "minItems": int(c.get("minItems") or 0),
                       "message": text(c.get("message"), where, 180), "action": text(c.get("action"), where, 120) if c.get("action") else "",
                       "src": remap(c.get("src"), m, where)})
    ratios = None
    if raw.get("ratios"):
        r = raw["ratios"]
        per = {}
        for k, v in (r.get("perBottom") or {}).items():
            key = enum(k, CATEGORIES, "ratios.perBottom", "category")
            if key:
                per[key] = float(v)
        ratios = {"summary": text(r.get("summary"), "ratios", 260), "perBottom": per, "src": remap(r.get("src"), m, "ratios")}
    essentials = []
    for i, e in enumerate(raw.get("essentials", [])):
        where = f"essentials[{i}]"
        cat = enum(e.get("category"), CATEGORIES, where, "category")
        if not cat:
            continue
        essentials.append({"name": text(e.get("name"), where, 50), "category": cat,
                           "types": type_names(e.get("types"), where, cat), "why": text(e.get("why"), where, 180),
                           "src": remap(e.get("src"), m, where)})
    return {"tips": tips, "checks": checks, "ratios": ratios, "essentials": essentials}


def main() -> int:
    colours = build_colours(load("colours"))
    formulas = build_formulas(load("formulas"))
    pairings, patterns, style_rules = build_pairings(load("pairings"))
    occasions, codes, weather, fabrics = build_occasions(load("occasions"))
    trends = build_trends(load("trends"))
    wardrobe = build_wardrobe(load("wardrobe"))
    knowledge = {
        "version": 1,
        "updated": trends.get("updated") or date.today().isoformat(),
        "sources": sources,
        "colors": colours,
        "formulas": formulas,
        "pairings": pairings,
        "patterns": patterns,
        "styleRules": style_rules,
        "occasions": occasions,
        "dressCodes": codes,
        "weather": weather,
        "fabrics": fabrics,
        "trends": trends,
        "wardrobe": wardrobe,
    }
    counts = {
        "sources": len(sources), "colour pairs": len(colours["pairs"]), "palettes": len(colours["palettes"]),
        "colour profiles": len(colours["profiles"]), "colour rules": len(colours["rules"]), "formulas": len(formulas),
        "type pairings": len(pairings), "pattern pairs": len(patterns), "style rules": len(style_rules),
        "occasions": len(occasions), "dress codes": len(codes), "weather rules": len(weather), "fabrics": len(fabrics),
        "trends": len(trends["trends"]), "fading": len(trends["fading"]), "classics": len(trends["classics"]),
        "tips": len(wardrobe["tips"]), "checks": len(wardrobe["checks"]), "essentials": len(wardrobe["essentials"]),
    }
    for k, v in counts.items():
        print(f"{k:>16}: {v}")
    if problems:
        print(f"\n{len(problems)} problem(s):")
        for p in problems:
            print("  -", p)
    if "--check" not in sys.argv:
        OUT.parent.mkdir(parents=True, exist_ok=True)
        OUT.write_text(json.dumps(knowledge, ensure_ascii=False, separators=(",", ":")) + "\n")
        print(f"\nWrote {OUT.relative_to(ROOT)} ({OUT.stat().st_size // 1024} KB)")
    return 0


if __name__ == "__main__":
    sys.exit(main())
