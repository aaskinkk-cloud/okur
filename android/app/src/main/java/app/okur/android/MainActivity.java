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
        if (web != null && web.canGoBack()) web.goBack(); else super.onBackPressed();
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
