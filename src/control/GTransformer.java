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
import frame.GToolBar.ETool;
import shapes.GRectangle;
import shapes.GShape;

public class GTransformer {

	public enum GDrawingState {
		IDLE,
		DRAWING,
		DRAWINGPOLYGON,
		MOVE,
		RESIZE;
	}
	
//	private int veiwPoint;
	private boolean fixed;
	private Point startPoint, previousPoint, currentPoint;
	private GRectangle selectRange;
	private GShape drawingShape;
	private Vector<GShape> shapes;
	private Vector<Integer> selectedShapes;
	private GToolBar toolbar;
	private GDrawingPanel canvus;
	private ETool currentTool;
	private GDrawingState state = GDrawingState.IDLE;
	private AnchorStore anchors;
	
	public GTransformer() {
		this.shapes = new Vector<GShape>();
		this.selectedShapes = new Vector<Integer>();
		this.selectRange = new GRectangle();
		this.anchors = new AnchorStore();
	}
	public void initialize(GToolBar toolbar, GDrawingPanel gDrawingPanel) {
		this.toolbar = toolbar;
		this.canvus = gDrawingPanel;
		this.currentTool = ETool.ESELECTINGTOOL;
		this.setTools();
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
		for (int i=this.selectedShapes.size()-1; i>-1;i--) {
			GShape s = this.shapes.get(selectedShapes.get(i));
			s.drawAnchors(bfrG);
		}
		g.drawImage(buffer, 0,0,null);
	}
	public void draw() {
		this.drawingShape.initialize(startPoint, currentPoint);
		this.drawPaints(this.canvus.getGraphics());
	}
	
	public void selectedActions(Vector<Integer> selected, GDrawingState action){
		int dx = this.currentPoint.x - this.previousPoint.x;
		int dy = this.currentPoint.y - this.previousPoint.y;
		if(action == GDrawingState.IDLE) {
//			System.out.println(dx +", "+ dy);
			this.selectRange.initialize(startPoint, currentPoint);
		}else if(action == GDrawingState.MOVE) {
			for(Integer i : selected) {
				this.shapes.get(i).move(dx, dy);
			}
		}else if(action == GDrawingState.RESIZE) {
//			System.out.println(action.getAnchorNumber());
			byte anchorNum = anchors.getAnchorNumber();
			Vector<Point> origin = anchors.getAnchorPoint();
			Vector<Point> opposite = anchors.getOppositePoint();
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
				shape.resize(opposite.get(i),dummy);
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
						this.state = GDrawingState.RESIZE;
						this.anchors.setAnchorNumber(inAnchor);
						setAnchorPoints(inAnchor);
						notSelect = false;
						break;
//						System.out.println("Captain!!!!!");
					}else if(shape.grab(startPoint)) {
						this.state = GDrawingState.MOVE;
						notSelect = false;
						break;
//						System.out.println("focused!!!   "+selectedObject);
					}
				}
				if(notSelect) {
					this.state = GDrawingState.IDLE;
					selectShape(-1);
					this.shapes.add(0, this.selectRange);
				}
				System.out.println(this.state);
			}else {
				this.state = GDrawingState.IDLE;
				this.shapes.add(0, this.selectRange);
			}
		}else if(state != GDrawingState.DRAWINGPOLYGON){
			if(this.selectedShapes.size()!=0) this.selectShape(-1);
			drawingShape = this.toolbar.generate(selected.getShape());
			shapes.add(0, drawingShape);	
			if(selected == EShape.EPOLYGON) {
				this.state = GDrawingState.DRAWINGPOLYGON;
				this.drawingShape.addPoint(startPoint);
				drawingShape.initialize(null, startPoint);
			}else {
				this.state = GDrawingState.DRAWING;
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
			this.anchors.addAnchorPoint(p1);
			this.anchors.addOppositePoint(p2);
		}
	}
	public void keepTransforming(MouseEvent e) {
		this.currentPoint = e.getPoint();
		if(toolbar.getSelectedShape()==EShape.SELECT) {
			selectedActions(selectedShapes, state);
			if(this.selectedShapes.size()!=0) {
				previousPoint = this.currentPoint;
			}
		}else if(state == GDrawingState.DRAWINGPOLYGON) {
			drawingShape.initialize(null, currentPoint);
			this.drawPaints(this.canvus.getGraphics());
		}else draw();
//		System.out.print(endPoint);
	}
	public void finalizeTransforming(MouseEvent e) {
		if(toolbar.getSelectedShape()!=EShape.SELECT) {
			Color lineColor = this.toolbar.getDefaultLineColor();
			Color innerColor = this.toolbar.getDefaultInnerColor();
			this.drawingShape = drawingShape.finalize(innerColor, lineColor);
			if(this.drawingShape!=null) {
				this.shapes.setElementAt(drawingShape, 0);
//				System.out.println(this.drawingShape);
				this.selectShape(0);
			}else {
				this.shapes.remove(0);
			}
			drawingShape=null;
			toolbar.setShape(EShape.SELECT);
			this.state = GDrawingState.IDLE;
		}else if (this.state == GDrawingState.IDLE) {
			this.shapes.remove(0);
			
			for (int i=0;i<this.shapes.size();i++) {
				GShape s = shapes.get(i);
				if(this.selectRange.grab(s.getCenter())) {
					s.select(true);
					//TODO
					this.selectedShapes.add(0, i);
				}
			}
			this.setTools();
			this.selectRange.reset();
			this.drawPaints(this.canvus.getGraphics());
		}else if(this.state == GDrawingState.RESIZE) {
			this.anchors.clearPoint();
		}
	}
	private void setTools() {
		ETool tool;
		if(this.selectedShapes.size()!=0) {
			tool = ETool.ESELECTINGTOOL;
			Color line = null;
			Color inner = null;
			if(this.selectedShapes.size()==1) {
				GShape shape = this.shapes.get(this.selectedShapes.get(0));
				line = shape.getLineColor();
				inner = shape.getInnerColor();
			}
			this.toolbar.setLineColor(line);
			this.toolbar.setInnerColor(inner);
		}
		else tool = ETool.EDRAWINGTOOL; 
		if(tool != this.currentTool) {
			this.toolbar.setTools(tool);
			this.currentTool = tool;
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
		}else if (state == GDrawingState.DRAWINGPOLYGON){
			this.drawingShape.addPoint(e.getPoint());
		}
	}
	public void mouseDoubleClick(MouseEvent e) {
		System.out.println("double clicked");
		if(state == GDrawingState.DRAWINGPOLYGON) {
			this.finalizeTransforming(null);
		}
	}
	public void polygonAnimation(Point p) {
		if(state == GDrawingState.DRAWINGPOLYGON) {
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
			this.setTools();
		}
		this.drawPaints(this.canvus.getGraphics());
	}
	public boolean state(GDrawingState s) {
		return s == this.state;
	}
	
	public void deleteSelectedShape() {
		if(this.selectedShapes.size()!=0) {
			for(Integer i:this.selectedShapes) {
				this.shapes.removeElementAt(i);
			}
			this.selectedShapes.clear();
			this.drawPaints(this.canvus.getGraphics());
			this.setTools();
		}
	}
	public void changeColor(Color inner, Color line) {
		for(Integer i : this.selectedShapes) {
			GShape shape = this.shapes.get(i);
			shape.setInnerColor(inner);
			shape.setLineColor(line);
		}
	}
	public void setInnerColor(Color color) {
		for(Integer i : this.selectedShapes) {
			this.shapes.get(i).setInnerColor(color);
		}
	}
	public void setLineColor(Color color) {
		for(Integer i : this.selectedShapes) {
			this.shapes.get(i).setLineColor(color);
		}
	}
	public void shiftDown(boolean b) {
		this.fixed = b;
		System.out.println(this.fixed);
	}
	public void goUp() {
		GShape index0 = null;
		for(Integer i : this.selectedShapes) {
			GShape shape = this.shapes.get(i);
			if(i==0) {
				index0 = shape;
				continue;
			}
			this.shapes.add(i-1,shape);
			
		}
		if(index0 != null) {
			this.shapes.add(0, index0);
		}
	}
	public void goDown() {
		GShape index0 = null;
		for(Integer i : this.selectedShapes) {
			GShape shape = this.shapes.get(i);
			if(i==0) {
				index0 = shape;
				continue;
			}
			this.shapes.add(i+1,shape);
			
		}
		if(index0 != null) {
			this.shapes.add(1, index0);
		}
	}
	
	
//=====================================================================================	
	
	private class AnchorStore{

		private byte anchorNum;
		private Vector<Point> anchorPoint;
		private Vector<Point> oppositePoint;
		
		private AnchorStore() {
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

}