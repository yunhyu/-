package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class GToolElement {

	private int x,y;
	private String name;
	private Font font;
	private Rectangle2D bounds;
	
	public GToolElement(String name, Graphics2D g) {
		this.name = name;
		this.font = g.getFont();
		this.bounds = font.getStringBounds(name, g.getFontRenderContext());
		this.bounds.setFrame(0, 0, bounds.getWidth(), bounds.getHeight());
		FontRenderContext f;
	}
	
	public void setLocation(int x, int y) {
		this.bounds.setFrame(x, y-bounds.getHeight(), bounds.getWidth(), bounds.getHeight());
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(new Color(250, 250, 250));
		g.fill(bounds);
		g.setColor(Color.black);
		g.drawString(name, x, y);
	}
	
	public boolean onShape(int x, int y) {
		return this.bounds.contains(x, y);
	}
}
