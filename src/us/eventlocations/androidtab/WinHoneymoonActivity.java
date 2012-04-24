package us.eventlocations.androidtab;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import us.eventlocations.androidtab.utilityfunctions.WebServiceHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jasonkostempski.android.calendar.CalendarView;

public class WinHoneymoonActivity extends Activity {
	// protected SharedPreferences preferences; 
	 private boolean scrolling = false;
	 private String dateSelected;
	 ProgressDialog dialog;
	 final private Handler handler = new Handler();
	 private EditText yourName,lastName,et_email,et_address1,et_state,et_city,et_zip,et_people,et_price,et_address2;
	 private kankan.wheel.widget.WheelView event_county,event_eventsite;
	 private ImageView check_blue1,check_blue2,check_blue3,check_blue4,check_blue5,check_blue6,check_blue7,check_blue8,check_blue9,check_blue10,check_blue11;
	 private com.jasonkostempski.android.calendar.CalendarView calendarView;
	 private int ieventSite=0,ieventCounty=0;
	 
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.by_winhoneymoon);
	        
	        settings();
	    	Button rules_regulation = (Button)findViewById(R.id.rules_regulation);
	    	rules_regulation.setOnClickListener(mOnclick_rules_regulation);
	  
	    	   final WheelView country = (WheelView) findViewById(R.id.country);
	           country.setVisibleItems(5);
	           country.setViewAdapter(new CountryAdapter(this));
	           
	           
	    	   final WheelView country2 = (WheelView) findViewById(R.id.country2);
	           if (country2!=null)
	           {
	        	   country2.setVisibleItems(5);
	        	   country2.setViewAdapter(new CountryAdapter2(this));
	           }

	           
	           country.addScrollingListener( new OnWheelScrollListener() {
	               public void onScrollingStarted(WheelView wheel) {
	                   scrolling = true;
	               }
	               public void onScrollingFinished(WheelView wheel) {
	                   scrolling = false;
	                   int value = country.getCurrentItem();
	                   System.out.println(value);
	                   if (value ==0)
	                   	country.setCurrentItem(1);
	                   else if (value ==6)
	                   	country.setCurrentItem(7);
	                   else if (value ==15)
	                   	country.setCurrentItem(16);
	                   
	               //    updateCities(city, cities, country.getCurrentItem());
	               }
	           });

	           country.setCurrentItem(1);
	  
	           country2.addScrollingListener( new OnWheelScrollListener() {
	               public void onScrollingStarted(WheelView wheel) {
	                   scrolling = true;
	               }
	               public void onScrollingFinished(WheelView wheel) {
	                   scrolling = false;
	               //    updateCities(city, cities, country.getCurrentItem());
	               }
	           });

	           country2.setCurrentItem(0);
	           
	           
	        /*   _calendar = (CalendarView) findViewById(R.id.calendar_view);

	   		_calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
	   			public void onMonthChanged(CalendarView view) {
	   				markDays();
	   			}
	   		});

	   		_calendar.setOnSelectedDayChangedListener(new OnSelectedDayChangedListener() {
	   			public void onSelectedDayChanged(CalendarView view) {
	   				
	   				Calendar calendar = Calendar.getInstance();
	   				 
	   			    calendar.set(Calendar.DAY_OF_MONTH, view._currentDay);
	   			    calendar.set(Calendar.MONTH, view._currentMonth);
	   			    calendar.set(Calendar.YEAR, view._currentYear);
	   				_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(calendar, Color.CYAN,1) });
	   				_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(Calendar.getInstance(), Color.TRANSPARENT) });
	   			}
	   		});

	   		markDays();*/

	   		
	   		
	   		//markDaysDefault();
	   		//displayContacts();
	   		//updateContact("Horizon Productions","9732537042");
	   		//updateContact("Horizon Productions","1");
	        
	    }      
	 
	    private void settings()
	    {
	    	yourName = (EditText) findViewById(R.id.et_name);
	    	lastName = (EditText) findViewById(R.id.et_lastname);
	    	et_email = (EditText) findViewById(R.id.et_email);
	    	et_address1 = (EditText) findViewById(R.id.et_address1);
	    	et_address2= (EditText) findViewById(R.id.et_address2);
	    	et_state = (EditText) findViewById(R.id.et_state);
	    	et_zip = (EditText) findViewById(R.id.et_zip);
	    	et_city = (EditText) findViewById(R.id.et_city);
	    	et_people = (EditText) findViewById(R.id.et_people);
	    	et_price = (EditText) findViewById(R.id.et_price);
	    	
	    	event_county = (WheelView) findViewById(R.id.country);
	    	event_eventsite = (WheelView) findViewById(R.id.country2);
	    	calendarView = (CalendarView) findViewById(R.id.calendar_view);
	    	
	    	check_blue1 = (ImageView) findViewById(R.id.check_blue1);
	    	check_blue2 = (ImageView) findViewById(R.id.check_blue2);
	    	check_blue3 = (ImageView) findViewById(R.id.check_blue3);
	    	check_blue4 = (ImageView) findViewById(R.id.check_blue4);
	    	check_blue5 = (ImageView) findViewById(R.id.check_blue5);
	    	check_blue6 = (ImageView) findViewById(R.id.check_blue6);
	    	check_blue7 = (ImageView) findViewById(R.id.check_blue7);
	    	check_blue8 = (ImageView) findViewById(R.id.check_blue8);
	    	check_blue9 = (ImageView) findViewById(R.id.check_blue9);
	    	check_blue10 = (ImageView) findViewById(R.id.check_blue10);
	    	check_blue11 = (ImageView) findViewById(R.id.check_blue11);
	    	
	    	java.util.Date date = new java.util.Date(); 
	    	java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("MM/dd/yyyy");
	    	String fecha = sdf.format(date);
	    	dateSelected = fecha;
	    	
		      EditText  label_datesel = (EditText) findViewById(R.id.label_datesel);
		      label_datesel.setText(fecha);
		      
	    	
	    	
	    	ImageView label_event_date = (ImageView) findViewById(R.id.calendar_picker);
	    	label_event_date.setOnClickListener(mOnclick_gotoShowCalendar);
	    	
	    	ImageView county2_picker = (ImageView) findViewById(R.id.county2_picker);
	    	county2_picker.setOnClickListener(mOnclick_gotoShowCounty2);
	    	
	    	ImageView county1_picker = (ImageView) findViewById(R.id.county1_picker);
	    	county1_picker.setOnClickListener(mOnclick_gotoShowCounty1);
	    	
	        Button sendEmail = (Button) findViewById(R.id.enter);
	        if (sendEmail!=null)
	        sendEmail.setOnClickListener(mOnclick_gotoSendMail);
	        

	        RelativeLayout rel_antigua = (RelativeLayout) findViewById(R.id.re_antigua);
	        if (rel_antigua!=null)
	        rel_antigua.setOnClickListener(mOnclick_antigua);

	        RelativeLayout rel_bahamas = (RelativeLayout) findViewById(R.id.re_bahamas);
	        if (rel_bahamas!=null)
	        rel_bahamas.setOnClickListener(mOnclick_bahamas);

	        RelativeLayout re_bermuda = (RelativeLayout) findViewById(R.id.re_bermuda);
	        if (re_bermuda!=null)
	        re_bermuda.setOnClickListener(mOnclick_bermudas);
	        
	        RelativeLayout re_british = (RelativeLayout) findViewById(R.id.re_british);
	        if (re_british!=null)
	        re_british.setOnClickListener(mOnclick_british);
	        
	        RelativeLayout re_france = (RelativeLayout) findViewById(R.id.re_france);
	        if (re_france!=null)
	        re_france.setOnClickListener(mOnclick_france);

	        RelativeLayout re_hawaii = (RelativeLayout) findViewById(R.id.re_hawaii);
	        if (re_hawaii!=null)
	        re_hawaii.setOnClickListener(mOnclick_hawaii);

	        RelativeLayout re_italy = (RelativeLayout) findViewById(R.id.re_italy);
	        if (re_italy!=null)
	        re_italy.setOnClickListener(mOnclick_italy);
	        
	        RelativeLayout re_jamaica = (RelativeLayout) findViewById(R.id.re_jamaica);
	        if (re_jamaica!=null)
	        re_jamaica.setOnClickListener(mOnclick_jamaica);
	        
	        RelativeLayout re_stbarts = (RelativeLayout) findViewById(R.id.re_stbarts);
	        if (re_stbarts!=null)
	        re_stbarts.setOnClickListener(mOnclick_stbarts);
	        
	        RelativeLayout re_stlucia = (RelativeLayout) findViewById(R.id.re_stlucia);
	        if (re_stlucia!=null)
	        re_stlucia.setOnClickListener(mOnclick_stlucia);
	        
	        RelativeLayout re_martin = (RelativeLayout) findViewById(R.id.re_stmartin);
	        if (re_martin!=null)
	        re_martin.setOnClickListener(mOnclick_stmartin);
	        


	    }
	    
	    private int eventSiteValue(int value)
	    {
	      if (value==0)
	    	  return 6;
	      else if (value==1)
	    	  return 7;
	      else if (value==2)
	    	  return 8;
	      else if (value==3)
	    	  return 10;
	      else if (value==4)
	    	  return 11;
	      else if (value==5)
	    	  return 14;
	      else if (value==6)
	    	  return 18;
	      else if (value==7)
	    	  return 20;
	      else if (value==8)
	    	  return 21;
	      else if (value==9)
	    	  return 22;
	      else if (value==10)
	    	  return 50;
	      else if (value==11)
	    	  return 25;
	      else if (value==12)
	    	  return 26;
	      else if (value==13)
	    	  return 36;
	      else if (value==14)
	    	  return 27;
	      else if (value==15)
	    	  return 28;
	      else if (value==16)
	    	  return 30;
	      else
	    	  return 6;
	    }
	    
	    private int eventCountyValue(int value)
	    {
	      if (value==1)
	    	  return 1;
	      else if (value==2)
	    	  return 2;
	      else if (value==3)
	    	  return 3;
	      else if (value==4)
	    	  return 4;
	      else if (value==5)
	    	  return 5;
	      else if (value==7)
	    	  return 16;
	      else if (value==8)
	    	  return 43;
	      else if (value==9)
	    	  return 17;
	      else if (value==10)
	    	  return 32;
	      else if (value==11)
	    	  return 24;
	      else if (value==12)
	    	  return 123;
	      else if (value==13)
	    	  return 103;
	      else if (value==14)
	    	  return 18;
	      else if (value==16)
	    	  return 19;
	      else
	    	  return 0;
	    }
	    
	   
	    
	    private OnClickListener mOnclick_antigua = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue1.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue1.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue1.setVisibility(View.INVISIBLE);
	 	        	}
	 	        	
	 	        	
	 	        	
	 	        }
	 	        
	 	};
	    
	 	        	
	    private OnClickListener mOnclick_bahamas = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue2.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue2.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue2.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};
	 	
	 	
	    private OnClickListener mOnclick_bermudas = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue3.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue3.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue3.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};
	 	
	    private OnClickListener mOnclick_british = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue4.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue4.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue4.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};
	 	
	    private OnClickListener mOnclick_france = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue5.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue5.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue5.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};
	 	
	 	
	    private OnClickListener mOnclick_hawaii = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue6.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue6.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue6.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};
	 	
	    private OnClickListener mOnclick_italy = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue7.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue7.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue7.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};
	 	
	 	
	    private OnClickListener mOnclick_jamaica = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue8.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue8.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue8.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};
	    private OnClickListener mOnclick_stbarts = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue9.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue9.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue9.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};

	 	private OnClickListener mOnclick_stlucia = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue10.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue10.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue10.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};
	 	
	    private OnClickListener mOnclick_stmartin = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 	        	if (check_blue11.getVisibility()==View.INVISIBLE)
	 	        	{
	 	        		check_blue11.setVisibility(View.VISIBLE);	
	 	        	}
	 	        	else
	 	        	{
	 	        		check_blue11.setVisibility(View.INVISIBLE);
	 	        	}
	 	        }
	 	};
	 	
	    private OnClickListener mOnclick_gotoShowCalendar = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 				  Intent myIntent = new Intent(getApplicationContext(), ShowCalendar.class);
	 				  startActivityForResult(myIntent,0);
	 	        }
	 	};
	 	
	    private OnClickListener mOnclick_gotoShowCounty2 = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 				  Intent myIntent = new Intent(getApplicationContext(), ShowCounty2.class);
	 				  startActivityForResult(myIntent,0);
	 	        }
	 	};
	 	
	    private OnClickListener mOnclick_gotoShowCounty1 = new OnClickListener()
	 	{
	 	        public void onClick(View v)
	 	        {
	 				  Intent myIntent = new Intent(getApplicationContext(), ShowCounty1.class);
	 				  startActivityForResult(myIntent,0);
	 	        }
	 	};
	 	
	 	
	 	
	 	
	 	 @Override  
		  public void onActivityResult(int requestCode, int resultCode, Intent data) {   
		    
			      if (resultCode == RESULT_OK) {  
			    	  dateSelected = data.getStringExtra("dateSelected");
				      EditText  label_datesel = (EditText) findViewById(R.id.label_datesel);
				      label_datesel.setText(dateSelected);
			      }
			      
			      if (resultCode == 99) {
			      
				      int county2= data.getIntExtra("county2",0);
				      EditText  label_datesel_county2 = (EditText) findViewById(R.id.label_datesel_county2);
				      String scounty2="";

				      
				      
				      if (county2==1)
				      {
				    	  scounty2="Catering Hall";
				    	  ieventSite=eventSiteValue(0);
				      }
				      else if (county2==2)
				      {
				    	  scounty2="Club";
				    	  ieventSite=eventSiteValue(1);
				      }
				      else if (county2==3)
				      {
				    	  scounty2="Conference Center";
				    	  ieventSite=eventSiteValue(2);
				      }
				      else if (county2==4)
				      {
				    	  scounty2="Country Club";
				    	  ieventSite=eventSiteValue(3);
				      }
				      else if (county2==5)
				      {
				    	  scounty2="Event Site";
				    	  ieventSite=eventSiteValue(4);
				      }
				      else if (county2==6)
				      {
				    	  scounty2="Hotel";
				    	  ieventSite=eventSiteValue(5);
				      }
				      else if (county2==7)
				      {
				    	  scounty2="Loft";
				    	  ieventSite=eventSiteValue(6);
				      }
				      else if (county2==8)
				      {
				    	  scounty2="Mansion";
				    	  ieventSite=eventSiteValue(7);
				      }
				      else if (county2==9)
				      {
				    	  scounty2="Museums";
				    	  ieventSite=eventSiteValue(8);
				      }
				      else if (county2==10)
				      {
				    	  scounty2="Oceanfront /Wavefront";
				    	  ieventSite=eventSiteValue(9);
				      }
				      else if (county2==11)
				      {
				    	  scounty2="Private Club";
				    	  ieventSite=eventSiteValue(10);
				      }
				      else if (county2==12)
				      {
				    	  scounty2="Restaurant";
				    	  ieventSite=eventSiteValue(11);
				      }
				      else if (county2==13)
				      {
				    	  scounty2="Sport Event Site";
				    	  ieventSite=eventSiteValue(12);
				      }
				      else if (county2==14)
				      {
				    	  scounty2="Temple";
				    	  ieventSite=eventSiteValue(13);
				      }
				      else if (county2==15)
				      {
				    	  scounty2="Townhouse";
				    	  ieventSite=eventSiteValue(14);
				      }
				      else if (county2==16)
				      {
				    	  scounty2="Yatch/Boat";
				    	  ieventSite=eventSiteValue(15);
				      }
				      
				      label_datesel_county2.setText(scounty2);
			      }

				   if (resultCode == 88) {

					      int county1= data.getIntExtra("county1",0);
					      EditText  label_datesel_county1 = (EditText) findViewById(R.id.label_datesel_county1);
					      String scounty1="";
					      
				      if (county1==1)
				      {
				    	  scounty1="Bronx";
				    	  ieventCounty=eventCountyValue(1);
				      }
				      else if (county1==2)
				      {
				    	  scounty1="Brooklyn";
				    	  ieventCounty=eventCountyValue(2);
				      }
				      else if (county1==3)
				      {
				    	  scounty1="Manhattan";
				    	  ieventCounty=eventCountyValue(3);
				      }
				      else if (county1==4)
				      {
				    	  scounty1="Queens";
				    	  ieventCounty=eventCountyValue(4);
				      }
				      else if (county1==5)
				      {
				    	  scounty1="Staten Island";
				    	  ieventCounty=eventCountyValue(5);
				      }
				      else if (county1==6)
				      {
				    	  scounty1="Dutchess";
				    	  ieventCounty=eventCountyValue(7);
				      }
				      else if (county1==7)
				      {
				    	  scounty1="Hudson Valley";
				    	  ieventCounty=eventCountyValue(8);
				      }
				      else if (county1==8)
				      {
				    	  scounty1="Orange";
				    	  ieventCounty=eventCountyValue(9);
				      }
				      else if (county1==9)
				      {
				    	  scounty1="Putnam";
				    	  ieventCounty=eventCountyValue(10);
				      }
				      else if (county1==10)
				      {
				    	  scounty1="Rockland";
				    	  ieventCounty=eventCountyValue(11);
				      }
				      else if (county1==11)
				      {
				    	  scounty1="Sullivan";
				    	  ieventCounty=eventCountyValue(12);
				      }
				      else if (county1==12)
				      {
				    	  scounty1="Ulster";
				    	  ieventCounty=eventCountyValue(13);
				      }
				      else if (county1==13)
				      {
				    	  scounty1="Westchester";
				    	  ieventCounty=eventCountyValue(14);
				      }
				      else if (county1==14)
				      {
				    	  scounty1="Nassau";
				    	  ieventCounty=eventCountyValue(16);
				      }
				      label_datesel_county1.setText(scounty1);
				      
				      
			      }
			     else if (resultCode == Activity.RESULT_CANCELED)
			     {
			    	 

			     }
		  }
		  
	 	/*private void possibleMap()
	 	{
	 		private WebView                 _webView;

	 	    _webView = (WebView)_yourrelativelayourSrc.findViewById(R.id.layout_webview);

	 	    _webView.getSettings().setJavaScriptEnabled(true);
	 	    _webView.getSettings().setBuiltInZoomControls(true);
	 	    _webView.getSettings().setSupportZoom(true);

	 	    _webView.setWebChromeClient(new WebChromeClient() {
	 	        public void onProgressChanged(WebView view, int progress) {
	 	            // Activities and WebViews measure progress with different scales.
	 	            // The progress meter will automatically disappear when we reach 100%
	 	            ((Activity) activity).setProgress(progress * 1000);
	 	        }

	 	    });

	 	    // to handle its own URL requests :
	 	    _webView.setWebViewClient(new MyWebViewClient());
	 	    _webView.loadUrl("http://maps.google.com/maps?q=Pizza,texas&ui=maps");
	 	}*/
	    
	    private OnClickListener mOnclick_gotoSendMail = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	
	       	 	String syourName=yourName.getText().toString();
	       	 	String syourLastName=lastName.getText().toString();
	       	 	String semail=et_email.getText().toString();
	       	 	String set_address1=et_address1.getText().toString();
	       	 	String set_address2=et_address2.getText().toString();
	       	 	String set_state=et_state.getText().toString();
	       	 	String set_city=et_city.getText().toString();
	       	 	String set_zip=et_zip.getText().toString();
	       	 	String set_people=et_people.getText().toString();
	       	 	String set_price=et_price.getText().toString();
	       	 	int valueCounty =event_county.getCurrentItem();
	       	 	int valueEventSite =event_eventsite.getCurrentItem();
	       	 	/*int day =calendarView._currentDay;
	       	 	int month=calendarView._currentMonth;
	       	 	int year =calendarView._currentYear;*/
	            EditText  label_datesel = (EditText) findViewById(R.id.label_datesel);
	            String dateEvent=label_datesel.getText().toString();
	            
	       	 	//String dateEvent=month+"/"+day+"/"+year;
	       	 	//String dateEvent=dateSelected;
	       	 
	       	 	
	       	 	int eventSite=eventSiteValue(valueEventSite);
	       	 	int eventCounty=eventCountyValue(valueCounty);
	       	 
	       	 	
	       	 	
	        	ArrayList error = new ArrayList ();
	        	boolean byourname=false,byourLastName=false,bemail=false,baddress1=false,bstate=false,bcity=false,bzip=false,bprice=false,bpeople=false,bhoneymoon=false;
	        	boolean bdateEvent=false;
	        	boolean beventCounty=false;
	        	boolean beventSite=false;
	        	
	       	 	if (syourName==null || syourName.length()==0)
	       	 	{
	       	 		error.add("First Name is required");
	       	 		byourname=true;
	       	 	}
	       	 	if (syourLastName==null || syourLastName.length()==0)
	       	 	{
	       	 		error.add("Last Name is required");
	       	 		byourLastName=true;
	       	 	}
	       	 	if (semail==null || semail.length()==0)
	       	 	{
	       	 		error.add("Email is required");
	       	 		bemail=true;
	       	 	}

	       	 	if (!Common.IsEmail(semail))
	       	 	{
	       	 		error.add("Email Address is invalid");
	       	 		bemail=true;
	       	 	}
	       	 	if (dateEvent==null || dateEvent.length()==0)
	       	 	{
	       	 		error.add("Date Event is required");
	       	 		bdateEvent=true;
	       	 	}
	       	 	
	       	 	/*if (set_address1==null || set_address1.length()==0)
	       	 	{
	       	 		error.add("Address1 is required");
	       	 		baddress1=true;
	       	 	}*/
	       	 	if (set_state==null || set_state.length()==0)
	       	 	{
	       	 		error.add("State is required");
	       	 		bstate=true;
	       	 	}
	       	 	if (set_city==null || set_city.length()==0)
	       	 	{
	       	 		error.add("City is required");
	       	 		bcity=true;
	       	 	}
	       	 	if (set_zip==null || set_zip.length()==0)
	       	 	{
	       	 		error.add("Zip is required");
	       	 		bzip=true;
	       	 	}
	       	 	if (set_people==null || set_people.length()==0)
	       	 	{
	       	 		error.add("Number of People is required");
	       	 		bpeople=true;
	       	 	}
	       	 	if (set_price==null || set_price.length()==0)
	       	 	{
	       	 		error.add("Price / Person is required");
	       	 		bprice=true;
	       	 	}
	       	 	if (ieventCounty ==0)
	       	 	{
	       	 		error.add("Event County is required");
	       	 		beventCounty=true;
	       	 	}
	       	 	if (ieventSite ==0)
	       	 	{
	       	 		error.add("Type of Event Site is required");
	       	 		beventSite=true;
	       	 	}
	      	 	
	       	 	if (check_blue1.getVisibility()==View.INVISIBLE && check_blue2.getVisibility()==View.INVISIBLE && check_blue3.getVisibility()==View.INVISIBLE && check_blue4.getVisibility()==View.INVISIBLE
	       	 		&& check_blue5.getVisibility()==View.INVISIBLE && check_blue6.getVisibility()==View.INVISIBLE && check_blue7.getVisibility()==View.INVISIBLE && check_blue8.getVisibility()==View.INVISIBLE
	       	 		&& check_blue9.getVisibility()==View.INVISIBLE && check_blue10.getVisibility()==View.INVISIBLE && check_blue11.getVisibility()==View.INVISIBLE)
	       	 	{
	       	 		error.add("Country Honeymoon is required");
	       	 		bhoneymoon=true;
	       	 		
	       	 	}
	       		 
	       	 	
	       	 	if (byourname || byourLastName ||  bemail || baddress1 || bstate|| bcity|| bzip || bprice || bpeople || bhoneymoon || bdateEvent || beventCounty  || beventSite)
	       	 	{
	       	 		showMessage(getString(R.string.form_errors), error,-1);
	       	 	}
	       	 	else  
	       	 	{
	       	 		submitWinHoneyMoon(syourName,syourLastName,semail,set_address1,set_address2,set_state,set_city,set_zip,set_people,set_price,ieventCounty,ieventSite,dateEvent);
	       	 	}
	       	 	
	       	 	
	       	 		  
	        }
	    };
	    

	    private void submitWinHoneyMoon(final String syourName,final String syourLastName,final String semail,final String set_address1,final String set_address2,final String set_state,final String set_city,final String set_zip,final String set_people,final String set_price,final int county,final int site,final String date){    	
	    	Thread splashThread = new Thread() {
	            @Override
	            public void run() {            	
	            	
	            	WebServiceHelper webservice = new WebServiceHelper();
	            	
	            	 StringBuffer text01 = new StringBuffer ();
	            	 text01.append("");
	            	if (check_blue1.getVisibility()==View.VISIBLE)      text01.append("Antigua,");
	            	if (check_blue2.getVisibility()==View.VISIBLE) text01.append("Bahamas,");
	            	if (check_blue3.getVisibility()==View.VISIBLE) text01.append("Bermudas,");
	            	if (check_blue4.getVisibility()==View.VISIBLE) text01.append("British Virgin Islands,");
	            	if (check_blue5.getVisibility()==View.VISIBLE) text01.append("France,");
	            	if (check_blue6.getVisibility()==View.VISIBLE) text01.append("Hawaii,");
	            	if (check_blue7.getVisibility()==View.VISIBLE) text01.append("Italy,");
	            	if (check_blue8.getVisibility()==View.VISIBLE) text01.append("Jamaica,");
	            	if (check_blue9.getVisibility()==View.VISIBLE) text01.append("St. Barts,");
	            	if (check_blue10.getVisibility()==View.VISIBLE) text01.append("St. Lucia,");
	            	if (check_blue11.getVisibility()==View.VISIBLE) text01.append("St. Martin,");
	            	String country =text01.toString();
	            	int last = country.lastIndexOf(",");
	            	if (last>0)
	            	{
	            		country = country.substring(0, country.length()-1);
	            	}
	            	System.out.println(country);
	            	
	            	webservice.getDataWinHoneyMoonForm(Common.SendWinaHoneyMoon,syourName,syourLastName,semail,set_address1,set_address2,set_state,set_city,set_zip,set_people,set_price,county,site,date,country);
	              	handler.post(finishSendEmail);
	            }
	         };
	         splashThread.start();
	    }
	    
	    private Runnable finishSendEmail = new Runnable() {
	 		 public void run() {
	 			 finishedSendEmail();
	 		 }
	 	};
	    
		 public void finishedSendEmail(){  
			 showMessageOK(getString(R.string.label_honeymoon_ok_title),getString(R.string.label_honeymoon_ok));
			 finish();

			 
		    } 
	    
		 
		 protected void showMessageOK(String messageString, String  titleMessage){
			  Bundle bundle = new Bundle();
		      bundle.putString("title",messageString);
		      bundle.putString("text",titleMessage);
		      bundle.putString("flag","emailsent");
		      
		      /*if (entity!=-1)
		    	  bundle.putString("controlFocus",getString(entity));*/
			  Intent myIntent = new Intent(getApplicationContext(), ShowMessageActivity.class);
			  bundle.putString("type","messageOK");
			  myIntent.putExtras(bundle);
			  
			 /* if (entity!=-1)
				  startActivityForResult(myIntent, 0);
			  else*/
				  startActivityForResult(myIntent,0);
		}
	  //if entity parameter is null the message will be show without focus option
		protected void showMessage(String messageString, ArrayList titleMessage, int entity){
			  Bundle bundle = new Bundle();
		      bundle.putString("title",messageString);
		      bundle.putString("flag","validboxes");
		      String data = "";
		      for (int i = 0;i<titleMessage.size();i++)
		      {
		    	  data = data + (String)titleMessage.get(i)+ "\n";
		      }
		     
		      
		      bundle.putString("text",data);
		      
		      if (entity!=-1)
		    	  bundle.putString("controlFocus",getString(entity));
			  Intent myIntent = new Intent(getApplicationContext(), ShowMessageActivity.class);
			  myIntent.putExtras(bundle);
			  
			  if (entity!=-1)
				  startActivityForResult(myIntent, 0);
			  else
				  startActivity(myIntent);
		}
	    
   
    
	/*private void markDaysDefault() {
		// TODO: Find items in the range of _calendar.getVisibleStartDate() and _calendar.getVisibleEndDate().
		// TODO: Create CalendarDayMarker for each item found.
		// TODO: Pass CalendarDayMarker array to _calendar.setDaysWithEvents(markers)
		
		//Example of setting just today with an event
			Calendar calendar = Calendar.getInstance();
		    calendar.set(Calendar.DAY_OF_MONTH, calendar.DAY_OF_MONTH);
		    calendar.set(Calendar.MONTH, calendar.MONTH);
		    calendar.set(Calendar.YEAR, calendar.YEAR);
		_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(calendar, Color.CYAN) });
	}*/
	
	/*private void markDays() {
		// TODO: Find items in the range of _calendar.getVisibleStartDate() and _calendar.getVisibleEndDate().
		// TODO: Create CalendarDayMarker for each item found.
		// TODO: Pass CalendarDayMarker array to _calendar.setDaysWithEvents(markers)
		
		//Example of setting just today with an event
		_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(Calendar.getInstance(), Color.CYAN) });
	}*/

	//private CalendarView _calendar;
	

    private OnClickListener mOnclick_rules_regulation = new OnClickListener()
	{
        public void onClick(View v)
        {
        	 Intent mainIntent = new Intent(WinHoneymoonActivity.this, RulesRegulationsActivity.class);
        	 startActivity(mainIntent);
        }
    };

    
    

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        
        private String countries[] =
                new String[] {"--New York City--", "Bronx", "Brooklyn", "Manhattan", "Queens", "Staten Island", "--New York State--","Dutchess", "Hudson Valley", "Orange", "Putnam", "Rockland", "Sullivan","Ulster","Westchester","--Long Island--","Nassau"};
        

        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.country_layout2, NO_RESOURCE);
            
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return countries.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }

    
    /**
     * Adapter for countries
     */
    private class CountryAdapter2 extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] =
            new String[] {"Catering Hall", "Club", "Conference Center", "Country Club", "Event Site", "Hotel", "Loft","Mansion", "Museums", "Oceanfront /Wavefront", "Private Club", "Restaurant", "Sport Event Site","Temple","Townhouse","Yatch/Boat"};

        /**
         * Constructor
         */
        protected CountryAdapter2(Context context) {
            super(context, R.layout.country_layout2, NO_RESOURCE);
            
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return countries.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }
    
    /*public Boolean matchingContacts() {
        Boolean matchesFound = false;
        String[] projection = new String[]{ android.provider.Contacts.Phones.NUMBER,};

        Cursor managedCursor =  managedQuery(android.provider.Contacts.People.CONTENT_URI, projection,android.provider.Contacts.Phones.NUMBER + "=\'" +"9732537042" + "\'", null, null);           

        int size = managedCursor.getCount();
        if (managedCursor.isFirst()) {
           // Log.d("MatchingContacts", "Found a match!");
        	matchesFound = true;
        }
        return matchesFound;
    }*/
  
    private void displayContacts() {
    	
    	ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
        	while (cur.moveToNext()) {
        		String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        		String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                     Cursor pCur = cr.query(
                    		 ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
                    		 null, 
                    		 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
                    		 new String[]{id}, null);
                     while (pCur.moveToNext()) {
                    	 String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    	 Toast.makeText(this, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                     } 
      	        pCur.close();
      	    }
        	}
        }
    }
    
    private void updateContact(String name, String phone) {
    	//ContentResolver cr = getContentResolver();
 
      //  String where = ContactsContract.Data.DISPLAY_NAME + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ? AND " +String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE) + " = ? ";
    	  String where = ContactsContract.Data.DISPLAY_NAME + " = ?";
        //String[] params = new String[] {name,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)};
    	  String[] params = new String[] {name};

        Cursor phoneCur = managedQuery(ContactsContract.Data.CONTENT_URI, null, where, params, null);
        int size =phoneCur.getCount();
        
        //ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        //int size =ops.size();
        /*if ( (null == phoneCur)  ) {
        	createContact(name, phone);
        } else {
        	ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
        	        .withSelection(where, params)
        	        .withValue(ContactsContract.CommonDataKinds.Phone.DATA, phone)
        	        .build());
        }*/
        
        phoneCur.close();
        Toast.makeText(this, "Contact Already Exist : " + name, Toast.LENGTH_SHORT).show();
        
      /*  try {
			cr.applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		//Toast.makeText(NativeContentProvider.this, "Updated the phone number of 'Sample Name' to: " + phone, Toast.LENGTH_SHORT).show();
    }

    	  
}
