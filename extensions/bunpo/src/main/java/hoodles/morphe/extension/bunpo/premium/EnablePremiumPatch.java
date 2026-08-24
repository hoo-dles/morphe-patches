package hoodles.morphe.extension.bunpo.premium;

import com.revenuecat.purchases.EntitlementInfo;
import com.revenuecat.purchases.OwnershipType;
import com.revenuecat.purchases.PeriodType;
import com.revenuecat.purchases.Store;
import com.revenuecat.purchases.VerificationResult;

import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class EnablePremiumPatch {
    private static final long YEAR_IN_MS = 31536000000L;
    private static final Date now;
    private static final Date expiry;

    static {
        now = new Date();
        expiry = new Date(now.getTime() + YEAR_IN_MS);
    }

    public static void addEntitlement(Map<String, EntitlementInfo> all, VerificationResult verification) {
        EntitlementInfo entitlement = new EntitlementInfo("", true, true, PeriodType.NORMAL, now, now, expiry, Store.PLAY_STORE, "", "", false, null, null, OwnershipType.PURCHASED, new JSONObject(), verification);
        all.put("platinum", entitlement);
    }
}
