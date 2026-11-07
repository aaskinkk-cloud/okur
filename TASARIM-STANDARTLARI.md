# Tasarım standartları (Ahmet'in uygulamaları — her düzenlemede sorulmadan uygulanır)

Bu kurallar Okur & Bütçe ve Ahmet'in diğer uygulamaları (Tını, Karanlık Oda, Kareden Videoya, İkibe…) için **varsayılandır**. Yeni bir düzenleme yaparken tekrar sorulmaz, kontrol listesi olarak uygulanır.

## Ekran ve sistem çubukları
1. Uygulama açılışta **tam ekran** açılır.
2. Chrome'un **"Tam ekrandan çıkmak için…"** uyarısı çıkmaz (tam ekran isteği kendiliğinden gönderilmez).
3. Bir kelimeye dokununca Google **"Dokunarak ara"** paneli açılmaz.
4. Alt gezinme çubuğu yukarı itilince **titreme / kararma / kayma olmaz** (örnek: Bütçe & Hedef'in kararlı davranışı).
5. **Üstteki sistem çubuğu / ön kamera bölgesi temanın parçasıdır:** ayrı düz renkli bant olmaz; bölgeyi uygulamanın kendi başlığı (tema resmi/degradesi) boyar. Sayfa kayınca o bölge sayfa renginde ince bir şeritle örtülür. `theme-color` her zaman başlığın en üst rengini bildirir.
6. Başlıkta **boş satır, işlevsiz logo/ikon** bırakılmaz; dikey boşluk dengeli tutulur.

## Düzen
7. Bölüm başlıkları **açılır-kapanır** (dokununca aç/kapa); başlığa dokunmak yeniden adlandırma/düzenleme menüsü açmaz.
8. Düğmeler **düzgün hizalı satırlarda**, boş hücre bırakmadan; oynatıcıların altındaki araç çubukları **tek satır**.
9. Yeni düğmeler **ilgili oldukları yerde, belirgin** durur; görsel tasarım işlevsellik kadar önemlidir.
10. Telefon ekranında **taşma ve yatay kayma olmaz** (380 px genişlikte denenir).

## Temalar
11. Her tema (Klasik, Karadeniz, kitaplık temaları…) **tüm sayfalarda belirgin biçimde** uygulanır; bir sekmede iki tema aynı görünmez.
12. Yeni bir görünüm eskisinin yerine geldiğinde **eski görünüm silinmez, seçenek olarak kalır** (örnek: Notlar › Aa › Kart / Defter).

## Veri ve teslim
13. Mevcut kayıtlar, yedekler ve işlevler **bozulmaz**; yeni alanlar isteğe bağlı eklenir.
14. Yalnız **ücretsiz** çözümler (ücretli API / faturalı hesap yok).
15. Her değişiklikte **README** güncellenir; sürüm numarası **tüm sayfalarda birlikte** (`<meta name="surum">` + `sw.js`) artırılır.
16. GitHub'a gönderim **toplu** yapılır (tekrar tekrar push yok); güncel dosyalar sohbette indirilebilir olarak da verilir.
