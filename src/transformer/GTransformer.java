package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GShape;

public abstract class GTransformer {

	protected Point start, end;
	protected GShape shape;
	protected Vector<GShape> allShapes;
	protected Vector<Integer> selected;
	
	public GTransformer(Vector<GShape> drawingShape, Vector<Integer> selected) {
		this.allShapes = drawingShape;
		this.selected = selected;
	}
	
	public void setShape(GShape shape) {
		this.shape = shape;
	}
	
	public void prepare(Point start) {
		this.start = start;
	}
	public abstract void keep(Point end);
	public abstract GShape finalize(Color in, Color line);
	
	public void addPoint(Point p) {}
	
}
