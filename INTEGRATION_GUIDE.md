# Frontend-Backend Integration Guide

This guide explains how the Clinical Copilot frontend communicates with the backend API.

## Architecture Overview

```
┌─────────────────────────────────────────────────┐
│                   Frontend (React/Expo)         │
│  - mobile.html (Web UI)                         │
│  - mobile.js (API Integration)                  │
│  - mobile.css (Styling)                         │
└────────────┬────────────────────────────────────┘
             │ HTTP/REST API
             │ (localhost:8080/api/v1)
             ↓
┌─────────────────────────────────────────────────┐
│         Backend (Spring Boot Java)              │
│  - ConsultationController (REST endpoints)      │
│  - ConsultationOrchestrator (Business logic)    │
│  - Services (Documentation, Compliance, etc)    │
└────────────┬────────────────────────────────────┘
             │ HTTP Calls
             │ (localhost:5000)
             ↓
┌─────────────────────────────────────────────────┐
│          LLAMA Server (AI Processing)           │
│  - Model Inference                              │
│  - Clinical Analysis                            │
└─────────────────────────────────────────────────┘
```

## API Endpoints

### Base URL
```
http://localhost:8080/api/v1
```

### Endpoints

#### 1. Upload Consultation (Start Processing)
```
POST /consultations/upload-audio
Content-Type: application/json

Request:
{
  "patientId": "550e8400-e29b-41d4-a716-446655440001",
  "clinicianId": "660e8400-e29b-41d4-a716-446655440000",
  "rawTranscript": "Patient reports severe headache...",
  "audioUrl": null  // optional
}

Response:
{
  "id": "consultation-uuid",
  "state": "QUEUED"
}
```

#### 2. Get Consultation Status
```
GET /consultations/{id}/status

Response:
{
  "id": "consultation-uuid",
  "state": "PROCESSING",  // QUEUED, PROCESSING, READY, APPROVED, SYNCED, ERROR
  "errorMessage": null
}
```

#### 3. Get Consultation Details
```
GET /consultations/{id}

Response:
{
  "id": "consultation-uuid",
  "patientId": "550e8400-e29b-41d4-a716-446655440001",
  "clinicianId": "660e8400-e29b-41d4-a716-446655440000",
  "state": "READY",
  "rawTranscript": "Patient reports...",
  "generatedNoteId": "note-uuid",
  "generatedNote": {
    "id": "note-uuid",
    "soapAssessment": "Patient presents with...",
    "soapSubjective": "...",
    "soapObjective": "...",
    "soapPlan": "...",
    "icd10Codes": "G43.0: Migraine without aura",
    "suggestedActions": "[{\"id\":\"a1\",\"type\":\"PRESCRIPTION\",...}]",
    "confidence": 0.85
  }
}
```

#### 4. Approve Consultation
```
POST /consultations/{id}/approve
Content-Type: application/json

Request:
{
  "clinicianId": "660e8400-e29b-41d4-a716-446655440000",
  "approve": true,
  "notes": "Approved with minor modifications"  // optional
}

Response:
{
  "success": true,
  "message": "Consultation approved and synced"
}
```

## Frontend Implementation

### API Helper Function

```javascript
const API_BASE_URL = window.location.origin + '/api/v1';

async function apiCall(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    const response = await fetch(url, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...options.headers
        }
    });
    
    if (!response.ok) {
        const error = await response.json().catch(() => ({ message: 'Unknown error' }));
        throw new Error(error.message || `HTTP ${response.status}`);
    }
    
    return response.json();
}
```

### Usage Example

```javascript
// Submit a consultation
async function submitConsultation() {
    const transcript = document.getElementById('transcript').value;
    
    const data = await apiCall('/consultations/upload-audio', {
        method: 'POST',
        body: JSON.stringify({
            patientId: currentPatient,
            clinicianId: currentClinician,
            rawTranscript: transcript
        })
    });
    
    consultationId = data.id;
    await pollStatus(data.id);
}

// Poll for status
async function pollStatus(id) {
    const maxAttempts = 30;
    
    for (let attempt = 0; attempt < maxAttempts; attempt++) {
        const data = await apiCall(`/consultations/${id}/status`);
        
        if (data.state === 'READY') {
            const details = await apiCall(`/consultations/${id}`);
            displayResults(details);
            return;
        }
        
        if (data.state === 'ERROR') {
            throw new Error(data.errorMessage);
        }
        
        // Wait 1 second before next poll
        await new Promise(r => setTimeout(r, 1000));
    }
    
    throw new Error('Processing timeout');
}
```

## Data Flow

### Step 1: Submission
1. User enters consultation notes in frontend
2. Frontend calls `POST /consultations/upload-audio`
3. Backend creates consultation with state `QUEUED`
4. Backend returns consultation ID

### Step 2: Processing
1. Frontend polls `GET /consultations/{id}/status`
2. Backend processes consultation asynchronously:
   - Calls PerceptionService (analyzes symptoms)
   - Calls DocumentationService (generates SOAP)
   - Calls CoordinationService (generates actions)
   - Calls ComplianceService (validates)
3. Backend updates consultation state: `QUEUED` → `PROCESSING` → `READY`

### Step 3: Display Results
1. Frontend detects state = `READY`
2. Frontend calls `GET /consultations/{id}`
3. Frontend displays SOAP note, ICD-10 codes, action items

### Step 4: Approval
1. Clinician reviews results
2. Frontend calls `POST /consultations/{id}/approve`
3. Backend syncs to EHR system
4. Consultation state: `APPROVED` → `SYNCED`

## Error Handling

The frontend should handle these error scenarios:

```javascript
async function handleErrors() {
    try {
        await apiCall('/consultations/upload-audio', { ... });
    } catch (error) {
        if (error.message.includes('HTTP 400')) {
            // Invalid request (missing fields)
            alert('Please fill in all required fields');
        } else if (error.message.includes('HTTP 404')) {
            // Patient not found
            alert('Patient not found in system');
        } else if (error.message.includes('HTTP 500')) {
            // Server error
            alert('Server error. Please try again later.');
        } else {
            // Network or LLAMA error
            alert('Error: ' + error.message);
        }
    }
}
```

## Testing the Integration

### Test 1: Basic API Call
```bash
curl http://localhost:8080/api/v1/consultations/upload-audio \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "550e8400-e29b-41d4-a716-446655440001",
    "clinicianId": "660e8400-e29b-41d4-a716-446655440000",
    "rawTranscript": "Patient has headache"
  }'
```

### Test 2: Check Status
```bash
# Replace {id} with consultation ID from above
curl http://localhost:8080/api/v1/consultations/{id}/status
```

### Test 3: Get Full Details
```bash
curl http://localhost:8080/api/v1/consultations/{id}
```

### Test 4: Frontend Integration Test
1. Open http://localhost:8080/mobile.html
2. Click "Start Session"
3. Select a patient
4. Enter consultation notes
5. Click "Generate SOAP Note"
6. Wait for processing to complete
7. Review generated SOAP note

## Debugging

### Frontend Console
Open browser DevTools (F12) to see:
- API calls and responses
- Error messages
- Network timing

### Backend Logs
```bash
# View backend logs
tail -f /home/wtc/Nocta-system/backend.log

# Look for:
# - API endpoint hits
# - Processing status changes
# - Error exceptions
```

### LLAMA Server Logs
```bash
# View LLAMA logs
docker logs <llama-container-id>

# Or if running locally:
# Check terminal where server is running
```

## Performance Considerations

- **API Timeout**: 30 seconds for consultation processing
- **Polling Interval**: 1 second (adjustable)
- **Max Concurrent**: No limit (consider adding in production)
- **Database**: H2 (in-memory) for development, PostgreSQL for production

## Security

Current setup (Development):
- ✅ CORS enabled for localhost
- ⚠️ No authentication (add for production)
- ⚠️ HTTP only (use HTTPS in production)
- ⚠️ Security headers not configured

### Production Security Checklist
- [ ] Enable HTTPS/TLS
- [ ] Implement OAuth2/JWT authentication
- [ ] Add CORS restrictions
- [ ] Rate limiting
- [ ] Input validation
- [ ] Output escaping
- [ ] Database encryption
- [ ] HIPAA compliance (if handling real patient data)

See `SECURITY.md` for detailed security setup.

## Troubleshooting

### Frontend Can't Reach Backend
```javascript
// Check if backend is accessible
fetch('http://localhost:8080/api/v1/health')
  .then(r => r.json())
  .then(d => console.log('Backend status:', d))
  .catch(e => console.error('Backend unreachable:', e))
```

### API Returns 404
- Ensure backend is running: `ps aux | grep clinical-copilot`
- Check port 8080 is open: `lsof -i :8080`
- Verify API path in frontend matches backend routes

### Consultation Stuck in PROCESSING
- Check if LLAMA server is running: `curl http://localhost:5000/health`
- Check backend logs for errors
- Increase polling timeout if LLAMA is slow

### LLAMA Returns Empty Response
- Verify model is loaded correctly
- Check model file size (should be 4GB+)
- Check available RAM
- Try with smaller model for testing

## Next Steps

1. Test full integration (see Testing section above)
2. Set up LLAMA server (see `LLAMA_SETUP.md`)
3. Deploy to production (see `DEPLOYMENT.md`)
4. Implement authentication (see `SECURITY.md`)
5. Configure production database (see `README.md`)
