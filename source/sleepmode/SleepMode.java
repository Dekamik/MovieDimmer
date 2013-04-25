package sleepmode;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * SleepMode is a program for creating black boxes around a specified rectangle.
 * It's perfect for creating dimmers for any kind of video site that you are visiting.
 * 
 * @author Dennis von Bargen
 *
 */
public class SleepMode {
	
	static SleepMode s;
	static MainWindow mainWindow;
	static DarkWindow du, dd, dl, dr;	//du = up, dd = down, dl = left, dr = right
	static PeekWindow p;
	static MeasuringWindow measure;
	static TransparencyWindow transOptions;
	
	public static ArrayList<DarkWindowSetting> settings = new ArrayList<DarkWindowSetting>();
	public static String[] settingNames;
	
	private static int selectedSetting;
	
	/**
	 * Initializes the main program
	 */
	public SleepMode() {
		load();
		refresh();
	}
	
	/**
	 * Removes the active dimmers and restores the program to its original state
	 */
	public static void closeAllDark() {
		if (du != null) du.dispose();
		if (dd != null) dd.dispose();
		if (dl != null) dl.dispose();
		if (dr != null) dr.dispose();
		if (p != null) p.dispose();
		if(transOptions != null) transOptions.dispose();
		mainWindow.setVisible(true);
	}
	
	/**
	 * Activates the dimmers
	 * 
	 * @param index			Indicates which frame configuration will be selected
	 * @param sliderOn		Indicates whether the sliders will appear or not
	 * @param optionsOnTop	Indicates where the sliders will appear
	 * 
	 * @return				True if successful, false if there are no frame settings stored
	 */
	public static boolean shroud(int index, boolean sliderOn, boolean optionsOnTop) {
		Object[] shroud;
		selectedSetting = index;
		if (settings.size() > 0) {
			shroud = settings.get(index).getShroud();
		}
		else {
			mainWindow.setAlwaysOnTop(false);
			JOptionPane.showMessageDialog(null, "No frames are stored.");
			mainWindow.setAlwaysOnTop(true);
			return false;
		}
		if (shroud[DarkWindowSetting.UP] != null) {
			du = new DarkWindow((Rectangle)shroud[DarkWindowSetting.UP]);
		}
		if (shroud[DarkWindowSetting.DOWN] != null) {
			dd = new DarkWindow((Rectangle)shroud[DarkWindowSetting.DOWN]);
		}
		if (shroud[DarkWindowSetting.LEFT] != null) {
			dl = new DarkWindow((Rectangle)shroud[DarkWindowSetting.LEFT]);
		}
		if (shroud[DarkWindowSetting.RIGHT] != null) {
			dr = new DarkWindow((Rectangle)shroud[DarkWindowSetting.RIGHT]);
		}
		if(sliderOn) {
			transOptions = new TransparencyWindow();
			transOptions.frameTrans.addChangeListener(s.new FrameTrans());
			transOptions.midTrans.addChangeListener(s.new MidTrans());
			transOptions.frameOn.addActionListener(s.new FrameCheck());
			transOptions.midOn.addActionListener(s.new WindowCheck());
			transOptions.midTrans.setEnabled(false);
			transOptions.mTLabel.setForeground(new Color(0, 0, 155));
			if(optionsOnTop) 
				transOptions.setBounds(dl.getWidth(), (du.getHeight() - transOptions.getHeight()), transOptions.getWidth(), transOptions.getHeight());
			if (du != null) du.setOpacity(75);
			if (dd != null) dd.setOpacity(75);
			if (dl != null) dl.setOpacity(75);
			if (dr != null) dr.setOpacity(75);
			transOptions.frameTrans.setValue(75);
		}
		else {
			du.setOpacity(100);
			dd.setOpacity(100);
			dl.setOpacity(100);
			dr.setOpacity(100);
		}
		return true;
	}
	
	/**
	 * Adds a new shroud to the settings ArrayList and sorts the ArrayList accordingly
	 * 
	 * @param name	The name of the setting
	 * @param port	A rectangle representing the position and size of the porthole
	 * @param up	A rectangle representing the position and size of the upper DarkWindow
	 * @param down	A rectangle representing the position and size of the lower DarkWindow
	 * @param left	A rectangle representing the position and size of the left DarkWindow
	 * @param right	A rectangle representing the position and size of the right DarkWindow
	 */
	public static void addShroud(String name, Rectangle port, Rectangle up, Rectangle down, Rectangle left, Rectangle right) {
		settings.add(new DarkWindowSetting(name, port, up, down, left, right));
		Collections.sort(settings);
		refresh();
	}
	
	/**
	 * Deletes the frame setting that is specified by the index
	 * 
	 * 
	 * @param index		The index of the frame setting to be deleted
	 */
	public static void deleteShroud(int index) {
		int cmd = JOptionPane.showConfirmDialog(null, 
				"Are you sure you want to delete "+settings.get(index).getName()+"?", 
				"Deletion confirmation", 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE);
		if(cmd == JOptionPane.YES_OPTION) {
			settings.remove(index);
			refresh();
		}
	}
	
	/**
	 * Refreshes the window in order for the list to update
	 */
	public static void refresh() {
		settingNames = null;
		settingNames = new String[settings.size()];
		for(int i = 0; i < settings.size(); i++) {
			settingNames[i] = settings.get(i).getName();
		}
		if (mainWindow != null) {
			mainWindow.dispose();
			mainWindow = null;
			mainWindow = new MainWindow();
		}
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Loads the settings file.
	 * If it doesn't find the file, a new file is created with the default
	 * frame settings added. After that the method is called recursively
	 * in order to complete the loading
	 */
	public static void load() {
		try{
            FileInputStream fileStream = new FileInputStream("settings.shroud");
            ObjectInputStream in = new ObjectInputStream(fileStream);
            settings = (ArrayList<DarkWindowSetting>)in.readObject();
        }
        catch (FileNotFoundException e) {
            addShroud("Black Screen", 
            		null,
            		new Rectangle(
            				0,
            				0,
            				Toolkit.getDefaultToolkit().getScreenSize().width, 
            				Toolkit.getDefaultToolkit().getScreenSize().height), 
            		null, 
            		null, 
            		null);
            addShroud("Example Frame",
            		new Rectangle(100, 100, 640, 480),
            		new Rectangle(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, 100),
            		new Rectangle(0, 580, Toolkit.getDefaultToolkit().getScreenSize().width, (Toolkit.getDefaultToolkit().getScreenSize().height - 580)),
            		new Rectangle(0, 100, 100, 480),
            		new Rectangle(740, 100, (Toolkit.getDefaultToolkit().getScreenSize().width - 740), 480));
            save();
            load();
        }
        catch (IOException e) {
            System.out.println("Load IOException");
        }
        catch (ClassNotFoundException e) {
        	System.out.println("Load ClassNotFoundException");
        }
	}
	
	/**
	 * Saves the current settings ArrayList into the settings.shroud file 
	 */
	public static void save() {
		try {
            FileOutputStream fileStream = new FileOutputStream("settings.shroud");
            ObjectOutputStream out = new ObjectOutputStream(fileStream);
            out.writeObject(settings);
        }
        catch (IOException e) {
        	System.out.println("Save IOException");
        }
	}
	
	/**
	 * Checks if the user wishes to have the porthole on.
	 * 
	 * If so, the porthole is created, if not and if the porthole
	 * isn't null, the porthole is disposed
	 */
	public void changeWindowState() {
		boolean windowOn = transOptions.midOn.isSelected();
		if (windowOn) {
			if (settings.get(selectedSetting).getPortHole() != null) {
				p = new PeekWindow(settings.get(selectedSetting).getPortHole());
				p.setOpacity(transOptions.midTrans.getValue());
			}
			transOptions.midTrans.setEnabled(true);
			transOptions.mTLabel.setForeground(Color.BLUE);
		}
		else {
			if(p != null) {
				p.setVisible(false);
				p.dispose();
			}
			transOptions.midTrans.setEnabled(false);
			transOptions.mTLabel.setForeground(new Color(0, 0, 155));
		}
	}
	
	/**
	 * Handles communication between the Frame slider and the actual windows
	 */
	class FrameTrans implements ChangeListener {
		public void stateChanged(ChangeEvent c) {
			if (du != null) du.setOpacity(transOptions.frameTrans.getValue());
			if (dd != null) dd.setOpacity(transOptions.frameTrans.getValue());
			if (dl != null) dl.setOpacity(transOptions.frameTrans.getValue());
			if (dr != null) dr.setOpacity(transOptions.frameTrans.getValue());
		}
	}
	
	/**
	 * Handles communication between the Porthole slider and the actual porthole
	 */
	class MidTrans implements ChangeListener {
		public void stateChanged(ChangeEvent c) {
			if (p != null) p.setOpacity(transOptions.midTrans.getValue());
		}
	}
	
	/**
	 * Checks if the Frame checkbox is selected, if it isn't, the frames will be disposed
	 */
	class FrameCheck implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			if (!transOptions.frameOn.isSelected()) {
				closeAllDark();
			}
		}
	}
	
	/**
	 * Calls changeWindowState if the checkbox is either checked or unchecked
	 */
	class WindowCheck implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			changeWindowState();
		}
	}
	
	public static void main(String[]args) {
		s = new SleepMode();
		mainWindow = new MainWindow();
	}
}
