package us.eventlocations.androidtab;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class RulesRegulationsActivity extends GPSTools {
//http://www.locationsmagazine.com/win/rules
	 ProgressDialog dialog;
	 private Configuration c;
	 protected SharedPreferences preferences; 
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_regulations);
        
    	TextView rules_55 = (TextView)findViewById(R.id.rules_55);
    	rules_55.setOnClickListener(mOnclick_rules_directions);
      	
    	TextView rules_52 = (TextView)findViewById(R.id.rules_52);
    	rules_52.setOnClickListener(mOnclick_rules_url);
      	loadGPS();
    }
    
    private OnClickListener mOnclick_rules_url = new OnClickListener()
	{
        public void onClick(View v)
        {
        	Intent i = new Intent(Intent.ACTION_VIEW);
        	Uri u = Uri.parse(Common.WEBPAGE_WIN_HONEYMOON);
        	i.setData(u);
        	startActivity(i);
        }
    };

    private OnClickListener mOnclick_rules_directions = new OnClickListener()
	{
        public void onClick(View v)
        {
        	/*Intent i = new Intent(Intent.ACTION_VIEW);
        	Uri u = Uri.parse(Common.WEBPAGE_WIN_HONEYMOON);
        	i.setData(u);
        	startActivity(i);*/
        	callDialogDrivingDirection();
        }
    };
    
    private void callDialogDrivingDirection()
    {
    	Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
    	String address="124 East 79th St., NY NY 10075";
    	//Uri u =Uri.parse("http://maps.google.com/maps?saddr="+currLatitude+","+currLongitude+"&daddr="+dCurrLatitude+","+dCurrLongitude);
    	//Uri u =Uri.parse("geo:0,0?q="+address);
    	Uri u =Uri.parse("http://maps.google.com/maps?saddr="+currLatitude+","+currLongitude+"&daddr="+address);
    	//Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+accountAddress.getText().toString()));
    	
    	intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
    	intent.setData(u);
    	startActivity(intent);

    }
    
    private void loadGPS()
    {
    	  //HashMap hashMap = loadLocationGPS2();
    	HashMap hashMap = loadLocationGPS2_emulator();
    	
          
          if (hashMap!=null && hashMap.get("dCurrLatitude")!=null)
          {
        	  currLatitude =(String) hashMap.get("dCurrLatitude").toString();
        	  currLongitude = (String) hashMap.get("dCurrLongitude").toString();
          }
          else
          {
          	hashMap = getTriangulationData();
          	currLatitude =(String) hashMap.get("dCurrLatitude").toString();
          	currLongitude = (String) hashMap.get("dCurrLongitude").toString();
          }

    }
}
