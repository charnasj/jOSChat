package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;

import server.ChatServerInterface;
import server.ChatServerManagerInterface;

public class ChatClient implements CommandsFromWindow, CommandsFromServer {

	/**
	 * The name of the user of this client
	 */
	private String userName;

	/**
	 * The graphical user interface, accessed through its interface. In return,
	 * the GUI will use the CommandsFromWindow interface to call methods to the
	 * ChatClient implementation.
	 */
	private final CommandsToWindow window;

	/**
	 * The server name.
	 * */
//	private final String serverLookUpName = "localhost";

	/**
	 * The server remote interface.
	 * */
	private ChatServerManagerInterface server;

	/**
	 * Vector of chatrooms client is currently in.
	 * */
	private Hashtable<String,ChatServerInterface> chatRooms;
	
	/**
	 * Constructor for the ChatClient. Must perform the connection to the
	 * server. If the connection is not successful, it must exit with an error.
	 * 
	 * @param window
	 */
	public ChatClient(CommandsToWindow window, String userName) {
		this.window = window;
		this.userName = userName;
		chatRooms = new Hashtable<String, ChatServerInterface>();
		try {
			server = (ChatServerManagerInterface) Naming.lookup("server");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Implementation of the functions from the CommandsFromWindow interface.
	 * See methods description in the interface definition.
	 */

	public void sendText(String roomName, String message) {
		ChatServerInterface chatServer = chatRooms.get(roomName);
		try {
			chatServer.publish(message, userName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public Vector<String> getChatRoomsList() {
		Vector<String> ret;
		try {
			ret = server.getRoomsList();
			return ret;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean joinChatRoom(String roomName) {
		try {
			ChatServerInterface chatServer = (ChatServerInterface) Naming.lookup("room_" + roomName);
			chatServer.register((CommandsFromServer)this);
			chatRooms.put(roomName,chatServer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean leaveChatRoom(String roomName) {
		ChatServerInterface chatServer = chatRooms.get(roomName);
		try {
			chatServer.unregister((CommandsFromServer)this);
			chatRooms.remove(roomName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean createNewRoom(String roomName) {
		try {
			return server.createRoom(roomName);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Implementation of the functions from the CommandsFromServer interface.
	 * See methods description in the interface definition.
	 */

	public void receiveMsg(String roomName, String message) {
		window.publish(roomName, message);
	}

	// This class does not contain a main method. You should launch the whole
	// program by launching ChatClientWindow's main method.
}
