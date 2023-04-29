package frame;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GFileMenu extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1474586358815320135L;

	
	
	public GFileMenu(String title) {
		super(title);
		
		JMenuItem itemNew = new JMenuItem("new");
		this.add(itemNew);
	}
}
