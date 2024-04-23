package com.alfa.api.sdk.sample.app

import com.alfa.api.sdk.sample.app.configuration.ApplicationProperties
import com.github.tomakehurst.wiremock.WireMockServer
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import java.io.FileOutputStream
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Security
import java.security.cert.X509Certificate
import java.util.*


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

        @JvmStatic
        @DynamicPropertySource
        fun createTestKeystore(registry: DynamicPropertyRegistry) {
            Security.addProvider(BouncyCastleProvider())
            val keyPair = generateKeyPair()
            val certificate = createCertificate(keyPair)

            registry.add("sdk-sample-app.signature.rsa.path") {
                addToPKCS12Keystore(keyPair.private, certificate).toString()
            }
        }

        private fun addToPKCS12Keystore(privateKey: PrivateKey, certificate: X509Certificate): Path {
            val tempDir = Files.createTempDirectory("keystore")
            val keystorePath = tempDir.resolve("keystore.p12")

            val keyStore = KeyStore.getInstance("PKCS12", "BC")
            keyStore.load(null, "testpass".toCharArray())

            val chain = arrayOf<X509Certificate>(certificate)
            keyStore.setKeyEntry("test", privateKey, "testpass".toCharArray(), chain)

            FileOutputStream(keystorePath.toFile()).use {
                keyStore.store(it, "testpass".toCharArray())
            }
            return keystorePath
        }

        private fun createCertificate(keyPair: KeyPair): X509Certificate {
            val startDate = Date()
            val endDate = Date(startDate.time + 365L * 24 * 60 * 60 * 1000) // 1 year
            val certBuilder = JcaX509v3CertificateBuilder(
                X500Name("CN=Test"),
                BigInteger.valueOf(System.currentTimeMillis()),
                startDate,
                endDate,
                X500Name("CN=Test"),
                keyPair.public
            )
            val signer = JcaContentSignerBuilder("SHA256withRSA")
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .build(keyPair.private)
            val certHolder = certBuilder.build(signer)
            val certificate = JcaX509CertificateConverter().getCertificate(certHolder)
            return certificate
        }

        private fun generateKeyPair(): KeyPair {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
            keyPairGenerator.initialize(2048)
            val keyPair = keyPairGenerator.generateKeyPair()
            return keyPair
        }
    }
}
