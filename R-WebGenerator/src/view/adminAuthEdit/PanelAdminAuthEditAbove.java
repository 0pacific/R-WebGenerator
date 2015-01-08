package view.adminAuthEdit;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JPanel;

import property.GeneratorProperty;
import utility.SerializableSpringLayout;
import mainFrame.MainFrame;

public class PanelAdminAuthEditAbove extends JPanel implements ActionListener,Serializable{

	public final int panelWidth = MainFrame.frameWidth - 20;
	public final int panelHeight = 80;
	
	public SerializableSpringLayout springLayout;
	
	
	
	private static PanelAdminAuthEditAbove obj = new PanelAdminAuthEditAbove();
	
	
	private PanelAdminAuthEditAbove(){
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		setBackground(Color.WHITE);
	}
	
	
	public void relocate(String part){
		boolean japanese = GeneratorProperty.japanese();
		
		removeAll();
		
		if(part.equals("table")){
			
		}
		else if(part.equals("role")){
			
		}
		else if(part.equals("page")){
			
		}
		else{
			
		}
	}
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	
	public static PanelAdminAuthEditAbove getInstance(){
		return PanelAdminAuthEditAbove.obj;
	}

}
