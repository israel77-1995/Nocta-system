-- Add vital signs columns to consultations table
ALTER TABLE consultations ADD COLUMN IF NOT EXISTS blood_pressure VARCHAR(20);
ALTER TABLE consultations ADD COLUMN IF NOT EXISTS heart_rate INTEGER;
ALTER TABLE consultations ADD COLUMN IF NOT EXISTS temperature DOUBLE PRECISION;
ALTER TABLE consultations ADD COLUMN IF NOT EXISTS oxygen_saturation INTEGER;
ALTER TABLE consultations ADD COLUMN IF NOT EXISTS respiratory_rate INTEGER;
ALTER TABLE consultations ADD COLUMN IF NOT EXISTS weight DOUBLE PRECISION;
ALTER TABLE consultations ADD COLUMN IF NOT EXISTS height DOUBLE PRECISION;
