package com.afk.client.ads.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afk.client.ads.demo.utils.AbScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ks.client.ads.AdNative;
import com.ks.client.ads.entity.NativeAd;
import com.ks.client.ads.entity.NativeAdResponse;
import com.ks.client.ads.entity.NativeBaseResponse;
import com.ks.client.ads.inf.AdNativeNetworkListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yf_15 on 2016/12/27.
 *
 * 信息流插屏广告
 */

public class InsertListActivity extends AppCompatActivity implements View.OnClickListener {
    private static String YOUR_AD_PLACE_ID = Ids.NATIVEID;
    private ViewPager vp;
    private LinearLayout ll_points;
    private List<ImageView> imgs;
    private int preRedPointIndex;
    private Button btn_cancel;
    private Button btn_download;
    private ImageView iv_title;
    private TextView tv_content;
    private List<NativeAd> list;
    private TextView tv_ad;
    private MyAdapter adapter;
    private int currentPosition = 0;
    int down_x;
    int down_y;
    int up_x;
    int up_y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_list);
        initView();
        initListener();
        fetchAd(this);

    }

    private void initListener() {
        btn_cancel.setOnClickListener(this);
    }

    private void fetchAd(InsertListActivity activity) {
        AdNative adNative = new AdNative(activity, YOUR_AD_PLACE_ID, new AdNativeNetworkListener() {

            @Override
            public void onResponse(NativeBaseResponse nativeBaseResponse) {

                {
                    NativeAdResponse adResponse = (NativeAdResponse) nativeBaseResponse;

                    list = adResponse.nativeAds;
//                    list = arg0;//将参数赋值给全局的list，方便调用
                    imgs = new ArrayList<ImageView>();
                    // 一个广告只允许展现一次，多次展现、点击只会计入一次
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            //根据集合长度动态添加ImageView
                            ImageView imageView = new ImageView(InsertListActivity.this);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                            NativeResponse nativeResponse = list.get(i);
                            NativeAd nativeResponse = list.get(i);
                            if (nativeResponse != null) {
                                Glide.with(InsertListActivity.this).load(nativeResponse.getData().imageUrl).asBitmap().listener(new RequestListener<String, Bitmap>() {
                                    //以下两个方法为图片是否展示，如果展示则所有控件为Visible，否则为gone
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        btn_download.setVisibility(View.GONE);
                                        btn_cancel.setVisibility(View.GONE);
                                        tv_ad.setVisibility(View.GONE);
                                        iv_title.setVisibility(View.GONE);
                                        tv_content.setVisibility(View.GONE);
                                        ll_points.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        btn_download.setVisibility(View.VISIBLE);
                                        btn_cancel.setVisibility(View.VISIBLE);
                                        tv_ad.setVisibility(View.VISIBLE);
                                        iv_title.setVisibility(View.VISIBLE);
                                        tv_content.setVisibility(View.VISIBLE);
                                        ll_points.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                }).into(imageView);
                                //nativeResponse.handleDisplayEvent(imageView);
                                if (i == 0) {
                                    //ViewPager第一页不会调用onPageSelected()方法
//                                positionList.add(i);
                                    //如果是集合中第一个元素，给图标和标题赋值，否则在ViewPager滑动过程中赋值

                                    nativeResponse.onShown(imageView);
//                                    nativeResponse.handleDisplayEvent(imageView);
                                    tv_content.setText(nativeResponse.getData().description);
                                    btn_download.setText(nativeResponse.getData().mIsDownloadApp ? "立即下载" : "立即查看");
                                    Glide.with(InsertListActivity.this).load(nativeResponse.getData().logoUrl).into(iv_title);
                                }


                                btn_download.setOnTouchListener(new View.OnTouchListener() {
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

                                btn_download.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (list != null && !list.isEmpty()) {
//
                                            NativeAd response = list.get(currentPosition);
                                            if (response != null) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject();
                                                    jsonObject.put("down_x", down_x);
                                                    jsonObject.put("down_y", down_y);
                                                    jsonObject.put("up_x", up_x);
                                                    jsonObject.put("up_y", up_y);
                                                    list.get(currentPosition).onClicked(view, jsonObject);
                                                } catch (Exception e) {
                                                    list.get(currentPosition).onClicked(view, null);
                                                }
                                            }
                                        }
                                    }
                                });
                                imgs.add(imageView);
                                if (list.size() > 1) {
                                    //根据图片的数量创建点
                                    ImageView point = new ImageView(InsertListActivity.this);
                                    point.setBackgroundResource(R.drawable.point_normal);
                                    //给点设置宽高
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
                                    params.leftMargin = 10;
                                    point.setLayoutParams(params);
                                    //把点添加到线性容器中
                                    ll_points.addView(point);
                                }
                            }
                        }//for循环
                        adapter = new MyAdapter();
                        vp.setAdapter(adapter);
                        vp.setOnPageChangeListener(new MyOnPagechangeListener());
                        //初始化圆点指示器
                        if (ll_points != null && ll_points.getChildAt(0) != null)
                            ll_points.getChildAt(0).setBackgroundResource(R.drawable.point_red);
                    }
                }
            }

        });
        adNative.loadNativeAd();
    }

    private void initView() {
        tv_ad = (TextView) findViewById(R.id.tv_ad);
        RelativeLayout ll_title = (RelativeLayout) findViewById(R.id.ll_title);
        LinearLayout btn_ll = (LinearLayout) findViewById(R.id.btn_ll);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        vp = (ViewPager) findViewById(R.id.vp);
        //根据给定的高宽动态设置控件宽高，以保证在任何屏幕上的适配问题 16:9
        int realWidth = AbScreenUtils.getRealWidth(this, 480);
        int realHeight = AbScreenUtils.getRealHeight(this, 270);
        LinearLayout.LayoutParams vpparams = new LinearLayout.LayoutParams(realWidth, realHeight);
        vpparams.gravity = Gravity.CENTER_HORIZONTAL;
        vp.setLayoutParams(vpparams);
        //vp_relative.setGravity(Gravity.CENTER_HORIZONTAL);
        //动态设置布局中图标、标题、按钮的宽高位置，达到跟ViewPager宽一致且居中的效果
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(realWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_ll.setLayoutParams(params);
        btn_ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ll_title.setLayoutParams(params);
        ll_points = (LinearLayout) findViewById(R.id.Ll_points);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_download = (Button) findViewById(R.id.btn_download);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel://不感兴趣按钮的点击事件
                finish();
                break;
        }
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imgs.get(position));//删除页卡
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
            container.addView(imgs.get(position), 0);//添加页卡
            return imgs.get(position);
        }
    }

    class MyOnPagechangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //在ViewPager滑动过程中，获取list集合NativeResponse对象，并动态给控件赋值
//            NativeResponse response = list.get(position);
            NativeAd response = list.get(position);
            tv_content.setText(response.getData().description);
            Glide.with(InsertListActivity.this).load(response.getData().imageUrl).into(iv_title);
//            if (!positionList.contains(position)) {//如果不存在则上报
            //手动上报
            if (response != null) {
//                response.handleDisplayEvent(imgs.get(position));
                response.onShown(imgs.get(position));
            }
//                positionList.add(position);
//            }
            btn_download.setText(response.getData().mIsDownloadApp ? "立即下载" : "立即查看");
            if (imgs.size() > 1) {
                ll_points.getChildAt(position).setBackgroundResource(R.drawable.point_red);
                ll_points.getChildAt(preRedPointIndex).setBackgroundResource(R.drawable.point_normal);
            }
            currentPosition = position;
            preRedPointIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
