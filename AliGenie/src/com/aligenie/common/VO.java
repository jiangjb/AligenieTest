package com.aligenie.common;

import java.util.ArrayList;

public class VO {

	public String RequestAction="NONE";
	public String TableName="";
	public int id=0;
	private ArrayList<MListItem> PropertyList;
	
	public VO(){
		PropertyList=new ArrayList<MListItem>();
	}
	public VO(String action){
		this.RequestAction=action;
		PropertyList=new ArrayList<MListItem>();
	}
	public Object getProperty(String key){
		for(MListItem item:this.PropertyList){
			if(item.getKey().equals(key)){
				return item.getValue();
			}
		}
		return null;
	}

	public Object getProperty(int index){
		if(index>=this.PropertyList.size())return null;
		return this.PropertyList.get(index).getValue();
	}

	public String getPropertyName(int index){
		if(index>=this.PropertyList.size())return null;
		return this.PropertyList.get(index).getKey();
	}
	public int PropertySize(){
		return this.PropertyList.size();
	}
	public void setProperty(String key, String value) {
		// TODO Auto-generated method stub
		this.PropertyList.add(new MListItem(key,value));
	}
}
