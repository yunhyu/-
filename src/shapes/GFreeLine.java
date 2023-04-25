package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Vector;

public class GFreeLine extends GShape{
	

	protected boolean complete;
	protected int maxX,maxY,minX,minY;
	protected Vector<Integer> xCoordinate;
	protected Vector<Integer> yCoordinate;
	
	public GFreeLine() {
		super();
		this.xCoordinate = new Vector<Integer>();
		this.yCoordinate = new Vector<Integer>();
		this.complete = false;
	}
	public GFreeLine(Collection<Integer> x, Collection<Integer> y, Rectangle size) {
		this();
		this.xCoordinate.addAll(x);
		this.yCoordinate.addAll(y);
		this.minX=size.x;
		this.minY=size.y;
		this.maxX=size.x+size.width;
		this.maxY=size.y+size.height;
		this.complete = true;
		this.finishResize();
	}
	
	@Override
	public void initialize(Point start, Point end) {
		if(this.complete) {
			this.resize(start, end);
		}else {
			this.setMaxMinCoordinate(end);
			this.xCoordinate.add(end.x);
			this.yCoordinate.add(end.y);
		}
	}
	protected void setMaxMinCoordinate(Point p) {
		if (this.xCoordinate.size()==0) {
			this.maxX = p.x;
			this.minX = p.x;
			this.maxY = p.y;
			this.minY = p.y;
		}else {
			if(p.x>this.maxX)		this.maxX = p.x;
			else if(p.x<this.minX)	this.minX = p.x;
			if(p.y>this.maxY)		this.maxY = p.y;
			else if(p.y<this.minY)	this.minY = p.y;
		}
	}
	@Override
	public GShape finalize(Color innerColor, Color lineColor) {
		int last = this.xCoordinate.size()-1;
		if(last<=0) {
			return null;
		}
		this.innerColor = innerColor;
		this.lineColor = lineColor;
		Point p1 = new Point(this.xCoordinate.get(0),this.yCoordinate.get(0));
		Point p2 = new Point(this.xCoordinate.get(last),this.yCoordinate.get(last));
		double distance = p1.distance(p2);
		if(distance<5) {
			this.xCoordinate.set(last, this.xCoordinate.get(0));
			this.yCoordinate.set(last, this.yCoordinate.get(0));
			Rectangle bounds = new Rectangle
					(this.minX,this.minY,this.maxX-this.minX,this.maxY-this.minY);
			return new GPolygon(xCoordinate,yCoordinate,bounds);
		}else {
			finishResize();
			this.complete = true;
			return this;
		}
	}
	protected void resize(Point start, Point end) {
		/* 6-4 - 앵커 위치
		 * 0-2
		 * 앵커는 움직이는 게 end, 반대편이 start임. 0 - 4 움직임에서 0이 4쪽으로 갈때는 rate가 줄어들지만, x좌표는 오히려
		 * 늘어남. 이걸 캐치해야함. => minX만큼 빼고 rate를 곱한 후 다시 minX를 더하는 방식으로?
		 * 0 - 4움직임에선 maxX가 움직이면 안됨. 
		 * 
		 * transPoint로 움직임을 고정해야하나? => 그럼 편할려나?
		 * => min이랑 max를 고정할 수 있네.
		 * */
//		double widthRate = Math.abs(start.getX()-end.getX())/this.width;
//		double heightRate = Math.abs(start.getY()-end.getY())/this.height;
////		System.out.println(start.x+", "+end.x+", "+(start.x-end.x)+", "+this.width);
////		System.out.println(start.y+", "+end.y+", "+(start.y-end.y)+", "+this.height);
//		System.out.println(widthRate+",  "+heightRate);
//		for (int i=0;i<this.xCoordinate.size();i++) {
//			if(widthRate!=0) {
//				int x = (int) (this.xCoordinate.get(i)*widthRate);
//				this.xCoordinate.setElementAt(x, i);
//			}
//			if(heightRate!=0) {
//				int y = (int) (this.yCoordinate.get(i)*heightRate);
//				this.yCoordinate.setElementAt(y, i);
//			}
//		}
//		this.maxX = (int) (this.maxX*widthRate);
//		this.maxY = (int) (this.maxY*heightRate);
//		this.minX = (int) (this.minX*widthRate);
//		this.minY = (int) (this.minY*heightRate);
	}
	@Override
	public void finishResize() {
		this.x = this.minX;
		this.y = this.minY;
		this.width = this.maxX - this.minX;
		this.height = this.maxY - this.minY;
		this.center.setLocation(x+(width/2), y+(height/2));
		this.setAnchorLocation();
	}

	@Override
	public void draw(Graphics g) { 
		if(this.lineColor!=null) {
			g.setColor(lineColor);
			for (int i=0;i<this.xCoordinate.size()-1;i++) {
				int x1 = this.xCoordinate.get(i);
				int x2 = this.xCoordinate.get(i+1);
				int y1 = this.yCoordinate.get(i);
				int y2 = this.yCoordinate.get(i+1);
				g.drawLine(x1,y1,x2,y2);
			}
			this.drawAnchors(g);
		}
	}
	@Override
	public boolean grab(Point mouse) {
		if(this.xCoordinate.size()<=2 || !this.isInRectRange(x, y, width, height, mouse)) {
			return false;	
		}
		return true;
	}
	@Override
	public void move(int dx, int dy) {
		this.x+=dx;
		this.y+=dy;
		this.center.setLocation(this.center.x+dx, this.center.y+dy);
		for (int i=0;i<this.xCoordinate.size();i++) {
			int a = this.xCoordinate.get(i);
			this.xCoordinate.setElementAt(a+dx,i);
		}
		for (int i=0;i<this.yCoordinate.size();i++) {
			int a = this.yCoordinate.get(i);
			this.yCoordinate.setElementAt(a+dy,i);
		}
		this.maxX+=dx;
		this.minX+=dx;
		this.maxY+=dy;
		this.minY+=dy;
		this.setAnchorLocation();
	}
}
