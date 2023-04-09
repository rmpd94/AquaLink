package com.aqualink.iot.Station;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.util.ArrayList;

public class SetSchedulerActivity extends AppCompatActivity implements View.OnClickListener  {
    TimePicker timePicker;
    Spinner spinnerPower,spinnerAction,spinnerDuration;
    TextView textViewTime;
    TextView textViewAction;
    TextView textViewPower;
    EditText TxtSchName;
    private ProgressBar spinner;
    public static final String EXTRA_SCHEDULE =  "com.aqualink.iot.EXTRA_SCHEDULE";
    public static final String EXTRA_SCHEDULE_URL =  "com.aqualink.iot.EXTRA_SCHEDULE_URL";
    private String schURL;
    private String hour;
    private String minute;
    private String action;
    private String power,duration;
    private String device_id, device_name,fbHost,fbAuth,ExtraUdpIp;
    private int mcodeShedule;
    private String scheduleVal;
    private String code;
    Button Sunday, Monday,Tuesday,Wednesday,Thursday,Friday,Saturday;
    private String[] Days = { "Sunday", "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private String Selected = "Selected";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_scheduler);
        Intent intent1 =getIntent();
        String mcode = intent1.getStringExtra(EXTRA_SCHEDULE);
        device_id =intent1.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID);
        schURL = intent1.getStringExtra(EXTRA_SCHEDULE_URL);
        device_name = intent1.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC);
        fbAuth = intent1.getStringExtra(AddEditDeviceActivity.EXTRA_FB_AUTH);
        fbHost = intent1.getStringExtra(AddEditDeviceActivity.EXTRA_FB_HOST);
        ExtraUdpIp = intent1.getStringExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST);
        assert mcode != null;
        mcodeShedule = Integer.parseInt(mcode.substring(1, 3));
        setTitle(device_name +" : Schedule" + mcodeShedule);
        textViewTime= findViewById(R.id.textViewTime);
        textViewAction=findViewById(R.id.textViewAction);
        textViewPower=findViewById(R.id.textViewPower);
        spinnerPower = findViewById(R.id.spinnerPower);
        spinnerAction = findViewById(R.id.spinnerAction);
        spinnerDuration = findViewById(R.id.spinnerDuration);
        timePicker =  findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);
        TxtSchName =  findViewById(R.id.editTxtSchName);
        TxtSchName.setText(new Utility().getSharedPrefdef(this,device_id + "_schdl",ScheduleStationActivity.firebaseSchNode[mcodeShedule - 1],"Schedule " + mcodeShedule));

        setOnClickForDays ();
        RelativeLayout layout = findViewById(R.id.set_schedule_root);
        spinner = new ProgressBar(SetSchedulerActivity.this, null, android.R.attr.progressBarStyleLarge);
        spinner.getIndeterminateDrawable().setTint(Color.parseColor("#FF0000"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(spinner, params);
        spinner.setVisibility(View.GONE);

        ArrayList<String> actionList = new ArrayList<>();
        ArrayList<String> powerList = new ArrayList<>();
        ArrayList<String> durationList = new ArrayList<>();
        durationList.add("--");
        for(int i=1; i <60; i++ ){
            durationList.add(String.format("%02d", i));
        }
        actionList.add("ON");
        actionList.add("OFF");
        powerList.add(new Utility().getSharedPrefdef(this, device_id + "_buttons","1","Power1"));
        powerList.add(new Utility().getSharedPrefdef(this, device_id + "_buttons","2","Power2"));
        powerList.add(new Utility().getSharedPrefdef(this, device_id + "_buttons","3","Power3"));
        powerList.add(new Utility().getSharedPrefdef(this, device_id + "_buttons","4","Power4"));
        powerList.add(new Utility().getSharedPrefdef(this, device_id + "_buttons","5","Power5"));
        powerList.add(new Utility().getSharedPrefdef(this, device_id + "_buttons","6","Power6"));
        ArrayAdapter<String> actionArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,actionList);
        actionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAction.setAdapter(actionArrayAdapter);
        ArrayAdapter<String> powerArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,powerList);
        powerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPower.setAdapter(powerArrayAdapter);
        ArrayAdapter<String> durationArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,durationList);
        durationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuration.setAdapter(durationArrayAdapter);

        if (mcode.length() >4) {
            spinnerPower.setSelection(getIndex(spinnerPower,new Utility().getSharedPrefdef(this, device_id + "_buttons",mcode.substring(4, 5),"Power"+mcode.substring(4, 5)) ));
            spinnerDuration.setSelection(getIndex(spinnerDuration, mcode.substring(14, 16)));
            spinnerAction.setSelection(getIndex(spinnerAction, redefineReverseAction(mcode.substring(6, 7))));
            timePicker.setHour(Integer.parseInt(mcode.substring(8, 10)));
            timePicker.setMinute(Integer.parseInt(mcode.substring(11, 13)));
            updateDaysButtons(mcode.substring(17));
        }
        spinnerAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                action = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        spinnerPower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                power = String.valueOf(position + 1);
                // power = parent.getItemAtPosition(position).toString();
                //Toast.makeText(SetSchedulerActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                duration = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });



    }
    public String pad(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }
    public static String redefineAction(String input) {
        if (input.equals("OFF")) {
            return "0";
        } else {
            return "1";
        }
    }
    public static String redefineReverseAction(String input) {
        if (input.equals("0")) {
            return "OFF";
        } else {
            return "ON";
        }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
    public void onBackPressed(){
        Intent intent = new Intent(this, ScheduleStationActivity.class);
        intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID, device_id);
        intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC, device_name);
        intent.putExtra(AddEditDeviceActivity.EXTRA_FB_HOST, fbHost);
        intent.putExtra(AddEditDeviceActivity.EXTRA_FB_AUTH, fbAuth);
        intent.putExtra(SetSchedulerActivity.EXTRA_SCHEDULE_URL, schURL);
        intent.putExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST, ExtraUdpIp);
        startActivity(intent);

    }
    private  void SaveSchedule () {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        try {
            req.put(ScheduleStationActivity.firebaseSchNode[mcodeShedule - 1], code);
            req.put("SS", "Y");
            } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, schURL, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            scheduleVal = response.getString(ScheduleStationActivity.firebaseSchNode[mcodeShedule - 1]);
                            if (!scheduleVal.isEmpty()){
                                new Utility().setSharedPref(SetSchedulerActivity.this,ScheduleStationActivity.SHARED_PREFS, new String[][]{{device_id +ScheduleStationActivity.firebaseSchNode[mcodeShedule - 1], code}});
                                new Utility().setSharedPref(SetSchedulerActivity.this,device_id + "_schdl", new String[][]{{ScheduleStationActivity.firebaseSchNode[mcodeShedule - 1], TxtSchName.getText().toString().trim()}});
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(SetSchedulerActivity.this, "Schedule: "+ mcodeShedule + " Saved", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(SetSchedulerActivity.this, "Please Check your Internet Connection/Device Settings", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sunday:
                if(Sunday.getTooltipText().equals(Days[0])){
                    Sunday.setTooltipText(Selected);
                    Sunday.setBackgroundColor(Color.parseColor("#3949AB"));
                } else
                {
                    Sunday.setTooltipText(Days[0]);
                    Sunday.setBackgroundColor(Color.parseColor("#F3E1EEF4"));
                }
                break;
            case R.id.monday:
                if(Monday.getTooltipText().equals(Days[1])){
                    Monday.setTooltipText(Selected);
                    Monday.setBackgroundColor(Color.parseColor("#3949AB"));
                } else
                {
                    Monday.setTooltipText(Days[1]);
                    Monday.setBackgroundColor(Color.parseColor("#F3E1EEF4"));
                }
                break;
            case R.id.tuesday:
                if(Tuesday.getTooltipText().equals(Days[2])){
                    Tuesday.setTooltipText(Selected);
                    Tuesday.setBackgroundColor(Color.parseColor("#3949AB"));
                } else
                {
                    Tuesday.setTooltipText(Days[2]);
                    Tuesday.setBackgroundColor(Color.parseColor("#F3E1EEF4"));
                }
                break;
            case R.id.wednesday:
                if(Wednesday.getTooltipText().equals(Days[3])){
                    Wednesday.setTooltipText(Selected);
                    Wednesday.setBackgroundColor(Color.parseColor("#3949AB"));
                } else
                {
                    Wednesday.setTooltipText(Days[3]);
                    Wednesday.setBackgroundColor(Color.parseColor("#F3E1EEF4"));
                }
                break;
            case R.id.thursday:
                if(Thursday.getTooltipText().equals(Days[4])){
                    Thursday.setTooltipText(Selected);
                    Thursday.setBackgroundColor(Color.parseColor("#3949AB"));
                } else
                {
                    Thursday.setTooltipText(Days[4]);
                    Thursday.setBackgroundColor(Color.parseColor("#F3E1EEF4"));
                }
                break;
            case R.id.friday:
                if(Friday.getTooltipText().equals(Days[5])){
                    Friday.setTooltipText(Selected);
                    Friday.setBackgroundColor(Color.parseColor("#3949AB"));
                } else
                {
                    Friday.setTooltipText(Days[5]);
                    Friday.setBackgroundColor(Color.parseColor("#F3E1EEF4"));
                }
                break;
            case R.id.saturday:
                if(Saturday.getTooltipText().equals(Days[6])){
                    Saturday.setTooltipText(Selected);
                    Saturday.setBackgroundColor(Color.parseColor("#3949AB"));
                } else
                {
                    Saturday.setTooltipText(Days[6]);
                    Saturday.setBackgroundColor(Color.parseColor("#F3E1EEF4"));
                }
                break;
        }
    }
    void setOnClickForDays (){
        Sunday = findViewById(R.id.sunday);
        Monday = findViewById(R.id.monday);
        Tuesday = findViewById(R.id.tuesday);
        Wednesday = findViewById(R.id.wednesday);
        Thursday = findViewById(R.id.thursday);
        Friday = findViewById(R.id.friday);
        Saturday = findViewById(R.id.saturday);

        Sunday.setOnClickListener(this);
        Monday.setOnClickListener(this);
        Tuesday.setOnClickListener(this);
        Wednesday.setOnClickListener(this);
        Thursday.setOnClickListener(this);
        Friday.setOnClickListener(this);
        Saturday.setOnClickListener(this);
    }
    private String getDaysList(){
        StringBuilder days = new StringBuilder();
        if (Sunday.getTooltipText().equals(Selected)&&
                Monday.getTooltipText().equals(Selected) &&
                Tuesday.getTooltipText().equals(Selected) &&
                Wednesday.getTooltipText().equals(Selected)
        && Thursday.getTooltipText().equals(Selected)
        && Friday.getTooltipText().equals(Selected)
        && Saturday.getTooltipText().equals(Selected)){
            days.append("7");
        } else {


            if (Sunday.getTooltipText().equals(Selected)) {
                days.append("0,");
            }
            if (Monday.getTooltipText().equals(Selected)) {
                days.append("1,");
            }
            if (Tuesday.getTooltipText().equals(Selected)) {
                days.append("2,");
            }
            if (Wednesday.getTooltipText().equals(Selected)) {
                days.append("3,");
            }
            if (Thursday.getTooltipText().equals(Selected)) {
                days.append("4,");
            }
            if (Friday.getTooltipText().equals(Selected)) {
                days.append("5,");
            }
            if (Saturday.getTooltipText().equals(Selected)) {
                days.append("6");
            }
        }
        return  days.toString();
    }
    public static String getDaysName(String pwdays){
        StringBuilder wdays = new StringBuilder();
        if(!pwdays.isEmpty()){
            if (pwdays.contains("7")){
                wdays.append("Sun,Mon,Tue,Wed,Thu,Fri,Sat");
            } else {
                if (pwdays.contains("0")) {
                    wdays.append("Sun,");
                }
                if (pwdays.contains("1")) {
                    wdays.append("Mon,");
                }
                if (pwdays.contains("2")) {
                    wdays.append("Tue,");
                }
                if (pwdays.contains("3")) {
                    wdays.append("Wed,");
                }
                if (pwdays.contains("4")) {
                    wdays.append("Thu,");
                }
                if (pwdays.contains("5")) {
                    wdays.append("Fri,");
                }
                if (pwdays.contains("6")) {
                    wdays.append("Sat");
                }
            }
        return wdays.toString();
        } else {
            return "";
        }

    }
    void updateDaysButtons(String extraDays){
        if (extraDays.contains("7")){
            Sunday.setTooltipText(Selected);
            Sunday.setBackgroundColor(Color.parseColor("#3949AB"));
            Monday.setTooltipText(Selected);
            Monday.setBackgroundColor(Color.parseColor("#3949AB"));
            Tuesday.setTooltipText(Selected);
            Tuesday.setBackgroundColor(Color.parseColor("#3949AB"));
            Wednesday.setTooltipText(Selected);
            Wednesday.setBackgroundColor(Color.parseColor("#3949AB"));
            Thursday.setTooltipText(Selected);
            Thursday.setBackgroundColor(Color.parseColor("#3949AB"));
            Friday.setTooltipText(Selected);
            Friday.setBackgroundColor(Color.parseColor("#3949AB"));
            Saturday.setTooltipText(Selected);
            Saturday.setBackgroundColor(Color.parseColor("#3949AB"));
        }
        else {
            if (extraDays.contains("0")) {
                Sunday.setTooltipText(Selected);
                Sunday.setBackgroundColor(Color.parseColor("#3949AB"));
            }

            if (extraDays.contains("1")) {
                Monday.setTooltipText(Selected);
                Monday.setBackgroundColor(Color.parseColor("#3949AB"));
            }

            if (extraDays.contains("2")) {
                Tuesday.setTooltipText(Selected);
                Tuesday.setBackgroundColor(Color.parseColor("#3949AB"));
            }

            if (extraDays.contains("3")) {
                Wednesday.setTooltipText(Selected);
                Wednesday.setBackgroundColor(Color.parseColor("#3949AB"));
            }

            if (extraDays.contains("4")) {
                Thursday.setTooltipText(Selected);
                Thursday.setBackgroundColor(Color.parseColor("#3949AB"));
            }

            if (extraDays.contains("5")) {
                Friday.setTooltipText(Selected);
                Friday.setBackgroundColor(Color.parseColor("#3949AB"));
            }

            if (extraDays.contains("6")) {
                Saturday.setTooltipText(Selected);
                Saturday.setBackgroundColor(Color.parseColor("#3949AB"));
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveSch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveSch()  {
        hour = pad(timePicker.getHour());
        minute = pad(timePicker.getMinute());

        code = power+":"+ redefineAction(action)+":"+hour+":"+minute+":"+duration+":"+getDaysList();
        if (getDaysName(getDaysList()).isEmpty()){
            Toast.makeText(SetSchedulerActivity.this, "Please Select At least One Day", Toast.LENGTH_SHORT).show();
        } else {
            spinner.setVisibility(View.VISIBLE);
            SaveSchedule ();
        }

          /*      try {
                    //set time in mili
                    Thread.sleep(2000);

                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(SetSchedulerActivity.this,ScheduleStationActivity.class);
                intent.putExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID, device_id);
                startActivity(intent); */
    }
}
