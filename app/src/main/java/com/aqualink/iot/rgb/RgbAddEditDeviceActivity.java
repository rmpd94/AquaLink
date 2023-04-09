package com.aqualink.iot.rgb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.aqualink.iot.R;

public class RgbAddEditDeviceActivity extends AppCompatActivity {
    private EditText editTextDeviceId;
    private EditText editTextDeviceName;
    private EditText editTextPreviewTime;
    private EditText editTextIpAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgb_add_edit_device);

        editTextDeviceId = findViewById(R.id.rgb_device_id);
        editTextDeviceName = findViewById(R.id.rgb_device_name);
        editTextPreviewTime = findViewById(R.id.PreviewTime);
        editTextIpAddress = findViewById(R.id.IpAddress);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(RgbDeviceListActivity.EXTRA_ID)) {
            setTitle("Edit Device");
            editTextDeviceId.setText(intent.getStringExtra(RgbDeviceListActivity.EXTRA_DEVICE_ID));
            // editTextDeviceId.setFocusableInTouchMode(false);
            editTextDeviceName.setText(intent.getStringExtra(RgbDeviceListActivity.EXTRA_DEVICE_NAME));
            editTextPreviewTime.setText(intent.getStringExtra(RgbDeviceListActivity.EXTRA_FB_HOST));
            editTextIpAddress.setText(intent.getStringExtra(RgbDeviceListActivity.EXTRA_FB_AUTH));
        } else {
            setTitle("Add Device");
        }
    }
    private void saveNote() {
        String deviceId = editTextDeviceId.getText().toString();
        String deviceDescription = editTextDeviceName.getText().toString();
        String PreviewTime = editTextPreviewTime.getText().toString();
        String IpAddress = editTextIpAddress.getText().toString();
        if (deviceId.trim().isEmpty() || deviceDescription.trim().isEmpty() || PreviewTime.trim().isEmpty()
                || IpAddress.trim().isEmpty()) {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(RgbDeviceListActivity.EXTRA_DEVICE_ID, deviceId);
        data.putExtra(RgbDeviceListActivity.EXTRA_DEVICE_NAME, deviceDescription);
        data.putExtra(RgbDeviceListActivity.EXTRA_FB_HOST, PreviewTime);
        data.putExtra(RgbDeviceListActivity.EXTRA_FB_AUTH, IpAddress);
        data.putExtra(RgbDeviceListActivity.EXTRA_DEVICE_MODE, "LT");

        int id = getIntent().getIntExtra(RgbDeviceListActivity.EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(RgbDeviceListActivity.EXTRA_ID, id);
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