package com.androidboys.spellarena.mediators;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.gameworld.GameFactory;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.model.Spell.Spells;
import com.androidboys.spellarena.net.NetworkInterface;
import com.androidboys.spellarena.net.NetworkListenerAdapter;
import com.androidboys.spellarena.net.model.RoomModel;
import com.androidboys.spellarena.net.protocol.ClockSyncReqCommand;
import com.androidboys.spellarena.net.protocol.ClockSyncResCommand;
import com.androidboys.spellarena.net.protocol.Command;
import com.androidboys.spellarena.net.protocol.CommandFactory;
import com.androidboys.spellarena.net.protocol.CreateGameCommand;
import com.androidboys.spellarena.net.protocol.GameEndCommand;
import com.androidboys.spellarena.net.protocol.ReadyCommand;
import com.androidboys.spellarena.net.protocol.SpellCommand;
import com.androidboys.spellarena.net.protocol.StartGameCommand;
import com.androidboys.spellarena.net.protocol.UpdateCommand;
import com.androidboys.spellarena.net.protocol.GameEndCommand.GameEndReason;
import com.androidboys.spellarena.net.protocol.MoveCommand;
import com.androidboys.spellarena.servers.GameClient;
import com.androidboys.spellarena.servers.GameServer;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.async.AsyncExecutor;

public class GameScreenMediator extends Mediator{

	private static final String TAG = "GameScreenMediator";
	
	private GameServer gameServer;
	private GameClient gameClient;
	private NetworkInterface networkInterface;
	private NetworkListenerAdapter networkListenerAdapter;
	private GameScreen gameScreen;
	private CommandFactory commandFactory = new CommandFactory();
	private GameFactory.GameModel gameModel;
	private RoomModel room;
	private int level = 1;

	ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
	
	private LinkedList<String> receivedMessages = new LinkedList<String>();
	
	public GameScreenMediator(SpellArena game, NetworkInterface networkInterface) {
		super(game);
		this.gameModel = GameFactory.getGameModel(level);
		this.networkInterface = networkInterface;
		this.networkListenerAdapter = new NetworkListenerAdapter(){
			
			@Override
			public void onMessageReceived(String from, final String message) {
				synchronized (receivedMessages) {
//					Gdx.app.log(TAG, "accessing lock");
					receivedMessages.addLast(message);
				}
//				Gdx.app.log(TAG, "exiting lock");
			}
			
			@Override
			public void onPlayerLeftRoom(RoomModel room, String playerName) {
				super.onPlayerLeftRoom(room, playerName);
				if(GameScreenMediator.this.getRoom().equals(room) && !playerName.equals(UserSession.getInstance().getUserName())){
					GameScreenMediator.this.onPlayerLeftRoom(playerName);
				}
			}
			
			@Override
			public void onPlayerJoinedRoom(RoomModel room, String playerName) {
				super.onPlayerJoinedRoom(room, playerName);
				Gdx.app.log(TAG,""+GameScreenMediator.this.getRoom().getId().equals(room.getId()));
				if(GameScreenMediator.this.getRoom().getId().equals(room.getId()) && !playerName.equals(UserSession.getInstance().getUserName())){
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
				GameScreenMediator.this.roomInfoProcessed();
			}
			
			@Override
			public void onDisconnected() {
				gameScreen.onDisconnected();
			}
		};
		networkInterface.addNetworkListener(networkListenerAdapter);
		initCommandHandler();
	}

	protected void roomInfoProcessed() {
		gameScreen.roomInfoProcessed();
	}

	private void handleCreateGameCommand(Command c){
		String ip = ((CreateGameCommand)c).getIP();
//		gameScreen.setServerReady(true);
		gameScreen.connectToServer(ip);
	}
	
	private void handleReadyGameCommand(Command command) {
		Gdx.app.log(TAG, "handleReadyGameCommand "+command);
		String playerName = command.getFromUser();
		if(UserSession.getInstance().isServer()){
			
			checkSync(playerName);
		}
		gameScreen.onPlayerReady(playerName);
	}
	
	private void handleStartGameCommand(Command c){
		networkInterface.removeNetworkListener(networkListenerAdapter);
		gameScreen.onStartGame();
		if(UserSession.getInstance().isServer()){
			networkInterface.deleteRoom(UserSession.getInstance().getRoom().getId());
		}
	}
	
	private void handleClockSyncRequestCommand(Command c){
		ClockSyncResCommand command = new ClockSyncResCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		command.setInitialTimeStamp(c.getTimeStamp());
		gameClient.sendMessage(command.serialize());
	}
	
	private void handleClockSyncResponseCommand(Command c){
		long delay = ((ClockSyncResCommand)c).getInitialTimeStamp() - c.getTimeStamp();
		Gdx.app.log(TAG,"lag: "+delay);
	}
	
	private void handleMoveCommand(Command c){
		long time = c.getTimeStamp();
		int movement = ((MoveCommand)c).getMovement();
		Vector2 position = ((MoveCommand)c).getPosition();
		if(!c.getFromUser().equals(UserSession.getInstance().getUserName())){
			gameScreen.onMove(time, c.getFromUser(), movement, position.x, position.y);
		}
	}
	
	private void handleUpdateCommand(Command c){
		Vector2 position = ((UpdateCommand)c).getPosition();
		Vector2 velocity = ((UpdateCommand)c).getVelocity();
		float health = ((UpdateCommand)c).getHealth();
		long timestamp = c.getTimeStamp();
		if(!c.getFromUser().equals(UserSession.getInstance().getUserName())){
			gameScreen.onUpdate(c.getFromUser(), timestamp, position, velocity, health);
		}
	}
	
	private void handleGameEndCommand(Command command) {
        if (((GameEndCommand)command).getReason() == GameEndCommand.GameEndReason.GAME_END) {
        	String winnerName = ((GameEndCommand)command).getWinner();
            if (UserSession.getInstance().getUserName().equals(winnerName)) {
                gameScreen.displayWinGamePopup();
            } else {
            	gameScreen.displayLoseGamePopup(winnerName);
            }
        }
    }
	
	private void handleSpellCommand(Command command){
		SpellCommand spellCommand = (SpellCommand)command;
		String playerName = command.getFromUser();
		gameScreen.castSpell(playerName, spellCommand.getPosition().x, spellCommand.getPosition().y, spellCommand.getSpell(), spellCommand.getDirection());
	}
	
	@Override
	protected void onScreenShow() {
		super.onScreenShow();
		if(!UserSession.getInstance().isServer()) {
			networkInterface.getRoomInfo(UserSession.getInstance().getRoom().getId());
		}
	}
	
	private void onPlayerJoinedRoom(String playerName) {
		Gdx.app.log(TAG, "Player: "+playerName+" has joined room "+room.getName());
		gameScreen.onPlayerJoinedRoom(playerName);
	}

	private void checkSync(String playerName){
		ClockSyncReqCommand command = new ClockSyncReqCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		gameServer.sendMessage(command.serialize());
	}
	
	public void onPlayerLeftRoom(String playerName) {
		if((playerName != null)&&playerName.equals(getRoom().getOwner())){
			onGameOwnerLeft();
		} else {
			gameScreen.removePlayer(playerName);
		}
	}
	
	private void onGameOwnerLeft() {
        gameScreen.onOwnerLeft();
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

	public void startGame(){
		StartGameCommand command = new StartGameCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		gameServer.sendMessage(command.serialize());
		gameScreen.onStartGame();
	}
	
	public void move(int movement) {
		MoveCommand command = new MoveCommand();
		command.setMovement(movement);
		command.setFromUser(UserSession.getInstance().getUserName());
		if(UserSession.getInstance().isServer()){
			gameServer.sendMessage(command.serialize());
		} 
		else {	
			gameClient.sendMessage(command.serialize());
		}
	}

	public void update(Bob playerModel) {
		if(playerModel != null){
			UpdateCommand command = new UpdateCommand();
			command.setUpdate(playerModel.getPosition(),playerModel.getVelocity());
			command.setHealth(playerModel.getHealth());
			command.setFromUser(UserSession.getInstance().getUserName());
			if(UserSession.getInstance().isServer()){
				gameServer.sendMessage(command.serialize());
			} 
			else {	
				gameClient.sendMessage(command.serialize());
			}
		}
	}

	public void onServerStarted() {
		Gdx.app.log(TAG, "onServerStarted");
		gameScreen.onServerStarted();
	}

	public void onServerStartFail() {
		Gdx.app.log(TAG, "onServerStartFail");
		gameScreen.onServerStartFail();
	}

	public void sendServerAddress(String playerName) {
		try {
			CreateGameCommand command = new CreateGameCommand();
			command.setIP(UserSession.getInstance().getIP());
			command.setFromUser(UserSession.getInstance().getUserName());
			networkInterface.sendMessageTo(playerName, command.serialize());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connectToServerSuccess(GameClient client) {
		if(client!=null){
			this.gameClient = client;
		}
		Gdx.app.log(TAG, "connectToServerSuccess");
		gameScreen.connectToServerSuccess();
		ReadyCommand command = new ReadyCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		gameClient.sendMessage(command.serialize());
	}
	
	public void endGame(String winner) {
		GameEndCommand command = new GameEndCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		command.setReason(GameEndReason.GAME_END);
		command.setWinner(winner);
		gameServer.sendMessage(command.serialize());
		handleGameEndCommand(command);
	}

	public void connectToServerFailed() {
		Gdx.app.log(TAG, "connectToServerFailed");
		gameScreen.connectToServerFail();
	}

	public void processMessage(String object) {
		synchronized (receivedMessages) {
			receivedMessages.addLast(object);
		}
	}

	public void initCommandHandler() {
		new Thread( new Runnable() {
				
			@Override
			public void run() {
				while(true){
		    		try {
						Thread.sleep(75);
						final String message;
						synchronized (receivedMessages) {
//							Gdx.app.log(TAG, "accessing lock");
							if(!receivedMessages.isEmpty()){
								message = receivedMessages.removeFirst();
							} else {
								message = null;
							}
						}
//						Gdx.app.log(TAG, "exiting lock");
						if(message != null){
							executor.execute(new Runnable() {
								@Override
								public void run() {
									Gdx.app.log(TAG,"Accepting command: "+message);
									Command command = commandFactory.createCommand(message);
									//Gdx.app.log(TAG, "Time difference: "+(command.getTimeStamp()-System.currentTimeMillis()));
									if(command == null){
										Gdx.app.log(TAG, "Waiting for split message");
										return;
									}
									switch(command.getCommand()){
										case Command.CREATE_GAME:
											handleCreateGameCommand(command);
											break;
										case Command.READY:
											handleReadyGameCommand(command);
											break;
										case Command.START_GAME:
											handleStartGameCommand(command);
											break;
										case Command.CLOCK_SYNC_REQ:
											handleClockSyncRequestCommand(command);
											break;
										case Command.CLOCK_SYNC_RES:
											handleClockSyncResponseCommand(command);
											break;
										case Command.MOVE:
											handleMoveCommand(command);
											break;
										case Command.UPDATE:
											handleUpdateCommand(command);
											break;
										case Command.CAST:
											handleSpellCommand(command);
											break;
										case Command.GAME_END:
											handleGameEndCommand(command);
											break;
									}
								}
							});
						}
		    		} catch (Exception e){
		    			Gdx.app.log(TAG,"Message handling failed");
		    		}
				}
			}
		}).start();
	}

	public void leaveRoom() {
		networkInterface.leaveRoom(UserSession.getInstance().getRoom().getId());
	}
	
	public void spellCommand(int direction, int spell, float x, float y){
		SpellCommand command = new SpellCommand();
		command.setDirection(direction);
		command.setPosition(x, y);
		command.setSpell(spell);
		command.setFromUser(UserSession.getInstance().getUserName());
		if(UserSession.getInstance().isServer()){
			gameServer.sendMessage(command.serialize());
		} 
		else {	
			gameClient.sendMessage(command.serialize());
		}
	}

	public void disconnect(boolean gameStarted) {
		if(!gameStarted){
			if(this.getNetworkListenerAdapter() != null){
				game.getClient().removeNetworkListener(this.getNetworkListenerAdapter());
			}
			RoomModel roomModel = UserSession.getInstance().getRoom();
			if(roomModel != null){
				game.getClient().leaveRoom(roomModel.getId());
				if(UserSession.getInstance().isRoomOwner()){
					game.getClient().deleteRoom(roomModel.getId());
				}
			}
		} else {
			if(UserSession.getInstance().isServer()){
//				gameServer.stop();
			} else {
				gameClient.close();
			}
		}
	}

	
}
