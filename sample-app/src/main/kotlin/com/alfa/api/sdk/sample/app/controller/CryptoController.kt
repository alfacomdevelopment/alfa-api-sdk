package com.alfa.api.sdk.sample.app.controller

import com.alfa.api.sdk.crypto.CmsSignatureService
import com.alfa.api.sdk.crypto.JwsSignatureService
import com.alfa.api.sdk.crypto.XmlSignatureService
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sdk/crypto")
class CryptoController(
    private val cmsSignatureService: CmsSignatureService,
    private val jwsSignatureService: JwsSignatureService,
    private val xmlSignatureService: XmlSignatureService
) {
    @PostMapping("/sign/rsa/cms", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun signRsaCms(@RequestBody data: ByteArray, @RequestParam attached: Boolean): ByteArray {
        return if (attached) {
            Base64.encodeBase64(cmsSignatureService.signAttached(data), false)
        } else {
            Base64.encodeBase64(cmsSignatureService.signDetached(data), false)
        }
    }

    @PostMapping("/verify/rsa/detached/cms", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun verifyRsaDetachedCms(@RequestBody data: ByteArray, @RequestHeader signature: String): Boolean {
        return cmsSignatureService.verifyDetached(data, Base64.decodeBase64(signature))
    }

    @PostMapping("/verify/rsa/attached/cms", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun verifyRsaAttachedCms(@RequestBody signatureWithFileBase64: String): Boolean {
        return cmsSignatureService.verifyAttached(Base64.decodeBase64(signatureWithFileBase64))
    }

    @PostMapping("/sign/rsa/jws", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun signRsaJws(@RequestBody data: String, @RequestParam attached: Boolean): String {
        return if (attached) {
            jwsSignatureService.signAttached(data)
        } else {
            jwsSignatureService.signDetached(data)
        }
    }

    @PostMapping("/verify/rsa/attached/jws", consumes = ["application/jose"])
    fun verifyRsaAttachedJws(@RequestBody jwsWithData: String): Boolean {
        return jwsSignatureService.verifyAttached(jwsWithData)
    }

    @PostMapping("/verify/rsa/detached/jws", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun verifyRsaDetachedJws(@RequestBody data: String, @RequestHeader jwsWithoutData: String): Boolean {
        return jwsSignatureService.verifyDetached(data, jwsWithoutData)
    }

    @PostMapping("/sign/rsa/xml", consumes = [MediaType.APPLICATION_XML_VALUE])
    fun signRsaXml(@RequestBody data: String): String {
        return xmlSignatureService.sign(data)

    }

    @PostMapping("/verify/rsa/xml", consumes = [MediaType.APPLICATION_XML_VALUE])
    fun verifyRsaAttachedXml(@RequestBody jwsWithData: String): Boolean {
        return xmlSignatureService.verify(jwsWithData)
    }
}