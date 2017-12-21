package com.afk.client.ads.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import java.util.Random;

/**
 * Created by yf_15 on 2016/12/27.
 *
 * 信息流开屏广告
 */

public class DisplayAdActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
    private static String YOUR_AD_PLACE_ID = Ids.NATIVEID;

    private Button btn;
    private NativeAd nativeResponse;
    private CountDownTimer timer;
    private TextView tv_time;
    private TextView tv_content;

    private ImageViewPlus main_image;
    private TextView tv_ad;
    private int[] bgs;
    private int[] btns;
    int down_x;
    int down_y;
    int up_x;
    int up_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        initData();
        initView();
        fetchAd(this);
    }

    private void initData() {
        //Activity背景的集合
        bgs = new int[]{R.drawable.ic_bg1, R.drawable.ic_bg2, R.drawable.ic_bg3, R.drawable.ic_bg4, R.drawable.ic_bg5};
        //按钮背景的集合
        btns = new int[]{R.drawable.btn1, R.drawable.btn_2, R.drawable.btn_3};
    }

    private void initView() {
        RelativeLayout display_list = (RelativeLayout) findViewById(R.id.display_list);
        //随机给Activity设置背景图片
        display_list.setBackgroundResource(bgs[new Random().nextInt(5)]);
        btn = (Button) findViewById(R.id.btn);
        btn.setBackgroundResource(btns[new Random().nextInt(3)]);
        tv_ad = (TextView) findViewById(R.id.tv_ad);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_time = (TextView) findViewById(R.id.tv_time);
        main_image = (ImageViewPlus) findViewById(R.id.main_image);
        //获取屏幕分辨率
        int widthResolution = AbScreenUtils.getScreenWidth(this);//1152屏幕分辨率
        int heightResolution = AbScreenUtils.getScreenHeight(this);//1920
        //背景图的宽高
        /*int bgWidth = AbScreenUtils.px2dip(this, 750);//250
        int bgHeight = AbScreenUtils.px2dip(this, 1334);//445*/
        //屏幕宽高
       /* int scanneWidth = AbScreenUtils.px2dip(this, widthResolution);//384
        int scanneHeight = AbScreenUtils.px2dip(this, heightResolution);//640*/
        //高宽比例
        /*float heigthScale = (float) scanneHeight / (float) bgHeight ;//0.69
        float widthScale = (float)scanneWidth / (float)bgWidth ;//0.65*/

        //根据分辨率比例
        float heigthScale = (float) heightResolution / (float) 1355;//0.69
        float widthScale = (float) widthResolution / (float) 750;

        /*int realWidth = (int) (AbScreenUtils.px2dip(this, 573) * widthScale);//191  293
        int realHeight =(int) (AbScreenUtils.px2dip(this, 323) * heigthScale);//108  155
        int topMargin = (int) (AbScreenUtils.px2dip(this, 328) * heigthScale);//156*/

        int realWidth = (int) (563 * widthScale);
        int realHeight = (int) (313 * heigthScale);
        int topMargin = (int) (308 * heigthScale);
        //Toast.makeText(this , String.valueOf(realWidth) +"---"+String.valueOf(realHeight +"---"+String.valueOf(topMargin)) , Toast.LENGTH_SHORT).show();
        //宽度293 高度155
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) main_image.getLayoutParams();
        layoutParams.width = realWidth;
        layoutParams.height = realHeight;
        layoutParams.setMargins(0, topMargin, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        main_image.setLayoutParams(layoutParams);
        btn.setOnClickListener(this);
        main_image.setOnClickListener(this);
        //倒计时，默认显示5s。时间为0是finish
        startThread();
        //开始计时
        timer.start();
    }

    private void startThread() {
        timer = new CountDownTimer(6000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
    }

    private void fetchAd(DisplayAdActivity activity) {
        AdNative adNative = new AdNative(activity, YOUR_AD_PLACE_ID, new AdNativeNetworkListener() {

//            @Override
//            public void onNativeFail(NativeErrorCode arg0) {
//                Log.w("ListViewActivity", "onNativeFail reason:" + arg0.name());
//            }
//
//            @Override
//            public void onNativeLoad(List<NativeResponse> arg0) {
//                // 一个广告只允许展现一次，多次展现、点击只会计入一次
//                if (arg0 != null && arg0.size() > 0) {
//                    nativeResponse = arg0.get(0);
//                    showData(nativeResponse);
//                }
//            }


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

    private void showData(final NativeAd nativeResponse) {
        if (nativeResponse != null) {
            Glide.with(DisplayAdActivity.this).load(nativeResponse.getData().imageUrl).asBitmap().listener(new RequestListener<String, Bitmap>() {
                //以下两个方法为图片是否展示，如果展示则所有控件为Visible，否则为gone
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    tv_content.setVisibility(View.GONE);
                    tv_ad.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    tv_content.setVisibility(View.VISIBLE);
                    tv_ad.setVisibility(View.VISIBLE);
//                    nativeResponse.handleDisplayEvent(main_image);
                    nativeResponse.onShown(main_image);
                    return false;
                }
            }).into(main_image);
            // Glide.with(DisplayAdActivity.this).load(nativeResponse.getImageUrl()).error(com.mobile.adsdk.R.drawable.ic_launcher).placeholder(com.mobile.adsdk.R.drawable.ic_launcher).into(icon_image);
            btn.setText(nativeResponse.getData().mIsDownloadApp ? "立即下载" : "立即查看");
            tv_content.setText(nativeResponse.getData().description);

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() != R.id.main_image || v.getId() != R.id.btn) {
            return false;
        }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_image:
            case R.id.btn:
                if (nativeResponse != null) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("down_x", down_x);
                        jsonObject.put("down_y", down_y);
                        jsonObject.put("up_x", up_x);
                        jsonObject.put("up_y", up_y);
//                    nativeResponse.handleClick(view);
                        nativeResponse.onClicked(view, jsonObject);
                    } catch (Exception e) {
                        nativeResponse.onClicked(view, null);
                    }
                }
                break;
        }
    }


}
