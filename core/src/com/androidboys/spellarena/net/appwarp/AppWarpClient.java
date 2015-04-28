package com.androidboys.spellarena.net.appwarp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.androidboys.spellarena.net.NetworkInterface;
import com.androidboys.spellarena.net.NetworkInterface.NetworkListener;
import com.androidboys.spellarena.net.model.RoomModel;
import com.androidboys.spellarena.session.UserSession;
import com.badlogic.gdx.Gdx;
import com.shephertz.app42.gaming.multiplayer.client.ConnectionState;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.sun.javafx.property.adapter.PropertyDescriptor.Listener;

// TODO: Auto-generated Javadoc
/**
 * The Class AppWarpClient.
 */
public class AppWarpClient implements NetworkInterface{
	
	/** The Constant API_KEY. */
	public final static String API_KEY = "b00bdd0998a53bb5916349f708de074644dca88a88aa731dda20d7362a17eda8";
	
	/** The Constant PVT_KEY. */
	public final static String PVT_KEY = "85a61da6f2e21775d09b45cdc02543408add69719d80b195412170026751e597";
	
	/** The Constant GAME_STARTED_KEY. */
	public final static String GAME_STARTED_KEY = "GAME_STARTED";
	
	/** The Constant TAG. */
	private static final String TAG = "AppWarpClient";
	
	/** The network listeners. */
	private Set<NetworkListener> networkListeners = Collections.synchronizedSet(new HashSet<NetworkListener>());
	
	/** The warp client. */
	private WarpClient warpClient;
	
	/** The room filter for started games. */
	private HashMap<String, Object> roomFilterForStartedGames = new HashMap<String, Object>() {{
		put(GAME_STARTED_KEY, true);
	}};
	
	/** The room filter for not started games. */
	private HashMap<String, Object> roomFilterForNotStartedGames = new HashMap<String, Object>() {{
		put(GAME_STARTED_KEY, false);
	}};

	/**
	 * Instantiates a new app warp client.
	 */
	public AppWarpClient(){

		roomFilterForNotStartedGames.put(GAME_STARTED_KEY, false);
		roomFilterForStartedGames.put(GAME_STARTED_KEY, true);
	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName(){
		return UserSession.getInstance().getUserName();
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#addNetworkListener(com.androidboys.spellarena.net.NetworkInterface.NetworkListener)
	 */
	@Override
	public void addNetworkListener(NetworkListener listener) {
		networkListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#removeNetworkListener(com.androidboys.spellarena.net.NetworkInterface.NetworkListener)
	 */
	@Override
	public void removeNetworkListener(NetworkListener listener) {
		networkListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#clearNetworkListeners()
	 */
	@Override
	public void clearNetworkListeners() {
		networkListeners.clear();
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#connect()
	 */
	@Override
	public void connect() {
		WarpClient.initialize(API_KEY, PVT_KEY);
		try{
			this.warpClient = WarpClient.getInstance();
		} catch (Exception e){
			for(NetworkListener listener : this.networkListeners){
				listener.onConnectionFailure(e);
			}
		}
		addConnectionRequestListener();
		addZoneRequestListener();
		addRoomRequestListener();
		addNotificationListener();
		warpClient.connectWithUserName(getUserName());
	}

	/**
	 * Adds the notification listener.
	 */
	private void addNotificationListener() {
		warpClient.addNotificationListener(new NotificationListenerAdapter(){
			
			@Override
			public void onChatReceived(ChatEvent chatEvent){
				for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
					listener.onMessageReceived(chatEvent.getSender(), chatEvent.getMessage());
				}
			}
			
			@Override
			public void onUserJoinedRoom(RoomData roomData, String user){
				for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
					listener.onPlayerJoinedRoom(createRoomModelFromRoomData(roomData), user);
				}
			}
		
			@Override
			public void onUserLeftRoom(RoomData roomData, String user){
				for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
					listener.onPlayerLeftRoom(createRoomModelFromRoomData(roomData), user);
				}
			}
			
			@Override
			public void onPrivateChatReceived(String from, String message){
				for (NetworkListener listener : networkListeners.toArray(new NetworkListener[0])) {
                    listener.onMessageReceived(from, message);
                }
			}
			
		});
	}

	/**
	 * Adds the room request listener.
	 */
	private void addRoomRequestListener() {
		warpClient.addRoomRequestListener(new RoomListenerAdapter(){
			
			@Override
			public void onJoinRoomDone(RoomEvent roomEvent){
				if(roomEvent.getResult() == WarpResponseResultCode.SUCCESS){
					warpClient.subscribeRoom(roomEvent.getData().getId());
					Gdx.app.log(TAG, "Joined room: "+roomEvent.getData().getId());
					for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
						listener.onJoinRoomSuccess(roomEvent.getData().getId());
					}
				} else {
					for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
						listener.onJoinRoomFailed();
					}
				}
			}
			
			@Override
			public void onGetLiveRoomInfoDone(LiveRoomInfoEvent liveRoomInfoEvent){
				if (liveRoomInfoEvent.getResult() == WarpResponseResultCode.SUCCESS){
					for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
						listener.onRoomInfoReceived(liveRoomInfoEvent.getJoinedUsers(), 
								liveRoomInfoEvent.getCustomData());
					}
				} else {
					Gdx.app.log(TAG, "getLiveRoomInfo failed");
				}
			}
			
		});
	}

	/**
	 * Adds the zone request listener.
	 */
	private void addZoneRequestListener() {
		warpClient.addZoneRequestListener(new ZoneListenerAdapter(){
			
			@Override
			public void onGetMatchedRoomsDone(MatchedRoomsEvent matchedRoomsEvent){
				if(matchedRoomsEvent.getResult() == WarpResponseResultCode.SUCCESS){
					RoomData[] roomsData = matchedRoomsEvent.getRoomsData();
					List<RoomModel> rooms = new ArrayList<RoomModel>();
					if(roomsData != null){
						for(RoomData roomData: roomsData){
							RoomModel room = createRoomModelFromRoomData(roomData);
							rooms.add(room);
						}
					}
					for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
						listener.onRoomListReceived(rooms);
					}
				} else {
					for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
						listener.onRoomListRequestFailed();
					}
				}
			}
			
			@Override
			public void onCreateRoomDone(RoomEvent roomEvent){
				if (roomEvent.getResult() == WarpResponseResultCode.SUCCESS){
					RoomModel roomModel = createRoomModelFromRoomData(roomEvent.getData());
					Gdx.app.log(TAG, "Room created: "+roomModel);
					for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
						listener.onRoomCreated(roomModel);
					}
				} else {
					for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
						listener.onCreateRoomFailed();
					}
				}
			}
			
			@Override
			public void onDeleteRoomDone(RoomEvent roomEvent){
				if(roomEvent.getResult() == WarpResponseResultCode.SUCCESS){
					for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
						listener.onRoomDeleted(roomEvent.getData().getId());
					}
				} else {
					for(NetworkListener listener : networkListeners.toArray(new NetworkListener[0])){
						listener.onDeleteRoomFailed();
					}
				}
			}
		});
	}

	/**
	 * Adds the connection request listener.
	 */
	private void addConnectionRequestListener() {
		warpClient.addConnectionRequestListener(new ConnectionRequestListener() {
			
			@Override
			public void onInitUDPDone(byte b) {
			}
			
			@Override
			public void onDisconnectDone(ConnectEvent connectEvent) {
				for(NetworkListener listener: networkListeners.toArray(new NetworkListener[0])){
					listener.onDisconnected();
				}
				Gdx.app.log(TAG, "Disconnected");
			}
			
			@Override
			public void onConnectDone(ConnectEvent connectEvent) {
				if(connectEvent.getResult() == WarpResponseResultCode.SUCCESS
						|| connectEvent.getResult() == WarpResponseResultCode.BAD_REQUEST
						|| connectEvent.getResult() == WarpResponseResultCode.SUCCESS_RECOVERED){
					for(NetworkListener listener: networkListeners.toArray(new NetworkListener[0])){
						listener.onConnected();
					}
					Gdx.app.log(TAG, "Connection established");
				} else {
					for(NetworkListener listener: networkListeners.toArray(new NetworkListener[0])){
						listener.onDisconnected();
					}
					Gdx.app.log(TAG, "Connection failed to establish " + connectEvent.getResult());
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#disconnect()
	 */
	@Override
	public void disconnect() {
		Gdx.app.log(TAG, "Disconnecting");
		warpClient.disconnect();		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#listRooms()
	 */
	@Override
	public void listRooms() {
		Gdx.app.log(TAG, "Listing rooms");
		warpClient.getRoomWithProperties(roomFilterForNotStartedGames);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#createRoom(java.lang.String)
	 */
	@Override
	public void createRoom(String roomName) {
		warpClient.createRoom(roomName, getUserName(), MAX_USERS, this.roomFilterForNotStartedGames);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#deleteRoom(java.lang.String)
	 */
	@Override
	public void deleteRoom(String roomId) {
		warpClient.deleteRoom(roomId);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#joinRoom(java.lang.String)
	 */
	@Override
	public void joinRoom(String roomId) {
		warpClient.joinRoom(roomId);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#leaveRoom(java.lang.String)
	 */
	@Override
	public void leaveRoom(String roomId) {
		warpClient.leaveRoom(roomId);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String message) {
		Gdx.app.log(TAG, "Sending message "+message);
		warpClient.sendChat(message);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#sendMessageTo(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessageTo(final String destination, final String message) {
		Gdx.app.log(TAG, "Sending message "+message+" to "+destination);
		new Thread(){
			@Override
			public void run() {
				warpClient.sendPrivateChat(destination,message);
			}
		}.start();
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#startGame(java.lang.String)
	 */
	@Override
	public void startGame(String roomId) {
		warpClient.updateRoomProperties(roomId, roomFilterForStartedGames, new String[0]);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#getRoomInfo(java.lang.String)
	 */
	@Override
	public void getRoomInfo(String roomId) {
		warpClient.getLiveRoomInfo(roomId);
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return warpClient.getConnectionState() == ConnectionState.CONNECTED;
	}
	
	/**
	 * Creates the room model from room data.
	 *
	 * @param roomData the room data
	 * @return the room model
	 */
	private RoomModel createRoomModelFromRoomData(RoomData roomData){
		RoomModel room = new RoomModel();
		room.setName(roomData.getName());
		room.setId(roomData.getId());
		room.setOwner(roomData.getRoomOwner());
		return room;
	}
}
