package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Vector;

import main.GContants.EAnchors;

public class GGroup extends GShape {

	private Vector<GShape> children;
	
	public GGroup() {
		super();
		this.children = new Vector<GShape>();
		this.shape = new Rectangle2D.Double();
		this.start = new Point();
		this.center = new Point();
	}
	
	@Override
	public void initialize(Point start) {}
	
	@Override
	public void keep(Point end) {}

	public void addChild(GShape shape) {
		shape.group(true);
		this.children.add(shape);
		
	}
	
	public Collection<GShape> releaseGrouping(){
		return this.children;
	}
	
	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		if(this.children.size()<=1)return null;

		setGroupShape();
		this.finalizeTransforming();
		return this;
	}
	private void setGroupShape() {
		Path2D bounds = new Path2D.Double();
		Shape shape = children.get(0).getShape();
		bounds.append(shape, false);
		for(int i=1;i<this.children.size();i++) {
			shape = children.get(i).getShape();
			bounds.append(shape, false);
		}
		this.shape = bounds;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.draw(shape);
		for(GShape shape : this.children) {
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
		EAnchors anchor = this.gAnchors.onAnchor(x, y);
		if(anchor == null) {	
			if(shape.contains(x, y))return EAnchors.MM;
			return null;
		}else {
			return anchor;
		}
	}

	@Override
	public void transform(AffineTransform affine) {
		for(GShape shape : this.children) {
			shape.transform(affine);
		}
	}
	@Override
	public void finalizeTransforming() {
		for(GShape shape : this.children) {
			shape.finalizeTransforming();
		}
		this.setGroupShape();
		Rectangle r = this.shape.getBounds();
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
//		for(int i=0;i<this.children.size();i++) {
//			System.out.println(adding+", "+this.children.get(i)+", "+i);
//			if(adding > this.children.get(i)) {
//				this.children.add(i, adding);
//				return;
//			}
//		}
//		this.children.add(adding);
//		return;
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
}
