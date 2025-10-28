# KEMANA - Modernisasi Backend & Rencana Aksi

**PEMBERITAHUAN (27 OKTOBER 2025):** Proyek ini telah melalui perombakan dan modernisasi besar-besaran. Dokumentasi ini berfungsi sebagai panduan komprehensif yang menggabungkan instruksi orisinal dengan alur kerja pengembangan modern.

---

## Arsitektur & Tumpukan Teknologi (Stack)

Arsitektur modern memisahkan beberapa lapisan untuk skalabilitas dan kemudahan pengembangan.

*   **BACKEND (Core Service):**
    *   **Framework:** Spring Boot (Kotlin)
    *   **Tugas:** Mengelola logika inti, data pengguna, pekerjaan (jobs), dan status pengemudi.
    *   **Database:** MongoDB

*   **MIDDLE-END (Business & Communication Layer):**
    *   **Platform:** Google Cloud Functions, RabbitMQ
    *   **Tugas:** Menangani logika bisnis sekunder, notifikasi real-time, dan sebagai *Backend-for-Frontend* (BFF) untuk menyederhanakan respons ke klien.

*   **ANDROID (Klien Native):**
    *   **Bahasa:** Kotlin
    *   **Arsitektur:** Modular, MVP
    *   **Tugas:** Aplikasi untuk Penumpang dan Pengemudi.

*   **PWA - HTML5 (Future):**
    *   **Framework:** (Belum ditentukan, misal: React, Vue, atau Angular)
    *   **Tugas:** Menyediakan alternatif klien berbasis web yang ringan dan dapat diinstal di berbagai platform.

---

## Prasyarat (Prerequisite)

*   **JDK 8** atau lebih tinggi.
*   **MongoDB:** Database NoSQL untuk menyimpan semua data persisten.
*   **RabbitMQ:** Message broker untuk komunikasi real-time dan antrian tugas.
*   **Akun Google & Firebase:** Diperlukan untuk deployment dan beberapa layanan backend.
*   **Android Studio:** Untuk membangun dan menjalankan aplikasi klien Android.

---

## Cara Membangun & Menjalankan (How To Build & Run)

### 1. Lingkungan Lokal (MongoDB & RabbitMQ)

Sebelum memulai pengembangan backend, pastikan server MongoDB dan RabbitMQ Anda berjalan.

*   **RabbitMQ:** Untuk pengguna lokal, kunjungi `http://localhost:15672/` (user: guest, pass: guest) untuk mengelola antrian. Anda juga bisa menggunakan layanan cloud seperti [CloudAMQP](https://www.cloudamqp.com/).

### 2. Pengembangan Backend (via Google AI Studio)

Lingkungan pengembangan ini dioptimalkan untuk iterasi cepat menggunakan alat AI.

1.  **Buka Proyek:** Buka direktori `source/kemana/backend-side` di Google AI Studio.
2.  **Analisis & Modifikasi:** Gunakan Gemini untuk menganalisis kode, melakukan refactoring, menambahkan endpoint baru, atau memperbaiki bug.
3.  **Jalankan & Debug:** Gunakan terminal terintegrasi untuk menjalankan aplikasi Spring Boot. Perintah standarnya adalah `./mvnw spring-boot:run`.
4.  **Iterasi:** Ulangi proses modifikasi dan pengujian hingga fitur yang diinginkan selesai.

### 3. Deployment (via Firebase Studio)

Setelah backend siap, Anda dapat mendeploy-nya.

1.  **Build Aplikasi:** Pastikan Anda telah membangun versi produksi dari aplikasi front-end (jika ada, misal: PWA) ke dalam direktori seperti `dist` atau `build`.
2.  **Gunakan Alat Deploy:** Di Google AI Studio, panggil fungsi `classic_firebase_hosting_deploy`.
    *   **Contoh:** `default_api.classic_firebase_hosting_deploy(path='path/to/your/dist', appType='client')`
    *   Ini akan secara otomatis mendeploy aset statis Anda ke Firebase Hosting.
3.  **Backend Service:** Untuk backend Spring Boot, deploy sebagai layanan di Google Cloud Run atau lingkungan server lainnya.

### 4. Klien Android

1.  **Buka Proyek:** Buka direktori `source/kemana/android` di Android Studio.
2.  **Konfigurasi Kunci API:** Perbarui URL backend dan RabbitMQ di `android/base/src/main/java/com/utsman/kemana/base/KEY.kt`.
3.  **Build & Run:** Jalankan aplikasi `driver` dan `passenger` di emulator atau perangkat fisik.

---

## Rencana Aksi & Tugas Selanjutnya

Backend sekarang fungsional untuk alur "pembuatan pekerjaan" -> "notifikasi ke pengemudi terdekat". Tugas selanjutnya adalah:

*   **Implementasi Endpoint:** `/accept`, `/reject`, dan update status pekerjaan.
*   **Update Lokasi Real-time:** Bangun mekanisme untuk driver mengirimkan lokasi secara periodik.
*   **Penyesuaian Klien Android:** Sesuaikan aplikasi Android untuk bekerja dengan API dan model data yang baru.

---

## Atribusi dan Lisensi

Proyek ini adalah *fork* yang dimodernisasi dan dikembangkan ulang untuk tujuan pembelajaran dan perbaikan arsitektur.

*   **Sumber Asli:** [utsmannn/Kemana](https://github.com/utsmannn/Kemana) oleh Muhammad Utsman.
*   **Sumber Remake Saat Ini:** [DhannyNara/Kemana-Remake](https://github.com/DhannyNara/Kemana-Remake)

Proyek ini dilisensikan di bawah Lisensi Apache 2.0. Hak cipta asli dipertahankan.

```
Copyright 2019 Muhammad Utsman

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
mandistributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```