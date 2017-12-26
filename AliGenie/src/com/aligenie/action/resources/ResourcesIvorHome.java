package com.aligenie.action.resources;

import java.util.ArrayList;

import com.aligenie.common.HttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ResourcesIvorHome extends AliGenieResources {

	public static String DO_URL="http://localhost:8080/IvorHome/do";
	
	private String _sessionId="";
	private String _userId="";
	private String _username="";
	private String _password="";
	
	private JSONArray _devices;
	
	@Override
	public boolean login(String username, String password) throws Exception {
		// TODO Auto-generated method stub
//		data.action="ADMIN_LOGIN";
//		data.username=username;
//		data.password=password;
//		data.temp=Math.random();
		String url=DO_URL+"?action=ADMIN_LOGIN&username="+username+"&password=" + password + "&temp=" + Math.random();
		String result=HttpUtil.executeGet(url);
		JSONObject json=JSONObject.fromObject(result);
		if(json.getString("status").equals("SUCCESS")){
			this._password=password;
			this._userId=json.getString("userId");
			this._username=username;
			this._sessionId=json.getString("sessionId");
			this._login_time=5400;
			return true;
		}else{
			throw new Exception(json.getString("ErrorMessage"));
		}
		
	}
	public void timeTickSecond() {
		if(this._login_time>0)this._login_time--;
		if(this._login_time<=0) {
			try {
				if(this._username.trim().length()>0)this.login(this._username, this._password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void deviceAction(String deviceId,String deviceType,String action) {

		if(deviceType.equals("light")) {
			if(action.equals("TurnOn")) {
				lightClick(deviceId,"1");
			}
			if(action.equals("TurnOff")) {
				lightClick(deviceId,"0");
			}
		}
		
	}
	private void lightClick(String deviceId,String action) {
//		data.action="LIGHT_CLICK";
//		data.sessionId=SESSION_ID;
//		data.lightId=$(this).attr("lightId");
//		data.power=power;
//		data.temp=Math.random();
		String url=DO_URL+"?action=LIGHT_CLICK&sessionId=" + _sessionId + "&lightId=" + deviceId + "&power=" + action +"&temp=" + Math.random();
		String result=HttpUtil.executeGet(url);
		
	}
	public JSONArray getDevices()throws Exception {
		String url=DO_URL+"?action=LOAD_GATEWAY&sessionId=" + _sessionId + "&temp=" + Math.random();
		String result=HttpUtil.executeGet(url);
		JSONObject json=JSONObject.fromObject(result);
		if(_devices==null)_devices=new JSONArray();
		if(json.getString("status").equals("SUCCESS")) {
			JSONArray gatewaylist=json.getJSONArray("Record");
			for(int i=0;i<gatewaylist.size();i++) {
				JSONObject gateway=gatewaylist.getJSONObject(i);
				JSONArray rooms=gateway.getJSONArray("ListRoom");
				for(int j=0;j<rooms.size();j++) {
					JSONObject room=rooms.getJSONObject(j);
					JSONArray lights=room.getJSONArray("ListLight");
					for(int k=0;k<lights.size();k++) {
						JSONObject light=lights.getJSONObject(k);
						JSONObject device=createDeviceByLight(light,room.getString("name"));
						_devices.add(device);

					}
				}
			}
			return _devices;
		}
		return null;
		//sessionId
	}
	
	private JSONObject createDeviceByLight(JSONObject light,String room) {
		JSONObject device=new JSONObject();
		device.put("deviceId", light.getString("id"));
		device.put("deviceName", light.getString("name"));
		device.put("deviceType", "light");
		device.put("zone", room);
		device.put("brand", "世捷智能技术");
		device.put("model", "Ivor T3000");
		device.put("icon", "https://www.gdyouliao.com/IvorHome/images/icon/1c.png");
		
		JSONArray properties=new JSONArray();
		JSONObject color=new JSONObject();
		color.put("color", "Red");
		properties.add(color);
		device.put("properties",properties);
		
		ArrayList<String> actions=new ArrayList<String>();
		actions.add("TurnOn");
		actions.add("TurnOff");
		actions.add("Query");
		device.put("actions", actions);
		
		JSONObject extensions=new JSONObject();
		extensions.put("extension1", "");
		device.put("extensions", extensions);
		return device;
	}

	
}	
