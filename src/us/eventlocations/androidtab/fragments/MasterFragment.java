
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

//http://eventlocations.us/admina/services/iphone.asmx
public class MasterFragment extends ListFragment  implements OnTutSelectedListener{

	boolean mDualPane;
    int mCurCheckPosition = 0;
    private static String nameState="";
	private AccountListAdapter adapter;
	private int ipos;
	ArrayList<Accounts> accounts,accountsFiltred;
	final private Handler handler = new Handler();
	SoapObject returnObject;
	static String extra = "",text="";
    private AccountListAdapter selectedAdapter;
	protected SharedPreferences preferences; 
	public  OnTutSelectedListener UI_UPDATE_LISTENER;
	private ArrayList<Accounts> accounts2;
	
    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        
        Bundle extras = getActivity().getIntent().getExtras();        
        extra = extras.getString("which");
        text= extras.getString("text");
        
        getAccounts(extra);
        //simulateKeyStroke(KeyEvent.KEYCODE_BACK, getActivity());
        preferences = getActivity().getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
        //accounts = null;
        
        selectedAdapter = new AccountListAdapter(getView().getContext(),accounts,1);
        selectedAdapter.setNotifyOnChange(true);
        //selectedAdapter.notifyDataSetChanged();
      //  selectedAdapter.notifyDataSetChanged();
        
        
        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedState.getInt("curChoice", 0);
            accounts2 =  (ArrayList<Accounts>)savedState.getSerializable("account");
        }

        
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
			        save_Preference(preferences,"default_row",mCurCheckPosition);
			        //simulateKeyStroke(KeyEvent.KEYCODE_BACK, getActivity());
		    	}
	        }
        }
        
        if (mDualPane) {
            // In dual-pane mode, list view highlights selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            getListView().setItemsCanFocus(true);

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
    	//ipos = pos;
    	/*selectedAdapter.setSelectedPosition(pos);
    	getListView().setItemChecked(pos, true);
        getListView().setSelection(pos);
        getListView().setFocusable(true);
        getListView().setFocusableInTouchMode(true);*/
       
    	String state = accounts.get(pos).getState();

    	if (UI_UPDATE_LISTENER!=null)
    	{
    		UI_UPDATE_LISTENER.onTutSelected(accounts.get(pos).getName());
    	}

  	  	//TextView textView = (TextView) v.findViewById(R.id.textViewTitle1);
    	//textView.setText("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    	
    	if (state!=null)
    	{
	        showDetails(pos);
	     //   save_Preference(preferences,"default_row",pos);
	        save_Preference(preferences,"default_row",accounts.get(pos).getId());
    	}
    	
 
    	//InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);       
        //inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),  InputMethodManager.HIDE_IMPLICIT_ONLY);
    	
       // View rowview = (View) l.getChildAt(pos);
        //rowview.setSelected(true);
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
            // Check what fragment is shown, replace if needed.
        	// getListView().setItemChecked(index, true);
            DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.details);
            
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
            	
                details = DetailFragment.newInstance(index);
                if(accounts != null){
	               	DetailFragment.setData(accounts, index,text);
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
        	DetailFragment.setData(accounts, index,text);                
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("option", Common.SEARCH_BY_SITE_NAME);
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
    	accountsFiltred = new ArrayList<Accounts> (); 
    	for (int i=0;i<accounts.size();i++)
    	{
    		String name = accounts.get(i).getName().toLowerCase();
    		if (text!=null && text.length()>0)
    		{	
	    		int cos=name.indexOf(text.toLowerCase());
	    		if (cos>=0)
	    		{
	    			accountsFiltred.add( accounts.get(i)) ;  			
	    		}
    		}
    	}
    	
    	if (accountsFiltred!=null && accountsFiltred.size()>0)
    	{
    		this.adapter = new AccountListAdapter(getActivity(), accountsFiltred,ipos);
    		accounts =accountsFiltred;
    	}
    	else
    		this.adapter = new AccountListAdapter(getActivity(), accounts,ipos);
    	
    	
        //selectedAdapter.setSelectedPosition(ipos);
        setListAdapter(this.adapter);
       // simulateKeyStroke(KeyEvent.KEYCODE_BACK, getActivity());
        
    	//getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    	//InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    	//imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm !=null && getActivity()!=null && getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus().getWindowToken()!=null)
        {	
        	imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    	
    }
    
    public static ArrayList<Accounts> retrieveAccountsFromSoap(SoapObject soap,int pos){
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
        
        if(extra.equals("GetServices") ){
	       	 for (int i = 0; i < soap.getPropertyCount(); i++) {
	             SoapObject obj1 = (SoapObject)soap.getProperty(i);
	             Accounts account = new Accounts();
	             currentHeader = obj1.getProperty(11).toString();

	             if(!previousHeader.equals(currentHeader)){ 	             	
	             		Accounts accountx = new Accounts();
	                 	accountx.setId(0);
	                 	accountx.setName(currentHeader);
	                    accounts.add(accountx);
	                    previousHeader = currentHeader;
	                 	accountx = null;
	             }
	             
	             String text;
	 			 if (obj1.getProperty(1).toString().indexOf("andamp;")>0)
	 			 {
	 				text = obj1.getProperty(1).toString().replace("andamp;", "&");
	 			 }
	 			 else
	 			 {
	 				text = obj1.getProperty(1).toString();
	 			 }
	             
	             
	             account.setId(Integer.parseInt(obj1.getProperty(0).toString()));
	             account.setName(text);
	             account.setContact(obj1.getProperty(2).toString());
	             account.setComments(obj1.getProperty(3).toString());
	             account.setAddress1(obj1.getProperty(4).toString());
	             account.setCity(obj1.getProperty(5).toString());
	             account.setState(obj1.getProperty(6).toString());
	             account.setUrl(obj1.getProperty(7).toString());
	             account.setZip(obj1.getProperty(8).toString());
	             account.setMap(Integer.parseInt(obj1.getProperty(9).toString()));
	             account.setServiceId(Integer.parseInt(obj1.getProperty(10).toString()));
	             account.setServiceName(obj1.getProperty(11).toString());            
	             accounts.add(account);
	         }        	
       }
        
        if(extra.equals("GetAccounts") || extra.equals("GetCaterers")   ){
        	 for (int i = 0; i < soap.getPropertyCount(); i++) {
                 SoapObject obj1 = (SoapObject)soap.getProperty(i);
                 Accounts account = new Accounts();
                 currentLetter = obj1.getProperty(1).toString().substring(0, 1);
     			
                 
                 if(!previousLetter.equals(currentLetter)){ 
                 	try{
                 		@SuppressWarnings("unused")
     					int currentNumber = Integer.parseInt(currentLetter);
                 		if(!isNumberHeaderDone){
                 			Accounts accountx = new Accounts();
                         	accountx.setId(0);
                         	accountx.setName("#");
                         	accountx.setMap(pos);
                            accounts.add(accountx);
                         	previousLetter = currentLetter;
                         	accountx = null;
                 			isNumberHeaderDone = true;
                 		}
                 	}catch(Exception ex){
                 		Accounts accountx = new Accounts();
                     	accountx.setId(0);
                     	accountx.setName(currentLetter);
                     	accountx.setMap(pos);
                         accounts.add(accountx);
                     	previousLetter = currentLetter;
                     	accountx = null;
                 	}
                 }
                 account.setId(Integer.parseInt(obj1.getProperty(0).toString()));
                 account.setMap(pos);
                 account.setName(obj1.getProperty(1).toString());
                 account.setContact(obj1.getProperty(2).toString());
                 account.setComments(obj1.getProperty(3).toString());
                 account.setAddress1(obj1.getProperty(4).toString());
                 account.setCity(obj1.getProperty(5).toString());
                 account.setState(obj1.getProperty(6).toString());
                 account.setUrl(obj1.getProperty(7).toString());
                 account.setZip(obj1.getProperty(8).toString());
                 //account.setMap(Integer.parseInt(obj1.getProperty(9).toString()));
                 account.setServiceId(Integer.parseInt(obj1.getProperty(10).toString()));
                 account.setServiceName(obj1.getProperty(11).toString());            
                 accounts.add(account);
             }        	
        }
        
        
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	//imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
    	
   	 
    	
        return accounts;
    }
    
    private static void simulateKeyStroke(int keyCode, Activity parent) {
        injectKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode), parent);
        injectKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyCode), parent);
    }

    private static void injectKeyEvent(KeyEvent keyEvent, Activity parent) {
        parent.dispatchKeyEvent(keyEvent);
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
