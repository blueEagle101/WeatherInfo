package com.homecredit.weatherinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.logging.Handler;

public class SplashScreen extends Activity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    loadMain();
                }
            }
        };
        timer.start();

    }

    private void loadMain(){
        Intent main = new Intent(this, MainScreen.class);
        startActivity(main);
    }

}
