package 패턴중심사고;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class GToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6044105974330751117L;
	
	private int preButton;
	private JButton[] buttons;
	private GDrawingPanel dPanel;
	
	public enum EButtonShape{
		SELECT(new ImageIcon("resource/select.png")),
		ERECTANGLE(new ImageIcon("resource/square.png")),
		EOVAL(new ImageIcon("resource/oval.png")),
		ELINE(new ImageIcon("resource/line.png")),
		EPOLYGON(new ImageIcon("resource/polygon.png"));
		
		private ImageIcon image;
		private EButtonShape(ImageIcon name) {
			this.image = name;
		}
		public ImageIcon getImage() {
			return this.image;
		}
	}

	public GToolBar() {
		ListenerAction action = new ListenerAction();
		this.buttons = new JButton[EButtonShape.values().length];
		int i = 0;
		for(EButtonShape e : EButtonShape.values()) {
			JButton button = new JButton(e.getImage());
			button.setActionCommand(e.toString());
			button.addActionListener(action);
			this.add(button);
			this.buttons[i] = button;
			i++;
		}
	}
	
	public void initialize(GDrawingPanel dPanel) {
		this.dPanel = dPanel;
		this.preButton = EButtonShape.SELECT.ordinal();
		this.buttons[EButtonShape.SELECT.ordinal()].setEnabled(false);
		dPanel.setMethod(preButton);
	}
	public void setShape(String eButtonShape) {
		EButtonShape chosen = EButtonShape.valueOf(eButtonShape);
		this.buttons[chosen.ordinal()].setEnabled(false);
		this.buttons[this.preButton].setEnabled(true);
		this.preButton = chosen.ordinal();
		this.dPanel.setMethod(chosen.ordinal());
	}

	private class ListenerAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			setShape(e.getActionCommand());
		}
	}
}
