package com.alfa.api.sdk.sample.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class Application fun main(vararg args: String) {
    runApplication<Application>(*args)
}