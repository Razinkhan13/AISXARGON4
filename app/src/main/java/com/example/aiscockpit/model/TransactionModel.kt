package com.example.aiscockpit.model

enum class DepositChannel(val title: String) {
    BANK_WIRE("Bank RTGS / BEFTN Wire"),
    BKASH_MERCHANT("bKash Merchant Pay"),
    NAGAD_SHEBA("Nagad Direct Business"),
    CITYGEM_PRIORITY("City Bank Citygem Priority")
}

enum class DepositStatus(val label: String) {
    FUNDS_LOCKED("Funds Locked"),
    AWAITING_HUMAN_LOCK("Awaiting Verification"),
    VERIFIED("Reconciled & Cleared")
}

data class LockDeposit(
    val id: String,
    val clientName: String,
    val packageTier: String,
    val amountBDT: Long,
    val channel: DepositChannel,
    val referenceNumber: String,
    val status: DepositStatus,
    val timestamp: String,
    val verifiedBy: String = "Treasury Officer"
)
