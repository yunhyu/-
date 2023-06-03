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

import javax.swing.JColorChooser;
import javax.swing.JFrame;

import frame.GDrawingPanel;
import frame.GMenuBar;
import frame.GToolBar;

public class GMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5629717143701652344L;
	
	private GToolBar toolbar;
	private GMenuBar menuBar;
	private GDrawingPanel canvus;
	private BorderLayout layout;
	
	public GMainFrame(GraphicsConfiguration gc) {super(gc);}
	public GMainFrame(String title) throws HeadlessException {super(title);}
	public GMainFrame(String title, GraphicsConfiguration gc) {super(title, gc);}

	public GMainFrame() throws HeadlessException {
		this.setSize(GContants.CMainframe.SIZE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage(GContants.CMainframe.ICON);
		this.setIconImage(img); // 이미지 아이콘 세팅
	    setTitle(GContants.CMainframe.TITLE); // 제목
		
		this.menuBar = new GMenuBar();
		this.toolbar = new GToolBar();
		this.canvus = new GDrawingPanel();
		
		this.layout = new BorderLayout();
		this.setLayout(layout);
		this.setJMenuBar(this.menuBar);
		
		this.add(toolbar,BorderLayout.NORTH);
		this.add(canvus,BorderLayout.CENTER);
	}
	public void initialize() {
		this.toolbar.initialize(new ColorListener(this));
		this.canvus.initialize(this.toolbar);
		this.setVisible(true);
		
		EKeyHandler keyboard = new EKeyHandler();
		this.addKeyListener(keyboard);
		this.requestFocus();
	}
	private void reset() {
		this.toolbar.initialize(new ColorListener(this));
		this.canvus.initialize(this.toolbar);
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
				canvus.deleteSelectedShape();
			}else if(input>7&&input<127) {
				
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println("pressed");
			if(e.isShiftDown()) {
				canvus.shiftDown(true);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println("released");
			if(!e.isShiftDown()) {
				canvus.shiftDown(false);
			}
		}
		
	}

	private class ColorHandler implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class ColorListener implements ActionListener{

		private GMainFrame parent;
		
		public ColorListener(GMainFrame parent) {
			this.parent = parent;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			Color c = JColorChooser.showDialog(parent, getTitle(), getForeground());
			switch(e.getActionCommand()) {
			case "default-in":
				toolbar.setDefaultInnerColor(c);
				break;
			case "default-line":
				toolbar.setDefaultLineColor(c);
				break;
			case "shape-in":
				break;
			case "shape-line":
				break;
				
			}
		}
	}

}
