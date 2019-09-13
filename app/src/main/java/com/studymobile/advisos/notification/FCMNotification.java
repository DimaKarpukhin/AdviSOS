package com.studymobile.advisos.notification;

import android.os.AsyncTask;

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
                } catch (MalformedURLException e) {}
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {}

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                try {
                    conn.setRequestMethod("POST");
                } catch (ProtocolException e) {}
                conn.setRequestProperty("Authorization","key="+authKey);
                conn.setRequestProperty("Content-Type","application/json");

                JSONObject json = new JSONObject();
                try {
                    json.put("to",DeviceIdKey.trim());
                } catch (JSONException e) { }
                JSONObject info = new JSONObject();
                try {
                    info.put("title", title);   // Notification title
                } catch (JSONException e) {}
                try {
                    info.put("body", body); // Notification body
                } catch (JSONException e) {}
                try {
                    json.put("notification", info);
                } catch (JSONException e) {}

                OutputStreamWriter wr = null;
                try {
                    wr = new OutputStreamWriter(conn.getOutputStream());
                } catch (IOException e) {}
                try {
                    wr.write(json.toString());
                } catch (IOException e) {}
                try {
                    wr.flush();
                } catch (IOException e) {}
                try {
                    conn.getInputStream();
                } catch (IOException e) {}
                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
