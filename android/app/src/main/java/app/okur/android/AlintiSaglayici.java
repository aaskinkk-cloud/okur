package app.okur.android;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;

/**
 * Okur'daki vurgular (alıntılar) Bütçe & Hedef'in Alıntılar sekmesine buradan gider.
 * Yalnız Bütçe uygulaması (app.butce.android, kendi imza anahtarıyla) okuyabilir; başka hiçbir uygulama okuyamaz.
 * Adres: content://app.okur.android.alintilar  →  tek satır, "json" sütunu.
 */
public class AlintiSaglayici extends ContentProvider {
    static final String DOSYA = "alintilar.json";
    /** Bütçe APK imza sertifikasının SHA-256'sı (butce/android/okur.jks). */
    private static final String BUTCE_PAKET = "app.butce.android";
    private static final String BUTCE_IMZA = "5EA3719702938AF1C63860338F1693B97EBFFC5F60D938621AB1FD78B15E0CFD";

    @Override public boolean onCreate() { return true; }

    private boolean izinli() {
        String p = getCallingPackage();
        if (!BUTCE_PAKET.equals(p)) return false;
        try {
            PackageManager pm = getContext().getPackageManager();
            Signature[] imzalar;
            if (Build.VERSION.SDK_INT >= 28) {
                SigningInfo si = pm.getPackageInfo(p, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo;
                imzalar = si.hasMultipleSigners() ? si.getApkContentsSigners() : si.getSigningCertificateHistory();
            } else {
                imzalar = pm.getPackageInfo(p, PackageManager.GET_SIGNATURES).signatures;
            }
            for (Signature s : imzalar) {
                byte[] h = MessageDigest.getInstance("SHA-256").digest(s.toByteArray());
                StringBuilder b = new StringBuilder();
                for (byte x : h) b.append(String.format("%02X", x));
                if (BUTCE_IMZA.equals(b.toString())) return true;
            }
        } catch (Exception e) { }
        return false;
    }

    @Override public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (!izinli()) return null;
        MatrixCursor c = new MatrixCursor(new String[]{"json"});
        String json = "[]";
        try {
            File f = new File(getContext().getFilesDir(), DOSYA);
            if (f.exists()) json = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
        } catch (Exception e) { }
        c.addRow(new Object[]{json});
        return c;
    }

    @Override public String getType(Uri uri) { return "application/json"; }
    @Override public Uri insert(Uri uri, ContentValues values) { return null; }
    @Override public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }
    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { return 0; }
}
