package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import main.GContants.CAnchors;
import main.GContants.EAnchors;
import main.GContants.EDrawableAnchors;

public class GAnchors {
	
	private Shape shape;
	private Ellipse2D[] anchors;
	private EAnchors[] eAnchors;
	
	public GAnchors() {
		int numOfDrawableAnchor = EDrawableAnchors.DRAWABLE_ANCHORS.getDrawableAnchor();
		this.eAnchors = new EAnchors[numOfDrawableAnchor];
		for(EAnchors anchor : EAnchors.values()) {
			if(anchor.isDrawable())this.eAnchors[anchor.getAnchorNum()] = anchor;
		}
		
		this.anchors = new Ellipse2D[numOfDrawableAnchor];
		for(int i=0; i<anchors.length;i++) {
			this.anchors[i] = new Ellipse2D.Double(0,0,10,10);
		}
	}
	//
	public void setPosition(Shape shape) {
		this.shape = shape;
		
		Rectangle rectangle = this.shape.getBounds();
		int d1 = CAnchors.RESIZE_ANCHOR_RADIUS*2;
		int x = rectangle.x - CAnchors.RESIZE_ANCHOR_RADIUS;
		int y = rectangle.y - CAnchors.RESIZE_ANCHOR_RADIUS;
		int w = rectangle.width;
		int h = rectangle.height;
		this.anchors[EAnchors.SW.getAnchorNum()].setFrame(x,	 y+h, 	d1, d1);
		this.anchors[EAnchors.SS.getAnchorNum()].setFrame(x+w/2, y+h, 	d1, d1);
		this.anchors[EAnchors.SE.getAnchorNum()].setFrame(x+w,	 y+h, 	d1, d1);
		this.anchors[EAnchors.WW.getAnchorNum()].setFrame(x,	 y+h/2,	d1, d1);
		this.anchors[EAnchors.NE.getAnchorNum()].setFrame(x+w,	 y,		d1, d1);
		this.anchors[EAnchors.NN.getAnchorNum()].setFrame(x+w/2, y, 	d1, d1);
		this.anchors[EAnchors.NW.getAnchorNum()].setFrame(x,	 y, 	d1, d1);
		this.anchors[EAnchors.EE.getAnchorNum()].setFrame(x+w,	 y+h/2,	d1, d1);
		
		int d2 = CAnchors.ROTATE_ANCHOR_RADIUS*2;
		x += CAnchors.RESIZE_ANCHOR_RADIUS;
		this.anchors[EAnchors.RR.getAnchorNum()].setFrame
		(x+w/2-CAnchors.ROTATE_ANCHOR_RADIUS, y-d2*2, d2, d2);
	}
	public void draw(Graphics2D g, Rectangle rect) {
		//setPosition
//		this.setPosition(rect);
		int x = rect.x+rect.width/2;
		g.setColor(Color.black);
		g.drawLine(x, rect.y, x, rect.y-CAnchors.ROTATE_ANCHOR_RADIUS*4);
		for(Ellipse2D anchor : this.anchors) {
			g.setColor(Color.white);
			g.fill(anchor);
			g.setColor(Color.black);
			g.draw(anchor);
		}
	}
	public EAnchors onAnchor(int x, int y) {
		for(int i=0; i<this.anchors.length;i++) {
			Ellipse2D anchor = this.anchors[i];
			if(anchor.contains(x, y)) return this.eAnchors[i];
		}
		return null;
	}
	
	public Point getAnchor(int num) {
		int r = CAnchors.RESIZE_ANCHOR_RADIUS;
		Ellipse2D anchor = this.anchors[num];
		return new Point((int)anchor.getX() + r,(int)anchor.getY() + r);
	}
}
