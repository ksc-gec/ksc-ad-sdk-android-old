package com.afk.client.ads.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ks.client.ads.InterstitialAd;
import com.ks.client.ads.inf.InterstitialAdListener;


/*
 *
 *
 * 版 权 :@Copyright desperado版权所有
 *
 * 作 者 :desperado
 *
 * 版 本 :1.0
 *
 * 创建日期 :2016/12/21       19:40
 *
 * 描 述 :插屏广告
 *
 * 修订日期 :
 */
public class InterstitialAdtivity extends Activity {

    String adId = Ids.INTERSTITIALID;
    final static String _NAME = "KsMSSPTester";
    final static String _VERSION = "V0.9.4";
    private LinearLayout ll_main_container;
    private InterstitialAd ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        findViewById(R.id.intershowad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ad != null)
                    if (ad.isAdReady())
                        ad.showAd();
            }
        });


        findViewById(R.id.interloadad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ad != null)
                    ad.loadAd();
            }
        });
        initView();
        requestData();

    }

    public void advclick(View view) {
        Toast.makeText(this, "点击了我", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !ad.isAdClosed()) {
            ad.hideAd();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    private void requestData() {
        ad = new InterstitialAd(this, adId, 600, 500, new InterstitialAdListener() {
            /**
             * 广告准备好时回调
             */
            @Override
            public void onAdReady() {
                Toast.makeText(InterstitialAdtivity.this, "onAdReady", Toast.LENGTH_SHORT).show();
            }

            /**
             * 广告展示时回调
             */
            @Override
            public void onAdPresent() {
                Toast.makeText(InterstitialAdtivity.this, "onAdPresent", Toast.LENGTH_SHORT).show();

            }

            /**
             * 广告点击时回调
             */
            @Override
            public void onAdClick() {
                Toast.makeText(InterstitialAdtivity.this, "onAdClick", Toast.LENGTH_SHORT).show();

            }

            /**
             * 广告关闭时回调
             */
            @Override
            public void onAdDismissed() {
                Toast.makeText(InterstitialAdtivity.this, "onAdDismissed", Toast.LENGTH_SHORT).show();

            }

            /**
             * 广告请求失败时回调
             */
            @Override
            public void onAdFailed(String var1) {
                Toast.makeText(InterstitialAdtivity.this, "onAdFailed " + var1, Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    protected void onDestroy() {
        if (ad != null) {
            ad.destroy();
        }
        super.onDestroy();
    }
}
