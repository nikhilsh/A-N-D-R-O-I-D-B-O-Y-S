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

// TODO: Auto-generated Javadoc
/**
 * The Class GameServer.
 */
public class GameServer {
	
	/** The instance. */
	private static GameServer INSTANCE;
	
	/**
	 * Gets the single instance of GameServer.
	 *
	 * @return single instance of GameServer
	 */
	public static GameServer getInstance(){
		if(INSTANCE == null){
			INSTANCE = new GameServer();
		}
		return INSTANCE;
	}

	/** The in buffer. */
	private LinkedList<String> inBuffer = new LinkedList<String>();
	
	/** The out buffer. */
	private LinkedList<String> outBuffer = new LinkedList<String>();
	
	/** The connections list. */
	private HashMap<Connection, String> connectionsList = new HashMap<Connection, String>();
	
	/** The Constant TCP_PORT. */
	public static final int TCP_PORT = 4455;
	
	/** The Constant UDP_PORT. */
	public static final int UDP_PORT = 4456;
	
	/** The Constant TAG. */
	protected static final String TAG = "GameServer";
	
	/** The executor service. */
	private ScheduledExecutorService executorService;
    
    /** The is server started. */
    private boolean isServerStarted;
    
    /** The is disposed. */
    private boolean isDisposed = false;
	
	/** The world. */
	private GameWorld world;
	
	/** The network interface. */
	private NetworkInterface networkInterface;
	
	/** The command factory. */
	private CommandFactory commandFactory = new CommandFactory();
	
	/** The game screen mediator. */
	private GameScreenMediator gameScreenMediator;

	/** The server. */
	private Server server;
	
	/**
	 * Instantiates a new game server.
	 */
	public GameServer(){
		this.isServerStarted = false;
	}
	
//	public GameServer(GameWorld world){
//		this.world = world;
//		this.isServerStarted = false;
//	}
	
	/**
 * Initialize.
 *
 * @param gameWorld the game world
 * @param client the client
 * @param gameScreenMediator the game screen mediator
 */
public void initialize(GameWorld gameWorld, NetworkInterface client,
			GameScreenMediator gameScreenMediator){
		this.world = gameWorld;
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
	
	/**
	 * Start server.
	 */
	public void startServer(){
		Gdx.app.log(TAG,""+isServerStarted);
		if(!isServerStarted){
			isServerStarted = true;
			Gdx.app.log(TAG,""+isServerStarted);
			this.server = new Server();
	//		this.server.getKryo().register(String.class);
			this.server.start();
			try {
				this.server.bind(TCP_PORT, UDP_PORT);
				this.server.addListener(new Listener(){
					
					@Override
					public void connected(Connection connection) {
						connectionsList.put(connection, null);
					}
					
					@Override
					public void disconnected(Connection connection) {
						Gdx.app.log("GameServer", connection+" disconnected");
						String user = connectionsList.get(connection);
						gameScreenMediator.onPlayerLeftRoom(user);
					}
					
					@Override
					public void received(Connection connection, Object object) {
						if(connectionsList.get(connection) == null){
							Command command = commandFactory.createCommand((String)object);
							if(command == null){
								Gdx.app.log(TAG, "Waiting for split message");
								return;
							}
							String user = command.getFromUser();
							connectionsList.put(connection, user);
						}
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
		} else {
			gameScreenMediator.onServerStarted();
		}
	}

	/**
	 * Handle clock sync response command.
	 *
	 * @param command the command
	 */
	protected void handleClockSyncResponseCommand(Command command) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Creates the game.
	 */
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
	/**
 * Send message.
 *
 * @param serialize the serialize
 */
public void sendMessage(String serialize) {
		synchronized (outBuffer) {
			outBuffer.add(serialize);
		}
	}
	
	/**
	 * Inits the sender.
	 */
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

	/**
	 * Stop.
	 */
	public void stop() {
		server.stop();
	}
}
