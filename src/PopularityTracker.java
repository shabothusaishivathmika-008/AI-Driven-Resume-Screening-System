import java.util.HashMap;
import java.util.Map;

public class PopularityTracker {

    private Map<String, Integer> popularityMap;

    public PopularityTracker() {
        popularityMap = new HashMap<>();
    }

    public void recordShortlist(String candidateId) {

        if (candidateId == null ||
                candidateId.trim().isEmpty()) {

            return;
        }

        String id =
                candidateId.trim().toUpperCase();

        popularityMap.put(
                id,
                popularityMap.getOrDefault(id, 0) + 1
        );
    }

    public int getShortlistCount(String candidateId) {

        if (candidateId == null ||
                candidateId.trim().isEmpty()) {

            return 0;
        }

        String id =
                candidateId.trim().toUpperCase();

        return popularityMap.getOrDefault(
                id,
                0
        );
    }

    public void displayPopularity() {

        System.out.println();
        System.out.println(
                "========== CANDIDATE POPULARITY =========="
        );

        if (popularityMap.isEmpty()) {

            System.out.println(
                    "No candidates have been shortlisted yet."
            );

        } else {

            for (Map.Entry<String, Integer> entry :
                    popularityMap.entrySet()) {

                System.out.println(
                        entry.getKey()
                                + " → Shortlisted "
                                + entry.getValue()
                                + " time(s)"
                );
            }
        }

        System.out.println(
                "==========================================="
        );
    }
}