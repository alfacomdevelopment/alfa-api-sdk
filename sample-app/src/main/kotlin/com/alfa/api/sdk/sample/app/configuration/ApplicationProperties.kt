package com.alfa.api.sdk.sample.app.configuration

import com.alfa.api.sdk.crypto.model.KeyStoreParameters
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "sdk-sample-app")
data class ApplicationProperties(
    var baseUrl: String,
    var tlsProperties: TlsProperties?,
    var accessToken: String,
    var transactions: TransactionsProperties?,
    var signature: SignatureProperties?,
) {
    data class TransactionsProperties(
        var contextPath: String
    )

    data class TlsProperties(
        var trustStorePath: String?,
        val trustStorePassword: String?,
        val keyStorePath: String?,
        val keyStorePassword: String?,
        val privateKeyAlias: String?
    )

    data class SignatureProperties(
        var rsa: KeyStoreParameters?
    )
}