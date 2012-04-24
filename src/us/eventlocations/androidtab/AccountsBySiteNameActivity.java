package us.eventlocations.androidtab;

import java.util.HashMap;

import us.eventlocations.androidtab.fragments.OnTutSelectedListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountsBySiteNameActivity extends GPSTools implements OnTutSelectedListener{
	 protected SharedPreferences preferences; 
	// private ImageView back_country;
	 private Button bback_country;
	 private  int option;
	 ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	loadGPS();
        Bundle extras = this.getIntent().getExtras();
        option= 0;
        if (extras!=null)
        {
        	option= extras.getInt("option");	
        }
        
		   preferences = getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
		   save_Preference(preferences,"default_row",0);

        
        if (option == Common.SEARCH_BY_COUNTY)
        	setContentView(R.layout.by_site_county);
        else  if (option == Common.SEARCH_BY_SITE_NAME)
        {
        	setContentView(R.layout.by_only_site_name);
      
        	final EditText edittext = (EditText) findViewById(R.id.searchEditText);
        	final ImageView searchImageView = (ImageView) findViewById(R.id.searchImageView);

        	edittext.setOnKeyListener(new OnKeyListener() {

        	    public boolean onKey(View v, int keyCode, KeyEvent event) {

        	        // If the event is a key-down event on the "enter" button

        	        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&

        	            (keyCode == KeyEvent.KEYCODE_ENTER)) {

        	          // Perform action on key press
        	        	//finish();
        	        	
        	        	save_Preference( preferences,"default_search",edittext.getText().toString());
        	         	//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        	        	//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        	        	
        	        	searchBySiteNameClickHandlerMethod(edittext.getText().toString());
        	        	
        	        	/*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        	        	imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);*/
        	        	
        	        	edittext.setFocusable(false);
        	        	edittext.setFocusableInTouchMode(false);
        				
        	        	//edittext.setInputType(InputType.TYPE_NULL);
        	        	//edittext.clearFocus();
        	        	
        	        //	searchImageView.requestFocus();
        	        	//getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
       	        	// simulateKeyStroke(KeyEvent.KEYCODE_BACK, AccountsBySiteNameActivity.this);
        	          return true;

        	        }
        	        

        	        return false;

        	    }

        	});
        	
        }
        else  if (option == Common.SEARCH_SERVICES)
        	setContentView(R.layout.by_site_name);
        else  if (option == Common.SEARCH_CATERERS)
        	setContentView(R.layout.by_site_name);
        else  if (option == Common.FIND_BRIDAL_SHOW)
        	setContentView(R.layout.by_site_bridal);
        else  if (option == Common.HONEYMOON)
        	setContentView(R.layout.by_site_honeymoon);
        else
        	setContentView(R.layout.by_site_honeymoon);
        
        TextView textView = (TextView) findViewById(R.id.textViewTitle1);
        
        if (option == Common.SEARCH_BY_COUNTY)
        	textView.setText(getString(R.string.lb_search_by_county));
        else  if (option == Common.SEARCH_BY_SITE_NAME)
        	textView.setText(getString(R.string.lb_search_by_site_name));
        else  if (option == Common.SEARCH_SERVICES)
        	textView.setText(getString(R.string.lb_search_services));
        else  if (option == Common.SEARCH_CATERERS)
        	textView.setText(getString(R.string.lb_search_caterers));
        else  if (option == Common.FIND_BRIDAL_SHOW)
        	textView.setText(getString(R.string.lb_find_bridal_show));
        else  if (option == Common.HONEYMOON)
        {
        	textView.setText(Common.HONEYMOON_TITLE);
            TextView titleBar = (TextView) findViewById(R.id.honeyoption);
            titleBar.setOnClickListener(mOnclick_honeymoon);
        }
        else{
        	textView.setText(Common.HONEYMOON_TITLE);
            TextView titleBar = (TextView) findViewById(R.id.honeyoption);
            titleBar.setOnClickListener(mOnclick_honeymoon);
        }
        	
        
    }      
    
    private void simulateKeyStroke(int keyCode, Activity parent) {
        injectKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode), parent);
        injectKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyCode), parent);
    }

    private void injectKeyEvent(KeyEvent keyEvent, Activity parent) {
        parent.dispatchKeyEvent(keyEvent);
    }
    private  void save_Preference(SharedPreferences preferences,String label,int value) {
        preferences.edit().putInt(label,value).commit();
    }	
    

    
/*   public boolean onKeyDown(int keyCode, KeyEvent event) {
			
	        switch (keyCode) { 
	        case KeyEvent.KEYCODE_BACK: 
		        {
		        	if (option == Common.SEARCH_BY_SITE_NAME)
		        	{
		        		return false;
		        	}
		        	else
		        	{
		        		finish();
			        	  return true;
		        		
		        	}
		        }
	        	 
	        } 
	        return onKeyDown(keyCode, event); 
	   }*/

	@Override
	public void onTutSelected(Boolean tutUri) {
		// TODO Auto-generated method stub
		bback_country = (Button) findViewById(R.id.back_county);
		bback_country.setVisibility(View.VISIBLE);
		bback_country.setOnClickListener(mOnclick_back_country);
	} 
	
	
	@Override
	public void onTutSelected(String Title) {
		// TODO Auto-generated method stub
		//TextView textView = (TextView) findViewById(R.id.textViewTitle2);
		//textView.setText(Title);
		
		bback_country = (Button) findViewById(R.id.back_county);
		if (bback_country !=null)
		{
			bback_country.setVisibility(View.VISIBLE);
			bback_country.setOnClickListener(mOnclick_back_country);
			
			if (Title!=null && Title.length()>0)
				bback_country.setText("Counties: "+Title);
	    }
	}
	
    private OnClickListener mOnclick_back_country = new OnClickListener()
	{
        public void onClick(View v)
        {
        	bback_country.setVisibility(View.GONE);
        	searchByCountyClickHandler();
        }
    };
    
    private OnClickListener mOnclick_honeymoon = new OnClickListener()
	{
        public void onClick(View v)
        {
            
            /*if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_LANDSCAPE)
            {*/
            	 Intent mainIntent = new Intent(AccountsBySiteNameActivity.this, WinHoneymoonActivity.class);
             	 AccountsBySiteNameActivity.this.startActivity(mainIntent);
             /*}
            else
            {
            	 Intent mainIntent = new Intent(AccountsBySiteNameActivity.this, WinHoneymoonActivity_Lands.class);
             	 AccountsBySiteNameActivity.this.startActivity(mainIntent);
            }*/
        }
    };
    
    
    

	
    public void searchByCountyClickHandler(){
 	   dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);    	
  	Thread splashThread = new Thread() {
          @Override
          public void run() {
         	 finish();
         	 Bundle bundle = new Bundle();
         	 bundle.putInt("option", Common.SEARCH_BY_COUNTY);
          	 Intent mainIntent = new Intent(AccountsBySiteNameActivity.this, AccountsBySiteNameActivity.class);
          	 mainIntent.putExtra("which", "GetCounties");
          	 mainIntent.putExtras(bundle);
          	 AccountsBySiteNameActivity.this.startActivity(mainIntent);
          	 dialog.dismiss();
          }
       };
       splashThread.start();
  }
    
    private void callDialogDrivingDirection(String address1)
    {
    	Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
    	//Uri u =Uri.parse("http://maps.google.com/maps?saddr="+currLatitude+","+currLongitude+"&daddr="+dCurrLatitude+","+dCurrLongitude);
    	//Uri u =Uri.parse("geo:0,0?q="+address);
    	//Uri u =Uri.parse("http://maps.google.com/maps?daddr="+address1);
    	

    	
    	Uri u =Uri.parse("http://maps.google.com/maps?saddr="+currLatitude+","+currLongitude+"&daddr="+address1);
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
    
	public String currLatitude ="";
	public String currLongitude="";
	@Override
	public void onCallGPS(String address) {
		// TODO Auto-generated method stub
		callDialogDrivingDirection(address);
	}


	@Override
	public void onDialogComingSoon() {
		// TODO Auto-generated method stub
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Coming Soon")
				.setCancelable(false)
				.setPositiveButton(getString(R.string.lb_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
						;
		AlertDialog alert = builder.create();
		alert.show();
	}
}


/* class MyApp extends Application {

	  private String currLatitude,currLongitude;

	  public String getCurrLatitude(){
	    return currLatitude;
	  }
	  public void setCurrLatitude(String s){
		  currLatitude = s;
	  }
	}*/