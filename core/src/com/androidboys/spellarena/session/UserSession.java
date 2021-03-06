package com.androidboys.spellarena.session;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

import com.androidboys.spellarena.helper.AssetLoader;
import com.androidboys.spellarena.net.model.RoomModel;
import com.badlogic.gdx.Gdx;

// TODO: Auto-generated Javadoc
/**
 * The Class UserSession.
 *
 * @author Nikhil
 */
public class UserSession {

	/** The user id. */
	private long userId;
	
	/** The user name. */
	private String userName;
	
	/** The room. */
	private RoomModel room;
	
	/** The random seed. */
	private long randomSeed;
	
	/** The rng. */
	private Random rng;
	
	/** The Constant INSTANCE. */
	public static final UserSession INSTANCE = new UserSession();
	
	/** The Constant TAG. */
	private static final String TAG = "UserSession";
	
	/**
	 * Gets the single instance of UserSession.
	 *
	 * @return single instance of UserSession
	 */
	public static UserSession getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Instantiates a new user session.
	 */
	private UserSession(){
		if(AssetLoader.getUsername().equals("")){
			this.setUserName(generateUserName());
		} else {
			this.userName = AssetLoader.getUsername();
		}
		randomSeed = System.nanoTime();
		rng = new Random(randomSeed);
	}

	/**
	 * Sets the random.
	 *
	 * @param seed the new random
	 */
	public void setRandom(long seed){
		this.rng = new Random(seed);
	}
	
	/**
	 * Generate user name.
	 *
	 * @return the string
	 */
	private String generateUserName() {
//		return "haohaohao";
		return "Player "+Long.toString((userId%20));
	}

	/**
	 * Generate user id.
	 *
	 * @return the long
	 */
	private long generateUserId() {
		return (long) (Math.random()*1000000);
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		Gdx.app.log(TAG, "User name changed: "+userName);
		AssetLoader.setUsername(userName);
		Gdx.app.log(TAG, "new username: "+AssetLoader.getUsername());
		this.userName = userName;
	}

	/**
	 * Gets the room.
	 *
	 * @return the room
	 */
	public RoomModel getRoom() {
		return room;
	}

	/**
	 * Sets the room.
	 *
	 * @param room the new room
	 */
	public void setRoom(RoomModel room) {
		this.room = room;
	}
	
	/**
	 * Checks if is room owner.
	 *
	 * @return true, if is room owner
	 */
	public boolean isRoomOwner(){
		if(room != null){
			return userName.equals(room.getOwner());
		}
		return false;
	}
	
	/**
	 * Checks if is server.
	 *
	 * @return true, if is server
	 */
	public boolean isServer(){
		return isRoomOwner();
	}
	
	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
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
