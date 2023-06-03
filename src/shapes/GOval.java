package shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

public class GOval extends GShape{

	public GOval() {
		super();
		this.shape = new Ellipse2D.Double();
	}
	@Override
	public void initialize(Point start) {
		this.start = start;
	}
	@Override
	public void keep(Point end) {
		int[] dummy = super.transPoint(start, end);
		Ellipse2D oval = (Ellipse2D)this.shape;
		oval.setFrame(dummy[0],dummy[1],dummy[2],dummy[3]);
	}
	
	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		Rectangle r = this.shape.getBounds();
		if(r.width<1&&r.height<1) {
			return null;
		}
		this.innerColor = innerColor;
		this.lineColor = lineColor;
		this.finalizeTransforming();
		return this;
	}
}
