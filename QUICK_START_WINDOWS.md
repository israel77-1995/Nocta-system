# Quick Start Guide - Windows

## Current Problem: Java Not Installed ❌

The application **cannot run** without Java 17 or higher installed on your system.

---

## Step-by-Step Setup (5 Minutes)

### **Step 1: Install Java 17** ⚙️

**Option A: Eclipse Temurin (Recommended - Free & Open Source)**

1. Open this link: https://adoptium.net/temurin/releases/?version=17
2. Select:
   - **Operating System:** Windows
   - **Architecture:** x64
   - **Package Type:** JDK
   - **Version:** 17 - LTS
3. Download the `.msi` installer
4. Run the installer (click through all defaults)
5. **Important:** Restart PowerShell/Terminal after installation

**Option B: Oracle JDK**

1. Visit: https://www.oracle.com/java/technologies/downloads/#java17
2. Download Windows installer
3. Install and restart terminal

---

### **Step 2: Verify Java Installation** ✅

After installing and restarting your terminal:

```powershell
java -version
```

You should see something like:
```
openjdk version "17.0.x" ...
```

---

### **Step 3: Get Free API Key** 🔑

1. Visit: https://console.groq.com
2. Sign up (completely free, no credit card required)
3. Click "Create API Key"
4. Copy the key (starts with `gsk_`)
5. Edit the `.env` file in this project:
   ```
   GROQ_API_KEY=gsk_paste_your_key_here
   ```

---

### **Step 4: Run the Application** 🚀

**Easy Method (Automated Script):**
```powershell
.\START-WINDOWS.ps1
```

**Manual Method:**
```powershell
# Build
.\mvnw.cmd clean package -DskipTests

# Run
java -jar target\clinical-copilot-1.0.0.jar
```

---

### **Step 5: Access the Application** 🌐

Once running, open your browser to:

- **Web Interface:** http://localhost:8080
- **Mobile Interface:** http://localhost:8080/mobile.html
- **API Health Check:** http://localhost:8080/api/v1/health
- **Database Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:clinicaldb`
  - Username: `sa`
  - Password: (leave empty)

---

## Troubleshooting 🔧

### **Error: "java not recognized"**
- Java is not installed OR
- You didn't restart PowerShell after installation
- Solution: Install Java, restart terminal, try again

### **Error: "JAVA_HOME not defined"**
- Set JAVA_HOME manually:
```powershell
# Find Java installation
$javaPath = (Get-Command java).Source
$javaHome = Split-Path (Split-Path $javaPath)
[Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, "User")

# Restart PowerShell
```

### **Error: "GROQ_API_KEY not set"**
- You haven't configured the API key in `.env` file
- Solution: Get key from https://console.groq.com and add to `.env`

### **Error: "Port 8080 already in use"**
- Another application is using port 8080
- Solution: Stop the other app or change port in `src/main/resources/application.properties`

### **Build takes too long**
- First build downloads dependencies (takes 2-5 minutes)
- Subsequent builds are much faster
- Make sure you have internet connection

---

## What You Need

✅ **Java 17+** - Runtime environment  
✅ **Free Groq API Key** - For LLAMA AI  
✅ **Internet Connection** - To download dependencies and call API  
✅ **2GB RAM** - Minimum system requirement  
✅ **Port 8080** - Must be available  

---

## Alternative: Use Docker 🐳

If you have Docker Desktop installed:

```powershell
# Build Docker image
docker build -t clinical-copilot .

# Run container
docker run -p 8080:8080 --env-file .env clinical-copilot
```

---

## Quick Commands Reference

```powershell
# Check Java version
java -version

# Check if port 8080 is available
netstat -ano | findstr :8080

# Kill process on port 8080
# Get PID from above command, then:
taskkill /PID <PID> /F

# Build only
.\mvnw.cmd clean package -DskipTests

# Run tests
.\mvnw.cmd test

# Run application
java -jar target\clinical-copilot-1.0.0.jar

# Run with specific profile
java -jar target\clinical-copilot-1.0.0.jar --spring.profiles.active=prod
```

---

## System Requirements

| Component | Requirement |
|-----------|-------------|
| **OS** | Windows 10/11 |
| **Java** | 17 or higher (JDK) |
| **RAM** | 2GB minimum, 4GB recommended |
| **Disk** | 500MB for app + dependencies |
| **Port** | 8080 must be free |
| **Internet** | Required for build & AI API |

---

## File Structure

```
Nocta-system/
├── .env                    # API keys (you need to create/edit this)
├── START-WINDOWS.ps1       # Easy startup script
├── mvnw.cmd                # Maven wrapper (builds project)
├── pom.xml                 # Project dependencies
├── src/                    # Source code
└── target/                 # Compiled files (created after build)
    └── clinical-copilot-1.0.0.jar  # Runnable application
```

---

## Next Steps After Installation

1. **Try the demo patient:** Sarah Johnson (pre-loaded)
2. **Create a test consultation** with sample transcript
3. **Watch the 4 AI agents** work in real-time
4. **Review the generated SOAP note**
5. **Test the mobile interface**

---

## Support & Documentation

- **System Overview:** [SYSTEM_OVERVIEW.md](SYSTEM_OVERVIEW.md)
- **Architecture:** [docs/technical/ARCHITECTURE.md](docs/technical/ARCHITECTURE.md)
- **API Guide:** [docs/technical/API_GUIDE.md](docs/technical/API_GUIDE.md)
- **Demo Guide:** [docs/DEMO_GUIDE.md](docs/DEMO_GUIDE.md)

---

## Still Having Issues?

Common problems and solutions:

1. **"Cannot find Java"** → Java not in PATH, reinstall Java
2. **"Port in use"** → Another app on 8080, change port or stop other app
3. **"Build fails"** → Check internet connection, delete `target/` folder and rebuild
4. **"API error"** → Check API key in `.env` file
5. **"Out of memory"** → Increase Java heap: `java -Xmx2g -jar target/clinical-copilot-1.0.0.jar`

---

**Once Java is installed, running the app is just ONE command:**

```powershell
.\START-WINDOWS.ps1
```

🎉 **Happy Coding!**
