import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; // Fixes "Cannot resolve symbol 'List'"
import java.util.Map;

// 1. Define Transaction class (Fixes visibility scope error)
class Transaction {
    int id;
    int amount;

    public Transaction(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", amount=" + amount + "}";
    }
}

public class TwoSumTransactions {

    /**
     * Finds two transactions whose amounts sum up to the target.
     * Uses a HashMap for O(n) time complexity.
     */
    public static List<Transaction> findTwoSum(List<Transaction> transactions, int target) {
        Map<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                List<Transaction> result = new ArrayList<>();
                result.add(map.get(complement));
                result.add(t);
                return result;
            }

            map.put(t.amount, t);
        }
        return new ArrayList<>(); // Return empty list if no pair found
    }

    public static void main(String[] args) {
        // Sample usage to clear "is never used" warnings
        List<Transaction> txns = new ArrayList<>();
        txns.add(new Transaction(1, 100));
        txns.add(new Transaction(2, 250));
        txns.add(new Transaction(3, 150));

        int target = 400;
        List<Transaction> pair = findTwoSum(txns, target);

        if (!pair.isEmpty()) {
            System.out.println("Found pair: " + pair.get(0) + " and " + pair.get(1));
        } else {
            System.out.println("No pair found for target " + target);
        }
    }
}