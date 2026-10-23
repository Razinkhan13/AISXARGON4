package com.example.aiscockpit.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
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
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                            text = if (isRealEstate) "Interactive Architectural Simulator" else "3D Precision Fit & RTO Eliminator",
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
                            text = "LIVE ENGINE",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = StitchEmerald
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A fully interactive client-facing preview. Try changing dimensions, floor plans, sizing calibration, and payment financing to see real-time dynamic pricing and booking locks.",
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
            RealEstateInteractiveEngine(
                brandSlug = uiState.selectedSandboxBrandSlug,
                onShareBooking = { message ->
                    shareText(context, "VIP Booking Proposal", message)
                    viewModel.showSnackbar("VIP Booking Proposal copied to clipboard")
                }
            )
        } else {
            EcomInteractiveSizingEngine(
                brandSlug = uiState.selectedSandboxBrandSlug,
                onOrderSubmit = { message ->
                    shareText(context, "Pre-Paid VIP Order", message)
                    viewModel.showSnackbar("Pre-Paid Order details ready for WhatsApp dispatch")
                }
            )
        }
    }
}

@Composable
fun RealEstateInteractiveEngine(
    brandSlug: String,
    onShareBooking: (String) -> Unit
) {
    val isDomInno = brandSlug == "dom_inno"
    val projectName = if (isDomInno) "Dom-Inno Lake Vista Residence (Banani)" else "Sheltech Crown Tower (Gulshan 2)"

    // State
    var selectedUnitType by remember { mutableStateOf("Type-A (2,450 SqFt)") }
    var selectedFloor by remember { mutableStateOf(6) }
    var parkingSlots by remember { mutableStateOf(2) }
    var downPaymentPercent by remember { mutableStateOf(20f) }
    var tenureMonths by remember { mutableStateOf(36) }
    var selectedRoom by remember { mutableStateOf("Master Suite") }

    // Math
    val basePricePerSqft = if (isDomInno) 11500.0 else 14500.0
    val sqft = when (selectedUnitType) {
        "Type-A (2,450 SqFt)" -> 2450.0
        "Type-B (2,150 SqFt)" -> 2150.0
        else -> 3800.0 // Penthouse
    }
    val floorPremium = (selectedFloor - 1) * 150.0 * sqft
    val parkingCost = parkingSlots * 750000.0
    val baseApartmentCost = sqft * basePricePerSqft
    val totalCost = baseApartmentCost + floorPremium + parkingCost

    val downPaymentBDT = totalCost * (downPaymentPercent / 100.0)
    val remainingBalance = totalCost - downPaymentBDT
    val monthlyEMI = remainingBalance / tenureMonths

    // Statutory Registration & Stamp Duty (approx 8.5% total in Bangladesh)
    val statutoryRegistrationCost = totalCost * 0.085

    Card(
        modifier = Modifier.fillMaxWidth().testTag("re_showroom_card"),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StitchCyan.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = projectName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = "South-Facing Lakefront View • RAJUK Approved",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StitchCyan,
                        fontSize = 11.sp
                    )
                }
                Text(
                    text = "৳${formatNumber(totalCost.toLong())}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = StitchCyan
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Unit Selector
            Text("Select Unit Configuration:", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Type-A (2,450 SqFt)", "Type-B (2,150 SqFt)", "Penthouse (3,800 SqFt)").forEach { type ->
                    FilterChip(
                        selected = selectedUnitType == type,
                        onClick = { selectedUnitType = type },
                        label = { Text(type.split(" ")[0], fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StitchCyan,
                            selectedLabelColor = ObsidianBg
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Room Plan Visualizer Canvas
            Text("Interactive Blueprint Zones (Tap Room to Inspect):", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .background(ObsidianSurface2, RoundedCornerShape(12.dp))
                    .border(1.dp, StitchCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Blueprint grid
                    drawRect(color = Color(0xFF0F172A), size = size)

                    // Master Suite Box
                    val isMaster = selectedRoom == "Master Suite"
                    drawRect(
                        color = if (isMaster) Color(0x5538BDF8) else Color(0x2238BDF8),
                        topLeft = Offset(10f, 10f),
                        size = Size(w * 0.55f - 20f, h * 0.55f - 20f)
                    )
                    drawRect(
                        color = if (isMaster) Color(0xFF38BDF8) else Color(0x6638BDF8),
                        topLeft = Offset(10f, 10f),
                        size = Size(w * 0.55f - 20f, h * 0.55f - 20f),
                        style = Stroke(width = if (isMaster) 3f else 1f)
                    )

                    // Lakefront Balcony
                    val isBalcony = selectedRoom == "Lake Balcony"
                    drawRect(
                        color = if (isBalcony) Color(0x5510B981) else Color(0x2210B981),
                        topLeft = Offset(10f, h * 0.55f),
                        size = Size(w * 0.55f - 20f, h * 0.40f)
                    )
                    drawRect(
                        color = if (isBalcony) Color(0xFF10B981) else Color(0x6610B981),
                        topLeft = Offset(10f, h * 0.55f),
                        size = Size(w * 0.55f - 20f, h * 0.40f),
                        style = Stroke(width = if (isBalcony) 3f else 1f)
                    )

                    // Drawing & Dining Box
                    val isDrawing = selectedRoom == "Drawing & Dining"
                    drawRect(
                        color = if (isDrawing) Color(0x55FBBF24) else Color(0x22FBBF24),
                        topLeft = Offset(w * 0.55f, 10f),
                        size = Size(w * 0.45f - 10f, h * 0.90f)
                    )
                    drawRect(
                        color = if (isDrawing) Color(0xFFFBBF24) else Color(0x66FBBF24),
                        topLeft = Offset(w * 0.55f, 10f),
                        size = Size(w * 0.45f - 10f, h * 0.90f),
                        style = Stroke(width = if (isDrawing) 3f else 1f)
                    )
                }

                // Room Selector Overlay Buttons
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Surface(
                            onClick = { selectedRoom = "Master Suite" },
                            color = if (selectedRoom == "Master Suite") StitchCyan else ObsidianSurface1,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Text("Master Suite (580 sqft)", modifier = Modifier.padding(6.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (selectedRoom == "Master Suite") ObsidianBg else SlateTextPrimary)
                        }
                        Surface(
                            onClick = { selectedRoom = "Drawing & Dining" },
                            color = if (selectedRoom == "Drawing & Dining") NeonAmber else ObsidianSurface1,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Text("Drawing & Dining (680 sqft)", modifier = Modifier.padding(6.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (selectedRoom == "Drawing & Dining") ObsidianBg else SlateTextPrimary)
                        }
                    }

                    Surface(
                        onClick = { selectedRoom = "Lake Balcony" },
                        color = if (selectedRoom == "Lake Balcony") StitchEmerald else ObsidianSurface1,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Text("Lakefront Balcony (190 sqft • South Wind)", modifier = Modifier.padding(6.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (selectedRoom == "Lake Balcony") ObsidianBg else SlateTextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dynamic Financing & Payment Schedule
            Text("FINANCING & RETAINER CALCULATOR", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Down Payment (${downPaymentPercent.toInt()}%):", style = MaterialTheme.typography.bodyMedium, color = SlateTextSecondary)
                Text("৳${formatNumber(downPaymentBDT.toLong())}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = StitchCyan)
            }
            Slider(
                value = downPaymentPercent,
                onValueChange = { downPaymentPercent = it },
                valueRange = 10f..50f,
                steps = 7,
                colors = SliderDefaults.colors(thumbColor = StitchCyan, activeTrackColor = StitchCyan)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Payment Tenure (${tenureMonths} Months):", style = MaterialTheme.typography.bodyMedium, color = SlateTextSecondary)
                Text("৳${formatNumber(monthlyEMI.toLong())} / Month", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = StitchEmerald)
            }
            Slider(
                value = tenureMonths.toFloat(),
                onValueChange = { tenureMonths = it.toInt() },
                valueRange = 12f..60f,
                steps = 3,
                colors = SliderDefaults.colors(thumbColor = StitchEmerald, activeTrackColor = StitchEmerald)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Statutory Breakdown Info Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ObsidianSurface2, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Govt. Registration & Stamp (8.5%):", fontSize = 11.sp, color = SlateTextSecondary)
                        Text("৳${formatNumber(statutoryRegistrationCost.toLong())}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Initial Booking Token (refundable 14d):", fontSize = 11.sp, color = SlateTextSecondary)
                        Text("৳5,00,000", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StitchCyan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    val memo = """
                        *VIP APARTMENT BOOKING PROPOSAL*
                        Project: $projectName
                        Configuration: $selectedUnitType (Floor: $selectedFloor, $parkingSlots Parking)
                        Total Investment: ৳${formatNumber(totalCost.toLong())}
                        Down Payment (${downPaymentPercent.toInt()}%): ৳${formatNumber(downPaymentBDT.toLong())}
                        Installment: ৳${formatNumber(monthlyEMI.toLong())}/month x $tenureMonths months
                        Estimated Govt. Registration: ৳${formatNumber(statutoryRegistrationCost.toLong())}
                        
                        Initiated via AIS 38-Second Showroom Terminal.
                    """.trimIndent()
                    onShareBooking(memo)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = StitchCyan),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Booking", tint = ObsidianBg)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Generate Official Booking Proposal", color = ObsidianBg, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EcomInteractiveSizingEngine(
    brandSlug: String,
    onOrderSubmit: (String) -> Unit
) {
    val isArtisan = brandSlug == "artisan_outfitters"
    val brandName = if (isArtisan) "Artisan Outfitters (Retail BD)" else "Gorur Ghash Apparel"

    // Inputs
    var heightFeet by remember { mutableStateOf(5.8f) } // 5'8"
    var weightKg by remember { mutableStateOf(72f) }
    var fitStyle by remember { mutableStateOf("Classic Regular") }
    var quantity by remember { mutableStateOf(1) }
    var promoCode by remember { mutableStateOf("AISVIP10") }
    var promoApplied by remember { mutableStateOf(true) }
    var paymentMethod by remember { mutableStateOf("bKash Prepayment") }

    // Recommendation Algorithm
    val recommendedSize = remember(heightFeet, weightKg, fitStyle) {
        when {
            weightKg < 62 -> if (fitStyle == "Slim Fit") "Size S (38)" else "Size M (40)"
            weightKg in 62f..76f -> if (fitStyle == "Relaxed Comfort") "Size L (42)" else "Size M (40)"
            weightKg in 76f..88f -> if (fitStyle == "Slim Fit") "Size L (42)" else "Size XL (44)"
            else -> "Size XXL (46)"
        }
    }

    val fitConfidencePercent = remember(weightKg, heightFeet) {
        (97.2 + ((weightKg.toInt() % 3) * 0.9)).coerceIn(96.0, 99.8)
    }

    // Pricing & RTO Math
    val basePrice = if (isArtisan) 4850.0 else 3250.0
    val rawSubtotal = basePrice * quantity
    val discount = if (promoApplied) rawSubtotal * 0.10 else 0.0
    val deliveryFee = if (paymentMethod == "bKash Prepayment") 0.0 else 80.0
    val finalTotal = rawSubtotal - discount + deliveryFee

    // RTO risk drops from 24% (standard COD) to 3.8% with verified sizing calibration
    val baselineRtoRisk = if (paymentMethod == "Cash on Delivery") 24.5 else 4.2
    val protectedSavings = 340.0 * quantity // packaging + return courier fee

    Card(
        modifier = Modifier.fillMaxWidth().testTag("ecom_showroom_card"),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonRose.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = brandName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = "Handcrafted Egyptian Giza Cotton Panjabi",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NeonRosePastel,
                        fontSize = 11.sp
                    )
                }
                Text(
                    text = "৳${formatNumber(finalTotal.toLong())}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = NeonRosePastel
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Body Measurement Sliders
            Text("AI BODY MEASUREMENT & CALIBRATION", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val ft = heightFeet.toInt()
                val inch = ((heightFeet - ft) * 10).toInt()
                Text("Your Height: ${ft}'${inch}\"", style = MaterialTheme.typography.bodyMedium, color = SlateTextSecondary)
                Text("Body Weight: ${weightKg.toInt()} kg", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
            }

            Slider(
                value = weightKg,
                onValueChange = { weightKg = it },
                valueRange = 50f..105f,
                steps = 11,
                colors = SliderDefaults.colors(thumbColor = NeonRose, activeTrackColor = NeonRose)
            )

            // Fit Preference Chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Slim Fit", "Classic Regular", "Relaxed Comfort").forEach { style ->
                    FilterChip(
                        selected = fitStyle == style,
                        onClick = { fitStyle = style },
                        label = { Text(style, fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonRose,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Precision Match Result Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ObsidianSurface2, RoundedCornerShape(10.dp))
                    .border(1.dp, StitchEmerald.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "CALIBRATED SIZE MATCH", style = MaterialTheme.typography.labelSmall, color = StitchEmerald)
                        Text(text = recommendedSize, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = SlateTextPrimary)
                        Text(text = "${String.format("%.1f", fitConfidencePercent)}% Body Geometry Alignment", style = MaterialTheme.typography.bodyMedium, color = SlateTextSecondary, fontSize = 11.sp)
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StitchEmerald.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "RTO RISK: ${baselineRtoRisk}%",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = StitchEmerald
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Payment & VIP Discount
            Text("CHECKOUT & RETENTION SAFEGUARDS", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { paymentMethod = "bKash Prepayment" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (paymentMethod == "bKash Prepayment") StitchEmeraldDark else ObsidianSurface2
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("bKash (Free Shipping)", fontSize = 11.sp, color = Color.White)
                }

                Button(
                    onClick = { paymentMethod = "Cash on Delivery" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (paymentMethod == "Cash on Delivery") NeonRose else ObsidianSurface2
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cash on Delivery", fontSize = 11.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    val orderMemo = """
                        *PRE-CALIBRATED VIP ORDER CONFIRMATION*
                        Brand: $brandName
                        Item: Handcrafted Egyptian Cotton Panjabi
                        Recommended Size: $recommendedSize (${String.format("%.1f", fitConfidencePercent)}% Match)
                        Fit Style: $fitStyle
                        Payment: $paymentMethod
                        Net Total: ৳${formatNumber(finalTotal.toLong())} (Saved ৳${formatNumber(protectedSavings.toLong())} in RTO risk)
                        
                        Processed through AIS Experiential Sizing Engine.
                    """.trimIndent()
                    onOrderSubmit(orderMemo)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonRose),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.ShoppingCartCheckout, contentDescription = "Order", tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Confirm Calibrated VIP Booking", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun shareText(context: Context, subject: String, body: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(subject, body))

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(Intent.createChooser(shareIntent, subject).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    } catch (_: Exception) {
        // Fallback: primary clip is already set
    }
}
