package ie.gmit.dip;

import java.io.IOException;

public class Runner {
	
	/**
	 * Runner class to separate functionality from GroupChatClient
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public static void main(String[] args) throws IOException, InterruptedException {
		GroupChatClient gcc = new GroupChatClient();
		gcc.chatStarter();
	}
	
}
