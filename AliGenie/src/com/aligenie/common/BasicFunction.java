package com.aligenie.common;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


public class BasicFunction {
	
    public static boolean IS_DEBUG=false;
	public static String replaceSQL(String param){
		return param;
	}
	public static String bytesToHexStringWithSpace(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv+" ");
        }
        return stringBuilder.toString();
    }
    /**
     * Convert byte[] to hex string.�������ǿ��Խ�byteת����int��Ȼ������Integer.toHexString(int)��ת����16�����ַ�
     * @param src byte[] data
     * @return hex string
     */   
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv+"");
        }
        return stringBuilder.toString();
    }
    public static String byteToHexString(byte src){
    	int v = src & 0xFF;
    	String hv = Integer.toHexString(v);
    	if (hv.length() ==1)hv="0"+hv;
        return hv;
    }
    /**
     * Convert byte[] to hex string.�������ǿ��Խ�byteת����int��Ȼ������Integer.toHexString(int)��ת����16�����ַ�
     * @param src byte[] data
     * @return hex string
     */   
    public static String bytesIpToString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv =v+"";
            if(i<3){
            	stringBuilder.append(hv+".");
            }else{
            	stringBuilder.append(hv+"");
            }
        }
        return stringBuilder.toString();
    }
    
    public static String intipToHexString(int ip){
    	byte[] temp=new byte[4];
    	temp[0]=(byte) (ip & 0xff);
    	temp[1]=(byte)((ip>>8)&0xff);
    	temp[2]=(byte)((ip>>16)&0xff);
    	temp[3]=(byte)((ip>>24)&0xff);
    	return bytesToHexString(temp);
    }
    public static String stringIpToHexString(String ip){
    	byte[] temp=new byte[4];
    	String[] tempstr=ip.split("\\.");
    	temp[0]=(byte) Integer.parseInt(tempstr[0]);
    	temp[1]=(byte) Integer.parseInt(tempstr[1]);
    	temp[2]=(byte) Integer.parseInt(tempstr[2]);
    	temp[3]=(byte) Integer.parseInt(tempstr[3]);
    	return bytesToHexString(temp);
    }
    public static String intIpToString(int ipaddress){
    	return (ipaddress & 0xFF ) + "." + ((ipaddress >> 8 ) & 0xFF) + "." +  ((ipaddress >> 16 ) & 0xFF) + "." + ( ipaddress >> 24 & 0xFF) ;
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        String hexs[];
        if(hexString.indexOf(" ")>=0){
        	hexs=hexString.split(" ");
        }else{
        	hexs=new String[hexString.length()/2];
        	for(int i=0;i<hexs.length;i++){
        		hexs[i]=hexString.substring(i*2, i*2+2);
        	}
        }
        
        int length = hexs.length;
        
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
     	   char[] hexChars = hexs[i].toCharArray();
            d[i] = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
        }
        return d;
    }
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
     private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
     
     public static byte hexStringToSingleByte(String hexString){
  	   char[] hexChars = hexString.toCharArray();
  	   return (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
     }
     public static String Unicode2GBK(String dataStr) {
		    int index = 0;
		    StringBuffer buffer = new StringBuffer();
		    int li_len = dataStr.length();
		    while (index < li_len) {
		        if (index >= li_len - 1|| !"\\u".equals(dataStr.substring(index, index + 2))) {
		            buffer.append(dataStr.charAt(index));
		            index++;
		            continue;
		        }
		        String charStr = "";
		        charStr = dataStr.substring(index + 2, index + 6);
		        char letter = (char) Integer.parseInt(charStr, 16);
		        buffer.append(letter);
		        index += 6;
		    }
		    return buffer.toString();
		}
	public static boolean isInteger(String str){  
  	   for(int i=str.length();--i>=0;){  
 	      int chr=str.charAt(i);  
 	      if(chr<48 || chr>57)  
 	         return false;  
 	   }  
 	   return true;  
 	}
     public static String getParam(String paramsStr,String name,String defaultvalue){
    	 return getParam(paramsStr,name,defaultvalue,";");
     }
     public static String getParam(String paramsStr,String name,String defaultvalue,String splitchar){
    	 String[] items=paramsStr.split(splitchar);
    	 for(String item:items){
    		 if(item.length()>0 && item.contains("=")){
    			 if(item.startsWith(name + "=")){
    				 return item.replace(name + "=", "");
    			 }
    		 }
    	 }
    	 return defaultvalue;
     }
     /** 
      * ����������Ԫ�����򣬲����ա�����=����ֵ����ģʽ�á�&���ַ�ƴ�ӳ��ַ���
      * @param params ��Ҫ���򲢲����ַ�ƴ�ӵĲ�����
      * @return ƴ�Ӻ��ַ���
      */
     public static String createLinkString(Map<String, String> params) {

         List<String> keys = new ArrayList<String>(params.keySet());
         Collections.sort(keys);

         String prestr = "";

         for (int i = 0; i < keys.size(); i++) {
             String key = keys.get(i);
             String value = params.get(key);

             if (i == keys.size() - 1) {//ƴ��ʱ�����������һ��&�ַ�
                 prestr = prestr + key + "=" + value;
             } else {
                 prestr = prestr + key + "=" + value + "&";
             }
         }

         return prestr;
     }
     public static String createNoncestr(int length){
    	 String chars="abcdefghijklmnopqrstuvwxyz0123456789";
    	 StringBuilder sb=new StringBuilder();
    	 for(int i=0;i<length;i++){
    		 sb.append(chars.charAt((int) Math.min(Math.random()*length, length)));
    	 }
    	 return sb.toString();
     }
     public static Document json2XML(JSONObject json) throws ParserConfigurationException, JSONException, ClassNotFoundException{
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
        
    	 Document doc= dbBuilder.newDocument();
    	 Iterator it = json.keys();
    	 Element result=doc.createElement("Result");
    	 while (it.hasNext()) {  
    		 String key = (String) it.next(); 
    		 Object value = json.get(key);
    		 if(value.getClass()==Class.forName("net.sf.json.JSONObject")){
    			 Element node=json2XMLElement((JSONObject) value,doc,key);
    			 result.appendChild(node); 
    		 }else if(value.getClass()==Class.forName("net.sf.json.JSONArray")){
    			 Element node=doc.createElement(key);
    			 for(int i=0;i<((JSONArray)value).size();i++){
    				 JSONObject record=((JSONArray)value).getJSONObject(i);
    				 node.appendChild(json2XMLElement(record,doc,"record"));
    			 }
    			 result.appendChild(node);
    		 }else{
    			 Element node=doc.createElement(key);
    			 node.appendChild(doc.createTextNode(value.toString()));
    			 result.appendChild(node); 
    		 }
    	 }
    	 doc.appendChild(result);
    	 return doc;
     }
     private static Element json2XMLElement(JSONObject json,Document doc,String elementname) throws JSONException, ClassNotFoundException{
    	 Element root=doc.createElement(elementname);
    	 Iterator<String> it = json.keys();
    	 while (it.hasNext()) {  
    		 String key = (String) it.next(); 
    		 Object value = json.get(key);
    		 
    		 if(value.getClass()==Class.forName("net.sf.json.JSONObject")){
    			 Element node=json2XMLElement((JSONObject) value,doc,key);
    			 root.appendChild(node); 
    		 }else if(value.getClass()==Class.forName("net.sf.json.JSONArray")){
    			 Element node=doc.createElement(key);
    			 for(int i=0;i<((JSONArray)value).size();i++){
    				 JSONObject record=((JSONArray)value).getJSONObject(i);
    				 node.appendChild(json2XMLElement(record,doc,"record"));
    			 }
    			 root.appendChild(node);
    		 }else{
    			 Element node=doc.createElement(key);
    			 node.appendChild(doc.createTextNode((String) value));
    			 root.appendChild(node); 
    		 }
    	 }
    	 return root;
     }
     public static String json2XMLString(JSONObject json) throws ParserConfigurationException, JSONException, ClassNotFoundException{
    	 Document doc=json2XML(json);
    	 return toStringFromDoc(doc);
     }
     public static String json2ParamsString(JSONObject json) throws JSONException, ClassNotFoundException{
    	 StringBuilder sb=new StringBuilder();
    	 Iterator<String> it = json.keys();
    	 
    	 while (it.hasNext()) {  
    		 String key = (String) it.next(); 
    		 Object value = json.get(key);
    		 if(value.getClass()==Class.forName("net.sf.json.JSONArray")){
    			 
    			 //JSONObject record=(JSONObject) ((JSONArray)value).get(0);
    			 JSONArray table=(JSONArray) value;
    			 for(int i=0;i<table.size();i++){
    				 sb.append(key+"=[");
    				 JSONObject record=(JSONObject) table.get(i);
    				 Iterator<String> columnnames = record.keys();
    				 while (columnnames.hasNext()) {  
    					 String columnname = (String) columnnames.next();
    					 Object columnvalue=record.get(columnname);
    					 if(columnnames.hasNext()){
    						 sb.append(columnname + "="+columnvalue+",");
    					 }else{
    						 sb.append(columnname + "="+columnvalue);
    					 }
    				 }
    				 sb.append("];");
    			 }
    		 }else{
    			 sb.append(key + "=" + value+";");
    		 }
    		 
    	 }
    	 return sb.toString();
     }
     public static String jsonTable2ParamsString(JSONObject json) throws JSONException, ClassNotFoundException{
    	 StringBuilder sb=new StringBuilder();
    	 Iterator<String> it = json.keys();
    	 JSONArray columns=null;
    	 //JSONArray captions=null;
    	 JSONArray Records=null;
    	 while (it.hasNext()) {  
    		 String key = (String) it.next(); 
    		 Object value = json.get(key);
    		 if(key.equals("ColumnsLongStatus")){
    			 columns=(JSONArray)value;
    		 }else if(key.equals("Caption")){
    			 //captions=(JSONArray) ((JSONArray)value).get(0);
    		 }else if(key.equals("Record")){
    			 Records=(JSONArray)value;
    		 }else if(value.getClass()==Class.forName("net.sf.json.JSONArray")){
    			 JSONArray table=(JSONArray) ((JSONArray)value).get(0);
    			 for(int i=0;i<table.size();i++){
    				 sb.append(key+"=[");
    				 JSONObject record=table.getJSONObject(i);
    				 Iterator<String> columnnames = record.keys();
    				 while (columnnames.hasNext()) {  
    					 String columnname = (String) columnnames.next();
    					 Object columnvalue=record.get(columnname);
    					 if(columnnames.hasNext()){
    						 sb.append(columnname + "="+columnvalue+",");
    					 }else{
    						 sb.append(columnname + "="+columnvalue);
    					 }
    				 }
    				 sb.append("];");
    			 }
    		 }else{
    			 sb.append(key + "=" + value+";");
    		 }
    	 }
    	 boolean hasCode=false;
    	 if(columns!=null){
    		 if(Records.size()>0){
    			 JSONObject record=Records.getJSONObject(0);
    			 if(record.has("code")){
    				 hasCode=true;
    			 }
    		 }
    		 
    		 sb.append("Columns=[");
    		 if(hasCode){
    			 sb.append("code=75,roomType=75,");
    		 }else{
    			 sb.append("roomType=75,");
    		 }
    		 
    		 for(int i=0;i<columns.size();i++){
    			 JSONObject record=columns.getJSONObject(i);
    			 if(i<columns.size()-1){
					 sb.append(record.getString("columnName") + "="+record.getString("width")+",");
				 }else{
					 sb.append(record.getString("columnName") + "="+record.getString("width"));
				 }
    		 }
    		 sb.append("];");
    		 sb.append("Caption=[");
    		 if(hasCode){
    			 sb.append("code=�����,roomType=��������,");
    		 }else{
    			 sb.append("roomType=��������,");
    		 }
    		
    		 for(int i=0;i<columns.size();i++){
    			 JSONObject record=columns.getJSONObject(i);
    			 if(i<columns.size()-1){
					 sb.append(record.getString("columnName") + "="+record.getString("columnName")+",");
				 }else{
					 sb.append(record.getString("columnName") + "="+record.getString("columnName"));
				 }
    		 }
    		 sb.append("];");
    	 }
    	 if(Records!=null){
    		 for(int i=0;i<Records.size();i++){
    			 sb.append("Record=[");
    			 JSONObject record=Records.getJSONObject(i);
    			 if(hasCode){
    				 sb.append("code="+record.getString("code")+",");
    			 }
    			 sb.append("roomType="+record.getString("roomType")+",");
    			 for(int j=0;j<columns.size();j++){
    				 JSONObject column=columns.getJSONObject(j);
    				 if(j<columns.size()-1){
    					 sb.append(column.getString("columnName") + "="+record.getString(column.getString("columnName"))+",");
    				 }else{
    					 sb.append(column.getString("columnName") + "="+record.getString(column.getString("columnName")));
    				 }
    			 }
    			 
    			 sb.append("];");
    		 }
    	 }
    	 return sb.toString();
     }
     /*  
      * ��dom�ļ�ת��Ϊxml�ַ���  
      */  
     private static String toStringFromDoc(Document document) {  
         String result = null;  
   
         if (document != null) {  
             StringWriter strWtr = new StringWriter();  
             StreamResult strResult = new StreamResult(strWtr);  
             TransformerFactory tfac = TransformerFactory.newInstance();  
             try {  
                 javax.xml.transform.Transformer t = tfac.newTransformer();  
                 t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  
                 t.setOutputProperty(OutputKeys.INDENT, "yes");  
                 t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,  
                 // text  
                 t.setOutputProperty(  
                         "{http://xml.apache.org/xslt}indent-amount", "4");  
                 t.transform(new DOMSource(document.getDocumentElement()),  
                         strResult);  
             } catch (Exception e) {  
                 System.err.println("XML.toString(Document): " + e);  
             }  
             result = strResult.getWriter().toString();  
             try {  
                 strWtr.close();  
             } catch (IOException e) {  
                 e.printStackTrace();  
             }  
         }  
   
         return result;  
     }
     
     public static byte[] long2Bytes(long num) {  
    	    byte[] byteNum = new byte[8];  
    	    for (int ix = 0; ix < 8; ++ix) {  
    	        int offset = 64 - (ix + 1) * 8;  
    	        byteNum[ix] = (byte) ((num >> offset) & 0xff);  
    	    }  
    	    return byteNum;  
    	}  
    	  
    	public static long bytes2Long(byte[] byteNum) {  
    	    long num = 0;  
    	    for (int ix = 0; ix < 8; ++ix) {  
    	        num <<= 8;  
    	        num |= (byteNum[ix] & 0xff);  
    	    }  
    	    return num;  
    	} 
    	/**  
         * ������������֮����������  
         * @param smdate ��С��ʱ�� 
         * @param bdate  �ϴ��ʱ�� 
         * @return ������� 
         * @throws ParseException  
         */    
        public static int daysBetween(Date smdate,Date bdate) throws ParseException    
        {    
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
            smdate=sdf.parse(sdf.format(smdate));  
            bdate=sdf.parse(sdf.format(bdate));  
            Calendar cal = Calendar.getInstance();    
            cal.setTime(smdate);    
            long time1 = cal.getTimeInMillis();                 
            cal.setTime(bdate);    
            long time2 = cal.getTimeInMillis();         
            long between_days=(time2-time1)/(1000*3600*24);  
                
           return Integer.parseInt(String.valueOf(between_days));           
        }
        
        public static String filter(String str) {  
            
            if(str.trim().isEmpty()){  
                return str;  
            }  
            String pattern="[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";  
            String reStr="";  
            Pattern emoji=Pattern.compile(pattern);  
            Matcher  emojiMatcher=emoji.matcher(str);  
            str=emojiMatcher.replaceAll(reStr);  
            return str;  
        }
}
