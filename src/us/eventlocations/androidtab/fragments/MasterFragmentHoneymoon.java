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
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
//http://eventlocations.us/win
//http://eventlocations.us/admina/services/iphone.asmx
public class MasterFragmentHoneymoon extends ListFragment  implements OnTutSelectedListener{

	boolean mDualPane;
    int mCurCheckPosition = 0;
    private static String nameState="";
	private AccountListAdapter adapter;
	private int ipos;
	ArrayList<Accounts> accounts;
	final private Handler handler = new Handler();
	SoapObject returnObject;
	static String extra = "";
    private AccountListAdapter selectedAdapter;
	protected SharedPreferences preferences; 
	public  OnTutSelectedListener UI_UPDATE_LISTENER;
	private ArrayList<Accounts> accounts2;

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        
        Bundle extras = getActivity().getIntent().getExtras();        
        extra = extras.getString("which");
        
        preferences = getActivity().getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
		selectedAdapter = new AccountListAdapter(getView().getContext(),accounts,1);
		selectedAdapter.setNotifyOnChange(true);
        
        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        accounts2 =null;
        if (savedState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedState.getInt("curChoice", 0);
            accounts2 =  (ArrayList<Accounts>)savedState.getSerializable("account");
           // System.out.println("x");
        }
        
        /*if (accounts2 ==null)
        {*/
        	getAccounts(extra);
       /* }*/
        
        if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_PORTRAIT)
        {
	         if (mCurCheckPosition>0)
	         {
		    	
	        	 String state = accounts2.get(mCurCheckPosition).getState();
		    	
		    	if (UI_UPDATE_LISTENER!=null)
		    	{
		    		UI_UPDATE_LISTENER.onTutSelected(accounts2.get(mCurCheckPosition).getName());
		    	}
	
		    	if (state!=null)
		    	{
		    		accounts =accounts2;
			        showDetails(mCurCheckPosition);
			        save_Preference(preferences,"default_row",mCurCheckPosition); //here
		    	}
	        }
        }

        if (mDualPane) {
            // In dual-pane mode, list view highlights selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
         //   showDetails(mCurCheckPosition);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
        outState.putSerializable("account", accounts);
        
    }
    
    
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
	    try{
	    	UI_UPDATE_LISTENER = (OnTutSelectedListener) activity;
	    }catch(Exception e)
	    {
	    	
	    }
    
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
       
    	String state = accounts.get(pos).getState();

    	if (UI_UPDATE_LISTENER!=null)
    	{
    		UI_UPDATE_LISTENER.onTutSelected(accounts.get(pos).getName());
    	}

    	if (state!=null)
    	{
	        showDetails(pos);
	       // save_Preference(preferences,"default_row",pos);
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
            
       	
            DetailFragmentHoneymoon details = (DetailFragmentHoneymoon) getFragmentManager().findFragmentById(R.id.details);
            
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
            	
                details = DetailFragmentHoneymoon.newInstance(index);
                if(accounts != null){
                	
         
	               	DetailFragmentHoneymoon.setData(accounts, index);
                }
                // Execute a transaction, replacing any existing
                // fragment with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
           /*  else
            {
            	  details = DetailFragmentHoneymoon.newInstance(index);
                  if(accounts2 != null){
                  	
           
  	               	DetailFragmentHoneymoon.setData(accounts2, index);
                  }
                  // Execute a transaction, replacing any existing
                  // fragment with this one inside the frame.
                  FragmentTransaction ft = getFragmentManager().beginTransaction();
                  ft.replace(R.id.details, details);
                  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                  ft.commit();
            }*/
            
        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
        	DetailFragmentHoneymoon.setData(accounts, index);                
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("option", Common.HONEYMOON);
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
    	accounts = retrieveAccountsFromSoap(returnObject,ipos);
        this.adapter = new AccountListAdapter(getActivity(), accounts,ipos);
        //selectedAdapter.setSelectedPosition(ipos);
        setListAdapter(this.adapter);
    }
    
    public static ArrayList<Accounts> retrieveAccountsFromSoap(SoapObject soap,int pos){
    	ArrayList<Accounts> accounts = new ArrayList<Accounts>();  
    	String previousLetter = "";
        String currentLetter = "";
        String previousHeader = "";
        String currentHeader = "";
        boolean isNumberHeaderDone = false;
        
        
        if(extra.equals("GetMobileEnabledHoneymoonAccounts") ){
	       	 for (int i = 0; i < soap.getPropertyCount(); i++) {
	             SoapObject obj1 = (SoapObject)soap.getProperty(i);
	             Accounts account = new Accounts();
	             currentHeader = obj1.getProperty(7).toString();
	 				             
	             //if(!previousHeader.equals(currentHeader)){
	             if (currentHeader.equals("NA")){
	             		Accounts accountx = new Accounts();
	                 	accountx.setId(0);
	                 	accountx.setName(obj1.getProperty(1).toString().replace("-", " "));
	                    accounts.add(accountx);
	                  //  previousHeader = currentHeader;
	                 	accountx = null;
	             }
	             else
	             {
		             account.setId(Integer.parseInt(obj1.getProperty(0).toString()));
		             account.setName(obj1.getProperty(1).toString());
		             account.setContact(obj1.getProperty(2).toString());
		             account.setComments(obj1.getProperty(3).toString());
		             account.setAddress1(obj1.getProperty(4).toString());
		             account.setCity(obj1.getProperty(5).toString());
		             account.setState(obj1.getProperty(6).toString());
		             account.setUrl(obj1.getProperty(7).toString());
		             account.setZip(obj1.getProperty(8).toString());
		             account.setMap(Integer.parseInt(obj1.getProperty(9).toString()));
		             account.setServiceId(Integer.parseInt(obj1.getProperty(10).toString()));
		           //  account.setServiceName(obj1.getProperty(11).toString());            
		             accounts.add(account);
	             }
	         }        	
       }
        

        
        return accounts;
    }
   

    
   
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
    

	
	   
}
