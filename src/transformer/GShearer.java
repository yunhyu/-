package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GShape;

public class GShearer extends GTransformer {

	public GShearer(GShape shape) {
		super(shape);
	}

	@Override
	public void initTransform(Point start) {}

	@Override
	public void keepTransform(Point end) {
		
	}

	@Override
	public GShape finalizeTransform(Color in, Color line) {
		return null;
	}

}
