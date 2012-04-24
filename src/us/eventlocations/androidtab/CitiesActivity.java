package us.eventlocations.androidtab;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class CitiesActivity extends Activity {
    // Scrolling flag
    private boolean scrolling = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cities_layout);
        
        final WheelView country = (WheelView) findViewById(R.id.country);
        country.setVisibleItems(4);
        country.setViewAdapter(new CountryAdapter(this));

        /*country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
			    if (!scrolling) {
			        updateCities(city, cities, newValue);
			    }
			}
		});*/
        
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
    }
    
    /**
     * Updates the city wheel
     */
   /* private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter =
            new ArrayWheelAdapter<String>(this, cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(cities[index].length / 2);        
    }*/
    
    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String eventsite[] =
            new String[] {"Catering Hall", "Club", "Conference Center", "Country Club", "Event Site", "Hotel", "Loft","Mansion", "Museums", "Oceanfront /Wavefront", "Private Club", "Restaurant", "Sport Event Site","Temple","Townhouse","Yatch/Boat"};
        
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
}
