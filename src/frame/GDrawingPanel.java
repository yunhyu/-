package frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import control.GTransformer;
import control.GTransformer.GState;

public class GDrawingPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5537928810270967677L;
	
	private GTransformer transformer;

	public GDrawingPanel() {
		super();
		this.setBackground(Color.white);
	}
	public void initialize(GTransformer transformer) {
		this.transformer = transformer;
		MouseHandler mouse = new MouseHandler();
		this.addMouseMotionListener(mouse);
		this.addMouseListener(mouse);
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
				transformer.mouseSingleClick(e);
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
			if(!transformer.state(GState.DRAWINGPOLYGON)) {
				transformer.finalizeTransforming(e);
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {System.out.println(e.getPoint()+", mouseEntered");}
		@Override
		public void mouseExited(MouseEvent e) {System.out.println(e.getPoint()+", mouseExited");}
		
	}
}
