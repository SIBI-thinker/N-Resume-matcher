# Resume Parser and Job Matcher - Testing Guide

## 📋 Overview

This guide will walk you through testing all features of the Resume Parser and Job Matcher application.

---

## 🚀 Quick Start Testing

### Step 1: Launch the Application

The application should already be running. If not, run:

```powershell
D:\java-dep\maven\apache-maven-3.9.11-bin\apache-maven-3.9.11\bin\mvn.cmd javafx:run
```

### Step 2: Verify Application Window

You should see a window with:

- **Left Panel**: Candidate list (empty initially) with "Load Resumes" button
- **Center Panel**: Text area for job description
- **Bottom Panel**: Results table with columns (Candidate, Score, Matched Skills, Missing Skills)

---

## 🧪 Test Scenarios

### Test 1: Load Single Resume (Text Format)

**Steps:**

1. Click the **"Load Resumes"** button
2. Navigate to `D:\projects\Vishnu\resume-tracker\test-resumes\`
3. Select `john_doe_resume.txt`
4. Click "Open"

**Expected Results:**
✅ Console shows: "Processing file: john_doe_resume.txt"
✅ Candidate "John Doe" appears in the left panel
✅ Email: john.doe@email.com is extracted
✅ Phone: (555) 123-4567 is extracted
✅ Skills extracted: Java, Python, JavaScript, Spring Boot, etc.
✅ 5 years of experience detected

---

### Test 2: Load Multiple Resumes

**Steps:**

1. Click **"Load Resumes"** button
2. Select ALL three resume files (Ctrl + Click):
   - john_doe_resume.txt
   - jane_smith_resume.txt
   - robert_johnson_resume.txt
3. Click "Open"

**Expected Results:**
✅ All three candidates appear in the left panel:

- John Doe
- Jane Smith
- Robert Johnson
  ✅ Each candidate shows their email and phone
  ✅ Console shows parsing progress for each file

---

### Test 3: Match Candidates with Job Description

**Steps:**

1. Make sure candidates are loaded (from Test 2)
2. Open `test-resumes\sample_job_description.txt` in Notepad
3. Copy the entire job description
4. Paste it into the center text area of the application
5. Click the **"Match Candidates"** button at the bottom

**Expected Results:**
✅ Results table populates with all candidates
✅ Candidates are sorted by match score (highest first)
✅ Expected ranking:

- **John Doe** should have the HIGHEST score (~80-95%)
  - Reason: Has Java, Spring Boot, 5 years experience, AWS, Docker, Kubernetes
- **Jane Smith** should have MEDIUM score (~50-70%)
  - Reason: Has React, Docker, Kubernetes, but lacks Java/Spring Boot
- **Robert Johnson** should have LOWER score (~40-60%) - Reason: Has DevOps skills (Docker, Kubernetes, AWS) but not Java developer
  ✅ "Matched Skills" column shows common skills
  ✅ "Missing Skills" column shows required skills not found

---

### Test 4: View Candidate Details

**Steps:**

1. Click on a candidate name in the left panel (e.g., "John Doe")

**Expected Results:**
✅ Candidate details appear (may show in console or side panel depending on implementation)
✅ Shows complete information: name, email, phone, skills, education, experience

---

### Test 5: Test Custom Job Description

**Steps:**

1. Clear the job description text area
2. Type a custom job description, for example:

```
We need a Full-Stack Developer with React, Node.js, MongoDB,
and 3+ years of experience. Must know Docker and AWS.
```

3. Click "Match Candidates"

**Expected Results:**
✅ **Jane Smith** should now have the HIGHEST score

- Has React, Node.js, MongoDB, Docker, AWS, 3 years experience
  ✅ Results update based on new requirements
  ✅ Matched/Missing skills update accordingly

---

### Test 6: Test PDF Resume (If Available)

**Prerequisites:** You need a PDF resume file

**Steps:**

1. Place a PDF resume in the test-resumes folder
2. Click "Load Resumes"
3. Select the PDF file
4. Click "Open"

**Expected Results:**
✅ PDF text is extracted successfully
✅ Candidate information is parsed
✅ No errors in console

---

### Test 7: Test DOCX Resume (If Available)

**Prerequisites:** You need a DOCX resume file

**Steps:**

1. Place a DOCX resume in the test-resumes folder
2. Click "Load Resumes"
3. Select the DOCX file
4. Click "Open"

**Expected Results:**
✅ DOCX text is extracted successfully
✅ Candidate information is parsed
✅ No errors in console

---

## 🔍 Advanced Testing

### Test 8: Database Persistence

**Steps:**

1. Load candidates and match with a job description
2. Note the results
3. Close the application
4. Restart the application:
   ```powershell
   D:\java-dep\maven\apache-maven-3.9.11-bin\apache-maven-3.9.11\bin\mvn.cmd javafx:run
   ```
5. Click "Load Resumes" → the candidates should still be in the database

**Expected Results:**
✅ Previously loaded candidates persist in SQLite database
✅ Data survives application restart
✅ Database file `database.db` exists in project root

---

### Test 9: Skills Matching Algorithm

**Test Exact Match:**
Job requires: "Java"
Resume has: "Java"
✅ Should match perfectly

**Test Case Variations:**
Job requires: "javascript"
Resume has: "JavaScript"
✅ Should match (case-insensitive)

**Test Abbreviations:**
Job requires: "JS"
Resume has: "JavaScript"
✅ Should match (synonym detection)

**Test Similar Terms:**
Job requires: "React.js"
Resume has: "React"
✅ Should match (fuzzy matching)

---

### Test 10: Experience Calculation

**Steps:**

1. Load John Doe's resume (5 years experience)
2. Match with job requiring 4-6 years
3. Load Jane Smith's resume (3 years experience)
4. Match with same job

**Expected Results:**
✅ John Doe gets full experience score (matches 5 years requirement)
✅ Jane Smith gets partial experience score (only 3 years)
✅ Experience accounts for 30% of total score

---

## 📊 What to Check

### Console Output

Monitor the console for:

- ✅ "Connected to SQLite database"
- ✅ "Tokenizer model loaded successfully"
- ✅ "Person NER model loaded successfully"
- ✅ "Processing file: [filename]"
- ✅ Candidate details being extracted
- ❌ No error messages or stack traces

### GUI Elements

- ✅ Candidate list updates when loading resumes
- ✅ Results table updates when matching
- ✅ Scroll bars work if content is large
- ✅ Buttons are clickable and responsive
- ✅ Text area accepts input

### Database

Check if `database.db` file exists in:

```
D:\projects\Vishnu\resume-tracker\database.db
```

---

## 🐛 Common Issues & Solutions

### Issue 1: No candidates appear after loading

**Solution:** Check console for errors. File may not be readable or format unsupported.

### Issue 2: Match scores seem incorrect

**Solution:**

- Verify skills are being extracted (check console output)
- Ensure job description has clear skill requirements
- Remember: 70% weight for skills, 30% for experience

### Issue 3: Application crashes on file load

**Solution:**

- Check file format (TXT, PDF, DOCX supported)
- Ensure file is not corrupted
- Check console for specific error

### Issue 4: OpenNLP models not loading

**Solution:**

- Verify models exist: `src\main\resources\models\en-token.bin` and `en-ner-person.bin`
- Check console for "model loaded successfully" messages

---

## 📈 Expected Match Scores

For the sample job description (Senior Java Developer):

| Candidate      | Expected Score Range | Why                                            |
| -------------- | -------------------- | ---------------------------------------------- |
| John Doe       | 80-95%               | Perfect match: Java, Spring Boot, 5 years, AWS |
| Jane Smith     | 50-70%               | Partial match: Has Docker/K8s but not Java     |
| Robert Johnson | 40-60%               | Lower match: DevOps focus, not Java developer  |

---

## ✅ Success Criteria

Your application passes testing if:

- [x] Can load TXT resumes successfully
- [x] Extracts candidate information (name, email, phone, skills)
- [x] Parses job descriptions correctly
- [x] Calculates match scores accurately
- [x] Displays results in sorted order (highest score first)
- [x] Shows matched and missing skills
- [x] Persists data to SQLite database
- [x] No crashes or errors during normal operation
- [x] Console shows expected success messages
- [x] GUI is responsive and functional

---

## 🎯 Testing Checklist

Use this checklist to track your testing progress:

- [ ] Test 1: Load single resume - PASSED
- [ ] Test 2: Load multiple resumes - PASSED
- [ ] Test 3: Match with job description - PASSED
- [ ] Test 4: View candidate details - PASSED
- [ ] Test 5: Custom job description - PASSED
- [ ] Test 6: PDF resume parsing - PASSED (if applicable)
- [ ] Test 7: DOCX resume parsing - PASSED (if applicable)
- [ ] Test 8: Database persistence - PASSED
- [ ] Test 9: Skills matching algorithm - PASSED
- [ ] Test 10: Experience calculation - PASSED

---

## 📝 Notes

- The application uses **weighted scoring**: 70% skills + 30% experience
- Skills matching uses fuzzy logic for abbreviations and synonyms
- Experience is calculated from work history date ranges
- Database file is created in the project root directory
- NLP models improve name and entity extraction accuracy

---

## 🆘 Need Help?

If tests fail:

1. Check the console output for error messages
2. Verify all dependencies are loaded
3. Ensure OpenNLP models are in correct location
4. Check that database file has write permissions
5. Restart the application and try again

---

**Happy Testing! 🎉**
