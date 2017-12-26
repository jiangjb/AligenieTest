package com.aligenie.common;

public class MListItem {

	private String _key="";
	private Object _value="";
	public MListItem(String key,Object value){
		this._key=key;
		this._value=value;
	}
	public String getKey() {
		return _key;
	}
	public void setKey(String value) {
		this._key = value;
	}
	public Object getValue() {
		return _value;
	}
	public void setValue(String value) {
		this._value = value;
	}
	
}
