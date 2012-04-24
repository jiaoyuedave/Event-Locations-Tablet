package us.eventlocations.androidtab.utilityfunctions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceHelper {
	
    public String NAMESPACE = "http://tempuri.org/";
    public String URL = "http://www.eventlocations.us/admina/services/iphone.asmx";
    public HttpTransportSE androidHttpTransport;
    public SoapSerializationEnvelope envelope;

    public WebServiceHelper(){
    	
    }
	
	public SoapObject getDataFromWebService(String method){
	    SoapObject returnObject = null;
	    String soap_action = "http://tempuri.org/" + method;

        try{
            SoapObject Request = new SoapObject(NAMESPACE, method);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);
            androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(soap_action, envelope);
            returnObject = (SoapObject)envelope.getResponse();  
        }
        catch(Exception e){
            e.printStackTrace();
        }
	    return returnObject;
	} 
	
	public SoapObject getDataFromWebService(String method,String text){
	    SoapObject returnObject = null;
	    String soap_action = "http://tempuri.org/" + method;

        try{
            SoapObject Request = new SoapObject(NAMESPACE, method);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);
            androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(soap_action, envelope);
            returnObject = (SoapObject)envelope.getResponse();  
        }
        catch(Exception e){
            e.printStackTrace();
        }
	    return returnObject;
	} 
	
	
	public SoapObject getDataDetailFromWebServiceBridal(String method,String var){
	    SoapObject returnObject = null;
	    String soap_action = "http://tempuri.org/" + method;

        try{
            SoapObject Request = new SoapObject(NAMESPACE, method);
            Request.addProperty("bridalCompany", var);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);
            androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(soap_action, envelope);
            returnObject = (SoapObject)envelope.getResponse();  
        }
        catch(Exception e){
            e.printStackTrace();
        }
	    return returnObject;
	} 
	
	public SoapObject getDataDetailFromWebServiceSentEmail(String method,String yourname, String yourEmail,String ssubject, String smessage,final int accid){
	    SoapObject returnObject = null;
	    String soap_action = "http://tempuri.org/" + method;

        try{
            SoapObject Request = new SoapObject(NAMESPACE, method);
            Request.addProperty("accid", accid);
            Request.addProperty("email", yourEmail);
            Request.addProperty("name", yourname);
            Request.addProperty("subject", ssubject);
            Request.addProperty("message", smessage);
            
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);
            androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(soap_action, envelope);
            returnObject = (SoapObject)envelope.getResponse();  
        }
        catch(Exception e){
            e.printStackTrace();
        }
	    return returnObject;
	} 
	
	public SoapObject getDataWinHoneyMoonForm(String method,String syourName,String syourLastName,String semail,String set_address1,String set_address2,String set_state,String set_city,String set_zip,String set_people,String set_price,int county,int site,String date,String country){
	    SoapObject returnObject = null;
	    String soap_action = "http://tempuri.org/" + method;

        try{
            SoapObject Request = new SoapObject(NAMESPACE, method);
            Request.addProperty("firstName", syourName);
            Request.addProperty("lastName", syourLastName);
            Request.addProperty("emailAddress", semail);
            Request.addProperty("address1", set_address1);
            Request.addProperty("address2", set_address2);
            Request.addProperty("city", set_city);
            Request.addProperty("state", set_state);
            Request.addProperty("zip", set_zip);
            Request.addProperty("numberOfPeople", set_people);
            Request.addProperty("pricePerPerson", set_price);
            
            Request.addProperty("typeOfSite", site);
            Request.addProperty("weddingCounty", county);
            Request.addProperty("eventDate", date);

            Request.addProperty("honeymoonCountry", country);
            
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);
            androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(soap_action, envelope);
            returnObject = (SoapObject)envelope.getResponse();  
        }
        catch(Exception e){
            e.printStackTrace();
        }
	    return returnObject;
	} 
	
	
	public SoapObject getDataDetailFromWebService(String method,String var){
	    SoapObject returnObject = null;
	    String soap_action = "http://tempuri.org/" + method;

        try{
            SoapObject Request = new SoapObject(NAMESPACE, method);
            Request.addProperty("countyid", var);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);
            androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(soap_action, envelope);
            returnObject = (SoapObject)envelope.getResponse();  
        }
        catch(Exception e){
            e.printStackTrace();
        }
	    return returnObject;
	} 
	
	public String getStringUrlForAccountImage(String method, int accountid){
		String Url = "";
		String soap_action = "http://tempuri.org/" + method;

        try{
            SoapObject Request = new SoapObject(NAMESPACE, method); 
            Request.addProperty("accountid", accountid);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);
            androidHttpTransport = new HttpTransportSE(URL);

            androidHttpTransport.call(soap_action, envelope);
            SoapPrimitive returnObject = (SoapPrimitive)envelope.getResponse();
            Url = returnObject.toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
	    return Url;
	} 
	
}
