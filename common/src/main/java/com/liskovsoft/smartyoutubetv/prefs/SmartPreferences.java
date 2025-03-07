package com.liskovsoft.smartyoutubetv.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.liskovsoft.sharedutils.mylogger.Log;

public final class SmartPreferences extends SmartPreferencesBase {
    private static final String TAG = SmartPreferences.class.getSimpleName();
    private static final String VIDEO_FORMAT_NAME = "videoFormatName"; // e.g. '360p' or '720p'
    private static final String BOOTSTRAP_ACTIVITY_NAME = "bootstrapActivityName";
    private static final String BOOTSTRAP_CHECKBOX_CHECKED = "bootstrapCheckBoxChecked";
    private static final String BOOTSTRAP_SELECTED_LANGUAGE = "bootstrapSelectedLanguage";
    private static final String BOOTSTRAP_UPDATE_CHECK = "bootstrapUpdateCheck";
    private static final String BOOTSTRAP_END_CARDS = "bootstrapEndCards";
    private static final String PREFERRED_CODEC = "preferredCodec";
    private static final String BOOTSTRAP_OK_PAUSE = "bootstrapOKPause";
    private static final String UNPLAYABLE_VIDEO_FIX = "unplayableVideoFix";
    private static final String LOCK_LAST_LAUNCHER = "lockLastLauncher";
    private static final String BOOT_PAGE = "bootPage";
    private static final String GLOBAL_AFR_FIX_STATE = "afrFixState";
    private static final String AUTHORIZATION_HEADER = "authorization_header";
    private static final String COOKIE_HEADER = "cookie_header";
    private static final String USE_EXTERNAL_PLAYER = "use_external_player";
    private static final String FIX_ASPECT_RATIO = "fix_aspect_ratio";
    private static final String LOG_TYPE = "log_type";
    private static final String PLAYBACK_WORKING_KEY = "playback_working_key";
    private static final String ANIMATED_PREVIEWS = "animated_previews";
    public static final String MUSIC_PAGE = "music_page";
    public static final String SUBSCRIPTIONS_PAGE = "subscriptions_page";
    public static final String WATCH_LATER_PAGE = "watch_later_page";
    public static final String DEFAULT_PAGE = "default_page";
    public static final String UPDATE_CHECK_STABLE = "update_check_stable";
    public static final String UPDATE_CHECK_BETA = "update_check_beta";
    public static final String UPDATE_CHECK_DISABLED = "update_check_disabled";
    public static final String GLOBAL_AFR_FIX_STATE_ENABLED = "afr_fix_state_enabled";
    public static final String GLOBAL_AFR_FIX_STATE_DISABLED = "afr_fix_state_disabled";
    private static final String IS_APP_JUST_INSTALLED = "is_app_just_installed";
    private static final String BACK_PRESS_EXIT = "back_press_exit";
    private static final String PREVIOUS_APP_VERSION_CODE = "previous_app_version_code";
    public static final int PLAYBACK_UNKNOWN = 0;
    public static final int PLAYBACK_IS_WORKING = 1;
    public static final int PLAYBACK_NOT_WORKING = 2;
    public static final String AVC = "avc";
    public static final String VP9 = "vp9";
    public static final String NONE = "";
    private static SmartPreferences sInstance;

    public static SmartPreferences instance(Context ctx) {
        if (sInstance == null)
            sInstance = new SmartPreferences(ctx);
        return sInstance;
    }

    private SmartPreferences(Context context) {
        super(context);
    }

    public void setSelectedFormat(String fmt) {
        putString(VIDEO_FORMAT_NAME, fmt);
    }

    public String getSelectedFormat() {
        String name = getString(VIDEO_FORMAT_NAME, "Auto");
        return name;
    }

    public void setBootstrapActivityName(String name) {
        putString(BOOTSTRAP_ACTIVITY_NAME, name);
    }

    public String getBootstrapActivityName() {
        String name = getString(BOOTSTRAP_ACTIVITY_NAME, null);
        return name;
    }

    public boolean getBootstrapSaveSelection() {
        boolean isChecked = getBoolean(BOOTSTRAP_CHECKBOX_CHECKED, true);
        return isChecked;
    }

    public void setBootstrapSaveSelection(boolean isChecked) {
        putBoolean(BOOTSTRAP_CHECKBOX_CHECKED, isChecked);
    }

    public void setPreferredLanguage(String name) {
        putString(BOOTSTRAP_SELECTED_LANGUAGE, name);
    }

    public String getPreferredLanguage() {
        String name = getString(BOOTSTRAP_SELECTED_LANGUAGE, "");
        return name;
    }

    public void setPreferredCodec(String name) {
        putString(PREFERRED_CODEC, name);
    }

    public String getPreferredCodec() {
        return getString(PREFERRED_CODEC, "");
    }

    public String getBootstrapUpdateCheck() {
        return getString(BOOTSTRAP_UPDATE_CHECK, UPDATE_CHECK_STABLE);
    }

    public void setBootstrapUpdateCheck(String releaseType) {
        putString(BOOTSTRAP_UPDATE_CHECK, releaseType);
    }

    public void setEnableEndCards(boolean isChecked) {
        putBoolean(BOOTSTRAP_END_CARDS, isChecked);
    }

    public boolean getEnableEndCards() {
        return getBoolean(BOOTSTRAP_END_CARDS, true);
    }

    public void setEnableOKPause(boolean isChecked) {
        putBoolean(BOOTSTRAP_OK_PAUSE, isChecked);
    }

    public boolean getEnableOKPause() {
        return getBoolean(BOOTSTRAP_OK_PAUSE, true);
    }

    public void setUnplayableVideoFix(boolean isChecked) {
        putBoolean(UNPLAYABLE_VIDEO_FIX, isChecked);
    }

    public boolean getUnplayableVideoFix() {
        return getBoolean(UNPLAYABLE_VIDEO_FIX, false);
    }

    public void setLockLastLauncher(boolean isChecked) {
        putBoolean(LOCK_LAST_LAUNCHER, isChecked);
    }

    public boolean getLockLastLauncher() {
        return getBoolean(LOCK_LAST_LAUNCHER, false);
    }

    public void setBootPage(String name) {
        putString(BOOT_PAGE, name);
    }

    public String getBootPage() {
        return getString(BOOT_PAGE, DEFAULT_PAGE);
    }

    public void setGlobalAfrFixState(String state) {
        putString(GLOBAL_AFR_FIX_STATE, state);
    }

    public String getGlobalAfrFixState() {
        return getString(GLOBAL_AFR_FIX_STATE, GLOBAL_AFR_FIX_STATE_DISABLED);
    }

    public void setAuthorizationHeader(String header) {
        putString(AUTHORIZATION_HEADER, header);
    }

    public String getAuthorizationHeader() {
        return getString(AUTHORIZATION_HEADER, null);
    }

    public void setCookieHeader(String header) {
        putString(COOKIE_HEADER, header);
    }

    public String getCookieHeader() {
        return getString(COOKIE_HEADER, null);
    }

    public void setUseExternalPlayer(boolean isChecked) {
        putBoolean(USE_EXTERNAL_PLAYER, isChecked);
    }

    public boolean getUseExternalPlayer() {
        return getBoolean(USE_EXTERNAL_PLAYER, false);
    }

    public void setFixAspectRatio(boolean isChecked) {
        putBoolean(FIX_ASPECT_RATIO, isChecked);
    }

    public boolean getFixAspectRatio() {
        return getBoolean(FIX_ASPECT_RATIO, false);
    }

    public int getLogType() {
        return getInt(LOG_TYPE, Log.LOG_TYPE_SYSTEM);
    }

    public void setLogType(int type) {
        putInt(LOG_TYPE, type);
    }

    public void setPlaybackWorking(int state) {
        putInt(PLAYBACK_WORKING_KEY, state);
    }

    public int getPlaybackWorking() {
        return getInt(PLAYBACK_WORKING_KEY, PLAYBACK_UNKNOWN);
    }

    public boolean getEnableAnimatedPreviews() {
        return getBoolean(ANIMATED_PREVIEWS, false);
    }

    public void setEnableAnimatedPreviews(boolean enable) {
        putBoolean(ANIMATED_PREVIEWS, enable);
    }

    public boolean isAppJustInstalled() {
        boolean justInstalled = getBoolean(IS_APP_JUST_INSTALLED, true);

        if (justInstalled) {
            putBoolean(IS_APP_JUST_INSTALLED, false);
        }

        Log.d(TAG, "Is app just installed: " + justInstalled);

        return justInstalled;
    }

    public boolean getEnableBackPressExit() {
        return getBoolean(BACK_PRESS_EXIT, false);
    }

    public void setEnableBackPressExit(boolean enable) {
        putBoolean(BACK_PRESS_EXIT, enable);
    }

    public int getPreviousAppVersionCode() {
        return getInt(PREVIOUS_APP_VERSION_CODE, 0);
    }

    public void setPreviousAppVersionCode(int versionCode) {
        putInt(PREVIOUS_APP_VERSION_CODE, versionCode);
    }
}
