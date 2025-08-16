package com.example.mobileapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;

public class ThingSpeakReader {

    // Informacje potrzebne do odczytu liczby miejsc z ThingSpeak
    // enter your values here
    private static final String READ_API_KEY = "X";
    private static final String CHANNEL_ID = "X";
    private static final String FIELD_NUMBER = "1";

    private static final String THINGSPEAK_URL = "https://api.thingspeak.com/channels/" + CHANNEL_ID + "/fields/" + FIELD_NUMBER + ".json";

    public static String readFromThingSpeak() {
        try {

            URL url = new URL(THINGSPEAK_URL + "?api_key=" + READ_API_KEY + "&results=1");


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Error fetching data from ThingSpeak: " + responseCode);
                return null;
            }


            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject data = new JSONObject(response.toString());
            JSONArray feeds = data.getJSONArray("feeds");
            if (feeds.length() > 0) {
                JSONObject latestEntry = feeds.getJSONObject(feeds.length() - 1);
                String value = latestEntry.getString("field" + FIELD_NUMBER);
                String timestamp = latestEntry.getString("created_at");
                return value;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error fetching data from ThingSpeak: " + e);
            return null;
        }
    }
}