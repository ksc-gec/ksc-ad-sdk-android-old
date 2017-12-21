package com.afk.client.ads.demo;

import android.app.Application;


/**
 * Created by azkf-XT on 2016/12/23.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //测试环境
//        ADApplication.getInstance().init(this,Ids.APPID, Ids.isTestServer,true);
    }
}
