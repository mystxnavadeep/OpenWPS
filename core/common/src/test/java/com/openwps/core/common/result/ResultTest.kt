package com.openwps.core.common.result

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {
    @Test
    fun testSuccessMap() {
        val success = Result.Success(5)
        val mapped = success.map { it * 2 }
        assertTrue(mapped is Result.Success)
        assertEquals(10, (mapped as Result.Success).data)
    }

    @Test
    fun testErrorMap() {
        val error = Result.Error(AppError.Unknown("test"))
        val mapped = error.map { "won't run" }
        assertTrue(mapped is Result.Error)
        assertEquals("test", (mapped as Result.Error).error.message)
    }
}
