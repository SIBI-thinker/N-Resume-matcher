package com.resumetracker.parser;

import com.resumetracker.model.JobDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing job descriptions.
 * Extracts job title, required skills, and experience requirements.
 */
public class JobDescriptionParser {

    /**
     * Parses a job description text and creates a JobDescription object.
     * 
     * @param text The raw job description text
     * @return A JobDescription object with extracted information
     */
    public JobDescription parse(String text) {
        JobDescription jobDesc = new JobDescription();
        jobDesc.setRawDescription(text);

        // Extract job title
        String jobTitle = extractJobTitle(text);
        jobDesc.setJobTitle(jobTitle);

        // Extract required skills
        List<String> skills = extractRequiredSkills(text);
        jobDesc.setRequiredSkills(skills);

        // Extract years of experience
        int years = extractYearsOfExperience(text);
        jobDesc.setRequiredYearsOfExperience(years);

        return jobDesc;
    }

    /**
     * Extracts the job title from the text.
     * Looks for patterns like "Job Title:", "Position:", or assumes first meaningful line.
     */
    private String extractJobTitle(String text) {
        String[] lines = text.split("\n");
        
        // Look for explicit job title markers
        Pattern titlePattern = Pattern.compile(
            "(?i)(?:job\\s+title|position|role)\\s*[:;]\\s*(.+)",
            Pattern.CASE_INSENSITIVE
        );

        for (String line : lines) {
            Matcher matcher = titlePattern.matcher(line.trim());
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        }

        // Fallback: use first non-empty line as title
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && trimmed.length() > 3 && trimmed.length() < 100) {
                return trimmed;
            }
        }

        return "Position";
    }

    /**
     * Extracts required skills from the job description.
     * Looks for skills sections and parses skill lists.
     */
    private List<String> extractRequiredSkills(String text) {
        List<String> skills = new ArrayList<>();
        
        // Find skills section
        String skillsSection = extractSection(text, 
            "(?:required\\s+)?skills?|technical\\s+skills?|requirements?|qualifications?");

        if (skillsSection != null && !skillsSection.isEmpty()) {
            // Split by common delimiters
            String[] skillTokens = skillsSection.split("[,;•◦▪▫–\n\\|]+");
            
            for (String skill : skillTokens) {
                String trimmed = skill.trim()
                    .replaceAll("(?i)^(?:required\\s+)?skills?\\s*[:;]?\\s*", "")
                    .replaceAll("(?i)^(?:technical\\s+)?skills?\\s*[:;]?\\s*", "")
                    .replaceAll("(?i)^requirements?\\s*[:;]?\\s*", "")
                    .trim();
                
                // Filter out noise and very short strings
                if (trimmed.length() > 1 && 
                    !trimmed.matches("^[\\d.\\-]+$") && 
                    !trimmed.toLowerCase().matches("^(and|or|the|with|for|in|on|at)$")) {
                    skills.add(trimmed);
                }
            }
        }

        return skills;
    }

    /**
     * Extracts years of experience requirement from the job description.
     * Looks for patterns like "3+ years", "3-5 years", "3 years experience".
     */
    private int extractYearsOfExperience(String text) {
        // Patterns to match experience requirements
        Pattern[] patterns = {
            Pattern.compile("(\\d+)\\+?\\s*(?:to|\\-)?\\s*\\d*\\s*years?\\s+(?:of\\s+)?experience", Pattern.CASE_INSENSITIVE),
            Pattern.compile("experience\\s*[:;]\\s*(\\d+)\\+?\\s*years?", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(\\d+)\\+?\\s*years?", Pattern.CASE_INSENSITIVE)
        };

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                try {
                    return Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException e) {
                    // Continue to next pattern
                }
            }
        }

        return 0;
    }

    /**
     * Extracts a section from text based on a header pattern.
     * 
     * @param text The full text to search
     * @param headerPattern Regex pattern for section headers
     * @return The text content of the section
     */
    private String extractSection(String text, String headerPattern) {
        // Create pattern to match the section header
        Pattern pattern = Pattern.compile(
            "(?i)(?:^|\\n)\\s*(" + headerPattern + ")\\s*[:;]?\\s*\\n?",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            int sectionStart = matcher.end();
            
            // Find the next section header
            Pattern nextSectionPattern = Pattern.compile(
                "(?i)(?:\\n\\s*(?:job\\s+title|position|role|skills?|education|experience|work|" +
                "professional|employment|certifications?|projects?|awards?|references?|" +
                "summary|objective|responsibilities|duties|about|description)\\s*[:;]?\\s*\\n)",
                Pattern.MULTILINE | Pattern.CASE_INSENSITIVE
            );
            
            Matcher nextMatcher = nextSectionPattern.matcher(text);
            
            int sectionEnd = text.length();
            if (nextMatcher.find(sectionStart)) {
                sectionEnd = nextMatcher.start();
            }
            
            return text.substring(sectionStart, sectionEnd).trim();
        }
        
        return "";
    }

    /**
     * Validates if a job description has minimum required information.
     * 
     * @param jobDesc The job description to validate
     * @return true if valid, false otherwise
     */
    public boolean isValid(JobDescription jobDesc) {
        // Must have at least a title and some skills
        return jobDesc.getJobTitle() != null && 
               !jobDesc.getJobTitle().isEmpty() &&
               jobDesc.getRequiredSkills() != null &&
               !jobDesc.getRequiredSkills().isEmpty();
    }

    /**
     * Extracts key responsibilities from the job description.
     * 
     * @param text The job description text
     * @return List of responsibilities
     */
    public List<String> extractResponsibilities(String text) {
        List<String> responsibilities = new ArrayList<>();
        
        String respSection = extractSection(text, 
            "responsibilities|duties|you\\s+will|what\\s+you['']ll\\s+do");

        if (respSection != null && !respSection.isEmpty()) {
            // Split by bullet points or newlines
            String[] lines = respSection.split("\\n");
            
            for (String line : lines) {
                String trimmed = line.trim()
                    .replaceAll("^[•◦▪▫–\\-*+]+\\s*", "") // Remove bullet points
                    .trim();
                
                if (trimmed.length() > 10) {
                    responsibilities.add(trimmed);
                }
            }
        }

        return responsibilities;
    }
}
