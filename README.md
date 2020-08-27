# Utilizando Certificados em Java

Existem três [KeyStore Types](https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore), neste exemplo é domostrado o uso do KeyStore tpo ``JKS``

| KeyStore Types |
| :------------: |
|    jceks       |
|     jks        |
|    pkcs12      |

## Utilizando ferramenta keytool
Keytool é uma ferramenta disponível na ``JVM`` para realizar manipulação de certificados e Keystore.

~~~sh
# Consulte ajuda da ferramenta
keytool --help
~~~

Imagine que você vai realizar uma implementação que irá consumir uma serviço disponível em um protocolo seguro (https), para isso será necessário realizar a configuração de um ``KeyStore``, instalar o certificado e configurar a aplicação java para usar o ``KeyStore``.

Como eu faço isso? 
Os passos a seguir demostram como baixar um certificado e posteriormente instalar em um ``KeyStore``.

### Download do certificado
O primeiro passo será realizar o download do cerificado, aqui usaremos o ``Google`` como exemplo, mas é possível fazer o mesmo procedimento em outros endereços.

~~~sh
# Baixar o certificado
keytool -J-Djava.net.useSystemProxies=true -printcert -rfc -sslserver www.google.com.br:443 > google.pem
~~~

### Importar certificado
Com o certificado baixado vamos instlar ele em nosso ``KeyStore``.

~~~sh
# Instalar certificado no keystore
keytool -importcert -file google.pem -alias google -storepass changeit -keystore $JAVA_HOME/jre/lib/security/cacerts
~~~

### Listar certificado
Após ter realizado a instalação vamos verificar se o certificado está realmente instalado.

~~~sh
# Lista certificado no keystore pelo alias
keytool -list -alias sigac-hom -storepass changeit -keystore $JAVA_HOME/jre/lib/security/cacerts
~~~

## Definir keystore na aplicação
Agora vamos para o código.

* Implementação
    * [/src/main/java/co/villalabs/certs/SSLContextJKS.java](/src/main/java/co/villalabs/certs/SSLContextJKS.java)
* Teste
    * [/src/test/java/co/villalabs/certs/KeycloakConnectTests.java](/src/test/java/co/villalabs/certs/KeycloakConnectTests.java)
    * [/src/test/java/co/villalabs/certs/SSLContextJKSTest.java](/src/test/java/co/villalabs/certs/SSLContextJKSTest.java)

## Referências
* [Java Platform, Standard Edition Tools Reference](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)
* [KeyStore Types](https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore)
* [KeystoreImplementation](https://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html#KeystoreImplementation)
