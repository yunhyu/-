package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GShape;

public class GDrawer extends GTransformer {

	public GDrawer(GShape drawingShape) {
		super(drawingShape);
	}

	@Override
	public void initTransform(Point start) {
		shape.initialize(start);
	}
	@Override
	public void keepTransform(Point end) {
		shape.keep(end);
	}
	@Override
	public void continueTransform(Point p) {
		shape.addPoint(p);
	}

	@Override
	public GShape finalizeTransform(Color in, Color line) {
		return shape.finalize(in, line);
	}

}
