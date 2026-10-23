package com.example.aiscockpit.model

enum class MushakStatus {
    DRAFT,
    ISSUED,
    SETTLED
}

data class MushakInvoice(
    val id: String,
    val serialNo: String,
    val clientName: String,
    val clientBin: String,
    val serviceDescription: String,
    val packageName: String,
    val baseAmountBDT: Long,
    val vatRatePercent: Double = 5.0,
    val status: MushakStatus = MushakStatus.ISSUED,
    val settlementRef: String = "",
    val issuedDate: String = "2026-10-23",
    val authorizedSignatory: String = "Managing Director / AIS Enterprise Systems"
) {
    val vatAmountBDT: Long = ((baseAmountBDT * vatRatePercent) / 100.0).toLong()
    val totalAmountBDT: Long = baseAmountBDT + vatAmountBDT
}
