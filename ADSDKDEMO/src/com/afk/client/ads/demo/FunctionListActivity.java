package com.afk.client.ads.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.afk.client.ads.demo.video.MainActivity;


/**
 * Created by azkf-XT on 2016/12/21.
 */

public class FunctionListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_list);
    }

    public void videoAd(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //banner 广告
    public void transformBanerAd(View view) {
        Intent intent = new Intent(this, BannerAdActivity.class);
        startActivity(intent);
    }

    //插屏 广告
    public void transformInterstitialAd(View view) {
        Intent intent = new Intent(this, InterstitialAdtivity.class);
        startActivity(intent);
    }

    public void transformNativeListView(View view) {
        Intent intent = new Intent(this, ShowDataActivity.class);
        startActivity(intent);
    }

}
