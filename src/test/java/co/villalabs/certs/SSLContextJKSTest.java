package co.villalabs.certs;

import org.junit.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HttpsURLConnection;

import java.nio.charset.StandardCharsets;

/**
 * @author Deusimar Ferreira <deusimar.anjos@capgemini.com>
 */
public class SSLContextJKSTest {

    private final static String REST_URI_TOKEN = "?";
    private final static String REDIRECT_URL = "?";

    private final static String CODE_CNPQ = "?";
    private final static String PASS_CNPQ = "?";

    @Test
    public void testCert() {
        System.out.println("JKS - SSL TEST!");

        try {
            SSLContext sslContext = SSLContextJKS.getSslContext();
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            String auth = new String((CODE_CNPQ + ":" + PASS_CNPQ).getBytes(StandardCharsets.UTF_8),"UTF8");
            String code = "";

            URL url = new URL(REST_URI_TOKEN +
                                    "?grant_type=authorization_code&code=" +
                                    code +
                                    "&redirect_uri=" +
                                    REDIRECT_URL);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Basic " + auth);
            con.setRequestProperty("Content-type", "application/xwww-form-urlencoded");

            Integer status = con.getResponseCode();
            Reader reader = new InputStreamReader(con.getInputStream());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
