package com.example.streamvideo.view.Presenter;

import android.content.Context;
import android.hardware.Camera;

import com.example.streamvideo.view.Model.MqttConnection;

import java.nio.ByteBuffer;
import java.sql.Struct;

public class SendData {

public static void sendData(Context context, byte[] data, int w, int h, int f){
    ByteBuffer bb = ByteBuffer.allocate(data.length + Integer.SIZE/8 + Integer.SIZE/8+ Integer.SIZE/8);
    bb.putInt(w);
    bb.putInt(h);
    bb.putInt(f);
    bb.put(data);
    MqttConnection.getInstance(context);
    MqttConnection.getInstance(context).publish(bb.array());
//    ReciveData.reciveData(bb.array());
}
}
