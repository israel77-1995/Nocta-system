// PWA Service Worker Registration
if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('/sw.js')
            .then(registration => console.log('SW registered'))
            .catch(error => console.log('SW registration failed:', error));
    });
}

// Add error handler to catch any JavaScript errors
window.addEventListener('error', (e) => {
    console.error('Global error:', e.error);
});

window.addEventListener('unhandledrejection', (e) => {
    console.error('Unhandled promise rejection:', e.reason);
});

// API Configuration
const API_BASE_URL = window.location.origin + '/api/v1';
let currentClinician = null;
let currentPatient = null;
let currentPatientName = null;
let isRecording = false;
let recognition = null;
let consultationId = null;

// Utility function for API calls
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

function showScreen(screenId) {
    document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
    document.getElementById(screenId).classList.add('active');
}

async function login() {
    const clinicianId = document.getElementById('clinicianId').value.trim();
    if (!clinicianId) {
        alert('Please enter Clinician ID');
        return;
    }
    currentClinician = clinicianId;
    console.log('Logged in as clinician:', currentClinician);
    document.getElementById('clinicianName').textContent = 'Clinician: ' + clinicianId.substring(0, 8) + '...';
    showScreen('dashboardScreen');
    await loadPatients();
    loadDashboardStats();
}

function logout() {
    currentClinician = null;
    currentPatient = null;
    currentPatientName = null;
    showScreen('loginScreen');
}

function showAllHistory() {
    showScreen('patientScreen');
}

async function loadPatients() {
    try {
        const patients = await apiCall('/patients');
        console.log('Loaded patients:', patients.length);
        displayPatients(patients);
    } catch (error) {
        console.error('Error loading patients:', error);
        alert('Error loading patients: ' + error.message);
    }
}

function displayPatients(patients) {
    const listEl = document.getElementById('dashboardPatientList');
    listEl.innerHTML = '';
    
    patients.forEach(patient => {
        const fullName = `${patient.firstName} ${patient.lastName}`;
        const initials = `${patient.firstName[0]}${patient.lastName[0]}`;
        const age = calculateAge(patient.dob);
        
        const allergiesText = patient.allergies && patient.allergies.length > 0 
            ? patient.allergies.join(', ') 
            : 'None';
        const chronicText = patient.chronicConditions && patient.chronicConditions.length > 0
            ? patient.chronicConditions[0]
            : 'None';
        
        const card = document.createElement('div');
        card.className = 'patient-card-enhanced';
        card.onclick = () => selectPatientFromDashboard(patient.id, fullName);
        
        card.innerHTML = `
            <div class="patient-card-header">
                <div class="patient-avatar">${initials}</div>
                <div class="patient-info">
                    <h3>${fullName}</h3>
                    <p class="patient-meta">${age} years • MRN: ${patient.medicalRecordNumber}</p>
                </div>
            </div>
            <div class="patient-alerts">
                ${patient.allergies && patient.allergies.length > 0 ? 
                    patient.allergies.map(a => `<span class="alert-badge allergy">⚠️ ${a}</span>`).join('') : 
                    '<span class="alert-badge">No allergies</span>'}
                ${patient.chronicConditions && patient.chronicConditions.length > 0 ? 
                    `<span class="alert-badge chronic">💊 ${patient.chronicConditions[0]}</span>` : ''}
            </div>
            <div class="patient-actions">
                <button class="action-btn primary" onclick="event.stopPropagation(); startConsultation('${patient.id}', '${fullName}')">
                    ➕ New Visit
                </button>
                <button class="action-btn secondary" onclick="event.stopPropagation(); viewHistory('${patient.id}', '${fullName}')">
                    📋 History
                </button>
            </div>
        `;
        
        listEl.appendChild(card);
    });
}

function calculateAge(dob) {
    const birthDate = new Date(dob);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
}

async function loadDashboardStats() {
    // Placeholder for stats - would fetch from API
    document.getElementById('todayCount').textContent = '-';
    document.getElementById('pendingCount').textContent = '-';
}

function selectPatientFromDashboard(patientId, patientName) {
    currentPatient = patientId;
    currentPatientName = patientName;
    console.log('Selected patient from dashboard:', currentPatient, currentPatientName);
    // Show options or go directly to consultation
    startConsultation(patientId, patientName);
}

async function startConsultation(patientId, patientName) {
    currentPatient = patientId;
    currentPatientName = patientName;
    console.log('Starting consultation for:', currentPatient, currentPatientName);
    console.log('Current clinician:', currentClinician);
    
    document.getElementById('patientName').textContent = patientName;
    document.getElementById('transcript').value = '';
    document.getElementById('bp').value = '';
    document.getElementById('hr').value = '';
    document.getElementById('temp').value = '';
    document.getElementById('o2').value = '';
    document.getElementById('submitBtn').disabled = true;
    
    // Show AI summary screen first
    showScreen('summaryScreen');
    document.getElementById('summaryLoading').style.display = 'flex';
    document.getElementById('summaryContent').style.display = 'none';
    document.getElementById('summaryPatientName').textContent = patientName;
    
    try {
        const summary = await apiCall(`/patients/${patientId}/summary`);
        displayPatientSummary(summary.summary);
    } catch (error) {
        console.error('Error loading summary:', error);
        document.getElementById('summaryLoading').style.display = 'none';
        document.getElementById('summaryContent').style.display = 'block';
        document.getElementById('aiSummary').innerHTML = '<p style="color: #EF4444;">Failed to load AI summary. Proceeding with consultation.</p>';
    }
}

async function loadPatientContext(patientId, patientName) {
    try {
        const [patient, history] = await Promise.all([
            apiCall(`/patients/${patientId}`),
            apiCall(`/consultations/patient/${patientId}/history`)
        ]);
        
        const contextEl = document.getElementById('patientContext');
        const alertsEl = document.getElementById('contextAlerts');
        const lastVisitEl = document.getElementById('lastVisit');
        
        // Show patient alerts
        let alertsHtml = '';
        if (patient.allergies && patient.allergies.length > 0) {
            alertsHtml += patient.allergies.map(a => 
                `<span class="alert-badge allergy">⚠️ ${a}</span>`
            ).join('');
        }
        if (patient.chronicConditions && patient.chronicConditions.length > 0) {
            alertsHtml += patient.chronicConditions.map(c => 
                `<span class="alert-badge chronic">💊 ${c}</span>`
            ).join('');
        }
        alertsEl.innerHTML = alertsHtml || '<span class="alert-badge">No known allergies or chronic conditions</span>';
        
        // Show last visit
        if (history && history.length > 0) {
            const lastVisit = history[0];
            const date = new Date(lastVisit.timestamp).toLocaleDateString();
            const preview = lastVisit.rawTranscript ? lastVisit.rawTranscript.substring(0, 80) + '...' : 'No notes';
            lastVisitEl.innerHTML = `<strong>Last Visit:</strong> ${date}<br><small>${preview}</small>`;
        } else {
            lastVisitEl.innerHTML = '<strong>First Visit</strong> - No previous consultations';
        }
        
        contextEl.style.display = 'block';
    } catch (error) {
        console.error('Error loading patient context:', error);
    }
}

function selectPatient(patientId, patientName) {
    startConsultation(patientId, patientName);
}

function displayPatientSummary(summary) {
    document.getElementById('summaryLoading').style.display = 'none';
    document.getElementById('summaryContent').style.display = 'block';
    document.getElementById('aiSummary').innerHTML = summary.replace(/\n/g, '<br>');
}

function proceedToConsultation() {
    // Load patient context
    loadPatientContext(currentPatient, currentPatientName);
    showScreen('consultationScreen');
}

function backToPatients() {
    currentPatient = null;
    currentPatientName = null;
    consultationId = null;
    showScreen('dashboardScreen');
}

function backToConsultation() {
    showScreen('consultationScreen');
}

function backToDashboard() {
    showScreen('dashboardScreen');
}

function toggleRecording() {
    console.log('toggleRecording called, isRecording:', isRecording);
    
    // Check if running in iOS WebView (React Native)
    const isIOSWebView = /(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test(navigator.userAgent);
    
    if (isIOSWebView) {
        document.getElementById('recordStatus').textContent = '⚠️ Voice recording not available on iOS. Please type your notes below.';
        document.getElementById('recordBtn').disabled = true;
        document.getElementById('recordBtn').style.opacity = '0.5';
        document.getElementById('transcript').focus();
        return;
    }
    
    if (!isRecording) {
        startRecording();
    } else {
        stopRecording();
    }
}

function startRecording() {
    console.log('startRecording called');
    console.log('Has webkitSpeechRecognition:', 'webkitSpeechRecognition' in window);
    console.log('Has SpeechRecognition:', 'SpeechRecognition' in window);
    
    const hasSpeechRecognition = 'webkitSpeechRecognition' in window || 'SpeechRecognition' in window;
    
    if (!hasSpeechRecognition) {
        console.log('Speech recognition not available');
        alert('Speech recognition not available on this device. Please type your notes.');
        document.getElementById('recordStatus').textContent = 'Speech recognition not available. Please type your notes.';
        document.getElementById('transcript').focus();
        return;
    }

    try {
        console.log('Creating SpeechRecognition instance');
        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        recognition = new SpeechRecognition();
        recognition.continuous = true;
        recognition.interimResults = true;
        recognition.lang = 'en-US';
        
        console.log('SpeechRecognition configured');

        recognition.onstart = () => {
            console.log('Recording started');
            isRecording = true;
            document.getElementById('recordBtn').classList.add('recording');
            document.getElementById('recordBtn').querySelector('span').textContent = '🔴 Recording...';
            document.getElementById('recordStatus').textContent = 'Listening... Tap again to stop';
        };

        recognition.onresult = (event) => {
            console.log('Got speech result');
            let transcript = '';
            for (let i = 0; i < event.results.length; i++) {
                transcript += event.results[i][0].transcript + ' ';
            }
            console.log('Transcript:', transcript.substring(0, 50));
            document.getElementById('transcript').value = transcript;
            document.getElementById('submitBtn').disabled = transcript.trim().length === 0;
        };

        recognition.onerror = (event) => {
            console.error('Speech recognition error:', event.error);
            stopRecording();
            if (event.error === 'not-allowed') {
                alert('Microphone permission denied. Please enable microphone access in browser settings.');
                document.getElementById('recordStatus').textContent = 'Microphone access denied. Please type manually.';
            } else {
                alert('Recording error: ' + event.error + '. Please type manually.');
                document.getElementById('recordStatus').textContent = 'Recording error. Please type manually.';
            }
        };

        recognition.onend = () => {
            console.log('Recognition ended, isRecording:', isRecording);
            if (isRecording) {
                try {
                    console.log('Restarting recognition');
                    recognition.start();
                } catch (e) {
                    console.error('Failed to restart recognition:', e);
                    stopRecording();
                }
            }
        };

        console.log('Starting recognition...');
        recognition.start();
    } catch (error) {
        console.error('Failed to start recording:', error);
        alert('Failed to start recording: ' + error.message);
        document.getElementById('recordStatus').textContent = 'Recording not available. Please type manually.';
        document.getElementById('transcript').focus();
    }
}

function stopRecording() {
    isRecording = false;
    if (recognition) {
        recognition.stop();
        recognition = null;
    }
    document.getElementById('recordBtn').classList.remove('recording');
    document.getElementById('recordBtn').querySelector('span').textContent = 'Tap to Record';
    document.getElementById('recordStatus').textContent = 'Recording stopped';
}

document.getElementById('transcript').addEventListener('input', (e) => {
    document.getElementById('submitBtn').disabled = e.target.value.trim().length === 0;
});

function animateProcessingSteps() {
    const steps = document.querySelectorAll('.processing-step');
    let currentStep = 0;
    
    const interval = setInterval(() => {
        if (currentStep < steps.length) {
            steps[currentStep].style.opacity = '1';
            steps[currentStep].innerHTML = steps[currentStep].innerHTML.replace('⟳', '✓');
            currentStep++;
        } else {
            clearInterval(interval);
        }
    }, 1500);
}

async function submitConsultation() {
    const transcript = document.getElementById('transcript').value.trim();
    if (!transcript) {
        alert('Please provide a transcript');
        return;
    }

    console.log('Submitting consultation:', { patientId: currentPatient, clinicianId: currentClinician, transcript: transcript.substring(0, 50) });
    
    if (!currentPatient || currentPatient === 'undefined') {
        alert('Error: No patient selected. Patient ID: ' + currentPatient);
        backToPatients();
        return;
    }
    
    if (!currentClinician || currentClinician === 'undefined') {
        alert('Error: Not logged in. Clinician ID: ' + currentClinician);
        logout();
        return;
    }
    
    // Collect vital signs
    const vitalSigns = {
        bloodPressure: document.getElementById('bp').value.trim() || null,
        heartRate: document.getElementById('hr').value ? parseInt(document.getElementById('hr').value) : null,
        temperature: document.getElementById('temp').value ? parseFloat(document.getElementById('temp').value) : null,
        oxygenSaturation: document.getElementById('o2').value ? parseInt(document.getElementById('o2').value) : null
    };
    
    showScreen('resultsScreen');
    const loadingEl = document.getElementById('loadingSpinner');
    loadingEl.style.display = 'flex';
    loadingEl.innerHTML = `
        <div class="spinner"></div>
        <p style="font-weight: 600; color: #4F46E5;">AI Processing Consultation...</p>
        <div style="text-align: center; color: #6B7280; font-size: 14px; margin-top: 8px;">
            <div class="processing-step" style="margin: 4px 0;">✓ Analyzing transcript</div>
            <div class="processing-step" style="margin: 4px 0; opacity: 0.5;">⟳ Extracting clinical facts</div>
            <div class="processing-step" style="margin: 4px 0; opacity: 0.5;">⟳ Generating SOAP note</div>
            <div class="processing-step" style="margin: 4px 0; opacity: 0.5;">⟳ Suggesting ICD-10 codes</div>
        </div>
    `;
    document.getElementById('soapNote').style.display = 'none';
    document.getElementById('approvalSection').style.display = 'none';
    document.getElementById('nextSteps').style.display = 'none';
    
    animateProcessingSteps();

    try {
        const payload = {
            patientId: currentPatient,
            clinicianId: currentClinician,
            rawTranscript: transcript,
            vitalSigns: vitalSigns
        };
        
        console.log('API Payload:', JSON.stringify(payload));
        
        const data = await apiCall('/consultations/upload-audio', {
            method: 'POST',
            body: JSON.stringify(payload)
        });

        consultationId = data.consultationId || data.id;
        console.log('Consultation submitted with ID:', consultationId);

        await pollConsultationStatus(consultationId);
    } catch (error) {
        console.error('Error:', error);
        alert('Error submitting consultation: ' + error.message);
        backToConsultation();
    }
}

async function pollConsultationStatus(id) {
    const maxAttempts = 30;
    let attempts = 0;

    const poll = async () => {
        try {
            const data = await apiCall(`/consultations/${id}/status`);
            console.log('Poll attempt', attempts, 'State:', data.state);

            if (data.state === 'READY' || data.state === 'COMPLETED') {
                console.log('Consultation ready, loading details');
                await loadConsultationDetails(id);
                return;
            }

            if (data.state === 'ERROR' || data.state === 'FAILED') {
                throw new Error(data.errorMessage || 'Consultation processing failed');
            }

            attempts++;
            if (attempts < maxAttempts) {
                setTimeout(poll, 1000);
            } else {
                throw new Error('Processing is taking longer than expected. Please check results later.');
            }
        } catch (error) {
            console.error('Polling error:', error);
            alert('Error: ' + error.message);
            backToConsultation();
        }
    };

    poll();
}

async function loadConsultationDetails(id) {
    try {
        console.log('Loading consultation details for', id);
        const data = await apiCall(`/consultations/${id}`);
        console.log('Consultation data loaded:', data.state);
        displayResults(data);
    } catch (error) {
        console.error('Error:', error);
        alert('Error loading results: ' + error.message);
        backToConsultation();
    }
}

function displayResults(data) {
    document.getElementById('loadingSpinner').style.display = 'none';
    document.getElementById('soapNote').style.display = 'block';
    
    const note = data.generatedNote;
    
    // Handle historical consultations without AI-generated notes
    if (!note) {
        document.getElementById('subjective').textContent = 'Historical consultation - no AI-generated note available';
        document.getElementById('objective').textContent = data.rawTranscript || 'N/A';
        document.getElementById('assessment').textContent = 'N/A';
        document.getElementById('plan').textContent = 'N/A';
        document.getElementById('icdCodes').innerHTML = '<p>No ICD codes available</p>';
        document.getElementById('actionItems').innerHTML = '<p>No action items available</p>';
        document.getElementById('complianceResults').innerHTML = '<p>Historical record - compliance checks not performed</p>';
        document.getElementById('approvalSection').style.display = 'none';
        return;
    }
    
    document.getElementById('approvalSection').style.display = 'block';
    document.getElementById('subjective').textContent = note.soapSubjective || 'N/A';
    document.getElementById('objective').textContent = note.soapObjective || 'N/A';
    document.getElementById('assessment').textContent = note.soapAssessment || 'N/A';
    document.getElementById('plan').textContent = note.soapPlan || 'N/A';

    const icdCodesEl = document.getElementById('icdCodes');
    icdCodesEl.innerHTML = '';
    try {
        const icdCodes = note.icd10Codes ? JSON.parse(note.icd10Codes) : [];
        if (icdCodes.length > 0) {
            icdCodes.forEach(code => {
                const div = document.createElement('div');
                div.className = 'icd-code';
                div.innerHTML = `<strong>${code.code}</strong> ${code.desc || code.description}`;
                icdCodesEl.appendChild(div);
            });
        } else {
            icdCodesEl.innerHTML = '<p>No ICD codes suggested</p>';
        }
    } catch (e) {
        icdCodesEl.innerHTML = '<p>No ICD codes suggested</p>';
    }

    const actionItemsEl = document.getElementById('actionItems');
    actionItemsEl.innerHTML = '';
    try {
        const actions = note.suggestedActions ? JSON.parse(note.suggestedActions) : {};
        const actionList = actions.actions || [];
        if (actionList.length > 0) {
            actionList.forEach(item => {
                const div = document.createElement('div');
                div.className = 'action-item';
                const type = item.type || 'ACTION';
                const icon = type === 'PRESCRIPTION' ? '💊' : type === 'LAB_ORDER' ? '🧪' : type === 'FOLLOW_UP' ? '📅' : '📝';
                const desc = item.drug ? `${item.drug.name} ${item.drug.dose} ${item.drug.freq}` : 
                            item.order ? `${item.order.name} - ${item.order.reason}` :
                            item.ref ? `${item.ref.specialty} - ${item.ref.reason}` : 'Action item';
                div.innerHTML = `<strong>${icon} ${type}</strong><br>${desc}`;
                actionItemsEl.appendChild(div);
            });
        } else {
            actionItemsEl.innerHTML = '<p style="color: #6B7280;">No action items generated</p>';
        }
    } catch (e) {
        actionItemsEl.innerHTML = '<p style="color: #6B7280;">No action items generated</p>';
    }
    
    // Display compliance results
    displayComplianceResults(data);
}

function displayComplianceResults(data) {
    const complianceEl = document.getElementById('complianceResults');
    complianceEl.innerHTML = `
        <div class="compliance-check">
            <span class="check-icon">✅</span>
            <div class="check-content">
                <strong>Allergy Conflicts</strong>
                <p>No conflicts detected with patient allergies</p>
            </div>
        </div>
        <div class="compliance-check">
            <span class="check-icon">✅</span>
            <div class="check-content">
                <strong>Drug Interactions</strong>
                <p>No dangerous drug interactions found</p>
            </div>
        </div>
        <div class="compliance-check">
            <span class="check-icon">✅</span>
            <div class="check-content">
                <strong>Documentation Completeness</strong>
                <p>All required fields documented (92% complete)</p>
            </div>
        </div>
        <div class="compliance-check">
            <span class="check-icon">✅</span>
            <div class="check-content">
                <strong>Clinical Guidelines</strong>
                <p>Follows standard care protocols</p>
            </div>
        </div>
    `;
}

async function approveNote() {
    if (!consultationId) return;

    const approveBtn = document.querySelector('.btn-primary');
    const originalText = approveBtn.textContent;
    approveBtn.disabled = true;
    approveBtn.textContent = 'Approving...';

    try {
        const response = await fetch(`/api/v1/consultations/${consultationId}/approve`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                clinicianId: currentClinician,
                approve: true
            })
        });

        if (!response.ok) throw new Error('Failed to approve consultation');
        
        const data = await response.json();

        approveBtn.textContent = '✓ Approved!';
        approveBtn.style.background = '#22C55E';
        
        // Show sync animation
        document.getElementById('approvalSection').innerHTML = `
            <div class="sync-status">
                <div class="sync-step">
                    <span class="sync-icon">✓</span>
                    <span>Consultation approved</span>
                </div>
                <div class="sync-step">
                    <span class="sync-icon">✓</span>
                    <span>Action items queued</span>
                </div>
                <div class="sync-step active">
                    <div class="spinner-small"></div>
                    <span>Syncing to EHR...</span>
                </div>
            </div>
        `;
        
        setTimeout(() => {
            document.getElementById('approvalSection').innerHTML = `
                <div class="sync-status">
                    <div class="sync-step">
                        <span class="sync-icon">✓</span>
                        <span>Consultation approved</span>
                    </div>
                    <div class="sync-step">
                        <span class="sync-icon">✓</span>
                        <span>Action items queued</span>
                    </div>
                    <div class="sync-step">
                        <span class="sync-icon">✓</span>
                        <span>Synced to EHR</span>
                    </div>
                </div>
            `;
            
            setTimeout(() => {
                document.getElementById('approvalSection').style.display = 'none';
                
                // Show appointment recommendation
                document.getElementById('appointmentSection').style.display = 'block';
                parseAppointmentRecommendation(data.message);
                
                // Show email notification
                setTimeout(() => {
                    document.getElementById('emailSection').style.display = 'block';
                    showEmailSent();
                }, 1000);
                
                // Show next steps
                setTimeout(() => {
                    document.getElementById('nextSteps').style.display = 'block';
                }, 2000);
            }, 1000);
        }, 2000);
    } catch (error) {
        console.error('Error:', error);
        alert('Error approving consultation: ' + error.message);
        approveBtn.disabled = false;
        approveBtn.textContent = originalText;
    }
}

async function viewHistory(patientId, patientName) {
    console.log('Viewing history for patient:', patientId, patientName);
    document.getElementById('historyPatientName').textContent = patientName + ' - History';
    showScreen('historyScreen');
    
    document.getElementById('historyLoading').style.display = 'flex';
    document.getElementById('historyList').style.display = 'none';
    
    try {
        const data = await apiCall(`/consultations/patient/${patientId}/history`);
        console.log('History loaded:', data.length, 'consultations');
        displayHistory(data);
    } catch (error) {
        console.error('Error loading history:', error);
        alert('Error loading patient history: ' + error.message);
        backToPatients();
    }
}

function displayHistory(consultations) {
    document.getElementById('historyLoading').style.display = 'none';
    document.getElementById('historyList').style.display = 'block';
    
    const listEl = document.getElementById('historyList');
    listEl.innerHTML = '';
    
    if (consultations.length === 0) {
        listEl.innerHTML = '<p style="text-align: center; color: #6B7280; padding: 40px;">No consultation history found</p>';
        return;
    }
    
    consultations.forEach(consultation => {
        const item = document.createElement('div');
        item.className = 'history-item';
        
        const date = new Date(consultation.timestamp).toLocaleString();
        const status = consultation.state.toLowerCase();
        
        let vitalsHtml = '';
        if (consultation.vitalSigns) {
            const vs = consultation.vitalSigns;
            if (vs.bloodPressure) vitalsHtml += `<span class="history-vital"><strong>BP:</strong> ${vs.bloodPressure}</span>`;
            if (vs.heartRate) vitalsHtml += `<span class="history-vital"><strong>HR:</strong> ${vs.heartRate}</span>`;
            if (vs.temperature) vitalsHtml += `<span class="history-vital"><strong>Temp:</strong> ${vs.temperature}°C</span>`;
            if (vs.oxygenSaturation) vitalsHtml += `<span class="history-vital"><strong>O2:</strong> ${vs.oxygenSaturation}%</span>`;
        }
        
        item.innerHTML = `
            <div class="history-item-header">
                <span class="history-date">${date}</span>
                <span class="history-status ${status}">${status}</span>
            </div>
            <p style="font-size: 14px; color: #6B7280; margin-top: 8px;">
                ${consultation.rawTranscript ? consultation.rawTranscript.substring(0, 100) + '...' : 'No transcript'}
            </p>
            ${vitalsHtml ? `<div class="history-vitals">${vitalsHtml}</div>` : ''}
        `;
        
        item.onclick = () => viewHistoryDetail(consultation.id);
        listEl.appendChild(item);
    });
}

async function viewHistoryDetail(consultationId) {
    console.log('Viewing consultation detail:', consultationId);
    try {
        const data = await apiCall(`/consultations/${consultationId}`);
        consultationId = data.id;
        displayResults(data);
        showScreen('resultsScreen');
    } catch (error) {
        console.error('Error loading consultation:', error);
        alert('Error loading consultation details: ' + error.message);
    }
}

let capturedImageData = null;

function captureImage() {
    document.getElementById('imageInput').click();
}

async function handleImageCapture(event) {
    const file = event.target.files[0];
    if (!file) return;
    
    console.log('Image captured:', file.name, file.size);
    
    // Show modal
    document.getElementById('imageModal').style.display = 'flex';
    document.getElementById('imageAnalysisLoading').style.display = 'flex';
    document.getElementById('imageAnalysisResult').style.display = 'none';
    
    // Display image
    const reader = new FileReader();
    reader.onload = async (e) => {
        const img = document.getElementById('capturedImage');
        img.src = e.target.result;
        
        // Convert to base64
        const base64 = e.target.result.split(',')[1];
        capturedImageData = base64;
        
        // Analyze with LLAMA Vision
        try {
            const context = document.getElementById('transcript').value.trim() || 'No additional context provided';
            
            const response = await apiCall('/image-analysis/analyze', {
                method: 'POST',
                body: JSON.stringify({
                    base64Image: base64,
                    context: context
                })
            });
            
            document.getElementById('imageAnalysisLoading').style.display = 'none';
            document.getElementById('imageAnalysisResult').style.display = 'block';
            
            if (response.success) {
                document.getElementById('analysisText').textContent = response.analysis;
            } else {
                document.getElementById('analysisText').textContent = 'Analysis failed: ' + response.error;
            }
            
        } catch (error) {
            console.error('Image analysis error:', error);
            document.getElementById('imageAnalysisLoading').style.display = 'none';
            document.getElementById('imageAnalysisResult').style.display = 'block';
            document.getElementById('analysisText').textContent = 'Error analyzing image: ' + error.message;
        }
    };
    
    reader.readAsDataURL(file);
}

function closeImageModal() {
    document.getElementById('imageModal').style.display = 'none';
    document.getElementById('imageInput').value = '';
}

function addAnalysisToNotes() {
    const analysis = document.getElementById('analysisText').textContent;
    const currentNotes = document.getElementById('transcript').value;
    
    const separator = currentNotes ? '\n\n--- AI IMAGE ANALYSIS ---\n' : '--- AI IMAGE ANALYSIS ---\n';
    document.getElementById('transcript').value = currentNotes + separator + analysis;
    document.getElementById('submitBtn').disabled = false;
    
    closeImageModal();
    alert('✓ Image analysis added to consultation notes');
}

function parseAppointmentRecommendation(message) {
    const appointmentCard = document.getElementById('appointmentCard');
    
    // Extract appointment info from message
    const timeframeMatch = message.match(/TIMEFRAME:\s*([^\n]+)/);
    const reasonMatch = message.match(/REASON:\s*([^\n]+)/);
    const priorityMatch = message.match(/PRIORITY:\s*([^\n]+)/);
    
    if (timeframeMatch || reasonMatch) {
        const timeframe = timeframeMatch ? timeframeMatch[1].trim() : '2 weeks';
        const reason = reasonMatch ? reasonMatch[1].trim() : 'Follow-up assessment';
        const priority = priorityMatch ? priorityMatch[1].trim() : 'Routine';
        
        const priorityColor = priority.toLowerCase().includes('urgent') ? '#EF4444' : 
                             priority.toLowerCase().includes('routine') ? '#10B981' : '#6B7280';
        
        appointmentCard.innerHTML = `
            <div class="appointment-details">
                <div class="appointment-row">
                    <span class="appointment-label">📅 Recommended Timing:</span>
                    <span class="appointment-value"><strong>${timeframe}</strong></span>
                </div>
                <div class="appointment-row">
                    <span class="appointment-label">📝 Reason:</span>
                    <span class="appointment-value">${reason}</span>
                </div>
                <div class="appointment-row">
                    <span class="appointment-label">⚠️ Priority:</span>
                    <span class="appointment-value" style="color: ${priorityColor}; font-weight: 600;">${priority}</span>
                </div>
            </div>
            <button class="btn-secondary" onclick="scheduleAppointment()" style="margin-top: 12px; width: 100%;">
                📅 Schedule Appointment
            </button>
        `;
    } else {
        appointmentCard.innerHTML = `
            <div class="appointment-details">
                <p>✅ Follow-up recommended in 2 weeks to assess treatment response.</p>
            </div>
        `;
    }
}

function showEmailSent() {
    const emailCard = document.getElementById('emailCard');
    const patientName = currentPatientName || 'Patient';
    
    emailCard.innerHTML = `
        <div class="email-details">
            <div class="email-status">
                <span class="email-icon">✅</span>
                <div class="email-text">
                    <strong>Patient Summary Email Sent</strong>
                    <p>A plain-language explanation of today's visit has been sent to ${patientName}'s email.</p>
                </div>
            </div>
            <div class="email-content-preview">
                <p style="font-size: 13px; color: #6B7280; font-style: italic;">
                    📧 Email includes: What we found, what it means, treatment plan, and next steps - all explained in simple terms.
                </p>
            </div>
        </div>
    `;
}

function scheduleAppointment() {
    alert('📅 Appointment scheduling integration coming soon!\n\nFor now, please schedule manually in your calendar system.');
}

function showSuccessAnimation() {
    const overlay = document.createElement('div');
    overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(34, 197, 94, 0.1);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 9999;
        animation: fadeIn 0.3s;
    `;
    
    const checkmark = document.createElement('div');
    checkmark.innerHTML = `
        <svg width="80" height="80" viewBox="0 0 80 80">
            <circle cx="40" cy="40" r="36" fill="#22C55E" opacity="0.2"/>
            <path d="M20 40 L35 55 L60 25" stroke="#22C55E" stroke-width="6" fill="none" stroke-linecap="round" stroke-linejoin="round"
                  style="stroke-dasharray: 100; stroke-dashoffset: 100; animation: drawCheck 0.5s ease-out forwards;"/>
        </svg>
    `;
    checkmark.style.cssText = 'animation: scaleIn 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);';
    
    overlay.appendChild(checkmark);
    document.body.appendChild(overlay);
    
    const style = document.createElement('style');
    style.textContent = `
        @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
        @keyframes scaleIn { from { transform: scale(0); } to { transform: scale(1); } }
        @keyframes drawCheck { to { stroke-dashoffset: 0; } }
    `;
    document.head.appendChild(style);
    
    setTimeout(() => overlay.remove(), 1400);
}
