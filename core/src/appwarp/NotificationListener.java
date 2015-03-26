package appwarp;

import java.util.HashMap;

import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;

public class NotificationListener implements NotifyListener {

	private WarpController callback;

	public NotificationListener(WarpController callback) {
		this.callback = callback;
	}
	
	@Override
	public void onChatReceived(ChatEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameStarted(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameStopped(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMoveCompleted(MoveEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNextTurnRequest(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrivateChatReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrivateUpdateReceived(String arg0, byte[] arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomCreated(RoomData arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomDestroyed(RoomData arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdatePeersReceived(UpdateEvent event) {
		callback.onGameUpdateReceived(new String(event.getUpdate()));
	}

	@Override
	public void onUserChangeRoomProperty(RoomData roomData, String userName,
			HashMap<String, Object> properties, HashMap<String, String> lockProperties) {
		int code = Integer.parseInt(properties.get("result").toString());
		callback.onResultUpdateReceived(userName,code);

	}

	@Override
	public void onUserJoinedLobby(LobbyData arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserJoinedRoom(RoomData data, String username) {
		callback.onUserJoinedRoom(data.getId(), username);
	}

	@Override
	public void onUserLeftLobby(LobbyData arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserLeftRoom(RoomData data, String username) {
		callback.onUserLeftRoom(data.getId(), username);

	}

	@Override
	public void onUserPaused(String arg0, boolean arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserResumed(String arg0, boolean arg1, String arg2) {
		// TODO Auto-generated method stub

	}

}
