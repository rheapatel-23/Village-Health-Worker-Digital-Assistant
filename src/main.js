// State management
const state = {
    role: 'asha', // 'asha' or 'doctor'
    currentView: 'login',
    lang: 'en', // 'en' or 'hi'
    user: null,
    patients: [],
    alerts: [],
    isSyncing: false,
    isRecording: false,
    isOnline: navigator.onLine
};

// Translations
const translations = {
    en: {
        app_name: "ASHA Saathi App",
        tagline: "ASHA Worker Digital Assistant",
        login: "Login",
        asha_worker: "ASHA Worker",
        doctor: "Doctor",
        gmail: "Gmail",
        password: "Password",
        forgot: "Forgot?",
        secure_access: "Secure access for authorized users only.",
        namaste: "Namaste",
        village_overview: "Your village health overview for today.",
        add_patient: "Add Patient",
        pending_sync: "Pending Sync",
        alert_history: "Alert History",
        total_patients: "Total Patients",
        recent_activity: "Recent Activity",
        view_all: "View All",
        search_placeholder: "Search patient by name or ID...",
        registration: "Registration",
        basic_details: "Patient Basic Details",
        name_label: "Name",
        age_label: "Age",
        gender_label: "Gender",
        address_label: "Address",
        next_health: "Next: Health Check",
        health_details: "Health Check Details",
        pregnancy: "Pregnancy Status",
        bp: "Blood Pressure (mmHg)",
        sugar: "Sugar Level (mg/dL)",
        tb: "TB Follow-up Required",
        vaccination: "Vaccination Remarks",
        save_record: "Save Record",
        voice_alert: "Voice Alert",
        tap_to_record: "Tap to record alert voice message",
        critical: "Critical",
        send_alert: "Send Alert",
        report_title: "Monthly Health Report",
        sync_mgmt: "Sync Management",
        no_activity: "No recent activity found.",
        no_alerts: "No active alerts found."
    },
    hi: {
        app_name: "आशा साथी ऐप",
        tagline: "आशा कार्यकर्ता डिजिटल सहायक",
        login: "लॉगिन करें",
        asha_worker: "आशा कार्यकर्ता",
        doctor: "डॉक्टर",
        gmail: "जीमेल / ई-मेल",
        password: "पासवर्ड",
        forgot: "भूल गए?",
        secure_access: "केवल अधिकृत उपयोगकर्ताओं के लिए सुरक्षित पहुंच।",
        namaste: "नमस्ते",
        village_overview: "आज के लिए आपके गांव का स्वास्थ्य अवलोकन।",
        add_patient: "मरीज जोड़ें",
        pending_sync: "सिंक बाकी",
        alert_history: "अलर्ट इतिहास",
        total_patients: "कुल मरीज",
        recent_activity: "हाल की गतिविधि",
        view_all: "सब देखें",
        search_placeholder: "नाम या आईडी से मरीज खोजें...",
        registration: "पंजीकरण",
        basic_details: "मरीज का मूल विवरण",
        name_label: "नाम",
        age_label: "आयु",
        gender_label: "लिंग",
        address_label: "पता",
        next_health: "आगे: स्वास्थ्य जांच",
        health_details: "स्वास्थ्य जांच विवरण",
        pregnancy: "गर्भावस्था की स्थिति",
        bp: "रक्तचाप (mmHg)",
        sugar: "शुगर लेवल (mg/dL)",
        tb: "टीबी फॉलो-अप आवश्यक",
        vaccination: "टीकाकरण टिप्पणी",
        save_record: "रिकॉर्ड सहेजें",
        voice_alert: "वॉयस अलर्ट",
        tap_to_record: "अलर्ट वॉयस संदेश रिकॉर्ड करने के लिए टैप करें",
        critical: "गंभीर",
        send_alert: "अलर्ट भेजें",
        report_title: "मासिक स्वास्थ्य रिपोर्ट",
        sync_mgmt: "सिंक प्रबंधन",
        no_activity: "कोई हाल की गतिविधि नहीं मिली।",
        no_alerts: "कोई सक्रिय अलर्ट नहीं मिला।"
    }
};

const t = (key) => translations[state.lang][key] || key;

// DOM Elements
const mainContent = document.getElementById('main-content');
const appHeader = document.getElementById('app-header');
const bottomNav = document.getElementById('bottom-nav');
const headerTitle = document.getElementById('header-title');
const profilePic = document.getElementById('profile-pic');
const navItems = document.querySelectorAll('.nav-item');
const toast = document.getElementById('toast');
const toastMessage = document.getElementById('toast-message');
const langToggle = document.getElementById('lang-toggle');

// Speech Recognition Setup
const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
let recognition = null;
if (SpeechRecognition) {
    recognition = new SpeechRecognition();
    recognition.continuous = false;
    recognition.lang = state.lang === 'hi' ? 'hi-IN' : 'en-US';
    
    recognition.onresult = (event) => {
        const transcript = event.results[0][0].transcript;
        if (state.currentView === 'voiceAlert') {
            const lt = document.getElementById('live-transcript');
            if (lt) lt.innerText = `"${transcript}"`;
        } else {
            const activeInput = document.activeElement;
            if (activeInput && (activeInput.tagName === 'INPUT' || activeInput.tagName === 'TEXTAREA')) {
                activeInput.value = transcript;
            }
        }
    };

    recognition.onend = () => {
        state.isRecording = false;
        render();
    };
}

// Templates
const templates = {
    login: () => `
        <div class="login-screen fade-in">
            <div class="login-card">
                <div class="header-logo" style="justify-content: center; margin-bottom: 10px;">
                    <i class="fas fa-briefcase-medical" style="font-size: 32px;"></i>
                </div>
                <h2 style="margin-bottom: 5px;">${t('app_name')}</h2>
                <p style="color: var(--text-muted); font-size: 13px; margin-bottom: 24px;">${t('tagline')}</p>
                
                <div class="role-selector">
                    <button class="role-btn ${state.role === 'asha' ? 'active' : ''}" onclick="window.setRole('asha')">${t('asha_worker')}</button>
                    <button class="role-btn ${state.role === 'doctor' ? 'active' : ''}" onclick="window.setRole('doctor')">${t('doctor')}</button>
                </div>

                <div class="input-group" style="text-align: left;">
                    <label>${t('gmail')}</label>
                    <div style="position: relative;">
                        <input type="email" class="input-field" placeholder="example@gmail.com">
                        <i class="fas fa-microphone" onclick="window.startVoice(event)" style="position: absolute; right: 12px; top: 12px; color: var(--primary-color);"></i>
                    </div>
                </div>

                <div class="input-group" style="text-align: left;">
                    <label>${t('password')}</label>
                    <div style="position: relative;">
                        <input type="password" class="input-field" value="........">
                    </div>
                </div>

                <button class="btn btn-primary btn-block" onclick="window.login()" style="margin-top: 10px;">
                    ${t('login')} <i class="fas fa-sign-in-alt"></i>
                </button>

                <p style="font-size: 10px; color: var(--text-muted); margin-top: 30px;">
                    <i class="fas fa-shield-alt"></i> ${t('secure_access')}
                </p>
            </div>
        </div>
    `,

    ashaDashboard: () => `
        <div class="fade-in">
            <h2 style="color: var(--secondary-color); margin-bottom: 4px;">${t('namaste')}, Sunita</h2>
            <p style="color: var(--text-muted); font-size: 13px; margin-bottom: 24px;">${t('village_overview')}</p>

            <div class="dashboard-grid">
                <div class="card stat-card" onclick="window.navigate('patientRegistration')">
                    <div style="width: 40px; height: 40px; background: #E7F0FF; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 10px;">
                        <i class="fas fa-user-plus" style="color: #1D4E89;"></i>
                    </div>
                    <div class="stat-label">${t('add_patient')}</div>
                </div>
                <div class="card stat-card" onclick="window.navigate('sync')">
                    <div class="stat-value" style="color: #D48806;">0</div>
                    <div class="stat-label">${t('pending_sync')}</div>
                </div>
                <div class="card stat-card">
                    <div class="stat-value" style="color: #E63946;">0</div>
                    <div class="stat-label">${t('alert_history')}</div>
                </div>
                <div class="card stat-card">
                    <div class="stat-value">0</div>
                    <div class="stat-label">${t('total_patients')}</div>
                </div>
            </div>

            <div style="display: flex; justify-content: space-between; align-items: center; margin: 20px 0 10px;">
                <h3 style="font-size: 16px;">${t('recent_activity')}</h3>
                <a href="#" style="color: var(--primary-color); font-size: 12px; font-weight: 600; text-decoration: none;">${t('view_all')}</a>
            </div>

            <div class="card" style="padding: 15px; text-align: center;">
                ${state.patients.length > 0 ? state.patients.map(p => `
                    <div class="list-item">
                        <img src="${p.img}" class="list-item-img">
                        <div class="list-item-content">
                            <div class="list-item-name">${p.name}</div>
                            <div class="list-item-sub">ID: #${p.id}-${p.type}</div>
                        </div>
                        <div style="text-align: right;">
                            <div class="badge badge-${p.status.toLowerCase()}">${p.status}</div>
                        </div>
                    </div>
                `).join('') : `
                    <div style="padding: 20px; color: var(--text-muted);">
                        <i class="fas fa-user-slash" style="font-size: 32px; margin-bottom: 10px; display: block;"></i>
                        <p style="font-size: 13px;">${t('no_activity')}</p>
                    </div>
                `}
            </div>

            <div style="position: relative; margin-top: 20px;">
                <input type="text" class="input-field" placeholder="${t('search_placeholder')}" style="padding-right: 45px;">
                <i class="fas fa-microphone" onclick="window.startVoice(event)" style="position: absolute; right: 15px; top: 12px; color: var(--primary-color);"></i>
            </div>
        </div>
    `,

    doctorDashboard: () => `
        <div class="fade-in">
            <h2 style="color: var(--secondary-color); margin-bottom: 4px;">${t('namaste')}, Dr. Mehta</h2>
            <div class="dashboard-grid">
                <div class="card stat-card">
                    <div style="color: var(--accent-color); font-size: 11px; font-weight: 700; margin-bottom: 5px;"><i class="fas fa-bell"></i> New Alerts</div>
                    <div class="stat-value" style="color: var(--accent-color);">0</div>
                </div>
                <div class="card stat-card" onclick="window.navigate('governmentReport')">
                    <div style="color: #1D4E89; font-size: 11px; font-weight: 700; margin-bottom: 5px;"><i class="fas fa-file-invoice"></i> Reports</div>
                    <div class="stat-value" style="color: #1D4E89;">0</div>
                </div>
            </div>

            <div class="card" style="padding: 15px; text-align: center; margin-top: 20px;">
                ${state.alerts.length > 0 ? state.alerts.map(a => `
                    <div class="list-item" onclick="window.navigate('alertDetails')">
                        <div class="list-item-name">${a.name}</div>
                    </div>
                `).join('') : `
                    <div style="padding: 20px; color: var(--text-muted);">
                        <i class="fas fa-bell-slash" style="font-size: 32px; margin-bottom: 10px; display: block;"></i>
                        <p style="font-size: 13px;">${t('no_alerts')}</p>
                    </div>
                `}
            </div>
        </div>
    `,

    patientRegistration: () => `
        <div class="fade-in">
            <h2>${t('basic_details')}</h2>
            <div class="input-group">
                <label>${t('name_label')}</label>
                <input type="text" class="input-field">
            </div>
            <button class="btn btn-primary btn-block" onclick="window.navigate('healthCheck')">${t('next_health')}</button>
        </div>
    `,

    healthCheck: () => `
        <div class="fade-in">
            <h2>${t('health_details')}</h2>
            <div class="input-group">
                <label>${t('bp')}</label>
                <input type="number" class="input-field" id="systolic" placeholder="Systolic">
                <input type="number" class="input-field" id="diastolic" placeholder="Diastolic">
            </div>
            <button class="btn btn-primary btn-block" onclick="window.saveRecord()">${t('save_record')}</button>
        </div>
    `,

    voiceAlert: () => `
        <div class="fade-in">
            <h2 style="text-align: center;">${t('voice_alert')}</h2>
            <div class="voice-record-container" onclick="window.toggleRecording()" style="text-align: center; margin: 30px 0;">
                <div class="mic-circle ${state.isRecording ? 'pulse' : ''}" style="${state.isRecording ? 'background: #E63946;' : 'background: #6c757d;'} width: 80px; height: 80px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto; color: white; font-size: 30px;">
                    <i class="fas fa-microphone"></i>
                </div>
            </div>
            <div class="transcription-box" style="background: #f8f9fa; padding: 15px; border-radius: 8px; min-height: 80px; border-left: 4px solid var(--accent-color);">
                <div id="live-transcript" style="font-size: 16px; font-weight: 600;">${state.isRecording ? 'Listening...' : t('tap_to_record')}</div>
            </div>
            <button class="btn btn-secondary btn-block" style="margin-top: 20px;" onclick="window.sendAlert()">${t('send_alert')}</button>
        </div>
    `,

    sync: () => `
        <div class="fade-in">
            <i class="fas fa-arrow-left" onclick="window.navigate('dashboard')"></i>
            <h2>${t('sync_mgmt')}</h2>
            <div class="card" style="text-align: center; padding: 20px;">
                <i class="fas fa-cloud-upload-alt" style="font-size: 40px; color: var(--primary-color);"></i>
                <h3>0 Pending Records</h3>
            </div>
        </div>
    `,

    governmentReport: () => `
        <div class="fade-in">
            <i class="fas fa-arrow-left" onclick="window.navigate('dashboard')"></i>
            <h2>${t('report_title')}</h2>
            <p style="color: var(--text-muted); text-align: center; padding: 40px;">No reports available for the current month.</p>
        </div>
    `,

    alertDetails: () => `<div class="fade-in"><h2>Alert Details</h2><p>No alert data to display.</p></div>`
};

// Core Functions
window.toggleLang = () => {
    state.lang = state.lang === 'en' ? 'hi' : 'en';
    langToggle.innerText = state.lang === 'en' ? 'हिंदी' : 'English';
    if (recognition) recognition.lang = state.lang === 'hi' ? 'hi-IN' : 'en-US';
    render();
};

window.startVoice = (e) => {
    if (!recognition) { alert("Speech recognition not supported."); return; }
    e.stopPropagation();
    state.isRecording = true;
    recognition.start();
    window.showToast("Listening...");
};

window.toggleRecording = () => {
    if (state.isRecording) recognition.stop();
    else { state.isRecording = true; recognition.start(); }
    render();
};

window.navigate = (view) => { state.currentView = view; render(); };
window.setRole = (role) => { state.role = role; render(); };
window.login = () => { state.currentView = 'dashboard'; render(); };
window.saveRecord = () => {
    const sys = document.getElementById('systolic')?.value;
    if (sys > 140) window.navigate('voiceAlert');
    else { window.showToast('Record saved!'); window.navigate('dashboard'); }
};
window.sendAlert = () => { window.showToast('SMS Sent!'); window.navigate('dashboard'); };
window.triggerSync = (isAuto = false) => {
    if (state.isSyncing || !state.isOnline) return;
    state.isSyncing = true; render();
    if (isAuto) window.showToast('Auto-syncing...');
    setTimeout(() => { state.isSyncing = false; window.showToast('Synced!'); render(); }, 3000);
};

window.updateConnectionUI = () => {
    const statusBar = document.querySelector('.status-bar');
    const statusText = document.getElementById('connection-status');
    if (statusBar && statusText) {
        if (state.isOnline) {
            statusBar.style.backgroundColor = 'var(--primary-color)';
            statusText.innerText = state.lang === 'hi' ? 'कनेक्टेड / ऑनलाइन' : 'Connected / Online';
        } else {
            statusBar.style.backgroundColor = '#6c757d';
            statusText.innerText = state.lang === 'hi' ? 'ऑफलाइन / डिस्कनेक्टेड' : 'Offline / Disconnected';
        }
    }
};

window.showToast = (msg) => {
    toastMessage.innerText = msg; toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), 3000);
};

// Event Listeners
window.addEventListener('online', () => { state.isOnline = true; window.updateConnectionUI(); window.triggerSync(true); });
window.addEventListener('offline', () => { state.isOnline = false; window.updateConnectionUI(); });
langToggle.onclick = window.toggleLang;
navItems.forEach(item => item.addEventListener('click', () => window.navigate(item.dataset.view)));

function render() {
    let viewKey = state.currentView;
    if (viewKey === 'dashboard') viewKey = state.role === 'asha' ? 'ashaDashboard' : 'doctorDashboard';
    
    if (state.currentView === 'login') {
        appHeader.style.display = 'none'; bottomNav.style.display = 'none';
    } else {
        appHeader.style.display = 'flex'; bottomNav.style.display = 'flex';
        headerTitle.innerText = state.role === 'asha' ? 'ASHA Saathi' : 'CareSync';
    }
    
    navItems.forEach(item => {
        item.classList.toggle('active', item.dataset.view === state.currentView);
        if (item.dataset.view === 'sync') item.style.display = state.role === 'doctor' ? 'none' : 'flex';
    });

    mainContent.innerHTML = templates[viewKey] ? templates[viewKey]() : `<h2>View not found: ${viewKey}</h2>`;
    window.updateConnectionUI();

    if (viewKey === 'sync' && state.isOnline && !state.isSyncing) {
        setTimeout(() => window.triggerSync(true), 500);
    }
}

render();
