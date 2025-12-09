#!/bin/bash

# Start LLAMA server for Clinical Copilot OS
# Requires llama.cpp to be installed

LLAMA_CPP_DIR="${LLAMA_CPP_DIR:-$HOME/llama.cpp}"
MODEL_PATH="${MODEL_PATH:-$LLAMA_CPP_DIR/models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf}"
PORT=5000

if [ ! -d "$LLAMA_CPP_DIR" ]; then
    echo "❌ llama.cpp not found at $LLAMA_CPP_DIR"
    echo "Please install llama.cpp:"
    echo "  git clone https://github.com/ggerganov/llama.cpp"
    echo "  cd llama.cpp && cmake -B build -DLLAMA_CURL=OFF && cmake --build build --config Release"
    exit 1
fi

if [ ! -f "$MODEL_PATH" ]; then
    echo "❌ Model not found at $MODEL_PATH"
    echo "Download a model from HuggingFace:"
    echo "  https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF"
    exit 1
fi

echo "🚀 Starting LLAMA server..."
echo "   Model: $MODEL_PATH"
echo "   Port: $PORT"

cd "$LLAMA_CPP_DIR"
./build/bin/llama-server -m "$MODEL_PATH" \
  --host 0.0.0.0 \
  --port $PORT \
  --ctx-size 2048 \
  --threads 4 \
  --n-predict 1024
