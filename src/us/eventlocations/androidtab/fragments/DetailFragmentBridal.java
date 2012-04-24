package us.eventlocations.androidtab.fragments;

import java.io.File;
import java.util.ArrayList;

import org.ksoap2.serialization.SoapObject;

import us.eventlocations.androidtab.Common;
import us.eventlocations.androidtab.EmailFormActivity;
import us.eventlocations.androidtab.R;
import us.eventlocations.androidtab.layouts.AccountViewBridal;
import us.eventlocations.androidtab.models.Accounts;
import us.eventlocations.androidtab.utilityfunctions.ImageCache;
import us.eventlocations.androidtab.utilityfunctions.WebServiceHelper;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonsware.cwac.bus.SimpleBus;
import com.commonsware.cwac.cache.AsyncCache;
import com.commonsware.cwac.cache.WebImageCache;

public class DetailFragmentBridal extends DetailFragmentBase {
	public TextView accountAddress; 
	public RelativeLayout layout_main;
	private AccountViewBridal viewer;
	private static String addressMap;
	private static String bridalTitle;
	static ArrayList<Accounts> accounts;
	static ArrayList<Accounts> accounts2;
	static Accounts account;
	static int ind,accid;

   	private SoapObject returnDetailObject;
	private WebImageCache cache=null;
	private SimpleBus bus=new SimpleBus();
	private Drawable placeholder=null;
	private String name;
	ProgressDialog dialog;
	Boolean loadingAccountData = false;
	Boolean shouldLoadNewAccountData = false;
	int previousScrollValue = 0;
	int scrollValue = 0;
	
	final private Handler handler = new Handler();
	String imageUrl = "";
	String accReception = "";
	String accDinner = "";
	String accComments = "";
	String accPhone = "";
	ImageCache imagecache;

	Bitmap bitmapAccountImage = null;
	AccountViewBridal accountView = null;
    
	/**
     * Create a new instance of DetailFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailFragmentBridal newInstance(int index) {
        DetailFragmentBridal f = new DetailFragmentBridal();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
    
    public static void setData(ArrayList<Accounts> accounts, int index) {
    	DetailFragmentBridal.account = accounts.get(index);
    	DetailFragmentBridal.ind = index;
    	DetailFragmentBridal.accounts2 = accounts;
    	//bridalTitle=DetailFragmentBridal.account2.getName();
    }
    
    public static void setData1(ArrayList<Accounts> accounts, int index) {
    	
    	ArrayList<Accounts> accounts2 = new ArrayList<Accounts>(); 
    	int header = 0;
    	index++;
    	accounts2.add(accounts.get(0));
    	
    	for (int i = 0; i<accounts.size();i++)
    	{
    	    		String address = accounts.get(i).getState();
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


    	DetailFragmentBridal.account = accounts2.get(index);
    	DetailFragmentBridal.ind = index;
    	DetailFragmentBridal.accounts2 = accounts2;
    	//bridalTitle=DetailFragmentBridal.account.getName();
    }

   /* public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // llena el fragment2 primeras lineas
        if (container == null) {
            // Currently in a layout without a container, so no
            // reason to create our view.
            return null;
        }

        

        
      if(accounts != null){
    	  

    	  
        	final HorizontalPager pager  = new HorizontalPager(getActivity(), null);

			// using traditional for loop
      
        	
		for(int i=0; i<accounts.size(); i++)
			{
				viewer = (AccountViewBridal) inflater.inflate(R.layout.account_view_bridal, container, false);
				
				if(account != null){
					viewer.setAccount(accounts.get(i));
				}
				pager.addView(viewer); 
			}
			//Set current page to index
		pager.setCurrentPage(ind);
		    
	        pager.addOnScrollListener(new HorizontalPager.OnScrollListener() {
	            public void onScroll(int scrollX) {
	                //Log.d("TestActivity", "scrollX=" + scrollX);
	                //float scale = (float) (pager.getPageWidth() * pager.getChildCount()) / (float) control.getWidth();
	                //control.setPosition((int) (scrollX / scale));
	            //	scrollValue = scrollX;
	            }
	            public void onViewScrollFinished(int currentPage) {
	                //Log.d("TestActivity", "viewIndex=" + currentPage);
	                //control.setCurrentPage(currentPage);
	            	
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

	            	}
	            	
	            	if(!loadingAccountData && shouldLoadNewAccountData){
	            		loadingAccountData = true;
	            		shouldLoadNewAccountData = false;

	            		dialog = ProgressDialog.show(getActivity(), accounts.get(currentPage).getName(), "Loading Account Information. Please wait...", true);
	            		accountView = (AccountViewBridal)pager.getChildAt(currentPage);
	            		//setAccountImageUrl(accounts.get(currentPage).getId());
	            		fillTitle(accounts.get(currentPage).getName());
	            		//loadingAccountData = true;
	            	}
	            }
	        });		
		
	        return pager;
        }
			else{
	        	viewer = (AccountViewBridal) inflater.inflate(R.layout.account_view_bridal, container, false);
	        	if(account != null){
					viewer.setAccount(accounts.get(ind));
				}
        	return viewer;
        } 
    }*/
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // llena el fragment2 primeras lineas
        if (container == null) {
            // Currently in a layout without a container, so no
            // reason to create our view.
            return null;
        }
        //MasterFragmentBridal viewer = (MasterFragmentBridal) getFragmentManager().findFragmentById(R.id.master);	
        
        if(accounts2 != null){
        	//final HorizontalPager pager  = new HorizontalPager(getActivity(), null);
			// using traditional for loop
				for(int i=0; i<accounts2.size(); i++)
					{
						viewer = (AccountViewBridal) inflater.inflate(R.layout.account_view_bridal, container, false);
						if(accounts2 != null){
							viewer.setAccount(accounts2.get(i));
						}
						//pager.addView(viewer); 
					}
				
	        		loadingAccountData = true;
	        		shouldLoadNewAccountData = false;
	        		dialog = ProgressDialog.show(getActivity(), accounts2.get(ind).getName(), "Loading Account Information. Please wait...", true);
	        		//accountView = (AccountViewBridal)pager.getChildAt(currentPage);
	        		//accountView = (AccountViewBridal)viewer.getChildAt(ind);
	        		accountView =viewer;
	        		
	              	accountAddress = (TextView)accountView.findViewById(R.id.accountAddress);
	              	accountAddress.setText(bridalTitle);

	              	ImageView accountImageView= (ImageView)accountView.findViewById(R.id.accountImageView);
	              	accountImageView.setImageResource(R.drawable.bridal);
	        		
            		name = accounts2.get(ind).getName();
            		accid= accounts2.get(ind).getId();
            		
            		
            		ImageView imageAccount= (ImageView)accountView.findViewById(R.id.imageAccount);
            		TextView title1= (TextView)accountView.findViewById(R.id.title1);
            		TextView website1= (TextView)accountView.findViewById(R.id.website1);
            		
            		if (accid==1)
            		{
            			imageAccount.setImageResource(R.drawable.bridalaffairtoremember_bs);	
            			title1.setText(R.string.label_affairtoremember);
            			website1.setText(R.string.label_affairtoremember_web);
            		}
            		else if (accid==2)
            		{
            			imageAccount.setImageResource(R.drawable.greatbridalextravangaza_bs);
            			title1.setText(R.string.label_greatbridalextravangaza);
            			website1.setText(R.string.label_greatbridalextravangaza_web);
            		}
            		else if (accid==3)
            		{
            			imageAccount.setImageResource(R.drawable.everythingbridal_bs);
            			title1.setText(R.string.label_everythingbridal_bs);
            			website1.setText(R.string.label_everythingbridal_bs_web);
            		}
            		else if (accid==4)
            		{
            			imageAccount.setImageResource(R.drawable.bosco_bs);
            			title1.setText(R.string.label_bosco);
            			website1.setText(R.string.label_bosco_web);
            		}
            		else if (accid==5)
            		{
            			imageAccount.setImageResource(R.drawable.bridalaffairtoremember_bs);
            			title1.setText(R.string.label_affairtoremember);
            			website1.setText(R.string.label_affairtoremember_web);
            		}
            		else if (accid==6)
            		{
            			imageAccount.setImageResource(R.drawable.greatbridalextravangaza_bs);
            			title1.setText(R.string.label_greatbridalextravangaza);
            			website1.setText(R.string.label_greatbridalextravangaza_web);
            		}
            		else if (accid==7)
            		{
            			imageAccount.setImageResource(R.drawable.nypremierbridal_bs);
            			title1.setText(R.string.label_nypremierbridal);
            			website1.setText(R.string.label_nypremierbridal_web);
            		}
            		else if (accid==8)
            		{
            			imageAccount.setImageResource(R.drawable.bosco_bs);
            			title1.setText(R.string.label_bosco);
            			website1.setText(R.string.label_bosco_web);
            		}
            		else if (accid==9)
            		{
            			imageAccount.setImageResource(R.drawable.bridalaffairtoremember_bs);
            			title1.setText(R.string.label_affairtoremember);
            			website1.setText(R.string.label_affairtoremember_web);
            		}
            		else if (accid==10)
            		{
            			imageAccount.setImageResource(R.drawable.elegantbridaiprod_bs);
            			title1.setText(R.string.label_elegantbridaiprod);
            			website1.setText(R.string.label_elegantbridaiprod_web);
            			
            		}
            		else if (accid==11)
            		{
            			imageAccount.setImageResource(R.drawable.planitexpo_bs);
            			title1.setText(R.string.label_planitexpo);
            			website1.setText(R.string.label_planitexpo_web);
            		}
            		
            		ImageView registerImageView= (ImageView)accountView.findViewById(R.id.register);
            		registerImageView.setOnClickListener(mOnclick_register);
	              	
                    if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_LANDSCAPE)
                    {
                      	TextView textViewTitle4= (TextView)accountView.findViewById(R.id.textViewTitle4);
                      	textViewTitle4.setText(name);

                    }
            		
	        		fillTitle(accounts2.get(ind).getName());
	        		
	              	Button bcall = (Button)accountView.findViewById(R.id.b_call);
	              	bcall.setOnClickListener(mOnclick_call2);
	              	

	              	
	              	Button b_email = (Button)accountView.findViewById(R.id.b_email);
	              	b_email.setOnClickListener(mOnclick_email);
	              	
			//Set current page to index
	        /*pager.setCurrentPage(ind);
		    
	        pager.addOnScrollListener(new HorizontalPager.OnScrollListener() {
	            public void onScroll(int scrollX) {
	                //Log.d("TestActivity", "scrollX=" + scrollX);
	                //float scale = (float) (pager.getPageWidth() * pager.getChildCount()) / (float) control.getWidth();
	                //control.setPosition((int) (scrollX / scale));
	            	scrollValue = scrollX;
	            }
	            public void onViewScrollFinished(int currentPage) {
	                //Log.d("TestActivity", "viewIndex=" + currentPage);
	                //control.setCurrentPage(currentPage);
	            	
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
	            	}
	            	
	            	if(!loadingAccountData && shouldLoadNewAccountData){
	            		loadingAccountData = true;
	            		shouldLoadNewAccountData = false;
	            		dialog = ProgressDialog.show(getActivity(), accounts.get(0).getName(), "Loading Account Information. Please wait...", true);
	            		accountView = (AccountViewBridal)pager.getChildAt(currentPage);
	            		fillTitle(accounts.get(currentPage).getName());
	            		
	            	}
	            }
	        });*/		
		
	      //  return pager;
	        return viewer;
        }else{
        	viewer = (AccountViewBridal) inflater.inflate(R.layout.account_view_bridal, container, false);
        	if(accounts2 != null){
				viewer.setAccount(accounts2.get(ind));
			}
        	return viewer;
        } 
    }
    
    private void call_register()
    {
    	String url="";
    	
    	if (accid==1)
		{
			url="http://www.locationsmagazine.com/clickthrough?address=www.bridalaffair.com/index.php?page=Show%20Dates";	
		}
		else if (accid==2)
		{
			url="http://www.locationsmagazine.com/clickthrough?address=www.a2zeventservices.com/shows.html";	
		}
		else if (accid==3)
		{
			url="http://www.locationsmagazine.com/clickthrough?address=www.everythingbridalshows.com/bridalshows.php";	
		}
		else if (accid==4)
		{
			url="http://www.locationsmagazine.com/clickthrough?address=www.boscobridal.com#content=shows.html";	
		}
		else if (accid==5)
		{
			url="http://www.locationsmagazine.com/clickthrough?address=www.bridalaffair.com/index.php?page=Show%20Dates";	
		}
		else if (accid==6)
		{
			url="http://www.locationsmagazine.com/clickthrough?address=www.a2zeventservices.com/shows.html";	
		}
		else if (accid==7)
		{
			url="http://www.locationsmagazine.com/clickthrough?address=www.1888metroexpo.com/bridal_expo/information.asp";
		}
		else if (accid==8)
		{
			url="http://www.locationsmagazine.com/clickthrough?address=www.boscobridal.com#content=shows.html";		
		}
		else if (accid==9)
		{
			url="http://www.locationsmagazine.com/clickthrough?address=www.bridalaffair.com/index.php?page=Show%20Dates";		
		}
		else if (accid==10)
		{
			url="http://www.elegantbridal.com/showschedule.php";	
		}
		else if (accid==11)
		{
			url="http://www.planitexpo.com/shows.html";	
		}
    	
    	
    	Intent i = new Intent(Intent.ACTION_VIEW);
    	Uri u = Uri.parse(url);
    	i.setData(u);
    	startActivity(i);
    }
    
    
    private OnClickListener mOnclick_register = new OnClickListener()
	{
        public void onClick(View v)
        {
        	 call_register() ;
        }
    };

    
    private OnClickListener mOnclick_email = new OnClickListener()
	{
        public void onClick(View v)
        {
        	 call_email() ;
        }
    };
  
    
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
    	
    	
    private OnClickListener mOnclick_shared = new OnClickListener()
  		{
  	        public void onClick(View v)
  	        {
  	        	share_email(1);
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

    
    
    private void fillTitle(final String title){    	
    	Thread splashThread = new Thread() {
            @Override
            public void run() {            	
            	
         
            	WebServiceHelper webservice = new WebServiceHelper();
            	returnDetailObject = webservice.getDataDetailFromWebServiceBridal(Common.GetBridalShows,title);
            	
            	//WebServiceHelper webservice = new WebServiceHelper();
            /*	imageUrl = webservice.getStringUrlForAccountImage("GetAccountImage", accid);
            	
            	accReception = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=reception&accid=" + accid);
            	accDinner = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=dinner&accid=" + accid);
            	accComments = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=comments&accid=" + accid);
            	accPhone = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/acc_phone_number.php?accid=" + accid);
			
                imagecache = new ImageCache(getActivity().getCacheDir());
            	if(!imagecache.isImageinCache(imageUrl)){
            		imagecache.getImageDataFromUrl(imageUrl);
            	}*/
            	
            	handler.post(finishSetAccountImageUrl);
            }
         };
         splashThread.start();
    }
    
    
    
    private void setAccountImageUrl(final int accid){    	
    	Thread splashThread = new Thread() {
            @Override
            public void run() {            	
            	
            	WebServiceHelper webservice = new WebServiceHelper();
            /*	imageUrl = webservice.getStringUrlForAccountImage("GetAccountImage", accid);
            	
            	accReception = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=reception&accid=" + accid);
            	accDinner = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=dinner&accid=" + accid);
            	accComments = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/text_data_iphone.php?what=comments&accid=" + accid);
            	accPhone = UtilityFunctions.getTextDataFromUrl("http://locationsmagazine.com/admina/pages/fixes/iphone/acc_phone_number.php?accid=" + accid);
			
                imagecache = new ImageCache(getActivity().getCacheDir());
            	if(!imagecache.isImageinCache(imageUrl)){
            		imagecache.getImageDataFromUrl(imageUrl);
            	}*/
            	
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
	        	// UI_UPDATE_LISTENER.onDialogComingSoon();
	        }
	    };
	    

    

	    public void call_phone(String phoneNumber) 
	    { 
	    	
	    	try{
	    		Intent intent = new Intent(Intent.ACTION_CALL);
	    	    intent.setData(Uri.parse("tel:"+phoneNumber));
	    	    startActivity(intent);

	    		
	    	}catch(Exception e)
	    	{
	    		e.getMessage();
	    	}
	    } 
	    
	    private OnClickListener mOnclick_map = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	call_map(addressMap) ;
	        }
	    };
	    

		 public void call_map(String address)
		 {
					 //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:41.656313,-0.877351"));
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+address));
					 //		 //
					startActivity(intent);
		  }

		    protected OnClickListener mOnclickDetailList = new OnClickListener()
			{
		        public void onClick(View v)
		        {
		        	 Object oPos =((LinearLayout) v).getTag();
		        	 Accounts xbean =(Accounts) oPos;
		        	// ((LinearLayout) v).setBackgroundColor(Color.BLUE);
		        	 
		        	 showDetails2(xbean);
		        }
		    };
		    
		    void showDetails2(Accounts bean) {
		      
		    	
		    /*	LinearLayout view_detail2 = (LinearLayout)accountView.findViewById(R.id.view_detail2);
		    	view_detail2.setVisibility(View.VISIBLE);
		    	
		    	TextView textViewCost = (TextView)accountView.findViewById(R.id.textViewCost);
		    	TextView textViewAddress = (TextView)accountView.findViewById(R.id.textViewAddress);
		    	TextView textViewLocation = (TextView)accountView.findViewById(R.id.textViewLocation);
		    	TextView textViewTime = (TextView)accountView.findViewById(R.id.textViewTime);
		    	TextView textViewDate = (TextView)accountView.findViewById(R.id.textViewDate);
		    	
		    	textViewCost.setText(bean.getName());
		    	textViewAddress.setText(bean.getCity());
		    	textViewTime.setText(bean.getUrl());
		    	textViewLocation.setText(bean.getAddress1());
		    	textViewDate.setText(bean.getZip());
		    	
	*/
		    	
		    	addressMap=bean.getCity();


		    }
		    
		 
		 protected LinearLayout fillList(Accounts bean ,Context ctx)
		    {

			    LayoutInflater layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	View view=layoutInflater.inflate(R.layout.bridal_list, null);
		    	
		    	LinearLayout layoutFrame1 = (LinearLayout)view.findViewById(R.id.LinearLayout01);

		    	TextView tv_date = (TextView)view.findViewById(R.id.tv_date);
		    	tv_date.setText(bean.getName());
				
		    	tv_date.setVisibility(View.VISIBLE);
		    	
		    	TextView tv_address = (TextView)view.findViewById(R.id.tv_address2);
		    	tv_address.setText(bean.getCity());
		    	addressMap=bean.getCity();
		    	tv_address.setVisibility(View.VISIBLE);
		    	
		    	ImageView image_view= (ImageView)view.findViewById(R.id.image_view);
		    	image_view.setVisibility(View.VISIBLE);
		    	
		    	TextView textViewAddress = (TextView)view.findViewById(R.id.textViewAddress);
		    	textViewAddress.setText(bean.getAddress1());
		    	
              	Button bshared = (Button)view.findViewById(R.id.b_shared2);
              	bshared.setOnClickListener(mOnclick_shared);
              	
        		Button bmap = (Button)view.findViewById(R.id.b_map);
              	bmap.setOnClickListener(mOnclick_map);
		    	return layoutFrame1;

		    	 
		    }
		    
		 
    public void finishedSetAccountImageUrl(){  // fill all the fields from fragment 2  	

      	
    	accounts2 = retrieveBridalDetailFromSoap(returnDetailObject);
     	LinearLayout layoutItem = null;
     	layoutItem = (LinearLayout)accountView.findViewById(R.id.record_data);
    	layoutItem.removeAllViews();

    	for (int i=0;i<accounts2.size();i++)
    	{
       	 	Accounts bean = (Accounts)accounts2.get(i);
       	 	if (bean.getCity()!=null)
       	 		layoutItem.addView(fillList(bean,accountView.getContext()));
    	}
    	
    	
    	
      	accountAddress = (TextView)accountView.findViewById(R.id.accountAddress);
;

      	
		//Button bmap = (Button)accountView.findViewById(R.id.b_map2);
      	//bmap.setOnClickListener(mOnclick_map);

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
		  	             account.setId(Integer.parseInt(obj1.getProperty(0).toString()));
		  	             
		  	           String linea = obj1.getProperty(3).toString();  
		  	           String [] campos = linea.split("\\s+");
		  	           String date1=campos[0];
		  	           date1=campos[0];
		  	           
		  	           String [] fechaSplit = date1.split("/");
		  	           String newFecha=fechaSplit[2]+"-"+fechaSplit[0]+"-"+fechaSplit[1];
		  	        		 
			  	       //String text = campos[0]+", "+obj1.getProperty(2).toString()+" @ "+ campos[1]+" "+ campos[2].toLowerCase();
		  	           String text = newFecha+", "+obj1.getProperty(2).toString()+" @ "+ campos[1]+" "+ campos[2].toLowerCase();
			  	       account.setName(text);
		  	           account.setCity(obj1.getProperty(5).toString());
		  	           account.setAddress1(obj1.getProperty(4).toString());
		  	           account.setZip(obj1.getProperty(2).toString()+", "+campos[0]);
		  	           account.setUrl(campos[1]+" "+ campos[2].toLowerCase());
		  	           
		  	         //BridalShows{Id=201; Company=A Bridal Affair To Remember; Day=Wed; Datex=9/14/2011 6:45:00 PM; Location=Villa Barone Manor; Address=737 Throgs Neck Expwy Bronx, NY; Cost=Free; Notes=anyType{}; }
		  	           
		  	           //account.setState(state);
		  	           accounts.add(account);
	             }
	            	 
	         }        	
        return accounts;
    }
    
	

	
}
