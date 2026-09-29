# Java Installation Checker

Write-Host "========================================"
Write-Host "Java Installation Diagnostic Tool"
Write-Host "========================================"
Write-Host ""

# Check if java command is available
Write-Host "[1] Checking if java command is available..."
$javaAvailable = $false
try {
    $output = & java -version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "SUCCESS: Java is available!" -ForegroundColor Green
        Write-Host $output
        $javaAvailable = $true
    }
} catch {
    Write-Host "Java command not found in PATH" -ForegroundColor Red
}
Write-Host ""

if ($javaAvailable) {
    Write-Host "Java is ready! You can now run: .\START-WINDOWS.ps1" -ForegroundColor Green
    exit 0
}

# Search for Java installations
Write-Host "[2] Searching for Java installations..."
$javaLocations = @(
    "C:\Program Files\Java",
    "C:\Program Files\Eclipse Adoptium",
    "C:\Program Files\OpenJDK",
    "C:\Program Files (x86)\Java"
)

$foundJava = $false
foreach ($location in $javaLocations) {
    if (Test-Path $location) {
        Write-Host "Found: $location" -ForegroundColor Yellow
        $foundJava = $true
    }
}

if (-not $foundJava) {
    Write-Host "No Java directories found" -ForegroundColor Red
}
Write-Host ""

# Check JAVA_HOME
Write-Host "[3] Checking JAVA_HOME environment variable..."
if ($env:JAVA_HOME) {
    Write-Host "JAVA_HOME = $env:JAVA_HOME" -ForegroundColor Green
} else {
    Write-Host "JAVA_HOME is not set" -ForegroundColor Red
}
Write-Host ""

# Recommendations
Write-Host "========================================"
Write-Host "WHAT TO DO:"
Write-Host "========================================"
Write-Host ""

if (-not $foundJava) {
    Write-Host "Java is NOT installed yet." -ForegroundColor Red
    Write-Host ""
    Write-Host "Install Java 17:" -ForegroundColor Yellow
    Write-Host "1. Download: https://adoptium.net/temurin/releases/?version=17"
    Write-Host "2. Select: Windows, x64, JDK, .msi installer"
    Write-Host "3. Run the installer"
    Write-Host "4. Restart PowerShell"
    Write-Host "5. Run this check again"
} else {
    Write-Host "Java appears to be installed but not accessible." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Try this:" -ForegroundColor Yellow
    Write-Host "1. Close this PowerShell window completely"
    Write-Host "2. Open a NEW PowerShell window"
    Write-Host "3. Run: java -version"
    Write-Host "4. If it works, run: .\START-WINDOWS.ps1"
}
Write-Host ""
