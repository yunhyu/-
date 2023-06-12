package valueObject;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Vector;

import shapes.GShape;

public class GShapeInfo {

	private double radian; 
	private String name;
	private StringBuffer text;
	private Shape shape;
	private Point axis;
	private Color innerColor, lineColor;
	private Vector<GShape> shapes;
	private Vector<GShapeInfo> children;

	public void setRadian(double radian) {this.radian = radian;}
	public void setName(String name) {this.name = name;}
	public void setShape(Shape shape) {this.shape = shape;}
	public void setInnerColor(Color color) {this.innerColor = color;}
	public void setLineColor(Color color) {this.lineColor = color;}
	public void setAxis(Point p) {this.axis = p;}
	public void setChildren(Vector<GShapeInfo> children) {this.children = children;}
	public void setShapeVector(Vector<GShape> shapes) {this.shapes = shapes;}
	public StringBuffer getText() {return text;}
	
	public Point getAxis() {return this.axis;} 
	public double getRadian() {return radian;}
	public String getName() {return this.name;}
	public Shape getShape() {return new Path2D.Double(this.shape);}
	public Color getInnerColor() {return this.innerColor;}
	public Color getLineColor() {return this.lineColor;}
	public Vector<GShapeInfo> getChildren() {return children;}
	public Vector<GShape> getShapeVector() {return shapes;}
	public void setText(StringBuffer text) {this.text = new StringBuffer(text.toString());}
}
