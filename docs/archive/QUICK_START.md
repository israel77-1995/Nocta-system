# Clinical Copilot OS - Quick Start Guide

## 🚀 Get Running in 10 Minutes

This is the fastest way to get the Clinical Copilot OS system up and running locally.

## Prerequisites

You only need **Java 17+**. Maven is included in the wrapper.

```bash
# Check Java version
java -version   # Must be 17 or higher
```

## Step 1: Start LLAMA Server (5 minutes)

The system requires a LLAMA language model server running on `localhost:5000`.

### Option A: Quick Start (TinyLlama - Fastest)

```bash
# Clone llama.cpp
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp
make

# Download tiny model
mkdir -p models
wget https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf -O models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf

# Start server
./server -m models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf \
  --host 0.0.0.0 \
  --port 5000 \
  --ctx-size 2048

# Verify in another terminal
curl http://localhost:5000/health
```

### Option B: Production Model (Llama-2-7B)

```bash
wget https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF/resolve/main/llama-2-7b-chat.Q4_K_M.gguf -O models/llama-2-7b-chat.Q4_K_M.gguf

./server -m models/llama-2-7b-chat.Q4_K_M.gguf \
  --host 0.0.0.0 \
  --port 5000 \
  --ctx-size 2048
```

**⚠️ Do not proceed until LLAMA server is running!**

## Step 2: Build the Application (2 minutes)

In a new terminal:

```bash
cd /home/wtc/Nocta-system
./mvnw clean package -DskipTests
```

## Step 3: Run the Application (1 minute)

```bash
java -jar target/clinical-copilot-1.0.0.jar
```

You should see: `Started ClinicalCopilotApplication`

## 🎉 Success!

The system is now running on **http://localhost:8080**

## Try It Out

### Web UI
Open in browser: **http://localhost:8080**

### API Test

```bash
curl -X POST http://localhost:8080/api/v1/consultations/upload-audio \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "123e4567-e89b-12d3-a456-426614174000",
    "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
    "rawTranscript": "Patient reports fever and cough for 2 days"
  }'
```

## 📖 Full Documentation

- [README.md](README.md) - Complete documentation
- [SETUP.md](SETUP.md) - Advanced configuration
- [ARCHITECTURE.md](ARCHITECTURE.md) - System design

## 🔧 Useful URLs

| URL | Purpose |
|-----|---------|
| http://localhost:8080 | Web UI |
| http://localhost:8080/mobile.html | Mobile interface |
| http://localhost:8080/h2-console | Database browser |
| http://localhost:5000/health | LLAMA server status |

## �� Common Issues

**LLAMA Server Error**: Make sure it's running on localhost:5000
```bash
curl http://localhost:5000/health
```

**Port 8080 in use**: Kill the process
```bash
lsof -ti:8080 | xargs kill -9
```

**Java version**: Must be 17 or higher
```bash
java -version
```

## ✅ You're All Set!

The Clinical Copilot OS is now running with full AI-powered clinical documentation.
