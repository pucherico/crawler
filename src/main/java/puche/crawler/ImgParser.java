package puche.crawler;

public class ImgParser {

    private int startIndex;
    private String line;
    
    public void initLine(String line) {
        this.startIndex = 0;
        this.line = line.toLowerCase().replace('"', '\'');
    }
    
    public String nextImageSrc() {
        if (!skipToPattern("<img")) { return null; }
        if (!skipToPattern("src")) { return null; }
        if (!skipToPattern("'")) { return null; }
        startIndex++;
        String srcUrl = takeToPattern("'");
        return srcUrl;
    }
    
    private boolean skipToPattern(String pattern) {
        startIndex = line.indexOf(pattern, startIndex);
        return startIndex != -1;
    }
    
    private String takeToPattern(String pattern) {
        int index = startIndex;
        startIndex = line.indexOf(pattern, startIndex);
        if (startIndex != -1) {
            return line.substring(index, startIndex);
        } else {
            return null;
        }
    }
}
