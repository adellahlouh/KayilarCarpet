package com.madeveloper.kayilarcarpet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.chaos.view.PinView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.model.User;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {


    User userModel;


    TextInputEditText etName, etPhone, etDate;
    MaterialButton btSignUp, btVerification;
    MaterialRadioButton rdMale, rdFemale;
    String numberPhone, mVerificationId;
    CountryCodePicker codePicker;
    FirebaseAuth signUpAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PinView pinView;
    Calendar myCalendar;
    ViewFlipper flipperVerification;
    RelativeLayout layVerification;
    View parentLayout;
    Snackbar snackbar;
    TextView txResend;
    RadioGroup rdGroup;
    KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        myCalendar = Calendar.getInstance();

        progressHUD = Util.getProgressDialog(this, getString(R.string.upload_data));

        parentLayout = findViewById(android.R.id.content);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDate = findViewById(R.id.etDate);
        btSignUp = findViewById(R.id.btLogin);
        pinView = findViewById(R.id.pinView);
        btVerification = findViewById(R.id.btVerification);
        codePicker = findViewById(R.id.codePicker);
        txResend = findViewById(R.id.txResend);
        flipperVerification = findViewById(R.id.flipperVerification);
        signUpAuth = FirebaseAuth.getInstance();
        layVerification = findViewById(R.id.layVerification);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        rdGroup = findViewById(R.id.rdGroup);


        etDate.setInputType(InputType.TYPE_NULL);
        codePicker.setCountryForPhoneCode(+962);

        findViewById(R.id.layLogin).setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));

        findViewById(R.id.btPrevious).setOnClickListener(v -> flipperVerification.showPrevious());

        btSignUp.setOnClickListener(v -> {

            if (checkValidation()) {
                progressHUD.show();
                numberPhone = "+" + codePicker.getFullNumber() + Objects.requireNonNull(etPhone.getText()).toString();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        numberPhone,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        SignUpActivity.this, // Activity (for callback binding)
                        mCallbacks);
            }
        });


        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateCalender();
        };

        etDate.setOnClickListener(view -> new DatePickerDialog(SignUpActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressHUD.dismiss();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                flipperVerification.showNext();
                progressHUD.dismiss();
                mVerificationId = s;
                mResendToken = forceResendingToken;

            }
        };

        txResend.setOnClickListener(v -> {

            numberPhone = "+" + codePicker.getFullNumber() + Objects.requireNonNull(etPhone.getText()).toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    numberPhone,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    SignUpActivity.this, // Activity (for callback binding)
                    mCallbacks);

        });

        btVerification.setOnClickListener(v -> {
            String code = Objects.requireNonNull(pinView.getText()).toString();
            if (!code.isEmpty()) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            } else {
                Snackbar.make(parentLayout, R.string.please_enter_verfi_code, Snackbar.LENGTH_LONG).show();

            }
        });
    }

    void updateCalender() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDate.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean checkValidation() {

        if (Objects.requireNonNull(etName.getText()).toString().isEmpty()) {
            etName.setError(getString(R.string.enter_your_full_name));

        } else if (Objects.requireNonNull(etDate.getText()).toString().isEmpty()) {
            etDate.setError(getString(R.string.enter_your_age));

        } else if (Objects.requireNonNull(etPhone.getText()).toString().isEmpty()) {
            etPhone.setError(getString(R.string.enter_your_phone_number));

        } else if (rdGroup.getCheckedRadioButtonId() == -1) {
            rdMale.setError(getString(R.string.select_your_gender));

        } else {
            etName.setError(null);
            etPhone.setError(null);
            etDate.setError(null);
            rdMale.setError(null);
            return true;
        }


        return false;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        signUpAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                pinView.setLineColor(Color.GREEN);
                createUser();

            } else {
                pinView.setLineColor(Color.RED);
                snackbar = Snackbar
                        .make(parentLayout, R.string.there_was_some_error_in_login_in, Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.dismiss), v ->
                                snackbar.dismiss());
                snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                snackbar.show();
                progressHUD.dismiss();

                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    snackbar = Snackbar
                            .make(parentLayout, R.string.the_verification_code_entered_was_invalid, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.dismiss), v ->
                                    snackbar.dismiss());
                    snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
                    snackbar.show();
                    progressHUD.dismiss();

                }
            }
        });
    }

    private void createUser() {

        progressHUD.show();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null)
            return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference loginTrackerDoc = db.collection(Constant.TRACKER_USERS_LOGIN).document(numberPhone);

        loginTrackerDoc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }else {
                DocumentReference userDocument = db.collection(Constant.USERS_COLLECTION).document(firebaseUser.getUid());

                User user = new User();

                user.setUid(firebaseUser.getUid());
                user.setName(Objects.requireNonNull(etName.getText()).toString());
                user.setPhone(Objects.requireNonNull(etPhone.getText()).toString());
                user.setDate(Objects.requireNonNull(etDate.getText()).toString());

                if (rdMale.isChecked())
                    user.setGender(rdMale.getText().toString());
                else
                    user.setGender(rdFemale.getText().toString());


                userDocument.set(user).addOnSuccessListener(aVoid -> {


                    //save tracker in document empty using HashMap
                    loginTrackerDoc.set(new HashMap<>());

                    Util.saveUser(this,user);

                    progressHUD.dismiss();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();

                });

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (flipperVerification.getDisplayedChild() == 0) {
            super.onBackPressed();
        } else {
            flipperVerification.showPrevious();
        }

    }
}