package com.example.dingusagar.brutecall;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Dingu Sagar on 18-08-2016.
 */
public class callService extends Service {

    String number;
    boolean bruteCallCancelled = false;
    int numberOfCalls_Called = 0;
    int totalNumberOfCalls;
    int serviceID;

    PhoneCallListener phoneListener;
    TelephonyManager telephonyManager;


    final public class myRunnable implements Runnable {
        @Override
        public void run() {
            Looper.prepare();
             phoneListener = new PhoneCallListener();
             telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            callAgain(number);
            Looper.loop();


        }
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
//    public callService(String name) {
//        super(name);
//    }
    @Override
    public void onCreate() {

        Log.e("DINGU", "service started : oncreate");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("DINGU", "service started : onstartcommand");
        serviceID = startId;
        number = intent.getStringExtra("number");
        totalNumberOfCalls = intent.getIntExtra("numberOfCalls", 0);

        if (numberOfCalls_Called == totalNumberOfCalls)
            stopSelf(serviceID);

        try {
            Thread myThread = new Thread(new myRunnable());
            myThread.start();
        } catch (Exception e) {
            Log.e("Dingu", "Thread error");
        }
        return START_STICKY;
    }

//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if(numberOfCalls_Called == totalNumberOfCalls)
//            stopSelf(serviceID);
//
//        PhoneCallListener phoneListener = new PhoneCallListener();
//        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Brute call is Stopped", Toast.LENGTH_SHORT).show();
        Log.e("DINGU", "service destroyed");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void callAgain(String number) {
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




    //-----------------------------phoneClass-----------------------------

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







                        callAgain(number);
                        Log.e(LOG_TAG, "calling again");

                    }
                }else
                {
                    Log.e(LOG_TAG, "IDLE");
                }


            }
        }
    }
    //-----------------------------end of phoneClass-----------------------------



}
