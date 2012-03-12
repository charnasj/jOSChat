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
import client.CommandsFromWindow;


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
		
	}
	
	public void publish(String message, String publisher) {
		for(CommandsFromServer c : registeredClients) {
			c.receiveMsg(roomName, publisher + " : " + message);
		}
	}

	public void register(CommandsFromServer client) {
		registeredClients.add(client);
	}

	public void unregister(CommandsFromServer client) {
		registeredClients.remove(client);
	}
	
}
