package us.eventlocations.androidtab;

import java.util.ArrayList;

import us.eventlocations.androidtab.fragments.DetailFragment;
import us.eventlocations.androidtab.fragments.DetailFragmentBridal;
import us.eventlocations.androidtab.fragments.DetailFragmentCounty;
import us.eventlocations.androidtab.fragments.DetailFragmentHoneymoon;
import us.eventlocations.androidtab.models.Accounts;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class DetailActivity extends Activity {
	/** Called when the activity is first created. */
    	
	public static ArrayList<Accounts> accounts;
	public static Accounts account;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
        	Bundle extras = getIntent().getExtras();
        	int opt=0;
            if (extras != null) 
            {
            	opt= extras.getInt("option");
            	if (opt == Common.FIND_BRIDAL_SHOW)
            	{
            		DetailFragmentBridal details = new DetailFragmentBridal();
                    details.setArguments(getIntent().getExtras());
                    getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();

            	}
            	else if (opt == Common.SEARCH_BY_COUNTY)
            	{
            		DetailFragmentCounty details = new DetailFragmentCounty();
                    details.setArguments(getIntent().getExtras());
                    getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
            		
            	}
            	else if (opt == Common.HONEYMOON)
            	{
            		DetailFragmentHoneymoon details = new DetailFragmentHoneymoon();
                    details.setArguments(getIntent().getExtras());
                    getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
            		
            	}
                else 
                {
                    DetailFragment details = new DetailFragment();
                    details.setArguments(getIntent().getExtras());
                    getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
                	
                }
           	
            }

        	
        }
    }
    
    
 
}
