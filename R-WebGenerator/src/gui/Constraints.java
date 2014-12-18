package gui;

import java.awt.GridBagConstraints;

public class Constraints {
	public static void set(GridBagConstraints constraints, int gridx, int gridy, int gridwidth, int gridheight) {
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
	}
}
