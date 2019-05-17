package olayemi.chatapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static olayemi.chatapp.Utils.Utils.CONNECTION_DISCONNECTED;
import static olayemi.chatapp.Utils.Utils.CONNECTION_FAILED;
import static olayemi.chatapp.Utils.Utils.CONNECTION_SUCCESSFULLY;
import static olayemi.chatapp.Utils.Utils.HOST_URI;

public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private String  Topic;
    private String TAG = "ChatApp";
    private static  AdapterClass adapter;
    private static  List<Messages> listArray = new ArrayList<Messages>();
    private ListView listView;
    private String tokenID = String.valueOf(new Random().nextInt());
    private EditText inputedMessage;
    private ImageView sendImage ;
    static Context context;
    private String clientId = MqttClient.generateClientId();
    private int qos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendImage = (ImageView)findViewById(R.id.Send);

        Intent i = getIntent();
        Topic =  i.getStringExtra("userID");//we use the user's ID as the topic.
        Toast.makeText(this, Topic, Toast.LENGTH_SHORT).show();

        context = MainActivity.this;
        inputedMessage = (EditText)findViewById(R.id.InputMessage);


        listView = (ListView)findViewById(R.id.Chat);

        createConnection();
        clickSend(sendImage);
        setTitle(Topic);

    }



    public void createConnection(){


        try {  client =
                new MqttAndroidClient(this.getApplicationContext(), HOST_URI,
                        clientId);


            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Subscribe();
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this, CONNECTION_FAILED, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure");


                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void Subscribe(){

        try {
            IMqttToken subToken = client.subscribe(Topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    messageListener();
                    // The message was published
                    Log.d(TAG, "Subscribe Successful");
                    Toast.makeText(MainActivity.this, CONNECTION_SUCCESSFULLY+ " subscribed to: "+ Topic, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Toast.makeText(MainActivity.this, CONNECTION_FAILED + " Subscribed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Subscribe Failed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void PublishMessage(){
//        This method is to publish the message to server and using QOS(Quality of Service) = 1 so as to guarantee delivering of the message at least once

        try {
            String payload = "<" + inputedMessage.getText().toString() + ">" + tokenID;
            byte[] encodedPayload = new byte[0];
            encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
//            message.setId(5866);
            message.setRetained(true);
            message.setQos(qos);
            client.publish(Topic, message);
        } catch (MqttException e) {
            Toast.makeText(MainActivity.this, "Your message wasn't sent", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.d(TAG, "Publish Message Failed");
        }

    }

    public void messageListener(){
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

                Log.d(TAG, "Connection is Lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                //create Date and Time
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh.mm EEEE");
                String dateTime = String.valueOf(dateFormat.format(date));

//                Differentiate between different tokenID and remove the TokenId on Display
                 Messages loadmessage ;

                if (message.toString().contains(tokenID)){
                    loadmessage = new Messages("Me", removeId(message.toString()), dateTime );
                }
//
                else{

                    loadmessage = new Messages("CHATBUDDY",removeId(message.toString()), dateTime );
                }

                listArray.add(loadmessage);

                adapter = new AdapterClass(listArray, MainActivity.this);

                listView.setAdapter(adapter);


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {


            }
        });
    }


    //        this methos his to format the string of the message
    private String removeId(String messagewithID){


//        That pattern will extract any characters within the '<' and '>' characters.
        Pattern pattern = Pattern.compile("\\<(.*?)\\>");


        Matcher matcher = pattern.matcher(messagewithID);

        String message = "";

        while(matcher.find()){
//           remove leading or trailing space
            message = matcher.group(1).trim();
        }

        return message;
    }


    public static void deleteMessage(final int chatmessage){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Sure u want to delete this message?");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listArray.remove(chatmessage);
                adapter.notifyDataSetChanged();
                Toast.makeText(context, "Message has been deleted", Toast.LENGTH_LONG).show();
            }
        }).show();

    }


//    To Disconnect
    public void disconnect()
            throws MqttException {
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Toast.makeText(MainActivity.this, CONNECTION_DISCONNECTED, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Successfully disconnected");
            }
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d(TAG, "Failed to disconnected " + throwable.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void clickSend(View view){
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishMessage();

                inputedMessage.setText(""); // To clear EditText after message has been sent
            }
        });

    }
}
