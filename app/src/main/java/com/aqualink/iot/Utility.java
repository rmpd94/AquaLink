package com.aqualink.iot;

import android.content.Context;
import android.content.SharedPreferences;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public  class Utility {
    private RequestQueue mQueue;
   private String  JsonResPayload ;
   public String Months [] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

  public String voleySendHttpRequest ( Context context,String url, String method, String[][] payload ){

      int voleyMethod;
      JSONObject JsonReqPayload = new JSONObject();
      mQueue = Volley.newRequestQueue(context.getApplicationContext());

      if (payload.length > 0) {
      try {
          for(int i=0; i <payload.length; i++ ) {
              JsonReqPayload.put(payload[i][0], payload[i][1]);
          }

      } catch (JSONException e) {
          e.printStackTrace();
      } }
      switch(method){
          case "POST":
              voleyMethod = 1;
              break;
          case "GET":
              voleyMethod = 0;
              break;
          case "PATCH":
              voleyMethod = 7;
              break;

          default:
              voleyMethod = 0 ;
      }
      JsonObjectRequest request = new JsonObjectRequest(voleyMethod, url, JsonReqPayload,
              new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {
                      JsonResPayload = response.toString() ;
                  }
              }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              error.printStackTrace();
          }
      });
      mQueue.add(request);

         return "";
    }

    public void setSharedPref ( Context context, String prefName, String[][] elementValue){
        for(int i=0; i <elementValue.length; i++ ) {
            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(prefName, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(elementValue[i][0], elementValue[i][1]);
            editor.apply();
        }
    }

    public String  getSharedPref ( Context context, String prefName,String prefElement){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(prefName, context.MODE_PRIVATE);
        return sharedPreferences.getString(prefElement, "");
    }
    public void removeSharedPref ( Context context, String prefName, String key){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(prefName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
    public String  getSharedPrefdef ( Context context, String prefName,String prefElement, String defValue){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(prefName, context.MODE_PRIVATE);
        return sharedPreferences.getString(prefElement, defValue);
    }
   public String getSplitValue(String data, char separator, int index)
    {
        int found = 0;
        int strIndex[] = { 0, -1 };
        int maxIndex = data.length() - 1;

        for (int i = 0; i <= maxIndex && found <= index; i++) {
            if (data.charAt(i) == separator || i == maxIndex) {
                found++;
                strIndex[0] = strIndex[1] + 1;
                strIndex[1] = (i == maxIndex) ? i+1 : i;
            }
        }
        return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
    }
}
