package brijesh.hg.eopleapp;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;

public class Transaction_status extends AppCompatActivity {


    RelativeLayout relativeLayout;

    private ImageView traback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_status);

        LottieAnimationView animationView = findViewById(R.id.animation_view);
        animationView.setAnimation("433-checked-done.json");

        relativeLayout = (RelativeLayout) findViewById(R.id.rel);
        traback=findViewById(R.id.traback);



        traback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Transaction_status.this, Main.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (animationDrawable != null && !animationDrawable.isRunning()) {
//            // start the animation
//            animationDrawable.start();
//        }
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (animationDrawable != null && animationDrawable.isRunning()) {
//            // stop the animation
//            animationDrawable.stop();
//        }
//    }

}
