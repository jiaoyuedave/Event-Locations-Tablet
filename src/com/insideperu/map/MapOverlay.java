package com.insideperu.map;



import android.graphics.Canvas;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapOverlay  extends Overlay {
    
    private MapLocationsManager mManager;  
    
    public MapLocationsManager getManager() {
    	return mManager;
    }
        
	public MapOverlay() {
	
		mManager = new MapLocationsManager();
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {		
		
		
        //---when user lifts his finger---
       /* if (event.getAction() == 1) {                
            GeoPoint p = mapView.getProjection().fromPixels(
                (int) event.getX(),
                (int) event.getY());
                Toast.makeText(mapView.getContext(), 
                    p.getLatitudeE6() / 1E6 + "," + 
                    p.getLongitudeE6() /1E6 , 
                    Toast.LENGTH_SHORT).show();
        }    		*/
		return mManager.verifyHitMapLocation(mapView, event);
     
	}
	
    public void draw (Canvas canvas, MapView mapView, boolean shadow) {		
    	mManager.draw(canvas, mapView, shadow);
    }


    
}