public class Candidate {

    private Resume resume;

    private double cosineSimilarityScore;
    private double skillScore;
    private double experienceScore;
    private double educationScore;
    private double combinedScore;

    public Candidate(Resume resume) {
        this.resume = resume;
    }

    public Resume getResume() {
        return resume;
    }

    public double getCosineSimilarityScore() {
        return cosineSimilarityScore;
    }

    public double getSkillScore() {
        return skillScore;
    }

    public double getExperienceScore() {
        return experienceScore;
    }

    public double getEducationScore() {
        return educationScore;
    }

    public double getCombinedScore() {
        return combinedScore;
    }

    public void setCosineSimilarityScore(double score) {
        this.cosineSimilarityScore = score;
    }

    public void setSkillScore(double score) {
        this.skillScore = score;
    }

    public void setExperienceScore(double score) {
        this.experienceScore = score;
    }

    public void setEducationScore(double score) {
        this.educationScore = score;
    }

    public void setCombinedScore(double score) {
        this.combinedScore = score;
    }

    @Override
    public String toString() {

        return "Candidate{" +
                "candidateId='" + resume.getCandidateId() + '\'' +
                ", name='" + resume.getName() + '\'' +
                ", cosineSimilarity=" + cosineSimilarityScore +
                ", skillScore=" + skillScore +
                ", experienceScore=" + experienceScore +
                ", educationScore=" + educationScore +
                ", combinedScore=" + combinedScore +
                '}';
    }
}