# Clinical Copilot OS

**AI-Powered Clinical Documentation System Using Meta's LLAMA 3.3 70B**

Save clinicians 2+ hours daily on documentation. See 20% more patients. Reduce burnout.

---

## Quick Start (2 Steps)

### Step 1: Get Free LLAMA API Key

1. Visit https://console.groq.com
2. Sign up (free, no credit card)
3. Create API key
4. Copy key (starts with `gsk_`)

### Step 2: Run Application

**Web Version:**
```bash
# Clone repository
git clone <repository-url>
cd Nocta-system

# Create .env file
echo "GROQ_API_KEY=gsk_your_key_here" > .env

# Run (handles build + start automatically)
./start-app.sh
```

**Access:** http://localhost:8080

**Mobile Version (Expo Go):**
```bash
# Navigate to mobile app
cd mobile-app

# Install dependencies and start
npm start

# Scan QR code with Expo Go app
# iOS: Download from App Store
# Android: Download from Google Play
```

**🚀 Deploy & Make Downloadable:**

[![Deploy on Railway](https://railway.app/button.svg)](https://railway.app/new/template/spring-boot)

- **Railway (Free):** Deploy in 2 minutes - see [DEPLOY.md](DEPLOY.md)
- **PWA Install:** Users install from browser like native app
- **Works Offline:** Cached for offline use after first visit
- **All Platforms:** iOS, Android, Desktop, any device with browser

---

## What It Does

Clinical Copilot OS automates clinical documentation using a **4-agent AI architecture**:

1. **Perception Agent** - Extracts structured clinical facts from consultation transcript
2. **Documentation Agent** - Generates professional SOAP notes with ICD-10 codes
3. **Coordination Agent** - Creates actionable care plan (labs, referrals, prescriptions)
4. **Compliance Agent** - Validates against allergies and drug interactions

**Result:** 12-15 minutes of documentation completed in 30 seconds.

---

## Key Features

- ✅ **Multi-Agent AI** - 4 specialized agents for accuracy
- ✅ **Medical Image Analysis** - LLAMA 3.2 Vision for wounds/rashes
- ✅ **Real-Time Compliance** - Catches allergy conflicts before they happen
- ✅ **Patient Summaries** - AI-generated history analysis before consultation
- ✅ **Appointment Scheduling** - Automated follow-up recommendations
- ✅ **Patient Communications** - Layman's terms email explanations
- ✅ **EHR Integration Ready** - REST API for any system

---

## Architecture

**Layered Design for Separation of Concerns:**

```
┌─────────────────────────────────────┐
│   Presentation Layer (web/)         │  REST API + Web UI
├─────────────────────────────────────┤
│   Application Layer (app/)          │  Business Logic + Agents
├─────────────────────────────────────┤
│   Domain Layer (domain/)            │  Core Entities
├─────────────────────────────────────┤
│   Infrastructure Layer (infra/)     │  LLM + Database + External APIs
└─────────────────────────────────────┘
```

**Tech Stack:**
- Java 17 + Spring Boot 3.1.5
- Meta LLAMA 3.3 70B (via Groq API)
- Meta LLAMA 3.2 11B Vision (via OpenRouter API)
- Spring Data JPA + H2/PostgreSQL
- Flyway Migrations

---

## REST API

Base URL: `http://localhost:8080/api/v1`

**Key Endpoints:**

```bash
# Health checks
GET /health
GET /llama/health

# Patients
GET /patients
GET /patients/{id}/summary

# Consultations
POST /consultations/upload-audio
GET /consultations/{id}
GET /consultations/{id}/status
POST /consultations/{id}/approve
GET /consultations/patient/{patientId}/history

# Image analysis
POST /image-analysis/analyze
```

**Full API documentation:** [docs/technical/API_GUIDE.md](docs/technical/API_GUIDE.md)

---

## Project Structure

```
Nocta-system/
├── src/main/java/za/co/ccos/
│   ├── web/                    # Presentation Layer (Controllers, DTOs)
│   ├── app/                    # Application Layer (Services, Agents)
│   ├── domain/                 # Domain Layer (Entities, Business Rules)
│   └── infra/                  # Infrastructure Layer (LLM, Database)
├── src/main/resources/
│   ├── prompts/                # LLM prompt templates
│   ├── db/migration/           # Flyway database migrations
│   └── static/                 # Web UI (mobile.html, mobile.js, mobile.css)
├── docs/
│   ├── technical/              # Architecture, API docs
│   └── business/               # Business model, go-to-market
├── start-app.sh                # Single script to build & run
└── README.md                   # This file
```

---

## Documentation

**For Judges:**
- [DEMO_GUIDE.md](docs/DEMO_GUIDE.md) - 5-minute demo script with Q&A

**Technical:**
- [ARCHITECTURE.md](docs/technical/ARCHITECTURE.md) - System design and layers
- [API_GUIDE.md](docs/technical/API_GUIDE.md) - REST API reference
- [SETUP.md](docs/SETUP.md) - Detailed setup instructions

**Business:**
- [BUSINESS_MODEL.md](docs/business/BUSINESS_MODEL.md) - Business Model Canvas
- [GO_TO_MARKET.md](docs/business/GO_TO_MARKET.md) - Market strategy

---

## Demo Workflow

1. **Login** - Select clinician
2. **Patient Selection** - Choose patient, view AI-generated summary
3. **Consultation** - Record or type transcript + vital signs
4. **AI Processing** - Watch 4 agents work in real-time
5. **Review** - See SOAP note, ICD-10 codes, action items
6. **Approve** - Clinician signs off
7. **Sync** - Auto-sync to EHR, schedule appointment, notify patient

**Sample Patient:** Sarah Johnson (ID: 550e8400-e29b-41d4-a716-446655440001)

---

## Business Model

**Pricing:**
- Individual: $99/clinician/month
- Small Practice (5-10): $79/clinician/month
- Enterprise (20+): $59/clinician/month

**Target Market:**
- South Africa: 40,000 practicing physicians
- Year 1 goal: 500 clinicians
- Break-even: Month 8

**Value Proposition:**
- Save 2+ hours daily per clinician
- Increase capacity by 20% (3-4 more patients/day)
- Generate $180K+ additional revenue annually per clinician

---

## Competitive Advantages

1. **Multi-Agent Architecture** - More accurate than single-model solutions
2. **Vision Integration** - Only solution with medical image analysis
3. **Real-Time Compliance** - Catches errors before they happen
4. **Open-Source LLM** - No vendor lock-in, transparent AI
5. **Layered Architecture** - Easy integration with any EHR system

---

## Development

**Build:**
```bash
./mvnw clean package -DskipTests
```

**Run:**
```bash
java -jar target/clinical-copilot-1.0.0.jar
```

**Test:**
```bash
./mvnw test
```

**Database Console:**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:clinicaldb`
- Username: `sa`
- Password: (empty)

---

## Environment Variables

Create `.env` file in project root:

```bash
# Required: LLAMA 3.3 70B for text processing
GROQ_API_KEY=gsk_your_key_here

# Optional: LLAMA 3.2 11B Vision for image analysis
OPENROUTER_API_KEY=sk-or-v1-your_key_here
```

**Get API Keys:**
- Groq (free): https://console.groq.com
- OpenRouter (free $1 credit): https://openrouter.ai/keys

---

## Troubleshooting

**Error: "GROQ_API_KEY not set"**
- Ensure `.env` file exists with valid key

**Error: "Port 8080 already in use"**
```bash
lsof -ti:8080 | xargs kill -9
```

**Error: "Java version mismatch"**
- Install Java 17+: https://adoptium.net/

---

## System Requirements

- **Java:** 17 or higher
- **Maven:** 3.6+ (included via `mvnw` wrapper)
- **Memory:** 2GB RAM minimum
- **Disk:** 500MB for application + dependencies

---

## License

Proprietary - Clinical Copilot OS MVP

---

## Support

For questions or issues:
- Technical docs: [docs/technical/](docs/technical/)
- Business docs: [docs/business/](docs/business/)
- Demo guide: [docs/DEMO_GUIDE.md](docs/DEMO_GUIDE.md)

---

**Built with ❤️ using Meta's LLAMA models**
