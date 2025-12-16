import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
public class hyperDoc {
    public Document doc;
    public ArrayList<String> connections = new ArrayList<String>();
    public hyperDoc(String url){
        try{
        doc = Jsoup.connect(url).get();
        }
        catch (IOException e) {
            System.out.println( "Error: " + e.getMessage());
        }
    }
    public ArrayList<String> extractAnchors(){
        Elements links = doc.select("a[href^='/wiki/']:not([href*=':'])");
        for(Element link: links){
            String linkName = "https://en.wikipedia.org" + link.attr("href").split("#")[0];
            connections.add(linkName);
        }
        return this.connections;
    }
}