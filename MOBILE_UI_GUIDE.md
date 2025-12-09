# Mobile UI Navigation Guide

## 🏠 Dashboard (Landing Page)

**After Login, you see:**

### Feature Cards (2x2 Grid):
1. **New Consultation** 👤
   - Start a new patient consultation
   - Tap → Patient Selection Screen

2. **Patient History** 📋
   - View all patient records
   - Tap → Patient Selection Screen

3. **Analytics** 📊
   - Coming soon: View insights & metrics
   - Currently shows "Coming soon" alert

4. **Settings** ⚙️
   - Coming soon: Configure preferences
   - Currently shows "Coming soon" alert

### Quick Stats:
- **Today's Consultations**: Count of today's visits
- **Pending Approval**: Consultations awaiting review

### Header:
- Clinical Copilot logo
- Clinician name/ID
- Logout button (top right)

---

## 📱 Screen Flow

```
Login Screen
    ↓
Dashboard (Home)
    ↓
    ├─→ Patient Selection
    │       ↓
    │       ├─→ New Consultation
    │       │       ↓
    │       │   Vital Signs + Notes
    │       │       ↓
    │       │   AI Processing
    │       │       ↓
    │       │   SOAP Note Results
    │       │       ↓
    │       │   Approve & Sign
    │       │
    │       └─→ Patient History
    │               ↓
    │           Consultation List
    │               ↓
    │           View Details
    │
    └─→ (Future: Analytics, Settings)
```

---

## 🎯 Key Features

### 1. Dashboard Navigation
- **Back to Dashboard**: Tap back arrow on any screen
- **Logout**: Tap logout icon (top right)
- **Quick Access**: Tap any feature card

### 2. Patient Selection
- **Select Patient**: Tap patient card
- **View History**: Tap "📋 History" button
- **Back**: Tap back arrow → Dashboard

### 3. New Consultation
- **Vital Signs**: Fill BP, HR, Temp, O2 (optional)
- **Notes**: Type or record consultation
- **Submit**: Tap "Generate SOAP Note"
- **Back**: Tap back arrow → Patient Selection

### 4. Patient History
- **View All**: Shows all consultations (newest first)
- **Details**: Tap any consultation
- **Info Shown**: Date, status, transcript, vital signs
- **Back**: Tap back arrow → Patient Selection

### 5. Results Screen
- **SOAP Note**: View all sections (S, O, A, P)
- **ICD-10 Codes**: Suggested diagnoses
- **Action Items**: Prescriptions, labs, referrals
- **Approve**: Tap "Approve & Sign"
- **New**: Tap "New Consultation" → Dashboard

---

## 🎨 Visual Design

### Color Scheme:
- **Primary**: Purple gradient (#667EEA → #764BA2)
- **Success**: Green (#22C55E)
- **Warning**: Amber (#F59E0B)
- **Info**: Blue (#4F46E5)

### Card Types:
- **Feature Cards**: Large, colorful icons with descriptions
- **Patient Cards**: Avatar + info + history button
- **History Items**: Date, status badge, preview
- **Stat Cards**: Large numbers with labels

### Interactions:
- **Tap**: All cards are tappable
- **Active State**: Cards scale down slightly
- **Hover**: Border highlights (desktop)
- **Loading**: Spinner with animation

---

## 📋 Screen Details

### Dashboard Screen
**Elements:**
- Header with gradient background
- 4 feature cards in 2x2 grid
- 2 stat cards at bottom
- Logout button

**Actions:**
- Tap "New Consultation" → Patient Selection
- Tap "Patient History" → Patient Selection
- Tap "Analytics" → Coming soon alert
- Tap "Settings" → Coming soon alert
- Tap Logout → Login Screen

---

### Patient Selection Screen
**Elements:**
- Back button (to Dashboard)
- "Select Patient" title
- Logout button
- Patient cards with:
  - Avatar (initials)
  - Name
  - Allergies
  - Chronic conditions
  - "📋 History" button

**Actions:**
- Tap patient card → Consultation Screen
- Tap "📋 History" → History Screen
- Tap back → Dashboard
- Tap logout → Login Screen

---

### Consultation Screen
**Elements:**
- Back button (to Patient Selection)
- Patient name in header
- **Vital Signs section** (NEW):
  - BP input
  - HR input
  - Temp input
  - O2 input
- Record button (disabled on iOS)
- Consultation notes textarea
- Submit button

**Actions:**
- Fill vital signs (optional)
- Type or record notes
- Tap Submit → Processing → Results
- Tap back → Patient Selection

---

### History Screen (NEW)
**Elements:**
- Back button (to Patient Selection)
- Patient name + "History" in header
- Loading spinner (while fetching)
- List of consultations:
  - Date/time
  - Status badge (color-coded)
  - Transcript preview (100 chars)
  - Vital signs (if captured)

**Actions:**
- Tap consultation → View full details
- Tap back → Patient Selection

---

### Results Screen
**Elements:**
- Back button (to Consultation)
- "SOAP Note" title
- Loading animation (during AI processing)
- SOAP sections:
  - Subjective
  - Objective
  - Assessment
  - Plan
- ICD-10 codes section
- Action items section
- Two buttons:
  - "New Consultation"
  - "Approve & Sign"

**Actions:**
- Review SOAP note
- Tap "Approve & Sign" → Success animation → Dashboard
- Tap "New Consultation" → Dashboard

---

## 🔄 Navigation Patterns

### Primary Navigation:
```
Dashboard ←→ Patient Selection ←→ Consultation/History
```

### Back Button Behavior:
- **Patient Selection** → Dashboard
- **Consultation** → Patient Selection
- **History** → Patient Selection
- **Results** → Consultation (or Dashboard after approval)

### Logout:
- Available from Dashboard and Patient Selection
- Returns to Login Screen
- Clears all session data

---

## ✨ New Features Highlighted

### 1. Dashboard Landing Page
- **Before**: Went straight to patient selection
- **Now**: Professional dashboard with feature cards
- **Benefit**: Clear navigation, room for growth

### 2. Vital Signs Capture
- **Location**: Consultation Screen
- **Fields**: BP, HR, Temp, O2
- **Display**: Shows in history and results
- **Optional**: Can submit without them

### 3. Patient History View
- **Access**: "📋 History" button on patient cards
- **Shows**: All past consultations
- **Details**: Date, status, vitals, transcript
- **Interactive**: Tap to view full consultation

---

## 🎯 User Workflows

### Quick Consultation:
1. Login
2. Dashboard → "New Consultation"
3. Select patient
4. Type notes
5. Submit
6. Approve
7. Done!

### Review Patient History:
1. Login
2. Dashboard → "Patient History"
3. Select patient
4. Tap "📋 History"
5. Browse consultations
6. Tap to view details

### Full Consultation with Vitals:
1. Login
2. Dashboard → "New Consultation"
3. Select patient
4. Fill vital signs (BP, HR, Temp, O2)
5. Type consultation notes
6. Submit
7. Review SOAP note
8. Approve & Sign
9. View in history to verify

---

## 📱 Mobile Optimization

### Touch Targets:
- All buttons ≥ 44px (Apple guideline)
- Cards have generous padding
- Tap areas extend beyond visible elements

### Responsive Design:
- 2x2 grid on normal screens
- 1 column on small screens (<360px)
- Flexible layouts adapt to screen size

### Performance:
- Lazy loading for history
- Optimized animations
- Minimal re-renders

---

## 🐛 Troubleshooting

### Dashboard not showing?
- Clear browser cache
- Check version: Should be v=13
- Verify login successful

### History button not working?
- Check console for errors
- Verify API endpoint accessible
- Try refreshing page

### Vital signs not saving?
- Check network tab for API call
- Verify payload includes vitalSigns
- Check backend logs

---

## 🚀 Future Enhancements

### Coming Soon:
- [ ] Analytics dashboard (real data)
- [ ] Settings screen
- [ ] Search patients
- [ ] Filter history by date
- [ ] Export SOAP notes to PDF
- [ ] Push notifications
- [ ] Offline mode

---

*Mobile UI Guide - Version 1.0*
*Last Updated: 2025-12-09*
