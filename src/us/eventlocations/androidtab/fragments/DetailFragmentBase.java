package us.eventlocations.androidtab.fragments;

import us.eventlocations.androidtab.R;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.Html;

public class DetailFragmentBase extends Fragment implements OnTutSelectedListener{

	@Override
	public void onTutSelected(Boolean tutUri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTutSelected(String Title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallGPS(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogComingSoon() {
		// TODO Auto-generated method stub
		
	}

    
    protected void share_email(int accountNumber)
    {
    	String subject=getString(R.string.label_subject);
    	String email="";
    	String body ="";
		String[] mailto = { email };
	    Intent sendIntent = new Intent(Intent.ACTION_SEND);
	    // Add attributes to the intent
	    sendIntent.putExtra(Intent.EXTRA_EMAIL, "");
	    sendIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
	    StringBuffer text01 = new StringBuffer ();
	    text01.append("<font style='font-size: 16pt;' size='4'>"+getString(R.string.label_email0)+"<br></font>");
    	text01.append("<font style='font-size: 16pt;' size='4'>"+getString(R.string.label_email1)+"<br></font>");
    	text01.append("<font style='font-size: 16pt;' size='4'>"+getString(R.string.label_email2)+"<br></font>");
    	text01.append("<font style='font-size: 16pt;' size='4'>"+getString(R.string.label_email3)+"<br></font>");
    	text01.append("<br>");
    	text01.append("<br>");
    	text01.append("<font style='font-size: 16pt;' size='4'>"+getString(R.string.label_email4)+"<br></font>");
    	text01.append("<br>");
    	body = text01.toString();
	    
	    sendIntent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml (body)); 
	    sendIntent.setType("text/message");
	    /*if (attachment!=null)
	    {
	    	sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(attachment)));
	    }*/

	    startActivity(Intent.createChooser(sendIntent, "Complete action using"));
    }
	
    protected int findContact(String name) {
   	 
    	//String where = ContactsContract.Data.DISPLAY_NAME + " = ?";
    	String where = ContactsContract.CommonDataKinds.Organization.COMPANY + " = ?";
    	
    	
    	String[] params = new String[] {name};
        Cursor phoneCur = getActivity().managedQuery(ContactsContract.Data.CONTENT_URI, null, where, params, null);
        int size =phoneCur.getCount();
        //phoneCur.close();
        return size;
    }
}
