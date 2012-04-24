package us.eventlocations.androidtab.fragments;

import java.util.ArrayList;
import org.ksoap2.serialization.SoapObject;

import us.eventlocations.androidtab.Common;
import us.eventlocations.androidtab.DetailActivity;
import us.eventlocations.androidtab.R;
import us.eventlocations.androidtab.adapters.AccountListAdapter;
import us.eventlocations.androidtab.models.Accounts;
import us.eventlocations.androidtab.utilityfunctions.WebServiceHelper;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

//http://eventlocations.us/admina/services/iphone.asmx
public class MasterFragmentBridal extends ListFragment {
	protected SharedPreferences preferences; 
	boolean mDualPane,mDualPane2;
    int mCurCheckPosition = 0;
    private static String nameState="";
	private AccountListAdapter adapter;
	ArrayList<Accounts> accounts;
	final private Handler handler = new Handler();
	SoapObject returnObject;
	static String extra = "";


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        
        Bundle extras = getActivity().getIntent().getExtras();        
        extra = extras.getString("which");
        accounts = null;
        getAccounts(extra);
        preferences = getActivity().getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

       // View detailsFrame2 = getActivity().findViewById(R.id.details2);
       // mDualPane2 = detailsFrame2 != null && detailsFrame2.getVisibility() == View.VISIBLE;
        
        if (savedState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, list view highlights selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
          //  showDetails(mCurCheckPosition);
        }
        
        /*if (mDualPane2) {
            // In dual-pane mode, list view highlights selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails2(mCurCheckPosition);
        }*/
     //   showDetails2();
    }
    
   /* void showDetails2() {
        
        // Check what fragment is shown, replace if needed.
        DetailFragmentBridalRight details = (DetailFragmentBridalRight) getFragmentManager().findFragmentById(R.id.details2);
        
        if (details == null ) {
            // Make new fragment to show this selection.
            details = DetailFragmentBridalRight.newInstance(0);
            if(accounts != null){
            	DetailFragmentBridalRight.setData(accounts, 0);
            }
            // Execute a transaction, replacing any existing
            // fragment with this one inside the frame.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.details2, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
        
 /*   } else {
        // Otherwise we need to launch a new activity to display
        // the dialog fragment with selected text.
    	DetailFragmentBridalRight.setData(accounts, index);                
        Intent intent = new Intent();
        intent.setClass(getActivity(), DetailActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }*/
    //}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
    	
    	String state = accounts.get(pos).getState();
    	if (state!=null)
    	{
	        showDetails(pos);
	        //save_Preference(preferences,"default_row",pos);
	        save_Preference(preferences,"default_row",accounts.get(pos).getId());
    	}
    }

    
    protected  void save_Preference(SharedPreferences preferences,String label,int value) {
        preferences.edit().putInt(label,value).commit();
    }	
    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;
       
        if (mDualPane) {
            // We can display everything in-place with fragments.
            // Have the list highlight this item and show the data.
            getListView().setItemChecked(index, true);
                      
            // Check what fragment is shown, replace if needed.
            DetailFragmentBridal details = (DetailFragmentBridal) getFragmentManager().findFragmentById(R.id.details);
            
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailFragmentBridal.newInstance(index);
                if(accounts != null){
                	DetailFragmentBridal.setData(accounts, index);
                }
                // Execute a transaction, replacing any existing
                // fragment with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
            
        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
        	DetailFragmentBridal.setData(accounts, index);                
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("option", Common.FIND_BRIDAL_SHOW);
            startActivity(intent);
        }
    }
    
  
    
    private void getAccounts(final String which){    	
    	Thread splashThread = new Thread() {
            @Override
            public void run() {            	
            	WebServiceHelper webservice = new WebServiceHelper();
            	returnObject = webservice.getDataFromWebService(which);
            	handler.post(finishedLoadingData);
            }
         };
         splashThread.start();
    }
    
    private Runnable finishedLoadingData = new Runnable() {
		 public void run() {
			 finishedLoadingData();
		 }
	};
	
    public void finishedLoadingData(){
        // Populate list with our static array of titles.
    	accounts = retrieveAccountsFromSoap(returnObject);
        this.adapter = new AccountListAdapter(getActivity(), accounts,1);
        setListAdapter(this.adapter);
    }
    
    public static ArrayList<Accounts> retrieveAccountsFromSoap(SoapObject soap){
    	ArrayList<Accounts> accounts = new ArrayList<Accounts>();  
    	String previousLetter = "";
        String currentLetter = "";
        String previousHeader = "";
        String currentHeader = "";
        boolean isNumberHeaderDone = false;
        
        
        if(extra.equals("GetBridalCompanies") ){
       	 for (int i = 0; i < soap.getPropertyCount(); i++) {
	             SoapObject obj1 = (SoapObject)soap.getProperty(i);
	             Accounts account = new Accounts();
	             
	             int iof = obj1.getProperty(2).toString().lastIndexOf("-");
	             String state ="";
	             if (iof>0)
	             {
	            	 state = obj1.getProperty(2).toString().replace("-", "");
	             }
	             else
	             {
	            	 state = obj1.getProperty(2).toString();
	             } 
	            	   currentHeader = state;
			             
	  	             if(!previousHeader.equals(currentHeader)){ 	             	
	  	             		Accounts accountx = new Accounts();
	  	                 	accountx.setId(0);
	  	                 	accountx.setName(currentHeader);
	  	                    accounts.add(accountx);
	  	                    previousHeader = currentHeader;
	  	                 	accountx = null;
	  	             }
	  	             account.setId(Integer.parseInt(obj1.getProperty(0).toString()));
	  	             account.setName(obj1.getProperty(1).toString());
	  	             
	  	           
	  	             
	  	             account.setState(state);
	  	             accounts.add(account);
	           
	             
	          
	         }        	
       }
        
    
        
        return accounts;
    }
    
    
    
}
