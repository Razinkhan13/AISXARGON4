# GOOGLE STITCH — MASTER FRONT-END DESIGN & CUSTOMER PREVIEW PROMPT PACK
**Application:** AIS Enterprise Systems — Capital Relief Cockpit & White-Labeled Client Portal (`ais_capital_terminal.html`)  
**Founder & Final Signatory:** Rizum Khan (Managing Partner, AIS — Army IBA Specialist)  
**Statutory Identity:** NBR BIN `004982103-0101` (Service Code `S099.10` — 5% VAT) • HQ: 15 Mayakanon, Basaboo, Khilgaon, Dhaka-1214  
**Verified Settlement:** Trust Bank PLC (Jalalabad Cantonment Branch) A/C `00110466000494` | bKash `01715219176` | Nagad `01708761298`  

---

## HOW TO USE THIS FILE IN GOOGLE STITCH (3 SIMPLE STEPS)

1. **Upload Your Code or Screenshot:** Open [Google Stitch](https://www.google.com/search?q=https%3A%2F%2Fstitch.withgoogle.com%2F) and upload your `ais_capital_terminal.html` file (or a screenshot of the current view along with the HTML code).
2. **Paste the Matching Prompt Below:**
   - Use **Prompt 1 (Master Full-System Design Prompt)** to upgrade the overall visual polish of the entire application while keeping all 7 tabs, calculators, and JavaScript logic 100% intact.
   - Use **Prompt 2 (Real Estate & Zero-Online Builder Customer Preview Prompt)** to generate or refine the **3-Second Customer-Understanding Preview** for Real Estate Chairmen, Managing Directors, and local JVA builders (`?demo=sheltech` or `?demo=zero_online`).
   - Use **Prompt 3 (E-Commerce & F-Commerce Customer Preview Prompt)** to generate or refine the **3-Second Customer-Understanding Preview** for D2C and Facebook commerce founders (`?demo=gorurghash`, `?demo=fabrilife`, or `?demo=ecom_fcom`).
3. **Export Clean Tailwind HTML/CSS:** Copy the generated Tailwind CSS / HTML refinements back into your `index.html` on [Vercel](https://www.google.com/search?q=https%3A%2F%2Fvercel.com%2Fdocs) without removing any existing `id="..."` attributes.

---

## VISUAL REFERENCE BENCHMARKS (RELATED EXAMPLES FOR GOOGLE STITCH)

| Reference Product | Visual Element to Emulate | Why It Works for AIS Customers in Bangladesh |
| :--- | :--- | :--- |
| **[Linear](https://www.google.com/search?q=https%3A%2F%2Flinear.app%2F) (Dark Mode)** | Elevated obsidian surfaces (`#06090F` to `#0B101B`), crisp `1px solid rgba(255,255,255,0.08)` hairline borders, and subtle radial top-glows | Replaces noisy neon borders with calm, executive boardroom authority that looks like a private banking terminal on mobile and iPad. |
| **[Stripe Radar \& Billing](https://www.google.com/search?q=https%3A%2F%2Fstripe.com%2Fradar)** | High-contrast risk badges (`79% RTO Risk` vs. `94% Verified VIP`), tabular-nums financial counters, and clean 3-step telemetry timelines | Lets an E-Commerce owner or Real Estate Chairman understand their exact monthly Taka bleed and 38-second automated solution at a single glance. |
| **[Vercel Geist Design System](https://www.google.com/search?q=https%3A%2F%2Fvercel.com%2Fgeist%2Fintroduction)** | Segmented pill navigation, high-legibility `Inter` + `Plus Jakarta Sans` headings, and `JetBrains Mono` financial figures | Prevents layout shift (`CLS = 0`) when toggling between Corporate English and Formal Business Bangla (`Noto Sans Bengali`). |
| **[Apple Wallet](https://www.google.com/search?q=https%3A%2F%2Fwww.apple.com%2Fwallet%2F) / Private Banking Pass** | Branded VIP Client Header (`Prepared Exclusively for [Client Name]`), verified green trust badges, and 1-tap WhatsApp action bar | Makes a 55-year-old Dhaka Chairman or local builder feel respected and proud when opening their private WhatsApp demo link. |

---

## PROMPT 1: MASTER FULL-APP FRONT-END DESIGN PROMPT (COPY & PASTE INTO GOOGLE STITCH)

```text
ROLE & OBJECTIVE:
You are a Principal Staff Product Designer at Google Stitch, Linear, and Stripe. I am uploading the complete production code for "AIS Enterprise Systems — Capital Relief Cockpit & White-Labeled Client Portal" (`ais_capital_terminal.html`).

Your task is to elevate the front-end visual design, spacing, visual hierarchy, and mobile/tablet boardroom responsiveness to a world-class Institutional FinTech & Private Banking standard—while strictly preserving 100% of the existing HTML IDs, JavaScript event listeners, bilingual Bangla/English strings, and multi-tenant isolation rules.

1. VISUAL DESIGN SYSTEM & COLOR TOKENS (STITCH OBSIDIAN v4.2):
- Primary Canvas Background: Deep Obsidian (`#06090F`) with a subtle ambient radial top spotlight (`radial-gradient(ellipse 80% 40% at 50% -10%, rgba(56, 189, 248, 0.12), transparent)`).
- Card Surfaces: Elevated Dark Slate (`linear-gradient(180deg, rgba(17, 24, 39, 0.90) 0%, rgba(11, 16, 27, 0.96) 100%)`) with `backdrop-filter: blur(16px)`, `border-radius: 16px`, and crisp `1px solid rgba(255, 255, 255, 0.08)` hairline borders (no heavy thick neon borders).
- Semantic Accent Palette:
  * Primary Executive Action / Brand Accent: Sky Cyan (`#38BDF8` to `#0EA5E9`)
  * Verified Trust / Profit Recovery / WhatsApp CTA: Emerald (`#10B981` / `#34D399`)
  * Zero-Online Builder / Delay Warning: Amber Gold (`#F59E0B` / `#FBBF24`)
  * Financial Bleed / Bank OD Drag / High RTO Fraud Alert: Rose Crimson (`#F43F5E` / `#FB7185`)
- Typography Scale:
  * Display & Section Headings: 'Plus Jakarta Sans', sans-serif (700/800 weight, tight tracking `-0.025em`)
  * Body & Bilingual Bangla UI: 'Inter', 'Noto Sans Bengali', sans-serif (clean 1.5 line-height for effortless reading by senior executives)
  * Financial Figures, BDT Lakh/Crore Counters, Latencies & BIN/Bank Numbers: 'JetBrains Mono', monospace with `font-variant-numeric: tabular-nums` so numbers never jitter when sliders move.

2. DUAL-WORKSPACE ARCHITECTURE (ADMIN COCKPIT vs. ISOLATED CLIENT WHATSAPP VIEW):
Keep the two distinct visual experiences controlled by `body.stitch-client-mode`:
- Mode A — Founder Command (`👑 Founder Command`): Shows the top 90-Day ৳1 Crore Net Scoreboard, the 7-tab segmented navigation bar (`📱 1-Tap WhatsApp`, `🏗️ 10s Cold Audit`, `🛡️ Approve & Lock`, `🧾 Mushak-6.3`, `🏢 Client Sandbox`, `📄 4-in-1 PDF Vault`, `⚡ Webhook & RLS`), all 24 target company presets, and the 7-file Open-Source Code Vault.
- Mode B — Isolated Client WhatsApp View (`🔒 Client WhatsApp View`, triggered automatically via `?demo=slug&scope=demo,calc`):
  * Completely hides all internal founder admin bars, competitor names, outreach scripts, and internal revenue scoreboards (`display: none !important`).
  * Displays the VIP Private Client Header (`#stitchClientVipHeader`) reading: "🔒 VERIFIED PRIVATE CLIENT PORTAL • Prepared Exclusively for [Client Name]" with 3 big, zero-jargon segmented buttons:
    1. `✨ 1. Live Interactive Demo`
    2. `📊 2. Savings Calculator`
    3. `🧾 3. Official Tax Invoice`
    plus a direct green `💬 WhatsApp Rizum Khan` button.
  * Industry-Aware Visual Isolation:
    - When `body.stitch-client-re` is active (Real Estate Developers & Zero-Online Builders), show ONLY Real Estate calculators (`#rePortfolioCockpitCard`, `#reSingleSiteAuditCard`, `#zeroOnlineShowroomBuilderBox`) and hide all E-Commerce cards.
    - When `body.stitch-client-ecom` is active (E-Commerce & F-Commerce Brands), show ONLY E-Commerce calculators (`#ecomRtoAuditCard`, `#ecomExperientialLabBox`) and hide all Real Estate cards.

3. ZERO-DAMAGE DOM CONTRACT (DO NOT RENAME OR REMOVE THESE IDs):
Preserve all existing DOM IDs and classes so the embedded JavaScript engine works without errors, including:
- Mode & Header IDs: `#stitchTopModeSwitcherBar`, `#enterFounderModeBtn`, `#enterClientPreviewModeBtn`, `#toggleCompactGuidesBtn`, `#stitchClientVipHeader`, `#clientPortalHeaderTitle`, `#headerLockedRevenue`, `#langSwitchBtn`, `#quickAddDepositBtn`
- Outreach & Pitch IDs: `#demoBaseUrlInput`, `#scopeCheckDemo`, `#scopeCheckCalc`, `#scopeCheckInvoice`, `#generatedClientDemoUrl`, `#protoCompanySelect`, `#protoSirName`, `#protoCurrencySelect`, `#dualPitchOutput`, `#waChatFlowBox`, `#leadsContainer`
- Calculator IDs: `#ecomRtoAuditCard`, `#ecomAuditBrand`, `#ecomDailyOrders`, `#ecomAov`, `#ecomRtoPct`, `#ecomCostPerRto`, `#ecomNightComments`, `#outEcomMonthlyRtoBurn`, `#outEcomLostInboxSales`, `#outEcomTotalRecovered`, `#rePortfolioCockpitCard`, `#cockpitSqft`, `#cockpitPrice`, `#cockpitOdRate`, `#cockpitDelay`, `#reSingleSiteAuditCard`
- Client Sandbox & Demo Lab IDs: `#sandboxDevSelect`, `#zeroOnlineShowroomBuilderBox`, `#ecomExperientialLabBox`, `#sandboxBrandedHero`, `#sbBuyerName`, `#sbBuyerPhone`, `#sbBuyerCurr`, `#sbTriggerJourneyBtn`, `#sbJourneySteps`
- Invoice, PDF Vault & Gateway IDs: `#invClientName`, `#invPackageSelect`, `#invBaseAmt`, `#vaultExportWrapper`, `#vercelSupabaseSetupBox`, `#txQueueContainer`
```

---

## PROMPT 2: CUSTOMER-FIRST PREVIEW PROMPT FOR REAL ESTATE & ZERO-ONLINE BUILDERS

Use this prompt in [Google Stitch](https://www.google.com/search?q=https%3A%2F%2Fstitch.withgoogle.com%2F) when you want to refine or generate the **Real Estate Customer Preview (`?demo=sheltech` or `?demo=zero_online`)** so any Chairman, Managing Director, or local landowner-builder understands the entire value proposition in under 10 seconds:

```text
DESIGN TASK: REAL ESTATE EXECUTIVE & ZERO-ONLINE BUILDER "10-SECOND UNDERSTANDING" CUSTOMER PREVIEW
Target User Persona: A 50-to-65-year-old Bangladeshi Real Estate Chairman, Managing Director (e.g., Sheltech, bti, Navana, Dom-Inno, Anwar Landmark), or a local Banasree/Mirpur/Uttara builder who has zero website and zero tech background, viewing this link on WhatsApp on their phone or iPad.

VISUAL LAYOUT & CUSTOMER COMPREHENSION FLOW (3 CLEAR VISUAL CARDS):

1. TOP VIP TRUST BANNER ("No-Blame" Institutional Respect):
- Style: Like an Apple Wallet Private Banking Pass or Stripe VIP Portal.
- Content: Company badge + "🔒 VERIFIED PRIVATE CLIENT PORTAL • Prepared Exclusively for [Builder/Developer Name]".
- Sub-headline in plain, respectful business language: "Zero IT Staff Required. We stop 14%–17% bank interest bleed and bring verified overseas NRB & local flat buyers directly to your WhatsApp in 38 seconds."

2. INTERACTIVE VISUAL STORY CARD A — "THE 38-SECOND LIVE BUYER TEST" (Side-by-Side Comparison):
- Left Visual ("Old Way — Why Buyers Are Lost"): A subtle rose-tinted card showing:
  * "2:14 AM London/New York NRB Inquiry or Gate Signboard Walk-In"
  * "72-Hour Manual Sales Desk Delay OR ৳3.5 Lakh Local Broker (Dalal) Commission Loss"
  * "Result: 78% of cash-ready NRB buyers move to another project."
- Right Visual ("AIS New Way — Live 38-Second Concierge"): A glowing emerald/sky-cyan interactive WhatsApp phone preview card where the Chairman types their own phone number and taps one big button: `[🚀 Run Live 38-Second Qualification Test]`.
- Live Visual Telemetry Steps (with animated green checkmarks):
  * `0.2s` — Buyer scans Gate Signboard QR or clicks Probashi Ad at 2:14 AM.
  * `0.8s` — Instant reply in polite Bangla/English with verified Floor Plan PDF & price converted to USD ($), GBP (£), or BDT (৳).
  * `1.4s` — 36-Month Bank EMI / 75% NRB Mortgage calculated automatically.
  * `2.1s` — 48-Hour Unit Hold locked & Friday 4:00 PM site visit booked directly on the Managing Director's phone.

3. INTERACTIVE VISUAL STORY CARD B — "YOUR MONTHLY SAVINGS MIRROR & 100% GUARANTEE":
- Clean, touch-friendly sliders (large thumb targets for mobile/iPad) showing:
  * Unsold Luxury Square Footage (`sq ft`) & Bank Overdraft Rate (`14%–17%`)
  * Big Red Callout: "Monthly Dead Bank Interest Burn: ৳2.41 Crore / mo"
  * Big Emerald Callout: "90-Day Cash Unlocked via AIS Sandbox: ৳31.01 Crore"
- Bottom Guarantee Seal: Official NBR Mushak-6.3 Tax Invoice badge (`BIN: 004982103-0101`) + Trust Bank PLC (`A/C 00110466000494`) + bold Risk-Reversal Box:
  "🛡️ 100% Money-Back Guarantee: If we do not cut buyer response time to under 4 minutes and generate 5× our fee in verified buyer pipeline within 60 days (or 15+ verified buyer visits in 30 days for local builders), we refund 100% of your deposit."
```

---

## PROMPT 3: CUSTOMER-FIRST PREVIEW PROMPT FOR E-COMMERCE & F-COMMERCE FOUNDERS

Use this prompt in [Google Stitch](https://www.google.com/search?q=https%3A%2F%2Fstitch.withgoogle.com%2F) when you want to refine or generate the **E-Commerce & F-Commerce Customer Preview (`?demo=gorurghash`, `?demo=fabrilife`, `?demo=shajgoj`, `?demo=khaasfood`, or `?demo=ecom_fcom`)**:

```text
DESIGN TASK: E-COMMERCE & F-COMMERCE "3-SECOND COD FRAUD SHIELD & AUTO-CHECKOUT" CUSTOMER PREVIEW
Target User Persona: A busy Bangladeshi D2C Brand Owner (e.g., Gorur Ghash, Fabrilife, Sailor, Shajgoj, Khaas Food) or a Zero-Website Facebook/Instagram Page Seller dealing with 300+ daily "Price Please" comments and 18%–25% outside-Dhaka Cash-on-Delivery (COD) doorstep returns (RTO).

VISUAL LAYOUT & CUSTOMER COMPREHENSION FLOW (3 CLEAR VISUAL CARDS):

1. TOP VIP BRAND HEADER:
- Displays: "🔒 VERIFIED D2C PROFIT PROTECTION PORTAL • Prepared Exclusively for [Brand Name]"
- Subtitle: "Stop burning ৳155–৳380 per returned Steadfast/Pathao/RedX parcel and convert 2:00 AM 'Price Koto?' comments in 2.4 seconds—with zero extra moderators."

2. INTERACTIVE VISUAL CARD A — "LIVE 3-SECOND COURIER RADAR & WHATSAPP CHECKOUT SIMULATOR":
- Top Scenario Selector Pills (1-Tap Customer Preview Examples):
  * `[✅ Example 1: Genuine Repeat VIP Buyer (94% Success -> Auto-Dispatch)]`
  * `[🚨 Example 2: Serial Prank / High-RTO Buyer (79% Return -> Auto-Hold & ৳100 bKash Gate)]`
  * `[🌙 Example 3: 2:14 AM "Price Koto?" FB Comment (-> 2.4s Auto-Inbox Checkout)]`
- Left Panel (Incoming Order / Comment Card): Shows Shopper Name, District (e.g., Sylhet / Cumilla / Rajshahi), Mobile Number, Product SKU (with Size/Shade/Perishable tag), and COD Amount (`৳1,850`).
- Right Panel (Instant 3-Courier Radar + WhatsApp Bot Preview):
  * 3 mini telemetry cards side-by-side in `JetBrains Mono`:
    - `STEADFAST: 2 Deliv / 9 Ret` (Red badge)
    - `PATHAO: 1 Deliv / 5 Ret` (Red badge)
    - `REDX: 1 Deliv / 1 Ret` (Amber badge)
  * High-contrast Decision Banner:
    `🛑 AUTO-HELD BEFORE DISPATCH (SAVED ৳155 TWO-WAY COURIER FEE)`
  * Live WhatsApp Bubble Preview showing the exact Bangla/Banglish message sent to the shopper in 0.8 seconds: confirming chest size/shade and requesting a `৳100 bKash/Nagad delivery advance` (or offering `৳50 instant discount` for full bKash prepayment).

3. INTERACTIVE VISUAL CARD B — "MONTHLY RTO & MIDNIGHT INBOX PROFIT CALCULATOR":
- 5 smooth touch sliders:
  1. Daily COD Parcels Dispatched (`30` to `4,000/day`)
  2. Average Order Value (`৳500` to `৳6,000`)
  3. Outside-Dhaka Return Rate (`RTO %`: `5%` to `40%`)
  4. Two-Way Courier + Packaging Loss per Return (`৳110` to `৳650`)
  5. Unanswered Midnight Inbox Inquiries/Day (`20` to `1,000/day`)
- 3 Big Financial Outcome Boxes:
  * Rose Box: `Monthly Courier & Packaging Burn (Dead Cash): ৳11.72 Lakh / mo`
  * Amber Box: `Lost Midnight Inbox Sales (After 11 PM): ৳11.54 Lakh / mo`
  * Emerald Box: `Net Monthly Profit Recovered by AIS COD Shield: ৳18.57 Lakh / mo`
- Risk-Free Pilot CTA Bar:
  * "30-Day Pilot: ৳18,000 (F-Commerce) to ৳25,000–৳30,000 (D2C) • 100% Refund Guarantee if outside-Dhaka RTO does not drop by at least 40% in 30 days."
```

---

## QUICK CHECKLIST BEFORE SENDING ANY PREVIEW LINK TO A CUSTOMER

1. **Test the Exact Customer View First:** Click **`🔒 Client WhatsApp View (Hide All Admin Info)`** on the top bar of your app (or open `https://your-vercel-domain.vercel.app/?demo=sheltech&scope=demo,calc#sandbox` for Real Estate, or `https://your-vercel-domain.vercel.app/?demo=gorurghash&scope=demo,calc#sandbox` for E-Commerce).
2. **Verify Zero Admin Leakage:** Confirm that only the **VIP Client Header**, **Live Interactive Demo**, and **Savings Calculator** are visible—and that Real Estate clients only see Real Estate tools while E-Commerce clients only see E-Commerce tools.
3. **Send via 1-Tap WhatsApp:** Switch back to **`👑 Founder Command`**, pick your target in **Tab 1**, and tap **`💬 1-Tap WhatsApp + Demo Link`**—your custom pitch and isolated customer preview link are automatically combined and ready to send.
