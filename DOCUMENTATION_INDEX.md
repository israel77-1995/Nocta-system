# Clinical Copilot OS - Documentation Index

Welcome to the Clinical Copilot OS documentation. This guide helps you find the right documentation for your needs.

## 🚀 Getting Started (Start Here!)

### [QUICK_START.md](QUICK_START.md)
**For those who want to run the system quickly (10 minutes)**
- 3-step quick start
- Prerequisites checklist
- Common troubleshooting
- **Read this first!**

### [RUNNING.md](RUNNING.md)
**For detailed step-by-step instructions**
- Complete setup guide with all options
- Docker setup instructions
- API testing examples
- Performance tuning tips

## 📚 Main Documentation

### [README.md](README.md)
**Complete system documentation**
- Architecture overview
- Technology stack
- Full setup instructions (3 options)
- REST API endpoints with examples
- Project structure
- Troubleshooting guide

### [SETUP.md](SETUP.md)
**Advanced configuration and setup**
- Detailed prerequisite installation
- LLAMA server setup options (A, B)
- Build and deployment instructions
- Docker setup and configuration
- Database configuration
- Development setup

## 🏗️ System Design

### [ARCHITECTURE.md](ARCHITECTURE.md)
**System design and architecture details**
- Layered architecture explanation
- Component descriptions
- Data flow diagrams
- Service responsibilities
- Technology choices rationale

### [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)
**Project overview and goals**
- Project objectives
- Key features
- Success criteria
- Timeline and milestones

## 🎯 Features & Capabilities

### [PRODUCT_FEATURES.md](PRODUCT_FEATURES.md)
**Detailed feature list**
- Clinical features
- System capabilities
- User workflows
- Integration points

### [DEMO_GUIDE.md](DEMO_GUIDE.md)
**How to demonstrate the system**
- Demo scenarios
- Step-by-step walkthroughs
- Sample data and test cases
- Expected outcomes

## 📱 Mobile Development

### [MOBILE_APP_GUIDE.md](MOBILE_APP_GUIDE.md)
**Mobile app development guide**
- Mobile UI features
- Installation and setup
- Development workflow
- Testing procedures

## 🔧 Tools & Integration

### [postman_collection.json](postman_collection.json)
**API testing collection**
- All API endpoints
- Example requests
- Sample payloads
- Response examples

## 📊 Quick Reference

### What's Running?

| Component | Port | URL |
|-----------|------|-----|
| Clinical Copilot API | 8080 | http://localhost:8080 |
| LLAMA Server | 5000 | http://localhost:5000 |
| H2 Database | 8080/h2-console | http://localhost:8080/h2-console |

### Key Directories

```
/home/wtc/Nocta-system/
├── src/main/java/za/co/ccos/
│   ├── app/                 # Business logic services
│   ├── config/             # Spring configuration
│   ├── domain/             # JPA entities
│   ├── infra/              # Infrastructure layer
│   └── web/                # REST controllers
├── src/main/resources/
│   ├── application.yml     # Configuration
│   ├── db/migration/       # Database migrations
│   └── prompts/            # LLM prompt templates
├── docker/                 # Docker files
├── mobile-app/             # Mobile app code
└── pom.xml                 # Maven configuration
```

## 🎓 Learning Path

### For New Users
1. **Read**: [QUICK_START.md](QUICK_START.md) (5 min)
2. **Run**: Start the system (10 min)
3. **Test**: Use the API (5 min)
4. **Explore**: Check the Web UI (5 min)

### For Developers
1. **Read**: [ARCHITECTURE.md](ARCHITECTURE.md)
2. **Read**: [README.md](README.md)
3. **Setup**: Follow [RUNNING.md](RUNNING.md)
4. **Code**: Review `src/main/java` structure
5. **Test**: Try the [postman_collection.json](postman_collection.json)

### For DevOps/Deployment
1. **Read**: [SETUP.md](SETUP.md)
2. **Review**: [RUNNING.md](RUNNING.md) Docker section
3. **Configure**: Update application.yml for your environment
4. **Deploy**: Use docker-compose or manual setup

### For Product Managers
1. **Read**: [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)
2. **Review**: [PRODUCT_FEATURES.md](PRODUCT_FEATURES.md)
3. **Demo**: Follow [DEMO_GUIDE.md](DEMO_GUIDE.md)

## 📋 Checklists

### Pre-Launch Checklist
- [ ] Java 17+ installed
- [ ] LLAMA model downloaded
- [ ] LLAMA server built
- [ ] Application built with Maven
- [ ] All ports available (5000, 8080)

### Getting System Running
- [ ] Start LLAMA server
- [ ] Verify LLAMA health: `curl http://localhost:5000/health`
- [ ] Build application: `./mvnw clean package`
- [ ] Run application: `java -jar target/clinical-copilot-*.jar`
- [ ] Access UI: http://localhost:8080

### First Test Workflow
- [ ] Create consultation via API
- [ ] Check status endpoint
- [ ] Wait for processing (10-30s)
- [ ] View results
- [ ] Approve consultation
- [ ] Verify sync status

## 🔍 Finding Specific Information

**I want to...** | **Read this**
---|---
...run the system quickly | [QUICK_START.md](QUICK_START.md)
...understand the architecture | [ARCHITECTURE.md](ARCHITECTURE.md)
...deploy to production | [SETUP.md](SETUP.md)
...test the API | [postman_collection.json](postman_collection.json)
...develop features | [ARCHITECTURE.md](ARCHITECTURE.md) + [README.md](README.md)
...see what's possible | [PRODUCT_FEATURES.md](PRODUCT_FEATURES.md)
...demonstrate the system | [DEMO_GUIDE.md](DEMO_GUIDE.md)
...build the mobile app | [MOBILE_APP_GUIDE.md](MOBILE_APP_GUIDE.md)

## 🆘 Troubleshooting

If you encounter issues:

1. **Check**: [QUICK_START.md](QUICK_START.md) troubleshooting section
2. **Review**: [RUNNING.md](RUNNING.md) detailed troubleshooting
3. **Search**: [README.md](README.md) for your error message
4. **Setup**: Follow [SETUP.md](SETUP.md) step-by-step

## 📞 Support Resources

- **Quick answers**: Troubleshooting sections in documentation
- **API reference**: [postman_collection.json](postman_collection.json)
- **Code reference**: See `src/` directory with javadoc comments
- **Logs**: Check `app.log` in project root

## 📝 File Descriptions

| File | Purpose |
|------|---------|
| README.md | Complete system documentation |
| QUICK_START.md | Fast setup guide (10 min) |
| RUNNING.md | Detailed step-by-step instructions |
| SETUP.md | Advanced setup and configuration |
| ARCHITECTURE.md | System design and components |
| PROJECT_SUMMARY.md | Project overview |
| PRODUCT_FEATURES.md | Feature descriptions |
| DEMO_GUIDE.md | How to demonstrate the system |
| MOBILE_APP_GUIDE.md | Mobile development guide |
| postman_collection.json | API testing file |
| pom.xml | Maven build configuration |
| application.yml | Application configuration |
| docker-compose.yml | Docker multi-container setup |

## ✨ Key Highlights

- **Zero Cloud Dependencies**: Runs entirely locally
- **HIPAA-Friendly**: No data leaves your servers
- **AI-Powered**: Uses open-source LLAMA models
- **Fast**: TinyLlama model for quick testing
- **Accurate**: Llama-2 model for production use
- **REST API**: Standard API for integration
- **Web UI**: Easy-to-use interface included

---

**Last Updated**: December 9, 2025
**Version**: 1.0.0

Start with [QUICK_START.md](QUICK_START.md) for the fastest way to get running!
