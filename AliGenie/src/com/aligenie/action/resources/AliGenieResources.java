package com.aligenie.action.resources;

import net.sf.json.JSONArray;

public abstract class AliGenieResources {
	protected int _login_time=0;
	public abstract boolean login(String username,String password)throws Exception;
	public abstract JSONArray getDevices()throws Exception; 
	public abstract void deviceAction(String deviceId,String deviceType,String action,String value);
	
	public abstract void timeTickSecond();
}
