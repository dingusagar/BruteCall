package com.example.dingusagar.brutecall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.skyfishjy.library.RippleBackground;

public class Main2Activity extends AppCompatActivity {

    AnimationState state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

         state = AnimationState.OFF;

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.equals(AnimationState.OFF)){
                    rippleBackground.startRippleAnimation();
                    state = AnimationState.ON;
                }
                else {
                    rippleBackground.stopRippleAnimation();
                    state = AnimationState.OFF;
                }

            }
        });
    }

}
