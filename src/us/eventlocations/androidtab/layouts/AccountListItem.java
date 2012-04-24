package us.eventlocations.androidtab.layouts;

import us.eventlocations.androidtab.R;
import us.eventlocations.androidtab.models.Accounts;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountListItem extends RelativeLayout{

	private TextView accountName;

	public AccountListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setAccount(Accounts account) {
		findViews();
		accountName.setText(account.toString());
	}
	
	private void findViews() {
		accountName = (TextView)findViewById(R.id.account_name);
	}
}
