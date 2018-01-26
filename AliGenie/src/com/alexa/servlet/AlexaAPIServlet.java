package com.alexa.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aligenie.action.AliGenieAction;

import net.sf.json.JSONObject;

public class AlexaAPIServlet extends HttpServlet {
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
		String postbody=getRequestPostStr(request);
		System.out.print(postbody);
		if(postbody.length()<=0)return;
		JSONObject json=JSONObject.fromObject(postbody);
		JSONObject result=new JSONObject();
		try {
			AliGenieAction.action.submit(json, result);
			PrintWriter out=response.getWriter();
			out.println(result.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**      
     * 描述:获取 post 请求的 byte[] 数组
     * <pre>
     * 举例：
     * </pre>
     * @param request
     * @return
     * @throws IOException      
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if(contentLength<0){
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength;) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }
    
    /**      
     * 描述:获取 post 请求内容
     * <pre>
     * 举例：
     * </pre>
     * @param request
     * @return
     * @throws IOException      
     */
    public static String getRequestPostStr(HttpServletRequest request) throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        if(buffer==null)return "";
        return new String(buffer, charEncoding);
    }
}
