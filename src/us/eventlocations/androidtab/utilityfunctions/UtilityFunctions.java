package us.eventlocations.androidtab.utilityfunctions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.util.ByteArrayBuffer;


public class UtilityFunctions {

	public UtilityFunctions(){
		
	}
	
	public static  String getTextDataFromUrl(String url){
		String html = "";
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
            URL updateURL = new URL(url);            
            URLConnection conn = updateURL.openConnection();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while((current = bis.read()) != -1){
                baf.append((byte)current);
            }
            /* Convert the Bytes read to a String. */
            html = new String(baf.toByteArray());
        } catch (Exception e) {
        	e.printStackTrace();   
        }
        
        try {
			is.close();
			bis.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return html;
	}	
	
}
