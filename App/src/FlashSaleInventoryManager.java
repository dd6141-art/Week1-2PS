import java.util.*;

public class FlashSaleInventoryManager {

    // Product stock table
    private HashMap<String, Integer> stockMap = new HashMap<>();

    // Waiting list (FIFO)
    private LinkedHashMap<Integer, String> waitingList = new LinkedHashMap<>();

    public FlashSaleInventoryManager() {
        stockMap.put("IPHONE15_256GB", 100);
    }

    // Check stock availability
    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    // Thread-safe purchase method
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        }

        else {
            waitingList.put(userId, productId);
            return "Added to waiting list, position #" + waitingList.size();
        }
    }

    // View waiting list
    public void displayWaitingList() {
        System.out.println(waitingList);
    }

    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        System.out.println(
                "Stock: " + manager.checkStock("IPHONE15_256GB") + " units available"
        );

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}