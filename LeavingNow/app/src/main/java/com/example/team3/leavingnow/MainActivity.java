package com.example.team3.leavingnow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.Arrays;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import static android.os.SystemClock.sleep;


public class MainActivity extends Activity {

    ListView listview;
    static ArrayList<ContactModel> contactModels;
    ArrayList<ContactModel> contactModelsSection;
    static PebbleDictionary pebble_contacts = new PebbleDictionary();
    static ArrayList<String> contact_names;

    static boolean endTime;
    private static final UUID TEST_UUID = UUID.fromString("2c4dbeaf-d3d1-46af-9588-112f1745c9f2");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        PebbleKit.startAppOnPebble(getApplicationContext(), TEST_UUID);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        endTime = false;
        listview = (ListView)findViewById(R.id.list_contact);
        contactModels = new ArrayList<ContactModel>();
        contact_names = new ArrayList<String>();
        new getContactTask().execute((Void[]) null);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = contactModels.get(position).getName();
                final String number = contactModels.get(position).getPhone();
                SharedPreferences prefs = getSharedPreferences("com.example.homebase", Context.MODE_PRIVATE);
                prefs = getId();
                startService(new Intent(getBaseContext(), MapService.class));
                textContact(number, "Leaving now!\n"+"https://leaving-now.firebaseapp.com/?"+prefs.getString("id", "null"));
                Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void userSelected(int position){
    }


    public void populatePebbleContacts(){
        pebble_contacts.addString(6, contact_names.get(81));
        pebble_contacts.addString(7, contact_names.get(24));
        pebble_contacts.addString(8, contact_names.get(187));
        //pebble_contacts.addString(9, contact_names.get(250));
    }

    private class getContactTask extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected Void doInBackground(Void... params) {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            while (phones.moveToNext())
            {
                ContactModel contactModel = new ContactModel();
                contactModel.setName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).toUpperCase(Locale.ENGLISH));
                contact_names.add(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).toUpperCase(Locale.ENGLISH));
                contactModel.setPhone(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                contactModels.add(contactModel);
            }
            phones.close();
            Collections.sort(contactModels, new ContactModel());
            Collections.sort(contact_names);
            populatePebbleContacts();
            Collections.sort(contactModels, new ContactModel());
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            listview.setAdapter(new ContactListAdapter(getApplicationContext(), contactModels));
        }

    }

    public SharedPreferences getId(){
        SharedPreferences prefs = getSharedPreferences("com.example.homebase", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Random r = new Random();
        editor.putString("id", String.valueOf(r.nextInt(Integer.MAX_VALUE) + 1));
        editor.commit();
        return prefs;
    }

    public static void textContact(String phoneNum, String message){
        int numLength = phoneNum.length();
        Log.i("randomNum", "The random number is:" + message);
        //make sure that the phone number is larger than 7 digits
        if(numLength > 7){
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNum, null, message, null, null);
        }
    }




}
