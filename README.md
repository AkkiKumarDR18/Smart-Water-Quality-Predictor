# 💧 Smart Water Quality Predictor

A Full-Stack Machine Learning application that predicts water potability using a Random Forest model. Features a Java Swing GUI, real-time Python integration, and MySQL database logging.

### *An Integrated Machine Learning System with Java GUI & MySQL Database*

## 🌟 Project Overview
The **Smart Water Quality Predictor** is a full-stack data science application designed to classify water as potable (safe for drinking) or non-potable based on its chemical properties. 

Unlike standard Machine Learning projects that stay inside a Jupyter Notebook, this project is a **functional end-to-end application**. It bridges a **Java Swing Frontend**, a **MySQL Database**, and a **Python Machine Learning Backend** to provide a real-time decision-making tool.

## 🏗️ System Architecture & Workflow
The project demonstrates seamless communication between different layers of a modern tech stack:

1.  **Frontend (Java Swing):** A user-friendly desktop GUI where users input water parameters (pH, Hardness, etc.).
2.  **Logic Layer (Java to Python):** Java uses `ProcessBuilder` to trigger a Python script (`predict.py`) and passes user inputs as arguments.
3.  **Intelligence Layer (Python/ML):** A **Random Forest Classifier** processes the inputs using a pre-trained model and a saved `StandardScaler` to return a prediction.
4.  **Data Layer (MySQL):** Every tested sample is logged into a MySQL database via **JDBC** for historical tracking and auditing.

## 💡 Key Features
* **Real-Time Classification:** Instantly predicts water safety using a model trained on 3,000+ real-world water samples.
* **WHO Safety Alerts:** Includes built-in logic to warn users if parameters (like pH or Turbidity) fall outside of **World Health Organization (WHO)** recommended safety ranges.
* **Data Persistence:** Uses a relational database to store every input and its corresponding prediction result.
* **Robust Pre-processing:** Handles missing/zero values in the training dataset using median imputation to ensure model reliability.

## 🛠️ Tech Stack
* **Programming Languages:** Java, Python, SQL
* **Machine Learning:** Scikit-Learn (Random Forest), Pandas, NumPy
* **Database:** MySQL (JDBC)
* **GUI Library:** Java Swing / AWT

## 📂 Repository Structure
```text
├── java_app/
│   ├── WaterQualityGUI.java    # Main Desktop Application
│   └── background1.jpg         # GUI Background
├── ml_model/
│   ├── train_model.py          # Model Training Script
│   ├── predict.py              # Real-time Prediction Script
│   ├── water_quality_model.pkl # Saved Random Forest Model
│   └── scaler.pkl              # Saved Feature Scaler
├── data/
│   ├── water_potability.csv    # Training Dataset
│   └── database_schema.sql     # MySQL Table Setup
├── requirements.txt            # Python Dependencies
└── README.md                   # Project Documentation

### 📸 Preview
<img width="601" height="803" alt="Screenshot from 2026-03-17 20-55-11" src="https://github.com/user-attachments/assets/31f2d439-8667-4c6e-91a5-88b3b6fccc2c" />


