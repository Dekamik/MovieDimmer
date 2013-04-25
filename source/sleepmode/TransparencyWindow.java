package sleepmode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class TransparencyWindow extends JFrame{
	
	private int width;
	private int height;
	
	JLabel fTLabel, mTLabel, sLabel;
	JPanel bar, p1, p2;
	public JButton minimize;
	public JCheckBox frameOn, midOn;
	public JSlider frameTrans, midTrans;
	
	Font font = new Font("Monospaced", Font.PLAIN, 14);
	
	DragNDrop dnd = new DragNDrop();
	
	public TransparencyWindow() {
		super();
		setUndecorated(true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setBackground(null);
		
		bar = new JPanel();
		bar.setLayout(new BorderLayout());
		bar.setBorder(BorderFactory.createLineBorder(new Color(100, 0, 100), 2, false));
		bar.add(sLabel = new JLabel("Sliders"), BorderLayout.CENTER);
		bar.add(minimize = new JButton(" - "), BorderLayout.EAST);
		
		bar.setBackground(Color.BLACK);
		sLabel.setForeground(new Color(100, 0, 100));
		minimize.setBackground(Color.BLACK);
		minimize.setForeground(new Color(100, 0, 100));
		minimize.setFont(new Font("Monospaced", Font.PLAIN, 14));
		minimize.setBorder(BorderFactory.createLineBorder(new Color(100, 0, 100), 2, false));
		
		minimize.addActionListener(new Minimize());
		
		add(bar);
		
		p1 = new JPanel();
		frameTrans = new JSlider();
		frameTrans.setMinimum(1);
		frameTrans.setMaximum(100);
		frameTrans.setValue(100);
		
		p1.add(frameOn = new JCheckBox());
		p1.add(fTLabel = new JLabel("Frame   "));
		p1.add(frameTrans);
		
		add(p1);
		
		p1.setBackground(Color.BLACK);
		fTLabel.setFont(font);
		fTLabel.setForeground(Color.BLUE);
		frameTrans.setBackground(Color.BLACK);
		frameOn.setBackground(Color.BLACK);
		frameOn.setSelected(true);
		
		p2 = new JPanel();
		midTrans = new JSlider();
		midTrans.setMinimum(1);
		midTrans.setMaximum(100);
		midTrans.setValue(1);
		
		p2.add(midOn = new JCheckBox());
		p2.add(mTLabel = new JLabel("Porthole"));
		p2.add(midTrans);
		
		add(p2);
		
		p2.setBackground(Color.BLACK);
		mTLabel.setFont(font);
		mTLabel.setForeground(Color.BLUE);
		midTrans.setBackground(Color.BLACK);
		midOn.setBackground(Color.BLACK);
		
		bar.addMouseListener(dnd);
		bar.addMouseMotionListener(dnd);
		
		pack();
		setAlwaysOnTop(true);
		setVisible(true);
		
		width = getWidth();
		height = getHeight();
	}
	
	/**
	 * Handles the drag and drop functions of the window
	 */
	class DragNDrop extends MouseAdapter {
		
		int xStart;
		int yStart;
		
		public void mousePressed(MouseEvent m) {
			xStart = m.getX();
			yStart = m.getY();
			requestFocusInWindow();
		}
		
		public void mouseDragged(MouseEvent m) {
			int x = getX() - xStart + m.getX();
			int y = getY() - yStart + m.getY();
			setLocation(x,y);
		}
	}
	
	/**
	 * Sets the contents visibile to false, or restores the window
	 * by setting visible to true.
	 */
	class Minimize implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			if (p1.isVisible() && p2.isVisible()) {
				p1.setVisible(false);
				p2.setVisible(false);
				setSize(width, bar.getHeight());
			}
			else {
				p1.setVisible(true);
				p2.setVisible(true);
				setSize(width, height);
			}
		}
	}
}
