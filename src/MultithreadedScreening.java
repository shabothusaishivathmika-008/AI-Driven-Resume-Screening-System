import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultithreadedScreening {

    private ScoringEngine scoringEngine;

    public MultithreadedScreening() {
        scoringEngine = new ScoringEngine();
    }

    public List<Candidate> screenResumesWithThreads(
            JobDescription jobDescription,
            List<Resume> resumes,
            int numberOfThreads) {

        List<Candidate> candidates =
                Collections.synchronizedList(
                        new ArrayList<>()
                );

        if (jobDescription == null) {

            System.out.println(
                    "ERROR: Job description is missing."
            );

            return candidates;
        }

        if (resumes == null || resumes.isEmpty()) {

            System.out.println(
                    "ERROR: No resumes found."
            );

            return candidates;
        }

        if (numberOfThreads <= 0) {

            System.out.println(
                    "ERROR: Number of threads must be greater than 0."
            );

            return candidates;
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        numberOfThreads
                );

        for (Resume resume : resumes) {

            executor.submit(() -> {

                try {

                    System.out.println(
                            "Thread " +
                            Thread.currentThread().getName() +
                            " screening " +
                            resume.getCandidateId()
                    );

                    Candidate candidate =
                            scoringEngine.calculateCandidateScore(
                                    resume,
                                    jobDescription
                            );

                    candidates.add(candidate);

                } catch (Exception e) {

                    System.out.println(
                            "ERROR while screening "
                                    + resume.getCandidateId()
                    );

                    System.out.println(
                            "Reason: " + e.getMessage()
                    );
                }
            });
        }

        executor.shutdown();

        try {

            if (!executor.awaitTermination(
                    30,
                    TimeUnit.SECONDS)) {

                System.out.println(
                        "WARNING: Screening took too long."
                );

                executor.shutdownNow();
            }

        } catch (InterruptedException e) {

            System.out.println(
                    "ERROR: Screening was interrupted."
            );

            executor.shutdownNow();

            Thread.currentThread().interrupt();
        }

        return candidates;
    }
}