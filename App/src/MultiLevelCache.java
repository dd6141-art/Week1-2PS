import java.util.*;

class L1Cache extends LinkedHashMap<String, String> {

    private int capacity;

    public L1Cache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
        return size() > capacity;
    }
}

public class MultiLevelCache {

    private L1Cache L1 = new L1Cache(10000);
    private HashMap<String,String> L2 = new HashMap<>();
    private HashMap<String,String> database = new HashMap<>();

    public String getVideo(String videoId){

        // L1 check
        if(L1.containsKey(videoId)){
            System.out.println("L1 Cache HIT");
            return L1.get(videoId);
        }

        // L2 check
        if(L2.containsKey(videoId)){
            System.out.println("L2 Cache HIT");

            String data = L2.get(videoId);

            L1.put(videoId,data); // promote to L1
            return data;
        }

        // Database check
        System.out.println("Database HIT");

        String data = database.get(videoId);

        if(data != null){
            L2.put(videoId,data);
        }

        return data;
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        // simulate database
        cache.database.put("video1","Funny Cats");
        cache.database.put("video2","Java Tutorial");

        System.out.println(cache.getVideo("video1"));
        System.out.println(cache.getVideo("video1"));
        System.out.println(cache.getVideo("video1"));
    }
}