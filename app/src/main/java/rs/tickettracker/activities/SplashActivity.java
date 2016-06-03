package rs.tickettracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;

import rs.tickettracker.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ActiveAndroid.initialize(getApplicationContext());
                Intent startMainScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startMainScreen);
                finish();
            }
        };
        myThread.start();
    }
}
