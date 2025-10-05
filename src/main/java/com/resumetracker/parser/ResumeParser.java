package com.resumetracker.parser;

import com.resumetracker.model.Candidate;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses resume files (PDF and DOCX) and extracts candidate information.
 * Uses Apache PDFBox for PDF parsing, Apache POI for DOCX parsing,
 * and Apache OpenNLP for Natural Language Processing.
 */
public class ResumeParser {
    private Tokenizer tokenizer;
    private NameFinderME personFinder;
    private boolean nlpModelsLoaded = false;

    /**
     * Constructor that attempts to load OpenNLP models.
     */
    public ResumeParser() {
        try {
            loadNLPModels();
        } catch (Exception e) {
            System.err.println("Warning: Could not load NLP models. NLP features will be limited.");
            System.err.println("Error: " + e.getMessage());
            nlpModelsLoaded = false;
        }
    }

    /**
     * Loads OpenNLP models for tokenization and named entity recognition.
     * Models should be placed in src/main/resources/models/
     * 
     * Download models from: https://opennlp.sourceforge.net/models-1.5/
     * Required models:
     * - en-token.bin (tokenizer)
     * - en-ner-person.bin (person name recognition)
     */
    private void loadNLPModels() throws IOException {
        try {
            // Load tokenizer model
            InputStream tokenizerStream = getClass().getResourceAsStream("/models/en-token.bin");
            if (tokenizerStream != null) {
                TokenizerModel tokenizerModel = new TokenizerModel(tokenizerStream);
                tokenizer = new TokenizerME(tokenizerModel);
                tokenizerStream.close();
                System.out.println("Tokenizer model loaded successfully.");
            } else {
                System.out.println("Tokenizer model not found. Using basic tokenization.");
            }

            // Load person name finder model
            InputStream personStream = getClass().getResourceAsStream("/models/en-ner-person.bin");
            if (personStream != null) {
                TokenNameFinderModel personModel = new TokenNameFinderModel(personStream);
                personFinder = new NameFinderME(personModel);
                personStream.close();
                nlpModelsLoaded = true;
                System.out.println("Person NER model loaded successfully.");
            } else {
                System.out.println("Person NER model not found. Using basic name extraction.");
            }
        } catch (IOException e) {
            System.err.println("Error loading NLP models: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Extracts text from a PDF file using Apache PDFBox.
     * @param file The PDF file to extract text from
     * @return The extracted text as a string
     */
    public String extractTextFromPDF(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            System.out.println("Extracted text from PDF: " + file.getName());
            return text;
        } catch (IOException e) {
            System.err.println("Error extracting text from PDF: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Extracts text from a DOCX file using Apache POI.
     * @param file The DOCX file to extract text from
     * @return The extracted text as a string
     */
    public String extractTextFromDocx(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            
            String text = extractor.getText();
            System.out.println("Extracted text from DOCX: " + file.getName());
            return text;
        } catch (IOException e) {
            System.err.println("Error extracting text from DOCX: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Main parsing method that takes raw text and extracts candidate information.
     * Uses NLP for name extraction and regex for email/phone extraction.
     * Uses keyword-based sectioning for skills, education, and experience.
     * 
     * @param rawText The raw text extracted from a resume
     * @return A Candidate object with extracted information
     */
    public Candidate parse(String rawText) {
        Candidate candidate = new Candidate();

        if (rawText == null || rawText.trim().isEmpty()) {
            System.err.println("Warning: Empty text provided for parsing.");
            return candidate;
        }

        // Extract name
        String name = extractName(rawText);
        candidate.setName(name != null ? name : "Unknown");

        // Extract email
        String email = extractEmail(rawText);
        candidate.setEmail(email);

        // Extract phone
        String phone = extractPhone(rawText);
        candidate.setPhone(phone);

        // Extract skills
        List<String> skills = extractSkills(rawText);
        candidate.setSkills(skills);

        // Extract education
        List<String> education = extractEducation(rawText);
        candidate.setEducation(education);

        // Extract work experience
        List<String> experience = extractWorkExperience(rawText);
        candidate.setWorkExperience(experience);

        System.out.println("Parsed candidate: " + candidate.getName());
        return candidate;
    }

    /**
     * Extracts the candidate's name using OpenNLP NER.
     * Falls back to heuristics if NLP models are not available.
     */
    private String extractName(String text) {
        if (nlpModelsLoaded && tokenizer != null && personFinder != null) {
            // Use OpenNLP to find person names
            String[] tokens = tokenizer.tokenize(text);
            Span[] nameSpans = personFinder.find(tokens);
            
            if (nameSpans.length > 0) {
                // Return the first person name found
                StringBuilder name = new StringBuilder();
                for (int i = nameSpans[0].getStart(); i < nameSpans[0].getEnd(); i++) {
                    name.append(tokens[i]).append(" ");
                }
                return name.toString().trim();
            }
        }

        // Fallback: Assume name is in the first few lines
        String[] lines = text.split("\n");
        for (int i = 0; i < Math.min(5, lines.length); i++) {
            String line = lines[i].trim();
            // Look for a line with 2-4 words (typical name pattern)
            if (line.matches("^[A-Z][a-z]+(\\s+[A-Z][a-z]+){1,3}$")) {
                return line;
            }
        }

        return "Unknown";
    }

    /**
     * Extracts email address using regex pattern.
     */
    private String extractEmail(String text) {
        Pattern emailPattern = Pattern.compile(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = emailPattern.matcher(text);
        
        if (matcher.find()) {
            return matcher.group();
        }
        
        return null;
    }

    /**
     * Extracts phone number using regex pattern.
     * Supports various phone number formats.
     */
    private String extractPhone(String text) {
        // Pattern matches various phone formats: (123) 456-7890, 123-456-7890, 123.456.7890, etc.
        Pattern phonePattern = Pattern.compile(
            "(?:\\+\\d{1,3}[-.\\s]?)?(?:\\(\\d{3}\\)|\\d{3})[-.\\s]?\\d{3}[-.\\s]?\\d{4}"
        );
        Matcher matcher = phonePattern.matcher(text);
        
        if (matcher.find()) {
            return matcher.group();
        }
        
        return null;
    }

    /**
     * Extracts skills using keyword-based sectioning.
     * Looks for "Skills" section and parses the content.
     */
    private List<String> extractSkills(String text) {
        List<String> skills = new ArrayList<>();
        
        // Find the Skills section
        String skillsSection = extractSection(text, "SKILLS?|TECHNICAL\\s+SKILLS?|KEY\\s+SKILLS?");
        
        if (skillsSection != null && !skillsSection.isEmpty()) {
            // Split by common separators: comma, semicolon, bullet points, newlines
            String[] skillTokens = skillsSection.split("[,;•◦▪▫–\\n]+");
            
            for (String skill : skillTokens) {
                String trimmedSkill = skill.trim();
                // Filter out very short strings and headers
                if (trimmedSkill.length() > 1 && !trimmedSkill.matches("^[\\d.\\-]+$")) {
                    skills.add(trimmedSkill);
                }
            }
        }
        
        return skills;
    }

    /**
     * Extracts education information using keyword-based sectioning.
     */
    private List<String> extractEducation(String text) {
        List<String> education = new ArrayList<>();
        
        String educationSection = extractSection(text, "EDUCATION|ACADEMIC\\s+BACKGROUND|QUALIFICATIONS?");
        
        if (educationSection != null && !educationSection.isEmpty()) {
            // Split by newlines and filter meaningful entries
            String[] lines = educationSection.split("\n");
            
            for (String line : lines) {
                String trimmedLine = line.trim();
                // Look for degree keywords or university patterns
                if (trimmedLine.length() > 10 && 
                    (trimmedLine.matches(".*(?i)(bachelor|master|phd|degree|university|college|diploma).*") ||
                     trimmedLine.matches(".*\\d{4}.*"))) { // Contains a year
                    education.add(trimmedLine);
                }
            }
        }
        
        return education;
    }

    /**
     * Extracts work experience using keyword-based sectioning.
     */
    private List<String> extractWorkExperience(String text) {
        List<String> experience = new ArrayList<>();
        
        String experienceSection = extractSection(text, 
            "WORK\\s+EXPERIENCE|PROFESSIONAL\\s+EXPERIENCE|EMPLOYMENT\\s+HISTORY|EXPERIENCE");
        
        if (experienceSection != null && !experienceSection.isEmpty()) {
            // Split by newlines and look for job entries
            String[] lines = experienceSection.split("\n");
            
            StringBuilder currentEntry = new StringBuilder();
            for (String line : lines) {
                String trimmedLine = line.trim();
                
                // Detect potential job title lines (often contain years or company indicators)
                if (trimmedLine.matches(".*\\d{4}.*") || trimmedLine.length() > 20) {
                    if (currentEntry.length() > 0) {
                        experience.add(currentEntry.toString().trim());
                        currentEntry = new StringBuilder();
                    }
                    currentEntry.append(trimmedLine).append(" ");
                } else if (!trimmedLine.isEmpty()) {
                    currentEntry.append(trimmedLine).append(" ");
                }
            }
            
            // Add the last entry
            if (currentEntry.length() > 0) {
                experience.add(currentEntry.toString().trim());
            }
        }
        
        return experience;
    }

    /**
     * Helper method to extract a section from text based on a header pattern.
     * @param text The full text to search
     * @param headerPattern Regex pattern for section headers
     * @return The text content of the section
     */
    private String extractSection(String text, String headerPattern) {
        // Create pattern to match the section header
        Pattern pattern = Pattern.compile(
            "(?i)(?:^|\\n)\\s*(" + headerPattern + ")\\s*:?\\s*\\n",
            Pattern.MULTILINE
        );
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            int sectionStart = matcher.end();
            
            // Find the next section header (common resume section keywords)
            Pattern nextSectionPattern = Pattern.compile(
                "(?i)(?:\\n\\s*(?:SKILLS?|EDUCATION|EXPERIENCE|WORK|PROFESSIONAL|EMPLOYMENT|" +
                "CERTIFICATIONS?|PROJECTS?|AWARDS?|REFERENCES?|SUMMARY|OBJECTIVE)\\s*:?\\s*\\n)",
                Pattern.MULTILINE
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
     * Parses a resume file and returns a Candidate object.
     * Automatically detects file type based on extension.
     * 
     * @param file The resume file (PDF or DOCX)
     * @return A Candidate object with extracted information
     */
    public Candidate parseFile(File file) throws IOException {
        String fileName = file.getName().toLowerCase();
        String rawText;

        if (fileName.endsWith(".pdf")) {
            rawText = extractTextFromPDF(file);
        } else if (fileName.endsWith(".docx")) {
            rawText = extractTextFromDocx(file);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Only PDF and DOCX are supported.");
        }

        return parse(rawText);
    }
}
