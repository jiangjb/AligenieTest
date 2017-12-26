package com.aligenie.common;

public class MSession {
	public String username="";
	public String province="";
	public String openId="";
	public String WallPaperPath="D:/workspace/IvorHome/WebContent/images/bg.jpg";
	public String WallPaperServerId="";
	public String WallPaperCreateTime="";
	public String WallPaperLocalId="";
	public String WeixinRequestCode="";
	public int userId=0;
	private boolean _is_validate=false;
	public void setIsValidateLight(boolean validate){
		_is_validate=validate;
	}
	public boolean getIsValidateLight(){
		return _is_validate;
	}
	
	private int _time_limit=MConfig.SESSION_TIME_LIMIT;
	private String _session_id="";
	
	public MSession(String sessionid){
		_session_id=sessionid;
	}
	
	public String getSessionId(){
		//_time_limit=MConfig.SESSION_TIME_LIMIT;
		return _session_id;
	}
	public void actived(){
		_time_limit=MConfig.SESSION_TIME_LIMIT;
	}
	public void timeTick(){
		_time_limit--;
	}
	public int getTimeLimit(){
		return _time_limit;
	}
}
