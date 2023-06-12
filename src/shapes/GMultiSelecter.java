package shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Vector;

import main.GContants.EAnchors;
import valueObject.GShapeInfo;

public class GMultiSelecter extends GGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5513135369566601730L;
	private int anchor = -1;
	private GShape selected; 
	
	public GMultiSelecter() {
		super();
	}
	public GMultiSelecter(Vector<GShape> shapes, Vector<GShape> children) {
		super(shapes, children);
	}
	
	@Override
	public void initialize(Point start) {}
	@Override
	public void keep(Point end) {}

	public void multiSelect(Vector<GShape> shapes) {
		this.shapes = shapes;
	}
	
	public void addChild(GShape shape) {
		this.children.add(shape);
	}
	
	@Override
	public GShape group(){
		for(GShape shape : this.children) {
			shape.setGroupChild(true);
			this.shapes.remove(shape);
		}
		GGroup group = new GGroup(this.shapes, children);
		this.shapes.add(0, group);
		return group;
	}
	
	@Override
	public GShape ungroup(){
		return this;
	}
	
	@Override
	public GShape finish() {
		switch(this.children.size()) {
		case 0: return null;
		case 1: return this.children.get(0);
		default :
			setGroupShape();
			this.finalizeTransforming();
			System.out.println(this.children);
			return this;
		}
	}
	
	@Override
	protected void setGroupShape() {
		Path2D drawing = new Path2D.Double();
		Path2D origin = new Path2D.Double();
		Shape shape = children.get(0).getDrawingShape();
		drawing.append(shape, false);
		shape = children.get(0).getOriginShape();
		origin.append(shape, false);
		for(int i=1;i<this.children.size();i++) {
			shape = children.get(i).getDrawingShape();
			drawing.append(shape, false);
			shape = children.get(i).getOriginShape();
			origin.append(shape, false);
		}
		this.drawing = drawing;
		this.origin = origin;
	}
	
	@Override
	public void draw(Graphics2D g) {}
	
	@Override
	public void drawAnchors(Graphics g) {
		for(GShape shape : children) {
			shape.drawAnchors(g);
		}
	}
	
	@Override
	public boolean grab(Point mouse) {
		for(GShape shape : this.children) {
			if(shape.grab(mouse))return true;
		}
		return false;
	}
	
	@Override
	public EAnchors onShape(int x, int y ) {
		for(GShape shape : children) {
			EAnchors anchor = shape.onShape(x, y);
			if(anchor != null) {
				this.selected = shape;
				this.axis = shape.getAxis();
				System.out.println("asdf");
				return anchor;
			}
		}
		return null;
	}

	@Override
	public void setSelected(boolean bSelected) {
		this.bSelected = bSelected;
		if(!this.bSelected) {
			this.shapes.remove(this);
		}
	}
	/**
	 * @return Returns anchor's coordinate. 
	 * */
	public Point getAnchor(int anchor) {
		this.anchor = anchor;
		return this.selected.getAnchor(anchor);
	}
	
	@Override
	public void translate(AffineTransform affine) {
		for(GShape shape : children) {
			shape.translate(affine);
		}
	}
	@Override
	public void scale(AffineTransform affine) {
		AffineTransform origin = (AffineTransform)affine.clone();
		affine.setToScale(affine.getScaleX(), affine.getScaleY());
		for(GShape shape : children) {
			Point p = shape.getAnchor(anchor);
			affine.setToScale(origin.getScaleX(), origin.getScaleY());
			affine.translate(-p.getX(), -p.getY());
			shape.translate(affine);
			affine.setToTranslation(p.getX(), p.getY());
			shape.scale(affine);
		}
	}
	@Override
	public void finalizeTransforming() {
		this.anchor = -1;
		for(GShape shape : this.children) {
			shape.finalizeTransforming();
		}
		this.setGroupShape();
		Rectangle r = this.origin.getBounds();
		this.start.setLocation(r.x, r.y);
		this.center.setLocation(r.getCenterX(), r.getCenterY());
		this.setAnchorLocation();
	}
	/**
	 * Adds Integer object in descending order. 
	 * This method returns true for stop looping. Returned boolean means nothing.
	 * @param adding An adding Integer.
	 * @return Always returns true.
	 * */
	private void orderSelected(int adding) {
		
	}
	@Override
	public void delete() {
		for(GShape shape : this.children) {
			this.shapes.remove(shape);
		}
	}
	@Override
	public void setAtribute(GShapeInfo info) {
		this.shapes = info.getShapeVector();
		this.shapes.remove(this);
		for(GShapeInfo i : info.getChildren()) {
			GShape shape = generate(i.getName());
			shape.setAtribute(i);
			shapes.add(0, shape);
			this.children.add(shape);
		}
		this.shapes.add(0, this);
		super.setAtribute(info);
	}
}
