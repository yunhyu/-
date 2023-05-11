package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GShape;

public class GNPointDrawer extends GTransformer {

	public GNPointDrawer(Vector<GShape> drawingShape, Vector<Integer> selected) {
		super(drawingShape, selected);
	}

	@Override
	public void prepare(Point start) {
		this.start = start;
		this.keep(start);
	}
	
	@Override
	public void keep(Point p) {
		this.shape.initialize(null, p);
	}

	@Override
	public void addPoint(Point p) {
		this.shape.addPoint(p);
	}
	
	@Override
	public GShape finalize(Color in, Color line) {
		return this.shape.finalize(in, line);
	}

}
