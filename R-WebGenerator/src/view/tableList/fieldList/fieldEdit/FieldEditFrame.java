package view.tableList.fieldList.fieldEdit;

import debug.Debug;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import javax.swing.*;

import mainFrame.MainFrame;

/*
 * Singleton
 */
public class FieldEditFrame extends JFrame implements Serializable,WindowListener {
	public static final int frameWidth = 400;
	public static final int frameHeight = 500;
	
	public FieldEditPanel panel;

	/*
	 * インスタンス参照変数は最後！
	 */
	private static FieldEditFrame obj = new FieldEditFrame();

	
	
	
	
	private FieldEditFrame() {
		setBounds(400, 300, FieldEditFrame.frameWidth, FieldEditFrame.frameHeight);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	    // フィールド編集パネル
	    panel = FieldEditPanel.getInstance();
	    int PADD = 5;
	    getContentPane().add(panel, BorderLayout.CENTER);

		addWindowListener(this);
	}

	
	public void openAddMode() {
		FieldEditPanel.getInstance().openAddMode();
	}

	public static void repaintAndValidate() {
		FieldEditFrame.obj.repaint();
		FieldEditFrame.obj.validate();
		FieldEditFrame.obj.panel.repaint();
		FieldEditFrame.obj.panel.validate();
	}

	
	
	
	
	public void windowOpened(WindowEvent e) {
	}
	public void windowClosing(WindowEvent e) {
		FieldEditPanel.getInstance().frameClosed();
	}
	public void windowClosed(WindowEvent e) {
	}
	public void windowActivated(WindowEvent e) {
	}
	public void windowDeactivated(WindowEvent e) {
	}
	public void windowIconified(WindowEvent e) {
	}
	public void windowDeiconified(WindowEvent e) {
	}
	
	
	public static FieldEditFrame getInstance() {
		return FieldEditFrame.obj;
	}



	public static void updateInstance(FieldEditFrame newObject) {
		FieldEditFrame.obj = newObject;
	}
}
