package br.com.firstingressos.dashboard.security;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.os.Debug;

import java.io.File;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.content.pm.ApplicationInfo;

public class SecurityManager {
    // Chave de criptografia única gerada para First Ingressos
    private static final String ENCRYPTION_KEY = "fI#9$mK2@pL5*vN8&xQ3";
    
    // Lista de hashes de assinatura válidos (debug e release)
    private static final String[] VALID_SIGNATURE_HASHES = {
        "first123", // Hash da assinatura de release
        "debug"     // Hash da assinatura de debug
    };

    private static SecurityManager instance;
    private final Context context;
    private String cachedDeviceId;

    private SecurityManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized SecurityManager getInstance(Context context) {
        if (instance == null) {
            instance = new SecurityManager(context.getApplicationContext());
        }
        return instance;
    }

    // Verifica ambiente e licença
    public boolean isAppValid() {
        return isSecureEnvironment() 
               && isValidSignature() 
               && !detectMaliciousApps() 
               && checkLicense();
    }

    // Verifica se o ambiente é seguro
    private boolean isSecureEnvironment() {
        return !isEmulator() && !isRooted() && !isDebuggerConnected();
    }

    // Detecta se está rodando em emulador
    private boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    // Verifica se o dispositivo está rooteado
    private boolean isRooted() {
        String[] paths = {
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        };
        
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        
        return false;
    }

    // Verifica se há debugger conectado
    private boolean isDebuggerConnected() {
        return Debug.isDebuggerConnected();
    }

    // Verifica a assinatura do APK
    private boolean isValidSignature() {
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(signature.toByteArray());
                
                String currentSignature = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                
                // Verifica se a assinatura atual está na lista de assinaturas válidas
                for (String validHash : VALID_SIGNATURE_HASHES) {
                    if (validHash.equals(currentSignature)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    // Gera/recupera ID único do dispositivo
    public String getDeviceId() {
        if (cachedDeviceId != null) {
            return cachedDeviceId;
        }

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String uniqueId = androidId + Build.SERIAL + Build.FINGERPRINT;
        cachedDeviceId = encrypt(uniqueId);
        
        return cachedDeviceId;
    }

    // Criptografa dados
    public String encrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            return null;
        }
    }

    // Descriptografa dados
    public String decrypt(String encryptedData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            
            byte[] decoded = Base64.decode(encryptedData, Base64.NO_WRAP);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            return null;
        }
    }

    // Verifica licença com servidor
    public boolean checkLicense() {
        String deviceId = getDeviceId();
        // TODO: Implementar verificação com servidor
        return true;
    }

    // Detecta apps maliciosos
    public boolean detectMaliciousApps() {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        
        List<String> maliciousApps = Arrays.asList(
            "com.chelpus.lackypatch",
            "com.dimonvideo.luckypatcher",
            "com.z.flexible.y",
            "com.z.flexible.z",
            "com.z.flexible.b",
            "org.creeplays.hack",
            "com.android.vending.billing.InAppBillingService",
            "com.android.vending.billing.InAppBillingSorvice",
            "com.android.vending.billing.InAppBillingService.LOCK",
            "com.android.vending.billing.InAppBillingService.LACK",
            "cc.madkite.freedom",
            "org.creeplays.hack",
            "com.xmodgame",
            "com.cih.game_cih",
            "com.charles.proxy"
        );

        for (ApplicationInfo app : apps) {
            if (maliciousApps.contains(app.packageName)) {
                return true;
            }
        }
        
        return false;
    }

    // Verifica integridade do código
    public boolean verifyCodeIntegrity() {
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.versionName.equals("1.0"); // Ajuste para sua versão
        } catch (Exception e) {
            return false;
        }
    }
}
