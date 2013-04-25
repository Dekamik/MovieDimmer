package sleepmode;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class PeekWindow extends JFrame{
	
	public PeekWindow(Rectangle rect) {
		super();
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setBounds(rect);
		setVisible(true);
	}
	
	public void setOpacity(int procent) {
		Double opacity = procent * 2.55;
		setBackground(new Color(0, 0, 0, opacity.intValue()));
	}
}
