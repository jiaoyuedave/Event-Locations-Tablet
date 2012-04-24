package us.eventlocations.androidtab.fragments;

import java.io.File;
import java.util.ArrayList;

import org.ksoap2.serialization.SoapObject;

import us.eventlocations.androidtab.Common;
import us.eventlocations.androidtab.R;
import us.eventlocations.androidtab.layouts.AccountViewCounty;
import us.eventlocations.androidtab.models.Accounts;
import us.eventlocations.androidtab.pager.HorizontalPager;
import us.eventlocations.androidtab.utilityfunctions.ImageCache;
import us.eventlocations.androidtab.utilityfunctions.UtilityFunctions;
import us.eventlocations.androidtab.utilityfunctions.WebServiceHelper;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonsware.cwac.bus.SimpleBus;
import com.commonsware.cwac.cache.AsyncCache;
import com.commonsware.cwac.cache.WebImageCache;

public class DetailFragmentDemo extends Fragment {
	public TextView accountAddress; 
	public RelativeLayout layout_main;
	private AccountViewCounty viewer;
	private static String addressMap;
	private static String bridalTitle;
	private String name,city,url,address,zip;
	static ArrayList<Accounts> accounts;
	static Accounts account;
	static int ind;

   	private SoapObject returnDetailObject;
	private WebImageCache cache=null;
	private SimpleBus bus=new SimpleBus();
	private Drawable placeholder=null;
	
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
	AccountViewCounty accountView = null;
    
	/**
     * Create a new instance of DetailFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailFragmentCounty newInstance(int index) {
        DetailFragmentCounty f = new DetailFragmentCounty();
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
    	DetailFragmentCounty.account = accounts.get(index);
    	DetailFragmentCounty.ind = index;
    	DetailFragmentCounty.accounts = accounts;
    	bridalTitle=DetailFragmentCounty.account.getName();
    //	System.out.println(bridalTitle);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // llena el fragment2 primeras lineas
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
				viewer = (AccountViewCounty) inflater.inflate(R.layout.account_view_county, container, false);
				
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

	            		AccountViewCounty accountView1 = (AccountViewCounty)pager.getChildAt(currentPage-1);
	            		AccountViewCounty accountView2 = (AccountViewCounty)pager.getChildAt(currentPage+1);
	                  	ImageView accountImage = null;
	                  	accountImage= (ImageView)accountView1.findViewById(R.id.accountImageView);
	                  	accountImage.setImageDrawable(null);
	                  	
	                  	
	                  	
	                  	if (currentPage+1>accounts.size() )
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

	            		dialog = ProgressDialog.show(getActivity(), accounts.get(currentPage).getName(), "Loading Account Information. Please wait...", true);
	            		accountView = (AccountViewCounty)pager.getChildAt(currentPage);
	            		setAccountImageUrl(accounts.get(currentPage).getId());
	            		String id =String.valueOf(accounts.get(currentPage).getId()); 
	            		

	            		//fillTitle(id);
	            		executeNew();
	            		
	            	}
	            }
	        });		
		
	        return pager;
        }else{
        	viewer = (AccountViewCounty) inflater.inflate(R.layout.account_view_county, container, false);
        	if(account != null){
				viewer.setAccount(accounts.get(ind));
			}
        	return viewer;
        } 
    }
    
    
    private void executeNew()
    {

       	 Accounts bean = (Accounts)accounts.get(DetailFragmentCounty.ind);
    	 //showDetails2(bean);
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
    
    
    
    private void setAccountImageUrl(final int accid){    	
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
            	else
            		imagecache.getImageDataFromUrl(imageUrl);
            	
            	handler.post(finishSetAccountImageUrl2);
            }
         };
         splashThread.start();
    }
    
    private Runnable finishSetAccountImageUrl2 = new Runnable() {
 		 public void run() {
 			 finishedSetAccountImageUrl2();
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
	        }
	    };
	    

	  

	    public void call_phone(String phoneNumber) 
	    { 
	    	
	    	try{
	    		/*Intent intent = new Intent(Intent.ACTION_CALL);
	    	    intent.setData(Uri.parse("tel:"+phoneNumber));
	    	    startActivity(intent);*/
	    		addContact(accPhone);
	    		
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
 	    	intent.putExtra(ContactsContract.Intents.Insert.COMPANY, name);
 	    	intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
 	    	intent.putExtra(ContactsContract.Intents.Insert.PHONE, accPhone);
 	    	intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,  ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
 	    	
 	    	
 	    	//intent.putExtra(ContactsContract.Intents.Insert.EMAIL, "dtoledos@gmail.com");
 	    	//intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,  ContactsContract.CommonDataKinds.Email.TYPE_WORK);
 	    	
 	    	//intent.putExtra(ContactsContract.Intents.Insert.NOTES, "x  dsdsjksda  dsalkdsajdsa ");

 	    	intent.putExtra(ContactsContract.Intents.Insert.POSTAL_TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK);
 	    	intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);

 	    	intent.putExtra(ContactsContract.CommonDataKinds.StructuredPostal.CITY, city);
 	    	intent.putExtra(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, zip);
 	    	//intent.putExtra(ContactsContract.CommonDataKinds.StructuredPostal.POBOX, "12233");
 	    	
 	    /*	intent.putExtra(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
 	    	intent.putExtra(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK);
 	    	intent.putExtra(ContactsContract.CommonDataKinds.StructuredPostal.STREET,"street 1");
 	    	intent.putExtra(ContactsContract.CommonDataKinds.StructuredPostal.CITY,"city");
 	    	intent.putExtra(ContactsContract.CommonDataKinds.StructuredPostal.REGION,"region");
 	    	intent.putExtra(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,"peru");
 	    	
 	    	intent.putExtra(ContactsContract.CommonDataKinds.Website.URL, "www.xxx.com");
 	    	intent.putExtra(ContactsContract.CommonDataKinds.Website.TYPE, ContactsContract.CommonDataKinds.Website.TYPE_WORK);
 	    	intent.putExtra(ContactsContract.CommonDataKinds.Website.LABEL, "www.xxx.com");*/
 	    	
 	    	
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
		        	 
		        	// showDetails2(xbean);
		        }
		    };
		    
		   /* void showDetails2(Accounts bean) {
		      
		    	
		    	LinearLayout view_detail2 = (LinearLayout)accountView.findViewById(R.id.view_detail2);
		    	view_detail2.setVisibility(View.VISIBLE);
		    	
		    	TextView textViewCost = (TextView)accountView.findViewById(R.id.textViewCost);
		    	TextView textViewAddress = (TextView)accountView.findViewById(R.id.textViewAddress);
		    	TextView textViewLocation = (TextView)accountView.findViewById(R.id.textViewLocation);

		    	textViewCost.setText(bean.getName());
		    	textViewAddress.setText(bean.getUrl());
		    	textViewLocation.setText(bean.getAddress1());
		    	addressMap=bean.getAddress1();
		    	
		    	//dialog = ProgressDialog.show(getActivity(), bean.getName(), "Loading Account Information. Please wait...", true);
		    	setAccountImageUrl(bean.getId());
		    	
		    	name = bean.getName();
		    	city=bean.getCity();
		    	url=bean.getUrl();
		    	address=bean.getAddress1();
		    	zip=bean.getZip();
		    	
		    }*/
		    
		 
		 protected LinearLayout fillList(Accounts bean ,Context ctx)
		    {

			    LayoutInflater layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	View view=layoutInflater.inflate(R.layout.bridal_list, null);
		    	
		    	LinearLayout layoutFrame1 = (LinearLayout)view.findViewById(R.id.LinearLayout01);
		    	layoutFrame1.setOnClickListener(mOnclickDetailList);
		    	layoutFrame1.setTag(bean);
			 
		    	
		    	TextView tv_date = (TextView)view.findViewById(R.id.tv_date);
		    	tv_date.setText(bean.getName());
				
		    	tv_date.setVisibility(View.VISIBLE);
		    	
		    	TextView tv_address = (TextView)view.findViewById(R.id.tv_address2);
		    	tv_address.setText(bean.getCity());
		    	tv_address.setVisibility(View.VISIBLE);
		    	
		    	ImageView image_view= (ImageView)view.findViewById(R.id.image_view);
		    	image_view.setVisibility(View.VISIBLE);
		    	
		    	return layoutFrame1;


		    	 
		    }
		    
		    public void finishedSetAccountImageUrl2(){  // fill all the fields from fragment 2  	
		      	
		    	ImageView accountImage = (ImageView)accountView.findViewById(R.id.accountImageView);
		    	TextView accountReception = (TextView)accountView.findViewById(R.id.accountReception);
		      	TextView accountDinner = (TextView)accountView.findViewById(R.id.accountDinner);
		      	TextView accountComments = (TextView)accountView.findViewById(R.id.accountComments);
		      	

		      	

		      	
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
		    		//getCache().handleImageView(accountImage,imageUrl.toString(),"demo");
		      		String imageLocalPath = imagecache.getImagePathLocal(imageUrl).toString();
		      		//Log.e("OMONONNIUBIU",imageLocalPath);
		      		//Bitmap myBitmap = BitmapFactory.decodeFile(imageLocalPath);
		      	    //accountImage.setImageBitmap(myBitmap);
		      		//myBitmap = null;

		      		Drawable d = Drawable.createFromPath(imageLocalPath);
		      		accountImage.setImageDrawable(d);
		      		d = null;
		          	accountReception = null;
		          	accountDinner = null;
		          	accountComments = null;

		      		System.gc();
		      		
		      	}catch(Exception ex){
		      		Log.e("XOXOXOXO"," on 154" + ex.toString());
		      		
		      	}
		   	
		    	 
				loadingAccountData = false;
				dialog.dismiss();
		    } 		 
		 
		 
    public void finishedSetAccountImageUrl(){  // fill all the fields from fragment 2  	
      	
    /*	ImageView accountImage = (ImageView)accountView.findViewById(R.id.accountImageView);
    	TextView accountReception = (TextView)accountView.findViewById(R.id.accountReception);
      	TextView accountDinner = (TextView)accountView.findViewById(R.id.accountDinner);
      	TextView accountComments = (TextView)accountView.findViewById(R.id.accountComments);*/
      	
    	accounts = retrieveBridalDetailFromSoap(returnDetailObject);

   
    	
     	LinearLayout layoutItem = null;
    	layoutItem = (LinearLayout)accountView.findViewById(R.id.record_data);
    	layoutItem.removeAllViews();

    	for (int i=0;i<accounts.size();i++)
    	{
       	 	Accounts bean = (Accounts)accounts.get(i);
       	 	if (bean.getName()!=null)
       	 		layoutItem.addView(fillList(bean,accountView.getContext()));
    	}
    	
    	
    	
      	accountAddress = (TextView)accountView.findViewById(R.id.accountAddress);
      	accountAddress.setText(bridalTitle);
      	
		Button bmap = (Button)accountView.findViewById(R.id.b_map);
      	bmap.setOnClickListener(mOnclick_map);

      	Button bcall = (Button)accountView.findViewById(R.id.b_call);
      	bcall.setOnClickListener(mOnclick_call);
      	

    	 
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
		  	           //Account{Id=4868; Name=Abigail Kirsch at New York Botanical Garden; Contact=NA; Comments=NA; Address1=Southern Blvd (@ 200 Street); City=New York; State=NY; Url=www.abigailkirsch.com; Zip=10458; Map=1; ServiceId=0; }
		  	           //String linea = obj1.getProperty(3).toString();  
		  	          // String [] campos = linea.split("\\s+");  
			  	      // String text = campos[0]+", "+obj1.getProperty(2).toString()+" @ "+ campos[1]+" "+ campos[2].toLowerCase();
		  	            account.setName(obj1.getProperty(1).toString());
		  	          // account.setCity(obj1.getProperty(5).toString());
		  	            account.setAddress1(obj1.getProperty(4).toString()+", "+obj1.getProperty(5).toString()+", "+obj1.getProperty(6).toString()+" ,"+obj1.getProperty(8).toString());
		  	          // account.setZip(obj1.getProperty(2).toString()+", "+campos[0]);
		  	            account.setUrl(obj1.getProperty(7).toString());
		  	           
		  	         //BridalShows{Id=201; Company=A Bridal Affair To Remember; Day=Wed; Datex=9/14/2011 6:45:00 PM; Location=Villa Barone Manor; Address=737 Throgs Neck Expwy Bronx, NY; Cost=Free; Notes=anyType{}; }
		  	           
		  	           //account.setState(state);
		  	           accounts.add(account);
	             }
	            	 
	         }        	
        return accounts;
    }
    
	

	
}
