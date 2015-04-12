package com.androidboys.spellarena.servers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.androidboys.spellarena.gameworld.GameWorld;
import com.androidboys.spellarena.mediators.GameScreenMediator;
import com.androidboys.spellarena.net.NetworkInterface;
import com.androidboys.spellarena.net.NetworkListenerAdapter;
import com.androidboys.spellarena.net.protocol.Command;
import com.androidboys.spellarena.net.protocol.CommandFactory;
import com.androidboys.spellarena.net.protocol.CreateGameCommand;
import com.androidboys.spellarena.net.protocol.StartGameCommand;
import com.androidboys.spellarena.session.UserSession;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer {

	private LinkedList<String> inBuffer = new LinkedList<String>();
	private LinkedList<String> outBuffer = new LinkedList<String>();
	
	private HashMap<Connection, LinkedList<String>> outBuffers= new HashMap<Connection,LinkedList<String>>();
	
	public static final int TCP_PORT = 4455;
	public static final int UDP_PORT = 4456;
	protected static final String TAG = "GameServer";
	private ScheduledExecutorService executorService;
    private boolean isGameStarted = false;
    private boolean isDisposed = false;
	
	private GameWorld world;
	private NetworkInterface networkInterface;
	private CommandFactory commandFactory = new CommandFactory();
	private GameScreenMediator gameScreenMediator;

	private Server server;
	
	public GameServer(GameWorld world){
		this.world = world;
	}
	
	public void initialize(NetworkInterface client,
			GameScreenMediator gameScreenMediator){
		
		this.networkInterface = client;
		this.gameScreenMediator = gameScreenMediator;
		this.networkInterface.addNetworkListener(new NetworkListenerAdapter() {
			
			@Override
			public void onMessageReceived(String from, String message) {
				Command command = commandFactory.createCommand(message);
				if(command != null) {
					switch (command.getCommand()) {
						case Command.MOVE:
							
							break;
						default:
							break;
					}
				}
			}
			
		});
		
//		GameFactory.GameModel gameModel = GameFactory.getGameModel(gameScreenMediator.getLevel());
//		executorService = Executors.newScheduledThreadPool(corePoolSize) 
	}
	
	public void startServer(){
		this.server = new Server();
//		this.server.getKryo().register(String.class);
		this.server.start();
		try {
			this.server.bind(TCP_PORT, UDP_PORT);
			this.server.addListener(new Listener(){

				@Override
				public void received(Connection connection, Object object) {
					if(object instanceof String){
						Gdx.app.log(TAG, "Sending to all: "+object);
						gameScreenMediator.processMessage((String) object);
//						server.sendToAllTCP(object);
						server.sendToAllExceptTCP(connection.getID(), object);
					}
				}
				
			});
			gameScreenMediator.onServerStarted();
		} catch (IOException e) {
			gameScreenMediator.onServerStartFail();
		}
		this.initSender();
	}

	protected void handleClockSyncResponseCommand(Command command) {
		// TODO Auto-generated method stub
		
	}

	public void createGame() {
		CreateGameCommand createGameCommand = new CreateGameCommand();
		createGameCommand.setFromUser(UserSession.getInstance().getUserName());
		String message = createGameCommand.serialize();
		for (String splitMessage : createGameCommand.splitMessage(message)) {
            networkInterface.sendMessage(splitMessage);
        }
	}

//	public void startGame() {
//		StartGameCommand startGameCommand = new StartGameCommand();
//		startGameCommand.setFromUser(UserSession.getInstance().getUserName());
//		networkInterface.sendMessage(startGameCommand.serialize());
//		
//		isGameStarted = true;
//		networkInterface.startGame(UserSession.getInstance().getRoom().getId());
//	}
	public void sendMessage(String serialize) {
		synchronized (outBuffer) {
			outBuffer.add(serialize);
		}
	}
	
	private void initSender(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				while (true){
					try{
			    		Thread.sleep(75);
			    		String message;
			    		synchronized (outBuffer) {
							if(!outBuffer.isEmpty()){
								message = outBuffer.removeFirst();
							} else {
								message = null;
							}
						}
			    		if(message != null){
			    			Gdx.app.log(TAG, "Sending to server: "+ message);
			    			server.sendToAllTCP(message);
			    			
			    		}
			    	}catch(Exception e){
			    		Gdx.app.log(TAG,"fail to send");
			    	}
				}
			}
		}).start();
	}
}
