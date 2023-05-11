package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GShape;

public class GDrawer extends GTransformer {

	public GDrawer(Vector<GShape> drawingShape, Vector<Integer> selected) {
		super(drawingShape, selected);
	}

	@Override
	public void keep(Point end) {
		this.shape.initialize(start, end);
	}

	@Override
	public GShape finalize(Color in, Color line) {
		return this.shape.finalize(in, line);
	}

}
