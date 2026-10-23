package com.example.aiscockpit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aiscockpit.ui.CockpitViewModel
import com.example.aiscockpit.ui.theme.*

@Composable
fun SandboxScreen(
    viewModel: CockpitViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var selectedUnit by remember { mutableStateOf("Unit 4A (2,250 SqFt)") }
    var unitReserved by remember { mutableStateOf(false) }

    var selectedGarmentSize by remember { mutableStateOf("Size L (Chest 42\")") }
    var arPreviewActive by remember { mutableStateOf(false) }

    val brands = listOf(
        "dom_inno" to "Dom-Inno (Real Estate)",
        "sheltech" to "Sheltech (Real Estate)",
        "artisan_outfitters" to "Artisan Outfitters (E-Com)",
        "gorur_ghash" to "Gorur Ghash (Fashion)"
    )

    val isRealEstate = uiState.selectedSandboxBrandSlug == "dom_inno" || uiState.selectedSandboxBrandSlug == "sheltech"

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
            modifier = Modifier.fillMaxWidth().testTag("sandbox_hero_card"),
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
                            text = "38-SECOND EXPERIENTIAL SHOWROOM",
                            style = MaterialTheme.typography.labelSmall,
                            color = StitchCyan
                        )
                        Text(
                            text = "Live Client Sandbox",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = StitchEmerald.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StitchEmerald)
                    ) {
                        Text(
                            text = "LIVE SIMULATION",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = StitchEmerald
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "This is what prospective high-ticket buyers experience when tapping your WhatsApp demo link (`?demo=${uiState.selectedSandboxBrandSlug}#sandbox`).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Brand Switcher Chips
        Text(
            text = "SELECT WHITE-LABELED CLIENT BRAND:",
            style = MaterialTheme.typography.labelSmall,
            color = SlateTextSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            brands.take(2).forEach { (slug, name) ->
                FilterChip(
                    selected = uiState.selectedSandboxBrandSlug == slug,
                    onClick = { viewModel.setSandboxBrand(slug) },
                    label = { Text(name, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StitchCyan,
                        selectedLabelColor = ObsidianBg
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            brands.drop(2).forEach { (slug, name) ->
                FilterChip(
                    selected = uiState.selectedSandboxBrandSlug == slug,
                    onClick = { viewModel.setSandboxBrand(slug) },
                    label = { Text(name, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StitchCyan,
                        selectedLabelColor = ObsidianBg
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isRealEstate) {
            // Real Estate Interactive Floor Plan Simulator
            Card(
                modifier = Modifier.fillMaxWidth().testTag("re_showroom_card"),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BANANI LAKEFRONT RESIDENCES",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "৳11,000 / SqFt",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StitchCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Floor plan wireframe visual box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(ObsidianSurface2, RoundedCornerShape(12.dp))
                            .border(1.dp, StitchCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Apartment,
                                contentDescription = "Floor Plan",
                                tint = StitchCyan,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "3D Interactive Floor Plan & Lake View Simulation",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                            Text(
                                text = "South-Facing • 4 Bed • 4 Bath • 2 Parking Slots",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlateTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Available Units for Immediate Retainer Hold:",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateTextSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    listOf("Unit 4A (2,250 SqFt)", "Unit 6B (2,450 SqFt)", "Penthouse P1 (4,100 SqFt)").forEach { unit ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedUnit = unit
                                    unitReserved = false
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedUnit == unit,
                                onClick = {
                                    selectedUnit = unit
                                    unitReserved = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = StitchCyan)
                            )
                            Text(
                                text = unit,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (selectedUnit == unit) StitchCyan else SlateTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            unitReserved = true
                            viewModel.showSnackbar("Unit Reserved! WhatsApp VIP hold alert triggered.")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (unitReserved) StitchEmerald else StitchCyan
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("re_reserve_btn")
                    ) {
                        Icon(
                            imageVector = if (unitReserved) Icons.Default.CheckCircle else Icons.Default.Lock,
                            contentDescription = "Hold",
                            tint = ObsidianBg
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (unitReserved) "Unit 4A Locked Under 24h VIP Hold" else "Place 24h VIP Hold on $selectedUnit",
                            fontWeight = FontWeight.Bold,
                            color = ObsidianBg
                        )
                    }
                }
            }
        } else {
            // E-Commerce 3D Product Fitting & AR Preview Simulator
            Card(
                modifier = Modifier.fillMaxWidth().testTag("ecom_showroom_card"),
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
                        Text(
                            text = "PREMIUM HANDCRAFTED PANJABI",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "৳4,850",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NeonRosePastel
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(ObsidianSurface2, RoundedCornerShape(12.dp))
                            .border(1.dp, NeonRose.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Checkroom,
                                contentDescription = "Garment Fitting",
                                tint = NeonRose,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (arPreviewActive) "AR Camera Active: 99.4% Fit Precision" else "3D Augmented Reality Sizing Calibration",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                            Text(
                                text = "Zero RTO Guarantee: Buyer fits virtually before COD dispatch",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlateTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Calibrate Body Sizing Model:",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateTextSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    listOf("Size M (Chest 40\", Length 42\")", "Size L (Chest 42\", Length 44\")", "Size XL (Chest 44\", Length 46\")").forEach { size ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedGarmentSize = size }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedGarmentSize == size,
                                onClick = { selectedGarmentSize = size },
                                colors = RadioButtonDefaults.colors(selectedColor = NeonRose)
                            )
                            Text(
                                text = size,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (selectedGarmentSize == size) NeonRosePastel else SlateTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            arPreviewActive = !arPreviewActive
                            viewModel.showSnackbar(if (arPreviewActive) "3D Sizing calibrated! RTO risk down 70%." else "AR preview stopped")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRose),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("ecom_fit_btn")
                    ) {
                        Icon(imageVector = Icons.Default.ViewInAr, contentDescription = "AR", tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (arPreviewActive) "Reset Fitting Calibration" else "Launch 3D Body Fit Simulator",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
