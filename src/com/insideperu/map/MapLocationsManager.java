package com.insideperu.map;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class MapLocationsManager {
	
	private List<MapLocation> mapLocations;
	
	public static Paint innerPaint, borderPaint, textPaint;
	
	private MapLocation selectedLocation = null;
	
    public MapLocationsManager() {
    	mapLocations = new ArrayList<MapLocation>();
    	
		innerPaint = new Paint();
		innerPaint.setARGB(255, 255, 255, 255);
		innerPaint.setAntiAlias(true);
		
		borderPaint = new Paint();
		borderPaint.setARGB(255, 0, 0, 0);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(4);
		
		textPaint = new Paint();
		textPaint.setARGB(255, 0, 0, 0);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(12);
		textPaint.setColor(Color.RED);
	}
	
	public void addMapLocation(MapLocation ml) {
		mapLocations.add(ml);
	}
	
	public List<MapLocation> getMapLocations() {
		return mapLocations;
	}
	
	public MapLocation getSelectedMapLocation() {
		return selectedLocation;
	}
	
    public boolean verifyHitMapLocation(MapView mapView, MotionEvent event) {
    	
    	
    	if (event.getAction()==MotionEvent.ACTION_DOWN) {
	    	Iterator<MapLocation> iterator = getMapLocations().iterator();
	    	while(iterator.hasNext()) {
	    		MapLocation testLocation = iterator.next();
	    		
	    		Point p = new Point();
	    		
	    		mapView.getProjection().toPixels(testLocation.getPoint(), p);
	    		
	    		if (testLocation.getHit(p.x, p.y, event.getX(),event.getY())) {
	    			if (selectedLocation == testLocation)
	    				selectedLocation = null;
	    			else
	    				selectedLocation = testLocation;
	    			return true;
	    	    }
	    	    
	    	}
    	}
    	//selectedLocation = null;
    	return false; 
    }
    
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    	
		Iterator<MapLocation> iterator = getMapLocations().iterator();

		while(iterator.hasNext()) {	   
    		MapLocation location = iterator.next();
    		location.draw(canvas, mapView, shadow);
    	}
		
    	if ( selectedLocation != null) {
    		selectedLocation.drawBubble(canvas, mapView, shadow);
    	}
    	
    }
    
}
