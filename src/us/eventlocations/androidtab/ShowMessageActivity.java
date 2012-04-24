package us.eventlocations.androidtab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Simple screen that show the incoming push message together with an icon.
 * Started after receiving the push message in
 * {@link MyPushReceiver#onPushMessageReceived(android.content.Context, String)}
 * .
 */
public class ShowMessageActivity extends Activity {

   /**
    * Tag for logging.
    */
   private static final String TAG = "ShowMessageActivity";
   private String controlFocus,flag;

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  requestWindowFeature(Window.FEATURE_NO_TITLE); 
      super.onCreate(savedInstanceState);
      //requestWindowFeature(Window.FEATURE_LEFT_ICON);

      
      Intent trigger = getIntent();
        
      String typeDialog = trigger.getStringExtra("type");
     
      if (typeDialog!=null && typeDialog.equals("messageOK"))
    	  setContentView(R.layout.show_msg_short);  
      else
    	  setContentView(R.layout.show_msg);

      
            	 
      // Get the text view to update.
      TextView msgTitle= (TextView) findViewById(R.id.message_title);
      TextView msgView = (TextView) findViewById(R.id.message_static);
      //ImageView btn_cancel = (ImageView) findViewById(R.id.btn_cancel);
      settingsControl();
      // Get the sms message from the Intent and display it.
    
     String msg = trigger.getStringExtra("text");
     String title = trigger.getStringExtra("title");
     controlFocus  = trigger.getStringExtra("controlFocus");
     flag =trigger.getStringExtra("flag");
     
     
    // this.setTitle(title);
     msgView.setText(msg);
     msgTitle.setText(title);
   }
   
   private void settingsControl()
   {
	   Button btn_cancel= (Button)findViewById(R.id.button_ok);	
	   btn_cancel.setOnClickListener(cancel_popup);
   }
    
   private OnClickListener cancel_popup = new OnClickListener()
	{
       public void onClick(View v)
       {
   	      	  Bundle bundle = new Bundle();
	          bundle.putString("controlFocus",controlFocus);
	          
	          Intent mIntent = new Intent();
	          mIntent.putExtras(bundle);
	          
	          if (flag.equals("emailsent"))
	        	  setResult(RESULT_OK, mIntent);
	          else
	        	  setResult(RESULT_CANCELED, mIntent);
	          
    	   finish();
       }
   };
   




}
