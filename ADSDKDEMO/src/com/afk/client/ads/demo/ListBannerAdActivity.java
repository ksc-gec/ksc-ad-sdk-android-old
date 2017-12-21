package com.afk.client.ads.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afk.client.ads.demo.utils.AbScreenUtils;
import com.afk.client.ads.demo.utils.ImageViewPlus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ks.client.ads.AdNative;
import com.ks.client.ads.entity.NativeAd;
import com.ks.client.ads.entity.NativeAdResponse;
import com.ks.client.ads.entity.NativeBaseResponse;
import com.ks.client.ads.inf.AdNativeNetworkListener;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by yf_15 on 2016/12/27.
 * <p>
 * 信息流banner广告
 */

public class ListBannerAdActivity extends AppCompatActivity implements View.OnClickListener {
    private static String YOUR_AD_PLACE_ID = Ids.NATIVEID;
    private NativeAd nativeResponse;
    private ImageViewPlus iv;
    private TextView tv_title;
    private TextView tv_content;
    private Button btn_down_load;
    private RelativeLayout list_banner;
    private Button show_banner_btn;
    private ImageView iv_close;
    private TextView tv_ad;

    int down_x;
    int down_y;
    int up_x;
    int up_y;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_banner);
        initView();
        initListener();
        fetchAd(this);
    }

    private void initListener() {
        list_banner.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        btn_down_load.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        down_x = (int) event.getX();
                        down_y = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        up_x = (int) event.getX();
                        up_y = (int) event.getY();
                        break;
                }

                return false;
            }
        });
        btn_down_load.setOnClickListener(this);
        show_banner_btn.setOnClickListener(this);
    }

    private void fetchAd(ListBannerAdActivity activity) {
        final AdNative adNative = new AdNative(activity, YOUR_AD_PLACE_ID, new AdNativeNetworkListener() {

            @Override
            public void onResponse(NativeBaseResponse nativeBaseResponse) {
                NativeAdResponse adResponse = (NativeAdResponse) nativeBaseResponse;
                List<NativeAd> nativeAds = adResponse.nativeAds;
                if (nativeAds != null && nativeAds.size() > 0) {
                    nativeResponse = nativeAds.get(0);
                    showData(nativeResponse);
                }
            }
        });
        adNative.loadNativeAd();
    }

    private void showData(NativeAd nativeResponse) {
        if (nativeResponse != null) {
            Glide.with(ListBannerAdActivity.this).load(nativeResponse.getData().logoUrl).asBitmap().listener(new RequestListener<String, Bitmap>() {
                //以下两个方法为图片是否展示，如果展示则所有控件为Visible，否则为gone
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    list_banner.setVisibility(View.GONE);
                    tv_ad.setVisibility(View.GONE);
                    tv_title.setVisibility(View.GONE);
                    tv_content.setVisibility(View.GONE);
                    iv_close.setVisibility(View.GONE);
                    btn_down_load.setVisibility(View.GONE);
                    iv.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    list_banner.setVisibility(View.VISIBLE);
                    tv_ad.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_content.setVisibility(View.VISIBLE);
                    iv_close.setVisibility(View.VISIBLE);
                    btn_down_load.setVisibility(View.VISIBLE);
                    iv.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(iv);
            tv_title.setText(nativeResponse.getData().title);
            tv_content.setText(nativeResponse.getData().description);
            btn_down_load.setText(nativeResponse.getData().mIsDownloadApp ? "立即下载" : "立即查看");
            //展示上报
//            nativeResponse.handleDisplayEvent(iv);
            nativeResponse.onShown(iv);
        }
    }

    private void initView() {

        tv_ad = (TextView) findViewById(R.id.tv_ad);
        show_banner_btn = (Button) findViewById(R.id.show_banner_btn);
        list_banner = (RelativeLayout) findViewById(R.id.list_banner);
        //根据屏幕分辨率设置控件宽高
        int realWidth = AbScreenUtils.dip2px(ListBannerAdActivity.this, 640);
        int realHeight = AbScreenUtils.dip2px(ListBannerAdActivity.this, 100);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(realWidth, realHeight);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        list_banner.setLayoutParams(params);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv = (ImageViewPlus) findViewById(R.id.iv);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_down_load = (Button) findViewById(R.id.btn_down_load);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                list_banner.setVisibility(View.GONE);
                show_banner_btn.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_down_load:
            case R.id.list_banner:
                if (nativeResponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("down_x", down_x);
                        jsonObject.put("down_y", down_y);
                        jsonObject.put("up_x", up_x);
                        jsonObject.put("up_y", up_y);
                        nativeResponse.onClicked(view, jsonObject);
                    } catch (Exception e) {
                        nativeResponse.onClicked(view, null);
                    }
                }
                break;
            case R.id.show_banner_btn:
                show_banner_btn.setVisibility(View.GONE);
                list_banner.setVisibility(View.VISIBLE);
                fetchAd(this);
                break;
        }
    }
}
