# REST API Guide

Base URL: `http://localhost:8080/api/v1`

## Health Checks

### System Health
```bash
GET /health
```

**Response:**
```json
{
  "status": "UP",
  "timestamp": "2024-12-09T10:30:00Z"
}
```

### LLAMA Health
```bash
GET /llama/health
```

**Response:**
```json
{
  "status": "connected",
  "provider": "groq",
  "model": "llama-3.3-70b-versatile"
}
```

## Patient Endpoints

### List All Patients
```bash
GET /patients
```

**Response:**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "firstName": "Sarah",
    "lastName": "Johnson",
    "dateOfBirth": "1985-03-15",
    "allergies": "Penicillin",
    "chronicConditions": "Hypertension, Type 2 Diabetes"
  }
]
```

### Get Patient by ID
```bash
GET /patients/{patientId}
```

### Get Patient Summary (AI-Powered)
```bash
GET /patients/{patientId}/summary
```

**Response:**
```json
{
  "patientId": "550e8400-e29b-41d4-a716-446655440001",
  "summary": "58-year-old female with well-controlled hypertension...",
  "keyAlerts": ["Penicillin allergy", "Diabetes monitoring required"],
  "lastVisit": "2024-11-15",
  "generatedAt": "2024-12-09T10:30:00Z"
}
```

## Consultation Endpoints

### Create Consultation
```bash
POST /consultations/upload-audio
Content-Type: application/json

{
  "patientId": "550e8400-e29b-41d4-a716-446655440001",
  "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
  "rawTranscript": "Patient reports severe headache for 3 days...",
  "vitalSigns": {
    "bloodPressure": "140/90",
    "heartRate": 78,
    "temperature": 37.2,
    "oxygenSaturation": 98,
    "respiratoryRate": 16,
    "weight": 75.5,
    "height": 165
  }
}
```

**Response:**
```json
{
  "consultationId": "123e4567-e89b-12d3-a456-426614174000",
  "status": "PROCESSING",
  "message": "Consultation created successfully"
}
```

### Get Consultation Status
```bash
GET /consultations/{consultationId}/status
```

**Response:**
```json
{
  "consultationId": "123e4567-e89b-12d3-a456-426614174000",
  "status": "COMPLETED",
  "currentStep": "COMPLIANCE_CHECK",
  "progress": 100
}
```

**Status Values:**
- `PROCESSING` - AI agents working
- `COMPLETED` - Ready for review
- `APPROVED` - Clinician approved
- `SYNCED` - Sent to EHR
- `FAILED` - Error occurred

### Get Consultation Details
```bash
GET /consultations/{consultationId}
```

**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "patientId": "550e8400-e29b-41d4-a716-446655440001",
  "status": "COMPLETED",
  "rawTranscript": "Patient reports...",
  "vitalSigns": {
    "bloodPressure": "140/90",
    "heartRate": 78
  },
  "generatedNote": {
    "soapNote": "S: Patient reports severe headache...",
    "icd10Codes": ["R51.9 - Headache"],
    "actionItems": ["Order CT scan", "Prescribe pain relief"],
    "complianceNotes": "No allergy conflicts detected"
  },
  "createdAt": "2024-12-09T10:00:00Z"
}
```

### Get Patient History
```bash
GET /consultations/patient/{patientId}/history
```

**Response:**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "date": "2024-12-09",
    "chiefComplaint": "Headache",
    "status": "APPROVED",
    "icd10Codes": ["R51.9"]
  }
]
```

### Approve Consultation
```bash
POST /consultations/{consultationId}/approve
Content-Type: application/json

{
  "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
  "approve": true
}
```

**Response:**
```json
{
  "status": "APPROVED",
  "syncedToEhr": true,
  "appointmentRecommendation": {
    "timeframe": "2 weeks",
    "reason": "Follow-up for headache management",
    "priority": "routine"
  },
  "emailSent": true
}
```

## Image Analysis Endpoint

### Analyze Medical Image
```bash
POST /image-analysis/analyze
Content-Type: application/json

{
  "imageBase64": "data:image/jpeg;base64,/9j/4AAQSkZJRg...",
  "imageType": "wound",
  "patientContext": "Diabetic patient with foot ulcer"
}
```

**Response:**
```json
{
  "analysis": "The image shows a stage 2 pressure ulcer...",
  "findings": [
    "Wound size approximately 3cm x 2cm",
    "Moderate exudate present",
    "No signs of infection"
  ],
  "recommendations": [
    "Daily dressing changes",
    "Monitor for infection",
    "Consider wound culture if no improvement"
  ],
  "confidence": 0.87
}
```

## Error Responses

All endpoints return standard error format:

```json
{
  "error": "Bad Request",
  "message": "Patient ID is required",
  "timestamp": "2024-12-09T10:30:00Z",
  "path": "/api/v1/consultations/upload-audio"
}
```

**HTTP Status Codes:**
- `200` - Success
- `201` - Created
- `400` - Bad Request
- `404` - Not Found
- `500` - Internal Server Error

## Rate Limits

- **Groq API:** 14,400 requests/day (free tier)
- **OpenRouter API:** Based on credits ($1 free = ~10,000 images)

## Authentication

Current MVP: No authentication required

Production: Add `Authorization: Bearer <token>` header

## CORS

Allowed origins:
- `http://localhost:*`
- `http://192.168.*.*:*`

## Postman Collection

Import `postman_collection.json` for pre-configured requests.

## Integration Examples

### JavaScript/Fetch
```javascript
const response = await fetch('http://localhost:8080/api/v1/patients', {
  method: 'GET',
  headers: { 'Content-Type': 'application/json' }
});
const patients = await response.json();
```

### cURL
```bash
curl -X POST http://localhost:8080/api/v1/consultations/upload-audio \
  -H "Content-Type: application/json" \
  -d '{"patientId":"550e8400-e29b-41d4-a716-446655440001","clinicianId":"550e8400-e29b-41d4-a716-446655440099","rawTranscript":"Patient reports headache"}'
```

### Python
```python
import requests

response = requests.get('http://localhost:8080/api/v1/patients')
patients = response.json()
```
