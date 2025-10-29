
// Variabel global untuk mode debug
let debugTapCount = 0;
const DEBUG_TAP_TARGET = 5;

function loginWithMock(mode) {
    let mockDriver;
    if (mode === 'debug') {
        console.log("Masuk mode debug offline...");
        mockDriver = {
            id: "-1", name: "Debug User", email: "debug@kemana.com",
            vehiclePlate: "W 3B PWA", vehicleType: "PWA", 
            photoUrl: "https://www.gravatar.com/avatar/0?d=mp&f=y", balance: 999999
        };
    } else { // mode === 'demo'
        console.log("Masuk mode demo offline...");
        mockDriver = {
            id: "000001-offline", name: "Demo User (Offline)", email: "demo-offline@kemana.com",
            vehiclePlate: "B 0001 DEM", vehicleType: "Motor", 
            photoUrl: "https://www.gravatar.com/avatar/1?d=mp&f=y", balance: 50000
        };
    }
    localStorage.setItem('driver', JSON.stringify(mockDriver));
    window.location.href = 'main.html';
}

document.addEventListener('DOMContentLoaded', function () {
    const loginButton = document.getElementById('login-button');
    const userIdInput = document.getElementById('user-id');
    const passwordInput = document.getElementById('password');
    const errorMessage = document.getElementById('error-message');

    loginButton.addEventListener('click', function () {
        const userId = userIdInput.value;
        const password = passwordInput.value;

        errorMessage.style.display = 'none';

        if (userId === 'debug' && password === 'debug') {
            debugTapCount++;
            if (debugTapCount >= DEBUG_TAP_TARGET) {
                loginWithMock('debug');
            }
            return;
        }
        
        debugTapCount = 0;

        if (!userId || !password) {
            errorMessage.textContent = 'User ID dan Password tidak boleh kosong.';
            errorMessage.style.display = 'block';
            return;
        }

        const loginUrl = 'http://192.168.1.10:8080/api/db/driver/login';
        fetch(loginUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: userId, password: password }),
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => Promise.reject(err))
                    .catch(() => Promise.reject({ message: 'Respons server tidak valid.' }));
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "OK" && data.payload && data.payload.length > 0) {
                const driver = data.payload[0];
                localStorage.setItem('driver', JSON.stringify(driver));
                window.location.href = 'main.html';
            } else {
                // Jika login online gagal, cek apakah ini mode demo
                if (userId === '000001' && password === 'demo') {
                    loginWithMock('demo');
                } else {
                    errorMessage.textContent = data.message || 'User ID atau Password salah.';
                    errorMessage.style.display = 'block';
                }
            }
        })
        .catch(error => {
            console.error('Fetch Error:', error);
            // JIKA KONEKSI GAGAL & ini adalah mode demo, masuk mode offline
            if (userId === '000001' && password === 'demo') {
                loginWithMock('demo');
            } else {
                errorMessage.textContent = error.message || 'Tidak dapat terhubung ke server.';
                errorMessage.style.display = 'block';
            }
        });
    });
});
