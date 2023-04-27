package shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;

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
		if(this.width<1&&this.height<1) {
			return null;
		}
		this.innerColor = innerColor;
		this.lineColor = lineColor;
		this.finishResize();
		return this;
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
		
		this.polygon.reset();
		this.polygon.addPoint(x, y);
		this.polygon.addPoint(x+this.width, y);
		this.polygon.addPoint(movingP.x, movingP.y);
	}

}
