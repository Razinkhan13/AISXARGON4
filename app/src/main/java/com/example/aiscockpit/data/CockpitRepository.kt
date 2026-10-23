package com.example.aiscockpit.data

import com.example.aiscockpit.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class CockpitRepository {

    private val _leads = MutableStateFlow<List<PipelineLead>>(getInitialLeads())
    val leads: StateFlow<List<PipelineLead>> = _leads.asStateFlow()

    private val _realEstateAudit = MutableStateFlow(RealEstateAuditInput())
    val realEstateAudit: StateFlow<RealEstateAuditInput> = _realEstateAudit.asStateFlow()

    private val _ecomAudit = MutableStateFlow(EcomRtoAuditInput())
    val ecomAudit: StateFlow<EcomRtoAuditInput> = _ecomAudit.asStateFlow()

    private val _mushakInvoices = MutableStateFlow<List<MushakInvoice>>(getInitialInvoices())
    val mushakInvoices: StateFlow<List<MushakInvoice>> = _mushakInvoices.asStateFlow()

    private val _deposits = MutableStateFlow<List<LockDeposit>>(getInitialDeposits())
    val deposits: StateFlow<List<LockDeposit>> = _deposits.asStateFlow()

    fun addLead(lead: PipelineLead) {
        _leads.value = listOf(lead) + _leads.value
    }

    fun updateLeadStatus(leadId: String, newStatus: LeadStatus) {
        _leads.value = _leads.value.map {
            if (it.id == leadId) it.copy(status = newStatus) else it
        }
    }

    fun updateRealEstateAudit(input: RealEstateAuditInput) {
        _realEstateAudit.value = input
    }

    fun updateEcomAudit(input: EcomRtoAuditInput) {
        _ecomAudit.value = input
    }

    fun addMushakInvoice(invoice: MushakInvoice) {
        _mushakInvoices.value = listOf(invoice) + _mushakInvoices.value
    }

    fun updateMushakStatus(invoiceId: String, newStatus: MushakStatus) {
        _mushakInvoices.value = _mushakInvoices.value.map {
            if (it.id == invoiceId) it.copy(status = newStatus) else it
        }
    }

    fun addDeposit(deposit: LockDeposit) {
        _deposits.value = listOf(deposit) + _deposits.value
    }

    fun updateDepositStatus(depositId: String, newStatus: DepositStatus) {
        _deposits.value = _deposits.value.map {
            if (it.id == depositId) it.copy(status = newStatus) else it
        }
    }

    fun getVaultDocuments(): List<VaultDocument> {
        return listOf(
            VaultDocument(
                docType = VaultDocType.MSA,
                title = "Master Service Agreement (MSA)",
                executiveSummary = "Direct, white-labeled client onboarding terms. Secures 50% upfront retainer, governs proprietary deployment of AIS Argon Cockpit engines, isolates brand telemetry, and protects IP rights.",
                statutoryLegalText = "This Master Service Agreement (\"Agreement\") is entered into between AIS Enterprise Systems and the Client. Pursuant to the Bangladesh Contract Act 1872 and Information & Communication Technology Act 2006, AIS grants an enterprise non-exclusive deployment license. Client covenants to remit all milestone retainers strictly into designated corporate banking escrow channels.",
                keyGuarantees = listOf(
                    "Zero Vendor Lock-in with Full Code Access",
                    "Bank-Grade AES-256 Tenant Data Isolation",
                    "Statutory REDMA Compliance Safeguards",
                    "Dedicated Senior Engineering Squad Deployment"
                )
            ),
            VaultDocument(
                docType = VaultDocType.MUSHAK_6_3,
                title = "Statutory Mushak-6.3 VAT Tax Settlement",
                executiveSummary = "Official National Board of Revenue (NBR) Form Mushak-6.3 settlement certificate. Imposes statutory 5% VAT rate, generating instant NBR-compliant tax deductible documentation for corporate accounts.",
                statutoryLegalText = "Under Value Added Tax and Supplementary Duty Act 2012, Section 51 and Rule 40(1), Form Mushak-6.3 constitutes the primary legal proof of taxable service supply. Retained withholding tax certificates (Mushak-6.6) must be countersigned upon wire clearance.",
                keyGuarantees = listOf(
                    "100% NBR Audit Ready with Verified Serial",
                    "Instant Corporate Expense Tax Shield",
                    "Automated VAT Withholding Reconciliation"
                )
            ),
            VaultDocument(
                docType = VaultDocType.SLA,
                title = "Service Level Agreement & 5x Risk Reversal",
                executiveSummary = "Ironclad guarantee: If client implementation does not unlock at least 5x the engagement fee in identified capital relief or verified conversions within 60 days, AIS refunds 100% of the retainer immediately with zero penalty.",
                statutoryLegalText = "AIS Enterprise Systems covenants 99.9% operational availability for all white-labeled client booking cockpits. Under Section 8.4 (Risk-Reversal Warranty), failure to demonstrate measurable liquidity recovery creates immediate statutory refund recourse.",
                keyGuarantees = listOf(
                    "60-Day Unconditional 5x Risk-Reversal Warranty",
                    "99.9% High-Availability Uptime SLA",
                    "1-Hour Critical Incident Response Window",
                    "No-Questions-Asked Retainer Refund Mechanism"
                )
            ),
            VaultDocument(
                docType = VaultDocType.SOW,
                title = "Scope of Work (SOW) & Deliverables Matrix",
                executiveSummary = "Full technical scope: 38-second experiential client showroom, interactive floor plan / 3D product fitting, 1-tap WhatsApp booking pipeline, and real-time bank reconciliation terminal.",
                statutoryLegalText = "Schedule A defines the phased implementation milestones. Phase 1: Rapid Telemetry Ingestion (Days 1-7). Phase 2: Live Showroom Deployment (Days 8-14). Phase 3: Sales Squad Enablement & Retainer Lock (Days 15-30).",
                keyGuarantees = listOf(
                    "38-Second Rapid Conversion Flow Architecture",
                    "Native Mobile WhatsApp Deep Linking",
                    "Customized Corporate Branding & Colors"
                )
            )
        )
    }

    fun getPitchSlides(): List<PitchSlide> = listOf(
        PitchSlide(
            slideNumber = 1,
            tag = "BOARDROOM PITCH • SLIDE 1",
            title = "The Silent Liquidity Hemorrhage",
            subtitle = "Capital is frozen in stalled units & crippling RTO return cycles",
            keyBulletPoints = listOf(
                "Real estate developers average ৳3.18Cr+ in frozen inventory and 13.5% bank OD interest bleed.",
                "E-Commerce merchants hemorrhage 22%+ GMV into courier reverse fees and damaged stock.",
                "Traditional sales brochures and slow web portals take days to respond, losing 84% of high-intent buyers."
            ),
            callToAction = "Next: The 38-Second Solution →"
        ),
        PitchSlide(
            slideNumber = 2,
            tag = "BOARDROOM PITCH • SLIDE 2",
            title = "The 38-Second Showroom Breakthrough",
            subtitle = "From a single phone photo to an interactive VIP mobile booking terminal",
            keyBulletPoints = listOf(
                "Zero tech barriers: snap a site signboard or floor plan, and the AI engine generates a live showroom.",
                "Interactive unit reservation & 3D garment fitting right inside mobile browsers.",
                "Pre-configured 1-Tap WhatsApp outreach embeds customized client demo links."
            ),
            callToAction = "Next: The 5x Guarantee →"
        ),
        PitchSlide(
            slideNumber = 3,
            tag = "BOARDROOM PITCH • SLIDE 3",
            title = "The 5x Zero-Risk Reversal Guarantee",
            subtitle = "We bear 100% of the financial risk so your board can move today",
            keyBulletPoints = listOf(
                "If the deployment does not identify or unlock at least 5x your fee in 60 days, we issue an immediate full refund.",
                "Backed by Bangladesh Bank corporate wire escrow & statutory Mushak-6.3 tax deductible receipts.",
                "Statutory REDMA Section 14 compliance isolates developer refund risk."
            ),
            callToAction = "Next: Fast-Track Retainer Lock →"
        ),
        PitchSlide(
            slideNumber = 4,
            tag = "BOARDROOM PITCH • SLIDE 4",
            title = "Fast-Track Retainer Lock & Launch",
            subtitle = "Activate your private showroom sandbox in 72 hours",
            keyBulletPoints = listOf(
                "Select Tier-2 Enterprise Relief Sandbox (৳6,00,000 Signature Retainer).",
                "Instant issuance of Form Mushak-6.3 VAT tax invoice with verified serial.",
                "Wire directly via City Bank Citygem Priority or bKash Merchant Gateway."
            ),
            callToAction = "Lock Wire & Issue Mushak-6.3"
        )
    )

    private fun getInitialLeads(): List<PipelineLead> = listOf(
        PipelineLead(
            id = UUID.randomUUID().toString(),
            slug = "dom_inno",
            companyName = "Dom-Inno Developers Ltd.",
            category = LeadCategory.REAL_ESTATE,
            contactPerson = "Engr. Abdus Salam",
            phone = "+8801711000001",
            packageName = "Tier-2 Single-Asset Relief Sandbox",
            status = LeadStatus.LOCKED_WIRE,
            dealValueBDT = 1200000L,
            customHook = "REDMA 2010 Section 14 statutory 15% refund interest shock on 2 cancelled Banani units creates ৳65L exposure.",
            demoDeepLink = "https://ais-cockpit.local/?demo=dom_inno#sandbox"
        ),
        PipelineLead(
            id = UUID.randomUUID().toString(),
            slug = "sheltech",
            companyName = "Sheltech (Pvt.) Ltd.",
            category = LeadCategory.REAL_ESTATE,
            contactPerson = "Tanvir Ahmed",
            phone = "+8801712000002",
            packageName = "Tier-1 Enterprise Portfolio Cockpit",
            status = LeadStatus.DEMO_COMPLETED,
            dealValueBDT = 2400000L,
            customHook = "3 Gulshan penthouses stalled over 180 days with bank OD interest compounding at 13.5% p.a.",
            demoDeepLink = "https://ais-cockpit.local/?demo=sheltech#sandbox"
        ),
        PipelineLead(
            id = UUID.randomUUID().toString(),
            slug = "bti",
            companyName = "Building Technology & Ideas (bti)",
            category = LeadCategory.REAL_ESTATE,
            contactPerson = "FR Khan",
            phone = "+8801713000003",
            packageName = "Tier-2 Single-Asset Relief Sandbox",
            status = LeadStatus.PITCH_SENT,
            dealValueBDT = 1200000L,
            customHook = "Badda waterfront project facing 4 buyer inquiry stalls; interactive 38s showroom unlocks instant buyer deposits.",
            demoDeepLink = "https://ais-cockpit.local/?demo=bti#sandbox"
        ),
        PipelineLead(
            id = UUID.randomUUID().toString(),
            slug = "artisan_outfitters",
            companyName = "Artisan Outfitters (Retail BD Ltd.)",
            category = LeadCategory.ECOM,
            contactPerson = "Zeeshan Khan",
            phone = "+8801714000004",
            packageName = "E-Com RTO Forensic Recovery Lab",
            status = LeadStatus.MUSHAK_ISSUED,
            dealValueBDT = 850000L,
            customHook = "22% RTO return rate hemorrhaging ৳3.2L/mo in courier fees + packaging; 3D sizing preview drops RTO to 9%.",
            demoDeepLink = "https://ais-cockpit.local/?demo=artisan_outfitters#sandbox"
        ),
        PipelineLead(
            id = UUID.randomUUID().toString(),
            slug = "gorur_ghash",
            companyName = "Gorur Ghash Apparel",
            category = LeadCategory.ECOM,
            contactPerson = "Fahim Rahman",
            phone = "+8801715000005",
            packageName = "E-Com RTO Forensic Recovery Lab",
            status = LeadStatus.PROSPECT,
            dealValueBDT = 650000L,
            customHook = "Chittagong & Sylhet COD orders suffering 28% delivery drop; WhatsApp VIP confirmation filter eliminates bogus bookings.",
            demoDeepLink = "https://ais-cockpit.local/?demo=gorur_ghash#sandbox"
        )
    )

    private fun getInitialInvoices(): List<MushakInvoice> = listOf(
        MushakInvoice(
            id = UUID.randomUUID().toString(),
            serialNo = "2026-2027-DHK-1001",
            clientName = "Dom-Inno Developers Ltd.",
            clientBin = "000184920-0101",
            serviceDescription = "Tier-2 Single-Asset Relief Sandbox (50% Signature Retainer)",
            packageName = "Tier-2 Single-Asset Sandbox",
            baseAmountBDT = 600000L,
            status = MushakStatus.SETTLED,
            settlementRef = "CITY-RTGS-994821",
            issuedDate = "2026-10-21"
        ),
        MushakInvoice(
            id = UUID.randomUUID().toString(),
            serialNo = "2026-2027-DHK-1002",
            clientName = "Artisan Outfitters (Retail BD Ltd.)",
            clientBin = "000341829-0204",
            serviceDescription = "E-Com RTO Forensic Recovery Lab & Experiential Engine",
            packageName = "E-Com RTO Recovery Suite",
            baseAmountBDT = 850000L,
            status = MushakStatus.ISSUED,
            settlementRef = "AWAITING_WIRE",
            issuedDate = "2026-10-22"
        )
    )

    private fun getInitialDeposits(): List<LockDeposit> = listOf(
        LockDeposit(
            id = UUID.randomUUID().toString(),
            clientName = "Dom-Inno Developers Ltd.",
            packageTier = "Tier-2 Single-Asset Sandbox (50% Retainer)",
            amountBDT = 630000L,
            channel = DepositChannel.CITYGEM_PRIORITY,
            referenceNumber = "CITY-RTGS-994821",
            status = DepositStatus.VERIFIED,
            timestamp = "Yesterday, 3:45 PM"
        ),
        LockDeposit(
            id = UUID.randomUUID().toString(),
            clientName = "Sheltech (Pvt.) Ltd.",
            packageTier = "Tier-1 Enterprise Portfolio Cockpit Advance",
            amountBDT = 1260000L,
            channel = DepositChannel.BANK_WIRE,
            referenceNumber = "BRAC-BEFTN-847291",
            status = DepositStatus.FUNDS_LOCKED,
            timestamp = "Today, 11:20 AM"
        ),
        LockDeposit(
            id = UUID.randomUUID().toString(),
            clientName = "Artisan Outfitters",
            packageTier = "E-Com RTO Forensic Recovery Lab Deposit",
            amountBDT = 425000L,
            channel = DepositChannel.BKASH_MERCHANT,
            referenceNumber = "BK-TXN-0029481A",
            status = DepositStatus.AWAITING_HUMAN_LOCK,
            timestamp = "Today, 2:15 PM"
        )
    )
}
