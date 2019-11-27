package nc.vanscoy;

import java.io.Serializable;
import java.util.Date;

public class Feedback implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7599947646777799117L;
	transient Date date;
	float rating;
	String comment; 
	int businessId;
}
