package com.example.tugaspraktikum2_pam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

data class KeadaanFeed(
    val kategori: String = "Semua",
    val daftar: List<TampilanBerita> = emptyList(),
    val totalMasuk: Int = 0,
    val berjalan: Boolean = true,
    val memuatDetail: Boolean = false,
    val detail: String? = null,
    val pesan: String? = null
)

class PengelolaBerita(private val sumber: SumberBerita = SumberBerita()) : ViewModel() {
    private val _keadaan = MutableStateFlow(KeadaanFeed())
    val keadaan: StateFlow<KeadaanFeed> = _keadaan.asStateFlow()
    private val _jumlahDibaca = MutableStateFlow(0)
    val jumlahDibaca: StateFlow<Int> = _jumlahDibaca.asStateFlow()
    private val idDibaca = mutableSetOf<Int>()
    private val riwayat = mutableListOf<TampilanBerita>()
    private var pekerjaanDetail: Job? = null

    init {
        viewModelScope.launch {
            sumber.beritaBaru()
                .onEach { berita ->
                    riwayat.add(0, berita.formatTampilan())
                    if (riwayat.size > 100) riwayat.removeAt(riwayat.lastIndex)
                    _keadaan.value = _keadaan.value.copy(totalMasuk = _keadaan.value.totalMasuk + 1)
                }
                .filter { _keadaan.value.kategori == "Semua" || it.kategori == _keadaan.value.kategori }
                .map { it.formatTampilan() }
                .catch { error ->
                    _keadaan.value = _keadaan.value.copy(
                        berjalan = false, pesan = error.message ?: "Aliran berita gagal."
                    )
                }
                .collect { tampilan ->
                    val sekarang = _keadaan.value
                    _keadaan.value = sekarang.copy(
                        daftar = (listOf(tampilan) + sekarang.daftar).take(100)
                    )
                }
        }
    }

    fun pilihKategori(kategori: String) {
        _keadaan.value = _keadaan.value.copy(
            kategori = kategori,
            daftar = riwayat.filter { kategori == "Semua" || it.berita.kategori == kategori }
        )
    }

    fun bacaBerita(berita: Berita, simulasiGagal: Boolean = false) {
        pekerjaanDetail?.cancel()
        pekerjaanDetail = viewModelScope.launch {
            _keadaan.value = _keadaan.value.copy(memuatDetail = true, detail = null, pesan = null)
            try {
                val isi = supervisorScope {
                    val hasil = async { sumber.ambilDetail(berita, simulasiGagal) }
                    hasil.await()
                }
                if (idDibaca.add(berita.id)) _jumlahDibaca.value = idDibaca.size
                _keadaan.value = _keadaan.value.copy(detail = isi)
            } catch (batal: CancellationException) {
                throw batal
            } catch (error: Exception) {
                _keadaan.value = _keadaan.value.copy(pesan = error.message ?: "Terjadi kesalahan.")
            } finally {
                _keadaan.value = _keadaan.value.copy(memuatDetail = false)
            }
        }
    }

    fun tutupDetail() {
        pekerjaanDetail?.cancel()
        _keadaan.value = _keadaan.value.copy(detail = null, pesan = null, memuatDetail = false)
    }
}
