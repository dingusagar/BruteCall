package com.example.dingusagar.brutecall;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

       callServiceIntent = new Intent(this,callService.class);

        bcancel = (Button)findViewById(R.id.bcancel);
        bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               stopService(callServiceIntent);
                Log.e("DINGU", "cancel call clicked");

            }
        });




    }

// when call button is pressed
    @Override
    public void onClick(View v) {

        callServiceIntent = new Intent(this,callService.class);
        callServiceIntent.putExtra("number",numberText.getText().toString());
        callServiceIntent.putExtra("numberOfCalls", Integer.parseInt(noOfCallsText.getText().toString()));
        startService(callServiceIntent);
        Log.e("DINGU","call clicked");
    }
}
