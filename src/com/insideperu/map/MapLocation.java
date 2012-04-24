package com.insideperu.map;
import us.eventlocations.androidtab.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;


/** Class to hold our location information */
public class MapLocation {

	public static final int PADDING_X = 10;
	public static final int PADDING_Y = 8;
	public static final int RADIUS_BUBBLES = 5;
	public static final int DISTANCE_BUBBLE = 15;
	public static final int SIZE_SELECTOR_BUBBLE = 10;
	
	private GeoPoint point;
	private String name;
	private MapLocationViewer mapLocationView;
	private int type ;
	
	public int getType() {
		return type;
	}  

	private Bitmap drawIcon, shadowIcon;

	public MapLocation(MapLocationViewer mapView, String name, double latitude, double longitude, int type) {
		this.name = name;
		mapLocationView = mapView;
		point = new GeoPoint((int)(latitude*1e6),(int)(longitude*1e6));
		setType(type);		
	}
	
	public MapLocation(MapLocationViewer mapView, String name, GeoPoint point, int type) {
		this.name = name;
		mapLocationView = mapView;
		this.point = point;
		setType(type);
	}
	
	private void setType(int type) {
		this.type = type;	
		switch (type) { 
			
		case R.drawable.bubble:
			drawIcon = BitmapFactory.decodeResource(mapLocationView.getResources(),R.drawable.bubble);
			shadowIcon = BitmapFactory.decodeResource(mapLocationView.getResources(),R.drawable.bubble_shadow);
			break;				
		default:
			drawIcon = BitmapFactory.decodeResource(mapLocationView.getResources(),R.drawable.blank);
			shadowIcon = BitmapFactory.decodeResource(mapLocationView.getResources(),R.drawable.bubble_shadow);
			break;
		}
	}
	
	public Bitmap getDrawIcon() {
		return drawIcon;
	}
	
	public Bitmap getShadowIcon() {
		return shadowIcon;
	}
	

	
	public GeoPoint getPoint() {
		return point;
	}

	public String getName() {
		return name;
	}
	
	public int getWidthIcon() {
		return drawIcon.getWidth();
	}
	
	public int getHeightIcon() {
		return drawIcon.getHeight();
	}
	
	public int getWidthText() {
		return (int)MapLocationsManager.textPaint.measureText(this.getName());
	}
	
	public int getHeightText() {
		return (int)MapLocationsManager.textPaint.descent()-(int)MapLocationsManager.textPaint.ascent();
	}
	
	public RectF getHRectFIcon() {
		return getHRectFIcon(0, 0);
	}
	
	public RectF getHRectFIcon(int offsetx, int offsety) {
		RectF rectf = new RectF();
		rectf.set(-drawIcon.getWidth()/2,-drawIcon.getHeight(),drawIcon.getWidth()/2,0);
		rectf.offset(offsetx, offsety);
		return rectf;
	}
	
	public boolean getHit(int offsetx, int offsety, float event_x, float event_y) {
	    if ( getHRectFIcon(offsetx, offsety).contains(event_x,event_y) ) {
	        return true;
	    }
	    return false;
	}
	
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Point p = new Point();
		mapView.getProjection().toPixels(this.getPoint(), p);
		
    	if (shadow) {
    		canvas.drawBitmap(this.getShadowIcon(), p.x, p.y - this.getShadowIcon().getHeight(),null);
    	} else {
			canvas.drawBitmap(this.getDrawIcon(), p.x -this.getDrawIcon().getWidth()/2, p.y -this.getDrawIcon().getHeight(),null);
    	}
	}
	
	public void drawBubble(Canvas canvas, MapView mapView, boolean shadow) {
	    Point p = new Point();
	    mapView.getProjection().toPixels(this.getPoint(), p);
	    
	    int wBox = getWidthText()  + (PADDING_X*2);
	    int hBox = getHeightText() + (PADDING_Y*2); 
	    
	    RectF boxRect = new RectF(0, 0, wBox, hBox);
	    int offsetX = p.x - wBox/2;
	    int offsetY = p.y - hBox - this.getHeightIcon() - DISTANCE_BUBBLE;
	    boxRect.offset(offsetX, offsetY);
	    
	    Path pathBubble = new Path();
	    pathBubble.addRoundRect(boxRect, RADIUS_BUBBLES, RADIUS_BUBBLES, Direction.CCW);
	    pathBubble.moveTo(offsetX+(wBox/2)-(SIZE_SELECTOR_BUBBLE/2), offsetY+hBox);
	    pathBubble.lineTo(offsetX+(wBox/2), offsetY+hBox+SIZE_SELECTOR_BUBBLE);
	    pathBubble.lineTo(offsetX+(wBox/2)+(SIZE_SELECTOR_BUBBLE/2), offsetY+hBox);
	    
	    canvas.drawPath(pathBubble, MapLocationsManager.borderPaint);
	    canvas.drawPath(pathBubble, MapLocationsManager.innerPaint);
	
	    canvas.drawText(this.getName(), p.x-(getWidthText()/2),
	    		p.y-MapLocationsManager.textPaint.ascent()-this.getHeightIcon()-hBox+PADDING_Y - DISTANCE_BUBBLE, MapLocationsManager.textPaint);
	}
	
}
