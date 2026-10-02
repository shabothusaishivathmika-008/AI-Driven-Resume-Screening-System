import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class RankingEngine {

    // Rank candidates from highest score to lowest score
    public List<Candidate> rankCandidates(
            List<Candidate> candidates) {

        if (candidates == null ||
                candidates.isEmpty()) {

            return new ArrayList<>();
        }

        // PriorityQueue keeps the highest-scoring
        // candidate at the front
        PriorityQueue<Candidate> queue =
                new PriorityQueue<>(
                        Comparator.comparingDouble(
                                Candidate::getCombinedScore
                        ).reversed()
                );

        // Add all candidates to the queue
        queue.addAll(candidates);

        List<Candidate> rankedCandidates =
                new ArrayList<>();

        // Remove candidates in ranking order
        while (!queue.isEmpty()) {

            rankedCandidates.add(
                    queue.poll()
            );
        }

        return rankedCandidates;
    }

    // Display the ranked candidates
    public void displayRankedCandidates(
            List<Candidate> rankedCandidates) {

        if (rankedCandidates == null ||
                rankedCandidates.isEmpty()) {

            System.out.println(
                    "No candidates available."
            );

            return;
        }

        System.out.println();
        System.out.println(
                "========== RANKED CANDIDATES =========="
        );

        int rank = 1;

        for (Candidate candidate :
                rankedCandidates) {

            System.out.println(
                    rank + ". " +
                    candidate.getResume().getName() +
                    " (" +
                    candidate.getResume().getCandidateId() +
                    ")"
            );

            System.out.printf(
                    "   Combined Score: %.2f%n",
                    candidate.getCombinedScore()
            );

            rank++;
        }

        System.out.println(
                "========================================"
        );
    }
}