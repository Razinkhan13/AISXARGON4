package com.example.aiscockpit.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiscockpit.model.MushakInvoice
import com.example.aiscockpit.model.MushakStatus
import com.example.aiscockpit.ui.CockpitViewModel
import com.example.aiscockpit.ui.theme.*

@Composable
fun MushakScreen(
    viewModel: CockpitViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val invoices by viewModel.mushakInvoices.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(16.dp)
    ) {
        // Header Banner
        Card(
            modifier = Modifier.fillMaxWidth().testTag("mushak_banner_card"),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "NBR FORM MUSHAK-6.3",
                        style = MaterialTheme.typography.labelSmall,
                        color = StitchCyan
                    )
                    Text(
                        text = "VAT Tax Invoicing",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = "Value Added Tax & SD Act 2012, Section 51",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextSecondary,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = { viewModel.setShowAddMushakDialog(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = StitchCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("issue_mushak_btn")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Issue", tint = ObsidianBg, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Issue Invoice", color = ObsidianBg, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(invoices, key = { it.id }) { invoice ->
                MushakInvoiceCard(
                    invoice = invoice,
                    onSettle = { viewModel.markMushakSettled(invoice.id) },
                    onCopy = {
                        copyMushak(context, invoice)
                        viewModel.showSnackbar("Form Mushak-6.3 tax invoice data copied")
                    }
                )
            }
        }
    }

    if (uiState.showAddMushakDialog) {
        AddMushakDialog(
            onDismiss = { viewModel.setShowAddMushakDialog(false) },
            onAdd = { client, bin, cat, amount ->
                viewModel.addMushakInvoice(client, bin, cat, amount)
                viewModel.setShowAddMushakDialog(false)
            }
        )
    }
}

@Composable
fun MushakInvoiceCard(
    invoice: MushakInvoice,
    onSettle: () -> Unit,
    onCopy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("mushak_card_${invoice.serialNo}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF0F1722))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Authentic NBR Header
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GOVERNMENT OF THE PEOPLE'S REPUBLIC OF BANGLADESH",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "NATIONAL BOARD OF REVENUE • FORM MUSHAK-6.3",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F1722)
                )
                Text(
                    text = "[See Clauses (c) and (f) of Sub-Rule (1) of Rule 40]",
                    fontSize = 8.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Metadata Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Serial: ${invoice.serialNo}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF0284C7))
                    Text(text = "Client: ${invoice.clientName}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                    Text(text = "Client BIN: ${invoice.clientBin}", fontSize = 10.sp, color = Color.DarkGray)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Date: ${invoice.issuedDate}", fontSize = 10.sp, color = Color.DarkGray)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (invoice.status == MushakStatus.SETTLED) Color(0xFFD1FAE5) else Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = invoice.status.name,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (invoice.status == MushakStatus.SETTLED) Color(0xFF065F46) else Color(0xFF92400E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Table of Supply
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Description of Service", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Text(text = "Base Value (BDT)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                }
                Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFCBD5E1))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = invoice.serviceDescription, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.Black, modifier = Modifier.weight(1f))
                    Text(text = "৳${formatNumber(invoice.baseAmountBDT)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Statutory VAT @ 5%:", fontSize = 10.sp, color = Color(0xFFB45309))
                    Text(text = "৳${formatNumber(invoice.vatAmountBDT)}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                }

                Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color.Black)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "NET TOTAL WITH VAT:", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                    Text(text = "৳${formatNumber(invoice.totalAmountBDT)}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0284C7))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AIS Enterprise Systems • NBR BIN: 004928190-0101",
                    fontSize = 8.sp,
                    color = Color.Gray
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TextButton(onClick = onCopy) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp), tint = Color.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy", color = Color.Black, fontSize = 11.sp)
                    }

                    if (invoice.status != MushakStatus.SETTLED) {
                        Button(
                            onClick = onSettle,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Mark Settled", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddMushakDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Long) -> Unit
) {
    var client by remember { mutableStateOf("") }
    var bin by remember { mutableStateOf("000") }
    var service by remember { mutableStateOf("Tier-2 Single-Asset Relief Sandbox (50% Retainer)") }
    var baseAmtText by remember { mutableStateOf("600000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Issue Form Mushak-6.3", color = SlateTextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = client,
                    onValueChange = { client = it },
                    label = { Text("Client Company Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bin,
                    onValueChange = { bin = it },
                    label = { Text("Client BIN Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = service,
                    onValueChange = { service = it },
                    label = { Text("Service Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = baseAmtText,
                    onValueChange = { baseAmtText = it },
                    label = { Text("Base Retainer Amount (BDT)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = baseAmtText.toLongOrNull() ?: 600000L
                    if (client.isNotBlank()) {
                        onAdd(client, bin, service, amount)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = StitchCyan)
            ) {
                Text("Generate Tax Invoice", color = ObsidianBg, fontWeight = FontWeight.Bold)
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

fun copyMushak(context: Context, invoice: MushakInvoice) {
    val text = """
        *FORM MUSHAK-6.3 (VAT TAX INVOICE)*
        Serial No: ${invoice.serialNo}
        Date: ${invoice.issuedDate}
        Client: ${invoice.clientName} (BIN: ${invoice.clientBin})
        Supplier: AIS Enterprise Systems (BIN: 004928190-0101)
        Service: ${invoice.serviceDescription}
        Base Value: ৳${formatNumber(invoice.baseAmountBDT)}
        VAT (5%): ৳${formatNumber(invoice.vatAmountBDT)}
        *Net Total: ৳${formatNumber(invoice.totalAmountBDT)}*
        Status: ${invoice.status.name}
    """.trimIndent()
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Mushak-6.3", text))
}
