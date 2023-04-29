package main;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import control.GTransformer;
import frame.GDrawingPanel;
import frame.GMenuBar;
import frame.GToolBar;

public class GMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5629717143701652344L;
	
	private GToolBar toolBar;
	private GMenuBar menuBar;
	private GDrawingPanel canvus;
	private GTransformer transformer;
//	private ListenerAction action;
	private BorderLayout layout;
	
	public GMainFrame(GraphicsConfiguration gc) {super(gc);}
	public GMainFrame(String title) throws HeadlessException {super(title);}
	public GMainFrame(String title, GraphicsConfiguration gc) {super(title, gc);}

	public GMainFrame() throws HeadlessException {
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.menuBar = new GMenuBar();
		this.toolBar = new GToolBar();
		this.transformer = new GTransformer();
		this.canvus = new GDrawingPanel();
//		this.action = new ListenerAction();
		
		this.layout = new BorderLayout();
		this.setLayout(layout);
		this.setJMenuBar(this.menuBar);
//		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		this.add(toolBar,BorderLayout.NORTH);
		this.add(canvus,BorderLayout.CENTER);
	}
	public void initialize() {
		this.setVisible(true);
		
		EKeyHandler keyboard = new EKeyHandler();
		this.toolBar.initialize();
		this.transformer.initialize(this.toolBar, this.canvus);
		this.canvus.initialize(this.transformer);
		
		this.addKeyListener(keyboard);
//		this.requestFocus();
	}
	
//===============================================================================
	
	private class EKeyHandler implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			System.out.println("typed "+(int)e.getKeyChar());
			int input = e.getKeyChar();
			if(input==3) {//ctrl c
				
			}
			else if(input==127) {
				transformer.deleteSelectedShape();
			}else if(input>7&&input<127) {
				
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println("pressed");
			if(e.isShiftDown()) {
				transformer.setFixed(true);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println("released");
			if(!e.isShiftDown()) {
				transformer.setFixed(false);
			}
		}
		
	}
//	private class ListenerAction implements ActionListener{
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			String dummy = e.getActionCommand();
//			int shape = Integer.parseInt(dummy);
//			toolBar.setShape(shape);
//			canvus.setMethod(shape);
//		}
//	}

}
