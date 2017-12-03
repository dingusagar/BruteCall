package com.example.dingusagar.brutecall;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneCallListener extends PhoneStateListener {

    private boolean phoneCalled = false;



    BruteCall bruteCall;

    String LOG_TAG = "LOGGING 123";
    Context context;

    public PhoneCallListener(Context context,BruteCall bruteCall) {
        this.bruteCall = bruteCall;
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        if (TelephonyManager.CALL_STATE_RINGING == state) {
            // phone ringing
            Log.e(LOG_TAG, "RINGING, number: " + incomingNumber);
        }

        if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
            // active
            Log.e(LOG_TAG, "OFFHOOK");

            phoneCalled = true;
        }

        if (TelephonyManager.CALL_STATE_IDLE == state) {
            // run when class initial and phone call ended,
            // need detect flag from CALL_STATE_OFFHOOK


            if(phoneCalled)
            {
                Log.e(LOG_TAG,"CALL ENDED");
                phoneCalled = false;
                bruteCall.setNumberOfCalls_Called(bruteCall.getNumberOfCalls_Called()+1);

                bruteCall.callAfter(15);

            }


        }
    }
}