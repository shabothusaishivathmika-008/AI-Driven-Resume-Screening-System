import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ScreeningSystem screeningSystem =
                new ScreeningSystem();

        MultithreadedScreening multithreadedScreening =
                new MultithreadedScreening();

        ResultCache resultCache =
                new ResultCache();

        PopularityTracker popularityTracker =
                new PopularityTracker();

        String jobDescriptionPath =
                "data/jobdesc.txt";

        String resumesFolderPath =
                "data/resumes";

        JobDescription jobDescription;

        try {

            jobDescription =
                    screeningSystem.loadJobDescription(
                            jobDescriptionPath
                    );

        } catch (IOException e) {

            System.out.println();
            System.out.println(
                    "ERROR: Could not load job description."
            );

            System.out.println(
                    "Reason: " + e.getMessage()
            );

            scanner.close();
            return;
        }

        List<Resume> resumes =
                screeningSystem.loadResumes(
                        resumesFolderPath
                );

        if (resumes.isEmpty()) {

            System.out.println();
            System.out.println(
                    "ERROR: No resume files were found."
            );

            scanner.close();
            return;
        }

        List<Candidate> rankedCandidates = null;

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "     AI RESUME SCREENING SYSTEM"
            );

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Job Role: "
                            + jobDescription.getRole()
            );

            System.out.println(
                    "Resumes Loaded: "
                            + resumes.size()
            );

            System.out.println();

            System.out.println(
                    "1. Screen Resumes"
            );

            System.out.println(
                    "2. View Top Candidates"
            );

            System.out.println(
                    "3. View Candidate Profile"
            );

            System.out.println(
                    "4. Exit"
            );

            System.out.println(
                    "5. Multithreaded Screening"
            );

            System.out.println(
                    "6. Cached Screening"
            );

            System.out.println(
                    "7. View Candidate Popularity"
            );

            System.out.println();

            System.out.print(
                    "Enter your choice: "
            );

            String choice =
                    scanner.nextLine().trim();

            if (choice.equals("1")) {

                System.out.println();

                System.out.println(
                        "Screening resumes..."
                );

                rankedCandidates =
                        screeningSystem.screenResumes(
                                jobDescription,
                                resumes
                        );

                System.out.println();

                System.out.println(
                        "Resume screening completed."
                );

                screeningSystem
                        .getRankingEngine()
                        .displayRankedCandidates(
                                rankedCandidates
                        );
            }

            else if (choice.equals("2")) {

                if (rankedCandidates == null ||
                        rankedCandidates.isEmpty()) {

                    System.out.println();

                    System.out.println(
                            "Please screen the resumes first."
                    );

                } else {

                    System.out.println();

                    System.out.println(
                            "========== TOP CANDIDATES =========="
                    );

                    int limit =
                            Math.min(
                                    3,
                                    rankedCandidates.size()
                            );

                    for (int i = 0;
                         i < limit;
                         i++) {

                        Candidate candidate =
                                rankedCandidates.get(i);

                        String candidateId =
                                candidate
                                        .getResume()
                                        .getCandidateId();

                        String candidateName =
                                candidate
                                        .getResume()
                                        .getName();

                        double score =
                                candidate
                                        .getCombinedScore();

                        System.out.printf(
                                "%d. %s - Score: %.2f%n",
                                i + 1,
                                candidateName,
                                score
                        );

                        popularityTracker.recordShortlist(
                                candidateId
                        );
                    }

                    System.out.println(
                            "===================================="
                    );

                    System.out.println(
                            "Top candidates recorded in popularity tracker."
                    );
                }
            }

            else if (choice.equals("3")) {

                if (rankedCandidates == null ||
                        rankedCandidates.isEmpty()) {

                    System.out.println();

                    System.out.println(
                            "Please screen the resumes first."
                    );

                } else {

                    System.out.print(
                            "Enter Candidate ID: "
                    );

                    String candidateId =
                            scanner.nextLine().trim();

                    Candidate selectedCandidate =
                            null;

                    for (Candidate candidate :
                            rankedCandidates) {

                        if (candidate
                                .getResume()
                                .getCandidateId()
                                .equalsIgnoreCase(
                                        candidateId
                                )) {

                            selectedCandidate =
                                    candidate;

                            break;
                        }
                    }

                    if (selectedCandidate == null) {

                        System.out.println();

                        System.out.println(
                                "Candidate not found."
                        );

                    } else {

                        screeningSystem
                                .displayCandidateDetails(
                                        selectedCandidate
                                );
                    }
                }
            }

            else if (choice.equals("4")) {

                running = false;

                System.out.println();

                System.out.println(
                        "Thank you for using the "
                                + "AI Resume Screening System!"
                );
            }

            else if (choice.equals("5")) {

                System.out.println();

                System.out.println(
                        "========== MULTITHREADED SCREENING =========="
                );

                System.out.print(
                        "Enter number of threads: "
                );

                String threadInput =
                        scanner.nextLine().trim();

                try {

                    int numberOfThreads =
                            Integer.parseInt(
                                    threadInput
                            );

                    if (numberOfThreads <= 0) {

                        System.out.println();

                        System.out.println(
                                "Number of threads must "
                                        + "be greater than 0."
                        );

                    } else {

                        System.out.println();

                        System.out.println(
                                "Starting multithreaded screening..."
                        );

                        List<Candidate> threadedCandidates =
                                multithreadedScreening
                                        .screenResumesWithThreads(
                                                jobDescription,
                                                resumes,
                                                numberOfThreads
                                        );

                        System.out.println();

                        System.out.println(
                                "Multithreaded screening completed."
                        );

                        System.out.println(
                                "Candidates screened: "
                                        + threadedCandidates.size()
                        );

                        System.out.println(
                                "============================================"
                        );
                    }

                } catch (NumberFormatException e) {

                    System.out.println();

                    System.out.println(
                            "Invalid number of threads."
                    );

                    System.out.println(
                            "Please enter a whole number."
                    );
                }
            }

            else if (choice.equals("6")) {

                System.out.println();

                System.out.println(
                        "========== CACHED SCREENING =========="
                );

                String jobRole =
                        jobDescription.getRole();

                if (resultCache.containsResult(jobRole)) {

                    System.out.println();

                    System.out.println(
                            "Result found in cache!"
                    );

                    rankedCandidates =
                            resultCache.getResult(
                                    jobRole
                            );

                    System.out.println();

                    System.out.println(
                            "Using previously calculated results."
                    );

                    screeningSystem
                            .getRankingEngine()
                            .displayRankedCandidates(
                                    rankedCandidates
                            );

                } else {

                    System.out.println();

                    System.out.println(
                            "No cached result found."
                    );

                    System.out.println(
                            "Screening resumes now..."
                    );

                    rankedCandidates =
                            screeningSystem.screenResumes(
                                    jobDescription,
                                    resumes
                            );

                    resultCache.storeResult(
                            jobRole,
                            rankedCandidates
                    );

                    System.out.println();

                    System.out.println(
                            "Screening completed."
                    );

                    System.out.println(
                            "Results stored in cache."
                    );

                    screeningSystem
                            .getRankingEngine()
                            .displayRankedCandidates(
                                    rankedCandidates
                            );
                }

                System.out.println(
                        "======================================"
                );
            }

            else if (choice.equals("7")) {

                popularityTracker.displayPopularity();

            }

            else {

                System.out.println();

                System.out.println(
                        "Invalid choice."
                );

                System.out.println(
                        "Please enter 1, 2, 3, 4, 5, 6, or 7."
                );
            }
        }

        scanner.close();
    }
}