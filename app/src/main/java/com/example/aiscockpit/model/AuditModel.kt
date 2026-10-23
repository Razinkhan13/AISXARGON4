package com.example.aiscockpit.model

enum class AuditMode {
    REAL_ESTATE,
    ECOM_RTO
}

data class RealEstateAuditInput(
    val companyName: String = "Dom-Inno Developers Ltd.",
    val katha: Double = 8.0,
    val units: Int = 16,
    val sqftPerUnit: Double = 2000.0,
    val pricePerSqft: Double = 11000.0,
    val cancelledUnits: Int = 2,
    val stalledUnits: Int = 3,
    val bankOdCrore: Double = 3.188,
    val annualBankOdRatePercent: Double = 13.5
) {
    // Calculations
    val totalProjectSqft: Double = units * sqftPerUnit
    val totalProjectValueBDT: Double = totalProjectSqft * pricePerSqft
    val totalProjectValueCrore: Double = totalProjectValueBDT / 10000000.0

    val unitValueBDT: Double = sqftPerUnit * pricePerSqft
    val frozenCapitalUnits: Int = cancelledUnits + stalledUnits
    val frozenCapitalBDT: Double = frozenCapitalUnits * unitValueBDT
    val frozenCapitalCrore: Double = frozenCapitalBDT / 10000000.0

    // REDMA 2010 Section 14 statutory 15% refund interest liability on cancelled units
    val redmaRefundLiabilityBDT: Double = (cancelledUnits * unitValueBDT) * 0.15
    val redmaRefundLiabilityCrore: Double = redmaRefundLiabilityBDT / 10000000.0

    // Monthly Bank OD Interest hemorrhage
    val monthlyBankOdBleedBDT: Double = (bankOdCrore * 10000000.0) * (annualBankOdRatePercent / 100.0) / 12.0
    val monthlyBankOdBleedCrore: Double = monthlyBankOdBleedBDT / 10000000.0

    // Total Bleed / Frozen Risk
    val totalBleedCrore: Double = frozenCapitalCrore + redmaRefundLiabilityCrore + (monthlyBankOdBleedCrore * 6)
}

data class EcomRtoAuditInput(
    val brandName: String = "Artisan Outfitters",
    val monthlyOrders: Int = 8500,
    val averageOrderValueBDT: Double = 2450.0,
    val returnToOriginPercent: Double = 22.0,
    val courierReverseFeeBDT: Double = 140.0,
    val packagingDepreciationLossBDT: Double = 60.0
) {
    val totalMonthlyRevenueBDT: Double = monthlyOrders * averageOrderValueBDT
    val rtoOrdersCount: Int = ((monthlyOrders * returnToOriginPercent) / 100.0).toInt()
    val courierHemhorrageBDT: Double = rtoOrdersCount * (courierReverseFeeBDT + packagingDepreciationLossBDT)
    val trappedGmvBDT: Double = rtoOrdersCount * averageOrderValueBDT
    val annualHemorrhageLakh: Double = (courierHemhorrageBDT * 12) / 100000.0
}
