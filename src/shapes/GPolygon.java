package shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class GPolygon extends GShape {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4308203260662452561L;
	private boolean complete;
	private Point animationPoint;
	
	public GPolygon() {
		super();
		this.origin = new Path2D.Double();
		this.drawing = this.origin;
	}
	public GPolygon(Shape shape) {
		super();
		this.origin = shape;
		this.drawing = shape;
		this.complete = true;
		this.start = new Point();
		this.setCenterOrigin();
		this.finalizeTransforming();
	}
	
	@Override
	public void initialize(Point start) {
		this.start = start;
		Path2D p = (Path2D)this.origin;
		p.moveTo(start.x, start.y);
		this.animationPoint = start;
	}
	
	@Override
	public void keep(Point end) {
		animationPoint = end;
	}

	@Override
	public void addPoint(Point p) {
		Path2D path = (Path2D)this.origin;
		path.lineTo(p.getX(), p.getY());
	}

	@Override
	public GShape finish() {
		Rectangle r = this.origin.getBounds();
		if(r.width*r.height<=4) {
			return null;
		}
		Path2D path = (Path2D)this.origin;
		Point2D p = path.getCurrentPoint();
		double distance = this.start.distance(p);
		if(distance>5) return new GFreeLine(path);
		
		path.lineTo(start.getX(), start.getY());
		this.finalizeTransforming();
		this.complete = true;
		return this;
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if(!this.complete) {
			Path2D path = (Path2D)this.origin;
			Point2D p = path.getCurrentPoint();
			g.drawLine((int)p.getX(),(int)p.getY(),animationPoint.x,animationPoint.y);
		}
	}
}
