# Getting Started with Clinical Copilot OS

A comprehensive guide to setting up and running the Clinical Copilot OS system.

## Quick Start (5 minutes)

### 1. Run Setup Script
```bash
cd /home/wtc/Nocta-system
bash setup.sh
```

This will:
- ✅ Check Java and Node.js installation
- ✅ Build the backend JAR
- ✅ Install frontend dependencies
- ✅ Display startup instructions

### 2. Start the Backend (Terminal 1)
```bash
java -jar target/clinical-copilot-1.0.0.jar
```

Wait for: `Started ClinicalCopilotApplication in X seconds`

### 3. Start the Frontend (Terminal 2)
```bash
cd mobile-app
npm start
```

Wait for: `Logs for your project will appear below`

### 4. Set Up LLAMA Server (Terminal 3)

**Option A: Docker (Easiest)**
```bash
# Download model (first time only, ~4GB)
mkdir -p ~/llama-models
wget -P ~/llama-models https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF/resolve/main/llama-2-7b-chat.Q4_K_M.gguf

# Run LLAMA server
docker pull ghcr.io/ggerganov/llama.cpp:server
docker run -p 5000:8000 \
  -v ~/llama-models:/models \
  ghcr.io/ggerganov/llama.cpp:server \
  --server --host 0.0.0.0 --port 8000 \
  --model /models/llama-2-7b-chat.Q4_K_M.gguf
```

**Option B: Docker Compose**
```bash
docker-compose up llama-server
```

See **LLAMA_SETUP.md** for detailed options and troubleshooting.

### 5. Test the System
```bash
# Check backend
curl http://localhost:8080/api/v1/health

# Check LLAMA
curl http://localhost:5000/health

# Open frontend
open http://localhost:8080/mobile.html
```

## System Architecture

```
Clinical Copilot OS
│
├─ Backend (Java/Spring Boot)
│  ├─ REST API on :8080
│  ├─ Business logic services
│  ├─ H2 in-memory database
│  └─ LLAMA integration
│
├─ Frontend (React/Expo)
│  ├─ Web UI at :8080/mobile.html
│  ├─ Mobile app via Expo at :8081
│  └─ Real-time status updates
│
└─ LLAMA Server (AI Engine)
   ├─ AI model inference
   ├─ Clinical analysis
   └─ Runs at :5000
```

## Detailed Setup

### Prerequisites

```bash
# Check Java
java -version  # Need Java 17+

# Check Node.js
node -v        # Need Node 14+
npm -v

# Optional: Docker
docker --version
```

### Installation

**1. Java 17**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# macOS
brew install openjdk@17

# Verify
java -version
```

**2. Node.js**
```bash
# Ubuntu/Debian
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install nodejs

# macOS
brew install node

# Verify
node -v
npm -v
```

**3. Docker (optional, recommended)**
```bash
# Ubuntu/Debian
sudo apt install docker.io docker-compose

# macOS
brew install docker

# Verify
docker --version
docker-compose --version
```

### Build Backend

```bash
cd /home/wtc/Nocta-system

# Build JAR (one time)
./mvnw clean package -DskipTests

# Or rebuild if code changed
./mvnw clean package -DskipTests

# JAR location
ls -lh target/clinical-copilot-1.0.0.jar
```

### Install Frontend

```bash
cd mobile-app

# Install dependencies
npm install

# Verify
npm list | head -20
```

## Running the System

### Option 1: Manual (3 Terminals)

**Terminal 1: Backend**
```bash
cd /home/wtc/Nocta-system
java -jar target/clinical-copilot-1.0.0.jar
```

**Terminal 2: Frontend**
```bash
cd /home/wtc/Nocta-system/mobile-app
npm start
```

**Terminal 3: LLAMA Server**
```bash
docker run -p 5000:8000 \
  -v ~/llama-models:/models \
  ghcr.io/ggerganov/llama.cpp:server \
  --server --host 0.0.0.0 --port 8000 \
  --model /models/llama-2-7b-chat.Q4_K_M.gguf
```

### Option 2: Docker Compose (Automatic)

```bash
cd /home/wtc/Nocta-system

# Start all services
docker-compose up

# View status
docker-compose ps

# View logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f llama-server

# Stop
docker-compose down
```

### Option 3: Using run.sh Script

```bash
cd /home/wtc/Nocta-system
bash run.sh
```

## Accessing the System

### Frontend (UI)
- **Web**: http://localhost:8080/mobile.html
- **Mobile (Expo)**: http://localhost:8081
- Scan QR code in terminal to open on phone

### Backend API
- **API Base**: http://localhost:8080/api/v1
- **Documentation**: See INTEGRATION_GUIDE.md
- **H2 Console**: http://localhost:8080/h2-console
  - Username: `sa`
  - Password: (leave empty)

### Development Tools
- **Backend Logs**: `tail -f backend.log`
- **Frontend Logs**: `cd mobile-app && npm start` (visible in terminal)
- **API Testing**: `curl http://localhost:8080/api/v1/consultations`

## Workflow: Creating Your First Consultation

1. **Open Frontend**
   - Go to http://localhost:8080/mobile.html

2. **Login**
   - Enter any Clinician ID (or use: `550e8400-e29b-41d4-a716-446655440099`)
   - Click "Start Session"

3. **Select Patient**
   - Click on "John Doe" or "Jane Smith"
   - These are sample patients pre-loaded in the database

4. **Enter Consultation Notes**
   - Type or dictate consultation notes
   - Example: "Patient reports severe headache for 3 days, throbbing pain, worse with light, no visual changes"

5. **Generate SOAP Note**
   - Click "Generate SOAP Note"
   - System processes through:
     - Perception Service (extracts symptoms)
     - Documentation Service (generates SOAP)
     - Coordination Service (suggests actions)
     - Compliance Service (validates)

6. **Review Results**
   - View SOAP note components
   - Check ICD-10 codes
   - Review suggested actions

7. **Approve & Sign**
   - Click "Approve & Sign"
   - Note syncs to simulated EHR system

## Troubleshooting

### Backend Won't Start
```bash
# Check if port 8080 is in use
lsof -i :8080

# Kill process if needed
kill -9 <PID>

# Try again
java -jar target/clinical-copilot-1.0.0.jar
```

### Frontend Can't Connect to Backend
```bash
# Check backend is running
curl http://localhost:8080/api/v1/health

# Check browser console (F12) for errors
# Check API_BASE_URL in mobile.js
```

### LLAMA Server Not Responding
```bash
# Check if running
curl http://localhost:5000/health

# Check Docker logs
docker logs <container_id>

# Restart
docker restart <container_id>

# Or restart with docker-compose
docker-compose restart llama-server
```

### Processing Takes Too Long
- LLAMA inference can be slow (30-60 seconds)
- Use faster model: TinyLlama instead of Llama 2
- Enable GPU if available
- See LLAMA_SETUP.md for performance tuning

### Database Already Locked
```bash
# H2 might have stale lock files
rm -rf /tmp/h2*

# Restart backend
```

## Common Operations

### View Logs

**Backend**
```bash
tail -f backend.log
tail -50 backend.log  # Last 50 lines
grep ERROR backend.log
```

**Frontend**
```bash
# Visible in Terminal 2 output
# Or check frontend.log
tail -f mobile-app/frontend.log
```

**LLAMA**
```bash
docker logs -f <llama-container-id>
```

### Reset Database

```bash
# Stop backend
# The H2 database is in-memory, so it resets automatically on restart

# Or reset sample data:
# Modify V2__sample_data.sql and rebuild
```

### Build Again After Code Changes

```bash
# Backend
cd /home/wtc/Nocta-system
./mvnw clean package -DskipTests
# Restart: java -jar target/clinical-copilot-1.0.0.jar

# Frontend  
cd mobile-app
npm start  # Auto-reloads on changes
```

### Check Consultation Status via API

```bash
# List all
curl http://localhost:8080/api/v1/consultations

# Get specific
curl http://localhost:8080/api/v1/consultations/{id}

# Check status
curl http://localhost:8080/api/v1/consultations/{id}/status
```

## Production Deployment

For production use, see:
- **DEPLOYMENT.md** - Cloud deployment (Azure, AWS, GCP)
- **DOCKER.md** - Docker containerization
- **SECURITY.md** - Security hardening

## Getting Help

1. **Documentation**
   - README.md - System overview
   - LLAMA_SETUP.md - LLM setup
   - INTEGRATION_GUIDE.md - API details
   - QUICK_START.md - Quick reference

2. **Checking Logs**
   - Backend logs: `backend.log`
   - Frontend logs: Terminal output
   - LLAMA logs: Docker logs

3. **API Testing**
   - Use `curl` commands from INTEGRATION_GUIDE.md
   - Open http://localhost:8080/h2-console for database
   - Check browser DevTools (F12) for frontend errors

4. **GitHub Issues**
   - Report bugs at https://github.com/israel77-1995/Nocta-system/issues

## What's Next?

1. ✅ System is running
2. Create some consultations and test the workflow
3. Set up LLAMA server for AI processing
4. Configure production database (PostgreSQL)
5. Deploy to cloud (Azure, AWS, GCP)
6. Add authentication and security
7. Set up monitoring and logging

## Performance Tips

- **For fast LLAMA**: Enable GPU in docker-compose
- **For slow machines**: Use TinyLlama model
- **For production**: Use PostgreSQL instead of H2
- **For scale**: Deploy with Kubernetes

See detailed setup guides in the documentation!
