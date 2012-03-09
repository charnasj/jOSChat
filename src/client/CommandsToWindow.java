package client;



/**
 * This interface must not be modified. It contains the calls from the client to the GUI.
 */
public interface CommandsToWindow {
	
	/**
	 * Publish message in a chat room of the interface. Will display an error message if the chat room does not exist. 
	 * @param chatName The name of the chat room
	 * @param message The message to display
	 */
	public void publish(String chatName, String message);
}
