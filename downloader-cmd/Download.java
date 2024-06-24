import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class Download {
    public static void main(String[] args) throws IOException {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            System.setProperty("https.protocols", "TLSv1.2");
            System.setProperty("jsse.enableSNIExtension", "false");

            String url = "https://file-examples.com/storage/fed5266c9966708dcaeaea6/2017/10/file-sample_150kB.pdf";
            URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");

            InputStream input = connection.getInputStream();

            File destination = new File("test.pdf");
            FileOutputStream output = new FileOutputStream(destination, false);

            byte[] buffer = new byte[2048];
            int read;
            System.out.println("Downloading.");
            while ((read = input.read(buffer)) > -1) {
                output.write(buffer, 0, read);
                System.out.print(".");
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
