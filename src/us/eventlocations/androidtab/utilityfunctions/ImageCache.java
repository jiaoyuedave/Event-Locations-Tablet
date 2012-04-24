package us.eventlocations.androidtab.utilityfunctions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import org.apache.http.util.ByteArrayBuffer;

public class ImageCache {

	static File cacheRoot = null;
	
	public ImageCache(File cache){
		cacheRoot = cache;
	}
	
	static protected String md5(String s) throws Exception {
		MessageDigest md=MessageDigest.getInstance("MD5");
		
		md.update(s.getBytes());
		
		byte digest[]=md.digest();
		StringBuffer result=new StringBuffer();
		
		for (int i=0; i<digest.length; i++) {
			result.append(Integer.toHexString(0xFF & digest[i]));
		}
		return(result.toString());
	}
	
	public Boolean isImageinCache(String url){
		File storage = new File (cacheRoot + "/images");
		File filePath = null;
		try {
			filePath = new File(storage, md5(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(filePath.exists()){
			return true;
		}
		return false;
	}
	
	public File getImagePathLocal(String url){
		File storage = new File (cacheRoot + "/images");
		File filePath = null;
		try {
			filePath = new File(storage, md5(url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}
	
	public void getImageDataFromUrl(String url){
		//String html = "";
		InputStream is = null;
		BufferedInputStream bis = null;
		ByteArrayBuffer baf = null;
		File filePath;
		FileOutputStream file;
		try {
            URL updateURL = new URL(url);            
            URLConnection conn = updateURL.openConnection();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            baf = new ByteArrayBuffer(50);
            int current = 0;
          
            while((current = bis.read()) != -1){
                baf.append((byte)current);
            }
            /* Convert the Bytes read to a String. */
            //html = new String(baf.toByteArray());
            is.close();
			bis.close();
        } catch (Exception e) {
        	e.printStackTrace();   
        }
       
		//File sdCard = Environment.getExternalStorageDirectory();				
		if (cacheRoot!=null) {
			try {				
				File storage = new File (cacheRoot + "/images"); storage.mkdirs();
				filePath = new File(storage, md5(url));
				file = new FileOutputStream(filePath);
				file.write(baf.toByteArray());
				file.flush();
				file.close();
				baf.clear();
				baf = null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}	
	}
	
	
}
