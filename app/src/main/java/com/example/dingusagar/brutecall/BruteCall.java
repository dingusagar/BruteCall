package com.example.dingusagar.brutecall;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class BruteCall {

    int numberOfCalls_Called = 0;
    int totalNoOfCalls = 0;
    boolean bruteCallCancelled = false;
    String phoneNumber;
    Context context;

    public BruteCall(Context context ) {
        this.context = context;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getNumberOfCalls_Called() {
        return numberOfCalls_Called;
    }

    public void setNumberOfCalls_Called(int numberOfCalls_Called) {
        this.numberOfCalls_Called = numberOfCalls_Called;
    }

    public int getTotalNoOfCalls() {
        return totalNoOfCalls;
    }

    public void setTotalNoOfCalls(int totalNoOfCalls) {
        this.totalNoOfCalls = totalNoOfCalls;
    }

    public boolean isBruteCallCancelled() {
        return bruteCallCancelled;
    }

    public void setBruteCallCancelled(boolean bruteCallCancelled) {
        this.bruteCallCancelled = bruteCallCancelled;
    }



    public void callAfter(final int duration) {

        if(numberOfCalls_Called >= totalNoOfCalls  || bruteCallCancelled) {
            numberOfCalls_Called = 0;
            bruteCallCancelled = false;
            return;
        }

        Thread callingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                startTimer(duration);

                if(numberOfCalls_Called >= totalNoOfCalls  || bruteCallCancelled) {
                    numberOfCalls_Called = 0;
                    bruteCallCancelled = false;
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                Log.e("LOGGING 123", "calling 1 ");
                if (checkCallingPermission()) {
                    context.startActivity(callIntent);

                }

            }
        });
        callingThread.start();

        ((MainActivity)context).updateUIforNumberOfTimesCalled("Called "+ numberOfCalls_Called+" / "+totalNoOfCalls + " times");




    }



    private void startTimer(final int seconds)
    {
        Log.e("LOGTAG","timer started ");

        ((MainActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)context).updateUIforSecondaryInfo("Next call in "+seconds +"seconds. \n You many cancel anytime");

            }
        });

        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    boolean checkCallingPermission()
    {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(context, "No Permission to call", Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }


}
