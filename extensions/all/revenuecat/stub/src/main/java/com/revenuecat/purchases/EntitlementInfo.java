package com.revenuecat.purchases;

import org.json.JSONObject;
import java.util.Date;
import kotlin.NotImplementedError;

public class EntitlementInfo {

    public EntitlementInfo(String identifier, boolean isActive, boolean willRenew, PeriodType periodType, Date latestPurchaseDate, Date originalPurchaseDate, Date expirationDate, Store store, String productIdentifier, String productPlanIdentifier, boolean isSandbox, Date unsubscribeDetectedAt, Date billingIssueDetectedAt, OwnershipType ownershipType, JSONObject jsonObject, VerificationResult verification) {
        throw new NotImplementedError();
    }
}