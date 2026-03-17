-- 1. Create the Database
CREATE DATABASE IF NOT EXISTS water_quality_db;
USE water_quality_db;

-- 2. Create the Table
CREATE TABLE IF NOT EXISTS water_quality_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ph FLOAT,
    hardness FLOAT,
    solids FLOAT,
    chloramines FLOAT,
    sulfate FLOAT,
    conductivity FLOAT,
    organic_carbon FLOAT,
    trihalomethanes FLOAT,
    turbidity FLOAT,
    result VARCHAR(50), -- This stores 'Potable' or 'No'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Verify the table structure
DESCRIBE water_quality_data;
