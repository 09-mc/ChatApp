package ie.gmit.dip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;

public class ChatClient {

	private static String username;
	private static String endConnection = "/q";
	private static int port;
	private static String inetAddress;

	/**
	 * The method that encompasses the key features of the program from the client
	 * point of view
	 */

	public void chatClient() throws UnknownHostException, IOException, InterruptedException {

		/**
		 * A few methods to call the first message, inet chooser & port chooser. Makes
		 * the main body of the code much easier to read
		 */

		firstMessage();

		try {
			inetAddress = getInetAddress();
			InetAddress group = InetAddress.getByName(inetAddress);

			try {
				port = getPort();
				username = getUsername();

				/**
				 * Uses the port & inet address to check for socket. Connects if one is active
				 * and creates if not
				 */

				MulticastSocket socket = new MulticastSocket(port);
				socket.setTimeToLive(0);
				socket.joinGroup(group);

				/**
				 * Informs active users of new user joining
				 */

				System.out.println("");
				String userjoin = LocalTime.now().withNano(0).withSecond(0) + " [" + username + "] has joined the chat";
				byte[] buff = userjoin.getBytes();
				DatagramPacket dgp = new DatagramPacket(buff, buff.length, group, port);
				socket.send(dgp);

				Thread sendMessage = new Thread(new Runnable() {
					
					/**
					 * Takes in a string called message. If message is /q, the user ends their connection and all users are notified
					 * If it is not /q, the message is sent all users
					 * I have added a time stamp to each message for easier reading
					 * Threads created for separate task of reading and writing messages
					 * Catches & methods below run() are the same as GroupChatClient in their functions
					 */

					public void run() {
						while (true) {

							Scanner s = new Scanner(System.in);
							String message = s.nextLine();
							if (message.equalsIgnoreCase(endConnection)) {
								try {
									message = LocalTime.now().withNano(0).withSecond(0) + " [" + username
											+ "] has left the chat";
									byte[] buffer = message.getBytes();
									DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
									socket.send(datagram);

									socket.leaveGroup(group);
									socket.close();
									System.exit(0);
									break;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							try {
								message = LocalTime.now().withNano(0).withSecond(0) + " [" + username + "]" + ": "
										+ message;
								byte[] buffer = message.getBytes();
								DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
								socket.send(datagram);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});

				Thread readMessage = new Thread(new Runnable() {

					public void run() {

						while (true) {
							{
								byte[] buffer = new byte[1024];
								DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
								String message;
								try {
									socket.receive(datagram);
									message = new String(buffer, 0, datagram.getLength(), "UTF-8");
									System.out.println(message);
								} catch (Exception e) {
									e.getMessage();
								}
							}
						}
					}
				});

				sendMessage.start();
				readMessage.start();

			} catch (SocketException se) {
				System.out.println("");
				System.out
						.println("Issue using that port, please consult the design document for the next port to use!");
				getPort();
			}
		} catch (SocketException se) {
			System.out.println("");
			System.out.println(
					"Issue using that Inet address, please consult the design document for the next Inet address to use!");
			getInetAddress();
		}
	}

	public void firstMessage() throws InterruptedException {
		System.out.println("");
		System.out.println("Please consult the design document for suggested network address & port to be used...");
		System.out.println("");
		System.out.println("If you do not have the design document to hand, please use: ");
		System.out.println("Network address: 229.0.0.0");
		System.out.println("Port number: 5555");
		System.out.println("");
		Thread.sleep(1000);
	}

	public String getInetAddress() {
		System.out.println("");
		System.out.println("Please enter the network address you wish to use: ");
		Scanner s = new Scanner(System.in);
		inetAddress = s.nextLine();
		return inetAddress;
	}

	public int getPort() {
		System.out.println("");
		System.out.println("Please enter the port you wish to use: ");
		Scanner sc = new Scanner(System.in);
		port = sc.nextInt();
		return port;
	}

	public String getUsername() {
		Scanner sca = new Scanner(System.in);
		System.out.println("");
		System.out.print("Enter your name: ");
		username = sca.nextLine();
		return username;

	}
}
