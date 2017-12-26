package com.aligenie.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SMS {
	public static String sign="";
	public static String pwd="104AC2DB150097ADD050C8C4152A";
	/**
	 * @param args
	 * @throws IOException
	 */
	public static String sendMessage(String content,String mobile) throws IOException {
		//��������
		String sign="���ܾƵ�";
		
		// ����StringBuffer�������������ַ���
		StringBuffer sb = new StringBuffer("http://web.cr6868.com/asmx/smsservice.aspx?");

		// ��StringBuffer׷���û���
		sb.append("name=alucard_jiang");

		// ��StringBuffer׷�����루��½��ҳ�棬�ڹ�������--��������--�ӿ����룬��28λ�ģ�
		sb.append("&pwd=104AC2DB150097ADD050C8C4152A");

		// ��StringBuffer׷���ֻ�����
		sb.append("&mobile="+mobile);

		// ��StringBuffer׷����Ϣ����תURL��׼��
		sb.append("&content="+URLEncoder.encode(content,"UTF-8"));
		
		//׷�ӷ���ʱ�䣬��Ϊ�գ�Ϊ��Ϊ��ʱ����
		sb.append("&stime=");
		
		//��ǩ��
		sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
		
		//typeΪ�̶�ֵpt  extnoΪ��չ�룬����Ϊ���� ��Ϊ��
		sb.append("&type=pt&extno=");
		// ����url����
		//String temp = new String(sb.toString().getBytes("GBK"),"UTF-8");
		System.out.println("sb:"+sb.toString());
		URL url = new URL(sb.toString());

		// ��url����
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// ����url����ʽ ��get�� ���� ��post��
		connection.setRequestMethod("POST");

		// ����
		InputStream is =url.openStream();

		//ת������ֵ
		String returnStr = convertStreamToString(is);
		
		// ���ؽ��Ϊ��0��20140009090990,1���ύ�ɹ��� ���ͳɹ�   �����˵���ĵ�
		return returnStr;
		// ���ط��ͽ��
	}
	/**
	 * ת������ֵ����ΪUTF-8��ʽ.
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {    
        StringBuilder sb1 = new StringBuilder();    
        byte[] bytes = new byte[4096];  
        int size = 0;  
        
        try {    
        	while ((size = is.read(bytes)) > 0) {  
                String str = new String(bytes, 0, size, "UTF-8");  
                sb1.append(str);  
            }  
        } catch (IOException e) {    
            e.printStackTrace();    
        } finally {    
            try {    
                is.close();    
            } catch (IOException e) {    
               e.printStackTrace();    
            }    
        }    
        return sb1.toString();    
    }
}
