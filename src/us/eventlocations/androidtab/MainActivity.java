package us.eventlocations.androidtab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends ActivityBase {

	
	 private Configuration c;
	 protected SharedPreferences preferences; 
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		  
    	Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();  
        //int height = display.getHeight();

        
        if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_LANDSCAPE)
        {
        	 setContentView(R.layout.main);
        }
        else
        {
        	setContentView(R.layout.main_portrait);
        }
        
        
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(mOnclick_button2);  
        
        
        
          ImageView about = (ImageView) findViewById(R.id.info);
          about.setOnClickListener(mOnclick_gotoAbout);
		   preferences = getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
		   save_Preference(preferences,"default_row",0);
		   
	        
			   save_Preference( preferences,"default_search",""); 
    }
    
    private OnClickListener mOnclick_button2 = new OnClickListener()
	{
        public void onClick(View v)
        {
        	searchBySiteNameClickHandlerMethod();
        }
    };
    
    private OnClickListener mOnclick_gotoAbout = new OnClickListener()
	{
        public void onClick(View v)
        {
         	 Intent mainIntent = new Intent(MainActivity.this, AboutActivity.class);
         	 MainActivity.this.startActivity(mainIntent);
        }
    };
    
    private  void save_Preference(SharedPreferences preferences,String label,int value) {
        preferences.edit().putInt(label,value).commit();
    }	
   
    protected  void save_Preference(SharedPreferences preferences,String value,String dateSelected) {
        preferences.edit().putString(value, dateSelected).commit();
    }

  

    
}
