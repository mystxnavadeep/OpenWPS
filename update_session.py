with open("office/api/src/main/java/com/openwps/office/api/DocumentSession.kt", "r") as f:
    content = f.read()

if "getDocumentOutline" not in content:
    content = content.replace("suspend fun search(query: String, matchCase: Boolean): List<DocumentRange>", 
    """suspend fun search(query: String, matchCase: Boolean): List<DocumentRange>
    suspend fun getDocumentOutline(): List<com.openwps.office.model.OutlineNode>""")
    
    with open("office/api/src/main/java/com/openwps/office/api/DocumentSession.kt", "w") as f:
        f.write(content)
