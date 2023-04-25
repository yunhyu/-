package frame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import shapes.GShape;

public class GToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6044105974330751117L;

	private ButtonGroup group;
	private EShape selectedShape;
	
	public enum EShape{
		ERECTANGLE(new ImageIcon("resource/square.png"), "shapes.GRectangle"),
		EOVAL(new ImageIcon("resource/oval.png"), "shapes.GOval"),
		ELINE(new ImageIcon("resource/line.png"), "shapes.GLine"),
		EFREELINE(new ImageIcon("resource/free.png"), "shapes.GFreeLine"),
		EPOLYGON(new ImageIcon("resource/polygon.png"), "shapes.GPolygon"),
		SELECT(new ImageIcon("resource/select.png"), null);

		private String shape;
		private ImageIcon image;
		private EShape(ImageIcon name, String shape) {
			this.image = name;
			this.shape = shape;
		}
		public ImageIcon getImage() {
			return this.image;
		}
		public String getShape() {
			return this.shape;
		}
		
	}
	
	public GToolBar() {
		super();
		this.setFocusable(false);
		this.requestFocusInWindow();
		ListenerAction action = new ListenerAction();
		group = new ButtonGroup();
		
		for(EShape e : EShape.values()) {
			if(e == EShape.SELECT)continue;
			JRadioButton button = new JRadioButton(e.getImage());
			button.setActionCommand(e.toString());
			button.addActionListener(action);
			button.addMouseListener(action);
			button.setFocusable(false);
			group.add(button);
			this.add(button);
		}
	}
	
	public void initialize() {
		this.selectedShape = EShape.SELECT;
		this.setShape(EShape.SELECT);
	}
	/**Select shapeButton
	 * @param eButtonShape The shape to select.*/
	public void setShape(EShape eButtonShape) {
		JRadioButton button;
		if(this.selectedShape != EShape.SELECT) {
			button = (JRadioButton)(this.getComponent(this.selectedShape.ordinal()));
			button.setBackground(null);
		}
		if(eButtonShape != EShape.SELECT) {
			button = (JRadioButton)(this.getComponent(eButtonShape.ordinal()));
//			button.setBackground(null);
//			button.setBackground(Color.LIGHT_GRAY);
		}else this.group.clearSelection();
		this.selectedShape = eButtonShape;
	}
	public EShape getSelectedShape() {
		return this.selectedShape;
	}
	@SuppressWarnings("deprecation")
	public GShape generate(String shape) {
		if(shape==null) return null;
		try {
			Class<?> newShape = Class.forName(shape);
			return (GShape)newShape.newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private class ListenerAction implements ActionListener, MouseListener{

		private boolean mousePressed = false;
		private JRadioButton button;
		
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("action");
			EShape selected = EShape.valueOf(e.getActionCommand());
			if(selected == selectedShape)setShape(EShape.SELECT); 
			else setShape(selected);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JRadioButton button = (JRadioButton)e.getSource();
			if(!button.isSelected()&&!this.mousePressed)button.setBackground(Color.white);
		}

		@Override
		public void mouseExited(MouseEvent e) {
//			System.out.println("Exit");
			JRadioButton button = (JRadioButton)e.getSource();
			if(!button.isSelected()&&!this.mousePressed) button.setBackground(null);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			this.mousePressed = true;
			button = (JRadioButton)e.getSource();
			button.setBackground(Color.gray);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			this.mousePressed = false;
			System.out.println("release");
			if(button.isSelected()) button.setBackground(Color.lightGray);
			else button.setBackground(null);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {}
	}
}
