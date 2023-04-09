package com.aqualink.iot.Station;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aqualink.iot.R;
import com.aqualink.iot.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleStationActivity extends AppCompatActivity implements View.OnClickListener{

    // Name of shared preference
    public static final String SHARED_PREFS = "sharedPrefs";
    // Other Constants
    private String deviceId ,deviceName,fbHost,fbAuth,ExtraUdpIp;
    public String schURL;
    private String netCheck;
    private ProgressBar spinner;

    public static final String[] firebaseSchNode = {"S01","S02","S03","S04","S05","S06","S07","S08","S09","S10","S11","S12"};
    private static String scheduleVal01,scheduleVal02,scheduleVal03,scheduleVal04,scheduleVal05,scheduleVal06,scheduleVal07,scheduleVal08,scheduleVal09,scheduleVal10,scheduleVal11,scheduleVal12;
    TextView tvShowSch1, tvShowSch2, tvShowSch3, tvShowSch4, tvShowSch5, tvShowSch6, tvShowSch7, tvShowSch8,tvShowSch9,tvShowSch10,tvShowSch11,tvShowSch12;
    TextView tvHeadSch1, tvHeadSch2, tvHeadSch3, tvHeadSch4, tvHeadSch5, tvHeadSch6, tvHeadSch7, tvHeadSch8,tvHeadSch9,tvHeadSch10,tvHeadSch11,tvHeadSch12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Intent intent = getIntent();
        deviceId = intent.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID);
        deviceName = intent.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC);
        fbHost = intent.getStringExtra(AddEditDeviceActivity.EXTRA_FB_HOST);
        fbAuth = intent.getStringExtra(AddEditDeviceActivity.EXTRA_FB_AUTH);
        schURL = intent.getStringExtra(SetSchedulerActivity.EXTRA_SCHEDULE_URL);
        ExtraUdpIp = intent.getStringExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST);

        setTitle("Schedules : " + deviceName);
        RelativeLayout layout = findViewById(R.id.root);
        spinner = new ProgressBar(ScheduleStationActivity.this, null, android.R.attr.progressBarStyleLarge);
        spinner.getIndeterminateDrawable().setTint(Color.parseColor("#FF0000"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(spinner, params);
        spinner.setVisibility(View.VISIBLE);

        tvShowSch1 = findViewById(R.id.textviewValue1);
        tvShowSch2 = findViewById(R.id.textviewValue2);
        tvShowSch3 = findViewById(R.id.textviewValue3);
        tvShowSch4 = findViewById(R.id.textviewValue4);
        tvShowSch5 = findViewById(R.id.textviewValue5);
        tvShowSch6 = findViewById(R.id.textviewValue6);
        tvShowSch7 = findViewById(R.id.textviewValue7);
        tvShowSch8 = findViewById(R.id.textviewValue8);
        tvShowSch9 = findViewById(R.id.textviewValue9);
        tvShowSch10 = findViewById(R.id.textviewValue10);
        tvShowSch11 = findViewById(R.id.textviewValue11);
        tvShowSch12 = findViewById(R.id.textviewValue12);
        tvHeadSch1 = findViewById(R.id.textviewName1);
        tvHeadSch2 = findViewById(R.id.textviewName2);
        tvHeadSch3 = findViewById(R.id.textviewName3);
        tvHeadSch4 = findViewById(R.id.textviewName4);
        tvHeadSch5 = findViewById(R.id.textviewName5);
        tvHeadSch6 = findViewById(R.id.textviewName6);
        tvHeadSch7 = findViewById(R.id.textviewName7);
        tvHeadSch8 = findViewById(R.id.textviewName8);
        tvHeadSch9 = findViewById(R.id.textviewName9);
        tvHeadSch10 = findViewById(R.id.textviewName10);
        tvHeadSch11 = findViewById(R.id.textviewName11);
        tvHeadSch12 = findViewById(R.id.textviewName12);
           getSchedulesFromPref();
           UpdateScheduleViewsServer ();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit1:
                setScheduler( firebaseSchNode[0]+":"+scheduleVal01);
                break;
            case R.id.edit2:
                setScheduler( firebaseSchNode[1]+":"+scheduleVal02);
                break;
            case R.id.edit3:
                setScheduler( firebaseSchNode[2]+":"+scheduleVal03);
                break;
            case R.id.edit4:
                setScheduler( firebaseSchNode[3]+":"+scheduleVal04);
                break;
            case R.id.edit5:
                setScheduler( firebaseSchNode[4]+":"+scheduleVal05);
                break;
            case R.id.edit6:
                setScheduler( firebaseSchNode[5]+":"+scheduleVal06);
                break;
            case R.id.edit7:
                setScheduler( firebaseSchNode[6]+":"+scheduleVal07);
                break;
            case R.id.edit8:
                setScheduler( firebaseSchNode[7]+":"+scheduleVal08);
                break;
            case R.id.edit9:
                setScheduler( firebaseSchNode[8]+":"+scheduleVal09);
                break;
            case R.id.edit10:
                setScheduler( firebaseSchNode[9]+":"+scheduleVal10);
                break;
            case R.id.edit11:
                setScheduler( firebaseSchNode[10]+":"+scheduleVal11);
                break;
            case R.id.edit12:
                setScheduler( firebaseSchNode[11]+":"+scheduleVal12);
                break;
            case R.id.delete1:
                deleteSch(1,tvHeadSch1,tvShowSch1);
                break;
          case R.id.delete2:
              deleteSch(2,tvHeadSch2,tvShowSch2);
                break;
            case R.id.delete3:
                deleteSch(3,tvHeadSch3,tvShowSch3);
                break;
            case R.id.delete4:
                deleteSch(4,tvHeadSch4,tvShowSch4);
                break;
            case R.id.delete5:
                deleteSch(5,tvHeadSch5,tvShowSch5);
                break;
            case R.id.delete6:
                deleteSch(6,tvHeadSch6,tvShowSch6);
                break;
            case R.id.delete7:
                deleteSch(7,tvHeadSch7,tvShowSch7);
                break;
            case R.id.delete8:
                deleteSch(8,tvHeadSch8,tvShowSch8);
                break;
            case R.id.delete9:
                deleteSch(9,tvHeadSch9,tvShowSch9);
                break;
            case R.id.delete10:
                deleteSch(10,tvHeadSch10,tvShowSch10);
                break;
            case R.id.delete11:
                deleteSch(11,tvHeadSch11,tvShowSch11);
                break;
            case R.id.delete12:
                deleteSch(12,tvHeadSch12,tvShowSch12);
                break;

        }
    }


   void setScheduler (String intentExtra){
       Intent intent = new Intent(this, SetSchedulerActivity.class);
       intent.putExtra(SetSchedulerActivity.EXTRA_SCHEDULE, intentExtra);
       intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID, deviceId);
       intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC, deviceName);
       intent.putExtra(SetSchedulerActivity.EXTRA_SCHEDULE_URL, schURL);
       intent.putExtra(AddEditDeviceActivity.EXTRA_FB_HOST, fbHost);
       intent.putExtra(AddEditDeviceActivity.EXTRA_FB_AUTH, fbAuth);
       intent.putExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST, ExtraUdpIp);
       startActivity(intent);
    }

     void getSchedulesFromPref() {
         scheduleVal01 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[0]);
         scheduleVal02 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[1]);
         scheduleVal03 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[2]);
         scheduleVal04 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[3]);
         scheduleVal05 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[4]);
         scheduleVal06 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[5]);
         scheduleVal07 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[6]);
         scheduleVal08 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[7]);
         scheduleVal09 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[8]);
         scheduleVal10 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[9]);
         scheduleVal11 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[10]);
         scheduleVal12 =  new Utility().getSharedPref(this,SHARED_PREFS,deviceId +firebaseSchNode[11]);

         tvHeadSch1.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[0],"Schedule " + 1));
         tvHeadSch2.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[1],"Schedule " + 2));
         tvHeadSch3.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[2],"Schedule " + 3));
         tvHeadSch4.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[3],"Schedule " + 4));
         tvHeadSch5.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[4],"Schedule " + 5));
         tvHeadSch6.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[5],"Schedule " + 6));
         tvHeadSch7.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[6],"Schedule " + 7));
         tvHeadSch8.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[7],"Schedule " + 8));
         tvHeadSch9.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[8],"Schedule " + 9));
         tvHeadSch10.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[9],"Schedule " + 10));
         tvHeadSch11.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[10],"Schedule " + 11));
         tvHeadSch12.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl",firebaseSchNode[11],"Schedule " + 12));
          UpdateScheduleViews ();
    }

    void UpdateScheduleViews () {
        tvShowSch1.setText(deriveTxt(scheduleVal01));
        tvShowSch2.setText(deriveTxt(scheduleVal02));
        tvShowSch3.setText(deriveTxt(scheduleVal03));
        tvShowSch4.setText(deriveTxt(scheduleVal04));
        tvShowSch5.setText(deriveTxt(scheduleVal05));
        tvShowSch6.setText(deriveTxt(scheduleVal06));
        tvShowSch7.setText(deriveTxt(scheduleVal07));
        tvShowSch8.setText(deriveTxt(scheduleVal08));
        tvShowSch9.setText(deriveTxt(scheduleVal09));
        tvShowSch10.setText(deriveTxt(scheduleVal10));
        tvShowSch11.setText(deriveTxt(scheduleVal11));
        tvShowSch12.setText(deriveTxt(scheduleVal12));
    }
    void SetOnClickListner (){
        ImageView Edit1 = findViewById(R.id.edit1);
        ImageView Delete1 = findViewById(R.id.delete1);
        ImageView Edit2 = findViewById(R.id.edit2);
        ImageView Delete2 = findViewById(R.id.delete2);
        ImageView Edit3 = findViewById(R.id.edit3);
        ImageView Delete3 = findViewById(R.id.delete3);
        ImageView Edit4 = findViewById(R.id.edit4);
        ImageView Delete4 = findViewById(R.id.delete4);
        ImageView Edit5 = findViewById(R.id.edit5);
        ImageView Delete5 = findViewById(R.id.delete5);
        ImageView Edit6 = findViewById(R.id.edit6);
        ImageView Delete6 = findViewById(R.id.delete6);
        ImageView Edit7 = findViewById(R.id.edit7);
        ImageView Delete7 = findViewById(R.id.delete7);
        ImageView Edit8 = findViewById(R.id.edit8);
        ImageView Delete8 = findViewById(R.id.delete8);
        ImageView Edit9 = findViewById(R.id.edit9);
        ImageView Delete9 = findViewById(R.id.delete9);
        ImageView Edit10 = findViewById(R.id.edit10);
        ImageView Delete10 = findViewById(R.id.delete10);
        ImageView Edit11 = findViewById(R.id.edit11);
        ImageView Delete11 = findViewById(R.id.delete11);
        ImageView Edit12 = findViewById(R.id.edit12);
        ImageView Delete12 = findViewById(R.id.delete12);

        Edit1.setOnClickListener(this);
        Edit2.setOnClickListener(this);
        Edit3.setOnClickListener(this);
        Edit4.setOnClickListener(this);
        Edit5.setOnClickListener(this);
        Edit6.setOnClickListener(this);
        Edit7.setOnClickListener(this);
        Edit8.setOnClickListener(this);
        Edit9.setOnClickListener(this);
        Edit10.setOnClickListener(this);
        Edit11.setOnClickListener(this);
        Edit12.setOnClickListener(this);

        Delete1.setOnClickListener(this);
        Delete2.setOnClickListener(this);
        Delete3.setOnClickListener(this);
        Delete4.setOnClickListener(this);
        Delete5.setOnClickListener(this);
        Delete6.setOnClickListener(this);
        Delete7.setOnClickListener(this);
        Delete8.setOnClickListener(this);
        Delete9.setOnClickListener(this);
        Delete10.setOnClickListener(this);
        Delete11.setOnClickListener(this);
        Delete12.setOnClickListener(this);


    }

 private String deriveTxt( String  value) {
     String deriveVal = "";
     if (!value.isEmpty()) {
            deriveVal = ( new Utility().getSharedPrefdef(this, deviceId + "_buttons",value.substring(0, 1),"Power"+ value.substring(0, 1)))   + "\n" +
                    SetSchedulerActivity.redefineReverseAction(value.substring(2, 3)) + "\n" +
                    StationActivity.convert24Time(value.substring(4, 9))+"\n"+ value.substring(10, 12) +"\n"+
                    SetSchedulerActivity.getDaysName(value.substring(13));
        }
        return deriveVal;
    }
 private  void UpdateScheduleViewsServer () {
     RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, schURL, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                scheduleVal01 = response.getString(firebaseSchNode[0]);
                                scheduleVal02 = response.getString(firebaseSchNode[1]);
                                scheduleVal03 = response.getString(firebaseSchNode[2]);
                                scheduleVal04 = response.getString(firebaseSchNode[3]);
                                scheduleVal05 = response.getString(firebaseSchNode[4]);
                                scheduleVal06 = response.getString(firebaseSchNode[5]);
                                scheduleVal07 = response.getString(firebaseSchNode[6]);
                                scheduleVal08 = response.getString(firebaseSchNode[7]);
                                scheduleVal09 = response.getString(firebaseSchNode[8]);
                                scheduleVal10 = response.getString(firebaseSchNode[9]);
                                scheduleVal11 = response.getString(firebaseSchNode[10]);
                                scheduleVal12 = response.getString(firebaseSchNode[11]);
                           // netCheck = response.getString(StationActivity.firebase_net_c);
                          //  if (netCheck.equals("OK")){
                                UpdateScheduleViews ();
                                SetOnClickListner ();
                                spinner.setVisibility(View.GONE);
                         //   }


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

    private  void  deleteSch(int schNumber, TextView schName, TextView schVal){
        new Utility().setSharedPref(this,SHARED_PREFS, new String[][]{{deviceId +firebaseSchNode[schNumber -1], ""}});
        new Utility().setSharedPref(this,deviceId + "_schdl", new String[][]{{firebaseSchNode[schNumber -1], "Schedule " + schNumber}});
        schVal.setText(new Utility().voleySendHttpRequest(this,schURL,"PATCH",new String[][]{{firebaseSchNode[schNumber - 1], ""},{"SS", "Y"}}));
        schName.setText("Schedule " + schNumber);
    }
    public void onBackPressed(){
        Intent intent = new Intent(this,StationActivity.class);
        intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID, deviceId);
        intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC, deviceName);
        intent.putExtra(AddEditDeviceActivity.EXTRA_FB_HOST, fbHost);
        intent.putExtra(AddEditDeviceActivity.EXTRA_FB_AUTH, fbAuth);
        intent.putExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST, ExtraUdpIp);
        startActivity(intent);
    }

}
