import re
with open("office/api/src/main/java/com/openwps/office/api/command/DocumentCommand.kt", "r") as f:
    content = f.read()

content = content.replace("import com.openwps.office.model.ParagraphStyle", 
"""import com.openwps.office.model.ParagraphStyle
import com.openwps.office.model.SectionProperties""")

if "SetSectionProperties" not in content:
    content = content.replace("}", """    data class SetSectionProperties(val targetSectionId: String, val properties: SectionProperties) : DocumentCommand
}""")

with open("office/api/src/main/java/com/openwps/office/api/command/DocumentCommand.kt", "w") as f:
    f.write(content)
