package nc.vanscoy;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import nc.com.*;

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
				Object response = null;
				if(obj instanceof Location) {					
					Location message = (Location)obj;
					TCPServer.output.append("Location received:" + String.valueOf(message.lg) + ", " + String.valueOf(message.lt) + "\n");
					response = new ArrayList<Business>();
					for(int i = 0; i < TCPServer.data.size(); i++)
					{
						if(message.getDistance(TCPServer.data.get(i)) < 3000)
						{
							TCPServer.data.get(i).setAverageRating();
							((ArrayList)response).add(TCPServer.data.get(i));
						}						
					}
					TCPServer.output.append("Returned " + String.valueOf(((ArrayList)response).size()) + " business records");
				}
				else if(obj instanceof BusinessMessage)
				{
					response = TCPServer.data.get(((BusinessMessage)obj).BusinessID).feedback;
				}
				else if(obj instanceof Feedback)
				{
					int id = ((Feedback)obj).businessId;
					TCPServer.data.get(id).feedback.add((Feedback)obj);
					response = "Added";
				}
				else
					response = "Unkown Message Received";
				out.writeObject(response);
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
