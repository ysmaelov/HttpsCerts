import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PruebaHTTPSTwoWayProperties {
    public static void main(String[] args) {
        try {
            System.setProperty("javax.net.ssl.trustStore", "..\\certs\\ca.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "ismael");
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");

            System.setProperty("javax.net.ssl.keyStore", "..\\certs\\client.p12");
            System.setProperty("javax.net.ssl.keyStorePassword", "ismael");
            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

            // Abrimos la conexión y le pasamos el contexto SSL que hemos creado
            URL url = new URL("https://paloma");
            URLConnection conexion = url.openConnection();

            // Ya podemos leer.
            conexion.connect();
            InputStream is = conexion.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            char[] buffer = new char[1000];
            int leido;
            while ((leido = br.read(buffer)) > 0) {
                System.out.println(new String(buffer, 0, leido));
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}