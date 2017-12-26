package com.aligenie.devices;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AliGenieDevice {
	
	/**设备Id**/
	public String deviceId="";
	/**名称**/
	public String deviceName="";
	/**设备类型**/
	public String deviceType="";
	/**品牌**/
	public String brand="";
	/**型号**/
	public String model="";
	/**位置**/
	public String zone="";
	/**产品icon(https协议的url链接),像素最好160*160 以免在app显示模糊**/
	public String icon="";
	/**产品支持的标准属性列表**/
	public JSONArray properties;
	/**产品支持的操作(注：包括支持的查询操作) ,详情参照1.3.2和1.3.3章节**/
	public ArrayList<String> actions;
	/**产品扩展属性,为空返回null或者不返回该字段**/
	public JSONObject extensions;
}
