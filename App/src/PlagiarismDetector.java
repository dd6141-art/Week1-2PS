import java.util.*;

public class PlagiarismDetector {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    private int N = 5; // size of n-gram

    // Extract n-grams
    public List<String> extractNGrams(String text) {

        String[] words = text.toLowerCase().split(" ");
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    // Add document to index
    public void indexDocument(String docId, String text) {

        List<String> ngrams = extractNGrams(text);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }
    }

    // Analyze document similarity
    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = extractNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String doc : ngramIndex.get(gram)) {

                    matchCount.put(doc,
                            matchCount.getOrDefault(doc, 0) + 1);
                }
            }
        }

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Matches with " + doc +
                    " → " + similarity + "% similarity");
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        detector.indexDocument("essay_089",
                "machine learning improves computer intelligence systems");

        detector.indexDocument("essay_092",
                "machine learning improves computer intelligence and automation");

        detector.analyzeDocument("essay_123",
                "machine learning improves computer intelligence systems rapidly");
    }
}