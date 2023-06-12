package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import main.GContants.CAnchors;
import main.GContants.EAnchors;
import main.GContants.EDrawableAnchors;

public class GAnchors {
	
	private Rectangle textAnchor;
	private Ellipse2D[] originAnchors;
	private Shape[] drawingAnchors;
	private EAnchors[] eAnchors;
	
	public GAnchors() {
		int numOfDrawableAnchor = EDrawableAnchors.DRAWABLE_ANCHORS.getDrawableAnchor();
		this.eAnchors = new EAnchors[numOfDrawableAnchor];
		for(EAnchors anchor : EAnchors.values()) {
			if(anchor.isDrawable())this.eAnchors[anchor.getAnchorNum()] = anchor;
		}
		
		this.originAnchors = new Ellipse2D[numOfDrawableAnchor];
		this.drawingAnchors = new Shape[numOfDrawableAnchor];
		for(int i=0;i<this.originAnchors.length;i++){
			originAnchors[i] = new Ellipse2D.Double(0,0,10,10);
		}
		this.textAnchor = new Rectangle();
	}
	
	public void setPosition(Shape shape, double theta, Point axis) {
		Rectangle rectangle = shape.getBounds();
		int d1 = CAnchors.RESIZE_ANCHOR_RADIUS*2;
		int x = rectangle.x - CAnchors.RESIZE_ANCHOR_RADIUS;
		int y = rectangle.y - CAnchors.RESIZE_ANCHOR_RADIUS;
		int w = rectangle.width;
		int h = rectangle.height;
		this.originAnchors[EAnchors.SW.getAnchorNum()].setFrame(x,	 y+h, 	d1, d1);
		this.originAnchors[EAnchors.SS.getAnchorNum()].setFrame(x+w/2, y+h, 	d1, d1);
		this.originAnchors[EAnchors.SE.getAnchorNum()].setFrame(x+w,	 y+h, 	d1, d1);
		this.originAnchors[EAnchors.WW.getAnchorNum()].setFrame(x,	 y+h/2,	d1, d1);
		this.originAnchors[EAnchors.NE.getAnchorNum()].setFrame(x+w,	 y,		d1, d1);
		this.originAnchors[EAnchors.NN.getAnchorNum()].setFrame(x+w/2, y, 	d1, d1);
		this.originAnchors[EAnchors.NW.getAnchorNum()].setFrame(x,	 y, 	d1, d1);
		this.originAnchors[EAnchors.EE.getAnchorNum()].setFrame(x+w,	 y+h/2,	d1, d1);
		
		int d2 = CAnchors.ROTATE_ANCHOR_RADIUS*2;
		x += CAnchors.RESIZE_ANCHOR_RADIUS;
		this.originAnchors[EAnchors.RR.getAnchorNum()].setFrame
		(x+w/2-CAnchors.ROTATE_ANCHOR_RADIUS, y-d2*2, d2, d2);
		
		this.textAnchor.setBounds(x+w+3, y-3, 8, 8);
		
		AffineTransform affine = AffineTransform.getTranslateInstance(-axis.x, -axis.y);
		for(int i=0;i<this.drawingAnchors.length;i++){
			this.drawingAnchors[i] = affine.createTransformedShape(this.originAnchors[i]);
		}
		affine.setToRotation(theta);
		for(int i=0;i<this.drawingAnchors.length;i++){
			this.drawingAnchors[i] = affine.createTransformedShape(this.drawingAnchors[i]);
		}
		affine.setToTranslation(axis.x, axis.y);
		for(int i=0;i<this.drawingAnchors.length;i++){
			this.drawingAnchors[i] = affine.createTransformedShape(this.drawingAnchors[i]);
		}
	}
	public void draw(Graphics2D g) {
//		g.setColor(Color.black);
//		g.draw(this.textAnchor);
		
//		int x = r.x+r.width/2;
//		g.drawLine(x, r.y, x, r.y-CAnchors.ROTATE_ANCHOR_RADIUS*4);
		
		for(Shape anchor : this.drawingAnchors) {
			g.setColor(Color.white);
			g.fill(anchor);
			g.setColor(Color.black);
			g.draw(anchor);
		}
	}
	public EAnchors onAnchor(int x, int y) {
		for(int i=0; i<this.drawingAnchors.length;i++) {
			Shape anchor = this.drawingAnchors[i];
			if(anchor.contains(x, y)) return this.eAnchors[i];
		}
//		if(this.textAnchor.contains(x, y))return EAnchors.TT;
		return null;
	}
	
	public Point getAnchor(int num) {
		int r = CAnchors.RESIZE_ANCHOR_RADIUS;
		Rectangle anchor = this.originAnchors[num].getBounds();
		return new Point((int)anchor.getX() + r,(int)anchor.getY() + r);
	}
}
