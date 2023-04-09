package com.aqualink.iot.rgb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aqualink.iot.R;
import com.aqualink.iot.Utility;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RgbDeviceActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    TabLayout tabLayout;
    ViewPager viewPager;
     static Switch modeSwitch = null;
    TextView modeText;
    private ProgressBar spinner;
    public static  String host = "http://192.168.4.1";
    public static final String setModeUrl = "/saveModStngs";
    public static final String saveManStngsUrl = "/saveManStngs";
    public static final String saveAutStngsUrl = "/saveAutStngs";
    public static final String setManualRGBUrl = "/slidervalue";
    public static final String getRGBUrl = "/getRGB";
    public static final String getModeUrl = "/getMode";
    public static final String getAutoUrl = "/getAutoSet";
    public static final String showDemoUrl = "/showDemo";
    public static byte[] UdpIp;
    public static InetAddress local;
    //public static final Integer pwmMax = 255;
    public static String deviceMaxPWM,deviceName,previewTime,UdpIpAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgb_device);
        Intent intent = getIntent();
        deviceMaxPWM = intent.getStringExtra(RgbDeviceListActivity.EXTRA_DEVICE_ID);
        deviceName = intent.getStringExtra(RgbDeviceListActivity.EXTRA_DEVICE_NAME);
        previewTime = intent.getStringExtra(RgbDeviceListActivity.EXTRA_FB_HOST);
        UdpIpAddress = intent.getStringExtra(RgbDeviceListActivity.EXTRA_FB_AUTH);
        host = "http://" + UdpIpAddress;
        Integer Ip1 = Integer.parseInt(new Utility().getSplitValue(UdpIpAddress, '.',0));
        Integer Ip2 = Integer.parseInt(new Utility().getSplitValue(UdpIpAddress, '.',1));
        Integer Ip3 = Integer.parseInt(new Utility().getSplitValue(UdpIpAddress, '.',2));
        Integer Ip4 = Integer.parseInt(new Utility().getSplitValue(UdpIpAddress, '.',3));
        UdpIp =  new byte[] {Ip1.byteValue(),Ip2.byteValue(), Ip3.byteValue(), Ip4.byteValue() };
        local = null;
        try {

            local = InetAddress.getByAddress(UdpIp);
        } catch (UnknownHostException e) {
            //Log.d("saurav","RTP@ Exception is"+e);
        }
        setTitle(deviceName);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPagerContent);
        modeSwitch = (Switch) findViewById(R.id.switchMode);
        modeText = (TextView) findViewById(R.id.textMode);

        RelativeLayout layout = findViewById(R.id.viewPager);
        spinner = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        spinner.getIndeterminateDrawable().setTint(Color.parseColor("#FF0000"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(spinner, params);
        spinner.setVisibility(View.VISIBLE);
        setElementsFocusable(false);
        getModeHttpReq();
        tabLayout.addTab(tabLayout.newTab().setText("Manual"));
        tabLayout.addTab(tabLayout.newTab().setText("Auto"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final RgbDeviceAdapter adapter = new RgbDeviceAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String modeVal;
        if (b) {
            modeVal = "2";
            modeText.setText("Auto");
        } else {
            modeVal = "1";
            modeText.setText("Manual");
        }
        try {
            new Utility().voleySendHttpRequest(this, host+setModeUrl, "POST", new String[][]{{"modes",modeVal}});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isManMode(){

        return modeSwitch.isChecked();
    }
    private void setElementsFocusable(Boolean input){
        modeSwitch.setEnabled(input);

    }
    private void getModeHttpReq() {

        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, host+getModeUrl, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String mode = response.getString("mode");
                            if(mode.equals("2") ) {
                                modeSwitch.setChecked(true);
                                modeText.setText("Auto");
                            } else{
                                modeSwitch.setChecked(false);
                                modeText.setText("Manual");
                            }
                            setElementsFocusable(true);
                            spinner.setVisibility(View.GONE);
                            modeSwitch.setOnCheckedChangeListener(RgbDeviceActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);

    }
}