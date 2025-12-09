# System Architecture

## Layered Architecture Design

Clinical Copilot OS follows a **strict layered architecture** for separation of concerns and reusability.

```
┌─────────────────────────────────────────────────────────┐
│              Presentation Layer                         │
│  REST API Controllers + Web UI + Mobile Interface       │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│              Application Layer                          │
│  Business Logic Services + Agent Orchestration          │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│              Domain Layer                               │
│  Core Entities + Business Rules + Domain Logic          │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│              Infrastructure Layer                       │
│  LLM Adapters + Database + External Integrations        │
└─────────────────────────────────────────────────────────┘
```

## Layer Responsibilities

### 1. Presentation Layer (`web/`)
**Purpose:** Handle HTTP requests/responses, expose REST API

**Components:**
- `ConsultationController` - Consultation endpoints
- `PatientController` - Patient data endpoints
- `ImageAnalysisController` - Medical image analysis
- DTOs for request/response mapping

**Dependencies:** Application Layer only

### 2. Application Layer (`app/`)
**Purpose:** Business logic, orchestration, workflows

**Components:**
- `ConsultationOrchestrator` - Main workflow coordinator
- `PerceptionAgent` - Extract clinical facts
- `DocumentationAgent` - Generate SOAP notes
- `CoordinationAgent` - Create action items
- `ComplianceAgent` - Validate safety
- `PatientSummaryService` - Patient history analysis
- `AppointmentSchedulingService` - Schedule follow-ups
- `PatientEmailService` - Patient communications

**Dependencies:** Domain Layer + Infrastructure Layer

### 3. Domain Layer (`domain/`)
**Purpose:** Core business entities and rules

**Components:**
- `Patient` - Patient entity
- `Consultation` - Consultation entity
- `GeneratedNote` - AI-generated documentation
- `VitalSigns` - Embedded vital signs
- Business validation logic

**Dependencies:** None (pure domain logic)

### 4. Infrastructure Layer (`infra/`)
**Purpose:** External integrations, persistence, adapters

**Components:**
- `GroqLlamaAdapter` - LLAMA 3.3 70B text processing
- `GroqVisionAdapter` - LLAMA 3.2 11B Vision image analysis
- `PatientRepository` - Database access
- `ConsultationRepository` - Database access

**Dependencies:** Domain Layer only

## Multi-Agent AI Architecture

```
Transcript Input
      ↓
┌─────────────────┐
│ Perception Agent│ → Extract structured clinical facts
└─────────────────┘
      ↓
┌─────────────────┐
│Documentation Agt│ → Generate SOAP notes + ICD-10 codes
└─────────────────┘
      ↓
┌─────────────────┐
│Coordination Agt │ → Create action items (labs, referrals)
└─────────────────┘
      ↓
┌─────────────────┐
│ Compliance Agent│ → Validate allergies + drug interactions
└─────────────────┘
      ↓
   Final Note
```

## Technology Stack

**Backend:**
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- H2 Database (dev) / PostgreSQL (prod)
- Flyway Migrations
- Lombok

**AI/ML:**
- Meta LLAMA 3.3 70B (via Groq API)
- Meta LLAMA 3.2 11B Vision (via OpenRouter API)

**Frontend:**
- Vanilla JavaScript
- HTML5/CSS3
- Web Speech API

## API Design Principles

1. **RESTful** - Standard HTTP methods (GET, POST, PUT, DELETE)
2. **Stateless** - No server-side session state
3. **Versioned** - `/api/v1/` prefix for future compatibility
4. **JSON** - All requests/responses use JSON
5. **Idempotent** - Safe retry behavior

## Database Schema

```sql
patients
├── id (UUID, PK)
├── first_name
├── last_name
├── date_of_birth
├── allergies
└── chronic_conditions

consultations
├── id (UUID, PK)
├── patient_id (FK)
├── clinician_id (UUID)
├── raw_transcript
├── status (ENUM)
├── blood_pressure
├── heart_rate
├── temperature
├── oxygen_saturation
├── respiratory_rate
├── weight
├── height
└── created_at

generated_notes
├── id (UUID, PK)
├── consultation_id (FK)
├── soap_note
├── icd10_codes
├── action_items
└── compliance_notes
```

## Security Considerations

- Environment-based API key management
- No hardcoded credentials
- PII sanitization in logs
- CORS configuration for web clients
- Input validation at controller layer

## Scalability Design

- **Horizontal Scaling:** Stateless design allows multiple instances
- **Database:** Connection pooling, read replicas support
- **Caching:** Ready for Redis integration
- **Rate Limiting:** Prepared for API gateway integration
- **Async Processing:** Agent pipeline can be made async

## Integration Points

External systems can integrate via REST API:

- **EHR Systems:** POST consultations, GET results
- **Mobile Apps:** Full API access
- **Analytics Platforms:** Export consultation data
- **Billing Systems:** ICD-10 codes for claims

## Deployment Architecture

```
┌──────────────┐
│  Load Balancer│
└──────────────┘
       ↓
┌──────────────┐  ┌──────────────┐
│  App Server 1│  │  App Server 2│
└──────────────┘  └──────────────┘
       ↓                 ↓
┌──────────────────────────────┐
│      PostgreSQL Database      │
└──────────────────────────────┘
       ↓
┌──────────────────────────────┐
│    External LLM APIs          │
│  (Groq + OpenRouter)          │
└──────────────────────────────┘
```

## Performance Metrics

- **API Response Time:** < 200ms (excluding LLM calls)
- **LLM Processing:** 5-15 seconds per consultation
- **Database Queries:** < 50ms average
- **Concurrent Users:** 100+ supported per instance
