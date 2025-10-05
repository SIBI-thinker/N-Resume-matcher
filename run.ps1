# Resume Parser & Job Matcher - Build and Run Script
# For Windows PowerShell

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Resume Parser & Job Matcher" -ForegroundColor Cyan
Write-Host "Build and Run Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check Java version
Write-Host "Checking Java version..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1 | Select-String "version"
if ($javaVersion) {
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} else {
    Write-Host "✗ Java not found! Please install Java 11 or higher." -ForegroundColor Red
    exit 1
}

# Check Maven
Write-Host "Checking Maven..." -ForegroundColor Yellow
$mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven"
if ($mavenVersion) {
    Write-Host "✓ Maven found: $mavenVersion" -ForegroundColor Green
} else {
    Write-Host "✗ Maven not found! Please install Maven 3.6+." -ForegroundColor Red
    exit 1
}

# Check for OpenNLP models
Write-Host ""
Write-Host "Checking for OpenNLP models..." -ForegroundColor Yellow
$modelsDir = "src\main\resources\models"
$tokenModel = "$modelsDir\en-token.bin"
$nerModel = "$modelsDir\en-ner-person.bin"

$modelsFound = $true
if (!(Test-Path $tokenModel)) {
    Write-Host "✗ Missing: en-token.bin" -ForegroundColor Red
    $modelsFound = $false
}
if (!(Test-Path $nerModel)) {
    Write-Host "✗ Missing: en-ner-person.bin" -ForegroundColor Red
    $modelsFound = $false
}

if (!$modelsFound) {
    Write-Host ""
    Write-Host "WARNING: OpenNLP models not found!" -ForegroundColor Yellow
    Write-Host "Download from: https://opennlp.sourceforge.net/models-1.5/" -ForegroundColor Yellow
    Write-Host "Place in: $modelsDir" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Application will run with reduced NLP accuracy." -ForegroundColor Yellow
    Write-Host ""
    $continue = Read-Host "Continue anyway? (y/n)"
    if ($continue -ne "y") {
        exit 0
    }
} else {
    Write-Host "✓ OpenNLP models found" -ForegroundColor Green
}

# Ask user what to do
Write-Host ""
Write-Host "Select an option:" -ForegroundColor Cyan
Write-Host "1. Clean and build project" -ForegroundColor White
Write-Host "2. Run application" -ForegroundColor White
Write-Host "3. Clean, build, and run" -ForegroundColor White
Write-Host "4. Package as JAR" -ForegroundColor White
Write-Host "5. Exit" -ForegroundColor White
Write-Host ""

$choice = Read-Host "Enter choice (1-5)"

switch ($choice) {
    "1" {
        Write-Host ""
        Write-Host "Building project..." -ForegroundColor Yellow
        mvn clean install
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✓ Build successful!" -ForegroundColor Green
        } else {
            Write-Host "✗ Build failed!" -ForegroundColor Red
        }
    }
    "2" {
        Write-Host ""
        Write-Host "Running application..." -ForegroundColor Yellow
        mvn javafx:run
    }
    "3" {
        Write-Host ""
        Write-Host "Cleaning and building..." -ForegroundColor Yellow
        mvn clean install
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✓ Build successful!" -ForegroundColor Green
            Write-Host ""
            Write-Host "Starting application..." -ForegroundColor Yellow
            mvn javafx:run
        } else {
            Write-Host "✗ Build failed!" -ForegroundColor Red
        }
    }
    "4" {
        Write-Host ""
        Write-Host "Packaging as JAR..." -ForegroundColor Yellow
        mvn clean package
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✓ Package created!" -ForegroundColor Green
            Write-Host "JAR location: target\resume-parser-job-matcher-1.0-SNAPSHOT.jar" -ForegroundColor Cyan
        } else {
            Write-Host "✗ Package failed!" -ForegroundColor Red
        }
    }
    "5" {
        Write-Host "Exiting..." -ForegroundColor Yellow
        exit 0
    }
    default {
        Write-Host "Invalid choice!" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Script completed" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
