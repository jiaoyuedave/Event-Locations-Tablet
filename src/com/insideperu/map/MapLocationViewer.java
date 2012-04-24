package com.insideperu.map;


import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;

import com.google.android.maps.MapView;

public class MapLocationViewer extends MapView {

	private MapOverlay overlay;
	private GestureDetector gestureDetector;
	  
    public MapLocationsManager getManager() {
    	return overlay.getManager();
    }
    
    public MapLocationViewer(Context context, String apiKey) {
		super(context, apiKey);
	     gestureDetector = new GestureDetector((OnGestureListener) context);
	     gestureDetector.setOnDoubleTapListener((OnDoubleTapListener) context);
		init();
	}
	
	public MapLocationViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
	     gestureDetector = new GestureDetector((OnGestureListener) context);
	     gestureDetector.setOnDoubleTapListener((OnDoubleTapListener) context);
		init();
	}
	
	  // Override the onTouchEvent() method to intercept events and pass them
	  // to the GestureDetector. If the GestureDetector doesn't handle the event,
	  // propagate it up to the MapView.
	  public boolean onTouchEvent(MotionEvent ev) {
	    if(this.gestureDetector.onTouchEvent(ev))
	       return true;
	    else
	      return super.onTouchEvent(ev);
	  }
	
	private void init() {		
		overlay = new MapOverlay();
    	getOverlays().add(overlay);
    	
    	setClickable(true);

	}
	
}