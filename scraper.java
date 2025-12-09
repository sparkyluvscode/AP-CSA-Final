import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Scanner;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class scraper {
    
    public static void main(String[] args){

        Scanner input = new Scanner(System.in);
        //setting up the webscraper
        System.out.print("Enter the URL to scrape: ");
        String url = input.nextLine();
        webscraper(url);
    
    }

    public static void webscraper(String url){
        
        try {
            

            
            System.out.println("Connecting to " + url);
            Document doc = Jsoup.connect(url).get();
            System.out.println("Successfully connected! Page title: " + doc.title());

            Elements links = doc.select("a[href^='/wiki/']:not([href*=':'])");
            System.out.println("Links: " + links.size());
            for (Element link: links){
                String href = link.attr("href");
                System.out.println(href);
            }


        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
