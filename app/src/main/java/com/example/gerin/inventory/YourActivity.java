package com.example.gerin.inventory;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

public class YourActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Welcome to your", "Inventory", R.drawable.intro2);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("ADD", "Keep track of your growing inventory by adding items and details you care about.", R.drawable.add_intro);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("EDIT", "Always keep your inventory up to date by editing your items.", R.drawable.edit_intro);
        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("SEARCH", "Find your items easily using the search bar.", R.drawable.search_intro);
        AhoyOnboarderCard ahoyOnboarderCard5 = new AhoyOnboarderCard("SORT", "Organize you items to find them at a glance.", R.drawable.sort_intro);
        AhoyOnboarderCard ahoyOnboarderCard6 = new AhoyOnboarderCard("VIEW", "Click on an item to see a more detailed description.", R.drawable.view_intro);


        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard4.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard5.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard6.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);
        pages.add(ahoyOnboarderCard5);
        pages.add(ahoyOnboarderCard6);


        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
            //page.setDescriptionTextSize(dpToPixels(8, this));

        }

        ahoyOnboarderCard1.setTitleTextSize(dpToPixels(8, this));
        ahoyOnboarderCard1.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard1.setDescriptionColor(R.color.white);

        ahoyOnboarderCard1.setIconLayoutParams(600, 600, 1, 1, 1, 1);
        ahoyOnboarderCard2.setIconLayoutParams(1000, 800, 1, 1, 1, 1);
        ahoyOnboarderCard3.setIconLayoutParams(1000, 500, 1, 1, 1, 100);
        ahoyOnboarderCard4.setIconLayoutParams(1000, 500, 1, 1, 1, 100);
        ahoyOnboarderCard5.setIconLayoutParams(1000, 500, 1, 1, 1, 100);
        ahoyOnboarderCard6.setIconLayoutParams(600, 600, 1, 1, 1, 1);


        setFinishButtonTitle("Let's Get Started");
        showNavigationControls(true);
        setGradientBackground();

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
        }

//        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
//        setFont(face);

        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        Intent catalogIntent = new Intent(YourActivity.this, CatalogActivity.class);
        startActivity(catalogIntent);
        finish();
    }
}
