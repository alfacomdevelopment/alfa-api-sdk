package com.alfa.api.sdk.sample.app.controller

import com.alfa.api.sdk.signature.SignatureApi
import com.alfa.api.sdk.signature.generated.model.EnableQesResponse
import com.alfa.api.sdk.signature.generated.model.GetPoaStatus
import com.alfa.api.sdk.signature.generated.model.RegisterPoaRequest
import com.alfa.api.sdk.signature.generated.model.RegisterPoaResponse
import com.alfa.api.sdk.signature.generated.model.RequestData
import com.alfa.api.sdk.signature.generated.model.RequestIssueRSACertificate
import com.alfa.api.sdk.signature.generated.model.RequestPostOperations
import com.alfa.api.sdk.signature.generated.model.RequestPutOperations
import com.alfa.api.sdk.signature.generated.model.RequestReissueRSACertificate
import com.alfa.api.sdk.signature.generated.model.ResponseDN
import com.alfa.api.sdk.signature.generated.model.ResponsePostOperations
import com.alfa.api.sdk.signature.generated.model.ResponsePutOperations
import com.alfa.api.sdk.signature.generated.model.ResponseRsaCertificate
import com.alfa.api.sdk.signature.generated.model.RSACertificate
import com.alfa.api.sdk.signature.generated.model.RsaCertificates
import com.alfa.api.sdk.signature.generated.model.SendRequestQes
import com.alfa.api.sdk.signature.generated.model.SendRequestQesResponse
import com.alfa.api.sdk.signature.generated.model.SignRequestQes
import com.alfa.api.sdk.signature.generated.model.SignRequestQesResponse
import com.alfa.api.sdk.signature.generated.model.SignSettings
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sdk/signature")
class SignatureController(private val signatureApi: SignatureApi) {
    @GetMapping("/dn")
    fun getDn(): ResponseDN = signatureApi.getDn()

    @GetMapping("/dn/file", produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getDnFile(): String = signatureApi.getDnFile()

    @PostMapping("/rsa-certificates/requests/issue")
    fun issueRsaCertificate(@RequestBody request: RequestIssueRSACertificate): ResponseRsaCertificate =
        signatureApi.issueRsaCertificate(request)

    @PostMapping("/requests/{id}/operations")
    fun sendRequest(
        @PathVariable id: String,
        @RequestBody request: RequestPostOperations
    ): ResponsePostOperations = signatureApi.sendRequest(id, request)

    @PutMapping("/requests/operations/{id}")
    fun signRequest(
        @PathVariable id: String,
        @RequestBody request: RequestPutOperations
    ): ResponsePutOperations = signatureApi.signRequest(id, request)

    @GetMapping("/requests/{id}")
    fun getRequest(@PathVariable id: String): RequestData = signatureApi.getRequest(id)

    @GetMapping("/rsa-certificates/{id}")
    fun getRsaCertificate(@PathVariable id: String): RSACertificate = signatureApi.getRsaCertificate(id)

    @GetMapping("/rsa-certificates")
    fun getRsaCertificates(): RsaCertificates = signatureApi.getRsaCertificates()

    @PostMapping("/rsa-certificates/{id}/requests/activation")
    fun activateRsaCertificate(@PathVariable id: String): ResponseRsaCertificate =
        signatureApi.activateRsaCertificate(id)

    @PutMapping("/sign-settings")
    fun changeSignMethod(@RequestBody request: SignSettings): ResponseRsaCertificate =
        signatureApi.changeSignMethod(request)

    @PostMapping("/rsa-certificates/{id}/requests/switch")
    fun switchRsaCertificate(@PathVariable id: String): ResponseRsaCertificate =
        signatureApi.switchRsaCertificate(id)

    @PostMapping("/rsa-certificates/{id}/requests/reissue")
    fun reissueRsaCertificate(
        @PathVariable id: String,
        @RequestBody request: RequestReissueRSACertificate
    ): ResponseRsaCertificate = signatureApi.reissueRsaCertificate(id, request)

    @PostMapping("/rsa-certificates/{id}/requests/revocation")
    fun revokeRsaCertificate(@PathVariable id: String): ResponseRsaCertificate =
        signatureApi.revokeRsaCertificate(id)

    @PostMapping("/qes/requests")
    fun createQesRequest(): EnableQesResponse = signatureApi.createQesRequest()

    @PostMapping("/qes/requests/{id}")
    fun sendQesRequest(
        @PathVariable id: String,
        @RequestBody request: SendRequestQes
    ): SendRequestQesResponse = signatureApi.sendQesRequest(id, request)

    @PatchMapping("/qes/requests/{id}")
    fun signQesRequest(
        @PathVariable id: String,
        @RequestBody request: SignRequestQes
    ): SignRequestQesResponse = signatureApi.signQesRequest(id, request)

    @PostMapping("/powers-of-attorney/requests")
    fun registerPowerOfAttorney(@RequestBody request: RegisterPoaRequest): RegisterPoaResponse =
        signatureApi.registerPowerOfAttorney(request)

    @GetMapping("/powers-of-attorney/requests/{id}/status")
    fun getPowerOfAttorneyStatus(@PathVariable id: String): GetPoaStatus =
        signatureApi.getPowerOfAttorneyStatus(id)

    @GetMapping("/powers-of-attorney/requests/{id}")
    fun downloadPowerOfAttorney(
        @PathVariable id: String,
        @RequestHeader(HttpHeaders.ACCEPT) accept: String
    ): ResponseEntity<ByteArray> = ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(accept))
        .body(signatureApi.downloadPowerOfAttorney(id, accept))
}
