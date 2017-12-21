package com.afk.client.ads.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.afk.client.ads.demo.utils.AbScreenUtils;
import com.ks.client.ads.BannerAd;
import com.ks.client.ads.inf.BannerAdListener;


/*
 *
 *
 * 版 权 :@Copyright desperado版权所有
 *
 * 作 者 :desperado
 *
 * 版 本 :1.0
 *
 * 创建日期 :2016/12/21       19:41
 *
 * 描 述 :banner广告
 *
 * 修订日期 :
 */
public class BannerAdActivity extends Activity {
    String adId = Ids.BANNERID; //测试
    final static String _NAME = "KsMSSPTester";//vtt8ro1o
    final static String _VERSION = "V0.9.4";
    private RelativeLayout ll_main_container;
    private BannerAd bannerAd;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initView();

    }

    private void initView() {

        int screenHeight = AbScreenUtils.getScreenHeight(this);
        int screenWidth = AbScreenUtils.getScreenWidth(this);
        ll_main_container = (RelativeLayout) findViewById(R.id.ll_main_container);
        BannerAd bannerAd = new BannerAd(this, adId, AbScreenUtils.getRealWidth(this, 640), AbScreenUtils.getRealHeight(this, 100), new BannerAdListener() {

            /**
             * 广告展示回调
             */
            @Override
            public void onAdPresent() {
                Log.i("ManActivity", "onAdPresent");
            }

            /**
             * 广告隐藏时回调
             */
            @Override
            public void onAdDismissed() {
                Log.i("ManActivity", "onAdDismissed");
            }

            /**
             *广告请求失败时回调
             * @param var1
             */
            @Override
            public void onAdFailed(String var1) {
                Log.i("ManActivity", "onAdFailed");
            }

            /**
             * 广告被点击时回调
             */
            @Override
            public void onAdClick() {
                Log.i("ManActivity", "onAdClick");
            }
        });
        this.bannerAd = bannerAd;
        View bannerView = this.bannerAd.getBannerView();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bannerView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ll_main_container.addView(bannerView, layoutParams);


    }


    @Override
    protected void onPause() {
        if (bannerAd != null) {
            bannerAd.pauseAd();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (bannerAd != null) {
            bannerAd.resumeAd();
        }
        super.onResume();
    }

    public void getData(View view) {
        requestData();
    }

    @Override
    protected void onDestroy() {
        if (bannerAd != null) {
            bannerAd.destroy();
        }
        super.onDestroy();
    }

    private void requestData() {
        bannerAd.showAd();
    }

}
