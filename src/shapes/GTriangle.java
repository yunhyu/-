package shapes;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class GTriangle extends GShape {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3675762278428893612L;
	private double shearRate; 
	private Polygon polygon;
	
	public GTriangle() {
		super();
		this.polygon = new Polygon();
		this.origin = this.polygon;
		this.drawing = polygon;
		this.shearRate = 0.5;
		this.unselectRangeY = 1.4;
	}
	
	@Override
	public void keep(Point end) {
		int[] dummy = super.transPoint(start, end);
		setTriangle(this.polygon, dummy[0],dummy[1],dummy[2],dummy[3]);
	}

	@Override
	public GShape finish() {
		this.origin = this.polygon;
		Rectangle r = this.origin.getBounds();
		if(r.width<12&&r.height<12) {
			return null;
		}
		this.finalizeTransforming();
		return this;
	}
	
	private void setTriangle(Polygon polygon, int x, int y, int w, int h) {
		polygon.reset();
		polygon.addPoint(x, y+h);
		polygon.addPoint(x+w, y+h);
		polygon.addPoint(x+(int)((double)w*this.shearRate),y);
	}
}
