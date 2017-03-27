package com.example.bitcoin_app_android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Олег on 27.03.2017.
 */

class ServiceHelper {
    private final static String LOG_TAG = ServiceHelper.class.getSimpleName();
    private static ServiceHelper instance;

    private ServiceHelper() {

    }

    synchronized static ServiceHelper getInstance() {
        if (instance == null) {
            instance = new ServiceHelper();
        }
        return instance;
    }

    void getCurrency(final Context context, final String currentCurrency) {
        Log.i(LOG_TAG, "getCurrency");
        Intent intent = new Intent(context, BitcoinIntentService.class);
        intent.setAction(BitcoinIntentService.ACTION_BITCOIN);
        intent.putExtra(BitcoinIntentService.EXTRA_CURRENCY_NAME, currentCurrency);
        context.startService(intent);
    }
}
