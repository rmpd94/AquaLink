package com.aqualink.iot.rgb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class RgbManualActivity extends Fragment   {
    SeekBar seekBarRed;
    SeekBar seekBarGreen;
    SeekBar seekBarBlue;
    SeekBar seekBarWhite;
    TextView textRed;
    TextView textGreen;
    TextView textBlue;
    TextView textWhite;
    Button saveRGBManualBtn;
    Button btnPset1;
    Button btnPset2;
    Button btnPset3;
    Button btnPset4;
    Button btnPset5;
    Button btnPset6;
    Button btnPset7;
    Button btnPset8;
    final String pSetPrefElmnt[] = {"p_1", "p_2", "p_3", "p_4","p_5","p_6","p_7","p_8"};
    private Button[] btns ;
    // Name of shared preference
    public static final String rgbSharedPref = "rgbPrefs";
    public RgbManualActivity() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_rgb_manual, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        seekBarRed =(SeekBar) view.findViewById(R.id.seekRed);
        seekBarGreen =(SeekBar) view.findViewById(R.id.seekGreen);
        seekBarBlue =(SeekBar) view.findViewById(R.id.seekBlue);
        seekBarWhite =(SeekBar) view.findViewById(R.id.seekWhite);
        textRed = (TextView) view.findViewById(R.id.textViewRed) ;
        textGreen = (TextView) view.findViewById(R.id.textViewGreen) ;
        textBlue = (TextView) view.findViewById(R.id.textViewBlue) ;
        textWhite = (TextView) view.findViewById(R.id.textViewWhite) ;
        saveRGBManualBtn =(Button) view.findViewById(R.id.saveManualRGBButton);
        btnPset1 =(Button) view.findViewById(R.id.btnPset1);
        btnPset2 =(Button) view.findViewById(R.id.btnPset2);
        btnPset3 =(Button) view.findViewById(R.id.btnPset3);
        btnPset4 =(Button) view.findViewById(R.id.btnPset4);
        btnPset5 =(Button) view.findViewById(R.id.btnPset5);
        btnPset6 =(Button) view.findViewById(R.id.btnPset6);
        btnPset7 =(Button) view.findViewById(R.id.btnPset7);
        btnPset8 =(Button) view.findViewById(R.id.btnPset8);
        btns = new Button[]{btnPset1, btnPset2, btnPset3, btnPset4, btnPset5, btnPset6, btnPset7, btnPset8};
        setElementsFocusable(false);
        getRGBHttpReq();

    } //end onViewCreated
private void setElementsFocusable(Boolean input){
            seekBarRed.setEnabled(input);
            seekBarGreen.setEnabled(input);
            seekBarBlue.setEnabled(input);
            seekBarWhite.setEnabled(input);
            saveRGBManualBtn.setEnabled(input);
    btnPset1.setEnabled(input);
    btnPset2.setEnabled(input);
    btnPset3.setEnabled(input);
    btnPset4.setEnabled(input);
    btnPset5.setEnabled(input);
    btnPset6.setEnabled(input);
    btnPset7.setEnabled(input);
    btnPset8.setEnabled(input);
}

    private void setManualRGBHttpReq(String url,String type) {
        String red =String.valueOf(SetTransformValue(String.valueOf(seekBarRed.getProgress()))) ;
        String green =String.valueOf(SetTransformValue(String.valueOf(seekBarGreen.getProgress()))) ;
        String blue = String.valueOf(SetTransformValue(String.valueOf(seekBarBlue.getProgress())));
        String white = String.valueOf(SetTransformValue(String.valueOf(seekBarWhite.getProgress())));
        if(!RgbDeviceActivity.isManMode()){
            String[][] payload;
            if(type.equals("1")) {
                //payload = new String[][]{{"R", red}, {"G", green}, {"B", blue}, {"W", white}};
               // new Utility().voleySendHttpRequest(getActivity(), url, "POST", payload);
                new  SendUDPData().execute( red+","+ green+","+blue+","+white);

            }  else if (type.equals("2")){
                payload = new String[][]{{"mint", red+","+green+","+blue+","+white}};
                new Utility().voleySendHttpRequest(getActivity(), url, "POST", payload);
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
    } else{
           // Toast.makeText(getActivity(),"Please change the mode to Manual", Toast.LENGTH_SHORT).show();
        }
    }
    private void getRGBHttpReq() {

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        JSONObject req = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, RgbDeviceActivity.host+RgbDeviceActivity.getRGBUrl, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String R =getTransformValue(response.getString("R"));
                            String G = getTransformValue(response.getString("G"));
                            String B = getTransformValue(response.getString("B"));
                            String W = getTransformValue(response.getString("W"));
                            setProgress(R,G,B,W);
                            SeekBarChangeListener();
                            buttonChangeListener();
                            loadPresetBtns();
                            setElementsFocusable(true);
                            //spinner.setVisibility(View.GONE);
                            //modeSwitch.setOnCheckedChangeListener(RgbDeviceActivity.this);
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
    //Converts PWM value to % value
public static String getTransformValue(String input){
        int maxVal = Integer.valueOf(RgbDeviceActivity.deviceMaxPWM);
        float returnVal;
        returnVal = (float) (100.00/maxVal) * Integer.valueOf(input);
        //Toast.makeText(getActivity(),String.valueOf(returnVal), Toast.LENGTH_SHORT).show();
        return String.valueOf(Math.round(returnVal));
}
    //Converts % value to PWM value
public static String SetTransformValue(String input){
    int maxVal = Integer.valueOf(RgbDeviceActivity.deviceMaxPWM);
    float returnVal;
    returnVal = (float) (maxVal/100.00) * Integer.valueOf(input);
    //Toast.makeText(getActivity(),String.valueOf(returnVal), Toast.LENGTH_SHORT).show();
    return String.valueOf(Math.round(returnVal));
    }
    private void SeekBarChangeListener(){
        //Seekbar Red
        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                seekbarTextUpdater();
                setManualRGBHttpReq("","1");
                // Toast.makeText(getActivity(),"seekbar progress: " +progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //    Toast.makeText(getActivity(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setManualRGBHttpReq("","1");
                // Toast.makeText(getActivity(),"seekbar touch stopped!"+seekBarRed.getProgress(), Toast.LENGTH_SHORT).show();
            }

        });

        //Seekbar Green
        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekbarTextUpdater();
                setManualRGBHttpReq("","1");
                // Toast.makeText(getActivity(),"seekbar progress: " +progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //    Toast.makeText(getActivity(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setManualRGBHttpReq("","1");
                //Toast.makeText(getActivity(),"seekbar touch stopped!"+seekBarGreen.getProgress(), Toast.LENGTH_SHORT).show();
            }

        });

        //Seekbar Blue
        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                seekbarTextUpdater();
                setManualRGBHttpReq("","1");
                // Toast.makeText(getActivity(),"seekbar progress: " +progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //    Toast.makeText(getActivity(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setManualRGBHttpReq("","1");
                // Toast.makeText(getActivity(),"seekbar touch stopped!"+seekBarBlue.getProgress(), Toast.LENGTH_SHORT).show();
            }

        });

        //Seekbar White
        seekBarWhite.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekbarTextUpdater();
                setManualRGBHttpReq("","1");
                // Toast.makeText(getActivity(),"seekbar progress: " +progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //    Toast.makeText(getActivity(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setManualRGBHttpReq("","1");
                // Toast.makeText(getActivity(),"seekbar touch stopped!"+seekBarWhite.getProgress(), Toast.LENGTH_SHORT).show();

            }

        });
        //button on click listener
      /*  saveRGBManualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setManualRGBHttpReq(RgbDeviceActivity.host+RgbDeviceActivity.saveManStngsUrl,"2");
            }
        });*/
    }

    private void buttonChangeListener(){

        saveRGBManualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setManualRGBHttpReq(RgbDeviceActivity.host+RgbDeviceActivity.saveManStngsUrl,"2");
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });
/*
        btnPset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyPreset(pSetPrefElmnt[0],btnPset1);
                Toast.makeText(getContext(), "Preset applied", Toast.LENGTH_SHORT).show();
            }
        });
        btnPset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyPreset(pSetPrefElmnt[1],btnPset2);
                Toast.makeText(getContext(), "Preset applied", Toast.LENGTH_SHORT).show();
            }
        });
        btnPset3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyPreset(pSetPrefElmnt[2],btnPset3);
                Toast.makeText(getContext(), "Preset applied", Toast.LENGTH_SHORT).show();
            }
        });
        btnPset4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyPreset(pSetPrefElmnt[3],btnPset4);
                Toast.makeText(getContext(), "Preset applied", Toast.LENGTH_SHORT).show();
            }
        });
        btnPset5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyPreset(pSetPrefElmnt[4],btnPset5);
                Toast.makeText(getContext(), "Preset applied", Toast.LENGTH_SHORT).show();
            }
        });
        btnPset6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyPreset(pSetPrefElmnt[5],btnPset6);
                Toast.makeText(getContext(), "Preset applied", Toast.LENGTH_SHORT).show();
            }
        });
        btnPset7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyPreset(pSetPrefElmnt[6],btnPset7);
                Toast.makeText(getContext(), "Preset applied", Toast.LENGTH_SHORT).show();
            }
        });
        btnPset8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyPreset(pSetPrefElmnt[7],btnPset8);
                Toast.makeText(getContext(), "Preset applied", Toast.LENGTH_SHORT).show();
            }
        });
//Long press listner
        btnPset1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePresetDialog(btnPset1,pSetPrefElmnt[0],getCurrRGBText());
                return false;
            }
        });
        btnPset2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePresetDialog(btnPset2,pSetPrefElmnt[1],getCurrRGBText());
                return false;
            }
        });
        btnPset3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePresetDialog(btnPset1,pSetPrefElmnt[0],getCurrRGBText());
                return false;
            }
        });
        btnPset1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePresetDialog(btnPset1,pSetPrefElmnt[0],getCurrRGBText());
                return false;
            }
        });
        btnPset1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePresetDialog(btnPset1,pSetPrefElmnt[0],getCurrRGBText());
                return false;
            }
        });
        btnPset1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePresetDialog(btnPset1,pSetPrefElmnt[0],getCurrRGBText());
                return false;
            }
        });
        btnPset1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePresetDialog(btnPset1,pSetPrefElmnt[0],getCurrRGBText());
                return false;
            }
        });
        btnPset1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                savePresetDialog(btnPset1,pSetPrefElmnt[0],getCurrRGBText());
                return false;
            }
        });
*/
    }
    public void savePresetDialog (Button btn,String sPrefElement,String rgb){
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getContext());
        final EditText name = new EditText(getContext());

        name.setGravity(Gravity.LEFT);
        name.setPadding(40,40,40,40);
        name.setTextSize(15);
        name.setMaxLines(1);
        name.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });
        name.setText(btn.getText());
        popDialog.setTitle("Enter a preset name");
        popDialog.setView(name);

// Button OK
        popDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        btn.setText(name.getText().toString().trim());
                        new Utility().setSharedPref(getContext(),RgbDeviceActivity.deviceName + rgbSharedPref, new String[][]{{sPrefElement, name.getText().toString().trim()+";"+rgb}});
                        dialog.dismiss();
                    }
                });
        popDialog.create();
        popDialog.show();
    }
private void setProgress(String r,String g, String b, String w){
    seekBarRed.setProgress(Integer.parseInt(r));
    seekBarGreen.setProgress(Integer.parseInt(g));
    seekBarBlue.setProgress(Integer.parseInt(b));
    seekBarWhite.setProgress(Integer.parseInt(w));
    textRed.setText(r + "%");
    textGreen.setText(g+ "%");
    textBlue.setText(b+ "%");
    textWhite.setText(w+ "%");
}
private void seekbarTextUpdater(){
        textRed.setText(seekBarRed.getProgress() + "%");
        textGreen.setText(seekBarGreen.getProgress() + "%");
        textBlue.setText(seekBarBlue.getProgress() + "%");
        textWhite.setText(seekBarWhite.getProgress() + "%");
    }
    private String  getCurrRGBText(){
        String rVal;
        rVal = seekBarRed.getProgress() + ";";
        rVal += seekBarGreen.getProgress() + ";";
        rVal += seekBarBlue.getProgress() + ";";
        rVal += String.valueOf(seekBarWhite.getProgress());
        return rVal;
    }
    private void applyPreset(String sPrefElement,Button btn ){
        String rgbText =new Utility().getSharedPrefdef(getContext(), RgbDeviceActivity.deviceName + rgbSharedPref,sPrefElement,  btn.getText()+";"+btn.getTooltipText());
        String r;
        String g;
        String b;
        String w;
        String[][] payload;
        r=new Utility().getSplitValue(rgbText, ';',1);
        g=new Utility().getSplitValue(rgbText, ';',2);
        b=new Utility().getSplitValue(rgbText, ';',3);
        w=new Utility().getSplitValue(rgbText, ';',4);
       // payload = new String[][]{{"R", SetTransformValue(r)}, {"G", SetTransformValue(g)}, {"B", SetTransformValue(b)}, {"W", SetTransformValue(w)}};
      //  new Utility().voleySendHttpRequest(getActivity(), RgbDeviceActivity.host +RgbDeviceActivity.setManualRGBUrl, "POST", payload);
        new  SendUDPData().execute( SetTransformValue(r)+","+ SetTransformValue(g)+","+SetTransformValue(b)+","+SetTransformValue(w));
        setProgress(r,g,b,w);
    }
    private void loadPresetBtns(){
        String pSetVals;
        for(int i = 0; i <8; i++){
            pSetVals = new Utility().getSharedPrefdef(getContext(), RgbDeviceActivity.deviceName + rgbSharedPref,pSetPrefElmnt[i],  btns[i].getText()+";"+btns[i].getTooltipText());
            btns[i].setText(new Utility().getSplitValue(pSetVals, ';',0));
            btns[i].setTooltipText(pSetVals.substring(pSetVals.indexOf(";")+1));
           final int  j;
            j=i;
            btns[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    applyPreset(pSetPrefElmnt[j],btns[j]);
                   // Toast.makeText(getContext(), "Preset applied", Toast.LENGTH_SHORT).show();
                }
            });
            btns[j].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    savePresetDialog(btns[j],pSetPrefElmnt[j],getCurrRGBText());
                    return false;
                }
            });
        }

    }
}