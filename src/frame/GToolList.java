package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

public class GToolList {

	private final Color defaultColor = new Color(238,238,238,255);
	
	private Font font;
	private Rectangle bounds;
	private Vector<GToolElement> elements;
	
	public GToolList() {
		bounds = new Rectangle();
		this.elements = new Vector<GToolElement>();
//		this.font = new Font("C:/Windows/Fonts/arial.ttf", Font.PLAIN, 12);
		this.font = new Font(Font.SERIF, Font.PLAIN, 12);
	}
	
	public void draw(Graphics2D g) {
		g.setFont(this.font);
		g.setColor(defaultColor);
		g.fill(bounds);
		g.setColor(Color.black);
		g.draw(bounds);
		for(GToolElement element : this.elements) {
			element.draw(g);
		}
	}
	
	public void setElements(Vector<GToolElement> elements, Graphics g) {
		
		this.elements = elements;
	}
	
	public void test(Graphics2D g) {
		this.elements.clear();
		g.setFont(this.font);
		this.elements.add(new GToolElement("1", g));
		this.elements.add(new GToolElement("2THREE FOUR FIVE SIX", g));
		this.elements.add(new GToolElement("3 four five six", g));
		this.elements.add(new GToolElement("41234 124", g));
		this.elements.add(new GToolElement("5 육구 칠susu 팔 구", g));
	}
	
	public boolean onShape(Point p) {
		return this.bounds.contains(p);
	}
	
	public void setBounds(int x, int y) {
		int size = this.elements.size();
		if(size==0) {
			this.bounds.setFrame(0, 0, 0, 0);
		}else {
			int width = 100;
			int height = 20*size;
			this.bounds.setFrame(x, y, width, height);
			x += 5;
			y += 15;
			for(GToolElement element : this.elements) {
				element.setLocation(x, y);
				y+=20;
			}
		}
	}

	public void clear() {
		this.elements.clear();
		this.bounds.setFrame(0, 0, 0, 0);
	}
}
