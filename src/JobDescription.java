import java.util.ArrayList;
import java.util.List;

public class JobDescription {

    private String role;
    private List<String> requiredSkills;
    private int minExperience;
    private String educationRequired;
    private String rawText;

    public JobDescription(String role,
                          List<String> requiredSkills,
                          int minExperience,
                          String educationRequired,
                          String rawText) {

        this.role = role;

        // Prevent null skills
        this.requiredSkills = requiredSkills != null
                ? requiredSkills
                : new ArrayList<>();

        // Prevent negative experience
        this.minExperience = Math.max(0, minExperience);

        this.educationRequired = educationRequired;
        this.rawText = rawText;
    }

    public String getRole() {
        return role;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public String getEducationRequired() {
        return educationRequired;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills != null
                ? requiredSkills
                : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "JobDescription{" +
                "role='" + role + '\'' +
                ", requiredSkills=" + requiredSkills +
                ", minExperience=" + minExperience +
                ", educationRequired='" + educationRequired + '\'' +
                '}';
    }
}