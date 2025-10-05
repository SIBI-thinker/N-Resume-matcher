# Resume Parser and Job Matcher

A comprehensive JavaFX desktop application that automates the recruitment process by parsing resume files and matching candidates against job descriptions using advanced NLP techniques.

## Features

- **Resume Parsing**: Extract candidate information from PDF and DOCX files
- **NLP-Powered Extraction**: Uses Apache OpenNLP for accurate name recognition
- **Smart Matching Algorithm**: Scores candidates based on skills and experience
- **SQLite Database**: Local storage for candidates and job descriptions
- **Intuitive GUI**: Clean JavaFX interface for easy management
- **Batch Processing**: Load and process multiple resumes at once

## Technology Stack

- **Java 11+**
- **JavaFX 17** - GUI framework
- **Apache PDFBox** - PDF text extraction
- **Apache POI** - DOCX text extraction
- **Apache OpenNLP** - Natural Language Processing
- **SQLite** - Local database storage
- **Maven** - Build and dependency management

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6+
- Git (optional, for cloning)

## Installation & Setup

### 1. Clone or Download the Project

```bash
git clone <repository-url>
cd resume-tracker
```

### 2. Download OpenNLP Models (Important!)

The application uses Apache OpenNLP models for NLP features. Download the following pre-trained models:

**Download from:** https://opennlp.sourceforge.net/models-1.5/

**Required Models:**

- `en-token.bin` - English Tokenizer
- `en-ner-person.bin` - Person Name Recognition

**Installation Steps:**

1. Download the two model files from the link above
2. Create the models directory: `src/main/resources/models/`
3. Place both `.bin` files in this directory

Your directory structure should look like:

```
src/main/resources/models/
├── en-token.bin
└── en-ner-person.bin
```

**Note:** The application will run without these models but with reduced NLP accuracy. Basic regex-based extraction will be used as a fallback.

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

**Option 1: Using Maven**

```bash
mvn javafx:run
```

**Option 2: Using Java (after building)**

```bash
java -jar target/resume-parser-job-matcher-1.0-SNAPSHOT.jar
```

## Usage Guide

### Loading Resumes

1. Click the **"Load Resume(s)"** button in the left panel
2. Select one or more resume files (PDF or DOCX format)
3. The application will parse and store candidate information in the database
4. Loaded candidates appear in the "Loaded Candidates" list

### Matching Candidates

1. Enter or paste a job description in the center text area
2. Make sure to include:
   - Job title (optional)
   - Required skills (comma-separated or bulleted)
   - Years of experience (optional)
3. Click the **"Match Candidates"** button
4. Results will appear in the bottom table, sorted by match score

### Understanding Match Results

The results table shows:

- **Candidate Name**: Name extracted from resume
- **Match Score (%)**: Overall match percentage (0-100%)
- **Matched Skills**: Skills the candidate has that match job requirements
- **Missing Skills**: Required skills the candidate lacks
- **Email**: Candidate's contact email

### Match Score Calculation

The matching algorithm uses a weighted scoring system:

- **Skills Matching (70%)**: Percentage of required skills the candidate possesses
- **Experience (30%)**: Presence of work experience

## Project Structure

```
resume-tracker/
├── src/main/java/com/resumetracker/
│   ├── MainApp.java                    # JavaFX application entry point
│   ├── model/
│   │   ├── Candidate.java              # Candidate data model
│   │   ├── JobDescription.java         # Job description data model
│   │   └── MatchResult.java            # Match result data model
│   ├── database/
│   │   └── DatabaseManager.java        # SQLite database operations
│   ├── parser/
│   │   └── ResumeParser.java           # Resume parsing and NLP
│   └── matcher/
│       └── JobMatcher.java             # Matching algorithm
├── src/main/resources/
│   └── models/                         # OpenNLP model files (to be added)
├── pom.xml                             # Maven configuration
└── database.db                         # SQLite database (auto-created)
```

## Database Schema

### Candidates Table

```sql
CREATE TABLE candidates (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT,
    phone TEXT,
    skills TEXT,           -- Comma-separated
    education TEXT,        -- Pipe-separated
    experience TEXT        -- Pipe-separated
);
```

### Job Descriptions Table

```sql
CREATE TABLE job_descriptions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    required_skills TEXT,      -- Comma-separated
    required_experience INTEGER,
    raw_description TEXT
);
```

## Resume Format Recommendations

For best parsing results, structure resumes with clear sections:

```
John Doe
john.doe@email.com | (555) 123-4567

SKILLS
Java, Python, Spring Boot, MySQL, REST APIs, Git

EDUCATION
Bachelor of Science in Computer Science
University Name, 2018-2022

WORK EXPERIENCE
Software Engineer | Company Name | 2022-Present
- Developed REST APIs using Spring Boot
- Implemented microservices architecture
```

## Troubleshooting

### Issue: "NLP models not found"

**Solution:** Download and place OpenNLP models in `src/main/resources/models/` as described in setup step 2.

### Issue: "No candidates found"

**Solution:** Make sure to load resumes first using the "Load Resume(s)" button.

### Issue: "Could not extract skills"

**Solution:** Ensure the job description includes a "Skills:" or "Required Skills:" section with clear skill listings.

### Issue: Application won't start

**Solution:** Verify you have JDK 11+ and JavaFX is properly configured. Try rebuilding with `mvn clean install`.

## Extending the Application

### Adding More NLP Models

Download additional models from OpenNLP and update `ResumeParser.java`:

- `en-ner-location.bin` - Location recognition
- `en-ner-organization.bin` - Organization recognition

### Customizing the Matching Algorithm

Edit `JobMatcher.java` to adjust:

- Skill matching weight (currently 70%)
- Experience weight (currently 30%)
- Add new scoring criteria

### Supporting More File Formats

Extend `ResumeParser.java` to support:

- Plain text files (.txt)
- RTF files
- HTML resumes

## Development

### Building from Source

```bash
# Compile
mvn compile

# Run tests (if any)
mvn test

# Package as JAR
mvn package

# Clean build artifacts
mvn clean
```

### IDE Setup

**IntelliJ IDEA:**

1. File → Open → Select `pom.xml`
2. Right-click on `MainApp.java` → Run

**Eclipse:**

1. File → Import → Maven → Existing Maven Projects
2. Select the project directory
3. Run as Java Application

## Known Limitations

1. **Name Extraction**: May not work perfectly for all resume formats
2. **Experience Parsing**: Currently detects presence, not exact years
3. **Skill Matching**: Uses exact and fuzzy matching but may miss synonyms
4. **File Size**: Very large files (>10MB) may take longer to process

## Future Enhancements

- [ ] Support for more file formats (TXT, RTF, HTML)
- [ ] Advanced experience year calculation
- [ ] Skill synonym dictionary
- [ ] Export results to CSV/Excel
- [ ] Email integration for candidate outreach
- [ ] Machine learning-based matching
- [ ] Multi-language support
- [ ] Cloud database option

## License

This project is provided as-is for educational and commercial use.

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## Support

For issues and questions:

1. Check the Troubleshooting section
2. Review the code documentation
3. Open an issue on the repository

## Acknowledgments

- Apache PDFBox for PDF parsing
- Apache POI for DOCX parsing
- Apache OpenNLP for NLP capabilities
- JavaFX community for UI components

---

**Version:** 1.0.0  
**Last Updated:** 2025
