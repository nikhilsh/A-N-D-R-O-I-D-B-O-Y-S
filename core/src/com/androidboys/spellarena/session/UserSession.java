package com.androidboys.spellarena.session;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.net.model.RoomModel;
import com.badlogic.gdx.Gdx;

public class UserSession {

	private long userId;
	private String userName;
	private RoomModel room;
	private long randomSeed;
	private Random rng;
	
	public static final UserSession INSTANCE = new UserSession();
	private static final String TAG = "UserSession";
	
	public static UserSession getInstance(){
		return INSTANCE;
	}
	
	private UserSession(){
		if(AssetLoader.getUsername().equals(null)){
			this.setUserName(generateUserName());
		} else {
			this.userName = AssetLoader.getUsername();
		}
		randomSeed = System.nanoTime();
		rng = new Random(randomSeed);
	}

	public void setRandom(long seed){
		this.rng = new Random(seed);
	}
	
	private String generateUserName() {
//		return "haohaohao";
		return "Player "+Long.toString((userId%20));
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
		AssetLoader.setUsername(userName);
		Gdx.app.log(TAG, "new username: "+AssetLoader.getUsername());
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
