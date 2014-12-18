package utility;

import java.awt.Component;

import javax.swing.SpringLayout;

public class Slpc {
	public static void put(SerializableSpringLayout springLayout, String str1, Component comp1, String str2, Component comp2, int pad) {
		String edge1 = null;
		if(str1.equals("N")) {
			edge1 = SpringLayout.NORTH;
		}
		else if(str1.equals("S")) {
			edge1 = SpringLayout.SOUTH;
		}
		else if(str1.equals("W")) {
			edge1 = SpringLayout.WEST;
		}
		else if(str1.equals("E")) {
			edge1 = SpringLayout.EAST;
		}

		String edge2 = null;
		if(str2.equals("N")) {
			edge2 = SpringLayout.NORTH;
		}
		else if(str2.equals("S")) {
			edge2 = SpringLayout.SOUTH;
		}
		else if(str2.equals("W")) {
			edge2 = SpringLayout.WEST;
		}
		else if(str2.equals("E")) {
			edge2 = SpringLayout.EAST;
		}

		springLayout.putConstraint(edge1, comp1, pad, edge2, comp2);
	}
}
