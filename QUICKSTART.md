# Quick Start Guide

## Running the Application

### Prerequisites Check

1. **Java Version**: Run `java -version` (should be 11+)
2. **Maven Version**: Run `mvn -version` (should be 3.6+)

### First Time Setup

1. **Download OpenNLP Models** (Important!)

   ```
   Visit: https://opennlp.sourceforge.net/models-1.5/
   Download:
   - en-token.bin
   - en-ner-person.bin

   Place in: src/main/resources/models/
   ```

2. **Build the Project**

   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn javafx:run
   ```

### Alternative Run Methods

**Using Java directly:**

```bash
mvn package
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/resume-parser-job-matcher-1.0-SNAPSHOT.jar
```

**From IDE (IntelliJ/Eclipse):**

- Open project
- Navigate to `MainApp.java`
- Right-click → Run

## Basic Usage Flow

1. **Load Resumes**

   - Click "Load Resume(s)"
   - Select PDF or DOCX files
   - Wait for parsing to complete

2. **Enter Job Description**

   - Paste job description in center text area
   - Include section with "Required Skills:" and list skills
   - Optionally include "Experience: X years"

3. **Match Candidates**

   - Click "Match Candidates" button
   - View results sorted by match score
   - Top match is highlighted in success message

4. **Review Results**
   - Check matched skills vs missing skills
   - Use candidate contact info (email/phone) for outreach
   - Export or save results as needed

## Sample Job Description Format

```
Job Title: Senior Java Developer

Required Skills:
Java, Spring Boot, MySQL, REST APIs, Git, Docker

Experience: 3+ years

Responsibilities:
- Design and develop microservices
- Write clean, maintainable code
- Collaborate with cross-functional teams
```

## Common Commands

```bash
# Clean and build
mvn clean install

# Run application
mvn javafx:run

# Package as JAR
mvn package

# Clean build artifacts
mvn clean

# Skip tests during build
mvn clean install -DskipTests
```

## Tips for Best Results

### Resume Format

- Use clear section headers (SKILLS, EDUCATION, EXPERIENCE)
- List skills with commas or bullets
- Include contact information at the top

### Job Descriptions

- Always include a "Required Skills:" section
- List skills clearly (comma or bullet separated)
- Specify years of experience if relevant

### Database Management

- Use "Refresh Candidates" to reload from database
- Use "Clear All Candidates" to reset database
- Database file is `database.db` in project root

## Troubleshooting Quick Fixes

**Problem**: Application won't start

```bash
# Solution: Clean rebuild
mvn clean install
mvn javafx:run
```

**Problem**: NLP models not found

```bash
# Solution: Check models directory
ls -la src/main/resources/models/
# Should see: en-token.bin, en-ner-person.bin
```

**Problem**: JavaFX error

```bash
# Solution: Verify Java version
java -version
# Should be Java 11 or higher
```

**Problem**: No candidates showing

```bash
# Solution: Check database
# Delete database.db and restart application
rm database.db
mvn javafx:run
```

## Getting Help

1. Check main README.md for detailed documentation
2. Review console output for error messages
3. Ensure all prerequisites are installed
4. Verify file formats (PDF/DOCX only)

## Next Steps

After successful setup:

- Try loading sample resumes
- Experiment with different job descriptions
- Customize matching weights in `JobMatcher.java`
- Add more NLP models for enhanced features

---

For detailed documentation, see [README.md](README.md)
