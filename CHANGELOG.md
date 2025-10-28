# Changelog - Kemana Backend Modernization

---

## Versi 0.2.0 (27 Oktober 2025) - Perombakan Arsitektur Backend

Versi ini menandai perombakan total dan modernisasi dari arsitektur backend orisinal. Kode yang sebelumnya tidak fungsional telah dibangun ulang dengan fondasi yang logis, efisien, dan dapat diskalakan.

*This version marks a complete overhaul and modernization of the original backend architecture. The previously non-functional code has been rebuilt with a logical, efficient, and scalable foundation.*

### Perubahan Besar (`Breaking Changes`)

*   **[Arsitektur] Model `Job` menggantikan `Order`**: Sistem `Order` yang kaku telah dihapus sepenuhnya. Diperkenalkan model `Job` yang abstrak dan fleksibel (`RideDetails`, `FoodDetails`, `MartDetails`) untuk skalabilitas di masa depan.

    *English: **[Architecture] `Job` Model Replaces `Order`**: The rigid `Order` system has been completely removed. A flexible and abstract `Job` model has been introduced (`RideDetails`, `FoodDetails`, `MartDetails`) for future scalability.*

*   **[Arsitektur] Controller & Repository Baru**: `OrderController` dan `OrderRepository` yang usang telah dihapus. Sebagai gantinya, `JobController` dan `JobRepository` baru telah dibuat untuk mengelola seluruh alur kerja pekerjaan.

    *English: **[Architecture] New Controller & Repository**: The obsolete `OrderController` and `OrderRepository` have been deleted. New `JobController` and `JobRepository` have been created to manage the entire job workflow.*

*   **[Model] Konsolidasi `Driver`**: Model `Driver` yang membingungkan telah digabungkan menjadi satu data class `Driver` yang solid, menggantikan properti `isActive` dengan enum `DriverStatus` (`OFFLINE`, `AVAILABLE`, `ON_JOB`) yang lebih jelas.

    *English: **[Model] `Driver` Consolidation**: The confusing `Driver` models have been merged into a single, solid `Driver` data class, replacing the ambiguous `isActive` property with a clearer `DriverStatus` enum (`OFFLINE`, `AVAILABLE`, `ON_JOB`).*

### Fitur & Peningkatan Baru (Features & Improvements)

*   **[Pencarian Cerdas] Pencarian Driver Berdasarkan Kedekatan**: Logika bisnis inti telah diimplementasikan di `JobController` untuk menargetkan tawaran pekerjaan hanya kepada sejumlah kecil driver terdekat yang relevan, meningkatkan efisiensi secara drastis.

    *English: **[Smart Search] Proximity-Based Driver Search**: Core business logic has been implemented in `JobController` to target job offers only to a small number of relevant, nearby drivers, drastically improving efficiency.*

*   **[Komunikasi Real-time] Implementasi RabbitMQ**: Fungsi `sendTo` yang sebelumnya tidak ada, kini telah diimplementasikan dari awal di `Ext.kt` untuk memungkinkan backend mempublikasikan pesan secara andal ke klien.

    *English: **[Real-time Communication] RabbitMQ Implementation**: The previously non-existent `sendTo` function has now been implemented from scratch in `Ext.kt`, allowing the backend to reliably publish messages to clients.*

*   **[Utilitas] Perbaikan Kritis**: Parser JSON yang rusak (`JSON-Ext.kt`) telah digantikan dengan konversi objek menggunakan Gson yang andal, dan file serta fungsi penting lainnya (`Position.kt`, `distanceTo`) telah dibuat atau diperbaiki.

    *English: **[Utilities] Critical Fixes**: The broken JSON parser (`JSON-Ext.kt`) was replaced with reliable object conversion using Gson, and other critical missing files and functions (`Position.kt`, `distanceTo`) were created or fixed.*

### Perbaikan Bug (Bug Fixes)

*   Memperbaiki alur kerja pemesanan yang rusak total yang disebabkan oleh model data tidak sinkron, dependensi rusak, dan ketiadaan logika komunikasi.

    *English: Fixed the completely broken ordering workflow caused by asynchronous data models, broken dependencies, and a lack of communication logic.*
