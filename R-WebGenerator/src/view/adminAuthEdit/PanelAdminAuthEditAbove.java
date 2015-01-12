package view.adminAuthEdit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

import debug.Debug;
import property.GeneratorProperty;
import role.Role;
import role.RoleManager;
import table.DataTable;
import table.TableManager;
import utility.SerializableSpringLayout;
import utility.Slpc;
import mainFrame.MainFrame;

public class PanelAdminAuthEditAbove extends JPanel implements ActionListener,Serializable{

	public final int panelWidth = MainFrame.frameWidth - 20;
	public final int panelHeight = 2000;
	public static final int SCLROLL_HEIGHT = 400;
	
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
		
		if(part.equals("role")){
			
			JTextPane textRole = new JTextPane();
			String msg1 = japanese? "�Ǘ��҂̃��[�������i�u�S�āv�ɂ̓A�J�E���g�e�[�u���ւ̃A�N�Z�X���܂ށj":"Admin's Role Accessibility(If you select \"Full Accessibility\",admin can access Account Table.)";
			textRole.setText(msg1);
			textRole.setEditable(false);
			Slpc.put(springLayout,"N",textRole,"N",this,20);
			Slpc.put(springLayout, "W", textRole, "W", this, 20);
			add(textRole);
			
			
			//�ǂ̂��炢�̌�����t�^����̂��̃��W�I�{�^��
			ButtonGroup groupRole = new ButtonGroup();
			JRadioButton radioRole1 = new JRadioButton(japanese? "�����Ȃ�":"No Accessibility");
			JRadioButton radioRole2 = new JRadioButton(japanese? "�ꕔ�̂�":"Limited Accessibility");
			JRadioButton radioRole3 = new JRadioButton(japanese? "�S��":"Full Accessibility");

			groupRole.add(radioRole1);
			groupRole.add(radioRole2);
			groupRole.add(radioRole3);

			Slpc.put(springLayout, "N", radioRole1, "N", textRole, 20);
			Slpc.put(springLayout, "W", radioRole1, "W", this, 20);

			Slpc.put(springLayout, "N", radioRole2, "N", radioRole1, 0);
			Slpc.put(springLayout, "W", radioRole2, "W", radioRole1, 20);

			Slpc.put(springLayout, "N", radioRole3, "N", radioRole2, 0);
			Slpc.put(springLayout, "W", radioRole3, "W", radioRole2, 20);
			
			radioRole1.addActionListener(this);
			radioRole1.setActionCommand("���[���F�����Ȃ�");
			
			radioRole2.addActionListener(this);
			radioRole2.setActionCommand("���[���F�ꕔ�̂�");
			
			radioRole3.addActionListener(this);
			radioRole3.setActionCommand("���[���F�S��");

			add(radioRole1);
			add(radioRole2);
			add(radioRole3);
			
			//�u�ꕔ�̂݁v���I�����ꂽ�ꍇ�A���[���̃��X�g��\��
			
			JPanel panelRole = new JPanel();
			panelRole.setPreferredSize(new Dimension(panelWidth,1400));
			panelRole.setBackground(Color.WHITE);
			
			JTextPane noticeRole = new JTextPane();
			String msg2 = japanese? "\"��\"��I������ƑJ�ڌ����A���쌠���̕ҏW���\�ɂȂ�܂��B":"If you select \"YES\",admin can edit Transition auth and accessibility.";
			noticeRole.setText(msg2);
			noticeRole.setEnabled(false);
			panelRole.add(noticeRole);
			
			RoleManager roleManager = RoleManager.getInstance();
			int roleNum = roleManager.getRoleNum();
			for(int i = 0;i<roleNum;i++){
				int height = 20 + 30*i;
				
				Role role = roleManager.getRole(i);
				String roleName = role.getRoleName();
				JLabel roleNameLabel = new JLabel(roleName);
				roleNameLabel.setPreferredSize(new Dimension(70,20));
				Slpc.put(springLayout, "N", roleNameLabel, "N", noticeRole,height);
				Slpc.put(springLayout, "W", roleNameLabel, "W", this, 40);
				add(roleNameLabel);
				
				JRadioButton roleYes = new JRadioButton(japanese? "��":"YES");
				JRadioButton roleNo = new JRadioButton(japanese? "�s��":"NO");

				roleYes.addActionListener(this);
				roleYes.setActionCommand("���[���F"+roleName+"�F��");
				roleNo.addActionListener(this);
				roleNo.setActionCommand("���[���F"+roleName+"�F�s��");
				
				ButtonGroup roleYorN = new ButtonGroup();
				roleYorN.add(roleYes);
				roleYorN.add(roleNo);
				
				//�y�v�ǉ��z���݂̓��͏󋵂ɉ����ă��W�I�{�^�����I������Ă���

				Slpc.put(springLayout,"N",roleYes,"N",roleNameLabel,0);
				Slpc.put(springLayout,"W",roleYes,"W",this,80);
				
				Slpc.put(springLayout,"N",roleNo,"N",roleNameLabel,0);
				Slpc.put(springLayout,"W",roleNo,"W",this,100);
				
				panelRole.add(roleYes);
				panelRole.add(roleNo);
				
				
				
			}
			
		}
		else if(part.equals("table")){

			
			JTextPane textTable = new JTextPane();
			String msg1 = japanese? "�Ǘ��҂̃e�[�u������":"Admin's Table Accessibility";
			textTable.setText(msg1);
			textTable.setEditable(false);
			Slpc.put(springLayout,"N",textTable,"N",this,20);
			Slpc.put(springLayout, "W", textTable, "W", this, 20);
			add(textTable);
			
			
			//�ǂ̂��炢�̌�����t�^����̂��̃��W�I�{�^��
			ButtonGroup groupTable = new ButtonGroup();
			JRadioButton radioTable1 = new JRadioButton(japanese? "�����Ȃ�":"No Accessibility");
			JRadioButton radioTable2 = new JRadioButton(japanese? "�ꕔ�̂�":"Limited Accessibility");
			JRadioButton radioTable3 = new JRadioButton(japanese? "�S��":"Full Accessibility");

			groupTable.add(radioTable1);
			groupTable.add(radioTable2);
			groupTable.add(radioTable3);

			Slpc.put(springLayout, "N", radioTable1, "N", textTable, 20);
			Slpc.put(springLayout, "W", radioTable1, "W", this, 20);

			Slpc.put(springLayout, "N", radioTable2, "N", radioTable1, 0);
			Slpc.put(springLayout, "W", radioTable2, "W", radioTable1, 20);

			Slpc.put(springLayout, "N", radioTable3, "N", radioTable2, 0);
			Slpc.put(springLayout, "W", radioTable3, "W", radioTable2, 20);
			
			radioTable1.addActionListener(this);
			radioTable1.setActionCommand("�e�[�u���F�����Ȃ�");
			
			radioTable2.addActionListener(this);
			radioTable2.setActionCommand("�e�[�u���F�ꕔ�̂�");
			
			radioTable3.addActionListener(this);
			radioTable3.setActionCommand("�e�[�u���F�S��");

			add(radioTable1);
			add(radioTable2);
			add(radioTable3);
			
			//�u�ꕔ�̂݁v���I�����ꂽ�ꍇ�A���[���̃��X�g��\��
			
			JPanel panelTable = new JPanel();
			panelTable.setPreferredSize(new Dimension(panelWidth,1400));
			panelTable.setBackground(Color.WHITE);
			
			JTextPane noticeTable = new JTextPane();
			String msg2 = japanese? "\"��\"��I������Ƃ��̃e�[�u���̃t�B�[���h�̕ҏW���\�ɂȂ�܂��B":"If you select \"YES\",admin can edit field of this table.";
			noticeTable.setText(msg2);
			noticeTable.setEnabled(false);
			panelTable.add(noticeTable);
			
			TableManager tableManager = TableManager.getInstance();
			int tableNum = tableManager.getDataTableNum();
			for(int i = 0;i<tableNum;i++){
				int height = 20 + 30*i;
				
				DataTable table = tableManager.getDataTable(i);
				String tableName = table.getTableName();
				JLabel tableNameLabel = new JLabel(tableName);
				tableNameLabel.setPreferredSize(new Dimension(70,20));
				Slpc.put(springLayout, "N", tableNameLabel, "N", noticeTable,height);
				Slpc.put(springLayout, "W", tableNameLabel, "W", this, 40);
				add(tableNameLabel);
				
				JRadioButton tableYes = new JRadioButton(japanese? "��":"YES");
				JRadioButton tableNo = new JRadioButton(japanese? "�s��":"NO");

				tableYes.addActionListener(this);
				tableYes.setActionCommand("���[���F"+tableName+"�F��");
				tableNo.addActionListener(this);
				tableNo.setActionCommand("���[���F"+tableName+"�F�s��");
				
				ButtonGroup tableYorN = new ButtonGroup();
				tableYorN.add(tableYes);
				tableYorN.add(tableNo);
				
				//�y�v�ǉ��z���݂̓��͏󋵂ɉ����ă��W�I�{�^�����I������Ă���

				Slpc.put(springLayout,"N",tableYes,"N",tableNameLabel,0);
				Slpc.put(springLayout,"W",tableYes,"W",this,80);
				
				Slpc.put(springLayout,"N",tableNo,"N",tableNameLabel,0);
				Slpc.put(springLayout,"W",tableNo,"W",this,100);
				
				panelTable.add(tableYes);
				panelTable.add(tableNo);
				
			}
		}
		else if(part.equals("page")){
			JTextPane textPage = new JTextPane();
			String msg1 = japanese? "�Ǘ��҂̃y�[�W����":"Admin's Page Accessibility";
			textPage.setText(msg1);
			textPage.setEditable(false);
			Slpc.put(springLayout,"N",textPage,"N",this,20);
			Slpc.put(springLayout, "W", textPage, "W", this, 20);
			add(textPage);
			
			
			//�ǂ̂��炢�̌�����t�^����̂��̃��W�I�{�^��
			ButtonGroup groupPage = new ButtonGroup();
			JRadioButton radioPage1 = new JRadioButton(japanese? "�����Ȃ�":"No Accessibility");
			JRadioButton radioPage3 = new JRadioButton(japanese? "�S��":"Full Accessibility");

			groupPage.add(radioPage1);
			groupPage.add(radioPage3);

			Slpc.put(springLayout, "N", radioPage1, "N", textPage, 20);
			Slpc.put(springLayout, "W", radioPage1, "W", this, 20);

			Slpc.put(springLayout, "N", radioPage3, "N", radioPage1, 0);
			Slpc.put(springLayout, "W", radioPage3, "W", radioPage1, 20);
			
			radioPage1.addActionListener(this);
			radioPage1.setActionCommand("�y�[�W�F�����Ȃ�");
			
			radioPage3.addActionListener(this);
			radioPage3.setActionCommand("�y�[�W�F�S��");

			add(radioPage1);
			add(radioPage3);
			
		}
		else{
			//�G���[	
			Debug.error("�Ǘ��Ҍ����̕ҏW��ʂɕ\���ł��Ȃ����߂ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		}
	}
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	
	public static PanelAdminAuthEditAbove getInstance(){
		return PanelAdminAuthEditAbove.obj;
	}
	
	public static void updateInstance(PanelAdminAuthEditAbove newObj){
		PanelAdminAuthEditAbove.obj = newObj;
	}

}
