package com.afk.client.ads.demo.video;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.WindowManager;
import android.widget.TextView;

import com.afk.client.ads.demo.R;


/**
 * Created by Alamusi on 2016/11/16.
 */

public class BaseActivity extends Activity {

    private TextView mLogMsg;
    private TextView mAppIdTextView;
    private TextView mAdSlotIdTextView;
    private TextView mImeiTextView;
    private TextView mVersionTextView;
    private TextView mModeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video);
        mLogMsg = (TextView) findViewById(R.id.demo_log_msg);
        mLogMsg.setMovementMethod(new ScrollingMovementMethod());
        mAppIdTextView = (TextView) findViewById(R.id.demo_info_app_id);
        mAdSlotIdTextView = (TextView) findViewById(R.id.demo_info_ad_slot_id);
        mImeiTextView = (TextView) findViewById(R.id.demo_info_imei);
        mVersionTextView = (TextView) findViewById(R.id.demo_info_version);
        mModeTextView = (TextView) findViewById(R.id.demo_info_mode);
    }

    protected void PrintLogMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogMsg.setText(mLogMsg.getText() + "\n" + msg);
            }
        });
    }

    protected void showAppInfo(String appId, String adSlotId, boolean mode) {
        mAppIdTextView.setText(appId);
        mAdSlotIdTextView.setText(adSlotId);
        if (mode) {
            mModeTextView.setText(R.string.demo_mode_debug);
        } else {
            mModeTextView.setText(R.string.demo_mode_release);
        }
//        mImeiTextView.setText(DeviceUtils.getImei(this));
//        mVersionTextView.setText(String.valueOf(AppUtils.getVersionCode(this)));
    }
}
