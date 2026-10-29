# Okur

## Bütçe & Hedef'e alıntı aktarımı (29.10.2026)
- Android uygulamasında vurgular (alıntılar) uygulamanın içine de kaydedilir; **Bütçe & Hedef** uygulaması bunları Alıntılar sekmesine alır. Yalnız Bütçe uygulaması okuyabilir (imza denetimi); başka uygulama erişemez.
- Yeni APK'yı kurduktan sonra Okur'u bir kez aç; mevcut tüm vurgular aktarılır, sonrakiler eklendikçe gider.

## 92 (29.10.2026)
- Kitaplıkta "Son okunan kitap" kartında kitap adına dokununca da kitap açılır.
- Kart kısaldı: düğme "Kaldığın yerden devam et" (tek satır), bilgi satırları tek satır; telefonda başlık en çok 2 satır.
- Kitap açılınca üst ve alt menü gelmez (tam ekran, yalnız metin); ekrana dokununca gelir. Üst bant varsayılanı "Kaybolan" yapıldı (Ayarlar › Üst bant › Sabit ile geri alınır).
- Kitap içindeki bölüm başlıkları (h1–h3) klasik: Palatino, ortalı, büyük, hafif harf aralığı, altında ince çizgi.

## 91 (28.10.2026)
- Kitaplık başlığında, sürümün yanında **kaynak etiketi**: yeşil **GitHub**, turuncu **Cloudflare · yedek**, mavi **Milli Emlak**, gri **Yerel dosya** — Okur'un o an hangi sunucudan çalıştığı görünür.

## 90 (27.10.2026)
- Okuma kilidi süresi bitince okunan sayfaya **yer imi** konur (zaten varsa ikinci kez konmaz), kitap kapanır ve **kitaplığa** dönülür.

## 89 (27.10.2026)
- Okuma kilidi sayacı sağ alt köşeye alındı; küçük ve çok silik, okumayı rahatsız etmez (acil çıkış: sayaca 3 sn basılı tut).
- Basit modda üst çubukta **tam ekran** düğmesi de görünür.

## 88 (27.10.2026) — Okuma kilidi
- Üst çubukta **saat** simgesi: "Kaç dakika okumak istiyorsun?" (10 / 15 / 20 / 30 / 45 / 60 dk ya da elle yaz). Süre bitene kadar kitaptan çıkılmaz: geri tuşu, kitaplığa dönüş ve sekmeyi kapatma engellenir; sayfa çevirme, İçindekiler, Yer imi ve Aa çalışır.
- Üstte geri sayım görünür. Uygulama kapanıp açılırsa aynı kitap kendiliğinden açılır, sayaç sürer. Süre bitince titreşim ve bildirim.
- **Acil çıkış:** sayaca 3 saniye basılı tut.
- Telefonda başka uygulamaya geçişi de engellemek için Android **Uygulama sabitleme** önerilir (açıklaması pencerede).

## 87 (27.10.2026) — Basit mod
- **Ayarlar › Ekran › Basit mod:** yalnız okuma — üstte geri, kitap adı, İçindekiler, Yer imi ve Aa; altta ilerleme çubuğu. Arama, sesli okuma, satır takibi, tam ekran düğmesi ve hızlı menü gizlenir (ayardan geri açılır).
- Cloudflare adresinde (pages.dev) varsayılan olarak açıktır.

## Android uygulaması (APK) — 83 (23.10.2026)
**İndir:** https://github.com/aaskinkk-cloud/okur/releases/download/android/okur.apk

- Okur 86: Moon+ Reader görünümü kaldırıldı, Okur'un kendi düğmelerine dönüldü (yazının bir satır yukarıdan başlaması korunur).
- Okur 85: **Moon+ Reader görünümü** (Android uygulamasında): üstte tema renginde şerit (geri · kitap adı · ⋮ menü); altta ilerleme çubuğu, sayfa okları ve iki sıra dolgun simge (İçindekiler, Ara, Yer imi, Sesli oku, Satır takibi, Akış, Görünüm / Kilit, Rastgele tema, Küçült, Büyüt, Yorumum, Sağ sayfa, Kitaplık); açık olanlar renkli görünür. Okuma yazısı bir satır yukarıdan başlar (kameranın altında kalır).
- 1.7 (28.10.2026): **Yedek adres** — GitHub sayfası açılmazsa (404, sunucu hatası, bağlantı sorunu) uygulama kendiliğinden Cloudflare kopyasından (ahmet-uygulamalar.pages.dev/okur) açılır; o da açılmazsa "Yeniden dene" ekranı çıkar. Not: Cloudflare kopyasının kitaplığı ayrıdır (kitaplar/yedek orada ayrıca yüklenir).
- 1.6 (26.10.2026): Uygulama simgesi: yeşil zemin üzerinde **açık kitap ve kırmızı yer imi şeridi** (yazısız; Android'in yuvarlak/kare simge biçimine uyar). Ana ekranda web sürümünden ("Ok") ve geçici Cloudflare sürümünden (turuncu bulutlu) ayırt edilir.
- 1.4 / Okur 84: Ortaya dokununca açılan üst menü ekranın en üstünde (saat/pil çubuğunun yerinde) açılır; yazı yine ön kameranın altından başlar.
- 1.3: Ön kamera bölgesindeki siyah şerit kaldırıldı: o bölge sayfanın renginde (temayla birlikte değişir), yazı yine kameranın altından başlar.
- 1.2: Tam ekran kesinleştirildi (pencere tam ekran + yeni ve eski Android yöntemi birlikte, pencere hazır olunca yinelenir). Yeni APK eskisinin üzerine kurulur, veriler silinmez.

- Telefon uygulaması gibi **kalıcı tam ekran**: açılışta tam ekran, hiç çıkmaz. Alttaki tuşlar yukarı kaydırılınca sayfanın **üstüne biner**, sayfa kaymaz/yeniden dizilmez, birkaç saniye sonra kendiliğinden gizlenir.
- Chrome'un "Tam ekrandan çıkmak için…" uyarısı ve kelimeye dokununca çıkan Google paneli bu uygulamada **yoktur**.
- Ortaya dokununca menüler açılır, tekrar dokununca gizlenir. Yazı ön kameranın altına girmez.
- Sayfa GitHub'daki Okur'dur: buraya gönderilen her güncelleme uygulamaya kendiliğinden gelir; APK'yı yeniden kurmak gerekmez.
- Yedek al / dışa aktarım dosyaları **İndirilenler** klasörüne kaydedilir; kitap ve yedek seçimi telefonun dosya seçicisiyle yapılır.
- Kaynak: `android/` klasörü; APK'yı GitHub Actions ("Okur Android APK") derler. İmza anahtarı (`android/okur.jks`) sabit tutulur, böylece yeni sürüm eskisinin üzerine kurulur ve veriler silinmez.
- Not: Uygulamanın verisi Chrome'dakinden ayrıdır; ilk açılışta kitapları ekleyip **Yedeği yükle** ile kayıtları geri getir.

## 82 (23.10.2026) — Tam ekrana geçişte sayfa atlaması / boş sayfa
- Ekran kaydında görüldü: tam ekrana girerken tarayıcı ekran boyunu birkaç adımda değiştiriyor, kitap 3–4 kez yeniden çiziliyordu (boş sayfa → önceki sayfa → boş → bir sonraki sayfa). Artık geçiş boyunca sayfa kâğıt rengi bir örtüyle gizlenir, ekran durulunca **tek kez** yerleşilir ve okunan satıra dönülür; örtü ~1,5 sn sonra kalkar.
- Test: 4 adımlı boy değişimiyle tam ekrana giriş → aynı konum; çıkış → okunan satır ekranda.
- **Chrome sekmesinde** alt tuşlar açılınca tarayıcı sayfanın tamamını aşağı itip geri alıyor (kayıtta görülen titreme); bunu sayfa engelleyemez. Ana ekrandan **yüklenmiş uygulamada** tuşlar sayfanın üstüne biner, sayfa kaymaz.

## 81 (23.10.2026) — Üst boşluk geri alındı
- 80'deki "ilk satır çubuğun hemen altından başlasın" değişikliği geri alındı (yazı ön kameranın altına giriyordu); yazı yine bir satır aşağıdan başlar. 80'deki ayar güncellemesi (Benim ayarlarım) korunur.

## 80 (23.10.2026) — Ayarlar güncellendi, üst boşluk kaldırıldı
- **Benim ayarlarım** (varsayılan + Hazır görünümler'deki "Benim ayarlarım · Yosun") 23.10.2026 01:09 yedeğine göre güncellendi: kenar boşluğu 8, sol 17 / sağ 7 px, harf aralığı 0,01, köşe yuvarlaklığı 2, cümle boşluğu %100 satır, üst bant sabit (yüzen).
- **Telefonda EPUB:** sayfanın iç üst/alt boşluğu çubukların altına alındı; ilk satır üst simge çubuğunun hemen altından başlar, sayfaya en az bir satır daha sığar.

## 79 (23.10.2026) — Benim ayarlarım
- **Varsayılan ayarlar:** İlk kurulumda / uygulama silinip yeniden yüklenince Ahmet'in ayarlarıyla açılır (23.10.2026 yedeği): Yosun tema, serif, yazı %100 (otomatik boyut kapalı), satır 1,25, paragraf arası boşluk, kenar boşluğu 0 (sol 8 / sağ 3 px), çerçevesiz, sayfa genişliği 350, yüzen üst bant, kaybolan menü, kilit, satır takibi, kitap açılınca tam ekran, sesli okuma 1,0×, vurgu rengi #9fd3b0.
- **Temalar › Hazır görünümler:** En üstte **"Benim ayarlarım · Yosun"**; tek dokunuşla bu görünüme dönülür.
- **Değiştirilen ayar:** "Menü açılınca tam ekrandan çık" kapatıldı. Açıkken ortaya her dokunuşta uygulama tam ekrandan çıkıp yeniden giriyordu: alttaki uyarı, sayfanın yeniden dizilmesi, yazı kaybolması ve sayfa atlaması buradan geliyordu. Ayarlar › Ekran'dan yeniden açılabilir (önerilmez).

## 78 (23.10.2026) — Tam ekran alanı
- Tam ekranda üst menü en üste (sistem çubuğunun yerine), alt bant en alta çıkar; okuma alanı üstten ve alttan genişler.
- Tam ekrana giriş/çıkış bilinçli geçiş sayılır: sayfa bir kez yerleşir ve aynı sayfada kalır. Alt gezinme tuşlarının geçici görünmesi yine yok sayılır (yeniden dizilme yok).
- Test: tam ekrana giriş → alan 760→860 px, 1 yerleşim, sayfa aynı; tuşlar 4 kez aç/kapa → 0 ek yerleşim; çıkış → sayfa aynı.

## 77 (23.10.2026) — Tam ekran düğmesi geri
- Kurulu uygulamada gizlenen tam ekran düğmesi yeniden görünür ve çalışır. Düğmeye basınca Chrome kendi "Tam ekrandan çıkmak için…" uyarısını bir kez gösterir (tarayıcı kuralı). Dokununca kendiliğinden tam ekran isteği gönderilmez.

## 76 (22.10.2026) — Kurulu uygulamada tam ekran düğmesi
- Ana ekrandan yüklenen uygulama baştan tam ekran açıldığı için tam ekran düğmesi gizlendi ve basılsa da tam ekran isteği gönderilmez. Önceden düğmeye basınca uyarı çıkıyor, sayfa yeniden diziliyor, yazılar kayboluyor / sayfa değişiyordu.

## 75 (22.10.2026) — Tam ekran uyarısı kesin çözüm
- Sayfanın kendiliğinden tam ekrana geçme istekleri (ilk dokunuşta, açılışta, dosya seçiciden dönünce) **tamamen kaldırıldı**; Chrome'un "Tam ekrandan çıkmak için…" uyarısı ekrana dokununca artık çıkmaz. Tam ekran isteği yalnız tam ekran düğmesine basınca gönderilir. Ana ekrandan yüklenen uygulama zaten tam ekran açılır.

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

> 27.10.2026: Hesap geri açıldıktan sonra sayfa yeniden yayımlandı.

## Dosyalar

| Dosya | Açıklama |
|---|---|
| `index.html` | Uygulamanın tamamı (tek dosya) |
| `sw.js` | Çevrimdışı önbellek ve sürüm (`V=P+'…'`) |
| `manifest.webmanifest`, `icon-*.png`, `icon.svg` | Ana ekran simgesi ve uygulama bilgileri |

## Sürüm

**74** (22.10.2026)
- **Kâğıt dokusu ayarı:** Görünüm ve okuma › Tema ve parlaklık altında, **Kâğıt** ve **Doğal kâğıt** temalarında "Kâğıt dokusu" kaydırıcısı (%0–100; 100 = önceki görünüm, 0 = düz kâğıt) ve **Sıfırla** düğmesi. Doku ve kenar gölgesi birlikte silikleşir; sayfa rengi ve yazı değişmez. Ayar kalıcıdır.

**73** (22.10.2026)
- **Kurulu uygulamada da titreme yok:** Küçük ekran boyu değişimleri (alt tuşlar, durum çubuğu) artık tam ekranda, kurulu uygulamada ve normal pencerede hiçbir zaman yeniden dizilme yapmaz; ekran uzarsa sahne büyür, sayfa yerinde kalır. Yalnız döndürmede yeniden yerleşir.
- **Tam ekran uyarısı:** Kurulu uygulama zaten tam ekran olduğu için içeriden tam ekran isteği gönderilmez → Chrome'un "Tam ekrandan çıkmak için…" uyarısı çıkmaz. (Chrome sekmesinde tam ekrana her girişte bu uyarıyı tarayıcı kendisi gösterir; sayfa kapatamaz.)
- **Kelimeye dokununca Google paneli:** Chrome'un "Dokunarak ara" paneli açılmasın diye sayfa ve EPUB çerçeveleri dokunuşu kendisi karşılar. Uzun basıp seçme çalışır.
- "Sessiz ekran" bloğu diğer uygulamalarla ortaktır.

**72** (22.10.2026)
- **Tam ekranda alt tuşlar: titreme / yanıp sönme tamamen kaldırıldı** (Bütçe & Hedef'teki gibi). Tam ekrandayken alttaki gezinme tuşları yukarı itilince okuma alanı hiç yeniden dizilmez; tuşlar sayfanın üstüne biner, kaybolunca aynı sayfa olduğu gibi görünür. v70'teki "1,5 sn sonra yeniden yerleş" adımı kaldırıldı (tuşlar 2–3 sn açık kaldığı için önce kısa, sonra uzun boya iki kez diziliyordu).
- Tam ekran dışında tuşlar kalıcı açılırsa bir kez sakin yerleşim yapılır (satırlar tuşların altında kalmaz). Ekran döndürme normal çalışır.
- Test: tam ekranda 6 kez tuş göster/gizle ve 5 sn açık bekleme → 0 yeniden çizim.

**71** (19.10.2026)
- **Sürüm etiketi düzeltildi:** v69 ve v70'te ekrandaki sürüm yazısı (`<meta name="surum">`) güncellenmemişti; her yerde "sürüm 68 · 15.10.2026" görünüyordu. Milli Emlak modülü sürümü `sw.js`'den okuduğu için etiket düzeltmesinin oraya da inmesi amacıyla sürüm 71'e yükseltildi.
- **Güncelleme kontrolü:** Uygulama açılınca (ve uygulamaya geri dönülünce) sunucudaki sürüme bakar; daha yeniyse hemen indirir ve altta **"Yeni sürüm hazır – Yenile"** çıkar. Yavaş kurum ağında sayfa önbellekten açılsa da eski sürümde kalınmaz.
- **Kural:** Her sürümde `sw.js` (`V=P+'…'`) ve `index.html` içindeki `<meta name="surum" content="…" data-tarih="…">` birlikte değiştirilir.

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
