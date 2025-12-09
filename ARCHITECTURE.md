# Clinical Copilot OS - Architecture Documentation

## Overview

Clinical Copilot OS is a Java-based clinical documentation system that uses local LLAMA models to automate SOAP note generation, ICD-10 coding, and care coordination from consultation transcripts.

## Architectural Pattern: Layered Architecture

```
┌─────────────────────────────────────────────────────────┐
│           PRESENTATION LAYER                             │
│  REST Controllers, DTOs, WebSocket, Web UI              │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│           APPLICATION LAYER                              │
│  Orchestration, Agent Services, Business Logic          │
│  - ConsultationOrchestrator                             │
│  - PerceptionService                                     │
│  - DocumentationService                                  │
│  - CoordinationService                                   │
│  - ComplianceService                                     │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│           DOMAIN LAYER                                   │
│  Entities, Value Objects, Domain Logic                  │
│  - Patient, Consultation, GeneratedNote                 │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│           INFRASTRUCTURE LAYER                           │
│  Persistence, External Integrations, Adapters           │
│  - LlamaAdapter (HTTP/Process)                          │
│  - Repositories (JPA)                                    │
│  - EHR/FHIR Adapter                                      │
└─────────────────────────────────────────────────────────┘
```

## Agent Workflow

```
Consultation Upload
       ↓
   [STT Adapter] (optional)
       ↓
   Raw Transcript
       ↓
┌──────────────────┐
│ Perception Agent │ → Extract structured clinical facts
└──────────────────┘
       ↓
  Structured JSON
       ↓
┌────────────────────┐
│ Documentation Agent│ → Generate SOAP + ICD-10
└────────────────────┘
       ↓
  SOAP Note + Codes
       ↓
┌────────────────────┐
│ Coordination Agent │ → Generate actions (labs, referrals, Rx)
└────────────────────┘
       ↓
┌──────────────────┐
│ Compliance Agent │ → Validate for conflicts & completeness
└──────────────────┘
       ↓
  Draft Note (READY)
       ↓
  Clinician Review
       ↓
  Approve → EHR Sync
```

## Component Details

### Presentation Layer

**Controllers:**
- `ConsultationController`: Main API for consultation lifecycle
- `HealthController`: Health checks for app and LLAMA

**DTOs:**
- Request/Response objects for API contracts
- Decouples internal domain from external API

### Application Layer

**ConsultationOrchestrator:**
- Coordinates multi-agent workflow
- Async processing with @Async
- Error handling and state management

**Agent Services:**
Each agent service:
1. Loads prompt template from resources
2. Injects context (patient data, transcript)
3. Calls LlamaAdapter with appropriate options
4. Parses and validates JSON response
5. Returns structured data

### Domain Layer

**Entities:**
- `Patient`: Demographics, allergies, chronic conditions
- `Consultation`: Consultation metadata and state
- `GeneratedNote`: SOAP note, ICD-10, actions

**State Machine:**
```
QUEUED → PROCESSING → READY → APPROVED → SYNCED
                  ↓
                ERROR
```

### Infrastructure Layer

**LlamaAdapter Interface:**
- Abstraction for LLAMA integration
- Implementations:
  - `HttpLlamaAdapter`: Calls LLAMA REST server
  - `ProcessLlamaAdapter`: Invokes CLI (alternative)

**Repositories:**
- Spring Data JPA repositories
- H2 for dev, PostgreSQL for production

## Data Flow

1. **Upload**: Client POSTs transcript → Controller creates Consultation (QUEUED)
2. **Async Processing**: Orchestrator picks up consultation
3. **Perception**: Extract facts → JSON
4. **Documentation**: Generate SOAP → GeneratedNote entity
5. **Coordination**: Generate actions → JSON stored in note
6. **Compliance**: Validate → Log issues
7. **Ready**: Consultation state = READY, note available
8. **Approval**: Clinician approves → state = APPROVED
9. **Sync**: Simulate FHIR POST → state = SYNCED

## LLAMA Integration

**Prompt Engineering:**
- Templates stored in `resources/prompts/`
- Placeholders: `<<TRANSCRIPT>>`, `<<PATIENT_NAME>>`, etc.
- Few-shot examples included in prompts
- Temperature tuning per agent (0.1-0.3 for deterministic)

**HTTP Protocol:**
```
POST http://localhost:5000/generate
{
  "prompt": "...",
  "temperature": 0.2,
  "max_tokens": 1024
}

Response:
{
  "content": "...",
  "tokens_evaluated": 150
}
```

## Security Considerations

**Current (MVP):**
- Permissive security (no auth)
- CSRF disabled
- All endpoints public

**Production Requirements:**
- JWT authentication
- Role-based access (clinician, admin)
- Encrypt PII at rest
- Audit logging with PHI redaction
- TLS/HTTPS only

## Scalability

**Current:**
- Single instance
- Async processing with thread pool
- In-memory H2 database

**Production Scaling:**
- Horizontal scaling with load balancer
- PostgreSQL with connection pooling
- Redis for distributed caching
- Message queue (RabbitMQ/Kafka) for async jobs
- Separate LLAMA inference cluster

## Testing Strategy

**Unit Tests:**
- Mock LlamaAdapter
- Test each service in isolation
- Verify JSON parsing and validation

**Integration Tests:**
- Full workflow with mocked LLAMA
- Database transactions
- State transitions

**E2E Tests:**
- Real LLAMA server
- Full API flow
- UI automation (optional)

## Deployment

**Development:**
```bash
./mvnw spring-boot:run
```

**Docker:**
```bash
docker-compose up
```

**Production:**
- Build JAR: `mvn clean package`
- Deploy to Kubernetes/ECS
- External PostgreSQL
- Dedicated LLAMA inference service

## Monitoring

**Health Checks:**
- `/api/v1/health` - Application health
- `/api/v1/llama/health` - LLAMA connectivity

**Metrics (Future):**
- Micrometer + Prometheus
- Agent response times
- Success/error rates
- Token usage

## Configuration

**application.yml:**
- Database connection
- LLAMA server URL
- Logging levels
- Thread pool size

**Profiles:**
- `dev`: H2, verbose logging
- `prod`: PostgreSQL, optimized settings

## Dependencies

**Core:**
- Spring Boot 3.2
- Spring Data JPA
- Spring Security

**Database:**
- H2 (dev)
- PostgreSQL (prod)
- Flyway migrations

**Utilities:**
- Lombok (boilerplate reduction)
- Jackson (JSON)
- JWT (authentication)

## Future Enhancements

1. **Real-time STT**: WebSocket audio streaming
2. **Multi-modal**: Support images (X-rays, photos)
3. **EHR Integration**: Real FHIR endpoints
4. **Advanced Compliance**: Drug interaction checking
5. **Analytics Dashboard**: Clinician productivity metrics
6. **Voice Commands**: Hands-free operation
7. **Mobile App**: iOS/Android clients
