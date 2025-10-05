package com.resumetracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a job description with its requirements.
 * This class stores the key information needed to match candidates against a job opening.
 */
public class JobDescription {
    private int id;
    private String jobTitle;
    private List<String> requiredSkills;
    private int requiredYearsOfExperience;
    private String rawDescription; // Store the full job description text

    /**
     * Default constructor initializes empty lists.
     */
    public JobDescription() {
        this.requiredSkills = new ArrayList<>();
        this.requiredYearsOfExperience = 0;
    }

    /**
     * Constructor with essential fields.
     */
    public JobDescription(String jobTitle, List<String> requiredSkills, int requiredYearsOfExperience) {
        this.jobTitle = jobTitle;
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
        this.requiredYearsOfExperience = requiredYearsOfExperience;
    }

    /**
     * Constructor with all fields including ID and raw description.
     */
    public JobDescription(int id, String jobTitle, List<String> requiredSkills, 
                          int requiredYearsOfExperience, String rawDescription) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
        this.requiredYearsOfExperience = requiredYearsOfExperience;
        this.rawDescription = rawDescription;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public int getRequiredYearsOfExperience() {
        return requiredYearsOfExperience;
    }

    public void setRequiredYearsOfExperience(int requiredYearsOfExperience) {
        this.requiredYearsOfExperience = requiredYearsOfExperience;
    }

    public String getRawDescription() {
        return rawDescription;
    }

    public void setRawDescription(String rawDescription) {
        this.rawDescription = rawDescription;
    }

    /**
     * Returns a comma-separated string of all required skills for database storage.
     */
    public String getRequiredSkillsAsString() {
        return String.join(", ", requiredSkills);
    }

    /**
     * Utility method to check if any skills are specified.
     */
    public boolean hasRequiredSkills() {
        return requiredSkills != null && !requiredSkills.isEmpty();
    }

    @Override
    public String toString() {
        return "JobDescription{" +
                "id=" + id +
                ", jobTitle='" + jobTitle + '\'' +
                ", requiredSkills=" + requiredSkills.size() +
                ", requiredYearsOfExperience=" + requiredYearsOfExperience +
                '}';
    }
}
