package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GShape;
import valueObject.AnchorInfo;

public class GResizer extends GTransformer {

	private AnchorInfo anchorInfo;
	
	public GResizer(Vector<GShape> drawingShape, Vector<Integer> selected) {
		super(drawingShape, selected);
	}

	public void setAnchorPoints(byte anchor) {
		AnchorInfo anchorInfo = new AnchorInfo();
		anchorInfo.setAnchorNumber(anchor);
		byte opposite = anchor;
		opposite+=4;
		if (opposite>7)opposite-=8;
		for(Integer i : this.selected) {
			GShape shape = this.allShapes.get(i);
			Point p1, p2;
			if(opposite%2 != 0) {
				p1 = transAnchor(shape, anchor);
				p2 = transAnchor(shape, opposite);
			}else {
				p1 = shape.getAnchor(anchor);
				p2 = shape.getAnchor(opposite);
			}
			anchorInfo.addAnchorPoint(p1);
			anchorInfo.addOppositePoint(p2);
		}
		this.anchorInfo = anchorInfo;
	}

	@Override
	public void keep(Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		
		byte anchorNum = anchorInfo.getAnchorNumber();
		Vector<Point> origin = anchorInfo.getAnchorPoint();
		Vector<Point> opposite = anchorInfo.getOppositePoint();
		boolean is37 = false;
		boolean is15 = false;
		if(anchorNum==3 || anchorNum==7) {
			is37 = true;
		}else if(anchorNum==1 || anchorNum==5) {
			is15 = true;
		}
		for(int i=0;i<selected.size();i++) {
			GShape shape = this.allShapes.get(selected.get(i));
			Point p = origin.get(i);
			p.setLocation(p.x+dx, p.y+dy);
			Point dummy = this.transAnchor(shape, anchorNum);
			if(is37) {
				dummy.setLocation(p.x, dummy.y);
			}else if(is15) {
				dummy.setLocation(dummy.x, p.y);
			}else {
				dummy = p;
			}
			shape.resize(opposite.get(i),dummy);
			shape.finishResize();
		}
		this.start = end;
	}
	/**
	 * Transfer anchorNum that is odd to definite anchor
	 * @param I 6 - 5 - 4
	 * @param I 3 - - - 7 			3 = 4||2번, 7 = 6||0번, 5 = 2||0번, 1 = 6||4번
	 * @param I 0 - 1 - 2			L> 3번 1번 = 4, 5번 7번 = 0으로 감.
	 * @return Point[0] is fixed Point and Point[1] is unfixed Point.
	 * */
	private Point transAnchor(GShape shape, byte anchor) {
		byte transAnchor;
		if(anchor>4) transAnchor = 4;
		else transAnchor = 0;
		return shape.getAnchor(transAnchor);
	}
	
	
	@Override
	public GShape finalize(Color in, Color line) {
		return null;
	}
	
}
