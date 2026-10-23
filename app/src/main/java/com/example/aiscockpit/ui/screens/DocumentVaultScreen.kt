package com.example.aiscockpit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.aiscockpit.model.VaultDocument
import com.example.aiscockpit.ui.CockpitViewModel
import com.example.aiscockpit.ui.theme.*

@Composable
fun DocumentVaultScreen(
    viewModel: CockpitViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val vaultDocs = viewModel.vaultDocs
    val pitchSlides = viewModel.pitchSlides

    if (uiState.isPitchModeFullscreen) {
        // 4-Slide Boardroom Pitch Presentation Mode
        BoardroomPitchDeck(
            slides = pitchSlides,
            activeSlideIndex = uiState.activePitchSlideIndex,
            onSelectSlide = { viewModel.setPitchSlide(it) },
            onClose = { viewModel.togglePitchFullscreen() },
            onAction = {
                viewModel.togglePitchFullscreen()
                viewModel.selectTab(com.example.aiscockpit.ui.CockpitTab.GATEWAY)
            }
        )
    } else {
        // Standard Document Vault View
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(ObsidianBg)
                .padding(16.dp)
        ) {
            // Presentation Trigger Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.togglePitchFullscreen() }
                    .testTag("launch_boardroom_pitch_card"),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonRose.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Slideshow, contentDescription = "Pitch", tint = NeonRose, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "BOARDROOM PITCH MODE",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonRosePastel
                            )
                        }
                        Text(
                            text = "Launch 4-Slide Pitch Deck",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Fullscreen presentation optimized for high-ticket C-level board meetings.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlateTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.togglePitchFullscreen() },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRose),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Present", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dual Mode Toggle: Executive Plain Language vs Statutory Legal Text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ObsidianSurface1, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = { if (!uiState.isExecutivePlainMode) viewModel.toggleExecutivePlainMode() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.isExecutivePlainMode) StitchCyan else Color.Transparent
                    )
                ) {
                    Text(
                        text = "📋 Executive Plain English",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (uiState.isExecutivePlainMode) ObsidianBg else SlateTextSecondary
                    )
                }

                Button(
                    onClick = { if (uiState.isExecutivePlainMode) viewModel.toggleExecutivePlainMode() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!uiState.isExecutivePlainMode) StitchCyan else Color.Transparent
                    )
                ) {
                    Text(
                        text = "⚖️ Statutory Legal Text",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (!uiState.isExecutivePlainMode) ObsidianBg else SlateTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4-in-1 Document Cards List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(vaultDocs, key = { it.docType.code }) { doc ->
                    VaultDocumentCard(
                        doc = doc,
                        isExecutiveMode = uiState.isExecutivePlainMode
                    )
                }
            }
        }
    }
}

@Composable
fun VaultDocumentCard(
    doc: VaultDocument,
    isExecutiveMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    text = doc.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                Text(
                    text = doc.docType.code,
                    style = MaterialTheme.typography.labelSmall,
                    color = StitchCyan
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ObsidianSurface2, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = if (isExecutiveMode) doc.executiveSummary else doc.statutoryLegalText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateTextPrimary,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Key Protections & Statutory Covenants:",
                style = MaterialTheme.typography.labelSmall,
                color = SlateTextSecondary,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            doc.keyGuarantees.forEach { guarantee ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Check", tint = StitchEmerald, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = guarantee,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BoardroomPitchDeck(
    slides: List<com.example.aiscockpit.model.PitchSlide>,
    activeSlideIndex: Int,
    onSelectSlide: (Int) -> Unit,
    onClose: () -> Unit,
    onAction: () -> Unit
) {
    val slide = slides[activeSlideIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(16.dp)
            .testTag("pitch_deck_fullscreen")
    ) {
        // Pitch Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = slide.tag,
                style = MaterialTheme.typography.labelSmall,
                color = NeonRosePastel,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SlateTextPrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Slide Content Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface1),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonRose.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = slide.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = SlateTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = slide.subtitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = StitchCyan
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    slide.keyBulletPoints.forEach { point ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                modifier = Modifier.size(24.dp),
                                shape = RoundedCornerShape(6.dp),
                                color = NeonRose.copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("✓", color = NeonRosePastel, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = point,
                                style = MaterialTheme.typography.bodyLarge,
                                color = SlateTextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }

                // CTA Button
                Button(
                    onClick = {
                        if (activeSlideIndex < slides.size - 1) {
                            onSelectSlide(activeSlideIndex + 1)
                        } else {
                            onAction()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRose)
                ) {
                    Text(
                        text = slide.callToAction,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Slide Pagination Dots & Next/Prev
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { if (activeSlideIndex > 0) onSelectSlide(activeSlideIndex - 1) },
                enabled = activeSlideIndex > 0,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Previous")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                slides.indices.forEach { idx ->
                    Box(
                        modifier = Modifier
                            .size(if (idx == activeSlideIndex) 24.dp else 10.dp, 10.dp)
                            .background(
                                if (idx == activeSlideIndex) NeonRose else ObsidianSurface3,
                                RoundedCornerShape(5.dp)
                            )
                    )
                }
            }

            OutlinedButton(
                onClick = { if (activeSlideIndex < slides.size - 1) onSelectSlide(activeSlideIndex + 1) },
                enabled = activeSlideIndex < slides.size - 1,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Next")
            }
        }
    }
}
