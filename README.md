# Okur

EPUB / PDF okuyucu ve dönüştürücü (Türkçe) — telefonda, internetsiz çalışır; dosyalar cihazdan çıkmaz.

**Uygulamayı aç:** https://aaskinkk-cloud.github.io/okur/

Milli Emlak uygulamasının **Okur** modülü de buradan gelir (sürüm `sw.js`'den okunur, yeni sürüm kendiliğinden iner).

## Taranmış kitapları EPUB'a dönüştürme (OCR)

Metin katmanı olmayan ya da bozuk olan PDF sayfaları Türkçe tanıma (Tesseract) ile yeniden okunur. 67'den itibaren OCR Stüdyo'nun okuma hattı kullanılır:

- Sayfa **300 dpi** çizilir (önceden ~190 dpi).
- Görüntü iyileştirme: tarayıcı/fotokopi **siyah kenarları kırpılır**, **gölge/düzensiz ışık dengelenir**, **kontrast** artırılır, bulanık sayfa **netleştirilir**.
- **Eğim** otomatik düzeltilir.
- Kelime kelime **güven** ölçülür; leke/çizgi gürültüsü atılır. Güveni düşük ve sözlükte olmayan kelimeler 150 bin kelimelik Türkçe sözlükle düzeltilir; güveni yüksek kelimelere (özel adlar dahil) dokunulmaz.

Tanıma bileşenleri ilk kullanımda internetten iner (~15 MB), sonra tarayıcı önbelleğinde kalır.

## Dosyalar

| Dosya | Açıklama |
|---|---|
| `index.html` | Uygulamanın tamamı (tek dosya) |
| `sw.js` | Çevrimdışı önbellek ve sürüm (`V=P+'…'`) |
| `manifest.webmanifest`, `icon-*.png`, `icon.svg` | Ana ekran simgesi ve uygulama bilgileri |

## Sürüm

**68** (15.10.2026)
- **Sesi dosyaya kaydet (WAV / MP3):** Ekran paylaşımı kaldırıldı ("Paylaşım izni verilmedi" hatası yok). Metin **Okur sesiyle** (Fahrettin / Fettah / DFKI) doğrudan dosyaya çevrilir ve İndirilenler'e iner; okunmasını beklemek gerekmez, ilerleme ve kalan süre görünür, Vazgeç ile durdurulur. Bilgisayarda ve telefonda aynı çalışır. Okuma hızı ayarı dosyaya da uygulanır. Bilgisayarda eski yöntem (sistem sesi + ekran kaydı) seçenek olarak duruyor.
- **Windows'ta Okur sesi:** Okur sesi telefona özgü değil, tarayıcının içinde çalışır; Windows'ta da Ayarlar › Sesli okuma › **Okur sesi** seçilince okur. Ses dosyaları artık önce Okur'un kendi adresinden (`ses/` klasörü) iner — kurum ağında huggingface/jsdelivr kapalı olsa da çalışır. `ses/` klasörü GitHub Actions'taki **"Okur sesi dosyalarını indir"** iş akışıyla dolar.
- **Tam ekran:** Tam ekrana girip çıkınca ya da pencere boyu değişince kitap aynı sayfada kalır; sayfa numarası atlamaz, kitap düzeni bozulmaz (art arda aç/kapa da kaydırmaz).
- **Sayfa çevirme ve yer imi:** Kitabın dışındaki sol/sağ boşluğa tıklamak sayfa çevirir (sağ → sonraki, sol → önceki). Boşluğa tıklamak artık yer imi koymaz. Yer imi **yalnız sayfanın sağ üst köşesine** tıklayınca/dokununca eklenir veya kaldırılır.
- **OCR motoru OCR Stüdyo ile ortak:** Taranmış PDF okumada OCR Stüdyo'nun gömülü motoru ve Türkçe modeli kullanılır (`ocr-studyo/motor/`); alınamazsa eski yol (jsdelivr) devreye girer.

67 — Taranmış kitap OCR'ı OCR Stüdyo hattıyla: 300 dpi, kenar kırpma, ışık/kontrast/netlik iyileştirme, eğim düzeltme, kelime güvenine göre sözlüklü düzeltme. Kötü taranmış test sayfasında hata %192 → %3,3.  
66 — önceki sürüm.
