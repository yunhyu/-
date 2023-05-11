package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class GLine extends GShape{

	private Line2D line;
	
	public GLine() {
		super();
		this.isSelected = false;
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
	public void resize(Point start, Point end) {
//		int dx = end.x - start.x;
//		int dy = end.y - start.y;
//		Point p1, p2;
//		if(this.quadrant%2==0) {
//			this.initialize(start, end);
//			if(dx<0) {
//				
//			}
//		}else {
//			p1 = new Point(start.x, end.y);
//			p2 = new Point(end.x, start.y);
//			this.initialize(p1, p2);
//		}
		
		this.initialize(start, end);
	}
	@Override
	public void finishResize() {
		Point p1 = new Point();
		Point p2 = new Point();
		p1.setLocation(line.getP1());
		p2.setLocation(line.getP2());
		int[] dummy = this.transPoint(p1, p2);
		this.setAnchorBounds(dummy[0],dummy[1],dummy[2],dummy[3]);
		this.setAnchorLocation();
	}
	@Override
	public boolean onShape(Point mouse) {
		return this.line.ptLineDist(mouse)<3;
	}
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		Point2D p1 = this.line.getP1();
		Point2D p2 = this.line.getP2();
		p1.setLocation(p1.getX()+dx, p1.getY()+dy);
		p2.setLocation(p2.getX()+dx, p2.getY()+dy);
		this.line.setLine(p1, p2);
		this.setAnchorLocation();
	}
	private boolean angle(Point start, Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		return dx*dy>0;
	}
	/**
	 * Returns byte between -1 and 7. Returns -1 when no anchor is under the Point. 
	 * Anchors are on every edge of this line.
	 * @param I Anchor number are ordered like this :
	 * @param I 6 ----- 2
	 * @param I x ----- x2
	 * @return 
	 * Returns byte between -1 and 7. Returns -1 when no anchor is under the Point.
	 * A number represents an anchor. Follow the text up above.*/
//	@Override
//	public byte grabAnchor(Point mouse) {
//		if(super.isInRectRange(x-2, y-2, 4, 4, mouse))return 6;
//		else if(super.isInRectRange(x+width-2, y+height-2, 4, 4, mouse))return 2;
//		return -1;
//	}
	@Override
	public void drawAnchors(Graphics g) {
		if(isSelected) {
			g.setColor(Color.white);
			g.fillRect((int)(line.getX1()-2), (int)(line.getY1()-2), 4, 4);
			g.fillRect((int)(line.getX2()-2), (int)(line.getY2()-2), 4, 4);
			g.setColor(Color.black);
			g.drawRect((int)(line.getX1()-2), (int)(line.getY1()-2), 4, 4);
			g.drawRect((int)(line.getX2()-2), (int)(line.getY2()-2), 4, 4);
		}
	}
//	@Override
//	public Point getAnchor(byte anchorNum) {
//		Point dummy = new Point();
//		if(anchorNum==0)dummy.setLocation(this.line.getX1(), this.line.getY1());
//		else if(anchorNum==4)dummy.setLocation(this.line.getX2(), this.line.getY2());
//		return dummy;
//	}
}
