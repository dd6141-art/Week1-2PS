import java.util.*;

public class RealTimeAnalytics {

    // page → total visits
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // page → unique users
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic sources
    private HashMap<String, Integer> sourceCount = new HashMap<>();

    // process page view event
    public void processEvent(String url, String userId, String source) {

        // Update visit count
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Update unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Update traffic sources
        sourceCount.put(source, sourceCount.getOrDefault(source, 0) + 1);
    }

    // show dashboard
    public void getDashboard() {

        System.out.println("Top Pages:");

        // sort pages by visits
        List<Map.Entry<String,Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        list.sort((a,b) -> b.getValue() - a.getValue());

        for(int i=0;i<Math.min(10,list.size());i++){

            String page = list.get(i).getKey();
            int visits = list.get(i).getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println((i+1) + ". " + page +
                    " - " + visits +
                    " views (" + unique + " unique)");
        }

        System.out.println("\nTraffic Sources:");

        int total = sourceCount.values().stream().mapToInt(i->i).sum();

        for(String src : sourceCount.keySet()){

            int count = sourceCount.get(src);
            double percent = (count * 100.0)/total;

            System.out.println(src + ": " + percent + "%");
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent("/article/breaking-news","user_123","google");
        analytics.processEvent("/article/breaking-news","user_456","facebook");
        analytics.processEvent("/sports/championship","user_999","google");

        analytics.getDashboard();
    }
}