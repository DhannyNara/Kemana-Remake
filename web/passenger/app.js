
// GANTI DENGAN ACCESS TOKEN MAPBOX ANDA
mapboxgl.accessToken = 'pk.eyJ1IjoibWFwYm94dXNlciIsImEiOiJjbDBzZ3U2eWkwczJmM2lwaXU2d2s2aXViIn0.2W3o_2b5x3dw3j4l8z5g2g'; // Contoh token, ganti dengan milik Anda

const map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11', // Gaya peta default
    center: [106.8272, -6.1754], // Pusat peta awal (contoh: Jakarta)
    zoom: 12
});

map.on('load', () => {
    console.log('Peta berhasil dimuat!');
    // Logika lebih lanjut untuk menambahkan marker, geocoding, dll. akan ditambahkan di sini.
});

// Menambahkan kontrol navigasi (zoom in/out)
map.addControl(new mapboxgl.NavigationControl());

document.addEventListener('DOMContentLoaded', () => {
    const orderButton = document.getElementById('order-button');
    const startLocationInput = document.getElementById('start-location');
    const endLocationInput = document.getElementById('end-location');

    orderButton.addEventListener('click', () => {
        const start = startLocationInput.value;
        const end = endLocationInput.value;

        if (!start || !end) {
            alert('Lokasi jemput dan tujuan harus diisi!');
            return;
        }

        // Untuk saat ini, kita hanya akan log valuenya.
        // Logika untuk geocoding dan membuat rute akan diimplementasikan selanjutnya.
        console.log(`Memesan dari: ${start} ke: ${end}`);

        alert('Fitur pemesanan belum terhubung. Cek konsol untuk melihat input.');
    });
});
