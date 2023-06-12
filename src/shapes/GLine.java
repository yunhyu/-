package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import main.GContants.CAnchors;
import main.GContants.EAnchors;

public class GLine extends GShape{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8082415159328906177L;
	private Point2D rotateAnchor;
	private Point2D[] anchor;
	
	public GLine() {
		super();
		this.origin = new Line2D.Double();
		this.drawing = this.origin;
		this.anchor = new Point2D[2];
		for(int i=0;i<anchor.length;i++) {
			anchor[i]=new Point2D.Double();
		}
		rotateAnchor = new Point2D.Double();
	}
	
	@Override
	public void initialize(Point start) {
		this.start = start;
	}
	
	@Override
	public void keep(Point end) {
		Line2D line = (Line2D)origin;
		line.setLine(start, end);
	}

	@Override
	public GShape finish() {
		Line2D line = (Line2D)origin;
		if(line.getP1().distance(line.getP2())<=1) return null;;
		this.finalizeTransforming();
		return this;
	}
	
	@Override
	public boolean grab(Point mouse) {
		Point2D p1 = this.anchor[0];
		Point2D p2 = this.anchor[1];
		return Line2D.ptSegDist(p1.getX(), p1.getY(), p2.getX(), p2.getY(), mouse.getX(), mouse.getY())<10;	
	}

	/**
	 * Returns byte between -1 and 7. Returns -1 when no anchor is under the Point. 
	 * Anchors are on every edge of this line.
	 * @param I Anchor number are ordered like this :
	 * @param I x ----- x2
	 * @return 
	 * */
	@Override
	public EAnchors onShape(int x, int y ) {
		EAnchors anchor = null;
		if(this.rotateAnchor.distance(x, y)<5.1)return EAnchors.RR;
		for(Point2D p:this.anchor) {
			if(p.distance(x, y)<3.1)return anchor = this.gAnchors.onAnchor(x, y);
		}
		if(anchor == null) {
			if(grab(new Point(x, y)))return EAnchors.MM;
		}
		return null;
	}
	@Override
	public void setInnerColor(Color color) {
		this.innerColor = Color.black;
	}
	
	@Override
	protected void setAnchorLocation() {
		super.setAnchorLocation();
		this.rotateAnchor.setLocation(center.x, center.y-CAnchors.ROTATE_ANCHOR_RADIUS*4);
		
		
		Rectangle r = this.drawing.getBounds();
		boolean isOnStart = Path2D.intersects(drawing.getPathIterator(null), new Rectangle(r.x-1, r.y-1, 2, 2));
		
		System.out.println(isOnStart);
		if(isOnStart) {
			anchor[0].setLocation(r.x, r.y);
			anchor[1].setLocation(r.x+r.getWidth(), r.y+r.height);
		}else {
			anchor[0].setLocation(r.x+r.getWidth(), r.y);
			anchor[1].setLocation(r.x, r.y+r.height);
		}
	}

	@Override
	public void drawAnchors(Graphics g) {
		int r = CAnchors.RESIZE_ANCHOR_RADIUS;
		g.setColor(Color.white);
		for(Point2D p : this.anchor) {
			g.fillOval((int)p.getX()-r, (int)p.getY()-r, 2*r, 2*r);
		}
		g.setColor(Color.black);
		for(Point2D p : this.anchor) {
			g.drawOval((int)p.getX()-r, (int)p.getY()-r, 2*r, 2*r);
		}
		r = CAnchors.ROTATE_ANCHOR_RADIUS;
		g.drawLine(center.x, center.y, center.x, center.y-(r*4));
		g.setColor(Color.white);
		Point2D p = this.rotateAnchor;
		g.fillOval((int)p.getX()-r, (int)p.getY()-r, 2*r, 2*r);
		g.setColor(Color.black);
		g.drawOval((int)p.getX()-r, (int)p.getY()-r, 2*r, 2*r);
	}
	
}
