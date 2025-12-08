import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Scanner;

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
            Document doc = Jsoup.connect(url).get();
            System.out.println("Successfully connected! Page title: " + doc.title());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
