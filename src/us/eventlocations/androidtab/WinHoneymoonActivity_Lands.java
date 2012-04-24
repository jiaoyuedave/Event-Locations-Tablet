package us.eventlocations.androidtab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WinHoneymoonActivity_Lands extends Activity {
	// protected SharedPreferences preferences; 
	 ProgressDialog dialog;
	    private void settings()
	    {
	    	Button rules_regulation = (Button)findViewById(R.id.rules_regulation);
	    	rules_regulation.setOnClickListener(mOnclick_rules_regulation);

	    	Button next = (Button)findViewById(R.id.next);
	    	next.setOnClickListener(mOnclick_next);

	    }
	    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.by_winhoneymoon);
        settings();
    }      
    
	
    private OnClickListener mOnclick_rules_regulation = new OnClickListener()
	{
        public void onClick(View v)
        {
        	 Intent mainIntent = new Intent(WinHoneymoonActivity_Lands.this, RulesRegulationsActivity.class);
        	 startActivity(mainIntent);
        }
    };

    
    private OnClickListener mOnclick_next = new OnClickListener()
	{
        public void onClick(View v)
        {
        	 Intent mainIntent = new Intent(WinHoneymoonActivity_Lands.this, WinHoneymoonNext.class);
        	 startActivity(mainIntent);
        }
    };
}
