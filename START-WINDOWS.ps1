# Clinical Copilot OS - Windows PowerShell Startup Script
# Run this script after installing Java 17+

Write-Host "=========================================="
Write-Host "Clinical Copilot OS - Setup & Run"
Write-Host "=========================================="
Write-Host ""

# Check Java
Write-Host "[1/4] Checking Java..." -ForegroundColor Blue
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_ -replace '.*"(\d+).*', '$1' }
    if ([int]$javaVersion -lt 17) {
        Write-Host "X Java 17+ required (found: $javaVersion)" -ForegroundColor Red
        Write-Host "Install Java 17 from: https://adoptium.net/" -ForegroundColor Yellow
        exit 1
    }
    Write-Host "✓ Java $javaVersion found" -ForegroundColor Green
} catch {
    Write-Host "X Java not found" -ForegroundColor Red
    Write-Host ""
    Write-Host "INSTALL JAVA 17 FIRST:" -ForegroundColor Yellow
    Write-Host "1. Visit: https://adoptium.net/temurin/releases/?version=17" -ForegroundColor Cyan
    Write-Host "2. Download: Windows x64 JDK .msi installer" -ForegroundColor Cyan
    Write-Host "3. Run installer" -ForegroundColor Cyan
    Write-Host "4. Restart PowerShell" -ForegroundColor Cyan
    Write-Host "5. Run this script again" -ForegroundColor Cyan
    Write-Host ""
    exit 1
}
Write-Host ""

# Check environment variables
Write-Host "[2/4] Checking environment..." -ForegroundColor Blue
if (Test-Path .env) {
    Write-Host "✓ .env file found" -ForegroundColor Green
    
    # Load .env file
    Get-Content .env | ForEach-Object {
        if ($_ -match '^([^=]+)=(.*)$') {
            $name = $matches[1]
            $value = $matches[2]
            [Environment]::SetEnvironmentVariable($name, $value, "Process")
        }
    }
    
    # Check if API key is set
    $groqKey = [Environment]::GetEnvironmentVariable("GROQ_API_KEY", "Process")
    if ([string]::IsNullOrWhiteSpace($groqKey) -or $groqKey -eq "your_groq_key_here") {
        Write-Host "X GROQ_API_KEY not configured" -ForegroundColor Red
        Write-Host ""
        Write-Host "GET FREE API KEY:" -ForegroundColor Yellow
        Write-Host "1. Visit: https://console.groq.com" -ForegroundColor Cyan
        Write-Host "2. Sign up (free, no credit card)" -ForegroundColor Cyan
        Write-Host "3. Create API key (starts with 'gsk_')" -ForegroundColor Cyan
        Write-Host "4. Edit .env file and replace 'your_groq_key_here' with your key" -ForegroundColor Cyan
        Write-Host ""
        exit 1
    }
    Write-Host "✓ API keys configured" -ForegroundColor Green
} else {
    Write-Host "⚠ .env file not found, creating template..." -ForegroundColor Yellow
    @"
# Required: LLAMA 3.3 70B for text processing
GROQ_API_KEY=your_groq_key_here

# Optional: LLAMA 3.2 11B Vision for image analysis
OPENROUTER_API_KEY=your_openrouter_key_here
"@ | Out-File -FilePath .env -Encoding UTF8
    Write-Host ""
    Write-Host "GET FREE API KEY:" -ForegroundColor Yellow
    Write-Host "1. Visit: https://console.groq.com" -ForegroundColor Cyan
    Write-Host "2. Sign up (free)" -ForegroundColor Cyan
    Write-Host "3. Create API key" -ForegroundColor Cyan
    Write-Host "4. Edit .env file with your key" -ForegroundColor Cyan
    Write-Host "5. Run this script again" -ForegroundColor Cyan
    exit 1
}
Write-Host ""

# Build application
Write-Host "[3/4] Building application..." -ForegroundColor Blue
if (Test-Path "target\clinical-copilot-1.0.0.jar") {
    Write-Host "✓ JAR already built" -ForegroundColor Green
    Write-Host "  (Run '.\mvnw.cmd clean package -DskipTests' to rebuild)" -ForegroundColor Gray
} else {
    Write-Host "Building JAR file (this may take a few minutes)..." -ForegroundColor Yellow
    .\mvnw.cmd clean package -DskipTests
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Build complete" -ForegroundColor Green
    } else {
        Write-Host "X Build failed" -ForegroundColor Red
        exit 1
    }
}
Write-Host ""

# Start application
Write-Host "[4/4] Starting application..." -ForegroundColor Blue
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Clinical Copilot OS Starting..." -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Access the application:" -ForegroundColor White
Write-Host "  Web UI: http://localhost:8080" -ForegroundColor Yellow
Write-Host "  Mobile UI: http://localhost:8080/mobile.html" -ForegroundColor Yellow
Write-Host "  API Health: http://localhost:8080/api/v1/health" -ForegroundColor Yellow
Write-Host "  Database: http://localhost:8080/h2-console" -ForegroundColor Yellow
Write-Host ""
Write-Host "Press Ctrl+C to stop" -ForegroundColor Gray
Write-Host ""

# Set environment variables for Java process
$env:GROQ_API_KEY = [Environment]::GetEnvironmentVariable("GROQ_API_KEY", "Process")
$env:OPENROUTER_API_KEY = [Environment]::GetEnvironmentVariable("OPENROUTER_API_KEY", "Process")

# Run application
java -jar target\clinical-copilot-1.0.0.jar
