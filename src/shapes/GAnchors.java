package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class GAnchors {
	/**
	 * Anchor order
	 * <p>
	 * 6 - 5 - 4
	 * <p>
	 * 3 - - - 7
	 * <p>
	 * 0 - 1 - 2
	 * */
	public enum EAnchors{
		SW,
		SS,
		SE,
		WW,
		NE,
		NN, 
		NW,
		EE,
		
		RR,
		MM;
	}

	private Ellipse2D[] anchors;
	private Shape shape;
	
	public GAnchors(Shape shape) {
		this.shape = shape;
		this.anchors = new Ellipse2D[EAnchors.values().length-1];
		for(int i=0; i<this.anchors.length;i++) {
			this.anchors[i] = new Ellipse2D.Double(0,0,10,10);
		}
	}
	public void setPosition(Rectangle rectangle) {
		int x = rectangle.x;
		int y = rectangle.y;
		int w = rectangle.width;
		int h = rectangle.height;
		this.anchors[0].setFrame(x-2, y+h-2, 4, 4);
		this.anchors[1].setFrame(x+w/2 -2, y+h-2, 4, 4);
		this.anchors[2].setFrame(x+w-2, y+h-2, 4, 4);
		this.anchors[3].setFrame(x-2, y+h/2 -2, 4, 4);
		this.anchors[4].setFrame(x+w-2, y-2, 4, 4);
		this.anchors[5].setFrame(x+w/2 -2, y-2, 4, 4);
		this.anchors[6].setFrame(x-2, y-2, 4, 4);
		this.anchors[7].setFrame(x+w-2, y+h/2 -2, 4, 4);
		
		this.anchors[8].setFrame(x+w/2-3, y-10, 6, 6);
	}
	public void draw(Graphics2D g, Rectangle rect) {
		//setPosition
		this.setPosition(rect);
		for(Ellipse2D anchor : this.anchors) {
			g.setColor(Color.white);
			g.fill(anchor);
			g.setColor(Color.black);
			g.draw(anchor);
		}
	}
	public EAnchors onAnchor(Point p) {
		for(int i=0; i<this.anchors.length;i++) {
			Ellipse2D anchor = this.anchors[i];
			if(anchor.contains(p))return EAnchors.values()[i];
		}
		if(this.shape.contains(p))return EAnchors.MM;
		return null;
	}
}
