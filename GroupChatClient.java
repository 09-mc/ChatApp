package ie.gmit.dip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.Scanner;

public class GroupChatClient {

	private static final String endConnection = "/q";
	protected static String username;
	protected static volatile boolean isFinished = false;
	private static String inetAddress;
	private static int port;
	
	/**
	 * The method that encompasses the key features of the program from the server point of view
	 */

	public void chatStarter() throws IOException, InterruptedException {
		
		/**
		 * A few methods to call the first message, inet chooser & port chooser. 
		 * Makes the main body of the code much easier to read
		 */
		firstMessage();
		try {
			inetAddress = getInetAddress();
			InetAddress group = InetAddress.getByName(inetAddress);

			try {
				port = getPort();
				username = getUsername();
				
				/**
				 * Creates a new multicast socket using the input port 
				 * The uses this socket to join the group specified from the inet address
				 * Finally creates a new Thread using the socket, group & port as parameters
				 */

				MulticastSocket socket = new MulticastSocket(port);
				socket.setTimeToLive(0);
				socket.joinGroup(group);
				Thread thread = new Thread(new Threads(socket, group, port));

				/**
				 * Begins execution of the thread
				 */
				thread.start();
				
				/**
				 * Messages all active users on the thread that a new user has joined
				 */

				System.out.println("");
				String userjoin = LocalTime.now().withNano(0).withSecond(0) + " [" + username + "] has joined the chat";
				byte[] buff = userjoin.getBytes();
				DatagramPacket dgp = new DatagramPacket(buff, buff.length, group, port);
				socket.send(dgp);

				while (true) {
					
					/**
					 * Takes in a string called message. If message is /q, the user ends their connection and all users are notified
					 * If it is not /q, the message is sent all users
					 * I have added a time stamp to each message for easier reading
					 */

					String message;
					Scanner scan = new Scanner(System.in);
					message = scan.nextLine();
					if (message.equalsIgnoreCase(GroupChatClient.endConnection)) {

						message = LocalTime.now().withNano(0).withSecond(0) + " [" + username + "] has left the chat";
						byte[] buffer = message.getBytes();
						DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
						socket.send(datagram);

						isFinished = true;
						socket.leaveGroup(group);
						socket.close();
						break;
					}
					message = LocalTime.now().withNano(0).withSecond(0) + " [" + username + "]" + ": " + message;
					byte[] buffer = message.getBytes();
					DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
					socket.send(datagram);
				}
			} catch (SocketException se) {				
				/**
				 * catches exception when entering port details and re-routes you back to enter a new port
				 */
				System.out.println("");
				System.out
						.println("Issue using that port, please consult the design document for the next port to use!");
				getPort();
			}

		} catch (SocketException se) {
			/**
			 * catches exception when entering inet details and re-routes you back to enter a new inet
			 */
			System.out.println("");
			System.out.println(
					"Issue using that Inet address, please consult the design document for the next Inet address to use!");
			getInetAddress();
		}
	}

	public void firstMessage() throws InterruptedException {
		/**
		 * Simple instructions to begin
		 */
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
		/**
		 * Method to get inet address
		 */
		System.out.println("");
		System.out.println("Please enter the network address you wish to use: ");
		Scanner s = new Scanner(System.in);
		inetAddress = s.nextLine();
		return inetAddress;
	}

	public int getPort() {
		/**
		 * Method to get port 
		 */
		System.out.println("");
		System.out.println("Please enter the port you wish to use: ");
		Scanner sc = new Scanner(System.in);
		port = sc.nextInt();
		return port;
	}

	public String getUsername() {
		/**
		 * Method to get username 
		 */
		Scanner sca = new Scanner(System.in);
		System.out.println("");
		System.out.print("Enter your name: ");
		username = sca.nextLine();
		return username;

	}

}