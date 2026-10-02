import java.io.File;
import java.util.List;

public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        System.out.println();
        System.out.println("========================================");
        System.out.println("       AI RESUME SCREENING TESTS");
        System.out.println("========================================");

        testJobDescriptionFile();
        testResumeFolder();
        testResumeParsing();
        testJobDescriptionParsing();
        testScoring();
        testRanking();
        testMissingFileHandling();
        testCache();
        testPopularityTracker();
        testMultithreading();

        System.out.println();
        System.out.println("========================================");
        System.out.println("             TEST SUMMARY");
        System.out.println("========================================");

        System.out.println(
                "Tests Passed: " + passed
        );

        System.out.println(
                "Tests Failed: " + failed
        );

        System.out.println(
                "Total Tests: " + (passed + failed)
        );

        System.out.println("========================================");

        if (failed == 0) {

            System.out.println(
                    "ALL TESTS PASSED!"
            );

        } else {

            System.out.println(
                    "SOME TESTS FAILED."
            );
        }
    }

    private static void testJobDescriptionFile() {

        File file =
                new File("data/jobdesc.txt");

        check(
                "Job description file exists",
                file.exists() && file.isFile()
        );
    }

    private static void testResumeFolder() {

        File folder =
                new File("data/resumes");

        check(
                "Resume folder exists",
                folder.exists() && folder.isDirectory()
        );

        if (folder.exists() && folder.isDirectory()) {

            File[] files =
                    folder.listFiles();

            int resumeCount = 0;

            if (files != null) {

                for (File file : files) {

                    if (file.isFile() &&
                            file.getName()
                                    .toLowerCase()
                                    .endsWith(".txt")) {

                        resumeCount++;
                    }
                }
            }

            check(
                    "Eight resume files exist",
                    resumeCount == 8
            );
        }
    }

    private static void testResumeParsing() {

        try {

            Resume resume =
                    TextParser.parseResume(
                            "data/resumes/resume_01.txt"
                    );

            check(
                    "Resume candidate ID parsed",
                    resume.getCandidateId()
                            .equals("C001")
            );

            check(
                    "Resume name parsed",
                    resume.getName()
                            .equals("Rahul Sharma")
            );

            check(
                    "Resume skills parsed",
                    resume.getSkills().size() == 5
            );

            check(
                    "Resume experience parsed",
                    resume.getYearsOfExperience() == 4
            );

        } catch (Exception e) {

            check(
                    "Resume parsing",
                    false
            );
        }
    }

    private static void testJobDescriptionParsing() {

        try {

            JobDescription jobDescription =
                    TextParser.parseJobDescription(
                            "data/jobdesc.txt"
                    );

            check(
                    "Job role parsed",
                    jobDescription
                            .getRole()
                            .equals("Software Engineer")
            );

            check(
                    "Required skills parsed",
                    jobDescription
                            .getRequiredSkills()
                            .size() == 5
            );

            check(
                    "Minimum experience parsed",
                    jobDescription
                            .getMinExperience() == 2
            );

            check(
                    "Education requirement parsed",
                    jobDescription
                            .getEducationRequired()
                            .equals("BTech")
            );

        } catch (Exception e) {

            check(
                    "Job description parsing",
                    false
            );
        }
    }

    private static void testScoring() {

        try {

            ScreeningSystem system =
                    new ScreeningSystem();

            JobDescription jobDescription =
                    system.loadJobDescription(
                            "data/jobdesc.txt"
                    );

            List<Resume> resumes =
                    system.loadResumes(
                            "data/resumes"
                    );

            List<Candidate> candidates =
                    system.screenResumes(
                            jobDescription,
                            resumes
                    );

            check(
                    "All eight resumes scored",
                    candidates.size() == 8
            );

            if (!candidates.isEmpty()) {

                check(
                        "Candidate has a valid score",
                        !Double.isNaN(
                                candidates
                                        .get(0)
                                        .getCombinedScore()
                        )
                );
            }

        } catch (Exception e) {

            check(
                    "Scoring system",
                    false
            );
        }
    }

    private static void testRanking() {

        try {

            ScreeningSystem system =
                    new ScreeningSystem();

            JobDescription jobDescription =
                    system.loadJobDescription(
                            "data/jobdesc.txt"
                    );

            List<Resume> resumes =
                    system.loadResumes(
                            "data/resumes"
                    );

            List<Candidate> candidates =
                    system.screenResumes(
                            jobDescription,
                            resumes
                    );

            boolean correctlyRanked = true;

            for (int i = 0;
                 i < candidates.size() - 1;
                 i++) {

                double current =
                        candidates
                                .get(i)
                                .getCombinedScore();

                double next =
                        candidates
                                .get(i + 1)
                                .getCombinedScore();

                if (current < next) {

                    correctlyRanked = false;
                    break;
                }
            }

            check(
                    "Candidates ranked by descending score",
                    correctlyRanked
            );

        } catch (Exception e) {

            check(
                    "Candidate ranking",
                    false
            );
        }
    }

    private static void testMissingFileHandling() {

        try {

            ScreeningSystem system =
                    new ScreeningSystem();

            system.loadJobDescription(
                    "data/missing_jobdesc.txt"
            );

            check(
                    "Missing job description handled",
                    false
            );

        } catch (Exception e) {

            check(
                    "Missing job description handled",
                    true
            );
        }
    }

    private static void testCache() {

        ResultCache cache =
                new ResultCache();

        boolean initiallyEmpty =
                !cache.containsResult(
                        "Software Engineer"
                );

        check(
                "Cache initially empty",
                initiallyEmpty
        );

        try {

            ScreeningSystem system =
                    new ScreeningSystem();

            JobDescription jobDescription =
                    system.loadJobDescription(
                            "data/jobdesc.txt"
                    );

            List<Resume> resumes =
                    system.loadResumes(
                            "data/resumes"
                    );

            List<Candidate> candidates =
                    system.screenResumes(
                            jobDescription,
                            resumes
                    );

            cache.storeResult(
                    "Software Engineer",
                    candidates
            );

            check(
                    "Result stored in cache",
                    cache.containsResult(
                            "Software Engineer"
                    )
            );

            check(
                    "Cached result retrieved",
                    cache.getResult(
                            "Software Engineer"
                    ) != null
            );

        } catch (Exception e) {

            check(
                    "Cache functionality",
                    false
            );
        }
    }

    private static void testPopularityTracker() {

        PopularityTracker tracker =
                new PopularityTracker();

        tracker.recordShortlist("C001");
        tracker.recordShortlist("C001");
        tracker.recordShortlist("C005");

        check(
                "Popularity tracker counts C001",
                tracker.getShortlistCount("C001") == 2
        );

        check(
                "Popularity tracker counts C005",
                tracker.getShortlistCount("C005") == 1
        );

        check(
                "Popularity tracker handles unknown candidate",
                tracker.getShortlistCount("C999") == 0
        );
    }

    private static void testMultithreading() {

        try {

            ScreeningSystem system =
                    new ScreeningSystem();

            JobDescription jobDescription =
                    system.loadJobDescription(
                            "data/jobdesc.txt"
                    );

            List<Resume> resumes =
                    system.loadResumes(
                            "data/resumes"
                    );

            MultithreadedScreening multithreadedScreening =
                    new MultithreadedScreening();

            List<Candidate> candidates =
                    multithreadedScreening
                            .screenResumesWithThreads(
                                    jobDescription,
                                    resumes,
                                    5
                            );

            check(
                    "Multithreaded screening processes all resumes",
                    candidates.size() == 8
            );

        } catch (Exception e) {

            check(
                    "Multithreaded screening",
                    false
            );
        }
    }

    private static void check(
            String testName,
            boolean condition) {

        if (condition) {

            System.out.println(
                    "[PASS] " + testName
            );

            passed++;

        } else {

            System.out.println(
                    "[FAIL] " + testName
            );

            failed++;
        }
    }
}
