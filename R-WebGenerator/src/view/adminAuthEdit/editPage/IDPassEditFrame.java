package view.adminAuthEdit.editPage;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Serializable;

import javax.swing.JFrame;

public class IDPassEditFrame extends JFrame implements Serializable,WindowListener{

	public static final int framewidth = 400;
	public static final int frameheight = 250;
	
	public IDPassEditPanel panel;
	
	private static IDPassEditFrame obj = new IDPassEditFrame();
	
	
	private IDPassEditFrame() {
		setBounds(400,300,IDPassEditFrame.framewidth,IDPassEditFrame.frameheight);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		panel = IDPassEditPanel.getInstance();
		getContentPane().add(panel,BorderLayout.CENTER);
		
		addWindowListener(this);
	}
	
	public void openAdmin(){
		IDPassEditPanel.getInstance().openAdmin();
	}
	
	public void openCreater(){
		IDPassEditPanel.getInstance().openCreater();
	}
	
	
	public static void repaintAndValidate(){
		IDPassEditFrame.obj.repaint();
		IDPassEditFrame.obj.validate();
		IDPassEditFrame.obj.panel.repaint();
		IDPassEditFrame.obj.panel.validate();
	}
	
	
	
	public void windowActivated(WindowEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void windowClosing(WindowEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		IDPassEditPanel.getInstance().frameClosed();
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void windowIconified(WindowEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void windowOpened(WindowEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public static IDPassEditFrame getInstance(){
		return IDPassEditFrame.obj;
	}
	
	
}
