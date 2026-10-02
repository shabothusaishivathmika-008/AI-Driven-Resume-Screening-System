import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultCache {

    private Map<String, List<Candidate>> cache;

    public ResultCache() {
        cache = new HashMap<>();
    }

    public void storeResult(
            String jobRole,
            List<Candidate> candidates) {

        if (jobRole == null ||
                jobRole.trim().isEmpty()) {

            return;
        }

        if (candidates == null) {

            return;
        }

        cache.put(
                jobRole.toLowerCase(),
                candidates
        );
    }

    public List<Candidate> getResult(
            String jobRole) {

        if (jobRole == null ||
                jobRole.trim().isEmpty()) {

            return null;
        }

        return cache.get(
                jobRole.toLowerCase()
        );
    }

    public boolean containsResult(
            String jobRole) {

        if (jobRole == null ||
                jobRole.trim().isEmpty()) {

            return false;
        }

        return cache.containsKey(
                jobRole.toLowerCase()
        );
    }

    public void clearCache() {

        cache.clear();
    }

    public int getCacheSize() {

        return cache.size();
    }
}