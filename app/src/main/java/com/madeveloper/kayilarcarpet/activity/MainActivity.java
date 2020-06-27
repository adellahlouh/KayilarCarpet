package com.madeveloper.kayilarcarpet.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.databinding.FragmentFavoriteBinding;
import com.madeveloper.kayilarcarpet.fragments.BaseFragment;
import com.madeveloper.kayilarcarpet.fragments.CartFragment;
import com.madeveloper.kayilarcarpet.fragments.DescriptionProductFragment;
import com.madeveloper.kayilarcarpet.fragments.FavoriteFragment;
import com.madeveloper.kayilarcarpet.fragments.HomeFragment;
import com.madeveloper.kayilarcarpet.fragments.ListProductFragment;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.User;
import com.madeveloper.kayilarcarpet.utils.Util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements OnNavigateFragment {

    private NavController navController;

    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabsHome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Util.setCloseIconAsBack(this);
        Util.showBarFromActivity(this,false);


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0){
                    navController.navigate(R.id.homeFragment);
                }else if (tab.getPosition() == 1){
                    navController.navigate(R.id.cartFragment);
                }else if (tab.getPosition() == 2){
                    navController.navigate(R.id.favoriteFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onNavigate(int resFragmentId, @Nullable Bundle bundle) {
        navController.navigate(resFragmentId, bundle);
    }

    @Override
    public void onFragmentShow(BaseFragment currentFragment) {

        if (currentFragment instanceof HomeFragment) {
            Util.showBarFromActivity(this,false);
            setTitle("");
            tabLayout.setVisibility(View.VISIBLE);
        }
        else if (currentFragment instanceof ListProductFragment) {
            Util.showBarFromActivity(this,true);
            setTitle(currentFragment.getTitle());
            tabLayout.setVisibility(View.GONE);
        }
        else if (currentFragment instanceof DescriptionProductFragment) {
            Util.showBarFromActivity(this,false);
            setTitle(currentFragment.getTitle());
            tabLayout.setVisibility(View.GONE);
        }
        else if (currentFragment instanceof FavoriteFragment){
            Util.showBarFromActivity(this,false);
            setTitle(currentFragment.getTitle());
            tabLayout.setVisibility(View.VISIBLE);
        }else if (currentFragment instanceof CartFragment){
            Util.showBarFromActivity(this,false);
            tabLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}