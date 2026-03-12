import java.util.*;

public class UsernameChecker {

    // Stores registered usernames
    private HashMap<String, Integer> usernameMap = new HashMap<>();

    // Stores frequency of username attempts
    private HashMap<String, Integer> attemptFrequency = new HashMap<>();

    // Constructor with some existing users
    public UsernameChecker() {
        usernameMap.put("john_doe", 101);
        usernameMap.put("admin", 102);
        usernameMap.put("user123", 103);
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // track attempt frequency
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernameMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // add variation with dot
        suggestions.add(username.replace("_", "."));

        return suggestions;
    }

    // Find most attempted username
    public String getMostAttempted() {

        String maxUser = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxUser = entry.getKey();
            }
        }

        return maxUser + " (" + maxCount + " attempts)";
    }

    public static void main(String[] args) {

        UsernameChecker checker = new UsernameChecker();

        System.out.println("john_doe available? " +
                checker.checkAvailability("john_doe"));

        System.out.println("jane_smith available? " +
                checker.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe: " +
                checker.suggestAlternatives("john_doe"));

        System.out.println("Most attempted: " +
                checker.getMostAttempted());
    }
}