package com.sctdroid.app.textemoji.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tendcloud.tenddata.TCAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixindong on 4/26/17.
 */

public class TCAgentUtils {
    public static void TextInput(Context context, @NonNull String inputText) {
        String label = Constants.LABEL_INPUT_SIZE_PREFIX;

        int length = inputText.length();

        if (length < 10) {
            label += length;
        } else if (length >= 10 && length < 16) {
            label += "10_to_16";
        } else {
            label += "16_no_limit";
        }

        TCAgent.onEvent(context, Constants.EVENT_INPUT_TEXT, label, getTextMap(inputText));
    }

    public static void Share(Context context, String label, @NonNull String text) {
        TCAgent.onEvent(context, Constants.EVENT_SHARE_TO_FRIEND, label, getTextMap(text));
    }
    public static void ShareGif(Context context, String label, @NonNull String text) {
        Map<String, String> map = new HashMap<>();
        map.put("tag", text);
        TCAgent.onEvent(context, Constants.EVENT_SHARE_GIF_TO_FRIEND, label, map);
    }

    private static Map getTextMap(String text) {
        int length = text.length();
        int emojiCount = EmojiUtils.emojiCount(text);
        float rate = emojiCount * 1.0f / length;

        Map<String, String> kv = new HashMap<>();
        kv.put("text", text);
        kv.put("length", "" + length);
        kv.put("emojiCount", "" + emojiCount);
        kv.put("rate", "" + rate);

        return kv;
    }

    public static void OptionClicked(Context context, String label) {
        TCAgent.onEvent(context, Constants.EVENT_OPTION_CLICK, label);
    }

    public static void UpdateTextSize(Context context, int textSize) {
        Map<String, String> kv = new HashMap<>();
        kv.put("textSize", textSize + "");
        String label = Constants.LABEL_TEXT_SIZE_SMALL;
        if (textSize >= 20 && textSize < 40) {
            label = Constants.LABEL_TEXT_SIZE_MIDDLE;
        } else if (textSize >= 40) {
            label = Constants.LABEL_TEXT_SIZE_LARGE;
        }
        TCAgent.onEvent(context, Constants.EVENT_UPDATE_TEXT_SIZE, label, kv);
    }

    public static void SwitchShadow(Context context, boolean isChecked) {
        TCAgent.onEvent(context, Constants.EVENT_SWITCH_SHADOW,
                isChecked ? Constants.LABEL_WITH_SHADOW : Constants.LABEL_WITHOUT_SHADOW);
    }

    public static void SaveToGallery(Context context, boolean withAlpha, String text) {
        TCAgent.onEvent(context,
                Constants.EVENT_SAVE_TO_GALLERY,
                withAlpha ? Constants.LABEL_WITH_ALPHA : Constants.LABEL_WITHOUT_ALPHA,
                getTextMap(text));
    }

    public static void SaveGifToGallery(Context context, String text) {
        Map<String, String> map = new HashMap<>();
        map.put("tag", text);
        TCAgent.onEvent(context,
                Constants.EVENT_SAVE_GIF_TO_GALLERY,
                Constants.LABEL_FROM_EMOJI,
                map);
    }

    public static void UpdateAvatar(Context context, boolean isSuccess) {
        TCAgent.onEvent(context,
                Constants.EVENT_UPDATE_AVATAR,
                isSuccess ? Constants.LABEL_UPDATE_AVATAR_SUCCESS : Constants.LABEL_UPDATE_AVATAR_FAILED);
    }

    public static void onPageStart(Context context, String pageName) {
        TCAgent.onPageStart(context, pageName);
    }

    public static void onPageEnd(Context context, String pageName) {
        TCAgent.onPageStart(context, pageName);
    }
}
