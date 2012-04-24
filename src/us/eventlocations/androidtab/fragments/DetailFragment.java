package us.eventlocations.androidtab.fragments;

import java.io.File;
import java.util.ArrayList;

import us.eventlocations.androidtab.CategoryMap;
import us.eventlocations.androidtab.Common;
import us.eventlocations.androidtab.EmailFormActivity;
import us.eventlocations.androidtab.R;
import us.eventlocations.androidtab.layouts.AccountView;
import us.eventlocations.androidtab.models.Accounts;
import us.eventlocations.androidtab.pager.HorizontalPager;
import us.eventlocations.androidtab.utilityfunctions.ImageCache;
import us.eventlocations.androidtab.utilityfunctions.UtilityFunctions;
import us.eventlocations.androidtab.utilityfunctions.WebServiceHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.bus.SimpleBus;
import com.commonsware.cwac.cache.AsyncCache;
import com.commonsware.cwac.cache.WebImageCache;
import com.insideperu.map.MyParcelable;

public class DetailFragment extends DetailFragmentBase implements OnTutSelectedListener{
	public TextView accountAddress; 
	public RelativeLayout layout_main;
	private AccountView viewer;
	static ArrayList<Accounts> accounts;
	static ArrayList<Accounts> accounts2;
	static Accounts account;
	static int ind;
	private String name,address,address1,contact;
	private TextView textViewTitle3;
	private WebImageCache cache=null;
	private SimpleBus bus=new SimpleBus();
	private Drawable placeholder=null;
	private int accid;
	ProgressDialog dialog;
	Boolean loadingAccountData = false;
	Boolean shouldLoadNewAccountData = false;
	int previousScrollValue = 0;
	int scrollValue = 0;
	public  OnTutSelectedListener UI_UPDATE_LISTENER;
	protected SharedPreferences preferences;
	final private Handler handler = new Handler();
	String imageUrl = "";
	String accReception = "";
	String accDinner = "";
	String accComments = "";
	String accPhone = "";
	ImageCache imagecache;
	private static int conHeader=0;
	Bitmap bitmapAccountImage = null;
	AccountView accountView = null;
    
	/**
     * Create a new instance of DetailFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailFragment newInstance(int index) {
        DetailFragment f = new DetailFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
    
    public static void setData(ArrayList<Accounts> accounts, int index,String text) {
    	
    	ArrayList<Accounts> accounts2 = new ArrayList<Accounts>(); 
    	int header = 0;
    	index++;
    	accounts2.add(accounts.get(0));
    	
    	for (int i = 0; i<accounts.size();i++)
    	{
    	    		String address = accounts.get(i).getUrl();
    	    		if (address!=null && address.length()>0)
    	    		{
    	    			accounts2.add(accounts.get(i));
    	    		}
    	    		else
    	    		{
    	    			if (i<index)
    	    			header  ++;
    	    		}

    	}
    	index = index -header  ;
    	//conHeader =header  ;

    	DetailFragment.account = accounts2.get(index);
    	DetailFragment.ind = index;
    	DetailFragment.accounts2 = accounts2;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            // Currently in a layout without a container, so no
            // reason to create our view.
            return null;
        }
        
       
        if(accounts2 != null){
        	final HorizontalPager pager  = new HorizontalPager(getActivity(), null);
			// using traditional for loop
			for(int i=0; i<accounts2.size(); i++)
			{
				viewer = (AccountView) inflater.inflate(R.layout.account_view, container, false);
				
				if(accounts2 != null){
	               		viewer.setAccount(accounts2.get(i));
				}
				pager.addView(viewer); 
			}
			//Set current page to index
			if (ind ==0)shouldLoadNewAccountData = true;
			//else shouldLoadNewAccountData = true;
			pager.setCurrentPage(ind);
		    
	        pager.addOnScrollListener(new HorizontalPager.OnScrollListener() {
	            public void onScroll(int scrollX) {
	            	scrollValue = scrollX;
	            }
	            public void onViewScrollFinished(int currentPage) {

	            	MasterFragment viewer = (MasterFragment) getFragmentManager().findFragmentById(R.id.master);
	            	viewer.getListView().setItemChecked(getArguments().getInt("index", 0), true);
	            	
	            	
	            	if(		scrollValue == previousScrollValue+925 
	            			|| scrollValue == previousScrollValue-925 
	            			|| scrollValue == previousScrollValue+800
	            			|| scrollValue == previousScrollValue-800
	            			|| previousScrollValue == 0
	            			&& currentPage>0
	            	)
	            	{
	            		shouldLoadNewAccountData = true;
	            		previousScrollValue = scrollValue;

	            		AccountView accountView1 = (AccountView)pager.getChildAt(currentPage-1);
	            		AccountView accountView2 = (AccountView)pager.getChildAt(currentPage+1);
	                  	ImageView accountImage = null;
	                  	accountImage= (ImageView)accountView1.findViewById(R.id.accountImageView);
	                  	accountImage.setImageDrawable(null);
	                  	
	                  	if (currentPage+1>=accounts2.size() )
	                  	{
	                  		
	                  	}
	                  	else
	                  	{
		                  	accountImage = (ImageView)accountView2.findViewById(R.id.accountImageView);
		                  	accountImage.setImageDrawable(null);
		                  	accountImage = null;
	                  	}
	                  	System.gc();	                  	
	            	}
	            	
	            	if(!loadingAccountData && shouldLoadNewAccountData){
	            		loadingAccountData = true;
	            		shouldLoadNewAccountData = false;

	            		dialog = ProgressDialog.show(getActivity(), accounts2.get(currentPage).getName(), "Loading Account Information. Please wait...", true);
	            		name = accounts2.get(currentPage).getName();
	            		contact= accounts2.get(currentPage).getContact();
	            		accid= accounts2.get(currentPage).getId();
	            		address1= accounts2.get(currentPage).getAddress1()+","+accounts2.get(currentPage).getCity()+","+accounts2.get(currentPage).getState()+","+accounts2.get(currentPage).getZip();
	                	
		         		   preferences = getActivity().getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
		        		   //save_Preference(preferences,"default_row",currentPage+conHeader);
		         		   save_Preference(preferences,"default_row",accid);
		                
		        		   
	                	if (UI_UPDATE_LISTENER!=null)
	                	{
	                		UI_UPDATE_LISTENER.onTutSelected(accounts2.get(currentPage).getName());
	                	}
	            		
	            		
	                	String state = accounts2.get(currentPage).getState();
	                	accountView = (AccountView)pager.getChildAt(currentPage);
	                	
	                	   if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_LANDSCAPE)
	                        {
		                      	TextView textViewTitle4= (TextView)accountView.findViewById(R.id.textViewTitle4);
		                      	textViewTitle4.setText(name);

	                        }
	                	
	                	if (state!=null)
	                	{
		            	
		            		setAccountImageUrl(accounts2.get(currentPage).getId());
		            		setAccountImageUrl2(accounts2.get(currentPage).getId());
	                      	layout_main = (RelativeLayout)accountView.findViewById(R.id.layout_main);
	                      	layout_main.setVisibility(View.VISIBLE);

	                      	accountAddress = (TextView)accountView.findViewById(R.id.accountAddress);
	                      	accountAddress .setVisibility(View.VISIBLE);
	                      	//dialog.dismiss();
	                      	

	                      	
	                	}

	            		
	            	}
	            }
	        });			
		
	        return pager;
        }else{
        	viewer = (AccountView) inflater.inflate(R.layout.account_view, container, false);
        	if(accounts2 != null){
				viewer.setAccount(accounts2.get(ind));
			}
        	return viewer;
        } 
    }
    
    
    private  void save_Preference(SharedPreferences preferences,String label,int value) {
        preferences.edit().putInt(label,value).commit();
    }	
    
    
    private void setAccountImageUrl(final int accid){    	
    	Thread splashThread = new Thread() {
            @Override
            public void run() {            	
            	
            	WebServiceHelper webservice = new WebServiceHelper();
            	imageUrl = webservice.getStringUrlForAccountImage("GetAccountImage", accid);
                imagecache = new ImageCache(getActivity().getCacheDir());
            	
                if(!imagecache.isImageinCache(imageUrl)){
            		imagecache.getImageDataFromUrl(imageUrl);
            	}
            	
            	handler.post(finishSetAccountImageUrl2);
            }
         };
         splashThread.start();
    }
    
    
    private void setAccountImageUrl2(final int accid){    	
    	Thread splashThread = new Thread() {
            @Override
            public void run() {            	
            	
                	accReception = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=reception&accid=" + accid);
                	accDinner = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=dinner&accid=" + accid);
                	accComments = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=comments&accid=" + accid);
                	accPhone = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/acc_phone_number.php?accid=" + accid);
            	    handler.post(finishSetAccountImageUrl);
            }
         };
         splashThread.start();
    }
    
    private void fillImages()
    {
     	try{
    		//getCache().handleImageView(accountImage,imageUrl.toString(),"demo");
      		String imageLocalPath = imagecache.getImagePathLocal(imageUrl).toString();
      		//Log.e("OMONONNIUBIU",imageLocalPath);
      		//Bitmap myBitmap = BitmapFactory.decodeFile(imageLocalPath);
      	    //accountImage.setImageBitmap(myBitmap);
      		//myBitmap = null;

      		Drawable d = Drawable.createFromPath(imageLocalPath);
      		ImageView accountImage = (ImageView)accountView.findViewById(R.id.accountImageView);
      		accountImage.setImageDrawable(d);
      		d = null;


      		System.gc();
      		
      	}catch(Exception ex){
      		Log.e("XOXOXOXO"," on 154" + ex.toString());
      		
      	}
    }

    
    private Runnable finishSetAccountImageUrl2 = new Runnable() {
		 public void run() {
			 fillImages();
		 }
	};
	
    
    private Runnable finishSetAccountImageUrl = new Runnable() {
		 public void run() {
			 finishedSetAccountImageUrl();
		 }
	};
	
	
	    private OnClickListener mOnclick_call = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	call_phone(accPhone) ;
	        	//ComingSoon();
	        	//UI_UPDATE_LISTENER.onDialogComingSoon();
	        }
	    };
	    
	    
	    private OnClickListener mOnclick_call2 = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	//call_phone(accPhone) ;
	        	//ComingSoon();
	        	//UI_UPDATE_LISTENER.onDialogComingSoon();
	        	//onCreateDialog().show();
	        	ComingSoon();
	        }
	    };
	    
	    public Dialog onCreateDialog() {
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("");
	        builder.setItems(
	                new String[]{
	                        getString(R.string.button_call),
	                        getString(R.string.btn_cancel)
	                },
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int item) {
	                        switch (item) {
	                            case 0:
	                                break;
	                            case 1:
	                                FragmentManager manager = getFragmentManager();
	                                FragmentTransaction transaction = manager.beginTransaction();

	                                Fragment fragment = new DetailFragment();

	                                transaction.replace(R.id.account_name, fragment);
	                                transaction.addToBackStack(null);
	                                transaction.commit();
	                                
	                                break;
	                            case 2:
	                                break;
	                        }
	                    }
	                });


	        AlertDialog dialog = builder.create();

	        return dialog;
	    }
	    
	    private void ComingSoon()
	    {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    		builder.setMessage(
    				"Coming Soon")
    				.setCancelable(false)
    				.setPositiveButton(getString(R.string.lb_ok),
    						new DialogInterface.OnClickListener() {
    							public void onClick(DialogInterface dialog, int id) {
    								dialog.cancel();
    							}
    						})
    						;
    		AlertDialog alert = builder.create();
    		alert.show();
	    	 //Toast.makeText(getActivity(),  R.string.label_coming_soon ,Toast.LENGTH_SHORT).show();
	    	// Toast.makeText(getActivity(),  " Coming Soon " ,Toast.LENGTH_SHORT).show();
	    	 
	    }
	    

	    
  
	    
	    private OnClickListener mOnclick_email = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	 call_email() ;
	        }
	    };
	    
	    private OnClickListener mOnclick_shared = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	share_email(accid);
	        }
	    };

	    private OnClickListener mOnclick_directions = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	if (UI_UPDATE_LISTENER!=null)
	        	{
	        		UI_UPDATE_LISTENER.onCallGPS(address1);
	        	}
	        	
	        	
	        }
	    };


	    

	  

	    public void call_phone(String phoneNumber) 
	    { 
	    	
	    	try{
	    		/*Intent intent = new Intent(Intent.ACTION_CALL);
	    	    intent.setData(Uri.parse("tel:"+phoneNumber));
	    	    startActivity(intent);*/
	    		int size = findContact(name);
	    		
	    		if (size==0)
	    		{
	    			addContact(phoneNumber);	
	    		}
	    		else
	    		{
	    			 Toast.makeText(getActivity(),  name+" Contact, Already Exist " ,Toast.LENGTH_SHORT).show();
	    		}
	    	    
	    		
	    	}catch(Exception e)
	    	{
	    		e.getMessage();
	    	}
	    } 
	    
	    public void call_email() 
	    { 
	    	
	    	try{
	    		Intent intent = new Intent();
	    	    intent.setClass(getActivity(), EmailFormActivity.class);
	      		intent.putExtra("titleemail", name);
	    	    intent.putExtra("accid", accid);
				startActivity(intent);
				
	    	}catch(Exception e)
	    	{
	    		e.getMessage();
	    	}
	    } 
	    
	    
	    
	    

	    
	    private void addContact(String accPhone)
 	    {
 	    	Intent intent = new Intent(Intent.ACTION_INSERT);
 	    	intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

 	    	// Just two examples of information you can send to pre-fill out data for the
 	    	// user.  See android.provider.ContactsContract.Intents.Insert for the complete
 	    	// list.
 	    	if (name!=null && name.equalsIgnoreCase("NA"))
 	    		name ="";
 	    	else if (contact!=null &&  contact.equalsIgnoreCase("NA"))
 	    		contact=""; 	
 	    		
 	    	
 	    	intent.putExtra(ContactsContract.Intents.Insert.COMPANY, name);
 	    	intent.putExtra(ContactsContract.Intents.Insert.NAME, contact);
 	    	intent.putExtra(ContactsContract.Intents.Insert.PHONE, accPhone);
 	    	intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,  ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
 	    	intent.putExtra(ContactsContract.Intents.Insert.POSTAL_TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK);
 	    	address=accountAddress.getText().toString();
 	    	intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
 	    	
 	    	// Send with it a unique request code, so when you get called back, you can
 	    	// check to make sure it is from the intent you launched (ideally should be
 	    	// some public static final so receiver can check against it)
 	    	int PICK_CONTACT = 100;
 	    	startActivityForResult(intent, PICK_CONTACT);
 	    }
	    
	    private OnClickListener mOnclick_map = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	call_map(accountAddress) ;
	        }
	    };


		 public void call_map(View view)
		 {
					 //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:41.656313,-0.877351"));
			
			 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+accountAddress.getText().toString()));
			 startActivity(intent);

			 
			 
	         //   executeWebBrowser2(accountAddress.getText().toString());
		  }

		    //private void executeWebBrowser2(Bundle bundle )
		 	private void executeWebBrowser2(String bundle )
		    {
		    	//"http://maps.google.com/maps?q=

	        	//bundle.putString("url", "http://maps.google.com/maps?q=");
		    	
		    	// Intent myIntent = new Intent(getActivity(), WebViewBilder.class);
		    	// myIntent.putExtras(bundle);
		    	// startActivityForResult(myIntent,0);
		    	  MyParcelable[] myParcelableArray = null;
		 	  	  myParcelableArray = new MyParcelable[1];
		 	  	  MyParcelable myParcelable;
		 	  	  
		 	     myParcelable = new MyParcelable(0,0,bundle ,0,name);
		 	     myParcelableArray[0] = myParcelable;


		 	     
		 	    callMap(myParcelableArray);
		    	
		    }
		    
		    private void callMap( MyParcelable[] myParcelableArray)
		    {
		    	try{
			    	Intent myIntent = new Intent(getActivity(), CategoryMap.class);
		  	   	  	myIntent.putExtra(Common.KEY_ROW_STATIONLIST, myParcelableArray);
		  	   	  	startActivity(myIntent);
		    	}catch(Exception e)
		    	{
		    		System.out.println(e.getMessage());
		    	}
		  	   	  	

		    }
		 
	
    public void finishedSetAccountImageUrl(){    	
    //  	ImageView accountImage = (ImageView)accountView.findViewById(R.id.accountImageView);
      	TextView accountReception = (TextView)accountView.findViewById(R.id.accountReception);
      	TextView accountDinner = (TextView)accountView.findViewById(R.id.accountDinner);
      	TextView accountComments = (TextView)accountView.findViewById(R.id.accountComments);
      	accountAddress = (TextView)accountView.findViewById(R.id.accountAddress);
      	
      	textViewTitle3= (TextView)accountView.findViewById(R.id.textViewTitle3);
      	textViewTitle3.setText(name);
      	
        if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_LANDSCAPE)
        {
        	textViewTitle3.setVisibility(View.GONE);        
        }
        else
        {
        	textViewTitle3.setVisibility(View.VISIBLE);        
        }
      	
      	Button b_email = (Button)accountView.findViewById(R.id.b_email);
      	b_email.setOnClickListener(mOnclick_email);
      	
      	Button bcall = (Button)accountView.findViewById(R.id.b_call);
      	bcall.setOnClickListener(mOnclick_call);

      	Button bcall2 = (Button)accountView.findViewById(R.id.b_call2);
      	bcall2.setOnClickListener(mOnclick_call2);

      	
		Button bmap = (Button)accountView.findViewById(R.id.b_map);
      	bmap.setOnClickListener(mOnclick_map);

      	Button bshared = (Button)accountView.findViewById(R.id.b_shared);
      	bshared .setOnClickListener(mOnclick_shared);
      	
      	Button b_directions= (Button)accountView.findViewById(R.id.b_directions);
      	b_directions.setOnClickListener(mOnclick_directions);
      	
      	
      	if (!accReception.equals(""))
      	{
      		accountReception.setText("Room Capacity Reception: "+accReception);
      	}
      	if (!accDinner.equals(""))
      	{
      		accountDinner.setText("Room Capacity Dinner w/Dancing: " + accDinner);
      	}
      		
      		accountComments.setText(accComments);
       
      	
      	try{
      		//fillImages();
          	accountReception = null;
          	accountDinner = null;
          	accountComments = null;
      		//System.gc();
      		
      	}catch(Exception ex){
      		//Log.e("XOXOXOXO"," on 154" + ex.toString());
      		
      	}
      	
      	layout_main = (RelativeLayout)accountView.findViewById(R.id.layout_main);
      	layout_main.setVisibility(View.VISIBLE);
      	
    	//TextView numberPhone = (TextView)accountView.findViewById(R.id.numberPhone);
    	//numberPhone.setText(accPhone);
    	
    	 
		loadingAccountData = false;
		dialog.dismiss();
    } 
    
	synchronized WebImageCache getCache() {
		if (cache==null) {
			placeholder=getResources().getDrawable(R.drawable.placeholder);
			cache=new WebImageCache(getActivity().getCacheDir(), bus, policy, 101,placeholder);
		}
		return(cache);
	}
	
	boolean isPlaceholder(Drawable d) {
		return(d==placeholder);
	}
	
	SimpleBus getBus() {
		return(bus);
	}
	
	private AsyncCache.DiskCachePolicy policy=new AsyncCache.DiskCachePolicy() {
		public boolean eject(File file) {
			return(System.currentTimeMillis()-file.lastModified()>1000*60*60*24*7);
		}
	};

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
	
	/* public static class MyDialogFragment extends DialogFragment {
	        int mNum;


	        static MyDialogFragment newInstance(int num) {
	            MyDialogFragment f = new MyDialogFragment();

	            // Supply num input as an argument.
	            Bundle args = new Bundle();
	            args.putInt("num", num);
	            f.setArguments(args);

	            return f;
	        }

	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);
	            mNum = getArguments().getInt("num");

	            // Pick a style based on the num.
	            int style = DialogFragment.STYLE_NORMAL, theme = 0;
	            switch ((mNum-1)%6) {
	                case 1: style = DialogFragment.STYLE_NO_TITLE; break;
	                case 2: style = DialogFragment.STYLE_NO_FRAME; break;
	                case 3: style = DialogFragment.STYLE_NO_INPUT; break;
	                case 4: style = DialogFragment.STYLE_NORMAL; break;
	                case 5: style = DialogFragment.STYLE_NORMAL; break;
	                case 6: style = DialogFragment.STYLE_NO_TITLE; break;
	                case 7: style = DialogFragment.STYLE_NO_FRAME; break;
	                case 8: style = DialogFragment.STYLE_NORMAL; break;
	            }
	            switch ((mNum-1)%6) {
	                case 4: theme = android.R.style.Theme_Holo; break;
	                case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
	                case 6: theme = android.R.style.Theme_Holo_Light; break;
	                case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
	                case 8: theme = android.R.style.Theme_Holo_Light; break;
	            }
	            setStyle(style, theme);
	        }

	        @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
	        	View v = inflater.inflate(R.layout.fragment_dialog, container, false);
	            View tv = v.findViewById(R.id.text);
	            //((TextView)tv).setText("Dialog #" + mNum + ": using style "+ getNameForNum(mNum));

	            // Watch for button clicks.
	            Button button = (Button)v.findViewById(R.id.show);
	            button.setOnClickListener(new OnClickListener() {
	                public void onClick(View v) {
	                    // When button is clicked, call up to owning activity.
	                   // ((DetailFragment)getActivity()).showDialog();
	                }
	            });

	            return v;
	        }
	    }
*/

	
	
}
