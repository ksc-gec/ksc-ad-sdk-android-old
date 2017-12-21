package com.afk.client.ads.demo.video;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.afk.client.ads.demo.Ids;
import com.afk.client.ads.demo.R;
import com.ks.client.ads.ADSDK;
import com.ks.client.ads.AdEventListener;
import com.ks.client.ads.VideoStatus;


public class MainActivity extends BaseActivity {

    private String mAppId = Ids.VIDEOAPPID;// 应用ID，测试参数， 正式请替换自己的渠道
    private String mAdSlotId = Ids.VIDEOID;// 广告位ID，测试参数，正式请替换自己的参数
    private boolean mDebug = Ids.isTestServer;

    ImageView mShowVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showAppInfo(mAppId, mAdSlotId, mDebug);

        mShowVideo = (ImageView) findViewById(R.id.demo_show_ad);
        mShowVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ADSDK.getInstance().getAdStatus(MainActivity.this) != VideoStatus.NO_AD) {
                    ADSDK.getInstance().showAdVideo(MainActivity.this);
                } else {
                    ADSDK.getInstance().load(MainActivity.this);
                }
            }
        });
        ADSDK.getInstance().setDebug(mDebug).setLogSwitch(true);
        ADSDK.getInstance().init(this, mAppId, mAdSlotId, new AdEventListener() {
            @Override
            public void onAdExist(boolean isAdExist, long code) {
                if (isAdExist) {
                    PrintLogMsg("有广告");
                } else {
                    PrintLogMsg("没有广告");
                }
            }


            @Override
            public void onVideoCached(boolean isCached) {
                if (isCached) {
                    PrintLogMsg("已缓存广告视频");
                } else {
                    PrintLogMsg("缓存广告视频失败");
                }
            }

            @Override
            public void onVideoStart() {
                PrintLogMsg("开始播放");
            }

            @Override
            public void onVideoCompletion(boolean isLookBack) {
                PrintLogMsg("播放完成");
                if (!isLookBack) {
                    // 可以发放奖励
                    PrintLogMsg("奖励已发放");
                }
            }

            @Override
            public void onVideoClose(int currentPosition) {
                PrintLogMsg("关闭广告视频，当前进度[" + currentPosition / 1000 + "]秒");
            }

            @Override
            public void onVideoError(String reason) {
                PrintLogMsg("视频播放错误，错误信息[" + reason + "]");
            }

            @Override
            public void onLandingPageClose(boolean status) {
                PrintLogMsg("落地页关闭");
                // 请在落地页关闭之后显示发放奖励提示
            }

            @Override
            public void onDownloadStart() {
                PrintLogMsg("开始下载");
            }

            @Override
            public void onNetRequestError(String error) {
                PrintLogMsg("网络请求错误，错误信息[" + error + "]");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ADSDK.getInstance().onResume(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ADSDK.getInstance().onPause(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        ADSDK.getInstance().release(MainActivity.this);
        super.onDestroy();
    }
}
