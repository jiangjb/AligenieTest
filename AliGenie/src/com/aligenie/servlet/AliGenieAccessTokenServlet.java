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

public class AliGenieAccessTokenServlet extends HttpServlet {
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
			//https://XXXXX/token?grant_type=authorization_code&client_id=XXXXX&client_secret=XXXXXX&code=XXXXXXXX&redirect_uri=https%3A%2F%2Fopen.bot.tmall.com%2Foauth%2Fcallback
			//https://XXXXX/token?grant_type=refresh_token&client_id=XXXXX&client_secret=XXXXXX&refresh_token=XXXXXX
			if(request.getParameter("grant_type")!=null){
				if( request.getParameter("grant_type").equals("authorization_code")){
					action="AUTHORIZATION_CODE";
				}
				if( request.getParameter("grant_type").equals("refresh_token")){
					action="REFRESH_TOKEN";
				}
			}
			submit(result,request,response,action);
			out.println(result.toString());
		}catch(Exception e){
			out.println("hello");
		}
		
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
