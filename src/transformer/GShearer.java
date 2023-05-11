package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GShape;

public class GShearer extends GTransformer {

	public GShearer(Vector<GShape> drawingShape, Vector<Integer> selected) {
		super(drawingShape, selected);
	}

	@Override
	public void prepare(Point start) {}

	@Override
	public void keep(Point end) {
		
	}

	@Override
	public GShape finalize(Color in, Color line) {
		return null;
	}

}
