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

**67** — Taranmış kitap OCR'ı OCR Stüdyo hattıyla: 300 dpi, kenar kırpma, ışık/kontrast/netlik iyileştirme, eğim düzeltme, kelime güvenine göre sözlüklü düzeltme. Kötü taranmış test sayfasında hata %192 → %3,3.  
66 — önceki sürüm.
