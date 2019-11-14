# Utilizando Certificados em Java

Existem três [KeyStore Types](https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore), neste exemplo é domostrado o uso do KeyStore tpo ``JKS``

| KeyStore Types |
| :------------: |
|    jceks       |
|     jks        |
|    pkcs12      |

## Utilizando ferramenta keytool

```shell script
keytool --help
```
> Download do certificado de um determinado site
```shell script
keytool -J-Djava.net.useSystemProxies=true -printcert -rfc -sslserver www.google.com.br:443 > google.pem
```

> Importar certificado (.pem) para o keystore
```shell script
keytool -importcert -file google.pem -alias google -storepass changeit -keystore /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts
```

> Listar certificado por alias
```shell script
keytool -list -alias sigac-hom -storepass jboss123 -keystore ...cacerts
```

## Referências
* [Java Platform, Standard Edition Tools Reference](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)
* [KeyStore Types](https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore)
* [KeystoreImplementation](https://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html#KeystoreImplementation)
