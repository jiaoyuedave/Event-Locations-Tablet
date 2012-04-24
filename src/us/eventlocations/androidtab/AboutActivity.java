package us.eventlocations.androidtab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutActivity extends ActivityBase {

	 ProgressDialog dialog;
	 private String text1="",text2="";
	 private Configuration c;
	 protected SharedPreferences preferences; 
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_about);
        //setContentView(R.layout.about);
        
        text1 =getTitle1();
        
        TextView contactUs = (TextView) findViewById(R.id.contactUs);
        contactUs.setOnClickListener(mOnclick_gotoEmail);
    }
    
    private String getTitle1()
    {
    	
    	try {

    		
    		myProgressDialog = ProgressDialog.show(AboutActivity.this,"", "Please wait while loading data ...", true);
      	  new Thread() { 
  		    	public void run() { 
  	           try{ 
  	        	 text1=sendRegister(Common.urlAbout);
  	        	   
  	        	   
  	           } catch (Exception e) 
  	           { 
  	        	e.getMessage
  	        	();   
  	           } 
  	           
  	          
  	           Message msg = new Message();
                 msg.what = 1;
                 handler4.sendMessage(msg);

  	           // Dismiss the Dialog 
  		    	} 
  		  }.start();
    		
    		
	    	System.out.println(text1 );
		  
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	
    	return text1;
    }
    
    
    protected Handler handler4 = new Handler() {
    	//@Override
    	public void handleMessage(Message msg) {

            myProgressDialog.dismiss();
            
            TextView textView1 = (TextView) findViewById(R.id.body1);
            textView1.setText(text1);
            text2=getTitle2();
    	}
    };
    
    protected Handler handler5 = new Handler() {
    	//@Override
    	public void handleMessage(Message msg) {

    		myProgressDialog2.dismiss();
    		
            
           TextView textView2 = (TextView) findViewById(R.id.body2);
           textView2.setText(text2);


    	}
    };
	protected String sendRegister(String xmlContentToSend){
		
		HttpResponse response =null;
    	String result = null;
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	try
    	{

    		HttpGet httpget = null;
    		httpget = new HttpGet(xmlContentToSend);
    		response= httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			InputStream instream = entity.getContent();
			result = convertStreamToString(instream);

    	}
    	catch (Exception e)
    	{
			System.out.println(e.getMessage());
    	}
    	
    	return result;
	}
	
	
	protected static String convertStreamToString(InputStream is)
    {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}


    private String getTitle2()
    {

    	try {

    		
    		myProgressDialog2 = ProgressDialog.show(AboutActivity.this,"", "Please wait while loading data ...", true);
      	  new Thread() { 
  		    	public void run() { 
  	           try{ 
  	        	 text2 = sendRegister(Common.urlAdditional);
  	        	   
  	        	   
  	           } catch (Exception e) 
  	           { 
  	        	e.getMessage
  	        	();   
  	           } 
  	           
  	          
  	           Message msg = new Message();
                 msg.what = 1;
                 handler5.sendMessage(msg);
  	           // Dismiss the Dialog 
  		    	} 
  		  }.start();
    		
    		
	    	System.out.println(text2 );
		  
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	
    	return text2;
    }
    private OnClickListener mOnclick_gotoEmail = new OnClickListener()
	{
        public void onClick(View v)
        {
        	sendEmail(Common.email,Common.emailBody,"",""); 
        }
    };
  
	protected void sendEmail(String email,String body,String subject,String attachment) 
	{
		String[] mailto = { email };
	    Intent sendIntent = new Intent(Intent.ACTION_SEND);
	    // Add attributes to the intent
	    sendIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
	    sendIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
	    sendIntent.putExtra(Intent.EXTRA_TEXT,body);
	    sendIntent.setType("text/html");
	   /* if (attachment!=null)
	    {
	    	sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(attachment)));
	    }*/
	    
	    startActivity(Intent.createChooser(sendIntent, "SendMail"));
	    finish();
	}
    
    
    public void searchByCountyClickHandler(View view){
    	   dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);    	
     	Thread splashThread = new Thread() {
             @Override
             public void run() {
            	 
            	 Bundle bundle = new Bundle();
            	 bundle.putInt("option", Common.SEARCH_BY_COUNTY);
             	 Intent mainIntent = new Intent(AboutActivity.this, AccountsBySiteNameActivity.class);
             	 mainIntent.putExtra("which", "GetCounties");
             	 mainIntent.putExtras(bundle);
             	 AboutActivity.this.startActivity(mainIntent);
             	 dialog.dismiss();
             }
          };
          splashThread.start();
     }
    
    
  
    
   
    public void call_email(){
        Intent myIntent = new Intent(getApplicationContext(), ShareEmailActivity.class);
	    startActivityForResult(myIntent,0);
  }
    
   

    
}
