package com.radicalninja.speedtestpowercycler.ui;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.radicalninja.speedtestpowercycler.R;
import com.radicalninja.speedtestpowercycler.ui.options.OptionsFragment;
import com.radicalninja.speedtestpowercycler.ui.speedtest.SpeedtestFragment;

import java.lang.ref.WeakReference;

public enum UiManager {

    INSTANCE;

    private final int contentFrameId = R.id.fragment_container;

    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private FragmentManager fragmentManager;
    private WeakReference<Toolbar> toolbar;

    public static void postToUiThread(final Runnable r) {
        INSTANCE.uiHandler.post(r);
    }

    public static void postDelayedToUiThread(final Runnable r, final long delayMillis) {
        INSTANCE.uiHandler.postDelayed(r, delayMillis);
    }

    @UiThread
    public static void init(@NonNull final MainActivity mainActivity) {
        final UiManager manager = INSTANCE;

        final Toolbar toolbar = mainActivity.findViewById(R.id.toolbar);
        manager.toolbar = new WeakReference<>(toolbar);
        mainActivity.setSupportActionBar(toolbar);
        manager.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void startApp() {
         loadInitialFragment();
    }

    public boolean back() {
        Log.d("abc", "Backstack count: " + fragmentManager.getBackStackEntryCount());
        return fragmentManager.getBackStackEntryCount() > 1 && fragmentManager.popBackStackImmediate();
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

    public Fragment getCurrentFragment() {
        return fragmentManager.findFragmentById(contentFrameId);
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
        loadFragment(SpeedtestFragment.newInstance(), true);
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
