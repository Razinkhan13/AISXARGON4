# AIS Enterprise Systems — Obsidian Cockpit v4.2 (Android Native)

Native Android implementation of the **AIS x GEMINI 4 ARGON Capital Relief Cockpit & White-Labeled Client Onboarding Infrastructure**, rewritten from React to modern **Kotlin + Jetpack Compose**.

## Architecture & Framework

- **Target Platform**: Android (minSdk 24, compileSdk 35)
- **UI Framework**: Jetpack Compose with Material Design 3 (M3)
- **Language**: Kotlin 2.1.0
- **Build System**: Gradle 8.8.0 (Kotlin DSL - `.gradle.kts`) with Version Catalog (`libs.versions.toml`)
- **Design System**: Elevated Obsidian & Neon Noir Palette (`#06090F`, `#0B101B`, `#111827`, `#38BDF8`, `#10B981`, `#FF4B89`)
- **State Architecture**: MVVM with Kotlin Coroutines and StateFlow

## Ported Core Features & Business Logic

1. **High-Ticket CRM & Outreach Engine (`OutreachScreen`)**:
   - Real Estate & E-Commerce enterprise target pipeline (Dom-Inno, Sheltech, bti, Artisan Outfitters, Gorur Ghash).
   - 1-Tap WhatsApp pitch launcher embedding client-specific deep links (`?demo=slug#sandbox`) and REDMA / RTO statutory pain points.
   - Status transitions (`PROSPECT` → `PITCH_SENT` → `DEMO_COMPLETED` → `FUNDS_LOCKED` → `MUSHAK_ISSUED`).
   - Live pipeline deal metrics and filterable chips.

2. **Forensic Capital Bleed & RTO Recovery Calculator (`AuditCalculatorScreen`)**:
   - **Real Estate Capital Bleed**: Calculates frozen capital in stalled units, statutory 15% refund interest penalty under Bangladesh REDMA Section 14, and monthly compounding bank OD facility bleed.
   - **E-Commerce RTO Recovery**: Computes annual return-to-origin courier hemorrhage, trapped GMV in transit, and margin recovery from 3D virtual garment fitting.
   - One-tap executive forensic memo copy to clipboard.
   - Prominent **5x Risk-Reversal Guarantee Seal**.

3. **Locked Revenue & Transaction Clearing Ledger (`RevenueLockScreen`)**:
   - Real-time locked revenue progress against the 90-day ৳1.00 Crore target.
   - Transaction clearing ledger for Bank RTGS, BEFTN wires, and bKash Merchant payments.
   - Immediate Treasury verification and reconciliation actions.

4. **Bangladesh NBR Statutory Form Mushak-6.3 VAT Tax Invoicing (`MushakScreen`)**:
   - National Board of Revenue (NBR) Form Mushak-6.3 compliant tax invoice builder.
   - Automatic 5% statutory VAT calculation, net invoice totaling in BDT (৳), and serial assignment (e.g., `2026-2027-DHK-1001`).
   - Authentic printable white certificate card with official NBR header, stamp, and settlement status toggle.

5. **38-Second Experiential Client Showroom Sandbox (`SandboxScreen`)**:
   - White-labeled interactive client showroom simulator for Dom-Inno, Sheltech, Artisan Outfitters, and Gorur Ghash.
   - Real Estate: Interactive 3D floor plan simulator, lakefront unit selector, and 24h VIP reservation hold lock.
   - E-Commerce: 3D AR garment calibration and virtual panjabi fitting simulator.

6. **Legal Document Vault & Boardroom Pitch Deck (`DocumentVaultScreen`)**:
   - **4-in-1 Executive Legal Suite**: Master Service Agreement (MSA), Mushak-6.3, Service Level Agreement (SLA) with 99.9% uptime & 60-day 5x guarantee, and Scope of Work (SOW).
   - **Dual-Mode Presentation**: One-tap toggle between "Boardroom Executive / Plain English" and "Statutory Legal Text".
   - **Boardroom Pitch Mode**: Fullscreen 4-slide C-level boardroom presentation mode.

7. **Escrow Payment Gateway & Account Routing (`GatewayScreen`)**:
   - Corporate routing for City Bank Citygem Priority, BRAC Bank Corporate, and bKash Merchant accounts.
   - One-tap clipboard copy for instant wire coordination.

## Android Project Directory Layout

```
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/aiscockpit/
│       │   ├── MainActivity.kt
│       │   ├── model/
│       │   │   ├── Lead.kt
│       │   │   ├── AuditModel.kt
│       │   │   ├── MushakModel.kt
│       │   │   ├── TransactionModel.kt
│       │   │   └── VaultModel.kt
│       │   ├── data/
│       │   │   └── CockpitRepository.kt
│       │   ├── ui/
│       │   │   ├── CockpitViewModel.kt
│       │   │   ├── theme/
│       │   │   │   ├── Color.kt
│       │   │   │   ├── Type.kt
│       │   │   │   └── Theme.kt
│       │   │   └── screens/
│       │   │       ├── OutreachScreen.kt
│       │   │       ├── AuditCalculatorScreen.kt
│       │   │       ├── RevenueLockScreen.kt
│       │   │       ├── MushakScreen.kt
│       │   │       ├── SandboxScreen.kt
│       │   │       ├── DocumentVaultScreen.kt
│       │   │       └── GatewayScreen.kt
│       └── res/
│           ├── values/
│           │   ├── strings.xml
│           │   ├── colors.xml
│           │   └── themes.xml
│           ├── drawable/
│           │   ├── ic_launcher_background.xml
│           │   └── ic_launcher_foreground.xml
│           └── mipmap-*/
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── metadata.json
```
