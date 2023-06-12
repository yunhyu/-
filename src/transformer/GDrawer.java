package transformer;

import java.awt.Point;

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
	public void keepTransform(Point end, boolean shiftDown) {
		shape.keep(end);
	}
	@Override
	public void continueTransform(Point p) {
		shape.addPoint(p);
	}

	@Override
	public GShape finalizeTransform(Point end) {
		return shape.finish();
	}
}
