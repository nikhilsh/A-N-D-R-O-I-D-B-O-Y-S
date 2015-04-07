package com.androidboys.spellarena.net;

import java.util.List;

import com.androidboys.spellarena.net.NetworkInterface.NetworkListener;
import com.androidboys.spellarena.net.model.RoomModel;

public class NetworkListenerAdapter implements NetworkListener{

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailure(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomListReceived(List<RoomModel> rooms) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomListRequestFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomCreated(RoomModel room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreateRoomFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomDeleted(String roomId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeleteRoomFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoinRoomSuccess(String roomId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoinRoomFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageReceived(String from, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerJoinedRoom(RoomModel room, String playerName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerLeftRoom(RoomModel room, String playerName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomInfoReceived(String[] players, String data) {
		// TODO Auto-generated method stub
		
	}

}
