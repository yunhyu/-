package transformer;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.util.Vector;

import shapes.GShape;

public class GMover extends GTransformer {

	public GMover(GShape shape) {
		super(shape);
	}

	@Override
	public void keepTransform(Point end) {
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		this.affineTransfrom.setToTranslation(dx, dy);
		shape.transform(affineTransfrom);
		this.start = end;
	}

	@Override
	public GShape finalizeTransform(Color in, Color line) {
		shape.finalizeTransforming();
		return null;
	}

}
