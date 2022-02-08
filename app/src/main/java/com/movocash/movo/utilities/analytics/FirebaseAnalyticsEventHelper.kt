package com.movocash.movo.utilities.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object FirebaseAnalyticsEventHelper {

    fun trackAnalyticEvent(mContext: Context, eventName: String, paramBundle: Bundle) {
        FirebaseAnalytics.getInstance(mContext).logEvent(eventName, paramBundle)
    }
}