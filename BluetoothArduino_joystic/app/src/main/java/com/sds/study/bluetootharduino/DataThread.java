package com.sds.study.bluetootharduino;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by lee on 2016-12-08.
 */

public class DataThread extends Thread{
    MainActivity mainActivity;
    BufferedReader buffr;
    BufferedWriter buffw;
    BluetoothSocket socket;
    boolean flag =true;

    public DataThread(MainActivity mainActivity,BluetoothSocket socket) {
        this.mainActivity =mainActivity;
        this.socket = socket;
        try {
            buffr = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //듣기
    public void listen(){
        String msg = null;
        try {
            msg = buffr.readLine();

            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("msg",msg);
            message.setData(bundle);

            mainActivity.handler.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //말하기
    public void send(String msg){
        try {
            buffw.write(msg);
            buffw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (flag) {
            listen();
        }
    }
}
