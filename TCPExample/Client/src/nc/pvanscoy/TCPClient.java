//PROG1415
//Vanscoy
//F2015

package nc.pvanscoy;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;
import java.awt.event.*;

//A java client program
public class TCPClient extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;

	//Networking Objects
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private boolean go = true;
	
	private JTextArea output = new JTextArea("Ready...\n");
	private JTextField input = new JTextField();
	
	//constructor
	public TCPClient() {
		//build interface
		output.setEditable(false);
		this.getContentPane().add(input,BorderLayout.NORTH);
		this.getContentPane().add(new JScrollPane(output),BorderLayout.CENTER);
		this.setTitle("Client");
		this.setBounds(100,100,300,500);
		this.setVisible(true);
		this.addWindowListener(this);
		
		//Connect to the server
		try {
			InetAddress address = InetAddress.getByName("127.0.0.1");
			socket = new Socket(address,8000);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//event to send user's input to the server
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(input.getText().length() > 0) {
					try {
						LocationMessage lm = new LocationMessage(0,0);
						out.writeObject(lm);
						out.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		new ReadMessages().start();
	}
	
	//thread to read messages coming from the server
	class ReadMessages extends Thread {
		@Override
		public void run() {
			while(go) {
				try {
					Object obj = in.readObject();
					if(obj instanceof String)
						output.append(obj.toString() + "\n");
				} catch (Exception e) { }
			}
		}
		
	}
	
	public static void main(String[] args) {
		new TCPClient();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

}
