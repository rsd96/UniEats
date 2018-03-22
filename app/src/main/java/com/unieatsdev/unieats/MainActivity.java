package com.unieatsdev.unieats;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {



    TabLayout tabs;
    ViewPager viewPager;
    Toolbar toolbarMain;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize ads
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        findViews();
        setSupportActionBar(toolbarMain);
        setupViewPager();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }


    // Setup Tabs with fragments
    public void setupViewPager() {
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(new FragmentRestList(), "Restaurants");
        tabsAdapter.addFragment(new FragmentPromo(), "Promotions");
        viewPager.setAdapter(tabsAdapter);
        viewPager.setCurrentItem(0);
        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
//        tabs.getTabAt(0).setIcon(R.drawable.tab_icon_rest);
//        tabs.getTabAt(1).setIcon(R.drawable.tab_icon_promo);
    }

    public void findViews() {
        toolbarMain = findViewById(R.id.toolbarMain);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                startActivity(new Intent(this, MapsActivityView.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}

