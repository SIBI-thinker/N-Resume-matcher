package com.resumetracker.matcher;

import com.resumetracker.model.Candidate;
import com.resumetracker.model.JobDescription;
import com.resumetracker.model.MatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the matching algorithm to compare candidates against job descriptions.
 * Calculates a match score based on skill matching and experience.
 */
public class JobMatcher {
    // Weights for scoring components
    private static final double SKILL_WEIGHT = 0.70;  // 70% weight for skills
    private static final double EXPERIENCE_WEIGHT = 0.30;  // 30% weight for experience

    /**
     * Matches a candidate against a job description and returns a MatchResult.
     * 
     * Scoring Algorithm:
     * - Skills Matching (70%): Percentage of required skills that the candidate possesses
     * - Experience (30%): Based on presence of work experience
     * 
     * @param candidate The candidate to evaluate
     * @param job The job description to match against
     * @return A MatchResult object containing the score and details
     */
    public MatchResult match(Candidate candidate, JobDescription job) {
        // Initialize lists for matched and missing skills
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        // Calculate skill match score
        double skillScore = calculateSkillScore(candidate, job, matchedSkills, missingSkills);

        // Calculate experience score
        double experienceScore = calculateExperienceScore(candidate, job);

        // Calculate final match score (weighted sum)
        double finalScore = (skillScore * SKILL_WEIGHT) + (experienceScore * EXPERIENCE_WEIGHT);

        // Create match details string
        String matchDetails = generateMatchDetails(candidate, job, skillScore, experienceScore, finalScore);

        // Create and return the match result
        MatchResult result = new MatchResult(
            candidate,
            job,
            finalScore,
            matchedSkills,
            missingSkills,
            matchDetails
        );

        return result;
    }

    /**
     * Calculates the skill matching score.
     * Compares candidate skills against required job skills.
     * 
     * @param candidate The candidate to evaluate
     * @param job The job description
     * @param matchedSkills List to populate with matched skills
     * @param missingSkills List to populate with missing skills
     * @return Skill score from 0.0 to 100.0
     */
    private double calculateSkillScore(Candidate candidate, JobDescription job,
                                       List<String> matchedSkills, List<String> missingSkills) {
        List<String> candidateSkills = candidate.getSkills();
        List<String> requiredSkills = job.getRequiredSkills();

        // If no skills are required, give full score
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 100.0;
        }

        // If candidate has no skills, score is 0
        if (candidateSkills == null || candidateSkills.isEmpty()) {
            missingSkills.addAll(requiredSkills);
            return 0.0;
        }

        // Normalize skills to lowercase for case-insensitive comparison
        List<String> normalizedCandidateSkills = normalizeLower(candidateSkills);

        int matchCount = 0;
        for (String requiredSkill : requiredSkills) {
            String normalizedRequired = requiredSkill.toLowerCase().trim();
            
            // Check if candidate has this skill (case-insensitive, partial match)
            boolean hasSkill = normalizedCandidateSkills.stream()
                .anyMatch(candidateSkill -> 
                    candidateSkill.contains(normalizedRequired) || 
                    normalizedRequired.contains(candidateSkill) ||
                    areSimilarSkills(candidateSkill, normalizedRequired)
                );

            if (hasSkill) {
                matchedSkills.add(requiredSkill);
                matchCount++;
            } else {
                missingSkills.add(requiredSkill);
            }
        }

        // Calculate percentage of required skills matched
        double skillMatchPercentage = (double) matchCount / requiredSkills.size();
        return skillMatchPercentage * 100.0;
    }

    /**
     * Calculates the experience score based on work experience.
     * 
     * @param candidate The candidate to evaluate
     * @param job The job description
     * @return Experience score from 0.0 to 100.0
     */
    private double calculateExperienceScore(Candidate candidate, JobDescription job) {
        // Check if candidate has work experience
        boolean hasExperience = candidate.hasWorkExperience() && 
                               !candidate.getWorkExperience().isEmpty();

        // Simple scoring: if job requires experience and candidate has it, give full score
        // This can be enhanced to parse years of experience from text
        if (job.getRequiredYearsOfExperience() > 0) {
            if (hasExperience) {
                // Could be enhanced to calculate actual years from experience text
                // For now, we give full credit if any experience is present
                return 100.0;
            } else {
                return 0.0;
            }
        } else {
            // No experience required, give full score
            return 100.0;
        }
    }

    /**
     * Generates detailed match information as a formatted string.
     */
    private String generateMatchDetails(Candidate candidate, JobDescription job,
                                       double skillScore, double experienceScore, double finalScore) {
        StringBuilder details = new StringBuilder();
        
        details.append(String.format("Candidate: %s\n", candidate.getName()));
        details.append(String.format("Job: %s\n", job.getJobTitle()));
        details.append(String.format("Overall Match: %.1f%%\n\n", finalScore));
        details.append(String.format("Skill Match: %.1f%% (Weight: %.0f%%)\n", 
            skillScore, SKILL_WEIGHT * 100));
        details.append(String.format("Experience Match: %.1f%% (Weight: %.0f%%)\n", 
            experienceScore, EXPERIENCE_WEIGHT * 100));
        
        return details.toString();
    }

    /**
     * Normalizes a list of strings to lowercase and trims whitespace.
     */
    private List<String> normalizeLower(List<String> strings) {
        List<String> normalized = new ArrayList<>();
        for (String str : strings) {
            if (str != null) {
                normalized.add(str.toLowerCase().trim());
            }
        }
        return normalized;
    }

    /**
     * Checks if two skills are similar based on common abbreviations or synonyms.
     * This can be extended with more sophisticated similarity matching.
     */
    private boolean areSimilarSkills(String skill1, String skill2) {
        // Remove common separators and compare
        String s1 = skill1.replaceAll("[.\\-_/\\s]", "").toLowerCase();
        String s2 = skill2.replaceAll("[.\\-_/\\s]", "").toLowerCase();
        
        // Check for exact match after normalization
        if (s1.equals(s2)) {
            return true;
        }
        
        // Check for common skill abbreviations and synonyms
        return checkSkillSynonyms(s1, s2);
    }

    /**
     * Checks for common skill synonyms and abbreviations.
     * This method can be extended with a more comprehensive skill mapping.
     */
    private boolean checkSkillSynonyms(String skill1, String skill2) {
        // Define common synonyms (can be extended with a configuration file)
        String[][] synonymGroups = {
            {"js", "javascript"},
            {"ts", "typescript"},
            {"py", "python"},
            {"cpp", "c++"},
            {"cs", "csharp", "c#"},
            {"sql", "database", "databases"},
            {"ml", "machinelearning"},
            {"ai", "artificialintelligence"},
            {"rest", "restful", "restapi"},
            {"ui", "userinterface"},
            {"ux", "userexperience"}
        };

        for (String[] group : synonymGroups) {
            boolean has1 = false;
            boolean has2 = false;
            
            for (String synonym : group) {
                if (skill1.contains(synonym) || synonym.contains(skill1)) has1 = true;
                if (skill2.contains(synonym) || synonym.contains(skill2)) has2 = true;
            }
            
            if (has1 && has2) {
                return true;
            }
        }

        return false;
    }

    /**
     * Matches a list of candidates against a job description and returns sorted results.
     * Results are sorted by match score in descending order (highest first).
     * 
     * @param candidates List of candidates to evaluate
     * @param job The job description to match against
     * @return List of MatchResult objects sorted by score
     */
    public List<MatchResult> matchMultipleCandidates(List<Candidate> candidates, JobDescription job) {
        List<MatchResult> results = new ArrayList<>();

        for (Candidate candidate : candidates) {
            MatchResult result = match(candidate, job);
            results.add(result);
        }

        // Sort results by score (highest first) using the Comparable implementation
        results.sort(MatchResult::compareTo);

        System.out.println("Matched " + candidates.size() + " candidates against job: " + job.getJobTitle());
        return results;
    }

    /**
     * Filters match results to return only candidates above a certain score threshold.
     * 
     * @param results List of match results
     * @param threshold Minimum score threshold (0.0 to 100.0)
     * @return Filtered list of match results
     */
    public List<MatchResult> filterByThreshold(List<MatchResult> results, double threshold) {
        List<MatchResult> filtered = new ArrayList<>();
        
        for (MatchResult result : results) {
            if (result.getMatchScore() >= threshold) {
                filtered.add(result);
            }
        }
        
        System.out.println("Filtered " + filtered.size() + " candidates above threshold: " + threshold + "%");
        return filtered;
    }
}
