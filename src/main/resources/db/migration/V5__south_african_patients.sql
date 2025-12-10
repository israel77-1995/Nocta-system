-- South African Patient Data

-- Patient 6: Thabo Mthembu - Hypertension and Diabetes (common in SA)
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440006', 'Thabo', 'Mthembu', '1972-04-08', 'MRN006', 'ACE inhibitors');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440006', 'ACE inhibitors');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440006', 'Type 2 Diabetes Mellitus'),
('550e8400-e29b-41d4-a716-446655440006', 'Hypertension'),
('550e8400-e29b-41d4-a716-446655440006', 'Diabetic Nephropathy');

-- Patient 7: Nomsa Dlamini - HIV positive with TB history
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440007', 'Nomsa', 'Dlamini', '1988-11-23', 'MRN007', 'Rifampin');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440007', 'Rifampin');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440007', 'HIV'),
('550e8400-e29b-41d4-a716-446655440007', 'Previous Pulmonary TB'),
('550e8400-e29b-41d4-a716-446655440007', 'Chronic Kidney Disease Stage 2');

-- Patient 8: Sipho Ndlovu - Young adult with asthma
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440008', 'Sipho', 'Ndlovu', '1995-02-14', 'MRN008', 'Aspirin');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440008', 'Aspirin'),
('550e8400-e29b-41d4-a716-446655440008', 'NSAIDs');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440008', 'Asthma'),
('550e8400-e29b-41d4-a716-446655440008', 'Allergic Rhinitis');

-- Patient 9: Zanele Khumalo - Pregnant woman
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440009', 'Zanele', 'Khumalo', '1992-08-30', 'MRN009', 'Penicillin');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440009', 'Penicillin');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440009', 'Pregnancy - 28 weeks'),
('550e8400-e29b-41d4-a716-446655440009', 'Gestational Diabetes');

-- Patient 10: Pieter van der Merwe - Elderly Afrikaner with multiple conditions
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440010', 'Pieter', 'van der Merwe', '1948-12-05', 'MRN010', 'Sulfonamides');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440010', 'Sulfonamides'),
('550e8400-e29b-41d4-a716-446655440010', 'Shellfish');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440010', 'Coronary Artery Disease'),
('550e8400-e29b-41d4-a716-446655440010', 'Type 2 Diabetes Mellitus'),
('550e8400-e29b-41d4-a716-446655440010', 'Chronic Kidney Disease Stage 3'),
('550e8400-e29b-41d4-a716-446655440010', 'Osteoarthritis');

-- Patient 11: Fatima Patel - Indian South African with metabolic syndrome
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440011', 'Fatima', 'Patel', '1965-05-17', 'MRN011', 'Metformin');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440011', 'Metformin');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440011', 'Type 2 Diabetes Mellitus'),
('550e8400-e29b-41d4-a716-446655440011', 'Hypertension'),
('550e8400-e29b-41d4-a716-446655440011', 'Hyperlipidemia'),
('550e8400-e29b-41d4-a716-446655440011', 'Obesity');

-- Patient 12: Mandla Zulu - Young man with epilepsy
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440012', 'Mandla', 'Zulu', '1990-10-12', 'MRN012', 'Phenytoin');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440012', 'Phenytoin');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440012', 'Epilepsy'),
('550e8400-e29b-41d4-a716-446655440012', 'Depression');

-- Patient 13: Lerato Mokoena - Woman with rheumatoid arthritis
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440013', 'Lerato', 'Mokoena', '1978-01-25', 'MRN013', 'Gold salts');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440013', 'Gold salts'),
('550e8400-e29b-41d4-a716-446655440013', 'Latex');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440013', 'Rheumatoid Arthritis'),
('550e8400-e29b-41d4-a716-446655440013', 'Osteoporosis');

-- Patient 14: Johannes Botha - Farmer with skin cancer history
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440014', 'Johannes', 'Botha', '1960-07-03', 'MRN014', 'Iodine');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440014', 'Iodine');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440014', 'Basal Cell Carcinoma - treated'),
('550e8400-e29b-41d4-a716-446655440014', 'Hypertension'),
('550e8400-e29b-41d4-a716-446655440014', 'Chronic Back Pain');

-- Patient 15: Aisha Mohamed - Young Muslim woman with thyroid issues
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440015', 'Aisha', 'Mohamed', '1987-03-19', 'MRN015', 'None');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440015', 'Hypothyroidism'),
('550e8400-e29b-41d4-a716-446655440015', 'Iron Deficiency Anemia');

-- Patient 16: Bongani Mahlangu - Construction worker with back injury
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440016', 'Bongani', 'Mahlangu', '1982-09-07', 'MRN016', 'Morphine');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440016', 'Morphine'),
('550e8400-e29b-41d4-a716-446655440016', 'Codeine');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440016', 'Chronic Lower Back Pain'),
('550e8400-e29b-41d4-a716-446655440016', 'Lumbar Disc Herniation');

-- Patient 17: Priya Naidoo - IT professional with stress-related conditions
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440017', 'Priya', 'Naidoo', '1985-12-11', 'MRN017', 'Benzodiazepines');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440017', 'Benzodiazepines');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440017', 'Generalized Anxiety Disorder'),
('550e8400-e29b-41d4-a716-446655440017', 'Migraine'),
('550e8400-e29b-41d4-a716-446655440017', 'Gastroesophageal Reflux Disease');

-- Patient 18: Themba Sithole - Taxi driver with cardiovascular issues
INSERT INTO patients (id, first_name, last_name, dob, medical_record_number, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440018', 'Themba', 'Sithole', '1970-06-28', 'MRN018', 'Beta-blockers');
INSERT INTO patient_allergies (patient_id, allergies) VALUES
('550e8400-e29b-41d4-a716-446655440018', 'Beta-blockers');
INSERT INTO patient_chronic_conditions (patient_id, condition) VALUES
('550e8400-e29b-41d4-a716-446655440018', 'Hypertension'),
('550e8400-e29b-41d4-a716-446655440018', 'Type 2 Diabetes Mellitus'),
('550e8400-e29b-41d4-a716-446655440018', 'Sleep Apnea');