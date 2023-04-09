package com.aqualink.iot.Station;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aqualink.iot.R;
import com.aqualink.iot.Utility;

public class SettingsStation extends AppCompatActivity implements View.OnClickListener {

    public static final String SHARED_PREFS_ST_GLOBAL = "sharedPrefsStglobal";
    public static final String  FirebaseUrl = "fbURL";
    public static final String  FirebaseAuth = "fbAuth";
    private EditText firebaseUrl;
    private EditText firebaseToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_station);

        setTitle("Default Firebase Settings"); //This Can be done in Manifest file
        Button buttonSave = findViewById(R.id.buttonSave);
        firebaseUrl = findViewById(R.id.firebase_url);
        firebaseToken = findViewById(R.id.firebase_auth);
        buttonSave.setOnClickListener(this);
        updateViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                saveData();
                Intent intent = new Intent(this, StationDeviceListActivity.class);
                startActivity(intent);
        }
    }

    private void updateViews() {

        firebaseUrl.setText(new Utility().getSharedPref(this,SHARED_PREFS_ST_GLOBAL,FirebaseUrl));
        firebaseToken.setText(new Utility().getSharedPref(this,SHARED_PREFS_ST_GLOBAL,FirebaseAuth));
    }
    private void saveData() {
        new Utility().setSharedPref(this,SHARED_PREFS_ST_GLOBAL,new String[][]{{FirebaseUrl, firebaseUrl.getText().toString()},{FirebaseAuth, firebaseToken.getText().toString()}});
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}
