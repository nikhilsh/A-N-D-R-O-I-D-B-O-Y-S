package com.androidboys.spellarena.servers;

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

public class GameServer {

	private ScheduledExecutorService executorService;
    private boolean isGameStarted = false;
    private boolean isDisposed = false;
	
	private GameWorld world;
	private NetworkInterface networkInterface;
	private CommandFactory commandFactory = new CommandFactory();
	private GameScreenMediator gameScreenMediator;

	public GameServer(GameWorld world){
		this.world = world;
	}
	
	public void initialize(NetworkInterface client,
			GameScreenMediator gameScreenMediator) {
		
		this.networkInterface = client;
		this.gameScreenMediator = gameScreenMediator;
		
		this.networkInterface.addNetworkListener(new NetworkListenerAdapter() {
			
			@Override
			public void onMessageReceived(String from, String message) {
				Command command = commandFactory.createCommand(message);
				if(command != null) {
					switch (command.getCommand()) {
						case Command.MOVE_EAST:
							
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

	public void createGame() {
		CreateGameCommand createGameCommand = new CreateGameCommand();
		createGameCommand.setFromUser(UserSession.getInstance().getUserName());
		String message = createGameCommand.serialize();
		for (String splitMessage : createGameCommand.splitMessage(message)) {
            networkInterface.sendMessage(splitMessage);
        }
        startGame();
	}

	private void startGame() {
		StartGameCommand startGameCommand = new StartGameCommand();
		startGameCommand.setFromUser(UserSession.getInstance().getUserName());
		networkInterface.sendMessage(startGameCommand.serialize());
		
		isGameStarted = true;
		networkInterface.startGame(UserSession.getInstance().getRoom().getId());
	}

}
