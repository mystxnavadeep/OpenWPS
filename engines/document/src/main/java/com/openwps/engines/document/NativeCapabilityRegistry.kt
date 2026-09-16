package com.openwps.engines.document

import com.openwps.office.api.CapabilityRegistry
import com.openwps.office.api.DocumentCapability

class NativeCapabilityRegistry : CapabilityRegistry {
    private val capabilities = listOf(
        DocumentCapability("document.text.read", "Can read raw text from the document", true),
        DocumentCapability("document.text.insert", "Can insert basic text into the document", true),
        DocumentCapability("document.text.delete", "Can delete text ranges", true),
        DocumentCapability("document.text.style", "Can apply basic formatting", true),
        DocumentCapability("document.structure.read", "Can read the structured document model", true),
        DocumentCapability("document.paragraph.edit", "Can mutate paragraph structure", false),
        DocumentCapability("document.range.resolve", "Can resolve text positions to stable ranges", false)
    )

    override fun getCapabilities(): List<DocumentCapability> = capabilities

    override fun supports(capabilityId: String): Boolean {
        return capabilities.find { it.id == capabilityId }?.isSupported ?: false
    }
}
