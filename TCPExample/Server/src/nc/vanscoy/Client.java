package nc.vanscoy;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

	//streams to read and write with client instances
	ObjectInputStream in = null;
	ObjectOutputStream out = null;
	boolean go = false;
	public Socket socket;

	public Client(Socket socket) {
		try {
			this.socket = socket;
			
			//create IO streams
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			//add client to the server's client list
			TCPServer.clients.add(this);
			TCPServer.output.append("Client " + TCPServer.clients.size() + " connected...\n");
			//start a thread to read incoming client messages
			this.go = true;
			Thread thread = new Thread(this);
			thread.start();
		} catch (IOException e) {}
	}
	
	@Override
	public void run() {
		while(go) {
			try {
				//wait until a message comes from the client
				Object obj = in.readObject();
				//add valid message to the queue
				if(obj instanceof String) {
					TCPServer.messages.add(obj.toString());					
				}
				else if(obj instanceof LocationMessage) {
					TCPServer.messages.add("Location Message Received");
				}
				else
					TCPServer.messages.add("Unkown Message Received");
			} catch (Exception e) {
				//remove from the client list if streams are broken
				TCPServer.clients.remove(this);
				try {
					TCPServer.output.append("Disconnected client\n");
					this.socket.close();
					go = false;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
		}
	}
}
