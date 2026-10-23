package com.example.aiscockpit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aiscockpit.ui.CockpitTab
import com.example.aiscockpit.ui.CockpitViewModel
import com.example.aiscockpit.ui.screens.*
import com.example.aiscockpit.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AISCockpitTheme {
                val viewModel: CockpitViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                // Propagate ViewModel snackbars
                LaunchedEffect(uiState.snackbarMessage) {
                    uiState.snackbarMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSnackbar()
                    }
                }

                // BackHandler returns to OUTREACH if on another tab
                BackHandler(enabled = uiState.activeTab != CockpitTab.OUTREACH || uiState.isPitchModeFullscreen) {
                    if (uiState.isPitchModeFullscreen) {
                        viewModel.togglePitchFullscreen()
                    } else {
                        viewModel.selectTab(CockpitTab.OUTREACH)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        if (!uiState.isPitchModeFullscreen) {
                            CockpitTopAppBar(
                                isAdminMode = uiState.isAdminMode,
                                onToggleMode = { viewModel.toggleAdminMode() },
                                activeTab = uiState.activeTab
                            )
                        }
                    },
                    bottomBar = {
                        if (!uiState.isPitchModeFullscreen) {
                            CockpitBottomNavigation(
                                activeTab = uiState.activeTab,
                                onSelectTab = { viewModel.selectTab(it) }
                            )
                        }
                    },
                    floatingActionButton = {
                        if (uiState.activeTab == CockpitTab.OUTREACH && !uiState.isPitchModeFullscreen) {
                            FloatingActionButton(
                                onClick = { viewModel.setShowAddLeadDialog(true) },
                                containerColor = StitchCyan,
                                contentColor = ObsidianBg,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .testTag("fab_add_lead")
                                    .padding(bottom = 16.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Lead")
                            }
                        }
                    },
                    containerColor = ObsidianBg
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.activeTab) {
                            CockpitTab.OUTREACH -> OutreachScreen(viewModel = viewModel)
                            CockpitTab.AUDIT -> AuditCalculatorScreen(viewModel = viewModel)
                            CockpitTab.REVENUE -> RevenueLockScreen(viewModel = viewModel)
                            CockpitTab.MUSHAK -> MushakScreen(viewModel = viewModel)
                            CockpitTab.SANDBOX -> SandboxScreen(viewModel = viewModel)
                            CockpitTab.VAULT -> DocumentVaultScreen(viewModel = viewModel)
                            CockpitTab.GATEWAY -> GatewayScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CockpitTopAppBar(
    isAdminMode: Boolean,
    onToggleMode: () -> Unit,
    activeTab: CockpitTab
) {
    TopAppBar(
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AIS COCKPIT",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = SlateTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = StitchCyan.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StitchCyan.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "ARGON 4.2",
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            color = StitchCyan
                        )
                    }
                }
                Text(
                    text = activeTab.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateTextSecondary,
                    fontSize = 11.sp
                )
            }
        },
        actions = {
            // Mode Toggle: Admin Command vs Client VIP
            Surface(
                onClick = onToggleMode,
                shape = RoundedCornerShape(20.dp),
                color = if (isAdminMode) ObsidianSurface2 else StitchCyan.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isAdminMode) SlateBorder else StitchCyan
                ),
                modifier = Modifier
                    .padding(end = 12.dp)
                    .testTag("mode_toggle_btn")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isAdminMode) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                        contentDescription = "Mode",
                        tint = if (isAdminMode) StitchCyan else NeonRosePastel,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAdminMode) "ADMIN" else "CLIENT VIP",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (isAdminMode) SlateTextPrimary else StitchCyan
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ObsidianSurface1,
            titleContentColor = SlateTextPrimary
        )
    )
}

@Composable
fun CockpitBottomNavigation(
    activeTab: CockpitTab,
    onSelectTab: (CockpitTab) -> Unit
) {
    NavigationBar(
        containerColor = ObsidianSurface1,
        contentColor = SlateTextSecondary,
        tonalElevation = 8.dp
    ) {
        val navItems = listOf(
            Triple(CockpitTab.OUTREACH, Icons.Default.Contacts, "Pipeline"),
            Triple(CockpitTab.AUDIT, Icons.Default.Calculate, "Audit"),
            Triple(CockpitTab.REVENUE, Icons.Default.AccountBalanceWallet, "Revenue"),
            Triple(CockpitTab.MUSHAK, Icons.Default.ReceiptLong, "Mushak"),
            Triple(CockpitTab.SANDBOX, Icons.Default.ViewInAr, "Sandbox"),
            Triple(CockpitTab.VAULT, Icons.Default.Description, "Vault"),
            Triple(CockpitTab.GATEWAY, Icons.Default.Payment, "Gateway")
        )

        navItems.forEach { (tab, icon, label) ->
            NavigationBarItem(
                selected = activeTab == tab,
                onClick = { onSelectTab(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (activeTab == tab) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ObsidianBg,
                    selectedTextColor = StitchCyan,
                    indicatorColor = StitchCyan,
                    unselectedIconColor = SlateTextSecondary,
                    unselectedTextColor = SlateTextSecondary
                ),
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}
