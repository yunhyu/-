package frame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import main.GContants.EShape;
import shapes.GShape;

public class GToolBar extends JToolBar {

	/**
	 * -6044105974330751117L
	 */
	private static final long serialVersionUID = -6044105974330751117L;

	private ItemListener itemHandler;
	private JComboBox<EColor> line, in, defaultLine, defualtIn;
	private ButtonGroup group;
	private EShape selectedShape;
	
	public enum ETool{
		EDRAWINGTOOL(new JPanel()),
		ESELECTINGTOOL(new JPanel());
		
		private JPanel panel;
		
		private ETool(JPanel panel) {
			this.panel = panel;
			panel.setLayout(new FlowLayout());
		}
		private JPanel getTools() {
			return this.panel;
		}
	}
	public enum EColor{
		NONE(null),
		RED(Color.red), 
		BLUE(Color.blue),
		GREEN(Color.green),
		YELLOW(Color.yellow),
		ORANGE(Color.orange),
		BLACK(Color.black),
		WHITE(Color.white);
		
		private Color color;
		private EColor(Color color) {
			this.color = color;
		}
		public Color getColor() {
			return this.color;
		}
	}
//	JColorChooser
	public GToolBar() {
		super();
		this.setFocusable(false);
		ListenerAction action = new ListenerAction();
		group = new ButtonGroup();
		JPanel panel = ETool.EDRAWINGTOOL.getTools();
		
		for(EShape e : EShape.values()) {
			if(e == EShape.SELECT)continue;
			JRadioButton button = new JRadioButton(e.getImage());
			button.setActionCommand(e.toString());
			button.addActionListener(action);
			button.addMouseListener(action);
			button.setFocusable(false);
			group.add(button);
			panel.add(button);
		}
		line = new JComboBox<EColor>();
		in = new JComboBox<EColor>();
		this.addColorBox(line, EColor.BLACK, ETool.ESELECTINGTOOL);
		this.addColorBox(in, EColor.NONE, ETool.ESELECTINGTOOL);
		defaultLine = new JComboBox<EColor>();
		defualtIn = new JComboBox<EColor>();
		this.addColorBox(defaultLine, EColor.BLACK, ETool.EDRAWINGTOOL);
		this.addColorBox(defualtIn, EColor.NONE, ETool.EDRAWINGTOOL);
	}
	private void addColorBox (JComboBox<EColor> box, EColor defaultColor, ETool tool) {
		for(EColor c:EColor.values()) {
			box.addItem(c);
		}
		box.setSelectedItem(defaultColor);
		box.setFocusable(false);
		tool.getTools().add(box);
	}
	public void initialize(ItemListener listener) {
		this.selectedShape = EShape.SELECT;
		this.setShape(EShape.SELECT);
		this.itemHandler = listener;
	}
	/**Select shapeButton
	 * @param eButtonShape The shape to select.*/
	public void setShape(EShape eButtonShape) {
		JRadioButton button;
		JPanel panel = ETool.EDRAWINGTOOL.getTools();
		if(this.selectedShape != EShape.SELECT) {
			button = (JRadioButton)(panel.getComponent(this.selectedShape.ordinal()));
			button.setBackground(null);
		}
		if(eButtonShape != EShape.SELECT) {
			button = (JRadioButton)(panel.getComponent(eButtonShape.ordinal()));
		}else this.group.clearSelection();
		this.selectedShape = eButtonShape;
	}
	
	public void setTools(ETool tool) {
		this.removeAll();
		if(tool == ETool.ESELECTINGTOOL) {
			this.line.addItemListener(this.itemHandler);
			this.in.addItemListener(this.itemHandler);
		}else {
			this.line.removeItemListener(itemHandler);
			this.in.removeItemListener(itemHandler);
		}
		this.add(tool.getTools());
		this.paintAll(getGraphics());
//		System.out.println(tool.getTools().getComponentCount()+", "+this.countComponents());
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
	public Color getDefaultLineColor() {
		return this.getEColor(this.defaultLine);
	}
	public Color getDefaultInnerColor() {
		return this.getEColor(this.defualtIn);
	}
	public Color getLineColor() {
		return this.getEColor(line);
	}
	public Color getInnerColor() {
		return this.getEColor(in);
	}
	private Color getEColor(JComboBox<EColor> box) {
		EColor eColor = (EColor)box.getSelectedItem();
		return eColor.getColor();
	}
	
	public void setLineColor(Color c) {
		this.setEColor(line, c);
	}
	public void setInnerColor(Color c) {
		this.setEColor(in, c);
	}
	private void setEColor(JComboBox<EColor> box, Color c) {
		if(c==null) {
			box.setSelectedItem(EColor.NONE);
		}else {
			for(EColor eColor : EColor.values()) {
				if(c.equals(eColor.getColor())) {
					box.setSelectedItem(eColor);
					break;
				}
			}
		}
	}
//==================================================================================
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
