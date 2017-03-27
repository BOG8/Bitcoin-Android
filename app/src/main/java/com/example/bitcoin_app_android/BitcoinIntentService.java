package com.example.bitcoin_app_android;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Олег on 27.03.2017.
 */

public class BitcoinIntentService extends IntentService {
    private final static String LOG_TAG = BitcoinIntentService.class.getSimpleName();

    public final static String ACTION_BITCOIN = "action.BITCOIN";
    public final static String ACTION_BITCOIN_RESULT_SUCCESS = "action.BITCOIN_RESULT_SUCCESS";
    public final static String ACTION_BITCOIN_RESULT_ERROR = "action.BITCOIN_RESULT_ERROR";
    public final static String EXTRA_CURRENCY_NAME = "extra.CURRENCY_NAME";

    public BitcoinIntentService() {
        super("BitcoinIntentService");
        Log.i(LOG_TAG, "BitcoinIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_BITCOIN.equals(action)) {
                final String currentCurrency = intent.getStringExtra(EXTRA_CURRENCY_NAME);
                handleActionCurrency(currentCurrency);
            }
        }
    }

    private void handleActionCurrency(final String currentCurrency) {
        Log.i(LOG_TAG, "handleActionNews");
        try {
            final boolean success = BitcoinProcessor.processCurrency(this, currentCurrency);
            final Intent intent = new Intent(success ? ACTION_BITCOIN_RESULT_SUCCESS : ACTION_BITCOIN_RESULT_ERROR);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        catch (Exception ex) {
            Log.i(LOG_TAG, ex.getMessage());
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_BITCOIN_RESULT_ERROR));
        }
    }
}
