package com.example.aiscockpit.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiscockpit.model.LeadCategory
import com.example.aiscockpit.model.LeadStatus
import com.example.aiscockpit.model.PipelineLead
import com.example.aiscockpit.ui.CockpitViewModel
import com.example.aiscockpit.ui.theme.*

@Composable
fun OutreachScreen(
    viewModel: CockpitViewModel,
    modifier: Modifier = Modifier
) {
    val leads by viewModel.leads.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val filteredLeads = remember(leads, uiState.selectedIndustryFilter, uiState.searchQuery) {
        leads.filter { lead ->
            val matchesCategory = uiState.selectedIndustryFilter == null || lead.category == uiState.selectedIndustryFilter
            val matchesSearch = uiState.searchQuery.isBlank() ||
                    lead.companyName.contains(uiState.searchQuery, ignoreCase = true) ||
                    lead.customHook.contains(uiState.searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    val totalPipelineBDT = remember(leads) { leads.sumOf { it.dealValueBDT } }
    val lockedDealsBDT = remember(leads) {
        leads.filter { it.status == LeadStatus.LOCKED_WIRE || it.status == LeadStatus.MUSHAK_ISSUED }
            .sumOf { it.dealValueBDT }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(horizontal = 16.dp)
    ) {
        // Pipeline Stats Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("outreach_kpi_card"),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TOTAL PIPELINE",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateTextSecondary
                    )
                    Text(
                        text = formatBDTShort(totalPipelineBDT),
                        style = MaterialTheme.typography.headlineMedium,
                        color = StitchCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${leads.size} High-Value Accounts",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextSecondary
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(48.dp)
                        .width(1.dp),
                    color = SlateBorder
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "LOCKED / ISSUED",
                        style = MaterialTheme.typography.labelSmall,
                        color = StitchEmerald
                    )
                    Text(
                        text = formatBDTShort(lockedDealsBDT),
                        style = MaterialTheme.typography.headlineMedium,
                        color = StitchEmerald,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Statutory Backed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextSecondary
                    )
                }
            }
        }

        // Industry Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = uiState.selectedIndustryFilter == null,
                onClick = { viewModel.setIndustryFilter(null) },
                label = { Text("All (${leads.size})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StitchCyan,
                    selectedLabelColor = ObsidianBg
                )
            )
            FilterChip(
                selected = uiState.selectedIndustryFilter == LeadCategory.REAL_ESTATE,
                onClick = { viewModel.setIndustryFilter(LeadCategory.REAL_ESTATE) },
                label = { Text("Real Estate") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StitchCyan,
                    selectedLabelColor = ObsidianBg
                )
            )
            FilterChip(
                selected = uiState.selectedIndustryFilter == LeadCategory.ECOM,
                onClick = { viewModel.setIndustryFilter(LeadCategory.ECOM) },
                label = { Text("E-Commerce / D2C") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StitchCyan,
                    selectedLabelColor = ObsidianBg
                )
            )
        }

        // Leads List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(filteredLeads, key = { it.id }) { lead ->
                LeadCard(
                    lead = lead,
                    onLaunchWhatsApp = {
                        launchWhatsAppPitch(context, lead)
                        viewModel.showSnackbar("Opening WhatsApp pitch for ${lead.companyName}")
                    },
                    onStatusChange = { newStatus ->
                        viewModel.updateLeadStatus(lead.id, newStatus)
                    }
                )
            }
        }
    }

    if (uiState.showAddLeadDialog) {
        AddLeadDialog(
            onDismiss = { viewModel.setShowAddLeadDialog(false) },
            onAdd = { company, category, phone, pkg, value, hook ->
                viewModel.addLead(company, category, phone, pkg, value, hook)
                viewModel.setShowAddLeadDialog(false)
            }
        )
    }
}

@Composable
fun LeadCard(
    lead: PipelineLead,
    onLaunchWhatsApp: () -> Unit,
    onStatusChange: (LeadStatus) -> Unit
) {
    var expandedStatusMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lead_card_${lead.slug}"),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Company name + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lead.companyName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = "${lead.category.displayName} • ${lead.packageName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StitchCyan
                    )
                }

                Box {
                    Surface(
                        modifier = Modifier
                            .clickable { expandedStatusMenu = true }
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = when (lead.status) {
                            LeadStatus.LOCKED_WIRE -> StitchEmerald.copy(alpha = 0.2f)
                            LeadStatus.MUSHAK_ISSUED -> StitchCyan.copy(alpha = 0.2f)
                            LeadStatus.DEMO_COMPLETED -> NeonRose.copy(alpha = 0.2f)
                            else -> ObsidianSurface3
                        },
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            when (lead.status) {
                                LeadStatus.LOCKED_WIRE -> StitchEmerald
                                LeadStatus.MUSHAK_ISSUED -> StitchCyan
                                LeadStatus.DEMO_COMPLETED -> NeonRose
                                else -> SlateBorder
                            }
                        )
                    ) {
                        Text(
                            text = lead.status.displayName,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = when (lead.status) {
                                LeadStatus.LOCKED_WIRE -> StitchEmerald
                                LeadStatus.MUSHAK_ISSUED -> StitchCyan
                                LeadStatus.DEMO_COMPLETED -> NeonRose
                                else -> SlateTextPrimary
                            }
                        )
                    }

                    DropdownMenu(
                        expanded = expandedStatusMenu,
                        onDismissRequest = { expandedStatusMenu = false }
                    ) {
                        LeadStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.displayName) },
                                onClick = {
                                    onStatusChange(status)
                                    expandedStatusMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Forensic Hook Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ObsidianSurface2, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = lead.customHook,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateTextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row: Deal Value + 1-Tap WhatsApp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DEAL TARGET",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateTextSecondary,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "৳${formatNumber(lead.dealValueBDT)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                }

                Button(
                    onClick = onLaunchWhatsApp,
                    colors = ButtonDefaults.buttonColors(containerColor = StitchEmeraldDark),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("whatsapp_btn_${lead.slug}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "WhatsApp",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "1-Tap Pitch",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AddLeadDialog(
    onDismiss: () -> Unit,
    onAdd: (String, LeadCategory, String, String, Long, String) -> Unit
) {
    var company by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(LeadCategory.REAL_ESTATE) }
    var phone by remember { mutableStateOf("+8801") }
    var packageName by remember { mutableStateOf("Tier-2 Single-Asset Relief Sandbox") }
    var dealValueText by remember { mutableStateOf("1200000") }
    var customHook by remember { mutableStateOf("REDMA Section 14 statutory 15% refund interest shock on stalled units creates critical financial liability.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Pipeline Account", color = SlateTextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth().testTag("add_lead_company_input")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { category = LeadCategory.REAL_ESTATE },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (category == LeadCategory.REAL_ESTATE) StitchCyan else ObsidianSurface3
                        )
                    ) {
                        Text("Real Estate", color = if (category == LeadCategory.REAL_ESTATE) ObsidianBg else SlateTextPrimary)
                    }
                    Button(
                        onClick = { category = LeadCategory.ECOM },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (category == LeadCategory.ECOM) StitchCyan else ObsidianSurface3
                        )
                    ) {
                        Text("E-Commerce", color = if (category == LeadCategory.ECOM) ObsidianBg else SlateTextPrimary)
                    }
                }
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone / WhatsApp") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dealValueText,
                    onValueChange = { dealValueText = it },
                    label = { Text("Target Retainer (BDT)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = customHook,
                    onValueChange = { customHook = it },
                    label = { Text("Forensic Bleed Hook") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val value = dealValueText.toLongOrNull() ?: 1000000L
                    if (company.isNotBlank()) {
                        onAdd(company, category, phone, packageName, value, customHook)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = StitchCyan)
            ) {
                Text("Add Account", color = ObsidianBg, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SlateTextSecondary)
            }
        },
        containerColor = ObsidianSurface1
    )
}

fun launchWhatsAppPitch(context: Context, lead: PipelineLead) {
    val message = """
        *AIS Enterprise Systems — Capital Relief Protocol*
        Attention: ${lead.contactPerson} (${lead.companyName})
        
        ${lead.customHook}
        
        We have spun up your private white-labeled 38-second experiential simulation:
        🔗 ${lead.demoDeepLink}
        
        Backed by a 60-Day 5x Risk-Reversal Warranty & Bangladesh Form Mushak-6.3 VAT Settlement.
    """.trimIndent()

    val cleanPhone = lead.phone.replace("+", "").replace(" ", "").replace("-", "")
    val url = "https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}"

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback: copy pitch text to clipboard
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("AIS WhatsApp Pitch", message)
        clipboard.setPrimaryClip(clip)
    }
}

fun formatBDTShort(amount: Long): String {
    return when {
        amount >= 10000000L -> "৳${String.format("%.2f", amount / 10000000.0)} Cr"
        amount >= 100000L -> "৳${String.format("%.2f", amount / 100000.0)} L"
        else -> "৳$amount"
    }
}

fun formatNumber(amount: Long): String {
    return String.format("%,d", amount)
}
