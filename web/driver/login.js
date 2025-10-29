
document.addEventListener('DOMContentLoaded', () => {
    const googleLoginBtn = document.getElementById('google-login-btn');
    const emailLoginBtn = document.getElementById('email-login-btn');
    const declarationPopup = document.getElementById('declaration-popup');
    const closePopupBtn = document.getElementById('close-popup-btn');
    const startDeclarationBtn = document.getElementById('start-declaration-btn');

    const backendUrl = 'http://localhost:8080'; // URL Backend

    // --- UI Logic ---
    function showDeclarationPopup(userName) {
        console.log('Login successful for:', userName);
        declarationPopup.style.display = 'flex';
    }

    // --- Firebase Auth (Google) ---
    // (Kode ini tetap sama)
    googleLoginBtn.addEventListener('click', () => {
        // Logika login Google...
        alert('Login dengan Google belum diimplementasikan sepenuhnya di backend.');
    });

    // --- User/Pass Login Logic (Updated to use Backend) ---
    emailLoginBtn.addEventListener('click', () => {
        const userIdInput = document.getElementById('userid').value;
        const passwordInput = document.getElementById('password').value;

        const loginData = {
            id: userIdInput,
            password: passwordInput
        };

        fetch(`${backendUrl}/api/db/driver/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginData),
        })
        .then(response => {
            if (!response.ok) {
                // Jika respons tidak OK (misal: 401, 404), lempar error untuk ditangkap di .catch
                return response.json().then(err => { throw new Error(err.payload || 'Login failed') });
            }
            return response.json();
        })
        .then(data => {
            if (data.status === 'OK' && data.payload && data.payload.length > 0) {
                const user = data.payload[0];
                showDeclarationPopup(user.name); // Tampilkan nama dari data backend
            } else {
                // Jika status OK tapi payload kosong (seharusnya tidak terjadi dengan logika backend saat ini)
                throw new Error(data.payload || 'Login failed: Invalid response from server.');
            }
        })
        .catch(error => {
            console.error('Login Error:', error);
            alert(`Login Gagal: ${error.message}`);
        });
    });

    // --- Declaration Popup Logic ---
    closePopupBtn.addEventListener('click', () => {
        declarationPopup.style.display = 'none';
    });

    startDeclarationBtn.addEventListener('click', () => {
        alert('Deklarasi berhasil! Anda akan diarahkan ke halaman utama.');
        window.location.href = 'home.html';
    });
});
