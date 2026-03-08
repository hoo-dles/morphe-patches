package app.morphe.extension.fotmob.plus;

import app.morphe.extension.shared.Logger;
import app.morphe.extension.shared.Utils;

import java.lang.reflect.Constructor;

@SuppressWarnings("unused")
public final class EnablePlusPatch {
    public static Object createEntitlement(String entitlementClass) {
        try {
            String className = Utils.smaliClassToJava(entitlementClass);
            Class clazz = Class.forName(className);

            // Get first, parameterized constructor
            Constructor ctor = clazz.getConstructors()[0];
            Object obj = ctor.newInstance("FotMob+", "Lifetime", false, null, null, true, false, false, false, false, false);

            return obj;
        } catch (Exception ex) {
            Logger.printException(() -> "Failed creating Entitlement", ex);
            return null;
        }
    }
}