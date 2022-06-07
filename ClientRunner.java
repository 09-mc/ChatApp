package ie.gmit.dip;

import java.io.IOException;
import java.net.UnknownHostException;

public class Runner {
	
	/**
	 * Runner class to separate functionality from GroupChatClient
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		ChatClient cc = new ChatClient();
		cc.chatClient();
	}

}
