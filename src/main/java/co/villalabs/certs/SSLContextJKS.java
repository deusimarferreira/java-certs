package co.villalabs.certs;

import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SSLContextJKS {

    private final static String CACERTS_DIR = "C:\\clientes\\CNPq\\dev\\jdk\\jdk1.7.0_131\\jre\\lib\\security\\cacerts";
    private final static String CACERTS_PASSWD = "changeit";

    /**
     * @author Deusimar Ferreira <deusimar.anjos@capgemini.com>
     */
    public static SSLContext getSslContext(){

        try{

            // Configurando KeyStore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(CACERTS_DIR), CACERTS_PASSWD.toCharArray());

            // Configurando KeyStore Management
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, CACERTS_PASSWD.toCharArray());
            KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

            // Configurando Trust Management
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagers, trustManagers, new java.security.SecureRandom());

            return sslContext;

        } catch (Exception exception) {

            exception.printStackTrace();
            return null;

        }
     }
}
