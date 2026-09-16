package com.openwps.engines.document

import org.junit.Assert.assertEquals
import org.junit.Test
import com.openwps.office.model.DocumentObjectId
import com.openwps.office.model.DocumentRange
import com.openwps.office.api.OperationResult
import com.openwps.office.api.ErrorCode
import org.json.JSONObject

class DocumentStructureParserTest {

    @Test
    fun testParseOperationResult() {
        // Simulating the result returned from JNI
        val jsonStr = """
        {
            "success": true,
            "errorCode": "NONE",
            "affectedRange": {
                "startObjectId": "par_1",
                "startOffset": 5,
                "endObjectId": "par_1",
                "endOffset": 10
            }
        }
        """.trimIndent()
        
        val obj = JSONObject(jsonStr)
        val success = obj.optBoolean("success", false)
        val errCodeStr = obj.optString("errorCode", "NONE")
        val errorCode = ErrorCode.valueOf(errCodeStr)
        val rangeObj = obj.getJSONObject("affectedRange")
        val affectedRange = DocumentRange(
            startObjectId = DocumentObjectId(rangeObj.getString("startObjectId")),
            startOffset = rangeObj.getInt("startOffset"),
            endObjectId = DocumentObjectId(rangeObj.getString("endObjectId")),
            endOffset = rangeObj.getInt("endOffset")
        )
        
        val result = OperationResult(success, "op_123", affectedRange, errorCode)
        assertEquals(true, result.success)
        assertEquals(ErrorCode.NONE, result.errorCode)
        assertEquals("par_1", result.affectedRange?.startObjectId?.id)
        assertEquals(5, result.affectedRange?.startOffset)
        assertEquals(10, result.affectedRange?.endOffset)
    }


    @Test
    fun testParseParagraphStyle() {
        val jsonStr = """
        {
            "id": "doc_1",
            "version": 1,
            "sections": [
                {
                    "id": "sec_1",
                    "properties": {
                        "orientation": "LANDSCAPE"
                    },
                    "blocks": [
                        {
                            "id": "blk_1",
                            "paragraph": {
                                "id": "par_1",
                                "style": {
                                    "alignment": "center",
                                    "headingLevel": 2,
                                    "isList": true,
                                    "listId": "list1",
                                    "listLevel": 1
                                },
                                "runs": []
                            }
                        }
                    ]
                }
            ]
        }
        """.trimIndent()
        
        val doc = DocumentStructureParser.parseDocument(jsonStr)
        val p = doc.sections.first().blocks.first().paragraph
        assertEquals("center", p?.style?.alignment)
        assertEquals(2, p?.style?.headingLevel)
        assertEquals(true, p?.style?.isList)
        assertEquals("list1", p?.style?.listId)
        assertEquals(1, p?.style?.listLevel)
        assertEquals("LANDSCAPE", doc.sections.first().properties?.orientation)
    }

}
