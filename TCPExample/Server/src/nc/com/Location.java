package nc.com;
import java.io.Serializable;

public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public double lg;
	public double lt;
	
	public double getDistance(Location l)
	{
		double deg = Math.sqrt(Math.pow((lt - l.lt),2) + Math.pow(lg -l.lg, 2));
		return deg * 60 * 60 * 30; 		
	}
	
	public Location(double lg, double lt)
	{
		this.lg = lg;
		this.lt = lt;
	}
}
