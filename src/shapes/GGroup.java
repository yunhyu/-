package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

public class GGroup extends GShape {

	private Vector<GShape> children;
	
	public GGroup() {
		super();
		this.children = new Vector<GShape>();
	}
	
	@Override
	public void initialize(Point start, Point mouse) {
		
	}

	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		
		return null;
	}

	@Override
	public void resize(Point start, Point mouse) {
		for(GShape shape : this.children) {
			shape.resize(start, mouse);
		}
	}

	@Override
	public void finishResize() {
		
	}
	
	@Override
	public void draw(Graphics g) { 
		for(GShape shape : this.children) {
			shape.draw(g);
		}
	}
	@Override
	public boolean onShape(Point mouse) {
		for(GShape shape : this.children) {
			if(shape.onShape(mouse))return true;
		}
		return false;
	}
	
	public void addChild(GShape shape) {
		shape.group(true);
		this.children.add(shape);
		int[] size = shape.getAnchorBounds();
		this.setBounds(size);
		
	}
	private void setBounds(int[] size) {
		if(size[0]<this.x) {
			x=size[0];
		}
		if(size[1]<this.y) {
			y=size[1];
		}
		if(size[0]+size[2]>this.x+this.width) {
			this.width = size[0]+size[2]-x;
		}
		if(size[1]+size[3]>this.y+this.height) {
			this.height = size[1]+size[3]-y;
		}
	}

}
