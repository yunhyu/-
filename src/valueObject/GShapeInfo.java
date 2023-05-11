package valueObject;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.util.Vector;

public class GShapeInfo {

	private int x, y, width, height;
	private Point center, axis;
	private Shape shape;
	private Color innerColor, lineColor;
	private Vector<Integer> xCoordinate;
	private Vector<Integer> yCoordinate;
	
	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.center.setLocation(x+(width/2), y+(height/2));
	}
	public void setShape(Shape shape) {this.shape = shape;}
	public void setInnerColor(Color color) {this.innerColor = color;}
	public void setLineColor(Color color) {this.lineColor = color;}
	public void setAxis(Point p) {this.axis = p;}
	public void setXYCoordinate(Vector<Integer> x, Vector<Integer> y) {
		this.xCoordinate = x;
		this.yCoordinate = y;
	}

	public int getX() {return x;}
	public int getY() {return y;}
	public int getHeight() {return height;}
	public int getWidth() {return width;}
	public Point getCenter() {return this.center;}
	public int[] getBounds() {
		int[] dummy = {x,y,width,height};
		return dummy;
	}
	public Shape getShape() {return this.shape;}
	public Color getInnerColor() {return this.innerColor;}
	public Color getLineColor() {return this.lineColor;}
	public Point getAxis() {return this.axis;}
	public Vector<Integer> getXCoordinate() {
		return this.xCoordinate;
	}
	public Vector<Integer> getYCoordinate() {
		return this.yCoordinate;
	}
}
