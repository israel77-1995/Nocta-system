-- Patients table
CREATE TABLE patients (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    dob DATE
);

CREATE TABLE patient_identifiers (
    patient_id UUID NOT NULL,
    identifier_type VARCHAR(255),
    identifier_value VARCHAR(255),
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

CREATE TABLE patient_allergies (
    patient_id UUID NOT NULL,
    allergy VARCHAR(255),
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
    timestamp TIMESTAMP NOT NULL,
    raw_transcript TEXT,
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
