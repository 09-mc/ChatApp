package ie.gmit.dip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Threads implements Runnable {
	private MulticastSocket socket;
	private InetAddress group;
	private int port;
	private static final int MAX_LEN = 1000;

	Threads(MulticastSocket socket, InetAddress group, int port) {
		/**
		 * Thread constructor setting these parameters which is used in GroupChatClient
		 */
		this.socket = socket;
		this.group = group;
		this.port = port;
	}

	public void run() {
		while (!GroupChatClient.isFinished) {
			/**
			 * Sets behaviour of the program
			 */
			byte[] buffer = new byte[Threads.MAX_LEN];
			DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
			String message;
			try {
				socket.receive(datagram);
				message = new String(buffer, 0, datagram.getLength(), "UTF-8");
				if (!message.startsWith(GroupChatClient.username))
					System.out.println(message);
			} catch (IOException e) {
				System.out.println("You have left the group chat!");
			}
		}
	}
}