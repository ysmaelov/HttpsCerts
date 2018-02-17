import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class PruebaHTTPSTwoWay {
    public static void main(String[] args) {
        try {
            // Carga del fichero que tiene los certificados de los servidores en
            // los que confiamos.
            InputStream fileCertificadosConfianza = new FileInputStream(new File(
                    "..\\certs\\ca.jks"));
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(fileCertificadosConfianza, "ismael".toCharArray());
            fileCertificadosConfianza.close();

            // Ponemos el contenido en nuestro manager de certificados de
            // confianza.
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            // Cargamos el fichero con el certificado de cliente
            InputStream fileCertificadoCliente = new FileInputStream(
                    new File(
                            "..\\certs\\client.p12"));
            KeyStore ksCliente = KeyStore.getInstance("PKCS12");
            ksCliente.load(fileCertificadoCliente, "ismael".toCharArray());
            fileCertificadoCliente.close();

            // Creamos un manager de certificados con nuestro certificado de
            // cliente
            KeyManagerFactory kmf = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ksCliente, "ismael".toCharArray());

            // Creamos un contexto SSL tanto con el manger de certificados de
            // servidor en los que confiamos como con el manager de certificados de
            // cliente de los que disponemos
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLSocketFactory sslSocketFactory = context.getSocketFactory();

            // Abrimos la conexión y le pasamos el contexto SSL que hemos creado
            URL url = new URL("https://paloma");
            URLConnection conexion = url.openConnection();
            ((HttpsURLConnection) conexion).setSSLSocketFactory(sslSocketFactory);

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
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}