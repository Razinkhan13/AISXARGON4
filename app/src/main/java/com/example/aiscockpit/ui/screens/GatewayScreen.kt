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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiscockpit.ui.CockpitViewModel
import com.example.aiscockpit.ui.theme.*

@Composable
fun GatewayScreen(
    viewModel: CockpitViewModel,
    modifier: Modifier = Modifier
) {
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
        // Hero Header
        Card(
            modifier = Modifier.fillMaxWidth().testTag("gateway_hero_card"),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StitchEmerald.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CAPITAL SETTLEMENT & RETAINER LOCK",
                    style = MaterialTheme.typography.labelSmall,
                    color = StitchEmerald
                )
                Text(
                    text = "Bank Escrow & Payment Routing",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                Text(
                    text = "Corporate RTGS/BEFTN accounts and MFS Merchant settlement channels for AIS Enterprise Retainer locks.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // City Bank Citygem Priority Account Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StitchCyan.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CITY BANK CITYGEM PRIORITY",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StitchCyan
                        )
                        Text(
                            text = "Primary Escrow Wire Channel (BDT)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlateTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "Bank",
                        tint = StitchCyan,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ObsidianSurface2, RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AccountDetailRow("Account Name", "AIS Enterprise Systems Ltd.")
                    AccountDetailRow("Account No.", "1503-8829-1001-9002")
                    AccountDetailRow("Bank", "The City Bank Limited")
                    AccountDetailRow("Branch", "Gulshan Citygem Center, Dhaka")
                    AccountDetailRow("Routing No.", "225271829")
                    AccountDetailRow("SWIFT Code", "CIBLBDDH")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val text = """
                            *AIS ENTERPRISE SYSTEMS — BANK WIRE ROUTING*
                            Bank: The City Bank Limited (Citygem Center)
                            Account Name: AIS Enterprise Systems Ltd.
                            Account No: 1503-8829-1001-9002
                            Routing Number: 225271829
                            SWIFT: CIBLBDDH
                            Purpose: 50% Milestone Retainer Lock
                        """.trimIndent()
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Bank Routing", text))
                        viewModel.showSnackbar("Citygem Bank routing details copied to clipboard")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = StitchCyan),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = ObsidianBg, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy City Bank Wire Details", color = ObsidianBg, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // bKash Merchant & Nagad Sheba Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonRose.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "INSTANT MFS MERCHANT CHANNELS",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NeonRosePastel
                        )
                        Text(
                            text = "bKash Merchant Pay & Nagad Corporate",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlateTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "QR",
                        tint = NeonRose,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ObsidianSurface2, RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AccountDetailRow("bKash Merchant", "01711-948201 (Counter 1)")
                    AccountDetailRow("Nagad Direct", "01819-332910 (Corporate)")
                    AccountDetailRow("Reference", "CLIENT_NAME (e.g. DOMINNO)")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val text = "bKash Merchant: 01711948201 (Use Client Name as Ref)"
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("MFS Pay", text))
                        viewModel.showSnackbar("bKash Merchant number copied")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRose),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy bKash Merchant Number", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AccountDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = SlateTextSecondary,
            fontSize = 12.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = SlateTextPrimary,
            fontSize = 12.sp
        )
    }
}
