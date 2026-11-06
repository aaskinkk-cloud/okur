package app.okur.android;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.util.TypedValue;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Okur — Android kabuğu.
 * Sayfa: https://aaskinkk-cloud.github.io/okur/ (GitHub'a gönderilen her güncelleme kendiliğinden gelir, çevrimdışı da açılır).
 * Ekran: kalıcı tam ekran (immersive). Alttaki tuşlar kaydırılınca sayfanın ÜSTÜNE biner, sayfa yeniden boyutlanmaz,
 * birkaç saniye sonra kendiliğinden gizlenir. Chrome'un "Tam ekrandan çıkmak için…" uyarısı ve "Dokunarak ara" paneli bu uygulamada yoktur.
 */
public class MainActivity extends Activity {
    private static final String ADRES = "https://aaskinkk-cloud.github.io/okur/";
    /** GitHub açılmazsa (404, sunucu hatası, bağlantı yok) sayfa buradan açılır. */
    private static final String YEDEK = "https://ahmet-uygulamalar.pages.dev/okur/";
    private boolean yedekte = false;
    /* 1.8: açılışta bağlantı henüz hazır değilse ilk deneme düşüyor → önce aynı adres yeniden denenir, sonra önbellekten, sonra yedek; hata ekranı kendiliğinden yeniden dener */
    private int deneme = 0, otoDeneme = 0;
    private boolean sayfaHata = false;
    private static final int DOSYA_SEC = 41;
    private WebView web;
    private ValueCallback<Uri[]> dosyaCb;
    private android.widget.FrameLayout kap;
    private int kameraPx = 0;

    /** Ön kamera yüksekliğini sayfaya bildirir (CSS: --kamera). Üst menü en üstte açılır, yazı kameranın altından başlar. */
    private void kameraYaz() {
        if (web == null) return;
        float d = getResources().getDisplayMetrics().density;
        int css = Math.round(kameraPx / d);
        web.evaluateJavascript("document.documentElement.style.setProperty('--kamera','" + css + "px');try{RUI&&RUI.open&&RUI.vfit()}catch(e){}", null);
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES; // ekranın tamamı (kamera bölgesi dahil) kullanılır
            w.setAttributes(lp);
        }
        web = new WebView(this);
        web.setBackgroundColor(0xFFDAE4D4);
        /* Kamera bölgesi siyah şerit olarak kalmasın: kap, sayfanın renginde boyanır; yazı kameranın altından başlar. */
        kap = new android.widget.FrameLayout(this);
        kap.setBackgroundColor(0xFFDAE4D4);
        kap.addView(web, new android.widget.FrameLayout.LayoutParams(-1, -1));
        setContentView(kap);
        kap.setOnApplyWindowInsetsListener((v, ins) -> {
            int ust = 0, sol = 0, sag = 0;
            if (Build.VERSION.SDK_INT >= 28 && ins.getDisplayCutout() != null) {
                ust = ins.getDisplayCutout().getSafeInsetTop();
                sol = ins.getDisplayCutout().getSafeInsetLeft();
                sag = ins.getDisplayCutout().getSafeInsetRight();
            }
            kameraPx = ust;
            kameraYaz();
            return ins;
        });

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(false);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setTextZoom(100);
        s.setUserAgentString(s.getUserAgentString() + " OkurAndroid/1");
        web.addJavascriptInterface(new Kopru(), "OkurAndroid");

        web.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView v, String url, android.graphics.Bitmap fav) { if (url != null && url.startsWith("http")) sayfaHata = false; }
            @Override public void onPageFinished(WebView v, String url) {
                if (url != null && url.startsWith("http") && !sayfaHata) { deneme = 0; otoDeneme = 0; v.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); }
                v.evaluateJavascript(INDIR_JS, null); kameraYaz(); tamEkran(); }
            @Override public void onReceivedHttpError(WebView v, android.webkit.WebResourceRequest r, android.webkit.WebResourceResponse res) {
                if (r.isForMainFrame() && res != null && (res.getStatusCode() == 404 || res.getStatusCode() >= 500)) { sayfaHata = true; yedegeGec(v, "HTTP " + res.getStatusCode()); }
            }
            @Override public void onReceivedError(WebView v, android.webkit.WebResourceRequest r, android.webkit.WebResourceError e) {
                if (r.isForMainFrame()) { sayfaHata = true; yedegeGec(v, e.getErrorCode() + " " + e.getDescription()); }
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> cb, FileChooserParams p) {
                if (dosyaCb != null) dosyaCb.onReceiveValue(null);
                dosyaCb = cb;
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, p.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE);
                try { startActivityForResult(i, DOSYA_SEC); } catch (Exception e) { dosyaCb = null; return false; }
                return true;
            }
        });
        if (b != null) web.restoreState(b); else web.loadUrl(ADRES);
        tamEkran();
    }

    /** Kalıcı tam ekran: sistem çubukları gizli; kaydırınca geçici görünür ve sayfanın üstüne biner.
     *  Hem yeni (Android 11+) hem eski yöntem birlikte uygulanır; pencere hazır olmadan çağrılırsa bir sonraki karede yinelenir. */
    private void tamEkran() {
        final Window w = getWindow();
        final View d = w.getDecorView();
        w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        d.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        if (Build.VERSION.SDK_INT >= 30) {
            Runnable r = () -> {
                WindowInsetsController c = d.getWindowInsetsController();
                if (c == null) c = w.getInsetsController();
                if (c != null) {
                    c.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                    c.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                }
            };
            r.run();
            d.post(r);
            d.postDelayed(r, 300);
        }
    }

    @Override public void onWindowFocusChanged(boolean f) { super.onWindowFocusChanged(f); if (f) tamEkran(); }
    @Override protected void onResume() { super.onResume(); tamEkran(); if (web != null) web.onResume(); }
    @Override protected void onPause() { if (web != null) web.onPause(); super.onPause(); }
    @Override protected void onSaveInstanceState(Bundle o) { super.onSaveInstanceState(o); if (web != null) web.saveState(o); }

    @Override public void onBackPressed() {
        if (tarKap != null && tarKap.getVisibility() == View.VISIBLE) { if (tar.canGoBack()) tar.goBack(); else tarayiciKapat(); return; }
        if (web != null && web.canGoBack()) web.goBack(); else super.onBackPressed();
    }

    /* ---------- v113: uygulama içi tarayıcı (Kitap indir) ----------
       Site Okur'un üstünde açılır; sitede EPUB/PDF indirilince dosya alınır ve Okur kitaplığına eklenir. */
    private LinearLayout tarKap; private WebView tar; private TextView tarBaslik;
    private File indirDosya; private String indirAd, indirTur;
    private int dp(int v) { return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, getResources().getDisplayMetrics())); }
    private TextView tarDugme(String t) {
        TextView b = new TextView(this); b.setText(t); b.setTextColor(0xFFFFFFFF); b.setTextSize(22); b.setGravity(Gravity.CENTER);
        b.setLayoutParams(new LinearLayout.LayoutParams(dp(48), dp(44))); return b;
    }
    private void tarayiciAc(String url) {
        if (tarKap == null) {
            tarKap = new LinearLayout(this); tarKap.setOrientation(LinearLayout.VERTICAL); tarKap.setBackgroundColor(0xFFFFFFFF);
            LinearLayout bar = new LinearLayout(this); bar.setOrientation(LinearLayout.HORIZONTAL); bar.setGravity(Gravity.CENTER_VERTICAL);
            bar.setBackgroundColor(0xFF2C6B3C); bar.setPadding(dp(2), kameraPx + dp(2), dp(2), dp(2));
            TextView geri = tarDugme("‹"), kapat = tarDugme("✕"), dis = tarDugme("⇱");
            dis.setContentDescription("Chrome'da aç");
            dis.setOnClickListener(v -> { String u = tar.getUrl(); if (u == null) return; Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(u)); i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try { i.setPackage("com.android.chrome"); startActivity(i); } catch (Exception e) { try { i.setPackage(null); startActivity(i); } catch (Exception e2) { } } });
            geri.setOnClickListener(v -> { if (tar.canGoBack()) tar.goBack(); else tarayiciKapat(); });
            kapat.setOnClickListener(v -> tarayiciKapat());
            tarBaslik = new TextView(this); tarBaslik.setTextColor(0xFFFFFFFF); tarBaslik.setTextSize(15); tarBaslik.setSingleLine(true);
            tarBaslik.setEllipsize(android.text.TextUtils.TruncateAt.END);
            bar.addView(geri); bar.addView(tarBaslik, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); bar.addView(dis); bar.addView(kapat);
            tar = new WebView(this);
            WebSettings ts = tar.getSettings(); ts.setJavaScriptEnabled(true); ts.setDomStorageEnabled(true); ts.setLoadWithOverviewMode(true); ts.setUseWideViewPort(true);
            /* 1.9: bazı devlet siteleri (ör. Kültür Bakanlığı e-Kitap) uygulama içi tarayıcı imzasını ("; wv") reddediyor → olağan Chrome kimliği */
            try { ts.setUserAgentString(ts.getUserAgentString().replace("; wv", "").replaceAll(" Version/[0-9.]+", "")); } catch (Exception e) { }
            ts.setSupportZoom(true); ts.setBuiltInZoomControls(true); ts.setDisplayZoomControls(false); ts.setAllowFileAccess(false);
            try { CookieManager.getInstance().setAcceptThirdPartyCookies(tar, true); } catch (Exception e) { }
            tar.setWebViewClient(new WebViewClient() {
                @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                    String sc = r.getUrl().getScheme();
                    if ("http".equals(sc) || "https".equals(sc)) return false;
                    try { startActivity(new Intent(Intent.ACTION_VIEW, r.getUrl())); } catch (Exception e) { }
                    return true;
                }
                @Override public void onPageStarted(WebView v, String u, android.graphics.Bitmap f) { try { tarBaslik.setText(Uri.parse(u).getHost()); } catch (Exception e) { } }
            });
            tar.setWebChromeClient(new WebChromeClient() {
                @Override public void onReceivedTitle(WebView v, String t) { if (t != null && !t.isEmpty()) tarBaslik.setText(t); }
            });
            tar.setDownloadListener((u, ua, cd, mt, len) -> indir(u, ua, cd, mt));
            tarKap.addView(bar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tarKap.addView(tar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
            kap.addView(tarKap, new android.widget.FrameLayout.LayoutParams(-1, -1));
        }
        tarKap.setVisibility(View.VISIBLE);
        if (url != null && !url.equals(tar.getUrl())) tar.loadUrl(url);
    }
    private void tarayiciKapat() { if (tarKap != null) tarKap.setVisibility(View.GONE); }
    private void ileti(String m) { runOnUiThread(() -> Toast.makeText(MainActivity.this, m, Toast.LENGTH_LONG).show()); }
    private void indir(String u, String ua, String cd, String mt) {
        String ad0 = URLUtil.guessFileName(u, cd, mt);
        ileti("İndiriliyor: " + ad0);
        new Thread(() -> {
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(u).openConnection();
                c.setInstanceFollowRedirects(true); c.setConnectTimeout(20000); c.setReadTimeout(90000);
                String ck = CookieManager.getInstance().getCookie(u); if (ck != null) c.setRequestProperty("Cookie", ck);
                if (ua != null) c.setRequestProperty("User-Agent", ua);
                int kod = c.getResponseCode(); if (kod >= 400) throw new Exception("sunucu " + kod + " döndü");
                File dir = new File(getCacheDir(), "indir"); dir.mkdirs();
                File[] eski = dir.listFiles(); if (eski != null) for (File x : eski) x.delete();
                File f = new File(dir, "kitap.bin"); long top = 0;
                try (InputStream in = c.getInputStream(); FileOutputStream o = new FileOutputStream(f)) {
                    byte[] b = new byte[65536]; int n;
                    while ((n = in.read(b)) > 0) { o.write(b, 0, n); top += n; if (top > 150L * 1024 * 1024) throw new Exception("dosya 150 MB'tan büyük"); }
                }
                byte[] h = new byte[64]; int hn;
                try (InputStream in = new java.io.FileInputStream(f)) { hn = in.read(h); }
                String bas = hn > 0 ? new String(h, 0, hn, "ISO-8859-1") : "";
                String tur = bas.startsWith("%PDF") ? "application/pdf" : (bas.startsWith("PK") && bas.contains("epub")) ? "application/epub+zip" : null;
                String ad = ad0 == null || ad0.isEmpty() ? "kitap" : ad0;
                if (tur == null) {
                    if (Build.VERSION.SDK_INT >= 29) {
                        ContentValues cv = new ContentValues(); cv.put(MediaStore.Downloads.DISPLAY_NAME, ad);
                        cv.put(MediaStore.Downloads.MIME_TYPE, mt == null || mt.isEmpty() ? "application/octet-stream" : mt);
                        Uri du = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, cv);
                        try (OutputStream o = getContentResolver().openOutputStream(du); InputStream in = new java.io.FileInputStream(f)) { byte[] b = new byte[65536]; int n; while ((n = in.read(b)) > 0) o.write(b, 0, n); }
                        ileti("Bu dosya EPUB ya da PDF değil; İndirilenler'e kaydedildi: " + ad);
                    } else ileti("Bu dosya EPUB ya da PDF değil; Okur açamaz.");
                    f.delete(); return;
                }
                if (tur.endsWith("pdf") && !ad.toLowerCase().endsWith(".pdf")) ad = ad.replaceAll("\\.(bin|php|aspx?)$", "") + ".pdf";
                if (tur.endsWith("zip") && !ad.toLowerCase().endsWith(".epub")) ad = ad.replaceAll("\\.(bin|zip|php|aspx?)$", "") + ".epub";
                indirDosya = f; indirAd = ad; indirTur = tur;
                final String a = ad, t = tur; final long boy = f.length();
                runOnUiThread(() -> {
                    tarayiciKapat();
                    Toast.makeText(MainActivity.this, "Kitaplığa ekleniyor: " + a, Toast.LENGTH_SHORT).show();
                    web.evaluateJavascript("window.okurIndirildi&&window.okurIndirildi(" + JSONObject.quote(a) + "," + boy + "," + JSONObject.quote(t) + ")", null);
                });
            } catch (Exception e) { ileti("İndirilemedi: " + e.getMessage()); }
        }).start();
    }

    @Override protected void onActivityResult(int req, int res, Intent d) {
        if (req == DOSYA_SEC && dosyaCb != null) {
            Uri[] r = null;
            if (res == RESULT_OK && d != null) {
                if (d.getClipData() != null) {
                    int n = d.getClipData().getItemCount(); r = new Uri[n];
                    for (int i = 0; i < n; i++) r[i] = d.getClipData().getItemAt(i).getUri();
                } else if (d.getData() != null) r = new Uri[]{ d.getData() };
            }
            dosyaCb.onReceiveValue(r); dosyaCb = null;
            return;
        }
        super.onActivityResult(req, res, d);
    }

    /** 1.8: GitHub açılmazsa: 1) 1,5 sn sonra yeniden dene 2) önbellekteki kopyayı aç 3) Cloudflare 4) hata ekranı — 5 sn'de bir kendiliğinden yeniden dener. */
    private void yedegeGec(WebView v, String neden) {
        runOnUiThread(() -> {
            if (!yedekte && deneme == 0) { deneme = 1; v.postDelayed(() -> v.loadUrl(ADRES), 1500); return; }
            if (!yedekte && deneme == 1) { deneme = 2; v.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); v.postDelayed(() -> v.loadUrl(ADRES), 800); return; }
            if (!yedekte) { yedekte = true; v.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                Toast.makeText(MainActivity.this, "GitHub açılmadı (" + neden + ") — yedek adresten (Cloudflare) açılıyor", Toast.LENGTH_LONG).show(); v.loadUrl(YEDEK); return; }
            otoDeneme++;
            v.loadDataWithBaseURL(null,
                "<html><body style='font:18px sans-serif;background:#dae4d4;color:#25362a;padding:40px 24px;text-align:center'>"
                + "<p>Okur şu an açılamıyor.<br>İnternet bağlantını kontrol et.</p><p style='font-size:13px;opacity:.6'>" + neden + "</p>"
                + (otoDeneme <= 6 ? "<p style='font-size:14px;opacity:.75'>Birkaç saniye içinde kendiliğinden yeniden denenecek…</p>" : "")
                + "<p><a href='" + ADRES + "' style='display:inline-block;margin-top:16px;padding:14px 22px;border-radius:12px;background:#2c6b3c;color:#fff;text-decoration:none'>Yeniden dene</a></p></body></html>",
                "text/html", "utf-8", null);
            if (otoDeneme <= 6) v.postDelayed(() -> { yedekte = false; deneme = 1; v.loadUrl(ADRES); }, 5000);
        });
    }

    private void renkUygula(String c) {
        try {
            int r = android.graphics.Color.parseColor(c.trim());
            kap.setBackgroundColor(r); web.setBackgroundColor(r); getWindow().getDecorView().setBackgroundColor(r);
        } catch (Exception e) { }
    }

    /** Sayfadan gelen dosyaları (yedek, dışa aktarım) İndirilenler klasörüne yazar. */
    class Kopru {
        @JavascriptInterface public void tarayici(String url) { runOnUiThread(() -> tarayiciAc(url)); }
        /** 1.10: siteyi Chrome'da açar (uygulama içi tarayıcıyı reddeden siteler için) */
        @JavascriptInterface public void disAc(String url) { runOnUiThread(() -> { Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try { i.setPackage("com.android.chrome"); startActivity(i); } catch (Exception e) { try { i.setPackage(null); startActivity(i); } catch (Exception e2) { } } }); }
        /** İndirilen dosyanın i. parçası (base64, 768 KB'lık); bitince boş döner. */
        @JavascriptInterface public String indirParca(int i) {
            try {
                if (indirDosya == null || !indirDosya.exists()) return "";
                final int P = 786432; long bas = (long) i * P; if (bas >= indirDosya.length()) return "";
                int n = (int) Math.min(P, indirDosya.length() - bas); byte[] b = new byte[n];
                try (RandomAccessFile r = new RandomAccessFile(indirDosya, "r")) { r.seek(bas); r.readFully(b); }
                return Base64.encodeToString(b, Base64.NO_WRAP);
            } catch (Exception e) { return ""; }
        }
        @JavascriptInterface public void indirTemizle() { try { if (indirDosya != null) indirDosya.delete(); } catch (Exception e) { } indirDosya = null; }
        @JavascriptInterface public void renk(String c) { runOnUiThread(() -> renkUygula(c)); }
        /** Sayfa vurguları (alıntıları) buraya yazar; Bütçe & Hedef AlintiSaglayici üzerinden okur. */
        @JavascriptInterface public void alintilar(String json) {
            try {
                File gecici = new File(getFilesDir(), AlintiSaglayici.DOSYA + ".tmp");
                try (FileOutputStream o = new FileOutputStream(gecici)) { o.write((json == null ? "[]" : json).getBytes("UTF-8")); }
                gecici.renameTo(new File(getFilesDir(), AlintiSaglayici.DOSYA));
            } catch (Exception e) { }
        }
        @JavascriptInterface public void kaydet(String ad, String tur, String b64) {
            try {
                byte[] veri = Base64.decode(b64, Base64.DEFAULT);
                if (ad == null || ad.trim().isEmpty()) ad = "okur-dosya";
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues cv = new ContentValues();
                    cv.put(MediaStore.Downloads.DISPLAY_NAME, ad);
                    cv.put(MediaStore.Downloads.MIME_TYPE, (tur == null || tur.isEmpty()) ? "application/octet-stream" : tur);
                    Uri u = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, cv);
                    try (OutputStream o = getContentResolver().openOutputStream(u)) { o.write(veri); }
                } else {
                    File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), ad);
                    try (FileOutputStream o = new FileOutputStream(f)) { o.write(veri); }
                }
                final String m = ad;
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "İndirilenler'e kaydedildi: " + m, Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Kaydedilemedi: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }
    }

    /** <a download href="blob:..."> tıklamalarını yakalayıp dosyayı Kopru.kaydet'e gönderir. */
    private static final String INDIR_JS =
        "(function(){try{var m=function(){var x=document.querySelector('meta[name=theme-color]');if(x&&window.OkurAndroid)OkurAndroid.renk(x.content)};m();" +
        "new MutationObserver(m).observe(document.head,{subtree:true,attributes:true,childList:true,attributeFilter:['content']})}catch(e){}})();" +
        "(function(){if(window.__okurIndir)return;window.__okurIndir=1;var o=HTMLAnchorElement.prototype.click;" +
        "HTMLAnchorElement.prototype.click=function(){var h=this.href||'',ad=this.getAttribute('download');" +
        "if(ad!==null&&/^(blob:|data:)/.test(h)&&window.OkurAndroid){fetch(h).then(function(r){return r.blob()}).then(function(b){" +
        "var f=new FileReader();f.onload=function(){var s=String(f.result),i=s.indexOf(',');OkurAndroid.kaydet(ad||'okur-dosya',b.type||'',s.slice(i+1))};f.readAsDataURL(b)});return}" +
        "return o.apply(this,arguments)}})();";
}
