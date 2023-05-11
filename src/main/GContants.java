package main;

import java.awt.Dimension;

import javax.swing.ImageIcon;

public class GContants {
	public static class CMainframe{
		public static final Dimension SIZE = new Dimension(600, 400);
		public static final String ICON = "resource/mju.png";
		public static final String TITLE = "그림판";
	}
	
	public static class CToolBar{
		
	}

	public enum EUserAction {
		e2Point,
		eNPoint
	}
	
	public enum EShape{
		ERECTANGLE(new ImageIcon("resource/square.png"), "shapes.GRectangle", EUserAction.e2Point),
		EROUNDRECT(new ImageIcon("resource/roundrect.png"), "shapes.GRoundRect", EUserAction.e2Point),
		EOVAL(new ImageIcon("resource/oval.png"), "shapes.GOval", EUserAction.e2Point),
		ELINE(new ImageIcon("resource/line.png"), "shapes.GLine", EUserAction.e2Point),
		ETRIANGLE(new ImageIcon("resource/triangle.png"), "shapes.GTriangle", EUserAction.e2Point),
		EFREELINE(new ImageIcon("resource/free.png"), "shapes.GFreeLine", EUserAction.e2Point),
		EPOLYGON(new ImageIcon("resource/polygon.png"), "shapes.GPolygon", EUserAction.eNPoint),
		SELECT(new ImageIcon("resource/select.png"), null, EUserAction.e2Point);

		private String shape;
		private ImageIcon image;
		private EUserAction userAction;
		
		private EShape(ImageIcon name, String shape, EUserAction userAction) {
			this.image = name;
			this.shape = shape;
			this.userAction = userAction;
		}
		public ImageIcon getImage() {
			return this.image;
		}
		public String getShape() {
			return this.shape;
		}
		public EUserAction getUserAction() {
			return this.userAction;
		}
	}
	
}
