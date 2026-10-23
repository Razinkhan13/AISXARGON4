package com.example.aiscockpit.model

enum class LeadCategory(val displayName: String) {
    REAL_ESTATE("Real Estate"),
    ECOM("E-Commerce / D2C")
}

enum class LeadStatus(val displayName: String) {
    PROSPECT("Prospect"),
    PITCH_SENT("Pitch Sent"),
    DEMO_COMPLETED("Demo Completed"),
    LOCKED_WIRE("Funds Locked"),
    MUSHAK_ISSUED("Mushak Issued")
}

data class PipelineLead(
    val id: String,
    val slug: String,
    val companyName: String,
    val category: LeadCategory,
    val contactPerson: String,
    val phone: String,
    val packageName: String,
    val status: LeadStatus,
    val dealValueBDT: Long,
    val customHook: String,
    val demoDeepLink: String,
    val createdAt: String = "Today"
)
