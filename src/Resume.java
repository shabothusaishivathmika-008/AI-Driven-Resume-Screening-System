import java.util.ArrayList;
import java.util.List;

public class Resume {

    private String candidateId;
    private String name;
    private List<String> skills;
    private int yearsOfExperience;
    private String educationLevel;
    private String rawText;

    public Resume(String candidateId,
                  String name,
                  List<String> skills,
                  int yearsOfExperience,
                  String educationLevel,
                  String rawText) {

        this.candidateId = candidateId;
        this.name = name;

        // Prevent null skills
        this.skills = skills != null ? skills : new ArrayList<>();

        // Prevent negative experience
        this.yearsOfExperience = Math.max(0, yearsOfExperience);

        this.educationLevel = educationLevel;
        this.rawText = rawText;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public String getName() {
        return name;
    }

    public List<String> getSkills() {
        return skills;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public String getRawText() {
        return rawText;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills != null ? skills : new ArrayList<>();
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = Math.max(0, yearsOfExperience);
    }

    @Override
    public String toString() {
        return "Resume{" +
                "candidateId='" + candidateId + '\'' +
                ", name='" + name + '\'' +
                ", skills=" + skills +
                ", yearsOfExperience=" + yearsOfExperience +
                ", educationLevel='" + educationLevel + '\'' +
                '}';
    }
}