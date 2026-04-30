const sqlite3 = require('sqlite3').verbose();
const path = require('path');

const dbPath = path.resolve(__dirname, 'backend', 'ashasaathi.db');
const db = new sqlite3.Database(dbPath);

db.serialize(() => {
    console.log("Checking and inserting users...");
    const users = [
        ["Riya", "password", "asha"],
        ["Komala", "password", "asha"],
        ["Nanditha", "password", "asha"],
        ["riya@gmail.com", "password", "asha"],
        ["komala@gmail.com", "password", "asha"],
        ["nanditha@gmail.com", "password", "asha"],
        ["example@gmail.com", "password", "asha"],
        ["doctor@gmail.com", "password", "doctor"]
    ];

    users.forEach(user => {
        db.run(`INSERT OR REPLACE INTO users (email, password, role) VALUES (?, ?, ?)`, user, (err) => {
            if (err) console.error(err.message);
            else console.log(`User ${user[0]} ready.`);
        });
    });
});

db.close();
