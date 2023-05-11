package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import frame.GToolBar.ETool;
import main.GContants.EShape;
import main.GContants.EUserAction;
import shapes.GShape;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GNPointDrawer;
import transformer.GResizer;
import transformer.GSelecter;
import transformer.GShearer;
import transformer.GTransformer;

public class GDrawingPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5537928810270967677L;
	
//	private enum GTransformers {
//		eMaking,
//		eMoving,
//		eResizer,
//		eShearer();
//		private GTransformers() {
//			
//		}
//	}
	
	private enum GDrawingState {
		IDLE,
		SELECTING,
		DRAWING,
		DRAWINGPOLYGON,
		MOVE,
		RESIZE;
	}
	
	private enum EDrawingEvent {
		eStart,
		eMoving,
		eCont,
		eEnd;
	}
	
//	private int veiwPoint;
	private boolean shiftDown;
	private Point startPoint;
	private Vector<GShape> shapes;
	private Vector<Integer> selectedShapes;
	private GToolBar toolbar;
	private ETool currentTool;
	private GTransformer transformer;
	private GDrawingState state = GDrawingState.IDLE;

	public GDrawingPanel() {
		super();
		this.setLayout(null);
		this.setBackground(Color.white);
		this.shapes = new Vector<GShape>();
		this.selectedShapes = new Vector<Integer>();
	}
	public void initialize(GToolBar toolbar) {
		this.toolbar = toolbar;
		this.currentTool = ETool.ESELECTINGTOOL;
		this.setTools();
		
		MouseHandler mouse = new MouseHandler();
		this.addMouseMotionListener(mouse);
		this.addMouseListener(mouse);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		this.drawPaints(g);
	}
	public void drawPaints(Graphics g) {
		Dimension size = this.getSize();
		Image buffer = this.createImage(size.width, size.height);
		Graphics2D bfrG =(Graphics2D) buffer.getGraphics();
		bfrG.setColor(this.getBackground());
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
	
	public void prepareTransforming(MouseEvent e) {
		this.startPoint = e.getPoint();
//		Point startPoint = e.getPoint();
		EShape method = toolbar.getSelectedShape();
		if(method == EShape.SELECT) {
			if(this.selectedShapes.size()!=0) {
				boolean notSelect = true;
				for(Integer i : this.selectedShapes) {
					GShape shape = shapes.get(i);
					byte resizeAnchor = shape.grabResize(startPoint);
					int shearAnchor = shape.grabShear(startPoint);
					if(shearAnchor!=-1) {
						notSelect = false;
						this.transformer = new GShearer(this.shapes, this.selectedShapes);
						break;
					}else if(resizeAnchor!=-1) {
						this.state = GDrawingState.RESIZE;
						GResizer resizer = new GResizer(this.shapes, this.selectedShapes);
						resizer.setAnchorPoints(resizeAnchor);
						this.transformer = resizer; 
						notSelect = false;
						break;
					}else if(shape.onShape(startPoint)) {
						notSelect = false;
						this.state = GDrawingState.MOVE;
						this.transformer = new GMover(this.shapes, this.selectedShapes);
						break;
					}
				}
				if(notSelect) {
					if(!this.shiftDown) {
						selectShape(-1);
						this.state = GDrawingState.SELECTING;
						this.transformer = new GSelecter(this.shapes, this.selectedShapes);
					}
				}
				System.out.println(this.state);
			}else {
				if(!this.shiftDown) {
					this.state = GDrawingState.SELECTING;
					this.transformer = new GSelecter(this.shapes, this.selectedShapes);
				}
			}
		}else if(state != GDrawingState.DRAWINGPOLYGON){
			if(this.selectedShapes.size()!=0) this.selectShape(-1);
			GShape drawingShape = this.toolbar.generate(method.getShape());
			shapes.add(0, drawingShape);
			EUserAction userAction = method.getUserAction();
			if(userAction == EUserAction.eNPoint) {
				this.state = GDrawingState.DRAWINGPOLYGON;
				this.transformer = new GNPointDrawer(this.shapes, this.selectedShapes);
				this.transformer.setShape(drawingShape);
				this.transformer.addPoint(startPoint);
			}else {
				this.state = GDrawingState.DRAWING;
				this.transformer = new GDrawer(this.shapes, this.selectedShapes);
				this.transformer.setShape(drawingShape);
			}
		}
		this.transformer.prepare(startPoint);
	}
	
	public void keepTransforming(MouseEvent e) {
		this.transformer.keep(e.getPoint());
		this.drawPaints(this.getGraphics());
	}
	
	public void finalizeTransforming(MouseEvent e) {
		if(toolbar.getSelectedShape()!=EShape.SELECT) {
			Color lineColor = this.toolbar.getDefaultLineColor();
			Color innerColor = this.toolbar.getDefaultInnerColor();
			GShape shape = this.transformer.finalize(innerColor, lineColor);
			if(shape!=null) {
				this.shapes.setElementAt(shape, 0);
				this.selectShape(0);
			}else {
				this.shapes.remove(0);
			}
			toolbar.setShape(EShape.SELECT);
			this.state = GDrawingState.IDLE;
		}else if (this.state == GDrawingState.SELECTING) {
			if(!this.shiftDown) {
				this.transformer.finalize(null, null);
				this.setTools();
			}
			this.drawPaints(this.getGraphics());
		}else if(this.state == GDrawingState.RESIZE) {
			
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
				for (int i=0;i<shapes.size();i++) if(shapes.get(i).onShape(startPoint)) {
				selectShape(i);
				break;
				}
			}else {
				boolean notSelect;
				if(this.shiftDown) {
					notSelect = false;
					for (int i=0;i<shapes.size();i++) {
						GShape shape = shapes.get(i);
						if(shape.onShape(startPoint)) {
							shape.select(!shape.isSelected());
							if(shape.isSelected()) {
								if(i==0)this.selectedShapes.add(i);
								else orderSelected(i);
							}
							else this.releaseSelected(i);
							break;
						}
					}
				}else {
					notSelect = true;
					for(Integer i : this.selectedShapes) {
						GShape shape = this.shapes.get(i);
						if(shape.grabResize(startPoint)!=-1 || shape.onShape(startPoint)) {
							notSelect = false;
							break;
						}
					}
				}
				if(notSelect) {
					this.selectShape(-1);
				}
			}
		}else if (state == GDrawingState.DRAWINGPOLYGON){
			this.transformer.addPoint(e.getPoint());
		}
		System.out.println(this.selectedShapes);
		this.drawPaints(getGraphics());
	}
	private void releaseSelected(int index) {
		for(int i=0;i<this.selectedShapes.size();i++) {
			if(index==this.selectedShapes.get(i).intValue()) {
				this.selectedShapes.remove(i);
			}
		}
	}
	/**
	 * Adds Integer object in descending order. 
	 * This method returns true for stop looping. Returned boolean means nothing.
	 * @param adding An adding Integer.
	 * @return Always returns true.
	 * */
	private boolean orderSelected(int adding) {
		for(int i=0;i<this.selectedShapes.size();i++) {
			System.out.println(adding+", "+this.selectedShapes.get(i)+", "+i);
			if(adding > this.selectedShapes.get(i)) {
				this.selectedShapes.add(i, adding);
				return true;
			}
		}
		this.selectedShapes.add(adding);
		return true;
	}

	public void polygonAnimation(Point p) {
		this.transformer.keep(p);
		this.drawPaints(this.getGraphics());
	}
	public void mouseDoubleClick(MouseEvent e) {
		System.out.println("double clicked");
		if(state == GDrawingState.DRAWINGPOLYGON) {
			this.finalizeTransforming(null);
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
		this.drawPaints(this.getGraphics());
	}
	
	public void deleteSelectedShape() {
		if(this.selectedShapes.size()!=0) {
			for(Integer i:this.selectedShapes) {
				this.shapes.removeElementAt(i);
			}
			this.selectedShapes.clear();
			this.drawPaints(this.getGraphics());
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
		this.shiftDown = b;
		System.out.println(this.shiftDown);
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

	public int getNumOfSelected() {
		return this.selectedShapes.size();
	}
	
	public void addToolPanel(Point addPoint) {
		JPanel toolPanel = new JPanel();
		toolPanel.setBounds(addPoint.x, addPoint.y, 100, 200);
		toolPanel.setBackground(Color.red);
		toolPanel.add(new JButton("asd"));
		this.add(toolPanel);
	}
//==========================================================================================
	private class MouseHandler implements MouseMotionListener, MouseListener{
	
		@Override
		public void mouseDragged(MouseEvent e) {
			keepTransforming(e);
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			if(state == GDrawingState.DRAWINGPOLYGON) {
				polygonAnimation(e.getPoint());
			}
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()==1) {
				if(e.getButton()==3) {
					addToolPanel(e.getPoint());
				}else if(e.getButton()==1) {
					mouseSingleClick(e);
				}
			}else if(e.getClickCount()==2) {
				mouseDoubleClick(e);
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			prepareTransforming(e);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(state!=GDrawingState.DRAWINGPOLYGON) {
				finalizeTransforming(e);
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {System.out.println(e.getPoint()+", mouseEntered");}
		@Override
		public void mouseExited(MouseEvent e) {System.out.println(e.getPoint()+", mouseExited");}
		
	}
}
