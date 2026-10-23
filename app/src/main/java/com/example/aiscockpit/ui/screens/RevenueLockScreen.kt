package com.example.aiscockpit.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiscockpit.model.DepositChannel
import com.example.aiscockpit.model.DepositStatus
import com.example.aiscockpit.model.LockDeposit
import com.example.aiscockpit.ui.CockpitViewModel
import com.example.aiscockpit.ui.theme.*

@Composable
fun RevenueLockScreen(
    viewModel: CockpitViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val deposits by viewModel.deposits.collectAsState()
    val lockedTotalBDT by viewModel.totalLockedRevenueBDT.collectAsState()
    val targetBDT = viewModel.target90DayBDT

    val progress = (lockedTotalBDT.toFloat() / targetBDT.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(16.dp)
    ) {
        // Hero Trajectory Progress Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("revenue_trajectory_hero"),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StitchEmerald.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "LOCKED REVENUE PROGRESS",
                            style = MaterialTheme.typography.labelSmall,
                            color = StitchEmerald
                        )
                        Text(
                            text = "৳${formatNumber(lockedTotalBDT)}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = StitchEmerald
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "90-DAY TARGET",
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateTextSecondary
                        )
                        Text(
                            text = "৳1.00 Crore",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = StitchEmerald,
                    trackColor = ObsidianSurface3
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${String.format("%.1f", progress * 100)}% of target secured",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = "৳${formatNumber(targetBDT - lockedTotalBDT)} Remaining",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TRANSACTION CLEARING LEDGER",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )

            Button(
                onClick = { viewModel.setShowAddDepositDialog(true) },
                colors = ButtonDefaults.buttonColors(containerColor = StitchCyan),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("log_wire_btn")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = ObsidianBg, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Log Wire", color = ObsidianBg, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Deposits List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(deposits, key = { it.id }) { deposit ->
                DepositItemCard(
                    deposit = deposit,
                    onVerify = { viewModel.verifyDeposit(deposit.id) }
                )
            }
        }
    }

    if (uiState.showAddDepositDialog) {
        AddDepositDialog(
            onDismiss = { viewModel.setShowAddDepositDialog(false) },
            onAdd = { client, amount, channel, ref ->
                viewModel.addDeposit(client, amount, channel, ref)
                viewModel.setShowAddDepositDialog(false)
            }
        )
    }
}

@Composable
fun DepositItemCard(
    deposit: LockDeposit,
    onVerify: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = deposit.clientName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = "${deposit.packageTier} • ${deposit.channel.title}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextSecondary,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (deposit.status) {
                        DepositStatus.VERIFIED -> StitchEmerald.copy(alpha = 0.2f)
                        DepositStatus.FUNDS_LOCKED -> StitchCyan.copy(alpha = 0.2f)
                        DepositStatus.AWAITING_HUMAN_LOCK -> NeonAmber.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        text = deposit.status.label,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = when (deposit.status) {
                            DepositStatus.VERIFIED -> StitchEmerald
                            DepositStatus.FUNDS_LOCKED -> StitchCyan
                            DepositStatus.AWAITING_HUMAN_LOCK -> NeonAmber
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ref: ${deposit.referenceNumber} • ${deposit.timestamp}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateTextSecondary,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "৳${formatNumber(deposit.amountBDT)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = StitchCyan
                    )
                }

                if (deposit.status != DepositStatus.VERIFIED) {
                    OutlinedButton(
                        onClick = onVerify,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StitchEmerald),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StitchEmerald),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Verify", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Verify Clearance", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddDepositDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Long, DepositChannel, String) -> Unit
) {
    var client by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("600000") }
    var channel by remember { mutableStateOf(DepositChannel.BANK_WIRE) }
    var ref by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Retainer Wire", color = SlateTextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = client,
                    onValueChange = { client = it },
                    label = { Text("Client Company Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount (BDT)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ref,
                    onValueChange = { ref = it },
                    label = { Text("Transaction / BEFTN Ref") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toLongOrNull() ?: 600000L
                    if (client.isNotBlank()) {
                        onAdd(client, amount, channel, ref)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = StitchCyan)
            ) {
                Text("Confirm Deposit", color = ObsidianBg, fontWeight = FontWeight.Bold)
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
