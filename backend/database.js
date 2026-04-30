const sqlite3 = require('sqlite3').verbose();
const path = require('path');

const dbPath = path.resolve(__dirname, 'ashasaathi.db');
const db = new sqlite3.Database(dbPath, (err) => {
    if (err) {
        console.error("Error opening database " + err.message);
    } else {
        console.log("Connected to the SQLite database.");
        
        db.serialize(() => {
            // Tables are created below if they don't exist.
            
            // Create Users Table (for authentication)
            db.run(`CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE,
                password TEXT,
                role TEXT
            )`);

            // Create Patients Table
            db.run(`CREATE TABLE IF NOT EXISTS patients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                age INTEGER,
                gender TEXT,
                address TEXT,
                systolicBP INTEGER,
                diastolicBP INTEGER,
                temperature TEXT,
                hemoglobin TEXT,
                pregnancyStatus TEXT,
                sugarLevel INTEGER,
                tbFollowup TEXT,
                vaccinationRemarks TEXT,
                reportedSymptoms TEXT,
                status TEXT DEFAULT 'Synced',
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            )`);

            // Create Alerts Table (Critical Risks)
            db.run(`CREATE TABLE IF NOT EXISTS alerts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                patientId INTEGER,
                riskFactor TEXT,
                transcript TEXT,
                status TEXT DEFAULT 'Pending',
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(patientId) REFERENCES patients(id)
            )`);

            console.log("Database tables created or verified successfully.");
            
            db.serialize(() => {
                db.run(`INSERT OR IGNORE INTO users (email, password, role) VALUES (?, ?, ?)`, ["Riya", "password", "asha"]);
                db.run(`INSERT OR IGNORE INTO users (email, password, role) VALUES (?, ?, ?)`, ["Komala", "password", "asha"]);
                db.run(`INSERT OR IGNORE INTO users (email, password, role) VALUES (?, ?, ?)`, ["Nanditha", "password", "asha"]);
                db.run(`INSERT OR IGNORE INTO users (email, password, role) VALUES (?, ?, ?)`, ["riya@gmail.com", "password", "asha"]);
                db.run(`INSERT OR IGNORE INTO users (email, password, role) VALUES (?, ?, ?)`, ["komala@gmail.com", "password", "asha"]);
                db.run(`INSERT OR IGNORE INTO users (email, password, role) VALUES (?, ?, ?)`, ["nanditha@gmail.com", "password", "asha"]);
                db.run(`INSERT OR IGNORE INTO users (email, password, role) VALUES (?, ?, ?)`, ["example@gmail.com", "password", "asha"]);
                db.run(`INSERT OR IGNORE INTO users (email, password, role) VALUES (?, ?, ?)`, ["doctor@gmail.com", "password", "doctor"]);
                db.run(`INSERT OR IGNORE INTO users (email, password, role) VALUES (?, ?, ?)`, ["sarah@gmail.com", "password", "doctor"]);
            });
        });
    }
});

module.exports = db;
