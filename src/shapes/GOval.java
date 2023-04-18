package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;

public class GOval extends GShape{

	private int focusPoint1,focusPoint2;
	private boolean isfpOnLong;
	private Ellipse2D oval;
	
	public GOval() {
		super();
		this.shape = new Ellipse2D.Double();
		this.oval = (Ellipse2D)this.shape;
	}
	@Override
	public void draw(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		g2D.setColor(lineColor);
		if(this.lineColor!=null)g2D.draw(shape);
		g2D.setColor(innerColor);
		if(this.innerColor!=null)g2D.fill(shape);
		this.drawAnchors(g2D);
	}
	@Override
	public void setting(Point start, Point end) {
		int[] dummy = super.transPoint(start, end);
		this.x = dummy[0];
		this.y = dummy[1];
		this.width = dummy[2];
		this.height = dummy[3];
		this.oval.setFrame(x, y, width, height);
//		System.out.println(this.focusPoint1+", "+this.focusPoint2);
	}
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
		if(width<height) this.isfpOnLong=false;
		else this.isfpOnLong=true;
		int[] focusPoint = this.getFP(this.width/2, this.height/2);
		this.focusPoint1 = focusPoint[0];
		this.focusPoint2 = focusPoint[1];
	}
	/**
	 * Get oval's focus points in form of array.
	 * @return Returns two focus point. focusPoint[0] >= 0 >= focusPoint[1]. 
	 * if longAxis >= shortAxis, focusPoint is on X Axis.
	 * if shortAxis < longAxis, focusPoint is on Y Axis.*/
	private int[] getFP(int longAxis, int shortAxis) {	
		int[] focusPoint = new int[2];
		if(this.isfpOnLong) {
			int c = (int)Math.sqrt(Math.pow(longAxis, 2)-Math.pow(shortAxis, 2));
			focusPoint[0] = this.center.x + c;
			focusPoint[1] = this.center.x - c;
		}else {
			int c = (int)Math.sqrt(Math.pow(shortAxis, 2)-Math.pow(longAxis, 2));
			focusPoint[0] = this.center.y + c;
			focusPoint[1] = this.center.y - c;
		}
		return focusPoint;
	}

//	@Override
//	public void draw(Graphics g) {
//		g.drawOval(x, y, width, height);
//		this.drawAnchors(g);
////		if(this.isfpOnLong) {
////			g.drawOval(this.focusPoint1-2, this.center[1]-2, 4, 4);
////			g.drawOval(this.focusPoint2-2, this.center[1]-2, 4, 4);
////			g.setColor(Color.blue);
////			int[] fp = this.getFP((this.width/2)-4, (this.height/2)-4);
////			g.drawOval(fp[0]-2, this.center[1]-2, 4, 4);
////			g.drawOval(fp[1]-2, this.center[1]-2, 4, 4);
////			g.drawOval(this.x+4, this.y+4, this.width-8, this.height-8);
////			g.setColor(Color.black);
////		}else {
////			g.drawOval(this.center[0]-2, this.focusPoint1-2, 4, 4);
////			g.drawOval(this.center[0]-2, this.focusPoint2-2, 4, 4);
////			g.setColor(Color.blue);
////			int[] fp = this.getFP((this.width/2)-4, (this.height/2)-4);
////			g.drawOval(this.center[0]-2, fp[0]-2, 4, 4);
////			g.drawOval(this.center[0]-2, fp[1]-2, 4, 4);
////			g.drawOval(this.x+4, this.y+4, this.width-8, this.height-8);	
////			g.setColor(Color.black);
////		}
////		System.out.println(this.focusPoint1+", "+this.focusPoint2);
//	}
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		this.oval.setFrame(x, y, width, height);
		if(this.isfpOnLong) {
			this.focusPoint1+=dx;
			this.focusPoint2+=dx;
		}else {
			this.focusPoint1+=dy;
			this.focusPoint2+=dy;			
		}
	}
//	@Override
//	public boolean grab(Point mouse) {
//		int f1 = this.focusPoint1;
//		int f2 = this.focusPoint2;
//		if(this.isfpOnLong) {
//			if(mouse.distance(f1, this.center[1])+mouse.distance(f2, this.center[1])<this.width) {
//				if(innerColor==null) {
//					return mouse.distance(f1, this.center[1])+mouse.distance(f2, this.center[1])>this.width-6;
//				}else return true;
////				return true;
//			}
//			return false;
//		}else {
//			if(mouse.distance(this.center[0], f1)+mouse.distance(this.center[0], f2)<this.height) {
//				if(innerColor==null) {
//					return mouse.distance(this.center[0], f1)+mouse.distance(this.center[0], f2)>this.height-4;
//				}else return true;
////				return true;
//			}
//			return false;
//		}
//	}
}
