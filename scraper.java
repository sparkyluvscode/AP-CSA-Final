import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class scraper {
    
    public static void main(String[] args){
        //setting up the webscraper
        try {
            Document doc = Jsoup.connect("https://www.google.com").get();
            System.out.println("Successfully connected! Page title: " + doc.title());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
