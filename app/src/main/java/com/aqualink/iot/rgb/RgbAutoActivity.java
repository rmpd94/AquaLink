package com.aqualink.iot.rgb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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

public class RgbAutoActivity extends Fragment {
    Button btnStartRGB;
    Button btnPeakRGB;
    Button btnSetTime;
    Button btnSaveTime;
    Button btnDemo;
    TextView textStartRGB;
    TextView textPeakRGB;
    TextView textSetTime;
    private static final String startRGBTitle = "Select start intensity";
    private static final String peakRGBTitle = "Select peak intensity";
    private static final String timingsTitle = "Select timings (Minutes)";
    private static final int hourFactor = 60000;
    private String p[] ={"100","100","100","100","0","0","0","0","120","120","480"} ;
    //{maxRed,maxGreen,maxBlue,maxWhite,minRed,minGreen,minBlue,minWhite,rampTime,slowTime,totalTime}
    public RgbAutoActivity() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_rgb_auto, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        textStartRGB =(TextView) view.findViewById(R.id.textStartRGB);
        textPeakRGB =(TextView) view.findViewById(R.id.textPeakRGB);
        textSetTime =(TextView) view.findViewById(R.id.textSetTime);

        btnStartRGB =(Button) view.findViewById(R.id.btnStartRGB);
        btnPeakRGB =(Button) view.findViewById(R.id.btnPeakRGB);
        btnSetTime =(Button) view.findViewById(R.id.btnSetTime);
        btnSaveTime =(Button) view.findViewById(R.id.btnSaveTime);
        btnDemo =(Button) view.findViewById(R.id.btnDemo);
        setElementsFocusable(false);
        getTimeHttpReq();
        setBtnListener();

    } //end on view created
    private void setElementsFocusable(Boolean input){
        btnStartRGB.setEnabled(input);
        btnPeakRGB.setEnabled(input);
        btnSetTime.setEnabled(input);
        btnSaveTime.setEnabled(input);
        btnDemo.setEnabled(input);
    }
    private void setBtnListener(){
        //Starting Intensity set button
        btnStartRGB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowRGBDialog(textStartRGB,startRGBTitle,"S");
            }
        });
        //Peak Intensity set button
        btnPeakRGB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowRGBDialog(textPeakRGB,peakRGBTitle,"P");
            }
        });
        //Timing set button
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowTimeDialog(textSetTime,timingsTitle);
            }
        });
        //Save button
        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

if(validateData()) {
    String[][] payload;
    String value =
            RgbManualActivity.SetTransformValue(p[0]) + "," + RgbManualActivity.SetTransformValue(p[1]) + "," + RgbManualActivity.SetTransformValue(p[2]) + "," + RgbManualActivity.SetTransformValue(p[3]) + "," +
                    RgbManualActivity.SetTransformValue(p[4]) + "," + RgbManualActivity.SetTransformValue(p[5]) + "," + RgbManualActivity.SetTransformValue(p[6]) + "," + RgbManualActivity.SetTransformValue(p[7]) + "," +
                    Integer.valueOf(p[8]) * hourFactor + "," + Integer.valueOf(p[9]) * hourFactor + "," + Integer.valueOf(p[10]) * hourFactor;
    payload = new String[][]{{"atsetup", value}};
    new Utility().voleySendHttpRequest(getActivity(), RgbDeviceActivity.host + RgbDeviceActivity.saveAutStngsUrl, "POST", payload);
    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
}
            }
        });
        //Demo button
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!RgbDeviceActivity.isManMode()){
                    if(validateData()) {
                        String[][] payload;
                        String value =
                                RgbManualActivity.SetTransformValue(p[0]) + "," + RgbManualActivity.SetTransformValue(p[1]) + "," + RgbManualActivity.SetTransformValue(p[2]) + "," + RgbManualActivity.SetTransformValue(p[3]) + "," +
                                        RgbManualActivity.SetTransformValue(p[4]) + "," + RgbManualActivity.SetTransformValue(p[5]) + "," + RgbManualActivity.SetTransformValue(p[6]) + "," + RgbManualActivity.SetTransformValue(p[7]) + "," +
                                        Integer.valueOf(p[8]) * hourFactor + "," + Integer.valueOf(p[9]) * hourFactor + "," + Integer.valueOf(p[10]) * hourFactor+ "," + RgbDeviceActivity.previewTime;
                        payload = new String[][]{{"demo", value}};
                        new Utility().voleySendHttpRequest(getActivity(), RgbDeviceActivity.host + RgbDeviceActivity.showDemoUrl, "POST", payload);
                        Toast.makeText(getContext(), "Preview Started", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(),"Please change the mode to Manual to see Preview", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void ShowRGBDialog(TextView test,String title, String type){
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getContext());

        final SeekBar red = new SeekBar(getContext());
        final SeekBar green = new SeekBar(getContext());
        final SeekBar blue = new SeekBar(getContext());
        final SeekBar white = new SeekBar(getContext());
        final TextView rgbText = new TextView(getContext());

       // red.setScaleY(3);
        red.setProgressTintList(ColorStateList.valueOf(Color.RED));
        red.setThumbTintList(ColorStateList.valueOf(Color.RED));
        red.setPadding(50,80,50,50);


       // green.setScaleY(3);
        green.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        green.setThumbTintList(ColorStateList.valueOf(Color.GREEN));
        green.setPadding(50,0,50,50);

      //  blue.setScaleY(3);
        blue.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
        blue.setThumbTintList(ColorStateList.valueOf(Color.BLUE));
        blue.setPadding(50,0,50,50);

        //white.setScaleY(3);
        white.setProgressTintList(ColorStateList.valueOf(Color.GRAY));
        white.setThumbTintList(ColorStateList.valueOf(Color.GRAY));
        white.setPadding(50,0,50,50);

        red.setMax(100);
        green.setMax(100);
        blue.setMax(100);
        white.setMax(100);
        red.setKeyProgressIncrement(1);
        green.setKeyProgressIncrement(1);
        blue.setKeyProgressIncrement(1);
        white.setKeyProgressIncrement(1);
        rgbText.setTextSize(15);
        rgbText.setGravity(Gravity.CENTER);
        LinearLayout ll=new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);
        ll.addView(red);
        ll.addView(green);
        ll.addView(blue);
        ll.addView(white);
        ll.addView(rgbText);
        class getText {
            void  RGB()
            {
                rgbText.setText("R:"+red.getProgress()+"%, "+"G:"+green.getProgress()+"%, "+"B:"+blue.getProgress()+"%, "+"W:"+white.getProgress()+"%");
            }
        }

        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){new getText().RGB();}
            public void onStartTrackingTouch(SeekBar arg0) {}
            public void onStopTrackingTouch(SeekBar seekBar) {new getText().RGB();}
        });
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){new getText().RGB();}
            public void onStartTrackingTouch(SeekBar arg0) {}
            public void onStopTrackingTouch(SeekBar seekBar) {new getText().RGB();}
        });
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){new getText().RGB();}
            public void onStartTrackingTouch(SeekBar arg0) {}
            public void onStopTrackingTouch(SeekBar seekBar) {new getText().RGB();}
        });
        white.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){new getText().RGB();}
            public void onStartTrackingTouch(SeekBar arg0) {}
            public void onStopTrackingTouch(SeekBar seekBar) {new getText().RGB();}
        });
        popDialog.setTitle(title);
        popDialog.setView(ll);
        if (type.equals("S")){
            red.setProgress(Integer.valueOf(p[4]));
            green.setProgress(Integer.valueOf(p[5]));
            blue.setProgress(Integer.valueOf(p[6]));
            white.setProgress(Integer.valueOf(p[7]));

        } else if (type.equals("P")){
            red.setProgress(Integer.valueOf(p[0]));
            green.setProgress(Integer.valueOf(p[1]));
            blue.setProgress(Integer.valueOf(p[2]));
            white.setProgress(Integer.valueOf(p[3]));
        }
        new getText().RGB();
// Button OK
        popDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String value = "R: "+red.getProgress()+", G: "+green.getProgress()+", B: "+blue.getProgress()+", W: "+white.getProgress();
                        test.setText(value);
                        if (type.equals("S")){
                            p[4]= String.valueOf(red.getProgress());
                            p[5]= String.valueOf(green.getProgress());
                            p[6]= String.valueOf(blue.getProgress());
                            p[7]= String.valueOf(white.getProgress());
                        } else if (type.equals("P")){
                            p[0]= String.valueOf(red.getProgress());
                            p[1]= String.valueOf(green.getProgress());
                            p[2]= String.valueOf(blue.getProgress());
                            p[3]= String.valueOf(white.getProgress());
                        }
                        dialog.dismiss();
                    }
                });
        popDialog.create();
        popDialog.show();
    }

    public void ShowTimeDialog(TextView test,String title){
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getContext());

        final SeekBar total = new SeekBar(getContext());
        final SeekBar sunRise = new SeekBar(getContext());
        final SeekBar sunSet = new SeekBar(getContext());
        final TextView timeText = new TextView(getContext());
        final TextView totalText = new TextView(getContext());
        final TextView sunRiseText = new TextView(getContext());
        final TextView sunSetText = new TextView(getContext());
        // red.setScaleY(3);
        total.setProgressTintList(ColorStateList.valueOf(Color.RED));
        total.setThumbTintList(ColorStateList.valueOf(Color.RED));
        total.setPadding(50,80,50,50);


        // green.setScaleY(3);
        sunRise.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        sunRise.setThumbTintList(ColorStateList.valueOf(Color.GREEN));
        sunRise.setPadding(50,0,50,50);

        //  blue.setScaleY(3);
        sunSet.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
        sunSet.setThumbTintList(ColorStateList.valueOf(Color.BLUE));
        sunSet.setPadding(50,0,50,50);


        total.setMax(720);
        sunRise.setMax(720);
        sunSet.setMax(720);
        total.setKeyProgressIncrement(1);
        sunRise.setKeyProgressIncrement(1);
        sunSet.setKeyProgressIncrement(1);
        timeText.setTextSize(15);
        timeText.setGravity(Gravity.CENTER);
        totalText.setGravity(Gravity.LEFT);
        //totalText.setText("Total time (Hr)");
        totalText.setTextSize(8);
        sunRiseText.setGravity(Gravity.LEFT);
        //totalText.setText("Sunrise time (Hr)");
        sunRiseText.setTextSize(8);
        sunSetText.setGravity(Gravity.LEFT);
        //totalText.setText("Sunset time (Hr)");
        sunSetText.setTextSize(8);

        LinearLayout ll=new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);
        ll.addView(totalText);
        ll.addView(total);
        ll.addView(sunRiseText);
        ll.addView(sunRise);
        ll.addView(sunSetText);
        ll.addView(sunSet);
        ll.addView(timeText);
        class getText {
            void  time()
            {
                timeText.setText("Total:"+total.getProgress()+", "+"SunRise:"+sunRise.getProgress()+", "+"SunSet:"+sunSet.getProgress());
            }
        }

        total.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){new getText().time();}
            public void onStartTrackingTouch(SeekBar arg0) {}
            public void onStopTrackingTouch(SeekBar seekBar) {new getText().time();}
        });
        sunRise.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){new getText().time();}
            public void onStartTrackingTouch(SeekBar arg0) {}
            public void onStopTrackingTouch(SeekBar seekBar) {new getText().time();}
        });
        sunSet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){new getText().time();}
            public void onStartTrackingTouch(SeekBar arg0) {}
            public void onStopTrackingTouch(SeekBar seekBar) {new getText().time();}
        });
        popDialog.setTitle(title);
        popDialog.setView(ll);

            sunRise.setProgress(Integer.valueOf(p[8]));
            sunSet.setProgress(Integer.valueOf(p[9]));
            total.setProgress(Integer.valueOf(p[10]));
        new getText().time();

// Button OK
        popDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String value = "Total: "+total.getProgress()+", SunRise: "+sunRise.getProgress()+", SunSet: "+sunSet.getProgress();
                            test.setText(value);
                            p[8]= String.valueOf(sunRise.getProgress());
                            p[9]= String.valueOf(sunSet.getProgress());
                            p[10]= String.valueOf(total.getProgress());
                        dialog.dismiss();
                    }
                });
        popDialog.create();
        popDialog.show();
    }
    private void getTimeHttpReq() {

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, RgbDeviceActivity.host+RgbDeviceActivity.getAutoUrl, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            p[0] =RgbManualActivity.getTransformValue(response.getString("mr"));
                            p[1] =RgbManualActivity.getTransformValue(response.getString("mg"));
                            p[2] =RgbManualActivity.getTransformValue(response.getString("mb"));
                            p[3] =RgbManualActivity.getTransformValue(response.getString("mw"));
                            p[4] =RgbManualActivity.getTransformValue(response.getString("sr"));
                            p[5] =RgbManualActivity.getTransformValue(response.getString("sg"));
                            p[6] =RgbManualActivity.getTransformValue(response.getString("sb"));
                            p[7] =RgbManualActivity.getTransformValue(response.getString("sw"));
                            p[8] = String.valueOf(Integer.valueOf(response.getString("rmt"))/hourFactor);
                            p[9] =String.valueOf(Integer.valueOf(response.getString("slt"))/hourFactor);
                            p[10] =String.valueOf(Integer.valueOf(response.getString("tot"))/hourFactor);
                            updateTextViews();
                            setElementsFocusable(true);
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
private void updateTextViews(){
    String value1 = "Total: "+p[10]+", SunRise: "+p[8]+", SunSet: "+p[9];
    textSetTime.setText(value1);
    String value2 = "R: "+p[4]+", G: "+p[5]+", B: "+p[6]+", W: "+p[7];
    textStartRGB.setText(value2);
    String value3 = "R: "+p[0]+", G: "+p[1]+", B: "+p[2]+", W: "+p[3];
    textPeakRGB.setText(value3);
}
private boolean validateData(){
    boolean returnVal = true;
    if (Integer.valueOf(p[10]) < (Integer.valueOf(p[8]) + Integer.valueOf(p[9]))) {
        returnVal = false;
        Toast.makeText(getContext(), "Total time should be greater than sum of Sunrise and Sunset", Toast.LENGTH_SHORT).show();
    }
    if (    Integer.valueOf(p[0]) < Integer.valueOf(p[4])||
            Integer.valueOf(p[1]) < Integer.valueOf(p[5])||
            Integer.valueOf(p[2]) < Integer.valueOf(p[6])||
            Integer.valueOf(p[3]) < Integer.valueOf(p[7])
    ) {
        returnVal = false;
        Toast.makeText(getContext(), "Peak intensities should be greater than starting", Toast.LENGTH_SHORT).show();
    }
        return returnVal;
}
}