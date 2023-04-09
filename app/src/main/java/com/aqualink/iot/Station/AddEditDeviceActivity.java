package com.aqualink.iot.Station;


import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aqualink.iot.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AddEditDeviceActivity extends AppCompatActivity {
    public static final String EXTRA_ID =  "com.aqualink.iot.EXTRA_ID";
    public static final String EXTRA_DEVICE_ID =  "com.aqualink.iot.DEVICE_ID";
    public static final String EXTRA_DEVICE_DESC =  "com.aqualink.iot.DEVICE_DESC";
    public static final String EXTRA_FB_HOST =  "com.aqualink.iot.FB_HOST";
    public static final String EXTRA_FB_AUTH =  "com.aqualink.iot.FB_AUTH";
    public static final String EXTRA_DEVICE_MODE =  "com.aqualink.iot.DEVICE_MODE";
    public static final String EXTRA_LOCAL_HOST =  "com.aqualink.iot.LOCAL_HOST";
    private EditText editTextDeviceId,editTextDeviceDescription, editTextFbHost,editTextFbAuth,editWifiSSID, editLocalHost;
    private String isNew,deviceId, deviceDescription,fbHost,fbAuth,wifiSSID, localHost;
    private ProgressBar spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_device);
        editTextDeviceId = findViewById(R.id.edit_text_device_id);
        editTextDeviceDescription = findViewById(R.id.edit_text_device_desc);
        editTextFbHost = findViewById(R.id.edit_text_fb_host);
        editTextFbAuth = findViewById(R.id.edit_text_fb_auth);
        editWifiSSID = findViewById(R.id.edit_text_wifi_ssid);
        editLocalHost = findViewById(R.id.edit_localHost);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Device");
            editTextDeviceId.setText(intent.getStringExtra(EXTRA_DEVICE_ID));
           // disableEditText(editWifiSSID);
            //disableEditText(editLocalHost);
            editTextDeviceDescription.setText(intent.getStringExtra(EXTRA_DEVICE_DESC));
            editLocalHost.setText(intent.getStringExtra(EXTRA_LOCAL_HOST));
            if (intent.getStringExtra(EXTRA_FB_HOST).isEmpty()){
                editTextFbHost.setText(intent.getStringExtra(EXTRA_FB_HOST));
            } else{
            editTextFbHost.setText("https://"+ intent.getStringExtra(EXTRA_FB_HOST) +"/");
            editTextFbAuth.setText(intent.getStringExtra(EXTRA_FB_AUTH)); }
            isNew = "N";
        } else {
            isNew = "Y";
            setTitle("Add Device");
        }
        RelativeLayout layout = findViewById(R.id.add_edit_device_root);
        spinner = new ProgressBar(AddEditDeviceActivity.this, null, android.R.attr.progressBarStyleLarge);
        spinner.getIndeterminateDrawable().setTint(Color.parseColor("#FF0000"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(spinner, params);
        spinner.setVisibility(View.GONE);
    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.GRAY);
        editText.setFocusableInTouchMode(false);

    }
    private void saveNote() {
         deviceId = editTextDeviceId.getText().toString().trim();
         deviceDescription = editTextDeviceDescription.getText().toString().trim();
         fbHost = editTextFbHost.getText().toString().trim();
         fbHost = fbHost.replace("https://","");
         fbHost = fbHost.replace("/","");
         fbAuth = editTextFbAuth.getText().toString().trim();
         wifiSSID = editWifiSSID.getText().toString().trim();
         localHost = editLocalHost.getText().toString().trim();
        if (deviceId.trim().isEmpty() || deviceDescription.trim().isEmpty()) {
            Toast.makeText(this, "Please Provide Device Name and Id", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isNew.equalsIgnoreCase("Y")){
            if (localHost.isEmpty()) {
                Toast.makeText(this, "Please Enter Local Host", Toast.LENGTH_SHORT).show();
                return;
            }
            newIntent ();
           // SaveData();
        } else {
            newIntent ();
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
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private  void SaveData () {
        String url = "http://192.168.4.1/settings";
        RequestQueue mQueue = Volley.newRequestQueue(this);
        spinner.setVisibility(View.VISIBLE);
        JSONObject req = new JSONObject();
        try {
            req.put("ssid", wifiSSID);
            req.put("password", localHost);
            req.put("firebaseHost", fbHost);
            req.put("firebaseAuth", fbAuth);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = req.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url ,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(AddEditDeviceActivity.this, response, Toast.LENGTH_SHORT).show();
                newIntent ();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {

                    return null;
                }
            }

        };
        mQueue.add(stringRequest);

    }
    private  void newIntent (){
        Intent data = new Intent();
        data.putExtra(EXTRA_DEVICE_ID, deviceId);
        data.putExtra(EXTRA_DEVICE_DESC, deviceDescription);
        data.putExtra(EXTRA_FB_HOST, fbHost);
        data.putExtra(EXTRA_FB_AUTH, fbAuth);
        data.putExtra(EXTRA_DEVICE_MODE, "ST");
        data.putExtra(EXTRA_LOCAL_HOST, localHost);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();

    }

}
