# News Feed Simulator

Tugas Praktikum Pertemuan 2: Advanced Kotlin, Coroutines, dan Flow  
Pengembangan Aplikasi Mobile

**Nama:** Syehabudin  
**NIM:** 124140102  
**Kelas:** RB

## Deskripsi

News Feed Simulator adalah aplikasi Android sederhana yang menampilkan berita simulasi setiap 2 detik. Pengguna dapat memilih kategori berita, membuka detail, dan melihat jumlah berita yang sudah dibaca.

Aplikasi ini menggunakan Kotlin Multiplatform dan Compose Multiplatform. Data berita berasal dari program sehingga tidak membutuhkan koneksi internet saat digunakan. Koneksi internet diperlukan saat menyiapkan dan mengunduh dependency proyek.

## Fitur

| No. | Fitur | Implementasi |
| --- | --- | --- |
| 1 | Berita baru setiap 2 detik | `SumberBerita.beritaBaru()` menggunakan `flow`, `delay(2000)`, dan `emit` |
| 2 | Filter berdasarkan kategori | Operator `filter` pada aliran berita dan penyaringan riwayat ketika kategori diganti |
| 3 | Mengubah data ke format tampilan | Operator `map` dan fungsi `formatTampilan()` mengubah `Berita` menjadi `TampilanBerita` |
| 4 | Menghitung berita yang sudah dibaca | `jumlahDibaca` menggunakan `StateFlow<Int>` |
| 5 | Mengambil detail secara asynchronous | `bacaBerita()` menggunakan `async/await`, sedangkan `ambilDetail()` menggunakan `Dispatchers.IO` pada Android |
| 6 | Penanganan kesalahan | Operator `catch` pada Flow dan `try-catch` pada pengambilan detail |
| 7 | Unit test | Pengujian menggunakan `runTest` dan `StandardTestDispatcher` |

Kategori yang tersedia adalah **Teknologi**, **Pendidikan**, dan **Olahraga**. Pilihan **Semua** digunakan untuk menampilkan seluruh kategori.

Berita yang sama hanya dihitung satu kali meskipun detailnya dibuka berulang kali. Hitungan bertambah setelah detail berhasil dimuat.

## Struktur Proyek

Proyek memiliki dua modul utama:

| Modul | Kegunaan |
| --- | --- |
| `androidApp` | Menjalankan aplikasi pada perangkat Android |
| `shared` | Menyimpan tampilan, logika berita, dan pengujian |

Nama file dari proyek awal tetap digunakan. Berikut file utama dalam aplikasi:

| Lokasi | File | Kegunaan |
| --- | --- | --- |
| `shared/src/commonMain` | `App.kt` | Tampilan aplikasi |
| `shared/src/commonMain` | `Greeting.kt` | Model berita, sumber data simulasi, dan pengambilan detail |
| `shared/src/commonMain` | `GreetingUtil.kt` | `PengelolaBerita`, filter kategori, dan StateFlow |
| `shared/src/commonMain` | `Platform.kt` | Deklarasi fungsi dan dispatcher lintas platform |
| `shared/src/androidMain` | `Platform.android.kt` | Implementasi Android dan `Dispatchers.IO` |
| `shared/src/commonTest` | `SharedCommonTest.kt` | Pengujian Flow, operator, dan pengambilan detail |
| `shared/src/androidHostTest` | `SharedLogicAndroidHostTest.kt` | Pengujian state dan perilaku pengelola berita |

File Kotlin tersebut berada di dalam folder `kotlin/com/example/tugaspraktikum2_pam/` pada masing-masing lokasi.

## Cara Menjalankan Aplikasi

### 1. Yang disiapkan

- Android Studio.
- JDK 21 sesuai konfigurasi proyek.
- Android SDK 37 untuk kompilasi.
- Emulator atau HP dengan Android 7.0/API 24 atau lebih tinggi.
- Proses Gradle Sync sudah selesai.

### 2. Jalankan aplikasi

1. Pilih konfigurasi **androidApp** pada toolbar bagian atas.
2. Pilih emulator melalui **Device Manager**, atau gunakan HP dengan USB debugging aktif.
3. Klik tombol **Run**.
4. Tunggu proses build dan pemasangan aplikasi selesai.

Hasil program ditampilkan pada layar aplikasi Android.

### 3. Coba fitur aplikasi

1. Tunggu sekitar 2 detik sampai berita pertama muncul.
2. Pilih kategori untuk menyaring daftar berita.
3. Tekan **Baca detail** pada salah satu berita.
4. Tunggu sekitar 1 detik sampai detail ditampilkan.
5. Perhatikan jumlah **Sudah dibaca** yang bertambah.
6. Buka kembali berita yang sama. Hitungan tidak bertambah.
7. Centang **Simulasikan gagal memuat detail**, kemudian buka berita untuk mencoba penanganan kesalahan.
8. Hilangkan centangnya untuk mengambil detail secara normal.

Kategori berita muncul bergantian. Karena itu, berita baru untuk kategori yang sama muncul setiap 6 detik.

Menutup dialog ketika detail masih dimuat akan membatalkan pengambilan detail tersebut.

## Build dan Unit Test

Buka terminal Android Studio melalui **View > Tool Windows > Terminal**. Jalankan perintah berikut dari folder utama proyek menggunakan PowerShell.

### Membuat APK debug

```powershell
.\gradlew.bat :androidApp:assembleDebug
```

Perintah ini membuat APK tanpa membuka aplikasi di emulator. Untuk menjalankan aplikasi, gunakan tombol **Run**.

### Menjalankan unit test

```powershell
.\gradlew.bat :shared:testAndroidHostTest
```

Pengujian mencakup:

- Interval pengiriman berita setiap 2 detik.
- Pemrosesan data dengan `filter`, `map`, dan `onEach`.
- Pengambilan detail secara bersamaan menggunakan `async/await`.
- Kesalahan saat mengambil detail.
- Hitungan berita yang sudah dibaca tanpa menghitung ulang berita yang sama.
- Penyaringan kategori.
- Pembatalan pengambilan detail.

Tes menggunakan waktu virtual agar tidak perlu menunggu durasi simulasi sebenarnya. Jika berhasil, terminal menampilkan `BUILD SUCCESSFUL`. Laporan pengujian tersedia di folder `shared/build/reports/tests`.

## Gambaran Tampilan

Pada bagian atas aplikasi terdapat judul, identitas mahasiswa, jumlah berita masuk, dan jumlah berita yang sudah dibaca.

Di bawahnya terdapat pilihan kategori dan daftar berita. Setiap berita mempunyai tombol **Baca detail**. Saat tombol ditekan, aplikasi menampilkan indikator memuat, kemudian isi berita.

Daftar diperbarui secara otomatis. Riwayat tampilan dibatasi maksimal 100 berita. Jumlah dibaca disimpan selama sesi aplikasi dan kembali ke nol ketika proses aplikasi dimulai ulang.

## Konsep Kotlin yang Digunakan

### Flow

`flow` menghasilkan aliran berita, sedangkan `emit` mengirimkan berita kepada collector. `delay(2000)` memberi jeda tanpa memblokir thread.

Flow ini bersifat cold, sehingga proses menghasilkan berita dimulai ketika ada collector.

### Flow Operators

- `onEach` mencatat berita masuk.
- `filter` memilih berita sesuai kategori.
- `map` mengubah data menjadi format tampilan.
- `catch` menangani kesalahan pada aliran.
- `collect` menerima hasil untuk memperbarui state aplikasi.

### StateFlow

`MutableStateFlow` menyimpan keadaan aplikasi dan jumlah berita yang sudah dibaca. State diberikan ke tampilan melalui `StateFlow` yang hanya bisa dibaca.

ID berita yang sudah dibaca dicatat agar pembacaan ulang tidak menambah hitungan.

### Coroutines

`viewModelScope` menjalankan coroutine sesuai masa hidup ViewModel. Pengambilan detail menggunakan `async/await` dengan `Dispatchers.IO` pada Android.

`supervisorScope` digunakan agar kegagalan pengambilan detail dapat ditangani tanpa menghentikan aliran berita. Pembatalan coroutine tetap diteruskan agar pekerjaan yang tidak dibutuhkan dapat dihentikan.

