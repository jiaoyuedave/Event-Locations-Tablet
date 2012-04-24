package us.eventlocations.androidtab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class WebViewBilder extends Activity {
    private WebView webview;
    private static final String TAG = "Main";
    private ProgressDialog progressBar;  
	private String urlPage;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.webview);
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON); 
        executeWebBrowser();
    	   
    	ImageView share = (ImageView)findViewById(R.id.done);
    	share.setOnClickListener(mOnclickNavBack);
        
        ImageView info = (ImageView)findViewById(R.id.navback_refresh);
        info.setOnClickListener(mOnclick_refresh);
        
    }
    
    protected OnClickListener mOnclickNavBack = new OnClickListener()
	{
        public void onClick(View v)
        {
        	finish();
        }
    };
    
    private OnClickListener mOnclick_refresh = new OnClickListener()
		{
	        public void onClick(View v)
	        {
	        	executeWebBrowser();
	        }
	    };	  
	    
	    private void executeWebBrowser()
	    {
	        this.webview = (WebView)findViewById(R.id.webview);
	        
	        ImageView navback=(ImageView)findViewById(R.id.done);
	    	navback.setOnClickListener(mOnclickNavBack);

	        ImageView navback_refresh=(ImageView)findViewById(R.id.navback_refresh);
	        navback_refresh.setOnClickListener(mOnclick_refresh);
	    	
	    	
	        urlPage="";
	        Bundle extras = getIntent().getExtras();
	        if (extras != null) 
	        {
	        	
	        	urlPage= extras.getString("url");
	        }

	        WebSettings settings = webview.getSettings();
	        settings.setJavaScriptEnabled(true);
	        settings.setJavaScriptCanOpenWindowsAutomatically(true);
	        settings.setAllowContentAccess(true);
	        settings.setEnableSmoothTransition(true);
	        settings.setGeolocationEnabled(true);
	        settings.setLoadsImagesAutomatically(true);
	        settings.setUseWebViewBackgroundForOverscrollBackground(true);
	        settings.setSupportZoom(true);
	        settings.setBuiltInZoomControls(true);
	        //settings.set
	        webview.setInitialScale(100);   
	   //     webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
	        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

	        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	        final Activity MyActivity = this;
	        
	        webview.setWebChromeClient(new WebChromeClient() 
	        {
	         public void onProgressChanged(WebView view, int progress)   
	         {

	        	 MyActivity.setTitle("Loading...");
	        	 MyActivity.setProgress(progress * 100); 

	         if(progress == 100)
	        	 MyActivity.setTitle(R.string.app_name);
	         	 MyActivity.setTitleColor(Color.WHITE);
	           }
	         });
	        
	       
	       
	        webview.setWebViewClient(new WebViewClient() {
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	               // Log.i(TAG, "Processing webview url click...");
	                view.loadUrl(url);
	                return true;
	            }
	           
	            public void onPageFinished(WebView view, String url) {
	            }
	           
	            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	                Log.e(TAG, "Error: " + description);
	                //Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
	                alertDialog.setTitle("Error");
	                alertDialog.setMessage(description);
	                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                        return;
	                    }
	                });
	                alertDialog.show();
	            }
	        });
	        webview.loadUrl(urlPage);
	    }
    

    
}
