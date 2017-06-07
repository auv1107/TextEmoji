package com.sctdroid.app.textemoji;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.tendcloud.tenddata.TCAgent;

/**
 * Created by lixindong on 2017/4/26.
 */
public class TextEmojiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TCAgent.init(this);
        TCAgent.setReportUncaughtExceptions(true);

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"QBdCh4mtuIA4lmwRxxxoIKvk-gzGzoHsz","TLDojtcXsUdDBSUMgy67H9KG");
    }
}
