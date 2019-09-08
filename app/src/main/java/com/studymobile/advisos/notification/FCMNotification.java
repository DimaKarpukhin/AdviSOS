package com.studymobile.advisos.notification;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class FCMNotification {

    // Method to send Notifications from server to client end.
    public final static String AUTH_KEY_FCM = "AIzaSyCP5Dz86IykHmPAxo7hLfnQjebez5ctMFk";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static void pushFCMNotification(final String DeviceIdKey, final String title, final String body) throws Exception {

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                BufferedReader in = null;


                String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
                String FMCurl = API_URL_FCM;

                URL url = null;
                try {
                    url = new URL(FMCurl);
                    Log.d("Good", "1");
                } catch (MalformedURLException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    Log.d("Good", "2");
                } catch (IOException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try {
                    conn.setRequestMethod("POST");
                    Log.d("Good", "3");
                } catch (ProtocolException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }
                conn.setRequestProperty("Authorization","key="+authKey);
                conn.setRequestProperty("Content-Type","application/json");

                JSONObject json = new JSONObject();
                try {
                    json.put("to",DeviceIdKey.trim());
                    Log.d("Good", "4");
                } catch (JSONException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }
                JSONObject info = new JSONObject();
                try {
                    info.put("title", title);   // Notification title
                    Log.d("Good", "5");
                } catch (JSONException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }
                try {
                    info.put("body", body); // Notification body
                    Log.d("Good", "6");
                } catch (JSONException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }
                try {
                    json.put("notification", info);
                    Log.d("Good", "7");
                } catch (JSONException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }

                OutputStreamWriter wr = null;
                try {
                    wr = new OutputStreamWriter(conn.getOutputStream());
                    Log.d("Good", "8");
                } catch (IOException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }
                try {
                    wr.write(json.toString());
                    Log.d("Good", "9");
                } catch (IOException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }
                try {
                    wr.flush();
                    Log.d("Good", "10");
                } catch (IOException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }
                try {
                    conn.getInputStream();
                    Log.d("Good", "11");
                } catch (IOException e) {
                    Log.d("Error", "############################# BAD ###############################");
                }
                Log.d("Error", "############################# VERY GOOD ###############################");
                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Log.d("Error", "############################# VERY VERY GOOD ###############################");
    }
}
