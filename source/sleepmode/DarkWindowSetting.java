package sleepmode;

import java.awt.Rectangle;
import java.io.Serializable;

@SuppressWarnings("serial")
public class DarkWindowSetting implements Comparable<DarkWindowSetting>, Serializable {
	
	private String name;
	private Object[] shroud = new Object[5];
	
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public DarkWindowSetting(String name, Rectangle portHole, Rectangle upBox, Rectangle downBox, Rectangle leftBox, Rectangle rightBox) {
		this.name = name;
		shroud[0] = upBox;
		shroud[1] = downBox;
		shroud[2] = leftBox;
		shroud[3] = rightBox;
		shroud[4] = portHole;
	}
	
	public String getName() {
		return name;
	}
	
	public Rectangle getPortHole() {
		return (Rectangle)shroud[4];
	}
	
	public Object[] getShroud() {
		return shroud;
	}
	
	public int compareTo(DarkWindowSetting d) {
		return name.compareTo(d.getName());
	}
}
