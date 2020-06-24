package com.madeveloper.kayilarcarpet.handler;

import android.os.Bundle;

import com.madeveloper.kayilarcarpet.fragments.BaseFragment;

import org.jetbrains.annotations.Nullable;

public interface OnNavigateFragment {

    void onNavigate(int resFragmentId,@Nullable Bundle bundle);


    void onFragmentShow(BaseFragment currentFragment);
}
