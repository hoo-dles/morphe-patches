package hoodles.morphe.extension.macrofactor.premium;

import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;

import app.morphe.extension.shared.Logger;
import hoodles.morphe.extension.shared.Utils;

@SuppressWarnings("unused")
public class EnablePremiumPatch {
    private static final String oneYearFuture;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 1);
        oneYearFuture = Utils.toIso8601(calendar.getTime());
    }

    private static String pastDateFromId(String id) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, 2025);
        calendar.set(Calendar.DAY_OF_YEAR, (id.hashCode() % 365) + 1);

        return Utils.toIso8601(calendar.getTime());
    }

    public static void updateCustomerInfo(JSONObject json) {
        try {
            JSONObject subscriber = json.getJSONObject("subscriber");
            String pastDate = pastDateFromId(subscriber.getString("original_app_user_id"));
            subscriber.put("first_seen", pastDate);
            subscriber.put("original_purchase_date", pastDate);

            JSONObject subscriptions = new JSONObject();
            subscriber.put("subscriptions", subscriptions);

            JSONObject subscription = new JSONObject();
            subscriptions.put("com.sbs.diet.1y0599.2w0", subscription);

            subscription.put("original_purchase_date", pastDate);
            subscription.put("expires_date", oneYearFuture);
            subscription.put("is_sandbox", false);
            subscription.put("refunded_at", JSONObject.NULL);
            subscription.put("unsubscribe_detected_at", JSONObject.NULL);
            subscription.put("grace_period_expires_date", JSONObject.NULL);
            subscription.put("period_type", "normal");
            subscription.put("purchase_date", pastDate);
            subscription.put("billing_issues_detected_at", JSONObject.NULL);
            subscription.put("ownership_type", "PURCHASED");
            subscription.put("store", "app_store");
            subscription.put("auto_resume_date", JSONObject.NULL);

            JSONObject workoutsSubscription = new JSONObject(subscription.toString());
            subscriptions.put("com.sbs.train.subscription.1", workoutsSubscription);

            JSONObject entitlements = new JSONObject();
            subscriber.put("entitlements", entitlements);

            JSONObject entitlement = new JSONObject();
            entitlements.put("subscription", entitlement);

            entitlement.put("grace_period_expires_date", JSONObject.NULL);
            entitlement.put("purchase_date", pastDate);
            entitlement.put("product_identifier", "com.sbs.diet.1y0599.2w0");
            entitlement.put("expires_date", oneYearFuture);

            JSONObject workoutsEntitlement = new JSONObject(subscription.toString());
            workoutsEntitlement.put("product_identifier", "com.sbs.train.subscription.1");
            subscriptions.put("subscription_workouts", workoutsEntitlement);
        }
        catch (Exception ex) {
            Logger.printException(() -> "Error modifying customer JSON", ex);
        }
    }
}
