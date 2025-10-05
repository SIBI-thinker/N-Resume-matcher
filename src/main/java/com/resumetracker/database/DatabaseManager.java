package com.resumetracker.database;

import com.resumetracker.model.Candidate;
import com.resumetracker.model.JobDescription;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages all database operations for the Resume Tracker application.
 * Uses the Singleton pattern to ensure only one database connection exists.
 * Handles SQLite database creation, connection, and CRUD operations.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DATABASE_URL = "jdbc:sqlite:database.db";

    /**
     * Private constructor to prevent direct instantiation (Singleton pattern).
     */
    private DatabaseManager() {
        // Private constructor
    }

    /**
     * Returns the singleton instance of DatabaseManager.
     * Thread-safe implementation.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Establishes a connection to the SQLite database.
     * Creates the database file if it doesn't exist.
     */
    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println("Connected to SQLite database: " + DATABASE_URL);
                createTables();
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates the necessary database tables if they don't exist.
     * Tables: candidates, job_descriptions
     */
    public void createTables() {
        // SQL statement to create candidates table
        String createCandidatesTable = "CREATE TABLE IF NOT EXISTS candidates (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT, " +
                "phone TEXT, " +
                "skills TEXT, " +
                "education TEXT, " +
                "experience TEXT" +
                ");";

        // SQL statement to create job_descriptions table
        String createJobDescriptionsTable = "CREATE TABLE IF NOT EXISTS job_descriptions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "required_skills TEXT, " +
                "required_experience INTEGER, " +
                "raw_description TEXT" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCandidatesTable);
            stmt.execute(createJobDescriptionsTable);
            System.out.println("Database tables created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inserts a candidate into the database.
     * @param candidate The candidate object to insert
     * @return The generated ID of the inserted candidate, or -1 if insertion failed
     */
    public int insertCandidate(Candidate candidate) {
        String sql = "INSERT INTO candidates(name, email, phone, skills, education, experience) " +
                     "VALUES(?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, candidate.getName());
            pstmt.setString(2, candidate.getEmail());
            pstmt.setString(3, candidate.getPhone());
            pstmt.setString(4, candidate.getSkillsAsString());
            pstmt.setString(5, candidate.getEducationAsString());
            pstmt.setString(6, candidate.getWorkExperienceAsString());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        candidate.setId(id);
                        System.out.println("Candidate inserted with ID: " + id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting candidate: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves all candidates from the database.
     * @return A list of all candidates
     */
    public List<Candidate> getAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT * FROM candidates";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                
                // Parse comma-separated skills
                String skillsStr = rs.getString("skills");
                List<String> skills = parseCommaSeparatedString(skillsStr);
                
                // Parse pipe-separated education
                String educationStr = rs.getString("education");
                List<String> education = parsePipeSeparatedString(educationStr);
                
                // Parse pipe-separated experience
                String experienceStr = rs.getString("experience");
                List<String> experience = parsePipeSeparatedString(experienceStr);

                Candidate candidate = new Candidate(id, name, email, phone, skills, education, experience);
                candidates.add(candidate);
            }

            System.out.println("Retrieved " + candidates.size() + " candidates from database.");

        } catch (SQLException e) {
            System.err.println("Error retrieving candidates: " + e.getMessage());
            e.printStackTrace();
        }

        return candidates;
    }

    /**
     * Retrieves a candidate by ID.
     * @param id The candidate's ID
     * @return The candidate object, or null if not found
     */
    public Candidate getCandidateById(int id) {
        String sql = "SELECT * FROM candidates WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                List<String> skills = parseCommaSeparatedString(rs.getString("skills"));
                List<String> education = parsePipeSeparatedString(rs.getString("education"));
                List<String> experience = parsePipeSeparatedString(rs.getString("experience"));

                return new Candidate(id, name, email, phone, skills, education, experience);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving candidate: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Inserts a job description into the database.
     * @param jobDescription The job description object to insert
     * @return The generated ID of the inserted job description, or -1 if insertion failed
     */
    public int insertJobDescription(JobDescription jobDescription) {
        String sql = "INSERT INTO job_descriptions(title, required_skills, required_experience, raw_description) " +
                     "VALUES(?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, jobDescription.getJobTitle());
            pstmt.setString(2, jobDescription.getRequiredSkillsAsString());
            pstmt.setInt(3, jobDescription.getRequiredYearsOfExperience());
            pstmt.setString(4, jobDescription.getRawDescription());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        jobDescription.setId(id);
                        System.out.println("Job description inserted with ID: " + id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting job description: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves all job descriptions from the database.
     * @return A list of all job descriptions
     */
    public List<JobDescription> getAllJobDescriptions() {
        List<JobDescription> jobs = new ArrayList<>();
        String sql = "SELECT * FROM job_descriptions";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                List<String> requiredSkills = parseCommaSeparatedString(rs.getString("required_skills"));
                int requiredExperience = rs.getInt("required_experience");
                String rawDescription = rs.getString("raw_description");

                JobDescription job = new JobDescription(id, title, requiredSkills, requiredExperience, rawDescription);
                jobs.add(job);
            }

            System.out.println("Retrieved " + jobs.size() + " job descriptions from database.");

        } catch (SQLException e) {
            System.err.println("Error retrieving job descriptions: " + e.getMessage());
            e.printStackTrace();
        }

        return jobs;
    }

    /**
     * Deletes a candidate from the database.
     * @param id The ID of the candidate to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteCandidate(int id) {
        String sql = "DELETE FROM candidates WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Candidate with ID " + id + " deleted successfully.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting candidate: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to parse comma-separated strings into a list.
     */
    private List<String> parseCommaSeparatedString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(str.split(",\\s*")));
    }

    /**
     * Helper method to parse pipe-separated strings into a list.
     */
    private List<String> parsePipeSeparatedString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(str.split("\\s*\\|\\s*")));
    }

    /**
     * Returns the current database connection.
     * Mainly for testing purposes.
     */
    public Connection getConnection() {
        return connection;
    }
}
