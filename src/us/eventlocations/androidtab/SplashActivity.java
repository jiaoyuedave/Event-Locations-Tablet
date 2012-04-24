package us.eventlocations.androidtab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        
        Thread splashThread = new Thread() {
            @Override
            public void run() {
               try {
                  int waited = 0;
                  while (waited < 3000) {
                     sleep(100);
                     waited += 100;
                  }
               } catch (InterruptedException e) {
                  // do nothing
               } finally {
                  Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                  SplashActivity.this.startActivity(mainIntent);
                  SplashActivity.this.finish();  
               }
            }
         };
         splashThread.start();
    }
    
}