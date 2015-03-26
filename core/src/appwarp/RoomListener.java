package appwarp;

import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;

public class RoomListener implements RoomRequestListener {

	private WarpController callback;

	public RoomListener(WarpController callback){
		this.callback = callback;
	}
	
	@Override
	public void onGetLiveRoomInfoDone(LiveRoomInfoEvent event) {
		if(event.getResult() == WarpResponseResultCode.SUCCESS){
			callback.onGetLiveRoomInfo(event.getJoinedUsers());
		} else {
			callback.onGetLiveRoomInfo(null);
		}
	}

	@Override
	public void onJoinRoomDone(RoomEvent event) {
		callback.onJoinRoomDone(event);

	}

	@Override
	public void onLeaveRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLockPropertiesDone(byte arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSubscribeRoomDone(RoomEvent event) {
		if(event.getResult() == WarpResponseResultCode.SUCCESS){
			callback.onRoomSubscribed(event.getData().getId());
		} else {
			callback.onRoomSubscribed(null);
		}
	}

	@Override
	public void onUnSubscribeRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnlockPropertiesDone(byte arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdatePropertyDone(LiveRoomInfoEvent arg0) {
		// TODO Auto-generated method stub

	}

}
