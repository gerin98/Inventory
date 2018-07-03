package com.example.gerin.inventory;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logo = (ImageView) findViewById(R.id.splash_screen_logo);
        Animation fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom);
        logo.setAnimation(fromBottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent catalogIntent = new Intent(MainActivity.this, CatalogActivity.class);
                startActivity(catalogIntent);
            }
        }, SPLASH_TIME_OUT);

    }
}
