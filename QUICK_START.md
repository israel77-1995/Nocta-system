# Clinical Copilot OS - Quick Start Guide

## 🚀 Get Running in 5 Minutes

### Prerequisites Check
```bash
java -version    # Need Java 17+
mvn -version     # Need Maven 3.6+
```

### Step 1: Get LLAMA Model (One-time)

Download a GGUF model from HuggingFace:
```bash
# Example: Llama-2-7B-Chat (4GB)
wget https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF/resolve/main/llama-2-7b-chat.Q4_K_M.gguf

# Or use a smaller model for testing
wget https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf
```

### Step 2: Start LLAMA Server

**Option A: Using llama.cpp**
```bash
# Clone and build llama.cpp
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp
make

# Start server
./server -m /path/to/your-model.gguf \
  --host 0.0.0.0 \
  --port 5000 \
  --ctx-size 2048
```

**Option B: Using Docker**
```bash
docker run -p 5000:5000 -v /path/to/models:/models \
  ghcr.io/ggerganov/llama.cpp:server \
  --server --host 0.0.0.0 --port 5000 \
  --model /models/your-model.gguf
```

**Verify LLAMA is running:**
```bash
curl http://localhost:5000/health
# Should return: {"status":"ok"} or similar
```

### Step 3: Start Clinical Copilot

```bash
cd /home/wtc/Nocta-system
./run.sh
```

Or manually:
```bash
./mvnw spring-boot:run
```

### Step 4: Test It!

**Open Web UI:**
```
http://localhost:8080
```

**Or use cURL:**
```bash
# Upload a consultation
curl -X POST http://localhost:8080/api/v1/consultations/upload-audio \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "550e8400-e29b-41d4-a716-446655440001",
    "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
    "rawTranscript": "Patient reports severe throbbing headache for 3 days, worse with light and noise. Pain is on the right side. BP measured at 140/90."
  }'

# Save the consultationId from response, then:
curl http://localhost:8080/api/v1/consultations/{consultationId}
```

## 🎯 What You Should See

1. **Upload**: Returns consultation ID with status `QUEUED`
2. **Processing**: Status changes to `PROCESSING` (check via `/status` endpoint)
3. **Ready**: After ~10-30 seconds, status becomes `READY`
4. **View Note**: Full SOAP note with:
   - Subjective, Objective, Assessment, Plan
   - ICD-10 code suggestions
   - Suggested actions (labs, referrals, prescriptions)
5. **Approve**: Marks consultation as `APPROVED` and simulates EHR sync

## 🐛 Troubleshooting

### "Connection refused" to LLAMA
```bash
# Check if LLAMA server is running
curl http://localhost:5000/health

# Check application config
cat src/main/resources/application.yml | grep llama
```

### Port 8080 already in use
```bash
# Kill existing process
lsof -ti:8080 | xargs kill -9

# Or change port in application.yml
```

### LLAMA responses are slow
- Use smaller model (TinyLlama 1.1B)
- Reduce context size: `--ctx-size 1024`
- Increase threads: `--threads 8`

### Tests failing
```bash
# Run with verbose output
./mvnw test -X
```

## 📖 Next Steps

- Read [README.md](README.md) for full documentation
- Check [SETUP.md](SETUP.md) for detailed setup
- Review [ARCHITECTURE.md](ARCHITECTURE.md) for system design
- Import [postman_collection.json](postman_collection.json) for API testing

## 💡 Tips

- **Sample Patients**: Two patients pre-loaded with IDs in V2 migration
- **H2 Console**: http://localhost:8080/h2-console (JDBC: `jdbc:h2:mem:clinicaldb`, user: `sa`)
- **Logs**: Check console output for agent processing details
- **Prompts**: Customize in `src/main/resources/prompts/`

## 🎉 You're Ready!

The system is now processing clinical consultations using local LLAMA models. No cloud APIs, fully private and local.
