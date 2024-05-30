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

### Setting up API client
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

### TransactionApi
#### Methods
* `getStatement(String accountNumber, LocalDate statementDate, Integer page, CurFormat curFormat)` - retrieves a statement for a specific account number, statement date, page number, and currency format.
* `getSummary(String accountNumber, LocalDate statementDate)` - retrieves a summary for a specific account number and statement date.
* `getStatement1C(String accountNumber, LocalDate executeDate, Integer limit, Integer offset)` - retrieves a statement for a specific account number, statement date, limit, and offset in 1C format. 
* `getStatementMT940(String accountNumber, LocalDate executeDate, Integer limit, Integer offset)` - retrieves a statement for a specific account number, statement date, limit, and offset in MT940 format.


### Cryptography services
Dependency `api-sdk-crypto` contains various services that encapsulate the cryptography logic.
Currently supported only RSA signing and verification of CMS, JSON, XML types.
#### Creating Services
To create a `CmsSignatureService`, you need to write this code:
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
Similarly, for `JwsSignatureService` and `XmlSignatureService`

#### CmsSignatureService methods
* `signDetached(byte[] data)` - this method signs the given data using a detached signature approach. It takes the data to be signed as input and returns the signature as a byte array.
* `verifyDetached(byte[] data, byte[] signature)` - this method verifies a detached signature against the original data. It requires the original data and the signature to be verified as inputs. It returns true if the signature is valid, and false otherwise.
* `signAttached(byte[] data)` - this method signs the provided data with an attached signature. It takes the data to be signed as input and returns the data along with the signature, encapsulated in a single byte array.
* `verifyAttached(byte[] dataWithSignature)` - this method verifies an attached signature against the provided data that includes the signature. It takes the data with the attached signature as input and returns true if the signature is valid, and false otherwise.

#### JwsSignatureService methods
* `signDetached(String data)` - generates a JWS signature for the given data without including the data itself in the signature. Returns the signature as a string.
* `verifyDetached(String data, String jwsWithoutData)` - validates a detached JWS signature against the original data. Returns true if the signature is valid and the data hasn't been altered; otherwise, returns false.
* `signAttached(String data)` - creates a JWS signature that embeds the given data within the signature itself. Returns the combined signature and data as a string.
* `verifyAttached(String jwsWithData)` - checks the validity of a JWS signature that contains embedded data. Returns true if the signature is valid and the embedded data is unmodified; otherwise, returns false.

#### XmlSignatureService methods
* `sign(String data)` - this method takes an XML string (data) as input and returns a new string where the original XML content is digitally signed. The returned string includes both the original XML content and the embedded signature.
* `verify(String dataWithSignature)` - this method accepts an XML string (dataWithSignature) that contains an embedded signature. It verifies the signature against the expected signature and returns true if the signature is valid, or false if it is not.

## Usage example
You can check out an example of how to use the SDK in a [test Spring Boot 3 application](https://github.com/alfacomdevelopment/alfa-api-sdk/tree/main/sample-app).

