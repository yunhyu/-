package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;

public class GPolygon extends GFreeLine {

	private Point animationPoint;
	private Polygon polygon;
	
	public GPolygon() {
		super();
		this.shape = new Polygon();
		this.polygon = (Polygon)this.shape;
	}
	public GPolygon(Collection<Integer> x, Collection<Integer> y, Rectangle size) {
		this();
		this.xCoordinate.addAll(x);
		this.yCoordinate.addAll(y);
		this.minX=size.x;
		this.minY=size.y;
		this.maxX=size.x+size.width;
		this.maxY=size.y+size.height;
		this.reset();
		this.complete = true;
		this.finishResize();
	}
	private void reset() {
		this.polygon.reset();
		Iterator<Integer> x = xCoordinate.iterator();
		Iterator<Integer> y = yCoordinate.iterator();
		
		while(x.hasNext()) this.polygon.addPoint(x.next(), y.next());
	}
	
	@Override
	public void initialize(Point start, Point mouse) {
		if(this.complete) {
			//TODO resize
		}else {
			animationPoint = mouse;
		}
	}

	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		int last = this.xCoordinate.size()-1;
		if(last<=0) {
			return null;
		}
		this.complete = true;
		this.innerColor = innerColor;
		this.lineColor = lineColor;
		Point p1 = new Point(this.xCoordinate.get(0),this.yCoordinate.get(0));
		Point p2 = new Point(this.xCoordinate.get(last),this.yCoordinate.get(last));
		double distance = p1.distance(p2);
		if(distance>5) {
			Rectangle bounds = new Rectangle
					(this.minX,this.minY,this.maxX-this.minX,this.maxY-this.minY);
			return new GFreeLine(this.xCoordinate,this.yCoordinate,bounds);
		} 
		this.reset();
		finishResize();
		return this;
	}
	@Override
	protected void resize(Point start, Point end) {
		
	}
	@Override
	public void finishResize() {
		this.x = this.minX;
		this.y = this.minY;
		this.width = this.maxX - this.minX;
		this.height = this.maxY - this.minY;
		this.center.setLocation(x+(width/2), y+(height/2));
		this.setAnchorLocation();
	}
	@Override
	public void addPoint(Point p) {
//		this.print(p);
//		this.print(this.xCoordinate.toString());
		int size = this.xCoordinate.size()-1;
		if(size==-1 || !(p.x==this.xCoordinate.get(size) && p.y==this.yCoordinate.get(size))) {
			this.setMaxMinCoordinate(p);
			this.xCoordinate.add(p.x);
			this.yCoordinate.add(p.y);
		}
	}
	@Override
	public void draw(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		if(this.complete) {
			if(this.lineColor!=null) {
				g2D.setColor(lineColor);
				g2D.draw(shape);
			}
			if(this.innerColor!=null) {
				g2D.setColor(innerColor);
				g2D.fill(shape);
			}
		}else {
			super.draw(g2D);
			int size = this.xCoordinate.size()-1;
			Point p = new Point(this.xCoordinate.get(size),this.yCoordinate.get(size));
			g2D.drawLine(p.x,p.y,animationPoint.x,animationPoint.y);
		}
		this.drawAnchors(g2D);
	}
	@Override
	public boolean grab(Point mouse) {
		return this.polygon.contains(mouse);
	}
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		reset();
	}

}
