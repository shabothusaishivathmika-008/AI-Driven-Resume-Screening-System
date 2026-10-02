import java.util.List;
import java.util.Map;

public class ScoringEngine {

    private TFIDFCalculator tfidfCalculator;

    public ScoringEngine() {
        tfidfCalculator = new TFIDFCalculator();
    }

    // --------------------------------------------------
    // 1. COSINE SIMILARITY
    // --------------------------------------------------

    public double cosineSimilarity(
            Map<String, Double> vector1,
            Map<String, Double> vector2) {

        if (vector1 == null || vector2 == null ||
                vector1.isEmpty() || vector2.isEmpty()) {

            return 0.0;
        }

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        // Calculate dot product
        for (String word : vector1.keySet()) {

            if (vector2.containsKey(word)) {

                dotProduct +=
                        vector1.get(word) *
                        vector2.get(word);
            }
        }

        // Calculate magnitude of vector 1
        for (double value : vector1.values()) {

            magnitude1 += value * value;
        }

        // Calculate magnitude of vector 2
        for (double value : vector2.values()) {

            magnitude2 += value * value;
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        // Prevent NaN
        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }

    // --------------------------------------------------
    // 2. SKILL SCORE
    // --------------------------------------------------

    public double calculateSkillScore(
            Resume resume,
            JobDescription jobDescription) {

        List<String> resumeSkills =
                resume.getSkills();

        List<String> requiredSkills =
                jobDescription.getRequiredSkills();

        if (resumeSkills == null ||
                requiredSkills == null ||
                requiredSkills.isEmpty()) {

            return 0.0;
        }

        int matchedSkills = 0;

        for (String requiredSkill : requiredSkills) {

            for (String resumeSkill : resumeSkills) {

                if (resumeSkill.trim()
                        .equalsIgnoreCase(
                                requiredSkill.trim())) {

                    matchedSkills++;
                    break;
                }
            }
        }

        // +10 for every matched skill
        return matchedSkills * 10.0;
    }

    // --------------------------------------------------
    // 3. EXPERIENCE SCORE
    // --------------------------------------------------

    public double calculateExperienceScore(
            Resume resume,
            JobDescription jobDescription) {

        int candidateExperience =
                resume.getYearsOfExperience();

        int requiredExperience =
                jobDescription.getMinExperience();

        if (candidateExperience <= 0) {
            return 0.0;
        }

        // Maximum experience score = 30
        if (candidateExperience >= requiredExperience) {
            return 30.0;
        }

        // Partial score when experience is below requirement
        if (requiredExperience > 0) {

            return ((double) candidateExperience /
                    requiredExperience) * 30.0;
        }

        return 0.0;
    }

    // --------------------------------------------------
    // 4. EDUCATION SCORE
    // --------------------------------------------------

    public double calculateEducationScore(
            Resume resume,
            JobDescription jobDescription) {

        String candidateEducation =
                resume.getEducationLevel();

        String requiredEducation =
                jobDescription.getEducationRequired();

        if (candidateEducation == null ||
                requiredEducation == null ||
                candidateEducation.isEmpty() ||
                requiredEducation.isEmpty()) {

            return 0.0;
        }

        if (candidateEducation
                .equalsIgnoreCase(requiredEducation)) {

            return 20.0;
        }

        return 0.0;
    }

    // --------------------------------------------------
    // 5. COMBINED SCORE
    // --------------------------------------------------

    public Candidate calculateCandidateScore(
            Resume resume,
            JobDescription jobDescription) {

        Candidate candidate =
                new Candidate(resume);

        // Create documents for TF-IDF
        String[] documents = {
                jobDescription.getRawText(),
                resume.getRawText()
        };

        // Calculate IDF
        Map<String, Double> idf =
                tfidfCalculator.computeIDF(documents);

        // Calculate TF-IDF vectors
        Map<String, Double> jobVector =
                tfidfCalculator.computeTFIDF(
                        jobDescription.getRawText(),
                        idf
                );

        Map<String, Double> resumeVector =
                tfidfCalculator.computeTFIDF(
                        resume.getRawText(),
                        idf
                );

        // Cosine similarity
        double cosineScore =
                cosineSimilarity(
                        jobVector,
                        resumeVector
                );

        // Convert cosine similarity to 0-30
        double cosinePoints =
                cosineScore * 30.0;

        // Other scores
        double skillScore =
                calculateSkillScore(
                        resume,
                        jobDescription
                );

        double experienceScore =
                calculateExperienceScore(
                        resume,
                        jobDescription
                );

        double educationScore =
                calculateEducationScore(
                        resume,
                        jobDescription
                );

        // Final combined score
        double combinedScore =
                cosinePoints +
                skillScore +
                experienceScore +
                educationScore;

        // Store scores inside Candidate
        candidate.setCosineSimilarityScore(
                cosinePoints
        );

        candidate.setSkillScore(
                skillScore
        );

        candidate.setExperienceScore(
                experienceScore
        );

        candidate.setEducationScore(
                educationScore
        );

        candidate.setCombinedScore(
                combinedScore
        );

        return candidate;
    }
}