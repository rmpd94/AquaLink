package com.aqualink.iot.Station;

import android.os.AsyncTask;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SendUDPDataST extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String messageStr = params[0];
       // int server_port = 8888;
        DatagramSocket s = null;
        /// Log.d("saurav","RTP@ Message to send is"+messageStr);
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            // Log.d("saurav","RTP@ Exception is"+e);
        }


        int msg_length=messageStr.length();
        byte[] message = messageStr.getBytes();
        DatagramPacket p = new DatagramPacket(message, msg_length, StationActivity.STLocal,8888);
        try {
            //  Log.d("saurav","RTP@ Sending send()");
            s.send(p);
        } catch (IOException e) {
            //  Log.d("saurav","RTP@ Exception is"+e);
        }
        return null;
    }
}
