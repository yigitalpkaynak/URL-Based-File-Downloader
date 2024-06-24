import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PDFDownloader {
    public static void main(String[] args) {
        String url = "https://www.princexml.com/samples/";
        String downloadDirectory = "downloads";

        // Create the download directory if it does not exist
        Path downloadPath = Paths.get(downloadDirectory);
        try {
            if (!Files.exists(downloadPath)) {
                Files.createDirectories(downloadPath);
            }

            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href$=.pdf]"); // Select links ending with .pdf

            for (Element link : links) {
                String pdfUrl = link.absUrl("href");
                downloadPDF(pdfUrl, downloadDirectory);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadPDF(String pdfUrl, String downloadDirectory) {
        try (InputStream in = new URL(pdfUrl).openStream()) {
            String fileName = pdfUrl.substring(pdfUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(downloadDirectory, fileName);
            try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                System.out.println("Downloaded: " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
