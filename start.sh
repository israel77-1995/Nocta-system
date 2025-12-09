#!/bin/bash

# Load environment variables
if [ -f .env ]; then
    set -a
    source .env
    set +a
fi

# Start the application
java -jar target/clinical-copilot-1.0.0.jar
