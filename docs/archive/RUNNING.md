# How to Run Clinical Copilot OS

This document provides clear, step-by-step instructions to run the Clinical Copilot OS system.

## System Requirements

- **Java 17 or higher** - Download from [java.com](https://www.java.com)
- **At least 4GB RAM** for the application
- **Additional RAM for LLAMA server** (2GB for TinyLlama, 8GB for Llama-2-7B)
- **Linux, macOS, or Windows** with bash/terminal support

## Quick Reference (3 Steps)

```bash
# Terminal 1: Start LLAMA Server
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp && make
wget https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf -O models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf
./server -m models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf --host 0.0.0.0 --port 5000 --ctx-size 2048

# Terminal 2: Build & Run Application
cd /home/wtc/Nocta-system
./mvnw clean package -DskipTests
java -jar target/clinical-copilot-1.0.0.jar

# Terminal 3: Access the system
curl http://localhost:8080  # API test
# Open browser: http://localhost:8080
```

## Detailed Steps

### Step 1: Prepare LLAMA Server

The Clinical Copilot OS requires a LLAMA language model server to generate clinical notes.

#### 1.1: Clone and Build llama.cpp

```bash
# Clone the repository
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp

# Build from source (takes 2-5 minutes)
make

# Verify build
ls -la ./server
```

#### 1.2: Download a Model

Choose one option based on your needs:

**For Testing (Recommended)**
```bash
mkdir -p models
wget https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf \
  -O models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf
```

**For Production**
```bash
wget https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF/resolve/main/llama-2-7b-chat.Q4_K_M.gguf \
  -O models/llama-2-7b-chat.Q4_K_M.gguf
```

#### 1.3: Start LLAMA Server

**With TinyLlama:**
```bash
./server -m models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf \
  --host 0.0.0.0 \
  --port 5000 \
  --ctx-size 2048 \
  --threads 4
```

**With Llama-2-7B:**
```bash
./server -m models/llama-2-7b-chat.Q4_K_M.gguf \
  --host 0.0.0.0 \
  --port 5000 \
  --ctx-size 2048 \
  --threads 8
```

#### 1.4: Verify LLAMA is Running

```bash
# In another terminal
curl http://localhost:5000/health

# Should return something like:
# {"status":"ok"}
# Or other success indicator
```

**Do not proceed to Step 2 until LLAMA is confirmed running!**

### Step 2: Build the Application

In a new terminal:

```bash
# Navigate to project directory
cd /home/wtc/Nocta-system

# Build the JAR file (takes 30-60 seconds)
./mvnw clean package -DskipTests

# You should see:
# [INFO] BUILD SUCCESS
# [INFO] Building jar: target/clinical-copilot-1.0.0.jar
```

### Step 3: Run the Application

```bash
# Start the application
java -jar target/clinical-copilot-1.0.0.jar

# You should see (within 20-30 seconds):
# Started ClinicalCopilotApplication in X seconds
```

## Accessing the System

Once running, access the system via:

### Web Interface
- **Main UI**: http://localhost:8080
- **Mobile UI**: http://localhost:8080/mobile.html
- **Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:clinicaldb`
  - Username: `sa`
  - Password: (blank)

### REST API

**Test the API is running:**
```bash
curl http://localhost:8080/health
```

**Create a Consultation:**
```bash
curl -X POST http://localhost:8080/api/v1/consultations/upload-audio \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "123e4567-e89b-12d3-a456-426614174000",
    "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
    "audioUrl": "recording.wav",
    "rawTranscript": "Patient reports fever, cough, and fatigue for 2 days. Temperature 38.5C."
  }'
```

From the response, copy the `id` value and use it in:

**Check Status:**
```bash
curl http://localhost:8080/api/v1/consultations/{id}/status
```

**View Full Consultation:**
```bash
curl http://localhost:8080/api/v1/consultations/{id}
```

**Approve Consultation:**
```bash
curl -X POST http://localhost:8080/api/v1/consultations/{id}/approve \
  -H "Content-Type: application/json" \
  -d '{"approve": true, "clinicianId": "550e8400-e29b-41d4-a716-446655440099"}'
```

## Troubleshooting

### LLAMA Server Not Running

**Error Message:** "LLAMA server not detected at localhost:5000"

**Solution:**
```bash
# 1. Check if LLAMA is running
curl http://localhost:5000/health

# 2. If not, ensure llama.cpp is built
cd /path/to/llama.cpp
make clean && make

# 3. Start the server again
./server -m models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf --port 5000 ...
```

### Port Already in Use

**Error Message:** "Address already in use" for port 8080 or 5000

**Solution:**
```bash
# Find and kill the process
lsof -ti:8080 | xargs kill -9
lsof -ti:5000 | xargs kill -9

# Or change ports in application.yml and rebuild
```

### Java Version Issue

**Error Message:** "java.lang.UnsupportedClassVersionError"

**Solution:**
```bash
# Check current Java version
java -version

# Must be Java 17 or higher
# Download from https://www.java.com or use your package manager
```

### Build Fails

**Error Message:** "BUILD FAILURE"

**Solution:**
```bash
# Clean and rebuild
./mvnw clean
./mvnw package -DskipTests -e

# Check for enough disk space
df -h

# Check for Java installation
javac -version
```

### Application Crashes

**Check the logs:**
```bash
# View application output
tail -f app.log

# Or run directly to see errors
java -jar target/clinical-copilot-1.0.0.jar 2>&1 | grep -i error
```

## Alternative: Using Docker

If you have Docker installed:

```bash
cd docker
docker-compose up --build
```

This runs:
- Clinical Copilot on http://localhost:8080
- LLAMA Server on http://localhost:5000
- H2 Database (in-memory)

## Performance Tips

**For Slow LLAMA Responses:**
- Use TinyLlama (1.1B) instead of Llama-2-7B (7B)
- Reduce context size: `--ctx-size 1024`
- Increase threads: `--threads 8`
- Disable GPU if not needed: `--ngl 0`

**For Better Accuracy:**
- Use Llama-2-7B model
- Increase context size: `--ctx-size 4096`
- Use more threads: `--threads 16`

## Expected Workflow

1. Start LLAMA Server → waits for requests
2. Start Clinical Copilot → connects to database, waits for API calls
3. Submit Consultation → API queues for processing
4. System Processes:
   - Perception (extracts clinical facts)
   - Documentation (generates SOAP note)
   - Coordination (creates action items)
   - Compliance (validates safety)
5. Clinician Reviews → approves note
6. System Syncs → prepares for EHR export

Total processing time: 10-30 seconds depending on model size

## Stopping the System

**Stop Application:**
```bash
# Ctrl+C in the application terminal
# Or kill the process
lsof -ti:8080 | xargs kill -9
```

**Stop LLAMA Server:**
```bash
# Ctrl+C in the LLAMA terminal
# Or kill the process
lsof -ti:5000 | xargs kill -9
```

## Next Steps

- Visit the Web UI to test manually: http://localhost:8080
- Review [README.md](README.md) for full documentation
- Check [ARCHITECTURE.md](ARCHITECTURE.md) for system design
- Import [postman_collection.json](postman_collection.json) for API testing in Postman

## Support

For issues or questions:
1. Check the [README.md](README.md)
2. Review [SETUP.md](SETUP.md) for advanced configuration
3. Check application logs: `tail -f app.log`
4. Verify LLAMA server health: `curl http://localhost:5000/health`
