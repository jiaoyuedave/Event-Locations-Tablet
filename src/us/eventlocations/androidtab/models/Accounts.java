package us.eventlocations.androidtab.models;

import java.io.Serializable;

public class Accounts implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3150118598347265942L;
	/*
	 Id=8112; 
	 Name=230 FIFTH; 
	 Contact=Jack Toomey; 
	 Comments=NA; 
	 Address1=230 Fifth Avenue; 
	 City=New York; 
	 State=NY; 
	 Url=www.230-FIFTH.com; 
	 Zip=10001; 
	 Map=1; 
	 ServiceId=0; 
	 ServiceName=NA; 
	*/
	private int _id;
	private String _name;
	private String _contact;
	private String _comments;
	private String _address1;
	private String _city;
	private String _state;
	private String _url;
	private String _zip;
	private int _map;
	private int _serviceId;
	private String _serviceName;
	
	public Accounts(){
		super();
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public int getId() {
		return _id;
	}

	public void setName(String _name) {
		this._name = _name;
	}

	public String getName() {
		return _name;
	}

	public void setContact(String _contact) {
		this._contact = _contact;
	}

	public String getContact() {
		return _contact;
	}

	public void setComments(String _comments) {
		this._comments = _comments;
	}

	public String getComments() {
		return _comments;
	}

	public void setAddress1(String _address1) {
		this._address1 = _address1;
	}

	public String getAddress1() {
		return _address1;
	}

	public void setCity(String _city) {
		this._city = _city;
	}

	public String getCity() {
		return _city;
	}

	public void setState(String _state) {
		this._state = _state;
	}

	public String getState() {
		return _state;
	}

	public void setUrl(String _url) {
		this._url = _url;
	}

	public String getUrl() {
		return _url;
	}

	public void setZip(String _zip) {
		this._zip = _zip;
	}

	public String getZip() {
		return _zip;
	}

	public void setMap(int _map) {
		this._map = _map;
	}

	public int getMap() {
		return _map;
	}

	public void setServiceId(int _serviceId) {
		this._serviceId = _serviceId;
	}

	public int getServiceId() {
		return _serviceId;
	}

	public void setServiceName(String _serviceName) {
		this._serviceName = _serviceName;
	}

	public String getServiceName() {
		return _serviceName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this._name;
	}
}
