
document.addEventListener('DOMContentLoaded', () => {
    const menuBtn = document.getElementById('menu-btn');
    const sideNav = document.getElementById('side-nav');
    const mainContent = document.getElementById('main-content');
    const statusCheckbox = document.getElementById('status-checkbox');
    const noOrderOverlay = document.getElementById('no-order-overlay');
    const orderNotification = document.getElementById('order-notification');
    const closeNotificationBtn = document.getElementById('close-notification-btn');

    // Initialize Leaflet Map
    const map = L.map('map').setView([-6.200000, 106.816666], 13); // Default to Jakarta

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // --- UI Interactions ---

    // Toggle side navigation
    menuBtn.addEventListener('click', () => {
        sideNav.classList.toggle('open');
    });

    // Close side navigation when clicking on the main content
    mainContent.addEventListener('click', () => {
        if (sideNav.classList.contains('open')) {
            sideNav.classList.remove('open');
        }
    });
    
    // Handle driver status change
    statusCheckbox.addEventListener('change', () => {
        if (statusCheckbox.checked) {
            // Driver is online
            noOrderOverlay.style.display = 'flex'; 
             // Simulate receiving an order after 5 seconds
            setTimeout(showOrderNotification, 5000);
        } else {
            // Driver is offline
            noOrderOverlay.style.display = 'none';
            hideOrderNotification();
        }
    });

    // --- Order Notification Logic ---

    function showOrderNotification() {
        if(statusCheckbox.checked) { // Only show if driver is still online
            orderNotification.classList.add('open');
        }
    }

    function hideOrderNotification() {
        orderNotification.classList.remove('open');
    }

    closeNotificationBtn.addEventListener('click', hideOrderNotification);

    // --- Initial State ---
    noOrderOverlay.style.display = 'none';

});
