package 패턴중심사고.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Rectangle extends Shape{

	@Override
	public void setting(int[] x, int[] y, int nPoint, Color color) {}
	@Override
	public void setting(int x, int y, int width, int height, Color color) {
		super.x = x;
		super.y = y;
		super.width=width;
		super.height=height;
		super.center[0]=x+(width/2);
		super.center[1]=y+(height/2);
		super.color = color;
	}

	@Override
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
		if(super.isSelected) {
			g.drawRect(x, y, width, height);
		}
//		g.setColor(Color.blue);
//		g.drawRect(super.x+4, super.y+4, super.width-8, super.height-8);
//		g.setColor(Color.black);
		
//		g.setColor(Color.blue);
//		g.fillRect(x+1, y+1, width-1, height-1);
//		g.setColor(Color.black);
	}
	@Override
	public boolean grab(Point mouse) {
		if(super.isInRectRange(x, y, width, height, mouse)) {
			if(super.color==null) {
				if(!super.isInRectRange(x+4, y+4, width-8, height-8, mouse)) {
					return true;
				}
			}else {
				return true;
			}
		}
		return false;
	}

}
