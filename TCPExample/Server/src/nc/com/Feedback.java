package nc.com;

import java.io.Serializable;
import java.util.Date;

public class Feedback implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7599947646777799117L;
	public transient Date date;
	public float rating;
	public String comment; 
	public int businessId;
}
