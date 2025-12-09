# Clinical Copilot OS - MVP

A Java-based clinical documentation system using LLAMA for AI-powered SOAP note generation, ICD-10 coding, and care coordination.

## Architecture

**Layered Architecture:**
- **Presentation Layer**: REST API + Web UI + Mobile Web App
- **Application Layer**: Agent orchestration services (Perception, Documentation, Coordination, Compliance)
- **Domain Layer**: Core entities (Patient, Consultation, GeneratedNote)
- **Infrastructure Layer**: LLM adapter, persistence, external integrations

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Data JPA (H2/PostgreSQL)
- Flyway migrations
- LLAMA via HTTP adapter
- Maven
- Docker & Docker Compose

## Prerequisites

1. **Java 17+** installed
2. **Maven 3.6+** installed
3. **Docker & Docker Compose** (optional, for containerized deployment)
4. **LLAMA model weights** (GGUF format)

## Quick Start

### Option 1: Run with Local LLAMA Server

1. **Start LLAMA server** (in separate terminal):
```bash
# Download llama.cpp
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp
make

# Download a model (e.g., Llama-2-7B GGUF)
# Place model in ./models/llama-model.gguf

# Start server
./server -m ./models/llama-model.gguf --host 0.0.0.0 --port 5000 --ctx-size 2048
```

2. **Run the application**:
```bash
cd /home/wtc/Nocta-system
./mvnw spring-boot:run
```

3. **Access the UI**: 
   - Desktop: http://localhost:8080
   - Mobile: http://localhost:8080/mobile.html

### Option 2: Run with Docker Compose

1. **Place LLAMA model** in `docker/models/llama-model.gguf`

2. **Start services**:
```bash
cd docker
docker-compose up --build
```

3. **Access the UI**: 
   - Desktop: http://localhost:8080
   - Mobile: http://localhost:8080/mobile.html

## Mobile Demo App

Access the mobile-optimized web app at: **http://localhost:8080/mobile.html**

**Features:**
- Touch-optimized interface
- Voice recording with speech-to-text
- Real-time SOAP note generation
- Patient selection
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
