package 패턴중심사고;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import 패턴중심사고.shapes.Line;
import 패턴중심사고.shapes.Oval;
import 패턴중심사고.shapes.Polygon;
import 패턴중심사고.shapes.Rectangle;
import 패턴중심사고.shapes.Shape;

public class GDrawingPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5537928810270967677L;
	
	private int method=-1;
	private int selectedShape;
	private Point startPoint, endPoint;
	private Color color;
	private Shape drawingShape;
	private Vector<Shape> shapes;
	private MouseHandler mouse;
	
	public GDrawingPanel() {
		this.setBackground(Color.white);
		this.mouse = new MouseHandler();
		this.shapes = new Vector<Shape>();
		this.addMouseMotionListener(mouse);
		this.addMouseListener(mouse);
	}
	public void initialize(int method) {
		this.setMethod(method);
		this.color = null;
		this.selectedShape = -1;
	}
	public void draw() {
		this.drawPaints(this.getGraphics());
		int height;
		int width;
		int[] p;
		switch(this.method) {
		case 1:
			height = startPoint.y - endPoint.y;
			width = startPoint.x - endPoint.x;
			p=this.shapeWay(height, width);
			height = Math.abs(height);
			width = Math.abs(width);
			this.drawingShape.setting(p[0], p[1], width, height,this.color);
			break;
		case 2:
			height = startPoint.y - endPoint.y;
			width = startPoint.x - endPoint.x;
			p=this.shapeWay(height, width);
			height = Math.abs(height);
			width = Math.abs(width);
			this.drawingShape.setting(p[0], p[1], width, height,this.color);
			break;
		case 3:
			this.drawingShape.setting(startPoint.x, startPoint.y, endPoint.x, endPoint.y,this.color);
			break;
		case 4:
			break;
		}
	}
	private int[] shapeWay(int height, int width) {
		int[] point= new int[2];
		if(height>0) {
			if(width>0) {
				point[0]=this.endPoint.x;
				point[1]=this.endPoint.y;
			}else {
				point[0]=this.startPoint.x;
				point[1]=this.endPoint.y;
			}
		}else if(width>0) {
			point[0]=this.endPoint.x;
			point[1]=this.startPoint.y;
		}else {
			point[0]=this.startPoint.x;
			point[1]=this.startPoint.y;
		}
		return point;
	}
	public void setMethod(int shape) {
		this.method = shape;
		this.setShape();
	}
	private void makeShape() {
		this.shapes.add(0, this.drawingShape);
		setShape();
	}
	private void setShape(){
		switch(method) {
//		case 0:this.drawingShape = null;break;
		case 1:this.drawingShape = new Rectangle();break;
		case 2:this.drawingShape = new Oval();break;
		case 3:this.drawingShape = new Line();break;
		case 4:this.drawingShape = new Polygon();break;
		}
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawPaints(g);
	}
	public void drawPaints(Graphics g) {
		Image buffer = createImage(this.getSize().width,this.getSize().height);
		Graphics bfrG =buffer.getGraphics();
//		this.paintComponents(bfrG);
		bfrG.setColor(Color.white);
		bfrG.fillRect(0, 0, this.getSize().width,this.getSize().height);
		bfrG.setColor(Color.black);
		for (Shape s:this.shapes) s.draw(bfrG);
		g.drawImage(buffer, 0,0,null);
	}
	public void moveSelected(int index){
		int dx = this.endPoint.x - this.startPoint.x;
		int dy = this.endPoint.y - this.startPoint.y;
		this.shapes.get(index).move(dx, dy);
		this.drawPaints(this.getGraphics());
	}
	
	private class MouseHandler implements MouseMotionListener, MouseListener{
		
		@Override
		public void mouseDragged(MouseEvent e) {
			endPoint = e.getPoint();
			if(method==0) {
				if(selectedShape!=-1)moveSelected(selectedShape);
				startPoint = endPoint;
			}else draw();
//			System.out.print(endPoint);
//			System.out.println("   mouseDragged");
		}
		@Override
		public void mouseMoved(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}
		@Override
		public void mousePressed(MouseEvent e) {
			startPoint = e.getPoint();
			if(method==0) {
				for (int i=0;i<shapes.size();i++) if(shapes.get(i).grab(e.getPoint())) {
					selectedShape=i;
					System.out.println("I'm here!!!   "+selectedShape);
					break;
				}else selectedShape=-1;
			}else shapes.add(0, drawingShape);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(method!=0) makeShape();
		}
		@Override
		public void mouseEntered(MouseEvent e) {System.out.println(e.getPoint()+", mouseEntered");}
		@Override
		public void mouseExited(MouseEvent e) {System.out.println(e.getPoint()+", mouseExited");}
	}
}
