package com.madeveloper.kayilarcarpet.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.activity.LoginActivity;
import com.madeveloper.kayilarcarpet.databinding.FragmentAccountBinding;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.User;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends BaseFragment {


    FragmentAccountBinding binding;

    private OnNavigateFragment onNavigateFragment;

    FirebaseAuth auth;

    public AccountFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNavigateFragment = (OnNavigateFragment) getActivity();

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onNavigateFragment.onFragmentShow(this);

        User user = Util.getUser(getContext());

        binding.nameEt.setText("    " + user.getName());
        binding.phoneEt.setText("    " + "+962" + user.getPhone());
        binding.birthDateEt.setText("    " + user.getBirthDate());
        binding.genderEt.setText("    " + user.getGender());


        if (user.getLanguage().equals(Constant.ARABIC_LANGUAGE)) {
            binding.arabicBt.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            binding.englishBt.setBackgroundColor(getContext().getResources().getColor(R.color.red));

        } else {
            binding.englishBt.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            binding.arabicBt.setBackgroundColor(getContext().getResources().getColor(R.color.red));
        }


        binding.historyBt.setOnClickListener(view1 -> onNavigateFragment.onNavigate(R.id.ordersHistoryFragment, null));

        binding.arabicBt.setOnClickListener(view1 -> languageDatabase(user.getUid(), Constant.ARABIC_LANGUAGE));

        binding.englishBt.setOnClickListener(view1 -> languageDatabase(user.getUid(), Constant.ENGLISH_LANGUAGE));


        binding.logoutBt.setOnClickListener(view1 -> {
            auth.signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });


    }

    private void languageDatabase(String uid, String language) {

        User user = Util.getUser(getContext());

        user.setLanguage(language);

        CollectionReference userRef = FirebaseFirestore.getInstance().collection(Constant.USERS_COL);

        userRef.document(uid).set(user).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Change language to : " + language, Toast.LENGTH_SHORT).show();
            Util.setLocale(getContext(), language);
            getActivity().finish();
        });

    }


}