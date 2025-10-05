package com.resumetracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a candidate with their resume information.
 * This is a Plain Old Java Object (POJO) that stores all extracted data from a resume.
 */
public class Candidate {
    private int id;
    private String name;
    private String email;
    private String phone;
    private List<String> skills;
    private List<String> education;
    private List<String> workExperience;

    /**
     * Default constructor initializes empty lists to avoid null pointer exceptions.
     */
    public Candidate() {
        this.skills = new ArrayList<>();
        this.education = new ArrayList<>();
        this.workExperience = new ArrayList<>();
    }

    /**
     * Constructor with all fields except ID (for new candidates before database insertion).
     */
    public Candidate(String name, String email, String phone, List<String> skills, 
                     List<String> education, List<String> workExperience) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.skills = skills != null ? skills : new ArrayList<>();
        this.education = education != null ? education : new ArrayList<>();
        this.workExperience = workExperience != null ? workExperience : new ArrayList<>();
    }

    /**
     * Constructor with all fields including ID (for candidates loaded from database).
     */
    public Candidate(int id, String name, String email, String phone, List<String> skills, 
                     List<String> education, List<String> workExperience) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.skills = skills != null ? skills : new ArrayList<>();
        this.education = education != null ? education : new ArrayList<>();
        this.workExperience = workExperience != null ? workExperience : new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(List<String> workExperience) {
        this.workExperience = workExperience;
    }

    /**
     * Utility method to check if the candidate has any experience entries.
     */
    public boolean hasWorkExperience() {
        return workExperience != null && !workExperience.isEmpty();
    }

    /**
     * Returns a comma-separated string of all skills for database storage.
     */
    public String getSkillsAsString() {
        return String.join(", ", skills);
    }

    /**
     * Returns a formatted string of all education entries for database storage.
     */
    public String getEducationAsString() {
        return String.join(" | ", education);
    }

    /**
     * Returns a formatted string of all work experience entries for database storage.
     */
    public String getWorkExperienceAsString() {
        return String.join(" | ", workExperience);
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", skills=" + skills.size() +
                ", education=" + education.size() +
                ", workExperience=" + workExperience.size() +
                '}';
    }
}
