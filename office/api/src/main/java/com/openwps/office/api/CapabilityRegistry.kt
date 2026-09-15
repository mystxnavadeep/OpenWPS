package com.openwps.office.api
interface CapabilityRegistry {
    fun getCapabilities(): List<DocumentCapability>
    fun supports(capabilityId: String): Boolean
}
