package com.example.tugaspraktikum2_pam

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class SharedCommonTest {
    @Test
    fun flowMengirimBeritaSetiapDuaDetik() = runTest {
        val sumber = SumberBerita(StandardTestDispatcher(testScheduler))
        val hasil = sumber.beritaBaru().take(3).toList()
        assertEquals(listOf(1, 2, 3), hasil.map { it.id })
        assertEquals(6_000L, testScheduler.currentTime)
    }

    @Test
    fun filterMapDanOnEachMemprosesBerita() = runTest {
        var masuk = 0
        val sumber = SumberBerita(StandardTestDispatcher(testScheduler))
        val hasil = sumber.beritaBaru()
            .onEach { masuk++ }
            .filter { it.kategori == "Pendidikan" }
            .map { it.formatTampilan() }
            .take(2).toList()
        assertEquals(5, masuk)
        assertEquals(listOf(2, 5), hasil.map { it.berita.id })
        assertEquals("[Pendidikan] Mahasiswa ITERA berbagi hasil penelitian #2", hasil.first().teks)
    }

    @Test
    fun detailDiambilSecaraBersamaanDenganAsyncAwait() = runTest {
        val sumber = SumberBerita(StandardTestDispatcher(testScheduler))
        val berita = Berita(1, "Judul", "Teknologi")
        val pertama = async { sumber.ambilDetail(berita) }
        val kedua = async { sumber.ambilDetail(berita.copy(id = 2)) }
        assertEquals(true, pertama.await().contains("nomor 1"))
        assertEquals(true, kedua.await().contains("nomor 2"))
        assertEquals(1_000L, testScheduler.currentTime)
    }

    @Test
    fun kegagalanDetailMenghasilkanException() = runTest {
        val sumber = SumberBerita(StandardTestDispatcher(testScheduler))
        assertFailsWith<IllegalStateException> {
            sumber.ambilDetail(Berita(1, "Judul", "Teknologi"), simulasiGagal = true)
        }
    }
}
