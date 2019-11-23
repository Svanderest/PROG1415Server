package nc.vanscoy;
import java.io.Serializable;

public class LocationMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public double lg;
	public double lt;
	
	public LocationMessage(double lg, double lt)
	{
		this.lg = lg;
		this.lt = lt;
	}
}
