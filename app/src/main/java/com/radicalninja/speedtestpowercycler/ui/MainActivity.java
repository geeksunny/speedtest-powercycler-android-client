package com.radicalninja.speedtestpowercycler.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.radicalninja.speedtestpowercycler.App;
import com.radicalninja.speedtestpowercycler.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UiManager.init(this);

        App.getInstance().initSocket();

        BottomNavigationView navigation = findViewById(R.id.navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        UiManager.INSTANCE.startApp();
    }



}
