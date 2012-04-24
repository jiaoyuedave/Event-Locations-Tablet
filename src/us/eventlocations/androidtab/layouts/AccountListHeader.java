package us.eventlocations.androidtab.layouts;

import us.eventlocations.androidtab.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountListHeader extends RelativeLayout {
	private TextView headerTitle;

	public AccountListHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setHeader(String val) {
		findViews();
		headerTitle.setText(val);
	}

	private void findViews() {
		headerTitle = (TextView)findViewById(R.id.headerTitle);
	}
}
