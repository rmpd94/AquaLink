package com.aqualink.iot.rgb;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aqualink.iot.DefaultMode;
import com.aqualink.iot.R;
import com.aqualink.iot.StartActivity;
import com.aqualink.iot.Station.SettingsStation;
import com.aqualink.iot.database.Device;
import com.aqualink.iot.database.DeviceAdapter;
import com.aqualink.iot.database.DeviceViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Nullable;

import java.util.List;


public class RgbDeviceListActivity extends AppCompatActivity {
    private static final int ADD_DEVICE_REQUEST = 1;
    private static final int EDIT_DEVICE_REQUEST = 2;
    private DeviceViewModel deviceViewModel;
    private static final String SHARED_PREFS_DEFAULT_MODE = "sharedPrefsDefaultMode";
    private static final String default_val = "default_mode";
    private String default_mode_pref;
    public static final String EXTRA_ID =  "com.aqualink.iot.rgb.EXTRA_ID";
    public static final String EXTRA_DEVICE_ID =  "com.aqualink.iot.rgb.DEVICE_ID";
    public static final String EXTRA_DEVICE_NAME =  "com.aqualink.iot.rgb.DEVICE_NAME";
    public static final String EXTRA_FB_HOST =  "com.aqualink.iot.rgb.FB_HOST";
    public static final String EXTRA_FB_AUTH =  "com.aqualink.iot.rgb.FB_AUTH";
    public static final String EXTRA_DEVICE_MODE =  "com.aqualink.iot.rgb.DEVICE_MODE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgb_device_list);
        setTitle("Device List");
        FloatingActionButton buttonAddDevice = findViewById(R.id.rgb_button_add_device);
        buttonAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RgbDeviceListActivity.this, RgbAddEditDeviceActivity.class);
                startActivityForResult(intent, ADD_DEVICE_REQUEST);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.rgb_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final DeviceAdapter adapter = new DeviceAdapter();
        recyclerView.setAdapter(adapter);
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        deviceViewModel.getAllRGBDevices().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(@Nullable List<Device> devices) {adapter.submitList(devices);}
        });
        adapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Device device) {
               Intent intent = new Intent(RgbDeviceListActivity.this, RgbDeviceActivity.class);
                intent.putExtra(EXTRA_DEVICE_ID, device.getDeviceId());
                intent.putExtra(EXTRA_DEVICE_NAME, device.getDeviceName());
                intent.putExtra(EXTRA_FB_HOST, device.getFbHost());
                intent.putExtra(EXTRA_FB_AUTH, device.getFbAuth());
                startActivity(intent);
            }

            @Override
            public void onEditClick(Device device) {
                Intent intent = new Intent(RgbDeviceListActivity.this, RgbAddEditDeviceActivity.class);
                intent.putExtra(EXTRA_ID, device.getId());
                intent.putExtra(EXTRA_DEVICE_ID, device.getDeviceId());
                intent.putExtra(EXTRA_DEVICE_NAME, device.getDeviceName());
                intent.putExtra(EXTRA_FB_HOST, device.getFbHost());
                intent.putExtra(EXTRA_FB_AUTH, device.getFbAuth());
                intent.putExtra(EXTRA_DEVICE_MODE, device.getDeviceMode());
                startActivityForResult(intent, EDIT_DEVICE_REQUEST);
            }

            @Override
            public void onDeleteClick(final Device device) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RgbDeviceListActivity.this);
                builder.setTitle("Delete Device "+device.getDeviceName());
                builder.setMessage("Do you want to delete device "+device.getDeviceName()+ " ?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      /*  for(int i=0; i <12; i++ ) {
                            new Utility().removeSharedPref(RgbDeviceListActivity.this, device.getDeviceId()+ "_schdl",device.getDeviceId()+ ScheduleApActivity.EspSchNode[i]);
                        } */
                        deviceViewModel.delete(device);
                        Toast.makeText(getApplicationContext(), "Device Deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_DEVICE_REQUEST && resultCode == RESULT_OK) {
            String deviceId = data.getStringExtra(EXTRA_DEVICE_ID);
            String deviceName = data.getStringExtra(EXTRA_DEVICE_NAME);
            String fbHost = data.getStringExtra(EXTRA_FB_HOST);
            String fbAuth = data.getStringExtra(EXTRA_FB_AUTH);
            String deviceMode = data.getStringExtra(EXTRA_DEVICE_MODE);
            Device device = new Device(deviceId, deviceName,fbHost,fbAuth,deviceMode,null);
            deviceViewModel.insert(device);
            Toast.makeText(this, "Device saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_DEVICE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Device can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String deviceId = data.getStringExtra(EXTRA_DEVICE_ID);
            String deviceName = data.getStringExtra(EXTRA_DEVICE_NAME);
            String fbHost = data.getStringExtra(EXTRA_FB_HOST);
            String fbAuth = data.getStringExtra(EXTRA_FB_AUTH);
            String deviceMode = data.getStringExtra(EXTRA_DEVICE_MODE);
            Device device = new Device(deviceId, deviceName,fbHost,fbAuth,deviceMode,null);
            device.setId(id);
            deviceViewModel.update(device);
            Toast.makeText(this, "Device updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Changes not saved", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent = new Intent(this, SettingsStation.class);
                startActivity(intent);
                return true;
            case R.id.default_screen:
                Intent intent1 = new Intent(this, DefaultMode.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void onBackPressed(){
        loadDataFromPref();
        if (default_mode_pref.equals(StartActivity.RGBModeName)){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

        } else {
            Intent intent1 = new Intent(this, StartActivity.class);
            startActivity(intent1);
        }

    }
    public void loadDataFromPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_DEFAULT_MODE, MODE_PRIVATE);
        default_mode_pref = sharedPreferences.getString(default_val, StartActivity.NoModeName);
    }


    }
