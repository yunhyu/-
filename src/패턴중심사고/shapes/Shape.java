package 패턴중심사고.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public abstract class Shape {

	protected int x,y,height,width;
	protected int[] center;
	protected boolean isFilled;
	protected Color color;
	
	public Shape(){
		this.center = new int[2];
	}
	public abstract void setting(int x, int y, int width, int height, Color color);
	public abstract void setting(int[] x, int[] y, int nPoint, Color color);		//for Polygon
	public abstract void draw(Graphics g);
	public abstract boolean grab(Point mouse);
	
	public void move(int dx, int dy) {
		System.out.println("move "+dx+", "+dy);
		this.x+=dx;
		this.y+=dy;
		this.center[0]+=dx;
		this.center[1]+=dy;
	}
	public void resize(int dx, int dy,int dwidth,int dheight) {
		this.x+=dx;
		this.y+=dy;
		this.width+=dwidth;
		this.height+=dheight;
		this.center[0]+=dx+dwidth/2;
		this.center[1]+=dy+dheight/2;
	}
}
