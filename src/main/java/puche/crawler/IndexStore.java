package puche.crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;

public class IndexStore {
    
    private int nextId;
    private final File baseDir;
    private final Map<Integer, Entry> map = new java.util.HashMap<>();
    
    public IndexStore(File baseDir) {
        this.baseDir = baseDir;
    }
    
    public synchronized void store(URL url, File file) {
        Integer key = new Integer(++nextId);
        map.put(key, new Entry(key, url, file));
    }
    
    public void flush() throws IOException {
        try(PrintWriter out = new PrintWriter(new FileWriter(new File(baseDir, "index.csv")))) {
            for (Entry e: map.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(e.getId()).append(", ")
                  .append(e.getFile().getName()).append(", ")
                  .append(e.getUrl().toString());
                out.println(sb.toString());
            }
        }
    }
    
    static class Entry {
        
        private final Integer id;
        private final URL url;
        private final File file;
        
        public Entry(Integer id, URL url, File file) {
            this.id = id;
            this.url = url;
            this.file = file;
        }
        
        public Integer getId() { return id; }
        
        public URL getUrl() { return url; }
        
        public File getFile() { return file; }
    }
}