package com.example.bitcoin_app_android;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static com.example.bitcoin_app_android.MainActivity.BITCOIN;
import static com.example.bitcoin_app_android.MainActivity.CURRENT_CURRENCY;

public class CurrencyActivity extends AppCompatActivity {
    private final static String LOG_TAG = CurrencyActivity.class.getSimpleName();

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        preferences = getSharedPreferences(BITCOIN, 0);

        findViewById(R.id.currency_button_1).setOnClickListener(onCurrencyClick);
        findViewById(R.id.currency_button_2).setOnClickListener(onCurrencyClick);
    }

    private final View.OnClickListener onCurrencyClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(LOG_TAG, "onCurrencyClick");
            Button currencyButton = (Button) view;
            String currencyName = currencyButton.getText().toString();
            preferences.edit().putString(CURRENT_CURRENCY, currencyName).apply();
            finish();
        }
    };
}
