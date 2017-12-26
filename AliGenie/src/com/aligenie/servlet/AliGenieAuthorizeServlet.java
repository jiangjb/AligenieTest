package com.aligenie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aligenie.action.AliGenieAction;

import net.sf.json.JSONObject;

public class AliGenieAuthorizeServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public void init(){
		try {
			super.init();
			//if(action4wx==null)action4wx=new IvorAction4Weixin();
			
			if(AliGenieAction.action==null)AliGenieAction.action=new AliGenieAction();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void init(ServletConfig config){
		try {
			super.init(config);
			if(AliGenieAction.action==null)AliGenieAction.action=new AliGenieAction();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		doPorcess(request,response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		doPorcess(request,response);
		
	}
	private void doPorcess(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		JSONObject result=new JSONObject();
		String action="";
		try{
			if(request.getParameter("response_type")!=null && request.getParameter("response_type").equals("code")){
				//获取code，并跳转
	//			String client_id=request.getParameter("client_id");
				String redirect_uri=request.getParameter("redirect_uri");
	//			String username=request.getParameter("Text_UserName");
	//			String password=request.getParameter("Text_Password");
				action="AUTHORIZE_CODE";
				submit(result,request,response,action);
				String code=result.getString("code");
				String url=java.net.URLDecoder.decode(redirect_uri,"utf-8");
				if(url.indexOf("?")>0){
					url=url +"&code=" +code;
				}else{
					url=url +"?code=" +code;
				}
				response.sendRedirect(url);
			}
		
			submit(result,request,response,action);
			out.println(result.toString());
		}catch(Exception e){
			out.println(e.getMessage());
		}
		
		//验证登陆，匹配用户名密码和client_id,产生临时code，拼上code后，转到redirect_uri
		//client_id=c1ebe466-1cdc-4bd3-ab69-77c3561b9dee&response_type=code&redirect_uri=http://aimeizi.net
		
		//String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6f2809ab4ea432cb&redirect_uri=http%3a%2f%2fhome.ivor-electric.com%2fIvorHome%2fwxapi&response_type=code&scope=snsapi_userinfo&state=WX_BIND";
		//response.sendRedirect(url);
	}
	
	private void submit(JSONObject result,HttpServletRequest request,HttpServletResponse response,String action) throws Exception{
		
		Map<String, String> sParaTemp = new HashMap<String, String>();
		Enumeration<String> paramNames = request.getParameterNames();  
	    while (paramNames.hasMoreElements()) {  
	    	String paramName = (String) paramNames.nextElement(); 
	    	String[] paramValues = request.getParameterValues(paramName);  
    		if (paramValues.length == 1) {  
    			String paramValue = paramValues[0];  
    			sParaTemp.put(paramName, paramValue);
    		} 
	    }
	    AliGenieAction.action.submit(action,sParaTemp,result);
		
	}
}
