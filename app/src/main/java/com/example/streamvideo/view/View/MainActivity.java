package com.example.streamvideo.view.View;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.widget.ImageView;

import com.example.streamvideo.R;
import com.example.streamvideo.view.Presenter.ReciveData;
import com.example.streamvideo.view.Presenter.SendData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private SurfaceView localPreviewSfv;
    private ImageView remotePreviewImv;
    Camera camera;

public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext =this;
        camera = Camera.open(1);
        camera.startPreview();
        Camera.Parameters parameters = camera.getParameters();




        parameters.setPreviewSize(176,144);
        parameters.setJpegQuality(1);

        camera.setParameters(parameters);

        remotePreviewImv= findViewById(R.id.remotePreviewImv);
        localPreviewSfv= findViewById(R.id.localPreviewSfv);

        ReciveData.setOnDataComeListenner(new ReciveData.OnDataComeListenner() {
            @Override
            public void callBack(byte[] data,int w, int h, int f) {
                YuvImage yuvImage = new YuvImage(data, f , w,h,null);

                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                yuvImage.compressToJpeg(new Rect(0,0,w,h),100, buf);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outHeight = h;
                options.outWidth = w;
                Bitmap bitmap = BitmapFactory.decodeByteArray(buf.toByteArray(),
                        0,buf.toByteArray().length,options);
                remotePreviewImv.setImageBitmap(bitmap);

            }
        });
        final SurfaceHolder localPreviewHld = localPreviewSfv.getHolder();
        camera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                int h = parameters.getPreviewSize().height;
                int w = parameters.getPreviewSize().width;
                int f = parameters.getPreviewFormat();
                SendData.sendData(mContext,bytes,w,h,f);
                camera.setOneShotPreviewCallback(this);
            }
        });
        localPreviewHld.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    camera.setPreviewDisplay(localPreviewHld);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });

    }
}
