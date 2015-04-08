package com.androidboys.spellarena.session;

import com.androidboys.spellarena.net.model.RoomModel;
import com.badlogic.gdx.Gdx;

public class UserSession {

	private long userId;
	private String userName;
	private RoomModel room;
	
	public static final UserSession INSTANCE = new UserSession();
	private static final String TAG = "UserSession";
	
	public static UserSession getInstance(){
		return INSTANCE;
	}
	
	private UserSession(){
		this.userId = generateUserId();
		this.userName = generateUserName();
	}

	private String generateUserName() {
		return "haoqin";
		//return "Player "+Long.toString(userId);
	}

	private long generateUserId() {
		return (long) (Math.random()*1000000);
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		Gdx.app.log(TAG, "User name changed: "+userName);
		this.userName = userName;
	}

	public RoomModel getRoom() {
		return room;
	}

	public void setRoom(RoomModel room) {
		this.room = room;
	}
	
	public boolean isRoomOwner(){
		if(room != null){
			return userName.equals(room.getOwner());
		}
		return false;
	}
	
	public boolean isServer(){
		return isRoomOwner();
	}
}
