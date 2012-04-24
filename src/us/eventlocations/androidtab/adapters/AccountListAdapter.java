package us.eventlocations.androidtab.adapters;

import java.util.ArrayList;

import us.eventlocations.androidtab.R;
import us.eventlocations.androidtab.layouts.AccountListHeader;
import us.eventlocations.androidtab.layouts.AccountListItem;
import us.eventlocations.androidtab.models.Accounts;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountListAdapter extends ArrayAdapter<Accounts> {

		private Context context;
		private String searchType = "";		
		// used to keep selected position in ListView
		private int selectedPos = -1;	// init value for not-selected
		private   SharedPreferences p;
		private int row;

		
		public void setSelectedPosition(int pos){
			selectedPos = pos;
			// inform the view of this change
			//notifyDataSetChanged();

		}
		
		public int getSelectedPosition(){
			return selectedPos;
		}
		
		public AccountListAdapter(Context context, ArrayList<Accounts> accountList,int xpos) {
			super(context, android.R.layout.simple_list_item_1, accountList);
			this.context = context;
			this.searchType = searchType;
			
			  //SharedPreferences p = convertView.getContext().getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
			  p = context.getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
			  if (accountList!=null)
			  {
				  int x = accountList.size();
				  System.out.println("count :"+x);
					
			  }
			//selectedPos=xpos;
			  
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AccountListItem v = null;
			AccountListHeader h = null;
			//ViewGroup viewGroup = null;

			//notifyDataSetChanged();
				Accounts acc = (Accounts)getItem(position);
				String name= acc.getName();

				if(acc.getId()==0){ 
					
					if (null == convertView) {
						h = (AccountListHeader) View.inflate(context, R.layout.account_list_header, null);
					}
					else{
						if(convertView.getClass().getName().equals("us.eventlocations.androidtab.layouts.AccountListHeader"))
							h = (AccountListHeader) convertView;
						else
							h = (AccountListHeader) View.inflate(context, R.layout.account_list_header, null);
					}
					h.setHeader(acc.getName());
					return h;
				}
								
				if (null == convertView) {
					v = (AccountListItem) View.inflate(context, R.layout.account_list_item, null);
					//viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.account_list_item, null);

					
				} else {
					/*if(convertView.getClass().getName().equals("us.eventlocations.androidtab.layouts.AccountListItem"))
						v = (AccountListItem) convertView;
					
					else
					{*/
					 
						v = (AccountListItem) View.inflate(context, R.layout.account_list_item, null);
						//viewGroup = (ViewGroup) convertView;
						// notifyDataSetChanged();
					    // get text view
				        TextView label = (TextView)v.findViewById(R.id.account_name);
						//TextView label = (TextView)viewGroup.findViewById(R.id.account_name);
						
						if (label!=null)
						{
					        // change the row color based on selected state
							  row= get_Preferences(p,"default_row");
							  AccountListItem al =(AccountListItem)v.findViewById(R.id.fond);
							  //AccountListItem al =(AccountListItem)viewGroup.findViewById(R.id.fond);
							  
							  
			                    if(row == acc.getId())
			                    {
			                       //erase comment 
			                    	notifyDataSetChanged();
						        	label.setTextColor(Color.WHITE);
						        	al.setBackgroundColor(Color.BLUE);
			                    } else
			                    {
			        	        	al.setBackgroundColor(Color.TRANSPARENT);
			                    }							  
						}
				}
				
				
			
				
			v.setAccount((Accounts) getItem(position));
			//	((AccountListItem) viewGroup).setAccount((Accounts) getItem(position));
			return v;
			// return viewGroup;
		}	
		
	    private static int get_Preferences(SharedPreferences preferences,String value) {
	        return   preferences.getInt(value, 0);
	      }
		
}
