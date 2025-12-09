# LLAMA Server Setup Guide

The Clinical Copilot OS system uses LLAMA (Large Language Model Meta AI) for AI-powered clinical analysis. Follow this guide to set up the LLAMA server.

## Prerequisites

- Docker installed (recommended for easiest setup)
- OR: llama.cpp compiled locally (4GB+ RAM, 10GB+ disk space)
- Model file: A GGUF format model (4-7GB)

## Option 1: Docker Setup (Recommended)

### Step 1: Pull LLAMA Docker Image

```bash
docker pull ghcr.io/ggerganov/llama.cpp:server
```

### Step 2: Download a GGUF Model

Create a models directory and download a model:

```bash
mkdir -p ~/llama-models
cd ~/llama-models

# Option A: Llama 2 7B Chat (4.3GB) - Recommended
wget https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF/resolve/main/llama-2-7b-chat.Q4_K_M.gguf

# Option B: TinyLlama 1.1B (smaller, faster, less accurate)
wget https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf

# Option C: Mistral 7B (faster, good quality)
wget https://huggingface.co/TheBloke/Mistral-7B-Instruct-v0.1-GGUF/resolve/main/mistral-7b-instruct-v0.1.Q4_K_M.gguf
```

### Step 3: Run LLAMA Server in Docker

```bash
docker run -p 5000:8000 \
  -v ~/llama-models:/models \
  ghcr.io/ggerganov/llama.cpp:server \
  --server --host 0.0.0.0 --port 8000 \
  --model /models/llama-2-7b-chat.Q4_K_M.gguf \
  --ctx-size 2048 \
  --threads 4
```

Or with docker-compose:

```bash
cd /path/to/Nocta-system
docker-compose up llama-server
```

### Step 4: Verify LLAMA is Running

```bash
# Check health endpoint
curl http://localhost:5000/health

# Expected response:
# {"status":"ok"}
```

## Option 2: Local Installation (llama.cpp)

### Step 1: Clone and Build llama.cpp

```bash
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp
make
```

### Step 2: Download a Model

```bash
mkdir -p models
cd models

# Download using wget or curl
wget https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF/resolve/main/llama-2-7b-chat.Q4_K_M.gguf
```

### Step 3: Start the Server

```bash
./server -m models/llama-2-7b-chat.Q4_K_M.gguf \
  --host 0.0.0.0 \
  --port 5000 \
  --ctx-size 2048 \
  --threads 4
```

## Model Recommendations

| Model | Size | Speed | Quality | Use Case |
|-------|------|-------|---------|----------|
| **TinyLlama 1.1B** | 400MB | Very Fast | Basic | Testing, low-resource |
| **Mistral 7B** | 4GB | Fast | Good | Production (recommended) |
| **Llama 2 7B Chat** | 4.3GB | Medium | Excellent | Production (best quality) |
| **Llama 2 13B Chat** | 8GB | Slow | Excellent | High-accuracy scenarios |

**Recommendation for Clinical Copilot**: Use **Mistral 7B** or **Llama 2 7B Chat** for best quality.

## Running the Complete Stack

### Docker Compose (All Services)

```bash
cd /home/wtc/Nocta-system

# Build and start everything
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f llama-server
```

### Manual Local Execution

**Terminal 1 - LLAMA Server:**
```bash
cd ~/llama.cpp
./server -m models/llama-2-7b-chat.Q4_K_M.gguf --host 0.0.0.0 --port 5000 --ctx-size 2048
```

**Terminal 2 - Backend:**
```bash
cd /home/wtc/Nocta-system
java -jar target/clinical-copilot-1.0.0.jar
```

**Terminal 3 - Frontend:**
```bash
cd /home/wtc/Nocta-system/mobile-app
npm start
```

## Troubleshooting

### LLAMA Server Not Responding

```bash
# Check if service is running
curl http://localhost:5000/health

# Check Docker logs
docker logs <container_id>

# Check if port 5000 is in use
lsof -i :5000
# Kill if needed:
kill -9 <PID>
```

### Out of Memory

- Reduce `--ctx-size` from 2048 to 1024
- Use a smaller model (TinyLlama instead of Llama 2)
- Reduce `--threads` value

### Slow Response Times

- Your machine may need more RAM or GPU
- Use a quantized model (Q4_K_M files are pre-quantized)
- Reduce context size
- Use a smaller model

### Docker Permission Issues

```bash
# Add user to docker group
sudo usermod -aG docker $USER
newgrp docker
```

## Performance Tuning

### For CPU-Only Systems

```bash
docker run -p 5000:8000 \
  -v ~/llama-models:/models \
  ghcr.io/ggerganov/llama.cpp:server \
  --server --host 0.0.0.0 --port 8000 \
  --model /models/llama-2-7b-chat.Q4_K_M.gguf \
  --ctx-size 1024 \
  --threads $(nproc)  # Use all CPU threads
```

### For GPU Systems (NVIDIA CUDA)

```bash
docker run --gpus all -p 5000:8000 \
  -v ~/llama-models:/models \
  ghcr.io/ggerganov/llama.cpp:full \
  --server --host 0.0.0.0 --port 8000 \
  --model /models/llama-2-7b-chat.Q4_K_M.gguf \
  --n-gpu-layers 35 \
  --ctx-size 2048
```

## Verifying the System Works End-to-End

```bash
# 1. Check LLAMA is running
curl http://localhost:5000/health

# 2. Check Backend is running
curl http://localhost:8080/api/v1/health || echo '{"status":"error"}'

# 3. Test API with a consultation
curl -X POST http://localhost:8080/api/v1/consultations/upload-audio \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "550e8400-e29b-41d4-a716-446655440001",
    "clinicianId": "660e8400-e29b-41d4-a716-446655440000",
    "rawTranscript": "Patient reports severe headache for 3 days with nausea"
  }'

# 4. Access the frontend
open http://localhost:8080/mobile.html  # or your-server-ip:8080/mobile.html
```

## Production Deployment

For production use:

1. **Use a reverse proxy** (nginx) for load balancing
2. **Enable HTTPS** with SSL certificates
3. **Set up monitoring** (Prometheus, Grafana)
4. **Use a production database** (PostgreSQL instead of H2)
5. **Configure LLAMA for GPU** if available
6. **Set appropriate resource limits** in docker-compose or Kubernetes
7. **Implement logging and audit trails**
8. **Use environment-based configuration**

See `DEPLOYMENT.md` for production setup details.

## References

- LLAMA.cpp: https://github.com/ggerganov/llama.cpp
- Hugging Face Models: https://huggingface.co/TheBloke
- Docker Hub: https://hub.docker.com
- Clinical Copilot Documentation: See `README.md`
