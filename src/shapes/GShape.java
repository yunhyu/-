package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import main.GContants.EAnchors;
import valueObject.GShapeInfo;
/**
 * 
 * */
public abstract class GShape {

	/**
	 * To make {@code unselectRange} copied {@code shape} should translated to (0, 0)
	 * and scale down by {@link AffineTransform}. And then, unselecting shape should return to
	 * correct location. This field determines that. 
	 * 
	 * @see AffineTransform 
	 * */
	protected double unselectRangeX, unselectRangeY;
	/**
	 * Represents whether this shape is selected.
	 * */
	protected boolean bSelected;
	protected boolean isGroupChild = false;
	protected Point start, center;
	protected Shape shape, unselectRange;
	protected Color innerColor, lineColor;
	protected Vector<Ellipse2D> shearAnchor;
	protected AffineTransform unselectRangeMaker;
	protected Point2D[] anchorLocation;
	protected GAnchors gAnchors;
	
	public GShape(){
		this.center = new Point();
		this.bSelected = false;
		this.unselectRange = new Rectangle();
		this.unselectRangeMaker = new AffineTransform();
		this.lineColor = new Color(30, 30, 30, 100);
		this.shearAnchor = new Vector<Ellipse2D>();
		this.gAnchors = new GAnchors();
		this.unselectRangeX = 2;
		this.unselectRangeY = 2;
		this.makeAnchor(8);
	}
	protected void makeAnchor(int num) {
		this.anchorLocation = new Point2D[num];
	}
	/**for other shape*/
	public void initialize(Point start) {
		this.start = start;
	}
	/**for other shape*/
	public abstract void keep(Point mouse);
	/**Only for Polygon.*/
	public void addPoint(Point p) {};
	/**
	 * Finalize shape making. This method must contain finishResize() method.
	 * */
	public abstract GShape finalize(Color innerColor, Color lineColor);
	/**Draw this shape with given Graphics object.
	 * @param g Graphics object. Get it from where to draw this shape.*/
	public void draw(Graphics2D g) {
		if(this.innerColor!=null) {
			g.setColor(innerColor);
			g.fill(shape);
		}
		if(this.lineColor!=null) {
			g.setColor(lineColor);
			g.draw(shape);
		}
//		unselectRangeMaker.setToScale(0.75, 0.75);
//		unselectRangeMaker.translate(-start.x, -start.y);
//		Shape s = unselectRangeMaker.createTransformedShape(shape);
//		double w = (shape.getBounds().getWidth() - s.getBounds().getWidth())/unselectRangeX;
//		double h = (shape.getBounds().getHeight() - s.getBounds().getHeight())/unselectRangeY;
//		unselectRangeMaker.setToTranslation(start.getX()+w, start.getY()+h);
//		s = unselectRangeMaker.createTransformedShape(s);
		g.setColor(Color.blue);
		g.draw(this.unselectRange);
	}
	/**See whether mouse is on this shape.
	 * @param mouse Point that want to see if point is on this shape.
	 * @return Return true if Point is in this shape's range. Else return false.*/
	public boolean grab(Point mouse) {
		if(this.shape.contains(mouse)) {
//			if(innerColor!=null) return true;
			return !unselectRange.contains(mouse);
		}
		return false;
	};
	/**
	 * This method get starting point and bound of a rectangle from given two Point.  
	 * @return Returns starting point and bound of rectangle. int[0] is x coordinate, 
	 * int[1] is y coordinate, int[2] is width, and int[3] is height.
	 * */
	protected int[] transPoint(Point start, Point end) {
		int[] point= new int[4];
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		if(dy>0) {
			point[3]=dy;
			point[1]=start.y;
		}else {
			point[3]= -dy;
			point[1]=end.y;
		}
		if(dx>0) {
			point[0]=start.x;
			point[2]=dx;
		}else {
			point[2]= -dx;
			point[0]=end.x;
		}
		return point;
	}
	/**Draw resize anchors. Use it inside draw(Graphics g) method.
	 * @param g Graphics object.*/
	public void drawAnchors(Graphics g) {
		g.setColor(Color.black);
		Rectangle r = this.shape.getBounds();
		g.drawRect(r.x, r.y, r.width, r.height);
		this.gAnchors.draw((Graphics2D)g, this.shape.getBounds());
	}
	/** 
	 * This method see the given point is on anchor. If you add 4 from anchorNum, 
	 * you can get anchorNum of opposite side. If added anchorNum is over 7, than subtract 8
	 * from it.
	 * @param I Anchor number are ordered like this :
	 * @param I 6 - 5 - 4
	 * @param I 3 - - - 7
	 * @param I 0 - 1 - 2
	 * @return 
	 * Returns byte between -1 and 7. Returns -1 when no anchor is under the Point.
	 * The number represents an anchor. Follow the text up above.
	 * */
	public EAnchors onShape(int x, int y ) {
		EAnchors anchor = this.gAnchors.onAnchor(x, y);
		if(anchor == null) {
			if(shape.contains(x, y))return EAnchors.MM;
			else return null;
		}else {
			return anchor;
		}
	}
	protected void addShear() {
		
	}
	/**
	 * See whether the Point is on shearAnchor.
	 * @return -1 is no searAnchor is under the Point.
	 * */
	public int grabShear(Point mouse) {
		for(int i=0; i<this.shearAnchor.size();i++) {
			if(this.shearAnchor.get(i).contains(mouse))return i;
		}
		return -1;
	}
	
	/**
	 * Override to use.
	 * */
	public void shear(int shearAnchor) {}
	/**
	 * Save all anchor's location according to anchor number. 
	 * Anchors are on every edge and side from a rectangle which has same location, size attribute.
	 * @param I Anchor number are ordered like this :
	 * @param I 6 - 5 - 4
	 * @param I 3 - - - 7
	 * @param I 0 - 1 - 2
	 * */
	protected void setAnchorLocation() {
		this.gAnchors.setPosition(this.shape);
	}
	/**Turn on/off selected mode.
	 * @param bSelected true to turn on selected mode. false to turn it off.
	 * */
	public void setSelected(boolean bSelected) {this.bSelected = bSelected;}
	
	public void group(boolean group) {this.isGroupChild = group;}
	
	public void setLineColor(Color color) {this.lineColor = color;}
	public void setInnerColor(Color color) {
		this.innerColor = color;
		makeUnselectRange();
	}

	public boolean isSelected() {return this.bSelected;}
	public boolean isGrouped() {return this.isGroupChild;}
	public Point getCenter() {return this.center;}
	public Point getStart() {return this.start;}
	public Color getInnerColor() {return this.innerColor;}
	public Color getLineColor() {return this.lineColor;}
	public Shape getShape() {return this.shape;}

	/**
	 * @return Returns anchor's coordinate. 
	 * */
	public Point getAnchor(int anchor) {
		return this.gAnchors.getAnchor(anchor);
	}
	
	public void transform(AffineTransform affine) {
		this.shape = affine.createTransformedShape(shape);
	}
	/**
	 * 
	 * */
	public void finalizeTransforming() {
		Rectangle r = this.shape.getBounds();
		this.start.setLocation(r.x, r.y);
		this.center.setLocation(r.getCenterX(), r.getCenterY());
		this.makeUnselectRange();
		this.setAnchorLocation();
	}
	private void makeUnselectRange() {
		if(this.innerColor == null) {
			unselectRangeMaker.setToScale(0.75, 0.75);
			unselectRangeMaker.translate(-start.x, -start.y);
			unselectRange = unselectRangeMaker.createTransformedShape(shape);
			double w = (shape.getBounds().getWidth() - unselectRange.getBounds().getWidth())/unselectRangeX;
			double h = (shape.getBounds().getHeight() - unselectRange.getBounds().getHeight())/unselectRangeY;
			unselectRangeMaker.setToTranslation(start.getX()+w, start.getY()+h);
			unselectRange = unselectRangeMaker.createTransformedShape(unselectRange);
		}
	}
	/**
	 * Make this object to follow GShapeInfo's attribute.
	 * */
	public void setAtribute(GShapeInfo info) {
		this.shape = info.getShape();
		this.innerColor = info.getInnerColor();
		this.lineColor = info.getLineColor();
		this.setAnchorLocation();
	}
	/**
	 * @return GShapeInfo object that contains all attributes. 
	 * You can make clones of this GShape object to use setAtribute() method.
	 * */
	public GShapeInfo getAllAttribute() {
		GShapeInfo info = new GShapeInfo();
		info.setInnerColor(innerColor);
		info.setLineColor(lineColor);
		info.setShape(shape);
		return info;
	}
}
