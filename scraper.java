import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class scraper {
    
    public static void main(String[] args){

        Scanner input = new Scanner(System.in);
        //setting up the webscraper
        System.out.print("Enter the URL to scrape: ");
        String url = input.nextLine();
        System.out.print("Enter the destination URL: ");
        String dest_url = input.nextLine();
        webscraper(url, dest_url,0, new ArrayList<String>());
    
    }

    public static void webscraper(String url, String dest_url, int depth, ArrayList<String> visited){
        
        try {
            if(depth > 8) {
                System.out.println("Depth limit reached! " + url);
                return;
            }
            ArrayList<String> href_options = new ArrayList<String>();
            
            //System.out.println("Connecting to " + url);
            Document doc = Jsoup.connect(url).get();
            Document dest = Jsoup.connect(dest_url).get();
            //System.out.println("Successfully connected! " + doc.title() + " and " + dest.title());

            Elements links = doc.select("a[href^='/wiki/']:not([href*=':'])");
            //System.out.println("Links: " + links.size());
            
            for (Element link: links){
                // strips the href 
                String href = link.attr("href");
                
                if (href.equals(dest_url)) {
                    System.out.println("Destination found! " + href);
                    return;
                }
                if (visited.contains(href)) {
                    System.out.println("Terminate Path, repeat page." + href);
                    return;
                }
                href_options.add(href);
                webscraper("https://en.wikipedia.org" + href,dest_url,depth+1, href_options);
            }


        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
