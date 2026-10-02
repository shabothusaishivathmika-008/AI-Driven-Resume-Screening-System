import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScreeningSystem {

    private ScoringEngine scoringEngine;
    private RankingEngine rankingEngine;

    public ScreeningSystem() {
        scoringEngine = new ScoringEngine();
        rankingEngine = new RankingEngine();
    }

    public JobDescription loadJobDescription(
            String filePath) throws IOException {

        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException(
                    "Job description file not found: " + filePath
            );
        }

        if (!file.isFile()) {
            throw new IOException(
                    "Job description path is not a file: " + filePath
            );
        }

        return TextParser.parseJobDescription(filePath);
    }

    public List<Resume> loadResumes(
            String folderPath) {

        List<Resume> resumes = new ArrayList<>();

        File folder = new File(folderPath);

        // Check whether the folder exists
        if (!folder.exists()) {

            System.out.println(
                    "ERROR: Resume folder not found: "
                            + folderPath
            );

            return resumes;
        }

        // Check whether the path is actually a folder
        if (!folder.isDirectory()) {

            System.out.println(
                    "ERROR: Resume path is not a folder: "
                            + folderPath
            );

            return resumes;
        }

        File[] files = folder.listFiles();

        // Check whether Java could read the folder
        if (files == null) {

            System.out.println(
                    "ERROR: Could not read resume folder."
            );

            return resumes;
        }

        for (File file : files) {

            // Ignore folders
            if (!file.isFile()) {
                continue;
            }

            // Only process .txt files
            if (!file.getName()
                    .toLowerCase()
                    .endsWith(".txt")) {

                continue;
            }

            try {

                Resume resume =
                        TextParser.parseResume(
                                file.getPath()
                        );

                // Validate basic resume information
                if (resume.getCandidateId() == null ||
                        resume.getCandidateId().trim().isEmpty()) {

                    System.out.println(
                            "WARNING: Missing Candidate ID in "
                                    + file.getName()
                    );

                    continue;
                }

                if (resume.getName() == null ||
                        resume.getName().trim().isEmpty()) {

                    System.out.println(
                            "WARNING: Missing candidate name in "
                                    + file.getName()
                    );
                }

                if (resume.getSkills() == null ||
                        resume.getSkills().isEmpty()) {

                    System.out.println(
                            "WARNING: No skills found in "
                                    + file.getName()
                    );
                }

                resumes.add(resume);

            } catch (IOException e) {

                System.out.println(
                        "ERROR: Could not read file: "
                                + file.getName()
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );
            }
        }

        return resumes;
    }

    public List<Candidate> screenResumes(
            JobDescription jobDescription,
            List<Resume> resumes) {

        List<Candidate> candidates =
                new ArrayList<>();

        if (jobDescription == null) {

            System.out.println(
                    "ERROR: Job description is missing."
            );

            return candidates;
        }

        if (resumes == null || resumes.isEmpty()) {

            System.out.println(
                    "ERROR: No valid resumes found."
            );

            return candidates;
        }

        for (Resume resume : resumes) {

            if (resume == null) {

                System.out.println(
                        "WARNING: Skipping null resume."
                );

                continue;
            }

            try {

                Candidate candidate =
                        scoringEngine.calculateCandidateScore(
                                resume,
                                jobDescription
                        );

                candidates.add(candidate);

            } catch (Exception e) {

                System.out.println(
                        "ERROR: Could not score candidate "
                                + resume.getCandidateId()
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );
            }
        }

        return rankingEngine.rankCandidates(
                candidates
        );
    }

    public void displayCandidateDetails(
            Candidate candidate) {

        if (candidate == null) {

            System.out.println(
                    "Candidate not found."
            );

            return;
        }

        Resume resume = candidate.getResume();

        System.out.println();
        System.out.println(
                "========== CANDIDATE PROFILE =========="
        );

        System.out.println(
                "Candidate ID: "
                        + resume.getCandidateId()
        );

        System.out.println(
                "Name: "
                        + resume.getName()
        );

        System.out.println(
                "Skills: "
                        + resume.getSkills()
        );

        System.out.println(
                "Experience: "
                        + resume.getYearsOfExperience()
                        + " years"
        );

        System.out.println(
                "Education: "
                        + resume.getEducationLevel()
        );

        System.out.printf(
                "Cosine Similarity Score: %.2f%n",
                candidate.getCosineSimilarityScore()
        );

        System.out.printf(
                "Skill Score: %.2f%n",
                candidate.getSkillScore()
        );

        System.out.printf(
                "Experience Score: %.2f%n",
                candidate.getExperienceScore()
        );

        System.out.printf(
                "Education Score: %.2f%n",
                candidate.getEducationScore()
        );

        System.out.printf(
                "Combined Score: %.2f%n",
                candidate.getCombinedScore()
        );

        System.out.println(
                "========================================"
        );
    }

    public RankingEngine getRankingEngine() {
        return rankingEngine;
    }
}