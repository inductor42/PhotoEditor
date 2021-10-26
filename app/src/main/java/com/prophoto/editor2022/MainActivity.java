package com.prophoto.editor2022;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    Button button;
    private AdView adView;
    private static final String AD_UNIT_ID = "ca-app-pub-3389769914405265/2078928576";
    private static final String AD_UNIT_INter = "ca-app-pub-3389769914405265/9765846900";

    private FrameLayout adContainerView;
    private InterstitialAd mInterstitialAd;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loadAD();
       // Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
      //  setSupportActionBar(myToolbar);
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.extended_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAD();
                Intent i = new Intent(getApplicationContext(),EditImageActivity.class);
                startActivity(i);
                loadAD();
            }
        });

        TextView textView = findViewById(R.id.mailtextview);
        textView = (TextView) findViewById(R.id.mailtextview);

        TextPaint paint = textView.getPaint();
        textView.setTextColor(Color.parseColor("#12c2e9"));

        float width = paint.measureText("Hello. \\nLet's start creating!");

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#12c2e9"),
                        Color.parseColor("#c471ed"),
                        Color.parseColor("#f64f59")
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);




        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,AD_UNIT_INter, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().build());

        adContainerView = findViewById(R.id.ad_view_container);

        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                showAD();
                Intent i = new Intent(getApplicationContext(),EditImageActivity.class);
                startActivity(i);
                loadAD();

                return true;
            case R.id.action_settings:

                showDialogAbout();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showDialogAbout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    public void loadAD () {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,AD_UNIT_INter, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i("TAG", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("TAG", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

    }

    public void showAD(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);

        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(AD_UNIT_ID);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }



}