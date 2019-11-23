//PROG1415
//Vanscoy
//F2015

package nc.vanscoy;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

//a java server program
public class TCPServer extends JFrame implements Runnable, WindowListener {

	private static final long serialVersionUID = 1L;
	
	//Server
	private ServerSocket server;
	
	private boolean go = false;
	
	static JTextArea output = new JTextArea("Ready...\n");
	static List<Client> clients = new ArrayList<Client>();
	static List<String> messages = new ArrayList<String>();
	
	public	TCPServer() {
		//build interface
		output.setEditable(false);
		this.getContentPane().add(new JScrollPane(output));
		this.setTitle("Server");
		this.setBounds(100,100,300,500);
		this.setVisible(true);
		this.addWindowListener(this);
		
		//start server main thread
		Thread acceptThread = new Thread(this);
		acceptThread.setPriority(Thread.MAX_PRIORITY);
		acceptThread.start();
	}

	@Override
	public void run() {
		try {
			//construct the server
			server = new ServerSocket(8000);
			//start additional server threads
			go = true;
			new BroadCast().start();
		} catch (Exception e) {
			output.append("Server launch failed...\n");
			return;
		}
		
		//loop to receive client connections
		int count = 1;
		while(go) {
			try {
				output.append("Waiting...\n");
				Socket socket = server.accept();
				//start a new process for each client
				new Client(socket);
			} catch (IOException e) {
				output.append("Attempt to connect " + count + " failed...\n");
				if(count++ <= 3) 
					continue;
				else
					break;
			}
		}
		output.append("Server is no longer accepting clients...\n");
	}

	//a server thread to broadcast text messages to all connected clients
	class BroadCast extends Thread {
		@Override
		public void run() {
			output.append("Broadcasting enabled...\n");
			while(go) {
				//allow other threads to run
				Thread.yield();
				//check for clients and messages
				if(messages.size() > 0 && clients.size() > 0) {
					String msg = messages.get(0);
					for(int x=0;x<clients.size();x++)
						try {
							clients.get(x).out.writeObject(msg);
						} catch (IOException e) {
							output.append("Error writing to client...\n");
							clients.remove(x);
						}
					messages.remove(0);
				}
			}
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.go = false;
		for(int x=0;x<clients.size();x++)
			clients.get(x).go = false;
		try {
			server.close();
		} catch (IOException e) {}
		System.exit(0);
		
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}

	public static void main(String[] args) {
		new TCPServer();
	}
}
