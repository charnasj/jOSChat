package client;

import java.util.Vector;


/**
 * This is the interface between the window (that you do not need to modify) and your client code.
 * These methods will be called from the chat GUI to operate your client.
 * This file must not be modified.
 *  
 */
public interface CommandsFromWindow{
	
	/**
	 * ask the server to create a new room named roomName
	 * @param roomName String 
	 * @return true if success, false otherwise
	 */
	public boolean createNewRoom(String roomName)  ;
	
	/**
	 * Send a new message to the server to propagate to all clients
	 * @param text The message to send
	 */
	public void sendText (String chatName, String message);
	
	/**
	 * Get the list of chat rooms from the server (as a vector of String)
	 * @return a list of available chat rooms or an empty Vector if there is none, or if the server is unavailable
	 */
	public Vector<String> getChatRoomsList ();
	
	/**
	 * Ask to join the chat room. Does not leave existing chat rooms.
	 //to do so we need to know only chatRoom name
	 * @param name The name (identifier) of the chat room
	 * @return true if joining is successful
	 */
	public boolean joinChatRoom (String name);
	
	/**
	 * Ask the server to leave the chat room. No effect if not a member.
	 * @param name The name (identifier) of the chat room
	 * @return true if leaving is successful
	 */
	public boolean leaveChatRoom (String name);	
}
