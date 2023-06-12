package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import javax.swing.JPanel;

import main.GContants.EAnchors;
import main.GContants.ECursor;
import main.GContants.EShape;
import main.GContants.EToolPanel;
import main.GContants.EUserAction;
import shapes.GShape;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GResizer;
import transformer.GRotator;
import transformer.GSelecter;
import transformer.GTransformer;
import valueObject.GShapeInfo;

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
		EROTATE,
		EMOVE,
		ESCALE;
	}
	
//	private int veiwPoint;
	private boolean shiftDown;
	private Point startPoint;
	private Vector<GShape> shapes;
	private GShape selectedShape;
	private GToolBar toolbar;
	private ECursor cursor;
	private GShapeInfo copied;
	private GToolList toolList;
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
		this.cursor = ECursor.DEFAULT;
		
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
				for(GShape shape : this.shapes) {
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
					clearSelection();
					this.state = EDrawingState.SELECTING;
					this.transformer = new GSelecter(this.shapes);
					this.cursor = ECursor.DEFAULT;
				}
			}else {
				switch(chosen) {
				case RR:
					this.state = EDrawingState.EROTATE;
					this.transformer = new GRotator(this.selectedShape);
					break;
				case MM:
					this.state = EDrawingState.EMOVE;
					this.transformer = new GMover(this.selectedShape);
					break;
				default:
					this.state = EDrawingState.ESCALE;
					this.transformer = GResizer.createGResizser
							(chosen.getAnchorNum(), this.selectedShape);
				}
				this.cursor = chosen.getCursor();
			}
			this.transformer.initTransform(p);
			System.out.println(this.state);
		}
		else if(state != EDrawingState.ENPDRAWING){
			clearSelection();
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
		double a = Math.toDegrees(-Math.atan2(y, x));//x축 기준 반시계
//		double a = Math.toDegrees(Math.atan2(x, -y));//y축 기준 시계
		System.out.println("angle : "+a); 
		this.transformer.keepTransform(p, this.shiftDown);
		this.drawPaints(this.getGraphics());
	}
	/**
	 * for mouseMoved
	 * */
	public void continueTransforming(Point p) {
		if(state == EDrawingState.ENPDRAWING) {
			this.transformer.keepTransform(p, this.shiftDown);
			this.drawPaints(this.getGraphics());
		}else this.selectCusor(p);
	}
	
	public void finalizeTransforming(Point p) {
		if(toolbar.getESelectedShape()!=EShape.SELECT) {
			if(state!=EDrawingState.ENPDRAWING) {
				GShape shape = this.transformer.finalizeTransform(p);
				if(shape!=null) {
					Color lineColor = this.toolbar.getDefaultLineColor();
					Color innerColor = this.toolbar.getDefaultInnerColor();
					shape.setInnerColor(innerColor);
					shape.setLineColor(lineColor);
					this.shapes.setElementAt(shape, 0);
				}else {
					this.shapes.remove(0);
				}
				this.selectShape(shape);
				toolbar.setShape(EShape.SELECT);
				this.state = EDrawingState.IDLE;
			}
		}else if (state == EDrawingState.SELECTING) {
			GShape shape = this.transformer.finalizeTransform(p);
			this.selectedShape = shape;
			this.selectShape(shape);
			this.state = EDrawingState.IDLE;
		}else {
			this.transformer.finalizeTransform(p);
			this.state = EDrawingState.IDLE;
		}
		this.drawPaints(this.getGraphics());
	}
	
	public void mouseSingleClick(Point p) {
		EShape selected = toolbar.getESelectedShape();
		if(selected==EShape.SELECT) {
			this.listAction(p);
			for(int i=0;i<this.shapes.size();i++) {
				GShape shape = this.shapes.get(i);
				if(shape.grab(p)) {
					selectShape(shape);
					break;
				}
			}
			
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
	public void append(char c) {
		if(this.selectedShape != null) {
			this.selectedShape.appendText(c);
		}
		this.drawPaints(getGraphics());
	}
	public void deleteChar() {
		if(this.selectedShape != null) {
			this.selectedShape.deleteChar();
		}
		this.drawPaints(getGraphics());
	}
	/**
	 * Clear selection without changing tool panel.
	 * */
	private void clearSelection() {
		if(this.selectedShape!=null) {
			this.selectedShape.setSelected(false);
			this.selectedShape = null;	
		}
	}
	
	/**
	 * Release all selected shapes and select one GShape object in Vector. 
	 * @param shape Object that want to select. If shape is null, select nothing.
	 * */
	private void selectShape(GShape shape) {
		if(shape == null) {
			this.clearSelection();
			this.toolbar.setTools(EToolPanel.EDRAWINGTOOL);
		}else {
			this.clearSelection();
			shape.setSelected(true);
			this.selectedShape = shape;
			this.toolbar.setTools(EToolPanel.ESELECTINGTOOL);
		}
//		shift + 클릭하면 그룹에 추가
	}
	
	public void deleteSelectedShape() {
		if(this.selectedShape != null) {
			this.selectedShape.delete();
			this.shapes.remove(this.selectedShape);
			this.selectedShape = null;
			this.drawPaints(this.getGraphics());
			this.toolbar.setTools(EToolPanel.EDRAWINGTOOL);
		}
	}
	public void setInnerColor(Color color) {
		selectedShape.setInnerColor(color);
		this.drawPaints(getGraphics());
	}
	public void setLineColor(Color color) {
		selectedShape.setLineColor(color);
		this.drawPaints(getGraphics());
	}
	public void setStrColor(Color color) {
		selectedShape.setStrColor(color);
		this.drawPaints(getGraphics());
	}
	public void group() {
		this.selectShape(selectedShape.group());
		this.drawPaints(getGraphics());
	}
	public void ungroup() {
		this.selectShape(selectedShape.ungroup());
		this.drawPaints(getGraphics());
	}
	public void shiftDown(boolean b) {
		this.shiftDown = b;
		System.out.println(this.shiftDown);
	}

	public void copy(boolean cut) {
		if(this.selectedShape!=null) {
			this.copied = this.selectedShape.getAllAttribute();
			if(cut)this.deleteSelectedShape();
		}
	}
	public void paste() {
		GShape shape = this.toolbar.generate(this.copied.getName());
		shape.setAtribute(copied);
		Rectangle r = copied.getShape().getBounds();
		if(onShape(r.x, r.y)!=null) {
			AffineTransform affine = new AffineTransform();
			affine.translate(10, 10);
			shape.translate(affine);
		}
		shape.finalizeTransforming();
		this.shapes.add(0, shape);
		this.selectShape(shape);
		this.drawPaints(getGraphics());
	}
	public void goDown() {
		if(this.selectedShape!=null) {
			int index = this.shapes.indexOf(this.selectedShape);
			if(index>this.shapes.size()) setZOrder(-1);
			else setZOrder(index+1);
		}
	}
	public void goUp() {
		if(this.selectedShape!=null) {
			int index = this.shapes.indexOf(this.selectedShape);
			if(index==0) setZOrder(0);
			else setZOrder(index-1);
		}
	}
	/**
	 * @param location is -1, than the bottom of order.
	 * */
	public void setZOrder(int location) {
		if(this.selectedShape!=null) {
			this.shapes.remove(selectedShape);
			if(location == -1) {
				shapes.add(selectedShape);
			}else shapes.add(location, selectedShape);
		}
		this.drawPaints(getGraphics());
	}
	
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
		if(!this.toolList.onShape(p))this.toolList.clear();
	}
	/**
	 * angle 
	 * <pre>
	 *           90
	 *           |
	 * 180 ------+------ 0
	 *           |
	 *          -90
	 * </pre>
	 * */
	private Point shiftPoint(Point p) {
		if(shiftDown) {
			int x = p.x - this.startPoint.x;
			int y = p.y - this.startPoint.y;
			switch(this.state){
			case ENPDRAWING:
			case SELECTING:
			case ESCALE:
				if(x>y) {
					p.setLocation(startPoint.x+x, startPoint.y+x);
				}else p.setLocation(startPoint.x+y, startPoint.y+y);
			case E2PDRAWING:
			case EMOVE:
			case EROTATE:
				double a = Math.toDegrees(-Math.atan2(y, x));//x축 기준 반시계
				if((a>-45&&a<=45)||a>135||a<=-135) p.setLocation(p.x, startPoint.y);
				else p.setLocation(startPoint.x, p.y);
				break;
			default:
			}
		}
		return p;
	}
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
