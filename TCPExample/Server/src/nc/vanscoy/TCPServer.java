//PROG1415
//Vanscoy
//F2015

package nc.vanscoy;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import java.awt.event.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;


//a java server program
public class TCPServer extends JFrame implements Runnable, WindowListener {

	private static final long serialVersionUID = 1L;
	
	//Server
	private ServerSocket server;
	
	private boolean go = false;
	
	static JTextArea output = new JTextArea("Ready...\n");
	static List<Client> clients = new ArrayList<Client>();	
	static List<Business> data = new ArrayList<Business>();
	
	public	TCPServer() {
		//build interface
		output.setEditable(false);
		this.getContentPane().add(new JScrollPane(output));
		this.setTitle("Server");
		this.setBounds(100,100,300,500);
		this.setVisible(true);
		this.addWindowListener(this);
		
		//Read existing data
		try
		{
			File inputFile = new File("buisnesses.xml");             
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	        NodeList nList = doc.getElementsByTagName("business");
	        for(int i = 0; i < nList.getLength(); i++)
	        {
	        	Element e = (Element)nList.item(i);
	        	Business b = new Business();
	        	b.id = i;
	        	b.name = e.getElementsByTagName("name").item(0).getTextContent();
	        	b.postal = e.getElementsByTagName("posta").item(0).getTextContent();
	        	b.website = e.getElementsByTagName("website").item(0).getTextContent();
	        	NodeList feedback = e.getElementsByTagName("feedback");
	        	for(int j = 0; j < feedback.getLength(); j++)
	        	{
	        		Feedback f = new Feedback();
	        		f.businessId = i;
	        		Element fe = (Element)feedback.item(j);
	        		f.comment = fe.getElementsByTagName("comment").item(0).getTextContent();
	        		f.rating = Float.parseFloat(fe.getElementsByTagName("rating").item(0).getTextContent());
	        		f.date = new SimpleDateFormat("dd/MM/yyyy").parse(fe.getElementsByTagName("date").item(0).getTextContent());	        		
	        		b.feedback.add(f);
	        	}	        
	        	b.feedbackCount = b.feedback.size();
	        	data.add(b);
	        	System.out.println(i);
	        	System.out.println(data.size());
	        }	       
		} catch (Exception e) {
        	System.out.println(e.getMessage());
		}
		
		//Start file save thread
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try
				{
					File inputFile = new File("buisnesses.xml");             
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(inputFile);
					doc.getDocumentElement().normalize();
			        NodeList nList = doc.getElementsByTagName("business");
			        Boolean changed = false;
			        for(int i = 0; i < data.size(); i++)
			        {
			        	Element e = (Element)nList.item(i);
			        	Business b = data.get(i); 
			        	for(int j = b.feedbackCount; j < b.feedback.size(); j++) {			        		
			        		Element fe = doc.createElement("feedback");			  
			        		Element de = doc.createElement("date");
			        		de.setTextContent(b.feedback.get(j).date.toString());
			        		fe.appendChild(de);
			        		Element re = doc.createElement("rating");
			        		de.setTextContent(Float.toString(b.feedback.get(j).rating));
			        		fe.appendChild(re);
			        		Element ce = doc.createElement("comment");
			        		de.setTextContent(b.feedback.get(j).comment);
			        		fe.appendChild(ce);
			        		e.appendChild(fe);
			        		changed = true;
			        	}
			        	b.feedbackCount = b.feedback.size();
			        }
			        if(changed) {
			        	TransformerFactory transformerFactory = TransformerFactory.newInstance();
			    		Transformer transformer = transformerFactory.newTransformer();
			    		DOMSource source = new DOMSource(doc);
			    		StreamResult result = new StreamResult(inputFile);
			    		transformer.transform(source, result);
			        }
				} catch (Exception e) {
					
				}
			}
		}, 1000,1000);//600000, 600000);
		
		
		Feedback f = new Feedback();
		f.rating = 3.5f;
		f.comment = "Added in code";
		f.date = new Date();
		data.get(2).feedback.add(f);
		
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
