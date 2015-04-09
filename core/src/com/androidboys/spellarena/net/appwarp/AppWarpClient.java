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

public class AppWarpClient implements NetworkInterface{
	
	public final static String API_KEY = "b00bdd0998a53bb5916349f708de074644dca88a88aa731dda20d7362a17eda8";
	public final static String PVT_KEY = "85a61da6f2e21775d09b45cdc02543408add69719d80b195412170026751e597";
	
	public final static String GAME_STARTED_KEY = "GAME_STARTED";
	private static final String TAG = "AppWarpClient";
	
	private final String userName;
	private Set<NetworkListener> networkListeners = Collections.synchronizedSet(new HashSet<NetworkListener>());
	private WarpClient warpClient;
	
	private HashMap<String, Object> roomFilterForStartedGames = new HashMap<String, Object>() {{
		put(GAME_STARTED_KEY, true);
	}};
	private HashMap<String, Object> roomFilterForNotStartedGames = new HashMap<String, Object>() {{
		put(GAME_STARTED_KEY, false);
	}};

	public AppWarpClient(String userName){
		this.userName = userName;
		roomFilterForNotStartedGames.put(GAME_STARTED_KEY, false);
		roomFilterForStartedGames.put(GAME_STARTED_KEY, true);
	}
	
	public String getUserName(){
		return userName;
	}

	@Override
	public void addNetworkListener(NetworkListener listener) {
		networkListeners.add(listener);
	}

	@Override
	public void removeNetworkListener(NetworkListener listener) {
		networkListeners.remove(listener);
	}

	@Override
	public void clearNetworkListeners() {
		networkListeners.clear();
	}

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
		warpClient.connectWithUserName(userName);
	}

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

	@Override
	public void disconnect() {
		Gdx.app.log(TAG, "Disconnecting");
		warpClient.disconnect();		
	}

	@Override
	public void listRooms() {
		Gdx.app.log(TAG, "Listing rooms");
		warpClient.getRoomWithProperties(roomFilterForNotStartedGames);
	}

	@Override
	public void createRoom(String roomName) {
		warpClient.createRoom(roomName, userName, MAX_USERS, this.roomFilterForNotStartedGames);
	}

	@Override
	public void deleteRoom(String roomId) {
		warpClient.deleteRoom(roomId);
	}

	@Override
	public void joinRoom(String roomId) {
		warpClient.joinRoom(roomId);
	}

	@Override
	public void leaveRoom(String roomId) {
		warpClient.leaveRoom(roomId);
	}

	@Override
	public void sendMessage(String message) {
		Gdx.app.log(TAG, "Sending message "+message);
		warpClient.sendChat(message);
	}

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

	@Override
	public void startGame(String roomId) {
		warpClient.updateRoomProperties(roomId, roomFilterForStartedGames, new String[0]);
	}

	@Override
	public void getRoomInfo(String roomId) {
		warpClient.getLiveRoomInfo(roomId);
	}

	@Override
	public boolean isConnected() {
		return warpClient.getConnectionState() == ConnectionState.CONNECTED;
	}
	
	private RoomModel createRoomModelFromRoomData(RoomData roomData){
		RoomModel room = new RoomModel();
		room.setName(roomData.getName());
		room.setId(roomData.getId());
		room.setOwner(roomData.getRoomOwner());
		return room;
	}
}
