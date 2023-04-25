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
		private Point anchorPoint;
		private GState() {}
		public void setAnchorNumber(byte num) {this.anchorNum = num;}
		public byte getAnchorNumber() {return this.anchorNum;}
		public void setOppositePoint(Point anchor) {this.anchorPoint = anchor;}
		public Point getOppositePoint() {return this.anchorPoint;}
	}
	
	private int selectedObject;
	private Color innerColor, lineColor;
	private Point startPoint, previousPoint, currentPoint;
	private GRectangle selectRange;
	private GShape drawingShape;
	private Vector<GShape> shapes;
	private GToolBar toolbar;
	private GDrawingPanel canvus;
	private GState state = GState.IDLE;
	
	public GTransformer() {
		this.shapes = new Vector<GShape>();
		this.selectRange = new GRectangle();
	}
	public void initialize(GToolBar toolbar, GDrawingPanel gDrawingPanel) {
		this.toolbar = toolbar;
		this.canvus = gDrawingPanel;
		this.innerColor = null;
		this.lineColor = Color.black;
		this.selectedObject = -1;
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
	
	public void selectedActions(int index,GState action){
		int dx = this.currentPoint.x - this.previousPoint.x;
		int dy = this.currentPoint.y - this.previousPoint.y;
		if(action == GState.IDLE) {
//			System.out.println(dx +", "+ dy);
			this.selectRange.initialize(startPoint, currentPoint);
		}else if(action == GState.MOVE) {
			this.shapes.get(index).move(dx, dy);
		}else if(action == GState.RESIZE) {
//			System.out.println(action.getAnchorNumber());
			byte anchorNum = action.getAnchorNumber();
			GShape shape = this.shapes.get(index);
			Point dummy;
			if(anchorNum%2!=0){
				dummy = this.transAnchor(shape, anchorNum);
				if(anchorNum==3 || anchorNum==7) {
					dummy.setLocation(this.currentPoint.x, dummy.y);
				}else if(anchorNum==1 || anchorNum==5) {
					dummy.setLocation(dummy.x, this.currentPoint.y);
				}
			}else {
				dummy = this.currentPoint;
			}
			shape.initialize(action.getOppositePoint(),dummy);
			shape.finishResize();
		}
		this.drawPaints(this.canvus.getGraphics());
	}
	 
	/**
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
			if(selectedObject!=-1) {
				GShape shape = shapes.get(selectedObject);
				byte inAnchor=shape.grabAnchor(startPoint);
				if(inAnchor!=-1) {
					this.state = GState.RESIZE;
					this.state.setAnchorNumber(inAnchor);
					inAnchor+=4;
					if (inAnchor>7)inAnchor-=8;
					if(inAnchor%2!=0) {
						this.state.setOppositePoint(transAnchor(shape, inAnchor));
					}else this.state.setOppositePoint(shape.getAnchor(inAnchor));
//					System.out.println("Captain!!!!!");
				}else if(shape.grab(startPoint)) {
					this.state = GState.MOVE;
//					System.out.println("focused!!!   "+selectedObject);
				}else {
					this.state = GState.IDLE;
					selectObject(-1);
					this.shapes.add(0, this.selectRange);
				}
				System.out.println(this.state);
			}else {
				this.state = GState.IDLE;
				this.shapes.add(0, this.selectRange);
			}
		}else if(state != GState.DRAWINGPOLYGON){
			if(selectedObject!=-1)this.selectObject(-1);
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
	public void keepTransforming(MouseEvent e) {
		this.currentPoint = e.getPoint();
		if(toolbar.getSelectedShape()==EShape.SELECT) {
			selectedActions(selectedObject,state);
			if(selectedObject!=-1) {
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
				this.selectObject(0);
			}
			drawingShape=null;
			toolbar.setShape(EShape.SELECT);
			this.state = GState.IDLE;
		}else if (this.state == GState.IDLE) {
			this.shapes.remove(0);
			
//			for (int i=0;i<this.shapes.size();i++) {
//				GShape s = shapes.get(i);
//				if(this.selectRange.grab(s.getCenter())) {
//					s.select(true);
//					//TODO
//				}
//			}
			this.selectRange.reset();
			this.drawPaints(this.canvus.getGraphics());
		}
	}
	public void mouseSingleClick(MouseEvent e) {
		EShape selected = toolbar.getSelectedShape();
		if(selected==EShape.SELECT) {
			if(selectedObject==-1) {
				for (int i=0;i<shapes.size();i++) if(shapes.get(i).grab(startPoint)) {
				selectObject(i);
				System.out.println("I'm here!!!   "+selectedObject);
				break;
				}
			}else if(shapes.get(selectedObject).grabAnchor(startPoint)==-1
					&& !shapes.get(selectedObject).grab(startPoint)) {
				selectObject(-1);
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
	 * Select GShape object in Vector, 
	 * @param index The number of shape in shapes Vector. if the number is -1,
	 * than no shapes are selected
	 * */
	private void selectObject(int index) {
		if(selectedObject!=-1)shapes.get(selectedObject).select(false);
		if(index != -1) shapes.get(index).select(true);
		selectedObject = index;
		this.drawPaints(this.canvus.getGraphics());
	}
	public boolean state(GState s) {
		return s == this.state;
	}
	
	public void deleteSelectedShape() {
		if(this.selectedObject!=-1) {
			this.shapes.removeElementAt(selectedObject);
			this.selectedObject = -1;
			this.drawPaints(this.canvus.getGraphics());
		}
	}
}