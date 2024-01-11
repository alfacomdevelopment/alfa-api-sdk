package com.alfa.api.sdk.sample.app

import com.github.tomakehurst.wiremock.WireMockServer
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import com.alfa.api.sdk.sample.app.configuration.ApplicationProperties


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractIntegrationTest {
    @Autowired
    protected lateinit var properties: ApplicationProperties
    @Autowired
    protected lateinit var mockMvc: MockMvc
    @AfterEach
    fun afterEach() {
        wiremock.resetAll()
    }

    companion object {
        @JvmStatic
        protected val wiremock = WireMockServer(18089).apply { start() }
    }
}
