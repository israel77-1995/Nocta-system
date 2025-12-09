# Clinical Copilot - User Stories & Flow

## 🎯 Core User Story

**As a clinician**, I want to document patient consultations efficiently while maintaining complete patient context, so that I can provide better continuity of care and spend more time with patients.

---

## 📖 Primary User Journey

### Story 1: New Patient Consultation

**Scenario**: Dr. Smith sees John Doe for a follow-up visit

**Flow**:
1. **Login** → Dashboard
2. **See Patient Card** with:
   - Patient name & demographics
   - ⚠️ Allergy alerts (Penicillin)
   - 💊 Chronic conditions (Hypertension)
   - Two action buttons: "➕ New Visit" | "📋 History"

3. **Tap "➕ New Visit"** → Consultation Screen
4. **See Patient Context** (auto-loaded):
   - Allergy warnings
   - Chronic conditions
   - Last visit summary
   - Link to full history

5. **Capture Vital Signs**:
   - BP: 150/95
   - HR: 92
   - Temp: 37.2
   - O2: 98%

6. **Document Consultation**:
   - Type or record notes
   - System remembers patient context

7. **Submit** → AI Processing (4 agents)
8. **Review SOAP Note**:
   - Subjective, Objective, Assessment, Plan
   - ICD-10 codes
   - Action items (prescriptions, labs, referrals)

9. **Approve & Sign**
10. **Choose Next Action**:
    - ➕ New Visit - Same Patient
    - 📋 View Patient History
    - 🏠 Back to Dashboard

---

## 🔄 Patient-Centric Flow

### Key Principle: Everything Revolves Around the Patient

```
Dashboard (Patient List)
    ↓
Select Patient → Patient Context Loaded
    ↓
    ├─→ New Visit
    │   ├─ See: Allergies, Chronic Conditions, Last Visit
    │   ├─ Capture: Vital Signs
    │   ├─ Document: Consultation Notes
    │   ├─ Review: AI-Generated SOAP Note
    │   └─ Approve → Next Steps (Stay with Patient)
    │
    └─→ View History
        ├─ See: All Past Consultations
        ├─ Filter: By date, status
        ├─ View: Full consultation details
        └─ Action: Start new visit from history
```

---

## 📋 Detailed User Stories

### Story 2: Reviewing Patient History Before Consultation

**As a clinician**, I want to review a patient's history before starting a new consultation, so that I have full context of their medical journey.

**Acceptance Criteria**:
- ✅ Can access history from dashboard
- ✅ See all past consultations chronologically
- ✅ View vital signs trends
- ✅ Read previous SOAP notes
- ✅ Start new consultation from history view

**Flow**:
1. Dashboard → Patient Card
2. Tap "📋 History"
3. See list of all consultations:
   - Date & time
   - Status (Approved, Pending, etc.)
   - Vital signs
   - Transcript preview
4. Tap any consultation → Full details
5. From details → "➕ New Visit" button

---

### Story 3: Continuity of Care - Same Patient Multiple Visits

**As a clinician**, I want to easily document multiple visits for the same patient in one session, so that I can efficiently handle follow-ups.

**Acceptance Criteria**:
- ✅ After approving consultation, stay in patient context
- ✅ Quick action to start new visit for same patient
- ✅ Patient context persists across visits
- ✅ Can view history between visits

**Flow**:
1. Complete consultation for John Doe
2. Approve & Sign
3. See "Next Steps" options:
   - **➕ New Visit - Same Patient** (highlighted)
   - 📋 View Patient History
   - 🏠 Back to Dashboard
4. Tap "New Visit" → Immediately start new consultation
5. Patient context auto-loaded
6. Vital signs form cleared
7. Ready for next visit

---

### Story 4: Safety Alerts & Clinical Decision Support

**As a clinician**, I want to see critical patient information (allergies, chronic conditions) at all times during consultation, so that I can make safe prescribing decisions.

**Acceptance Criteria**:
- ✅ Allergy alerts visible on dashboard
- ✅ Allergy alerts visible during consultation
- ✅ Chronic conditions highlighted
- ✅ Last visit summary shown
- ✅ Warnings persist throughout session

**Flow**:
1. Select patient → See allergy badge (⚠️ Penicillin)
2. Start consultation → Patient context box shows:
   ```
   ⚠️ Allergies: Penicillin
   💊 Chronic: Hypertension
   Last Visit: 2025-12-01
   "Patient reported headache, BP was 145/90..."
   ```
3. Context stays visible while documenting
4. AI compliance agent checks prescriptions against allergies
5. Warnings shown if conflicts detected

---

### Story 5: Efficient Workflow - Minimal Clicks

**As a clinician**, I want to complete a consultation with minimal navigation, so that I can see more patients per day.

**Acceptance Criteria**:
- ✅ Dashboard → Patient → Consultation (2 taps)
- ✅ All patient info visible without scrolling
- ✅ Vital signs + notes on same screen
- ✅ One-tap approval
- ✅ Quick return to dashboard

**Flow**:
1. **Tap 1**: Patient card "➕ New Visit"
2. **Tap 2**: Fill vitals (optional)
3. **Type**: Consultation notes
4. **Tap 3**: Submit
5. **Wait**: AI processing (~5-10 seconds)
6. **Review**: SOAP note
7. **Tap 4**: Approve & Sign
8. **Tap 5**: Back to Dashboard

**Total**: 5 taps, 1 type, 1 wait = ~2 minutes

---

## 🎨 UI/UX Principles

### 1. Patient Context is King
- Patient info always visible
- Allergies & chronic conditions highlighted
- Last visit summary shown
- History accessible with one tap

### 2. Progressive Disclosure
- Dashboard: High-level patient cards
- Consultation: Detailed context + capture
- Results: Full SOAP note + actions
- History: Chronological timeline

### 3. Action-Oriented Design
- Clear action buttons: "➕ New Visit" | "📋 History"
- Next steps after approval
- No dead ends - always show what's next

### 4. Safety First
- Allergy warnings in red
- Chronic conditions in blue
- Context box in yellow (attention)
- Compliance checks before approval

---

## 📊 Data Flow & Session Management

### Patient Session Lifecycle

```
1. SELECT PATIENT
   ↓
   currentPatient = patientId
   currentPatientName = patientName
   
2. LOAD CONTEXT
   ↓
   - Fetch patient demographics
   - Fetch allergy list
   - Fetch chronic conditions
   - Fetch last consultation
   - Fetch consultation history
   
3. CONSULTATION
   ↓
   - Capture vital signs
   - Document notes
   - Submit to AI
   
4. AI PROCESSING
   ↓
   - Perception Agent (extract facts)
   - Documentation Agent (generate SOAP)
   - Coordination Agent (create actions)
   - Compliance Agent (validate safety)
   
5. REVIEW & APPROVE
   ↓
   - Display SOAP note
   - Show ICD-10 codes
   - List action items
   - Clinician approves
   
6. POST-APPROVAL
   ↓
   - Save to database
   - Sync to EHR (simulated)
   - Update patient history
   - Show next steps
   
7. NEXT ACTION
   ↓
   - New visit (same patient) → Back to step 3
   - View history → Show all consultations
   - Back to dashboard → Back to step 1
```

---

## 🔐 Data Persistence & Continuity

### What Gets Saved:
- ✅ Patient demographics
- ✅ Consultation transcript
- ✅ Vital signs
- ✅ Generated SOAP note
- ✅ ICD-10 codes
- ✅ Action items
- ✅ Approval status
- ✅ Timestamp
- ✅ Clinician ID

### What Gets Displayed:
- **Dashboard**: Patient cards with alerts
- **Consultation**: Patient context + last visit
- **History**: All consultations chronologically
- **Results**: Full SOAP note + codes + actions

### Session Variables:
```javascript
currentClinician = "uuid"  // Set at login
currentPatient = "uuid"    // Set when patient selected
currentPatientName = "name" // For display
consultationId = "uuid"    // Set after submission
```

---

## 🎯 Success Metrics

### User Story Validation:
- ✅ Can complete consultation in < 2 minutes
- ✅ Patient context visible at all times
- ✅ History accessible with 1 tap
- ✅ Multiple visits for same patient without re-selecting
- ✅ Safety alerts always visible
- ✅ Clear next steps after every action

### Clinical Workflow:
- ✅ Reduced documentation time (70%)
- ✅ Improved continuity of care
- ✅ Better allergy awareness
- ✅ Faster patient throughput
- ✅ Higher clinician satisfaction

---

## 🚀 Future User Stories

### Story 6: Smart Suggestions Based on History
**As a clinician**, I want the system to suggest relevant information from past visits, so that I can quickly reference previous treatments.

### Story 7: Multi-Patient Session Management
**As a clinician**, I want to switch between patients without losing context, so that I can handle interruptions efficiently.

### Story 8: Collaborative Care
**As a clinician**, I want to share patient consultations with specialists, so that we can coordinate care effectively.

---

## 📱 Mobile-First Design

### Touch Targets:
- All buttons ≥ 44px
- Patient cards: Full width, easy to tap
- Action buttons: Large, clearly labeled
- Context box: Collapsible on small screens

### Gestures:
- Tap: Select patient, start consultation
- Swipe: Navigate history (future)
- Long press: Quick actions (future)

---

*User Stories Document - Version 1.0*
*Last Updated: 2025-12-09*
