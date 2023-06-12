package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import main.GContants.EButtons;
import main.GContants.EShape;
import main.GContants.EToolPanel;
import shapes.GShape;

public class GToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6044105974330751117L;

	private Color line, inner;
	private ButtonGroup group;
	private EShape eSelectedShape;
	
	public GToolBar() {
		super();
		this.setFocusable(false);
		ListenerAction action = new ListenerAction();
		group = new ButtonGroup();
		JPanel panel = EToolPanel.EDRAWINGTOOL.getTools();
		
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
		File file = new File("C:/Windows//Fonts");
		if(file.exists()) {
//			for(String str : file.list()) {
//				System.out.println(str);
//			}
		}
//		line = new JComboBox<EColor>();
//		in = new JComboBox<EColor>();
//		this.addColorBox(line, EColor.BLACK, ETool.ESELECTINGTOOL);
//		this.addColorBox(in, EColor.NONE, ETool.ESELECTINGTOOL);
//		defaultLine = new JComboBox<EColor>();
		
//		this.addColorBox(defaultLine, EColor.BLACK, ETool.EDRAWINGTOOL);
//		this.addColorBox(defualtIn, EColor.WHITE, ETool.EDRAWINGTOOL);
		
//		try {
//			Font[] font = Font.createFonts(file);
//			l.setFont(font[0]);
//		} catch (FontFormatException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		this.setButton(panel, EButtons.DEFAULT_IN);
		this.setButton(panel, EButtons.DEFAULT_LINE);

		JComboBox<String> font = new JComboBox<String>();
		font.setFocusable(false);
		for(File f : file.listFiles()) {
			font.addItem(f.getName());
		}
		panel.add(font);
		
		panel = EToolPanel.ESELECTINGTOOL.getTools();
		for(EButtons button : EButtons.values()) {
			if(button.ordinal()>1)this.setButton(panel, button);
		}
	}
	private void setButton(JPanel panel, EButtons button) {
		String str = button.getName(); 
		if(str != null) panel.add(new JLabel(str));
		panel.add(button.getButton());
	}
	
	public void initialize(ActionListener listener) {
		this.eSelectedShape = EShape.SELECT;
		this.setShape(EShape.SELECT);
		
		for(EButtons button : EButtons.values()) {
			button.getButton().addActionListener(listener);
		}
		
		this.setDefaultLineColor(Color.black);
		this.setDefaultInnerColor(null);
		this.add(EToolPanel.EDRAWINGTOOL.getTools());
	}
	/**Select shapeButton
	 * @param eButtonShape The shape to select.*/
	public void setShape(EShape eButtonShape) {
		JRadioButton button;
		JPanel panel = EToolPanel.EDRAWINGTOOL.getTools();
		if(this.eSelectedShape != EShape.SELECT) {
			button = (JRadioButton)(panel.getComponent(this.eSelectedShape.ordinal()));
			button.setBackground(null);
		}
		if(eButtonShape != EShape.SELECT) {
			button = (JRadioButton)(panel.getComponent(eButtonShape.ordinal()));
		}else this.group.clearSelection();
		this.eSelectedShape = eButtonShape;
	}
	
	public void setTools(EToolPanel tool) {
		this.removeAll();
		this.add(tool.getTools());
		this.paintAll(getGraphics());
	}
	public EShape getESelectedShape() {
		return this.eSelectedShape;
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
		return this.line;
	}
	public Color getDefaultInnerColor() {
		return this.inner;
	}
	
	public void setDefaultLineColor(Color c) {
		this.line = c;
		EButtons.DEFAULT_LINE.getButton().setBackground(c);
	}
	public void setDefaultInnerColor(Color c) {
		this.inner = c;
		EButtons.DEFAULT_IN.getButton().setBackground(c);
	}
//==================================================================================
	private class ListenerAction implements ActionListener, MouseListener{

		private boolean mousePressed = false;
		private JRadioButton button;
		
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("action");
			EShape selected = EShape.valueOf(e.getActionCommand());
			if(selected == eSelectedShape)setShape(EShape.SELECT); 
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
