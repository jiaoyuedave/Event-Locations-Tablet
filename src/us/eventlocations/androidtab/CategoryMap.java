package us.eventlocations.androidtab;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import mapviewballoons.example.MyItemizedOverlay;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.insideperu.map.MapLocationViewer;
import com.insideperu.map.MyParcelable;

public class CategoryMap extends MapActivity  implements OnGestureListener, OnDoubleTapListener  {
	MapLocationViewer mMapView;
    private MapController mMapController;
    private View zoomControls;
	List<Overlay> mapOverlays;
	private OverlayItem overlayItem;
	Drawable drawable;
	Drawable drawable2;
	MyItemizedOverlay itemizedOverlay;
	MyItemizedOverlay itemizedOverlay2;
	private FrameLayout frame;
	//private PlacesItemizedOverlay placesItemizedOverlay;
	//private String currLatitude ="",currLongitude="",dCurrLatitude,dCurrLongitude;
	
    
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			//requestWindowFeature(Window.FEATURE_NO_TITLE);
	        initialiseMapView();
			List<Address> addressList = null;
			mapOverlays = mMapView.getOverlays();
			// first overlay
			//drawable = getResources().getDrawable(R.drawable.note_blue);
			drawable = getResources().getDrawable(R.drawable.google_pin);
			itemizedOverlay = new MyItemizedOverlay(drawable, mMapView);
	    	 
	        Bundle extras = getIntent().getExtras();
	     	Parcelable[] myparcelable = (Parcelable[]) extras.getParcelableArray("STATIONLIST");
	    	//new code//
	     	//rest_id = extras.getString("rest_id");
	     	//currLatitude = extras.getString("currLatitude");
	     	//currLongitude = extras.getString("currLongitude");
	     	//Double scurrLatitude = extras.getDouble("dCurrLatitude");
	     	 GeoPoint point2 = null;
			     	//new code//     	
	     	//GeoPoint point = null;
	        FrameLayout.LayoutParams p =new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL);
	        frame.addView(zoomControls, p);
	       // String address="";
	        for(int i=0;i<myparcelable.length;i++){
	        	
	        	MyParcelable myparceBean = (MyParcelable)myparcelable[i];
	        	
				// Geocoder geocoder = null;
				 //Geocoder geocoder = new Geocoder(this,Locale.getDefault());
				 Geocoder geocoder = new Geocoder(this,Locale.ENGLISH);
		
				
				try {
					//+"United States"
						addressList = geocoder.getFromLocationName(myparceBean.getNameAdressStation(), 1);
						
					 //addressList = geocoder.getFromLocationName(myparceBean.getNameAdressStation()+"United States EEUU", 1);
					 //addressList = geocoder.getFromLocationName("Chelsea Piers (Pier 61 & West Side Hwy), New York, NY, 10011", 1);
					//addressList = geocoder.getFromLocationName("10011", 1);
				//	 addressList = geocoder.getFromLocationName("Chelsea Piers, New York, UNITED STATES", 1);
					// addressList = geocoder.getFromLocationName("Chelsea Piers, New York", 1);
					 
					 
					 if(addressList != null && addressList.size() > 0)
				        {
				            int lat = (int)(addressList.get(0).getLatitude()*1E6);
				            int lng = (int)(addressList.get(0).getLongitude()*1E6);
				            point2 = new GeoPoint(lat, lng);

				            overlayItem = new OverlayItem(point2, myparceBean.getNameAdressStation(),myparceBean.getAddressRestaurant());
							itemizedOverlay.addOverlay(overlayItem);
							itemizedOverlay.setBalloonBottomOffset(20);
							itemizedOverlay.setDrawFocusedItem(true);
							itemizedOverlay.onTap(point2, mMapView);
							mapOverlays.add(itemizedOverlay);

				        }
					 else
					 {
						 
						 //Toast.makeText(CategoryMap.this,"No matches were found or there is no backend service!",Toast.LENGTH_LONG).show();
						 
						 addressList = geocoder.getFromLocationName(myparceBean.getAddressRestaurant(), 1);
						 
						 if(addressList != null && addressList.size() > 0)
					        {
					            int lat = (int)(addressList.get(0).getLatitude()*1E6);
					            int lng = (int)(addressList.get(0).getLongitude()*1E6);
					            point2 = new GeoPoint(lat, lng);

					            overlayItem = new OverlayItem(point2, myparceBean.getNameAdressStation(),myparceBean.getAddressRestaurant());
								itemizedOverlay.addOverlay(overlayItem);
								itemizedOverlay.setBalloonBottomOffset(20);
								itemizedOverlay.setDrawFocusedItem(true);
								itemizedOverlay.onTap(point2, mMapView);
								mapOverlays.add(itemizedOverlay);
					        }
						 
						 else
						 {
							 int ubic =myparceBean.getAddressRestaurant().lastIndexOf("at");
							 if (ubic>0)
							 {
								 String newUbic= myparceBean.getAddressRestaurant().substring(ubic+3);
								 addressList = geocoder.getFromLocationName(newUbic, 1);
							 }

						 }
						 
						 
						 
						 if(addressList != null && addressList.size() > 0)
						 {
					            int lat = (int)(addressList.get(0).getLatitude()*1E6);
					            int lng = (int)(addressList.get(0).getLongitude()*1E6);
					            point2 = new GeoPoint(lat, lng);

					            overlayItem = new OverlayItem(point2, myparceBean.getNameAdressStation(),myparceBean.getAddressRestaurant());
								itemizedOverlay.addOverlay(overlayItem);
								itemizedOverlay.setBalloonBottomOffset(20);
								itemizedOverlay.setDrawFocusedItem(true);
								itemizedOverlay.onTap(point2, mMapView);
								mapOverlays.add(itemizedOverlay);
						 }
						 
						 
						// GmmGeocoder geocoder = new GmmGeocoder(Locale.getDefault());
						// Address[] addresses = geocoder.query(myparceBean.getNameAdressStation(), GmmGeocoder.QUERY_TYPE_LOCATION, 0, 0, 180, 360);
					 }
					 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   	
	        	
				//point = new GeoPoint((int)(myparceBean.getLatitudeStation()*1E6), (int)(myparceBean.getLongitudeStation()*1E6));
				//GeoPoint point2 = new GeoPoint((int)(myparceBean.getLatitudeStation()*1E6),(int)(myparceBean.getLongitudeStation()*1E6));
			//	GeoPoint point2 = new GeoPoint((int)(myparceBean.getLatitudeStation()*1E6),(int)(myparceBean.getLongitudeStation()*1E6));
				 
				//address = myparceBean.getAddressRestaurant();
				//Drawable defaultMarker = getResources().getDrawable(R.drawable.marker);
				//placesItemizedOverlay = new PlacesItemizedOverlay(this, defaultMarker);
				//placesItemizedOverlay.addOverlayItem(new OverlayItem(point2, myparceBean.getNameAdressStation(),"( "+myparceBean.getAddressRestaurant()+" )"));
				//mMapView.getOverlays().add(placesItemizedOverlay);
				//nameRestaurant=myparceBean.getNameAdressStation();
	        }
	        if(addressList != null && addressList.size() > 0)
	        {
	        	mMapController.setZoom(15);
	        	mMapController.animateTo(point2);
	        }
	    }
	 

	 private void initialiseMapView() 
	 {
	   //  requestWindowFeature(Window.FEATURE_LEFT_ICON);
		//new code//
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 
		    /*Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
	        int width = display.getWidth();  
	        int height = display.getHeight();*/
		    setContentView(R.layout.generic_map);

		   settings();
		  LinearLayout layoutItem = (LinearLayout)findViewById(R.id.record_data);
		  layoutItem.removeAllViews();
		 
	   
	        frame = new FrameLayout(this);
	        mMapView = new MapLocationViewer(this, GoogleMapKey.GOOGLE_MAP_KEY);
	        frame.addView(mMapView);
			//setContentView(frame); 	
			layoutItem.addView(frame);
	      //  setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);
	       // setTitleColor(Color.WHITE);
			//new code//
	        
	        zoomControls = mMapView.getZoomControls();
	        mMapController = mMapView.getController();
			//mMapView.setTraffic(true);
	    	//mMapView.setSatellite(true);
	    	mMapView.displayZoomControls(true);
	    	mMapView.setBuiltInZoomControls(true);

		  }
	/* @Override
	 protected boolean onTap(int index) {
	     OverlayItem item = overlayItem.get(index);
	     AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	     dialog.setTitle(item.getTitle());
	     dialog.setMessage(item.getSnippet());
	     dialog.setPositiveButton("Yes", new OnClickListener() {
	        
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	             dialog.dismiss();
	         }
	     });
	     dialog.show();
	     return true;
	 }*/
	 
	
	    

	 




	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//new code//
   /* private void callDialogDrivingDirection()
    {
    	//sendJsonMap();
    	Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
    	Uri u =Uri.parse("http://maps.google.com/maps?saddr="+currLatitude+","+currLongitude+"&daddr="+dCurrLatitude+","+dCurrLongitude);
    	intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
    	intent.setData(u);
    	startActivity(intent);
    }*/

    
	/*protected void sendJsonMap(){
		
		String xmlContentToSend ="http://maps.google.com/maps/api/directions/json?origin=-33.8678,151.2083&destination=-33.9072,151.2850&sensor=false";
		HttpResponse response =null;
    	String result = null;
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	// Using POST here
    	HttpPost httpPost = new HttpPost("http://maps.google.com/maps/api/directions/json");
    	// Make sure the server knows what kind of a response we will accept
    	httpPost.addHeader("Accept", "text/xml");
    	// Also be sure to tell the server what kind of content we are sending
    	//httpPost.addHeader("Content-Type", "application/xml");
    	httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
    	try
    	{
    	    StringEntity entity = new StringEntity("origin=-33.8678,151.2083&destination=-33.9072,151.2850&sensor=false", "UTF-8");
    	    entity.setContentType("application/xml");
    	    httpPost.setEntity(entity);
    	    // execute is a blocking call, it's best to call this code in a thread separate from the ui's
    	    response = httpClient.execute(httpPost);
	    
    	    HttpEntity entitys = response.getEntity();  
	         if (entity != null) {  
	             InputStream instream = entitys.getContent();  
	             result = convertStreamToString(instream);  
	             instream.close();
	             JSONObject json = new JSONObject(result);
					
					if (json.getString("status").equals("success")){
						
						finish();
					}
					else{
						// add error messages
						
					}
	         }  
    	        // Have your way with the response
    	}
    	catch (Exception ex)
    	{
    	        ex.printStackTrace();
    	        
    	}
	}
*/
 
	protected static String convertStreamToString(InputStream is)
    {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

    
   /* private String getAddressFromPosition2() {
    	Geocoder gc = new Geocoder(this, Locale.getDefault()); 
    	List<Address> addressResult = null;
    	Address resultAddress = null;
    	String text="";
        try {
			addressResult = gc.getFromLocation(Double.parseDouble(currLatitude) ,Double.parseDouble(currLongitude) ,1);
			if (!addressResult.isEmpty())
			{
		        resultAddress = addressResult.get(0);
		        text = resultAddress.getAddressLine(0)+", "+resultAddress.getAddressLine(1); 
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
		return text;
    }*/
    
  /*  private OnClickListener mOnclick_driving_directions = new OnClickListener()
	{
        public void onClick(View v)
        {
        	callDialogDrivingDirection();
        }
    };*/


	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
	   /* GeoPoint p = mMapView.getProjection().fromPixels((int)e.getX(), (int)e.getY());
	    
	    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    dialog.setTitle("Double Tap");
	    dialog.setMessage("Location: " + p.getLatitudeE6() + ", " + p.getLongitudeE6());
	    dialog.show();*/
	    
	    //mMapView.getController().zoomInFixing((int)e.getX(),(int)e.getY());
		//mMapView.getController().zoomIn();
		mMapView.getController().zoomOut();
	    
	    return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	  private void settings()
	    {
		
	        ImageView ivClose = (ImageView)findViewById(R.id.back);
	        ivClose.setOnClickListener(mOnclick_close);
	        
	        ImageView img_standard = (ImageView)findViewById(R.id.img_standard);
	        img_standard.setOnClickListener(mOnclick_standard);

	        ImageView img_satellite = (ImageView)findViewById(R.id.img_satellite);
	        img_satellite.setOnClickListener(mOnclick_satellite);

	        ImageView img_hybrid = (ImageView)findViewById(R.id.img_hybrid);
	        img_hybrid.setOnClickListener(mOnclick_hybrid);

	        
	    }
	  
	    private OnClickListener mOnclick_close = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        		finish();
	        }
	    };	 
	    
	    private OnClickListener mOnclick_standard = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	mMapView.setSatellite(false);
	        	
	        }
	    };
	    
	    private OnClickListener mOnclick_satellite = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	//mMapView.setStreetView (true);
	        	mMapView.setTraffic(true);
	        	mMapView.setSatellite(true);
	        }
	    };	  
	    
	    private OnClickListener mOnclick_hybrid = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	//
	        	mMapView.setSatellite(true);
	        	//mMapView.setStreetView (true);
	        	mMapView.setStreetView(false);
	            mMapView.setTraffic(false);
	            
	        }
	    };	  
	    

    
}
