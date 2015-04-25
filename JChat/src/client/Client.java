package client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Client extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	//constructor
	public Client(String host)
	{
		super("JChat Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		chatWindow.setFont(new Font("Consolas",Font.PLAIN,14));
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(500,500);
		setVisible(true);
	}
	
	//connect to server
	public void startRunning()
	{
		try
		{
			connectToServer();
			setupStreams();
			whileChatting();
		}
		catch(EOFException e)
		{
			showMessage("\nClient Terminated the Connection");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeChat();
		}
	}
	
	//connect to server
	private void connectToServer() throws IOException
	{
		showMessage("\nAttempting Connection...\n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Connectted to : " + connection.getInetAddress().getHostName() + "\n");
	}
	
	//setup streams to send and receive messages
	private void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\nStream setting Sucessful...\n");
	}
	
	//while in chat with server
	private void whileChatting() throws IOException
	{
		ableToType(true);
		do
		{
			try
			{
				message = (String) input.readObject();
				showMessage(message + "\n");
			}
			catch(ClassNotFoundException e)
			{
				showMessage("\nUnknown Object Type...\n");
			}
		}
		while(!message.equals("SERVER - END"));
	}
	
	//close the streams and socket
	private void closeChat()
	{
		showMessage("\nClosing Connections...\n");
		ableToType(false);
		
		try
		{
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException e)
		{
			showMessage("\nConnection Closed...\n");
		}
	}
	
	//send messages to server
	private void sendMessage(String message)
	{
		try
		{
			output.writeObject("CLIENT - " + message);
			output.flush();
			
			showMessage("\nCLIENT - " + message);
		}
		catch(IOException e)
		{
			chatWindow.append("\nSomething went wrong sending Message...\n");
		}
	}
	
	//update chat window
	private void showMessage(final String message)
	{
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						chatWindow.append(message);
					}
				}
		);
	}
	
	// permission to user for typing
	private void ableToType(final boolean tof)
	{
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						userText.setEditable(tof);
					}
				}
		);
	}
	
}
