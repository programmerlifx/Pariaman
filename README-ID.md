# the lifx — Android

Aplikasi Android native sederhana dengan tema dark navy + blue, navigasi bawah, checklist tugas, dan catatan lokal.

## Fitur
- Beranda dan kartu pintasan.
- Checklist tugas harian dengan progress.
- Catatan yang disimpan di penyimpanan lokal aplikasi (SharedPreferences).
- Navigasi Home / Tasks / Notes / About.
- Tidak membutuhkan login atau API eksternal.

## Build di Termux
Build native Android di Termux perlu JDK, Gradle, Android SDK yang kompatibel ARM64, dan ruang kosong yang cukup. Salah satu toolchain komunitas yang menyediakan setup Termux adalah Termux Studio:
https://github.com/poordevcode/termux-android-studio

Ikuti instruksi instalasi pada README tool tersebut, lalu:

```bash
termux-setup-storage
pkg update
pkg install unzip git
```

Ekstrak ZIP proyek ke direktori home Termux. Jika ZIP ada di folder Download:
```bash
cp /sdcard/Download/the-lifx-android.zip ~/
cd ~
unzip the-lifx-android.zip
cd the-lifx-android
```

Buat `local.properties` dengan path SDK yang sesuai instalasi kamu. Contoh:
```bash
echo "sdk.dir=$HOME/Android/android-sdk" > local.properties
```
Periksa path SDK terlebih dahulu dengan `ls "$HOME/Android"`; sesuaikan jika nama folder SDK berbeda.

Lalu jalankan build:
```bash
gradle assembleDebug
```

Jika memakai Termux Studio, bisa coba:
```bash
studio build .
```

APK debug yang dihasilkan:
`app/build/outputs/apk/debug/app-debug.apk`

Salin ke folder Download:
```bash
cp app/build/outputs/apk/debug/app-debug.apk /sdcard/Download/the-lifx.apk
```

Buka aplikasi Files / File Manager, masuk ke Download, lalu tekan `the-lifx.apk` untuk memasang. Android mungkin meminta izin "Install unknown apps" untuk aplikasi File Manager yang digunakan.

## Catatan
- Build pertama akan mengunduh Gradle/Android plugin dan memerlukan internet serta ruang penyimpanan.
- Kompatibilitas Termux tergantung arsitektur perangkat, SDK, JDK, dan versi toolchain. Jika build gagal, kirimkan log error lengkap untuk diperbaiki.
- Ini APK debug untuk instalasi pribadi, bukan rilis Play Store.
