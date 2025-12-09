-- Realistic Patient Data

-- Patient 1: Sarah Johnson - Type 2 Diabetes with complications
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Sarah', 'Johnson', '1968-03-12', 'MRN001', 'Sulfa drugs');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Sulfa drugs'),
('550e8400-e29b-41d4-a716-446655440001', 'Shellfish');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Type 2 Diabetes Mellitus'),
('550e8400-e29b-41d4-a716-446655440001', 'Hypertension'),
('550e8400-e29b-41d4-a716-446655440001', 'Hyperlipidemia');

-- Patient 2: Michael Chen - Asthma patient
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440002', 'Michael', 'Chen', '1985-07-22', 'MRN002', 'Penicillin');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440002', 'Penicillin'),
('550e8400-e29b-41d4-a716-446655440002', 'Pollen');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440002', 'Asthma'),
('550e8400-e29b-41d4-a716-446655440002', 'Seasonal Allergic Rhinitis');

-- Patient 3: Robert Williams - Cardiac patient
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440003', 'Robert', 'Williams', '1955-01-30', 'MRN003', 'Aspirin');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440003', 'Aspirin'),
('550e8400-e29b-41d4-a716-446655440003', 'Codeine');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440003', 'Coronary Artery Disease'),
('550e8400-e29b-41d4-a716-446655440003', 'Congestive Heart Failure'),
('550e8400-e29b-41d4-a716-446655440003', 'Atrial Fibrillation');

-- Patient 4: Emily Thompson - Mental health
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440004', 'Emily', 'Thompson', '1998-09-15', 'MRN004', 'None');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440004', 'Generalized Anxiety Disorder'),
('550e8400-e29b-41d4-a716-446655440004', 'Major Depressive Disorder');

-- Patient 5: James Anderson - COPD
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440005', 'James', 'Anderson', '1950-06-18', 'MRN005', 'Latex');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440005', 'Latex');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440005', 'COPD'),
('550e8400-e29b-41d4-a716-446655440005', 'Former Smoker'),
('550e8400-e29b-41d4-a716-446655440005', 'Osteoarthritis');

-- Default clinician
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440099', 'Dr. Sarah', 'Mitchell', '1975-06-15', 'CLIN001', 'None');
