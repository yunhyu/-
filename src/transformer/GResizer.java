package transformer;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import shapes.GShape;

public abstract class GResizer extends GTransformer {

	protected Point nextOriginPoint;
	protected Point anchorPoint;
	
	public GResizer(GShape shape) {
		super(shape);
	}

	public static GTransformer createGResizser (int anchor, GShape shape) {
		GResizer resizer;
		switch(anchor) {
		case 3:
		case 7: resizer = new XSideChange(shape); break;
		case 1:
		case 5: resizer = new YSideChange(shape); break;
		default : resizer = new VertexChange(shape); break;
		}
		resizer.setAnchor(anchor);
		return resizer;
	}
	
	public void setAnchor (int anchor) {
		int opposite = anchor;
		opposite+=4;
		if (opposite>7)opposite-=8;
		Point p1, p2;
		if(opposite%2 != 0) {
			p1 = transAnchor(shape, anchor);
			p2 = transAnchor(shape, opposite);
		}else {
			p1 = shape.getAnchor(anchor);
			p2 = shape.getAnchor(opposite);
		}
		this.anchorPoint = p1;
		this.start = p2;
	}

	/**
	 * Transfer anchorNum that is odd to definite anchor
	 * @param I 6 - 5 - 4
	 * @param I 3 - - - 7 			3 = 4||2번, 7 = 6||0번, 5 = 2||0번, 1 = 6||4번
	 * @param I 0 - 1 - 2			L> 3번 1번 = 4, 5번 7번 = 0으로 감.
	 * @return Point[0] is fixed Point and Point[1] is unfixed Point.
	 * */
	private Point transAnchor(GShape shape, int anchor) {
		byte transAnchor;
		if(anchor>4) transAnchor = 4;
		else transAnchor = 0;
		return shape.getAnchor(transAnchor);
	}
	
	private static class XSideChange extends GResizer{
		
		public XSideChange(GShape shape) {
			super(shape);
			this.nextOriginPoint = new Point();
		}
		
		@Override
		public void keepTransform(Point end) {
			double xRate;
			int dx = end.x - start.x;

			nextOriginPoint.setLocation(this.anchorPoint.x, 0);
			xRate = this.validXRate(dx, anchorPoint, end, nextOriginPoint);
			
			transform(xRate, 1, shape);
			
			anchorPoint = nextOriginPoint;
			
			System.out.println(xRate+", "+1);
		}
	}
	
	private static class YSideChange extends GResizer{
		
		public YSideChange(GShape shape) {
			super(shape);
			this.nextOriginPoint = new Point();
		}
		@Override
		public void keepTransform(Point end) {
			double yRate;
			int dy = end.y - start.y;

			nextOriginPoint.setLocation(0, anchorPoint.y);
			yRate = this.validYRate(dy, anchorPoint, end, nextOriginPoint);
			
			transform(1, yRate, shape);
			
			anchorPoint = nextOriginPoint;
			
			System.out.println(1+", "+yRate);
		}
		
	}
	
	private static class VertexChange extends GResizer{
		
		public VertexChange(GShape shape) {
			super(shape);
			this.nextOriginPoint = new Point();
		}
		
		@Override
		public void keepTransform(Point end) {
			double xRate;
			double yRate;
			int dx = end.x - start.x;
			int dy = end.y - start.y;

			xRate = this.validXRate(dx, anchorPoint, end, nextOriginPoint);
			yRate = this.validYRate(dy, anchorPoint, end, nextOriginPoint);
			
			transform(xRate, yRate, shape);
			
			anchorPoint = nextOriginPoint;
			
			System.out.println(xRate+", "+yRate);
		}
	}


	@Override
	public void initTransform(Point start) {}
	
	protected double validXRate(int dx, Point origin, Point end, Point p) {
		double xRate;
		int width = origin.x - start.x;
		if (dx>-1 && dx<1) {
			xRate = 1;
			p.setLocation(origin.x, p.y);
		}else {
			xRate = (double)dx/width;
			p.setLocation(end.x, p.y);
		}
		return xRate;
	}
	protected double validYRate(int dy, Point origin, Point end, Point p) {
		double yRate;
		int height = origin.y - start.y;
		if (dy>-1 && dy<1) {
			yRate = 1;
			p.setLocation(p.x, origin.y);
		}else {
			yRate = (double)dy/height;
			p.setLocation(p.x, end.y);
		}
		return yRate;
	}
	
	protected void transform(double xRate, double yRate, GShape shape) {
		this.affineTransfrom.setToScale(xRate, yRate);
		this.affineTransfrom.translate(-start.x, -start.y);
		shape.transform(this.affineTransfrom);
		
		this.affineTransfrom.setToTranslation(start.x, start.y);
		shape.transform(this.affineTransfrom);
	}
	
	@Override
	public GShape finalizeTransform(Color in, Color line) {
		shape.finalizeTransforming();
		return null;
	}
}
