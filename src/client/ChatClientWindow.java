package client;


/*
 * This Class is a graphical interface for your chat client. 
 * It must not be modified. 
 * Only a ChatClient object implementing the CommandsFromWindow interface is required.
 * All the logic and client-server interface goes in this ChatClient object.
 * (Feel free to observe the code of this class if you are curious about how one can create graphical interfaces.)
 */

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ChatClientWindow implements CommandsToWindow {

	// graphical elements
	private final JFrame frmChatClient;
	private final JTextField textField;
	private final JTabbedPane tabbedPane;
	private final JButton btnGetRoomList;
	private final JButton btnJoinRoom;
	private final JButton btnLeaveRoom;
	private final JButton btnSend;
	private final JButton btnCreateRoom;
	private final JTextField textFieldCreateRoom;
	private final JList listChatRooms;
	private final DefaultListModel listChatRoomsModel;
	private final JScrollPane scrollPaneChatRooms;
	
	private int selectedTab = -1 ;
	
	// map of String (room names) to the tabs.
	private final Map<String, JScrollPane> chats;
	
	private final CommandsFromWindow client;
	
	/**
	 * Launch the application. This is the main point of entry of the application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatClientWindow window = new ChatClientWindow();
					window.frmChatClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws RemoteException 
	 */
	public ChatClientWindow() {
		// create the client object first
		System.out.println("Initializing the ChatClient object ...");
		
		// display a dialog asking for the user name
		String userName = (String)JOptionPane.showInputDialog(
                null,
                "Enter your name:\n",
                "RMI-chat",
                JOptionPane.PLAIN_MESSAGE,
                null,null,
                "");
		
		client = new ChatClient(this, userName);
				
		System.out.println("Initializing the ChatClient Window ...");
		
		frmChatClient = new JFrame();
		frmChatClient.setTitle("Chat Client (OS - UniNE)");
		frmChatClient.setResizable(false);
		frmChatClient.setBounds(100, 100, 500, 500);
		frmChatClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatClient.getContentPane().setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 6, 359, 408);
		frmChatClient.getContentPane().add(tabbedPane);
		
		btnGetRoomList = new JButton("Get Room List");
		btnGetRoomList.setBounds(370, 198, 117, 29);
		frmChatClient.getContentPane().add(btnGetRoomList);
		
		btnJoinRoom = new JButton("Join Room");
		btnJoinRoom.setBounds(370, 239, 117, 29);
		frmChatClient.getContentPane().add(btnJoinRoom);
		
		btnLeaveRoom = new JButton("Leave Room");
		btnLeaveRoom.setBounds(370, 280, 117, 29);
		frmChatClient.getContentPane().add(btnLeaveRoom);
		
		btnCreateRoom = new JButton("Create Room");
		btnCreateRoom.setBounds(370, 362, 117, 29);
		frmChatClient.getContentPane().add(btnCreateRoom);
		
		textFieldCreateRoom = new JTextField();
		textFieldCreateRoom.setBounds(370, 321, 117, 29);
		frmChatClient.getContentPane().add(textFieldCreateRoom);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(370, 430, 117, 29);
		frmChatClient.getContentPane().add(btnSend);
		
		textField = new JTextField();
		textField.setBounds(10, 429, 350, 28);
		frmChatClient.getContentPane().add(textField);
		textField.setColumns(10);
		
		listChatRoomsModel = new DefaultListModel();
		listChatRooms = new JList(listChatRoomsModel);
		listChatRooms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listChatRooms.setLayoutOrientation(JList.VERTICAL);		
		scrollPaneChatRooms = new JScrollPane(listChatRooms);
		scrollPaneChatRooms.setBounds(370, 25, 120, 160);		
		frmChatClient.getContentPane().add(scrollPaneChatRooms);
		
		// create the map for chats (initially empty)
		chats = new HashMap<String, JScrollPane>();
		
		// add a change listener to the tabbedpane so we can track which one is in focus
		tabbedPane.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				selectedTab = tabbedPane.getSelectedIndex();				
			}
		});
		
		// pressing enter in the text field will do the same as clicking on "Send"
		textField.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnSend.doClick();
			}
		});
		
		// send a message to the chat client if the text box is not empty
		btnSend.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// get the message
				String message = textField.getText();				
				// send it if not empty
				if (message.equalsIgnoreCase("")) return;
				// get the tab with the focus and get the corresponding chat name
				if (selectedTab == -1) {
					// no chat room existing!
					JOptionPane.showMessageDialog(frmChatClient, "You must join a chat room before sending messages!", "Error message", JOptionPane.ERROR_MESSAGE);					
				} else {
					// get the String name of the selected tab
					String r = tabbedPane.getTitleAt(selectedTab);					
					client.sendText(r, message);
					// empty the text field
					textField.setText("");
				}
			}
		});
		
		// add an action to get the rooms list and display it
		btnGetRoomList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// get the list of rooms from the client
				Vector<String> listOfRooms = client.getChatRoomsList();
				listChatRoomsModel.clear();
				if (listOfRooms != null) {
					for (String r : listOfRooms) {
						listChatRoomsModel.addElement(r);
					}
				}
			}
		});
		
		// add an action to join a room
		btnJoinRoom.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// get the selected room name from the list
				int index = listChatRooms.getSelectedIndex();
				if (index == -1) {
					// no selection: warn with a pop up.
					JOptionPane.showMessageDialog(frmChatClient, "You must select a room name from the list first!", "Error message", JOptionPane.ERROR_MESSAGE);
				} else {
					// get the selected item name
					String r = (String) listChatRoomsModel.get(index);
					// check that the room is not already joined (there exists a tab)
					if (chats.containsKey(r)) {
						JOptionPane.showMessageDialog(frmChatClient, "You already joined this chat room.", "Error message", JOptionPane.ERROR_MESSAGE);
					} else {						
						// ask client to join room
						boolean status = client.joinChatRoom(r);
						if (!status) {
							// not possible to join, display error message and return
							JOptionPane.showMessageDialog(frmChatClient, "Joining chat room "+r+" was not successful.\nCheck client/server interaction.", "Error message", JOptionPane.ERROR_MESSAGE);
						} else {
							// joining ok, create a text area
							JTextArea jta = new JTextArea();
							jta.setLineWrap(true);
							jta.setWrapStyleWord(true);
							jta.setText("You have now joined the discussion on "+r+" and can now send and receive messages!\n");
							// embed it in a scroll pane
							JScrollPane jsc = new JScrollPane(jta);
							// disable horizontal scrolling
							jsc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
							// add this to the tabs
							tabbedPane.add(jsc, r);
							// and store it to the map
							chats.put(r, jsc) ;
						}
					}
				}					
			}
		});
		
		// add an action to leave a room
		btnLeaveRoom.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// get the selected room from the tab (if there is one)
				if (selectedTab == -1) {
					JOptionPane.showMessageDialog(frmChatClient, "There is no chat room to leave.", "Error message", JOptionPane.ERROR_MESSAGE);
				} else {
					// get the name of the tab
					String r = tabbedPane.getTitleAt(selectedTab);
					// ask client to leave room
					boolean status = client.leaveChatRoom(r);
					if (!status) {
						// fail
						JOptionPane.showMessageDialog(frmChatClient, "Leaving chat room "+r+" was not successful.\nCheck client/server interaction.", "Error message", JOptionPane.ERROR_MESSAGE);
					} else {
						// leaving ok, remove the corresponding tab
						tabbedPane.remove(chats.get(r));
						//alexey remove r from chat list
						chats.remove(r);
					}
				}
			}
		});
		
		// add an action to create a room
		btnCreateRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// get the text from the text field
				String roomName = textFieldCreateRoom.getText();
				// clear the text field
				textFieldCreateRoom.setText("");
				// create the room if name is not empty
				if (roomName.equalsIgnoreCase("")) {
					// error, no room to create!
					JOptionPane.showMessageDialog(frmChatClient, "No Chat Room name to create given!", "Error message", JOptionPane.ERROR_MESSAGE);
				} else {
					// create the room
					boolean success = client.createNewRoom(roomName);
					if (!success) {
						JOptionPane.showMessageDialog(frmChatClient, "Creating Chat room "+roomName+" was not successful.", "Error message", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		// add an action to create the room when enter is pressed for the text field
		textFieldCreateRoom.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnCreateRoom.doClick();
			}
		});
				
		System.out.println("... success");
	}

	// interface from the client
	public void publish(String chatName, String message) {
		System.out.println("received message "+message+" for chat room "+chatName);
		
		// check that the chat room exists
		if (! chats.containsKey(chatName)) {
			JOptionPane.showMessageDialog(frmChatClient, "The client asks for sending a message\nto a non-existing chat room ("+chatName+").", "Error message", JOptionPane.ERROR_MESSAGE);
		} else {
			// get the textpane embedded in the jscrollpane embedded in the tab
			JScrollPane jsp = chats.get(chatName);
			JTextArea jta = (JTextArea) jsp.getViewport().getView();
			jta.setText(jta.getText()+"\n"+message);			
		}
	}	
}