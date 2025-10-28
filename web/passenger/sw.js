
const CACHE_NAME = 'kemana-passenger-v2'; // Nama cache diperbarui untuk memicu upgrade
const URLS_TO_CACHE = [
    '/', // Root PWA, biasanya mengarah ke index.html
    'index.html',
    'main.html',      // Beranda baru
    'ride.html',      // Halaman pemesanan ojek
    'https://cdn.tailwindcss.com',
    'https://api.mapbox.com/mapbox-gl-js/v2.8.2/mapbox-gl.js',
    'https://api.mapbox.com/mapbox-gl-js/v2.8.2/mapbox-gl.css',
    'https://developers.google.com/identity/images/g-logo.png'
];

// Event listener untuk instalasi service worker
self.addEventListener('install', event => {
    console.log('Service Worker: Menginstall...');
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => {
                console.log('Service Worker: Caching App Shell');
                return cache.addAll(URLS_TO_CACHE);
            })
            .then(() => self.skipWaiting()) // Aktifkan service worker baru secara langsung
    );
});

// Event listener untuk aktivasi service worker
self.addEventListener('activate', event => {
    console.log('Service Worker: Mengaktifkan...');
    // Hapus cache lama jika ada
    event.waitUntil(
        caches.keys().then(cacheNames => {
            return Promise.all(
                cacheNames.map(cache => {
                    if (cache !== CACHE_NAME) {
                        console.log('Service Worker: Menghapus cache lama', cache);
                        return caches.delete(cache);
                    }
                })
            );
        })
    );
    return self.clients.claim();
});

// Event listener untuk setiap request (fetch)
self.addEventListener('fetch', event => {
    // Gunakan strategi Cache First
    event.respondWith(
        caches.match(event.request)
            .then(response => {
                // Jika ada di cache, kembalikan. Jika tidak, ambil dari network.
                return response || fetch(event.request);
            })
    );
});
