document.addEventListener("DOMContentLoaded", () => {
    // API Configuration
    const API_BASE = "http://localhost:3001/api";

    // Authentication & Session Logic
    const savedUser = localStorage.getItem('aarogyaNetUser');
    const welcomeMessage = document.getElementById('welcomeMessage');
    
    if (savedUser && welcomeMessage) {
        welcomeMessage.innerHTML = `Supervisor: <span style="color: var(--primary);">${savedUser}</span>`;
    }

    const authBtns = document.querySelectorAll('.auth-btn');
    authBtns.forEach(btn => {
        if (savedUser) {
            btn.innerHTML = `<i class="fa-solid fa-right-from-bracket"></i> Sign Out`;
            btn.href = "#";
            btn.style.color = "var(--red-alert)";
            btn.style.background = "rgba(239, 68, 68, 0.1)";
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                localStorage.removeItem('aarogyaNetUser');
                window.location.href = "login.html";
            });
        }
    });

    // Sidebar & UI Logic
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');
    const sidebarToggle = document.getElementById('sidebarToggle');

    function updateSidebarIcon(isCollapsed) {
        if (!sidebarToggle) return;
        sidebarToggle.innerHTML = isCollapsed 
            ? '<i class="fa-solid fa-bars" title="Open Sidebar"></i>' 
            : '<i class="fa-solid fa-chevron-left" title="Close Sidebar"></i>';
    }

    const isInitiallyCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
    if (isInitiallyCollapsed) {
        sidebar?.classList.add('collapsed');
        mainContent?.classList.add('expanded');
    }
    updateSidebarIcon(isInitiallyCollapsed);

    sidebarToggle?.addEventListener('click', () => {
        const isCollapsed = sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('expanded');
        localStorage.setItem('sidebarCollapsed', isCollapsed);
        updateSidebarIcon(isCollapsed);
    });

    // Date Display
    const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const currentDateEl = document.getElementById("currentDate");
    if (currentDateEl) {
        currentDateEl.innerText = new Date().toLocaleDateString('en-US', dateOptions);
    }

    // Data State
    let patientsData = [];
    let alertsData = [];
    let searchQuery = "";
    let lastPatientsRenderSignature = "";

    // Global Risk Calculator
    function getRisk(p) {
        if (p.systolicBP > 140 || p.diastolicBP > 90) return "HIGH";
        if (p.hemoglobin && parseFloat(p.hemoglobin) < 10) return "HIGH";
        if (p.temperature && parseFloat(p.temperature) > 100) return "HIGH";
        return "LOW";
    }

    // Fetch Live Data
    async function fetchLiveData() {
        try {
            // Fetch Patients
            const pResponse = await fetch(`${API_BASE}/patients`);
            if (!pResponse.ok) {
                throw new Error(`Patients API failed: ${pResponse.status}`);
            }
            const pData = await pResponse.json();
            const patientRows = Array.isArray(pData?.data) ? pData.data : [];
            
            // Map backend data to dashboard format
            patientsData = patientRows.map(p => ({
                name: p.name,
                age: p.age,
                gender: p.gender,
                id: `P-${p.id}`,
                address: p.address,
                asha: "Area Worker", // Derived from ASHA assignment
                bp: `${p.systolicBP}/${p.diastolicBP}`,
                temp: `${p.temperature}°F`,
                hb: p.hemoglobin,
                pregnancy: p.pregnancyStatus || "N/A",
                vaccines: p.vaccinationRemarks || "None",
                symptoms: p.reportedSymptoms || "None",
                sugar: p.sugarLevel || 0,
                tb: p.tbFollowup || "No",
                timestamp: p.timestamp
            }));

            // Fetch Alerts
            const aResponse = await fetch(`${API_BASE}/alerts`);
            if (!aResponse.ok) {
                throw new Error(`Alerts API failed: ${aResponse.status}`);
            }
            const aData = await aResponse.json();
            alertsData = Array.isArray(aData?.data) ? aData.data : [];

            updateUI();
        } catch (error) {
            console.error("Failed to fetch live data:", error);
            const syncText = document.getElementById('syncText');
            const syncStatus = document.getElementById('syncStatus');
            if (syncText) syncText.innerText = "Sync Error - Check Backend";
            if (syncStatus) syncStatus.classList.add('offline');
            renderPatientsGrid([]);
        }
    }

    function updateUI() {
        const filteredPatients = getFilteredPatients();
        renderTable(filteredPatients);
        renderPatientsGrid(filteredPatients);
        renderAlertsList(alertsData);
        updateKPIs();
        renderCharts();
    }

    function getFilteredPatients() {
        const query = searchQuery.trim().toLowerCase();
        if (!query) return patientsData;

        return patientsData.filter(p =>
            p.name.toLowerCase().includes(query) ||
            p.id.toLowerCase().includes(query) ||
            p.symptoms.toLowerCase().includes(query) ||
            p.address.toLowerCase().includes(query) ||
            p.bp.toLowerCase().includes(query) ||
            p.temp.toLowerCase().includes(query) ||
            p.hb.toLowerCase().includes(query) ||
            p.vaccines.toLowerCase().includes(query) ||
            p.pregnancy.toLowerCase().includes(query)
        );
    }

    function updateKPIs() {
        const kpiTotal = document.getElementById("kpiTotal");
        const kpiHighRisk = document.getElementById("kpiHighRisk");
        const kpiPregnant = document.getElementById("kpiPregnant");
        const aiAlertCount = document.getElementById("aiAlertCount");
        const aiAlertText = document.getElementById("aiAlertText");

        if (kpiTotal) kpiTotal.innerText = patientsData.length;
        
        const highRiskCount = patientsData.filter(p => getRisk(p) === "HIGH").length;
        if (kpiHighRisk) kpiHighRisk.innerText = highRiskCount;
        
        if (aiAlertCount) aiAlertCount.innerText = `${highRiskCount} critical cases`;
        if (aiAlertText && highRiskCount === 0) {
            aiAlertText.innerHTML = `System Stable. <span style="color:var(--green-safe); font-weight:600;">No high-risk cases detected in latest sync.</span>`;
        } else if (aiAlertText) {
            aiAlertText.innerHTML = `System has flagged <span class="highlight-red">${highRiskCount} high-risk cases</span>. SMS alerts dispatched. <a href="alerts.html">Review Alerts &rarr;</a>`;
        }

        if (kpiPregnant) {
            kpiPregnant.innerText = patientsData.filter(p => p.pregnancy && p.pregnancy !== "N/A" && p.pregnancy !== "Normal").length;
        }
    }

    function renderTable(data) {
        const tableBody = document.getElementById("tableBody");
        if (!tableBody) return;

        tableBody.innerHTML = "";
        data.forEach((p, idx) => {
            const risk = getRisk(p);
            const riskBadge = risk === "HIGH" 
                ? "<span class='badge danger'><i class='fa-solid fa-triangle-exclamation'></i> HIGH RISK</span>"
                : "<span class='badge success'><i class='fa-solid fa-check'></i> NORMAL</span>";
            
            const isHighBP = parseInt(p.bp.split('/')[0]) > 140;
            const isLowHb = parseFloat(p.hb) < 10.0;

            const row = document.createElement("tr");
            row.innerHTML = `
                <td>
                    <div class="patient-info">
                        <span class="patient-name">${p.name}</span>
                        <span class="patient-meta">ID: ${p.id} • Age: ${p.age} • ${p.gender}</span>
                    </div>
                </td>
                <td>
                    <div style="display: flex; gap: 6px; flex-wrap: wrap;">
                        <span class="vital-info ${isHighBP ? 'vital-high' : ''}"><i class="fa-solid fa-heart-pulse"></i> ${p.bp}</span>
                        <span class="vital-info"><i class="fa-solid fa-temperature-half"></i> ${p.temp}</span>
                        <span class="vital-info ${isLowHb ? 'vital-high' : ''}"><i class="fa-solid fa-droplet"></i> Hb: ${p.hb}</span>
                    </div>
                </td>
                <td>
                    <div style="display: flex; flex-direction: column; gap: 4px; align-items: flex-start;">
                        <span class='badge neutral'>${p.pregnancy}</span>
                        <span style="font-size: 11px; color: #0284c7;"><i class="fa-solid fa-syringe"></i> ${p.vaccines}</span>
                    </div>
                </td>
                <td style="color: #64748b; font-size: 13px;">${p.symptoms}</td>
                <td><span style="font-weight: 500; font-size: 13px;">${p.address}</span></td>
                <td>${riskBadge}</td>
            `;
            tableBody.appendChild(row);
        });
    }

    function renderAlertsList(data) {
        const alertsList = document.getElementById("alertsList");
        if (!alertsList) return;
        alertsList.innerHTML = "";
        
        if (data.length === 0) {
            alertsList.innerHTML = "<p style='color: var(--text-muted); padding: 20px;'>No active alerts.</p>";
            return;
        }

        data.forEach(alert => {
            const date = new Date(parseInt(alert.timestamp) || alert.timestamp).toLocaleString();
            const alertCard = document.createElement("div");
            alertCard.style = "background: var(--bg-card); padding: 20px; border-radius: 12px; border: 1px solid var(--border); border-left: 4px solid var(--red-alert);";
            alertCard.innerHTML = `
                <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                    <strong style="color: var(--red-alert);">${alert.riskFactor}</strong>
                    <span style="font-size: 13px; color: var(--text-muted);">${date}</span>
                </div>
                <p style="font-size: 14px;"><strong>Patient:</strong> ${alert.patientName} <br><strong>Transcript:</strong> ${alert.transcript || 'N/A'}</p>
            `;
            alertsList.appendChild(alertCard);
        });
    }

    window.openModal = function(title, content) {
        const overlay = document.createElement('div');
        overlay.className = 'modal-overlay';
        overlay.innerHTML = `
            <div class="modal-box">
                <div class="modal-header">
                    <h3>${title}</h3>
                    <button class="modal-close" onclick="this.closest('.modal-overlay').remove()"><i class="fa-solid fa-xmark"></i></button>
                </div>
                <div class="modal-body">${content}</div>
            </div>
        `;
        document.body.appendChild(overlay);
    };

    function renderPatientsGrid(data) {
        const patientsGrid = document.getElementById("patientsGrid");
        if (!patientsGrid) return;
        
        // Avoid unnecessary repaint only when the data actually didn't change.
        // This keeps the UI responsive while still allowing reliable empty/error states.
        const signature = JSON.stringify(
            data.map((p) => `${p.id}-${p.timestamp ?? ""}-${p.name}`)
        );
        if (signature === lastPatientsRenderSignature) {
            return;
        }
        lastPatientsRenderSignature = signature;

        patientsGrid.innerHTML = "";
        
        if (data.length === 0) {
            patientsGrid.innerHTML = "<p style='color: var(--text-muted); padding: 20px;'>No patients found.</p>";
            return;
        }

        data.forEach((p, idx) => {
            const delay = idx * 0.05;
            const card = document.createElement("div");
            card.className = "patient-card";
            card.style.animation = `fade-in 0.5s ease forwards ${delay}s`;
            card.style.opacity = "0";
            
            const risk = getRisk(p);
            const riskBadge = risk === "HIGH" 
                ? "<span class='badge danger'>HIGH RISK</span>"
                : "<span class='badge success'>NORMAL</span>";

            card.innerHTML = `
                <div class="card-header-pt">
                    <div>
                        <h4>${p.name}</h4>
                        <span>ID: ${p.id} • Age: ${p.age} • ${p.gender}</span>
                    </div>
                    ${riskBadge}
                </div>
                <div class="card-body-pt">
                    <div class="pt-stat"><i class="fa-solid fa-house"></i> <strong>Location:</strong> ${p.address}</div>
                    <div class="pt-stat"><i class="fa-solid fa-syringe"></i> <strong>Vaccines:</strong> ${p.vaccines}</div>
                    
                    <div class="vitals-box">
                        <div><span class="v-label">BP</span><span class="v-val">${p.bp}</span></div>
                        <div><span class="v-label">Temp</span><span class="v-val">${p.temp}</span></div>
                        <div><span class="v-label">Hb</span><span class="v-val">${p.hb}</span></div>
                    </div>
                    
                    <div class="pt-stat" style="margin-top: 8px; color: #64748b;"><strong>Symptoms:</strong> ${p.symptoms}</div>
                </div>
                <div class="card-footer-pt">
                    <button class="btn-outline">Full Medical Record &rarr;</button>
                </div>
            `;
            patientsGrid.appendChild(card);
            
            const btn = card.querySelector('.btn-outline');
            btn.addEventListener('click', () => {
                const content = `
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 20px;">
                        <div style="background: var(--bg-main); padding: 16px; border-radius: 8px; border: 1px solid var(--border);">
                            <div style="font-size:11px; font-weight:700; color:var(--text-muted); text-transform:uppercase; margin-bottom:8px;">Identity & Biometrics</div>
                            <div style="font-size:14px; margin-bottom:4px;"><strong>Age/Gender:</strong> ${p.age} Y / ${p.gender}</div>
                            <div style="font-size:14px;"><strong>Village Location:</strong> ${p.address}</div>
                        </div>
                        <div style="background: var(--bg-main); padding: 16px; border-radius: 8px; border: 1px solid var(--border);">
                            <div style="font-size:11px; font-weight:700; color:var(--text-muted); text-transform:uppercase; margin-bottom:8px;">Latest Vitals Profile</div>
                            <div style="font-size:14px; margin-bottom:4px;"><strong>Blood Pressure:</strong> ${p.bp} mmHg</div>
                            <div style="font-size:14px; margin-bottom:4px;"><strong>Temperature:</strong> ${p.temp}</div>
                            <div style="font-size:14px; margin-bottom:4px;"><strong>Hemoglobin:</strong> ${p.hb} g/dL</div>
                        </div>
                    </div>
                    <div style="background: var(--bg-main); padding: 16px; border-radius: 8px; border: 1px solid var(--border); margin-bottom: 20px;">
                        <div style="font-size:11px; font-weight:700; color:var(--text-muted); text-transform:uppercase; margin-bottom:8px;">Clinical Summary</div>
                        <p style="font-size: 14px; line-height: 1.6; margin-bottom:8px;"><strong>Reported Symptoms:</strong> ${p.symptoms}</p>
                        <p style="font-size: 14px; line-height: 1.6;"><strong>Vaccination Trace:</strong> ${p.vaccines}</p>
                    </div>
                    <button class="btn-primary" style="width:100%; justify-content:center;" onclick="this.closest('.modal-overlay').remove()"><i class="fa-solid fa-check"></i> Acknowledge & Close</button>
                `;
                window.openModal(`Medical Dossier: ${p.name} (${p.id})`, content);
            });
        });
    }

    let riskChart = null;
    let activityChart = null;

    function renderCharts() {
        const ctxDoughnut = document.getElementById('riskDoughnutChart');
        if (ctxDoughnut) {
            const high = patientsData.filter(p => getRisk(p) === "HIGH").length;
            const low = patientsData.length - high;

            if (riskChart) riskChart.destroy();
            riskChart = new Chart(ctxDoughnut, {
                type: 'doughnut',
                data: {
                    labels: ['Low Risk', 'High Risk'],
                    datasets: [{
                        data: [low, high],
                        backgroundColor: ['#10b981', '#ef4444'],
                        borderWidth: 0
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    cutout: '70%',
                    plugins: { legend: { position: 'bottom' } }
                }
            });
        }

        const ctxBar = document.getElementById('weeklyBarChart');
        if (ctxBar) {
            if (activityChart) activityChart.destroy();
            activityChart = new Chart(ctxBar, {
                type: 'bar',
                data: {
                    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                    datasets: [{
                        label: 'Syncs',
                        data: [0, 0, 0, 0, 0, patientsData.length, 0], // Simplified for demo
                        backgroundColor: '#4f46e5',
                        borderRadius: 6
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: { legend: { display: false } }
                }
            });
        }
    }

    // Initialize
    fetchLiveData();
    setInterval(fetchLiveData, 10000); // Polling every 10 seconds for "live" feel

    // Search Logic
    document.getElementById("searchInput")?.addEventListener("input", (e) => {
        searchQuery = e.target.value || "";
        updateUI();
    });
});
