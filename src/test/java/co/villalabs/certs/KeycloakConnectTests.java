package co.villalabs.certs;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.junit.Test;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class KeycloakConnectTests {
	private static String URL_AUTH = "https://{host}/auth";
	private static String REALM = "teste";
	private static String URL_TOKEN_ENDPOINT = URL_AUTH + "/realms/" + REALM + "/protocol/openid-connect/token";
	private static String URL_CERTS_ENDPOINT = URL_AUTH + "/realms/" + REALM + "/protocol/openid-connect/certs";
	private static String KID_SECRET_PUBLIC;
	private static String JWT;
	
	@Test
	public void testPasso1GetToken() {
        System.out.println("Keycloak GET Token JWT");
        
        String client = "";
        String user = "";
        String pass = "";

        try {
        	
            SSLContext sslContext = SSLContextJKS.getSslContext();
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            String params = "client_id=" + URLEncoder.encode(client, "UTF-8") + 
            		"&grant_type=password&username=" + URLEncoder.encode(user, "UTF-8") + 
            		"&password=" + URLEncoder.encode(pass, "UTF-8");
            byte[] paramsBytes = params.getBytes(StandardCharsets.UTF_8);
            
            URL url = new URL(URL_TOKEN_ENDPOINT);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "application/xwww-form-urlencoded");
            con.setRequestProperty("Content-Length", String.valueOf(paramsBytes.length));
            con.setDoOutput(true);
            
            DataOutputStream stream = new DataOutputStream(con.getOutputStream());
            stream.write(paramsBytes);
            stream.close();
//            OutputStream stream = con.getOutputStream();
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
//            bw.write(params);
//            bw.flush();
//            bw.close();

            Integer status = con.getResponseCode();
            if (status != 400 ) {
	            Reader reader = new InputStreamReader(con.getInputStream());
	            int dados = reader.read();
	            String json = "";
	            while (dados != -1) {
	            	json += (char) dados;
	            	dados = reader.read();
	            };
	            reader.close();
	            System.out.println(json);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	@Test
	public void testPasso2GetKid() {
        System.out.println("Keycloak JWK - Recuperar JSON Web Key");

        try {
        	
            SSLContext sslContext = SSLContextJKS.getSslContext();
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            URL url = new URL(URL_CERTS_ENDPOINT);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/xwww-form-urlencoded");

            Integer status = con.getResponseCode();
            if (status == 200) {
	            Reader reader = new InputStreamReader(con.getInputStream());
	            int dados = reader.read();
	            String json = "";
	            while (dados != -1) {
	            	json += (char) dados;
	            	dados = reader.read();
	            };
	            reader.close();
	            ObjectMapper mapper = new ObjectMapper();
	            KeysParser parser = mapper.readValue(json, KeysParser.class);
	            KID_SECRET_PUBLIC = parser.keys[0].kid;
	            System.out.println("JSON Web Key: " + json);
	            System.out.println("Kid: " + KID_SECRET_PUBLIC);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	@Test
	public void testPasso3ValidarToken() {
		System.out.println("Jwk - Get Provider JSON Web Key");
		
        try {
        	
            JwkProvider provider = new UrlJwkProvider(new URL(URL_CERTS_ENDPOINT));
            Jwk jwk = provider.get(KID_SECRET_PUBLIC);
            System.out.println(jwk.getPublicKey());
            
            Claims claims = Jwts.parserBuilder().setSigningKey(jwk.getPublicKey())
            		.build().parseClaimsJws(JWT).getBody();
            System.out.println("Nome: " + claims.get("name", String.class));
            System.out.println("E-mail: " + claims.get("email", String.class));
        } catch (JwkException ex) {
            ex.printStackTrace();
        } catch (JwtException ex) {
        	ex.printStackTrace();
        }  catch (Exception ex) {
            ex.printStackTrace();
        } 
    }

}
