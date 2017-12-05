package com.radicalninja.speedtestpowercycler.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.radicalninja.speedtestpowercycler.App;
import com.radicalninja.speedtestpowercycler.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UiManager.init(this);

        App.getInstance().initSocket();

        bottomNavigationView = findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener);
        bottomNavigationView.setOnNavigationItemReselectedListener(navItemReselectedListener);

        UiManager.INSTANCE.startApp();
    }

    @Override
    public void onBackPressed() {
        Log.d("abc","onBackPressed-");
        final boolean wentBack = UiManager.INSTANCE.back();
        if (wentBack) {
            final Fragment currentFragment = UiManager.INSTANCE.getCurrentFragment();
            if (currentFragment instanceof NavigationFragment) {
                final int navItemId = ((NavigationFragment) currentFragment).getNavId();
                bottomNavigationView.setSelectedItemId(navItemId);
            }
        } else {
            super.onBackPressed();
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Log.d("abc","OnNavigationItemSelectedListener--");
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            UiManager.INSTANCE.toSpeedtest();
                            return true;
                        case R.id.navigation_options:
                            UiManager.INSTANCE.toOptions();
                            return true;
                        case R.id.navigation_log:
                            UiManager.INSTANCE.toLog();
                            return true;
                    }
                    return false;
                }
            };

    private final BottomNavigationView.OnNavigationItemReselectedListener navItemReselectedListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem item) {
                    Log.d("abc", "onNavigationItemReselected---");
                }
            };

}
