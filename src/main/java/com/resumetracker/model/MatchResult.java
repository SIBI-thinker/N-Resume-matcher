package com.resumetracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of matching a candidate against a job description.
 * Contains the match score, matched skills, and missing skills.
 */
public class MatchResult implements Comparable<MatchResult> {
    private Candidate candidate;
    private JobDescription jobDescription;
    private double matchScore; // Score from 0.0 to 100.0
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String matchDetails; // Additional information about the match

    /**
     * Default constructor initializes empty lists.
     */
    public MatchResult() {
        this.matchedSkills = new ArrayList<>();
        this.missingSkills = new ArrayList<>();
        this.matchScore = 0.0;
    }

    /**
     * Constructor with essential fields.
     */
    public MatchResult(Candidate candidate, double matchScore, 
                       List<String> matchedSkills, List<String> missingSkills) {
        this.candidate = candidate;
        this.matchScore = matchScore;
        this.matchedSkills = matchedSkills != null ? matchedSkills : new ArrayList<>();
        this.missingSkills = missingSkills != null ? missingSkills : new ArrayList<>();
    }

    /**
     * Constructor with all fields.
     */
    public MatchResult(Candidate candidate, JobDescription jobDescription, 
                       double matchScore, List<String> matchedSkills, 
                       List<String> missingSkills, String matchDetails) {
        this.candidate = candidate;
        this.jobDescription = jobDescription;
        this.matchScore = matchScore;
        this.matchedSkills = matchedSkills != null ? matchedSkills : new ArrayList<>();
        this.missingSkills = missingSkills != null ? missingSkills : new ArrayList<>();
        this.matchDetails = matchDetails;
    }

    // Getters and Setters
    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public JobDescription getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(JobDescription jobDescription) {
        this.jobDescription = jobDescription;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public String getMatchDetails() {
        return matchDetails;
    }

    public void setMatchDetails(String matchDetails) {
        this.matchDetails = matchDetails;
    }

    /**
     * Returns a comma-separated string of matched skills for display.
     */
    public String getMatchedSkillsAsString() {
        return matchedSkills.isEmpty() ? "None" : String.join(", ", matchedSkills);
    }

    /**
     * Returns a comma-separated string of missing skills for display.
     */
    public String getMissingSkillsAsString() {
        return missingSkills.isEmpty() ? "None" : String.join(", ", missingSkills);
    }

    /**
     * Returns the match score formatted as a percentage string.
     */
    public String getMatchScoreAsPercentage() {
        return String.format("%.1f%%", matchScore);
    }

    /**
     * Compares match results by score in descending order (highest score first).
     */
    @Override
    public int compareTo(MatchResult other) {
        return Double.compare(other.matchScore, this.matchScore);
    }

    @Override
    public String toString() {
        return "MatchResult{" +
                "candidate=" + (candidate != null ? candidate.getName() : "null") +
                ", matchScore=" + String.format("%.1f", matchScore) +
                ", matchedSkills=" + matchedSkills.size() +
                ", missingSkills=" + missingSkills.size() +
                '}';
    }
}
