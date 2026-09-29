================================================================================
                    CLINICAL COPILOT OS - SETUP REQUIRED
================================================================================

PROBLEM: Application is NOT running because Java 17+ is NOT installed.

SOLUTION: Follow these 3 simple steps:

--------------------------------------------------------------------------------
STEP 1: INSTALL JAVA 17
--------------------------------------------------------------------------------

1. Open this link in your browser:
   https://adoptium.net/temurin/releases/?version=17

2. Download:
   - Operating System: Windows
   - Architecture: x64  
   - Package Type: JDK
   - File: .msi installer

3. Run the installer (click Next, Next, Finish)

4. Restart PowerShell/Terminal

--------------------------------------------------------------------------------
STEP 2: VERIFY JAVA INSTALLATION
--------------------------------------------------------------------------------

Open PowerShell and run:

   java -version

You should see: "openjdk version 17.0.x"

If you see this → Success! Continue to Step 3
If you see "not recognized" → Restart PowerShell and try again

--------------------------------------------------------------------------------
STEP 3: GET API KEY & RUN
--------------------------------------------------------------------------------

1. Get FREE Groq API key:
   - Visit: https://console.groq.com
   - Sign up (no credit card needed)
   - Create API key (starts with "gsk_")

2. Edit the .env file in this folder:
   - Open: .env
   - Replace: your_groq_key_here
   - With: gsk_your_actual_key_here
   - Save the file

3. Run the application:

   .\START-WINDOWS.ps1

4. Open browser to: http://localhost:8080

================================================================================
THAT'S IT! Total time: ~10 minutes
================================================================================

WHAT YOU HAVE NOW:
✓ Node.js 24.16.0
✓ Git
✓ Source code  
✓ .env file template
✓ Startup script (START-WINDOWS.ps1)

WHAT YOU NEED:
✗ Java 17+ (INSTALL THIS)
✗ Groq API Key (FREE from console.groq.com)

================================================================================
HELPFUL FILES:
================================================================================

WHY_NOT_RUNNING.md          - Detailed explanation of the problem
QUICK_START_WINDOWS.md      - Complete setup guide
START-WINDOWS.ps1           - Automated startup script (use after Java install)
SYSTEM_OVERVIEW.md          - How the system works
.env                        - API key configuration (edit this)

================================================================================
NEED HELP?
================================================================================

1. Java installation issues:
   - See: QUICK_START_WINDOWS.md (Troubleshooting section)

2. Understanding the system:
   - See: SYSTEM_OVERVIEW.md

3. After it's running:
   - See: docs/DEMO_GUIDE.md

================================================================================
DIRECT DOWNLOAD LINK FOR JAVA:
https://adoptium.net/temurin/releases/?version=17
================================================================================
