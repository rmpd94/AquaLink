package com.aqualink.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aqualink.iot.AP.ApActivity;
import com.aqualink.iot.AP.ApDeviceListActivity;
import com.aqualink.iot.Station.StationDeviceListActivity;
import com.aqualink.iot.rgb.RgbDeviceListActivity;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String SHARED_PREFS_DEFAULT_MODE = "sharedPrefsDefaultMode";
    private static final String default_val = "default_mode";
    private String default_mode_pref;
    public static final String ApModeName = "OffLine (AP)";
    public static final String StationModeName = "OnLine (Station)";
    public static final String RGBModeName = "RGB Controller";
    public static final String NoModeName = "None";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loadDataFromPref();
        if (default_mode_pref.equals(ApModeName)){
            Intent intent1 = new Intent(this, ApDeviceListActivity.class);
            startActivity(intent1);
        }else if (default_mode_pref.equals(StationModeName)){
            Intent intent = new Intent(this, StationDeviceListActivity.class);
            startActivity(intent);
        }else if (default_mode_pref.equals(RGBModeName)){
            Intent intent = new Intent(this, RgbDeviceListActivity.class);
            startActivity(intent);
        }
        else{
            Button btnStn = findViewById(R.id.buttonstn);
            Button btnAp = findViewById(R.id.buttonap);
            Button btnRgb = findViewById(R.id.buttonrgb);
            Button btnSetUp = findViewById(R.id.buttonSetup);
            btnStn.setOnClickListener(this);
            btnAp.setOnClickListener(this);
            btnRgb.setOnClickListener(this);
            btnSetUp.setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonstn:
                Intent intent = new Intent(this, StationDeviceListActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonap:
                Intent intent1 = new Intent(this, ApDeviceListActivity.class);
                startActivity(intent1);
                break;
            case R.id.buttonrgb:
                Intent intent2 = new Intent(this, RgbDeviceListActivity.class);
                startActivity(intent2);
                break;
            case R.id.buttonSetup:
                Intent intent3 = new Intent(this, MCUsetupActivity.class);
                startActivity(intent3);
                break;
        }
    }
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
    public void loadDataFromPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_DEFAULT_MODE, MODE_PRIVATE);
        default_mode_pref = sharedPreferences.getString(default_val, NoModeName);
    }
}