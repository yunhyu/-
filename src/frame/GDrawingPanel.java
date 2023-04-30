package frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import control.GTransformer;
import control.GTransformer.GDrawingState;

public class GDrawingPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5537928810270967677L;
	
	private GTransformer transformer;

	public GDrawingPanel() {
		super();
		this.setLayout(null);
		this.setBackground(Color.white);
	}
	public void setTransformer(GTransformer transformer) {
		this.transformer = transformer;
	}
	public void initialize() {
		MouseHandler mouse = new MouseHandler();
		this.addMouseMotionListener(mouse);
		this.addMouseListener(mouse);
	}
	public void addToolPanel(Point addPoint) {
		JPanel toolPanel = new JPanel();
		toolPanel.setBounds(addPoint.x, addPoint.y, 100, 200);
		toolPanel.setBackground(Color.red);
		toolPanel.add(new JButton("asd"));
		this.add(toolPanel);
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		this.transformer.drawPaints(g);
	}
//==========================================================================================
	private class MouseHandler implements MouseMotionListener, MouseListener{
	
		@Override
		public void mouseDragged(MouseEvent e) {
			transformer.keepTransforming(e);
//			System.out.println("   mouseDragged");
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			transformer.polygonAnimation(e.getPoint());
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()==1) {
				if(e.getButton()==3) {
					addToolPanel(e.getPoint());
				}else if(e.getButton()==1) {
					transformer.mouseSingleClick(e);
				}
			}else if(e.getClickCount()==2) {
				transformer.mouseDoubleClick(e);
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			transformer.prepareTransforming(e);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(!transformer.state(GDrawingState.DRAWINGPOLYGON)) {
				transformer.finalizeTransforming(e);
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {System.out.println(e.getPoint()+", mouseEntered");}
		@Override
		public void mouseExited(MouseEvent e) {System.out.println(e.getPoint()+", mouseExited");}
		
	}
}
