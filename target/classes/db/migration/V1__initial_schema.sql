-- Patients table
CREATE TABLE patients (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    dob DATE,
    medical_record_number VARCHAR(255),
    allergies TEXT
);

CREATE TABLE patient_allergies (
    patient_id UUID NOT NULL,
    allergies VARCHAR(255),
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

CREATE TABLE patient_chronic_conditions (
    patient_id UUID NOT NULL,
    condition VARCHAR(255),
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- Consultations table
CREATE TABLE consultations (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    clinician_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    audio_transcript TEXT,
    raw_transcript TEXT,
    structured_data TEXT,
    audio_url VARCHAR(500),
    state VARCHAR(50) NOT NULL,
    generated_note_id UUID,
    error_message TEXT
);

-- Generated notes table
CREATE TABLE generated_notes (
    id UUID PRIMARY KEY,
    consultation_id UUID NOT NULL,
    soap_subjective TEXT,
    soap_objective TEXT,
    soap_assessment TEXT,
    soap_plan TEXT,
    icd10_codes TEXT,
    suggested_actions TEXT,
    patient_summary TEXT,
    confidence DOUBLE,
    created_at TIMESTAMP NOT NULL,
    created_by UUID NOT NULL
);
