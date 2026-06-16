# Alfa API SDK
[![Build](https://github.com/alfacomdevelopment/alfa-api-sdk/actions/workflows/build.yml/badge.svg)](https://github.com/alfacomdevelopment/alfa-api-sdk/actions/workflows/build.yml)
[![CodeQL](https://github.com/alfacomdevelopment/alfa-api-sdk/actions/workflows/codeql.yml/badge.svg)](https://github.com/alfacomdevelopment/alfa-api-sdk/actions/workflows/codeql.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Java SDK for Alfa API integrations, including transactions, digital ruble, and cryptography helpers.

## Table of contents
- [Modules](#modules)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Transactions API](#transactions-api)
- [Customer Info API](#customer-info-api)
- [Digital Ruble API](#digital-ruble-api)
- [Cryptography](#cryptography)
- [Usage example](#usage-example)
- [Contributing](#contributing)
- [License](#license)

## Modules
- **api-sdk-all** - all modules in a single dependency
- **api-sdk-core** - SDK core, HTTP client, and common classes
- **api-sdk-transactions** - integration with `TransactionsApi`
- **api-sdk-customer-info** - integration with `CustomerInfoApi`
- **api-sdk-digital-ruble** - integration with `DigitalRubleApi`
- **api-sdk-crypto** - cryptography utilities (CMS/JWS/XML)

## Requirements
- Java 8+

## Installation
### Gradle (Kotlin DSL)
```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/alfacomdevelopment/alfa-api-sdk")
        credentials {
            username = "<your GitHub username>"
            password = "<your GitHub personal access token>"
        }
    }
}

dependencies {
    // All modules in one dependency:
    implementation("com.alfa.api.sdk:api-sdk-all:<version>")

    // OR add modules separately:
    implementation("com.alfa.api.sdk:api-sdk-transactions:<version>")
    implementation("com.alfa.api.sdk:api-sdk-customer-info:<version>")
    implementation("com.alfa.api.sdk:api-sdk-digital-ruble:<version>")
}
```
- Latest package versions: https://github.com/users/alfacomdevelopment/packages?repo_name=alfa-api-sdk
- Create a GitHub personal access token: https://github.com/settings/tokens/new

## Configuration
### API client
```java
TransportSecurityProvider transportSecurityProvider = new TlsProvider(
    TlsProvider.TlsProperties.builder()
        .keyStorePath("<path to your keystore>")
        .keyStorePassword("<password for your keystore>")
        .privateKeyAlias("<alias for private-key entry in keystore>")
        .trustStorePath("<path to your truststore>")
        .trustStorePassword("<password for your truststore>")
        .build()
);
CredentialProvider credentialProvider = new BearerTokenProvider("<your access token>");
ApiHttpClient apiHttpClient = new ApiSyncHttpClient(
    "<service url>",
    transportSecurityProvider,
    credentialProvider
);
```

### Credential providers
The SDK supports the following `CredentialProvider` implementations:

- `BearerTokenProvider` - sends `Authorization: Bearer <access token>`
- `ApiKeyProvider` - sends `Authorization: ApiKey <api key>`

Example with bearer token:
```java
CredentialProvider credentialProvider = new BearerTokenProvider("<your access token>");
ApiHttpClient apiHttpClient = new ApiSyncHttpClient("<service url>", credentialProvider);
```

Example with API key:
```java
CredentialProvider credentialProvider = new ApiKeyProvider("<your api key>");
ApiHttpClient apiHttpClient = new ApiSyncHttpClient("<service url>", credentialProvider);
```

## Transactions API
### Methods
- `getStatement(String accountNumber, LocalDate statementDate, Integer page, CurFormat curFormat)` - statement by account, date, page, and currency format.
- `getSummary(String accountNumber, LocalDate statementDate)` - summary by account and date.
- `getStatement1C(String accountNumber, LocalDate executeDate, Integer limit, Integer offset)` - statement in 1C format.
- `getStatementMT940(String accountNumber, LocalDate executeDate, Integer limit, Integer offset)` - statement in MT940 format.

## Customer Info API
### Methods
- `getCustomerInfoV2()` - returns organization profile.

## Digital Ruble API
Gateway headers such as `X-ClientId`, `X-Cus`, `X-Acus`, and `X-Sub` are derived from the access token and are not SDK method parameters.

### Methods
- `getWalletContext()` - returns the digital ruble wallet context.
- `getCertificates()` - returns certificates required for digital ruble operations.
- `getStatementTransactions(StatementRequest request)` - returns digital ruble statement transactions.

## Cryptography
The `api-sdk-crypto` module provides RSA signing/verification for CMS, JSON (JWS), and XML.

### Create services
Example: `CmsSignatureService`
```java
CmsSignatureService cmsSignatureService = new CmsSignatureServiceImpl(
    KeyStoreParameters.builder()
        .path("/path/to/keystore")
        .type(KeyStoreParameters.KeyStoreType.JKS)
        .password("example")
        .privateKey(PrivateKeyParameters.builder()
                .alias("example")
                .password("example")
                .build())
        .certificate(CertificateParameters.builder()
            .alias("example")
            .build())
        .build(),
    SignatureAlgorithm.RSA
);
```
Similarly, for `JwsSignatureService` and `XmlSignatureService`.

### CmsSignatureService methods
- `signDetached(byte[] data)` - returns a detached CMS signature.
- `verifyDetached(byte[] data, byte[] signature)` - validates a detached CMS signature.
- `signAttached(byte[] data)` - returns data with an attached CMS signature.
- `verifyAttached(byte[] dataWithSignature)` - validates an attached CMS signature.

Example:
```java
byte[] detachedSignature = cmsSignatureService.signDetached("some data to be signed".getBytes(StandardCharsets.UTF_8));
boolean verificationResult = cmsSignatureService.verifyDetached("some data to be signed".getBytes(StandardCharsets.UTF_8), detachedSignature);

byte[] attachedSignature = cmsSignatureService.signAttached("some data to be signed".getBytes(StandardCharsets.UTF_8));
boolean verificationResult = cmsSignatureService.verifyAttached(attachedSignature);
```

### JwsSignatureService methods
- `signDetached(String data)` - returns a detached JWS signature.
- `verifyDetached(String data, String jwsWithoutData)` - validates a detached JWS signature.
- `signAttached(String data)` - returns JWS with embedded data.
- `verifyAttached(String jwsWithData)` - validates an attached JWS signature.

Example:
```java
byte[] jwsWithoutData = jwsSignatureService.signDetached("{\"some\":\"json\"}");
boolean verificationResult = jwsSignatureService.verifyDetached("{\"some\":\"json\"}", jwsWithoutData);

byte[] jwsWithData = jwsSignatureService.signAttached("{\"some\":\"json\"}");
boolean verificationResult = jwsSignatureService.verifyAttached(jwsWithData);
```

### XmlSignatureService methods
- `sign(String data)` - signs XML and returns the signed XML.
- `verify(String dataWithSignature)` - validates a signed XML document.

Example:
```java
byte[] signedXml = xmlSignatureService.sign("<Some>Xml</Some>");
boolean verificationResult = xmlSignatureService.verify(signedXml);
```

## Usage example
See a test Spring Boot application in `sample-app`: https://github.com/alfacomdevelopment/alfa-api-sdk/tree/main/sample-app

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md) for local development, pull request, dependency update, and release guidelines.

## License
This project is licensed under the [MIT License](LICENSE).
