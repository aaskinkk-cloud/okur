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

**70** (17.10.2026)
- **Alt gezinme tuşlarında siyah ekran / titreme giderildi:** Telefonun alt tuşları (veya durum çubuğu) açılıp kapanınca ekran yüksekliği biraz değişiyor, bu da her seferinde kitabı baştan dizdiriyordu (yeniden çizim + konum koruma + boş ekran koruması üst üste). Artık genişlik aynı ve yükseklik farkı küçükse okuma alanı sabit kalır, tuşlar sayfanın üstüne biner; hiçbir şey yeniden çizilmez. Tuşlar kalıcı açık kalırsa menü kapalıyken bir kez sakin yerleşim yapılır. Ekran döndürme normal çalışır. "Menü açılınca tam ekrandan çık" ayarı da artık titretmez.

**69** (17.10.2026)
- **Kaldığın yer hatası düzeltildi:** Kitap açılırken, tam ekran/pencere boyu koruma özelliği konumu kitap henüz yüklenirken (çoğu zaman 1. sayfada) alıp 1–2 sn sonra oraya geri atıyor ve bunu kaydediyordu. Artık açılışta kayıtlı konum korunur; açılış sırasındaki ara konumlar kaydı ezmez.
- **En ileri okunan yer:** Her kitap için en ileri okuduğun yer ayrıca saklanır (geri dönüp okusan da kaybolmaz).
- **Alttan "Kaldığın yer" menüsü:** Kitap açıldığında daha ileride bir yer varsa (en ileri okuduğun yer, en ileri yer imi, en son koyduğun yer imi) alttan seçenekler çıkar: dokun → oraya git, ya da **Burada kal**. "Bir daha sorma" o anki yerleri susturur. Sayfa çevirince menü kapanır.
- **Yer imleri listesi:** Üstte "En ileri okuduğun yer" düğmesi; yer imlerinde **En son konan** ve **En ileri** etiketleri. Eski bir yer imine uğramak artık onu "son" yapmaz. Geride kalan bir yer imine dokununca hangisine gidileceği sorulur.
- Ayarlar › **Kaldığın yeri sor** ile kapatılabilir. Yedek dışa/içe aktarmada en ileri yer de taşınır.

**68** (15.10.2026)
- **Sesi dosyaya kaydet (WAV / MP3):** Ekran paylaşımı kaldırıldı ("Paylaşım izni verilmedi" hatası yok). Metin **Okur sesiyle** (Fahrettin / Fettah / DFKI) doğrudan dosyaya çevrilir ve İndirilenler'e iner; okunmasını beklemek gerekmez, ilerleme ve kalan süre görünür, Vazgeç ile durdurulur. Bilgisayarda ve telefonda aynı çalışır. Okuma hızı ayarı dosyaya da uygulanır. Bilgisayarda eski yöntem (sistem sesi + ekran kaydı) seçenek olarak duruyor.
- **Windows'ta Okur sesi:** Okur sesi telefona özgü değil, tarayıcının içinde çalışır; Windows'ta da Ayarlar › Sesli okuma › **Okur sesi** seçilince okur. Ses dosyaları artık önce Okur'un kendi adresinden (`ses/` klasörü) iner — kurum ağında huggingface/jsdelivr kapalı olsa da çalışır. `ses/` klasörü GitHub Actions'taki **"Okur sesi dosyalarını indir"** iş akışıyla dolar.
- **Tam ekran:** Tam ekrana girip çıkınca ya da pencere boyu değişince kitap aynı sayfada kalır; sayfa numarası atlamaz, kitap düzeni bozulmaz (art arda aç/kapa da kaydırmaz).
- **Sayfa çevirme ve yer imi:** Kitabın dışındaki sol/sağ boşluğa tıklamak sayfa çevirir (sağ → sonraki, sol → önceki). Boşluğa tıklamak artık yer imi koymaz. Yer imi **yalnız sayfanın sağ üst köşesine** tıklayınca/dokununca eklenir veya kaldırılır.
- **OCR motoru OCR Stüdyo ile ortak:** Taranmış PDF okumada OCR Stüdyo'nun gömülü motoru ve Türkçe modeli kullanılır (`ocr-studyo/motor/`); alınamazsa eski yol (jsdelivr) devreye girer.

67 — Taranmış kitap OCR'ı OCR Stüdyo hattıyla: 300 dpi, kenar kırpma, ışık/kontrast/netlik iyileştirme, eğim düzeltme, kelime güvenine göre sözlüklü düzeltme. Kötü taranmış test sayfasında hata %192 → %3,3.  
66 — önceki sürüm.
