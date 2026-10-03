import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class WebServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(PORT),
                0
        );

        // Frontend
        server.createContext("/", WebServer::serveFrontend);

        // Backend API
        server.createContext(
                "/api/screen",
                WebServer::screenResumes
        );

        server.start();

        System.out.println("======================================");
        System.out.println(" AI RESUME SCREENING WEB SERVER");
        System.out.println("======================================");
        System.out.println("Server started successfully!");
        System.out.println();
        System.out.println("Website:");
        System.out.println("http://localhost:8080");
        System.out.println();
        System.out.println("API:");
        System.out.println("http://localhost:8080/api/screen");
        System.out.println();
        System.out.println("Press Ctrl + C to stop the server.");
    }


    /*
     * ==========================================
     * FRONTEND
     * ==========================================
     */

    private static void serveFrontend(
            HttpExchange exchange) throws IOException {

        String requestPath =
                exchange.getRequestURI().getPath();

        if (requestPath.equals("/")) {
            requestPath = "/index.html";
        }

        Path filePath = Paths.get(
                "frontend",
                requestPath.substring(1)
        );

        if (!Files.exists(filePath)) {

            sendResponse(
                    exchange,
                    404,
                    "text/plain",
                    "404 - File Not Found"
            );

            return;
        }

        String contentType =
                getContentType(filePath.toString());

        byte[] fileBytes =
                Files.readAllBytes(filePath);

        exchange.getResponseHeaders()
                .set("Content-Type", contentType);

        exchange.sendResponseHeaders(
                200,
                fileBytes.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(fileBytes);
        }
    }


    /*
     * ==========================================
     * SCREENING API
     * ==========================================
     */

    private static void screenResumes(
            HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("GET")) {

            sendResponse(
                    exchange,
                    405,
                    "application/json",
                    "{\"error\":\"Only GET requests are allowed\"}"
            );

            return;
        }

        try {

            System.out.println();
            System.out.println(
                    "API request received: /api/screen"
            );

            ScreeningSystem screeningSystem =
                    new ScreeningSystem();


            // Load job description

            JobDescription jobDescription =
                    screeningSystem.loadJobDescription(
                            "data/jobdesc.txt"
                    );


            // Load resumes

            List<Resume> resumes =
                    screeningSystem.loadResumes(
                            "data/resumes"
                    );


            // Screen and rank candidates

            List<Candidate> candidates =
                    screeningSystem.screenResumes(
                            jobDescription,
                            resumes
                    );


            // Convert results to JSON

            String json =
                    candidatesToJson(candidates);


            sendResponse(
                    exchange,
                    200,
                    "application/json",
                    json
            );


            System.out.println(
                    "Screening completed successfully."
            );

            System.out.println(
                    "Candidates returned: "
                            + candidates.size()
            );

        } catch (Exception e) {

            e.printStackTrace();

            String errorJson =
                    "{\"error\":\""
                            + escapeJson(e.getMessage())
                            + "\"}";

            sendResponse(
                    exchange,
                    500,
                    "application/json",
                    errorJson
            );
        }
    }


    /*
     * ==========================================
     * CONVERT CANDIDATES TO JSON
     * ==========================================
     */

    private static String candidatesToJson(
            List<Candidate> candidates) {

        StringBuilder json =
                new StringBuilder();

        json.append("[");


        for (int i = 0;
             i < candidates.size();
             i++) {

            Candidate candidate =
                    candidates.get(i);

            Resume resume =
                    candidate.getResume();


            if (i > 0) {
                json.append(",");
            }


            json.append("{");

            json.append("\"rank\":")
                    .append(i + 1)
                    .append(",");


            json.append("\"id\":\"")
                    .append(
                            escapeJson(
                                    resume.getCandidateId()
                            )
                    )
                    .append("\",");


            json.append("\"name\":\"")
                    .append(
                            escapeJson(
                                    resume.getName()
                            )
                    )
                    .append("\",");


            json.append("\"skills\":[");

            List<String> skills =
                    resume.getSkills();

            for (int j = 0;
                 j < skills.size();
                 j++) {

                if (j > 0) {
                    json.append(",");
                }

                json.append("\"")
                        .append(
                                escapeJson(
                                        skills.get(j)
                                )
                        )
                        .append("\"");
            }

            json.append("],");


            json.append("\"experience\":")
                    .append(
                            resume.getYearsOfExperience()
                    )
                    .append(",");


            json.append("\"education\":\"")
                    .append(
                            escapeJson(
                                    resume.getEducationLevel()
                            )
                    )
                    .append("\",");


            json.append("\"cosine\":")
                    .append(
                            candidate
                                    .getCosineSimilarityScore()
                    )
                    .append(",");


            json.append("\"skillScore\":")
                    .append(
                            candidate
                                    .getSkillScore()
                    )
                    .append(",");


            json.append("\"experienceScore\":")
                    .append(
                            candidate
                                    .getExperienceScore()
                    )
                    .append(",");


            json.append("\"educationScore\":")
                    .append(
                            candidate
                                    .getEducationScore()
                    )
                    .append(",");


            json.append("\"score\":")
                    .append(
                            candidate
                                    .getCombinedScore()
                    );


            json.append("}");
        }


        json.append("]");

        return json.toString();
    }


    /*
     * ==========================================
     * SEND HTTP RESPONSE
     * ==========================================
     */

    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String contentType,
            String response) throws IOException {

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        contentType + "; charset=UTF-8"
                );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(bytes);
        }
    }


    /*
     * ==========================================
     * CONTENT TYPE
     * ==========================================
     */

    private static String getContentType(
            String fileName) {

        if (fileName.endsWith(".html")) {
            return "text/html";
        }

        if (fileName.endsWith(".css")) {
            return "text/css";
        }

        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }

        if (fileName.endsWith(".json")) {
            return "application/json";
        }

        return "text/plain";
    }


    /*
     * ==========================================
     * JSON ESCAPING
     * ==========================================
     */

    private static String escapeJson(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}