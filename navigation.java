public class navigation {
    
    // method to check if a link is a navigation/main page link
    public static boolean isNavigationLink(String href) {
        // common navigation links to skip
        String[] navigationPages = {
            "/wiki/Main_Page",
            "/wiki/Special:",
            "/wiki/Wikipedia:",
            "/wiki/Help:",
            "/wiki/File:",
            "/wiki/Template:",
            "/wiki/Category:",
            "/wiki/Portal:",
            "/wiki/User:",
            "/wiki/Talk:"
        };
        
        // check if href starts with any navigation prefix
        for (String navPage : navigationPages) {
            if (href.startsWith(navPage)) {
                return true; 
            }
        }
        
        return false;  
    }
}