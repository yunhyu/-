package shapes;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

public class GOval extends GShape{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4756248224509222529L;

	public GOval() {
		super();
		this.origin = new Ellipse2D.Double();
		this.drawing = origin;
	}
	@Override
	public void initialize(Point start) {
		this.start = start;
	}
	@Override
	public void keep(Point end) {
		int[] dummy = super.transPoint(start, end);
		Ellipse2D oval = (Ellipse2D)this.origin;
		oval.setFrame(dummy[0],dummy[1],dummy[2],dummy[3]);
	}
	
	@Override
	public GShape finish() {
		Rectangle r = this.origin.getBounds();
		if(r.width<1&&r.height<1) {
			return null;
		}
		this.finalizeTransforming();
		return this;
	}
}
