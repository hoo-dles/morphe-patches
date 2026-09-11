/**
 * Code adapted from the original implementation:
 * https://github.com/L-JINBIN/ApkSignatureKillerEx
 *
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.extension.signature;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.lang.reflect.Field;
import java.util.Map;

import app.morphe.extension.shared.Logger;

public class SignatureSpoofApplication extends Application {
    static {
        String packageName = "PACKAGE_NAME_PLACEHOLDER";
        String signature = "SIGNATURE_PLACEHOLDER";

        killPM(packageName, signature);
    }

    private static void killPM(String packageName, String signature) {
        Signature fakeSignature = new Signature(Base64.decode(signature, Base64.DEFAULT));
        Parcelable.Creator<PackageInfo> creator = getPackageInfoCreator(packageName, fakeSignature);

        try {
            findField(PackageInfo.class, "CREATOR").set(null, creator);
        } catch (Exception e) {
            Logger.printException(() -> "Signature Spoof: Failed to replace PackageInfo.CREATOR");
            throw new RuntimeException(e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions(
                    "Landroid/os/Parcel;",
                    "Landroid/content/pm",
                    "Landroid/app"
            );
        }

        try {
            Object cache = findField(PackageManager.class, "sPackageInfoCache").get(null);
            cache.getClass().getMethod("clear").invoke(cache);
        } catch (Exception e) {
            Logger.printDebug(() -> "Signature Spoof: Failed to clear PackageManager's PackageInfo cache", e);
        }

        try {
            Map<?, ?> mCreators = (Map<?, ?>) findField(Parcel.class, "mCreators").get(null);
            mCreators.clear();
        } catch (Exception e) {
            Logger.printDebug(() -> "Signature Spoof: Failed to clear Parcel's Creator cache", e);
        }

        try {
            Map<?, ?> sPairedCreators = (Map<?, ?>) findField(Parcel.class, "sPairedCreators").get(null);
            sPairedCreators.clear();
        } catch (Exception e) {
            Logger.printDebug(() -> "Signature Spoof: Failed to clear Parcel's paired Creator cache", e);
        }
    }

    private static Parcelable.Creator<PackageInfo> getPackageInfoCreator(String packageName, Signature fakeSignature) {
        Parcelable.Creator<PackageInfo> originalCreator = PackageInfo.CREATOR;

        return new Parcelable.Creator<>() {
            @Override
            public PackageInfo createFromParcel(Parcel source) {
                PackageInfo packageInfo = originalCreator.createFromParcel(source);
                if (packageInfo.packageName.equals(packageName)) {
                    if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                        packageInfo.signatures[0] = fakeSignature;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (packageInfo.signingInfo != null) {
                            Signature[] signaturesArray = packageInfo.signingInfo.getApkContentsSigners();
                            if (signaturesArray != null && signaturesArray.length > 0) {
                                signaturesArray[0] = fakeSignature;
                            }
                        }
                    }
                }
                return packageInfo;
            }

            @Override
            public PackageInfo[] newArray(int size) {
                return originalCreator.newArray(size);
            }
        };
    }

    private static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            while (true) {
                clazz = clazz.getSuperclass();
                if (clazz == null || clazz.equals(Object.class)) {
                    break;
                }
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field;
                } catch (NoSuchFieldException ignored) {
                }
            }
            throw e;
        }
    }
}
