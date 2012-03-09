package server;

import java.rmi.Remote;

import client.CommandsFromServer;



public interface ChatServerInterface extends Remote {

	/**
	 * receives a message from a client and send it to all subscribed clients
	 * @param message The message to propagate
	 */
	public void publish(String message, String publisher) ;
	
	/**
	 * registers a new client to the chat room
	 * @param clientLookupName the name of the client as registered on the RMI registry
	 */
	public void register(CommandsFromServer client);
	
	/**
	 * unregisters a new client to the chat room
	 * @param clientLookupName the name of the client as registered on the RMI registry
	 */
	public void unregister(CommandsFromServer client);
}
