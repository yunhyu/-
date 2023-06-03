package main;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;

import javax.swing.ImageIcon;

public class GContants {
	public static class CMainframe{
		public static final Dimension SIZE = new Dimension(600, 400);
		public static final String ICON = "resource/icons/mju.png";
		public static final String TITLE = "그림판";
	}
	
	public static class CToolBar{
		
	}
	
	public static class CAnchors{
		public static final int RESIZE_ANCHOR_RADIUS = 3;
		public static final int ROTATE_ANCHOR_RADIUS = 5;
	}

	public enum EUserAction {
		e2Point,
		eNPoint
	}
	
	public enum EShape{
		ERECTANGLE(new ImageIcon("resource/icons/square.png"), "shapes.GRectangle", EUserAction.e2Point),
		EROUNDRECT(new ImageIcon("resource/icons/roundrect.png"), "shapes.GRoundRect", EUserAction.e2Point),
		EOVAL(new ImageIcon("resource/icons/oval.png"), "shapes.GOval", EUserAction.e2Point),
		ELINE(new ImageIcon("resource/icons/line.png"), "shapes.GLine", EUserAction.e2Point),
		ETRIANGLE(new ImageIcon("resource/icons/triangle.png"), "shapes.GTriangle", EUserAction.e2Point),
		EFREELINE(new ImageIcon("resource/icons/free.png"), "shapes.GFreeLine", EUserAction.e2Point),
		EPOLYGON(new ImageIcon("resource/icons/polygon.png"), "shapes.GPolygon", EUserAction.eNPoint),
		
		SELECT(new ImageIcon("resource/icons/select.png"), null, EUserAction.e2Point);

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
	public enum ECursor{
		SW (new Cursor(Cursor.SW_RESIZE_CURSOR)),
		SS (new Cursor(Cursor.S_RESIZE_CURSOR)),
		SE (new Cursor(Cursor.SE_RESIZE_CURSOR)),
		WW (new Cursor(Cursor.W_RESIZE_CURSOR)),
		NE (new Cursor(Cursor.NE_RESIZE_CURSOR)),
		NN (new Cursor(Cursor.N_RESIZE_CURSOR)),
		NW (new Cursor(Cursor.NW_RESIZE_CURSOR)),
		EE (new Cursor(Cursor.E_RESIZE_CURSOR)),
		WAIT (new Cursor(Cursor.WAIT_CURSOR)),
		MOVE (new Cursor(Cursor.MOVE_CURSOR)),
		HAND (new Cursor(Cursor.HAND_CURSOR)),
		CROSS (new Cursor(Cursor.CROSSHAIR_CURSOR)),
		DEFAULT (new Cursor(Cursor.DEFAULT_CURSOR));
		
		private Cursor cursor;
		private ECursor(Cursor cursor) {
			this.cursor = cursor;
		}
		public Cursor getCursor() {return this.cursor;}
	}
	/**
	 * Resize Anchor order
	 * 
	 * <pre>
	 * 6 - 5 - 4
	 * 3 - - - 7
	 * 0 - 1 - 2
	 * </pre>
	 * */
	public enum EAnchors{
		SW (ECursor.SW, 0, true),
		SS (ECursor.SS, 1, true),
		SE (ECursor.SE, 2, true),
		WW (ECursor.WW, 3, true),
		NE (ECursor.NE, 4, true),
		NN (ECursor.NN, 5, true),
		NW (ECursor.NW, 6, true),
		EE (ECursor.EE, 7, true),
		RR (ECursor.DEFAULT, 8, true),
		
		SH (ECursor.DEFAULT, 10, false),
		MM (ECursor.MOVE, 9, false);
		
		private int anchorNum;
		private boolean drawable;
		private ECursor cursor;
		
		private EAnchors(ECursor cursor, int anchorNum, boolean drawable){
			this.cursor = cursor;
			this.anchorNum = anchorNum;
			this.drawable = drawable;
		}
		public ECursor getCursor() {return this.cursor;}
		public int getAnchorNum() {return this.anchorNum;}
		public boolean isDrawable() {return this.drawable;}
	}

	public enum EDrawableAnchors{
		DRAWABLE_ANCHORS;

		private int numOfDrawableAnchor=0;
		
		private EDrawableAnchors() {
			for(EAnchors anchor : EAnchors.values()) {
				if(anchor.isDrawable())numOfDrawableAnchor++;
			}
		}
		public int getDrawableAnchor() {return this.numOfDrawableAnchor;}
	}
	
	public static class EFonts{
		
		public EFonts(){
			File file = new File("C:/Windows//Fonts");
			if(file.exists()) {
				System.out.println(file.isDirectory());
			}
		}
	}
}
