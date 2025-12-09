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

function login() {
    const clinicianId = document.getElementById('clinicianId').value.trim();
    if (!clinicianId) {
        alert('Please enter Clinician ID');
        return;
    }
    currentClinician = clinicianId;
    showScreen('patientScreen');
}

function logout() {
    currentClinician = null;
    currentPatient = null;
    showScreen('loginScreen');
}

function selectPatient(patientId, patientName) {
    currentPatient = patientId;
    currentPatientName = patientName;
    document.getElementById('patientName').textContent = patientName;
    document.getElementById('transcript').value = '';
    document.getElementById('submitBtn').disabled = true;
    showScreen('consultationScreen');
}

function backToPatients() {
    currentPatient = null;
    consultationId = null;
    showScreen('patientScreen');
}

function backToConsultation() {
    showScreen('consultationScreen');
}

function toggleRecording() {
    const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
    
    if (isIOS) {
        document.getElementById('recordStatus').textContent = 'Type your consultation notes below';
        document.getElementById('transcript').placeholder = 'Patient reports severe headache for 3 days, throbbing pain, worse with light. Blood pressure 140/90. No visual changes or neck stiffness...';
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
    const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
    const hasSpeechRecognition = 'webkitSpeechRecognition' in window || 'SpeechRecognition' in window;
    
    if (!hasSpeechRecognition || isIOS) {
        document.getElementById('recordStatus').textContent = 'Speech recognition not available on iOS. Please type your consultation notes below.';
        document.getElementById('transcript').focus();
        return;
    }

    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    recognition = new SpeechRecognition();
    recognition.continuous = true;
    recognition.interimResults = true;
    recognition.lang = 'en-US';

    recognition.onstart = () => {
        isRecording = true;
        document.getElementById('recordBtn').classList.add('recording');
        document.getElementById('recordBtn').querySelector('span').textContent = 'Recording...';
        document.getElementById('recordStatus').textContent = 'Listening...';
    };

    recognition.onresult = (event) => {
        let transcript = '';
        for (let i = 0; i < event.results.length; i++) {
            transcript += event.results[i][0].transcript + ' ';
        }
        document.getElementById('transcript').value = transcript;
        document.getElementById('submitBtn').disabled = transcript.trim().length === 0;
    };

    recognition.onerror = (event) => {
        console.error('Speech recognition error:', event.error);
        stopRecording();
        if (event.error === 'not-allowed') {
            alert('Microphone access denied. Please enable microphone permissions in Settings.');
        } else {
            document.getElementById('recordStatus').textContent = 'Recording error. Please type manually.';
        }
    };

    recognition.onend = () => {
        if (isRecording) {
            recognition.start();
        }
    };

    recognition.start();
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

    alert('DEBUG: Starting submission for patient ' + currentPatient);
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
    document.getElementById('actionButtons').style.display = 'none';
    
    animateProcessingSteps();

    try {
        const data = await apiCall('/consultations/upload-audio', {
            method: 'POST',
            body: JSON.stringify({
                patientId: currentPatient,
                clinicianId: currentClinician,
                rawTranscript: transcript,
                audioUrl: null
            })
        });

        consultationId = data.id;
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
    document.getElementById('actionButtons').style.display = 'flex';

    const note = data.generatedNote;
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
                const desc = item.drug ? `${item.drug.name} ${item.drug.dose} ${item.drug.freq}` : 
                            item.order ? `${item.order.name} - ${item.order.reason}` :
                            item.ref ? `${item.ref.specialty} - ${item.ref.reason}` : 'Action item';
                div.innerHTML = `<strong>${type}</strong> ${desc}`;
                actionItemsEl.appendChild(div);
            });
        } else {
            actionItemsEl.innerHTML = '<p>No action items</p>';
        }
    } catch (e) {
        actionItemsEl.innerHTML = '<p>No action items</p>';
    }
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

        approveBtn.textContent = '✓ Approved!';
        approveBtn.style.background = '#22C55E';
        
        setTimeout(() => {
            showSuccessAnimation();
            setTimeout(backToPatients, 1500);
        }, 500);
    } catch (error) {
        console.error('Error:', error);
        alert('Error approving consultation: ' + error.message);
        approveBtn.disabled = false;
        approveBtn.textContent = originalText;
    }
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
