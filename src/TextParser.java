import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextParser {

    public static Resume parseResume(String filePath) throws IOException {

        String candidateId = "";
        String name = "";
        List<String> skills = new ArrayList<>();
        int yearsOfExperience = 0;
        String educationLevel = "";
        StringBuilder rawText = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {

                rawText.append(line).append("\n");

                String normalizedLine = line.trim();

                if (normalizedLine.toLowerCase().startsWith("candidate id:")) {
                    candidateId = getValue(normalizedLine);
                }

                else if (normalizedLine.toLowerCase().startsWith("name:")) {
                    name = getValue(normalizedLine);
                }

                else if (normalizedLine.toLowerCase().startsWith("skills:")) {

                    String skillText = getValue(normalizedLine);

                    if (!skillText.isEmpty()) {

                        String[] skillArray = skillText.split(",");

                        for (String skill : skillArray) {

                            String cleanedSkill = skill.trim();

                            if (!cleanedSkill.isEmpty()) {
                                skills.add(cleanedSkill.toLowerCase());
                            }
                        }
                    }
                }

                else if (normalizedLine.toLowerCase()
                        .startsWith("experience:")) {

                    String experienceText = getValue(normalizedLine);

                    try {
                        yearsOfExperience = Integer.parseInt(
                                experienceText
                        );
                    } catch (NumberFormatException e) {
                        yearsOfExperience = 0;
                    }
                }

                else if (normalizedLine.toLowerCase()
                        .startsWith("education:")) {

                    educationLevel = getValue(normalizedLine);
                }
            }
        }

        return new Resume(
                candidateId,
                name,
                skills,
                yearsOfExperience,
                educationLevel,
                rawText.toString()
        );
    }

    private static String getValue(String line) {

        int colonIndex = line.indexOf(":");

        if (colonIndex == -1) {
            return "";
        }

        return line.substring(colonIndex + 1).trim();
    }

    public static JobDescription parseJobDescription(
            String filePath) throws IOException {

        String role = "";
        List<String> requiredSkills = new ArrayList<>();
        int minExperience = 0;
        String educationRequired = "";
        StringBuilder rawText = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {

                rawText.append(line).append("\n");

                String normalizedLine = line.trim();

                if (normalizedLine.toLowerCase()
                        .startsWith("role:")) {

                    role = getValue(normalizedLine);
                }

                else if (normalizedLine.toLowerCase()
                        .startsWith("required skills:")) {

                    String skillText = getValue(normalizedLine);

                    if (!skillText.isEmpty()) {

                        String[] skillArray = skillText.split(",");

                        for (String skill : skillArray) {

                            String cleanedSkill = skill.trim();

                            if (!cleanedSkill.isEmpty()) {
                                requiredSkills.add(
                                        cleanedSkill.toLowerCase()
                                );
                            }
                        }
                    }
                }

                else if (normalizedLine.toLowerCase()
                        .startsWith("minimum experience:")) {

                    String experienceText = getValue(normalizedLine);

                    try {
                        minExperience = Integer.parseInt(
                                experienceText
                        );
                    } catch (NumberFormatException e) {
                        minExperience = 0;
                    }
                }

                else if (normalizedLine.toLowerCase()
                        .startsWith("education required:")) {

                    educationRequired = getValue(normalizedLine);
                }
            }
        }

        return new JobDescription(
                role,
                requiredSkills,
                minExperience,
                educationRequired,
                rawText.toString()
        );
    }
}