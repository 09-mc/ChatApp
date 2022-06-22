Network Programming Project

-------------------------
Brief Description
-------------------------

I started off with a simple socket based chat client that could facilitate chat between the 
server and a single user. I then moved on to using Multicast Sockets for group chat which
required a fairly substantial rewrite of the code as I had to replace the input/output streams
that worked well with standard sockets and instead use datagram packets. 

The code in both the server and chat client side that handles packets is mirrored, trying to 
change either side to be unique seemed to cause issues so I left it as is. This essentially
allows either program to be used for group chat, although the ChatServer program has 
added functionality with the Threads class. The ChatClient uses Threads but one handles 
incoming and the other handles outgoing messages.

To try to keep the main block of code concise on both the GroupChatClient class 
(ChatServer) and the ChatClient class (ChatClient), I thought it best to create getter methods
 for inet address, port number & username. This also made it easier to create a try-catch 
block for inet & port whereby we could loop back to the request if an error was thrown 
with the entered inet or port (i.e. the program would not crash in the case of an incorrect 
or already in use inet or port).

The main block of code that deals with the multicast sockets & datagrams seems to be well
repeated across numerous sources online (Refs: 1, 2, 3) so I kept the structure mostly the 
same but implemented slight changes to better suit my own goals (e.g. user input for port 
& inet, adding time-stamp to messages).

I also thought it a good idea to implement a feature that tells when a user has joined or 
left. Otherwise you could have people in the chat without the groups knowledge or you
could be talking to someone that has left the chat without knowing.

The methods Multicast socket .joinGroup & .leaveGroup are deprecated since Java 14 
although I found the replacement .joinGroup & .leaveGroup which required both a socket
and a network interface as parameters to be difficult to implement so I kept with the originals.

***Please note***
Any unassigned address between 224.0.0.0 to 239.255.255.255 can be used. I have used 
229.0.0.0 in combination with port 5555 and this is publicly shared on the program at startup.
For privacy, please use another combination and share with the relevant parties.

-------------------------
Instructions
-------------------------

I ran the program from the command line using the combination of the below commands.

ChatServer: cd eclipse-workspace\ChatServer\bin -> java ie.gmit.dip.Runner
ChatClient: cd eclipse-workspace\ChatClient\bin -> java ie.gmit.dip.Runner

I would suggest putting the folder onto your desktop as separate ChatServer & ChatClient 
folders which then can be accessed by the below (Assuming you are on Windows)

ChatServer: cd Desktop\ChatServer\bin -> java ie.gmit.dip.Runner
ChatClient: cd Desktop\ChatClient\bin -> java ie.gmit.dip.Runner

You will then be prompted to enter the address & port. Unless told otherwise, you should
use the ones that are displayed on the console. Of course you can use your own but be
sure to tell whoever is joining your chat that you are on a different socket.

Once the console displays that you have joined the chat, simply type your message into
the console and hit enter to send. Just to note that the string /q will terminate the chat
on the user end and tell all other connected users that you have left the chat.

-------------------------
References
-------------------------
1) https://www.developer.com/design/how-to-multicast-using-java-sockets/
2) https://www.baeldung.com/java-broadcast-multicast
3) https://www.geeksforgeeks.org/a-group-chat-application-in-java/?ref=lbp

Other sources used
https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html
https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html
https://docs.oracle.com/javase/7/docs/api/java/net/MulticastSocket.html
https://docs.oracle.com/javase/7/docs/api/java/net/DatagramPacket.html#:~:text=Datagram%20packets%20are%20used%20to,might%20arrive%20in%20any%20order.
