with open("engines/document/src/main/java/com/openwps/engines/document/NativeCapabilityRegistry.kt", "r") as f:
    content = f.read()

new_caps = """,
        DocumentCapability("document.text.strikethrough", "Support for strikethrough text styling", true),
        DocumentCapability("document.paragraph.style", "Support for paragraph alignment, heading level, spacing, etc.", true),
        DocumentCapability("document.section.layout", "Support for section properties like orientation", true),
        DocumentCapability("document.outline.read", "Support for getting document outline structured by headings", true)"""

content = content.replace("    )", new_caps + "\n    )")

with open("engines/document/src/main/java/com/openwps/engines/document/NativeCapabilityRegistry.kt", "w") as f:
    f.write(content)
