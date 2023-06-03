package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class GTriangle extends GShape {

	private double shearRate; 
	private Polygon polygon;
	
	public GTriangle() {
		super();
		this.polygon = new Polygon();
		this.shape = this.polygon;
		this.shearRate = 0.5;
		this.unselectRangeY = 1.4;
	}
	
	@Override
	public void keep(Point end) {
		int[] dummy = super.transPoint(start, end);
		setTriangle(this.polygon, dummy[0],dummy[1],dummy[2],dummy[3]);
	}

	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		Rectangle r = this.shape.getBounds();
		if(r.width<12&&r.height<12) {
			return null;
		}
		this.innerColor = innerColor;
		this.lineColor = lineColor;
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
