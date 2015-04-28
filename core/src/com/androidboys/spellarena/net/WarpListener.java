package com.androidboys.spellarena.net;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving warp events.
 * The class that is interested in processing a warp
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addWarpListener<code> method. When
 * the warp event occurs, that object's appropriate
 * method is invoked.
 *
 * @see WarpEvent
 */
public interface WarpListener {

	/**
	 * On waiting started.
	 *
	 * @param message the message
	 */
	public void onWaitingStarted(String message);
	
	/**
	 * On error.
	 *
	 * @param message the message
	 */
	public void onError(String message);
	
	/**
	 * On game started.
	 *
	 * @param message the message
	 */
	public void onGameStarted(String message);
	
	/**
	 * On game finished.
	 *
	 * @param code the code
	 * @param isRemote the is remote
	 */
	public void onGameFinished(int code, boolean isRemote);
	
	/**
	 * On game update received.
	 *
	 * @param message the message
	 */
	public void onGameUpdateReceived(String message);
	
}
