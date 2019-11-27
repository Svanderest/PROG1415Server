package nc.vanscoy;

import java.util.*;
import java.io.Serializable;

public class Business implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2435070298066651844L;
	int id;
	String name;
	String postal;
	String website;	
	
	transient List<Feedback> feedback;
	transient int feedbackCount;
	
	public Business()
	{
		feedback = new ArrayList<Feedback>();
	}
}
