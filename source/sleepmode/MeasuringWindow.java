package sleepmode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MeasuringWindow extends JFrame {
	
	JLabel xVal, yVal, wVal, hVal;
	JButton confirm, abort;
	JPanel p;
	
	Font f = new Font("Arial", Font.PLAIN, 20);
	
	public MeasuringWindow() {
		super("Measuring Window");
		setSize(420,320);
		setMinimumSize(new Dimension(420,320));
		setLocationRelativeTo(null);
		
		addComponentListener(new Window());
		
		p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.BLACK);
		
		p.add(xVal = new JLabel(), BorderLayout.WEST);
		p.add(yVal = new JLabel(), BorderLayout.NORTH);
		p.add(wVal = new JLabel(), BorderLayout.EAST);
		p.add(hVal = new JLabel(), BorderLayout.SOUTH);
		xVal.setFont(f);
		yVal.setFont(f);
		wVal.setFont(f);
		hVal.setFont(f);
		xVal.setForeground(Color.WHITE);
		yVal.setForeground(Color.WHITE);
		wVal.setForeground(Color.WHITE);
		hVal.setForeground(Color.WHITE);
		
		JPanel middle = new JPanel();
		middle.setBackground(Color.BLACK);
		middle.add(confirm = new JButton("Confirm Selection"), BorderLayout.CENTER);
		middle.add(abort = new JButton("Abort Selection"), BorderLayout.CENTER);
		p.add(middle);
		
		add(p);
		update();
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Updates the x,y,width,height lables
	 */
	public void update() {
		xVal.setText("X-Position: "+getBounds().x);
		yVal.setText("Y-Position: "+getBounds().y);
		wVal.setText("Width: "+getBounds().width);
		hVal.setText("Height: "+getBounds().height);
	}
	
	/**
	 * Calls the update method if the window is moved or resized
	 */
	class Window extends ComponentAdapter {
		public void componentMoved(ComponentEvent c) {
			update();
		}
		public void componentResized(ComponentEvent c) {
			validate();
			update();
		}
	}
}
