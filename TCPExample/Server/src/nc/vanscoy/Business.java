package nc.vanscoy;

import java.util.*;;
import java.io.Serializable;

public class Business implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2435070298066651844L;
	String name;
	String postal;
	String website;	
	transient List<Feedback> feedback;
}
