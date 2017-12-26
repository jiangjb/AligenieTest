package com.aligenie.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.aligenie.action.resources.AliGenieResources;
import com.aligenie.common.BasicFunction;
import com.aligenie.common.MAction;
import com.aligenie.common.MConfig;
import com.aligenie.vo.Authorize;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class AliGenieAction extends MAction {
	
	public static AliGenieAction action=null;
	
	private ArrayList<Authorize> _list_authorize;
	public AliGenieAction(){
		_list_authorize=new ArrayList<Authorize>();
	}
	public void timeTickSecond() {
		if(_list_authorize!=null) {
			for(Authorize auth:_list_authorize) {
				auth.timeTickSecond();
				if(auth.code_time_millies>0 && (System.currentTimeMillis()-auth.code_time_millies)>Authorize.CODE_TIME_MAX && auth.expires_in==0){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					System.out.println(df.format(new Date()) + "remove auth:" + auth.client_id);// new Date()为获取当前系统时间
					_list_authorize.remove(auth);
					return;
				}
			}
		}
	}
	public void submit(JSONObject postbody,JSONObject result) throws Exception{
		if(!postbody.containsKey("header")) throw new Exception("缺少数据头");
		if(!postbody.containsKey("payload")) throw new Exception("缺少数据令牌");
		JSONObject header=postbody.getJSONObject("header");
		JSONObject payload=postbody.getJSONObject("payload");
		String namespace=header.getString("namespace");
		String name=header.getString("name");
		String messageId=header.getString("messageId");
		String accessToken=payload.getString("accessToken");
		switch(namespace) {
		case "AliGenie.Iot.Device.Discovery":
			deviceDiscovery(accessToken,result);
			break;
		case "AliGenie.Iot.Device.Control":
			deviceControl(name,payload,result);
			break;
		}
	}
	public void submit(String action,Map<String, String> vo,JSONObject r) throws Exception{
		switch(action){
		case "AUTHORIZE_CODE":
			authorizeCode(vo,r);
			break;
		case "AUTHORIZATION_CODE":
			authorizationCode(vo,r);
			break;
		case "REFRESH_TOKEN":
			refreshToken(vo,r);
			break;
		default:
				
		}
	}
	/**鏍规嵁鐢ㄦ埛鍚嶏紝瀵嗙爜锛宑lient_id鏉ヤ骇鐢焎ode**/
	private void authorizeCode(Map<String, String> vo,JSONObject r) throws Exception{
		String client_id=BasicFunction.replaceSQL(vo.get("client_id"));
		String redirect_uri=BasicFunction.replaceSQL(vo.get("redirect_uri"));
		String username=BasicFunction.replaceSQL(vo.get("Text_UserName"));
		String password=BasicFunction.replaceSQL(vo.get("Text_Password"));
		
		Authorize auth=new Authorize();
		auth.client_id=client_id;
		String sql="SELECT * FROM oauth2_client WHERE client_id='" + client_id +"'";
		JSONArray rs=dao.fillRS(sql);
		if(rs.size()>0){
			JSONObject row=rs.getJSONObject(0);
			auth.config=new MConfig();
			auth.config.JDBC_URL=row.getString("jdbcURL");
			auth.config.JDBC_USERNAME=row.getString("jdbcUser");
			auth.config.JDBC_PASSWORD=row.getString("jdbcPass");
			auth.project=row.getString("project");
			auth.client_secret=row.getString("client_secret");
			//楠岃瘉鐧婚檰
			AliGenieResources resource=auth.createResources();
			if(resource.login(username, password)){
				auth.code=BasicFunction.createNoncestr(16);
				auth.code_time_millies=System.currentTimeMillis();
				_list_authorize.add(auth);
				r.put("code", auth.code);
			}
			
		}
	}
	/**鏍规嵁code锛宑lient_id,client_secret鏉ヤ骇鐢焌ccessToken**/
	private void authorizationCode(Map<String, String> vo,JSONObject r) throws Exception{
		if(vo.containsKey("code")){
			String code=vo.get("code");
			Authorize auth=this.getAuthByCode(code);
			if(auth!=null){
				auth.access_token=BasicFunction.createNoncestr(32);
				auth.refresh_token=BasicFunction.createNoncestr(32);
				auth.expires_in=Authorize.EXPIRES_IN_MAX;
				r.put("access_token", auth.access_token);
				r.put("refresh_token", auth.refresh_token);
				r.put("expires_in",auth.expires_in);
			}
		}
		//AUTHORIZATION_CODE
//		{
//			  "access_token": "XXXXXX",
//			  "refresh_token": "XXXXXX"锛�
//			  "expires_in":17600000
//			  }
	}
	
	/**根据refresh_token来产生新的accessToken，并返回新的refresh_token**/
	private void refreshToken(Map<String, String> vo,JSONObject r) throws Exception{
		if(vo.containsKey("refresh_token")){
			String refresh_token=vo.get("refresh_token");
			Authorize auth=this.getAuthByRefreshToken(refresh_token);
			if(auth!=null){
				auth.access_token=BasicFunction.createNoncestr(32);
				auth.refresh_token=BasicFunction.createNoncestr(32);
				auth.expires_in=Authorize.EXPIRES_IN_MAX;
				r.put("access_token", auth.access_token);
				r.put("refresh_token", auth.refresh_token);
				r.put("expires_in",auth.expires_in);
			}
		}
	}
	private Authorize getAuthByCode(String code){
		for(Authorize auth:this._list_authorize){
			if(auth.code.equals(code) && (System.currentTimeMillis()-auth.code_time_millies)<Authorize.CODE_TIME_MAX){
				return auth;
			}
		}
		return null;
	}
	private Authorize getAuthByAccessToken(String accessToken){
		for(Authorize auth:this._list_authorize){
			if(auth.access_token.equals(accessToken)){
				return auth;
			}
		}
		return null;
	}
	private Authorize getAuthByRefreshToken(String refreshToken){
		for(Authorize auth:this._list_authorize){
			if(auth.refresh_token.equals(refreshToken)){
				return auth;
			}
		}
		return null;
	}
	//********************************************************************************************************************
	//分割线，天猫精灵对接
	//********************************************************************************************************************
	
	/**设备发现**/
	private void deviceDiscovery(String accessToken,JSONObject result) {
		JSONObject header=new JSONObject();
		JSONObject payload=new JSONObject();
		header.put("namespace", "AliGenie.Iot.Device.Discovery");
		header.put("name", "DiscoveryDevicesResponse");
		header.put("messageId", "1bd5d003-31b9-476f-ad03-71d471922820");
		header.put("payLoadVersion", 1);
		
		JSONArray devices=new JSONArray();
		//获取sessionId
		Authorize auth=this.getAuthByAccessToken(accessToken);
		if(auth==null)return;
		//根据sessionId获取房间和灯的列表
		try {
			devices=auth.getResources().getDevices();
			payload.put("devices",devices);
			
			result.put("header", header);
			result.put("payload", payload);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	
	private void deviceControl(String name,JSONObject payload_get,JSONObject result) {
		String deviceId=payload_get.getString("deviceId");
		String accessToken=payload_get.getString("accessToken");
		String deviceType=payload_get.getString("deviceType");
		JSONObject header=new JSONObject();
		JSONObject payload=new JSONObject();
		header.put("namespace", "AliGenie.Iot.Device.Control");
		header.put("name", name+"Response");
		header.put("messageId", "1bd5d003-31b9-476f-ad03-71d471922820");
		header.put("payLoadVersion", 1);
		
		payload.put("deviceId", deviceId);
		
		result.put("header", header);
		result.put("payload", payload);
		
		Authorize auth=this.getAuthByAccessToken(accessToken);
		if(auth!=null) {
			auth.getResources().deviceAction(deviceId, deviceType, name);
		}
//		｛
//		  "header":{
//		      "namespace":"AliGenie.Iot.Device.Control",
//		      "name":"TurnOn",
//		      "messageId":"1bd5d003-31b9-476f-ad03-71d471922820",
//		      "payLoadVersion":1
//		   },
//		   "payload":{
//		       "accessToken":"access token",
//		       "deviceId":"34234",
//		       "deviceType":"XXX",
//		       "attribute":"powerstate",
//		       "value":"on",
//		       "extensions":{
//		          "extension1":"",
//		          "extension2":""
//		      }
//		    }
//		 ｝
	}
	private void lightClick() {
		
	}
	private void powerOnTest(JSONObject result) {
//		｛
//		  "header":{
//		      "namespace":"AliGenie.Iot.Device.Control",
//		      "name":"TurnOnResponse",
//		      "messageId":"1bd5d003-31b9-476f-ad03-71d471922820",
//		      "payLoadVersion":1
//		   },
//		   "payload":{
//		      "deviceId":"34234"
//		    }
//		 ｝
		JSONObject header=new JSONObject();
		JSONObject payload=new JSONObject();
		header.put("namespace", "AliGenie.Iot.Device.Control");
		header.put("name", "TurnOnResponse");
		header.put("messageId", "1bd5d003-31b9-476f-ad03-71d471922820");
		header.put("payLoadVersion", 1);
		
		payload.put("deviceId", "34234");
		
		result.put("header", header);
		result.put("payload", payload);
		
//		IvorDeviceLight light=IvorManager.getLightById(21);
//		if(light==null){
//			return;
//		}
//		light.IsPower=true;
//		IvorGateWay gateway=light.Own.Own;
//		gateway.send(light.buildCommand());
	}
}
