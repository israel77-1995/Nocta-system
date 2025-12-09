#!/bin/bash

echo "==================================="
echo "Clinical Copilot OS - Quick Start"
echo "==================================="
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 17+"
    exit 1
fi
echo "✓ Java found: $(java -version 2>&1 | head -n 1)"

# Check Maven
if [ ! -f "./mvnw" ]; then
    echo "❌ Maven wrapper not found"
    exit 1
fi
echo "✓ Maven wrapper found"

# Check LLAMA server
echo ""
echo "Checking LLAMA server at http://localhost:5000..."
if curl -s http://localhost:5000/health > /dev/null 2>&1; then
    echo "✓ LLAMA server is running"
else
    echo "⚠️  LLAMA server not detected at localhost:5000"
    echo "   Please start LLAMA server first (see SETUP.md)"
    echo ""
    read -p "Continue anyway? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo ""
echo "Building application..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

echo ""
echo "✓ Build successful"
echo ""
echo "Starting Clinical Copilot OS..."
echo "Access UI at: http://localhost:8080"
echo "API docs: See README.md"
echo ""

./mvnw spring-boot:run
