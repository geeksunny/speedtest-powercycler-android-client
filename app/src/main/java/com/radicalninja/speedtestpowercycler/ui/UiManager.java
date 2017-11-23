package com.radicalninja.speedtestpowercycler.ui;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.radicalninja.speedtestpowercycler.R;
import com.radicalninja.speedtestpowercycler.ui.options.OptionsFragment;
import com.radicalninja.speedtestpowercycler.ui.speedtest.SpeedtestFragment;

import java.lang.ref.WeakReference;

public enum UiManager {

    INSTANCE;

    private static final float SPLASH_FADE_SCALE = 0.15f;

    private static Handler uiHandler;

    private FragmentManager fragmentManager;
    private int contentFrameId = R.id.fragment_container;
    private WeakReference<Toolbar> toolbar;

    public static void postToUiThread(final Runnable r) {
        uiHandler.post(r);
    }

    public static void postDelayedToUiThread(final Runnable r, final long delayMillis) {
        uiHandler.postDelayed(r, delayMillis);
    }

    @UiThread
    public static void init(@NonNull final MainActivity mainActivity) {
        final UiManager manager = INSTANCE;
        uiHandler = new Handler();

        final Toolbar toolbar = (Toolbar) mainActivity.findViewById(R.id.toolbar);
        manager.toolbar = new WeakReference<>(toolbar);
        mainActivity.setSupportActionBar(toolbar);
        manager.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    final View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                Snackbar.make(view, "Find stops near me", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
    };

    public void startApp() {
         loadInitialFragment();
    }

    public boolean back() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }

    public void setTitle(final CharSequence title) {
        final Toolbar tb = toolbar.get();
        if (null != tb) {
            tb.setTitle(title);
//        } else {
//            // TODO: Log the null toolbar
        }
    }

    public void setTitle(@StringRes final int titleResId) {
        final Toolbar tb = toolbar.get();
        if (null != tb) {
            tb.setTitle(titleResId);
//        } else {
//            // TODO: Log the null toolbar
        }
    }

    private void loadFragment(final Fragment fragment) {
        loadFragment(fragment, true);
    }

    private void loadFragment(final Fragment fragment, final boolean addToBackStack) {
        // TODO: Add code that prevents reloading the currently focused fragment
        FragmentTransaction tx = fragmentManager.beginTransaction();
        if (addToBackStack) {
            tx.addToBackStack(null);
        }
        tx.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        tx.replace(contentFrameId, fragment);
        tx.commit();
    }

    private void loadInitialFragment() {
        loadFragment(SpeedtestFragment.newInstance(), false);
    }

    public void toSpeedtest() {
        loadFragment(SpeedtestFragment.newInstance(), true);
    }

    public void toOptions() {
        loadFragment(OptionsFragment.newInstance(), true);
    }

    public void toLog() {
        //loadFragment(SpeedtestFragment.newInstance(), true);
    }

}
