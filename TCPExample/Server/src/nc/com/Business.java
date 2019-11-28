package nc.com;

import java.util.*;
import java.io.Serializable;

public class Business extends Location implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2435070298066651844L;
	public int id;
	public String name;
	public String address;
	public String website;		
	public float averageRating;
	
	public void setAverageRating()
	{
		float total = 0;
		for(int i = 0; i < feedback.size(); i++)
			total += feedback.get(i).rating;		
		averageRating = total/feedback.size();
	}	
	
	public transient List<Feedback> feedback;
	public transient int feedbackCount;
	
	public Business() 
	{
		super(0,0);
		feedback = new ArrayList<Feedback>();
	}
	
}
