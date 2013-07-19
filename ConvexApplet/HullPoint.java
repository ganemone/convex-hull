import java.awt.geom.Point2D.Double;

/**
 * A wrapper for the Point2D.Double double class.
 * @author nsprague
 * @version 5/5/2009
 */
@SuppressWarnings("rawtypes")
public class HullPoint extends Double implements Comparable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public HullPoint() {
		super();
	}
	public HullPoint(double arg0, double arg1) {
		super(arg0, arg1);
	}
	/**
	 * Calculates the cross product.
	 * @param HullPoint, the HullPoint for the cross product to be 
	 * applied to.
	 */
	public double cross(HullPoint b)
	{
		return this.x*b.y - this.y*b.x;
	}
	public int compareTo(Object otherPoint) throws ClassCastException
	{
		if (!(otherPoint instanceof HullPoint))
		      throw new ClassCastException("A HullPoint object expected.");
		if(this.x - ((HullPoint) otherPoint).x > 0)
			return 1;
		else if(this.x - ((HullPoint)otherPoint).x < 0)
			return -1;
		return 0; 
	}
	
}
