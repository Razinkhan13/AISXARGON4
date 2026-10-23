package com.example.aiscockpit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiscockpit.data.CockpitRepository
import com.example.aiscockpit.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class CockpitTab(val title: String) {
    OUTREACH("Outreach"),
    AUDIT("Audit"),
    REVENUE("Revenue"),
    MUSHAK("Mushak-6.3"),
    SANDBOX("Sandbox"),
    VAULT("Vault"),
    GATEWAY("Gateway")
}

data class CockpitUiState(
    val activeTab: CockpitTab = CockpitTab.OUTREACH,
    val isAdminMode: Boolean = true,
    val selectedIndustryFilter: LeadCategory? = null,
    val searchQuery: String = "",
    val auditMode: AuditMode = AuditMode.REAL_ESTATE,
    val isExecutivePlainMode: Boolean = true,
    val activePitchSlideIndex: Int = 0,
    val isPitchModeFullscreen: Boolean = false,
    val showAddLeadDialog: Boolean = false,
    val showAddDepositDialog: Boolean = false,
    val showAddMushakDialog: Boolean = false,
    val selectedSandboxBrandSlug: String = "dom_inno",
    val snackbarMessage: String? = null
)

class CockpitViewModel(
    private val repository: CockpitRepository = CockpitRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CockpitUiState())
    val uiState: StateFlow<CockpitUiState> = _uiState.asStateFlow()

    val leads: StateFlow<List<PipelineLead>> = repository.leads
    val realEstateAudit: StateFlow<RealEstateAuditInput> = repository.realEstateAudit
    val ecomAudit: StateFlow<EcomRtoAuditInput> = repository.ecomAudit
    val mushakInvoices: StateFlow<List<MushakInvoice>> = repository.mushakInvoices
    val deposits: StateFlow<List<LockDeposit>> = repository.deposits

    val vaultDocs: List<VaultDocument> = repository.getVaultDocuments()
    val pitchSlides: List<PitchSlide> = repository.getPitchSlides()

    val totalLockedRevenueBDT: StateFlow<Long> = deposits.map { list ->
        list.filter { it.status == DepositStatus.VERIFIED || it.status == DepositStatus.FUNDS_LOCKED }
            .sumOf { it.amountBDT }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1890000L)

    val target90DayBDT: Long = 10000000L // ৳1 Crore Target

    fun selectTab(tab: CockpitTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun toggleAdminMode() {
        _uiState.update { it.copy(isAdminMode = !it.isAdminMode) }
    }

    fun setIndustryFilter(filter: LeadCategory?) {
        _uiState.update { it.copy(selectedIndustryFilter = filter) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setAuditMode(mode: AuditMode) {
        _uiState.update { it.copy(auditMode = mode) }
    }

    fun toggleExecutivePlainMode() {
        _uiState.update { it.copy(isExecutivePlainMode = !it.isExecutivePlainMode) }
    }

    fun setPitchSlide(index: Int) {
        val clamped = index.coerceIn(0, pitchSlides.size - 1)
        _uiState.update { it.copy(activePitchSlideIndex = clamped) }
    }

    fun togglePitchFullscreen() {
        _uiState.update { it.copy(isPitchModeFullscreen = !it.isPitchModeFullscreen) }
    }

    fun setSandboxBrand(slug: String) {
        _uiState.update { it.copy(selectedSandboxBrandSlug = slug) }
    }

    fun setShowAddLeadDialog(show: Boolean) {
        _uiState.update { it.copy(showAddLeadDialog = show) }
    }

    fun setShowAddDepositDialog(show: Boolean) {
        _uiState.update { it.copy(showAddDepositDialog = show) }
    }

    fun setShowAddMushakDialog(show: Boolean) {
        _uiState.update { it.copy(showAddMushakDialog = show) }
    }

    fun showSnackbar(msg: String) {
        _uiState.update { it.copy(snackbarMessage = msg) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun addLead(
        company: String,
        category: LeadCategory,
        phone: String,
        packageName: String,
        dealValueBDT: Long,
        customHook: String
    ) {
        val slug = company.lowercase().replace(" ", "_").replace(Regex("[^a-z0-9_]"), "")
        val lead = PipelineLead(
            id = UUID.randomUUID().toString(),
            slug = slug,
            companyName = company,
            category = category,
            contactPerson = "Executive Director",
            phone = phone,
            packageName = packageName,
            status = LeadStatus.PROSPECT,
            dealValueBDT = dealValueBDT,
            customHook = customHook,
            demoDeepLink = "https://ais-cockpit.local/?demo=$slug#sandbox"
        )
        repository.addLead(lead)
        showSnackbar("Deal added to pipeline: $company")
    }

    fun updateLeadStatus(id: String, status: LeadStatus) {
        repository.updateLeadStatus(id, status)
        showSnackbar("Lead updated to ${status.displayName}")
    }

    fun updateRealEstateAudit(
        units: Int,
        sqft: Double,
        priceSqft: Double,
        cancelled: Int,
        stalled: Int,
        bankOdCr: Double
    ) {
        val current = realEstateAudit.value
        repository.updateRealEstateAudit(
            current.copy(
                units = units,
                sqftPerUnit = sqft,
                pricePerSqft = priceSqft,
                cancelledUnits = cancelled,
                stalledUnits = stalled,
                bankOdCrore = bankOdCr
            )
        )
        showSnackbar("Real estate capital bleed recalculated")
    }

    fun updateEcomAudit(
        orders: Int,
        aov: Double,
        rtoRate: Double,
        courierFee: Double
    ) {
        val current = ecomAudit.value
        repository.updateEcomAudit(
            current.copy(
                monthlyOrders = orders,
                averageOrderValueBDT = aov,
                returnToOriginPercent = rtoRate,
                courierReverseFeeBDT = courierFee
            )
        )
        showSnackbar("E-Com RTO hemorrhage recalculated")
    }

    fun addMushakInvoice(client: String, bin: String, category: String, amountBDT: Long) {
        val nextNum = 1000 + (mushakInvoices.value.size + 1)
        val invoice = MushakInvoice(
            id = UUID.randomUUID().toString(),
            serialNo = "2026-2027-DHK-$nextNum",
            clientName = client,
            clientBin = if (bin.isBlank()) "000984122-0101" else bin,
            serviceDescription = category,
            packageName = category,
            baseAmountBDT = amountBDT,
            status = MushakStatus.ISSUED,
            settlementRef = "PENDING_WIRE"
        )
        repository.addMushakInvoice(invoice)
        showSnackbar("Issued Form Mushak-6.3 Serial: ${invoice.serialNo}")
    }

    fun markMushakSettled(id: String) {
        repository.updateMushakStatus(id, MushakStatus.SETTLED)
        showSnackbar("Mushak-6.3 marked SETTLED with tax stamp")
    }

    fun addDeposit(client: String, amountBDT: Long, channel: DepositChannel, ref: String) {
        val deposit = LockDeposit(
            id = UUID.randomUUID().toString(),
            clientName = client,
            packageTier = "Executive Retainer Lock",
            amountBDT = amountBDT,
            channel = channel,
            referenceNumber = if (ref.isBlank()) "WIRE-TXN-${System.currentTimeMillis() % 100000}" else ref,
            status = DepositStatus.FUNDS_LOCKED,
            timestamp = "Just Now"
        )
        repository.addDeposit(deposit)
        showSnackbar("Locked deposit logged: ৳$amountBDT from $client")
    }

    fun verifyDeposit(id: String) {
        repository.updateDepositStatus(id, DepositStatus.VERIFIED)
        showSnackbar("Deposit cleared and verified by Treasury")
    }
}
