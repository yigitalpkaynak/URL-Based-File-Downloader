import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;
import java.io.File;

public class Download {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String address;
        while (true) {
            System.out.print("Enter URL (or type 'exit' to quit): ");
            address = sc.nextLine();
            if (address.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                URL url = new URL(address);
                String[] paths = address.split("/");
                String fileName = paths[paths.length - 1];

                // Ensure the directory exists
                File dir = new File("files");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                InputStream is = url.openStream();
                OutputStream os = new FileOutputStream("files/" + fileName);
                byte[] bytes = new byte[1024];
                int len, downloaded = 0;
                while ((len = is.read(bytes)) != -1) {
                    os.write(bytes, 0, len);
                    downloaded += len;
                    System.out.printf("\rDownloaded... %.2fkb", downloaded / 1000.0f);
                }
                System.out.println("\nDownload completed.");
                is.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sc.close();
    }
}
