package rs.tickettracker.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;

import rs.tickettracker.R;
import rs.tickettracker.helpers.GlobalStaticValuesHelper;

/**
 * Splash screen activity. Shows application logo when application is started.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                ActiveAndroid.initialize(getApplicationContext());
                Intent startMainScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startMainScreen);
                finish();
            }
        }, GlobalStaticValuesHelper.SPLASH_SCREEN_LENGTH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
