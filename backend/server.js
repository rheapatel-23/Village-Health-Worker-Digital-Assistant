const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const db = require('./database');

const app = express();
const PORT = process.env.PORT || 3001;
const path = require('path');
const dashboardDir = path.join(__dirname, '../dashboard');

app.use(bodyParser.json());
app.use(cors());

// Serve the web-based dashboard directly from this backend
app.use('/dashboard', express.static(dashboardDir));

// Fallback route: some environments can miss static resolution for nested files.
// This guarantees /dashboard/index.html and similar URLs always resolve.
app.get('/dashboard/:file', (req, res) => {
    res.sendFile(path.join(dashboardDir, req.params.file));
});

// --- SELF-HEALING DATABASE MIGRATION ---
// This ensures that even if the database is old, we add the missing columns.
const migrateDB = () => {
    const columns = [
        "temperature TEXT",
        "hemoglobin TEXT",
        "pregnancyStatus TEXT",
        "sugarLevel INTEGER DEFAULT 0",
        "tbFollowup TEXT DEFAULT 'No'",
        "vaccinationRemarks TEXT",
        "reportedSymptoms TEXT"
    ];
    columns.forEach(col => {
        db.run(`ALTER TABLE patients ADD COLUMN ${col}`, (err) => {
            if (err) {
                // Column likely already exists, ignore error
            } else {
                console.log(`Migration: Added column ${col}`);
            }
        });
    });
};
migrateDB();
// ----------------------------------------

// Logging middleware
app.use((req, res, next) => {
    console.log(`${new Date().toLocaleTimeString()} - ${req.method} ${req.url}`);
    next();
});

// 1. Authentication Endpoint
app.post('/api/login', (req, res) => {
    // FORCE UPGRADE DATABASE UPON LOGIN
    migrateDB();
    
    const { email, password, role } = req.body;
    // Strip @gmail.com for checking against hardcoded names if needed
    const shortName = email.split('@')[0];
    
    const sql = `
        SELECT * FROM users 
        WHERE (LOWER(email) = LOWER(?) OR LOWER(email) = LOWER(?)) 
        AND password = ? 
        AND role = ?
    `;
    
    db.get(sql, [email, shortName, password, role], (err, row) => {
        if (err) {
            res.status(500).json({ error: err.message });
        } else if (row) {
            res.json({ message: "Login successful", user: { id: row.id, email: row.email, role: row.role } });
        } else {
            res.status(401).json({ error: "Invalid credentials" });
        }
    });
});

// 2. Add a new Patient (ASHA Syncing Data)
app.post('/api/patients', (req, res) => {
    const { 
        name, age, gender, address, systolicBP, diastolicBP, 
        temperature, hemoglobin, pregnancyStatus, 
        sugarLevel = 0, tbFollowup = 'No', vaccinationRemarks, reportedSymptoms 
    } = req.body;
    
    const sql = `INSERT INTO patients (name, age, gender, address, systolicBP, diastolicBP, temperature, hemoglobin, pregnancyStatus, sugarLevel, tbFollowup, vaccinationRemarks, reportedSymptoms) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`;
    const params = [
        name || 'Unknown', 
        age || 0, 
        gender || 'F', 
        address || 'Village', 
        systolicBP || 120, 
        diastolicBP || 80, 
        temperature || '98.6', 
        hemoglobin || '12.0', 
        pregnancyStatus || 'Normal', 
        sugarLevel, 
        tbFollowup, 
        vaccinationRemarks || 'None', 
        reportedSymptoms || 'None'
    ];
    
    db.run(sql, params, function(err) {
        if (err) {
            console.error("Database Error:", err.message);
            res.status(400).json({ error: "DB Error: " + err.message });
        } else {
            res.json({
                message: "Patient record saved successfully",
                data: { id: this.lastID, ...req.body },
                id: this.lastID
            });
        }
    });
});

// 3. Get all Patients (Doctor Dashboard)
app.get('/api/patients', (req, res) => {
    // LIVE MIGRATION: Trigger column checks every time doctor refreshes
    migrateDB();
    
    db.all(`SELECT * FROM patients ORDER BY timestamp DESC`, [], (err, rows) => {
        if (err) {
            res.status(400).json({ error: err.message });
        } else {
            res.json({ data: rows });
        }
    });
});

// 4. Trigger Voice Alert (High Risk)
app.post('/api/alerts', (req, res) => {
    const { patientId, riskFactor, transcript } = req.body;
    const sql = `INSERT INTO alerts (patientId, riskFactor, transcript) VALUES (?, ?, ?)`;
    
    db.run(sql, [patientId, riskFactor, transcript], function(err) {
        if (err) {
            res.status(400).json({ error: err.message });
        } else {
            res.json({ message: "Alert triggered successfully!", id: this.lastID });
        }
    });
});

// 4b. Sync Local Alerts
app.post('/api/alerts/sync', (req, res) => {
    const { patientName = 'Unknown', riskFactor = 'Critical', timestamp } = req.body;
    db.get(`SELECT id FROM patients WHERE name = ?`, [patientName], (err, patient) => {
        const patientId = patient ? patient.id : null;
        const sql = `INSERT INTO alerts (patientId, riskFactor, transcript) VALUES (?, ?, ?)`;
        db.run(sql, [patientId, riskFactor, `Auto-Alert: ${patientName}`], function(err) {
            if (err) {
                res.status(400).json({ error: err.message });
            } else {
                res.json({ message: "Alert synced successfully", id: this.lastID });
            }
        });
    });
});

// 5. Get Active Alerts (Doctor Dashboard)
app.get('/api/alerts', (req, res) => {
    const sql = `
        SELECT alerts.id, alerts.riskFactor, alerts.transcript, alerts.timestamp, 
               COALESCE(patients.name, 'Emergency Case') as patientName 
        FROM alerts 
        LEFT JOIN patients ON alerts.patientId = patients.id 
        ORDER BY alerts.timestamp DESC
    `;
    db.all(sql, [], (err, rows) => {
        if (err) {
            res.status(400).json({ error: err.message });
        } else {
            res.json({ data: rows });
        }
    });
});

// 6. Monthly Government Report Summary
app.get('/api/reports/monthly', (req, res) => {
    // Simple aggregation for demonstration
    const sql = `
        SELECT 
            (SELECT COUNT(*) FROM patients) as totalPatients,
            (SELECT COUNT(*) FROM alerts) as totalAlerts,
            (SELECT COUNT(*) FROM patients WHERE systolicBP > 140) as highBPCases
    `;
    db.get(sql, [], (err, row) => {
        if (err) {
            res.status(400).json({ error: err.message });
        } else {
            res.json({
                month: new Date().toLocaleString('default', { month: 'long', year: 'numeric' }),
                summary: row
            });
        }
    });
});

// Start Server
app.listen(PORT, '0.0.0.0', () => {
    console.log(`ASHA Saathi Backend API is running on http://0.0.0.0:${PORT}`);
});
