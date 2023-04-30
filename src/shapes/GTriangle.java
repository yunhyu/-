package shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class GTriangle extends GShape {

	private Point p1, p2, movingP;
	private Polygon polygon;
	
	public GTriangle() {
		super();
		this.p1 = new Point();
		this.p2 = new Point();
		this.movingP = new Point();
		this.shape = new Polygon();
		this.polygon = (Polygon)this.shape;
	}
	
	@Override
	public void initialize(Point start, Point mouse) {
		int[] dummy = super.transPoint(start, mouse);
		this.x = dummy[0];
		this.y = dummy[1];
		this.width = dummy[2];
		this.height = dummy[3];
		setTriangle();
	}

	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		if(this.width<12&&this.height<12) {
			return null;
		}
		this.innerColor = innerColor;
		this.lineColor = lineColor;
		this.finishResize();
		return this;
	}
	@Override
	public void resize(Point start, Point end) {
		this.initialize(start, end);
	}
	@Override
	public void finishResize() {
		this.center.setLocation(x+(width/2), y+(height/2));
		this.setAnchorLocation();
	}
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		this.setTriangle();
	}
	
	private void setTriangle() {
		int y = this.y+this.height;
		this.p1.setLocation(x, y);
		this.p2.setLocation(x+this.width, y);
		this.movingP.setLocation(x+(this.width/2), this.y);
		
		setTriangle2(this.polygon, this.x, this.y, this.width, this.height);
	}
	private void setTriangle2(Polygon polygon, int x, int y, int w, int h) {
		polygon.reset();
		polygon.addPoint(x, y+h);
		polygon.addPoint(x+w, y+h);
		polygon.addPoint(x+(w/2),y);
	}

	@Override
	public boolean grab(Point mouse) {
		if(this.shape.contains(mouse)) {
			if(super.innerColor!=null) {
				return true;
			}
			Polygon ploygon = new Polygon();
			setTriangle2(ploygon, x+6, y+6, width-12, height-12);
			return !ploygon.contains(mouse);
		}
		return false;
	}
	
}
