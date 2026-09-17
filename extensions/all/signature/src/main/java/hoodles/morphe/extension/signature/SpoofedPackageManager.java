/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.extension.signature;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;

public class SpoofedPackageManager {
    static String PACKAGE_NAME = "";
    static String SIGNATURE = "";
    static Signature spoofedSignature = new Signature(Base64.decode(SIGNATURE, Base64.DEFAULT));

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static PackageInfo getPackageInfo(PackageManager pm, String packageName, PackageManager.PackageInfoFlags flags) throws PackageManager.NameNotFoundException {
        return spoofSignature(pm.getPackageInfo(packageName, flags));
    }

    public static PackageInfo getPackageInfo(PackageManager pm, String packageName, int flags) throws PackageManager.NameNotFoundException {
        return spoofSignature(pm.getPackageInfo(packageName, flags));
    }

    private static PackageInfo spoofSignature(PackageInfo info) {
        if (info.packageName.equals(PACKAGE_NAME)) {
            if (info.signatures != null && info.signatures.length > 0) {
                info.signatures[0] = spoofedSignature;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (info.signingInfo != null) {
                    Signature[] signaturesArray = info.signingInfo.getApkContentsSigners();
                    if (signaturesArray != null && signaturesArray.length > 0) {
                        signaturesArray[0] = spoofedSignature;
                    }
                }
            }
        }

        return info;
    }
}