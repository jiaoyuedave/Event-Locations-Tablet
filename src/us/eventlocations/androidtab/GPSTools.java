package us.eventlocations.androidtab;


import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

public class GPSTools extends ActivityBase  {
	
	protected LocationManager lm;
	private long lMinTime;
	private Location loc;
	protected LocationListener locListenD;
	private float fMinDistance=1000.0f;
	private int mSatellites = 0;
	
	public static double distanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
		double pk = (double) (180/3.14169);

	    double a1 = lat_a / pk;
	    double a2 = lng_a / pk;
	    double b1 = lat_b / pk;
	    double b2 = lng_b / pk;

	    double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
	    double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
	    double t3 = Math.sin(a1)*Math.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);
	    return 6366000*tt;
	}
	
	   public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	      

	    }
	protected void removeGPSListener()
	{
	   lm.removeGpsStatusListener(mStatusListener);
	}
	   
    public HashMap loadLocationGPS2_emulator()
    {
    	try{
	    	HashMap hashMap = new HashMap();
	        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	        
	        
	            // connect to the GPS location service
		        loc = lm.getLastKnownLocation("gps");
		        if(loc == null) 
		        	 loc = lm.getLastKnownLocation("network");         
		
		        if(loc != null){
		        	hashMap = new HashMap();
		        	hashMap.put("dCurrLatitude", loc.getLatitude());
		        	hashMap.put("dCurrLongitude", loc.getLongitude());
		        	//hashMap.put("speedCalculate", loc.getSpeed());
					lMinTime = 60000*2; //each 2 minute
					//if GPS was not on during onCreate, add locationUpdates now
					if(locListenD==null){
						locListenD = new DispLocListener();
						//wait minimum lMinTime seconds and/or fMinDistance meters before updating location
				        lm.requestLocationUpdates("gps", lMinTime, fMinDistance, locListenD);
				    }
					
		    	}
		        
	        return hashMap;

    	}
    	catch(Exception e)
    	{
    		System.out.println(" "+e.getMessage());
	        return null;
    	}
    }

	
	
    public HashMap loadLocationGPS2()
    {
    	try{
	    	HashMap hashMap = new HashMap();
	        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	        
	        
	        if (lm== null)
	        {
	        	messageGPSOFF();
	        }
	        else if(lm!=null && !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

	        	messageGPSOFF();
	        }
	        else
	        {
		        // connect to the GPS location service
		        loc = lm.getLastKnownLocation("gps");
		        if(loc == null) 
		        	 loc = lm.getLastKnownLocation("network");         
		
		        if(loc != null){
		        	hashMap = new HashMap();
		        	hashMap.put("dCurrLatitude", loc.getLatitude());
		        	hashMap.put("dCurrLongitude", loc.getLongitude());
		        	//hashMap.put("speedCalculate", loc.getSpeed());
					lMinTime = 60000*2; //each 2 minute
					//if GPS was not on during onCreate, add locationUpdates now
					if(locListenD==null){
						locListenD = new DispLocListener();
						//wait minimum lMinTime seconds and/or fMinDistance meters before updating location
				        lm.requestLocationUpdates("gps", lMinTime, fMinDistance, locListenD);
				    }
					
		    	}
		        
		        else 
		        {
		        	configureGPS();
		        } 
	        	
	        }
	        

	        return hashMap;

    	}
    	catch(Exception e)
    	{
    		System.out.println(" "+e.getMessage());
	        return null;
    	}
    }
    
    
    
	private void configureGPS(){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(this); 
    	alertDialog.setMessage(getResources().getString(R.string.error_gpsturnoffTitle));
    	alertDialog.setTitle(getResources().getString(R.string.error_gpsturnoff));
    	alertDialog.setNeutralButton(getResources().getString(R.string.app_name),		
		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				showSettingLocation2();
			}
		}).show();
	}
    
    private void showSettingLocation2()
    {
    	startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

	
    public class DispLocListener implements LocationListener{
    	public void onLocationChanged(Location location)
    	{
    		loadLocationGPS();
    	}
    	public void onProviderDisabled(String provider){

    		loadLocationGPS();
    	}
    	public void onProviderEnabled(String provider){
    		//TODO

    		loadLocationGPS();
    	}
    	public void onStatusChanged(String provider, int status, Bundle extras)	{
    		//TODO

    		loadLocationGPS();
    	}
    }
    
	 public String loadSettingsLongitude(String dCurrLongitude) {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	        String pref = sharedPref.getString("editTextPrefLon", dCurrLongitude);
	        return pref;
	    }
	    
	 public String loadSettingsLatitud(String dCurrLatitude) {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	        String pref = sharedPref.getString("editTextPrefLat",dCurrLatitude);
	        return pref;
	 }
	 
	 public String loadSettingsSpeed() {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	        String pref = sharedPref.getString("editTextPrefSpeed", "5");
	        return pref;
	 }
	    
	 public String loadXMeters() {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	        String pref = sharedPref.getString("editTextXm", "100");
	        return pref;
	 }    

	 public String loadYMPH() {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	        String pref = sharedPref.getString("editTextYmph", "10");
	        return pref;
	}  
    
	 /**
	    * Listens to GPS status changes
	    */
	   protected Listener mStatusListener = new GpsStatus.Listener()
	      {
	         public synchronized void onGpsStatusChanged( int event )
	         {
	            switch( event )
	            {
	               case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
	                  GpsStatus status = lm.getGpsStatus( null );
	                  mSatellites = 0;
	                  Iterable<GpsSatellite> list = status.getSatellites();
	                  for( GpsSatellite satellite : list )
	                  {
	                     if( satellite.usedInFix() )
	                     {
	                        mSatellites++;
	                     }
	                  }
	                 // updateNotification();
	                  break;
	               default:
	                  break;
	            }
	         }
	      };

	      
	      protected void triangulation()
		    {

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
					
				    final double lat=location.getLatitude();
				    final double lng=location.getLongitude();
				    
				    Toast.makeText(this, " Latitud "+lat, Toast.LENGTH_LONG).show();
				    Toast.makeText(this, " Longitude "+lng, Toast.LENGTH_LONG).show();
				}
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
		    
		    public HashMap getTriangulationDataFragment()
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
}
