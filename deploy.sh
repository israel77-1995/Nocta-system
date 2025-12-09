#!/bin/bash

echo "🚀 Deploying Clinical Copilot to Railway..."

# Check if Railway CLI is installed
if ! command -v railway &> /dev/null; then
    echo "📦 Installing Railway CLI..."
    npm install -g @railway/cli
fi

# Login to Railway
echo "🔐 Login to Railway (browser will open)..."
railway login

# Initialize project
echo "🏗️ Initializing Railway project..."
railway init

# Set environment variables
echo "🔑 Setting environment variables..."
echo "Enter your GROQ API key (starts with gsk_):"
read -r GROQ_KEY
railway variables set GROQ_API_KEY="$GROQ_KEY"

echo "Enter your OpenRouter API key (optional, press Enter to skip):"
read -r OPENROUTER_KEY
if [ -n "$OPENROUTER_KEY" ]; then
    railway variables set OPENROUTER_API_KEY="$OPENROUTER_KEY"
fi

# Deploy
echo "🚀 Deploying to Railway..."
railway up

echo "✅ Deployment complete!"
echo "🌐 Your app will be available at the URL shown above"
echo "📱 Users can install it as a PWA from their browser"