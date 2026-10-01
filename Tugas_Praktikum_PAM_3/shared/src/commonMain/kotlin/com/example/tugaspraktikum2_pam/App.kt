package com.example.tugaspraktikum2_pam

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tugaspraktikum2_pam.resources.Res
import com.example.tugaspraktikum2_pam.resources.foto_profil
import org.jetbrains.compose.resources.painterResource

@Composable
fun App() {
    var tampilInformasi by remember { mutableStateOf(true) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6750A4),
            background = Color(0xFFF5F3FA),
            surface = Color.White
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "My Profile App",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                ProfileHeader(
                    nama = "Syehabudin",
                    nim = "124140102"
                )

                ProfileCard(
                    judul = "Tentang Saya",
                    isi = "Mahasiswa Teknik Informatika ITERA"
                )

                Button(
                    onClick = { tampilInformasi = !tampilInformasi },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (tampilInformasi) {
                            "Sembunyikan Kontak"
                        } else {
                            "Tampilkan Kontak"
                        }
                    )
                }

                if (tampilInformasi) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Informasi Kontak",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            InfoItem(
                                label = "Email",
                                nilai = "syehbaudin.124140102@student.itera.ac.id"
                            )

                            InfoItem(
                                label = "Phone",
                                nilai = "082384696868"
                            )

                            InfoItem(
                                label = "Location",
                                nilai = "Sukarame"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(nama: String, nim: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.foto_profil),
                contentDescription = "Foto profil $nama",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
            )
        }

        Text(
            text = nama,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "NIM $nim",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ProfileCard(judul: String, isi: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = judul,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = isi,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun InfoItem(label: String, nilai: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label.take(1),
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = nilai,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}