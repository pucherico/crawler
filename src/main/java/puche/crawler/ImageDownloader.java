package puche.crawler;

import java.net.URL;
import java.io.*;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageDownloader implements Runnable {

    private final IndexStore store;
    private final File baseDir;
    private final URL url;
    
    public ImageDownloader(IndexStore store, File baseDir, URL url) {
        this.store = store;
        this.baseDir = baseDir;
        this.url = url;
    }
    
    public void run() {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            URLConnection conn = url.openConnection();
            String contentType = conn.getContentType();
            System.out.println("** Content type: " + contentType);
            if ("image/jpeg".equals(contentType) || "image/png".equals(contentType)) {
                String ext = "image/png".equals(contentType) ? ".png" : ".jpg";
                in = new BufferedInputStream(conn.getInputStream());
                System.out.println("***" + url);
                File file = File.createTempFile("image", ext, baseDir);
                out = new BufferedOutputStream(new FileOutputStream(file));
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                store.store(url, file);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) { in.close(); }
            } catch (IOException ex) {
                Logger.getLogger(ImageDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (out != null) { out.close(); }
            } catch (IOException ex) {
                Logger.getLogger(ImageDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
