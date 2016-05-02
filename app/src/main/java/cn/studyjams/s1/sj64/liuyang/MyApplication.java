package cn.studyjams.s1.sj64.liuyang;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        ApiStoreSDK.init(this, "6304bab88de954d7e67deb39d34b30c1");
        super.onCreate();
    }
}
