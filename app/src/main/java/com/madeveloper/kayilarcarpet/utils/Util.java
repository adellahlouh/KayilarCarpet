package com.madeveloper.kayilarcarpet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.model.User;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

public class Util {


    public static KProgressHUD getProgressDialog(Context context, String title) {

        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    public static void showBarFromActivity(@NotNull AppCompatActivity activity, boolean show) {

        if (activity.getSupportActionBar() != null)
            if (!show) activity.getSupportActionBar().hide();
            else activity.getSupportActionBar().show();
        else if (activity.getActionBar() != null)
            if (!show) activity.getActionBar().hide();
            else activity.getActionBar().show();


    }

    public static void makeActivityFullScreen(@NotNull AppCompatActivity activity, boolean showNavigationBar) {

        Window window = activity.getWindow();

        if (showNavigationBar)
            window.setFlags(FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS);

    }

    public static void setCloseIconAsBack(@NotNull AppCompatActivity activity) {

        //change icon back arrow
        assert activity.getSupportActionBar() != null;
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(activity.getResources().getDrawable(R.drawable.ic_arrow_back));

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


        return new Gson().fromJson(userGson, User.class);
    }

    public static <T> ArrayList<T> getListFromCollection(QuerySnapshot queryDocumentSnapshots, Class<T> tClass) {

        ArrayList<T> list = new ArrayList<>();

        for (DocumentSnapshot dataSnapshotChild : queryDocumentSnapshots.getDocuments()) {
            list.add(dataSnapshotChild.toObject(tClass));
        }

        return list;


    }


    public static boolean isEnglishDevice() {
        return Locale.getDefault().getLanguage().equals("en");
    }


    public static <T> Type getClassListType() {
        return new TypeToken<T>() {
        }.getType();

    }

    public synchronized static void saveArrayToPreference(Context context, String name, List<String> stringArrayList) {

        SharedPreferences sharedPreference = getAppSharedPref(context);
        SharedPreferences.Editor mEdit1 = sharedPreference.edit();

        String gsonArray = new Gson().toJson(stringArrayList);
        mEdit1.putString(name, gsonArray);

        mEdit1.apply();

    }

    public synchronized static List<String> loadArrayFromPreference(Context mContext, String name) {

        SharedPreferences sharedPreference = getAppSharedPref(mContext);

        ArrayList<String> stringArrayList = new ArrayList<>();

        String favListJson = sharedPreference.getString(name, null);

        if (favListJson != null)
            stringArrayList = new Gson().fromJson(favListJson, new TypeToken<List<String>>() {
            }.getType());

        return stringArrayList;
    }


    public static SharedPreferences getAppSharedPref(Context context) {
        return context.getSharedPreferences(Constant.APP_PREF_NAME, Context.MODE_PRIVATE);
    }

}
