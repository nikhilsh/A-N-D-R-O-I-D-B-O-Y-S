package com.androidboys.spellarena.net;

import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.listener.ChatRequestListener;

public class ChatListener implements ChatRequestListener {

	WarpController callback;
	
	public ChatListener(WarpController callback){
		this.callback = callback;
	}
	
	@Override
	public void onSendChatDone(byte result) {
		if(result==WarpResponseResultCode.SUCCESS){
			callback.onSendChatDone(true); 
		} else {
			callback.onSendChatDone(false);
		}
	}

	@Override
	public void onSendPrivateChatDone(byte arg0) {
		// TODO Auto-generated method stub

	}

}
