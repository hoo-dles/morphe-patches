@import java.util.Map
@import java.util.List
@import hoodles.morphe.codegen.pairip.*

@param String appName
@param JavaCodegenData data

package hoodles.morphe.extension.${appName}.pairip;

import android.util.Log;
import java.lang.reflect.Method;

@for(String clazz : data.getStrings().keySet())
import ${UtilsKt.safelyReplaceHyphen(clazz)};
@endfor
@for(String clazz : data.getMethods().keySet())
import ${UtilsKt.safelyReplaceHyphen(clazz)};
@endfor


public class PairipHook {
    public static void inject() {
        injectStrings();
        injectMethods();
    }

    private static void injectMethods() {
        @for(Map.Entry<String, List<JavaField>> entry : data.getMethods().entrySet())
            @for(JavaField field : entry.getValue())
                ${UtilsKt.getClassFromFqcn(entry.getKey())}.${field.getName()} = getMethod("${UtilsKt.getOwningClass(field.getValue())}", "${field.getValue()}");
            @endfor
        @endfor
    }

    private static void injectStrings() {
        @for(Map.Entry<String, List<JavaField>> entry : data.getStrings().entrySet())
            @for(JavaField field : entry.getValue())
                ${UtilsKt.getClassFromFqcn(entry.getKey())}.${field.getName()} = "${UtilsKt.toLiteral(field.getValue())}";
            @endfor
        @endfor
    }

    private static Method getMethod(String className, String signature) {
        try {
            for (Method method : Class.forName(className, false, PairipHook.class.getClassLoader()).getDeclaredMethods()) {
                if (method.toGenericString().equals(signature)) {
                    return method;
                }
            }
        } catch (ClassNotFoundException e) {
            Log.e("MORPHE - PairIp", "Could not find class: " + className, e);
        }

        return null;
    }
}