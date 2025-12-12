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
        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add(url);
        System.out.print("Enter the destination URL: ");
        String dest_url = input.nextLine();
        ArrayList<ArrayList<String>> href_options = new ArrayList<ArrayList<String>>();
        href_options.add(urlList);
        System.out.println(webscraper(url, dest_url,0, href_options));
    
    }

    public static String webscraper(String url, String dest_url, int index, ArrayList<ArrayList<String>> visited){
        url = url.split("#")[0];
        dest_url = dest_url.split("#")[0];
        try {
            if(index > 6){
                return "These two pages are not connected.";
            }
            ArrayList<String> current = new ArrayList<String>();
            for(int i = 0; i < visited.get(index).size(); i++){
                String href = visited.get(index).get(i);
                Document doc = Jsoup.connect(href).get();
                Elements links = doc.select("a[href^='/wiki/']:not([href*=':'])");
                for(Element link: links){
                    if(dest_url.equals("https://en.wikipedia.org" + link.attr("href").split("#")[0])){
                        String path = "";
                        path = href;
                        int trace = i;
                        for(int x = index - 2; x >= 0; x++){
                            ArrayList<String> curr = visited.get(x+1);
                            ArrayList<String> back = visited.get(x);
                            int a = findPos("|", curr, trace);
                            path = back.get(a) + " --> " + path;
                            trace = a;
                        }
                        return url + " --> " + path + " --> " +  dest_url;
                        
                    }
                    if(!current.contains("https://en.wikipedia.org" + link.attr("href"))){
                        current.add("https://en.wikipedia.org" + link.attr("href"));
                    }
                }
                current.add("|");
            }
            visited.add(current);
            return webscraper(url, dest_url, index+1, visited);

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
    public static int findPos(String symb, ArrayList<String> str, int index){
        int c = 0;
        for(int i = index; i >= 0; i--){
            if(str.get(i).equals(symb)){
                c++;
            }
        }
        return c;
    }
}