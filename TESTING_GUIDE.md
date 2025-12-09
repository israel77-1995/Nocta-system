# Testing Guide - Enhanced Features

## ✅ Backend Running
- Application started successfully
- Database migration V3 applied (vital signs columns added)
- Server: http://192.168.8.114:8080

---

## 🧪 Feature Testing Checklist

### 1. Vital Signs Capture ✨ NEW

**Steps:**
1. Open mobile app
2. Login with default clinician ID
3. Select a patient (John Doe or Jane Smith)
4. **NEW**: Fill in vital signs form:
   - BP: `140/90`
   - HR: `85`
   - Temp: `37.5`
   - O2: `98`
5. Type consultation notes
6. Submit consultation
7. Wait for AI processing
8. Verify SOAP note generated

**Expected Result:**
- Vital signs saved with consultation
- Visible in consultation details
- Included in patient history

**API Test:**
```bash
curl -X POST http://192.168.8.114:8080/api/v1/consultations/upload-audio \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "550e8400-e29b-41d4-a716-446655440001",
    "clinicianId": "550e8400-e29b-41d4-a716-446655440099",
    "rawTranscript": "Patient reports headache for 3 days",
    "vitalSigns": {
      "bloodPressure": "140/90",
      "heartRate": 85,
      "temperature": 37.5,
      "oxygenSaturation": 98
    }
  }'
```

---

### 2. Patient History View ✨ NEW

**Steps:**
1. Open mobile app
2. Login with default clinician ID
3. On patient selection screen, tap **"📋 History"** button next to patient name
4. **NEW**: View list of all past consultations
5. Tap on any consultation to view full details
6. Verify vital signs displayed
7. Verify SOAP notes shown
8. Tap back to return to history list

**Expected Result:**
- All consultations listed (newest first)
- Each shows: date, status, transcript preview, vital signs
- Tapping opens full consultation details
- Can navigate back to patient list

**API Test:**
```bash
# View John Doe's history
curl http://192.168.8.114:8080/api/v1/consultations/patient/550e8400-e29b-41d4-a716-446655440001/history

# View Jane Smith's history
curl http://192.168.8.114:8080/api/v1/consultations/patient/550e8400-e29b-41d4-a716-446655440002/history
```

---

### 3. Complete Workflow Test

**Scenario: New Patient Consultation with Full Data**

1. **Login**
   - Use default clinician ID
   - Verify login successful

2. **View Patient History** ✨ NEW
   - Select John Doe
   - Tap "📋 History" button
   - View past consultations
   - Note: Should see any previous test consultations

3. **New Consultation**
   - Tap back to patient list
   - Select John Doe (tap on card, not history button)
   - Fill vital signs: ✨ NEW
     - BP: `150/95`
     - HR: `92`
     - Temp: `38.1`
     - O2: `96`
   - Type notes: "Patient presents with persistent headache, photophobia, and nausea. History of hypertension. Elevated BP noted."
   - Submit consultation

4. **AI Processing**
   - Watch processing animation
   - Wait for completion (~5-10 seconds)

5. **Review Results**
   - Verify SOAP note generated
   - Check ICD-10 codes suggested
   - Review action items
   - Approve consultation

6. **Verify History** ✨ NEW
   - Go back to patient list
   - Tap "📋 History" for John Doe
   - Verify new consultation appears at top
   - Verify vital signs displayed correctly
   - Tap on consultation to view full details

---

## 🎯 UI Elements to Verify

### Patient Selection Screen
- ✅ Patient cards with avatar, name, allergies, chronic conditions
- ✨ NEW: "📋 History" button on each patient card
- ✅ Logout button in header

### Consultation Screen
- ✨ NEW: Vital Signs section with 4 input fields (BP, HR, Temp, O2)
- ✅ Record button (disabled on iOS with message)
- ✅ Consultation notes textarea
- ✅ Submit button (enabled when notes entered)

### History Screen ✨ NEW
- ✅ Patient name in header
- ✅ Back button to patient list
- ✅ Loading spinner while fetching
- ✅ List of consultations with:
  - Date/time
  - Status badge (APPROVED, READY, etc.)
  - Transcript preview
  - Vital signs (if captured)
- ✅ Tap consultation to view full details

### Results Screen
- ✅ SOAP note sections (S, O, A, P)
- ✅ ICD-10 codes
- ✅ Action items
- ✅ Approve & Sign button
- ✅ New Consultation button

---

## 🐛 Known Issues & Limitations

### Current Limitations:
1. **Voice Recording**: Disabled on iOS WebView (not supported)
   - Workaround: Type notes manually
   - Status: Expected behavior

2. **Vital Signs**: Optional fields
   - Can submit consultation without vital signs
   - Status: By design

3. **History Pagination**: Not implemented
   - All consultations loaded at once
   - May be slow with many consultations
   - Status: Future enhancement

### Browser Compatibility:
- ✅ Chrome/Android: Full support
- ✅ Safari/iOS: Full support (except voice recording)
- ✅ React Native WebView: Full support

---

## 📊 Database Verification

### Check Vital Signs Saved:
```bash
# Access H2 console
http://192.168.8.114:8080/h2-console

# JDBC URL: jdbc:h2:mem:clinicaldb
# Username: sa
# Password: (empty)

# Query consultations with vital signs
SELECT id, patient_id, blood_pressure, heart_rate, temperature, oxygen_saturation, created_at 
FROM consultations 
WHERE blood_pressure IS NOT NULL 
ORDER BY created_at DESC;
```

### Check Patient History:
```sql
SELECT c.id, c.patient_id, c.state, c.blood_pressure, c.heart_rate, c.created_at,
       p.first_name, p.last_name
FROM consultations c
JOIN patients p ON c.patient_id = p.id
WHERE c.patient_id = '550e8400-e29b-41d4-a716-446655440001'
ORDER BY c.created_at DESC;
```

---

## 🔄 Regression Testing

### Verify Existing Features Still Work:
1. ✅ Login/Logout
2. ✅ Patient selection
3. ✅ Consultation submission (without vital signs)
4. ✅ AI processing (all 4 agents)
5. ✅ SOAP note generation
6. ✅ ICD-10 coding
7. ✅ Action items
8. ✅ Consultation approval
9. ✅ Status polling

### Backward Compatibility:
- ✅ Old consultations (without vital signs) still display correctly
- ✅ API accepts requests without vitalSigns field
- ✅ History shows consultations with and without vital signs

---

## 📱 Mobile App Testing

### React Native App:
```bash
cd mobile-app
npm start
```

### Test on Device:
1. Scan QR code with Expo Go app
2. Test all features on physical device
3. Verify touch interactions
4. Check responsive layout
5. Test back button navigation

---

## 🚀 Performance Testing

### Load Testing:
```bash
# Create 10 consultations with vital signs
for i in {1..10}; do
  curl -X POST http://192.168.8.114:8080/api/v1/consultations/upload-audio \
    -H "Content-Type: application/json" \
    -d "{
      \"patientId\": \"550e8400-e29b-41d4-a716-446655440001\",
      \"clinicianId\": \"550e8400-e29b-41d4-a716-446655440099\",
      \"rawTranscript\": \"Test consultation $i\",
      \"vitalSigns\": {
        \"bloodPressure\": \"120/80\",
        \"heartRate\": 75,
        \"temperature\": 37.0,
        \"oxygenSaturation\": 98
      }
    }"
  sleep 2
done

# Then view history to verify all 10 appear
curl http://192.168.8.114:8080/api/v1/consultations/patient/550e8400-e29b-41d4-a716-446655440001/history
```

---

## ✅ Success Criteria

### Feature Complete When:
- [x] Vital signs form displays correctly
- [x] Vital signs save to database
- [x] History button appears on patient cards
- [x] History screen loads consultations
- [x] History displays vital signs
- [x] Tapping history item shows full details
- [x] All existing features still work
- [x] No console errors
- [x] Mobile responsive
- [x] API endpoints functional

---

## 🎉 Testing Complete!

Once all tests pass:
1. Document any bugs found
2. Create GitHub issues for enhancements
3. Update README with new features
4. Prepare demo for stakeholders

---

## 📞 Support

**Issues?**
- Check app.log for backend errors
- Check browser console for frontend errors
- Verify database migrations applied
- Restart backend if needed

**Quick Restart:**
```bash
cd /home/wtc/Nocta-system
pkill -f clinical-copilot
bash run-with-llama-api.sh > app.log 2>&1 &
```

---

*Testing Guide - Version 1.0*
*Last Updated: 2025-12-09*
