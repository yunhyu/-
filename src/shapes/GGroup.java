package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import main.GContants.EAnchors;
import transformer.GResizer;
import transformer.GTransformer;
import valueObject.GShapeInfo;

public class GGroup extends GShape {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8225086194897299272L;
//	private GGroup group;
	protected Vector<GShape> shapes, children;
	
	public GGroup() {
		super();
		this.children = new Vector<GShape>();
		this.start = new Point();
		this.center = new Point();
	}
	public GGroup(Vector<GShape> shapes, Vector<GShape> children) {
		this();
		this.shapes = shapes;
		this.children = children;
		finish();
	}
	
	@Override
	public void initialize(Point start) {}
	@Override
	public void keep(Point end) {}

	@Override
	public GShape group(){
		return this;
	}
	@Override
	public GShape ungroup(){
		int i = this.shapes.indexOf(this);
		this.shapes.remove(i);
		for(GShape shape : children) {
			shapes.add(i, shape);
			shape.setGroupChild(false);
			shape.setCenterOrigin();
			shape.setAbsoluteAxis(shape.getCenter().getLocation());
			shape.finalizeTransforming();
		}
		GMultiSelecter multi = new GMultiSelecter(shapes, this.children);
		return multi;
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
		this.setAbsoluteAxis(center.getLocation());
	}
	
	@Override
	public void draw(Graphics2D g) {
		for(GShape shape : children) {
			shape.draw(g);
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
		EAnchors anchor = gAnchors.onAnchor(x, y);
		if(anchor == null) {
			if(drawing.contains(x, y))return EAnchors.MM;
			return null;
		}else {
			return anchor;
		}
	}
	
	@Override
	protected void setAnchorLocation() {
		this.gAnchors.setPosition(this.drawing, this.theta+this.groupTheta, this.axis);
	}
	
	@Override
	public void setAbsoluteAxis(Point nextAxis) {
		this.axis = nextAxis;
		for(GShape shape : children) {
			shape.setAbsoluteAxis(nextAxis);
		}
	}
	
	@Override
	public void translate(AffineTransform affine) {
		for(GShape shape : children) {
			shape.translate(affine);
		}
	}
	
	@Override
	public void rotate(double theta) {
		if(this.isGroupChild) this.groupTheta = theta;	
		else this.theta = theta;
		for(GShape child : children) {
			child.rotate(theta);
		}
		this.setRotatedShape(this.origin, this.center, this.theta);
		this.setRotatedShape(this.drawing, axis, this.groupTheta);
	}
	
	@Override
	public void scale(AffineTransform affine) {
		for(GShape child : children) {
			child.scale(affine);
		}
	}
	
	@Override
	public void finalizeTransforming() {
		for(GShape shape : this.children) {
			shape.finalizeTransforming();
		}
		this.setGroupShape();
		Rectangle r = this.origin.getBounds();
		this.start.setLocation(r.x, r.y);
		this.center.setLocation(r.getCenterX(), r.getCenterY());
		if(!this.isGroupChild) {
			this.setAbsoluteAxis(center.getLocation());
			r = this.origin.getBounds();
			double tx = center.x - r.getCenterX();
			double ty = center.y - r.getCenterY();
			affine.setToTranslation(tx, ty);
			origin = affine.createTransformedShape(origin);	
		}
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
	public void setInnerColor(Color color) {
		this.innerColor = color;
		for(GShape shape : this.children) shape.setInnerColor(color);
	}
	@Override
	public void setLineColor(Color color) {
		this.lineColor = color;
		for(GShape shape : this.children) shape.setLineColor(color);
	}
	@Override
	public void setAtribute(GShapeInfo info) {
		this.shapes = info.getShapeVector();
		for(GShapeInfo i : info.getChildren()) {
			GShape shape = generate(i.getName());
			shape.setAtribute(i);
			shape.setGroupChild(true);
			this.children.add(shape);
		}
		super.setAtribute(info);
	}
	@Override
	public GShapeInfo getAllAttribute() {
		GShapeInfo info = super.getAllAttribute();
		Vector<GShapeInfo> vChildren = new Vector<GShapeInfo>(); 
		for(GShape shape : this.children) {
			vChildren.add(shape.getAllAttribute());
		}
		info.setChildren(vChildren);
		info.setShapeVector(shapes);
		return info;
	}
	
	@SuppressWarnings("deprecation")
	protected GShape generate(String shape) {
		if(shape==null) return null;
		try {
			Class<?> newShape = Class.forName(shape);
			return (GShape)newShape.newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
