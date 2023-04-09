package com.aqualink.iot.AP;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aqualink.iot.R;
import com.aqualink.iot.Station.StationActivity;
import com.aqualink.iot.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ApActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar spinner;
    private static final String OP_HOST = "http://192.168.4.1/";
    private static final String OP_URL_RES = "/operations";
    private static final String PO_URL_RES = "/powerstat";
    private static final String DS_URL_RES = "/timenow";
    private static final String SDT_URL_RES = "/setdatetime";
    private String OP_URL;
    private String PO_URL;
    private String DS_URL;
    private String SDT_URL;
    private BroadcastReceiver minuteUpdateReceiver;
    private String deviceId,deviceName;
    private String fbButton1,fbButton2,fbButton3,fbButton4;
    private static final String switchOn = "ON";
    private static final String switchOff = "OFF";
    private static final String espOFF = "0";
    private static final String espON = "1";
    private static final String jsonElPower = "power";
    private static final String jsonElOperation= "operation";
    private static final String espPINS [] = {"0", "1", "2", "3","4","5"};
    private static final String espPOWJSON [] = {"P1", "P2", "P3", "P4"};
    private static final String btnTxtColorBlckON = "#000000";
    private static final String btnTxtColorGrnOFF = "#00FF7F";
    private TextView tvDeviceStatus, tvTemperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        deviceId = intent.getStringExtra(ApAddEditDevice.EXTRA_DEVICE_ID);
        deviceName = intent.getStringExtra(ApAddEditDevice.EXTRA_DEVICE_DESC);
        setTitle(deviceName);

        RelativeLayout layout = findViewById(R.id.root);
        spinner = new ProgressBar(ApActivity.this, null, android.R.attr.progressBarStyleLarge);
        spinner.getIndeterminateDrawable().setTint(Color.parseColor("#FF0000"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(spinner, params);
        spinner.setVisibility(View.VISIBLE);
        OP_URL = OP_HOST + deviceId + OP_URL_RES;
        PO_URL = OP_HOST + deviceId + PO_URL_RES;
        DS_URL = OP_HOST + deviceId + DS_URL_RES;
        SDT_URL = OP_HOST + deviceId + SDT_URL_RES;
        tvDeviceStatus = findViewById(R.id.tvStatus);
        tvTemperature = findViewById(R.id.tvTemp);
        Button buttonSch = findViewById(R.id.buttonsc);
        buttonSch.setOnClickListener(this);
        syncButtons();
    }

    public void onBackPressed() {
        Intent a = new Intent(this, ApDeviceListActivity.class);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(a);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonsc:
                Intent intent = new Intent(this, ScheduleApActivity.class);
                intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_ID, deviceId);
                intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_DESC, deviceName);
                startActivity(intent);
                break;
            case R.id.button1:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, OP_URL, "POST", new String[][]{{jsonElPower,espPINS[0]},{jsonElOperation,espOFF}});
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, OP_URL, "POST", new String[][]{{jsonElPower,espPINS[0]},{jsonElOperation,espON}});
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;
            case R.id.button2:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, OP_URL, "POST", new String[][]{{jsonElPower,espPINS[1]},{jsonElOperation,espOFF}});
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, OP_URL, "POST", new String[][]{{jsonElPower,espPINS[1]},{jsonElOperation,espON}});
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;

            case R.id.button3:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, OP_URL, "POST", new String[][]{{jsonElPower,espPINS[2]},{jsonElOperation,espOFF}});
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, OP_URL, "POST", new String[][]{{jsonElPower,espPINS[2]},{jsonElOperation,espON}});
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;

            case R.id.button4:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, OP_URL, "POST", new String[][]{{jsonElPower,espPINS[3]},{jsonElOperation,espOFF}});
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, OP_URL, "POST", new String[][]{{jsonElPower,espPINS[3]},{jsonElOperation,espON}});
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;
            case R.id.getStatus:
                getDeviceStatus();
                syncButtons();
                break;
            case R.id.setDateTme:
                setDateTime();
        }
    }



    private void updateButtons() {
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button buttonStatus = findViewById(R.id.getStatus);
        Button buttonDateTime = findViewById(R.id.setDateTme);
        if (fbButton1.equals(espOFF)) {
            button1.setText(switchOff);
            button1.setBackgroundResource(R.drawable.offbutton);
            button1.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton1.equals(espON)) {
            button1.setText(switchOn);
            button1.setBackgroundResource(R.drawable.onbutton);
            button1.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }
        if (fbButton2.equals(espOFF)) {
            button2.setText(switchOff);
            button2.setBackgroundResource(R.drawable.offbutton);
            button2.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton2.equals(espON)) {
            button2.setText(switchOn);
            button2.setBackgroundResource(R.drawable.onbutton);
            button2.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }

        if (fbButton3.equals(espOFF)) {
            button3.setText(switchOff);
            button3.setBackgroundResource(R.drawable.offbutton);
            button3.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton3.equals(espON)) {
            button3.setText(switchOn);
            button3.setBackgroundResource(R.drawable.onbutton);
            button3.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }

        if (fbButton4.equals(espOFF)) {
            button4.setText(switchOff);
            button4.setBackgroundResource(R.drawable.offbutton);
            button4.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton4.equals(espON)) {
            button4.setText(switchOn);
            button4.setBackgroundResource(R.drawable.onbutton);
            button4.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }
        button1.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        buttonStatus.setOnClickListener(this);
        buttonDateTime.setOnClickListener(this);
    }

    private void syncButtons() {

        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, PO_URL, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            fbButton1 = response.getString(espPOWJSON[0]);
                            fbButton2 = response.getString(espPOWJSON[1]);
                            fbButton3 = response.getString(espPOWJSON[2]);
                            fbButton4 = response.getString(espPOWJSON[3]);
                            updateButtons();
                            getDeviceStatus();
                            spinner.setVisibility(View.GONE);
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

    private void getDeviceStatus() {
        spinner.setVisibility(View.VISIBLE);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DS_URL, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String   deviceLon = response.getString("timeNow");
                            String   temp = response.getString("temp");
                            if (!deviceLon.isEmpty()){
                                tvDeviceStatus.setText("Device Time : "+ getDeviceStatusText(deviceLon));
                                tvTemperature.setText("Temperature : " +temp+ " °C");
                                spinner.setVisibility(View.GONE);
                            }  else {
                                Toast.makeText(ApActivity.this, "Please Check your Internet Connection / Device Settings", Toast.LENGTH_SHORT).show();
                            }
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
    private static String getDeviceStatusText(String text) {
        StringBuilder statusText = new StringBuilder();
        int intMon;
        try {
            intMon = Integer.parseInt( text.substring(3,5));
        }
        catch (NumberFormatException e)
        {
            intMon = 0;
        }


        statusText.append(StationActivity.convert24Time(text.substring(11,16))+"  " + new Utility().Months[intMon - 1] +" " +text.substring(0,2)+
                "," + text.substring(6,10));

        return statusText.toString();
    }
    private void setDateTime(){
        spinner.setVisibility(View.VISIBLE);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String Date = dateFormat.format(c.getTime());
        String Time = timeFormat.format(c.getTime());
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        try {
                req.put("date", Date);
                req.put("time", Time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SDT_URL, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String  status = response.getString("status");
                            if (status.equals("ok")) {
                                spinner.setVisibility(View.GONE);
                                getDeviceStatus();
                                Toast.makeText(ApActivity.this, "Device Date Time Updated", Toast.LENGTH_SHORT).show();
                            }
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
    public void startMinuteUpdater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        minuteUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getDeviceStatus();
                syncButtons();
            }
        };
        registerReceiver(minuteUpdateReceiver, intentFilter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        startMinuteUpdater();
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(minuteUpdateReceiver);
    }
}