package puche.crawler;

import java.io.*;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crawler {

    private static final int THREAD_POOL_SIZE = 10;
    
    private final URL url;
    private final File baseDir;
    //private final java.util.List<Thread> workers = new java.util.ArrayList<>();
    private final ExecutorService workers;
    private final IndexStore store;
    
    public Crawler(URL url, File baseDir) {
        this.url = url;
        this.baseDir = baseDir;
        this.workers = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.store = new IndexStore(baseDir);
    }
    
    public void start() throws IOException {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                ImgParser parser = new ImgParser();
                parser.initLine(line);
                String imgPath;
                while ((imgPath = parser.nextImageSrc()) != null) {
                    //System.out.println(">>>" + imgPath);
                    // Convertimos path de imagen (puede ser relativo, usar / o //, p. ej)
                    URL imageSrc = new URL(this.url, imgPath);
                    imageFound(imageSrc, baseDir);
                }
            }
            System.out.println("** Parse finished, waiting for workers to end...");
            // wait for all workers to end
//            for (Thread worker: workers) {
//                worker.join();
//            }
            workers.shutdown();
            workers.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            System.out.println("*** All workers are finished.");
            store.flush();
        } catch (InterruptedException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void imageFound(URL imageUrl, File baseDir) {
//        Thread worker = new Thread(new ImageDownloader(baseDir, imageUrl));
//        this.workers.add(worker);
//        worker.start();
        workers.submit(new ImageDownloader(store, baseDir, imageUrl));
    }
}
