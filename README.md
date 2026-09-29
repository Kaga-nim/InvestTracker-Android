# 📈 InvestTracker - Aplikasi Simulasi Investasi

> **Proyek Tugas Pertemuan 7 - Mata Kuliah Mobile Programming**

InvestTracker adalah aplikasi Android berbasis **Jetpack Compose** yang dirancang untuk membantu pengguna mensimulasikan pertumbuhan investasi dari waktu ke waktu berdasarkan modal awal, persentase return bulanan, serta rentang tanggal investasi yang dipilih.

---

## 📌 Latar Belakang & Tujuan

Aplikasi ini dikembangkan sebagai pemenuhan **Tugas Pertemuan 7** pada mata kuliah **Mobile Programming**. Tujuan utama dari proyek ini adalah mengimplementasikan antarmuka modern dengan **Jetpack Compose**, penggunaan komponen UI interaktif seperti `DatePickerDialog`, integrasi pustaka grafik **MPAndroidChart**, serta kalkulasi finansial berbasis bunga berbunga (*compound interest*).

---

## 🚀 Fitur Utama

- 💵 **Input Modal Awal & Return**: Pengguna dapat memasukkan jumlah modal awal (Rp) dan estimasi *return* bulanan (%).
- 📅 **Pemilihan Rentang Tanggal**: Penggunaan `DatePickerDialog` native untuk memilih tanggal mulai dan tanggal selesai investasi secara intuitif.
- 🧮 **Kalkulasi Bunga Berbunga Harian (Compound Growth)**: Menghitung pertumbuhan saldo harian secara akurat dengan rumus compound harian:
  $$\text{dailyRate} = \left(1 + \frac{\text{persenBulanan}}{100}\right)^{\frac{1}{30}} - 1$$
- 📊 **Grafik Pertumbuhan Interaktif**: Visualisasi grafik kurva miring yang halus (*cubic bezier curve*) menggunakan integrasi **MPAndroidChart** via Compose `AndroidView`.
- 💰 **Format Mata Uang Rupiah**: Hasil pertumbuhan investasi akhir ditampilkan dalam format standar Indonesia (`Rp X.XXX.XXX`).

---

## 📲 Download & Instalasi (File APK Siap Pakai)

Aplikasi ini telah di-build dan siap untuk dicoba dipasang di perangkat Android:

- **File APK Root Project**: [`InvestTracker.apk`](./InvestTracker.apk) *(12.3 MB)*
- **Lokasi Build Output**: `app/build/outputs/apk/debug/app-debug.apk`

### Langkah Pemasangan APK:
1. Unduh atau salin file [`InvestTracker.apk`](./InvestTracker.apk) ke HP Android Anda.
2. Buka file APK melalui aplikasi File Manager di HP Anda.
3. Apabila muncul peringatan keamanan, aktifkan opsi **"Allow/Permit from this source"** (Izinkan instalasi dari sumber tak dikenal).
4. Selesaikan proses instalasi dan buka aplikasi **InvestTracker**.

---

## 🛠️ Teknologi & Pustaka (Tech Stack)

| Komponen | Teknologi / Pustaka |
| :--- | :--- |
| **Bahasa Pemrograman** | [Kotlin](https://kotlinlang.org/) (JDK 17) |
| **UI Toolkit** | [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3) |
| **Data Visualization** | [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) |
| **Date & Time API** | `java.time` dengan *Desugaring* enabled |
| **Minimum SDK** | API Level 24 (Android 7.0 Nougat) |
| **Target & Compile SDK** | API Level 36 |

---

## 📂 Struktur Direktori Proyek

```text
InvestTracker/
├── InvestTracker.apk            # File APK siap install
├── README.md                    # Dokumentasi proyek
├── build.gradle.kts             # Konfigurasi Gradle root
├── settings.gradle.kts          # Konfigurasi repository & plugin
└── app/
    ├── build.gradle.kts         # Konfigurasi modul app & dependensi
    └── src/
        └── main/
            ├── AndroidManifest.xml
            └── java/id/kaganim/investtracker/
                ├── MainActivity.kt    # Logic perhitungan, UI Compose, & MPAndroidChart
                └── ui/theme/          # Tema, Warna, & Tipografi Material 3
```

---

## 💻 Cara Menjalankan dari Source Code

Jika Anda ingin menjalankan proyek ini dari Android Studio:

1. **Clone atau buka repositori ini** di **Android Studio** (Disarankan versi Ladybug / 2024.2+).
2. Pastikan versi **JDK** pada project terkonfigurasi ke **Java 17**.
3. Jalankan **Gradle Sync** hingga selesai.
4. Hubungkan perangkat Android/Emulator, lalu klik tombol **Run `app`** (`Shift + F10`).

Untuk melakukan kompilasi file APK manual via CLI:
```bash
./gradlew assembleDebug
```
Hasil file APK akan dibuat di `app/build/outputs/apk/debug/app-debug.apk`.

---

## 👨‍💻 Informasi Tugas

- **Mata Kuliah**: Mobile Programming
- **Pertemuan**: 7
- **Package Application**: `id.kaganim.investtracker`
