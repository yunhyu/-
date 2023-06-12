package transformer;

import java.awt.Point;

import shapes.GShape;

public class GMover extends GTransformer {

	private Point previous;
	
	public GMover(GShape shape) {
		super(shape);
		
	}
	
	@Override
	public void initTransform(Point start) {
		this.start = start;
		previous = start.getLocation();
	}

	@Override
	public void keepTransform(Point end, boolean shiftDown) {
		double dx;
		double dy;
		
		if(shiftDown) {
			dx = end.x - this.start.getX();
			dy = end.y - this.start.getY();
			
			double a = Math.toDegrees(-Math.atan2(dy, dx));//x축 기준 반시계
			if((a>-45&&a<=45)||a>135||a<=-135) end.setLocation(end.x, start.getY());
			else end.setLocation(start.getX(), end.y);
			
			dx = end.x - this.previous.getX();
			dy = end.y - this.previous.getY();
		}else {
			dx = end.x - previous.x;
			dy = end.y - previous.y;
		}
		
		this.affineTransfrom.setToTranslation(dx, dy);
		shape.translate(affineTransfrom);
		this.previous = end;
	}

	@Override
	public GShape finalizeTransform(Point end) {
		shape.finalizeTransforming();
		return null;
	}
}
