package transformer;

import java.awt.Point;

import shapes.GShape;

public class GShearer extends GTransformer {

	public GShearer(GShape shape) {
		super(shape);
	}

	@Override
	public void initTransform(Point start) {}

	@Override
	public void keepTransform(Point end, boolean shiftDown) {
		
	}

	@Override
	public GShape finalizeTransform(Point end) {
		return null;
	}
}
