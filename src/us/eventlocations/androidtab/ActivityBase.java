package us.eventlocations.androidtab;
//http://mobiforge.com/developing/story/using-google-maps-android
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class ActivityBase extends Activity {
    /** Called when the activity is first created. */
	protected String urlShared;
	protected double dCurrLatitude,dCurrLongitude;
    protected int width,height;
	protected LocationManager lm;
	protected Location loc;
	protected LocationListener locListenD;
	protected long lMinTime;
	protected float fMinDistance=1000.0f;
	private static final String TAG = "ActivityBase";
	protected SharedPreferences preferences; 
	protected  static final String PREFERENCES_INITIALDATAFILE = "initialdata";
	protected String currLatitude ="";
	protected String currLongitude="";
	protected  int place;
    protected static ProgressDialog myProgressDialog,myProgressDialog2;
    ProgressDialog dialog;
	
		protected boolean loadLocationGPS(){
	        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


	        if (lm== null)
	        {
	        	Settings.System.putString(getContentResolver(),Settings.System.LOCATION_PROVIDERS_ALLOWED,LocationManager.GPS_PROVIDER);
	        	Intent intent = new Intent(Intent.ACTION_PROVIDER_CHANGED);
	        	sendBroadcast(intent);
	        }
	        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locListenD);
	        else if(lm!=null && !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	        	Settings.System.putString(getContentResolver(),Settings.System.LOCATION_PROVIDERS_ALLOWED,LocationManager.GPS_PROVIDER);
	        	Intent intent = new Intent(Intent.ACTION_PROVIDER_CHANGED);
	        	sendBroadcast(intent);
	        }
	        
	        
	        // connect to the GPS location service
	        loc = lm.getLastKnownLocation("gps");
	        if(loc == null) 
	        	 loc = lm.getLastKnownLocation("network");         

	        if(loc != null){
				dCurrLatitude = loc.getLatitude();
				dCurrLongitude = loc.getLongitude();
				lMinTime = loadSettingsUpdateFrequency();
				//if GPS was not on during onCreate, add locationUpdates now
				if(locListenD==null){
					locListenD = new DispLocListener();
					//wait minimum lMinTime seconds and/or fMinDistance meters before updating location
			        lm.requestLocationUpdates("gps", lMinTime, fMinDistance, locListenD);
			    }
				getAddressFromPosition();
				return(true);
	    	}
	    	else{
	    		messageGPSOFF();
	    		return(false);
			}
	    }
		
		protected void messageGPSOFF(){
	    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(this); 
	    	alertDialog.setMessage(getResources().getString(R.string.error_gpsturnoff));
	    	alertDialog.setTitle(getResources().getString(R.string.error_gpsturnoffTitle));
	    	alertDialog.setNeutralButton(getResources().getString(R.string.btn_cancel),		
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{
					showSettingLocation();
				}
			}).show();
		}
		
	    private void showSettingLocation()
	    {
	    	startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	    }
	    
	    private void gpsON()
	    {
	    	Intent intent = new Intent(Intent.ACTION_PROVIDER_CHANGED);
	    	sendBroadcast(intent);
	    }
	    
		protected void getAddressFromPosition() {
	    	Geocoder gc = new Geocoder(this, Locale.getDefault()); 
	    	List<Address> addressResult = null;
	    	Address resultAddress = null;
	        try {
				addressResult = gc.getFromLocation(dCurrLatitude,dCurrLongitude,1);
				if (!addressResult.isEmpty())
				{
			        resultAddress = addressResult.get(0);
			        String text = " "+resultAddress.getAddressLine(0)+", "+resultAddress.getAddressLine(1); 
			        Log.d("Address", text);
			    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(" ",e.getMessage());
			}  
			finally{
				addressResult = null;
				gc = null;
				resultAddress = null;
			}
	    }

		
		protected class DispLocListener implements LocationListener{
	    	public void onLocationChanged(Location location){
	    		Log.v(TAG,"her");
	    		//TODO Set buttons normal
	    	}
	    	public void onProviderDisabled(String provider){
	    		//messageGPSOFF();
	    	}
	    	public void onProviderEnabled(String provider){
	    		//TODO
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras)	{
	    		//TODO
	    	}
	    }
		
		protected long loadSettingsUpdateFrequency()
	    {
	    	return 60l;
	    }
	
	    protected HashMap getTriangulationData()
	    {
	    	HashMap hashMap = new HashMap();
	    	double lat=0;
	    	double lng=0;
	        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        Criteria criteria=new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	        criteria.setAltitudeRequired(false);
	        criteria.setBearingRequired(false);
	        criteria.setCostAllowed(true);
	        criteria.setPowerRequirement(Criteria.POWER_HIGH);
	        String provider=lm.getBestProvider(criteria, true);
	        LocationListener locationListener = new DispLocListener();
	        lm.requestLocationUpdates(provider, 1000, 0,locationListener);

			Location location=lm.getLastKnownLocation(provider);
			if(location!=null)
			{
			    lat=location.getLatitude();
			    lng=location.getLongitude();
			}
			
	    	hashMap.put("dCurrLatitude", lat);
	    	hashMap.put("dCurrLongitude", lng);
			
			return hashMap;
	    }

		
 		protected boolean haveInternet(){ 

	    	NetworkInfo info=(NetworkInfo)( (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

	    	if(info==null || !info.isConnected()){ 
	    		return false; 
	    	} 
	    	if(info.isRoaming()){ 
	    	//here is the roaming option you can change it if you want to disable internet while roaming, just return false 
	    		return true; 
	    	} 
	    	return true; 
	    }


		
		   private OnDismissListener myDialogDismissListener = new    OnDismissListener(){ 
		        public void onDismiss(DialogInterface dialog) { 
		        	 dialog.dismiss();
		        } 
		    }; 

		    protected static int calculateDistanceByHaversineFormulaMt(double lon1, double lat1,double lon2, double lat2) {

		    		double earthRadius = 6371; // km

		    		lat1 = Math.toRadians(lat1);
		    		lon1 = Math.toRadians(lon1);
		    		lat2 = Math.toRadians(lat2);
		    		lon2 = Math.toRadians(lon2);

		    		double dlon = (lon2-lon1);
		    		double dlat = (lat2-lat1);

		    		double sinlat = Math.sin(dlat / 2);
		    		double sinlon = Math.sin(dlon / 2);

		    		double a = (sinlat * sinlat) + Math.cos(lat1)*Math.cos(lat2)*(sinlon*sinlon);
		    		double c = 2 * Math.asin (Math.min(1.0, Math.sqrt(a)));

		    		double distanceInMeters = earthRadius * c * 1000;

		    		return (int)distanceInMeters;

		    		}		    
		    
		    protected static double CalculationByHaversineKm (Double latitude1, Double longitude1,Double latitude2, Double longitude2)
		       {
		    	 
		          double lat1 = latitude1;
		          double lat2 = latitude2;
		          double lon1 = longitude1;
		          double lon2 = longitude2;
		          double dLat = Math.toRadians(lat2-lat1);
		          double dLon = Math.toRadians(lon2-lon1);
		          double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		          Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		          Math.sin(dLon/2) * Math.sin(dLon/2);
		          double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		          return Radius * c;
		       }
		    
		    protected double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
		        float pk = (float) (180/3.14169);

		        float a1 = lat_a / pk;
		        float a2 = lng_a / pk;
		        float b1 = lat_b / pk;
		        float b2 = lng_b / pk;

		        float t1 = FloatMath.cos(a1)*FloatMath.cos(a2)*FloatMath.cos(b1)*FloatMath.cos(b2);
		        float t2 = FloatMath.cos(a1)*FloatMath.sin(a2)*FloatMath.cos(b1)*FloatMath.sin(b2);
		        float t3 = FloatMath.sin(a1)*FloatMath.sin(b1);
		        double tt = Math.acos(t1 + t2 + t3);
		       
		        return 6366000*tt;
		    }
		    
		    private static double Radius = 6371;



		    
		
		    
		    protected static String get_Preferences(SharedPreferences preferences,String value) {
		        return   preferences.getString(value,"");
		      }
		    protected  void save_Preference(SharedPreferences preferences,String value,String dateSelected) {
		        preferences.edit().putString(value, dateSelected).commit();
		    }
	
		    /**********************/
		    
		    public void searchBySiteNameClickHandler(View view){
		    	 searchBySiteNameClickHandlerMethod();
		    }
		    
		    
		    protected void searchBySiteNameClickHandlerMethod()
		    {

		    	if (haveInternet())
		    	{    	
			   	   dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);    	
			    	Thread splashThread = new Thread() {
			            @Override
			            public void run() {
			           	 	Bundle bundle = new Bundle();
			           	 	bundle.putInt("option", Common.SEARCH_BY_SITE_NAME);
			
			            	 Intent mainIntent = new Intent(ActivityBase.this, AccountsBySiteNameActivity.class);
			            	 mainIntent.putExtra("which", "GetAccounts");
			            	 mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			            	 mainIntent.putExtras(bundle);
			            	 ActivityBase.this.startActivity(mainIntent);
			            	 dialog.dismiss();
			            }
			         };
			         splashThread.start();
		    	  }
			     	else
			    	{
			    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    		builder.setMessage(
			    				getString(R.string.label_nointernet))
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
		    
		    protected void searchBySiteNameClickHandlerMethod(final String textNew)
		    {
		    	
		    	//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		    	
		    	InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);       
		        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),  InputMethodManager.HIDE_IMPLICIT_ONLY);
		    	
		   	 	//InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		   	 	//imm.hideSoftInputFromWindow(this.getApplicationWindowToken(), 0);		    	
		    	
		    	//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    	//imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		    	
		    	
		    	if (haveInternet())
		    	{    	
			   	   dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);    	
			    	Thread splashThread = new Thread() {
			            @Override
			            public void run() {
			           	 	Bundle bundle = new Bundle();
			           	 	bundle.putInt("option", Common.SEARCH_BY_SITE_NAME);
			
			            	 Intent mainIntent = new Intent(ActivityBase.this, AccountsBySiteNameActivity.class);
			            	 mainIntent.putExtra("which", "GetAccounts");
			            	 mainIntent.putExtra("text", textNew);
			            	 mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			            	 mainIntent.putExtras(bundle);
			            	 ActivityBase.this.startActivity(mainIntent);
			            	 dialog.dismiss();
			            }
			         };
			         splashThread.start();
		    	  }
			     	else
			    	{
			    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    		builder.setMessage(
			    				getString(R.string.label_nointernet))
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
		    
		    
		    public void searchByCountyClickHandler(View view){
		    	
		    	if (haveInternet())
		    	{
		    	   dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);    	
		     	Thread splashThread = new Thread() {
		             @Override
		             public void run() {
		            	 
		            	 Bundle bundle = new Bundle();
		            	 bundle.putInt("option", Common.SEARCH_BY_COUNTY);
		             	 Intent mainIntent = new Intent(ActivityBase.this, AccountsBySiteNameActivity.class);
		             	 mainIntent.putExtra("which", "GetCounties");
		             	 mainIntent.putExtras(bundle);
		             	 ActivityBase.this.startActivity(mainIntent);
		             	 dialog.dismiss();
		             }
		          };
		          splashThread.start();
		    	}
		    	else
		    	{
		    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    		builder.setMessage(
		    				getString(R.string.label_nointernet))
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
		    
		    
		    public void searchByServicesClickHandler(View view){
		    	
		    	if (haveInternet())
		    	{        	
			   	   dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);    	
			    	Thread splashThread = new Thread() {
			            @Override
			            public void run() {
			            	
			           	 	Bundle bundle = new Bundle();
			           	 	bundle.putInt("option", Common.SEARCH_SERVICES);
			
			            	 Intent mainIntent = new Intent(ActivityBase.this, AccountsBySiteNameActivity.class);
			            	 mainIntent.putExtra("which", "GetServices");
			            	 mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            	 mainIntent.putExtras(bundle);
			            	 
			            	 ActivityBase.this.startActivity(mainIntent);
			            	 
			            	 dialog.dismiss();
			            }
			         };
			         splashThread.start();
		    	}
		    	else
		    	{
		    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    		builder.setMessage(
		    				getString(R.string.label_nointernet))
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
		    
		    public void searchByCaterersClickHandler(View view){
		    	if (haveInternet())
		    	{         	
			    	   dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);    	
			    	   Thread splashThread = new Thread() {
			             @Override
			             public void run() {
			            	 
			            	 	Bundle bundle = new Bundle();
			               	 	bundle.putInt("option", Common.SEARCH_CATERERS);
			            	 
			             	 Intent mainIntent = new Intent(ActivityBase.this, AccountsBySiteNameActivity.class);
			             	 mainIntent.putExtra("which", "GetCaterers");
			             	 mainIntent.putExtras(bundle);
			             	 ActivityBase.this.startActivity(mainIntent);
			             	 dialog.dismiss();
			             }
			          };
			          splashThread.start();
		    	}
		    	else
		    	{
		    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    		builder.setMessage(
		    				getString(R.string.label_nointernet))
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
		    
		    
		    public void findABridalShowClickHandler(View view){
		    	if (haveInternet())
		    	{            	
		 	   dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);    	
			  	Thread splashThread = new Thread() {
			          @Override
			          public void run() {
			        	  
		          	 	Bundle bundle = new Bundle();
		           	 	bundle.putInt("option", Common.FIND_BRIDAL_SHOW);
		           	 
		           	 	Intent mainIntent = new Intent(ActivityBase.this, AccountsBySiteNameActivity.class);
		           	 	mainIntent.putExtra("which", "GetBridalCompanies");
		           	 	mainIntent.putExtras(bundle);
		           	 	ActivityBase.this.startActivity(mainIntent);
			          	dialog.dismiss();
			          }
			       };
			       splashThread.start();
		    	}
		    	else
		    	{
		    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    		builder.setMessage(
		    				getString(R.string.label_nointernet))
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

		    public void winAHoneymoonClickHandler(View view){
		    	if (haveInternet())
		    	{                	
		 	   dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);    	
			  	Thread splashThread = new Thread() {
			          @Override
			          public void run() {

			          	 	Bundle bundle = new Bundle();
			           	 	bundle.putInt("option", Common.HONEYMOON);
			           	 
			           	 	Intent mainIntent = new Intent(ActivityBase.this, AccountsBySiteNameActivity.class);
			           	 	mainIntent.putExtra("which", Common.GetMobileEnabledHoneymoonAccounts);
			           	 	mainIntent.putExtras(bundle);
			           	 	ActivityBase.this.startActivity(mainIntent);
			        	  
			          	 dialog.dismiss();
			          }
			       };
			       splashThread.start();
		    	}
		    	else
		    	{
		    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    		builder.setMessage(
		    				getString(R.string.label_nointernet))
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
		    
		    
		    
		 
		    
   }