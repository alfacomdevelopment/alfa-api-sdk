package com.alfa.api.sdk.sample.app.mapper

import com.alfa.api.sdk.crypto.model.KeyStoreParameters
import com.alfa.api.sdk.sample.app.configuration.ApplicationProperties
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
@SuppressWarnings("kotlin:S6526")
abstract class SignaturePropertiesMapper {
    abstract fun map(source: ApplicationProperties.SignatureProperties.KeyStoreProperties): KeyStoreParameters
}