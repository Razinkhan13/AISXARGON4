package com.example.aiscockpit.model

enum class VaultDocType(val code: String, val title: String) {
    MSA("AIS-MSA-2026", "Master Service Agreement"),
    MUSHAK_6_3("NBR-MUSHAK-6.3", "Statutory VAT Tax Invoice"),
    SLA("AIS-SLA-999", "Service Level & 5x Risk Reversal Guarantee"),
    SOW("AIS-SOW-EXEC", "Scope of Work & Deliverables Architecture")
}

data class VaultDocument(
    val docType: VaultDocType,
    val title: String,
    val executiveSummary: String,
    val statutoryLegalText: String,
    val keyGuarantees: List<String>
)

data class PitchSlide(
    val slideNumber: Int,
    val tag: String,
    val title: String,
    val subtitle: String,
    val keyBulletPoints: List<String>,
    val callToAction: String
)
