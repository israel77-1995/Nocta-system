#!/usr/bin/env python3
"""
Simple HTTP wrapper for llama.cpp CLI
Provides REST API compatible with HttpLlamaAdapter
"""

from flask import Flask, request, jsonify
import subprocess
import json
import os

app = Flask(__name__)

LLAMA_BINARY = os.getenv('LLAMA_BINARY', './llama.cpp/main')
MODEL_PATH = os.getenv('MODEL_PATH', './models/llama-model.gguf')

@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "UP"})

@app.route('/generate', methods=['POST'])
def generate():
    try:
        data = request.json
        prompt = data.get('prompt', '')
        temperature = data.get('temperature', 0.2)
        max_tokens = data.get('max_tokens', 1024)
        
        cmd = [
            LLAMA_BINARY,
            '-m', MODEL_PATH,
            '-p', prompt,
            '-n', str(max_tokens),
            '--temp', str(temperature),
            '--ctx-size', '2048'
        ]
        
        result = subprocess.run(cmd, capture_output=True, text=True, timeout=120)
        
        # Extract generated text (llama.cpp outputs prompt + generation)
        output = result.stdout.strip()
        
        return jsonify({
            "content": output,
            "response": output,
            "tokens_evaluated": len(output.split())
        })
        
    except subprocess.TimeoutExpired:
        return jsonify({"error": "Generation timeout"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
