package gr.uth.cardshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import gr.uth.cardshop.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static int LIMIT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //color for notifications tab
        getWindow().setStatusBarColor(getResources().getColor(R.color._color));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },LIMIT);
    }
}