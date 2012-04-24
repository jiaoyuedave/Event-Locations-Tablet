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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

//http://eventlocations.us/admina/services/iphone.asmx
public class MasterFragmentCounty extends ListFragment {

	boolean mDualPane,mDualPane2;
    int mCurCheckPosition = 0;
    private static String nameState="";
	private AccountListAdapter adapter;
	ArrayList<Accounts> accounts;
	final private Handler handler = new Handler();
	SoapObject returnObject;
	static String extra = "";
	private SoapObject returnDetailObject;
	private boolean firstList=true;
	protected SharedPreferences preferences; 
	public  OnTutSelectedListener UI_UPDATE_LISTENER;
	private ArrayList<Accounts> accounts2;
	
	public void setUpdateListener(OnTutSelectedListener l) {
		UI_UPDATE_LISTENER = l;
	}
	
    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        
        Bundle extras = getActivity().getIntent().getExtras();        
        extra = extras.getString("which");
        

        
        getAccounts(extra);
        preferences = getActivity().getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
		save_Preference(preferences,"default_row",0);
		   
        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

       // View detailsFrame2 = getActivity().findViewById(R.id.details2);
       // mDualPane2 = detailsFrame2 != null && detailsFrame2.getVisibility() == View.VISIBLE;
        
        if (savedState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedState.getInt("curChoice", 0);
            accounts2 =  (ArrayList<Accounts>)savedState.getSerializable("account");
        }
        
        if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_PORTRAIT)
        {
	         if (mCurCheckPosition>0)
	         {
	        	 accounts =accounts2;
	        	/* String state = accounts2.get(mCurCheckPosition).getState();
		    	
		    	if (UI_UPDATE_LISTENER!=null)
		    	{
		    		UI_UPDATE_LISTENER.onTutSelected(accounts2.get(mCurCheckPosition).getName());
		    	}
	
		    	if (state!=null)
		    	{
		    		accounts =accounts2;
			       // showDetails(mCurCheckPosition);
			       // save_Preference(preferences,"default_row",mCurCheckPosition);
			        
			     */   
			        if (!firstList)
			        {
			        	//onTutSelected(true);
			        	if (UI_UPDATE_LISTENER!=null)
			        	{
			        		UI_UPDATE_LISTENER.onTutSelected(true);
			        		UI_UPDATE_LISTENER.onTutSelected(accounts.get(mCurCheckPosition).getName());
			        	}
			        	firstList= false;
			        	String ids =String.valueOf(accounts.get(mCurCheckPosition).getId());
			        	String name =String.valueOf(accounts.get(mCurCheckPosition).getName());
			        	
			    		//TextView textView = (TextView) v.findViewById(R.id.textViewTitle3);
			    		//textView.setVisibility(View.GONE);
			        	
			        	save_Preference(preferences,"default_row",mCurCheckPosition);	
			        	fillTitle(ids);
			        }
			        else
			        {
			    		//TextView textView = (TextView) v.findViewById(R.id.textViewTitle3);
			    		//textView.setVisibility(View.VISIBLE);
			        	
			        	if (UI_UPDATE_LISTENER!=null)
			        	{
			        	//	UI_UPDATE_LISTENER.onTutSelected(accounts.get(mCurCheckPosition).getName());
			        	}
			    		
			        	save_Preference(preferences,"default_row",mCurCheckPosition);	
			          	showDetailsNew(mCurCheckPosition);	
			        }
		    	}
	        /*}*/
        }

        if (mDualPane) {
            // In dual-pane mode, list view highlights selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
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
    	
    
        
        if (firstList)
        {
        	//onTutSelected(true);
        	if (UI_UPDATE_LISTENER!=null)
        	{
        		UI_UPDATE_LISTENER.onTutSelected(true);
        		UI_UPDATE_LISTENER.onTutSelected(accounts.get(pos).getName());
        	}
        	firstList= false;
        	String ids =String.valueOf(accounts.get(pos).getId());
        	String name =String.valueOf(accounts.get(pos).getName());
        	
       	
        	//save_Preference(preferences,"default_row",pos);
	        save_Preference(preferences,"default_row",accounts.get(pos).getId());
        	fillTitle(ids);
        }
        else
        {
       	
        	if (UI_UPDATE_LISTENER!=null)
        	{
        		//UI_UPDATE_LISTENER.onTutSelected(accounts.get(pos).getName());
        	}
    		
        	save_Preference(preferences,"default_row",pos);	
          	showDetailsNew(pos);	
        }
    	

    	

    }

    protected  void save_Preference(SharedPreferences preferences,String label,int value) {
        preferences.edit().putInt(label,value).commit();
    }	
    
    private void fillTitle(final String id){    	
    	Thread splashThread = new Thread() {
            @Override
            public void run() {            	
            	
         
            	WebServiceHelper webservice = new WebServiceHelper();
            	returnDetailObject = webservice.getDataDetailFromWebService(Common.GetAccountsByCounty,id);
            	handler.post(finishSetAccountImageUrl);
            	//WebServiceHelper webservice = new WebServiceHelper();
            }
         };
         splashThread.start();
    }
    
    private Runnable finishSetAccountImageUrl = new Runnable() {
 		 public void run() {
 			 finishedSetAccountImageUrl();
 		 }
 	};
    
 	
 	 public void finishedSetAccountImageUrl(){  // fill all the fields from fragment 2  	
       	
 	      	
 	    	accounts = retrieveBridalDetailFromSoap(returnDetailObject);
 	    	save_Preference(preferences,"default_row",-1);
 	        this.adapter = new AccountListAdapter(getActivity(), accounts,-1);
 	        setListAdapter(this.adapter);
 			//dialog.dismiss();
 	    } 
 	    
 	 
 	 public static ArrayList<Accounts> retrieveBridalDetailFromSoap(SoapObject soap){
     	ArrayList<Accounts> accounts = new ArrayList<Accounts>();  
     	String previousLetter = "";
         String currentLetter = "";
         String previousHeader = "";
         String currentHeader = "";
         boolean isNumberHeaderDone = false;
       //BridalShows{Id=201; Company=A Bridal Affair To Remember; Day=Wed; Datex=9/14/2011 6:45:00 PM; Location=Villa Barone Manor; Address=737 Throgs Neck Expwy Bronx, NY; Cost=Free; Notes=anyType{}; }
        	 for (int i = 0; i < soap.getPropertyCount(); i++) {
 	             SoapObject obj1 = (SoapObject)soap.getProperty(i);
 	             
 	             if (obj1.getProperty(3)!=null && obj1.getProperty(3).toString().length()>0)
 	             {
 		             Accounts account = new Accounts();
 		             
 		            /* int iof = obj1.getProperty(2).toString().lastIndexOf("-");
 		             String state ="";
 		             if (iof>0)
 		             {
 		            	 state = obj1.getProperty(2).toString().replace("-", "");
 		             }
 		             else
 		             {
 		            	 state = obj1.getProperty(2).toString();
 		             } 
 		            	   currentHeader = state;*/
 				             
 		  	             /*if(!previousHeader.equals(currentHeader)){ 	             	
 		  	             		Accounts accountx = new Accounts();
 		  	                 	accountx.setId(0);
 		  	                 	accountx.setName(currentHeader);
 		  	                    accounts.add(accountx);
 		  	                    previousHeader = currentHeader;
 		  	                 	accountx = null;
 		  	             }*/
 		             
 		             if (i==0)
 		             {
	             		Accounts accountx = new Accounts();
	                 	accountx.setId(0);
	                 	accountx.setName("Sites");
	                    accounts.add(accountx);
	                    //previousHeader = currentHeader;
	                 	accountx = null;
 		             }
 		             
 		  	             account.setId(Integer.parseInt(obj1.getProperty(0).toString()));
 		  	           //Account{Id=4868; Name=Abigail Kirsch at New York Botanical Garden; Contact=NA; Comments=NA; Address1=Southern Blvd (@ 200 Street); City=New York; State=NY; Url=www.abigailkirsch.com; Zip=10458; Map=1; ServiceId=0; }
 		  	           //String linea = obj1.getProperty(3).toString();  
 		  	          // String [] campos = linea.split("\\s+");  
 			  	      // String text = campos[0]+", "+obj1.getProperty(2).toString()+" @ "+ campos[1]+" "+ campos[2].toLowerCase();
 		  	            account.setName(obj1.getProperty(1).toString());
 		  	            account.setContact(obj1.getProperty(2).toString());
 		  	          // account.setCity(obj1.getProperty(5).toString());
 		  	            account.setAddress1(obj1.getProperty(4).toString()+", "+obj1.getProperty(5).toString()+", "+obj1.getProperty(6).toString()+" ,"+obj1.getProperty(8).toString());
 		  	          // account.setZip(obj1.getProperty(2).toString()+", "+campos[0]);
 		  	            account.setUrl(obj1.getProperty(7).toString());
 		  	            //account.setState("Sites");
 		  	         //BridalShows{Id=201; Company=A Bridal Affair To Remember; Day=Wed; Datex=9/14/2011 6:45:00 PM; Location=Villa Barone Manor; Address=737 Throgs Neck Expwy Bronx, NY; Cost=Free; Notes=anyType{}; }
 		  	           
 		  	           //account.setState(state);
 		  	           accounts.add(account);
 	             }
 	            	 
 	         }        	
         return accounts;
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
            DetailFragmentCounty details = (DetailFragmentCounty) getFragmentManager().findFragmentById(R.id.details);
            
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailFragmentCounty.newInstance(index);
                if(accounts != null){
                	DetailFragmentCounty.setData(accounts, index);
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
        	DetailFragmentCounty.setData(accounts, index);                
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("option", Common.SEARCH_BY_COUNTY);
            startActivity(intent);
        }
    }
    
    void showDetailsNew(int index) {
        mCurCheckPosition = index;
        
        if (mDualPane) {
            // We can display everything in-place with fragments.
            // Have the list highlight this item and show the data.
      //      getListView().setItemChecked(index, true);
                      
            // Check what fragment is shown, replace if needed.
            DetailFragmentCounty details = (DetailFragmentCounty) getFragmentManager().findFragmentById(R.id.details);
            
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailFragmentCounty.newInstance(index);
                if(accounts != null){
                	DetailFragmentCounty.setData(accounts, index);
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
        	DetailFragmentCounty.setData(accounts, index);                
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("option", Common.SEARCH_BY_COUNTY);
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
        this.adapter = new AccountListAdapter(getActivity(), accounts,-1);
        setListAdapter(this.adapter);
    }
    
    public static ArrayList<Accounts> retrieveAccountsFromSoap(SoapObject soap){
    	ArrayList<Accounts> accounts = new ArrayList<Accounts>();  
    	String previousLetter = "";
        String currentLetter = "";
        String previousHeader = "";
        String currentHeader = "";
        boolean isNumberHeaderDone = false;
        
        
        if(extra.equals("GetCounties") ){
       	 for (int i = 0; i < soap.getPropertyCount(); i++) {
	             SoapObject obj1 = (SoapObject)soap.getProperty(i);
	             Accounts account = new Accounts();
	             int iof = obj1.getProperty(1).toString().lastIndexOf("-");
	             String state ="";
	             if (iof>0)
	             {
	            	 state = obj1.getProperty(1).toString().replace("-", "");
	            	 currentHeader =state;
	            	 nameState =state ;
	             }
	             else
	             {
	            	  //currentHeader = obj1.getProperty(2).toString();
		             currentHeader =nameState;
		 				             
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
		             account.setState(obj1.getProperty(2).toString());
		             account.setContact(obj1.getProperty(2).toString());
		             accounts.add(account);
	             }
	         }        	
       }
        
    
        
        return accounts;
    }
    
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			
	        switch (keyCode) { 
	        case KeyEvent.KEYCODE_BACK: 
		        {

		        	 if (!firstList)
		             {
		        	        getAccounts("GetCounties");
		             }
		        	 return true;
		        }
	        	 
	        } 
	        return onKeyDown(keyCode, event); 
	   } 
	 
	 

    
}
