package com.openwps.engines.document

import com.openwps.office.api.CapabilityRegistry
import com.openwps.office.api.DocumentCapability

class NativeCapabilityRegistry : CapabilityRegistry {
    private val capabilities = listOf(
        DocumentCapability("document.text.character.read", "Can read text characters", true),
        DocumentCapability("document.text.character.edit", "Can edit text characters (insert/delete)", true),
        DocumentCapability("document.text.word.resolve", "Can resolve word indexes to stable ranges", true),
        DocumentCapability("document.text.sentence.resolve", "Can resolve sentence indexes to stable ranges", true),
        DocumentCapability("document.text.punctuation.resolve", "Can resolve punctuation characters", true),
        DocumentCapability("document.text.search", "Can search text deterministically", true),
        DocumentCapability("document.text.insert", "Can insert text at specific ranges", true),
        DocumentCapability("document.text.delete", "Can delete specific text ranges", true),
        DocumentCapability("document.text.replace", "Can replace specific text ranges", false), // Could be combo of delete/insert
        DocumentCapability("document.text.style", "Can apply formatting to character ranges", true),
        DocumentCapability("document.structure.read", "Can read the structured document model", true)
,
        DocumentCapability("document.text.strikethrough", "Support for strikethrough text styling", true),
        DocumentCapability("document.paragraph.style", "Support for paragraph alignment, heading level, spacing, etc.", true),
        DocumentCapability("document.section.layout", "Support for section properties like orientation", true),
        DocumentCapability("document.outline.read", "Support for getting document outline structured by headings", true)
    )

    override fun getCapabilities(): List<DocumentCapability> = capabilities

    override fun supports(capabilityId: String): Boolean {
        return capabilities.find { it.id == capabilityId }?.isSupported ?: false
    }
}
