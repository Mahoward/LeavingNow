package com.example.team3.leavingnow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends Activity {

    ListView listview;
    ArrayList<ContactModel> contactModels;
    ArrayList<ContactModel> contactModelsSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView)findViewById(R.id.list_contact);
        contactModels = new ArrayList<ContactModel>();
        new getContactTask().execute((Void[]) null);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String name = contactModels.get(position).getName();
                final String number = contactModels.get(position).getPhone();
                SharedPreferences prefs = getSharedPreferences("com.example.homebase", Context.MODE_PRIVATE);
                textContact(number, "Leaving now!\n"+"https://leaving-now.firebaseapp.com/?");//+prefs.getString("id", "null"));
                Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                startService(new Intent(getBaseContext(), MapService.class));

            }
        });



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
                contactModel.setPhone(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                contactModels.add(contactModel);
            }
            phones.close();
            Collections.sort(contactModels, new ContactModel());

            Collections.sort(contactModels, new ContactModel());
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            listview.setAdapter(new ContactListAdapter(getApplicationContext(), contactModels));
        }

    }

    public void textContact(String phoneNum, String message){
        int numLength = phoneNum.length();
        SharedPreferences prefs = getSharedPreferences("com.example.homebase", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Random r = new Random();
        editor.putString("id", String.valueOf(r.nextInt(Integer.MAX_VALUE) + 1));
        editor.commit();
        Log.i("randomNum", "The random number is:" + prefs.getString("id", "null"));
        //make sure that the phone number is larger than 7 digits
        if(numLength > 7){
            message = message+prefs.getString("id", "null");
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNum, null, message, null, null);
        }
    }




}
