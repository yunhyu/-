package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GGroup;
import shapes.GRectangle;
import shapes.GShape;

public class GSelecter extends GTransformer {

	private GRectangle selecter;
	
	public GSelecter(Vector<GShape> drawingShape, Vector<Integer> selected) {
		super(drawingShape, selected);
		this.selecter = new GRectangle();
		this.shape = this.selecter;
		this.selecter.finalize(new Color(230,255,255,80),new Color(200,235,235,255));
	}

	@Override
	public void prepare(Point start) {
		this.start = start;
		this.allShapes.add(0, selecter);
	}
	
	@Override
	public void keep(Point end) {
		this.shape.initialize(this.start, end);
	}
	
	@Override
	public GShape finalize(Color in, Color line) {
		this.allShapes.remove(0);
		GGroup group = new GGroup();
		for (int i=0;i<this.allShapes.size();i++) {
			GShape s = allShapes.get(i);
			if(this.selecter.onShape(s.getCenter())) {
				s.select(true);
				this.selected.add(0, i);
			}
		}
		this.selecter.reset();
		return null;
	}
}
