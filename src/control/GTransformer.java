package control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import frame.GDrawingPanel;
import frame.GToolBar;
import frame.GToolBar.EShape;
import shapes.GRectangle;
import shapes.GShape;

public class GTransformer {

	public enum GState {
		IDLE,
		DRAWING,
		DRAWINGPOLYGON,
		MOVE,
		RESIZE;
		private byte anchorNum;
//		private Point anchorPoint;
		private Vector<Point> anchorPoint;
		private Vector<Point> oppositePoint;
		private GState() {
			this.anchorPoint = new Vector<Point>();
			this.oppositePoint = new Vector<Point>();
		}
		public void setAnchorNumber(byte num) {this.anchorNum = num;}
		public byte getAnchorNumber() {return this.anchorNum;}
		public void clearPoint() {
			this.anchorPoint.clear();
			this.oppositePoint.clear();
		}
		public void addAnchorPoint(Point anchor) {
			this.anchorPoint.add(anchor);
			}
		public void addOppositePoint(Point anchor) {
			this.oppositePoint.add(anchor);
		}
		public Vector<Point> getAnchorPoint() {return this.anchorPoint;}
		public Vector<Point> getOppositePoint() {return this.oppositePoint;}
	}
	
//	private int selectedObject;
	private Color innerColor, lineColor;
	private Point startPoint, previousPoint, currentPoint;
	private GRectangle selectRange;
	private GShape drawingShape;
	private Vector<GShape> shapes;
	private Vector<Integer> selectedShapes;
	private GToolBar toolbar;
	private GDrawingPanel canvus;
	private GState state = GState.IDLE;
	
	public GTransformer() {
		this.shapes = new Vector<GShape>();
		this.selectedShapes = new Vector<Integer>();
		this.selectRange = new GRectangle();
	}
	public void initialize(GToolBar toolbar, GDrawingPanel gDrawingPanel) {
		this.toolbar = toolbar;
		this.canvus = gDrawingPanel;
		this.innerColor = null;
		this.lineColor = Color.black;
//		this.selectedObject = -1;
		this.selectRange.finalize(new Color(230,255,255,80),new Color(200,235,235,255));
	}
	public void drawPaints(Graphics g) {
		Dimension size = this.canvus.getSize();
		Image buffer = canvus.createImage(size.width, size.height);
		Graphics2D bfrG =(Graphics2D) buffer.getGraphics();
		bfrG.setColor(this.canvus.getBackground());
		bfrG.fillRect(0, 0, size.width,size.height);
		for (int i=this.shapes.size()-1; i>-1;i--) {
			GShape s = this.shapes.get(i);
			s.draw(bfrG);
		}
		g.drawImage(buffer, 0,0,null);
	}
	public void draw() {
		this.drawingShape.initialize(startPoint, currentPoint);
		this.drawPaints(this.canvus.getGraphics());
	}
	
	public void selectedActions(Vector<Integer> selected, GState action){
		int dx = this.currentPoint.x - this.previousPoint.x;
		int dy = this.currentPoint.y - this.previousPoint.y;
		if(action == GState.IDLE) {
//			System.out.println(dx +", "+ dy);
			this.selectRange.initialize(startPoint, currentPoint);
		}else if(action == GState.MOVE) {
			for(Integer i : selected) {
				this.shapes.get(i).move(dx, dy);
			}
		}else if(action == GState.RESIZE) {
//			System.out.println(action.getAnchorNumber());
			byte anchorNum = action.getAnchorNumber();
			Vector<Point> origin = action.getAnchorPoint();
			Vector<Point> opposite = action.getOppositePoint();
			boolean is37 = false;
			boolean is15 = false;
			if(anchorNum==3 || anchorNum==7) {
				is37 = true;
			}else if(anchorNum==1 || anchorNum==5) {
				is15 = true;
			}
			for(int i=0;i<selected.size();i++) {
				GShape shape = this.shapes.get(selected.get(i));
				Point p = origin.get(i);
				p.setLocation(p.x+dx, p.y+dy);
				Point dummy = this.transAnchor(shape, anchorNum);
				if(is37) {
					dummy.setLocation(p.x, dummy.y);
				}else if(is15) {
					dummy.setLocation(dummy.x, p.y);
				}else {
					dummy = p;
				}
				shape.initialize(opposite.get(i),dummy);
				shape.finishResize();
			}
		}
		this.drawPaints(this.canvus.getGraphics());
	}
	 
	/**
	 * Transfer anchorNum that is odd to definite anchor
	 * @param I 6 - 5 - 4
	 * @param I 3 - - - 7 			3 = 4||2번, 7 = 6||0번, 5 = 2||0번, 1 = 6||4번
	 * @param I 0 - 1 - 2			L> 3번 1번 = 4, 5번 7번 = 0으로 감.
	 * @return Point[0] is fixed Point and Point[1] is unfixed Point.
	 * */
	private Point transAnchor(GShape shape, byte anchor) {
		byte transAnchor;
		if(anchor>4) transAnchor = 4;
		else transAnchor = 0;
		return shape.getAnchor(transAnchor);
	}
	public void prepareTransforming(MouseEvent e) {
		this.startPoint = e.getPoint();
		EShape selected = toolbar.getSelectedShape();
		if(selected == EShape.SELECT) {
			previousPoint = startPoint;
			if(this.selectedShapes.size()!=0) {
				boolean notSelect = true;
				for(Integer i : this.selectedShapes) {
					GShape shape = shapes.get(i);
					byte inAnchor=shape.grabAnchor(startPoint);
					if(inAnchor!=-1) {
						this.state = GState.RESIZE;
						this.state.setAnchorNumber(inAnchor);
						setAnchorPoints(inAnchor);
						notSelect = false;
						break;
//						System.out.println("Captain!!!!!");
					}else if(shape.grab(startPoint)) {
						this.state = GState.MOVE;
						notSelect = false;
						break;
//						System.out.println("focused!!!   "+selectedObject);
					}
				}
				if(notSelect) {
					this.state = GState.IDLE;
					selectShape(-1);
					this.shapes.add(0, this.selectRange);
				}
				System.out.println(this.state);
			}else {
				this.state = GState.IDLE;
				this.shapes.add(0, this.selectRange);
			}
		}else if(state != GState.DRAWINGPOLYGON){
			if(this.selectedShapes.size()!=0) this.selectShape(-1);
			drawingShape = this.toolbar.generate(selected.getShape());
			shapes.add(0, drawingShape);	
			if(selected == EShape.EPOLYGON) {
				this.state = GState.DRAWINGPOLYGON;
				this.drawingShape.addPoint(startPoint);
				drawingShape.initialize(null, startPoint);
			}else {
				this.state = GState.DRAWING;
			}
		}
	}
	private void setAnchorPoints(byte anchor) {
		byte opposite = anchor;
		opposite+=4;
		if (opposite>7)opposite-=8;
		for(Integer i : this.selectedShapes) {
			GShape shape = shapes.get(i);
			Point p1, p2;
			if(opposite%2 != 0) {
				p1 = transAnchor(shape, anchor);
				p2 = transAnchor(shape, opposite);
			}else {
				p1 = shape.getAnchor(anchor);
				p2 = shape.getAnchor(opposite);
			}
			this.state.addAnchorPoint(p1);
			this.state.addOppositePoint(p2);
		}
	}
	public void keepTransforming(MouseEvent e) {
		this.currentPoint = e.getPoint();
		if(toolbar.getSelectedShape()==EShape.SELECT) {
			selectedActions(selectedShapes, state);
			if(this.selectedShapes.size()!=0) {
				previousPoint = this.currentPoint;
			}
		}else if(state == GState.DRAWINGPOLYGON) {
			drawingShape.initialize(null, currentPoint);
			this.drawPaints(this.canvus.getGraphics());
		}else draw();
//		System.out.print(endPoint);
	}
	public void finalizeTransforming(MouseEvent e) {
		if(toolbar.getSelectedShape()!=EShape.SELECT) {
			this.drawingShape = drawingShape.finalize(innerColor, lineColor);
			if(this.drawingShape!=null) {
				this.shapes.setElementAt(drawingShape, 0);
//				System.out.println(this.drawingShape);
				this.selectShape(0);
			}
			drawingShape=null;
			toolbar.setShape(EShape.SELECT);
			this.state = GState.IDLE;
		}else if (this.state == GState.IDLE) {
			this.shapes.remove(0);
			
			for (int i=0;i<this.shapes.size();i++) {
				GShape s = shapes.get(i);
				if(this.selectRange.grab(s.getCenter())) {
					s.select(true);
					//TODO
					this.selectedShapes.add(0, i);
				}
			}
			this.selectRange.reset();
			this.drawPaints(this.canvus.getGraphics());
		}else if(this.state == GState.RESIZE) {
			this.state.clearPoint();
		}
	}
	public void mouseSingleClick(MouseEvent e) {
		EShape selected = toolbar.getSelectedShape();
		if(selected==EShape.SELECT) {
			if(this.selectedShapes.size()==0) {
				for (int i=0;i<shapes.size();i++) if(shapes.get(i).grab(startPoint)) {
				selectShape(i);
//				System.out.println("I'm here!!!   "+selectedObject);
				break;
				}
			}else {
				boolean notSelect = true;
				for(Integer i : this.selectedShapes) {
					GShape shape = this.shapes.get(i);
					if(shape.grabAnchor(startPoint)!=-1 || shape.grab(startPoint)) {
						notSelect = false;
					}
				}
				if(notSelect) {
					this.selectShape(-1);
				}
			}
		}else if (state == GState.DRAWINGPOLYGON){
			this.drawingShape.addPoint(e.getPoint());
		}
	}
	public void mouseDoubleClick(MouseEvent e) {
		System.out.println("double clicked");
		if(state == GState.DRAWINGPOLYGON) {
			this.finalizeTransforming(null);
		}
	}
	public void polygonAnimation(Point p) {
		if(state == GState.DRAWINGPOLYGON) {
			drawingShape.initialize(null, p);
			this.drawPaints(this.canvus.getGraphics());
		}
	}
	/**
	 * Select one GShape object in Vector, 
	 * @param index The number of shape in shapes Vector. if the number is -1,
	 * than no shapes are selected
	 * */
	private void selectShape(int index) {
		if(this.selectedShapes.size()!=0) {
			for(Integer i : this.selectedShapes) {
				shapes.get(i).select(false);
			}
			this.selectedShapes.clear();
		}
		if(index != -1) {
			shapes.get(index).select(true);
			this.selectedShapes.add(index);
		}
		this.drawPaints(this.canvus.getGraphics());
	}
	public boolean state(GState s) {
		return s == this.state;
	}
	
	public void deleteSelectedShape() {
		if(this.selectedShapes.size()!=0) {
			for(Integer i:this.selectedShapes) {
				this.shapes.removeElementAt(i);
			}
			this.selectedShapes.clear();
			this.drawPaints(this.canvus.getGraphics());
		}
	}
}