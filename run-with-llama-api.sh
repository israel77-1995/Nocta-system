#!/bin/bash

# Load .env file if exists
if [ -f .env ]; then
    export $(cat .env | xargs)
fi

if [ -z "$GROQ_API_KEY" ]; then
    echo "❌ GROQ_API_KEY not set"
    echo ""
    echo "Get free API key from: https://console.groq.com"
    echo ""
    echo "Then add to .env file:"
    echo "  GROQ_API_KEY=your-key-here"
    exit 1
fi

echo "🚀 Starting Clinical Copilot with LLAMA 3.3 70B API..."
java -jar target/clinical-copilot-1.0.0.jar
