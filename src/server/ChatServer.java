package server;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import client.CommandsFromServer;


/**
 * Each instance of this class is a server for one room.
 * In a first time, there is only one room server, and the names of the room available is fixed.
 * In a second time, you will have multiple room server, each managed by its own ChatServer.
 * A ChatServerManager will then be responsible for creating new rooms are they are added. 
 */
public class ChatServer implements ChatServerInterface {
	
	private String roomName;
	private Vector<CommandsFromServer> registeredClients;
	
	/**
	 * Constructor: initializes the chat room and register it to the RMI registry
	 * @param roomName
	 */
	public ChatServer(String roomName){
		this.roomName = roomName;
		registeredClients = new Vector<CommandsFromServer>();
		
		try {
			Naming.rebind("room_" + roomName, (ChatServerInterface)this);
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		/*
		 * TODO register the ChatServer to the RMI registry
		 */
	}
	
	public void publish(String message, String publisher) {
		
		System.err.println("TODO: publish is not implemented");
		
		/*
		 * TODO send the message to all registered clients
		 */
	}

	public void register(CommandsFromServer client) {
		
		System.err.println("TODO: register is not implemented");
		
		/*
		 * TODO register the client
		 */
	}

	public void unregister(CommandsFromServer client) {
		
		System.err.println("TODO: unregister is not implemented");
		
		/*
		 * TODO unregister the client
		 */
	}
	
}
