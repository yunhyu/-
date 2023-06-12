package shapes;

import java.awt.Point;
import java.awt.Rectangle;

public class GRectangle extends GShape{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4832577751924617738L;
	public GRectangle(){
		super();
		this.origin = new Rectangle();
		this.drawing = origin;
	}
	@Override
	public void initialize(Point start) {
		this.start = start;
	}
	@Override
	public void keep(Point end) {
		int[] dummy = super.transPoint(start, end);
		Rectangle rect = (Rectangle)this.origin;
		rect.setRect(dummy[0],dummy[1],dummy[2],dummy[3]);
	}
	@Override
	public GShape finish() {
		Rectangle r = this.origin.getBounds();
		if(r.width<3&&r.height<3) {
			return null;
		}
		this.finalizeTransforming();
		return this;
	}
}
