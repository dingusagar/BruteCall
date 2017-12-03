package com.example.dingusagar.brutecall;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dingusagar.brutecall.Settings.SettingsActivity;

import java.util.ArrayList;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity{

    EditText numberText = null;
    EditText noOfCallsText = null;
    Button callButton =null;
    Button phoneBookButton = null;

     Intent callServiceIntent ;
    int numberOfCalls_Called = 0;
    int totalNoOfCalls = 0;
    boolean bruteCallCancelled = false;

    PhoneCallListener phoneListener;
    TelephonyManager telephonyManager;
    BruteCall bruteCall;
    ContactNumbersListDialog contactNumbersListDialog;

    //temp
    Button bcancel;
    private TextView infoText,secondaryInfoText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberText = (EditText)findViewById(R.id.setNumberText);
        noOfCallsText = (EditText)findViewById(R.id.setNoOfCalls);
        callButton = (Button)findViewById(R.id.buttonCall);
        phoneBookButton = (Button)findViewById(R.id.phoneBookButton);
        infoText = (TextView)findViewById(R.id.xCallsCalled);
        secondaryInfoText = (TextView) findViewById(R.id.secondaryInfo);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCallButtonClicked();
            }
        });


        bcancel = (Button)findViewById(R.id.bcancel);
        bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               bruteCall.setBruteCallCancelled(true);
               secondaryInfoText.setText("Cancelled Remaining Calls !!");
                Log.e("DINGU", "cancel call clicked");

            }
        });

        phoneBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent,1);
            }
        });


        bruteCall = new BruteCall(this);
        phoneListener = new PhoneCallListener(this,bruteCall);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);




    }

    private void onCallButtonClicked() {

        secondaryInfoText.setText("");
        String num = numberText.getText().toString();
        String noOfCalls = noOfCallsText.getText().toString();
        if(num.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please enter a phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(noOfCalls.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please enter the number of calls", Toast.LENGTH_SHORT).show();
            return;

        }

        bruteCall.setPhoneNumber(num);
        bruteCall.setTotalNoOfCalls(Integer.parseInt(noOfCalls));
        bruteCall.callAfter(0);
    }



    public void updateUIforNumberOfTimesCalled(String message)
    {
        infoText.setText(message);
    }

    public void updateUIforNumberFromContactList(String num){
        numberText.setText(num);
    }

    public void updateUIforSecondaryInfo(String message){
        secondaryInfoText.setText(message);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1 :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();

                    Cursor cur =  getContentResolver().query(contactData, null, null, null, null);
                    if (cur.getCount() > 0) {// thats mean some resutl has been found
                        if(cur.moveToNext()) {
                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            Log.e("Names", name);

                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                            {

                                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                                ArrayList<String> numberList = new ArrayList<>();
                                while (phones.moveToNext()) {
                                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    if(!numberList.contains(phoneNumber))
                                        numberList.add(phoneNumber);
                                }
                                phones.close();

                                contactNumbersListDialog = new ContactNumbersListDialog(this,numberList);
                                contactNumbersListDialog.setupDialog();
                                contactNumbersListDialog.showDialog();
                            }

                        }
                    }
                    cur.close();
                }
                break;
        }

    }

}
