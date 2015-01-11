package com.example.team3.leavingnow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.team3.leavingnow.MainActivity;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.Random;
import java.util.UUID;

public class Receiver extends PebbleKit.PebbleDataReceiver {
    static public int pebbleData = -1;
    static public int KEY_DATA = 5;
    static public int DATA_TYPE = 1;
    private static final UUID TEST_UUID = UUID.fromString("2c4dbeaf-d3d1-46af-9588-112f1745c9f2");
    Context c;

    public Receiver() {
        super(TEST_UUID);
    }

    @Override
    public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
        c = context;
        final Handler handler = new Handler();
        for(int i = 2; i <= 5; i++){
            if(data.contains(i)){
                pebbleData = i;
                break;
            }
        }

        handler.post(new Runnable() {

            @Override
            public void run() {
                if(pebbleData != 1){
                    if(pebbleData != 5){
                        pebbleSMS(data.getString(pebbleData));
                        PebbleDictionary data = new PebbleDictionary();
                        data.addString(KEY_DATA, "Sent!");
                        toPebble(data);
                    }else{
                        MainActivity.pebble_contacts.addInt32(DATA_TYPE, 0);
                        toPebble(MainActivity.pebble_contacts);
                    }
                }
            }

        });
        PebbleKit.sendAckToPebble(context.getApplicationContext(), transactionId);
    }

    public void toPebble(PebbleDictionary data) {
        // Send the assembled dictionary to the watchapp;
        PebbleKit.sendDataToPebble(c.getApplicationContext(), TEST_UUID, data);

        PebbleKit.registerReceivedAckHandler(c.getApplicationContext(), new PebbleKit.PebbleAckReceiver(TEST_UUID) {

            @Override
            public void receiveAck(Context context, int transactionId) {
                Log.i("", "Received ack for transaction " + transactionId);
            }

        });

        PebbleKit.registerReceivedNackHandler(c.getApplicationContext(), new PebbleKit.PebbleNackReceiver(TEST_UUID) {

            @Override
            public void receiveNack(Context context, int transactionId) {
                Log.i("", "Received nack for transaction " + transactionId);
            }

        });
    }

    public int pebbleSMS(String name){
        int ni = MainActivity.contact_names.indexOf(name);
        Log.i("pebbleSMS","Index of "+name+": "+ni );
        final String number = MainActivity.contactModels.get(ni).getPhone();
        SharedPreferences prefs;
        prefs = getId();
        c.startService(new Intent(c, MapService.class));
        MainActivity.textContact(number, "Leaving now!\n"+"https://leaving-now.firebaseapp.com/?"+prefs.getString("id", "null"));
        Toast.makeText(c, "Sent", Toast.LENGTH_SHORT).show();
        return 0;
    }

    public SharedPreferences getId(){
        SharedPreferences prefs = c.getSharedPreferences("com.example.homebase", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Random r = new Random();
        editor.putString("id", String.valueOf(r.nextInt(Integer.MAX_VALUE) + 1));
        editor.commit();
        return prefs;
    }
}
