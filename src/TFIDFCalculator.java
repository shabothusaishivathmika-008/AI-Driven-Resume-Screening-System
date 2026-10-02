import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TFIDFCalculator {

    // Calculate Term Frequency (TF)
    public Map<String, Double> computeTF(String text) {

        Map<String, Double> tf = new HashMap<>();

        if (text == null || text.trim().isEmpty()) {
            return tf;
        }

        // Convert text to lowercase and split into words
        String[] words = text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .split("\\s+");

        if (words.length == 0) {
            return tf;
        }

        // Count each word
        for (String word : words) {

            if (!word.isEmpty()) {
                tf.merge(word, 1.0, Double::sum);
            }
        }

        // Convert frequency into Term Frequency
        int totalWords = words.length;

        tf.replaceAll(
                (word, count) -> count / totalWords
        );

        return tf;
    }

    // Calculate Inverse Document Frequency (IDF)
    public Map<String, Double> computeIDF(
            String[] documents) {

        Map<String, Integer> documentFrequency =
                new HashMap<>();

        int totalDocuments = documents.length;

        // Find how many documents contain each word
        for (String document : documents) {

            if (document == null ||
                    document.trim().isEmpty()) {
                continue;
            }

            Map<String, Double> tf =
                    computeTF(document);

            Set<String> uniqueWords =
                    new HashSet<>(tf.keySet());

            for (String word : uniqueWords) {

                documentFrequency.merge(
                        word,
                        1,
                        Integer::sum
                );
            }
        }

        // Calculate IDF
        Map<String, Double> idf =
                new HashMap<>();

        for (Map.Entry<String, Integer> entry :
                documentFrequency.entrySet()) {

            String word = entry.getKey();
            int frequency = entry.getValue();

            double value = Math.log(
                    (double) totalDocuments /
                    (1 + frequency)
            );

            idf.put(word, value);
        }

        return idf;
    }

    // Calculate TF-IDF vector
    public Map<String, Double> computeTFIDF(
            String text,
            Map<String, Double> idf) {

        Map<String, Double> tf =
                computeTF(text);

        Map<String, Double> tfidf =
                new HashMap<>();

        for (Map.Entry<String, Double> entry :
                tf.entrySet()) {

            String word = entry.getKey();

            double tfValue = entry.getValue();

            double idfValue =
                    idf.getOrDefault(word, 0.0);

            tfidf.put(
                    word,
                    tfValue * idfValue
            );
        }

        return tfidf;
    }
}