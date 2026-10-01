package com.example.tugaspraktikum2_pam

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun App() {
    val pengelola = viewModel { PengelolaBerita() }
    val keadaan by pengelola.keadaan.collectAsStateWithLifecycle()
    val dibaca by pengelola.jumlahDibaca.collectAsStateWithLifecycle()
    var simulasiGagal by remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.safeDrawingPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("News Feed Simulator", style = MaterialTheme.typography.headlineSmall)
                Text("Syehabudin • 124140102", style = MaterialTheme.typography.bodyMedium)
                Text("Masuk: ${keadaan.totalMasuk}  |  Sudah dibaca: $dibaca")
                Text(if (keadaan.berjalan) "Berita baru setiap 2 detik" else "Aliran berita berhenti")
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Semua", "Teknologi", "Pendidikan", "Olahraga").forEach { kategori ->
                        FilterChip(
                            selected = keadaan.kategori == kategori,
                            onClick = { pengelola.pilihKategori(kategori) },
                            label = { Text(kategori) }
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Checkbox(checked = simulasiGagal, onCheckedChange = { simulasiGagal = it })
                    Text("Simulasikan gagal memuat detail", modifier = Modifier.padding(top = 12.dp))
                }
                if (keadaan.daftar.isEmpty()) {
                    Text("Menunggu berita kategori ${keadaan.kategori}...")
                }
                LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(keadaan.daftar, key = { it.berita.id }) { item ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(item.teks, style = MaterialTheme.typography.titleMedium)
                                Button(onClick = { pengelola.bacaBerita(item.berita, simulasiGagal) }) {
                                    Text("Baca detail")
                                }
                            }
                        }
                    }
                }
            }
            if (keadaan.memuatDetail || keadaan.detail != null || keadaan.pesan != null) {
                AlertDialog(
                    onDismissRequest = pengelola::tutupDetail,
                    title = { Text(if (keadaan.pesan != null) "Gagal memuat" else "Detail berita") },
                    text = {
                        Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (keadaan.memuatDetail) {
                                CircularProgressIndicator()
                                Text("Mengambil detail secara async...")
                            } else {
                                Text(keadaan.pesan ?: keadaan.detail.orEmpty())
                            }
                        }
                    },
                    confirmButton = { TextButton(onClick = pengelola::tutupDetail) { Text("Tutup") } }
                )
            }
        }
    }
}
