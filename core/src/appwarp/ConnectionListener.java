package appwarp;

import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

public class ConnectionListener implements ConnectionRequestListener {

	WarpController callBack;
	
	public ConnectionListener(WarpController callback) {
		this.callBack = callback;
	}
	
	@Override
	public void onConnectDone(ConnectEvent event) {
		if(event.getResult()==WarpResponseResultCode.SUCCESS){
			callBack.onConnectDone(true);
		} else {
			callBack.onConnectDone(false);
		}
	}

	@Override
	public void onDisconnectDone(ConnectEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitUDPDone(byte arg0) {
		// TODO Auto-generated method stub

	}

}
