# LLAMA Server Setup Guide

## Quick Start

### 1. Install llama.cpp

```bash
cd ~
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp
make
```

### 2. Download Model

Download TinyLlama (1.1B - fast, good for testing):
```bash
cd ~/llama.cpp
mkdir -p models
cd models
wget https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf
```

Or for better quality, download Llama-2-7B:
```bash
wget https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF/resolve/main/llama-2-7b-chat.Q4_K_M.gguf
```

### 3. Start LLAMA Server

```bash
cd /home/wtc/Nocta-system
./start-llama.sh
```

Or manually:
```bash
cd ~/llama.cpp
./server -m models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf \
  --host 0.0.0.0 \
  --port 5000 \
  --ctx-size 2048 \
  --threads 4
```

### 4. Verify Server

```bash
curl http://localhost:5000/health
```

Should return: `{"status":"ok"}`

### 5. Start Clinical Copilot

```bash
cd /home/wtc/Nocta-system
java -jar target/clinical-copilot-1.0.0.jar
```

## Configuration

Edit `application.yml` to switch between mock and real LLAMA:

```yaml
llama:
  server:
    url: http://localhost:5000
  mock:
    enabled: false  # false = real LLAMA, true = mock
```

## Troubleshooting

**LLAMA server not responding:**
- Check if running: `lsof -i :5000`
- Check logs in llama.cpp terminal
- Verify model file exists

**Out of memory:**
- Use smaller model (TinyLlama)
- Reduce `--ctx-size` to 1024
- Reduce `--threads`

**Slow responses:**
- Use quantized models (Q4_K_M)
- Increase `--threads` to match CPU cores
- Use GPU acceleration if available

## Model Recommendations

- **Development/Testing**: TinyLlama 1.1B (fast, 700MB)
- **Production**: Llama-2-7B or Llama-2-13B (better quality)
- **High Performance**: Mistral-7B or Mixtral-8x7B
