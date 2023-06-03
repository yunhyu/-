package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Vector;

public class GFreeLine extends GShape{

	public GFreeLine() {
		super();
		this.shape = new Path2D.Double();
	}
	public GFreeLine(Shape shape, Color innerColor, Color lineColor) {
		super();
		this.shape = shape;
		this.start = new Point();
		this.center = new Point();
		this.lineColor = lineColor;
		this.innerColor = innerColor;
		this.finalizeTransforming();
	}
	
	@Override
	public void initialize(Point start) {
		Path2D p = (Path2D)shape;
		p.moveTo(start.x, start.y);
		this.start = start;
	}
	@Override
	public void keep(Point end) {
		Path2D.Double p = (Path2D.Double)this.shape;
		p.lineTo(end.x, end.y);
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
		if(distance<5) {
			path.lineTo(start.getX(), start.getY());
			return new GPolygon(path, innerColor, lineColor);
		}else {
			this.innerColor = innerColor;
			this.lineColor = lineColor;
			this.finalizeTransforming();
			return this;
		}
	}

	@Override
	public void draw(Graphics2D g) { 
		if(this.lineColor!=null) {
			g.setColor(lineColor);
			g.draw(shape);
		}
	}
	@Override
	public boolean grab(Point mouse) {
//		return this.shape.contains(mouse);
		return this.shape.getBounds().contains(mouse);
	}
}
