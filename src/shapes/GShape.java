package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

public abstract class GShape {

	private int[] anchorX, anchorY;
	protected int x,y,height,width, angle;
	protected byte quadrant;
	protected Point center;
	protected boolean isSelected;
	protected Shape shape;
	protected Color innerColor, lineColor;
	
	public GShape(){
		this.center = new Point();
		this.isSelected = false;
		this.lineColor = new Color(30, 30, 30, 100);
		this.anchorX = new int[8];
		this.anchorY = new int[8];
	}
	/**for other shape*/
	public abstract void initialize(Point start, Point mouse);
	/**Only for Polygon.*/
	public void addPoint(Point p) {};
	/**
	 * Finalize shape making. This method must contain finishResize() method.
	 * */
	public abstract GShape finalize(Color innerColor, Color lineColor);
	/**
	 * initialize + adjust angle
	 * */
	public abstract void resize(Point start, Point mouse);
	/**
	 * This method finalizes only resizing. finalize() method determines size, color, etc.
	 * This method supplement setting() method.
	 * */
	public abstract void finishResize();
	/**Draw this shape with given Graphics object.
	 * @param g Graphics object. Get it from where to draw this shape.*/
	public void draw(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		if(this.lineColor!=null) {
			g2D.setColor(lineColor);
			g2D.draw(shape);
		}
		if(this.innerColor!=null) {
			g2D.setColor(innerColor);
			g2D.fill(shape);
		}
//		this.drawAnchors(g2D);
	}
	/**See whether mouse is on this shape.
	 * @param mouse Point that want to see if point is on this shape.
	 * @return Return true if Point is in this shape's range. Else return false.*/
	public boolean grab(Point mouse) {
		return this.shape.contains(mouse);
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
			if(dx>0) {
				point[0]=start.x;
				point[2]=dx;
				this.quadrant = 4;
			}else {
				point[2]= -dx;
				point[0]=end.x;
				this.quadrant = 3;
			}
		}else {
			point[3]= -dy;
			point[1]=end.y;
			if(dx>0) {
				point[2]=dx;
				point[0]=start.x;
				this.quadrant = 1;
			}else {
				point[2]= -dx;
				point[0]=end.x;
				this.quadrant = 2;
			}
		}
		return point;
	}
	/**Change this shape's location.
	 * @param dx = x1 - x2
	 * @param dy = y1 - y2*/
	public void move(int dx, int dy) {
//		System.out.println("move "+dx+", "+dy);
		this.x+=dx;
		this.y+=dy;
		this.center.setLocation(this.center.x+dx, this.center.y+dy);
		this.setAnchorLocation();
	}
	/**Draw resize anchors. Use it inside draw(Graphics g) method.
	 * @param g Graphics object.*/
	public void drawAnchors(Graphics g) {
		if(isSelected) {
			g.setColor(Color.black);
			g.drawRect(x, y, width, height);
			g.setColor(Color.white);
			for(int i=0;i<8;i++) g.fillRect(this.anchorX[i]-2, this.anchorY[i]-2, 4, 4);
			g.setColor(Color.black);
			for(int i=0;i<8;i++) g.drawRect(this.anchorX[i]-2, this.anchorY[i]-2, 4, 4);
		}
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
	public byte grabAnchor(Point mouse) {
		for (byte i=0;i<8;i++) if(isInRectRange(this.anchorX[i]-3,this.anchorY[i]-3,6,6,mouse)) {
			System.out.println(i);
			return i;
		}
		return -1;
	}
	/**
	 * See whether a rectangle contains a point.
	 * @param x Rectangle's x coordinate.
	 * @param y Rectangle's y coordinate.
	 * @param width Rectangle's width coordinate.
	 * @param height Rectangle's height coordinate.
	 * @param mouse The Point
	 * @return return true if point is in rectangle. If not, return false.
	 * */
	protected boolean isInRectRange(int x, int y, int width, int height, Point mouse) {
		return x<mouse.x&&x+width>mouse.x&&y<mouse.y&&y+height>mouse.y;
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
		this.anchorY[0]=this.y+this.height;
		this.anchorY[1]=this.y+this.height;
		this.anchorY[2]=this.y+this.height;
		this.anchorY[3]=this.center.y;
		this.anchorY[7]=this.center.y;
		this.anchorY[6]=this.y;
		this.anchorY[5]=this.y;
		this.anchorY[4]=this.y;
		
		this.anchorX[0]=this.x;
		this.anchorX[1]=this.center.x;
		this.anchorX[2]=this.x+this.width;
		this.anchorX[3]=this.x;
		this.anchorX[7]=this.x+this.width;
		this.anchorX[6]=this.x;
		this.anchorX[5]=this.center.x;
		this.anchorX[4]=this.x+this.width;
	}
	/**Turn on/off selected mode.
	 * @param selected true to turn on selected mode. false to turn it off.*/
	public void select(boolean selected) {this.isSelected = selected;}
	public void setInnerColor(Color color) {this.innerColor = color;}
	public void setLineColor(Color color) {this.lineColor = color;}
	public Color getInnerColor() {return this.innerColor;}
	public Color getLineColor() {return this.lineColor;}
	/**
	 * Returns x, y coordinate and size. Width is x size, and height is y size. 
	 * @return int[] bounds = {x,y,width,height}
	 * */
	public int[] getBounds() {
		int[] dummy = {x,y,width,height};
		return dummy;
	}
	public Point getCenter() {return this.center;}
	/**
	 * @return Returns anchor's coordinate. 
	 * */
	public Point getAnchor(byte anchorNum) {
		return new Point(this.anchorX[anchorNum],this.anchorY[anchorNum]);
	}
	/**for test*/
	public void print(String str) {System.out.println(str);}
	/**for test*/
	public void print(int i) {System.out.println(i);}
	/**for test*/
	public void print(Point p) {System.out.println("yeahA\t\t"+p);}
}
