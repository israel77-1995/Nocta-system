# Clinical Copilot OS - Documentation Index

## 📖 Start Here

New to the project? Read these in order:

1. **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - High-level overview (5 min read)
2. **[QUICK_START.md](QUICK_START.md)** - Get running in 5 minutes
3. **[README.md](README.md)** - Complete reference guide

## 📚 Documentation

### Getting Started
- **[QUICK_START.md](QUICK_START.md)** - Fastest way to run the system
- **[SETUP.md](SETUP.md)** - Detailed installation and configuration
- **[README.md](README.md)** - Complete user guide

### Technical Documentation
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - System design and patterns
- **[DELIVERABLES.md](DELIVERABLES.md)** - Feature checklist and acceptance criteria
- **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - Executive summary

### API & Testing
- **[postman_collection.json](postman_collection.json)** - Postman API collection
- **[run.sh](run.sh)** - Quick start script

## 🗂️ Project Structure

```
clinical-copilot/
│
├── 📄 Documentation
│   ├── INDEX.md                    ← You are here
│   ├── PROJECT_SUMMARY.md          ← Start here for overview
│   ├── QUICK_START.md              ← 5-minute setup
│   ├── README.md                   ← Complete guide
│   ├── SETUP.md                    ← Detailed setup
│   ├── ARCHITECTURE.md             ← System design
│   └── DELIVERABLES.md             ← Feature checklist
│
├── 🔧 Configuration
│   ├── pom.xml                     ← Maven dependencies
│   ├── .gitignore                  ← Git exclusions
│   └── src/main/resources/
│       └── application.yml         ← App configuration
│
├── 💻 Source Code
│   └── src/main/java/za/co/ccos/
│       ├── app/                    ← Application services
│       │   ├── PerceptionService.java
│       │   ├── DocumentationService.java
│       │   ├── CoordinationService.java
│       │   ├── ComplianceService.java
│       │   └── ConsultationOrchestrator.java
│       ├── web/                    ← REST controllers
│       │   ├── ConsultationController.java
│       │   ├── HealthController.java
│       │   └── dto/                ← Data transfer objects
│       ├── domain/                 ← Domain entities
│       │   ├── Patient.java
│       │   ├── Consultation.java
│       │   └── GeneratedNote.java
│       ├── infra/                  ← Infrastructure
│       │   ├── llm/                ← LLAMA integration
│       │   └── persistence/        ← Repositories
│       └── config/                 ← Spring configuration
│
├── 🧪 Tests
│   └── src/test/java/za/co/ccos/
│       ├── app/                    ← Unit tests
│       └── integration/            ← Integration tests
│
├── 🤖 AI Prompts
│   └── src/main/resources/prompts/
│       ├── perception.prompt.txt
│       ├── documentation.prompt.txt
│       ├── coordination.prompt.txt
│       └── compliance.prompt.txt
│
├── 🗄️ Database
│   └── src/main/resources/db/migration/
│       ├── V1__initial_schema.sql
│       └── V2__sample_data.sql
│
├── 🌐 Web UI
│   └── src/main/resources/static/
│       └── index.html
│
├── 🐳 Docker
│   └── docker/
│       ├── Dockerfile.backend
│       ├── docker-compose.yml
│       └── llama-server-wrapper.py
│
└── 🔄 CI/CD
    └── .github/workflows/
        └── ci.yml
```

## 🎯 Common Tasks

### First Time Setup
1. Read [QUICK_START.md](QUICK_START.md)
2. Download LLAMA model
3. Run `./run.sh`

### Development
1. Start LLAMA server: `./llama.cpp/server -m model.gguf --port 5000`
2. Run app: `./mvnw spring-boot:run`
3. Access UI: http://localhost:8080

### Testing
```bash
./mvnw test                    # Run all tests
./mvnw test -Dtest=ClassName   # Run specific test
```

### Docker Deployment
```bash
cd docker
docker-compose up --build
```

### API Testing
- Import [postman_collection.json](postman_collection.json) into Postman
- Or use cURL examples in [README.md](README.md)

## 📊 File Count Summary

| Category | Count |
|----------|-------|
| Java Source Files | 25 |
| Test Files | 4 |
| Configuration Files | 5 |
| Documentation Files | 7 |
| Prompt Templates | 4 |
| SQL Migrations | 2 |
| Docker Files | 3 |
| **Total** | **50+** |

## 🔍 Find What You Need

### "How do I...?"

**...get started quickly?**  
→ [QUICK_START.md](QUICK_START.md)

**...understand the architecture?**  
→ [ARCHITECTURE.md](ARCHITECTURE.md)

**...set up for production?**  
→ [SETUP.md](SETUP.md) + [README.md](README.md)

**...test the API?**  
→ [postman_collection.json](postman_collection.json)

**...customize prompts?**  
→ `src/main/resources/prompts/*.txt`

**...add a new agent?**  
→ See [ARCHITECTURE.md](ARCHITECTURE.md) - Agent System section

**...troubleshoot issues?**  
→ [SETUP.md](SETUP.md) - Troubleshooting section

**...deploy with Docker?**  
→ [README.md](README.md) - Docker Deployment section

## 🎓 Learning Path

### For Developers
1. [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - Understand the system
2. [ARCHITECTURE.md](ARCHITECTURE.md) - Learn the design
3. Source code in `src/main/java/za/co/ccos/`
4. Tests in `src/test/java/za/co/ccos/`

### For DevOps
1. [SETUP.md](SETUP.md) - Installation guide
2. `docker/docker-compose.yml` - Container setup
3. `.github/workflows/ci.yml` - CI/CD pipeline
4. `src/main/resources/application.yml` - Configuration

### For Product/Business
1. [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - What it does
2. [DELIVERABLES.md](DELIVERABLES.md) - What's included
3. [README.md](README.md) - How to use it

## 🆘 Getting Help

1. **Check logs**: `tail -f logs/application.log`
2. **Health checks**: 
   - App: http://localhost:8080/api/v1/health
   - LLAMA: http://localhost:8080/api/v1/llama/health
3. **H2 Console**: http://localhost:8080/h2-console
4. **Troubleshooting**: See [SETUP.md](SETUP.md)

## ✅ Quick Verification

After setup, verify everything works:

```bash
# 1. Check Java
java -version

# 2. Check LLAMA
curl http://localhost:5000/health

# 3. Check Application
curl http://localhost:8080/api/v1/health

# 4. Run tests
./mvnw test

# 5. Try the UI
open http://localhost:8080
```

## 📝 Notes

- All documentation is in Markdown format
- Code examples use bash/cURL
- Sample data is pre-loaded (see V2 migration)
- Default ports: 8080 (app), 5000 (LLAMA)

---

**Last Updated**: 2024  
**Version**: 1.0.0-SNAPSHOT  
**Status**: ✅ MVP Complete
