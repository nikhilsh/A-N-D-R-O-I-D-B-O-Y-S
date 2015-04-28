package com.androidboys.spellarena.servers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Stack;

import com.androidboys.spellarena.mediators.GameScreenMediator;
import com.androidboys.spellarena.net.NetworkInterface;
import com.androidboys.spellarena.net.NetworkListenerAdapter;
import com.androidboys.spellarena.net.protocol.Command;
import com.androidboys.spellarena.net.protocol.CommandFactory;
import com.androidboys.spellarena.session.UserSession;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class GameClient.
 */
public class GameClient {
	
	/** The out buffer. */
	private LinkedList<String> outBuffer = new LinkedList<String>();
	
	/** The Constant TCP_PORT. */
	public static final int TCP_PORT = 4455;
	
	/** The Constant UDP_PORT. */
	public static final int UDP_PORT = 4456;
	
	/** The Constant TAG. */
	protected static final String TAG = "GameClient";
	
	/** The network interface. */
	private NetworkInterface networkInterface;
	
	/** The game screen mediator. */
	private GameScreenMediator gameScreenMediator;
	
	/** The client. */
	private Client client;
	
	/**
	 * Initialize.
	 *
	 * @param client the client
	 * @param gameScreenMediator the game screen mediator
	 */
	public void initialize(NetworkInterface client,
			GameScreenMediator gameScreenMediator){
		
		this.networkInterface = client;
		this.gameScreenMediator = gameScreenMediator;
		this.client = new Client();
		this.client.start();
		Gdx.app.log(TAG, "Client started");
//		this.client.getKryo().register(String.class);
		
	}
	
	/**
	 * Connect to server.
	 *
	 * @param host the host
	 */
	public void connectToServer(String host){
		try {
			initListener();
			client.connect(5000, InetAddress.getByName(host), TCP_PORT, UDP_PORT);
			setUpSender();
			gameScreenMediator.connectToServerSuccess(this);
		} catch (UnknownHostException e) {
			gameScreenMediator.connectToServerFailed();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Inits the listener.
	 */
	public void initListener(){
		this.client.addListener(new Listener(){
			
			@Override
			public void disconnected(Connection connection) {
				gameScreenMediator.onPlayerLeftRoom(UserSession.getInstance().getRoom().getOwner());
			}
			
			@Override
			public void received(Connection connection, final Object object) {
				//Gdx.app.log(TAG, "Received: "+object);
				if(object instanceof String){
					gameScreenMediator.processMessage((String) object);
				}		
			}
		});
	}
	
	/**
	 * Sets the up sender.
	 */
	private void setUpSender(){
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
			    			client.sendTCP(message);
			    			
			    		}
			    	}catch(Exception e){
			    		Gdx.app.log(TAG,"fail to send");
			    	}
				}
			}
		}).start();
	}

	/**
	 * Send message.
	 *
	 * @param serialize the serialize
	 */
	public void sendMessage(String serialize) {
		synchronized (outBuffer) {
			Gdx.app.log(TAG,"Adding message to buffer");
			outBuffer.addLast(serialize);
		}
	}

	/**
	 * Close.
	 */
	public void close() {
		client.close();
	}
}
