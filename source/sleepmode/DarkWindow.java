package sleepmode;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DarkWindow extends JFrame {
	
	JPanel black;
	
	public DarkWindow(int x, int y, int width, int height) {
		super();
		setBackground(Color.BLACK);
		addWindowListener(new WindowClose());
		addMouseListener(new ClickClose());
		
		setAlwaysOnTop(true);
		setBounds(x, y, width, height);
		setUndecorated(true);
		setVisible(true);
	}
	
	public DarkWindow(Rectangle rect) {
		super();
		setBackground(Color.BLACK);
		addWindowListener(new WindowClose());
		addMouseListener(new ClickClose());
		
		setAlwaysOnTop(true);
		setBounds(rect);
		setUndecorated(true);
		setVisible(true);
	}
	
	public void setOpacity(int procent) {
		Double opacity = procent * 2.55;
		setBackground(new Color(0, 0, 0, opacity.intValue()));
	}
	
	class WindowClose extends WindowAdapter {
		public void windowClosing(WindowEvent w) {
			SleepMode.closeAllDark();
		}
	}
	
	class ClickClose extends MouseAdapter {
		public void mousePressed(MouseEvent m) {
			SleepMode.closeAllDark();
		}
	}
}
