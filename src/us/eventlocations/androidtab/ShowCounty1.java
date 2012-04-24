package us.eventlocations.androidtab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ShowCounty1 extends ActivityBase {
	
	 private int RESULT_OK2=88;

	 public void onCreate(Bundle savedInstanceState) {
	    	requestWindowFeature(Window.FEATURE_NO_TITLE);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.showcounty1);

	        TextView option_1 = (TextView) findViewById(R.id.option_1);
	        TextView option_2 = (TextView) findViewById(R.id.option_2);
	        TextView option_3 = (TextView) findViewById(R.id.option_3);
	        TextView option_4 = (TextView) findViewById(R.id.option_4);
	        TextView option_5 = (TextView) findViewById(R.id.option_5);
	        TextView option_6 = (TextView) findViewById(R.id.option_6);
	        TextView option_7 = (TextView) findViewById(R.id.option_7);
	        TextView option_8 = (TextView) findViewById(R.id.option_8);
	        TextView option_9 = (TextView) findViewById(R.id.option_9);
	        TextView option_10 = (TextView) findViewById(R.id.option_10);
	        TextView option_11 = (TextView) findViewById(R.id.option_11);
	        TextView option_12 = (TextView) findViewById(R.id.option_12);
	        TextView option_13 = (TextView) findViewById(R.id.option_13);
	        TextView option_14 = (TextView) findViewById(R.id.option_14);

	        
	        option_1.setOnClickListener(mOnclick_select1);
	        option_2.setOnClickListener(mOnclick_select2);
	        option_3.setOnClickListener(mOnclick_select3);
	        option_4.setOnClickListener(mOnclick_select4);
	        option_5.setOnClickListener(mOnclick_select5);
	        option_6.setOnClickListener(mOnclick_select6);
	        option_7.setOnClickListener(mOnclick_select7);
	        option_8.setOnClickListener(mOnclick_select8);
	        option_9.setOnClickListener(mOnclick_select9);
	        option_10.setOnClickListener(mOnclick_select10);
	        option_11.setOnClickListener(mOnclick_select11);
	        option_12.setOnClickListener(mOnclick_select12);
	        option_13.setOnClickListener(mOnclick_select13);
	        option_14.setOnClickListener(mOnclick_select14);

	        
	       
	    }
	    
	  private OnClickListener mOnclick_select1 = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	            Intent returnIntent = new Intent();
	            returnIntent.putExtra("county1",1);
	            setResult(RESULT_OK2,returnIntent);   
	        	finish();
	        }
	    };

	  private OnClickListener mOnclick_select2 = new OnClickListener()
		{
		     public void onClick(View v)
		     {
		            Intent returnIntent = new Intent();
		            returnIntent.putExtra("county1",2);
		            setResult(RESULT_OK2,returnIntent);   
		        	finish();
		     }
		};
		    
	private OnClickListener mOnclick_select3 = new OnClickListener()
	 {
			public void onClick(View v)
			{
				Intent returnIntent = new Intent();
			    returnIntent.putExtra("county1",3);
			    setResult(RESULT_OK2,returnIntent);   
			    finish();
			 }
	 };
		    
	  private OnClickListener mOnclick_select4 = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	            Intent returnIntent = new Intent();
	            returnIntent.putExtra("county1",4);
	            setResult(RESULT_OK2,returnIntent);   
	        	finish();
	        }
	    };

	  private OnClickListener mOnclick_select5= new OnClickListener()
		{
		     public void onClick(View v)
		     {
		            Intent returnIntent = new Intent();
		            returnIntent.putExtra("county1",5);
		            setResult(RESULT_OK2,returnIntent);   
		        	finish();
		     }
		};
		    
	private OnClickListener mOnclick_select6 = new OnClickListener()
	 {
			public void onClick(View v)
			{
				Intent returnIntent = new Intent();
			    returnIntent.putExtra("county1",6);
			    setResult(RESULT_OK2,returnIntent);   
			    finish();
			 }
	 };		    


	  private OnClickListener mOnclick_select7 = new OnClickListener()
		{
		     public void onClick(View v)
		     {
		            Intent returnIntent = new Intent();
		            returnIntent.putExtra("county1",7);
		            setResult(RESULT_OK2,returnIntent);   
		        	finish();
		     }
		};
		    
	private OnClickListener mOnclick_select8 = new OnClickListener()
	 {
			public void onClick(View v)
			{
				Intent returnIntent = new Intent();
			    returnIntent.putExtra("county1",8);
			    setResult(RESULT_OK2,returnIntent);   
			    finish();
			 }
	 };	 
		    
	  private OnClickListener mOnclick_select9 = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	            Intent returnIntent = new Intent();
	            returnIntent.putExtra("county1",9);
	            setResult(RESULT_OK2,returnIntent);   
	        	finish();
	        }
	    };

	  private OnClickListener mOnclick_select10 = new OnClickListener()
		{
		     public void onClick(View v)
		     {
		            Intent returnIntent = new Intent();
		            returnIntent.putExtra("county1",10);
		            setResult(RESULT_OK2,returnIntent);   
		        	finish();
		     }
		};
		    
	private OnClickListener mOnclick_select11 = new OnClickListener()
	 {
			public void onClick(View v)
			{
				Intent returnIntent = new Intent();
			    returnIntent.putExtra("county1",11);
			    setResult(RESULT_OK2,returnIntent);   
			    finish();
			 }
	 };		    
		    
	  private OnClickListener mOnclick_select12 = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	            Intent returnIntent = new Intent();
	            returnIntent.putExtra("county1",12);
	            setResult(RESULT_OK2,returnIntent);   
	        	finish();
	        }
	    };

	  private OnClickListener mOnclick_select13 = new OnClickListener()
		{
		     public void onClick(View v)
		     {
		            Intent returnIntent = new Intent();
		            returnIntent.putExtra("county1",13);
		            setResult(RESULT_OK2,returnIntent);   
		        	finish();
		     }
		};
		    
	private OnClickListener mOnclick_select14 = new OnClickListener()
	 {
			public void onClick(View v)
			{
				Intent returnIntent = new Intent();
			    returnIntent.putExtra("county1",14);
			    setResult(RESULT_OK2,returnIntent);   
			    finish();
			 }
	 };		    
		    
		    
			    		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
}
