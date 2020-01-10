package com.example.streamvideo.view.Model;

import android.content.Context;
import android.util.Log;

import com.example.streamvideo.view.Presenter.ReciveData;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttConnection {
    MqttConnectOptions options;
    String clientId;

    private static MqttAndroidClient client;
    private IMqttActionListener mqttListenner;
    MqttCallback callbackMQTT;
    static String _add="m12.cloudmqtt.com";
    static int _port = 16142;
    static String _user = "oyaiwxox";
    static String _pass= "FtVmfbn6kUeb";
    static String _topicRx = "luat/videoRx";

    static MqttConnection instance;

    Context mContext;

    public static MqttConnection getInstance(Context mContext) {
        if (instance == null)
            instance = new MqttConnection(mContext);
        return instance;
    }


    private  MqttConnection(Context context){
        mContext = context;
        options = new MqttConnectOptions();
        clientId = MqttClient.generateClientId();

        client= new MqttAndroidClient(mContext, "tcp://"+_add+":"+_port,
                clientId);

        options.setUserName(_user);
        options.setPassword(_pass.toCharArray());


        mqttListenner = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {

                    client.subscribe(_topicRx, 0);

                } catch (MqttException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        };
        callbackMQTT = new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {

                if(topic.equals("luat/videoRx")){

                    ReciveData.reciveData(message.getPayload());
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        };

        client.setCallback(callbackMQTT);
        try {
            client.connect(options,mContext,mqttListenner);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
    public static void publish(byte[] b){

        if (client == null
        || !client.isConnected())
            return;
        try {
            client.publish("luat/videoRx",b,0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

}
