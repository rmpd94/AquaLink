package com.aqualink.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;

public class DefaultMode extends AppCompatActivity implements View.OnClickListener  {
    Spinner spinnerDefaultScreen;
    Button  saveButton;
    private static final String SHARED_PREFS_DEFAULT_MODE = "sharedPrefsDefaultMode";
    private static final String default_val = "default_mode";
    private String default_mode_pref;
    private String spinner_selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_mode);
        spinnerDefaultScreen = findViewById(R.id.spinnerDefaultScreen);
        saveButton =findViewById(R.id.save_button_default);
        loadDataFromPref();

        ArrayList<String> DefaultModeList = new ArrayList<>();
        DefaultModeList.add(StartActivity.ApModeName);
        DefaultModeList.add(StartActivity.StationModeName);
        DefaultModeList.add(StartActivity.RGBModeName);
        DefaultModeList.add(StartActivity.NoModeName);
        ArrayAdapter<String> actionArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,DefaultModeList);
        actionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDefaultScreen.setAdapter(actionArrayAdapter);
        spinnerDefaultScreen.setSelection(getIndex(spinnerDefaultScreen,default_mode_pref));

        spinnerDefaultScreen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_selected = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        saveButton.setOnClickListener(this);
    }
    public void loadDataFromPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_DEFAULT_MODE, MODE_PRIVATE);
        default_mode_pref = sharedPreferences.getString(default_val, StartActivity.NoModeName);
    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button_default:
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_DEFAULT_MODE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(default_val,spinner_selected);
                editor.apply();
                Toast.makeText(this, "Saved. Please press back to return", Toast.LENGTH_SHORT).show();
        }
    }
}