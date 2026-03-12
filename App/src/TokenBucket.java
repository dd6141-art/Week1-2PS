public class TokenBucket {

    private double tokens; // Changed to double to track fractional refills
    private final int maxTokens;
    private final double refillRate; // tokens per second
    private long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens; // Start full
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    /**
     * Refills the bucket based on the time elapsed since the last check.
     * Uses double precision to ensure that fast requests don't "starve"
     * the bucket by constantly resetting the timer before a full integer
     * token can form.
     */
    private void refill() {
        long now = System.currentTimeMillis();
        double secondsElapsed = (now - lastRefillTime) / 1000.0;

        double tokensToAdd = secondsElapsed * refillRate;

        // Update tokens, capping at maxTokens
        tokens = Math.min(maxTokens, tokens + tokensToAdd);

        // Update the timestamp
        lastRefillTime = now;
    }

    /**
     * Attempts to consume 1 token.
     * @return true if a token was available and consumed, false otherwise.
     */
    public synchronized boolean allowRequest() {
        refill();

        if (tokens >= 1.0) {
            tokens -= 1.0;
            return true;
        }

        return false;
    }

    // Optional: Getter for monitoring
    public synchronized double getCurrentTokens() {
        refill();
        return tokens;
    }

    public static void main(String[] args) throws InterruptedException {
        // Allow 5 requests max, refills at 1 token per second
        TokenBucket bucket = new TokenBucket(5, 1.0);

        for (int i = 0; i < 10; i++) {
            if (bucket.allowRequest()) {
                System.out.println("Request " + i + ": Proceeding");
            } else {
                System.out.println("Request " + i + ": Rate Limited");
            }
            Thread.sleep(500); // Wait 0.5s between requests
        }
    }
}