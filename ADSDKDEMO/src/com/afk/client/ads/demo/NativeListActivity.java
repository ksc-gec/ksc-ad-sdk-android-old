package com.afk.client.ads.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afk.client.ads.demo.utils.AbScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ks.client.ads.AdNative;
import com.ks.client.ads.Logger;
import com.ks.client.ads.entity.NativeAd;
import com.ks.client.ads.entity.NativeAdResponse;
import com.ks.client.ads.entity.NativeBaseResponse;
import com.ks.client.ads.inf.AdNativeNetworkListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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
 * 描 述 :信息流list广告
 *
 * 修订日期 :
 */
public class NativeListActivity extends AppCompatActivity {
    private static String YOUR_AD_PLACE_ID = Ids.NATIVEID; // 双引号中填写自己的广告位ID
    ListView listView;
    MyAdapter adapter;
    List<NativeAd> nrAdList = new ArrayList<NativeAd>();

    int down_x;
    int down_y;
    int up_x;
    int up_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_listview);
        listView = (ListView) this.findViewById(R.id.native_list_view);
        listView.setCacheColorHint(Color.WHITE);
        adapter = new MyAdapter(this);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("demo", "ListViewActivity.onItemClick");
////                NativeResponse nrAd = nrAdList.get(poion);
////                nrAd.handleClick(view);
//                NativeAd ad = nrAdList.get(position);
//                ad.onClicked(view);
//
//            }
//        })
        fetchAd(this);
    }

    public void showAdList() {
        listView.setAdapter(adapter);
    }

    public void fetchAd(Activity activity) {

        //信息流广告
        AdNative adNative = new AdNative(activity, YOUR_AD_PLACE_ID, new AdNativeNetworkListener() {

            @Override
            public void onResponse(NativeBaseResponse nativeBaseResponse) {
//                if(nativeBaseResponse.errorCode)
//                nativeBaseResponse.

                NativeAdResponse adResponse = (NativeAdResponse) nativeBaseResponse;
//                Toast.makeText(this, "get ad succeed", Toast.LENGTH_SHORT).show();
                Logger.d("onGetAdSucceed");
//                for (NativeAd nativeAd : adResponse.nativeAds)
//                    adListAdapter.addNativeAd(nativeAd);
//                adListAdapter.notifyDataSetChanged();
                nrAdList = adResponse.nativeAds;
                showAdList();

            }

//            @Override
//            public void onNativeFail(NativeErrorCode arg0) {
//                Log.w("ListViewActivity", "onNativeFail reason:" + arg0.name());
//            }
//
//            @Override
//            public void onNativeLoad(List<NativeResponse> arg0) {
//                // 一个广告只允许展现一次，多次展现、点击只会计入一次
//                if (arg0 != null && arg0.size() > 0) {
//                    //将请求到的集合赋值给成员变量，方便调用
//                    nrAdList = arg0;
//                    showAdList();
//                }
//            }

        });
        adNative.loadNativeAd();
    }


    static class ViewHolder {

        public View rootview;
        public RelativeLayout native_outer_view;
        public ImageView iv_close;
        public ImageView native_main_image;
        public RelativeLayout ll_title;
        public ImageView iv_title;
        public TextView tv_title;
        public TextView tv_content;
        public Button btn_download;
        public TextView tv_ad;
        public LinearLayout list_inset;


        public ViewHolder(View convertView) {
            rootview = convertView;
            list_inset = (LinearLayout) convertView.findViewById(R.id.list_inset);
            native_outer_view = (RelativeLayout) convertView.findViewById(R.id.native_outer_view);
            iv_close = (ImageView) convertView.findViewById(R.id.iv_close);
            native_main_image = (ImageView) convertView.findViewById(R.id.native_main_image);
            ll_title = (RelativeLayout) convertView.findViewById(R.id.ll_title);
            iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            btn_download = (Button) convertView.findViewById(R.id.btn_download);
            tv_ad = (TextView) convertView.findViewById(R.id.tv_ad);
        }

    }

    class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;


        public MyAdapter(Context context) {
            super();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return nrAdList.size();
        }

        @Override
        public NativeAd getItem(int position) {
            return nrAdList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            final NativeAd nrAd = getItem(position);
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = inflater.inflate(R.layout.native_ad_row, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            //根据给定的高宽动态设置控件宽高，以保证在任何屏幕上的适配问题 16:9
            int realWidth = AbScreenUtils.dip2px(NativeListActivity.this, 320);
            int realHeight = AbScreenUtils.dip2px(NativeListActivity.this, 180);
            LinearLayout.LayoutParams vpparams = new LinearLayout.LayoutParams(realWidth, realHeight);
            vpparams.gravity = Gravity.CENTER_HORIZONTAL;
            vpparams.topMargin = 10;
            holder.native_main_image.setLayoutParams(vpparams);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(realWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.ll_title.setLayoutParams(params);
//            holder.iv_title.setOnClickListener(this);
//            holder.tv_title.setOnClickListener(this);
//            holder.tv_content.setOnClickListener(this);
//            holder.tv_ad.setOnClickListener(this);
//            holder.native_main_image.setOnClickListener(this);
//            holder.btn_download.setOnClickListener(this);
//            holder.iv_close.setOnClickListener(this);
            holder.list_inset.setOnTouchListener(new View.OnTouchListener() {
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
            holder.list_inset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    nrAd.onClicked(v);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("down_x", down_x);
                        jsonObject.put("down_y", down_y);
                        jsonObject.put("up_x", up_x);
                        jsonObject.put("up_y", up_y);
//                    nativeResponse.handleClick(view);
                        nrAd.onClicked(view, jsonObject);
                    } catch (Exception e) {
                        nrAd.onClicked(view, null);
                    }
                }
            });


            final ViewHolder finalHolder = holder;
            Glide.with(NativeListActivity.this).load(nrAd.getData().imageUrl).asBitmap().listener(new RequestListener<String, Bitmap>() {
                //以下两个方法为图片是否展示，如果展示则所有控件为Visible，否则为gone
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    finalHolder.native_outer_view.setVisibility(View.GONE);
                    finalHolder.iv_close.setVisibility(View.GONE);
                    finalHolder.iv_title.setVisibility(View.GONE);
                    finalHolder.tv_content.setVisibility(View.GONE);
                    finalHolder.tv_title.setVisibility(View.GONE);
                    finalHolder.btn_download.setVisibility(View.GONE);
                    finalHolder.tv_ad.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    finalHolder.native_outer_view.setVisibility(View.VISIBLE);
                    finalHolder.iv_close.setVisibility(View.VISIBLE);
                    finalHolder.iv_title.setVisibility(View.VISIBLE);
                    finalHolder.tv_content.setVisibility(View.VISIBLE);
                    finalHolder.tv_title.setVisibility(View.VISIBLE);
                    finalHolder.btn_download.setVisibility(View.VISIBLE);
                    finalHolder.tv_ad.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(holder.native_main_image);
            Glide.with(NativeListActivity.this).load(nrAd.getData().logoUrl).asBitmap().into(holder.iv_title);
            holder.tv_title.setText(nrAd.getData().title);
            holder.tv_content.setText(nrAd.getData().description);
            holder.btn_download.setText(nrAd.getData().mIsDownloadApp ? "立即下载" : "立即查看");
            //上报事件
            nrAd.onShown(holder.native_main_image);
//            nrAd.handleDisplayEvent(holder.native_main_image);

            /*btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(nrAd != null){
                        nrAd.handleClick(view);
                    }
                }
            });
*/
            //关闭图标的点击事件
            holder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    nrAd.handleCloseEvent(finalHolder.iv_close);
                    nrAdList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            //手动上报
//            nrAd.handleDisplayEvent(convertView);
//            nrAd.recordImpression(convertView);
            return convertView;
        }


//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.tv_ad:
//                case R.id.tv_content:
//                case R.id.tv_title:
//                case R.id.iv_title:
//                case R.id.native_main_image:
//                case R.id.btn_download:
//                    if (nrAd != null) {
//                        //点击信息流list条目任意地方的点击事件
//                        nrAd.handleClick(view);
//                    }
//                    break;
//            }
//        }
    }


}
