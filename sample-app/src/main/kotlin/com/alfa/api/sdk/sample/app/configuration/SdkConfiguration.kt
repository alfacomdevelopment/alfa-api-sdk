package com.alfa.api.sdk.sample.app.configuration

import com.alfa.api.sdk.client.ApiHttpClient
import com.alfa.api.sdk.client.ApiSyncHttpClient
import com.alfa.api.sdk.client.security.BearerTokenProvider
import com.alfa.api.sdk.client.security.TlsProvider
import com.alfa.api.sdk.crypto.AbstractSignatureService
import com.alfa.api.sdk.crypto.impl.CmsSignatureServiceImpl
import com.alfa.api.sdk.crypto.impl.JwsSignatureServiceImpl
import com.alfa.api.sdk.sample.app.mapper.SslPropertiesMapper
import com.alfa.api.sdk.transactions.TransactionsApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SdkConfiguration(
    private val properties: ApplicationProperties,
    private val sslPropertiesMapper: SslPropertiesMapper
) {
    @Bean
    fun createApiHttpClient(): ApiHttpClient =
        ApiSyncHttpClient(
            properties.baseUrl,
            TlsProvider(sslPropertiesMapper.map(properties.tlsProperties)),
            BearerTokenProvider(properties.accessToken)
        )

    @Bean
    fun createTransactionsApi(apiHttpClient: ApiHttpClient): TransactionsApi =
        TransactionsApi(apiHttpClient, properties.transactions?.contextPath)

    @Bean
    fun createRsaCmsSignatureService() =
        properties.signature?.rsa.let {
            CmsSignatureServiceImpl(
                properties.signature!!.rsa,
                AbstractSignatureService.SignatureAlgorithm.RSA
            )
        }

    @Bean
    fun createRsaJwsSignatureService() =
        properties.signature?.rsa.let {
            JwsSignatureServiceImpl(
                properties.signature!!.rsa,
                AbstractSignatureService.SignatureAlgorithm.RSA
            )
        }

}
