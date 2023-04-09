package com.aqualink.iot.Station;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.aqualink.iot.R;
import com.aqualink.iot.Utility;

public class RenameButtons extends AppCompatActivity {

    private EditText eTpower1, eTpower2,eTpower3,eTpower4,eTpower5,eTpower6;
    private String deviceId,deviceName,fbHost,fbAuth,ExtraUdpIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_buttons);
        setTitle("Rename Buttons : " + deviceName);
        Intent intent = getIntent();
        deviceId = intent.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_ID);
        deviceName = intent.getStringExtra(AddEditDeviceActivity.EXTRA_DEVICE_DESC);
        fbHost = intent.getStringExtra(AddEditDeviceActivity.EXTRA_FB_HOST);
        fbAuth = intent.getStringExtra(AddEditDeviceActivity.EXTRA_FB_AUTH);
        ExtraUdpIp = intent.getStringExtra(AddEditDeviceActivity.EXTRA_LOCAL_HOST);

        eTpower1 = findViewById(R.id.power1);
        eTpower2 = findViewById(R.id.power2);
        eTpower3 = findViewById(R.id.power3);
        eTpower4 = findViewById(R.id.power4);
        eTpower5 = findViewById(R.id.power5);
        eTpower6 = findViewById(R.id.power6);
        eTpower1.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","1","btn1"));
        eTpower2.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","2","btn2"));
        eTpower3.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","3","btn3"));
        eTpower4.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","4","btn4"));
        eTpower5.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","5","btn5"));
        eTpower6.setText(new Utility().getSharedPrefdef(this, deviceId + "_buttons","6","btn6"));

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
              Save();
                Toast.makeText(this, "New Names Saved", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private  void Save () {
        String URL = "";
        String BtnNames = "";
        BtnNames = BtnNames.concat(eTpower1.getText().toString().trim());
        BtnNames = BtnNames.concat(","+ eTpower2.getText().toString().trim());
        BtnNames = BtnNames.concat(","+ eTpower3.getText().toString().trim());
        BtnNames = BtnNames.concat(","+ eTpower4.getText().toString().trim());
        BtnNames = BtnNames.concat(","+ eTpower5.getText().toString().trim());
        BtnNames = BtnNames.concat(","+ eTpower6.getText().toString().trim());

        URL = "https://" + fbHost + "/"+ deviceId + "/powers.json?auth=" + fbAuth;
       // Toast.makeText(this, URL, Toast.LENGTH_SHORT).show();
        new Utility().voleySendHttpRequest(this, URL, "PATCH", new String[][]{{"btnNm", BtnNames}});
        new Utility().setSharedPref(this,deviceId + "_buttons", new String[][]{{"1", eTpower1.getText().toString().trim()},{"2", eTpower2.getText().toString().trim()},{"3", eTpower3.getText().toString().trim()},{"4", eTpower4.getText().toString().trim()},{"5", eTpower5.getText().toString().trim()},{"6", eTpower6.getText().toString().trim()}});
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
