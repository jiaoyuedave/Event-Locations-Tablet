package us.eventlocations.androidtab;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;

public class ShareEmailActivity extends Activity{
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
       // sendEmail(getString(R.string.email_default),"","",null);
        sendEmail("","","",null);
    }
    
    //research enclosed file
	protected void sendEmail(String email,String body,String subject,String attachment) 
		{
		String[] mailto = { email };
	    Intent sendIntent = new Intent(Intent.ACTION_SEND);
	    // Add attributes to the intent
	    sendIntent.putExtra(Intent.EXTRA_EMAIL, "");
	    sendIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
	   // sendIntent.putExtra(Intent.EXTRA_TEXT,body);
	    //sendIntent.putExtra(Intent.EXTRA_CC,body);
	  //  String sbody=TextUtils.htmlEncode(body); 
	    sendIntent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml (body)); 
	    //sendIntent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml("<HTML><BODY><b><IMG SRC= 'data:image/jpeg;base64,' + 'http://www.slottsfjell.no/wp-content/uploads/2011/01/header_web_3.png' + '' alt = 'pleaseview this image'/></b></BODY></HTML>"));	    
	    sendIntent.setType("text/message");
	    if (attachment!=null)
	    {
	    	sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(attachment)));
	    }
	    
	    //sendIntent.setType("image/jpg");
	    //sendIntent.setType("text/plain");
	   //sendIntent.setType("image/*");
	    //sendIntent.setType("message/rfc822");
	    startActivity(Intent.createChooser(sendIntent, "Complete action using"));
	    finish();
	}
}