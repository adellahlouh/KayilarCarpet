package com.madeveloper.kayilarcarpet.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.databinding.FragmentAccountBinding;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.User;
import com.madeveloper.kayilarcarpet.utils.Util;

public class AccountFragment extends BaseFragment {


    FragmentAccountBinding binding;
    private OnNavigateFragment onNavigateFragment;


    public AccountFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNavigateFragment = (OnNavigateFragment) getActivity();

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

        binding.historyBt.setOnClickListener(view1 -> onNavigateFragment.onNavigate(R.id.ordersHistoryFragment, null));

    }
}