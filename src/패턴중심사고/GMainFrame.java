package 패턴중심사고;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class GMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5629717143701652344L;
	
	private GToolBar toolBar;
	private GMenuBar menuBar;
	private GDrawingPanel canvus;
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
		this.canvus = new GDrawingPanel();
//		this.action = new ListenerAction();
		
		this.layout = new BorderLayout();
		this.setLayout(layout);
		this.setJMenuBar(this.menuBar);
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		this.add(toolBar,BorderLayout.NORTH);
		this.add(canvus,BorderLayout.CENTER);
	}
	public void initialize() {
		this.setVisible(true);
		
		this.toolBar.initialize(this.canvus);
		this.canvus.initialize(0);
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
