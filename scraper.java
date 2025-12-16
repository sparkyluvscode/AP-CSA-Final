import java.util.Scanner;
import java.util.ArrayList;

public class scraper {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        // Take user input via Scanner class (Java's built in class)
        System.out.print("Enter the URL to scrape: ");
        String url = input.nextLine();
        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add(url);
        System.out.print("Enter the destination URL: ");
        String dest_url = input.nextLine();
        input.close();
        // this is the list where we will store all the pages the algorithm visit
        // It is a 2d string arraylist, where each index represents the depth of all the
        // strings that preside in that specific arrayList.
        ArrayList<ArrayList<String>> href_options = new ArrayList<ArrayList<String>>();
        href_options.add(urlList);
        // splitting the string with hashtages prevents redundant links, which anchor to
        // the same web page, but at different places on the page.
        url = url.split("#")[0];
        dest_url = dest_url.split("#")[0];
        // enter the search with this print statement
        System.out.println(webscraper(url, dest_url, 1, href_options));

    }

    /**
     * 
     * @param url      the link to the page that the user wishes to start their
     *                 search from
     * @param dest_url the desired page which the user hopes to arrive at through a
     *                 series of clicking links
     * @param index    the recursive depth indicator, but more literally, the
     *                 current list of the visited 2d string list
     * @param visited  the entire history of where the algorithm has searched
     * @return the pathway or series of urls that the user needs to click to end up
     *         on the destination page.
     */
    public static String webscraper(String url, String dest_url, int index, ArrayList<ArrayList<String>> visited) {
        // this is the maximum recursive depth, or the "negative" base case.
        // we cap it at 7 because according to discrete math and graph theory, in such a
        // dense and large connection of nodes, each node requires
        // at most six steps to reach another node
        // in our experience, this number is typically closer to 3 or 4, since wikipedia
        // is so dense with links
        if (index > 6) {
            return "These two pages are not connected.";
        }
        // current is the list of all links at this recursive depth, and will be added
        // to visited should we not find the desired url
        ArrayList<String> current = new ArrayList<String>();
        // the algorithm now loops through the previous element of visited, the
        // "current" we just came from, of the previous recursive call.
        for (int i = 0; i < visited.get(index - 1).size(); i++) {
            String href = visited.get(index - 1).get(i);
            // this is an arbitrary marker I placed whose meaning cannot be understood until
            // later in the code
            if (href.equals("|")) {
                continue;
            }
            // the hyperDoc class (made by us) uses the jsoup library to parse and process
            // information on the webpage (such as the anchor links we desire)
            hyperDoc doc = new hyperDoc(href);
            ArrayList<String> links = doc.extractAnchors();
            // we now iterate through all the links found on the current webpage
            for (String link : links) {
                // this is the "positive" base case, where we have found the link
                // the following logic is used to retrace the steps we took
                if (dest_url.equals(link)) {
                    String path = "";
                    // we know the path ends at href, since that is the document whose links we are
                    // checking
                    path = href;
                    int trace = i;
                    // the path way is accumulated in reverse order, but in the end it can be read
                    // left to right
                    for (int x = index - 2; x >= 0; x--) {
                        // curr is the previos recursive call's current list
                        ArrayList<String> curr = visited.get(x + 1);
                        // back is the call preceeding curr
                        ArrayList<String> back = visited.get(x);
                        int a = findPos("|", curr, trace);
                        // the integer a now represents the number of arbitrary markers that occur
                        // before the document we are analyzing
                        // the number of markers before the index corresponds to the index of the
                        // document who leads us to this web page
                        // the links closer to the destination are added first, so we add them before we
                        // add path to itself
                        path = back.get(a) + " --> " + path;
                        // trace now becomes a, because back becomes the new curr, and the call
                        // preceeding back becomes the new back
                        // thus, trace must become the previous step in the path to repeat the back
                        // tracing process
                        trace = a;
                    }
                    return path + " --> " + dest_url;

                }
                // suppose the link we search isn't the destination. We then add it to the
                // visited list, if its not already there
                // current will soon be part of visited, that is why we check it
                // eliminating duplicates/repition elimnates the possibility of an infinite loop
                if (!current.contains(link)) {
                    for (int x = 0; x < visited.size(); x++) {
                        if (visited.get(x).contains(link)) {
                            break;
                        }
                        // if you've reached the end of visited and found that this link is nowhere,
                        // then it is new and you may add it to the visited list
                        if (x == visited.size() - 1) {
                            current.add(link);
                        }
                    }
                }
            }
            // here is where the arbitrary marker works. After we've appended all the links
            // a doc leads to, we now place a "|" after it, and this
            // marker seperates the list into sections. Each link of a specific section all
            // lead to the same link, whose index corresponds to the number of markers
            // preceeding a specific link
            current.add("|");
        }
        // add the all nodes of the current depth, and move on to the next one.
        visited.add(current);
        return webscraper(url, dest_url, index + 1, visited);
    }

    /**
     * 
     * @param symb  the arbitrary marker which we want to count. In our case, "|"
     * @param str   the actual list which we will traverse and analyse
     * @param index the index for which we want to check
     * @return the function returns the amount of times the arbitrary marker appears
     *         before a specified index
     */
    public static int findPos(String symb, ArrayList<String> str, int index) {
        int c = 0;
        for (int i = index; i >= 0; i--) {
            if (str.get(i).equals(symb)) {
                c++;
            }
        }
        return c;
    }
}