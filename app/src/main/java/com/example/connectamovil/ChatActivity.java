package com.example.connectamovil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSend;
    private ListView listViewMessages;

    private MQTTManager mqttManager;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        listViewMessages = findViewById(R.id.listViewMessages);

        mqttManager = new MQTTManager("your_client_id");
        chatAdapter = new ChatAdapter(this);

        listViewMessages.setAdapter(chatAdapter);

        mqttManager.connect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                mqttManager.subscribe("chat/topic", 1, new MqttCallbackHandler());
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                exception.printStackTrace();
            }
        });

        buttonSend.setOnClickListener(view -> {
            String message = editTextMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                mqttManager.publish("chat/topic", message);
                editTextMessage.getText().clear();
            }
        });
    }

    private class MqttCallbackHandler implements MqttCallback {
        @Override
        public void connectionLost(Throwable cause) {
            // reconectar en caso de pérdida de conexión
            new Handler(Looper.getMainLooper()).postDelayed(() -> mqttManager.connect(null), 5000);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String content = new String(message.getPayload());
            ChatMessage chatMessage = new ChatMessage("Sender", content);
            chatAdapter.addMessage(chatMessage);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            // no necesitamos implementar esto para la publicación
        }
    }
}
