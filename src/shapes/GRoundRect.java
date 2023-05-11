package shapes;

import java.awt.Graphics;

public class GRoundRect extends GRectangle {

	public void draw(Graphics g) {
		if(this.innerColor!=null) {
			g.setColor(innerColor);
			g.fillRoundRect(x,y,width,height,40,40);
		}
		if(this.lineColor!=null) {
			g.setColor(lineColor);
			g.drawRoundRect(x,y,width,height,40,40);
		}
	}
	
}
