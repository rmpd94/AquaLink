package com.aqualink.iot.Station;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aqualink.iot.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SetTemperature extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    Switch enableSwitch = null;
    Spinner temperature, type;
    private String deviceId ,deviceName,fbHost,fbAuth,url,tempVal,code,spinTempVal,spinTypeVal,ExtraUdpIp;
    private ProgressBar spinner;
    public static final String EXTRA_TEMP_URL =  "com.aqualink.iot.EXTRA_TEMP_URL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_temperature);

        Intent intent = getIntent();
        deviceId = intent.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID);
        deviceName = intent.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC);
        fbHost = intent.getStringExtra(AddEditDeviceActivity.EXTRA_FB_HOST);
        fbAuth = intent.getStringExtra(AddEditDeviceActivity.EXTRA_FB_AUTH);
        ExtraUdpIp = intent.getStringExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST);
        url = intent.getStringExtra(EXTRA_TEMP_URL);

        enableSwitch = (Switch) findViewById(R.id.switch1);
        temperature = findViewById(R.id.spinnerTemp);
        type = findViewById(R.id.spinnerType);
        ArrayList<String> tempList = new ArrayList<>();
        ArrayList<String> typeList = new ArrayList<>();
        for(int i=10; i <51; i++ ){
            tempList.add(String.valueOf(i));
        }
        typeList.add("Up");
        typeList.add("Down");
        ArrayAdapter<String> tempArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tempList);
        tempArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        temperature.setAdapter(tempArrayAdapter);

        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,typeList);
        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeArrayAdapter);

        RelativeLayout layout = findViewById(R.id.set_temp_root);
        spinner = new ProgressBar(SetTemperature.this, null, android.R.attr.progressBarStyleLarge);
        spinner.getIndeterminateDrawable().setTint(Color.parseColor("#FF0000"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(spinner, params);

        UpdateFieldsServer ();
      //  enableSwitch.setOnCheckedChangeListener(this);
        temperature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinTempVal = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinTypeVal = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
           code = spinTempVal + ".00," + spinTypeVal;
        } else {
            code = "";
        }
        SaveTemp ();
    }

    private  void UpdateFieldsServer () {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tempVal = response.getString("ST");
                        //  String  netCheck = response.getString(StationActivity.firebase_net_c);
                         //   if (netCheck.equals("OK")){
                                UpdateFields (tempVal);
                                spinner.setVisibility(View.GONE);

                        //    }


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
    void UpdateFields (String tempValue) {
        if (tempValue.isEmpty()) {

            temperature.setSelection(getIndex(temperature, "25"));
            type.setSelection(getIndex(type, "Down"));
            enableSwitch.setChecked(false);
            enableSwitch.setOnCheckedChangeListener(this);
        } else{
            String Temp = tempValue.substring(0, 2);
            String Type = tempValue.substring(6);
            temperature.setSelection(getIndex(temperature, Temp));
            type.setSelection(getIndex(type, Type));
            enableSwitch.setChecked(true);
            enableSwitch.setOnCheckedChangeListener(this);

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
    private  void SaveTemp () {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject();
        try {
            req.put("ST", code);
            req.put("SS", "Y");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                         String   TempValue = response.getString("ST");
                            if (!TempValue.isEmpty()){
                              //  new Utility().setSharedPref(SetSchedulerActivity.this,ScheduleStationActivity.SHARED_PREFS, new String[][]{{device_id +ScheduleStationActivity.firebaseSchNode[mcodeShedule - 1], code}});
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(SetTemperature.this, "Temperature has been set successfully", Toast.LENGTH_SHORT).show();
                                //onBackPressed();
                            } else {
                                Toast.makeText(SetTemperature.this, "Temperature Control is disabled", Toast.LENGTH_SHORT).show();
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