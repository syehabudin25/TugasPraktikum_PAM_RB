package com.example.tugaspraktikum2_pam

import androidx.lifecycle.ViewModelStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class SharedLogicAndroidHostTest {
    @Test
    fun stateFlowMenghitungBeritaUnikDanPulihSetelahGagal() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val penyimpanan = ViewModelStore()
        try {
            val pengelola = PengelolaBerita(SumberBerita(dispatcher))
            penyimpanan.put("feed", pengelola)
            runCurrent()
            advanceTimeBy(2_000)
            runCurrent()
            val berita = pengelola.keadaan.value.daftar.first().berita
            assertEquals(0, pengelola.jumlahDibaca.value)

            pengelola.bacaBerita(berita, simulasiGagal = true)
            runCurrent()
            advanceTimeBy(1_000)
            runCurrent()
            assertNotNull(pengelola.keadaan.value.pesan)
            assertEquals(0, pengelola.jumlahDibaca.value)

            repeat(2) {
                pengelola.bacaBerita(berita)
                runCurrent()
                advanceTimeBy(1_000)
                runCurrent()
            }
            assertNull(pengelola.keadaan.value.pesan)
            assertNotNull(pengelola.keadaan.value.detail)
            assertEquals(1, pengelola.jumlahDibaca.value)
            pengelola.pilihKategori("Pendidikan")
            assertEquals(listOf("Pendidikan"), pengelola.keadaan.value.daftar.map { it.berita.kategori })

            pengelola.bacaBerita(berita.copy(id = 99))
            runCurrent()
            pengelola.tutupDetail()
            runCurrent()
            advanceTimeBy(1_000)
            runCurrent()
            assertNull(pengelola.keadaan.value.detail)
            assertEquals(1, pengelola.jumlahDibaca.value)
        } finally {
            penyimpanan.clear()
            Dispatchers.resetMain()
        }
    }
}
