package com.alfa.api.sdk.sample.app.signature

import com.alfa.api.sdk.client.ApiHttpClient
import com.alfa.api.sdk.client.dto.ApiResponse
import com.alfa.api.sdk.client.dto.Method
import com.alfa.api.sdk.signature.SignatureApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class SignatureApiTest {
    @Test
    fun getSignMethodsPassesOptionalChannelAndParsesResponse() {
        var requestMethod: Method? = null
        var requestPath: String? = null
        var requestQuery: Map<String, String>? = null
        val client = ApiHttpClient { method, path, queryParams, _, _ ->
            requestMethod = method
            requestPath = path
            requestQuery = queryParams
            ApiResponse().apply {
                statusCode = 200
                response = """[{"channel":"BAAS","signMethods":[]}]""".toByteArray()
            }
        }
        val api = SignatureApi(client)

        val methods = api.getSignMethods("BAAS")

        assertEquals(Method.GET, requestMethod)
        assertEquals("/api/jp/v3/signature/sign-methods", requestPath)
        assertEquals(mapOf("channel" to "BAAS"), requestQuery)
        assertEquals("BAAS", methods.single().channel)

        api.getSignMethods()

        assertNull(requestQuery)
    }
}
