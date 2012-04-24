package com.jasonkostempski.android.calendar;

import java.util.Calendar;

import us.eventlocations.androidtab.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.jasonkostempski.android.calendar.CalendarView.OnMonthChangedListener;
import com.jasonkostempski.android.calendar.CalendarView.OnSelectedDayChangedListener;

public class Main extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		_calendar = (CalendarView) findViewById(R.id.calendar_view);

		_calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
			public void onMonthChanged(CalendarView view) {
				markDays();
			}
		});

		_calendar.setOnSelectedDayChangedListener(new OnSelectedDayChangedListener() {
			public void onSelectedDayChanged(CalendarView view) {
				
		
				/*View[] vs = new View[2];

				TextView tv1 = new TextView(Main.this);
				tv1.setText("TODO:");
				vs[0] = tv1;

				TextView tv2 = new TextView(Main.this);
				tv2.setText("Put events for this day here.");
				vs[1] = tv2;

				view.setListViewItems(vs);*/

			
				
				Calendar calendar = Calendar.getInstance();
				 
			    calendar.set(Calendar.DAY_OF_MONTH, view._currentDay);
			    calendar.set(Calendar.MONTH, view._currentMonth);
			    calendar.set(Calendar.YEAR, view._currentYear);

				
				_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(calendar, Color.CYAN,1) });
				
				
				_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(Calendar.getInstance(), Color.TRANSPARENT) });
			}
		});

		markDays();
	}
	

	
	
	private void markDays() {
		// TODO: Find items in the range of _calendar.getVisibleStartDate() and _calendar.getVisibleEndDate().
		// TODO: Create CalendarDayMarker for each item found.
		// TODO: Pass CalendarDayMarker array to _calendar.setDaysWithEvents(markers)
		
		//Example of setting just today with an event
		//System.out.println("xxx");
		_calendar.setDaysWithEvents(new CalendarDayMarker[] { new CalendarDayMarker(Calendar.getInstance(), Color.CYAN) });
	}

	private CalendarView _calendar;
	
	
	
	
	
}