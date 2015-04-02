package appwarp;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;

public class WarpController {

	private static WarpController instance;

	private WarpClient warpClient;

	private static String apiKey = "b00bdd0998a53bb5916349f708de074644dca88a88aa731dda20d7362a17eda8";
	private static String pvtKey = "85a61da6f2e21775d09b45cdc02543408add69719d80b195412170026751e597";

	private boolean isConnected = false;

	private String localUser;
	private String roomId;

	private boolean showLog = true;

	private WarpListener warpListener;

	private boolean isUDPEnabled;
	
	private int STATE;
	
	public static final int WAITING = 1;
	public static final int STARTED = 2;
	public static final int COMPLETED = 3;
	public static final int FINISHED = 4;
	
	public static final int GAME_WIN = 5;
	public static final int GAME_LOSE = 6;
	public static final int ENEMY_LEFT = 7;
	
	private WarpController() {
		initAppWarp();
		warpClient.addConnectionRequestListener(new ConnectionListener(this));
		warpClient.addChatRequestListener(new ChatListener(this));
		warpClient.addZoneRequestListener(new ZoneListener(this));
		warpClient.addRoomRequestListener(new RoomListener(this));
		warpClient.addNotificationListener(new NotificationListener(this));
	}
	
	public static WarpController getInstance(){
		if(instance == null){
			instance = new WarpController();
		}
		return instance;
	}

	private void initAppWarp() {
		try{
			WarpClient.initialize(apiKey, pvtKey);
			warpClient = WarpClient.getInstance();
		} catch (Exception e){
			e.printStackTrace();
		}	
	}
	
	public void startApp(String localUser){
		log("Connecting to Appwarp");
		this.localUser = localUser;
		warpClient.connectWithUserName(localUser);
	}
	
	public void setListener(WarpListener listener){
		this.warpListener = listener;
	}

	public void stopApp(){
		if(isConnected){
			warpClient.unsubscribeRoom(roomId);
			warpClient.leaveRoom(roomId);
		}
		warpClient.disconnect();
	}
	
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
	
	public void sendGameUpdate(String msg){
		if(isConnected){
			if(isUDPEnabled){
				warpClient.sendUDPUpdatePeers((localUser+"#@"+msg).getBytes());
			} else {
				warpClient.sendUpdatePeers((localUser+"#@"+msg).getBytes());
			}
		}
	}
	
	public void updateResult(int code, String msg){
		if(isConnected){
			STATE = COMPLETED;
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put("result", code);
			warpClient.lockProperties(properties);
		}
	}
	
	public void onSendChatDone(boolean status) {
		log("onSendChat	Done: "+status);
	}
	
	public void onRoomCreated(String id) {
		if(id!=null){
			warpClient.joinRoom(id);
		} else {
			handleError();
		}
	}

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
	
	public void onGameUpdateReceived(String message) {
		log("onGameUpdateReceived: "+message);
		String userName = message.substring(0, message.indexOf("#@"));
		String data = message.substring(message.indexOf("#@")+2, message.length());
		if(!localUser.equals(userName)){
			warpListener.onGameUpdateReceived(data);
		}
	}

	public void onUserJoinedRoom(String id, String username) {
		if(!localUser.equals(username)){
			startGame();
		}
		
	}

	public void onUserLeftRoom(String id, String username) {
		log("onUserLeftRoom");
		if(STATE == STARTED && !localUser.equals(username)){
			warpListener.onGameFinished(ENEMY_LEFT,true);
		}
		
	}

	public void onResultUpdateReceived(String userName, int code) {
		if(!localUser.equals(userName)){
			STATE = FINISHED;
			warpListener.onGameFinished(code, true);
		} else {
			warpListener.onGameFinished(code, false);
		}
	}
	
	public int getState(){
		return STATE;
	}

	private void waitForOtherUser() {
		STATE = WAITING;
		warpListener.onWaitingStarted("Waiting for other user");
	}

	private void startGame() {
		STATE = STARTED;
		warpListener.onGameStarted("Start the Game");		
	}
	
	private void log(String message){
		if(showLog){
			System.out.println(message);
		}
	}
	
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
	
	private void handleError() {
		if(roomId != null && roomId.length()>0){
			warpClient.deleteRoom(roomId);
		}
		disconnect();
	}

	private void disconnect() {
		warpClient.removeConnectionRequestListener(new ConnectionListener(this));
		warpClient.removeChatRequestListener(new ChatListener(this));
	}

}
