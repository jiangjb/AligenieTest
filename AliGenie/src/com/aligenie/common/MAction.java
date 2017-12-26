package com.aligenie.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class MAction {

	protected static MDAO dao;
	public MAction(){
		try {
			dao=new MDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected static void fillVOByRequest(HttpServletRequest request,VO vo){
		Enumeration forms=request.getParameterNames();
		while (forms.hasMoreElements()) {
			String key=forms.nextElement().toString();
			vo.setProperty(key, request.getParameter(key));		
		
		}
	}
	protected static JSONObject vo2json(VO vo){
		JSONObject json=new JSONObject();
		for(int i=0;i<vo.PropertySize();i++){
			try {
				json.put(vo.getPropertyName(i), vo.getProperty(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
	}
	
	protected String webGet(String api,List<NameValuePair> params){
		String url=api+"?temp=" + Math.random() ;
		
		//String uriAPI = "http://localhost:8080/IvorHotelServicesJ2EE/do?action=hotel_validate_mobile&temp="  ;
			for(NameValuePair item:params){
				try {
					url=url + "&" + item.getName() + "=" + URLEncoder.encode(item.getValue(),"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return getStringFromURL(url);
	}
	
	protected String getStringFromURL(String url){
		try{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse httpResponse = httpclient.execute(httpGet);

			if(httpResponse.getStatusLine().getStatusCode()==200){
			//閸欐牕鍤崶鐐茬安鐎涙ぞ瑕�
				String strResult=EntityUtils.toString(httpResponse.getEntity());
				return strResult;
				//final JSONObject jObject=new JSONObject(strResult.replace("jsonpCallback(", "").replace(")",""));
			}else{
				return "";
				//textView1.setText("Error Response"+httpResponse.getStatusLine().toString());
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
			return "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} //catch(JSONException e){e.printStackTrace();}
	}
	public static String postJSON(String strURL, String jsonstr) {
		try {
			URL url = new URL(strURL);// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗瀵拷
			connection.setRequestProperty("Accept", "application/json"); // 闁跨喐鏋婚幏鐑芥晸閻偅鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风柉骞愬В宥夋晸缂佺儑鎷�
			connection.setRequestProperty("Content-Type", "application/json"); // 闁跨喐鏋婚幏鐑芥晸閻偄鍤栭幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风柉骞愬В宥夋晸缂佺儑鎷�
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8"); // utf-8闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
			out.append(jsonstr);
			out.flush();
			out.close();
			// 闁跨喐鏋婚幏宄板絿闁跨喐鏋婚幏宄扮安
			int length = (int) connection.getContentLength();// 闁跨喐鏋婚幏宄板絿闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
			InputStream is = connection.getInputStream();
			if (length != -1) {
				byte[] data = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, data, destPos, readLen);
					destPos += readLen;
				}
				String result = new String(data, "UTF-8"); // utf-8闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
				System.out.println(result);
				return result;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error"; // 闁跨喓娈曠拋瑙勫闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撴潏鍐挎嫹
	}
	public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
	protected void bccCheck(byte[] data){
		byte result=0;
		for(int i=0;i<=6;i++){
			result^=data[i];
		}
		data[7]=result;
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
	
	/**
    *
    * @param theString
    * @return String
    */
   public static String unicodeToUtf8(String theString) {
       char aChar;
       int len = theString.length();
       StringBuffer outBuffer = new StringBuffer(len);
       for (int x = 0; x < len;) {
           aChar = theString.charAt(x++);
           if (aChar == '\\') {
               aChar = theString.charAt(x++);
               if (aChar == 'u') {
                   // Read the xxxx
                   int value = 0;
                   for (int i = 0; i < 4; i++) {
                       aChar = theString.charAt(x++);
                       switch (aChar) {
                       case '0':
                       case '1':
                       case '2':
                       case '3':
                       case '4':
                       case '5':
                       case '6':
                       case '7':
                       case '8':
                       case '9':
                           value = (value << 4) + aChar - '0';
                           break;
                       case 'a':
                       case 'b':
                       case 'c':
                       case 'd':
                       case 'e':
                       case 'f':
                           value = (value << 4) + 10 + aChar - 'a';
                           break;
                       case 'A':
                       case 'B':
                       case 'C':
                       case 'D':
                       case 'E':
                       case 'F':
                           value = (value << 4) + 10 + aChar - 'A';
                           break;
                       default:
                           throw new IllegalArgumentException(
                                   "Malformed   \\uxxxx   encoding.");
                       }
                   }
                   outBuffer.append((char) value);
               } else {
                   if (aChar == 't')
                       aChar = '\t';
                   else if (aChar == 'r')
                       aChar = '\r';
                   else if (aChar == 'n')
                       aChar = '\n';
                   else if (aChar == 'f')
                       aChar = '\f';
                   outBuffer.append(aChar);
               }
           } else
               outBuffer.append(aChar);
       }
       return outBuffer.toString();
   }

	public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();
 
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN) {
                sb.append(myBuffer[i]);
            } else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }
	public String utf82gbk(String utf) {
        String l_temp = utf8ToUnicode(utf);
        l_temp = Unicode2GBK(l_temp);
 
        return l_temp;
    }
	public String gbk2utf8(String gbk) {
        String l_temp = GBK2Unicode(gbk);
        l_temp = unicodeToUtf8(l_temp);
 
        return l_temp;
    }
	public static String GBK2Unicode(String str) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr1 = (char) str.charAt(i);
 
            if (!isNeedConvert(chr1)) {
                result.append(chr1);
                continue;
            }
 
            result.append("\\u" + Integer.toHexString((int) chr1));
        }
 
        return result.toString();
    }
	public static boolean isNeedConvert(char para) {
        return ((para & (0x00FF)) != para);
    }
}
