package com.example.streamvideo.view.Model;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

import static android.content.Context.WIFI_SERVICE;

public class CameraServer {

    private static CameraServer instance;
    OutputStream _outputStream;
    volatile static Buffer buffer=null;
    public static CameraServer getInstance(Context context) {
        if(instance == null){
            buffer = new Buffer();

            instance = new CameraServer(context);
        }
        return instance;
    }

//    public void setOutputStream(OutputStream outputStream) {
//        _outputStream = outputStream;
//        InputStream inputStream = new InputStream() {
//            @Override
//            public int read() throws IOException {
//                return 0;
//            }
//        };
//        inputStream.read(buffer.)
//
//    }

    public void setBuffer(Buffer buffer) {
        this.buffer.clear();
        this.buffer = buffer;
    }
    public void setBuffer(byte[] buffer) {
        this.buffer.clear();
        this.buffer.write(buffer);
    }

    public CameraServer(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        final String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("htl","dispatch: " + ip);
                final MockWebServer server = new MockWebServer();
                server.setDispatcher(new Dispatcher() {
                    @NotNull
                    @Override
                    public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
                        Log.d("htl","dispatch");
                        if(recordedRequest.getPath().equals("/camera")){
                            return new MockResponse().setHeader("content-type", "video/mp4")
                                    .setBody(buffer);
                        }
                        return null;
                    }
                });
                try {
                    server.start(InetAddress.getByName(ip),7878);
                    server.url("/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
