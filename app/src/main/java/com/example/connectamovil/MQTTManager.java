package com.example.connectamovil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTManager {

    private MqttAndroidClient mqttAndroidClient;

    public MQTTManager(String clientId, String serverUri) {
        mqttAndroidClient = new MqttAndroidClient(
                MyApplication.getAppContext(),
                serverUri,
                clientId
        );
    }

    public void connect(final IMqttActionListener callback) {
        try {
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            // configuraciones de conexión (por ejemplo, nombre de usuario y contraseña)

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    if (callback != null) {
                        callback.onSuccess(asyncActionToken);
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    if (callback != null) {
                        callback.onFailure(asyncActionToken, exception);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic, int qos, MqttCallback callback) {
        try {
            mqttAndroidClient.subscribe(topic, qos);
            mqttAndroidClient.setCallback(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String payload) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            mqttAndroidClient.publish(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
