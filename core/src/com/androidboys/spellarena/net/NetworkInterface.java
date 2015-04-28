package com.androidboys.spellarena.net;

import java.util.List;

import com.androidboys.spellarena.net.model.RoomModel;

// TODO: Auto-generated Javadoc
/**
 * The Interface NetworkInterface.
 */
public interface NetworkInterface {

	/** The Constant MAX_USERS. */
	public static final int MAX_USERS = 4;
	
	/**
	 * The listener interface for receiving network events.
	 * The class that is interested in processing a network
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addNetworkListener<code> method. When
	 * the network event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see NetworkEvent
	 */
	public static interface NetworkListener{
		 
 		/**
 		 * On connected.
 		 */
 		public void onConnected();

        /**
         * On disconnected.
         */
        public void onDisconnected();

        /**
         * On connection failure.
         *
         * @param e the e
         */
        public void onConnectionFailure(Exception e);

        /**
         * On room list received.
         *
         * @param rooms the rooms
         */
        public void onRoomListReceived(List<RoomModel> rooms);

        /**
         * On room list request failed.
         */
        public void onRoomListRequestFailed();

        /**
         * Invoked when on room is created.
         *
         * @param room the room
         */
        public void onRoomCreated(RoomModel room);

        /**
         * On create room failed.
         */
        public void onCreateRoomFailed();

        /**
         * On room deleted.
         *
         * @param roomId the room id
         */
        public void onRoomDeleted(String roomId);

        /**
         * On delete room failed.
         */
        public void onDeleteRoomFailed();

        /**
         * On join room success.
         *
         * @param roomId the room id
         */
        public void onJoinRoomSuccess(String roomId);

        /**
         * On join room failed.
         */
        public void onJoinRoomFailed();

        /**
         * On message received.
         *
         * @param from the from
         * @param message the message
         */
        public void onMessageReceived(String from, String message);

        /**
         * On player joined room.
         *
         * @param room the room
         * @param playerName the player name
         */
        public void onPlayerJoinedRoom(RoomModel room, String playerName);

        /**
         * On player left room.
         *
         * @param room the room
         * @param playerName the player name
         */
        public void onPlayerLeftRoom(RoomModel room, String playerName);

        /**
         * On room info received.
         *
         * @param players the players
         * @param data the data
         */
        public void onRoomInfoReceived(String [] players, String data);
	}
	
	/**
	 * Adds the network listener.
	 *
	 * @param listener the listener
	 */
	public void addNetworkListener(NetworkListener listener);

    /**
     * Removes the network listener.
     *
     * @param listener the listener
     */
    public void removeNetworkListener(NetworkListener listener);

    /**
     * Clear network listeners.
     */
    public void clearNetworkListeners();

    /**
     * Connect.
     */
    public void connect();

    /**
     * Disconnect.
     */
    public void disconnect();

    /**
     * List rooms.
     */
    public void listRooms();

    /**
     * Creates the room.
     *
     * @param roomName the room name
     */
    public void createRoom(String roomName);

    /**
     * Delete room.
     *
     * @param roomId the room id
     */
    public void deleteRoom(String roomId);

    /**
     * Join room.
     *
     * @param roomId the room id
     */
    public void joinRoom(String roomId);

    /**
     * Leave room.
     *
     * @param roomId the room id
     */
    public void leaveRoom(String roomId);

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message);

    /**
     * Send message to.
     *
     * @param destination the destination
     * @param message the message
     */
    public void sendMessageTo(String destination, String message);

    /**
     * Start game.
     *
     * @param roomId the room id
     */
    public void startGame(String roomId);

    /**
     * Gets the room info.
     *
     * @param roomId the room id
     * @return the room info
     */
    public void getRoomInfo(String roomId);

    /**
     * Checks if is connected.
     *
     * @return true, if is connected
     */
    public boolean isConnected();

}
