// Adapted from: https://github.com/L-JINBIN/ApkSignatureKiller/blob/master/hook/cc/binmt/signature/PmsHookApplication.java

package hoodles.morphe.extension.signature;

import android.app.Application;
import android.content.Context;
import android.content.pm.InstallSourceInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.os.Parcel;
import android.util.Base64;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SignatureSpoofApplication extends Application implements InvocationHandler {
    private static final int GET_SIGNATURES = 0x00000040;
    private static final int GET_SIGNING_CERTIFICATES = 0x08000000;
    private static final int PACKAGE_SOURCE_STORE = 0x00000002;
    private static final String PACKAGE_SOURCE_NAME = "com.android.vending";
    private static InstallSourceInfo SPOOFED_INSTALL_SOURCE_INFO = null;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Parcel parcel = Parcel.obtain();
            parcel.writeString(PACKAGE_SOURCE_NAME);            // initiatingPackageName
            parcel.writeParcelable(null, 0);    // no SigningInfo
            parcel.writeString(null);                       // originatingPackageName
            parcel.writeString(PACKAGE_SOURCE_NAME);            // installingPackageName

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                parcel.writeString(null);                   // UpdateOwnerPackageName (API 34+)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                parcel.writeInt(PACKAGE_SOURCE_STORE);          // packageSource (API 33+)

            parcel.setDataPosition(0);
            SPOOFED_INSTALL_SOURCE_INFO = InstallSourceInfo.CREATOR.createFromParcel(parcel);
            parcel.recycle();
        }
    }

    private Object base;
    private byte[] sign;
    private String appPkgName = "";

    private String getSignature() {
        return "SIGNATURE_SPOOF_PLACEHOLDER";
    }

    @Override
    protected void attachBaseContext(Context base) {
        hook(base);
        super.attachBaseContext(base);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        switch (methodName) {
            case "getPackageInfo" -> {
                String pkgName = (String) args[0];
                long flags = ((Number) args[1]).longValue();
                if (appPkgName.equals(pkgName)) {
                    if ((flags & GET_SIGNATURES) != 0) {
                        PackageInfo info = (PackageInfo) method.invoke(base, args);
                        info.signatures = new Signature[]{new Signature(this.sign)};
                        return info;
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && (flags & GET_SIGNING_CERTIFICATES) != 0) {
                        PackageInfo pkgInfo = (PackageInfo) method.invoke(base, args);
                        SigningInfo signInfo = pkgInfo.signingInfo;
                        Signature[] signatures = signInfo.getApkContentsSigners();
                        if (signatures != null && signatures.length > 0)
                            signatures[0] = new Signature(this.sign);
                        return pkgInfo;
                    }
                }
            }
            case "getInstallerPackageName" -> {
                String pkgName = (String) args[0];
                if (appPkgName.equals(pkgName)) {
                    return PACKAGE_SOURCE_NAME;
                }
            }
            case "getInstallSourceInfo" -> {
                String pkgName = (String) args[0];
                if (appPkgName.equals(pkgName) && SPOOFED_INSTALL_SOURCE_INFO != null) {
                    return SPOOFED_INSTALL_SOURCE_INFO;
                }
            }
        }

        return method.invoke(base, args);
    }

    private void hook(Context context) {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod =
                    activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);

            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            this.base = sPackageManager;
            this.sign = Base64.decode(getSignature(), Base64.DEFAULT);
            this.appPkgName = context.getPackageName();

            Object proxy = Proxy.newProxyInstance(
                    iPackageManagerInterface.getClassLoader(),
                    new Class<?>[]{iPackageManagerInterface},
                    this);

            sPackageManagerField.set(currentActivityThread, proxy);

            PackageManager pm = context.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);

            Log.d("MORPHE", "Application successfully hooked for signature spoofing");
        } catch (Exception e) {
            Log.e("MORPHE", "Failed hooking application for signature spoofing", e);
        }
    }

}
