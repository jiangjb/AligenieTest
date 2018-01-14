package com.aligenie.action.resources;

import java.util.ArrayList;

import com.aligenie.common.HttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ResourcesIvorHome extends AliGenieResources {

	public static String DO_URL="http://home.ivor-electric.com/IvorHome/do";
	
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
	public void deviceAction(String deviceId,String deviceType,String action,String value) throws Exception {
		//curtain，	aircondition，	light，	television
		if(deviceType.equals("light")) {
			if(action.equals("TurnOn")) {
				lightClick(deviceId,"1");
			}
			if(action.equals("TurnOff")) {
				lightClick(deviceId,"0");
			}
		}
		if(deviceType.equals("curtain")) {
			if(action.equals("TurnOn")) {
				curtainClick(deviceId,"open");
			}
			if(action.equals("TurnOff")) {
				curtainClick(deviceId,"close");
			}
		}
		if(deviceType.equals("aircondition")) {
			if(action.equals("TurnOn")) {
				airClick(deviceId,"power=1");
			}
			if(action.equals("TurnOff")) {
				airClick(deviceId,"power=0");
			}
			if(action.equals("SetTemperature")) {
				airClick(deviceId,"tempset="+value);
			}
			if(action.equals("AdjustDownTemperature")) {
				airClick(deviceId,"tempset=m1");
			}
			if(action.equals("AdjustUpTemperature")) {
				airClick(deviceId,"tempset=p1");
			}
			if(action.equals("SetWindSpeed")) {
				//auto low medium high
				// 0--关机 1--慢 2--中 3--快 4--自动
				int val=4;
				if(value.equals("auto"))val=4;
				if(value.equals("low"))val=1;
				if(value.equals("medium"))val=2;
				if(value.equals("high"))val=3;
				airClick(deviceId,"windset="+val);
			}
			if(action.equals("AdjustUpWindSpeed")) {
				airClick(deviceId,"windset=p1");
			}
			if(action.equals("AdjustDownWindSpeed")) {
				airClick(deviceId,"windset=m1");
			}
			if(action.equals("SetMode")) {
				//auto cold heat ventilate airsupply
				//0--送风 1--制冷 2--制暖 3--自动
				int val=4;
				if(value.equals("auto"))val=3;
				if(value.equals("cold"))val=1;
				if(value.equals("heat"))val=2;
				if(value.equals("ventilate"))val=0;
				if(value.equals("airsupply"))val=0;
				airClick(deviceId,"mode="+val);
			}
		}

		
	}
	private void lightClick(String deviceId,String action) throws Exception {
		String url=DO_URL+"?action=LIGHT_CLICK&sessionId=" + _sessionId + "&lightId=" + deviceId + "&power=" + action +"&temp=" + Math.random();
		String result=HttpUtil.executeGet(url);
		checkResult(result,url);
	}
	private void curtainClick(String deviceId,String action) throws Exception {
		String url=DO_URL+"?action=CURTAIN_CLICK&sessionId=" + _sessionId + "&curtainId=" + deviceId + "&commond=" + action +"&temp=" + Math.random();
		String result=HttpUtil.executeGet(url);
		checkResult(result,url);
	}
	private void airClick(String deviceId,String action) throws Exception{
		String url=DO_URL+"?action=AIR_CLICK&sessionId=" + _sessionId + "&airId=" + deviceId + "&" + action +"&temp=" + Math.random();
		String result=HttpUtil.executeGet(url);
		checkResult(result,url);
	}
	
	private void checkResult(String result,String url) throws Exception{
		JSONObject json=JSONObject.fromObject(result);
		String errorMessage=json.getString("errorMessage");
		if(errorMessage.trim().length()>0){
			System.out.println(result);
			this.login(this._username, this._password);
			String newresult=HttpUtil.executeGet(url);
			System.out.println("new result:" + newresult);
		}
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
					
					JSONArray curtains=room.getJSONArray("ListCurtain");
					for(int k=0;k<curtains.size();k++) {
						JSONObject curtain=curtains.getJSONObject(k);
						JSONObject device=createDeviceByCurtain(curtain,room.getString("name"));
						_devices.add(device);
					}
					
					JSONArray airs=room.getJSONArray("ListAir");
					for(int k=0;k<airs.size();k++) {
						JSONObject air=airs.getJSONObject(k);
						JSONObject device=createDeviceByAir(air,room.getString("name"));
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
	private JSONObject createDeviceByCurtain(JSONObject light,String room) {
		JSONObject device=new JSONObject();
		device.put("deviceId", light.getString("id"));
		device.put("deviceName", light.getString("name"));
		device.put("deviceType", "curtain");
		device.put("zone", room);
		device.put("brand", "世捷智能技术");
		device.put("model", "Ivor T3000");
		device.put("icon", "http://home.ivor-electric.com/IvorHome/images/icon/2c.png");
		
		JSONArray properties=new JSONArray();
		JSONObject color=new JSONObject();
		color.put("color", "Yellow");
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
	private JSONObject createDeviceByAir(JSONObject light,String room) {
		JSONObject device=new JSONObject();
		device.put("deviceId", light.getString("id"));
		device.put("deviceName", light.getString("name"));
		device.put("deviceType", "aircondition");
		device.put("zone", room);
		device.put("brand", "世捷智能技术");
		device.put("model", "Ivor T3000");
		device.put("icon", "http://home.ivor-electric.com/IvorHome/images/icon/3c.png");
		
		JSONArray properties=new JSONArray();
		JSONObject color=new JSONObject();
		color.put("color", "Yellow");
		properties.add(color);
		device.put("properties",properties);
		
		ArrayList<String> actions=new ArrayList<String>();
		//SetTemperature,AdjustUpTemperature,AdjustDownTemperature,SetWindSpeed,AdjustUpWindSpeed,AdjustDownWindSpeed,SetMode
		actions.add("TurnOn");
		actions.add("TurnOff");
		actions.add("SetTemperature");
		actions.add("AdjustUpTemperature");
		actions.add("AdjustDownTemperature");
		actions.add("SetWindSpeed");
		actions.add("AdjustUpWindSpeed");
		actions.add("AdjustDownWindSpeed");
		actions.add("SetMode");
		actions.add("Query");
		device.put("actions", actions);
		
		JSONObject extensions=new JSONObject();
		extensions.put("extension1", "");
		device.put("extensions", extensions);
		return device;
	}
	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return this._username;
	}
	@Override
	public String getPwd() {
		// TODO Auto-generated method stub
		return this._password;
	}
}	
