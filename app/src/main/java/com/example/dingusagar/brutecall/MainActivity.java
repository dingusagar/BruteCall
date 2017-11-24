package com.example.dingusagar.brutecall;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

    //temp
    Button bcancel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberText = (EditText)findViewById(R.id.setNumberText);
        noOfCallsText = (EditText)findViewById(R.id.setNoOfCalls);
        callButton = (Button)findViewById(R.id.buttonCall);
        phoneBookButton = (Button)findViewById(R.id.phoneBookButton);
        callButton.setOnClickListener(this);


        bcancel = (Button)findViewById(R.id.bcancel);
        bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               stopService(callServiceIntent);
                Log.e("DINGU", "cancel call clicked");

            }
        });

        phoneListener = new PhoneCallListener();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);




    }

// when call button is pressed
    @Override
    public void onClick(View v) {

        bruteCallCancelled = false;
        totalNoOfCalls = Integer.parseInt(noOfCallsText.getText().toString());
        callAgain(numberText.getText().toString());
        Log.e("DINGU","call clicked");
    }




    public void callAgain(String number) {

        if(numberOfCalls_Called >= totalNoOfCalls) {
            bruteCallCancelled = true;
            return;
        }
        Toast.makeText(getApplicationContext(), "Calling " + number, Toast.LENGTH_SHORT).show();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        Log.e("LOGGING 123", "calling 1 ");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "No Permission to call", Toast.LENGTH_SHORT).show();

        }else
            startActivity(callIntent);




    }













    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.e(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.e(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK


                if(isPhoneCalling)
                {
                    Log.e(LOG_TAG,"CALL ENDED");
                    isPhoneCalling = false;


//                    if(numberOfCalls_Called == 0)  // going to info fragment for only the first time
//                    {
//                            calledInfoFragment = new CalledInfoFragment();
//                            ControlsFragment controlsFragment = (ControlsFragment)manager.findFragmentById(R.id.fragment2);
//                            FragmentTransaction transaction = manager.beginTransaction();
//
//                            transaction.replace(R.id.gameChangingLayout,calledInfoFragment);
//                            Log.e(LOG_TAG,"replaced with new fragment ");



//                            Log.e(LOG_TAG,"goiing to remove old fragment ");
//                            transaction.remove(controlsFragment);
//                            Log.e(LOG_TAG, "removed old fragment ");
//                            transaction.add(R.id.fancyLayout, calledInfoFragment);
//                            Log.e(LOG_TAG, "added new fragment");
//                            transaction.commit();
//                            Log.e(LOG_TAG,"transaction committed ");


//                    }
                    numberOfCalls_Called++;
//                    calledInfoFragment.diplayinfo(numberToCall,numberOfCalls_Called);

                    if(!bruteCallCancelled)
                    // cancel button is not pressed
                    {
                        Log.e(LOG_TAG,"timer started ");

                        synchronized (this) {
                            try {
                                wait(15000);
                            } catch (InterruptedException e) {
                                Log.e(LOG_TAG, "waiting error");
                            }
                        }


                        Log.e(LOG_TAG,"timer ended ");

                        Toast.makeText(getApplicationContext(), "Calling again in" + 5 , Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Calling again in" + 4 , Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Calling again in" + 3 , Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Calling again in" + 2 , Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Calling again in" + 1 , Toast.LENGTH_SHORT).show();







                        callAgain(numberText.getText().toString());
                        Log.e(LOG_TAG, "calling again");

                    }
                }else
                {
                    Log.e(LOG_TAG, "IDLE");
                }


            }
        }
    }
}
