package com.aqualink.iot.AP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.aqualink.iot.Station.AddEditDeviceActivity;
import com.aqualink.iot.Station.SetSchedulerActivity;
import com.aqualink.iot.Station.StationActivity;
import com.aqualink.iot.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleApActivity extends AppCompatActivity  implements View.OnClickListener {

    private String deviceId ,deviceName;
    private static final String OP_HOST = "http://192.168.4.1/";
    private static final String OP_Sch = "/schedules";
    public String schURL;
    private ProgressBar spinner;

    public static final String[] EspSchNode = {"S01","S02","S03","S04","S05","S06","S07","S08","S09","S10","S11","S12"};
    private static String scheduleVal01,scheduleVal02,scheduleVal03,scheduleVal04,scheduleVal05,scheduleVal06,scheduleVal07,scheduleVal08,scheduleVal09,scheduleVal10,scheduleVal11,scheduleVal12,tempNode;
    TextView tvShowSch1, tvShowSch2, tvShowSch3, tvShowSch4, tvShowSch5, tvShowSch6, tvShowSch7, tvShowSch8,tvShowSch9,tvShowSch10,tvShowSch11,tvShowSch12;
    TextView tvHeadSch1, tvHeadSch2, tvHeadSch3, tvHeadSch4, tvHeadSch5, tvHeadSch6, tvHeadSch7, tvHeadSch8,tvHeadSch9,tvHeadSch10,tvHeadSch11,tvHeadSch12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_ap);
        Intent intent = getIntent();
        deviceId = intent.getStringExtra(ApAddEditDevice.EXTRA_DEVICE_ID);
        deviceName = intent.getStringExtra(ApAddEditDevice.EXTRA_DEVICE_DESC);
        setTitle("Schedules : " + deviceName);
        schURL = OP_HOST + deviceId + OP_Sch;
        RelativeLayout layout = findViewById(R.id.root);
        spinner = new ProgressBar(ScheduleApActivity.this, null, android.R.attr.progressBarStyleLarge);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit1:
                setScheduler( EspSchNode[0]+":"+scheduleVal01);
                break;
            case R.id.edit2:
                setScheduler( EspSchNode[1]+":"+scheduleVal02);
                break;
            case R.id.edit3:
                setScheduler( EspSchNode[2]+":"+scheduleVal03);
                break;
            case R.id.edit4:
                setScheduler( EspSchNode[3]+":"+scheduleVal04);
                break;
            case R.id.edit5:
                setScheduler( EspSchNode[4]+":"+scheduleVal05);
                break;
            case R.id.edit6:
                setScheduler( EspSchNode[5]+":"+scheduleVal06);
                break;
            case R.id.edit7:
                setScheduler( EspSchNode[6]+":"+scheduleVal07);
                break;
            case R.id.edit8:
                setScheduler( EspSchNode[7]+":"+scheduleVal08);
                break;
            case R.id.edit9:
                setScheduler( EspSchNode[8]+":"+scheduleVal09);
                break;
            case R.id.edit10:
                setScheduler( EspSchNode[9]+":"+scheduleVal10);
                break;
            case R.id.edit11:
                setScheduler( EspSchNode[10]+":"+scheduleVal11);
                break;
            case R.id.edit12:
                setScheduler( EspSchNode[11]+":"+scheduleVal12);
                break;

            case R.id.delete1:
                scheduleVal01 = "";
                tvShowSch1.setText(DeleteSchedule(1));
                tvHeadSch1.setText("Schedule " + 1);
                break;
            case R.id.delete2:
                scheduleVal02 = "";
                tvShowSch2.setText(DeleteSchedule(2));
                tvHeadSch2.setText("Schedule " + 2);
                break;
            case R.id.delete3:
                scheduleVal03 = "";
                tvShowSch3.setText(DeleteSchedule(3));
                tvHeadSch3.setText("Schedule " + 3);
                break;
            case R.id.delete4:
                scheduleVal04 = "";
                tvShowSch4.setText(DeleteSchedule(4));
                tvHeadSch4.setText("Schedule " + 4);
                break;
            case R.id.delete5:
                scheduleVal05 = "";
                tvShowSch5.setText(DeleteSchedule(5));
                tvHeadSch5.setText("Schedule " + 5);
                break;
            case R.id.delete6:
                scheduleVal06 = "";
                tvShowSch6.setText(DeleteSchedule(6));
                tvHeadSch6.setText("Schedule " + 6);
                break;
            case R.id.delete7:
                scheduleVal07 = "";
                tvShowSch7.setText(DeleteSchedule(7));
                tvHeadSch7.setText("Schedule " + 7);
                break;
            case R.id.delete8:
                scheduleVal08 = "";
                tvShowSch8.setText(DeleteSchedule(8));
                tvHeadSch8.setText("Schedule " + 8);
                break;
            case R.id.delete9:
                scheduleVal09 = "";
                tvShowSch9.setText(DeleteSchedule(9));
                tvHeadSch9.setText("Schedule " + 9);
                break;
            case R.id.delete10:
                scheduleVal10 = "";
                tvShowSch10.setText(DeleteSchedule(10));
                tvHeadSch10.setText("Schedule " + 10);
                break;
            case R.id.delete11:
                scheduleVal11 = "";
                tvShowSch11.setText(DeleteSchedule(11));
                tvHeadSch11.setText("Schedule " + 11);
                break;
            case R.id.delete12:
                scheduleVal12 = "";
                tvShowSch12.setText(DeleteSchedule(12));
                tvHeadSch12.setText("Schedule " + 12);
                break;

        }
    }


    void setScheduler (String Sch){
        Intent intent = new Intent(this, ApSetScheduler.class);
        String schVal[] = {scheduleVal01,scheduleVal02,scheduleVal03,scheduleVal04,scheduleVal05,scheduleVal06,scheduleVal07,scheduleVal08,scheduleVal09,scheduleVal10,scheduleVal11,scheduleVal12,tempNode};
        JSONObject scheduleJson = new JSONObject();
        try {
            for(int i=0; i <12; i++ ) {
                scheduleJson.put(EspSchNode[i], schVal[i]);
            }
            scheduleJson.put("ST", tempNode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra(ApSetScheduler.EXTRA_SCHEDULE_JSON, scheduleJson.toString());
        intent.putExtra(ApSetScheduler.EXTRA_SCHEDULE, Sch);
        intent.putExtra(ApSetScheduler.EXTRA_SCHEDULE_URL, schURL);
        intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_ID, deviceId);
        intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_DESC, deviceName);
        startActivity(intent);
    }

    void getSchedulesFromPref() {
        scheduleVal01 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[0]);
        scheduleVal02 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[1]);
        scheduleVal03 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[2]);
        scheduleVal04 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[3]);
        scheduleVal05 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[4]);
        scheduleVal06 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[5]);
        scheduleVal07 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[6]);
        scheduleVal08 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[7]);
        scheduleVal09 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[8]);
        scheduleVal10 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[9]);
        scheduleVal11 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[10]);
        scheduleVal12 =  new Utility().getSharedPref(this,deviceId + "_schdl", EspSchNode[11]);
        tempNode      =  new Utility().getSharedPrefdef(this,deviceId + "_schdl","temp","");

        tvHeadSch1.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[0],"Schedule " + 1));
        tvHeadSch2.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[1],"Schedule " + 2));
        tvHeadSch3.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[2],"Schedule " + 3));
        tvHeadSch4.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[3],"Schedule " + 4));
        tvHeadSch5.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[4],"Schedule " + 5));
        tvHeadSch6.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[5],"Schedule " + 6));
        tvHeadSch7.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[6],"Schedule " + 7));
        tvHeadSch8.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[7],"Schedule " + 8));
        tvHeadSch9.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[8],"Schedule " + 9));
        tvHeadSch10.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[9],"Schedule " + 10));
        tvHeadSch11.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[10],"Schedule " + 11));
        tvHeadSch12.setText(new Utility().getSharedPrefdef(this,deviceId + "_schdl","Name_" +EspSchNode[11],"Schedule " + 12));

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
        SetOnClickListner ();
        spinner.setVisibility(View.GONE);
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

    private static  String deriveTxt( String  value) {
        String deriveVal = "";
        if (!value.isEmpty()) {
            deriveVal = "Power - " + value.substring(0, 1) + "\n" +
                    SetSchedulerActivity.redefineReverseAction(value.substring(2, 3)) + "\n" +
                    StationActivity.convert24Time(value.substring(4, 9)) +"\n"+ value.substring(10, 12) +"\n"+
                    SetSchedulerActivity.getDaysName(value.substring(13));
        }
        return deriveVal;
    }
    private  String  DeleteSchedule (int schNumber) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        final int SchN = schNumber;
        spinner.setVisibility(View.VISIBLE);
        String schVal[] = {scheduleVal01,scheduleVal02,scheduleVal03,scheduleVal04,scheduleVal05,scheduleVal06,scheduleVal07,scheduleVal08,scheduleVal09,scheduleVal10,scheduleVal11,scheduleVal12,tempNode};
        try {
            for(int i=0; i <12; i++ ) {
                req.put(EspSchNode[i], schVal[i]);
            }
            req.put("ST",tempNode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, schURL, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                        String  status = response.getString("status");
                            if (status.equals("ok")){
                                new Utility().setSharedPref(ScheduleApActivity.this, deviceId + "_schdl", new String[][]{{"Name_" + EspSchNode[SchN - 1], "Schedule " + SchN}, {EspSchNode[SchN - 1], ""}});
                                spinner.setVisibility(View.GONE);
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
return "";
    }
    public void onBackPressed(){
        Intent intent = new Intent(this,ApActivity.class);
        intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_ID, deviceId);
        intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_DESC, deviceName);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.temp_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_temp:
                setTemp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void setTemp (){
        Intent intent = new Intent(this, ApSetTemperature.class);
        String schVal[] = {scheduleVal01,scheduleVal02,scheduleVal03,scheduleVal04,scheduleVal05,scheduleVal06,scheduleVal07,scheduleVal08,scheduleVal09,scheduleVal10,scheduleVal11,scheduleVal12};
        JSONObject scheduleJson = new JSONObject();
        try {
            for(int i=0; i <12; i++ ) {
                scheduleJson.put(EspSchNode[i], schVal[i]);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra(ApSetTemperature.EXTRA_TEMP_JSON, scheduleJson.toString());
        intent.putExtra(ApSetScheduler.EXTRA_SCHEDULE_URL, schURL);
        intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_ID, deviceId);
        intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_DESC, deviceName);
        startActivity(intent);
    }
}