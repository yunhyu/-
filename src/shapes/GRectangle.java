package shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape{

	public GRectangle(){
		super();
		this.shape = new Rectangle();
	}
	@Override
	public void initialize(Point start) {
		this.start = start;
	}
	@Override
	public void keep(Point end) {
		int[] dummy = super.transPoint(start, end);
		Rectangle rect = (Rectangle)this.shape;
		rect.setRect(dummy[0],dummy[1],dummy[2],dummy[3]);
	}
	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		Rectangle r = this.shape.getBounds();
		if(r.width<3&&r.height<3) {
			return null;
		}
		this.innerColor = innerColor;
		this.lineColor = lineColor;
		this.finalizeTransforming();
		return this;
	}
	
	public void reset() {
		Rectangle r = (Rectangle)this.shape;
		r.setSize(0, 0);
	}
}
