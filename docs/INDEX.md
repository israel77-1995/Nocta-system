# Documentation Index

## Quick Links

**Start Here:**
- [README.md](../README.md) - Project overview and quick start
- [SETUP.md](SETUP.md) - Detailed setup instructions
- [DEMO_GUIDE.md](DEMO_GUIDE.md) - 5-minute demo script for judges

## Technical Documentation

**Architecture & Design:**
- [ARCHITECTURE.md](technical/ARCHITECTURE.md) - System architecture and layered design
- [API_GUIDE.md](technical/API_GUIDE.md) - REST API reference

**Key Concepts:**
- **Layered Architecture** - Separation of concerns (Presentation → Application → Domain → Infrastructure)
- **Multi-Agent AI** - 4 specialized agents (Perception, Documentation, Coordination, Compliance)
- **LLAMA Integration** - Meta's LLAMA 3.3 70B (text) + 3.2 11B Vision (images)

## Business Documentation

**Strategy & Planning:**
- [BUSINESS_MODEL.md](business/BUSINESS_MODEL.md) - Business Model Canvas with financials
- [GO_TO_MARKET.md](business/GO_TO_MARKET.md) - Customer acquisition and market strategy

**Key Metrics:**
- Target: 500 clinicians by Month 12
- Pricing: $99/clinician/month
- Break-even: Month 8
- Year 1 Revenue: ~$250K

## For Judges

**Evaluation Criteria Alignment:**

1. **Problem-Solution Fit (25%)**
   - See: [README.md](../README.md) - "What It Does" section
   - See: [BUSINESS_MODEL.md](business/BUSINESS_MODEL.md) - Value Propositions

2. **Technical Implementation (25%)**
   - See: [ARCHITECTURE.md](technical/ARCHITECTURE.md) - Multi-agent architecture
   - See: [API_GUIDE.md](technical/API_GUIDE.md) - REST API design

3. **Business Viability (25%)**
   - See: [BUSINESS_MODEL.md](business/BUSINESS_MODEL.md) - Financial projections
   - See: [GO_TO_MARKET.md](business/GO_TO_MARKET.md) - Market strategy

4. **Scalability (15%)**
   - See: [ARCHITECTURE.md](technical/ARCHITECTURE.md) - Scalability design
   - See: [API_GUIDE.md](technical/API_GUIDE.md) - Integration points

5. **Presentation (10%)**
   - See: [DEMO_GUIDE.md](DEMO_GUIDE.md) - Complete demo script

## Project Structure

```
Nocta-system/
├── README.md                   # Start here
├── start-app.sh                # Single script to run everything
├── docs/
│   ├── INDEX.md               # This file
│   ├── SETUP.md               # Setup instructions
│   ├── DEMO_GUIDE.md          # Demo script
│   ├── technical/
│   │   ├── ARCHITECTURE.md    # System design
│   │   └── API_GUIDE.md       # API reference
│   └── business/
│       ├── BUSINESS_MODEL.md  # Business model canvas
│       └── GO_TO_MARKET.md    # Market strategy
├── src/
│   └── main/
│       ├── java/za/co/ccos/
│       │   ├── web/           # Presentation Layer
│       │   ├── app/           # Application Layer
│       │   ├── domain/        # Domain Layer
│       │   └── infra/         # Infrastructure Layer
│       └── resources/
│           ├── prompts/       # LLM prompts
│           ├── db/migration/  # Database migrations
│           └── static/        # Web UI
└── postman_collection.json    # API testing
```

## Quick Commands

**Run Application:**
```bash
./start-app.sh
```

**Build Only:**
```bash
./mvnw clean package -DskipTests
```

**Run Tests:**
```bash
./mvnw test
```

**Access Application:**
- Web UI: http://localhost:8080
- Mobile UI: http://localhost:8080/mobile.html
- API Health: http://localhost:8080/api/v1/health
- Database Console: http://localhost:8080/h2-console

## Support

**Common Issues:**
- API key not set → See [SETUP.md](SETUP.md)
- Port 8080 in use → `lsof -ti:8080 | xargs kill -9`
- Java version error → Install Java 17+

**Need Help?**
- Technical questions → [ARCHITECTURE.md](technical/ARCHITECTURE.md)
- API questions → [API_GUIDE.md](technical/API_GUIDE.md)
- Business questions → [BUSINESS_MODEL.md](business/BUSINESS_MODEL.md)
- Demo questions → [DEMO_GUIDE.md](DEMO_GUIDE.md)
