package com.androidboys.spellarena.mediators;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.gameworld.GameFactory;
import com.androidboys.spellarena.net.NetworkInterface;
import com.androidboys.spellarena.net.NetworkListenerAdapter;
import com.androidboys.spellarena.net.model.RoomModel;
import com.androidboys.spellarena.net.protocol.Command;
import com.androidboys.spellarena.net.protocol.CommandFactory;
import com.androidboys.spellarena.net.protocol.GameEndCommand;
import com.androidboys.spellarena.net.protocol.GameEndCommand.GameEndReason;
import com.androidboys.spellarena.servers.GameServer;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class GameScreenMediator extends Mediator{

	private static final String TAG = "GameScreenMediator";
	
	private GameServer gameServer;
	private NetworkInterface networkInterface;
	private NetworkListenerAdapter networkListenerAdapter;
	private GameScreen gameScreen;
	private CommandFactory commandFactory = new CommandFactory();
	private GameFactory.GameModel gameModel;
	private RoomModel room;
	private int level = 1;

	
	public GameScreenMediator(SpellArena game, NetworkInterface networkInterface) {
		super(game);
		this.gameModel = GameFactory.getGameModel(level);
		this.networkInterface = networkInterface;
		this.networkListenerAdapter = new NetworkListenerAdapter(){
			
			@Override
			public void onMessageReceived(String from, final String message) {
				Gdx.app.postRunnable( new Runnable() {
					
					@Override
					public void run() {
						Command command = commandFactory.createCommand(message);
						if(command == null){
							Gdx.app.log(TAG, "Waiting for split message");
							return;
						}
						switch(command.getCommand()){
							case Command.CREATE_GAME:
								handleCreateGameCommand(command);
								break;
							case Command.START_GAME:
								handleStartGameCommand(command);
						}
					}
				});
			}
			
			@Override
			public void onPlayerLeftRoom(RoomModel room, String playerName) {
				super.onPlayerLeftRoom(room, playerName);
				Gdx.app.log(TAG, "Player: "+playerName+" has left room "+room.getName());
				if(GameScreenMediator.this.getRoom().equals(room) && !playerName.equals(UserSession.getInstance().getUserName())){
					GameScreenMediator.this.onPlayerLeftRoom(playerName);
				}
			}
			
			@Override
			public void onPlayerJoinedRoom(RoomModel room, String playerName) {
				super.onPlayerJoinedRoom(room, playerName);
				if(GameScreenMediator.this.getRoom().equals(room) && !playerName.equals(UserSession.getInstance().getUserName())){
					GameScreenMediator.this.onPlayerJoinedRoom(playerName);
				}
			}
			
			@Override
			public void onRoomInfoReceived(String[] players, String data) {
				if(players != null){
					for(String player: players){
						GameScreenMediator.this.onPlayerJoinedRoom(player);
					}
				}
			}
			
			@Override
			public void onDisconnected() {
				gameScreen.onDisconnected();
			}
		};
		networkInterface.addNetworkListener(networkListenerAdapter);
	}

	private void handleCreateGameCommand(Command c){
		if(UserSession.getInstance().getUserName().equals(c.getFromUser())){
			return;
		}
		gameScreen.onCreateGame();
	}
	
	private void handleStartGameCommand(Command c){
		gameScreen.startGame();
	}
	
	@Override
	protected void onScreenShow() {
		super.onScreenShow();
		if(!UserSession.getInstance().isServer()) {
			networkInterface.getRoomInfo(UserSession.getInstance().getRoom().getId());
		}
	}
	
	private void onPlayerJoinedRoom(String playerName) {
		gameScreen.onPlayerJoinedRoom(playerName);		
	}

	private void onPlayerLeftRoom(String playerName) {
		if((playerName != null)&&playerName.equals(getRoom().getOwner())){
			onGameOwnerLeft();
		} else {
			gameScreen.removePlayer(playerName);
		}
	}

	private void onGameOwnerLeft() {
		GameEndCommand gameEndCommand = new GameEndCommand();
		gameEndCommand.setFromUser(UserSession.getInstance().getUserName());
		gameEndCommand.setReason(GameEndReason.OWNER_LEFT);
		networkInterface.sendMessage(gameEndCommand.serialize());
	}

	@Override
	public Screen createScreen() {
		this.screen = new GameScreen(game,this);
		this.gameScreen = (GameScreen) screen;
		return screen;
	}

	public NetworkListenerAdapter getNetworkListenerAdapter() {
		return networkListenerAdapter;
	}
	
	public RoomModel getRoom() {
		return room;
	}

	public void setRoom(RoomModel room) {
		this.room = room;
	}

	public void setGameServer(GameServer gameServer) {
		this.gameServer = gameServer;
	}

}
