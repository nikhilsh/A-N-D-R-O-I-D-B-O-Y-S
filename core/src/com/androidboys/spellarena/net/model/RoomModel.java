package com.androidboys.spellarena.net.model;

public class RoomModel {
	
	private String id;
	private String name;
	private String owner;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@Override 
	public String toString(){
		return "RoomModel{" + 
				"id = "+ id + 
				"name = " + name +
				"owner = " + owner + "}";
	}
	
	@Override
	public int hashCode(){
		return id != null ? id.hashCode() : 0;
	}
}
