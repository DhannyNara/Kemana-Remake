
document.addEventListener('DOMContentLoaded', () => {
    // --- Firebase Auth Check ---
    const auth = firebase.auth();
    auth.onAuthStateChanged(user => {
        if (!user) {
            window.location.href = 'login.html';
        }
    });

    // --- Element Selectors ---
    const autobidCheckbox = document.getElementById('autobid-checkbox');
    const slideContainer = document.getElementById('slide-container');
    const slideHandle = document.getElementById('slide-handle');
    const mainContent = document.getElementById('main-content');
    const tabsContainer = document.getElementById('tabs-container');
    const orderPopup = document.getElementById('order-popup-overlay');
    const autoConfirmPopup = document.getElementById('auto-accepted-confirm-overlay');

    // --- App State ---
    let driverStatus = 'offline'; // offline, online, in-order
    let map = null;
    let orderTimer = null;

    // --- State Management & UI Updates ---

    function updateDriverStatus(newStatus) {
        driverStatus = newStatus;
        console.log(`Driver status updated to: ${driverStatus}`);
        updateUIForStatus();
    }

    function updateUIForStatus() {
        // Tabs Logic
        tabsContainer.innerHTML = '';
        mainContent.innerHTML = '';

        if (driverStatus === 'online') {
            renderTabs(['heatmap']);
            setActiveTab('heatmap');
            // Simulate receiving an order after 5 seconds
            setTimeout(triggerNewOrder, 5000);
        } else if (driverStatus === 'in-order') {
            renderTabs(['order', 'heatmap']);
            setActiveTab('order');
        } else { // Offline
            slideContainer.style.display = 'block';
        }
    }

    function renderTabs(tabNames) {
        tabNames.forEach(name => {
            const button = document.createElement('button');
            button.className = 'tab-link';
            button.dataset.tab = name;
            button.textContent = name.charAt(0).toUpperCase() + name.slice(1); // Capitalize
            button.onclick = () => setActiveTab(name);
            tabsContainer.appendChild(button);
        });
    }

    function setActiveTab(tabName) {
        // Update tab link styles
        document.querySelectorAll('.tab-link').forEach(link => {
            link.classList.toggle('active', link.dataset.tab === tabName);
        });

        // Update content
        mainContent.innerHTML = ''; 
        const contentDiv = document.createElement('div');
        contentDiv.id = tabName;
        contentDiv.className = 'tab-content active';

        if (tabName === 'heatmap') {
            const mapDiv = document.createElement('div');
            mapDiv.id = 'map-container';
            contentDiv.appendChild(mapDiv);
            initializeMap(mapDiv);
        } else if (tabName === 'order') {
            contentDiv.innerHTML = createOrderInProgressView();
        }
        mainContent.appendChild(contentDiv);
    }
    
    function initializeMap(container) {
        if (map) map.remove();
        container.style.height = 'calc(100vh - 200px)';
        container.style.display = 'block';
        map = L.map(container).setView([-6.200000, 106.816666], 12);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
    }

    function createOrderInProgressView() {
        return `
            <div class="in-progress-card">
                <h2>#62LR</h2>
                <div class="route-step">
                    <p><strong>1. GUDANG BESAR KALIMANTAN</strong><br>Jl. Abadi III, Guntungmanggis...</p>
                </div>
                <div class="route-step">
                    <p><strong>2. JM4J+FC4, JL. PEMAJATAN</strong><br>GAMBUT, KAB. BANJAR...</p>
                </div>
                <p>Penerima: mellyalfianitaa</p>
                <button class="action-button">Di Titik Pengambilan</button>
            </div>`;
    }

    // --- New Order Logic ---

    function triggerNewOrder() {
        if (driverStatus !== 'online') return; 

        const isAutoBid = autobidCheckbox.checked;
        orderPopup.style.display = 'flex';
        
        const manualBtn = document.getElementById('manual-accept-btn');
        const autoBtn = document.getElementById('auto-accept-btn');
        const cancelBtn = document.getElementById('auto-cancel-btn');

        if (isAutoBid) {
            manualBtn.style.display = 'none';
            autoBtn.style.display = 'block';
            cancelBtn.style.display = 'block';
            let progress = 0;
            orderTimer = setInterval(() => {
                progress += 10;
                document.getElementById('auto-progress-bar').style.width = `${progress}%`;
                if (progress >= 100) {
                    acceptOrder(true); // Auto-accepted
                }
            }, 500); // 5 seconds to accept
        } else {
            manualBtn.style.display = 'flex';
            autoBtn.style.display = 'none';
            cancelBtn.style.display = 'none';
            let seconds = 20;
            document.getElementById('manual-timer').textContent = seconds;
            orderTimer = setInterval(() => {
                seconds--;
                document.getElementById('manual-timer').textContent = seconds;
                if (seconds <= 0) {
                    rejectOrder();
                }
            }, 1000);
        }
    }

    function acceptOrder(isAuto) {
        clearInterval(orderTimer);
        orderPopup.style.display = 'none';

        if (isAuto) {
            autoConfirmPopup.style.display = 'flex';
        } else {
            updateDriverStatus('in-order');
        }
    }

    function rejectOrder() {
        clearInterval(orderTimer);
        orderPopup.style.display = 'none';
        // Return to 'online' state, maybe trigger another order later
        updateDriverStatus('online');
    }
    
    // Event Listeners for popups
    document.getElementById('close-order-popup').onclick = rejectOrder;
    document.getElementById('manual-accept-btn').onclick = () => acceptOrder(false);
    document.getElementById('auto-cancel-btn').onclick = rejectOrder;
    document.getElementById('final-cancel-btn').onclick = () => {
        autoConfirmPopup.style.display = 'none';
        updateDriverStatus('online'); // Go back to searching
    };
    document.getElementById('final-continue-btn').onclick = () => {
        autoConfirmPopup.style.display = 'none';
        updateDriverStatus('in-order');
    };


    // --- Go Online/Offline Logic ---
    slideHandle.addEventListener('click', () => {
        if (driverStatus === 'offline') {
            slideContainer.style.display = 'none';
            updateDriverStatus('online');
        }
    });
    
    // Initial setup
    updateUIForStatus();
});
