# Setup Guide

## Prerequisites

- **Java 17+** - [Download](https://adoptium.net/)
- **Maven 3.6+** - Included via `mvnw` wrapper
- **LLAMA API Key** - Free from [Groq](https://console.groq.com)

## Quick Setup (2 Steps)

### Step 1: Get Free LLAMA API Key

1. Visit https://console.groq.com
2. Sign up (free, no credit card required)
3. Create API key
4. Copy key (starts with `gsk_`)

### Step 2: Configure Environment

Create `.env` file in project root:

```bash
# Required: LLAMA 3.3 70B for text processing
GROQ_API_KEY=gsk_your_key_here

# Optional: LLAMA 3.2 11B Vision for image analysis
OPENROUTER_API_KEY=sk-or-v1-your_key_here
```

**Get OpenRouter Key (Optional):**
- Visit https://openrouter.ai/keys
- Sign in with Google/GitHub
- Create key (free $1 credit included)

## Run Application

```bash
./start-app.sh
```

This script will:
1. Check prerequisites
2. Build the application
3. Start the backend server

Access at: **http://localhost:8080**

## Manual Build & Run

If you prefer manual control:

```bash
# Build
./mvnw clean package -DskipTests

# Run
java -jar target/clinical-copilot-1.0.0.jar
```

## Verify Installation

```bash
# Check backend health
curl http://localhost:8080/api/v1/health

# Check LLAMA integration
curl http://localhost:8080/api/v1/llama/health
```

## Troubleshooting

**Error: "GROQ_API_KEY not set"**
- Ensure `.env` file exists in project root
- Verify key starts with `gsk_`

**Error: "Port 8080 already in use"**
```bash
# Kill existing process
lsof -ti:8080 | xargs kill -9
```

**Error: "Java version mismatch"**
```bash
# Check Java version
java -version  # Should be 17+
```

## Next Steps

- See [API_GUIDE.md](API_GUIDE.md) for REST endpoints
- See [ARCHITECTURE.md](ARCHITECTURE.md) for system design
- See [DEMO_GUIDE.md](DEMO_GUIDE.md) for presentation tips
