package com.androidboys.spellarena.net;

import java.util.HashMap;

import com.androidboys.spellarena.net.appwarp.NotificationListenerAdapter;
import com.androidboys.spellarena.net.appwarp.RoomListenerAdapter;
import com.androidboys.spellarena.net.appwarp.ZoneListenerAdapter;
import com.badlogic.gdx.Gdx;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;

/**
 * The Class WarpController.
 */
public class WarpController {

	/** The instance. */
	private static WarpController instance;

	/** The warp client. */
	private WarpClient warpClient;

	/** The Constant API_KEY. */
	private final static String API_KEY = "b00bdd0998a53bb5916349f708de074644dca88a88aa731dda20d7362a17eda8";
	
	/** The Constant PVT_KEY. */
	private final static String PVT_KEY = "85a61da6f2e21775d09b45cdc02543408add69719d80b195412170026751e597";
	
	/** The local user. */
	private String localUser;
	
	/** The is connected. */
	private boolean isConnected = false;


	/** The room id. */
	private String roomId;

	/** The show log. */
	private boolean showLog = false;

	/** The warp listener. */
	private WarpListener warpListener;

	/** The is udp enabled. */
	private boolean isUDPEnabled;
	
	/** The state. */
	private int STATE;
	
	/** The Constant WAITING. */
	public static final int WAITING = 1;
	
	/** The Constant STARTED. */
	public static final int STARTED = 2;
	
	/** The Constant COMPLETED. */
	public static final int COMPLETED = 3;
	
	/** The Constant FINISHED. */
	public static final int FINISHED = 4;
	
	/** The Constant GAME_WIN. */
	public static final int GAME_WIN = 5;
	
	/** The Constant GAME_LOSE. */
	public static final int GAME_LOSE = 6;
	
	/** The Constant ENEMY_LEFT. */
	public static final int ENEMY_LEFT = 7;
	
	/**
	 * Instantiates a new warp controller.
	 */
	private WarpController() {
		initAppWarp();
		warpClient.addConnectionRequestListener(new ConnectionListener(this));
		warpClient.addChatRequestListener(new ChatListener(this));
	}
	
	/**
	 * Gets the single instance of WarpController.
	 *
	 * @return single instance of WarpController
	 */
	public static WarpController getInstance(){
		if(instance == null){
			instance = new WarpController();
		}
		return instance;
	}

	/**
	 * Inits the app warp.
	 */
	private void initAppWarp() {
		try{
			WarpClient.initialize(API_KEY, PVT_KEY);
			warpClient = WarpClient.getInstance();
		} catch (Exception e){
			e.printStackTrace();
		}	
	}
	
	/**
	 * Start app.
	 *
	 * @param localUser the local user
	 */
	public void startApp(String localUser){
		log("Connecting to Appwarp");
		this.localUser = localUser;
		warpClient.connectWithUserName(localUser);
	}
	
	/**
	 * Sets the listener.
	 *
	 * @param listener the new listener
	 */
	public void setListener(WarpListener listener){
		this.warpListener = listener;
	}

	/**
	 * Stop app.
	 */
	public void stopApp(){
		if(isConnected){
			warpClient.unsubscribeRoom(roomId);
			warpClient.leaveRoom(roomId);
		}
		warpClient.disconnect();
	}
	
	/**
	 * On connect done.
	 *
	 * @param status the status
	 */
	public void onConnectDone(boolean status) {
		log("onConnectDone: "+status);
		if(status){
			warpClient.initUDP();
			warpClient.joinRoomInRange(1, 1, false);
		} else {
			isConnected = false;
			handleError();
		}
	}
	
	/**
	 * Send game update.
	 *
	 * @param msg the msg
	 */
	@Deprecated
	public void sendGameUpdate(String msg){
		if(isConnected){
			if(isUDPEnabled){
				warpClient.sendUDPUpdatePeers((localUser+"#@"+msg).getBytes());
			} else {
				warpClient.sendUpdatePeers((localUser+"#@"+msg).getBytes());
			}
		}
	}
	
	/**
	 * Update result.
	 *
	 * @param code the code
	 * @param msg the msg
	 */
	public void updateResult(int code, String msg){
		if(isConnected){
			STATE = COMPLETED;
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put("result", code);
			warpClient.lockProperties(properties);
		}
	}
	
	/**
	 * On send chat done.
	 *
	 * @param status the status
	 */
	public void onSendChatDone(boolean status) {
		log("onSendChat	Done: "+status);
	}
	
	/**
	 * On room created.
	 *
	 * @param id the id
	 */
	public void onRoomCreated(String id) {
		if(id!=null){
			warpClient.joinRoom(id);
		} else {
			handleError();
		}
	}

	/**
	 * On get live room info.
	 *
	 * @param joinedUsers the joined users
	 */
	public void onGetLiveRoomInfo(String[] joinedUsers) {
		log("onGetLiveRoomInfo: "+joinedUsers.length);
		if(joinedUsers != null){
			if(joinedUsers.length == 2){
				startGame();
			} else {
				waitForOtherUser();
			}
		} else {
			warpClient.disconnect();
			handleError();
		}
	}

	/**
	 * On join room done.
	 *
	 * @param event the event
	 */
	public void onJoinRoomDone(RoomEvent event) {
		log("onJoinRoomDone: "+event.getResult());
		if(event.getResult() == WarpResponseResultCode.SUCCESS){
			this.roomId = event.getData().getId();
			warpClient.subscribeRoom(roomId);
		} else if (event.getResult() == WarpResponseResultCode.RESOURCE_NOT_FOUND){
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("result", "");
			warpClient.createRoom("spellArena", "client", 2, data);
		} else {
			warpClient.disconnect();
			handleError();
		}
	}
	
	/**
	 * On room subscribed.
	 *
	 * @param id the id
	 */
	public void onRoomSubscribed(String id) {
		log("onRoomSubscribed: "+id);
		if(id != null){
			isConnected = true;
			warpClient.getLiveRoomInfo(id);
		} else {
			warpClient.disconnect();
			handleError();
		}
		
	}
	
	/**
	 * On game update received.
	 *
	 * @param message the message
	 */
	public void onGameUpdateReceived(String message) {
		log("onGameUpdateReceived: "+message);
		String userName = message.substring(0, message.indexOf("#@"));
		String data = message.substring(message.indexOf("#@")+2, message.length());
		if(!localUser.equals(userName)){
			warpListener.onGameUpdateReceived(data);
		}
	}

	/**
	 * On user joined room.
	 *
	 * @param id the id
	 * @param username the username
	 */
	public void onUserJoinedRoom(String id, String username) {
		if(!localUser.equals(username)){
			startGame();
		}
		
	}

	/**
	 * On user left room.
	 *
	 * @param id the id
	 * @param username the username
	 */
	public void onUserLeftRoom(String id, String username) {
		log("onUserLeftRoom");
		if(STATE == STARTED && !localUser.equals(username)){
			warpListener.onGameFinished(ENEMY_LEFT,true);
		}
		
	}

	/**
	 * On result update received.
	 *
	 * @param userName the user name
	 * @param code the code
	 */
	public void onResultUpdateReceived(String userName, int code) {
		if(!localUser.equals(userName)){
			STATE = FINISHED;
			warpListener.onGameFinished(code, true);
		} else {
			warpListener.onGameFinished(code, false);
		}
	}
	
	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public int getState(){
		return STATE;
	}

	/**
	 * Wait for other user.
	 */
	private void waitForOtherUser() {
		STATE = WAITING;
		warpListener.onWaitingStarted("Waiting for other user");
	}

	/**
	 * Start game.
	 */
	private void startGame() {
		STATE = STARTED;
		warpListener.onGameStarted("Start the Game");		
	}
	
	/**
	 * Log.
	 *
	 * @param message the message
	 */
	private void log(String message){
		if(showLog){
			System.out.println(message);
		}
	}
	
	/**
	 * Handle leave.
	 */
	public void handleLeave(){
		if(isConnected){
			warpClient.unsubscribeRoom(roomId);
			warpClient.leaveRoom(roomId);
			if(STATE!=STARTED){
				warpClient.deleteRoom(roomId);
			}
			warpClient.disconnect();
		}
	}
	
	/**
	 * Handle error.
	 */
	private void handleError() {
		if(roomId != null && roomId.length()>0){
			warpClient.deleteRoom(roomId);
		}
		disconnect();
	}

	/**
	 * Disconnect.
	 */
	private void disconnect() {
		warpClient.removeConnectionRequestListener(new ConnectionListener(this));
		warpClient.removeChatRequestListener(new ChatListener(this));
	}

	




	

	
}
