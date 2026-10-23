package com.example.aiscockpit.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiscockpit.model.AuditMode
import com.example.aiscockpit.model.RealEstateAuditInput
import com.example.aiscockpit.model.EcomRtoAuditInput
import com.example.aiscockpit.ui.CockpitViewModel
import com.example.aiscockpit.ui.theme.*

@Composable
fun AuditCalculatorScreen(
    viewModel: CockpitViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val reAudit by viewModel.realEstateAudit.collectAsState()
    val ecomAudit by viewModel.ecomAudit.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .padding(bottom = 96.dp)
    ) {
        // Mode Switcher Tab: Real Estate vs E-Commerce RTO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ObsidianSurface1, RoundedCornerShape(14.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { viewModel.setAuditMode(AuditMode.REAL_ESTATE) },
                modifier = Modifier.weight(1f).testTag("audit_tab_re"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.auditMode == AuditMode.REAL_ESTATE) StitchCyan else Color.Transparent
                )
            ) {
                Text(
                    text = "🏢 Real Estate Bleed",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (uiState.auditMode == AuditMode.REAL_ESTATE) ObsidianBg else SlateTextSecondary
                )
            }

            Button(
                onClick = { viewModel.setAuditMode(AuditMode.ECOM_RTO) },
                modifier = Modifier.weight(1f).testTag("audit_tab_ecom"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.auditMode == AuditMode.ECOM_RTO) StitchCyan else Color.Transparent
                )
            ) {
                Text(
                    text = "📦 E-Com RTO Hemorrhage",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (uiState.auditMode == AuditMode.ECOM_RTO) ObsidianBg else SlateTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.auditMode == AuditMode.REAL_ESTATE) {
            RealEstateAuditSection(
                audit = reAudit,
                onUpdate = { units, sqft, priceSqft, cancelled, stalled, bankOd ->
                    viewModel.updateRealEstateAudit(units, sqft, priceSqft, cancelled, stalled, bankOd)
                },
                onCopyMemo = {
                    copyReMemo(context, reAudit)
                    viewModel.showSnackbar("Real estate forensic audit memo copied")
                }
            )
        } else {
            EcomAuditSection(
                audit = ecomAudit,
                onUpdate = { orders, aov, rto, fee ->
                    viewModel.updateEcomAudit(orders, aov, rto, fee)
                },
                onCopyMemo = {
                    copyEcomMemo(context, ecomAudit)
                    viewModel.showSnackbar("E-Commerce RTO recovery memo copied")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5x Risk Reversal Guarantee Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("audit_guarantee_seal"),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonRose.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Guarantee",
                        tint = NeonRose,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "5X CAPITAL RELIEF GUARANTEE",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NeonRosePastel
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "If AIS Cockpit telemetry does not demonstrate at least 5x the engagement retainer in identified frozen inventory relief within 60 days, client receives a 100% full refund with zero penalty.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateTextPrimary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "NBR Mushak-6.3 Tax Withholding Document Included • Escrow Wire Backed",
                    style = MaterialTheme.typography.labelSmall,
                    color = StitchCyan
                )
            }
        }
    }
}

@Composable
fun RealEstateAuditSection(
    audit: RealEstateAuditInput,
    onUpdate: (Int, Double, Double, Int, Int, Double) -> Unit,
    onCopyMemo: () -> Unit
) {
    var units by remember(audit) { mutableStateOf(audit.units.toString()) }
    var sqft by remember(audit) { mutableStateOf(audit.sqftPerUnit.toInt().toString()) }
    var priceSqft by remember(audit) { mutableStateOf(audit.pricePerSqft.toInt().toString()) }
    var cancelled by remember(audit) { mutableStateOf(audit.cancelledUnits.toString()) }
    var stalled by remember(audit) { mutableStateOf(audit.stalledUnits.toString()) }
    var bankOd by remember(audit) { mutableStateOf(audit.bankOdCrore.toString()) }

    // Summary Highlight Card
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface2),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StitchCyan.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "TOTAL 6-MONTH BLEED EXPOSURE",
                style = MaterialTheme.typography.labelSmall,
                color = SlateTextSecondary
            )
            Text(
                text = "৳${String.format("%.2f", audit.totalBleedCrore)} Crore",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = StitchCyan
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Frozen Units", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
                    Text("৳${String.format("%.2f", audit.frozenCapitalCrore)} Cr (${audit.frozenCapitalUnits} units)", style = MaterialTheme.typography.titleMedium, color = SlateTextPrimary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("REDMA Liability", style = MaterialTheme.typography.labelSmall, color = NeonRosePastel)
                    Text("৳${String.format("%.2f", audit.redmaRefundLiabilityCrore)} Cr (15% statutory)", style = MaterialTheme.typography.titleMedium, color = NeonRose)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Monthly Bank OD Bleed", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
                    Text("৳${String.format("%.2f", audit.monthlyBankOdBleedCrore * 100)} Lakh/mo", style = MaterialTheme.typography.titleMedium, color = SlateTextPrimary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Bank OD Facility", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
                    Text("৳${audit.bankOdCrore} Cr @ ${audit.annualBankOdRatePercent}%", style = MaterialTheme.typography.titleMedium, color = SlateTextPrimary)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Input Controls Card
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "AUDIT PARAMETERS (${audit.companyName})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = units,
                    onValueChange = {
                        units = it
                        onUpdate(it.toIntOrNull() ?: audit.units, sqft.toDoubleOrNull() ?: audit.sqftPerUnit, priceSqft.toDoubleOrNull() ?: audit.pricePerSqft, cancelled.toIntOrNull() ?: audit.cancelledUnits, stalled.toIntOrNull() ?: audit.stalledUnits, bankOd.toDoubleOrNull() ?: audit.bankOdCrore)
                    },
                    label = { Text("Total Units") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = sqft,
                    onValueChange = {
                        sqft = it
                        onUpdate(units.toIntOrNull() ?: audit.units, it.toDoubleOrNull() ?: audit.sqftPerUnit, priceSqft.toDoubleOrNull() ?: audit.pricePerSqft, cancelled.toIntOrNull() ?: audit.cancelledUnits, stalled.toIntOrNull() ?: audit.stalledUnits, bankOd.toDoubleOrNull() ?: audit.bankOdCrore)
                    },
                    label = { Text("SqFt / Unit") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = priceSqft,
                    onValueChange = {
                        priceSqft = it
                        onUpdate(units.toIntOrNull() ?: audit.units, sqft.toDoubleOrNull() ?: audit.sqftPerUnit, it.toDoubleOrNull() ?: audit.pricePerSqft, cancelled.toIntOrNull() ?: audit.cancelledUnits, stalled.toIntOrNull() ?: audit.stalledUnits, bankOd.toDoubleOrNull() ?: audit.bankOdCrore)
                    },
                    label = { Text("Price / SqFt (৳)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = bankOd,
                    onValueChange = {
                        bankOd = it
                        onUpdate(units.toIntOrNull() ?: audit.units, sqft.toDoubleOrNull() ?: audit.sqftPerUnit, priceSqft.toDoubleOrNull() ?: audit.pricePerSqft, cancelled.toIntOrNull() ?: audit.cancelledUnits, stalled.toIntOrNull() ?: audit.stalledUnits, it.toDoubleOrNull() ?: audit.bankOdCrore)
                    },
                    label = { Text("Bank OD (Cr)") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = cancelled,
                    onValueChange = {
                        cancelled = it
                        onUpdate(units.toIntOrNull() ?: audit.units, sqft.toDoubleOrNull() ?: audit.sqftPerUnit, priceSqft.toDoubleOrNull() ?: audit.pricePerSqft, it.toIntOrNull() ?: audit.cancelledUnits, stalled.toIntOrNull() ?: audit.stalledUnits, bankOd.toDoubleOrNull() ?: audit.bankOdCrore)
                    },
                    label = { Text("Cancelled Units") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = stalled,
                    onValueChange = {
                        stalled = it
                        onUpdate(units.toIntOrNull() ?: audit.units, sqft.toDoubleOrNull() ?: audit.sqftPerUnit, priceSqft.toDoubleOrNull() ?: audit.pricePerSqft, cancelled.toIntOrNull() ?: audit.cancelledUnits, it.toIntOrNull() ?: audit.stalledUnits, bankOd.toDoubleOrNull() ?: audit.bankOdCrore)
                    },
                    label = { Text("Stalled Units") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onCopyMemo,
                modifier = Modifier.fillMaxWidth().testTag("copy_audit_memo_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = StitchCyan),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = ObsidianBg)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Copy Executive Forensic Memo", color = ObsidianBg, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EcomAuditSection(
    audit: EcomRtoAuditInput,
    onUpdate: (Int, Double, Double, Double) -> Unit,
    onCopyMemo: () -> Unit
) {
    var orders by remember(audit) { mutableStateOf(audit.monthlyOrders.toString()) }
    var aov by remember(audit) { mutableStateOf(audit.averageOrderValueBDT.toInt().toString()) }
    var rtoRate by remember(audit) { mutableStateOf(audit.returnToOriginPercent.toString()) }
    var fee by remember(audit) { mutableStateOf(audit.courierReverseFeeBDT.toInt().toString()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface2),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonRose.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ANNUAL RTO PROFIT HEMORRHAGE",
                style = MaterialTheme.typography.labelSmall,
                color = SlateTextSecondary
            )
            Text(
                text = "৳${String.format("%.2f", audit.annualHemorrhageLakh)} Lakh / Yr",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = NeonRosePastel
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Monthly RTO Returns", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
                    Text("${audit.rtoOrdersCount} orders (${audit.returnToOriginPercent}%)", style = MaterialTheme.typography.titleMedium, color = SlateTextPrimary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Monthly Courier Bleed", style = MaterialTheme.typography.labelSmall, color = NeonRose)
                    Text("৳${formatNumber(audit.courierHemhorrageBDT.toLong())}", style = MaterialTheme.typography.titleMedium, color = NeonRose)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Trapped Monthly GMV in Transit: ৳${formatNumber(audit.trappedGmvBDT.toLong())}",
                style = MaterialTheme.typography.bodyMedium,
                color = StitchCyan
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "E-COMMERCE METRICS (${audit.brandName})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = orders,
                    onValueChange = {
                        orders = it
                        onUpdate(it.toIntOrNull() ?: audit.monthlyOrders, aov.toDoubleOrNull() ?: audit.averageOrderValueBDT, rtoRate.toDoubleOrNull() ?: audit.returnToOriginPercent, fee.toDoubleOrNull() ?: audit.courierReverseFeeBDT)
                    },
                    label = { Text("Monthly Orders") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = aov,
                    onValueChange = {
                        aov = it
                        onUpdate(orders.toIntOrNull() ?: audit.monthlyOrders, it.toDoubleOrNull() ?: audit.averageOrderValueBDT, rtoRate.toDoubleOrNull() ?: audit.returnToOriginPercent, fee.toDoubleOrNull() ?: audit.courierReverseFeeBDT)
                    },
                    label = { Text("Avg Order Value (৳)") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = rtoRate,
                    onValueChange = {
                        rtoRate = it
                        onUpdate(orders.toIntOrNull() ?: audit.monthlyOrders, aov.toDoubleOrNull() ?: audit.averageOrderValueBDT, it.toDoubleOrNull() ?: audit.returnToOriginPercent, fee.toDoubleOrNull() ?: audit.courierReverseFeeBDT)
                    },
                    label = { Text("RTO Rate (%)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = fee,
                    onValueChange = {
                        fee = it
                        onUpdate(orders.toIntOrNull() ?: audit.monthlyOrders, aov.toDoubleOrNull() ?: audit.averageOrderValueBDT, rtoRate.toDoubleOrNull() ?: audit.returnToOriginPercent, it.toDoubleOrNull() ?: audit.courierReverseFeeBDT)
                    },
                    label = { Text("Courier Reverse Fee") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onCopyMemo,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonRose),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Copy E-Com Recovery Memo", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun copyReMemo(context: Context, audit: RealEstateAuditInput) {
    val text = """
        *EXECUTIVE MEMO: REAL ESTATE CAPITAL BLEED AUDIT*
        Company: ${audit.companyName}
        Total Stalled & Cancelled: ${audit.frozenCapitalUnits} Units (৳${String.format("%.2f", audit.frozenCapitalCrore)} Cr frozen)
        REDMA Section 14 Refund Liability: ৳${String.format("%.2f", audit.redmaRefundLiabilityCrore)} Cr (15% statutory penalty)
        Monthly Bank OD Bleed: ৳${String.format("%.2f", audit.monthlyBankOdBleedCrore * 100)} Lakh/mo
        *Total 6-Month Bleed Exposure: ৳${String.format("%.2f", audit.totalBleedCrore)} Crore*
        
        AIS Solution: 38-Second Interactive Showroom with 60-Day 5x Risk Reversal Guarantee.
    """.trimIndent()
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("RE Audit Memo", text))
}

fun copyEcomMemo(context: Context, audit: EcomRtoAuditInput) {
    val text = """
        *EXECUTIVE MEMO: E-COMMERCE RTO RECOVERY LAB*
        Brand: ${audit.brandName}
        Monthly Orders: ${audit.monthlyOrders} @ ৳${audit.averageOrderValueBDT}
        RTO Return Rate: ${audit.returnToOriginPercent}% (${audit.rtoOrdersCount} returned orders/mo)
        Courier & Packaging Hemorrhage: ৳${formatNumber(audit.courierHemhorrageBDT.toLong())}/mo
        *Annual Cash Hemorrhage: ৳${String.format("%.2f", audit.annualHemorrhageLakh)} Lakh/Year*
        
        AIS Solution: 3D AR Garment & Sizing Visualizer cuts RTO from 22% down to 9%.
    """.trimIndent()
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("ECom Audit Memo", text))
}
