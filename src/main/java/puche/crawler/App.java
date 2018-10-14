package puche.crawler;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class App {

    private final Crawler crawler;
    
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Error. Syntax: java -jar crawler.jar <URL> <output-directory>");
            return;
        }
        String sourceUrl = args[0];
        String outputDir = args[1];
        //String sample = "https://www.laverdad.es/murcia/adif-montara-tres-20181013004838-ntvo.html";
        String sample = "https://www.geeksforgeeks.org/joining-threads-in-java/";
        System.out.println("Welcome to the Image Crawler!");
        App app = new App(new URL(sourceUrl), new File(outputDir));
        app.crawl();
    }
    
    public App(URL url, File baseDir) {
        this.crawler = new Crawler(url, baseDir);
    }
    
    public void crawl() {
        try {
            System.out.println("Start crawling...");
            this.crawler.start();
        } catch(IOException ex) {
            System.err.println("Error E/S");
            ex.printStackTrace();
        }
    }
}
