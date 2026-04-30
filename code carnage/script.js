document.addEventListener("DOMContentLoaded", () => {
    // Sidebar Toggle Logic
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');
    const sidebarToggle = document.getElementById('sidebarToggle');

    function updateSidebarIcon(isCollapsed) {
        if (!sidebarToggle) return;
        if (isCollapsed) {
            sidebarToggle.innerHTML = '<i class="fa-solid fa-bars" title="Open Sidebar"></i>';
        } else {
            sidebarToggle.innerHTML = '<i class="fa-solid fa-chevron-left" title="Close Sidebar"></i>';
        }
    }

    // Load state from local storage
    const isInitiallyCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
    if (isInitiallyCollapsed) {
        if (sidebar) sidebar.classList.add('collapsed');
        if (mainContent) mainContent.classList.add('expanded');
    }
    updateSidebarIcon(isInitiallyCollapsed);

    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', () => {
            const isCollapsed = sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded');
            localStorage.setItem('sidebarCollapsed', isCollapsed);
            updateSidebarIcon(isCollapsed);
        });
    }

    // Check for logged in user from localStorage
    const savedUser = localStorage.getItem('aarogyaNetUser');
    const welcomeMessage = document.getElementById('welcomeMessage');
    
    if (savedUser && welcomeMessage) {
        welcomeMessage.innerHTML = `Welcome back, <span style="color: var(--primary);">${savedUser}</span>`;
    }

    // Update Sidebar Auth Button if logged in
    const authBtn = document.querySelector('.auth-btn');
    if (savedUser && authBtn) {
        authBtn.innerHTML = `<i class="fa-solid fa-right-from-bracket"></i> Sign Out`;
        authBtn.href = "#";
        authBtn.style.color = "var(--red-alert)";
        authBtn.style.background = "rgba(239, 68, 68, 0.1)";
        authBtn.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('aarogyaNetUser');
            window.location.href = "login.html";
        });
    }

    // Current Date
    const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const currentDateEl = document.getElementById("currentDate");
    if (currentDateEl) {
        currentDateEl.innerText = new Date().toLocaleDateString('en-US', dateOptions);
    }

    // Mock Data mimicking real sync
    const patientsData = [
        { name: "Sita Devi", age: 28, gender: "F", id: "A-5991", asha: "Meena K.", bp: "110/75", temp: "98.6°F", hb: "11.2", pregnancy: "Month 5 (ANC-2)", vaccines: "TT-1 Done", symptoms: "Mild Fatigue" },
        { name: "Suresh Kumar", age: 62, gender: "M", id: "A-4122", asha: "Radha V.", bp: "165/95", temp: "99.1°F", hb: "13.5", pregnancy: "N/A", vaccines: "COVID Booster Due", symptoms: "Dizziness, Chronic Cough" },
        { name: "Anjali Sharma", age: 24, gender: "F", id: "A-5321", asha: "Meena K.", bp: "120/80", temp: "98.4°F", hb: "12.0", pregnancy: "Month 2 (ANC-1)", vaccines: "Up to date", symptoms: "None" },
        { name: "Geeta Patel", age: 31, gender: "F", id: "A-6710", asha: "Sunita M.", bp: "145/90", temp: "98.8°F", hb: "9.2", pregnancy: "Month 8 (ANC-4)", vaccines: "TT-2 Done", symptoms: "Swollen ankles" },
        { name: "Ravi (Child)", age: 2, gender: "M", id: "A-1102", asha: "Radha V.", bp: "90/60", temp: "101.2°F", hb: "10.5", pregnancy: "N/A", vaccines: "Polio/DPT Missed", symptoms: "High Fever, Diarrhea" },
        { name: "Priya (Child)", age: 4, gender: "F", id: "A-1106", asha: "Sunita M.", bp: "95/65", temp: "98.2°F", hb: "11.8", pregnancy: "N/A", vaccines: "Fully Vaccinated", symptoms: "None" }
    ];

    function getRisk(p) {
        if (p.bp && p.bp !== "N/A" && parseInt(p.bp.split('/')[0]) > 140) return "HIGH";
        if (p.temp && parseFloat(p.temp) > 100) return "HIGH";
        if (p.hb && parseFloat(p.hb) < 10) return "HIGH";
        return "LOW";
    }

    // Global Modal Function
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

    const tableBody = document.getElementById("tableBody");
    const patientsGrid = document.getElementById("patientsGrid");
    const searchInput = document.getElementById("searchInput");
    const downloadBtn = document.getElementById("downloadBtn");

    function renderTable(data) {
        if (tableBody) {
            tableBody.innerHTML = "";
            data.forEach((p, idx) => {
                const delay = idx * 0.1;
                const risk = getRisk(p);
                const riskBadge = risk === "HIGH" 
                    ? "<span class='badge danger'><i class='fa-solid fa-triangle-exclamation'></i> HIGH RISK</span>"
                    : "<span class='badge success'><i class='fa-solid fa-check'></i> NORMAL</span>";
                    
                const pregBadge = p.pregnancy !== "N/A"
                    ? `<span class='badge' style='background: #f3e8ff; color: #7e22ce; border: 1px solid #d8b4fe;'>${p.pregnancy}</span>`
                    : "<span class='badge neutral'>N/A</span>";

                const isHighBP = parseInt(p.bp.split('/')[0]) > 140;
                const isHighTemp = parseFloat(p.temp) > 100;
                const hbBadge = parseFloat(p.hb) < 10.0 ? 'vital-high' : '';

                const row = document.createElement("tr");
                row.style.animation = `fade-in 0.5s ease forwards ${delay}s`;
                row.style.opacity = "0";

                row.innerHTML = `
                    <td>
                        <div class="patient-info">
                            <span class="patient-name">${p.name}</span>
                            <span class="patient-meta">ID: ${p.id} • Age: ${p.age} • Gender: ${p.gender}</span>
                        </div>
                    </td>
                    <td>
                        <div style="display: flex; gap: 6px; flex-wrap: wrap;">
                            <span class="vital-info ${isHighBP ? 'vital-high' : ''}" title="Blood Pressure"><i class="fa-solid fa-heart-pulse"></i> ${p.bp}</span>
                            <span class="vital-info ${isHighTemp ? 'vital-high' : ''}" title="Temperature"><i class="fa-solid fa-temperature-half"></i> ${p.temp}</span>
                            <span class="vital-info ${hbBadge}" title="Hemoglobin"><i class="fa-solid fa-droplet"></i> Hb: ${p.hb}</span>
                        </div>
                    </td>
                    <td>
                        <div style="display: flex; flex-direction: column; gap: 4px; align-items: flex-start;">
                            ${pregBadge}
                            <span style="font-size: 11px; color: #0284c7; background: #e0f2fe; padding: 2px 6px; border-radius: 4px; font-weight: 600; border: 1px solid #bae6fd;"><i class="fa-solid fa-syringe"></i> ${p.vaccines}</span>
                        </div>
                    </td>
                    <td style="color: #64748b; font-size: 13px; max-width: 200px;">${p.symptoms}</td>
                    <td><span style="font-weight: 500; font-size: 13px;">${p.asha}</span></td>
                    <td>${riskBadge}</td>
                `;
                tableBody.appendChild(row);
            });
        }

        if (patientsGrid) {
            patientsGrid.innerHTML = "";
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
                        <div class="pt-stat"><i class="fa-solid fa-user-nurse"></i> <strong>Assigned ASHA:</strong> ${p.asha}</div>
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
                                <div style="font-size:14px; margin-bottom:4px;"><strong>Blood Group:</strong> O+</div>
                                <div style="font-size:14px;"><strong>Assigned ASHA:</strong> ${p.asha}</div>
                            </div>
                            <div style="background: var(--bg-main); padding: 16px; border-radius: 8px; border: 1px solid var(--border);">
                                <div style="font-size:11px; font-weight:700; color:var(--text-muted); text-transform:uppercase; margin-bottom:8px;">Latest Vitals Profile</div>
                                <div style="font-size:14px; margin-bottom:4px;"><strong>Blood Pressure:</strong> ${p.bp} mmHg</div>
                                <div style="font-size:14px; margin-bottom:4px;"><strong>Temperature:</strong> ${p.temp}</div>
                                <div style="font-size:14px;"><strong>Hemoglobin:</strong> ${p.hb} g/dL</div>
                            </div>
                        </div>
                        <div style="background: var(--bg-main); padding: 16px; border-radius: 8px; border: 1px solid var(--border); margin-bottom: 20px;">
                            <div style="font-size:11px; font-weight:700; color:var(--text-muted); text-transform:uppercase; margin-bottom:8px;">Clinical Summary</div>
                            <p style="font-size: 14px; line-height: 1.6; margin-bottom:8px;"><strong>Reported Symptoms:</strong> ${p.symptoms}</p>
                            <p style="font-size: 14px; line-height: 1.6; margin-bottom:8px;"><strong>Vaccination Trace:</strong> ${p.vaccines}</p>
                            <p style="font-size: 14px; line-height: 1.6;"><strong>Maternal/Child Profile:</strong> ${p.pregnancy !== "N/A" ? p.pregnancy : 'Standard Patient'}</p>
                        </div>
                        <button class="btn-primary" style="width:100%; justify-content:center;" onclick="this.closest('.modal-overlay').remove()"><i class="fa-solid fa-check"></i> Acknowledge & Close</button>
                    `;
                    window.openModal(`Medical Dossier: ${p.name} (${p.id})`, content);
                });
            });
        }
    }

    // Dynamically calculate KPIs based on dummy data array
    const kpiTotal = document.getElementById("kpiTotal");
    const kpiHighRisk = document.getElementById("kpiHighRisk");
    const kpiPregnant = document.getElementById("kpiPregnant");
    const aiAlertCount = document.getElementById("aiAlertCount");
    const aiAlertText = document.getElementById("aiAlertText");

    if (kpiTotal) kpiTotal.innerText = patientsData.length;
    
    if (kpiHighRisk || aiAlertCount) {
        const highRiskCount = patientsData.filter(p => getRisk(p) === "HIGH").length;
        
        if (kpiHighRisk) kpiHighRisk.innerText = highRiskCount;
        
        if (aiAlertCount && aiAlertText) {
            if (highRiskCount > 0) {
                aiAlertCount.innerText = `${highRiskCount} new high-risk cases`;
            } else {
                aiAlertText.innerHTML = `Our system has analyzed all incoming data. <span style="color:var(--green-safe); font-weight:600;"><i class="fa-solid fa-check-circle"></i> No high-risk cases detected today.</span>`;
            }
        }
    }
    
    if (kpiPregnant) {
        kpiPregnant.innerText = patientsData.filter(p => p.pregnancy && p.pregnancy !== "N/A").length;
    }

    renderTable(patientsData);

    // Dynamic Search
    if (searchInput) {
        searchInput.addEventListener("input", (e) => {
            const query = e.target.value.toLowerCase();
            const filtered = patientsData.filter(p => 
                p.name.toLowerCase().includes(query) ||
                p.id.toLowerCase().includes(query) ||
                p.symptoms.toLowerCase().includes(query)
            );
            renderTable(filtered);
        });
    }

    // Generate Report modal animation
    if (downloadBtn) {
        downloadBtn.addEventListener("click", () => {
            const options = patientsData.map(p => `<option value="${p.id}">${p.name} (${p.id})</option>`).join('');
            
            const content = `
                <div style="display:flex; flex-direction:column; gap:16px;">
                    <p style="font-size:14px; color:var(--text-muted);">Select a specific patient to compile their records into a PDF, or generate a batch report for all ASHA territories.</p>
                    <div>
                        <label style="font-weight:600; font-size:13px; color:var(--text-main); margin-bottom:6px; display:block;">Target Patient Record:</label>
                        <select style="width:100%; padding:12px; border-radius:8px; border:1px solid var(--border); background:var(--bg-main); color:var(--text-main); font-family:inherit; font-size:14px; outline:none;">
                            <option value="ALL">All Patients (Comprehensive Batch Report)</option>
                            <optgroup label="Registered Directory">
                                ${options}
                            </optgroup>
                        </select>
                    </div>
                    <button class="btn-primary" id="confirmGenerateBtn" style="justify-content:center; margin-top:8px;"><i class="fa-solid fa-file-pdf"></i> Generate PDF Export</button>
                    <div id="reportStatus" style="font-size:13px; text-align:center; color:var(--text-muted);"></div>
                </div>
            `;
            
            window.openModal("Extract System Report", content);
            
            setTimeout(() => {
                const btn = document.getElementById('confirmGenerateBtn');
                if(btn) {
                    btn.addEventListener('click', () => {
                        btn.innerHTML = "<i class='fa-solid fa-rotate fa-spin'></i> Compiling Encrypted Data...";
                        btn.style.opacity = "0.8";
                        setTimeout(() => {
                            btn.innerHTML = "<i class='fa-solid fa-file-arrow-down'></i> Secure Download Ready";
                            btn.style.backgroundColor = "var(--green-safe)";
                            btn.style.opacity = "1";
                            document.getElementById('reportStatus').innerHTML = "<strong style='color:var(--green-safe);'>Success!</strong> Report generated and downloading now...";
                            
                            try {
                                if (window.jspdf) {
                                    const { jsPDF } = window.jspdf;
                                    const doc = new jsPDF();
                                    
                                    const selectEl = document.querySelector('.modal-body select');
                                    const selectedId = selectEl ? selectEl.value : "ALL";
                                    
                                    doc.setFontSize(20);
                                    doc.setTextColor(30, 41, 59);
                                    doc.text("AarogyaNet Medical System Export", 20, 20);
                                    
                                    doc.setFontSize(11);
                                    doc.setTextColor(100, 116, 139);
                                    doc.text(`Generated Timestamp: ${new Date().toLocaleString()}`, 20, 28);
                                    doc.line(20, 32, 190, 32);
                                    
                                    if (selectedId === "ALL") {
                                        doc.setFontSize(14);
                                        doc.setTextColor(15, 23, 42);
                                        doc.text("Comprehensive Batch Report (All Active Territories)", 20, 45);
                                        
                                        let y = 55;
                                        doc.setFontSize(11);
                                        patientsData.forEach((p, i) => {
                                            if (y > 270) { doc.addPage(); y = 20; }
                                            doc.text(`${i+1}. ${p.name} | ID: ${p.id} | Risk: ${getRisk(p)} | BP: ${p.bp} | Hb: ${p.hb}`, 20, y);
                                            y += 10;
                                        });
                                    } else {
                                        const pt = patientsData.find(p => p.id === selectedId);
                                        if (pt) {
                                            doc.setFontSize(16);
                                            doc.setTextColor(15, 23, 42);
                                            doc.text(`Single Patient Dossier: ${pt.name}`, 20, 45);
                                            
                                            doc.setFontSize(12);
                                            doc.setTextColor(71, 85, 105);
                                            doc.text(`Unique ID: ${pt.id}`, 20, 58);
                                            doc.text(`Age / Gender: ${pt.age} Years / ${pt.gender}`, 20, 68);
                                            doc.text(`Blood Pressure: ${pt.bp} mmHg`, 20, 78);
                                            doc.text(`Temperature: ${pt.temp}`, 20, 88);
                                            doc.text(`Hemoglobin (Hb): ${pt.hb} g/dL`, 20, 98);
                                            doc.text(`Current Risk Status: ${getRisk(pt)}`, 20, 108);
                                            doc.text(`Reported Symptoms: ${pt.symptoms}`, 20, 118);
                                            doc.text(`Maternal Flag: ${pt.pregnancy !== "N/A" ? pt.pregnancy : "N/A"}`, 20, 128);
                                            doc.text(`Vaccination Trace: ${pt.vaccines}`, 20, 138);
                                            doc.text(`Assigned ASHA Worker: ${pt.asha}`, 20, 148);
                                        }
                                    }
                                    
                                    const filename = selectedId === "ALL" ? "AarogyaNet_Batch_Report.pdf" : `AarogyaNet_${selectedId}_Record.pdf`;
                                    doc.save(filename);
                                }
                            } catch (err) {
                                console.error("PDF generation failed:", err);
                            }

                            setTimeout(()=> { 
                                const modal = document.querySelector('.modal-overlay');
                                if(modal) modal.remove(); 
                            }, 2500);
                        }, 2000);
                    });
                }
            }, 100);
        });
    }

    // Chart JS Initialization
    const ctxDoughnut = document.getElementById('riskDoughnutChart');
    if (ctxDoughnut) {
        // Dynamically calculate risk distribution
        const lowCount = patientsData.filter(p => getRisk(p) === "LOW").length;
        const modCount = 0; // Moderate risk abstracted out in minimal schema
        const highCount = patientsData.filter(p => getRisk(p) === "HIGH").length;

        new Chart(ctxDoughnut.getContext('2d'), {
            type: 'doughnut',
            data: {
                labels: ['Low Risk', 'Moderate Risk', 'High Risk'],
                datasets: [{
                    data: [lowCount, modCount, highCount],
                    backgroundColor: ['#10b981', '#f59e0b', '#ef4444'],
                    borderWidth: 0,
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'bottom', labels: { usePointStyle: true, boxWidth: 8, font: { family: 'Plus Jakarta Sans' } } }
                },
                cutout: '70%'
            }
        });
    }

    const ctxBar = document.getElementById('weeklyBarChart');
    if (ctxBar) {
        new Chart(ctxBar.getContext('2d'), {
            type: 'bar',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
                datasets: [{
                    label: 'Screenings',
                    // Synchronized to total precisely 6 to match our mock patient array length
                    data: [1, 0, 2, 0, 1, 2], 
                    backgroundColor: '#4f46e5',
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    y: { 
                        beginAtZero: true, 
                        grid: { borderDash: [5, 5], drawBorder: false },
                        ticks: { stepSize: 1 } // ensure integers for small counts
                    },
                    x: { grid: { display: false } }
                }
            }
        });
    }

    // Removed: Sync status is now static 'Offline' for the demo presentation.

});

// Add keyframe animation to document
const styleSheet = document.createElement("style");
styleSheet.innerText = `
    @keyframes fade-in {
        0% { opacity: 0; transform: translateY(10px); }
        100% { opacity: 1; transform: translateY(0); }
    }
`;
document.head.appendChild(styleSheet);
