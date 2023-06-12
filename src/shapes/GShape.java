package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Vector;

import main.GContants.CShapes;
import main.GContants.EAnchors;
import valueObject.GShapeInfo;
/**
 * 
 * */
public abstract class GShape implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 237512273580602235L;
	/**
	 * To make {@code unselectRange} copied {@code shape} should translated to (0, 0)
	 * and scale down by {@link AffineTransform}. And then, unselecting shape should return to
	 * correct location. This field determines that. 
	 * 
	 * @see AffineTransform 
	 * */
	protected double unselectRangeX, unselectRangeY;
	
	protected double theta = 0;
	protected double groupTheta = 0;
	/**
	 * Represents whether this shape is selected.
	 * */
	protected boolean bSelected;
	protected boolean isGroupChild = false;
	protected StringBuffer text;
	protected Point start, center, axis;
	protected Shape origin, drawing, unselectRange;
	protected Color innerColor, lineColor, strColor;
	protected Vector<Ellipse2D> shearAnchor;
	protected AffineTransform unselectRangeMaker, affine;
	protected GAnchors gAnchors;
	
	public GShape(){
		this.text = new StringBuffer();
		this.center = new Point();
		this.axis = center;
		this.bSelected = false;
		this.unselectRange = new Rectangle();
		this.unselectRangeMaker = new AffineTransform();
		this.affine = new AffineTransform();
		this.lineColor = CShapes.DRAWING_COLOR;
		this.shearAnchor = new Vector<Ellipse2D>();
		this.gAnchors = new GAnchors();
		this.unselectRangeX = CShapes.UNSELECT_X;
		this.unselectRangeY = CShapes.UNSELECT_Y;
		this.strColor = Color.black;
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
	 * @return if made shape is not valid, returns null.
	 * */
	public abstract GShape finish();
	public GShape group(){return this;}
	public GShape ungroup(){return this;}
	public void delete() {}
	/**Draw this shape with given Graphics object.
	 * @param g Graphics object. Get it from where to draw this shape.*/
	public void draw(Graphics2D g) {
		if(this.innerColor!=null) {
			g.setColor(innerColor);
			g.fill(drawing);
		}
		if(this.lineColor!=null) {
			g.setColor(lineColor);
			g.draw(drawing);
		}
		g.setColor(this.strColor);
		Rectangle2D r2 = g.getFont().getStringBounds(text.toString(), g.getFontRenderContext());
		g.drawString(text.toString(), 
		(int)(center.x - r2.getWidth()/2), (int)(center.y - r2.getHeight()/2));
	}
	/**See whether mouse is on this shape.
	 * @param mouse Point that want to see if point is on this shape.
	 * @return Return true if Point is in this shape's range. Else return false.*/
	public boolean grab(Point mouse) {
		if(this.drawing.contains(mouse)) {
			if(innerColor!=null) return true;
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
		this.gAnchors.draw((Graphics2D)g);
	}
	/** 
	 * This method see the given point is on anchor. If you add 4 from anchorNum, 
	 * you can get anchorNum of opposite side. If added anchorNum is over 7, than subtract 8
	 * from it.
	 * <pre> 
	 * Anchor number are ordered like this :
	 * [ 6 - 5 - 4 ]
	 * [ 3 - - - 7 ]
	 * [ 0 - 1 - 2 ]
	 * </pre>
	 * @param x coordinate.
	 * @param y coordinate.
	 * @return 
	 * Returns byte between -1 and 7. Returns -1 when no anchor is under the Point.
	 * The number represents an anchor. Follow the text up above.
	 * */
	public EAnchors onShape(int x, int y ) {
		EAnchors anchor = this.gAnchors.onAnchor(x, y);
		if(anchor == null) {
			if(drawing.contains(x, y))return EAnchors.MM;
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
	
	public void appendText(char c) {
		text.append(c);
	}

	public void deleteChar() {
		if(text.length()!=0) text.deleteCharAt(text.length()-1);
	}
	/**
	 * Save all anchor's location according to anchor number. 
	 * Anchors are on every edge and side from a rectangle which has same location, size attribute.
	 * @param I Anchor number are ordered like this :
	 * @param I 6 - 5 - 4
	 * @param I 3 - - - 7
	 * @param I 0 - 1 - 2
	 * */
	protected void setAnchorLocation() {
		this.gAnchors.setPosition(this.origin, this.theta+this.groupTheta, this.axis);
	}
	/**Turn on/off selected mode.
	 * @param bSelected true to turn on selected mode. false to turn it off.
	 * */
	public void setSelected(boolean bSelected) {this.bSelected = bSelected;}
	public void setGroupChild(boolean bGrouped) {
		this.isGroupChild = bGrouped;
		if(!isGroupChild) {
			this.theta += this.groupTheta;
			this.groupTheta = 0;
		}
	}
	public void setStrColor(Color color) {this.strColor = color;}
	public void setLineColor(Color color) {this.lineColor = color;}
	public void setInnerColor(Color color) {
		this.innerColor = color;
		makeUnselectRange();
	}
	public void setAbsoluteAxis(Point nextAxis) {this.axis = nextAxis;}

	public boolean isSelected() {return this.bSelected;}
	public Point getCenter() {return this.center;}
	public Color getInnerColor() {return this.innerColor;}
	public Color getLineColor() {return this.lineColor;}
	public Shape getDrawingShape() {return this.drawing;}
	public Shape getOriginShape() {return this.origin;}
	public Point getAxis() {return axis;}

	/**
	 * @return Returns anchor's coordinate. 
	 * */
	public Point getAnchor(int anchor) {
		return this.gAnchors.getAnchor(anchor);
	}
	/**
	 * 
	 * */
	public void rotate(double theta) {
		if(this.isGroupChild) {
			this.groupTheta += theta;
		}else this.theta += theta;
		this.setCenterOrigin();
		this.setRotatedShape(this.origin, center, this.theta);
		this.setRotatedShape(this.drawing, axis, this.groupTheta);
		System.out.println();
	}
	protected void setRotatedShape(Shape origin, Point axis, double theta) {
		affine.setToTranslation(-axis.getX(), -axis.getY());
		Shape shape = affine.createTransformedShape(origin);
		
		affine.setToRotation(theta);
		shape = affine.createTransformedShape(shape);
		
		affine.setToTranslation(axis.getX(), axis.getY());
		this.drawing = affine.createTransformedShape(shape);
	}
	/**
	 * Transform this shape with {@code AffineTransform}.
	 * @see AffineTransform
	 * */
	public void translate(AffineTransform affine) {
		this.origin = affine.createTransformedShape(origin);
		this.drawing = affine.createTransformedShape(drawing);
	}
	public void scale(AffineTransform affine) {
		this.origin = affine.createTransformedShape(origin);
		this.setRotatedShape(this.origin, center, theta);
		this.setRotatedShape(this.drawing,axis, this.groupTheta);
	}
	/**
	 * Complete Shape transforming. {@code transform} does not determine shape's start point,
	 * center, unselect range, and anchor location.
	 * */
	public void finalizeTransforming() {
//		this.setRotatedShape();
		Rectangle r = this.drawing.getBounds();
		this.start.setLocation(r.x, r.y);
		this.center.setLocation(r.getCenterX(), r.getCenterY());
		//center를 finish 할 때 정하고 아핀변환으로 정하기... 근데 origin의 센터는 기존에 하던 대로 해도 될 듯. 
//		this.setAxis(center.getLocation());
		
		if(!this.isGroupChild) {
			this.setAbsoluteAxis(center.getLocation());
//			affine
//			affine.transform(center, center);
			if(this.theta>Math.PI) theta -= Math.PI;
			r = this.origin.getBounds();
			double tx = center.getX() - r.getCenterX();
			double ty = center.getY() - r.getCenterY();
			affine.setToTranslation(tx, ty);
			origin = affine.createTransformedShape(origin);	
		}else {
			if(this.groupTheta>Math.PI) groupTheta -= Math.PI;
		}
		
		this.makeUnselectRange();
		this.setAnchorLocation();
	}
	protected void setCenterOrigin() {
		Rectangle r = this.origin.getBounds();
		this.center.setLocation(r.getCenterX(), r.getCenterY());
	}
	private void makeUnselectRange() {
		if(this.innerColor == null) {
			unselectRangeMaker.setToScale(CShapes.UNSELECT_DEFAULT, CShapes.UNSELECT_DEFAULT);
			unselectRangeMaker.translate(-start.x, -start.y);
			unselectRange = unselectRangeMaker.createTransformedShape(drawing);
			double w = (drawing.getBounds().getWidth() - unselectRange.getBounds().getWidth())/unselectRangeX;
			double h = (drawing.getBounds().getHeight() - unselectRange.getBounds().getHeight())/unselectRangeY;
			unselectRangeMaker.setToTranslation(start.getX()+w, start.getY()+h);
			unselectRange = unselectRangeMaker.createTransformedShape(unselectRange);
		}
	}
	/**
	 * Make this object to follow GShapeInfo's attribute.
	 * */
	public void setAtribute(GShapeInfo info) {
		this.origin = info.getShape();
		this.innerColor = info.getInnerColor();
		this.lineColor = info.getLineColor();
		this.theta = info.getRadian();
		this.text = info.getText();
		
		Rectangle r = origin.getBounds();
		this.start = new Point(r.x, r.y);
		this.center.setLocation(r.getCenterX(), r.getCenterY());
		this.axis = center;
		this.setRotatedShape(this.origin, center, this.theta);
	}
	/**
	 * @return GShapeInfo object that contains all attributes. 
	 * You can make clones of this GShape object to use setAtribute() method.
	 * */
	public GShapeInfo getAllAttribute() {
		GShapeInfo info = new GShapeInfo();
		info.setName(getClass().getName());
		info.setInnerColor(innerColor);
		info.setLineColor(lineColor);
		info.setRadian(theta+this.groupTheta);
		info.setShape(origin);
		info.setText(text);
		return info;
	}
}
