package com.liskovsoft.smartyoutubetv.flavors.common;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.liskovsoft.browser.Browser;
import com.liskovsoft.browser.Browser.EngineType;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.smartyoutubetv.R;
import com.liskovsoft.smartyoutubetv.flavors.exoplayer.player.ExoPlayerFragment;
import com.liskovsoft.smartyoutubetv.fragments.BrowserFragment;
import com.liskovsoft.smartyoutubetv.fragments.GenericFragment;
import com.liskovsoft.smartyoutubetv.fragments.PlayerFragment;
import com.liskovsoft.smartyoutubetv.fragments.PlayerListener;
import com.liskovsoft.smartyoutubetv.fragments.TwoFragmentManager;

public abstract class TwoFragmentsManagerActivity extends FragmentManagerActivity implements TwoFragmentManager {
    private static final String TAG = TwoFragmentsManagerActivity.class.getSimpleName();
    private BrowserFragment mBrowserFragment;
    private PlayerFragment mPlayerFragment;
    private PlayerListener mPlayerListener;
    private boolean mTransparencyDone;
    private boolean mXWalkFixDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo);
        getLoadingManager().show();

        // all fragments should be initialized on start
        // or you will get different kinds of errors
        // because this process takes some time
        Log.d(TAG, "Creating fragments...");
        initBrowserFragment();
        initPlayerFragment();
        // set active but don't move top so loading won't be overlapped
        super.setActiveFragment(mBrowserFragment, true);
    }

    protected abstract BrowserFragment getBrowserFragment();
    protected abstract PlayerFragment getPlayerFragment();

    private void initBrowserFragment() {
        mBrowserFragment = getBrowserFragment();
        mBrowserFragment.setWrapper(findViewById(R.id.browser_wrapper));
        initFragment(mBrowserFragment);
    }

    private void initPlayerFragment() {
        mPlayerFragment = getPlayerFragment();
        mPlayerFragment.setWrapper(findViewById(R.id.player_wrapper));
        initFragment(mPlayerFragment);
    }

    private void initFragment(GenericFragment fragment) {
        if (fragment.getWrapper() == null) {
            throw new IllegalStateException("Wrapper not initialized");
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(fragment.getWrapper().getId(), (Fragment) fragment);
        transaction.commit();
    }

    @Override
    protected void setActiveFragment(GenericFragment fragment, boolean pausePrevious) {
        if (mBrowserFragment == null || mPlayerFragment == null) {
            Log.d(TAG, "Can't set active fragment. Some of them is null");
            return;
        }

        // if other fragment not initialized - skip remove
        // remove other fragment if exists
        // if container is empty add this

        moveToTop(fragment);

        super.setActiveFragment(fragment, pausePrevious);
    }

    private void removeFromContainer(GenericFragment fragment) {
        if (!isInitialized(fragment)) {
            return;
        }

        ViewGroup container = findViewById(R.id.exo_container);
        View child = fragment.getWrapper();

        if (containsChild(container, child)) {
            container.removeView(child);
        }
    }

    private void moveToTopOld(GenericFragment fragment) {
        if (!isInitialized(fragment)) {
            return;
        }

        ViewGroup container = findViewById(R.id.exo_container);
        View child = fragment.getWrapper();

        // fragment not attached
        if (!containsChild(container, child)) {
            container.addView(child);
            return;
        }

        // fragment already attached, so only reorder it
        child.bringToFront();
    }

    private void moveToTop(GenericFragment fragment) {
        if (!isInitialized(fragment)) {
            Log.d(TAG, "Can't move to top. Fragment isn't initialized: " + fragment.getClass().getSimpleName());
            return;
        }

        Log.d(TAG, "Moving fragment to top: " + fragment.getClass().getSimpleName());

        View wrapper = fragment.getWrapper();

        // fragment already attached, so only reorder it
        wrapper.bringToFront();
    }

    private static boolean containsChild(ViewGroup container, View view) {
        return container.indexOfChild(view) != -1;
    }

    private boolean isInitialized(GenericFragment fragment) {
        return ((Fragment) fragment).getView() != null;
    }

    @Override
    public void openExoPlayer(final Intent intent, final boolean pausePrevious) {
        runOnUiThread(() -> {
            Log.d(TAG, "opening player for intent=" + intent);
            setActiveFragment(mPlayerFragment, pausePrevious);
            mPlayerFragment.openVideo(intent);
            xwalkFix();
        });
    }

    @Override
    public void setPlayerListener(PlayerListener listener) {
        mPlayerListener = listener;
    }

    @Override
    public void onPlayerAction(Intent action) {
        Log.d(TAG, "on receive player action: " + action);

        boolean doNotPause =
                action.getBooleanExtra(ExoPlayerFragment.BUTTON_USER_PAGE, false) ||
                action.getBooleanExtra(ExoPlayerFragment.BUTTON_SUGGESTIONS, false) ||
                action.getBooleanExtra(ExoPlayerFragment.BUTTON_FAVORITES, false);
        setActiveFragment(mBrowserFragment, !doNotPause);

        mPlayerListener.onPlayerAction(action);
    }

    @Override
    public void openBrowser(boolean pausePrevious) {
        runOnUiThread(() -> setActiveFragment(mBrowserFragment, pausePrevious));
    }

    private void initBrowserTransparency() {
        if (mTransparencyDone) {
            return;
        }

        findViewById(R.id.exo_container).setBackgroundColor(Color.BLACK);

        ViewGroup browser = (ViewGroup) mBrowserFragment.getWrapper();
        setTransparencyRecursive(browser);

        mTransparencyDone = true;
    }

    private void setTransparencyRecursive(ViewGroup container) {
        if (container == null) {
            return;
        }

        container.setBackgroundColor(Color.TRANSPARENT);
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            child.setBackgroundColor(Color.TRANSPARENT);
            // set hw acceleration off: API 11+ fix
            //child.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            if (child instanceof ViewGroup) {
                setTransparencyRecursive((ViewGroup) child);
            }
        }
    }

    @Override
    public void onBrowserLoaded() {
        Log.d(TAG, "Begin init WebUI");
        initBrowserTransparency();
    }

    /**
     * Fix white screen on the first video (XWalk only)
     */
    private void xwalkFix() {
        if (Browser.getEngineType() == EngineType.XWalk && !mXWalkFixDone) {
            Intent intent = new Intent();
            intent.putExtra("null", true);
            onPlayerAction(intent);
            mXWalkFixDone = true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mBrowserFragment != null) {
            mBrowserFragment.onResumeFragment();
            mBrowserFragment.onNewIntent(intent);
        }
    }
}
