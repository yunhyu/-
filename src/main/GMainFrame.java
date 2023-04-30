package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("resource/mju.png");
		this.setIconImage(img); // 이미지 아이콘 세팅
	    setTitle("그림판"); // 제목
		
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
		
		this.canvus.setTransformer(transformer);
	}
	public void initialize() {
		this.toolBar.initialize(new ColorHandler());
		this.canvus.initialize();
		this.setVisible(true);
		
		EKeyHandler keyboard = new EKeyHandler();
		this.transformer.initialize(this.toolBar, this.canvus);
		
		this.addKeyListener(keyboard);
//		this.requestFocus();
	}
	private void reset() {
		this.toolBar.initialize(new ColorHandler());
		this.canvus.initialize();
		this.transformer.initialize(this.toolBar, this.canvus);
	}
	private void setColor() {
		Color in = this.toolBar.getInnerColor();
		Color line = this.toolBar.getLineColor();
		this.transformer.changeColor(in, line);
		this.transformer.drawPaints(this.canvus.getGraphics());
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
				transformer.shiftDown(true);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println("released");
			if(!e.isShiftDown()) {
				transformer.shiftDown(false);
			}
		}
		
	}

	private class ColorHandler implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			setColor();
		}
		
	}
	private class ListenerAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("new")) {
				reset();
			}
		}
	}

}
