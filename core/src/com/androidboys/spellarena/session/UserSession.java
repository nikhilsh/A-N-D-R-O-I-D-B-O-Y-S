package com.androidboys.spellarena.session;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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
//		return "haohaohao";
		return "haohao "+Long.toString((userId%6));
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
	
	public String getIP(){
		 String ip = null;
		    try {
		        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		        while (interfaces.hasMoreElements()) {
		            NetworkInterface iface = interfaces.nextElement();
		            // filters out 127.0.0.1 and inactive interfaces
		            if (iface.isLoopback() || !iface.isUp())
		                continue;

		            Enumeration<InetAddress> addresses = iface.getInetAddresses();
		            while(addresses.hasMoreElements()) {
		                InetAddress addr = addresses.nextElement();
		                ip = addr.getHostAddress();
		                System.out.println(iface.getDisplayName() + " " + ip);
		            }
		        }
		    } catch (SocketException e) {
		        throw new RuntimeException(e);
		    }
		    return ip;
	}
}
