package transformer;

import java.awt.Point;
import java.awt.geom.Point2D;

import shapes.GShape;

public class GRotator extends GTransformer{

	private double dtheta;
	private Point2D axis;
	
	public GRotator(GShape shape) {
		super(shape);
		this.axis = this.shape.getAxis();
	}

	@Override
	public void initTransform(Point start) {
		double x = start.x - this.axis.getX();
		double y = start.y - this.axis.getY();
		dtheta = Math.atan2(x, -y);//y축 기준 시계
	}
	
	@Override
	public void keepTransform(Point end, boolean shiftDown) {
		double x = end.x - this.axis.getX();
		double y = end.y - this.axis.getY();
		
		if(shiftDown) {
			double a = Math.toDegrees(-Math.atan2(y, x));//x축 기준 반시계
			if((a>-45&&a<=45)||a>135||a<=-135) end.setLocation(end.x, axis.getY());
			else end.setLocation(axis.getX(), end.y);
			
			x = end.x - this.axis.getX();
			y = end.y - this.axis.getY();
		}
		
		double theta = Math.atan2(x, -y);//y축 기준 시계
		
		shape.rotate(theta-dtheta);
		dtheta = theta;
	}

	@Override
	public GShape finalizeTransform(Point end) {
		shape.finalizeTransforming();
		return null;
	}
}
