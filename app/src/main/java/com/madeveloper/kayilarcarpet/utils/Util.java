package com.madeveloper.kayilarcarpet.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.model.User;

import java.util.ArrayList;
import java.util.List;

public class Util {


    public static KProgressHUD getProgressDialog(Context context, String title) {

        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }


    public static void saveUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.save_user), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        String userGson = new Gson().toJson(user);

        editor.putString("user", userGson);
        editor.apply();

    }

    public static User getUser(Context context) {

        User user;
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.save_user), Context.MODE_PRIVATE);

        String userGson = preferences.getString("user", "");

        assert userGson != null;
        if (userGson.isEmpty())
            return null;


        return new Gson().fromJson(userGson,User.class);
    }

    public static <T> List<T> getListFromCollection(QuerySnapshot queryDocumentSnapshots, Class<T> tClass){

        List<T> list = new ArrayList<>();

        for (DocumentSnapshot dataSnapshotChild : queryDocumentSnapshots.getDocuments()) {
            list.add(dataSnapshotChild.toObject(tClass));
        }

        return list;


    }

}
