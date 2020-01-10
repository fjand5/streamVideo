package com.example.streamvideo.view.Presenter;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

import com.example.streamvideo.view.Model.MqttConnection;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ReciveData {
    static OnDataComeListenner mOnDataComeListenner = null;

    public static void setOnDataComeListenner(OnDataComeListenner onDataComeListenner) {
        mOnDataComeListenner = onDataComeListenner;
    }
    public static void reciveData(byte[] data) {

        if(mOnDataComeListenner != null){
            ByteBuffer bb = ByteBuffer.allocate(data.length);
            bb.put(data);

            int w = bb.getInt(0 );
            int h = bb.getInt(4);
            int f = bb.getInt(8);

            mOnDataComeListenner.callBack(Arrays.copyOfRange(bb.array(), 12,data.length),  w,  h,  f);

        }
    }

    public interface OnDataComeListenner{
        void callBack(byte[] data, int w, int h, int f);
    }

}
