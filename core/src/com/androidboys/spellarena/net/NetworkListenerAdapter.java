package com.androidboys.spellarena.net;

import java.util.List;

import com.androidboys.spellarena.net.NetworkInterface.NetworkListener;
import com.androidboys.spellarena.net.model.RoomModel;

// TODO: Auto-generated Javadoc
/**
 * The Class NetworkListenerAdapter.
 */
public class NetworkListenerAdapter implements NetworkListener{

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onConnected()
	 */
	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onConnectionFailure(java.lang.Exception)
	 */
	@Override
	public void onConnectionFailure(Exception e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onRoomListReceived(java.util.List)
	 */
	@Override
	public void onRoomListReceived(List<RoomModel> rooms) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onRoomListRequestFailed()
	 */
	@Override
	public void onRoomListRequestFailed() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onRoomCreated(com.androidboys.spellarena.net.model.RoomModel)
	 */
	@Override
	public void onRoomCreated(RoomModel room) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onCreateRoomFailed()
	 */
	@Override
	public void onCreateRoomFailed() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onRoomDeleted(java.lang.String)
	 */
	@Override
	public void onRoomDeleted(String roomId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onDeleteRoomFailed()
	 */
	@Override
	public void onDeleteRoomFailed() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onJoinRoomSuccess(java.lang.String)
	 */
	@Override
	public void onJoinRoomSuccess(String roomId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onJoinRoomFailed()
	 */
	@Override
	public void onJoinRoomFailed() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onMessageReceived(java.lang.String, java.lang.String)
	 */
	@Override
	public void onMessageReceived(String from, String message) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onPlayerJoinedRoom(com.androidboys.spellarena.net.model.RoomModel, java.lang.String)
	 */
	@Override
	public void onPlayerJoinedRoom(RoomModel room, String playerName) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onPlayerLeftRoom(com.androidboys.spellarena.net.model.RoomModel, java.lang.String)
	 */
	@Override
	public void onPlayerLeftRoom(RoomModel room, String playerName) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.androidboys.spellarena.net.NetworkInterface.NetworkListener#onRoomInfoReceived(java.lang.String[], java.lang.String)
	 */
	@Override
	public void onRoomInfoReceived(String[] players, String data) {
		// TODO Auto-generated method stub
		
	}

}
