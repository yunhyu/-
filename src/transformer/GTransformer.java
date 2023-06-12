package transformer;

import java.awt.Point;
import java.awt.geom.AffineTransform;

import shapes.GShape;

public abstract class GTransformer {

	protected Point start, end;
	protected AffineTransform affineTransfrom;
	protected GShape shape;
	
	public GTransformer() {}
	public GTransformer(GShape shape) {
		this.shape = shape;
		this.affineTransfrom = new AffineTransform();
	}
	
	public void initTransform(Point start) {
		this.start = start;
	}
	public abstract void keepTransform(Point end, boolean shiftDown);
	public abstract GShape finalizeTransform(Point end);
	public void continueTransform(Point p) {}
	public void addText(String str) {}
	
}
