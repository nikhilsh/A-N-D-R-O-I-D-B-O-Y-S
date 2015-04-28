package com.androidboys.spellarena.net;

import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.listener.ChatRequestListener;

/**
 * The listener interface for receiving chat events.
 * The class that is interested in processing a chat
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addChatListener<code> method. When
 * the chat event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ChatEvent
 */
public class ChatListener implements ChatRequestListener {

	/** The callback. */
	WarpController callback;
	
	/**
	 * Instantiates a new chat listener.
	 *
	 * @param callback the callback
	 */
	public ChatListener(WarpController callback){
		this.callback = callback;
	}
	
	/* (non-Javadoc)
	 * @see com.shephertz.app42.gaming.multiplayer.client.listener.ChatRequestListener#onSendChatDone(byte)
	 */
	@Override
	public void onSendChatDone(byte result) {
		if(result==WarpResponseResultCode.SUCCESS){
			callback.onSendChatDone(true); 
		} else {
			callback.onSendChatDone(false);
		}
	}

	/* (non-Javadoc)
	 * @see com.shephertz.app42.gaming.multiplayer.client.listener.ChatRequestListener#onSendPrivateChatDone(byte)
	 */
	@Override
	public void onSendPrivateChatDone(byte arg0) {
		// TODO Auto-generated method stub

	}

}
