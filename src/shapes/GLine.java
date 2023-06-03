package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import main.GContants.EAnchors;

public class GLine extends GShape{

	public GLine() {
		super();
		this.shape = new Line2D.Double();
		this.makeAnchor(2);
	}
	
	@Override
	public void initialize(Point start) {
		this.start = start;
	}
	
	@Override
	public void keep(Point end) {
		Line2D line = (Line2D)shape;
		line.setLine(start, end);
	}

	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		Line2D line = (Line2D)shape;
		if(line.getP1().distance(line.getP2())<=0.3) return null;
		this.lineColor = lineColor;

		this.finalizeTransforming();
		return this;
	}
	
	@Override
	public boolean grab(Point mouse) {
		Point2D p1 = this.getAnchor(6);
		Point2D p2 = this.getAnchor(2);
		return Line2D.ptSegDist(p1.getX(), p1.getY(), p2.getX(), p2.getY(), mouse.getX(), mouse.getY())<10;	
	}//반대쪽 선도 되는 거 만들고 점수달라하기
	
	@Override
	public EAnchors onShape(int x, int y ) {
		EAnchors anchor = this.gAnchors.onAnchor(x, y);
		if(anchor == null) {
			if(grab(new Point(x, y)))return EAnchors.MM;
			else return null;
		}else {
			return anchor;
		}
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
	
//	@Override
//	public void drawAnchors(Graphics g) {
//		g.setColor(Color.white);
//		g.fillRect((int)(line.getX1()-2), (int)(line.getY1()-2), 4, 4);
//		g.fillRect((int)(line.getX2()-2), (int)(line.getY2()-2), 4, 4);
//		g.setColor(Color.black);
//		g.drawRect((int)(line.getX1()-2), (int)(line.getY1()-2), 4, 4);
//		g.drawRect((int)(line.getX2()-2), (int)(line.getY2()-2), 4, 4);
//	}
	
//	@Override
//	public Point getAnchor(byte anchorNum) {
//		Point dummy = new Point();
//		if(anchorNum==0)dummy.setLocation(this.line.getX1(), this.line.getY1());
//		else if(anchorNum==4)dummy.setLocation(this.line.getX2(), this.line.getY2());
//		return dummy;
//	}
}
