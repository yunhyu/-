package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class GLine extends GShape{

//	private int x2, y2;
	private Line2D line;
	
	public GLine() {
		this.isSelected = false;
		this.center = new Point();
		this.shape = new Line2D.Double();
		this.line = (Line2D)this.shape;
	}
	
	@Override
	public void initialize(Point start, Point end) {
		line.setLine(start, end);
	}

	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		if(this.line.getP1().distance(this.line.getP2())<=0.3) return null;
		this.lineColor = lineColor;
		finishResize();
		return this;
	}
	@Override
	public void finishResize() {
		int x = Math.abs((int)(this.line.getX1()-this.line.getX2())/2);
		int y = Math.abs((int)(this.line.getY1()-this.line.getY2())/2);
		this.center.setLocation(x, y);
	}
	@Override
	public boolean grab(Point mouse) {
		Point2D p1 = this.line.getP1();
		Point2D p2 = this.line.getP2();
		double distance = p1.distance(p2);
		if(mouse.distance(p1)+mouse.distance(p2)<=distance+0.5) {
			return true;
		}else return false;
	}
	@Override
	public void move(int dx, int dy) {
		Point2D p1 = this.line.getP1();
		Point2D p2 = this.line.getP2();
		p1.setLocation(p1.getX()+dx, p1.getY()+dy);
		p2.setLocation(p2.getX()+dx, p2.getY()+dy);
		this.line.setLine(p1, p2);
		this.finishResize();
	}
	/**
	 * Returns integer -1, 0 or  4. Returns -1 when no anchor is under the Point. 
	 * Anchors are on every edge of this line.
	 * @param I Anchor number are ordered like this :
	 * @param I 0 ----- 4
	 * @param I x ----- x2
	 * @return 
	 * Returns integer -1 or 0 or 4. Returns -1 when no anchor is under the Point.
	 * A number represents an anchor. Follow the text up above.*/
	@Override
	public byte grabAnchor(Point mouse) {
		if(super.isInRectRange((int)(line.getX1()-2), (int)(line.getY1()-2), 4, 4, mouse))return 0;
		else if(super.isInRectRange((int)(line.getX2()-2), (int)(line.getY2()-2), 4, 4, mouse))return 4;
		return -1;
	}
	@Override
	protected void drawAnchors(Graphics g) {
		if(isSelected) {
			g.setColor(Color.white);
			g.fillRect((int)(line.getX1()-2), (int)(line.getY1()-2), 4, 4);
			g.fillRect((int)(line.getX2()-2), (int)(line.getY2()-2), 4, 4);
			g.setColor(Color.black);
			g.drawRect((int)(line.getX1()-2), (int)(line.getY1()-2), 4, 4);
			g.drawRect((int)(line.getX2()-2), (int)(line.getY2()-2), 4, 4);
		}
	}
	@Override
	protected void setAnchorLocation() {}
	@Override
	public Point getAnchor(byte anchorNum) {
		Point dummy = new Point();
		if(anchorNum==0)dummy.setLocation(this.line.getX1(), this.line.getY1());
		else if(anchorNum==4)dummy.setLocation(this.line.getX2(), this.line.getY2());
		return dummy;
	}
}
