package com.resumetracker;

import com.resumetracker.database.DatabaseManager;
import com.resumetracker.matcher.JobMatcher;
import com.resumetracker.model.Candidate;
import com.resumetracker.model.JobDescription;
import com.resumetracker.model.MatchResult;
import com.resumetracker.parser.ResumeParser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main JavaFX application for Resume Parser and Job Matcher.
 * Provides a GUI for loading resumes, entering job descriptions, and viewing match results.
 */
public class MainApp extends Application {
    private DatabaseManager dbManager;
    private ResumeParser resumeParser;
    private JobMatcher jobMatcher;
    
    // UI Components
    private TextArea jobDescriptionArea;
    private TableView<MatchResult> resultsTable;
    private Label statusLabel;
    private ListView<String> loadedCandidatesList;
    private ObservableList<MatchResult> matchResults;
    private ObservableList<String> loadedCandidates;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize components
        initializeComponents();

        // Create UI
        primaryStage.setTitle("Resume Parser & Job Matcher");
        primaryStage.setScene(createMainScene());
        primaryStage.setOnCloseRequest(e -> cleanup());
        primaryStage.show();

        // Show welcome message
        showAlert(Alert.AlertType.INFORMATION, "Welcome", 
            "Resume Parser & Job Matcher", 
            "Load resume files (PDF/DOCX) and match candidates against job descriptions.");
    }

    /**
     * Initializes all backend components.
     */
    private void initializeComponents() {
        dbManager = DatabaseManager.getInstance();
        dbManager.connect();
        
        resumeParser = new ResumeParser();
        jobMatcher = new JobMatcher();
        
        matchResults = FXCollections.observableArrayList();
        loadedCandidates = FXCollections.observableArrayList();
    }

    /**
     * Creates the main scene with all UI components.
     */
    private Scene createMainScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Title and status
        VBox topSection = createTopSection();
        root.setTop(topSection);

        // Left: Resume loading panel
        VBox leftSection = createLeftSection();
        root.setLeft(leftSection);

        // Center: Job description and matching
        VBox centerSection = createCenterSection();
        root.setCenter(centerSection);

        // Bottom: Results table
        VBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);

        Scene scene = new Scene(root, 1200, 800);
        return scene;
    }

    /**
     * Creates the top section with title and status label.
     */
    private VBox createTopSection() {
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(0, 0, 10, 0));

        Label titleLabel = new Label("Resume Parser & Job Matcher");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: green;");

        topBox.getChildren().addAll(titleLabel, statusLabel);
        return topBox;
    }

    /**
     * Creates the left section with resume loading functionality.
     */
    private VBox createLeftSection() {
        VBox leftBox = new VBox(10);
        leftBox.setPadding(new Insets(0, 10, 0, 0));
        leftBox.setPrefWidth(250);

        Label candidatesLabel = new Label("Loaded Candidates");
        candidatesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        loadedCandidatesList = new ListView<>(loadedCandidates);
        loadedCandidatesList.setPrefHeight(300);

        Button loadResumesButton = new Button("Load Resume(s)");
        loadResumesButton.setPrefWidth(250);
        loadResumesButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        loadResumesButton.setOnAction(e -> handleLoadResumes());

        Button viewAllCandidatesButton = new Button("Refresh Candidates");
        viewAllCandidatesButton.setPrefWidth(250);
        viewAllCandidatesButton.setOnAction(e -> refreshCandidatesList());

        Button clearCandidatesButton = new Button("Clear All Candidates");
        clearCandidatesButton.setPrefWidth(250);
        clearCandidatesButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        clearCandidatesButton.setOnAction(e -> handleClearCandidates());

        leftBox.getChildren().addAll(
            candidatesLabel,
            loadedCandidatesList,
            loadResumesButton,
            viewAllCandidatesButton,
            clearCandidatesButton
        );

        return leftBox;
    }

    /**
     * Creates the center section with job description input.
     */
    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(0, 10, 10, 0));

        Label jobDescLabel = new Label("Job Description");
        jobDescLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        jobDescriptionArea = new TextArea();
        jobDescriptionArea.setPromptText(
            "Paste or enter the job description here...\n\n" +
            "Example:\n" +
            "Job Title: Senior Java Developer\n\n" +
            "Required Skills: Java, Spring Boot, MySQL, REST APIs, Git\n\n" +
            "Experience: 3+ years of professional experience"
        );
        jobDescriptionArea.setPrefHeight(300);
        jobDescriptionArea.setWrapText(true);

        Button matchButton = new Button("Match Candidates");
        matchButton.setPrefWidth(200);
        matchButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        matchButton.setOnAction(e -> handleMatchCandidates());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(matchButton);

        centerBox.getChildren().addAll(jobDescLabel, jobDescriptionArea, buttonBox);
        return centerBox;
    }

    /**
     * Creates the bottom section with results table.
     */
    private VBox createBottomSection() {
        VBox bottomBox = new VBox(10);
        bottomBox.setPadding(new Insets(10, 0, 0, 0));

        Label resultsLabel = new Label("Match Results");
        resultsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        resultsTable = createResultsTable();
        resultsTable.setItems(matchResults);

        bottomBox.getChildren().addAll(resultsLabel, resultsTable);
        return bottomBox;
    }

    /**
     * Creates the results table with appropriate columns.
     */
    private TableView<MatchResult> createResultsTable() {
        TableView<MatchResult> table = new TableView<>();
        table.setPrefHeight(300);

        // Candidate Name column
        TableColumn<MatchResult, String> nameCol = new TableColumn<>("Candidate Name");
        nameCol.setPrefWidth(200);
        nameCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCandidate().getName()
            )
        );

        // Match Score column
        TableColumn<MatchResult, String> scoreCol = new TableColumn<>("Match Score");
        scoreCol.setPrefWidth(120);
        scoreCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getMatchScoreAsPercentage()
            )
        );

        // Matched Skills column
        TableColumn<MatchResult, String> matchedSkillsCol = new TableColumn<>("Matched Skills");
        matchedSkillsCol.setPrefWidth(350);
        matchedSkillsCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getMatchedSkillsAsString()
            )
        );

        // Missing Skills column
        TableColumn<MatchResult, String> missingSkillsCol = new TableColumn<>("Missing Skills");
        missingSkillsCol.setPrefWidth(350);
        missingSkillsCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getMissingSkillsAsString()
            )
        );

        // Email column
        TableColumn<MatchResult, String> emailCol = new TableColumn<>("Email");
        emailCol.setPrefWidth(200);
        emailCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCandidate().getEmail()
            )
        );

        table.getColumns().addAll(nameCol, scoreCol, matchedSkillsCol, missingSkillsCol, emailCol);
        return table;
    }

    /**
     * Handles the "Load Resume(s)" button click.
     */
    private void handleLoadResumes() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Resume Files");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Resume Files", "*.pdf", "*.docx", "*.txt"),
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
            new FileChooser.ExtensionFilter("DOCX Files", "*.docx"),
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            int successCount = 0;
            int failCount = 0;

            for (File file : selectedFiles) {
                try {
                    updateStatus("Parsing: " + file.getName() + "...");
                    Candidate candidate = resumeParser.parseFile(file);
                    
                    // Save to database
                    int id = dbManager.insertCandidate(candidate);
                    
                    if (id > 0) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing file " + file.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                    failCount++;
                }
            }

            // Refresh candidates list
            refreshCandidatesList();

            // Show results
            String message = String.format(
                "Successfully loaded %d resume(s).\nFailed: %d",
                successCount, failCount
            );
            showAlert(Alert.AlertType.INFORMATION, "Load Complete", "Resume Loading Results", message);
            updateStatus("Ready - " + successCount + " resumes loaded");
        }
    }

    /**
     * Handles the "Match Candidates" button click.
     */
    private void handleMatchCandidates() {
        String jobDescText = jobDescriptionArea.getText().trim();

        if (jobDescText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Job Description", 
                "Empty Job Description", 
                "Please enter a job description before matching.");
            return;
        }

        // Parse job description
        JobDescription jobDesc = parseJobDescription(jobDescText);

        if (jobDesc.getRequiredSkills().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Skills Found", 
                "Could not extract skills", 
                "Please make sure the job description includes required skills.");
            return;
        }

        // Get all candidates from database
        List<Candidate> candidates = dbManager.getAllCandidates();

        if (candidates.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Candidates", 
                "No candidates found", 
                "Please load some resumes first.");
            return;
        }

        updateStatus("Matching " + candidates.size() + " candidates...");

        // Perform matching
        List<MatchResult> results = jobMatcher.matchMultipleCandidates(candidates, jobDesc);

        // Update table
        matchResults.clear();
        matchResults.addAll(results);

        updateStatus("Match complete - " + results.size() + " candidates matched");
        
        // Show summary
        if (!results.isEmpty()) {
            MatchResult topMatch = results.get(0);
            showAlert(Alert.AlertType.INFORMATION, "Matching Complete", 
                "Top Match Found", 
                String.format("Top candidate: %s with %.1f%% match score",
                    topMatch.getCandidate().getName(),
                    topMatch.getMatchScore()));
        }
    }

    /**
     * Parses a job description text into a JobDescription object.
     */
    private JobDescription parseJobDescription(String text) {
        JobDescription jobDesc = new JobDescription();
        jobDesc.setRawDescription(text);

        // Extract job title (look for "Job Title:" or assume first line)
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.toLowerCase().contains("job title:") || 
                line.toLowerCase().contains("position:")) {
                String title = line.replaceAll("(?i)job title:|position:", "").trim();
                jobDesc.setJobTitle(title);
                break;
            }
        }

        if (jobDesc.getJobTitle() == null || jobDesc.getJobTitle().isEmpty()) {
            jobDesc.setJobTitle("Position");
        }

        // Extract required skills
        List<String> skills = extractSkillsFromJobDescription(text);
        jobDesc.setRequiredSkills(skills);

        // Extract years of experience
        int years = extractYearsOfExperience(text);
        jobDesc.setRequiredYearsOfExperience(years);

        return jobDesc;
    }

    /**
     * Extracts skills from job description text.
     */
    private List<String> extractSkillsFromJobDescription(String text) {
        List<String> skills = new ArrayList<>();
        
        // Look for skills section
        String lowerText = text.toLowerCase();
        int skillsIndex = -1;
        
        String[] skillKeywords = {"required skills:", "skills:", "technical skills:", "requirements:"};
        for (String keyword : skillKeywords) {
            skillsIndex = lowerText.indexOf(keyword);
            if (skillsIndex != -1) {
                break;
            }
        }

        if (skillsIndex != -1) {
            // Extract text after skills keyword until next section or end
            int endIndex = text.length();
            String[] sectionKeywords = {"experience:", "education:", "responsibilities:", "qualifications:"};
            
            for (String keyword : sectionKeywords) {
                int idx = lowerText.indexOf(keyword, skillsIndex + 10);
                if (idx != -1 && idx < endIndex) {
                    endIndex = idx;
                }
            }

            String skillsText = text.substring(skillsIndex, endIndex);
            
            // Split by common delimiters
            String[] skillTokens = skillsText.split("[,;•◦▪▫–\n]+");
            
            for (String skill : skillTokens) {
                String trimmed = skill.trim()
                    .replaceAll("(?i)required skills:|skills:|technical skills:|requirements:", "")
                    .trim();
                
                if (trimmed.length() > 1 && !trimmed.matches("^[\\d.\\-]+$")) {
                    skills.add(trimmed);
                }
            }
        }

        return skills;
    }

    /**
     * Extracts years of experience from job description.
     */
    private int extractYearsOfExperience(String text) {
        // Look for patterns like "3+ years", "3-5 years", "3 years"
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\+?\\s*years?");
        java.util.regex.Matcher matcher = pattern.matcher(text.toLowerCase());
        
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        
        return 0;
    }

    /**
     * Refreshes the candidates list from the database.
     */
    private void refreshCandidatesList() {
        loadedCandidates.clear();
        List<Candidate> candidates = dbManager.getAllCandidates();
        
        for (Candidate candidate : candidates) {
            String displayText = String.format("%s (%d skills)", 
                candidate.getName(), 
                candidate.getSkills().size());
            loadedCandidates.add(displayText);
        }

        updateStatus("Loaded " + candidates.size() + " candidates from database");
    }

    /**
     * Handles clearing all candidates from the database.
     */
    private void handleClearCandidates() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Clear");
        confirmAlert.setHeaderText("Clear all candidates?");
        confirmAlert.setContentText("This will delete all candidates from the database. This action cannot be undone.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            List<Candidate> candidates = dbManager.getAllCandidates();
            for (Candidate candidate : candidates) {
                dbManager.deleteCandidate(candidate.getId());
            }

            loadedCandidates.clear();
            matchResults.clear();
            updateStatus("All candidates cleared");
        }
    }

    /**
     * Updates the status label.
     */
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Shows an alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Cleanup when application closes.
     */
    private void cleanup() {
        if (dbManager != null) {
            dbManager.close();
        }
    }
}
