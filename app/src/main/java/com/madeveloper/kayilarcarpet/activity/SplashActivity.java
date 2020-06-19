package com.madeveloper.kayilarcarpet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.model.User;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth splashAuth;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        splashAuth = FirebaseAuth.getInstance();



    }

    private void getUserData() {

        FirebaseUser firebaseUser = splashAuth.getCurrentUser();

        if (firebaseUser != null) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDoc = db.collection(Constant.USERS_COLLECTION).document(Objects.requireNonNull(splashAuth.getUid()));

            userDoc.get().addOnSuccessListener(documentSnapshot -> {

                User user = documentSnapshot.toObject(User.class);
                Util.saveUser(this, user);

                startActivity(new Intent(SplashActivity.this, HomeActivity.class));

            });

        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));

            finish();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        getUserData();


    }
}