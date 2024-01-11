package com.alfa.api.sdk.sample.app.mapper

import org.mapstruct.Mapper
import com.alfa.api.sdk.client.security.TlsProvider
import com.alfa.api.sdk.sample.app.configuration.ApplicationProperties

@Mapper(componentModel = "spring")
@SuppressWarnings("kotlin:S6526")
abstract class SslPropertiesMapper {
    abstract fun map(source: ApplicationProperties.TlsProperties?): TlsProvider.TlsProperties?
}