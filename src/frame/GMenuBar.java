package frame;

import javax.swing.JMenuBar;

public class GMenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6962024462360083513L;

	private GFileMenu fileMenu;
	
	public GMenuBar() {
		this.fileMenu = new GFileMenu("File");
		this.add(fileMenu);
	}

}
