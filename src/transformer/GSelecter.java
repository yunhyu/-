package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GGroup;
import shapes.GRectangle;
import shapes.GShape;

public class GSelecter extends GTransformer {

	private GRectangle selecter;
	private Vector<GShape> shapes;
	
	public GSelecter(Vector<GShape> shapes) {
		this.selecter = new GRectangle();
		this.shapes = shapes;
	}

	@Override
	public void initTransform(Point start) {
		this.start = start;
		this.shapes.add(0, selecter);
		this.selecter.initialize(this.start);
		this.selecter.finalize(new Color(230,255,255,80),new Color(200,235,235,255));
	}
	
	@Override
	public void keepTransform(Point end) {
		this.selecter.keep(end);
	}
	
	@Override
	public GShape finalizeTransform(Color in, Color line) {
		this.shapes.remove(0);
		GGroup group = new GGroup();
		for (int i=this.shapes.size()-1; i>-1;i--) {
			GShape shape = shapes.get(i);
			if(this.selecter.grab(shape.getCenter())) {
				group.addChild(shape);
				this.shapes.remove(i);
			}
		}
		this.selecter.reset();
		return group.finalize(in, line);
	}
}
