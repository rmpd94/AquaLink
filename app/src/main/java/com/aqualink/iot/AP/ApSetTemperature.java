package com.aqualink.iot.AP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.aqualink.iot.Station.AddEditDeviceActivity;
import com.aqualink.iot.Station.SetTemperature;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApSetTemperature extends AppCompatActivity  implements CompoundButton.OnCheckedChangeListener {
    Switch enableSwitch = null;
    Spinner temperature, type;
    private String code,spinTempVal,spinTypeVal;
    private ProgressBar spinner;
    private String text;
    private boolean switchOnOff;
    private String device_id, device_name,schURL,schJSONString;
    public static final String EXTRA_TEMP_JSON =  "com.aqualink.iot.EXTRA_TEMP_JSON";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_set_temperature);

        Intent intent1 =getIntent();
        schJSONString = intent1.getStringExtra(EXTRA_TEMP_JSON);
        device_id =intent1.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID);
        schURL = intent1.getStringExtra(ApSetScheduler.EXTRA_SCHEDULE_URL);
        device_name = intent1.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC);

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

        RelativeLayout layout = findViewById(R.id.set_temp_ap_root);
        spinner = new ProgressBar(ApSetTemperature.this, null, android.R.attr.progressBarStyleLarge);
        spinner.getIndeterminateDrawable().setTint(Color.parseColor("#FF0000"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(spinner, params);
        spinner.setVisibility(View.GONE);
        loadData();
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
        try {
            SaveTemp ();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private  void SaveTemp () throws JSONException {
        spinner.setVisibility(View.VISIBLE);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JSONObject req = new JSONObject (schJSONString);
        try {
            req.put("ST", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, schURL, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String   status = response.getString("status");
                            if (status.equals("ok")) {
                                saveData();
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(ApSetTemperature.this, "Temperature setting saved successfully", Toast.LENGTH_SHORT).show();
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
     void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(device_id + "_schdl", MODE_PRIVATE);
        text = sharedPreferences.getString("temp", "");
        switchOnOff = sharedPreferences.getBoolean("isEnable", false);
        UpdateFields(text);
    }
    void UpdateFields (String tempValue) {
        if (tempValue.isEmpty()) {

            temperature.setSelection(getIndex(temperature, "25"));
            type.setSelection(getIndex(type, "Down"));
            enableSwitch.setChecked(switchOnOff);
            enableSwitch.setOnCheckedChangeListener(this);
        } else{
            String Temp = tempValue.substring(0, 2);
            String Type = tempValue.substring(6);
            temperature.setSelection(getIndex(temperature, Temp));
            type.setSelection(getIndex(type, Type));
            enableSwitch.setChecked(switchOnOff);
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
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(device_id + "_schdl", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("temp", code);
        editor.putBoolean("isEnable", enableSwitch.isChecked());
        editor.apply();
    }
    public void onBackPressed(){
        Intent intent = new Intent(this, ScheduleApActivity.class);
        intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_ID, device_id);
        intent.putExtra(ApAddEditDevice.EXTRA_DEVICE_DESC, device_name);
        startActivity(intent);

    }
}