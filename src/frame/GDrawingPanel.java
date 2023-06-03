package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import frame.GToolBar.ETools;
import main.GContants.EAnchors;
import main.GContants.ECursor;
import main.GContants.EShape;
import main.GContants.EUserAction;
import shapes.GShape;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GResizer;
import transformer.GRotator;
import transformer.GSelecter;
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
	
	private enum EDrawingState {
		IDLE,
		SELECTING,
		E2PDRAWING,
		ENPDRAWING,
		MOVE,
		ROTATE,
		RESIZE,
		
		ETRANSFORMING;
	}
	
//	private int veiwPoint;
	private boolean shiftDown;
	private Point startPoint;
	private Vector<GShape> shapes;
	private GShape selectedShape;
	private GToolBar toolbar;
	private ECursor cursor;
	private ETools currentTool;
	private GTransformer transformer;
	private EDrawingState state;

	public GDrawingPanel() {
		super();
		this.setLayout(null);
		this.setBackground(Color.white);
		this.state = EDrawingState.IDLE;
		this.shapes = new Vector<GShape>();
		
		this.toolList = new GToolList();
		System.out.println(this.getFont());
		this.setFont(new Font(Font.SERIF, Font.PLAIN, 5));
	}
	public void initialize(GToolBar toolbar) {
		this.toolbar = toolbar;
		this.currentTool = ETools.ESELECTINGTOOL;
		this.cursor = ECursor.DEFAULT;
		this.dsetTools();
		
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
		if(this.state == EDrawingState.IDLE) {
			if(this.selectedShape != null) {
				selectedShape.drawAnchors(bfrG);
			}
			this.toolList.draw(bfrG);
		}
		g.drawImage(buffer, 0,0,null);
	}
	private void selectCusor(Point p) {
		if(toolbar.getESelectedShape() == EShape.SELECT) {
			if(this.selectedShape != null) {
				EAnchors chosen = this.onShape(p.x, p.y);
				if(chosen != null) {
					this.cursor = chosen.getCursor();
				}else {
					this.cursor = ECursor.DEFAULT;
				}
			}else {
				this.cursor = ECursor.DEFAULT;
				for(GShape shape : shapes) {
					if(shape.grab(p)) {
						this.cursor = ECursor.HAND;
						break;
					}
				}
			}
		}else if(state == EDrawingState.ENPDRAWING) {
			this.cursor = ECursor.CROSS;
		}
		this.setCursor(this.cursor.getCursor());
	}
	
	private void initTransforming(Point p) {
		this.startPoint = p;
		EShape method = toolbar.getESelectedShape();
		if(method == EShape.SELECT) {
			EAnchors chosen = onShape(p.x, p.y);
			if(chosen == null) {
				this.cursor = ECursor.DEFAULT;
				if(!this.shiftDown) {
					this.clearSelection();
					this.state = EDrawingState.SELECTING;
					this.transformer = new GSelecter(this.shapes);
					this.cursor = ECursor.DEFAULT;
				}
			}else {
				switch(chosen) {
				case RR:
					this.state = EDrawingState.ROTATE;
					this.transformer = new GRotator(this.selectedShape);
					break;
				case MM:
					this.state = EDrawingState.MOVE;
					this.transformer = new GMover(this.selectedShape);
					break;
				default:
					this.state = EDrawingState.RESIZE;
					this.transformer = GResizer.createGResizser
							(chosen.getAnchorNum(), this.selectedShape);
				}
				this.cursor = chosen.getCursor();
			}
			this.transformer.initTransform(p);
			System.out.println(this.state);
		}
		else if(state != EDrawingState.ENPDRAWING){
			if(this.selectedShape != null) this.clearSelection();
			GShape drawingShape = this.toolbar.generate(method.getShape());
			shapes.add(0, drawingShape);
			this.transformer = new GDrawer(drawingShape);
			EUserAction userAction = method.getUserAction();
			if(userAction == EUserAction.eNPoint) {
				this.state = EDrawingState.ENPDRAWING;
				this.cursor = ECursor.CROSS;
			}else {
				this.state = EDrawingState.E2PDRAWING;
				this.cursor = ECursor.DEFAULT;
			}
			this.transformer.initTransform(p);
		}
		this.setCursor(this.cursor.getCursor());
		this.drawPaints(this.getGraphics());
	}
	
	public void keepTransforming(Point p) {
		int x = p.x - this.startPoint.x;
		int y = p.y - this.startPoint.y;
		double a = Math.toDegrees(-Math.atan2(y, x));
		System.out.println("angle : "+a);
		if(this.shiftDown) {
			
			
		}else ; 
		this.transformer.keepTransform(p);
		this.drawPaints(this.getGraphics());
	}
	/**
	 * for mouseMoved
	 * */
	public void continueTransforming(Point p) {
		if(state == EDrawingState.ENPDRAWING) {
			this.transformer.keepTransform(p);
			this.drawPaints(this.getGraphics());
		}else this.selectCusor(p);
	}
	
	public void finalizeTransforming(Point p) {
		if(toolbar.getESelectedShape()!=EShape.SELECT) {
			if(state!=EDrawingState.ENPDRAWING) {
				Color lineColor = this.toolbar.getDefaultLineColor();
				Color innerColor = this.toolbar.getDefaultInnerColor();
				GShape shape = this.transformer.finalizeTransform(innerColor, lineColor);
				if(shape!=null) {
					this.shapes.setElementAt(shape, 0);
					this.selectShape(0);
				}else {
					this.shapes.remove(0);
				}
				toolbar.setShape(EShape.SELECT);
				this.state = EDrawingState.IDLE;
			}
		}else if (state == EDrawingState.SELECTING) {
			GShape shape = this.transformer.finalizeTransform(null, null);
			if(shape!=null) this.shapes.add(0, shape);
			this.selectedShape = shape;
			this.setTools();
			this.state = EDrawingState.IDLE;
		}else {
			this.transformer.finalizeTransform(null, null);
			this.state = EDrawingState.IDLE;
		}
		this.drawPaints(this.getGraphics());
	}
	
	public void mouseSingleClick(Point p) {
		EShape selected = toolbar.getESelectedShape();
		if(selected==EShape.SELECT) {
			singleSelect(p);
		}else if (state == EDrawingState.ENPDRAWING){
			transformer.continueTransform(p);
		}
		selectCusor(p);
		this.drawPaints(this.getGraphics());
	}
	
	public void mouseDoubleClick(Point p) {
		System.out.println("double clicked");
		if(state == EDrawingState.ENPDRAWING) {
			this.state = EDrawingState.IDLE;
			finalizeTransforming(null);
			this.setCursor(ECursor.DEFAULT.getCursor());
		}
		this.drawPaints(this.getGraphics());
	}
	
	private EAnchors onShape(int x, int y) {
		EAnchors chosen = null;
		if(selectedShape != null) {
			chosen = selectedShape.onShape(x, y);
		}
		return chosen;
	}
	private void setTools() {
		
	}
	private void dsetTools() {
		ETools tool;
		if(selectedShape != null) {
			tool = ETools.ESELECTINGTOOL;
			Color line = null;
			Color inner = null;
			if(this.selectedShape!=null) {
				GShape shape = this.selectedShape;
				line = shape.getLineColor();
				inner = shape.getInnerColor();
			}
			this.toolbar.setDefaultLineColor(line);
			this.toolbar.setDefaultInnerColor(inner);
		}
		else tool = ETools.EDRAWINGTOOL; 
		if(tool != this.currentTool) {
			this.toolbar.setTools(tool);
			this.currentTool = tool;
		}
	}
	private void singleSelect(Point p) {
		if(this.selectedShape == null) {
			for (int i=0;i<shapes.size();i++) if(shapes.get(i).grab(p)) {
				selectShape(i);
				break;
			}
		}else {
			if(this.shiftDown) {
				for (int i=0;i<shapes.size();i++) {
					GShape shape = shapes.get(i);
					if(shape.grab(p)) {
//						shape.setSelected(!shape.isSelected());
//						if(shape.isSelected()) {
//							if(i==0)this.selectedShape = shape;
//							else orderSelected(i);
//						}
//						else this.releaseSelected(i);
//						shift + 클릭하면 그룹에 추가  
						break;
					}
				}
			}else {
				this.clearSelection();
			}
		}
	}
	
	private void clearSelection() {
		if(this.selectedShape!=null) {
			this.selectedShape.setSelected(false);
			this.selectedShape = null;			
		}
	}
	
	/**
	 * Release all selected shapes and select one GShape object in Vector. 
	 * @param index The number of shape in Vector, shapes. 
	 * If index is -1, select nothing.
	 * */
	private void selectShape(int index) {
		this.clearSelection();
		if(index != -1) {
			shapes.get(index).setSelected(true);
			this.selectedShape = this.shapes.get(index);
			this.setTools();
		}
	}
	
	public void deleteSelectedShape() {
//		if(this.selectedShape.size()!=0) {
//			for(Integer i:this.selectedShape) {
//				this.shapes.removeElementAt(i);
//			}
//			this.selectedShape.clear();
//			this.drawPaints(this.getGraphics());
//			this.setTools();
//		}
	}
	public void changeColor(Color inner, Color line) {
		selectedShape.setInnerColor(inner);
		selectedShape.setLineColor(line);
	}
	public void setInnerColor(Color color) {
		selectedShape.setInnerColor(color);
	}
	public void setLineColor(Color color) {
		selectedShape.setLineColor(color);
	}
	public void shiftDown(boolean b) {
		this.shiftDown = b;
		System.out.println(this.shiftDown);
	}
//	public void goUp() {
//		GShape index0 = null;
//		for(Integer i : this.selectedShape) {
//			GShape shape = this.shapes.get(i);
//			if(i==0) {
//				index0 = shape;
//				continue;
//			}
//			this.shapes.add(i-1,shape);
//			
//		}
//		if(index0 != null) {
//			this.shapes.add(0, index0);
//		}
//	}
//	public void goDown() {
//		GShape index0 = null;
//		for(Integer i : this.selectedShape) {
//			GShape shape = this.shapes.get(i);
//			if(i==0) {
//				index0 = shape;
//				continue;
//			}
//			this.shapes.add(i+1,shape);
//			
//		}
//		if(index0 != null) {
//			this.shapes.add(1, index0);
//		}
//	}

//	public int getNumOfSelected() {
//		return this.selectedShape.size();
//	}
	
	public void addToolPanel(Point addPoint) {
//		this.panel.setElements(new Vector<GToolElement>());
		EAnchors anchor = null;
		for(GShape shape : this.shapes) {
			anchor = shape.onShape(addPoint.x, addPoint.y); 
			if(anchor == EAnchors.MM){
				this.toolList.test((Graphics2D)this.getGraphics());
				break;
			}
		}
		if(anchor == null)this.toolList.setElements(new Vector<GToolElement>(), this.getGraphics());
		
		this.toolList.setBounds(addPoint.x, addPoint.y);
		this.drawPaints(getGraphics());
//		this.paintImmediately(panel.getBounds());
//		DefaultListModel model;
//		model.
	}
	
	private void listAction(Point p) {
		if(this.toolList.onShape(p))this.toolList.clear();
	}
	
	private GToolList toolList;
//==========================================================================================
	private class MouseHandler implements MouseMotionListener, MouseListener{
	
		@Override
		public void mouseDragged(MouseEvent e) {
			keepTransforming(e.getPoint());
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			continueTransforming(e.getPoint());
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()==1) {
				if(e.getButton()==3) {
					addToolPanel(e.getPoint());
					
				}else if(e.getButton()==1) {
					mouseSingleClick(e.getPoint());
				}
			}else if(e.getClickCount()==2) {
				mouseDoubleClick(e.getPoint());
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			initTransforming(e.getPoint());
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			finalizeTransforming(e.getPoint());
			System.out.println(e.getButton());
		}
		@Override
		public void mouseEntered(MouseEvent e) {System.out.println(e.getPoint()+", mouseEntered");}
		@Override
		public void mouseExited(MouseEvent e) {System.out.println(e.getPoint()+", mouseExited");}
		
	}
}
