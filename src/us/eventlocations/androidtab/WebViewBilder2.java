package us.eventlocations.androidtab;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class WebViewBilder2 extends Activity {
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
        setContentView(R.layout.webview2);
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON); 
        this.webview = (WebView)findViewById(R.id.webview);
        
        ImageView navback=(ImageView)findViewById(R.id.navback_webview);
    	navback.setOnClickListener(mOnclickNavBack);


        urlPage="";
        Bundle extras = getIntent().getExtras();
        if (extras != null) 
        {
        	
        	urlPage= extras.getString("url");
        	//body= extras.getString("body");
        }

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        //settings.setSavePassword(false);
        //settings.setSaveFormData(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        webview.setInitialScale(50);       
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
         	// MyActivity.setT
         	//getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_OFF); 
        // getWindow().setFeatureInt( Window.FEATURE_NO_TITLE, Window.PROGRESS_VISIBILITY_OFF);

           }
         });
        
       
       /* progressBar = ProgressDialog.show(this, "", "Loading...",true,true,new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

                webview.stopLoading();
                progressBar.dismiss();
                finish();
			}
		});*/
       
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               // Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }
           
            public void onPageFinished(WebView view, String url) {
                //Log.i(TAG, "Finished loading URL: " +url);
              /*  if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }*/
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
        //urlPage =Common.visit_our_website_btn3;
        webview.loadUrl(urlPage);
       
        
        /*String data2="";
		try {
			//data2 = convertStreamToString(getAssets().open("map_hdpi.html"));
			data2 = convertStreamToString(getAssets().open("quartz_crystal_theory.pdf"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//webview.loadUrl("file:///android_asset/quartz_crystal_theory.pdf");
		//webview.loadUrl("http://docs.google.com/gview?embedded=true&url="+"file:///android_asset/quartz_crystal_theory.pdf");
       // webview.loadDataWithBaseURL("file:///android_asset/",data2, "text/html", "utf8", null);
        
        
    }
    
    protected String convertStreamToString(InputStream is) throws IOException {
        
    	if (is != null) {
    		Writer writer = new StringWriter();
    		char[] buffer = new char[1024];
    try {
    	Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    	int n;
    	while ((n = reader.read(buffer)) != -1) {
    		writer.write(buffer, 0, n);
    	}
    	} finally {
    		is.close();
    	}
    	return writer.toString();
    	} else {       
    		return "";
    	}
  }
    
    protected OnClickListener mOnclickNavBack = new OnClickListener()
	{
        public void onClick(View v)
        {
        	finish();
        }
    };
}
