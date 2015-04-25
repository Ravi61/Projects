package client;

import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) {
		
		Client chat;
		chat = new Client("127.0.0.1");
		chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chat.startRunning();
	}

}
