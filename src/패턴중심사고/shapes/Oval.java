package 패턴중심사고.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Oval extends Shape{

	private int focusPoint1,focusPoint2;
	private boolean isfpOnLong;
	
	@Override
	public void setting(int[] x, int[] y, int nPoint, Color color) {}
	@Override
	public void setting(int x, int y, int width, int height, Color color) {
		super.x = x;
		super.y = y;
		super.width=width;
		super.height=height;
		super.center[0]=x+(width/2);
		super.center[1]=y+(height/2);
		super.color = color;
		
		if(width<height) this.isfpOnLong=false;
		else this.isfpOnLong=true;
		int[] focusPoint = this.getFP(super.width/2, super.height/2);
		this.focusPoint1 = focusPoint[0];
		this.focusPoint2 = focusPoint[1];
//		System.out.println(this.focusPoint1+", "+this.focusPoint2);
	}
	private int[] getFP(int longAxis, int shortAxis) {
		int[] focusPoint = new int[2];
		if(this.isfpOnLong) {
			int c = (int)Math.sqrt(Math.pow(longAxis, 2)-Math.pow(shortAxis, 2));
			focusPoint[0] = super.center[0] + c;
			focusPoint[1] = super.center[0] - c;
		}else {
			int c = (int)Math.sqrt(Math.pow(shortAxis, 2)-Math.pow(longAxis, 2));
			focusPoint[0] = super.center[1] + c;
			focusPoint[1] = super.center[1] - c;
		}
		return focusPoint;
	}

	@Override
	public void draw(Graphics g) {
		g.drawOval(x, y, width, height);
//		if(this.isfpOnLong) {
//			g.drawOval(this.focusPoint1-2, super.center[1]-2, 4, 4);
//			g.drawOval(this.focusPoint2-2, super.center[1]-2, 4, 4);
//			g.setColor(Color.blue);
//			int[] fp = this.getFP((super.width/2)-4, (super.height/2)-4);
//			g.drawOval(fp[0]-2, super.center[1]-2, 4, 4);
//			g.drawOval(fp[1]-2, super.center[1]-2, 4, 4);
//			g.drawOval(super.x+4, super.y+4, super.width-8, super.height-8);
//			g.setColor(Color.black);
//		}else {
//			g.drawOval(super.center[0]-2, this.focusPoint1-2, 4, 4);
//			g.drawOval(super.center[0]-2, this.focusPoint2-2, 4, 4);
//			g.setColor(Color.blue);
//			int[] fp = this.getFP((super.width/2)-4, (super.height/2)-4);
//			g.drawOval(super.center[0]-2, fp[0]-2, 4, 4);
//			g.drawOval(super.center[0]-2, fp[1]-2, 4, 4);
//			g.drawOval(super.x+4, super.y+4, super.width-8, super.height-8);
//			
//		}
//		System.out.println(this.focusPoint1+", "+this.focusPoint2);
	}
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		if(this.isfpOnLong) {
			this.focusPoint1+=dx;
			this.focusPoint2+=dx;
		}else {
			this.focusPoint1+=dy;
			this.focusPoint2+=dy;			
		}
	}
	@Override
	public boolean grab(Point mouse) {
		int f1 = this.focusPoint1;
		int f2 = this.focusPoint2;
		if(this.isfpOnLong) {
			if(mouse.distance(f1, super.center[1])+mouse.distance(f2, super.center[1])<super.width) {
				if(color==null) {
					int[] fp = this.getFP((super.width/2)-4, (super.height/2)-4);
					if(mouse.distance(fp[0], super.center[1])+mouse.distance(fp[1], super.center[1])>super.width-8) {
						return true;
					}
				}else return true;
			}
			return false;
		}else {
			if(mouse.distance(super.center[0], f1)+mouse.distance(super.center[0], f2)<super.height) {
				if(color==null) {
					int[] fp = this.getFP((super.width/2)-4, (super.height/2)-4);
					if(mouse.distance(super.center[0], fp[0])+mouse.distance(super.center[0], fp[1])>super.height-8) {
						return true;
					}
				}else return true;
			}
			return false;
		}
	}
	
}
