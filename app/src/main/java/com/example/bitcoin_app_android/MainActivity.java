package com.example.bitcoin_app_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    public final static String BITCOIN = "Bitcoin";
    public final static String CURRENT_CURRENCY = "Current_currency";

    static {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .penaltyLog()
                .penaltyDeath()
                .build()
        );
    }

    private SharedPreferences preferences;
    private TextView contentTextView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.i(LOG_TAG, "BroadcastReceiver onReceive");
            final boolean success = intent.getAction().equals(BitcoinIntentService.ACTION_BITCOIN_RESULT_SUCCESS);
            onNewsResult(success);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(BITCOIN, 0);

        contentTextView = (TextView) findViewById(R.id.content_text_view);
        findViewById(R.id.currency_choice_button).setOnClickListener(onCurrencyChooseClick);
        findViewById(R.id.update_button).setOnClickListener(onUpdateClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
        String currentCurrency = preferences.getString(CURRENT_CURRENCY, null);
        String result = "Bitcoin = " + preferences.getString(currentCurrency, null) + " " + currentCurrency;
        contentTextView.setText(result);
        initBroadcastReceiver(this);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private final View.OnClickListener onCurrencyChooseClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(LOG_TAG, "onCurrencyChooseClick");
            final Intent intent = new Intent(MainActivity.this, CurrencyActivity.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener onUpdateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(LOG_TAG, "onUpdateClick");
            update();
        }
    };

    public void update() {
        Log.i(LOG_TAG, "update");
        String currencyName = preferences.getString(CURRENT_CURRENCY, null);
        ServiceHelper.getInstance().getCurrency(this, currencyName);
    }

    public void onNewsResult(boolean success) {
        Log.i(LOG_TAG, "onNewsResult");
        if (success) {
            String currentCurrency = preferences.getString(CURRENT_CURRENCY, null);
            String result = "Bitcoin = " + preferences.getString(currentCurrency, null) + " " + currentCurrency;
            contentTextView.setText(result);
            Toast.makeText(this, "Data was downloaded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Couldn't load data", Toast.LENGTH_SHORT).show();
        }
    }

    private void initBroadcastReceiver(Context context) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(BitcoinIntentService.ACTION_BITCOIN_RESULT_SUCCESS);
        filter.addAction(BitcoinIntentService.ACTION_BITCOIN_RESULT_ERROR);

        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, filter);
    }
}
