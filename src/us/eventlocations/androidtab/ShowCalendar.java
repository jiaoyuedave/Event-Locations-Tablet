package us.eventlocations.androidtab;

import java.util.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.jasonkostempski.android.calendar.CalendarDayMarker;
import com.jasonkostempski.android.calendar.CalendarView;
import com.jasonkostempski.android.calendar.CalendarView.OnMonthChangedListener;
import com.jasonkostempski.android.calendar.CalendarView.OnSelectedDayChangedListener;

public class ShowCalendar extends ActivityBase {
	private com.jasonkostempski.android.calendar.CalendarView calendarView;
	private CalendarView _calendar;
	 public void onCreate(Bundle savedInstanceState) {
	    	requestWindowFeature(Window.FEATURE_NO_TITLE);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.showcalendar);

	        _calendar = (CalendarView) findViewById(R.id.calendar_view);
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

	  	   		markDays();
	  	   		
	  	      Button b_close = (Button) findViewById(R.id.b_close);
	  	      b_close.setOnClickListener(mOnclick_DateSelected);
	  	      calendarView = (CalendarView) findViewById(R.id.calendar_view);
	    }
	    
		private void markDays() {
			// TODO: Find items in the range of _calendar.getVisibleStartDate() and _calendar.getVisibleEndDate().
			// TODO: Create CalendarDayMarker for each item found.
			// TODO: Pass CalendarDayMarker array to _calendar.setDaysWithEvents(markers)
			
			//Example of setting just today with an event
			//System.out.println("xxx");
			_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(Calendar.getInstance(), Color.CYAN) });
		}
		
		   private OnClickListener mOnclick_DateSelected = new OnClickListener()
			{
		        public void onClick(View v)
		        {

		            String date = getDateSelected();
		            Intent returnIntent = new Intent();
		            returnIntent.putExtra("dateSelected",date);
		            setResult(RESULT_OK,returnIntent);   
		        	finish();
		        }
		    };
		
		private String getDateSelected()
		{
       	 	int day =calendarView._currentDay;
       	 	int month=calendarView._currentMonth+1;
       	 	int year =calendarView._currentYear;
       	 	
       	 	return month+"/"+day+"/"+year;
       	 
		}
}
