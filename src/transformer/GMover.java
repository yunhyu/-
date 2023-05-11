package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GShape;

public class GMover extends GTransformer {

	public GMover(Vector<GShape> drawingShape, Vector<Integer> selected) {
		super(drawingShape, selected);
	}

	@Override
	public void keep(Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		for(Integer i : this.selected) {
			GShape shape = this.allShapes.get(i);
			shape.move(dx, dy);
		}
		this.start = end;
	}

	@Override
	public GShape finalize(Color in, Color line) {
		return null;
	}

}
