package valueObject;

import java.awt.Point;
import java.util.Vector;

public class AnchorInfo {
	
	private byte anchorNum;
	private Vector<Point> anchorPoint;
	private Vector<Point> oppositePoint;
	
	public AnchorInfo() {
		this.anchorPoint = new Vector<Point>();
		this.oppositePoint = new Vector<Point>();
	
	}
	public void setAnchorNumber(byte num) {this.anchorNum = num;}
	public byte getAnchorNumber() {return this.anchorNum;}
	public void clearPoint() {
		this.anchorPoint.clear();
		this.oppositePoint.clear();
	}
	public void addAnchorPoint(Point anchor) {
		this.anchorPoint.add(anchor);
		}
	public void addOppositePoint(Point anchor) {
		this.oppositePoint.add(anchor);
	}
	public Vector<Point> getAnchorPoint() {return this.anchorPoint;}
	public Vector<Point> getOppositePoint() {return this.oppositePoint;}
}
