package com.aqualink.iot.Station;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aqualink.iot.DefaultMode;
import com.aqualink.iot.R;
import com.aqualink.iot.Utility;
import com.aqualink.iot.rgb.RgbDeviceActivity;
import com.aqualink.iot.rgb.RgbDeviceListActivity;
import com.aqualink.iot.rgb.SendUDPData;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StationActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar spinner;
    private Switch modeSwitch,comModeSwitch;
    private TextView modeText,comModeText;
    private String fb_URL;
    private String mainUrl;
    private static final String powers_node = "/powers";
    private static final String schedules_URL = "/schedules";
    private String authString;
    private String urlFromPref, urlFromExtra;
    private String deviceId,deviceName,fbHost,fbAuth;
    private String fbButton1,fbButton2,fbButton3,fbButton4,fbButton5,fbButton6 ;
    public static final String switchOn = "ON";
    public static final String switchOff = "OFF";
    public static final String firebaseOff = "0";
    public static final String firebaseOn = "1";
    public static final String btnTxtColorBlckON = "#000000";
    public static final String btnTxtColorGrnOFF = "#00FF7F";
    public static final String firebasePower[] = {"P_1", "P_2", "P_3", "P_4","P_5","P_6"};
    private TextView power1,power2,power3,power4,power5,power6;
    public static final String firebase_net_c = "NC";
    private String netCheck;
    private  TextView tvDeviceStatus, tvTemperature;
    private BroadcastReceiver minuteUpdateReceiver;
    private Handler handler;
    public static byte[] STUdpIp;
    public static InetAddress STLocal;
    private String ExtraUdpIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station);
        Intent intent = getIntent();
        deviceId = intent.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID);
        deviceName = intent.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC);
        fbHost = intent.getStringExtra(AddEditDeviceActivity.EXTRA_FB_HOST);
        fbAuth = intent.getStringExtra(AddEditDeviceActivity.EXTRA_FB_AUTH);
        ExtraUdpIp = intent.getStringExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST);
        Integer Ip1 = Integer.parseInt(new Utility().getSplitValue(ExtraUdpIp, '.',0));
        Integer Ip2 = Integer.parseInt(new Utility().getSplitValue(ExtraUdpIp, '.',1));
        Integer Ip3 = Integer.parseInt(new Utility().getSplitValue(ExtraUdpIp, '.',2));
        Integer Ip4 = Integer.parseInt(new Utility().getSplitValue(ExtraUdpIp, '.',3));
        STUdpIp =  new byte[] {Ip1.byteValue(),Ip2.byteValue(), Ip3.byteValue(), Ip4.byteValue() };
        STLocal = null;
        try {

            STLocal = InetAddress.getByAddress(STUdpIp);
        } catch (UnknownHostException e) {
            //Log.d("saurav","RTP@ Exception is"+e);
        }
        setTitle(deviceName);
        power1 = findViewById(R.id.pow1);
        power2 = findViewById(R.id.pow2);
        power3 = findViewById(R.id.pow3);
        power4 = findViewById(R.id.pow4);
        power5 = findViewById(R.id.pow5);
        power6 = findViewById(R.id.pow6);
        modeSwitch = (Switch) findViewById(R.id.StnSwitchMode);
        modeText = (TextView) findViewById(R.id.StnTextMode);
        comModeSwitch = (Switch) findViewById(R.id.StnSwitchComMode);
        comModeText = (TextView) findViewById(R.id.StnTextComMode);
       // updateButtonNames();
        urlFromPref = makeUrlFromEXTRA(fbHost,fbAuth,"Y");
        urlFromExtra = makeUrlFromEXTRA(fbHost,fbAuth,"N");

        RelativeLayout layout = findViewById(R.id.root);
        spinner = new ProgressBar(StationActivity.this, null, android.R.attr.progressBarStyleLarge);
        spinner.getIndeterminateDrawable().setTint(Color.parseColor("#FF0000"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(spinner, params);
       // spinner.setVisibility(View.VISIBLE);

        tvDeviceStatus = findViewById(R.id.tvStatus);
        tvTemperature = findViewById(R.id.tvTemp);
        Button buttonSch = findViewById(R.id.buttonsc);
        Button buttonSetTemp = findViewById(R.id.buttonSetTemp);

        if (urlFromPref.isEmpty() && urlFromExtra.isEmpty() ){
            AlertDialog.Builder builder = new AlertDialog.Builder(StationActivity.this);
            builder.setTitle("Please configure Firebase Host and Auth!");
            builder.setMessage("Firebase configuration is missing in both Default Firebase Settings and Device Settings. \n" +
                    "Please add them as per requirement.");
            builder.setCancelable(true);
            builder.show();
        } else {
            if(!urlFromExtra.isEmpty()){
                fb_URL = urlFromExtra + powers_node + authString;
                mainUrl = urlFromExtra;
                buttonSch.setOnClickListener(this);
                buttonSetTemp.setOnClickListener(this);
                syncButtons();
            } else {
                fb_URL = urlFromPref + powers_node + authString;
                mainUrl =  urlFromPref;
                buttonSch.setOnClickListener(this);
                buttonSetTemp.setOnClickListener(this);
                syncButtons();
            }
        }


    }

    public void onBackPressed() {
        Intent a = new Intent(this, StationDeviceListActivity.class);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(a);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.station_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.rename_btn_op:
                Intent intent = new Intent(this, RenameButtons.class);
                intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID, deviceId);
                intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC, deviceName);
                intent.putExtra(AddEditDeviceActivity.EXTRA_FB_HOST, fbHost);
                intent.putExtra(AddEditDeviceActivity.EXTRA_FB_AUTH, fbAuth);
                intent.putExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST, ExtraUdpIp);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonsc:
                Intent intent = new Intent(this, ScheduleStationActivity.class);
                intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID, deviceId);
                intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC, deviceName);
                intent.putExtra(AddEditDeviceActivity.EXTRA_FB_HOST, fbHost);
                intent.putExtra(AddEditDeviceActivity.EXTRA_FB_AUTH, fbAuth);
                intent.putExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST, ExtraUdpIp);
                intent.putExtra(SetSchedulerActivity.EXTRA_SCHEDULE_URL,mainUrl+schedules_URL+authString );
                startActivity(intent);
                break;
            case R.id.buttonSetTemp:
                Intent intent1 = new Intent(this, SetTemperature.class);
                intent1.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID, deviceId);
                intent1.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC, deviceName);
                intent1.putExtra(AddEditDeviceActivity.EXTRA_FB_HOST, fbHost);
                intent1.putExtra(AddEditDeviceActivity.EXTRA_FB_AUTH, fbAuth);
                intent1.putExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST, ExtraUdpIp);
                intent1.putExtra(SetTemperature.EXTRA_TEMP_URL,mainUrl+schedules_URL+authString );
                startActivity(intent1);
                break;
            case R.id.button1:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[0], firebaseOff}});
                    new SendUDPDataST().execute(firebasePower[0].substring(2)+","+ firebaseOff );
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[0], firebaseOn}});
                    new SendUDPDataST().execute(firebasePower[0].substring(2)+","+ firebaseOn );
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;
            case R.id.button2:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[1], firebaseOff}});
                    new SendUDPDataST().execute(firebasePower[1].substring(2)+","+ firebaseOff );
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[1], firebaseOn}});
                    new SendUDPDataST().execute(firebasePower[1].substring(2)+","+ firebaseOn );
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;

            case R.id.button3:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[2], firebaseOff}});
                    new SendUDPDataST().execute(firebasePower[2].substring(2)+","+ firebaseOff );
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[2], firebaseOn}});
                    new SendUDPDataST().execute(firebasePower[2].substring(2)+","+ firebaseOn );
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;

            case R.id.button4:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[3], firebaseOff}});
                    new SendUDPDataST().execute(firebasePower[3].substring(2)+","+ firebaseOff );
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[3], firebaseOn}});
                    new SendUDPDataST().execute(firebasePower[3].substring(2)+","+ firebaseOn );
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;

            case R.id.button5:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[4], firebaseOff}});
                    new SendUDPDataST().execute(firebasePower[4].substring(2)+","+ firebaseOff );
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[4], firebaseOn}});
                    new SendUDPDataST().execute(firebasePower[4].substring(2)+","+ firebaseOn );
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;

            case R.id.button6:
                if (((Button) v).getText().toString().equals(switchOn)) {
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[5], firebaseOff}});
                    new SendUDPDataST().execute(firebasePower[5].substring(2)+","+ firebaseOff );
                    ((Button) v).setText(switchOff);
                    v.setBackgroundResource(R.drawable.offbutton);
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorGrnOFF));

                } else {
                    ((Button) v).setText(switchOn);
                    new Utility().voleySendHttpRequest(this, fb_URL, "PATCH", new String[][]{{firebasePower[5], firebaseOn}});
                    new SendUDPDataST().execute(firebasePower[5].substring(2)+","+ firebaseOn );
                    ((Button) v).setTextColor(Color.parseColor(btnTxtColorBlckON));
                    v.setBackgroundResource(R.drawable.onbutton);

                }
                break;
            case R.id.getStatus:
              //  getDeviceStatus();
                syncButtons();

        }
    }

    private String makeUrlFromEXTRA(String host, String auth, String isPref) {
        String URL = "";
        if (isPref.equals("Y")){
            host = new Utility().getSharedPref(this, SettingsStation.SHARED_PREFS_ST_GLOBAL, SettingsStation.FirebaseUrl);
            auth = new Utility().getSharedPref(this, SettingsStation.SHARED_PREFS_ST_GLOBAL, SettingsStation.FirebaseAuth);
              }
        if (!host.isEmpty() && !auth.isEmpty()) {
            URL = "https://" + host;
            URL = URL.concat("/"+ deviceId );
            authString = ".json?auth=" + auth;
            fbHost = host;
            fbAuth = auth;
        }

        return URL;
    }

    private void updateButtons() {
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button buttonStatus = findViewById(R.id.getStatus);
        if (fbButton1.equals(firebaseOff)) {
            button1.setText(switchOff);
            button1.setBackgroundResource(R.drawable.offbutton);
            button1.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton1.equals(firebaseOn)) {
            button1.setText(switchOn);
            button1.setBackgroundResource(R.drawable.onbutton);
            button1.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }
        if (fbButton2.equals(firebaseOff)) {
            button2.setText(switchOff);
            button2.setBackgroundResource(R.drawable.offbutton);
            button2.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton2.equals(firebaseOn)) {
            button2.setText(switchOn);
            button2.setBackgroundResource(R.drawable.onbutton);
            button2.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }

        if (fbButton3.equals(firebaseOff)) {
            button3.setText(switchOff);
            button3.setBackgroundResource(R.drawable.offbutton);
            button3.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton3.equals(firebaseOn)) {
            button3.setText(switchOn);
            button3.setBackgroundResource(R.drawable.onbutton);
            button3.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }

        if (fbButton4.equals(firebaseOff)) {
            button4.setText(switchOff);
            button4.setBackgroundResource(R.drawable.offbutton);
            button4.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton4.equals(firebaseOn)) {
            button4.setText(switchOn);
            button4.setBackgroundResource(R.drawable.onbutton);
            button4.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }

        if (fbButton5.equals(firebaseOff)) {
            button5.setText(switchOff);
            button5.setBackgroundResource(R.drawable.offbutton);
            button5.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton5.equals(firebaseOn)) {
            button5.setText(switchOn);
            button5.setBackgroundResource(R.drawable.onbutton);
            button5.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }

        if (fbButton6.equals(firebaseOff)) {
            button6.setText(switchOff);
            button6.setBackgroundResource(R.drawable.offbutton);
            button6.setTextColor(Color.parseColor(btnTxtColorGrnOFF));
        } else if (fbButton6.equals(firebaseOn)) {
            button6.setText(switchOn);
            button6.setBackgroundResource(R.drawable.onbutton);
            button6.setTextColor(Color.parseColor(btnTxtColorBlckON));
        }
        button1.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        buttonStatus.setOnClickListener(this);
        modeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
                                                           new Utility().voleySendHttpRequest(StationActivity.this, mainUrl+schedules_URL+authString, "PATCH", new String[][]{{"MD",modeVal},{"SS","Y"}});
                                                      }
                                                      catch (Exception e) {
                                                          e.printStackTrace();
                                                      }


                                                  }
                                              }
        );
        comModeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                      String modeVal;
                                                      if (b) {
                                                          modeVal = "1";
                                                          comModeText.setText("Firebase");
                                                      } else {
                                                          modeVal = "2";
                                                          comModeText.setText("Local");
                                                      }
                                                      try {
                                                          new Utility().voleySendHttpRequest(StationActivity.this, mainUrl+schedules_URL+authString, "PATCH", new String[][]{{"CMD",modeVal},{"SS","Y"}});
                                                      }
                                                      catch (Exception e) {
                                                          e.printStackTrace();
                                                      }


                                                  }
                                              }
        );
    }


private void updateButtonNames(String btnNames){
        String BtnName;
       if (btnNames != null && !btnNames.isEmpty()) {
           BtnName = btnNames;
       } else {
           BtnName ="btn1,btn2,btn3,btn4,btn5,btn6";
       }

    power1.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","1",new Utility().getSplitValue(BtnName, ',',0)));
    power2.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","2",new Utility().getSplitValue(BtnName, ',',1)));
    power3.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","3",new Utility().getSplitValue(BtnName, ',',2)));
    power4.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","4",new Utility().getSplitValue(BtnName, ',',3)));
    power5.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","5",new Utility().getSplitValue(BtnName, ',',4)));
    power6.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","6",new Utility().getSplitValue(BtnName, ',',5)));

}
    private void syncButtons() {
        spinner.setVisibility(View.VISIBLE);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fb_URL, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            fbButton1 = response.getString(firebasePower[0]);
                            fbButton2 = response.getString(firebasePower[1]);
                            fbButton3 = response.getString(firebasePower[2]);
                            fbButton4 = response.getString(firebasePower[3]);
                            fbButton5 = response.getString(firebasePower[4]);
                            fbButton6 = response.getString(firebasePower[5]);
                            netCheck = response.getString(firebase_net_c);
                            String BtnNames = response.getString("btnNm");
                            if (netCheck.equals("OK")){
                                updateButtonNames(BtnNames);
                             updateButtons();
                             getDeviceStatus();
                                getDeviceMode();
                                spinner.setVisibility(View.GONE);}
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
        String url = mainUrl +authString;
        RequestQueue mQueue = Volley.newRequestQueue(this);
        spinner.setVisibility(View.VISIBLE);
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                         String   deviceLon = response.getString("LO");
                         String   temperature = response.getString("TM");

                            if (!deviceLon.isEmpty()){
                                tvDeviceStatus.setText("Last Online : "+ getDeviceStatusText(deviceLon));
                                tvTemperature.setText("Temperature : "+getDeviceTempText(temperature) + " °C");
                                spinner.setVisibility(View.GONE);
                            }  else {
                                Toast.makeText(StationActivity.this, "Please Check your Internet Connection / Device Settings", Toast.LENGTH_SHORT).show();
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

    private void getDeviceMode() {
        String url = mainUrl+schedules_URL+authString;
        RequestQueue mQueue = Volley.newRequestQueue(this);
        spinner.setVisibility(View.VISIBLE);
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String Mode = response.getString("MD");
                            String Com_mode = response.getString("CMD");
                            if (!Mode.isEmpty()){
                                if(Com_mode.equals("1") ) {
                                    comModeSwitch.setChecked(true);
                                    comModeText.setText("Firebase");
                                } else{
                                    comModeSwitch.setChecked(false);
                                    comModeText.setText("Local");
                                }
                                if(Mode.equals("2") ) {
                                    modeSwitch.setChecked(true);
                                    modeText.setText("Auto");
                                } else{
                                    modeSwitch.setChecked(false);
                                    modeText.setText("Manual");
                                }

                                spinner.setVisibility(View.GONE);
                            }  else {
                                Toast.makeText(StationActivity.this, "Please Check your Internet Connection / Device Settings", Toast.LENGTH_SHORT).show();
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
        int intYear;
        try {
            intMon = Integer.parseInt( text.substring(3,5));
        }
        catch (NumberFormatException e)
        {
            intMon = 0;
        }
        try {
            intYear = Integer.parseInt( text.substring(6,9));
        }
        catch (NumberFormatException e)
        {
            intYear = 0;
        }

        statusText.append(convert24Time(text.substring(10,15))+ "  " + new Utility().Months[intMon] +" " +text.substring(0,2)+
                "," + (1900 + intYear) );

        return statusText.toString();
    }
    private static String getDeviceTempText(String text) {
        String Temp = "";
        if ( !text.equalsIgnoreCase("-127.00")){
            Temp = text;
        }
        return Temp.toString();
    }
    public void updateui () {
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                syncButtons();
                handler.postDelayed(this, 10000);
            }
        };

        handler.postDelayed(r, 10000);
    }
 /*   public void startMinuteUpdater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        minuteUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { */
          /*      try {
                    //set time in mili
                    Thread.sleep(1000);

                }catch (Exception e){
                    e.printStackTrace();
                } */
 /*               syncButtons();
            }
        };
        registerReceiver(minuteUpdateReceiver, intentFilter);
    } */
    @Override
    protected void onResume() {
        super.onResume();
      //  startMinuteUpdater();
      //  updateui ();

    }
    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(minuteUpdateReceiver);
    }

    public static String convert24Time(String time) {
        String time12 = null;
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(time);
            time12 = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
return time12;
    }
}


