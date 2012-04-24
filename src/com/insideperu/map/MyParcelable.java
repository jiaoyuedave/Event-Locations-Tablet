package com.insideperu.map;

import android.os.Parcel;
import android.os.Parcelable;

public final class MyParcelable implements Parcelable {
    private double longitudeStation;
    private double latitudeStation;
    private String nameAdressStation;
    private String addressRestaurant;

	private int logoStation;
    
    public MyParcelable(double _longitudeStation,double _latitudeStation,String _nameAdressStation ,int _logoStation,String _addressRestaurant)
    {
    	longitudeStation =_longitudeStation;
    	latitudeStation =_latitudeStation;
    	nameAdressStation =_nameAdressStation;
    	logoStation = _logoStation;
    	addressRestaurant = _addressRestaurant;
    }
   
    public String getAddressRestaurant() {
		return addressRestaurant;
	}
    
    public double getLongitudeStation() {
		return longitudeStation;
	}

	public double getLatitudeStation() {
		return latitudeStation;
	}

	public String getNameAdressStation() {
		return nameAdressStation;
	}

	public int getLogoStation() {
		return logoStation;
	}

	public void writeToParcel(Parcel out, int flags) {
        out.writeInt(logoStation);
        out.writeDouble(longitudeStation);
        out.writeDouble(latitudeStation);
        out.writeString(nameAdressStation);
        out.writeString(addressRestaurant);
        
        /// To "serialize" the HashMap
    /*    out.writeInt(contentSrc.size());
        Iterator i = contentSrc.entrySet().iterator();
        while (i.hasNext()){
            Map.Entry<String, String> e = (Map.Entry<String, String>) i.next();
            out.writeString(e.getKey());
            out.writeString(e.getValue());
        }*/
        
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>() 
    {
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };
    
    private MyParcelable(Parcel in) {
        readFromParcel(in);
    }
    
    private void readFromParcel(Parcel in) {
    	logoStation = in.readInt();
    	longitudeStation = in.readDouble();
    	latitudeStation = in.readDouble();
    	nameAdressStation = in.readString();
    	addressRestaurant = in.readString();
    	
        /// To "deserialize" the HashMap
        /*int hashSize = in.readInt();
        contentSrc = new HashMap<String, String>();
        for (int i = 0; i < hashSize; i++)
            contentSrc.put(in.readString(), in.readString());*/

    }
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}