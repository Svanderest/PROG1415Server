package nc.com;

import java.util.*;
import java.io.Serializable;

public class Business implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2435070298066651844L;
	public int id;
	public String name;
	public String postal;
	public String website;	
	
	public transient List<Feedback> feedback;
	public transient int feedbackCount;
	
	public Business()
	{
		feedback = new ArrayList<Feedback>();
	}
}
