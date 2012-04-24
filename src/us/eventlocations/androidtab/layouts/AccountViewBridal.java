package us.eventlocations.androidtab.layouts;

import us.eventlocations.androidtab.R;
import us.eventlocations.androidtab.models.Accounts;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountViewBridal extends RelativeLayout {
	public TextView accountName;
    public ImageView accountImage;
    public TextView accountAddress;
 /*   public TextView accountWebsite;
    public TextView accountReception;
    public TextView accountDinner;
    public TextView accountComments;*/
//    public RelativeLayout layout_main;

    public AccountViewBridal(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setAccount(Accounts account) {
		this.findViews();
		//accountName.setText(account.toString());

		//String accountAddressString = account.getAddress1() + ", " + account.getCity() + ", " + account.getState() + " - " + account.getZip();
		String accountAddressString = account.getName();
      	//String accountWebsiteString = account.getUrl();

      	if(accountAddressString != null){
    		accountAddress.setText(accountAddressString);
      	}
      /*	if(accountWebsiteString !=null){
    		accountWebsite.setText(accountWebsiteString);
      	}*/

      	//layout_main.setVisibility(View.VISIBLE);
	}

	private void findViews() {
		//accountName = (TextView)findViewById(R.id.textView1xx);
		accountImage = (ImageView)findViewById(R.id.accountImageView);
		accountAddress = (TextView)findViewById(R.id.accountAddress);
		//accountImage.setVisibility(View.GONE);
		
		/*accountWebsite = (TextView)findViewById(R.id.accountWebsite);
		accountReception = (TextView)findViewById(R.id.accountReception);
		accountDinner = (TextView)findViewById(R.id.accountDinner);
		accountComments = (TextView)findViewById(R.id.accountComments);
		accountComments.setMovementMethod(new ScrollingMovementMethod());

		accountWebsite.setVisibility(View.GONE);
		accountReception.setVisibility(View.GONE);
		accountDinner.setVisibility(View.GONE);
		accountComments.setVisibility(View.GONE);
		accountImage.setVisibility(View.GONE);*/
		
		//layout_main = (RelativeLayout)findViewById(R.id.layout_main);
	}
	
	  
}
