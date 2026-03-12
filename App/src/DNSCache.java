import java.util.HashMap;
import java.util.Map;

public class DNSCache {
    // 1. Internal Entry Class (Fixes "Cannot resolve symbol 'DNSEntry'")
    class DNSEntry {
        String domain;
        String ip;
        long expiryTime;
        DNSEntry prev, next;

        public DNSEntry(String domain, String ip, long ttlSeconds) {
            this.domain = domain;
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int CAPACITY;
    private final Map<String, DNSEntry> map = new HashMap<>();
    private DNSEntry head, tail; // For LRU tracking

    // Stats tracking
    private int hits = 0;
    private int misses = 0;
    private long totalLookupTime = 0;

    public DNSCache(int capacity) {
        this.CAPACITY = capacity; // Fixes "Field 'CAPACITY' not initialized"
    }

    public String resolve(String domain) {
        long startTime = System.nanoTime();
        DNSEntry entry = map.get(domain);

        // Check for HIT and ensure it hasn't expired
        if (entry != null && !entry.isExpired()) {
            hits++;
            moveToHead(entry);
            recordTime(startTime);
            System.out.println("resolve(\"" + domain + "\") → Cache HIT → " + entry.ip);
            return entry.ip;
        }

        // Handle MISS or EXPIRED
        misses++;
        if (entry != null) {
            System.out.print("resolve(\"" + domain + "\") → Cache EXPIRED → ");
            removeNode(entry);
            map.remove(domain);
        } else {
            System.out.print("resolve(\"" + domain + "\") → Cache MISS → ");
        }

        // Simulate Upstream Query
        String ip = queryUpstream(domain);
        long ttl = 300; // 5 minutes
        put(domain, ip, ttl);

        recordTime(startTime);
        System.out.println("Query upstream → " + ip);
        return ip;
    }

    private void put(String domain, String ip, long ttl) {
        if (map.containsKey(domain)) {
            removeNode(map.get(domain));
        } else if (map.size() >= CAPACITY) {
            map.remove(tail.domain);
            removeNode(tail);
        }

        DNSEntry newEntry = new DNSEntry(domain, ip, ttl);
        map.put(domain, newEntry);
        addToHead(newEntry);
    }

    // --- Helper Methods (Fixes "Cannot resolve method" errors) ---

    private void addToHead(DNSEntry node) {
        node.next = head;
        node.prev = null;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = node;
    }

    private void removeNode(DNSEntry node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
    }

    private void moveToHead(DNSEntry node) {
        removeNode(node);
        addToHead(node);
    }

    private String queryUpstream(String domain) {
        // Simulated latency
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        return "172.217.14." + (int)(Math.random() * 255);
    }

    private void recordTime(long startTime) {
        totalLookupTime += (System.nanoTime() - startTime);
    }

    public void getCacheStats() {
        double hitRate = (hits + misses == 0) ? 0 : (double) hits / (hits + misses) * 100;
        double avgTime = (hits + misses == 0) ? 0 : (totalLookupTime / 1_000_000.0) / (hits + misses);
        System.out.printf("Stats -> Hit Rate: %.1f%%, Avg Lookup Time: %.2fms\n", hitRate, avgTime);
    }

    public static void main(String[] args) throws InterruptedException {
        DNSCache cache = new DNSCache(2);
        cache.resolve("google.com");
        cache.resolve("google.com"); // Should hit
        cache.resolve("openai.com");
        cache.getCacheStats();
    }
}