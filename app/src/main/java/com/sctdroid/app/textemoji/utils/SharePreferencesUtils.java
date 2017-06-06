package com.sctdroid.app.textemoji.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by lixindong on 4/26/17.
 */

public class SharePreferencesUtils {
    private static final String SP_NAME = "options";
    private static final String KEY_OPTION_WITH_SHADOW= "withShadow";
    private static final String KEY_OPTION_TEXT_SIZE= "textSize";
    private static final String KEY_FIRST_TIME_START = "isFirstTimeStart";

    public static boolean withShadow(Context context, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_OPTION_WITH_SHADOW, defValue);
    }

    public static void applyWithShadow(Context context, boolean withShadow) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_OPTION_WITH_SHADOW, withShadow).apply();
    }

    public static int textSize(Context context, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(KEY_OPTION_TEXT_SIZE, defValue);
    }
    public static void applyTextSize(Context context, int textSize) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_OPTION_TEXT_SIZE, textSize).apply();
    }

    public static boolean isFirstTimeStart(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_FIRST_TIME_START, true);
    }

    public static void applyFirstTimeStart(Context context, boolean isFirst) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_FIRST_TIME_START, isFirst).apply();
    }

    public static void apply(Context context, @NonNull String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static void apply(Context context, @NonNull String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static int getInt(Context context, @NonNull String key) {
        return getInt(context, key, 0);
    }

    public static int getInt(Context context, @NonNull String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static boolean getBoolean(Context context, @NonNull String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, @NonNull String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }
}
