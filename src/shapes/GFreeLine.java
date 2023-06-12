package shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class GFreeLine extends GShape{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2434087033151626270L;
	public GFreeLine() {
		super();
		this.origin = new Path2D.Double();
		this.drawing = this.origin;
	}
	public GFreeLine(Shape shape) {
		super();
		this.origin = shape;
		this.drawing = this.origin;
		this.start = new Point();
		this.setCenterOrigin();
		this.finalizeTransforming();
	}
	
	@Override
	public void initialize(Point start) {
		Path2D p = (Path2D)origin;
		p.moveTo(start.x, start.y);
		this.start = start;
	}
	@Override
	public void keep(Point end) {
		Path2D.Double p = (Path2D.Double)this.origin;
		p.lineTo(end.x, end.y);
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
		if(distance<5) {
			path.lineTo(start.getX(), start.getY());
			return new GPolygon(path);
		}else {
			this.finalizeTransforming();
			return this;
		}
	}
	@Override
	public boolean grab(Point mouse) {
		return this.drawing.contains(mouse);
	}
}
