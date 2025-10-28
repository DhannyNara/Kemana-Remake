
package com.kemana.backend.repository

import com.kemana.backend.model.Job
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface JobRepository : MongoRepository<Job, String> {
    // Spring Data MongoDB akan secara otomatis mengimplementasikan metode dasar
    // seperti save, findById, findAll, delete, dll.

    // Kita bisa menambahkan query kustom di sini jika diperlukan nanti
    // Contoh: fun findByStatus(status: JobStatus): List<Job>
}
