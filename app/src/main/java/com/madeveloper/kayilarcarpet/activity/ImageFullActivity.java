package com.madeveloper.kayilarcarpet.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jsibbold.zoomage.ZoomageView;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.utils.Util;

public class ImageFullActivity extends AppCompatActivity {


    ZoomageView zoomageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full);

        Util.showBarFromActivity(this ,false);

        Util.makeActivityFullScreen(this , false);

        String imageUrl = getIntent().getStringExtra("img");

        zoomageView = findViewById(R.id.myZoomageView);

        Glide.with(this).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                zoomageView.setImageBitmap(resource);
                findViewById(R.id.progress_circular).setVisibility(View.GONE);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                //Toast.makeText(ImageFullActivity.this,  getString(R.string.failed_download_image), Toast.LENGTH_SHORT).show();
                findViewById(R.id.progress_circular).setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}