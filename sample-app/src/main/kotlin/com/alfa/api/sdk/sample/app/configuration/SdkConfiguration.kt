package com.alfa.api.sdk.sample.app.configuration

import com.alfa.api.sdk.client.ApiHttpClient
import com.alfa.api.sdk.client.ApiSyncHttpClient
import com.alfa.api.sdk.client.security.ApiKeyProvider
import com.alfa.api.sdk.client.security.CredentialProvider
import com.alfa.api.sdk.client.security.BearerTokenProvider
import com.alfa.api.sdk.client.security.TlsProvider
import com.alfa.api.sdk.crypto.AbstractSignatureService
import com.alfa.api.sdk.crypto.impl.CmsSignatureServiceImpl
import com.alfa.api.sdk.crypto.impl.JwsSignatureServiceImpl
import com.alfa.api.sdk.crypto.impl.XmlSignatureServiceImpl
import com.alfa.api.sdk.customer.info.CustomerInfoApi
import com.alfa.api.sdk.sample.app.mapper.SignaturePropertiesMapper
import com.alfa.api.sdk.sample.app.mapper.SslPropertiesMapper
import com.alfa.api.sdk.transactions.TransactionsApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SdkConfiguration(
    private val properties: ApplicationProperties,
    private val sslPropertiesMapper: SslPropertiesMapper,
    private val signaturePropertiesMapper: SignaturePropertiesMapper
) {
    @Bean
    fun createApiHttpClient(): ApiHttpClient =
        ApiSyncHttpClient(
            properties.baseUrl,
            TlsProvider(sslPropertiesMapper.map(properties.tlsProperties)),
            createCredentialProvider()
        )

    private fun createCredentialProvider(): CredentialProvider =
        when (properties.authType) {
            ApplicationProperties.AuthType.BEARER ->
                BearerTokenProvider(requireNotNull(properties.accessToken) { "sdk-sample-app.access-token is required" })

            ApplicationProperties.AuthType.API_KEY ->
                ApiKeyProvider(requireNotNull(properties.apiKey) { "sdk-sample-app.api-key is required" })
        }

    @Bean
    fun createTransactionsApi(apiHttpClient: ApiHttpClient): TransactionsApi =
        TransactionsApi(apiHttpClient, properties.transactions?.contextPath)

    @Bean
    fun createCustomerInfoApi(apiHttpClient: ApiHttpClient): CustomerInfoApi =
        CustomerInfoApi(apiHttpClient)

    @Bean
    fun createRsaCmsSignatureService() =
        CmsSignatureServiceImpl(
            signaturePropertiesMapper.map(properties.signature.rsa),
            AbstractSignatureService.SignatureAlgorithm.RSA
        )


    @Bean
    fun createRsaJwsSignatureService() =
        JwsSignatureServiceImpl(
            signaturePropertiesMapper.map(properties.signature.rsa),
            AbstractSignatureService.SignatureAlgorithm.RSA
        )

    @Bean
    fun createRsaXmlSignatureService() =
        XmlSignatureServiceImpl(
            signaturePropertiesMapper.map(properties.signature.rsa),
            AbstractSignatureService.SignatureAlgorithm.RSA
        )
}
