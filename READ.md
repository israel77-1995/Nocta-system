# Nocta — Ambient AI Scribe for Clinical Documentation

> **Nocta** is an ambient AI scribe that converts clinician–patient conversations into structured, EHR-ready clinical notes using Llama (≥3.2). Nocta aims to reduce documentation time, improve record completeness, and return time to patient care — while following strong privacy and safety controls.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Key Features](#key-features)
3. [Suggested Tech Stack](#suggested-tech-stack)
4. [Architecture & Components](#architecture--components)
5. [Getting Started (MVP)](#getting-started-mvp)
6. [Model Selection & Rationale](#model-selection--rationale)
7. [Privacy, Security & Compliance](#privacy-security--compliance)
8. [Evaluation & Metrics](#evaluation--metrics)
9. [Development & Contribution](#development--contribution)
10. [Deployment & Operations](#deployment--operations)
11. [Troubleshooting & FAQs](#troubleshooting--faqs)
12. [License & Attribution](#license--attribution)

---

## Project Overview

Nocta listens to clinical encounters (with patient consent), converts speech to text, applies PHI-aware preprocessing, and uses a Llama model to generate a concise, structured clinical note (e.g., SOAP). Notes are presented to clinicians for review and then pushed to the EHR.

**Goals:**

* Reduce clinician documentation time and burnout.
* Maintain high accuracy and clinician trust (low edit rates).
* Protect patient privacy and meet regulatory requirements.

---

## Key Features

* Real-time or near-real-time speech-to-text
* PII/PHI scrubbing and speaker diarization (clinician vs patient)
* Llama-based summarization into structured templates (SOAP, HPI, A/P)
* **Foreign-language-to-English translation for multilingual patient encounters**
* Hallucination detection and provenance tracking
* Clinician review UI with one-tap accept/push to EHR (FHIR-compatible)
* Audit logging, consent capture, and RBAC

## Suggested Tech Stack

(You said you want Java in the backend — this stack integrates Java where it makes sense.)

**Client / Edge**

* React Native (mobile clinician app) or Progressive Web App (PWA)
* On-device audio capture, VAD, basic diarization

**Speech-to-Text (STT)**

* On-device SDKs (Apple/Android) or HIPAA-capable cloud STT (Google Cloud Speech-to-Text with BAA, AWS Transcribe Medical, Azure Speech)

**Backend**

* **Java (Spring Boot)** — primary backend API, authentication, EHR integration (FHIR clients), orchestration
* **Python microservice(s)** — model orchestration, ML tooling, data pipelines (used for Llama prompt templates, post-processing, hallucination detectors)

**Model Inference**

* Containerized Llama inference service (ON-PREM or cloud) — use Llama 3.2+ variants
* Quantization & acceleration: GGML / Triton / vLLM / Hugging Face optimized runtimes (choose based on infra)

**Data & Retrieval**

* PostgreSQL for transactional data and metadata
* Encrypted vector DB for RAG (Milvus / Weaviate / Pinecone depending on hosting + compliance)

**Infrastructure**

* Docker + Kubernetes for orchestration
* CI/CD: GitHub Actions / GitLab CI
* Observability: Prometheus + Grafana + ELK/Opensearch

**Security & Compliance**

* Vault (secrets), Key Management (KMS), TLS everywhere
* Audit logs in WORM storage

---

## Architecture & Components

1. **Clinician Device** — capture audio; show consent; display draft note; allow edits.
2. **STT Service** — produces transcripts (prefer on-device or HIPAA cloud).
3. **Preprocessor** — diarization, PHI scrubber, segmentation.
4. **Model Service** — Llama inference + RAG (retrieves prior context when allowed).
5. **Postprocessor** — hallucination checks, template mapping, confidence scoring.
6. **Clinician Review UI** — provenance highlights, quick accept/edit.
7. **EHR Adapter** — push signed note to EHR via FHIR (requires clinician sign-off).
8. **Audit & Monitoring** — logs, metrics, incidents.

---

## Getting Started (MVP)

This MVP steps assume you have Docker and Java/Node/Python installed.

### Clone

```bash
git clone <your-repo-url>
cd nocta
```

### Local dev (suggested structure)

* `backend/` — Spring Boot Java service (auth, EHR adapter, APIs)
* `ml/` — Python service(s) for STT integration, prompt templates, inference client
* `frontend/` — React Native or PWA clinician app
* `infra/` — Dockerfiles, k8s manifests, CI pipelines

### Run locally (example)

1. Start backend services

```bash
cd backend
./mvnw spring-boot:run
```

2. Start ML service (Python virtualenv)

```bash
cd ml
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python app.py
```

3. Start frontend (PWA example)

```bash
cd frontend
npm install
npm run dev
```

> **Note:** The above commands are placeholders — update with your actual scripts and ports.

---

## Model Selection & Rationale

* Start with **Llama 3.2 (instruction-tuned)** mid-size (e.g., 3B–11B) for the MVP to balance latency, cost and capability.
* Use a 2-stage approach if needed: small model drafts the note, larger model verifies or enriches (or run a verification pass).
* For multimodal needs (images/scans), use Llama 3.2 vision variants only where necessary.

**Document your selection** in `docs/model-selection.md` including: latency targets, cost per 1k tokens, expected context window needs, and fallback plan.

---

## Privacy, Security & Compliance

* Obtain **explicit patient consent** per visit. Log consent with timestamp and clinician ID.
* **Minimize PHI** sent to models: scrub or pseudonymize where possible. Store PHI only in encrypted, access-controlled databases.
* If using cloud vendors, sign a **BAA** and enforce region-level controls.
* Maintain **immutable audit trails** for all edits and model outputs.
* Keep a human-in-the-loop clinician sign-off before notes go live in EHR.

---

## Evaluation & Metrics

Track these KPIs during pilot:

* **Time saved (min / encounter)** — primary metric
* **Edit rate** — % of AI notes requiring edits
* **Hallucination incidents** — number of incorrect claims that reached record
* **Clinician satisfaction (NPS)** — survey-based
* **Adoption** — % of invited clinicians who use Nocta regularly

Store evaluation datasets (anonymized) and an errors catalogue to inform improvements.

---

## Development & Contribution

**Branching model**: `main` (stable), `develop` (integration), feature branches `feature/<name>`.

**How to contribute:**

1. Fork & create a feature branch
2. Run tests and linters
3. Open a PR with description and testing steps

**Coding standards:**

* Java: follow Spring Boot conventions; use Checkstyle/SpotBugs
* Python: follow PEP8; run `pytest` for unit tests
* Frontend: follow chosen style guide (ESLint / Prettier)

---

## Deployment & Operations

* Build images: `docker build -t nocta-backend ./backend`
* Push to registry and deploy via Helm charts to Kubernetes (production)
* Use horizontal autoscaling and GPU/accelerator pools for inference nodes
* Set up monitoring dashboards and alerting for high hallucination or latency

---

## Troubleshooting & FAQs

**Q: What if the model hallucinates?**
A: Flagged notes should be returned for clinician edits and logged. If hallucination rate increases, fail-safe to smaller model, disable auto-push to EHR, and investigate.

**Q: How do we protect PHI in logs?**
A: Strip PHI from logs by design. If logging PHI for debugging, encrypt and restrict access strictly.

---

## License & Attribution

* Choose a license for your repo (e.g., `MIT`, `Apache-2.0`).
* You must **record which Llama model & version** you used and comply with its attribution and acceptable use policy. Add a `MODEL_CARD.md` with links and relevant license/attribution text.

---

## Next Steps & Suggested Files to Add

* `docs/model-selection.md` — documented selection and benchmarks
* `docs/clinical-safety-plan.md` — how hallucinations are handled
* `infra/helm/` — Helm charts for deployment
* `scripts/` — helper scripts for running local dev environment
* `CONTRIBUTING.md` and `CODE_OF_CONDUCT.md`

---

If you want, I can now:

* Generate a `MODEL_CARD.md` prefilled for Llama 3.2 usage,
* Add `docs/clinical-safety-plan.md`,
* Or customize the `Getting Started` scripts (`mvnw`, `docker-compose`, etc.) for your environment.

Tell me which of those I should add to the repository next and I will update the canvas with the new files.
