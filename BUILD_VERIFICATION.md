# Build and Verification Guide

## Pre-Build Checklist

### 1. System Requirements

- [ ] Java JDK 11 or higher installed
- [ ] Maven 3.6+ installed
- [ ] Git installed (optional)
- [ ] 500MB free disk space

### 2. Verify Installations

**Check Java:**

```powershell
java -version
```

Expected output: `java version "11.x.x"` or higher

**Check Maven:**

```powershell
mvn -version
```

Expected output: `Apache Maven 3.6.x` or higher

### 3. Download OpenNLP Models

**Required Models:**

1. `en-token.bin` - [Download here](http://opennlp.sourceforge.net/models-1.5/en-token.bin)
2. `en-ner-person.bin` - [Download here](http://opennlp.sourceforge.net/models-1.5/en-ner-person.bin)

**Installation:**

```powershell
# Create models directory if it doesn't exist
New-Item -ItemType Directory -Force -Path src\main\resources\models

# Move downloaded models to the directory
Move-Item en-token.bin src\main\resources\models\
Move-Item en-ner-person.bin src\main\resources\models\
```

## Build Process

### Option 1: Using PowerShell Script (Recommended)

```powershell
# Navigate to project directory
cd d:\projects\Vishnu\resume-tracker

# Run the build script
.\run.ps1

# Select option 3: Clean, build, and run
```

### Option 2: Manual Maven Commands

**Step 1: Clean previous builds**

```powershell
mvn clean
```

**Step 2: Install dependencies**

```powershell
mvn install
```

**Step 3: Build project**

```powershell
mvn clean install
```

**Step 4: Run application**

```powershell
mvn javafx:run
```

## Build Verification

### Expected Console Output

```
[INFO] Scanning for projects...
[INFO]
[INFO] ----------------< com.resumetracker:resume-parser-job-matcher >-----------------
[INFO] Building Resume Parser and Job Matcher 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ resume-parser-job-matcher ---
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ resume-parser-job-matcher ---
[INFO] --- maven-compiler-plugin:3.10.1:compile (default-compile) @ resume-parser-job-matcher ---
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ resume-parser-job-matcher ---
[INFO] --- maven-compiler-plugin:3.10.1:testCompile (default-testCompile) @ resume-parser-job-matcher ---
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ resume-parser-job-matcher ---
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ resume-parser-job-matcher ---
[INFO] Building jar: target/resume-parser-job-matcher-1.0-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### Application Startup Output

```
Connected to SQLite database: jdbc:sqlite:database.db
Database tables created successfully.
Tokenizer model loaded successfully.
Person NER model loaded successfully.
```

If models are not found:

```
Warning: Could not load NLP models. NLP features will be limited.
Tokenizer model not found. Using basic tokenization.
Person NER model not found. Using basic name extraction.
```

## Troubleshooting Build Issues

### Issue: "JAVA_HOME not set"

**Solution:**

```powershell
# Set JAVA_HOME (adjust path to your Java installation)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

### Issue: "mvn command not found"

**Solution:**

1. Install Maven from: https://maven.apache.org/download.cgi
2. Add Maven bin directory to PATH
3. Restart PowerShell

### Issue: "JavaFX runtime components are missing"

**Solution:**
The pom.xml already includes JavaFX dependencies. Try:

```powershell
mvn clean install -U
```

### Issue: "Cannot find symbol" compilation errors

**Solution:**

```powershell
# Delete Maven cache and rebuild
Remove-Item -Recurse -Force $HOME\.m2\repository\com\resumetracker
mvn clean install
```

### Issue: SQLite database locked

**Solution:**

```powershell
# Delete existing database
Remove-Item database.db
# Restart application
mvn javafx:run
```

### Issue: OutOfMemoryError

**Solution:**

```powershell
# Increase Maven memory
$env:MAVEN_OPTS = "-Xmx1024m -XX:MaxPermSize=256m"
mvn clean install
```

## Runtime Verification

### 1. GUI Should Display

When application starts, you should see:

- Window title: "Resume Parser & Job Matcher"
- Left panel with "Loaded Candidates" list
- Center panel with job description text area
- "Load Resume(s)" button
- "Match Candidates" button
- Bottom results table

### 2. Test Basic Functionality

**Test 1: Load Resume**

1. Click "Load Resume(s)" button
2. File chooser should open
3. Select a PDF or DOCX file
4. Status should show "Parsing: [filename]..."
5. Success message should appear

**Test 2: Database Connection**

1. Check project root directory
2. `database.db` file should be created
3. Console should show "Connected to SQLite database"

**Test 3: Match Candidates**

1. Enter sample job description
2. Click "Match Candidates"
3. Results should appear in table
4. Top match should be highlighted

## Project Structure Verification

Ensure all files exist:

```
resume-tracker/
├── pom.xml                                    ✓
├── README.md                                  ✓
├── QUICKSTART.md                              ✓
├── PROJECT_OVERVIEW.md                        ✓
├── BUILD_VERIFICATION.md                      ✓
├── .gitignore                                 ✓
├── run.ps1                                    ✓
│
├── src/main/java/
│   ├── module-info.java                       ✓
│   └── com/resumetracker/
│       ├── MainApp.java                       ✓
│       ├── model/
│       │   ├── Candidate.java                 ✓
│       │   ├── JobDescription.java            ✓
│       │   └── MatchResult.java               ✓
│       ├── database/
│       │   └── DatabaseManager.java           ✓
│       ├── parser/
│       │   ├── ResumeParser.java              ✓
│       │   └── JobDescriptionParser.java      ✓
│       └── matcher/
│           └── JobMatcher.java                ✓
│
├── src/main/resources/
│   └── models/
│       ├── README.md                          ✓
│       ├── en-token.bin                       (Download required)
│       └── en-ner-person.bin                  (Download required)
│
└── test-data/
    └── SAMPLE_RESUMES.md                      ✓
```

## Dependency Verification

All dependencies should download automatically via Maven:

```
[INFO] --- maven-dependency-plugin:3.1.2:tree ---
[INFO] com.resumetracker:resume-parser-job-matcher:jar:1.0-SNAPSHOT
[INFO] +- org.openjfx:javafx-controls:jar:17.0.2:compile
[INFO] +- org.openjfx:javafx-fxml:jar:17.0.2:compile
[INFO] +- org.apache.pdfbox:pdfbox:jar:2.0.27:compile
[INFO] +- org.apache.poi:poi:jar:5.2.3:compile
[INFO] +- org.apache.poi:poi-ooxml:jar:5.2.3:compile
[INFO] +- org.apache.opennlp:opennlp-tools:jar:1.9.4:compile
[INFO] +- org.xerial:sqlite-jdbc:jar:3.41.2.1:compile
[INFO] \- org.slf4j:slf4j-simple:jar:1.7.36:compile
```

## Performance Benchmarks

Expected performance on typical hardware:

| Operation                    | Expected Time |
| ---------------------------- | ------------- |
| Application startup          | 2-5 seconds   |
| Load single resume           | 1-3 seconds   |
| Parse PDF (5 pages)          | 2-4 seconds   |
| Parse DOCX (5 pages)         | 1-2 seconds   |
| Match 10 candidates          | <1 second     |
| Match 100 candidates         | 1-2 seconds   |
| Database query (100 records) | <100ms        |

## Success Criteria

Your build is successful if:

- ✓ Maven build completes without errors
- ✓ All Java files compile successfully
- ✓ Application window launches
- ✓ Database file is created
- ✓ GUI is responsive and functional
- ✓ File chooser opens
- ✓ Resume parsing works (with or without NLP models)
- ✓ Matching algorithm produces results
- ✓ Results display in table

## Next Steps After Successful Build

1. **Download Sample Resumes**: Create test PDF/DOCX files
2. **Test Full Workflow**: Load → Parse → Match
3. **Customize**: Adjust matching weights if needed
4. **Deploy**: Package as JAR for distribution
5. **Extend**: Add new features as required

## Getting Help

If you encounter issues:

1. Check console output for error messages
2. Review this verification guide
3. Check README.md troubleshooting section
4. Verify Java and Maven versions
5. Ensure all files are present
6. Try clean rebuild: `mvn clean install`

## Build Artifacts

After successful build, you should have:

```
target/
├── classes/                                   # Compiled classes
├── generated-sources/                         # Generated source files
├── maven-archiver/                            # Maven metadata
├── maven-status/                              # Build status
└── resume-parser-job-matcher-1.0-SNAPSHOT.jar # Executable JAR
```

## Final Verification Command

Run this comprehensive check:

```powershell
# Full verification
mvn clean verify

# Expected output should end with:
# [INFO] BUILD SUCCESS
```

---

**Build Status**: Ready for compilation and execution  
**Estimated Build Time**: 2-5 minutes (first time, includes dependency download)  
**Estimated Download Size**: ~50MB (dependencies)  
**Final Package Size**: ~30MB (with dependencies)
