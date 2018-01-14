package com.aligenie.vo;

import com.aligenie.action.resources.AliGenieResources;
import com.aligenie.action.resources.ResourcesIvorHome;
import com.aligenie.action.resources.ResourcesIvorHotel;
import com.aligenie.common.MConfig;

public class Authorize {
	public static final long EXPIRES_IN_MAX=17600000;
	public static final long CODE_TIME_MAX=120000;
	private AliGenieResources _resources;
	
	public String access_token="";
	public String refresh_token="";
	public long expires_in=0;
	public String code="";
	public long code_time_millies=0;
	public String project="";
	public MConfig config;
	public String client_id="";
	public String client_secret="";
	public AliGenieResources getResources(){
		return _resources;
	}
	
	public AliGenieResources createResources() throws Exception{
		switch(project){
			case "IVOR_HOME":
				_resources=new ResourcesIvorHome();
				return _resources;
			case "IVOR_HOTEL":
				_resources=new ResourcesIvorHotel();
				return _resources;
			default:
				throw new Exception("找不到对应的资源授权");
		}

	}
	public void timeTickSecond() {
		if(expires_in>0)expires_in--;
		if(this._resources!=null)this._resources.timeTickSecond();
	}
}
