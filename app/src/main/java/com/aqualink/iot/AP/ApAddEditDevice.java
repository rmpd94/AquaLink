package com.aqualink.iot.AP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.aqualink.iot.R;

public class ApAddEditDevice extends AppCompatActivity {
    public static final String EXTRA_ID =  "com.aqualink.iot.EXTRA_ID";
    public static final String EXTRA_DEVICE_ID =  "com.aqualink.iot.DEVICE_ID";
    public static final String EXTRA_DEVICE_DESC =  "com.aqualink.iot.DEVICE_DESC";
    public static final String EXTRA_FB_HOST =  "com.aqualink.iot.FB_HOST";
    public static final String EXTRA_FB_AUTH =  "com.aqualink.iot.FB_AUTH";
    public static final String EXTRA_DEVICE_MODE =  "com.aqualink.iot.DEVICE_MODE";
    private EditText editTextDeviceId;
    private EditText editTextDeviceDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_add_edit_device);
        editTextDeviceId = findViewById(R.id.edit_text_device_id);
        editTextDeviceDescription = findViewById(R.id.edit_text_device_desc);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Device");
            editTextDeviceId.setText(intent.getStringExtra(EXTRA_DEVICE_ID));
            // editTextDeviceId.setFocusableInTouchMode(false);
            editTextDeviceDescription.setText(intent.getStringExtra(EXTRA_DEVICE_DESC));
        } else {
            setTitle("Add Device");
        }
    }
    private void saveNote() {
        String deviceId = editTextDeviceId.getText().toString();
        String deviceDescription = editTextDeviceDescription.getText().toString();
        if (deviceId.trim().isEmpty() || deviceDescription.trim().isEmpty()) {
            Toast.makeText(this, "Please Provide Device Name and Id", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_DEVICE_ID, deviceId);
        data.putExtra(EXTRA_DEVICE_DESC, deviceDescription);
        data.putExtra(EXTRA_FB_HOST, "");
        data.putExtra(EXTRA_FB_AUTH, "");
        data.putExtra(EXTRA_DEVICE_MODE, "AP");

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
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
}