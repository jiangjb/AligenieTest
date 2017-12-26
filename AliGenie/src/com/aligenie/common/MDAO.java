package com.aligenie.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;





import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.mysql.jdbc.Statement;

public class MDAO {
	
	public MDAO() throws ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		/*
	     try{   
	    	 
	    	 Connection con = DriverManager.getConnection(JDBC_URL , JDBC_USERNAME , JDBC_PASSWORD ) ;
	    	 Statement stmt = (Statement) con.createStatement() ;
	    	 ResultSet rs = (ResultSet) stmt.executeQuery("SELECT * FROM t_user") ;   
	    	 while(rs.next()){   
	             String name = rs.getString("nickName") ;   
	             String pass = rs.getString("openId") ; 
	         }
	    	 con.close();
	     }catch(SQLException se){   
		    System.out.println("���ݿ�����ʧ�ܣ�");   
		    se.printStackTrace() ;   
	     }catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
	    	 e.printStackTrace();
	     }
	     */
	}
	public void beginTransaction(){
		
	}
	public void commit(){
		
	}
	public boolean execute(String sql){
		return execute(sql,MConfig.DEFAULT);
	}
	public boolean execute(String sql,MConfig config){
		
		try {
			Connection con = DriverManager.getConnection(config.JDBC_URL , config.JDBC_USERNAME , config.JDBC_PASSWORD );
			Statement stmt = (Statement) con.createStatement() ;
			boolean b=stmt.execute(sql);
			con.close();
			return b;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
   	 	
	}
	public int add(VO vo) throws Exception{
		return add(vo,MConfig.DEFAULT);
	}
	public int add(VO vo,MConfig config) throws Exception{
		int id=1;
		String sql="SELECT * FROM " + vo.TableName + " ORDER BY id DESC LIMIT 1";
		try {
			Connection con = DriverManager.getConnection(config.JDBC_URL , config.JDBC_USERNAME , config.JDBC_PASSWORD );
			Statement stmt = (Statement) con.createStatement() ;
			ResultSet rs = (ResultSet) stmt.executeQuery(sql) ;  
			ResultSetMetaData rsmd = rs.getMetaData() ;
			if(rs.next()){
				id=rs.getInt("id")+1;
			}
			//rs.moveToInsertRow();
			String list_column="";
			String list_value="";
			for(int i=1;i<=rsmd.getColumnCount();i++){
				String column_name=rsmd.getColumnName(i);
				int columntype=rsmd.getColumnType(i);
				if(column_name.equals("id")){
					list_column=list_column+"`"+column_name+"`"+",";
					list_value=list_value+id+",";
				}else{
					Object val=vo.getProperty(column_name);
					if(val!=null){
						list_column=list_column+"`"+column_name+"`"+",";
						if(columntype==(int)12||columntype==-1||columntype==93||columntype==91||columntype==-4){
							list_value=list_value+"'"+val+"',";
						}else{
							list_value=list_value+val+",";
						}
						//rs.updateObject(column_name, val);
					}
				}
			}
			//rs.insertRow();
			con.close();
			sql="INSERT INTO " + vo.TableName +"(" +list_column.substring(0, list_column.length()-1) + ") VALUES ("+list_value.substring(0, list_value.length()-1)+")";
			this.execute(sql);
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	public int update(VO vo) throws Exception{
		return update(vo,MConfig.DEFAULT);
	}
	public int update(VO vo,MConfig config) throws Exception{
		String sql="SELECT * FROM " + vo.TableName + " WHERE id=" + vo.id + " ORDER BY id DESC LIMIT 1";
		try {
			Connection con = DriverManager.getConnection(config.JDBC_URL , config.JDBC_USERNAME , config.JDBC_PASSWORD );
			Statement stmt = (Statement) con.createStatement() ;
			ResultSet rs = (ResultSet) stmt.executeQuery(sql) ;  
			ResultSetMetaData rsmd = rs.getMetaData() ;
			String list_column="";
			if(rs.next()){
				for(int i=1;i<=rsmd.getColumnCount();i++){
					String column_name=rsmd.getColumnName(i);
					int columntype=rsmd.getColumnType(i);
					Object val=vo.getProperty(column_name);
					if(val!=null){
						if(columntype==(int)12||columntype==-1||columntype==93||columntype==91){
							list_column=list_column+"`"+column_name+"`='"+val+"',";
						}else{
							list_column=list_column+"`"+column_name+"`="+val+",";
						}
					}
				}
				//rs.updateRow();
				con.close();
				sql="UPDATE " + vo.TableName +" SET " +list_column.substring(0, list_column.length()-1) + " WHERE id="+vo.id;
				this.execute(sql);
				return vo.id;
			}else{
				throw new Exception("�Ҳ���ָ��������");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	public JSONArray fillRS(String sql) throws Exception{
		return fillRS(sql,MConfig.DEFAULT);
	}
	public JSONArray fillRS(String sql,MConfig config) throws Exception{
		try {
			Connection con = DriverManager.getConnection(config.JDBC_URL , config.JDBC_USERNAME ,config.JDBC_PASSWORD );
			Statement stmt = (Statement) con.createStatement() ;
			ResultSet rs = (ResultSet) stmt.executeQuery(sql) ; 
			JSONArray records=rs2json(rs);
			con.close();
			return records;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	public JSONArray fillRS(String sql,JSONArray columns,MConfig config) throws Exception{
		try {
			Connection con = DriverManager.getConnection(config.JDBC_URL , config.JDBC_USERNAME ,config.JDBC_PASSWORD );
			Statement stmt = (Statement) con.createStatement() ;
			ResultSet rs = (ResultSet) stmt.executeQuery(sql) ; 
			JSONArray records=rs2json(rs,columns);
			con.close();
			return records;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	private String getColumnName(String column_name){

		return "";
	}
	public static JSONArray rs2json(ResultSet rs) throws SQLException, JSONException{
		ResultSetMetaData rsmd = rs.getMetaData();
		JSONArray rslist=new JSONArray();
		while(rs.next()){
			JSONObject row=new JSONObject();
			for(int i=1;i<=rsmd.getColumnCount();i++){
				//Boolean b=false;
				if(rs.getObject(i)!=null){
					row.put(rsmd.getColumnName(i), rs.getObject(i).toString());
				}else{
					row.put(rsmd.getColumnName(i), "");
				}
				
			}
			rslist.add(row);
		}
		return rslist;
	}
	//,JSONArray columns
	public static JSONArray rs2json(ResultSet rs,JSONArray columns) throws SQLException, JSONException{
		ResultSetMetaData rsmd = rs.getMetaData();
		JSONArray rslist=new JSONArray();
		while(rs.next()){
			boolean b=true;
			String datatype="";
			for(int i=1;i<=rsmd.getColumnCount();i++){
				for(int j=0;j<columns.size();j++){
					JSONObject column=columns.getJSONObject(j);
					if(column.getString("columnName").equals(rsmd.getColumnName(i))){
						datatype=column.getString("valueType");
						if(column.has("value")){
							String value=column.getString("value");
							if(column.getString("columnName").equals("id")){
								if(Integer.parseInt(rs.getObject(i).toString())!=Integer.parseInt(value)){
									b=false;
									break;
								}
							}else if(datatype.equals("System.String")){
								if(!rs.getObject(i).toString().contains(value)){
									b=false;
									break;
								}
							}else if(datatype.equals("System.Int32")){
								if(Integer.parseInt(rs.getObject(i).toString())!=Integer.parseInt(value)){
									b=false;
									break;
								}
							}else if(datatype.equals("System.Boolean")){
								boolean val;
								if(value.toLowerCase().equals("true")){
									val=true;
								}else if(value.equals("1")){
									val=true;
								}else{
									val=false;
								}
								if(Boolean.parseBoolean(rs.getObject(i).toString())!=val){
									b=false;
									break;
								}
							}else if(datatype.equals("System.DateTime")){
								if(Date.parse(rs.getObject(i).toString())!=Date.parse(value)){
									b=false;
									break;
								}
							}else if(datatype.equals("System.Decimal")){
								if(Float.parseFloat(rs.getObject(i).toString())!=Float.parseFloat(value)){
									b=false;
									break;
								}
							}
						}
					}
				}
			}
			if(b){
				JSONObject row=new JSONObject();
				for(int i=1;i<=rsmd.getColumnCount();i++){
					JSONObject column;
					for(int j=0;j<columns.size();j++){
						column=columns.getJSONObject(j);
						if(column.getString("columnName").equals(rsmd.getColumnName(i))){
							datatype=column.getString("valueType");
							break;
						}
					}
					if(rs.getObject(i)!=null){
						if(datatype.equals("System.Boolean") && (!rs.getObject(i).getClass().toString().equals("class java.lang.Boolean"))){
							row.put(rsmd.getColumnName(i), (Integer.parseInt(rs.getObject(i).toString())==1)?"true":"false");
						}else if(datatype.equals("System.Boolean")){
							row.put(rsmd.getColumnName(i), (boolean) (rs.getObject(i))?"true":"false");
						}else{
							row.put(rsmd.getColumnName(i), rs.getObject(i).toString());
						}
						
					}else{
						row.put(rsmd.getColumnName(i), "");
					}
					
				}
				rslist.add(row);
			}
			
		}
		return rslist;
	}
}
