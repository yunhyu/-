package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GMultiSelecter;
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
		this.selecter.setInnerColor(new Color(230,255,255,80));
		this.selecter.setLineColor(new Color(200,235,235,255));
	}
	
	@Override
	public void keepTransform(Point end, boolean shiftDown) {
		this.selecter.keep(end);
	}
	/**
	 * Make group with {@code GShape} object which {@code GRectangle} returns true with.
	 * The selected objects are removed in {@code Vector<GShape>}. Instead, they are add
	 * another {@code Vector<GShape>} in {@code GGroup} object reversed.
	 * <pre>
	 * origin {@code Vector<GShape>}
	 * {A, B ,C}
	 * GGroup {@code Vector<GShape>}
	 * {C, B, A}
	 * </pre>
	 * @param in This class doesn't need parameters. This is for other {@code GTransformer} class
	 * @param line This class doesn't need parameters. This is for other {@code GTransformer} class
	 * 
	 * @return If {@code GGroup} object is not valid, returns null. Or, if {@code GGroup} contains
	 * one {@code GShape} object, than returns the only {@code GShape} object. 
	 * */
	@Override
	public GShape finalizeTransform(Point end) {
		this.shapes.remove(0);
		System.out.println(this.shapes);
		GMultiSelecter group = new GMultiSelecter();
		int zOrder = 0;
		for (int i=this.shapes.size()-1; i>-1;i--) {
			GShape shape = shapes.get(i);
			if(this.selecter.grab(shape.getCenter())) {
				group.addChild(shape);
				zOrder = i;
			}
		}
		group.multiSelect(shapes);
		GShape shape = group.finish();
		if(shape!=null) {
			shape.setSelected(true);
			this.shapes.add(zOrder ,group);
		}
		return shape;
	}
}
