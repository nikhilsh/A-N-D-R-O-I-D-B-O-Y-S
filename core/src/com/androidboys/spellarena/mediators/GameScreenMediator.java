package com.androidboys.spellarena.mediators;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.androidboys.spellarena.game.SpellArena;
import com.androidboys.spellarena.model.Bob;
import com.androidboys.spellarena.net.NetworkInterface;
import com.androidboys.spellarena.net.NetworkListenerAdapter;
import com.androidboys.spellarena.net.model.RoomModel;
import com.androidboys.spellarena.net.protocol.ClockSyncReqCommand;
import com.androidboys.spellarena.net.protocol.ClockSyncResCommand;
import com.androidboys.spellarena.net.protocol.Command;
import com.androidboys.spellarena.net.protocol.CommandFactory;
import com.androidboys.spellarena.net.protocol.CreateGameCommand;
import com.androidboys.spellarena.net.protocol.GameEndCommand;
import com.androidboys.spellarena.net.protocol.GameEndCommand.GameEndReason;
import com.androidboys.spellarena.net.protocol.MoveCommand;
import com.androidboys.spellarena.net.protocol.ReadyCommand;
import com.androidboys.spellarena.net.protocol.SpellCommand;
import com.androidboys.spellarena.net.protocol.StartGameCommand;
import com.androidboys.spellarena.net.protocol.UpdateCommand;
import com.androidboys.spellarena.servers.GameClient;
import com.androidboys.spellarena.servers.GameServer;
import com.androidboys.spellarena.session.UserSession;
import com.androidboys.spellarena.view.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;

public class GameScreenMediator extends Mediator{

	private static final String TAG = "GameScreenMediator";
	
	private GameServer gameServer;
	private GameClient gameClient;
	private NetworkInterface networkInterface;
	private NetworkListenerAdapter networkListenerAdapter;
	private GameScreen gameScreen;
	private CommandFactory commandFactory = new CommandFactory();
	private RoomModel room;

	ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
	
	private LinkedList<String> receivedMessages = new LinkedList<String>();
	
	/**
	 * Instantiates a new game screen mediator.
	 *
	 * @param game the game object
	 * @param networkInterface the network interface
	 */
	public GameScreenMediator(SpellArena game, NetworkInterface networkInterface) {
		super(game);
		this.networkInterface = networkInterface;
		this.networkListenerAdapter = new NetworkListenerAdapter(){
			
			@Override
			public void onMessageReceived(String from, final String message) {
				synchronized (receivedMessages) {
					receivedMessages.addLast(message);
				}
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
//				Gdx.app.log(TAG,""+GameScreenMediator.this.getRoom().getId().equals(room.getId()));
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

	/**
	 * function to tell if room info processed.
	 */
	protected void roomInfoProcessed() {
		gameScreen.roomInfoProcessed();
	}

	/**
	 * Handle create game command.
	 *
	 * @param c the command
	 */
	private void handleCreateGameCommand(Command command){
		String ip = ((CreateGameCommand)command).getIP();
		gameScreen.connectToServer(ip);
	}
	
	/**
	 * Handle ready game command.
	 *
	 * @param command the command
	 */
	private void handleReadyGameCommand(Command command) {
		String playerName = command.getFromUser();
		if(UserSession.getInstance().isServer()){
			
			checkSync(playerName);
		}
		gameScreen.onPlayerReady(playerName);
	}
	
	/**
	 * Handle start game command.
	 *
	 * @param c the command
	 */
	private void handleStartGameCommand(Command c){
		networkInterface.removeNetworkListener(networkListenerAdapter);
		gameScreen.onStartGame();
		if(UserSession.getInstance().isServer()){
			networkInterface.deleteRoom(UserSession.getInstance().getRoom().getId());
		}
	}
	
	/**
	 * Handle clock sync request command.
	 *
	 * @param c the command
	 */
	private void handleClockSyncRequestCommand(Command c){
		ClockSyncResCommand command = new ClockSyncResCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		command.setInitialTimeStamp(c.getTimeStamp());
		gameClient.sendMessage(command.serialize());
	}
	
	/**
	 * Handle clock sync response command.
	 *
	 * @param c the command
	 */
	private void handleClockSyncResponseCommand(Command c){
		long delay = ((ClockSyncResCommand)c).getInitialTimeStamp() - c.getTimeStamp();
	}
	
	/**
	 * Handle move command.
	 *
	 * @param c the command
	 */
	private void handleMoveCommand(Command c){
		long time = c.getTimeStamp();
		int movement = ((MoveCommand)c).getMovement();
		Vector2 position = ((MoveCommand)c).getPosition();
		if(!c.getFromUser().equals(UserSession.getInstance().getUserName())){
			gameScreen.onMove(time, c.getFromUser(), movement, position.x, position.y);
		}
	}
	
	/**
	 * Handle update command.
	 *
	 * @param c the command
	 */
	private void handleUpdateCommand(Command c){
		Vector2 position = ((UpdateCommand)c).getPosition();
		Vector2 velocity = ((UpdateCommand)c).getVelocity();
		float health = ((UpdateCommand)c).getHealth();
		long timestamp = c.getTimeStamp();
		if(!c.getFromUser().equals(UserSession.getInstance().getUserName())){
			gameScreen.onUpdate(c.getFromUser(), timestamp, position, velocity, health);
		}
	}
	
	/**
	 * Handle game end command.
	 *
	 * @param command the command
	 */
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
	
	/**
	 * Handle spell command.
	 *
	 * @param command the command
	 */
	private void handleSpellCommand(Command command){
		SpellCommand spellCommand = (SpellCommand)command;
		String playerName = command.getFromUser();
		gameScreen.castSpell(playerName, spellCommand.getPosition().x, spellCommand.getPosition().y, spellCommand.getSpell(), spellCommand.getDirection());
	}
	
	/**
	 * Gets id of rooms when the main screen appears
	 */
	
	@Override
	protected void onScreenShow() {
		super.onScreenShow();
		if(!UserSession.getInstance().isServer()) {
			networkInterface.getRoomInfo(UserSession.getInstance().getRoom().getId());
		}
	}
	
	/**
	 * On player joined room.
	 *
	 * @param playerName the player name
	 */
	private void onPlayerJoinedRoom(String playerName) {
		gameScreen.onPlayerJoinedRoom(playerName);
	}

	/**
	 * Check sync.
	 *
	 * @param playerName the player name
	 */
	private void checkSync(String playerName){
		ClockSyncReqCommand command = new ClockSyncReqCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		gameServer.sendMessage(command.serialize());
	}
	
	/**
	 * Remove player when player has left room
	 *
	 * @param playerName the player name
	 */
	public void onPlayerLeftRoom(String playerName) {
		if((playerName != null)&&playerName.equals(getRoom().getOwner())){
			onGameOwnerLeft();
		} else {
			gameScreen.removePlayer(playerName);
		}
	}
	
	/**
	 * On game owner left, notify game screen
	 */
	private void onGameOwnerLeft() {
        gameScreen.onOwnerLeft();
	}

	/**
	 * Create the gamescreen
	 */
	@Override
	public Screen createScreen() {
		this.screen = new GameScreen(game,this);
		this.gameScreen = (GameScreen) screen;
		return screen;
	}

	/**
	 * Gets the network listener adapter.
	 *
	 * @return the network listener adapter
	 */
	public NetworkListenerAdapter getNetworkListenerAdapter() {
		return networkListenerAdapter;
	}
	
	/**
	 * Gets the room.
	 *
	 * @return the room
	 */
	public RoomModel getRoom() {
		return room;
	}

	/**
	 * Sets the room.
	 *
	 * @param room the new room
	 */
	public void setRoom(RoomModel room) {
		this.room = room;
	}

	/**
	 * Sets the game server.
	 *
	 * @param gameServer the new game server
	 */
	public void setGameServer(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	/**
	 * Start game.
	 */
	public void startGame(){
		StartGameCommand command = new StartGameCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		gameServer.sendMessage(command.serialize());
		gameScreen.onStartGame();
	}
	
	/**
	 * Move player.
	 *
	 * @param movement the movement
	 */
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

	/**
	 * Update player's details.
	 *
	 * @param playerModel the player model
	 */
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

	/**
	 * On server started, notify gamescreen
	 */
	public void onServerStarted() {
		gameScreen.onServerStarted();
	}

	/**
	 * On server start fail, notify game screen
	 */
	public void onServerStartFail() {
		gameScreen.onServerStartFail();
	}

	/**
	 * Send server address to network interface
	 *
	 * @param playerName the player name
	 */
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

	/**
	 * Tells game client if connection to server was a success.
	 *
	 * @param client the client
	 */
	public void connectToServerSuccess(GameClient client) {
		if(client!=null){
			this.gameClient = client;
		}
		gameScreen.connectToServerSuccess();
		ReadyCommand command = new ReadyCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		gameClient.sendMessage(command.serialize());
	}
	
	/**
	 * Upon end game, tell other clients
	 *
	 * @param winner the winner
	 */
	public void endGame(String winner) {
		GameEndCommand command = new GameEndCommand();
		command.setFromUser(UserSession.getInstance().getUserName());
		command.setReason(GameEndReason.GAME_END);
		command.setWinner(winner);
		gameServer.sendMessage(command.serialize());
		handleGameEndCommand(command);
	}

	/**
	 * tell game screen that connect to server failed.
	 */
	public void connectToServerFailed() {
		gameScreen.connectToServerFail();
	}

	/**
	 * process incoming messages and add to received messages
	 *
	 * @param object the object
	 */
	public void processMessage(String object) {
		synchronized (receivedMessages) {
			receivedMessages.addLast(object);
		}
	}

	/**
	 * Inits the command handler. takes care of messages in the received messages list
	 */
	public void initCommandHandler() {
		new Thread( new Runnable() {
				
			@Override
			public void run() {
				while(true){
		    		try {
						Thread.sleep(75);
						final String message;
						synchronized (receivedMessages) {
							if(!receivedMessages.isEmpty()){
								message = receivedMessages.removeFirst();
							} else {
								message = null;
							}
						}
						if(message != null){
							executor.execute(new Runnable() {
								@Override
								public void run() {
									Command command = commandFactory.createCommand(message);
									if(command == null){
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
		    		}
				}
			}
		}).start();
	}

	/**
	 * tells networking interface that local client has left room.
	 */
	public void leaveRoom() {
		networkInterface.leaveRoom(UserSession.getInstance().getRoom().getId());
	}
	
	/**
	 * tell other clients that spell command is casted
	 *
	 * @param direction the direction player  was facing
 	 * @param spell the spell casted
	 * @param x the x of the spell
	 * @param y the y of the spell
	 */
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

	/**
	 * Method is called on disconnect. Handles deleting room and removing network listeners so that can join other rooms
	 *
	 * @param gameStarted boolena on whether the game has started
	 */
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
