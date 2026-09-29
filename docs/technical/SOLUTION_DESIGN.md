# Clinical Copilot OS — Solution Design (Pilot)

> Design for the first real-world pilot. Anchored on two personas (private GP + public clinic
> nurse), best-case infrastructure, in-country data residency, EHR integration, English-first
> with a multilingual roadmap. This is a design document — it describes the target, not what is
> built today. See ENTERPRISE_ROADMAP.md for the gap from the current MVP.

---

## 1. Anchored Scope

| Constraint | Decision for the pilot |
|---|---|
| Primary users | **A: Private GP** + **B: Public clinic nurse** |
| Infrastructure | Best-case / properly provisioned (not offline-first yet) |
| Data residency | **All patient data stays in-country** (hard requirement) |
| EHR | **Integrate with each existing EHR** (we augment, not replace) |
| Language | **English v1**; architecture must not block adding all languages later |

Explicitly **out of scope for the pilot** (deferred, not forgotten): fully offline/edge operation
for field CHWs, on-device models, multi-country federation. The design leaves room for these.

---

## 2. The Personas & Their End-to-End Journeys

### Persona A — Dr. Naledi, private GP (connected, English)
1. Logs into the web app (or launches Copilot from *inside* her existing EHR).
2. Selects/loads the patient — patient context is pulled from her EHR via FHIR.
3. Consults; dictates or types the transcript (+ vitals).
4. 4-agent pipeline runs → SOAP note, ICD-10, action items, compliance check.
5. She reviews, edits, and **approves** (e-signature).
6. Approved note is **written back to her EHR** via FHIR; follow-up + patient summary generated.

### Persona B — Sister Thandi, public clinic nurse (connected pilot site, high volume)
1. Logs into the web/mobile app on a shared clinic device (strong auth, fast user switching).
2. Selects patient from the clinic/facility patient list (from the public HIS).
3. High-volume chronic-care visit (HIV/TB/diabetes/hypertension/maternal) — documents quickly.
4. Same pipeline; **Compliance Agent is critical here** (specialist oversight is scarce).
5. Reviews/approves; note written back to the public-sector record.
6. De-identified, aggregated signal contributes to population-health dashboard (governed).

**Shared core, different packaging:** same 4-agent engine; different tenancy, data-governance,
and (later) language packs.

---

## 3. Target Architecture (Pillars)

### 3.1 In-country regional deployment (data residency)
- One **region = one country**. All compute + data live inside that country's border
  (in-country cloud region or MoH-approved data centre).
- Patient data, audio, images, and audit logs never leave the region.
- This is the foundation the UN/MoH partnership depends on.

### 3.2 Multi-tenancy within a region
- **Tenant = Organization** (a private practice, a practice group, or a public facility/district).
- Strong isolation between tenants (row-level security keyed by tenant, or schema-per-tenant for
  larger tenants). Private (A) and public (B) tenants coexist in the same regional platform but
  are logically isolated; a large MoH deployment can be its own dedicated instance if required.

### 3.3 EHR Integration Service (the "integrate with each EHR" answer)
- **FHIR R4 is the primary contract** (read patient context in; write notes/observations back).
- **Adapter pattern per EHR** — mirrors the `LlamaAdapter` pattern you already use. You do NOT
  rebuild the app per EHR; you write a thin adapter that maps that EHR's API/format to our
  internal model. FHIR-native EHRs get the standard adapter; legacy ones get an HL7v2 or
  custom-API adapter.
- **SMART-on-FHIR app launch**: clinicians can launch Copilot from *inside* their EHR, already
  in the patient's context — the lowest-friction way to be "consumed."

### 3.4 AI tier (unchanged 4 agents, hardened + a key decision — see Section 5)
- The Perception → Documentation → Coordination → Compliance pipeline stays.
- Wrapped with resilience (retry/circuit-breaker), structured output handling, and a job queue.
- Compliance Agent is upgraded from advisory to **gating** (blocks/flags unsafe notes).

### 3.5 Identity, access, audit
- OIDC-based auth, RBAC (CLINICIAN / NURSE / ADMIN / INTEGRATION), per-tenant.
- Append-only audit trail for every access to and change of patient data (mandatory).

---

## 4. How It Is Consumed (Channels)

Ranked by friction (lowest = best adoption):

1. **Embedded in the EHR (SMART-on-FHIR launch)** — clinician clicks "Document with Copilot"
   inside their EHR; opens in patient context; note flows back. Best for Persona A.
2. **Web app** — standalone browser app; works for both personas; good for clinics on shared PCs.
3. **Mobile app** — the existing Expo app; good for Persona B on the move within a facility.
4. **REST API** — for EHR vendors / MoH systems to integrate Copilot directly into their own UI.

All four hit the same regional API. "Consumed" = the clinician gets the note where they already
work, with minimal new habits.

---

## 5. THE decision everything hinges on: LLM data residency

This is the crux and needs an explicit choice.

**The problem:** data residency says patient data stays in-country. But the current system sends
transcripts to **Groq (US)** for LLM inference. That is a cross-border transfer of PHI — it
contradicts the residency requirement, especially for the public/MoH (Persona B).

**Options:**

| Option | How | Fits A? | Fits B (sovereign)? |
|---|---|---|---|
| **1. External LLM API as-is** (Groq/OpenRouter) | Keep calling US API | Maybe, with DPA + patient consent | ✗ Usually not acceptable |
| **2. De-identification proxy** | Strip PHI before any external call, re-identify locally | Yes | Sometimes — depends on regulator |
| **3. In-region hosted open model** | Self-host LLaMA (or similar) on in-country GPUs | Yes | ✓ Cleanest for sovereignty |
| **4. Hybrid** | Private tenants use API (opt-in); public tenants use in-region model | Yes | ✓ |

**Recommendation:** design for **Option 4 (hybrid)** from day one. Your adapter pattern already
supports this — private (A) tenants can use the fast external API (with consent + DPA); public
(B) / sovereign tenants are pointed at an in-region self-hosted model. Same code, config-switched
per tenant/region. This is the single most important design decision for the UN story.

*Needs confirmation:* the exact regulatory line (POPIA + MoH policy) on whether de-identified
external inference is ever acceptable, vs. requiring fully in-region models. Get legal input early.

---

## 6. Multilingual Roadmap (English now, all later)

Design so language is a **pluggable layer**, not hardcoded:
- **Prompts**: prompt templates become language-keyed (`prompts/{lang}/*.prompt.txt`).
- **Transcription**: the speech-to-text provider must support the target languages (this, not the
  LLM, is often the harder constraint for African languages).
- **UI**: standard i18n resource bundles from the start (even if only `en` is populated).
- **Model choice**: some languages may need a different or fine-tuned model — the adapter pattern
  already lets you route per language.
- **Clinical safety per language**: each new language needs its own validation before go-live.

Do the *plumbing* for i18n now (cheap); populate languages later (expensive, needs validation).

---

## 7. Data Flow (happy path, Persona A)

```
Clinician (EHR/web/mobile)
   → API Gateway (authN/Z, rate limit)
   → API: create consultation (tenant-scoped)
   → Orchestrator (async, queued)
        → Perception  → Documentation → Coordination → Compliance(GATES)
        → LLM tier (in-region model OR external API, per tenant)
   → Persist note + audit (in-country DB)
   → Clinician reviews + approves (e-sign)
   → Integration Service writes note back to EHR (FHIR)
   → De-identified signal → population-health store (governed, Persona B)
```

---

## 8. From Current MVP → This Design (what changes)

| Area | Today | Pilot design |
|---|---|---|
| Auth | None (public) | OIDC + RBAC per tenant |
| DB | In-memory H2 (even in "prod") | In-country PostgreSQL, encrypted |
| Tenancy | None | Organization/tenant isolation |
| EHR sync | `simulateEhrSync()` stub | Real FHIR integration service + adapters |
| Compliance | Logged only | Gates/blocks unsafe notes |
| LLM | Groq US, no resilience | Hybrid in-region/external, resilient |
| Data residency | N/A | Hard in-country boundary |
| Language | English hardcoded | i18n plumbing in place, en populated |
| Observability | Minimal | Metrics, tracing, real health checks, audit |

---

## 9. Working Assumptions (locked for planning; revisit before go-live)

These were open questions; for planning momentum we proceed on these assumptions. Each must be
confirmed with the clinical/legal/MoH partners before production go-live.

1. **LLM sovereignty → Option 4 (Hybrid).** Private (A) tenants may use the external LLM API with
   patient consent + a data-processing agreement; public/MoH (B) tenants are routed to an
   **in-region self-hosted model**. Switched per tenant via the existing adapter pattern.
2. **Pilot region → a single in-country region** (e.g., South Africa) hosted in an in-country
   cloud region or MoH-approved data centre. Multi-country is out of scope for the pilot.
3. **EHR → assume a FHIR R4-capable EHR first.** Build the standard FHIR adapter first; legacy
   (HL7v2/custom) adapters follow per pilot site.
4. **Persona B clinical anchor → NCD chronic care (diabetes + hypertension).** Matches existing
   sample patients and the largest chronic-care burden. Maternal health is the fast-follow.
5. **Hosting → best-case provisioned** in-country infrastructure for the pilot (not offline/edge).
6. **Speech-to-text → paste-in / typed transcript for the pilot**; ambient (live) transcription is
   a fast-follow, English first.

### Still genuinely open (need partner input, tracked but not blocking design)
- Exact POPIA + MoH position on de-identified external inference (affects whether Option 4 needs
  hardening toward full in-region for A as well).
- The specific pilot EHR product(s) → which concrete adapter to implement first.
- The specific pilot country/region → concrete data-centre / cloud-region choice.

---

## Related Documents
- `ENTERPRISE_ROADMAP.md` — hardening roadmap & the current-state gap
- `UN_PARTNERSHIP_AND_SDG.md` — impact model & SDG alignment
- `BUSINESS_MODEL.md`, `GO_TO_MARKET.md` — commercial model
- `ARCHITECTURE.md` — current architecture
