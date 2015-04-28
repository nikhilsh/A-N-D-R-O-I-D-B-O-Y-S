package com.androidboys.spellarena.net;

import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving connection events.
 * The class that is interested in processing a connection
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addConnectionListener<code> method. When
 * the connection event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ConnectionEvent
 */
public class ConnectionListener implements ConnectionRequestListener {

	/** The call back. */
	WarpController callBack;
	
	/**
	 * Instantiates a new connection listener.
	 *
	 * @param callback the callback
	 */
	public ConnectionListener(WarpController callback) {
		this.callBack = callback;
	}
	
	/* (non-Javadoc)
	 * @see com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener#onConnectDone(com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent)
	 */
	@Override
	public void onConnectDone(ConnectEvent event) {
		if(event.getResult()==WarpResponseResultCode.SUCCESS){
			callBack.onConnectDone(true);
		} else {
			callBack.onConnectDone(false);
		}
	}

	/* (non-Javadoc)
	 * @see com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener#onDisconnectDone(com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent)
	 */
	@Override
	public void onDisconnectDone(ConnectEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener#onInitUDPDone(byte)
	 */
	@Override
	public void onInitUDPDone(byte arg0) {
		// TODO Auto-generated method stub

	}

}
