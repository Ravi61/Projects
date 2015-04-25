package server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public Server() {
		super("JChat Application");
		
		userText = new JTextField();
		userText.setEditable(false);
		
		userText.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		chatWindow.setFont(new Font("Consolas",Font.PLAIN,14));
		add(new JScrollPane(chatWindow));
		setSize(500,500);
		setVisible(true);
	}
	
	//set up and run the server
	
	public void startRunning() {
		try {
			
			server = new ServerSocket(6789, 100);
			
			while(true) {
				try {
					
					waitForConnection();
					setupStreams();
					whileChatting();
					
				} 
				catch(EOFException eofException) {
					showMessage("\nServer ended the Connection!");
					chatWindow.setText("");
				} 
				finally {
					closeChat();
				}
			}
			
		} catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//wait for connection, then display connection info.
	
	private void waitForConnection() throws IOException
	{
		showMessage("\nWaiting for Someone to Connect...\n");
		
		connection = server.accept();
		showMessage("Now Connected to " + connection.getInetAddress().getHostName() + "...\n");;
		
	}
	
	//get stream to send and receive data
	
	private void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("Streams are now setup!\n");
	}
	
	//during the chat conversation
	
	private void whileChatting() throws IOException
	{

		String message = "You are now connected!\n";	
		sendMessage(message);
		ableToType(true);
		
		do
		{
			try
			{
				message = (String) input.readObject();
				showMessage("\n" + message);
			}
			catch(ClassNotFoundException e)
			{
				showMessage("\nUnknown Information!");
			}
		}
		while(!message.equals("CLIENT - END"));
		
	}
	
	//close streams and sockets after chat
	
	private void closeChat()
	{
		showMessage("\nClosing Connections...\n\n");
		ableToType(false);
		try
		{
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException e)
		{
			showMessage("\n\nConnection Closed...");
		}
	}
	
	//send a message to client
	
	private void sendMessage(String message)
	{
		try
		{
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER - " + message);
		}
		catch(IOException e)
		{
			chatWindow.append("\nERROR : Message not sent \n");
		}
	}
	
	//updates chat window
	private void showMessage(final String text)
	{
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						chatWindow.append(text);
					}
				}
			);
	}
	
	//let the user type
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(tof);
					}
				}
			);
	}
	
}
