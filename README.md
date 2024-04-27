# Alfa API SDK
## SDK structure
- **api-sdk-all** - library that contains all other modules
- **api-sdk-core** - SDK core, contains client and common classes
- **api-sdk-transactions** - library for integration with TransactionsApi
- **api-sdk-crypto** - utility library for cryptography operations

## Prerequisites
Java JDK 8 or higher

## Get Started
### Adding dependency
```
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
    // You can add this library to your dependencies to connect all modules at once:
    implementation("com.alfa.api.sdk:api-sdk-all:<version>") 
    
    // OR you can connect each module separately:
    implementation("com.alfa.api.sdk:api-sdk-transactions:<version>") 
}
```
- Latest versions of the dependencies are listed [here](https://github.com/users/alfacomdevelopment/packages?repo_name=alfa-api-sdk).
- You can get a personal GitHub token [here](https://github.com/settings/tokens/new).

### Setting up client
```java
TransportSecurityProvider transportSecurityProvider = new TlsProvider(
        TlsProvider.TlsProperties.builder()
                .keyStorePath("<path to your keystore>")
                .keyStorePassword("<password for your keystore>")
                .privateKeyAlias("<alia for private-key entry in keystore>")
                .trustStorePath("<path to your truststore>")
                .trustStorePassword("<password for your truststore>")
                .build()
);
CredentialProvider credentialProvider = new BearerTokenProvider("<your access token here>");
ApiHttpClient apiHttpClient = new ApiSyncHttpClient("<service url>", transportSecurityProvider, credentialProvider);
```

### Using TransactionApi
```java
TransactionsApi transactionsApi = new TransactionsApi(apiHttpClient);
Statement statement = transactionsApi.getStatement("1234567890", LocalDate.parse("2024-01-01"), 1, CurFormat.CUR_TRANSFER);
```

## Usage example
You can check out an example of how to use the SDK in a [test Spring Boot 3 application](https://github.com/alfacomdevelopment/alfa-api-sdk/tree/main/sample-app).

