package com.madeveloper.kayilarcarpet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ViewFlipper;

import com.chaos.view.PinView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {


    TextInputEditText etPhoneLogin;
    MaterialButton btLogin;
    CountryCodePicker codePickerLogin;
    ViewFlipper viewFlipperLogin;
    String numberPhone, mVerificationId;
    View parentLayout;
    Snackbar snackbar;
    FirebaseAuth loginAuth;
    PinView pinView;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;


    KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        progressHUD = Util.getProgressDialog(this, getString(R.string.please_wait));


        parentLayout = findViewById(android.R.id.content);
        loginAuth = FirebaseAuth.getInstance();
        etPhoneLogin = findViewById(R.id.etPhoneLogin);
        btLogin = findViewById(R.id.btLogin);
        codePickerLogin = findViewById(R.id.codePickerLogin);
        viewFlipperLogin = findViewById(R.id.viewFlipperLogin);
        pinView = findViewById(R.id.pinViewLogin);

        findViewById(R.id.laySignUp).setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        codePickerLogin.setCountryForPhoneCode(+962);

        findViewById(R.id.btPrevious).setOnClickListener(v -> viewFlipperLogin.showPrevious());

        btLogin.setOnClickListener(v -> {

            String phone = etPhoneLogin.getText().toString();

            if (!phone.isEmpty() && ( phone.length() == 9 || phone.length() == 10 )) {

                if(phone.startsWith("0"))
                    phone = phone.substring(1);


                numberPhone = "+" + codePickerLogin.getFullNumber() + phone;
                etPhoneLogin.setError(null);
                checkUser();


            } else {
                etPhoneLogin.setError(getString(R.string.enter_your_phone_number));
            }
        });


        findViewById(R.id.btVerificationLogin).setOnClickListener(v -> {
            String code = Objects.requireNonNull(pinView.getText()).toString();
            if (!code.isEmpty()) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            } else {
                Snackbar.make(parentLayout, R.string.please_enter_verfi_code, Snackbar.LENGTH_LONG).show();

            }
        });

        findViewById(R.id.txResend).setOnClickListener(v -> {
            numberPhone = "+" + codePickerLogin.getFullNumber() + Objects.requireNonNull(etPhoneLogin.getText()).toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    numberPhone,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    LoginActivity.this, // Activity (for callback binding)
                    mCallbacks);
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
                progressHUD.dismiss();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                progressHUD.dismiss();

                viewFlipperLogin.showNext();
                mVerificationId = s;
                mResendToken = forceResendingToken;


            }
        };


    }

    private void checkUser() {

        progressHUD.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference loginTrackerDoc = db.collection(Constant.TRACKER_USERS_LOGIN).document(numberPhone);
        loginTrackerDoc.get().addOnSuccessListener(documentSnapshot -> {



            if (documentSnapshot.exists()) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        numberPhone,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        LoginActivity.this, // Activity (for callback binding)
                        mCallbacks);


            } else {
                snackbar = Snackbar.make(parentLayout, getString(R.string.this_number_is_not_sign_up_before_please_sign_up_first), Snackbar.LENGTH_LONG);
                snackbar.show();
                progressHUD.dismiss();
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        loginAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                pinView.setLineColor(Color.GREEN);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                progressHUD.dismiss();
                finish();
            } else {
                pinView.setLineColor(Color.RED);
                snackbar = Snackbar
                        .make(parentLayout, R.string.there_was_some_error_in_login_in, Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.dismiss), v ->
                                snackbar.dismiss());
                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                snackbar.show();

                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    snackbar = Snackbar
                            .make(parentLayout, R.string.the_verification_code_entered_was_invalid, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.dismiss), v ->
                                    snackbar.dismiss());
                    snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                    snackbar.show();

                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (viewFlipperLogin.getDisplayedChild() == 0) {
            super.onBackPressed();
        } else {
            viewFlipperLogin.showPrevious();
        }

    }
}