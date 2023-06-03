package transformer;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Vector;

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
	public abstract void keepTransform(Point end);
	public abstract GShape finalizeTransform(Color in, Color line);
	
	public void continueTransform(Point p) {}
	
}
