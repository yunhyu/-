package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class GPolygon extends GShape {

	private boolean complete;
	private Point animationPoint;
	
	public GPolygon() {
		super();
		this.shape = new Path2D.Double();
	}
	public GPolygon(Shape shape, Color innerColor, Color lineColor) {
		super();
		this.shape = shape;
		this.complete = true;
		this.start = new Point();
		this.center = new Point();
		this.lineColor = lineColor;
		this.innerColor = innerColor;
		this.finalizeTransforming();
	}
	
	@Override
	public void initialize(Point start) {
		this.start = start;
		Path2D p = (Path2D)this.shape;
		p.moveTo(start.x, start.y);
		this.animationPoint = start;
	}
	
	@Override
	public void keep(Point end) {
		animationPoint = end;
	}

	@Override
	public void addPoint(Point p) {
		Path2D path = (Path2D)this.shape;
		path.lineTo(p.getX(), p.getY());
	}

	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		Rectangle r = this.shape.getBounds();
		if(r.width*r.height<=4) {
			return null;
		}
		Path2D path = (Path2D)this.shape;
		Point2D p = path.getCurrentPoint();
		double distance = this.start.distance(p);
		if(distance>5) return new GFreeLine(path, innerColor, lineColor);
		
		path.lineTo(start.getX(), start.getY());
		this.innerColor = innerColor;
		this.lineColor = lineColor;
		this.finalizeTransforming();
		this.complete = true;
		return this;
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if(!this.complete) {
			Path2D path = (Path2D)this.shape;
			Point2D p = path.getCurrentPoint();
			g.drawLine((int)p.getX(),(int)p.getY(),animationPoint.x,animationPoint.y);
		}
	}
}
