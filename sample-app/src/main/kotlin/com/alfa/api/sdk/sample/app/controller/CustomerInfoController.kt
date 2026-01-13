package com.alfa.api.sdk.sample.app.controller

import com.alfa.api.sdk.customer.info.CustomerInfoApi
import com.alfa.api.sdk.customer.info.v2.generated.model.CustomerInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sdk/customer-info")
class CustomerInfoController(private val customerInfoApi: CustomerInfoApi) {
    @GetMapping
    fun getCustomerInfo(): CustomerInfo {
        return customerInfoApi.getCustomerInfoV2()
    }
}
