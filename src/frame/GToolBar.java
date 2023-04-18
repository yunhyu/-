package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	private EShape selectedShape;
	
	public enum EShape{
		SELECT(new ImageIcon("resource/select.png"), null),
		ERECTANGLE(new ImageIcon("resource/square.png"), "shapes.GRectangle"),
		EOVAL(new ImageIcon("resource/oval.png"), "shapes.GOval"),
		ELINE(new ImageIcon("resource/line.png"), "shapes.GLine"),
		EFREELINE(new ImageIcon("resource/free.png"), "shapes.GFreeLine"),
		EPOLYGON(new ImageIcon("resource/polygon.png"), "shapes.GPolygon");

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
		ListenerAction action = new ListenerAction();
		ButtonGroup group = new ButtonGroup();
		for(EShape e : EShape.values()) {
			JRadioButton button = new JRadioButton(e.getImage());
			button.setActionCommand(e.toString());
			button.addActionListener(action);
			this.add(button);
			group.add(button);
		}
	}
	
	public void initialize() {
		this.selectedShape = EShape.SELECT;
		this.setShape(EShape.SELECT);
	}
	/**Select shapeButton
	 * @param eButtonShape The shape to select.*/
	public void setShape(EShape eButtonShape) {
		JRadioButton button = (JRadioButton)(this.getComponent(this.selectedShape.ordinal()));
		button.setEnabled(true);
		button = (JRadioButton)(this.getComponent(eButtonShape.ordinal()));
		button.setSelected(true);
		button.setEnabled(false);
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
	
	private class ListenerAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			EShape selectedShape = EShape.valueOf(e.getActionCommand());
			setShape(selectedShape);
		}
	}
}
