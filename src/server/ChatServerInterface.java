package server;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import client.CommandsFromServer;



public interface ChatServerInterface extends Remote, Serializable{

	/**
	 * receives a message from a client and send it to all subscribed clients
	 * @param message The message to propagate
	 */
	public void publish(String message, String publisher) throws RemoteException;
	
	/**
	 * registers a new client to the chat room
	 * @param clientLookupName the name of the client as registered on the RMI registry
	 */
	public void register(CommandsFromServer client) throws RemoteException;
	
	/**
	 * unregisters a new client to the chat room
	 * @param clientLookupName the name of the client as registered on the RMI registry
	 */
	public void unregister(CommandsFromServer client) throws RemoteException;
}
