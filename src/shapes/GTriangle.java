package shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class GTriangle extends GShape {

	
	private Polygon polygon;
	
	public GTriangle() {
		super();
		this.shape = new Polygon();
		this.polygon = (Polygon)this.shape;
	}
	
	@Override
	public void initialize(Point start, Point mouse) {
		this.resize(start, mouse);
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
		int[] dummy = super.transPoint(start, end);
		this.setAnchorBounds(dummy[0],dummy[1],dummy[2],dummy[3]);
		setTriangle(this.polygon, dummy[0],dummy[1],dummy[2],dummy[3]);
	}
	@Override
	public void finishResize() {
		this.setAnchorLocation();
	}
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		this.setTriangle(this.polygon, this.x, this.y, this.width, this.height);
	}
	
	private void setTriangle(Polygon polygon, int x, int y, int w, int h) {
		polygon.reset();
		polygon.addPoint(x, y+h);
		polygon.addPoint(x+w, y+h);
		polygon.addPoint(x+(w/2),y);
	}

	@Override
	public boolean onShape(Point mouse) {
		if(this.shape.contains(mouse)) {
			if(super.innerColor!=null) {
				return true;
			}
			Polygon ploygon = new Polygon();
			setTriangle(ploygon, x+6, y+6, width-12, height-12);
			return !ploygon.contains(mouse);
		}
		return false;
	}
	
}
