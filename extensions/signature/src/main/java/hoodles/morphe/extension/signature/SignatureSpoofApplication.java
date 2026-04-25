// Adapted from: https://github.com/L-JINBIN/ApkSignatureKiller/blob/master/hook/cc/binmt/signature/PmsHookApplication.java

package hoodles.morphe.extension.signature;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SignatureSpoofApplication extends Application implements InvocationHandler {
    private static final int GET_SIGNATURES = 0x00000040;

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
        if ("getPackageInfo".equals(method.getName())) {
            String pkgName = (String) args[0];
            long flags = ((Number) args[1]).longValue();
            if ((flags & GET_SIGNATURES) != 0 && appPkgName.equals(pkgName)) {
                PackageInfo info = (PackageInfo) method.invoke(base, args);
                info.signatures = new Signature[] { new Signature(this.sign) };
                return info;
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
