-- Consultation History for All Patients

-- Sarah Johnson (Diabetes patient) - 3 visits showing disease progression
INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440099',
'Patient reports good compliance with metformin 1000mg twice daily. Blood sugar readings averaging 140 mg/dL fasting. No hypoglycemic episodes. Denies chest pain or shortness of breath. Reports mild tingling in feet bilaterally, worse at night. Blood pressure slightly elevated today. Discussed importance of diet and exercise.',
'APPROVED', DATEADD('DAY', -90, CURRENT_TIMESTAMP),
'142/88', 78, 98.4, 97, 16, 185.5, 64.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440099',
'Follow-up visit. HbA1c improved to 7.2% from 8.1%. Patient reports better diet adherence, walking 30 minutes daily. Blood pressure still elevated at 145/92. Increased lisinopril from 10mg to 20mg daily. Foot exam shows early peripheral neuropathy. Referred to podiatry. Added gabapentin 300mg at bedtime for neuropathic pain.',
'APPROVED', DATEADD('DAY', -60, CURRENT_TIMESTAMP),
'145/92', 82, 98.2, 98, 18, 183.0, 64.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440099',
'Patient presents with fatigue and increased thirst for 2 weeks. Blood sugar readings elevated, averaging 180-200 mg/dL. Weight loss of 7 lbs since last visit. Discussed medication adjustment. Added glipizide 5mg daily. Ordered HbA1c, lipid panel, comprehensive metabolic panel. Referred to endocrinology for diabetes management optimization. Emphasized importance of medication compliance.',
'APPROVED', DATEADD('DAY', -15, CURRENT_TIMESTAMP),
'138/86', 80, 98.6, 97, 16, 178.0, 64.0);

-- Michael Chen (Asthma patient) - 4 visits showing seasonal pattern
INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440099',
'Patient reports increased wheezing and shortness of breath for 3 days. Using albuterol rescue inhaler 4-5 times daily, usually only needs it twice weekly. Triggered by spring pollen exposure. No fever, no chest pain. Chest feels tight, especially at night affecting sleep. Peak flow 320 L/min, baseline 450. Lungs with diffuse expiratory wheezes. Started prednisone 40mg daily for 5 days. Increased fluticasone inhaler to twice daily.',
'APPROVED', DATEADD('DAY', -120, CURRENT_TIMESTAMP),
'118/76', 88, 98.1, 94, 22, 165.0, 69.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440099',
'Follow-up after asthma exacerbation. Completed prednisone course. Breathing much improved. Using rescue inhaler once daily. Peak flow back to baseline at 450 L/min. Lungs clear bilaterally. Discussed allergy testing. Referred to allergist. Continue fluticasone 220mcg twice daily. Provided asthma action plan.',
'APPROVED', DATEADD('DAY', -105, CURRENT_TIMESTAMP),
'120/78', 72, 98.4, 98, 16, 165.0, 69.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440099',
'Routine asthma follow-up. Patient doing well on current regimen. Using rescue inhaler 1-2 times per week. No nighttime symptoms. Allergy testing showed sensitivity to grass pollen, tree pollen, dust mites. Started on cetirizine 10mg daily for allergies. Peak flow 460 L/min. Lungs clear. Continue current medications.',
'APPROVED', DATEADD('DAY', -60, CURRENT_TIMESTAMP),
'122/80', 70, 98.6, 99, 14, 166.0, 69.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440099',
'Patient presents with mild upper respiratory symptoms. Runny nose, sneezing, mild sore throat for 2 days. No fever. Concerned about asthma exacerbation. Lungs clear, no wheezes. Peak flow 440 L/min. Likely viral URI. Advised increased fluids, rest. Continue asthma medications. Return if develops fever, chest tightness, or difficulty breathing.',
'APPROVED', DATEADD('DAY', -10, CURRENT_TIMESTAMP),
'118/78', 74, 98.8, 98, 16, 166.0, 69.0);

-- Robert Williams (Cardiac patient) - 5 visits showing CHF management
INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440099',
'Routine cardiac follow-up. Patient stable on current medications. No chest pain, no palpitations. Mild dyspnea on exertion climbing stairs. Sleeping flat without difficulty. Weight stable. Ankles with trace edema. Heart irregular rhythm, consistent with known AFib. Lungs clear. INR 2.3, therapeutic. Continue warfarin, metoprolol, lisinopril, furosemide. Follow up 3 months.',
'APPROVED', DATEADD('DAY', -150, CURRENT_TIMESTAMP),
'128/78', 76, 98.2, 96, 14, 188.0, 70.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440099',
'Patient reports increased leg swelling and shortness of breath with minimal exertion for 1 week. Now sleeping on 3 pillows, previously slept flat. Weight up 8 lbs in 2 weeks. Denies chest pain. Compliant with medications. Ankles with 2+ pitting edema. Lungs with bibasilar crackles. JVD present. CHF exacerbation. Increased furosemide to 80mg daily. Strict fluid restriction 1.5L daily. Daily weights. Follow up in 3 days.',
'APPROVED', DATEADD('DAY', -90, CURRENT_TIMESTAMP),
'158/94', 92, 98.0, 92, 20, 198.0, 70.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440010', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440099',
'Follow-up after CHF exacerbation. Diuresis successful, weight down 10 lbs. Breathing much improved. No orthopnea. Sleeping flat again. Edema resolved. Lungs clear. INR 2.4, therapeutic. BNP decreased from 850 to 320. Continue increased furosemide dose. Cardiology follow-up scheduled. Discussed importance of daily weights and sodium restriction.',
'APPROVED', DATEADD('DAY', -80, CURRENT_TIMESTAMP),
'132/82', 78, 98.2, 96, 16, 188.0, 70.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440099',
'Routine INR check. Patient feeling well. No bleeding, no bruising. INR 3.2, slightly elevated. Decreased warfarin from 5mg to 4mg daily. Recheck INR in 1 week. Continue other medications. No changes to cardiac regimen.',
'APPROVED', DATEADD('DAY', -45, CURRENT_TIMESTAMP),
'130/80', 74, 98.4, 97, 14, 189.0, 70.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440099',
'Patient presents with palpitations and lightheadedness for 2 days. Heart rate irregular, 110-120 bpm. EKG shows AFib with rapid ventricular response. Blood pressure 145/90. Increased metoprolol from 50mg to 100mg twice daily. Ordered echocardiogram. Cardiology consultation. Return if chest pain, syncope, or worsening symptoms.',
'APPROVED', DATEADD('DAY', -7, CURRENT_TIMESTAMP),
'145/90', 118, 98.6, 95, 18, 190.0, 70.0);

-- Emily Thompson (Mental health patient) - 4 visits showing treatment progression
INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440013', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440099',
'Patient reports increased anxiety and panic attacks over past month. Difficulty sleeping, racing thoughts, constant worry. Started new job 2 months ago, high stress. Denies suicidal ideation, no homicidal ideation. PHQ-9 score 16, GAD-7 score 18. Started sertraline 50mg daily. Referred to therapist for CBT. Discussed sleep hygiene. Follow up 2 weeks.',
'APPROVED', DATEADD('DAY', -120, CURRENT_TIMESTAMP),
'128/82', 88, 98.6, 99, 18, 135.0, 65.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440014', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440099',
'Two week follow-up on sertraline. Patient reports mild improvement in anxiety. Sleeping slightly better. No side effects from medication. Started therapy sessions, finding helpful. PHQ-9 score 14, GAD-7 score 15. Continue sertraline 50mg. Discussed increasing dose at next visit if needed. Encouraged continued therapy.',
'APPROVED', DATEADD('DAY', -105, CURRENT_TIMESTAMP),
'124/80', 82, 98.4, 99, 16, 135.0, 65.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440015', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440099',
'Follow-up at 6 weeks. Patient reports significant improvement in mood and anxiety. Sleeping better, panic attacks reduced from daily to once weekly. Therapy going well, learning coping strategies. PHQ-9 score 9, GAD-7 score 10. Increased sertraline to 75mg daily for further improvement. Continue therapy. Follow up 4 weeks.',
'APPROVED', DATEADD('DAY', -75, CURRENT_TIMESTAMP),
'122/78', 76, 98.4, 99, 16, 136.0, 65.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440016', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440099',
'Three month follow-up. Patient doing very well. Anxiety well controlled, no panic attacks in past month. Mood stable, enjoying work and social activities. PHQ-9 score 4, GAD-7 score 5. Continue sertraline 75mg daily. Continue therapy monthly for maintenance. Discussed long-term treatment plan. Follow up 3 months.',
'APPROVED', DATEADD('DAY', -30, CURRENT_TIMESTAMP),
'120/76', 72, 98.6, 99, 14, 137.0, 65.0);

-- James Anderson (COPD patient) - 4 visits showing disease management
INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440017', '550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440099',
'Patient presents with productive cough, yellow-green sputum for 5 days. Increased dyspnea on exertion. Low-grade fever 99.8F. Using nebulizer treatments more frequently. Quit smoking 10 years ago, 40 pack-year history. Chest feels congested. Lungs with diffuse wheezes and rhonchi. O2 sat 90% on room air. COPD exacerbation with possible bacterial bronchitis. Started azithromycin 500mg day 1, then 250mg days 2-5. Prednisone 40mg daily for 5 days. Increased albuterol nebulizer to every 4 hours.',
'APPROVED', DATEADD('DAY', -100, CURRENT_TIMESTAMP),
'142/86', 84, 99.2, 90, 24, 172.0, 68.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440018', '550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440099',
'Follow-up after COPD exacerbation. Completed antibiotics and prednisone. Breathing improved, cough decreased. Sputum now clear. O2 sat 94% on room air. Lungs with decreased wheezes. Discussed pulmonary rehabilitation referral. Emphasized importance of flu and pneumonia vaccines. Continue tiotropium and fluticasone/salmeterol inhalers. Rescue albuterol as needed.',
'APPROVED', DATEADD('DAY', -85, CURRENT_TIMESTAMP),
'138/82', 78, 98.4, 94, 18, 171.0, 68.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440019', '550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440099',
'Routine COPD follow-up. Patient stable on current regimen. Mild dyspnea with moderate exertion. Using rescue inhaler 2-3 times weekly. Completed pulmonary rehab program, reports improved exercise tolerance. O2 sat 95%. Spirometry shows FEV1 55% predicted, stable. Also reports right knee pain from osteoarthritis. Started acetaminophen 1000mg three times daily. Avoid NSAIDs due to cardiac history. Continue COPD medications.',
'APPROVED', DATEADD('DAY', -45, CURRENT_TIMESTAMP),
'136/80', 76, 98.6, 95, 16, 170.0, 68.0);

INSERT INTO consultations (id, patient_id, clinician_id, raw_transcript, state, created_at,
    blood_pressure, heart_rate, temperature, oxygen_saturation, respiratory_rate, weight, height) VALUES
('650e8400-e29b-41d4-a716-446655440020', '550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440099',
'Patient presents for flu vaccine. Feeling well today. No acute respiratory symptoms. Administered influenza vaccine. Discussed importance of annual vaccination given COPD. Also due for pneumococcal vaccine, administered PPSV23. Reviewed inhaler technique, using correctly. Continue current medications. Follow up in 3 months or sooner if symptoms worsen.',
'APPROVED', DATEADD('DAY', -14, CURRENT_TIMESTAMP),
'134/78', 74, 98.4, 96, 14, 169.0, 68.0);
