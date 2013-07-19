import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class ConvexApplet extends Applet
implements MouseListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int width, height;
	int mx, my;  // the mouse coordinates
	boolean isButtonPressed = false;
	public ArrayList<HullPoint> currentPoints = new ArrayList<HullPoint>();
	public ArrayList<HullPoint> hull = new ArrayList<HullPoint>();
	public Button run;
	public Button clearButton;
	public boolean isHull;
	public boolean clear = false;
	

	public void init() {
		this.isHull = false;
		width = getSize().width;
		height = getSize().height;
		setBackground( Color.black );
		setSize(500,500);
		mx = width/2;
		my = height/2;

		setLayout(new FlowLayout());
		this.run = new Button("Run");
		this.clearButton = new Button("Clear");
		run.addActionListener(this);
		clearButton.addActionListener(this);
		addMouseListener(this);
		add(this.run);
		add(this.clearButton);
	}


	public void mousePressed( MouseEvent e ) {  // called after a button is pressed down
		HullPoint h = new HullPoint(e.getX(), e.getY());
		this.currentPoints.add(h);
		repaint();
	}
	public void paint(Graphics g) {
		for(HullPoint h:this.currentPoints)
		{
			g.setColor(Color.CYAN);
			g.fillOval((int)h.x-5,(int)h.y-5, 5, 5);
		}
		if(this.isHull) {
			if(this.hull.size() > 1) {
				for(int i = 0; i < this.hull.size()-1; i++) {
					g.setColor(Color.GREEN);
					HullPoint first = this.hull.get(i);
					HullPoint second = this.hull.get(i+1);
					g.drawLine((int)first.x-2,(int)first.y-2,(int)second.x-2,(int)second.y-2);
				}
				HullPoint last = this.hull.get(this.hull.size()-1);
				HullPoint first = this.hull.get(0);
				g.drawLine((int)last.x-2, (int)last.y-2, (int)first.x-2, (int)first.y-2);
			}
			this.isHull = false;
		}
	}
	public void clear() {
		this.currentPoints = new ArrayList<HullPoint>();
		this.hull = new ArrayList<HullPoint>();
		repaint();
	}

@Override
public void actionPerformed(ActionEvent event) {
	if(event.getSource() == this.run)
	{
		this.hull = this.getHullArray(this.currentPoints);
		this.isHull = true;
		repaint();
	}
	else if(event.getSource() == this.clearButton)
	{
		this.clear();
	}

}


@Override
public void mouseClicked(MouseEvent arg0) {
	
}


@Override
public void mouseEntered(MouseEvent arg0) {
}


@Override
public void mouseExited(MouseEvent arg0) {
}


@Override
public void mouseReleased(MouseEvent arg0) {
}
@SuppressWarnings("unchecked")
public ArrayList<HullPoint> getHullArray(ArrayList<HullPoint> a)
{
	Collections.sort(a);
	ArrayList<HullPoint> c = this.hull(a);
	return c;
} 
/**
 * Calculates whether a point a is clockwise from another point b.
 * @param a HullPoint
 * @param b HullPoint.
 * @return boolean, True if a is clockwise from b. False if a is 
 * counterclockwise from b.
 */

public boolean isAClockwiseFromB(HullPoint a, HullPoint b)
{
	if(a.cross(b) > 0)
	{
		return true;
	}
	return false;
}
/**
 * Calculates the hull on a set of sorted points recursively through
 * a divide and conquer method.
 * @param points, sorted array of points. 
 * @return HullPoint[], array of HullPoints that are on the hull of
 * the inputed points.
 */
public ArrayList<HullPoint> hull(ArrayList<HullPoint> points)
{
	if(points.size() > 3)
	{
		ArrayList<HullPoint> left = new ArrayList<HullPoint>();
		ArrayList<HullPoint> right = new ArrayList<HullPoint>();
		for(int j = 0; j < (int)points.size()/2; j++)
		{
			left.add(points.get(j));
		}
		for(int j = (int)points.size()/2; j<points.size(); j++)
		{
			right.add(points.get(j));
		}
		ArrayList<HullPoint> l = this.hull(left);
		ArrayList<HullPoint> r = this.hull(right);

		return merge(l, r);
	}
	return this.putInClockwiseOrder(points);
}
public int getLeftMostIndex(ArrayList<HullPoint> a)
{
	int index = 0;
	for(int i=1; i<a.size(); i++)
	{
		if(a.get(index).x > a.get(i).x)
			index = i;
	}
	return index;
}
public ArrayList<HullPoint> putInClockwiseOrder(ArrayList<HullPoint> unordered)
{
	if(unordered.size() == 1)
		return unordered;
	ArrayList<HullPoint> ordered = new ArrayList<HullPoint>();
	ordered.add(unordered.get(0));
	int counter = 0;
	ArrayList<HullPoint> skipped = new ArrayList<HullPoint>();
	for(int i=1; i<unordered.size()-1; i++) {
		if(this.isClockwiseTurn(ordered.get(counter), unordered.get(i), unordered.get(i+1))) {
			ordered.add(unordered.get(i));
			counter++;
		}
		else {
			skipped.add(unordered.get(i));
		}
	}
	if(unordered.size() > 1)
		ordered.add(unordered.get(unordered.size()-1));
	for(int i=skipped.size()-1; i>=0; i--)
		ordered.add(skipped.get(i));
	return ordered;
}
/**
 * Function to merge two Hulls together.
 * @param left Hullpoint[] to be merged.
 * @param right Hullpoint[] to be merged.
 * @return HullPoint[] of points on the hull of the total points 
 * in the left and right hull.
 */
public ArrayList<HullPoint> merge(ArrayList<HullPoint> left, ArrayList<HullPoint> right)
{
	ArrayList<HullPoint> merged = new ArrayList<HullPoint>();

	// While the line is not upper tangent to 
	// the left and not upper tangent to the right 
	if((left.size() + right.size()) < 5) {

		for(int a=0; a<left.size(); a++) {
			merged.add(left.get(a));
		}
		for(int b=0; b<right.size(); b++) {
			merged.add(right.get(b));
		}
		if(left.size() + right.size() == 4) 
			return this.mergeSizeFour(this.putInClockwiseOrder(merged));
		return this.putInClockwiseOrder(merged);
	}

	left = this.putInClockwiseOrder(left);
	right = this.putInClockwiseOrder(right);

	int[] indexes = this.initializeIndexes(left, right);

	// While not upper tangent to left and right
	// indexes[0] = leftIndex		indexes[3] = rightIndex
	// indexes[1] = leftCCW			indexes[4] = rightCCW
	// indexes[2] = leftCW			indexes[5] = rightCW
	while(!(this.isUpperTangentToLeft(right.get(indexes[3]), left, indexes))
			|| !(this.isUpperTangentToRight(left.get(indexes[0]), right, indexes)))
	{

		// While not upper tangent to left, move right points in CW order
		while(!(this.isUpperTangentToLeft(right.get(indexes[3]), left, indexes)))
		{
			indexes = this.incrementLeftIndexesCCW(left, indexes);
		}
		// While not upper tangent to right, move left points in CCW order
		while(!(this.isUpperTangentToRight(left.get(indexes[0]), right, indexes)))
		{
			indexes = this.incrementRightIndexesCW(right, indexes);
		}
	}
	int leftUpperIndex = indexes[0]; 
	int rightUpperIndex = indexes[3];

	indexes = this.initializeIndexes(left, right);
	// right, left, right, left
	while(!(this.isLowerTangentToLeft(right.get(indexes[3]), left, indexes))
			|| !(this.isLowerTangentToRight(left.get(indexes[0]), right, indexes)))
	{
		// While not upper tangent to left, move right points in CW order
		while(!(this.isLowerTangentToLeft(right.get(indexes[3]), left, indexes)))
		{
			indexes = this.incrementLeftIndexesCW(left, indexes);
		}
		// While not upper tangent to right, move left points in CCW order
		while(!(this.isLowerTangentToRight(left.get(indexes[0]), right, indexes)))
		{
			indexes = this.incrementRightIndexesCCW(right, indexes);
		}
	}
	int leftLowerIndex = indexes[0];
	int rightLowerIndex = indexes[3];

	merged = this.addMergedPoints(leftUpperIndex, leftLowerIndex, rightUpperIndex, rightLowerIndex, left, right);

	return merged;
}
public ArrayList<HullPoint> mergeSizeFour(ArrayList<HullPoint> points)
{
	for(int i=0; i<points.size(); i++)
	{
		if(this.isClockwiseTurn(points.get(i),points.get(this.getNextCCWIndex(i, points)),points.get(this.getNextCWIndex(i,points))))
		{
			points.remove(i);
		}

	}
	return points;
}
/**
 * Calculates whether the turn from origin-a to origin-b is clockwise.
 * @param origin Point of reference.
 * @param a Point making first line with origin.
 * @param b Point making second line with origin.
 * @return boolean, True if the line from origin-a makes a clockwise 
 * turn to the line origin-b. False if it makes a counterclockwise turn.
 */
public boolean isClockwiseTurn(HullPoint origin, HullPoint a, HullPoint b)
{
	HullPoint pointA = new HullPoint(a.x-origin.x, a.y-origin.y);
	HullPoint pointB = new HullPoint(b.x-origin.x, b.y-origin.y);
	return this.isAClockwiseFromB(pointB, pointA);
}
public int getRightMostIndex(ArrayList<HullPoint> points) {
	int index = 0; 
	for(int i=1; i<points.size(); i++) {
		if(points.get(i).x > points.get(index).x)
			index = i;
	}
	return index;
}

public int getNextCCWIndex(int index, ArrayList<HullPoint> points) {
	if(index == 0)
		return points.size()-1;
	return index-1;
}
public int getNextCWIndex(int index, ArrayList<HullPoint> points) {
	if(index == points.size()-1)
		return 0;
	return index+1;
}
public boolean isUpperTangentToLeft(HullPoint rightPoint, ArrayList<HullPoint> left, int[] a)
{
	if(!(this.isClockwiseTurn(left.get(a[0]), rightPoint, left.get(a[2]))))
		return false;
	else if(!(this.isClockwiseTurn(left.get(a[0]), rightPoint, left.get(a[1])))) // leftCCW (last)
		return false;
	return true;
}
public boolean isUpperTangentToRight(HullPoint leftPoint, ArrayList<HullPoint> right, int[] a)
{
	if(this.isClockwiseTurn(right.get(a[3]), leftPoint, right.get(a[5]))) // right, rightCW
		return false;
	else if(this.isClockwiseTurn(right.get(a[3]), leftPoint, right.get(a[4]))) // right, rightCCW
		return false;
	return true;
}
public boolean isLowerTangentToLeft(HullPoint rightPoint, ArrayList<HullPoint> left, int[] a)
{
	if(this.isClockwiseTurn(left.get(a[0]),rightPoint,left.get(a[2]))) // left, leftCW ?
		return false;
	else if(this.isClockwiseTurn(left.get(a[0]),rightPoint,left.get(a[1]))) // left, leftCCW
		return false;
	return true;
}
public boolean isLowerTangentToRight(HullPoint leftPoint, ArrayList<HullPoint> right, int[] a)
{
	if(!(this.isClockwiseTurn(right.get(a[3]), leftPoint,right.get(a[5])))) // right, rightCW
		return false;
	else if(!(this.isClockwiseTurn(right.get(a[3]),leftPoint,right.get(a[4])))) // right, rightCCW
		return false;
	return true;
}
public int[] incrementLeftIndexesCCW(ArrayList<HullPoint> left, int[] a)
{
	a[2] = a[0];
	a[0] = a[1];
	a[1] = this.getNextCCWIndex(a[1], left);
	return a;
}
public int[] incrementLeftIndexesCW(ArrayList<HullPoint> left, int[] a)
{
	a[1] = a[0];
	a[0] = a[2];
	a[2] = this.getNextCWIndex(a[2], left);
	return a;
}
public int[] incrementRightIndexesCW(ArrayList<HullPoint> right, int[] a)
{
	/*
		this.rightIndexCCW = this.rightIndex;
		this.rightIndex = this.rightIndexCW;
		this.rightIndexCW = this.getNextCWIndex(this.rightIndexCW, right);
	 */
	a[4] = a[3];
	a[3] = a[5];
	a[5] = this.getNextCWIndex(a[5], right);
	return a;
}
public int[] incrementRightIndexesCCW(ArrayList<HullPoint> right, int[] a)
{
	/*
		this.rightIndexCW = this.rightIndex;
		this.rightIndex = this.rightIndexCCW;
		this.rightIndexCCW = this.getNextCCWIndex(this.rightIndexCCW, right);
	 */
	a[5] = a[3];
	a[3] = a[4];
	a[4] = this.getNextCCWIndex(a[4], right);
	return a;
}
public int[] initializeIndexes(ArrayList<HullPoint> left, ArrayList<HullPoint> right)
{
	int[] a = new int[6];
	// Indexes for left, leftCCW, and leftCW
	a[0] = this.getRightMostIndex(left);
	a[1] = this.getNextCCWIndex(a[0], left);
	a[2] = this.getNextCWIndex(a[0], left);
	// Indexes for right, rightCCW, and rightCW
	a[3] = 0;
	a[4] = this.getNextCCWIndex(0, right);
	a[5] = this.getNextCWIndex(0, right);
	return a;
}
public ArrayList<HullPoint> addMergedPoints(int leftUpperIndex, int leftLowerIndex, int rightUpperIndex, int rightLowerIndex, ArrayList<HullPoint> left, ArrayList<HullPoint> right)
{
	ArrayList<HullPoint> merged = new ArrayList<HullPoint>();
	int a = leftLowerIndex;
	int b = rightUpperIndex;

	if(leftUpperIndex == leftLowerIndex)
		merged.add(left.get(leftUpperIndex));
	else if(left.size() == 2)
	{
		merged.add(left.get(leftLowerIndex));
		merged.add(left.get(leftUpperIndex));
	}
	else if(leftLowerIndex == 0)
	{
		for(int i = 0; i<=leftUpperIndex; i++)
			merged.add(left.get(i));
	}
	else {
		while(!(a==leftUpperIndex))
		{
			merged.add(left.get(a));
			if(a+1 == left.size())
				a=0;
			else
				a++;
		}
		merged.add(left.get(leftUpperIndex));
	}
	int index = this.getNextCCWIndex(rightLowerIndex, right);

	if(rightUpperIndex == rightLowerIndex)
		merged.add(right.get(rightUpperIndex));
	else if(right.size() == 2)
	{
		merged.add(right.get(rightLowerIndex));
		merged.add(right.get(rightUpperIndex));
	}	
	else if(rightLowerIndex == 1 && rightUpperIndex == 0)
	{
		merged.add(right.get(0));
		merged.add(right.get(1));
	}
	else { 
		while(!(b==index))
		{
			merged.add(right.get(b));
			b++;
			if(b==right.size())
				b=0;
		}
		merged.add(right.get(index));
		merged.add(right.get(rightLowerIndex));
	}
	return this.putInClockwiseOrder(merged);
}



}
