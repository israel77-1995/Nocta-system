# Clinical Copilot OS - MVP

A Java-based clinical documentation system using LLAMA for AI-powered SOAP note generation, ICD-10 coding, and care coordination.

## Architecture

**Layered Architecture:**
- **Presentation Layer**: REST API + Web UI + Mobile Web App
- **Application Layer**: Agent orchestration services (Perception, Documentation, Coordination, Compliance)
- **Domain Layer**: Core entities (Patient, Consultation, GeneratedNote)
- **Infrastructure Layer**: LLM adapter, persistence, external integrations

## Tech Stack

- Java 17+
- Spring Boot 3.1.5
- Spring Data JPA (H2 in-memory database)
- Spring Security
- Flyway migrations
- LLAMA via HTTP adapter
- Lombok
- Maven
- Docker & Docker Compose

## Prerequisites

- **Java 17+** - Install from [java.com](https://www.java.com) or use your package manager
- **Maven 3.6+** - Included with `mvnw` wrapper (no separate install needed)

## Quick Start - 3 Steps

### Step 1: Start LLAMA Server (Required)

The system needs a LLAMA language model server running on `localhost:5000`.

**Option A: Using llama.cpp (Recommended)**
```bash
# Clone llama.cpp
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp
make

# Download a GGUF model (e.g., TinyLlama for testing, Llama-2-7B for production)
# https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF
mkdir -p models
# Place your .gguf file in models/

# Start the server
./server -m models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf \
  --host 0.0.0.0 \
  --port 5000 \
  --ctx-size 2048 \
  --threads 4

# Verify it's running
curl http://localhost:5000/health
```

**Option B: Using Docker**
```bash
docker run -p 5000:5000 -v $(pwd)/models:/models \
  ghcr.io/ggerganov/llama.cpp:server \
  --server --host 0.0.0.0 --port 5000 \
  --model /models/tinyllama-1.1b-chat-v1.0.Q4_K_M.gguf
```

### Step 2: Build the Application

```bash
cd /home/wtc/Nocta-system
./mvnw clean package -DskipTests
```

This creates a JAR file at `target/clinical-copilot-1.0.0.jar`

### Step 3: Run the Application

**Option A: Direct Java Execution**
```bash
java -jar target/clinical-copilot-1.0.0.jar
```

**Option B: Using Maven**
```bash
./mvnw spring-boot:run
```

**Option C: Using the Run Script**
```bash
./run.sh
```

The application will start on **http://localhost:8080**

## Accessing the Application

### Web UI
- **Desktop**: http://localhost:8080
- **Mobile Web**: http://localhost:8080/mobile.html
- **H2 Console** (for database): http://localhost:8080/h2-console

### REST API Endpoints

**Create Consultation**:
```bash
curl -X POST http://localhost:8080/api/v1/consultations/upload-audio \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "123e4567-e89b-12d3-a456-426614174000",
    "clinicianId": "123e4567-e89b-12d3-a456-426614174001",
    "audioUrl": "file.wav",
    "rawTranscript": "Patient reports severe headache for 3 days"
  }'
```

**Get Consultation Status**:
```bash
curl http://localhost:8080/api/v1/consultations/{id}/status
```

**Get Full Consultation Details**:
```bash
curl http://localhost:8080/api/v1/consultations/{id}
```

**Approve Consultation**:
```bash
curl -X POST http://localhost:8080/api/v1/consultations/{id}/approve \
  -H "Content-Type: application/json" \
  -d '{"approve": true, "clinicianId": "..."}'
```

## Docker Compose (Full Stack)

To run the entire system with Docker:

```bash
cd docker
docker-compose up --build
```

This starts:
- Clinical Copilot API (port 8080)
- H2 Database (in-memory)
- LLAMA Server (port 5000)

## Project Structure

```
.
├── src/main/java/za/co/ccos/
│   ├── app/                    # Business logic services
│   ├── config/                 # Spring configuration
│   ├── domain/                 # JPA entities
│   ├── infra/                  # Infrastructure adapters
│   │   ├── llm/               # LLAMA integration
│   │   └── persistence/       # Database repositories
│   └── web/                    # REST controllers
├── src/main/resources/
│   ├── application.yml         # Configuration
│   ├── db/migration/           # Flyway migrations
│   └── prompts/                # LLM prompt templates
├── pom.xml                     # Maven configuration
├── mvnw / mvnw.cmd             # Maven wrapper
└── docker/                     # Docker files
```

## Troubleshooting

**Error: "LLAMA server not detected at localhost:5000"**
- Make sure LLAMA server is running (see Step 1 above)
- Check with: `curl http://localhost:5000/health`

**Error: "Spring Boot application fails to start"**
- Verify Java 17+ is installed: `java -version`
- Check Maven wrapper: `./mvnw -version`
- Review logs in `app.log`

**Error: "Port 8080 already in use"**
- Kill the existing process: `lsof -ti:8080 | xargs kill -9`
- Or change port in `application.yml`

## System Workflow

1. **Upload Consultation**: Clinician uploads audio/transcript for a patient
2. **Perception**: LLM extracts structured facts (symptoms, vitals, differentials)
3. **Documentation**: LLM generates SOAP notes and suggests ICD-10 codes
4. **Coordination**: LLM creates action items and drug recommendations
5. **Compliance**: LLM validates against allergies and drug interactions
6. **Approval**: Clinician reviews and approves the generated documentation
7. **Sync**: System syncs approved notes to EHR

## Demo Data

Sample patients and consultations are automatically loaded via Flyway migrations in `src/main/resources/db/migration/V2__sample_data.sql`.

## Development

### Run Tests
```bash
./mvnw test
```

### View Application Logs
```bash
tail -f app.log
```

### Access H2 Database Console
1. Navigate to: http://localhost:8080/h2-console
2. Use these credentials:
   - **JDBC URL**: `jdbc:h2:mem:clinicaldb`
   - **Username**: `sa`
   - **Password**: (leave blank)

- Consultation approval workflow

**Demo Flow:**
1. Login with Clinician ID (default: 550e8400-e29b-41d4-a716-446655440099)
2. Select a patient (John Doe or Jane Smith)
3. Record consultation or type transcript manually
4. Submit for AI processing
5. Review generated SOAP note, ICD-10 codes, and action items
6. Approve and sign

**Note:** For best experience, use on mobile device or mobile browser emulation. Speech recognition requires HTTPS in production (works on localhost).

## API Endpoints

### Upload Consultation
```bash
POST /api/v1/consultations/upload-audio
Content-Type: application/json

{
  "patientId": "550e8400-e29b-41d4-a716-446655440001",
  "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
  "rawTranscript": "Patient reports severe headache for 3 days..."
}
```

### Check Status
```bash
GET /api/v1/consultations/{id}/status
```

### Get Consultation Details
```bash
GET /api/v1/consultations/{id}
```

### Approve Consultation
```bash
POST /api/v1/consultations/{id}/approve
Content-Type: application/json

{
  "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
  "approve": true
}
```

### Health Checks
```bash
GET /api/v1/health
GET /api/v1/llama/health
```

## Sample Data

Two sample patients are pre-loaded:
- **Patient 1**: John Doe (ID: 550e8400-e29b-41d4-a716-446655440001)
  - Allergies: Penicillin
  - Chronic: Hypertension
  
- **Patient 2**: Jane Smith (ID: 550e8400-e29b-41d4-a716-446655440002)
  - Allergies: NSAIDs
  - Chronic: Type 2 Diabetes

## Testing

Run unit tests:
```bash
./mvnw test
```

Run with coverage:
```bash
./mvnw clean test jacoco:report
```

## Agent Workflow

1. **Perception Agent**: Extracts structured clinical facts from transcript
2. **Documentation Agent**: Generates SOAP note + ICD-10 suggestions
3. **Coordination Agent**: Creates actionable items (labs, referrals, prescriptions)
4. **Compliance Agent**: Validates for allergy conflicts and completeness

All agents use LLAMA with prompts stored in `src/main/resources/prompts/`

## Configuration

Edit `src/main/resources/application.yml`:

```yaml
llama:
  server:
    url: http://localhost:5000  # LLAMA server endpoint

spring:
  datasource:
    url: jdbc:h2:mem:clinicaldb  # Use H2 for dev
```

## Project Structure

```
clinical-copilot/
├── src/main/java/za/co/ccos/
│   ├── app/                    # Application services
│   ├── web/                    # REST controllers & DTOs
│   ├── domain/                 # Entities
│   ├── infra/                  # Infrastructure adapters
│   ├── security/               # Security config
│   └── config/                 # Spring configuration
├── src/main/resources/
│   ├── prompts/                # LLAMA prompt templates
│   ├── db/migration/           # Flyway migrations
│   └── static/                 # Web UI (desktop + mobile)
│       ├── mobile.html         # Mobile web app
│       ├── mobile.css          # Mobile styles
│       └── mobile.js           # Mobile logic
├── docker/                     # Docker files
└── .github/workflows/          # CI/CD
```

## LLAMA Integration

The system uses `HttpLlamaAdapter` to communicate with a local LLAMA server via HTTP. The adapter expects:

**Request format:**
```json
{
  "prompt": "...",
  "temperature": 0.2,
  "max_tokens": 1024,
  "top_p": 0.9,
  "top_k": 40
}
```

**Response format:**
```json
{
  "content": "...",
  "tokens_evaluated": 150
}
```

## Prompt Templates

All prompts are in `src/main/resources/prompts/`:
- `perception.prompt.txt` - Extract clinical facts
- `documentation.prompt.txt` - Generate SOAP notes
- `coordination.prompt.txt` - Create action items
- `compliance.prompt.txt` - Validate compliance

## Security Notes

- MVP uses permissive security (no JWT enforcement)
- For production: Enable JWT authentication in `SecurityConfig`
- Encrypt PII at rest
- Redact PHI in logs using `PromptSanitizer`

## Troubleshooting

**LLAMA server not reachable:**
- Check if server is running on port 5000
- Verify `llama.server.url` in application.yml
- Test: `curl http://localhost:5000/health`

**Database errors:**
- H2 console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:clinicaldb`
- Username: `sa`, Password: (empty)

**Tests failing:**
- Ensure mocks are properly configured
- Check LLAMA adapter is mocked in tests

**Mobile app issues:**
- Speech recognition requires HTTPS in production (localhost works)
- Use Chrome/Safari for best compatibility
- Enable microphone permissions when prompted

## CI/CD

GitHub Actions workflow runs on push:
- Builds with Maven
- Runs all tests
- Uploads test reports

## License

Proprietary - Clinical Copilot OS MVP

## Support

For issues or questions, contact the development team.
