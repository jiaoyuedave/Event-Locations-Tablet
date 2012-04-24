package us.eventlocations.androidtab.fragments;

import java.io.File;
import java.util.ArrayList;

import us.eventlocations.androidtab.EmailFormActivity;
import us.eventlocations.androidtab.R;
import us.eventlocations.androidtab.WebViewBilder2;
import us.eventlocations.androidtab.layouts.AccountView;
import us.eventlocations.androidtab.models.Accounts;
import us.eventlocations.androidtab.pager.HorizontalPager;
import us.eventlocations.androidtab.utilityfunctions.ImageCache;
import us.eventlocations.androidtab.utilityfunctions.UtilityFunctions;
import us.eventlocations.androidtab.utilityfunctions.WebServiceHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class DetailFragmentHoneymoon extends DetailFragmentBase implements OnGestureListener{
	public TextView accountAddress; 
	public RelativeLayout layout_main;
	private AccountView viewer;
	static ArrayList<Accounts> accounts;
	static ArrayList<Accounts> accounts2;
	static Accounts account;
	static int ind;
	private String name,address,contact;
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
	final private Handler handler = new Handler();
	String imageUrl = "";
	String accReception = "";
	String accDinner = "";
	String accComments = "";
	String accPhone = "";
	ImageCache imagecache;
    private GestureDetector gestureScanner;
	Bitmap bitmapAccountImage = null;
	AccountView accountView = null;
	protected SharedPreferences preferences;

	/**
     * Create a new instance of DetailFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailFragmentHoneymoon newInstance(int index) {
        DetailFragmentHoneymoon f = new DetailFragmentHoneymoon();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
    
   /* public static void setData(ArrayList<Accounts> accounts, int index) {
    	
    	DetailFragmentHoneymoon.account = accounts.get(index);
    	DetailFragmentHoneymoon.ind = index;
    	DetailFragmentHoneymoon.accounts = accounts;
    }*/
    
    public static void setData(ArrayList<Accounts> accounts, int index) {
    	
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
    			{
    				header  ++;
    			}
    		}
    	}
    	index = index -header  ;
    	
    	DetailFragmentHoneymoon.account = accounts2.get(index);
    	DetailFragmentHoneymoon.ind = index;
    	DetailFragmentHoneymoon.accounts2 = accounts2;
    	
        
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
        	//System.out.println(container.getId());
        	//System.out.println(container.getId());
            return null;
        }
        

        
        if(accounts2 != null){
        	final HorizontalPager pager  = new HorizontalPager(getActivity(), null);
        	//final HorizontalPager2 pager  = new HorizontalPager2(getActivity(), null);
        	

			// using traditional for loop
        	
			for(int i=0; i<accounts2.size(); i++)
			{
				viewer = (AccountView) inflater.inflate(R.layout.account_view_honey, container, false);
				if(accounts2!= null){
	               	viewer.setAccount(accounts2.get(i));
				}
				pager.addView(viewer); 
			}
			//Set current page to index
			if (ind ==0)
				{
				shouldLoadNewAccountData = true;
				}
				
			pager.setCurrentPage(ind);
		    
	        pager.addOnScrollListener(new HorizontalPager.OnScrollListener() {
	        	

	        	

	        	public void onScroll(int scrollX) {
	            	scrollValue = scrollX;
	            }
	           
	            public void onViewScrollFinished(int currentPage) {
	            	
	            	MasterFragmentHoneymoon viewer = (MasterFragmentHoneymoon) getFragmentManager().findFragmentById(R.id.master);
	            	viewer.getListView().setItemChecked(getArguments().getInt("index", 0), true);
	            	
	            	if(		scrollValue == previousScrollValue+925 
	            			|| scrollValue == previousScrollValue-925 
	            			|| scrollValue == previousScrollValue+800
	            			|| scrollValue == previousScrollValue-800
	            			|| previousScrollValue == 0
	            			&& currentPage>0
	            	)
	            	{
	            		//pager.setCurrentPage(ind);
	            		shouldLoadNewAccountData = true;
	            		previousScrollValue = scrollValue;



	                  	ImageView accountImage = null;
	                  	
	                  	if (currentPage>0)
	                  	{
	                  		AccountView accountView1 = (AccountView)pager.getChildAt(currentPage-1);
	                  		accountImage= (ImageView)accountView1.findViewById(R.id.accountImageView);
	                  		accountImage.setImageDrawable(null);
	                  		
			 

	                  	}
	                  	AccountView accountView2 = (AccountView)pager.getChildAt(currentPage+1);	                  	
	                  	

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
	            		accid= accounts2.get(currentPage).getId();
	            		contact= accounts2.get(currentPage).getContact();
	         		   preferences = getActivity().getSharedPreferences("initialdata",Activity.MODE_PRIVATE);
	         		  //   save_Preference(preferences,"default_row",currentPage+conHeader); here

	         		   save_Preference(preferences,"default_row",accid);
	          
	                	if (UI_UPDATE_LISTENER!=null)
	                	{
	                		UI_UPDATE_LISTENER.onTutSelected(accounts2.get(currentPage).getName());
	                	}
	            		
	                		accountView = (AccountView)pager.getChildAt(currentPage);
		            		setAccountImageUrl(accounts2.get(currentPage).getId());
		            		setAccountImageUrl2(accounts2.get(currentPage).getId());
	                      	layout_main = (RelativeLayout)accountView.findViewById(R.id.layout_main);
	                      	layout_main.setVisibility(View.VISIBLE);

	                      	accountAddress = (TextView)accountView.findViewById(R.id.accountAddress);
	                      	accountAddress .setVisibility(View.GONE);
	                      	
	                        if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_LANDSCAPE)
	                        {
		                      	TextView textViewTitle4= (TextView)accountView.findViewById(R.id.textViewTitle4);
		                      	textViewTitle4.setText(name);

	                        }
                     	
	            		
	            	}
	            }
	        });			
		
	        return pager;
        }else{
        	viewer = (AccountView) inflater.inflate(R.layout.account_view_honey, container, false);
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
    
    private Runnable finishSetAccountImageUrl2 = new Runnable() {
		 public void run() {
			 fillImages();
		 }
	};
	
	   private void fillImages()
	    {
	     	try{
	     		if (imageUrl.equals("http://www.locationsmagazine.com/bins/LaSamanna-640x400.jpg1047333218.bin"))
	     		{
	     			ImageView accountImage = (ImageView)accountView.findViewById(R.id.accountImageView);
	        		accountImage.setImageResource(R.drawable.lasamanna);
	     		}
	     		else
	     		{
	     			//imageUrl =imageUrl.replace("http://www.locationsmagazine.com", "http://eventlocations.us");
	     			
		      		String imageLocalPath = imagecache.getImagePathLocal(imageUrl).toString();
		      		Drawable d = Drawable.createFromPath(imageLocalPath);
		      		ImageView accountImage = (ImageView)accountView.findViewById(R.id.accountImageView);
		      		accountImage.setImageDrawable(d);
		      		d = null;
		      		System.gc();
	     		}
	     		

	      		
	      	}catch(Exception ex){
	      		Log.e("XOXOXOXO"," on 154" + ex.toString());
	      		
	      	}
	    }
    
   /* private void setAccountImageUrl(final int accid){    	
    	Thread splashThread = new Thread() {
            @Override
            public void run() {            	
            	
            	WebServiceHelper webservice = new WebServiceHelper();
            	imageUrl = webservice.getStringUrlForAccountImage("GetAccountImage", accid);
            	
            	accReception = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=reception&accid=" + accid);
            	accDinner = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=dinner&accid=" + accid);
            	accComments = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=comments&accid=" + accid);
            	accPhone = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/acc_phone_number.php?accid=" + accid);

                imagecache = new ImageCache(getActivity().getCacheDir());
            	if(!imagecache.isImageinCache(imageUrl)){
            		imagecache.getImageDataFromUrl(imageUrl);
            	}
            	
            	handler.post(finishSetAccountImageUrl);
            }
         };
         splashThread.start();
    }*/
    
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
	        	ComingSoon();
	        	//UI_UPDATE_LISTENER.onDialogComingSoon();
	        }
	    };
	    
	    private OnClickListener mOnclick_email = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	 call_email() ;
	        }
	    };
	    
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

	    public void call_phone(String phoneNumber) 
	    { 
	    	
	    	try{
	    		/*Intent intent = new Intent(Intent.ACTION_CALL);
	    	    intent.setData(Uri.parse("tel:"+phoneNumber));
	    	    startActivity(intent);*/
	    	    //addContact(phoneNumber);
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
	    
	 /*   private int findContact(String name) {
		   	 
	    	String where = ContactsContract.Data.DISPLAY_NAME + " = ?";
	    	String[] params = new String[] {name};
	        Cursor phoneCur = getActivity().managedQuery(ContactsContract.Data.CONTENT_URI, null, where, params, null);
	        int size =phoneCur.getCount();
	        //phoneCur.close();
	        return size;
	    }*/
	    
	    
	    
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

	    private OnClickListener mOnclick_shared = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	share_email(accid);
	        }
	    };

		/* public void call_map(View view)
		 {
					 //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:41.656313,-0.877351"));
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+accountAddress.getText().toString()));
					 //		 //
					startActivity(intent);
		  }*/

		 public void call_map(View view)
		 {
					 //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:41.656313,-0.877351"));
					//Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+accountAddress.getText().toString()));
					 //		 //
					//startActivity(intent);
	            Bundle bundle = new Bundle();
	        	bundle.putString("url", "http://maps.google.com/maps?q="+accountAddress.getText().toString()+"&ui=maps");
	        	executeWebBrowser2(bundle);
		  }

		    private void executeWebBrowser2(Bundle bundle )
		    {
		    	//"http://maps.google.com/maps?q=

	        	//bundle.putString("url", "http://maps.google.com/maps?q=");
		    	
		    	 Intent myIntent = new Intent(getActivity(), WebViewBilder2.class);
		    	 myIntent.putExtras(bundle);
		    	 startActivityForResult(myIntent,0);
		    }		 
		    
		   /* private void share_email(int accountNumber)
		    {
		    	String subject="Shared by Event Locations App";
		    	String email="";
		    	String body ="";
				String[] mailto = { email };
			    Intent sendIntent = new Intent(Intent.ACTION_SEND);
			    // Add attributes to the intent
			    sendIntent.putExtra(Intent.EXTRA_EMAIL, "");
			    sendIntent.putExtra(Intent.EXTRA_SUBJECT,subject);

			  //  String sbody=TextUtils.htmlEncode(body); 
			    StringBuffer text01 = new StringBuffer ();
	    		text01.append("Hi, \n");
	    		text01.append("    I found this great app that  \n");
	    		text01.append("allows me to  share this link \n");
	    		text01.append("http://www.eventlocations.us/display/"+accountNumber);
	    		text01.append(" try it.. Have a great day. \n");
	    		body = text01.toString();
			    
			    
			    sendIntent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml (body)); 
			    sendIntent.setType("text/message");


			    startActivity(Intent.createChooser(sendIntent, "Complete action using"));
		    }*/
		  

	
    public void finishedSetAccountImageUrl(){    	
      	//ImageView accountImage = (ImageView)accountView.findViewById(R.id.accountImageView);
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
      		/*String imageLocalPath = imagecache.getImagePathLocal(imageUrl).toString();
      		Drawable d = Drawable.createFromPath(imageLocalPath);
      		accountImage.setImageDrawable(d);
      		d = null;*/
          	accountReception = null;
          	accountDinner = null;
          	accountComments = null;

      		//System.gc();
      		
      	}catch(Exception ex){
      		Log.e("XOXOXOXO"," on 154" + ex.toString());
      		
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
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
     	    int x1 = (int) e1.getX();
	        int y1 = (int) e1.getY();
	        int x2 = (int) e2.getX();
	        int y2 = (int) e2.getY();
	     
	     
	        if(x2>x1){
	            //East or Left
	        	//back();
	        	System.out.println("back");
	        }
	        else if(x2<x1){
	            //west or right
	        	//next();
	        	System.out.println("next");
	        }
	        else if(y2>y1){
	            //South or bottom
	        	
	        }
	        else if(y2<y1){
	        	//North or top
	        	
	        }

	        return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	
}
