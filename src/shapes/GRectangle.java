package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape{

	private Rectangle2D rect;
	public GRectangle(){
		super();
		this.shape = new Rectangle2D.Double();
		this.rect = (Rectangle2D)this.shape;
	}
	@Override
	public void setting(Point start, Point end) {
		int[] dummy = super.transPoint(start, end);
		this.x = dummy[0];
		this.y = dummy[1];
		this.width = dummy[2];
		this.height = dummy[3];
		this.rect.setRect(x, y, width, height);
	}
	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		if(this.width<1&&this.height<1) {
			return null;
		}
		this.innerColor = innerColor;
		this.lineColor = lineColor;
		finishResize();
		return this;
	}
	@Override
	public void finishResize() {
		this.center.setLocation(x+(width/2), y+(height/2));
		this.setAnchorLocation();
	}
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		this.rect.setRect(x, y, this.width,this.height);
	}
	public void reset() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.rect.setRect(x, y, width, height);
	}

//	@Override
//	public void draw(Graphics g) {
//		if(this.lineColor!=null) {
//			g.setColor(lineColor);
//			g.drawRect(x, y, width, height);
//		}
//		if(this.innerColor!=null) {
//			g.setColor(innerColor);
//			g.fillRect(x, y, width, height);
//		}
//		super.drawAnchors(g);
////		g.setColor(Color.blue);
////		g.drawRect(super.x+4, super.y+4, super.width-8, super.height-8);
////		g.setColor(Color.black);		
//	}
//	@Override
//	public boolean grab(Point mouse) {
//		if(this.isInRectRange(x, y, width, height, mouse)) {
//			if(super.innerColor==null) {
//				return !super.isInRectRange(x+4, y+4, width-8, height-8, mouse);
//			}else {
//				return true;
//			}
//		}
//		return false;
//	}
}
