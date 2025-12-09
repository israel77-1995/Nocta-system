#!/bin/bash

# Clinical Copilot OS - Complete Setup & Run Script
# This script sets up and runs all components of the system

set -e  # Exit on error

echo "=========================================="
echo "Clinical Copilot OS - Setup & Run"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check Java
echo -e "${BLUE}[1/5] Checking Java...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${YELLOW}Java not found. Installing...${NC}"
    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        sudo apt update && sudo apt install -y openjdk-17-jdk
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        brew install openjdk@17
    fi
fi
echo -e "${GREEN}✓ Java found: $(java -version 2>&1 | head -n 1)${NC}"
echo ""

# Check Node.js
echo -e "${BLUE}[2/5] Checking Node.js...${NC}"
if ! command -v node &> /dev/null; then
    echo -e "${YELLOW}Node.js not found. Installing...${NC}"
    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
        sudo apt-get install -y nodejs
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        brew install node
    fi
fi
echo -e "${GREEN}✓ Node.js found: $(node -v)${NC}"
echo ""

# Check Maven wrapper
echo -e "${BLUE}[3/5] Building backend...${NC}"
if [ ! -f "./mvnw" ]; then
    echo -e "${YELLOW}Maven wrapper not found${NC}"
    exit 1
fi

if [ ! -f "./target/clinical-copilot-1.0.0.jar" ]; then
    echo -e "${YELLOW}Building JAR...${NC}"
    ./mvnw clean package -DskipTests -q
else
    echo -e "${GREEN}✓ JAR already built${NC}"
fi
echo -e "${GREEN}✓ Backend build complete${NC}"
echo ""

# Install frontend dependencies
echo -e "${BLUE}[4/5] Setting up frontend...${NC}"
cd mobile-app
if [ ! -d "node_modules" ]; then
    echo -e "${YELLOW}Installing npm dependencies...${NC}"
    npm install -q
fi
echo -e "${GREEN}✓ Frontend ready${NC}"
cd ..
echo ""

# Summary and next steps
echo -e "${BLUE}[5/5] System ready!${NC}"
echo ""
echo -e "${GREEN}======================================${NC}"
echo -e "${GREEN}Clinical Copilot OS is ready to run!${NC}"
echo -e "${GREEN}======================================${NC}"
echo ""
echo "To start the system:"
echo ""
echo "Option 1: Start all services manually"
echo "  Terminal 1 (Backend):"
echo -e "    ${YELLOW}java -jar target/clinical-copilot-1.0.0.jar${NC}"
echo ""
echo "  Terminal 2 (Frontend):"
echo -e "    ${YELLOW}cd mobile-app && npm start${NC}"
echo ""
echo "  Terminal 3 (LLAMA Server - see LLAMA_SETUP.md):"
echo -e "    ${YELLOW}docker run -p 5000:8000 -v ~/llama-models:/models ghcr.io/ggerganov/llama.cpp:server --server --host 0.0.0.0 --port 8000 --model /models/llama-2-7b-chat.Q4_K_M.gguf${NC}"
echo ""
echo "Option 2: Use docker-compose (if Docker installed)"
echo -e "  ${YELLOW}docker-compose up${NC}"
echo ""
echo "Access the system:"
echo -e "  ${YELLOW}Frontend: http://localhost:8080/mobile.html${NC}"
echo -e "  ${YELLOW}API Docs: http://localhost:8080/swagger-ui.html${NC}"
echo -e "  ${YELLOW}H2 Console: http://localhost:8080/h2-console${NC}"
echo ""
echo "Documentation:"
echo -e "  ${YELLOW}• README.md - System overview${NC}"
echo -e "  ${YELLOW}• LLAMA_SETUP.md - LLM server setup${NC}"
echo -e "  ${YELLOW}• INTEGRATION_GUIDE.md - API documentation${NC}"
echo -e "  ${YELLOW}• QUICK_START.md - Quick start guide${NC}"
echo ""
echo "Need help?"
echo -e "  See RUNNING.md for detailed instructions"
echo ""
