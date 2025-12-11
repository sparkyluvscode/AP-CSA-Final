import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.Collections;

public class scraper {
    public static int MAX_LINKS_PER_PAGE = 12;
    
    public static void main(String[] args){

        Scanner input = new Scanner(System.in);
        //setting up the webscraper
        System.out.print("Enter the URL to scrape: ");
        String url = input.nextLine();
        System.out.print("Enter the destination URL: ");
        String dest_url = input.nextLine();

        ArrayList<String> href_options = new ArrayList<String>();
        href_options.add(url);
        webscraper(url, dest_url,0, href_options);
    
    }

    public static void webscraper(String url, String dest_url, int depth, ArrayList<String> visited){
        
        try {
            if(depth > 8) {
                System.out.println("Depth limit reached! " + url);
                return;
            }
            
            Document doc = Jsoup.connect(url).get();
            System.out.println("[Depth " + depth + "] Exploring: " + doc.title() + " (" + visited.size() + " pages visited)");

            Elements links = doc.select("a[href^='/wiki/']:not([href*=':'])");
            
            dest_url = dest_url.split("#")[0];
            String destPageName = dest_url.substring(dest_url.lastIndexOf("/") + 1);
            String[] keywords = destPageName.split("_");
            
            // FIRST: Check ALL links on current page to see if destination is here
            for (Element link : links) {
                String href = link.attr("href");
                if (navigation.isNavigationLink(href)) {
                    continue;
                }
                String full_href = "https://en.wikipedia.org" + href;
                full_href = full_href.split("#")[0];
                
                if (full_href.equals(dest_url)) {
                    System.out.println("Destination found! " + href);
                    return;
                }
            }
            
            // SECOND: Score and sort links for exploration
            ArrayList<Element> validLinks = new ArrayList<Element>();
            ArrayList<Integer> scores = new ArrayList<Integer>();
            
            int maxLinks = Math.min(MAX_LINKS_PER_PAGE, links.size());
            for (int i=0;i<maxLinks;i++){
                Element link = links.get(i);
                String href = link.attr("href");
                if (navigation.isNavigationLink(href)) {
                    continue;
                }
                validLinks.add(link);
                scores.add(scoreLink(href, keywords, destPageName));
            }
            
            // Sort by score (highest first)
            ArrayList<Integer> indices = new ArrayList<Integer>();
            for (int i = 0; i < validLinks.size(); i++) {
                indices.add(i);
            }
            Collections.sort(indices, (a, b) -> scores.get(b) - scores.get(a));
            
            // Explore in score order
            for (int idx : indices) {
                Element link = validLinks.get(idx);
                String href = link.attr("href");
                String full_href = "https://en.wikipedia.org" + href;
                full_href = full_href.split("#")[0];

                if (visited.contains(full_href)) {
                    continue;
                }
                visited.add(full_href);
                webscraper(full_href,dest_url,depth+1, visited);
            }


        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static int scoreLink(String href, String[] keywords, String destPageName) {
        int score = 0;
        String hrefLower = href.toLowerCase();
        String destLower = destPageName.toLowerCase();
        
        // Exact match gets highest score
        if (hrefLower.equals("/wiki/" + destLower)) {
            return 1000;
        }
        
        // Near-exact match (contains full destination name)
        if (hrefLower.contains(destLower)) {
            score += 100;
        }
        
        // Keyword matches
        for (String keyword : keywords) {
            if (hrefLower.contains(keyword.toLowerCase())) {
                score += 10; //increase the score if it matches
            }
        }
        return score;
    }
}
