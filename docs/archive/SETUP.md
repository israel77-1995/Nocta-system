# Clinical Copilot OS - Setup Guide

## Step-by-Step Setup

### 1. Install Prerequisites

**Java 17:**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# macOS
brew install openjdk@17

# Verify
java -version
```

**Maven:**
```bash
# Ubuntu/Debian
sudo apt install maven

# macOS
brew install maven

# Verify
mvn -version
```

### 2. Setup LLAMA Server

**Option A: Using llama.cpp (Recommended)**

```bash
# Clone llama.cpp
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp

# Build
make

# Download a model (example: Llama-2-7B-Chat GGUF)
# You can get models from HuggingFace
# Example: https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF

# Create models directory
mkdir -p models
# Place your downloaded .gguf model file in models/

# Start the server
./server -m models/llama-2-7b-chat.Q4_K_M.gguf \
  --host 0.0.0.0 \
  --port 5000 \
  --ctx-size 2048 \
  --threads 4
```

**Option B: Using Docker**

```bash
# Pull llama.cpp server image
docker pull ghcr.io/ggerganov/llama.cpp:server

# Run with your model
docker run -p 5000:5000 -v /path/to/models:/models \
  ghcr.io/ggerganov/llama.cpp:server \
  --server --host 0.0.0.0 --port 5000 \
  --model /models/llama-2-7b-chat.Q4_K_M.gguf
```

**Test LLAMA Server:**
```bash
curl http://localhost:5000/health
```

### 3. Build and Run Application

```bash
# Navigate to project directory
cd /home/wtc/Nocta-system

# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

**Alternative: Run JAR directly**
```bash
java -jar target/clinical-copilot-1.0.0-SNAPSHOT.jar
```

### 4. Verify Installation

**Check application health:**
```bash
curl http://localhost:8080/api/v1/health
```

**Check LLAMA integration:**
```bash
curl http://localhost:8080/api/v1/llama/health
```

**Access Web UI:**
Open browser: http://localhost:8080

**Access H2 Console (for debugging):**
Open browser: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:clinicaldb`
- Username: `sa`
- Password: (leave empty)

### 5. Test End-to-End Flow

**Using Web UI:**
1. Open http://localhost:8080
2. Use pre-filled patient ID: `550e8400-e29b-41d4-a716-446655440001`
3. Enter or use sample transcript
4. Click "Process Consultation"
5. Wait for processing (status will update)
6. View generated SOAP note and ICD-10 codes
7. Click "Approve & Sync to EHR"

**Using cURL:**

```bash
# 1. Upload consultation
CONSULTATION_ID=$(curl -X POST http://localhost:8080/api/v1/consultations/upload-audio \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "550e8400-e29b-41d4-a716-446655440001",
    "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
    "rawTranscript": "Patient reports severe headache for 3 days, throbbing pain, worse with light. BP 140/90."
  }' | jq -r '.consultationId')

echo "Consultation ID: $CONSULTATION_ID"

# 2. Check status (wait a few seconds for processing)
sleep 5
curl http://localhost:8080/api/v1/consultations/$CONSULTATION_ID/status | jq

# 3. Get full results
curl http://localhost:8080/api/v1/consultations/$CONSULTATION_ID | jq

# 4. Approve consultation
curl -X POST http://localhost:8080/api/v1/consultations/$CONSULTATION_ID/approve \
  -H "Content-Type: application/json" \
  -d '{
    "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
    "approve": true
  }' | jq
```

### 6. Docker Deployment (Optional)

```bash
cd docker

# Place your LLAMA model in docker/models/llama-model.gguf

# Start all services
docker-compose up --build

# Access application at http://localhost:8080
```

## Troubleshooting

### LLAMA Server Issues

**Problem: Connection refused to LLAMA server**
```bash
# Check if server is running
curl http://localhost:5000/health

# Check logs
# If using llama.cpp directly, check terminal output
# If using Docker:
docker logs <container-id>
```

**Solution:**
- Ensure LLAMA server is running on port 5000
- Update `application.yml` if using different port
- Check firewall settings

### Application Issues

**Problem: Port 8080 already in use**
```bash
# Change port in application.yml
server:
  port: 8081
```

**Problem: Database errors**
```bash
# Clear H2 database (in-memory, just restart app)
# Or check Flyway migrations in logs
```

**Problem: Out of memory**
```bash
# Increase JVM heap size
java -Xmx2g -jar target/clinical-copilot-1.0.0-SNAPSHOT.jar
```

### Model Performance

**Slow inference:**
- Use smaller quantized models (Q4_K_M or Q5_K_M)
- Reduce context size: `--ctx-size 1024`
- Increase threads: `--threads 8`
- Use GPU acceleration if available

**Poor quality outputs:**
- Use larger models (7B or 13B)
- Adjust temperature in prompt options
- Add more few-shot examples to prompts

## Model Recommendations

**For Development/Testing:**
- Llama-2-7B-Chat Q4_K_M (~4GB RAM)
- TinyLlama-1.1B Q4_K_M (~1GB RAM)

**For Production:**
- Llama-2-13B-Chat Q5_K_M (~9GB RAM)
- Llama-2-70B-Chat Q4_K_M (requires GPU)

**Download from:**
- HuggingFace: https://huggingface.co/TheBloke
- Look for GGUF format models

## Next Steps

1. Customize prompts in `src/main/resources/prompts/`
2. Add more sample patients via SQL migrations
3. Implement JWT authentication in `SecurityConfig`
4. Configure PostgreSQL for production
5. Add monitoring and logging
6. Implement WebSocket for real-time updates

## Support

Check logs:
```bash
tail -f logs/application.log
```

Enable debug logging:
```yaml
logging:
  level:
    za.co.ccos: DEBUG
```
