package com.afk.client.ads.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * Created by yf_15 on 2016/12/27.
 */

public class ShowDataActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdata);
    }

    //信息流list广告
    public void transformNativeListView(View v){
        Intent intent = new Intent(this, NativeListActivity.class);
        startActivity(intent);
    }

    //信息流插屏广告
    public void transformInterstitialAd(View v){
        Intent intent = new Intent(this , InsertListActivity.class);
        startActivity(intent);
    }

    //信息流开屏广告
    public void transformNativeDisplay(View view){
        Intent intent = new Intent(this, DisplayAdActivity.class);
        startActivity(intent);
    }

    //信息流banner广告
    public void transformNativeBanner(View v){
        Intent intent = new Intent(this, ListBannerAdActivity.class);
        startActivity(intent);
    }
}
