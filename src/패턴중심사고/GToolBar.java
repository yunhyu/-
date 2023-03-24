package 패턴중심사고;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

public class GToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6044105974330751117L;
	private int enabledButton;
	
	private JButton select,rectangle,oval,line,polygon;
	private JButton[] buttons;
	
	public GToolBar(int orientation) {super(orientation);}
	public GToolBar(String name) {super(name);}
	public GToolBar(String name, int orientation) {super(name, orientation);}

	public GToolBar() {
		this.select = new JButton("Select");
		this.rectangle = new JButton(new ImageIcon("resource/square.png"));
		this.oval = new JButton(new ImageIcon("resource/oval.png"));
		this.line = new JButton(new ImageIcon("resource/line.png"));
		this.polygon = new JButton(new ImageIcon("resource/polygon.png"));
		
		this.buttons = new JButton[5];
		this.buttons[0] = this.select;
		this.buttons[1] = this.rectangle;
		this.buttons[2] = this.oval;
		this.buttons[3] = this.line;
		this.buttons[4] = this.polygon;
		
		for(JButton btn:this.buttons) this.add(btn);
	}
	
	public void initialize(ActionListener action, int method) {
		for(int i=0;i<this.buttons.length;i++) {
			this.buttons[i].addActionListener(action);
			this.buttons[i].setActionCommand(i+"");
		}
		this.enabledButton = method;
		this.buttons[this.enabledButton].setEnabled(false);
	}
	public void setShape(int shape) {
		this.buttons[shape].setEnabled(false);
		if(shape!=this.enabledButton)this.buttons[this.enabledButton].setEnabled(true);
		this.enabledButton = shape;
	}
	
}
