package com.aligenie.common;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.aligenie.action.resources.ResourcesIvorHome;
import com.mysql.jdbc.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MConfig {

	public static boolean IS_DEBUG=false;
	public static MConfig DEFAULT=null;
	/**数据库**/
	public String JDBC_URL="";
	public String JDBC_USERNAME="root";
	public String JDBC_PASSWORD="boat";
	public static int SESSION_TIME_LIMIT=7200;
	/**一般业务入口**/
	public String SERVER="";
	/**项目邦定域名**/
	public String DOMAIN="";
	public String PATH_LOCAL="";

	
	
	
	static {
		try {
			InputStream in;
			if(!IS_DEBUG){
				in = MConfig.class.getClassLoader()
						.getResourceAsStream("config.properties");
			}else{
				in = MConfig.class.getClassLoader()
						.getResourceAsStream("config_debug.properties");
			}
			
			Properties props = new Properties();
			props.load(in);
			DEFAULT=new MConfig();
			DEFAULT.JDBC_URL=props.getProperty("JDBC_URL", "");
			DEFAULT.JDBC_USERNAME = props.getProperty("JDBC_USERNAME", "");
			DEFAULT.JDBC_PASSWORD = props.getProperty("JDBC_PASSWORD", "");
			DEFAULT.SERVER=props.getProperty("SERVER", "");
			DEFAULT.DOMAIN=props.getProperty("DOMAIN", "");
			DEFAULT.PATH_LOCAL=props.getProperty("PATH_LOCAL", "");
			//ResourcesIvorHome.DO_URL=props.getProperty("SERVER_IVOR_DO", "");
			//ResourcesIvorHotel.DO_URL=props.getProperty("SERVER_IVOR_DO", "");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("load config error " + e.getMessage());
		}
	}
}
