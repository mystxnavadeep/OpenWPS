package com.openwps.engines.document

import com.openwps.office.api.CapabilityRegistry
import com.openwps.office.api.DocumentCapability

class NativeCapabilityRegistry : CapabilityRegistry {
    private val capabilities = listOf(
        DocumentCapability("document.text.read", "Can read raw text from the document", true),
        DocumentCapability("document.text.insert", "Can insert basic text into the document", true),
        DocumentCapability("document.text.delete", "Can delete text ranges", false), // Placeholder
        DocumentCapability("document.text.style", "Can apply basic formatting", false)
    )

    override fun getCapabilities(): List<DocumentCapability> = capabilities

    override fun supports(capabilityId: String): Boolean {
        return capabilities.find { it.id == capabilityId }?.isSupported ?: false
    }
}
