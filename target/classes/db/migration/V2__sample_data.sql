-- Insert sample patients
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'John', 'Doe', '1980-05-15', 'MRN001', 'Penicillin'),
('550e8400-e29b-41d4-a716-446655440002', 'Jane', 'Smith', '1975-08-22', 'MRN002', 'NSAIDs');

-- Insert sample allergies (for ElementCollection)
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Penicillin'),
('550e8400-e29b-41d4-a716-446655440002', 'NSAIDs');

-- Insert sample chronic conditions
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Hypertension'),
('550e8400-e29b-41d4-a716-446655440002', 'Type 2 Diabetes');
