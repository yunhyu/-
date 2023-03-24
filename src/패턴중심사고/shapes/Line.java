package 패턴중심사고.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Line extends Shape{

	@Override
	public void setting(int[] x, int[] y, int nPoint, Color color) {}
	@Override
	public void setting(int sx, int sy, int ex, int ey, Color color) {
		super.x = sx;
		super.y = sy;
		super.width=ex;
		super.height=ey;
		super.center[0]=sx+(ex/2);
		super.center[1]=sy+(ey/2);
		super.color = color;
	}

	@Override
	public void draw(Graphics g) {
		g.drawLine(x, y, width, height);
	}
	@Override
	public boolean grab(Point mouse) {
		// TODO Auto-generated method stub
		return false;
	}

}
