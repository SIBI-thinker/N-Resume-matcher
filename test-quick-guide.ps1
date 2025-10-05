# Quick Test Script for Resume Parser

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  Resume Parser Quick Test Guide" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "TEST FILES CREATED:" -ForegroundColor Green
Write-Host "Location: D:\projects\Vishnu\resume-tracker\test-resumes\" -ForegroundColor Yellow
Write-Host ""

Write-Host "Available Test Resumes:" -ForegroundColor White
Write-Host "  1. john_doe_resume.txt       - Java Developer (5 years)" -ForegroundColor Gray
Write-Host "  2. jane_smith_resume.txt     - Full-Stack Developer (3 years)" -ForegroundColor Gray
Write-Host "  3. robert_johnson_resume.txt - DevOps Engineer (7 years)" -ForegroundColor Gray
Write-Host "  4. sample_job_description.txt - Senior Java Developer Job" -ForegroundColor Gray
Write-Host ""

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  STEP-BY-STEP TESTING" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "STEP 1: Load Resumes" -ForegroundColor Yellow
Write-Host "  • Click 'Load Resumes' button in the application" -ForegroundColor White
Write-Host "  • Navigate to: test-resumes folder" -ForegroundColor White
Write-Host "  • Select all 3 resume files (Ctrl+Click)" -ForegroundColor White
Write-Host "  • Click 'Open'" -ForegroundColor White
Write-Host "  ✓ Expected: 3 candidates appear in left panel" -ForegroundColor Green
Write-Host ""

Write-Host "STEP 2: Load Job Description" -ForegroundColor Yellow
Write-Host "  • Open: test-resumes\sample_job_description.txt" -ForegroundColor White
Write-Host "  • Copy the entire content" -ForegroundColor White
Write-Host "  • Paste into center text area of application" -ForegroundColor White
Write-Host ""

Write-Host "STEP 3: Match Candidates" -ForegroundColor Yellow
Write-Host "  • Click 'Match Candidates' button" -ForegroundColor White
Write-Host "  ✓ Expected Results:" -ForegroundColor Green
Write-Host "    - John Doe: ~85-90% match (BEST match for Java role)" -ForegroundColor Green
Write-Host "    - Jane Smith: ~55-65% match (Full-stack, not Java focused)" -ForegroundColor Green
Write-Host "    - Robert Johnson: ~45-55% match (DevOps, not Java dev)" -ForegroundColor Green
Write-Host ""

Write-Host "STEP 4: Verify Results Table" -ForegroundColor Yellow
Write-Host "  ✓ Check Matched Skills column (should show Java, Spring Boot, AWS, etc.)" -ForegroundColor Green
Write-Host "  ✓ Check Missing Skills column (skills candidate doesn't have)" -ForegroundColor Green
Write-Host "  ✓ Scores should be sorted highest to lowest" -ForegroundColor Green
Write-Host ""

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  QUICK CHECKS" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Console Output to Verify:" -ForegroundColor Yellow
Write-Host "  ✓ 'Connected to SQLite database'" -ForegroundColor Green
Write-Host "  ✓ 'Tokenizer model loaded successfully'" -ForegroundColor Green
Write-Host "  ✓ 'Person NER model loaded successfully'" -ForegroundColor Green
Write-Host "  ✓ 'Processing file: [filename]' for each resume" -ForegroundColor Green
Write-Host ""

Write-Host "Database File:" -ForegroundColor Yellow
$dbPath = "D:\projects\Vishnu\resume-tracker\database.db"
if (Test-Path $dbPath) {
    Write-Host "  ✓ Database exists: $dbPath" -ForegroundColor Green
    $dbSize = (Get-Item $dbPath).Length / 1KB
    Write-Host "  ✓ Database size: $($dbSize.ToString('F2')) KB" -ForegroundColor Green
} else {
    Write-Host "  ⚠ Database not yet created (will be created on first run)" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  ADVANCED TESTING" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Test Different Job Descriptions:" -ForegroundColor Yellow
Write-Host ""
Write-Host "Try this Full-Stack role (Jane should score highest):" -ForegroundColor White
Write-Host "-----------------------------------------------------" -ForegroundColor Gray
Write-Host "We need a Full-Stack Developer with React, Node.js," -ForegroundColor Cyan
Write-Host "MongoDB, and 3 years experience. Docker and AWS required." -ForegroundColor Cyan
Write-Host "-----------------------------------------------------" -ForegroundColor Gray
Write-Host ""

Write-Host "Try this DevOps role (Robert should score highest):" -ForegroundColor White
Write-Host "-----------------------------------------------------" -ForegroundColor Gray
Write-Host "Seeking DevOps Engineer with Kubernetes, Docker, Terraform," -ForegroundColor Cyan
Write-Host "AWS, and 6+ years experience. CI/CD pipeline expertise required." -ForegroundColor Cyan
Write-Host "-----------------------------------------------------" -ForegroundColor Gray
Write-Host ""

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  TIPS FOR BEST RESULTS" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "• Job descriptions should list specific skills" -ForegroundColor White
Write-Host "• Include years of experience requirement" -ForegroundColor White
Write-Host "• Clear format helps the parser work better" -ForegroundColor White
Write-Host "• Try loading different resume formats (PDF, DOCX)" -ForegroundColor White
Write-Host ""

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  READY TO TEST!" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Your application should be running now." -ForegroundColor Green
Write-Host "Follow the steps above to test all features." -ForegroundColor Green
Write-Host ""
Write-Host "For detailed testing guide, see: TESTING_GUIDE.md" -ForegroundColor Yellow
Write-Host ""

# Open test resumes folder
Write-Host "Opening test-resumes folder..." -ForegroundColor Cyan
Start-Process "D:\projects\Vishnu\resume-tracker\test-resumes"
Write-Host "✓ Folder opened!" -ForegroundColor Green
