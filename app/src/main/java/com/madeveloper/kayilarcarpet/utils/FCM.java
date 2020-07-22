package com.madeveloper.kayilarcarpet.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCM {

    private static String key = "AAAADVhJNh8:APA91bFqPEr2vZf_Wsc2-PcEjg_c_Jk-gzn0-T4PxIeTLyXMhoqR44ERh1b_EweA4alXlqvTTC6_0_SEMH-Os0Zcnjaan_g24BLXrwS8DXM9ljVFFlk3_KgrXRjxsdmovU6_GmfN5j4w";

    //Notification Action
    public static String OPEN_URL_ACTION = "URL_ACTION";
    /**
     * must added to data(JSON Object) to handle notification in flutter when click on it
     **/
    public static String CLICK_FLUTTER_ACTION = "FLUTTER_NOTIFICATION_CLICK";


    public static void subscribeToFcmTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    public static void sendNotificationToTopic(Context context, @NonNull JSONObject notificationObj, @Nullable JSONObject dataObj, @NonNull String topic) {

        if (dataObj == null)
            dataObj = new JSONObject();

        RequestQueue mRequestQue = Volley.newRequestQueue(context);


        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + topic);
            //replace notification with data when went send data
            json.put("notification", notificationObj);
            json.put("data", dataObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    response -> Log.d("MUR", "onResponse: " + response.toString()),
                    error -> Log.d("MUR", "onError: " + error.networkResponse)
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + key);
                    return header;
                }
            };


            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static JSONObject createNotificationObject(String title, String body) {

        JSONObject notificationObj = new JSONObject();
        try {
            notificationObj.put("title", title);
            notificationObj.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notificationObj;
    }

}
