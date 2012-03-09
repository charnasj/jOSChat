package server;

import java.util.Vector;


/**
 * This interface is the set of commands that can be called remotely for the ChatServerManager:
 * 
 * for the first part:
 * - getting the list of rooms
 * 
 * for the second part:
 * - creating new rooms
 * (deletion of rooms not required)
 */
public interface ChatServerManagerInterface {
	
	/**
	 * @return the list of available chat rooms
	 */
	public Vector<String> getRoomsList();
	
	/**
	 * Ask for the creation of a new room, which means the creation of a new ChatServer.
	 * @param roomName the name of the room to create
	 * @return true if success, false otherwise (e.g., a room with the same name already exists)
	 */
	public boolean createRoom(String roomName);
	
}
