package com.example.team3.leavingnow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
                textContact(number, "Leaving now!");
                Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                fireBaseUpdate();

            }
        });



    }

    private void fireBaseUpdate(){
        Firebase ref = new Firebase("https://leaving-now.firebaseio.com/");
        Log.i("fb","done with fb");
        SharedPreferences prefs = getSharedPreferences("com.example.homebase", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Random r = new Random();
        editor.putString("id", String.valueOf(r.nextInt(Integer.MAX_VALUE)+1));
        editor.commit();
        Log.i("i",prefs.getString("id", "null"));

        /*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Log.i("location",String.valueOf(latitude));

        Firebase usersRef = ref.child("Users");
        usersRef.child("Ryan").child("Lat").setValue(String.valueOf(latitude));
        usersRef.child("Ryan").child("Long").setValue(String.valueOf(longitude));*/
        Firebase usersRef = ref.child("Users");
        usersRef.child(prefs.getString("id", "null")).child("Lat").setValue("10000");
        usersRef.child(prefs.getString("id", "null")).child("Long").setValue("2000000");




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
        //make sure that the phone number is larger than 7 digits
        if(numLength > 7){
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNum, null, message, null, null);
        }
    }




}
