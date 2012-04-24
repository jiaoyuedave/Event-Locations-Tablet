package us.eventlocations.androidtab;

import java.util.ArrayList;

import org.ksoap2.serialization.SoapSerializationEnvelope;

import us.eventlocations.androidtab.utilityfunctions.WebServiceHelper;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EmailFormActivity extends Activity {

	 ProgressDialog dialog;
	 private Configuration c;
	 protected SharedPreferences preferences; 
	 private EditText yourName,YourEmail,Subject,Message;
	 final private Handler handler = new Handler();
	 private int accid;
	 private String titleemail;
	 
	 public SoapSerializationEnvelope envelope;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	
    	Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();  
        
        if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_LANDSCAPE)
        {
        	setContentView(R.layout.email_form);
        }
        else
        {
        	setContentView(R.layout.email_form_portrait);
        }
        
        
        
        Button sendEmail = (Button) findViewById(R.id.sendEmail);
        Button sendEmail2 = (Button) findViewById(R.id.sendEmail2);
        sendEmail.setOnClickListener(mOnclick_gotoSendMail);
        sendEmail2.setOnClickListener(mOnclick_gotoSendMail);

        Button cancelEmail = (Button) findViewById(R.id.cancelEmail);
        Button cancelEmail2 = (Button) findViewById(R.id.cancelEmail2);
        cancelEmail.setOnClickListener(mOnclick_gotocancelEmail);
        cancelEmail2.setOnClickListener(mOnclick_gotocancelEmail);

        
        TextView titleEmail = (TextView) findViewById(R.id.titleemail);
        
    	Bundle extras = getIntent().getExtras();
    	
        if (extras != null) 
        {
        	titleemail="";
        	titleemail= extras.getString("titleemail");
        	accid= extras.getInt("accid");
        	titleEmail.setText(titleemail);
        }
        
        settings();
        
    }
    
    private void settings()
    {
    	yourName = (EditText) findViewById(R.id.et_name);
    	YourEmail = (EditText) findViewById(R.id.et_email);
    	Subject = (EditText) findViewById(R.id.et_subject);
    	//Subject.setText(titleemail+" inquiry via Event Locations");
    	Subject.setText(titleemail+" inquiry");
    	
    	  Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
		  String possibleEmail ="";
		  if (accounts!=null && accounts.length>0)
		  {
			possibleEmail = accounts[0].name;  
		  }
		  YourEmail.setText(possibleEmail);
		  
    	 
    	Message= (EditText) findViewById(R.id.et_message);
    	
    }
    
    
    private OnClickListener mOnclick_gotocancelEmail = new OnClickListener()
	{
        public void onClick(View v)
        {
	    	//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        	
        	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        	imm.hideSoftInputFromWindow(YourEmail.getWindowToken(), 0);
        	imm.hideSoftInputFromWindow(yourName.getWindowToken(), 0);
        	imm.hideSoftInputFromWindow(Subject.getWindowToken(), 0);
        	
        	finish();
        }
	 };
	 
	 
    private OnClickListener mOnclick_gotoSendMail = new OnClickListener()
	{
        public void onClick(View v)
        {

       	 	String syourName=yourName.getText().toString();
       	 	String syourEmail=YourEmail.getText().toString();
       	 	String ssubject=Subject.getText().toString();
       	 	String smessagee=Message.getText().toString();
        	ArrayList error = new ArrayList ();
        	boolean byourname=false,byouremail=false,bsubject=false,bmessage=false;
        	
       	 	if (syourName==null || syourName.length()==0)
       	 	{
       	 		error.add("Name is required");
       	 		byourname=true;
       	 	}
       	 	if (syourEmail==null || syourEmail.length()==0)
       	 	{
       	 		error.add("Email is required");
       	 		byouremail=true;
       	 	}
       	 	if (ssubject==null || ssubject.length()==0)
       	 	{
       	 		error.add("Subject is required");
       	 		bsubject=true;
       	 	}
       	 	if (smessagee==null || smessagee.length()==0)
       	 	{
       	 		error.add("Message is required");
       	 		bmessage=true;
       	 	}
       	 	
       	 	
       	 	if (byourname || byouremail || bsubject || bmessage)
       	 	{
       	 		showMessage(getString(R.string.form_errors), error,-1);
       	 	}
       	 	else
       	 	{
       	 		sendEmail(syourName,syourEmail,ssubject,smessagee,accid);
       	 	}
       	 	
        	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        	imm.hideSoftInputFromWindow(YourEmail.getWindowToken(), 0);
        	imm.hideSoftInputFromWindow(yourName.getWindowToken(), 0);
        	imm.hideSoftInputFromWindow(Subject.getWindowToken(), 0);
        	
       	 		  
        }
    };
  
    private void sendEmail(final String yourname, final String yourEmail,final String ssubject, final String smessage, final int accid){    	
    	Thread splashThread = new Thread() {
            @Override
            public void run() {            	
            	
            	WebServiceHelper webservice = new WebServiceHelper();
            	webservice.getDataDetailFromWebServiceSentEmail(Common.SendAccountEmail,yourname,yourEmail,ssubject,smessage,accid);
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
		 showMessageOK(getString(R.string.locationMagazine),getString(R.string.locationMagazine_body));

	    } 
	 
	 @Override  
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {   
	    
		  try{
		  	
		      if (resultCode == RESULT_OK) {  
		    
		    	  finish();
			        	     
		       }
		          
		      else if (resultCode == Activity.RESULT_CANCELED)
		      {
		    	
		      }
		      
		  }catch(Exception e) 
		  {
			  String error = e.getMessage();
			  Toast.makeText(this, error, Toast.LENGTH_LONG).show();
		  }
	  }
	 
	 protected void showMessageOK(String messageString, String  titleMessage){
			  Bundle bundle = new Bundle();
		      bundle.putString("title",messageString);
		      bundle.putString("text",titleMessage);
		      bundle.putString("flag","emailsent");
		      
		      /*if (entity!=-1)
		    	  bundle.putString("controlFocus",getString(entity));*/
			  Intent myIntent = new Intent(getApplicationContext(), ShowMessageActivity.class);
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
    
}
