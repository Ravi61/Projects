package server;

import javax.swing.JFrame;

public class ServerTest {

	public static void main(String[] args) {
		Server chat = new Server();
		chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chat.startRunning();
	}

}
