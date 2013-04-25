package sleepmode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	JCheckBox slideCheck, optionLocation;
	JComboBox<String> setBox;
	JButton activateShroudBtn, addBtn, delBtn;
	MeasuringWindow m;
	
	Border border;
	Font titleFont = new Font("Arial", Font.PLAIN, 14);
	
	public MainWindow() {
		super("MovieDimmers 1.1");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel sliders = new JPanel();
		sliders.setLayout(new GridLayout(0,2));
		sliders.setBorder(BorderFactory.createTitledBorder(border, "Settings", 0, 0, titleFont, Color.BLUE));
		
		slideCheck = new JCheckBox("Dimmer options sliders");
		slideCheck.addChangeListener(new SliderChecked());
		slideCheck.setSelected(true);
		sliders.add(slideCheck);
		
		optionLocation = new JCheckBox("Sliders over porthole");
		optionLocation.setEnabled(false);
		sliders.add(optionLocation);
		
		add(sliders);
		
		JPanel activate = new JPanel();
		activate.setLayout(new GridLayout(0,1));
		activate.setBorder(BorderFactory.createTitledBorder(border, "Activate!", 0, 0, titleFont, Color.BLUE));
		activateShroudBtn = new JButton("Activate Dimmers");
		activateShroudBtn.addActionListener(new ActivateShroud());
		activate.add(activateShroudBtn);
		
		add(activate);
		
		JPanel settings = new JPanel();
		settings.setLayout(new GridLayout(0,1));
		settings.setBorder(BorderFactory.createTitledBorder(border, "Frames", 0, 0, titleFont, Color.BLUE));
		setBox = new JComboBox<String>(SleepMode.settingNames);
		settings.add(setBox);
		
		addBtn = new JButton("Add Frame");
		addBtn.addActionListener(new AddShroud());
		settings.add(addBtn);
		
		delBtn = new JButton("Delete Frame");
		delBtn.addActionListener(new DeleteShroud());
		settings.add(delBtn);
		
		add(settings);
		
		addWindowListener(new WindowClose());
		
		setSize(400, 300);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Enables the optionLocation checkbox if the slideCheck checkbox is checked and vice versa 
	 */
	class SliderChecked implements ChangeListener {
		public void stateChanged(ChangeEvent arg0) {
			if (optionLocation != null) {
				if(slideCheck.isSelected()) {
					optionLocation.setEnabled(true);
				}
				else {
					optionLocation.setEnabled(false);
				}
			}
		}
	}
	
	/**
	 * Activates the shroud and sets this windows visibility to false
	 */
	class ActivateShroud implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			boolean confirm = SleepMode.shroud(setBox.getSelectedIndex(), slideCheck.isSelected(), optionLocation.isSelected());
			if (confirm) {
				setVisible(false);
			}
		}
	}
	
	/**
	 * Sets the program into measuring mode
	 */
	class AddShroud implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			setVisible(false);
			m = new MeasuringWindow();
			m.confirm.addActionListener(new CreateShroud());
			m.abort.addActionListener(new Abort());
		}
	}
	
	/**
	 * Creates a new frame setting based on the measurements made in the
	 * measuring mode and passes the rectangles into the main program
	 */
	class CreateShroud implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			String name = "";
			while(name.equals("")) {
				name = JOptionPane.showInputDialog("Name:");
				if(name.equals("")) {
					JOptionPane.showMessageDialog(null, "Please input a name");
				}
			}
			Rectangle port = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
			Rectangle up = new Rectangle(0, 0, screen.width, m.getY());
			Rectangle down = new Rectangle(0, (m.getY() + m.getHeight()), screen.width, (screen.height - (m.getY() + m.getHeight())));
			Rectangle left = new Rectangle(0, m.getY(), m.getX(), m.getHeight());
			Rectangle right = new Rectangle((m.getX() + m.getWidth()), m.getY(), (screen.width - (m.getX() + m.getWidth())), m.getHeight());
			SleepMode.addShroud(name, port, up, down, left, right);
			m.dispose();
		}
	}
	
	/**
	 * Aborts measuring mode
	 */
	class Abort implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			m.dispose();
			setVisible(true);
		}
	}
	
	/**
	 * Activates the main programs delete frame setting method
	 */
	class DeleteShroud implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			setAlwaysOnTop(false);
			SleepMode.deleteShroud(setBox.getSelectedIndex());
			setAlwaysOnTop(true);
		}
	}
	
	/**
	 * Handles the window closing, makes sure that the main program saves
	 * the current settings and exits
	 */
	class WindowClose extends WindowAdapter {
		public void windowClosing(WindowEvent w) {
			SleepMode.save();
			System.exit(0);
		}
	}
}
