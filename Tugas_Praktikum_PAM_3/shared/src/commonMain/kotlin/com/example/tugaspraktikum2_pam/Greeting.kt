package com.example.tugaspraktikum2_pam

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

data class Berita(val id: Int, val judul: String, val kategori: String)
data class TampilanBerita(val berita: Berita, val teks: String)

fun Berita.formatTampilan(): TampilanBerita =
    TampilanBerita(this, "[$kategori] $judul")

// Data simulasi lokal; tidak memerlukan koneksi internet.
class SumberBerita(private val dispatcher: CoroutineDispatcher = ioDispatcher) {
    fun beritaBaru(): Flow<Berita> = flow {
        var nomor = 1
        val kategori = listOf("Teknologi", "Pendidikan", "Olahraga")
        val judul = listOf(
            "Kotlin memudahkan pengembangan aplikasi",
            "Mahasiswa ITERA berbagi hasil penelitian",
            "Tim mahasiswa bersiap mengikuti kompetisi"
        )
        while (true) {
            delay(2_000)
            val indeks = (nomor - 1) % kategori.size
            emit(Berita(nomor, "${judul[indeks]} #$nomor", kategori[indeks]))
            nomor++
        }
    }

    suspend fun ambilDetail(berita: Berita, simulasiGagal: Boolean = false): String =
        withContext(dispatcher) {
            delay(1_000)
            check(!simulasiGagal) { "Detail gagal dimuat. Silakan coba lagi." }
            "${berita.judul}\n\nKategori: ${berita.kategori}\n\n" +
                "Ini adalah isi berita simulasi nomor ${berita.id}. " +
                "Detail diambil menggunakan coroutine tanpa memblokir tampilan."
        }
}
