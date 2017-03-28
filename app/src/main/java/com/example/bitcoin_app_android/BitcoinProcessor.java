package com.example.bitcoin_app_android;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.bitcoin_app_android.Storage.CURRENT_CURRENCY;

/**
 * Created by Олег on 27.03.2017.
 */

class BitcoinProcessor {
    private final static String LOG_TAG = BitcoinProcessor.class.getSimpleName();
    private final static String API_KEY = "bB1STBE07gmsher1aYwtxLImohDdp1chYfTjsnhstGRxYB9osC";
    private final static String METHOD_URL = "https://community-bitcointy.p.mashape.com/average/";

    static boolean processCurrency(final Context context, String currentCurrency) {
        Log.i(LOG_TAG, "processCurrency (Making server request)");
        Storage storage = Storage.getInstance(context);
        try {
            final String uri = Uri.parse(METHOD_URL + currentCurrency)
                    .buildUpon()
                    .build()
                    .toString();

            HttpURLConnection conn = (HttpURLConnection) new URL(uri).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("X-Mashape-Key", API_KEY);
            conn.setRequestProperty("Accept", "text/plain");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(LOG_TAG, "HTTP_OK");
                InputStream inputStream = conn.getInputStream();
                Currency result = inputStreamToCurrency(inputStream);
                storage.saveString(result.currency, result.value.toString());
            }
        }
        catch (Exception ex) {
            Log.i(LOG_TAG, ex.getMessage());
            return false;
        }
        Log.i(LOG_TAG, "processCurrency ends without errors");
        return true;
    }

    private static Currency inputStreamToCurrency(final InputStream is) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        Gson gson = new GsonBuilder().create();
        Currency result = gson.fromJson(stringBuilder.toString(), Currency.class);
        Log.i(LOG_TAG, "Валюта: " + result.currency + "\nКурс: " + result.value);
        return result;
    }
}
