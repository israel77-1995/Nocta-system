#!/bin/bash

# Clinical Copilot OS - Complete Setup & Run Script
# This script handles everything: prerequisites check, build, and run

set -e

echo "=========================================="
echo "Clinical Copilot OS - Setup & Run"
echo "=========================================="
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Check Java
echo -e "${BLUE}[1/4] Checking Java...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java not found${NC}"
    echo "Install Java 17+: https://adoptium.net/"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo -e "${RED}❌ Java 17+ required (found: $JAVA_VERSION)${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Java $JAVA_VERSION found${NC}"
echo ""

# Check environment variables
echo -e "${BLUE}[2/4] Checking environment...${NC}"
if [ -f .env ]; then
    set -a
    source .env
    set +a
    echo -e "${GREEN}✓ .env file loaded${NC}"
else
    echo -e "${YELLOW}⚠ .env file not found${NC}"
    echo "Creating .env template..."
    cat > .env << 'EOF'
# Required: LLAMA 3.3 70B for text processing
GROQ_API_KEY=your_groq_key_here

# Optional: LLAMA 3.2 11B Vision for image analysis
OPENROUTER_API_KEY=your_openrouter_key_here
EOF
    echo -e "${YELLOW}Please edit .env and add your API keys${NC}"
    echo "Get Groq key: https://console.groq.com"
    echo "Get OpenRouter key: https://openrouter.ai/keys"
    exit 1
fi

if [ -z "$GROQ_API_KEY" ] || [ "$GROQ_API_KEY" = "your_groq_key_here" ]; then
    echo -e "${RED}❌ GROQ_API_KEY not configured${NC}"
    echo ""
    echo "Get free API key from: https://console.groq.com"
    echo "Then update .env file with your key"
    exit 1
fi

echo -e "${GREEN}✓ API keys configured${NC}"
echo ""

# Build application
echo -e "${BLUE}[3/4] Building application...${NC}"
if [ ! -f target/clinical-copilot-1.0.0.jar ]; then
    echo "Building JAR file..."
    ./mvnw clean package -DskipTests -q
    echo -e "${GREEN}✓ Build complete${NC}"
else
    echo -e "${GREEN}✓ JAR already built${NC}"
    echo "  (Run './mvnw clean package -DskipTests' to rebuild)"
fi
echo ""

# Start application
echo -e "${BLUE}[4/4] Starting application...${NC}"
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Clinical Copilot OS Starting...${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Access the application:"
echo -e "  ${YELLOW}Web UI: http://localhost:8080${NC}"
echo -e "  ${YELLOW}Mobile UI: http://localhost:8080/mobile.html${NC}"
echo -e "  ${YELLOW}API Health: http://localhost:8080/api/v1/health${NC}"
echo ""
echo "Press Ctrl+C to stop"
echo ""

# Run application
java -jar target/clinical-copilot-1.0.0.jar
